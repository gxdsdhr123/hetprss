/**
 *application name:hetprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年8月18日 上午09:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.KCQJStatisticsService;


/**
 * 
 *Discription:客舱清洁工作量统计页面
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/kcqj/statistics")
public class KCQJStatisticsController extends BaseController {
	
	@Autowired
	private KCQJStatisticsService kcqjStatisticsService;
	private Logger logger=Logger.getLogger(KCQJStatisticsController.class);
	
	
    @RequestMapping(value = "list" )
    public String list(Model model) throws ParseException{
    	String defaultStart=DateUtils.getDate("yyyyMMdd");
    	Date date = DateUtils.getCalculateDate(defaultStart, -1);
    	defaultStart = new SimpleDateFormat("yyyyMMdd").format(date);
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
        return "prss/statisticalanalysis/kcqjStatisticsList";
    }
    @RequestMapping(value = "list/monthly" )
    public String monthlyList(Model model) {
    	String year=DateUtils.getYear();
    	String month=DateUtils.getMonth();
    	model.addAttribute("year", year);
    	model.addAttribute("month", month);
        return "prss/statisticalanalysis/kcqjMonthlyStatsList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
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
			logger.error("/kcqj/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("searchText", searchText);
        return kcqjStatisticsService.getDataList(param);
    }
   
    
    @RequestMapping(value = "monthlyList" )
    @ResponseBody
    public Map<String,Object> getMonthlyList(int pageSize,int pageNumber,
    		String year,String month) {
    	year = StringEscapeUtils.unescapeHtml4(year);
    	month = StringEscapeUtils.unescapeHtml4(month);
    	String dateStart="";
    	String dateEnd="";
    	List<String> dateStrList=new ArrayList<String>();
    	try {
    		int yearInt=Integer.parseInt(year);
    		int monthInt=Integer.parseInt(month);
    		dateStart=DateUtils.getFirstDayOfMonth(yearInt, monthInt, "yyyyMMdd");
    		dateEnd=DateUtils.getLastDayOfMonth(yearInt, monthInt, "yyyyMMdd");
    		//获取此月中的所有日期    用来在SQL中查询
    		dateStrList=getDateStrList(dateStart, dateEnd, "yyyyMMdd");
		} catch (Exception e) {
			logger.error("/kcqj/statistics/monthlyList转换失败->"+e.getMessage());
		}
    	
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("dateStrList", dateStrList);
        return kcqjStatisticsService.getMonthlyList(param);
    }
    /**
     * 根据年月获取每月的天数列表，当作前台页面的title
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value="getColumns")
    @ResponseBody
    public JSONArray getColumns(String year,String month){
    	year = StringEscapeUtils.unescapeHtml4(year);
    	month = StringEscapeUtils.unescapeHtml4(month);
    	String dateStart="";
    	String dateEnd="";
    	JSONArray dateArr=new JSONArray();
    	try {
    		int yearInt=Integer.parseInt(year);
    		int monthInt=Integer.parseInt(month);
    		dateStart=DateUtils.getFirstDayOfMonth(yearInt, monthInt, "yyyyMMdd");
    		dateEnd=DateUtils.getLastDayOfMonth(yearInt, monthInt, "yyyyMMdd");
    		dateArr=getDateStrArr(dateStart, dateEnd, "yyyyMMdd");
		} catch (Exception e) {
			logger.error("/kcqj/statistics/getColumns->"+e.getMessage());
		}
    	return dateArr;
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
    		dateStart=DateUtils.formatDate(DateUtils.parseDate(dateStart), "yyyyMMdd");
    		dateEnd=DateUtils.formatDate(DateUtils.parseDate(dateEnd), "yyyyMMdd");
    		
		} catch (Exception e) {
			logger.error("/kcqj/statistics/exportExcel失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
		param.put("searchText", searchText);
		param.put("begin", 0);
	    param.put("end", 10000000);
		String[] titleArr = new String[] {};
		String columnName = "[{\"field\":\"FLIGHT_DATE\"},{\"field\":\"FLIGHT_NUMBER\"},"
				+ "{\"field\":\"AIRLINE_SHORTNAME\"},{\"field\":\"AIRCRAFT_NUMBER\"},{\"field\":\"ACTTYPE_CODE\"},"
				+ "{\"field\":\"PROPERTY_CODE\"},{\"field\":\"MONEY\"},{\"field\":\"PER_NUM\"},"
				+ "{\"field\":\"PER_MONEY\"},{\"field\":\"OPERATORS\"}]";
    		titleArr = new String[] {"航班日期","航班号","航空公司","机号","机型","航班性质", 
    				"金额","人数", "个人所得","姓名"};
    	
		try {
			String fileName =dateStart+"-"+dateEnd +"客舱清洁日工作量统计"+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String,String>> dataList=(List<Map<String, String>>) kcqjStatisticsService.getDataList(param).get("rows");
			excel.setDataList(titleArr, JSON.parseArray(columnName),dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("客舱清洁月工作量数据导出"+ e.getMessage());
		}
	}
    @RequestMapping(value = "exportMonthlyExcel")
    public void printMonthlyList(HttpServletRequest request, HttpServletResponse response,
    		String year,String month) {
    	year = StringEscapeUtils.unescapeHtml4(year);
    	month = StringEscapeUtils.unescapeHtml4(month);
    	String dateStart="";
    	String dateEnd="";
    	List<String> dateStrList=new ArrayList<String>();
    	JSONArray dateArr=new JSONArray();
    	try {
    		int yearInt=Integer.parseInt(year);
    		int monthInt=Integer.parseInt(month);
    		dateStart=DateUtils.getFirstDayOfMonth(yearInt, monthInt, "yyyyMMdd");
    		dateEnd=DateUtils.getLastDayOfMonth(yearInt, monthInt, "yyyyMMdd");
    		dateStrList=getDateStrList(dateStart, dateEnd, "yyyyMMdd");
    		dateArr=getDateStrArr(dateStart, dateEnd, "yyyyMMdd");
		} catch (Exception e) {
			logger.error("/kcqj/statistics/exportMonthlyExcel转换失败->"+e.getMessage());
		}
    	
    	Map<String, Object> param=new HashMap<String, Object>();
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("dateStrList", dateStrList);
		param.put("begin", 0);
	    param.put("end", 10000000);
		StringBuffer columnName=new StringBuffer();
		columnName.append("[{\"field\":\"NAME\"},{\"field\":\"workQuantity\"},");
		//field的名称不固定，此处动态获取
		for(int i=0;i<dateStrList.size();i++){
			String dateStr=dateStrList.get(i);
			columnName.append("{\"field\":\""+dateStr+"\"}");
			if(i!=dateStrList.size()-1){
				columnName.append(",");
			}
		}
		columnName.append("]");
		StringBuffer titleArr=new StringBuffer();
		titleArr.append("姓名,");
		titleArr.append("工作量,");
		//title的名称不固定，此处动态获取
		for(int i=0;i<dateArr.size();i++){
			JSONObject job = dateArr.getJSONObject(i);  
			if(job!=null){
				titleArr.append(job.getString("title")+",");
			}
		}
		String s=titleArr.toString();
		s=s.substring(0,s.length()-1);

		try {
			String fileName =dateStart+"-"+dateEnd +"客舱清洁月工作量统计"+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String,String>> dataList=(List<Map<String, String>>) kcqjStatisticsService.getMonthlyList(param).get("rows");
			excel.setDataList(s.split(","), JSON.parseArray(columnName.toString()),dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("客舱清洁月工作量数据导出"+ e.getMessage());
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
	 /**
	  * 获取开始日期和结束日期之间的日期(用于页面显示)
	  * @param begin
	  * @param end
	  * @param format
	  * @return
	  * @throws ParseException
	  */
	private JSONArray getDateStrArr(String begin, String end, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dBegin = sdf.parse(begin);
		Date dEnd = sdf.parse(end);
		JSONArray dateArr = new JSONArray();
		JSONObject o = new JSONObject();
		o.put("field", begin);
		o.put("title", dBegin.getDate());
		dateArr.add(o);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			JSONObject obj = new JSONObject();
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			obj.put("field", sdf.format(calBegin.getTime()));
			obj.put("title", calBegin.getTime().getDate());
			dateArr.add(obj);
		}
		return dateArr;
	}
	/**
	 * 获取开始日期和结束日期之间的日期(用于数据库查询)
	 * @param begin
	 * @param end
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	private List<String> getDateStrList(String begin, String end, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dBegin = sdf.parse(begin);
		Date dEnd = sdf.parse(end);
		List<String> lDate = new ArrayList<String>();
		lDate.add(begin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(sdf.format(calBegin.getTime()));
		}
		return lDate;
	}
    
   
}
