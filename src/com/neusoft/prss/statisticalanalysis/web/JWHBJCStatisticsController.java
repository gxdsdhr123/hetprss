package com.neusoft.prss.statisticalanalysis.web;

import com.alibaba.fastjson.JSON;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.JWHBJCStatisticsService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 
 *Discription:机务航班架次统计
 *@param 
 *@return:
 *@author:pei.g
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/jwhbjc/statistics")
public class JWHBJCStatisticsController extends BaseController{
	
	@Autowired
	private JWHBJCStatisticsService jwhbcjStatisticsService;
	private Logger logger=Logger.getLogger(JWHBJCStatisticsController.class);
	
	//机务航班架次统计页面	
    @RequestMapping(value = "list" )
    public String list(Model model) {
    	String defaultStart=DateUtils.getDate("yyyyMMdd");
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
        return "prss/statisticalanalysis/jwhbjcStatisticsList";
    }
   //表格数据
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String, Object> getDataList(int pageSize,int pageNumber,
    		String dateStart,String dateEnd,String searchText) {
    	
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8"); 
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
//    		dateStart=DateUtils.formatDate(DateUtils.parseDate(dateStart), "yyyyMMdd");
//    		dateEnd=DateUtils.formatDate(DateUtils.parseDate(dateEnd), "yyyyMMdd");
		} catch (Exception e) {
			logger.error("/jwhbjc/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("searchText", searchText.toUpperCase());
	    return jwhbcjStatisticsService.getDataList(param);

    }

    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,
    		String dateStart,String dateEnd,String searchText) {
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8");
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
//    		dateStart=DateUtils.formatDate(DateUtils.parseDate(dateStart), "yyyyMMdd");
//    		dateEnd=DateUtils.formatDate(DateUtils.parseDate(dateEnd), "yyyyMMdd");
    		
		} catch (Exception e) {
			logger.error("/jwhbjc/statistics/exportExcel失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
		param.put("searchText", searchText.toUpperCase());
		param.put("begin", 0);
	    param.put("end", 10000000);
	    String[] titleArr = new String[] {};
		String columnName = "[{\"field\":\"AIRLINE_SHORTNAME\"},"
				+ "{\"field\":\"ACTTYPE_CODE\"},{\"field\":\"FX_NUM\"},"
				+ "{\"field\":\"HQHH_NUM\"},{\"field\":\"QW_NUM\"},{\"field\":\"TOTAL_NUM\"},"
				+ "{\"field\":\"REMARK\"}]";
		titleArr = new String[] {"航班公司","机型","放行","航前/航后","勤务", "架次合计", "备注"};
    	
		try {
			String fileName =dateStart+"-"+dateEnd +"机务航班架次统计"+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String ,String>> dataList = (List<Map<String, String>>) jwhbcjStatisticsService.getDataList(param).get("rows");
			excel.setDataList(titleArr, JSON.parseArray(columnName),dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("机务航班架次数据导出"+ e.getMessage());
		}
	}

    private void setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		try {
			response.reset();
	        response.setContentType("application/octet-stream; charset=utf-8");
	        String agent = (String) request.getHeader("USER-AGENT");
	        String downloadFileName = "";
	        if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
	            downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
	        } else {
	            downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
		}catch(Exception e){
			logger.error("set header error:"+e.toString());
		}
	}
}
