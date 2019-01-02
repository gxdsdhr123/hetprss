/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月6日 下午4:26:29
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.neusoft.prss.arrange.service.GanttService;

@Controller
@RequestMapping(value = "${adminPath}/arrange/gantt")
public class GanttController {
	@Autowired
	private GanttService ganttService;
	/**
	 * 
	 *Discription:返回排班甘特页面.
	 *@param model
	 *@return
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年12月6日 SunJ [变更描述]
	 */
	@RequestMapping(value = "list")
    public String list(Model model) {
        return "prss/arrange/arrangeGantt";
    }
	
	@RequestMapping(value = "ganttData")
	@ResponseBody
	public List<Map<String, String>> ganttData(String terminal,String week) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("terminal", terminal);
		params.put("week", week);
		List<Map<String, String>> arr = ganttService.ganttData(params);
		return arr;
	}
}
