/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年4月24日 上午9:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.telegraph.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.telegraph.service.TelegraphFlightService;

/**
 * 
 *Discription:航班原始报文的查看
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/telegraph/flight")
public class TelegraphFlightController extends BaseController {

	@Autowired
	private TelegraphFlightService telegraphFlightService;
	
    @RequestMapping(value = "list" )
    public String flightList(Model model) {
    	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
    	list=telegraphFlightService.getTelType();
    	model.addAttribute("TEL_TYPE",list );
        return "prss/telegraph/flightTelegraphList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,String acceptDate,
    		String flightNumber,String telType,String acceptTime) {
    	acceptDate = StringEscapeUtils.unescapeHtml4(acceptDate).toUpperCase();
		flightNumber = StringEscapeUtils.unescapeHtml4(flightNumber).toUpperCase();
		telType = StringEscapeUtils.unescapeHtml4(telType).toUpperCase();
		acceptTime = StringEscapeUtils.unescapeHtml4(acceptTime).toUpperCase();
    	try {
    		acceptDate = java.net.URLDecoder.decode(acceptDate,"utf-8");
    		flightNumber = java.net.URLDecoder.decode(flightNumber,"utf-8");
    		telType = java.net.URLDecoder.decode(telType,"utf-8");
    		acceptTime = java.net.URLDecoder.decode(acceptTime,"utf-8");
		} catch (Exception e) {}
    	 
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("acceptDate", acceptDate);
	    param.put("flightNumber", flightNumber);
	    param.put("telType", telType);
	    param.put("acceptTime", acceptTime);
        return telegraphFlightService.getDataList(param);
    }
    
    
    
   
}
