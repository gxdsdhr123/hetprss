package com.neusoft.prss.api.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.prss.grid.service.GridColumnService;

@Controller
@RequestMapping(value="/api/rest/FlightDynamicAutoExport")
public class FlightDynamicExportAPIController {
	@Autowired
    private GridColumnService gridService;
	
	/**
	 *Discription:获取用户定义的列.
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年7月19日 SunJ [变更描述]
	 */
	@RequestMapping("getSheet1ExportColumn")
	@ResponseBody
	public String getSheet1ExportColumn(HttpServletRequest request){
		String userId = request.getParameter("userId");//用户ID
		String columns = gridService.getColumns(userId, "1");//默认表头
		return columns;
	}
	
	/**
	 * 
	 *Discription:获取需要导出的数据.
	 *@param request
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2018年7月19日 SunJ [变更描述]
	 */
	@RequestMapping("getSheet1ExportData")
	@ResponseBody
	public String getSheet1ExportData(HttpServletRequest request){
		String userId = request.getParameter("userId");//用户ID
		JSONArray jsonArr = gridService.getExportData(userId);
		return JSON.toJSONString(jsonArr, SerializerFeature.WriteMapNullValue);
	}
}
