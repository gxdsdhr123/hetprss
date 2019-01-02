package com.neusoft.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neusoft.framework.common.config.Global;

import io.codis.jodis.RoundRobinJedisPool;
import io.codis.jodis.RoundRobinJedisPool.Builder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

public class JodisUtils {
	private static Logger logger = LoggerFactory.getLogger(JodisUtils.class);
	private static RoundRobinJedisPool pool = null;
	static {
		Builder builder=RoundRobinJedisPool.create();
		if(!"".equals(StringUtils.nvl(Global.getConfig("codis.password")))){
			builder=builder.password(Global.getConfig("codis.password"));
		}
		JedisPoolConfig poolConfig=new JedisPoolConfig();
		poolConfig.setMaxTotal(500);//最大连接数
		poolConfig.setMaxIdle(50);
		poolConfig.setMinIdle(8);//设置最小空闲数
		poolConfig.setMaxWaitMillis(10000);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		//Idle时进行连接扫描
		poolConfig.setTestWhileIdle(true);
		//表示idle object evitor两次扫描之间要sleep的毫秒数
		poolConfig.setTimeBetweenEvictionRunsMillis(30000);
		//表示idle object evitor每次扫描的最多的对象数
		poolConfig.setNumTestsPerEvictionRun(10);
		//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		poolConfig.setMinEvictableIdleTimeMillis(60000);
		pool = builder.curatorClient(Global.getConfig("codis.zk.hosts"), 30000)
				.zkProxyDir(Global.getConfig("codis.proxy.zk.path"))
				.poolConfig(poolConfig)
				.build();
	}
	/**
	 * 获取资源
	 * 
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource(String s) throws JedisException {
		Jedis jedis = null;
		try {
			jedis = getResource();
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			throw e;
		}
		return jedis;
	}
	/**
	 * 获取资源
	 * 
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource(int database) throws JedisException {
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis != null&&jedis.isConnected()) {
				jedis.select(database);
			}
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			throw e;
		}
		return jedis;
	}

	/**
	 * 获取byte[]类型Key
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtils.getBytes((String) object);
		} else {
			return ObjectUtils.serialize(object);
		}
	}

	/**
	 * Object转换byte[]类型
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
	}
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource(0);
			result = jedis.rpush(key, value);
			logger.debug("listAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listAdd {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	/**
	 * 释放资源
	 * @param jedis
	 * @param jedis
	 */
    public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
    /**
     * 获取Jedis，如果获取连接超时责尝试获取3次
     * @return
     * @throws JedisException
     */
	public static Jedis getResource() throws JedisException {
		int timeoutCount = 0;// 获取连接超时次数
		while (true) {
			try {
				if (null != pool) {
					return JedisUtils.getResource();
//					return pool.getResource();
				} else {
					break;
				}
			} catch (Exception e) {
				if (e instanceof JedisConnectionException) {
					timeoutCount++;
					logger.warn("********getJedis timeoutCount={}*******", timeoutCount);
					// 3次失败责放弃
					if (timeoutCount > 3) {
						break;
					}
				} else {
					logger.warn("getResource.", e);
					throw new JedisException(e.toString());
				}
			}
		}
		return null;
	}
}
