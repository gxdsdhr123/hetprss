package com.neusoft.prss.statisticalanalysis.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.curator.retry.RetryUntilElapsed;
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
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.TimeSlotWorkloadService;
import com.vaadin.terminal.gwt.client.Console;

/**
 * 
 *Discription:员工某时段内工作量
 *@param 
 *@return:
 *@author:pei.g
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/timeslotworkload/statistics")
public class TimeSlotWorkloadController extends BaseController{
	
	@Autowired
	private TimeSlotWorkloadService timeslotWorkloadService;
	private Logger logger=Logger.getLogger(TimeSlotWorkloadController.class);
	
	//员工某时段内工作量页面	
    @RequestMapping(value = "list" )
    public String list(Model model) throws ParseException{
    	String defaultStart=DateUtils.getDate("yyyyMMdd");
    	Date date = DateUtils.getCalculateDate(defaultStart, -1);
    	defaultStart = new SimpleDateFormat("yyyyMMdd").format(date);
		//获取当前保障类型
    	List<JobKind> list=UserUtils.getCurrentJobKind();
    	String jobKind="";    	
    	 if (list!=null&&list.size()>0) {                
    		 jobKind=list.get(0).getKindCode();
    		 }else{
    			 jobKind="";
    		 }
    	Map<String, Object> param=new HashMap<String, Object>();
    	param.put("jobKind", jobKind);
    	JSONArray jobTypeList = timeslotWorkloadService.loadJobType(param);
    	    	    		 
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
        model.addAttribute("jobTypeList", jobTypeList);
        return "prss/statisticalanalysis/timeSlotWorkloadList";
    }
    
  //表格数据
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String, Object> getDataList(int pageSize,int pageNumber,
    		String dateStart,String dateEnd,String name,String jobType) {
    	    		
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	name=StringEscapeUtils.unescapeHtml4(name);
    	jobType=StringEscapeUtils.unescapeHtml4(jobType);

    	List<JobKind> list=UserUtils.getCurrentJobKind();
    	String jobKind="";    	
    	 if (list!=null&&list.size()>0) {                
    		 jobKind=list.get(0).getKindCode();
    		 }else{
    			 jobKind="";
    		 }

    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8");
    		name = java.net.URLDecoder.decode(name,"utf-8");
    		jobType = java.net.URLDecoder.decode(jobType,"utf-8");
    		jobKind = java.net.URLDecoder.decode(jobKind,"utf-8");
		} catch (Exception e) {
			logger.error("/workload/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("name", name);
	    param.put("jobType", jobType);
	    param.put("jobKind", jobKind);
	    return timeslotWorkloadService.getDataList(param);
    }
    
    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,
    		String dateStart,String dateEnd,String name,String jobType) {
    	
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	name=StringEscapeUtils.unescapeHtml4(name);
    	jobType=StringEscapeUtils.unescapeHtml4(jobType);

    	List<JobKind> list=UserUtils.getCurrentJobKind();
    	String jobKind="";    	
    	 if (list!=null&&list.size()>0) {                
    		 jobKind=list.get(0).getKindCode();
    		 }else{
    			 jobKind="";
    		 }

    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8");
    		name = java.net.URLDecoder.decode(name,"utf-8");
    		jobType = java.net.URLDecoder.decode(jobType,"utf-8");
    		jobKind = java.net.URLDecoder.decode(jobKind,"utf-8");
    		dateStart=DateUtils.formatDate(DateUtils.parseDate(dateStart), "yyyyMMdd");
    		dateEnd=DateUtils.formatDate(DateUtils.parseDate(dateEnd), "yyyyMMdd");
    		
		} catch (Exception e) {
			logger.error("/timeslotworkload/statistics/exportExcel失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
		param.put("name", name);
		param.put("jobType", jobType);
		param.put("jobKind", jobKind);
		param.put("begin", 0);
	    param.put("end", 10000000);
		String columnName = "[{\"field\":\"TIMESLOT\"},"
				+ "{\"field\":\"NAME\"},{\"field\":\"TYPE_NAME\"},"
				+ "{\"field\":\"KIND_COUNT\"},{\"field\":\"KIND_TIME\"},{\"field\":\"TYPE_COUNT\"},"
				+ "{\"field\":\"TYPE_TIME\"}]";
		String[] titleArr = new String[] {"日期","姓名","作业类型","保障次数", "保障时长", "保障总次数","保障总时长"};
    	
		try {
			String fileName =dateStart+"-"+dateEnd +"员工时段内工作量统计"+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String ,String>> dataList = (List<Map<String, String>>) timeslotWorkloadService.getDataList(param).get("rows");
			excel.setTimeSlotWorkloadData(titleArr, JSON.parseArray(columnName),dataList,param);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("员工某时段内工作量统计数据导出"+ e.getMessage());
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
