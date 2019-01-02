/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.neusoft.framework.modules.sys.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.security.shiro.session.SessionDAO;
import com.neusoft.framework.common.servlet.ValidateCodeServlet;
import com.neusoft.framework.common.utils.CacheUtils;
import com.neusoft.framework.common.utils.CookieUtils;
import com.neusoft.framework.common.utils.IdGen;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.User;
import com.neusoft.framework.modules.sys.security.FormAuthenticationFilter;
import com.neusoft.framework.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.neusoft.framework.modules.sys.security.UsernamePasswordToken;
import com.neusoft.framework.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
    
    @Autowired
    private SessionDAO sessionDAO;
    
    /**
     * 管理登录
     */
    @RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = UserUtils.getPrincipal();
//      // 默认页签模式
//      String tabmode = CookieUtils.getCookie(request, "tabmode");
//      if (tabmode == null){
//          CookieUtils.setCookie(response, "tabmode", "1");
//      }
        
        if (logger.isDebugEnabled()){
            logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }
        
        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
            CookieUtils.setCookie(response, "LOGINED", "false");
        }
        
        // 如果已经登录，则跳转到管理首页
        if(principal != null && !principal.isMobileLogin()){
            return "redirect:" + adminPath;
        }
        String serverIp = Global.getConfig("talk.ip");
        int port = Integer.parseInt(Global.getConfig("talk.port"));
        model.addAttribute("serverIp", serverIp);
        model.addAttribute("port", port);
        return "modules/sys/sysLogin";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = UserUtils.getPrincipal();
        
        // 如果已经登录，则跳转到管理首页
        if(principal != null){
            return "redirect:" + adminPath;
        }

        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
        
        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
        
        if (logger.isDebugEnabled()){
            logger.debug("login fail, active session size: {}, message: {}, exception: {}", 
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }
        
        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)){
            model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
        }
        
        // 验证失败清空验证码
        request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());
        
        // 如果是手机登录，则返回JSON字符串
        if (mobile){
            return renderString(response, model);
        }
        
        return "modules/sys/sysLogin";
    }

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "${adminPath}")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();

        // 登录成功后，验证码计算器清零
        isValidateCodeLogin(principal.getLoginName(), false, true);
        
        if (logger.isDebugEnabled()){
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }
        
        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
            String logined = CookieUtils.getCookie(request, "LOGINED");
            if (StringUtils.isBlank(logined) || "false".equals(logined)){
                CookieUtils.setCookie(response, "LOGINED", "true");
            }else if (StringUtils.equals(logined, "true")){
                UserUtils.getSubject().logout();
                return "redirect:" + adminPath + "/login";
            }
        }
        
        // 如果是手机登录，则返回JSON字符串
        if (principal.isMobileLogin()){
            if (request.getParameter("login") != null){
                return renderString(response, principal);
            }
            if (request.getParameter("index") != null){
                return "modules/sys/sysIndex";
            }
            return "redirect:" + adminPath + "/login";
        }
        //return "modules/sys/sysIndex";
        return "modules/sys/home";
    }
    
    /**
     * 获取主题方案
     */
    @RequestMapping(value = "/theme/{theme}")
    public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isNotBlank(theme)){
            CookieUtils.setCookie(response, "theme", theme);
        }else{
            theme = CookieUtils.getCookie(request, "theme");
        }
        return "redirect:"+request.getParameter("url");
    }
    
    /**
     * 是否是验证码登录
     * @param useruame 用户名
     * @param isFail 计数加1
     * @param clean 计数清零
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
        Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
        if (loginFailMap==null){
            loginFailMap = Maps.newHashMap();
            CacheUtils.put("loginFailMap", loginFailMap);
        }
        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum==null){
            loginFailNum = 0;
        }
        if (isFail){
            loginFailNum++;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean){
            loginFailMap.remove(useruame);
        }
        return loginFailNum >= 3;
    }
    /**
     * 校验用户是否在线
     * @return
     */
    @RequestMapping(value = "/login/isActive")
    @ResponseBody
    public String isActive(){
    	JSONObject activeInfo = new JSONObject();
    	int onlineCount = 0;
    	try {
    		Subject subject = UserUtils.getSubject();
        	boolean isActive = (subject==null?false:subject.isAuthenticated());
        	activeInfo.put("isActive", isActive);
    		if(sessionDAO!=null&&sessionDAO.getActiveSessions(false)!=null){
    			onlineCount = sessionDAO.getActiveSessions(false).size();
        	}
		} catch (Exception e) {
			logger.error("获取当前在线用户数异常："+e);
			activeInfo.put("isActive", true);
		}
    	activeInfo.put("onlineCount", onlineCount);
    	return activeInfo.toString();
    }
    
    /**
     * 重新登录表单
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "${adminPath}/switchLoginForm")
    public String switchLoginForm(HttpServletRequest request, HttpServletResponse response, Model model){
    	return "modules/sys/switchLoginForm";
    }
    /**
     * 重新登录
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "${adminPath}/switchLogin")
    @ResponseBody
    public String switchLogin(HttpServletRequest request, HttpServletResponse response, Model model){
    	String result = "success";
    	try{
    		String username = request.getParameter("username");
    		if(StringUtils.isEmpty(username)){
    			return "请输入登录账号！";
    		}
    		String password = request.getParameter("password");
    		if(StringUtils.isEmpty(password)){
    			return "请输入登录密码！";
    		}
    		User oldUser = UserUtils.getUser();
    		if(username.equalsIgnoreCase(oldUser.getLoginName())){
    			return "切换用户不能与当前登录用户相同！";
    		}
    		User user = UserUtils.getByLoginName(username);
    		if(user==null){
    			return "用户不存在！";
    		}
    		if(user.getRoleIdList()==null||!user.getRoleIdList().equals(oldUser.getRoleIdList())){
    			return "只能相同角色用户之间进行切换！";
    		}
    		String host = StringUtils.getRemoteAddr((HttpServletRequest)request);
        	UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(),false,host,FormAuthenticationFilter.DEFAULT_CAPTCHA_PARAM,false); 
        	UserUtils.getSubject().login(token);
        	UserUtils.clearCache();
        	logger.info("用户:"+oldUser.getName()+"["+oldUser.getLoginName()+"]"+"被切换为:"+user.getName()+"["+user.getLoginName()+"]");
    	} catch(Exception e){
    		String className = e.getClass().getName();
    		if (IncorrectCredentialsException.class.getName().equals(className)
    				|| UnknownAccountException.class.getName().equals(className)){
    			result = "用户或密码错误, 请重试.";
    		}
    		else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")){
    			result = StringUtils.replace(e.getMessage(), "msg:", "");
    		}
    		else{
    			result = "系统出现点问题，请稍后再试！";
    			logger.error(e.toString());
    		}
    	}
    	return result;
    }
}
