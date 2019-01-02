/**
 *application name:hetprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年8月23日 上午09:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.JWStatsFltTransportService;


/**
 * 
 *Discription:机务航班运输统计
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/jwys/statistics")
public class JWStatsFltTransportController extends BaseController {
	
	@Autowired
	private JWStatsFltTransportService jwStatsFltTransportService;
	@Autowired 
	private CacheService cacheService;
	private Logger logger=Logger.getLogger(JWStatsFltTransportController.class);
	
	
    @RequestMapping(value = "list" )
    public String list(Model model) {
    	String defaultStart=DateUtils.getDate("yyyyMMdd");
    	JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname");
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
    	model.addAttribute("airlines", airlinesCodeSource);
        return "prss/statisticalanalysis/jwStatsTransportList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String dateStart,String dateEnd,String alnCode,String searchText) {
    	
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	alnCode=StringEscapeUtils.unescapeHtml4(alnCode);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		alnCode = java.net.URLDecoder.decode(alnCode,"utf-8");
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
		} catch (Exception e) {
			logger.error("/jwys/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("alnCode", alnCode);
	    param.put("searchText", searchText.toUpperCase());
        return jwStatsFltTransportService.getDataList(param);
    }

    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,
    		String dateStart,String dateEnd,String alnCode,String searchText) {
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	alnCode = StringEscapeUtils.unescapeHtml4(alnCode);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		alnCode = java.net.URLDecoder.decode(alnCode,"utf-8");
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
    		
		} catch (Exception e) {
			logger.error("/jwys/statistics/exportExcel失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
		param.put("alnCode", alnCode);
		param.put("searchText", searchText.toUpperCase());
		param.put("begin", 0);
	    param.put("end", 10000000);
		String[] titleArr = new String[] {};
		String columnName = "[{\"field\":\"DESCRIPTION_CN\"},{\"field\":\"IN_OUT_FLAG\"},"
				+ "{\"field\":\"FLIGHT_DATE\"},{\"field\":\"ATA_D\"},{\"field\":\"DEPART_APT3CODE\"},"
				+ "{\"field\":\"ARRIVAL_APT3CODE\"},{\"field\":\"FLIGHT_NUMBER2\"},{\"field\":\"AIRCRAFT_NUMBER\"},"
				+ "{\"field\":\"ACTTYPE_CODE\"},{\"field\":\"PROPERTY_CN\"},{\"field\":\"FW_TYPE\"}]";
    		titleArr = new String[] {"航空公司","进出港","航班日期","起降时间","起飞机场","目的机场", 
    				"航班号","飞机号", "机型","飞行任务","是否放行"};
    	
		try {
			String fileName =dateStart+"-"+dateEnd +"机务航班运输统计"+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String,String>> dataList=(List<Map<String, String>>) jwStatsFltTransportService.getDataList(param).get("rows");
			excel.setDataList(titleArr, JSON.parseArray(columnName),dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("机务航班运输统计导出"+ e.getMessage());
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
