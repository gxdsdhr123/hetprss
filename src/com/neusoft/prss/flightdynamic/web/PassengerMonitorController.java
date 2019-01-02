/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年4月25日 下午15:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.flightdynamic.service.PassengerMonitorService;

/**
 * 
 *Discription:旅客流程监控页面
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/flightdynamic/passengerMonitor")
public class PassengerMonitorController extends BaseController {

	@Autowired
	private PassengerMonitorService passengerMonitorService;
	private Logger logger=Logger.getLogger(PassengerMonitorController.class);
    @RequestMapping(value = "list" )
    public String flightList(Model model) {
    	List<Map<String, Object>> airlinesList=new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> airportList=new ArrayList<Map<String, Object>>();
    	airlinesList=passengerMonitorService.getAirlines();
    	airportList=passengerMonitorService.getAirports();
    	String today = new SimpleDateFormat( "yyyyMMdd").format( Calendar.getInstance().getTime());
    	model.addAttribute("AIRLINES",airlinesList );
    	model.addAttribute("AIRPORTS", airportList);
    	model.addAttribute("FLIGHT_DATE", today);
        return "prss/flightdynamic/passengerMonitorList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String flightDate,String etd,String flightNumber,String airline,String airport) {
    	flightDate = StringEscapeUtils.unescapeHtml4(flightDate);
		flightNumber = StringEscapeUtils.unescapeHtml4(flightNumber).toUpperCase();
		etd = StringEscapeUtils.unescapeHtml4(etd);
		airline = StringEscapeUtils.unescapeHtml4(airline);
		airport = StringEscapeUtils.unescapeHtml4(airport);
    	try {
    		flightDate = java.net.URLDecoder.decode(flightDate,"utf-8");
    		flightNumber = java.net.URLDecoder.decode(flightNumber,"utf-8");
    		etd = java.net.URLDecoder.decode(etd,"utf-8");
    		airline = java.net.URLDecoder.decode(airline,"utf-8");
    		airport = java.net.URLDecoder.decode(airport,"utf-8");
		} catch (Exception e) {
			logger.error("/flightdynamic/passengerMonitor/dataList转换失败->"+e.getMessage());
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
	    param.put("flightDate", flightDate);
	    param.put("flightNumber", flightNumber);
	    param.put("etd", etd);
	    param.put("airline", airline);
	    param.put("airport", airport);
        return passengerMonitorService.getDataList(param);
    }
    
   
}
