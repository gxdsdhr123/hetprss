/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.neusoft.framework.common.security.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.JodisUtils;
import com.neusoft.framework.common.utils.ObjectUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.Servlets;

import redis.clients.jedis.Jedis;

/**
 * 自定义授权会话管理类
 * 
 * @author ThinkGem
 * @version 2014-7-20
 */
public class JedisSessionDAO extends AbstractSessionDAO implements SessionDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String sessionKeyPrefix = "shiro_session_";
	
	private static final int SESSION_CACHE_DATABASE = 1;
	
	public String getSessionKeyPrefix() {
		return sessionKeyPrefix;
	}

	public void setSessionKeyPrefix(String sessionKeyPrefix) {
		this.sessionKeyPrefix = sessionKeyPrefix;
	}
	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			return;
		}
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不更新SESSION
			if (Servlets.isStaticFile(uri)) {
				return;
			}
			// 如果是视图文件，则不更新SESSION
			if (StringUtils.startsWith(uri, Global.getConfig("web.view.prefix"))
					&& StringUtils.endsWith(uri, Global.getConfig("web.view.suffix"))) {
				return;
			}
			// 手动控制不更新SESSION
			if (Global.NO.equals(request.getParameter("updateSession"))) {
				return;
			}
		}
		Jedis jedis = null;
		try {
			jedis = JodisUtils.getResource(SESSION_CACHE_DATABASE);
			// 获取登录者编号
			PrincipalCollection pc = (PrincipalCollection) session
					.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			String principalId = pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY;
			Serializable sessionId = session.getId();// sessionid
			long sessionTimeOut = session.getTimeout();// session超时时长
			long lastAccessTime = session.getLastAccessTime().getTime();// 最后登录时间
			jedis.hset(sessionKeyPrefix, sessionId.toString(),principalId + "|" + sessionTimeOut + "|" + lastAccessTime);
			jedis.set(JodisUtils.getBytesKey(sessionKeyPrefix + sessionId), ObjectUtils.serialize(session));
			// 设置超期时间
			int timeoutSeconds = (int) (session.getTimeout() / 1000);
			if (timeoutSeconds > 0) {
				jedis.expire((sessionKeyPrefix + session.getId()), timeoutSeconds);
			}
			logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			logger.error("update {} {}", session.getId(), request != null ? request.getRequestURI() : "", e);
			throw new UnknownSessionException(e.toString());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			return;
		}
		Jedis jedis = null;
		try {
			jedis = JodisUtils.getResource(SESSION_CACHE_DATABASE);
			jedis.hdel(JodisUtils.getBytesKey(sessionKeyPrefix), JodisUtils.getBytesKey(session.getId().toString()));
			jedis.del(JodisUtils.getBytesKey(sessionKeyPrefix + session.getId()));
			logger.debug("delete {} ", session.getId());
		} catch (Exception e) {
			logger.error("delete {} ", session.getId(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		return getActiveSessions(true);
	}

	/**
	 * 获取活动会话
	 * 
	 * @param includeLeave
	 *            是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return getActiveSessions(includeLeave, null, null);
	}

	/**
	 * 获取活动会话
	 * 
	 * @param includeLeave
	 *            是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal
	 *            根据登录者对象获取活动会话
	 * @param filterSession
	 *            不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		Set<Session> sessions = Sets.newHashSet();
		Jedis jedis = null;
		try {
			jedis = JodisUtils.getResource(SESSION_CACHE_DATABASE);
			Map<String, String> map = jedis.hgetAll(sessionKeyPrefix);
			for (Map.Entry<String, String> e : map.entrySet()) {
				String sessionId = e.getKey();
				String sessionValue = e.getValue();
				if (StringUtils.isNotBlank(sessionId) && StringUtils.isNotBlank(sessionValue)) {
					String[] ss = StringUtils.split(sessionValue, "|");
					if (ss != null && ss.length == 3) {
						try {
							String principalId = ss[0];
							Long timeOut = Long.valueOf(ss[1]);
							Date lastAccessTime = new Date(Long.valueOf(ss[2]));
							SimpleSession session = null;
							boolean isActiveSession = false;
							if(principal != null){
								if(String.valueOf(principal).equals(principalId)&&jedis.exists(sessionKeyPrefix + sessionId)){
									session =(SimpleSession)JodisUtils.toObject(jedis.get(JodisUtils.getBytesKey(sessionKeyPrefix+ e.getKey())));
									session.validate();// 验证SESSION
								} else {
									continue;
								}
							} else {
								session = new SimpleSession();
								session.setId(sessionId);
								session.setAttribute("principalId", principalId);
								session.setTimeout(timeOut);
								session.setLastAccessTime(lastAccessTime);
								session.validate();
							}
							// 不包括离线并符合最后访问时间小于等于3分钟条件。
							if (includeLeave || DateUtils.pastMinutes(lastAccessTime) <= 3) {
								isActiveSession = true;
							}
							// 过滤掉的SESSION
							if (filterSession != null && filterSession.getId().equals(sessionId)) {
								isActiveSession = false;
							}
							if (session != null && isActiveSession) {
								sessions.add(session);
							}
						}
						catch (Exception e2) {
							jedis.hdel(sessionKeyPrefix, sessionId);
						}
					}
					// 存储的SESSION不符合规则
					else {
						jedis.hdel(sessionKeyPrefix, sessionId);
					}
				}
				// 存储的SESSION无Value
				else if (StringUtils.isNotBlank(sessionId)) {
					jedis.hdel(sessionKeyPrefix, sessionId);
				}
			}
			logger.info("getActiveSessions size: {} ", sessions.size());
		} catch (Exception e) {
			logger.error("getActiveSessions", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不创建SESSION
			if (Servlets.isStaticFile(uri)) {
				return null;
			}
		}
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.update(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {

		Session s = null;
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不获取SESSION
			if (Servlets.isStaticFile(uri)) {
				return null;
			}
			s = (Session) request.getAttribute("session_" + sessionId);
		}
		if (s != null) {
			return s;
		}

		Session session = null;
		Jedis jedis = null;
		try {
			jedis = JodisUtils.getResource(SESSION_CACHE_DATABASE);
			// if (jedis.exists(sessionKeyPrefix + sessionId)){
			session = (Session) JodisUtils.toObject(jedis.get(JodisUtils.getBytesKey(sessionKeyPrefix + sessionId)));
			// }
			logger.debug("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			logger.error("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		if (request != null && session != null) {
			request.setAttribute("session_" + sessionId, session);
		}
		return session;
	}

	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		try {
			return super.readSession(sessionId);
		} catch (UnknownSessionException e) {
			return null;
		}
	}
}
