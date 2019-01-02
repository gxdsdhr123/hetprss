/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月23日 下午4:28:46
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.rpt.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.neusoft.framework.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "/rpt")
public class RptController {
	private static String rptPath="/rpt/showWindow.do";
	/**
	 *Discription:报表跳转.
	 *@param windowId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年12月23日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = "show")
    public ModelAndView show(@RequestParam("id")String windowId) {
		String loginName=UserUtils.getUser().getLoginName().toUpperCase();
		String path="/.."+rptPath+"?id="+windowId+"&j_username="+loginName;
		return new ModelAndView("redirect:"+path); 
    }
}
