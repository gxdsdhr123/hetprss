/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2018年5月4日 下午5:12:57
 *@author:guoxudong
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.flightdynamic.service.FltDynMonitorService;

/**
 * 航班监控
 * @author Guoxd
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/flightdynamic/fltdynmonitor")
public class FltDynMonitorController extends BaseController {

	@Autowired
	private FltDynMonitorService fltDynMonitorService;
	
	@RequestMapping(value = "list")
    public String list(Model model) {
        model.addAttribute("type", "main");
        return "prss/flightdynamic/flightDynMonitorMain";
    }
	
	/**
	 * 获取航班数据
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "init")
	@ResponseBody
	public String init(HttpServletRequest request) {

		//获取参数
		/*Enumeration<String> enu = request.getParameterNames();
        Map<String,String> params = new HashMap<String,String>();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            String paraVlaue = request.getParameter(paraName);
            params.put(paraName, paraVlaue);
        }*/
		
		JSONArray list = new JSONArray();
		try {
			list = fltDynMonitorService.getFltData();//航班数据
		} catch (Exception e) {
			logger.error("/fltdynmonitor/list获取数据失败->"+e.getMessage());
		}
		
		String data = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
		return data;
	}
}
