/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年4月27日 上午10:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.plan.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.plan.entity.PlanQueryChartEntity;
import com.neusoft.prss.plan.service.PlanQueryService;


/**
 * 
 *Discription:长期计划筛选页面
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/plan/planFilter")
public class PlanQueryController extends BaseController {
	
	@Resource
	private PlanQueryService planQueryService;
	private Logger logger=Logger.getLogger(PlanQueryController.class);
    @RequestMapping(value = "list" )
    public String flightList(Model model) {
    	List<Map<String, Object>> airlinesList=new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> airportList=new ArrayList<Map<String, Object>>();
    	airlinesList=planQueryService.getAirlines();
    	airportList=planQueryService.getAirports();
    	String today = new SimpleDateFormat( "yyyyMMdd").format( Calendar.getInstance().getTime());
    	model.addAttribute("AIRLINES",airlinesList );
    	model.addAttribute("AIRPORTS", airportList);
    	model.addAttribute("planTimeStart", "0000");
    	model.addAttribute("planTimeEnd", "2359");
    	model.addAttribute("FLIGHT_DATE", today);
        return "prss/plan/planFilterList";
    }
    
    @RequestMapping(value = "dataListLeft" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String date,String time,String airline,String airport) {
    	
    	date = StringEscapeUtils.unescapeHtml4(date);
    	time = StringEscapeUtils.unescapeHtml4(time);
		airline = StringEscapeUtils.unescapeHtml4(airline);
		airport = StringEscapeUtils.unescapeHtml4(airport);
    	try {
    		date = java.net.URLDecoder.decode(date,"utf-8");
    		time = java.net.URLDecoder.decode(time,"utf-8");
    		airline = java.net.URLDecoder.decode(airline,"utf-8");
    		airport = java.net.URLDecoder.decode(airport,"utf-8");
		} catch (Exception e) {
			logger.error("/plan/planFilter/dataList转换失败->"+e.getMessage());
		}
    	//sql中in条件处理
    	if(!StringUtils.isBlank(airline))
    		airline = airline.replace(",", "','");
    	if(!StringUtils.isBlank(airport))
    		airport = airport.replace(",", "','");
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("date", date);
	    param.put("time", time);
	    param.put("airline", airline);
	    param.put("airport", airport);
	    param.put("baotou", Global.getConfig("airport_code4"));
        return planQueryService.getLeftDataList(param);
    }
    @RequestMapping(value = "dataListRight" )
    @ResponseBody
    public Map<String,Object> getDataRight(int pageSize,int pageNumber,
    		String date,String time,String airline,String airport) {
    	date = StringEscapeUtils.unescapeHtml4(date);
    	time = StringEscapeUtils.unescapeHtml4(time);
		airline = StringEscapeUtils.unescapeHtml4(airline);
		airport = StringEscapeUtils.unescapeHtml4(airport);
    	try {
    		date = java.net.URLDecoder.decode(date,"utf-8");
    		time = java.net.URLDecoder.decode(time,"utf-8");
    		airline = java.net.URLDecoder.decode(airline,"utf-8");
    		airport = java.net.URLDecoder.decode(airport,"utf-8");
		} catch (Exception e) {
			logger.error("/plan/planFilter/dataList转换失败->"+e.getMessage());
		}
    	//sql中in条件处理
    	if(!StringUtils.isBlank(airline))
    		airline = airline.replace(",", "','");
    	if(!StringUtils.isBlank(airport))
    		airport = airport.replace(",", "','");
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("date", date);
	    param.put("time", time);
	    param.put("airline", airline);
	    param.put("airport", airport);
	    param.put("baotou", Global.getConfig("airport_code4"));
        return planQueryService.getRightDataList(param);
    }
    @RequestMapping(value = "getChartPage" )
    public String chartPage() {
        return "prss/plan/planFilterChart";
    }
    @RequestMapping(value = "getChartData" )
    @ResponseBody
    public  ResponseVO<PlanQueryChartEntity> getChartData(String date,String time,String airport,String airline) {
    	Map<String, Integer[]> planChartData = new HashMap<String, Integer[]>();
    	Map<String, String[]> planChartX = new HashMap<String, String[]>();
    	PlanQueryChartEntity bean = new PlanQueryChartEntity();
    	Map<String, Object> param=new HashMap<String, Object>();
	    param.put("date", date);
	    param.put("time", time);
	    param.put("airline", airline);
	    param.put("airport", airport);
	    param.put("baotou", Global.getConfig("airport_code4"));
	    
        List<Map<String,Object>> chartList= planQueryService.getChartData(param);
        Integer[] a=new Integer[chartList.size()];
        Integer[] b=new Integer[chartList.size()];
        Integer[] c=new Integer[chartList.size()];
        String[] d=new String[chartList.size()];
        int i=0;
        for(Map<String,Object> map:chartList){
        	String dateStr=(String) map.get("SHOW_TIME");
        	BigDecimal allCnt=(BigDecimal) map.get("ALL_CNT");
        	BigDecimal outCnt=(BigDecimal) map.get("OUT_CNT");
        	BigDecimal inCnt=(BigDecimal) map.get("IN_CNT");
        	a[i]=allCnt.intValue();
        	b[i]=outCnt.intValue();
        	c[i]=inCnt.intValue();
        	d[i]=dateStr.toString();
        	i++;
        }
        planChartData.put("allCntArr", a);
        planChartData.put("outCntrr", b);
        planChartData.put("inCntArr", c);
        planChartX.put("StringHeader", d);
        bean.setPlanChartData(planChartData);
        bean.setPlanChartX(planChartX);
        return ResponseVO.<PlanQueryChartEntity>build().setData(bean);
    }
    
   
}
