/**
 *<p>application name:       lizard</p>
 *<p>application describing: this class handles the request of the client</p>
 *<p>copyrignt:              Copyright(c) 2014 本代码版权归东软集团(大连)有限公司所有，禁止任何未授权的传播和使用</p>
 *<p>company:                neusoft</p>
 *<p>time:                   2016-6-16 上午11:18:40
 *
 *@author                    <a href="mailto: baochl@neusoft.com">baochl</a>
 *@version                   [v1.0]
 */
package com.neusoft.framework.common.web;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.modules.sys.entity.User;
import com.neusoft.framework.modules.sys.utils.UserUtils;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * @author         <a href="mailto: baochl@neusoft.com">包春亮</a>
 * @version        [版本号] 
 */
@Controller
@RequestMapping(value = "${adminPath}/common/portal")
public class PortalController extends BaseController {
	@RequestMapping(value = "diyRpt")
	public String diyRpt(@RequestParam(required=false) String menuType,HttpServletResponse response) {
		if(menuType!=null){
			try {
				User user = UserUtils.getUser();
				String target = Global.getConfig("diyrpt.context")+"/diyServicePortal.do";
				target+="?adminType="+user.getUserType();
				target+="&employeeName="+user.getName();
				target+="&areaId=all";
				target+="&employeeId="+user.getId();
				target+="&w="+user.getLoginName();
				target+="&p="+user.getPassword();
				target+="&menuType="+menuType;
				
				JSONObject permit = new JSONObject();
				permit.put("data_permit", "ALL");
				target+="&permit="+URLEncoder.encode(permit.toString(),"utf-8");
				response.sendRedirect(target);
			} catch (IOException e) {
				logger.error("diyRpt跳转失败：", e);
			}
		}
		return null;
	}
}
