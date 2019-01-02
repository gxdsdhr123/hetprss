/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年7月23日 上午9:35:09
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.ATMZBOWExportService;
/**
 * 
 *Discription:航务保障部管制/运行指挥中心导出
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticalanalysis/ATMZBOW")
public class ATMZBOWExportController extends BaseController {
	@Autowired
	private ATMZBOWExportService atmzbowExportService;
	
	@Autowired
	private CacheService cacheService;
	
    @RequestMapping(value = "list/{param}" )
    public String getList(Model model,@PathVariable("param") String param) {
//    	默认查询2天前的数据（因为是历史表）
    	String defaultDate=DateUtils.formatDate(DateUtils.getCalculateDate(-2),"yyyyMMdd");
    	model.addAttribute("defaultDate", defaultDate);
    	model.addAttribute("type", param);
        return "prss/statisticalanalysis/atmZbowDataList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public  Map<String, Object> getDataList(int pageSize,int pageNumber,String date,String type) {
        return getDataList(pageSize, pageNumber, date, type, false);
    }
    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,String date,String type) {
    	date=StringEscapeUtils.unescapeHtml4(date);
    	String columnName ="";
    	String[] titleArr=new String[]{};
    	if("ATMZBOW".equals(type)){
    		columnName = "[{\"field\":\"FLIGHT_NUMBER\"},{\"field\":\"STD\"},{\"field\":\"DepAP\"},"
    	    	       + "{\"field\":\"ArrAP\"},{\"field\":\"ASRT\"},{\"field\":\"ASAT\"},"
    	    		   + "{\"field\":\"FRIT\"},{\"field\":\"ATOT\"},{\"field\":\"ALDT\"},"
    	    		   + "{\"field\":\"ApDepDelayReason\"},{\"field\":\"InitDepDelayReason\"},"
    	    		   + "{\"field\":\"FlightDelayReason\"}]";
    		titleArr = new String[] {"CallSign","ScheduleDate","DepAP", "ArrAP", "ASRT", 
    	    		 "ASAT","FRIT","ATOT","ALDT","ApDepDelayReason","InitDepDelayReason",
    	    		 "FlightDelayReason"};
    	}
    	if("ZBOW".equals(type)){
    		columnName = "[{\"field\":\"FLIGHT_NUMBER\"},{\"field\":\"STD\"},{\"field\":\"DepAP\"},"
 	    	       + "{\"field\":\"ArrAP\"},{\"field\":\"AOBT\"},{\"field\":\"AIBT\"},"
 	    		   + "{\"field\":\"ApDepDelayReason\"},{\"field\":\"InitDepDelayReason\"},"
 	    		   + "{\"field\":\"FlightDelayReason\"},{\"field\":\"IntialFlight\"}]";
    		titleArr =new String[] {"CallSign","ScheduleDate","DepAP", "ArrAP", "AOBT", 
    	    		 "AIBT","ApDepDelayReason","InitDepDelayReason","FlightDelayReason","IntialFlight"};
    	}
		try {
			String fileName = type + date+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String,String>> dataList=(List<Map<String, String>>) getDataList(0, 0, date, type, true).get("rows");
			excel.setDataList(titleArr, JSON.parseArray(columnName),dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error(type+"数据导出"+ e.getMessage());
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
    
    private Map<String, Object> getDataList(int pageSize,int pageNumber,String date,String type,boolean exportFlag){
    	Map<String, Object> param=new HashMap<String, Object>();
    	if(exportFlag){
    		param.put("begin", 0);
    	    param.put("end", 100000);
    	}else{
    		int begin=(pageNumber-1)*pageSize;
            int end=pageSize + begin;
            param.put("begin", begin);
    	    param.put("end", end);
    	}
	    param.put("date", date);
	    param.put("type", type);
	    Map<String,Object> result=atmzbowExportService.getDataList(param);
	    List<Map<String,Object>> dataList=(List<Map<String, Object>>) result.get("rows");
	    //从缓存中获取三字码 进行转换
	    Map<String,String> aptMap=cacheService.getMap("dim_airport3_to4_map");
	    for(Map<String,Object> obj:dataList){
	    	obj.put("DepAP", aptMap.get(obj.get("DEPART_APT3CODE")));
	    	obj.put("ArrAP", aptMap.get(obj.get("ARRIVAL_APT3CODE")));
	    }
	    return result;
    	
    }
}
