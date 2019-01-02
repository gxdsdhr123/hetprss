/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年5月14日 上午9:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.parameter.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.parameter.entity.AirborneHours;
import com.neusoft.prss.parameter.service.AirborneHoursService;
import com.neusoft.prss.stand.entity.ResultByCus;
/**
 * 
 *Discription:空中飞行时间
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/parameter/airborne")
public class AirborneHoursController extends BaseController {
	@Autowired
	private AirborneHoursService airborneHoursService;
	@Autowired
	private CacheService cacheService;
	
    @RequestMapping(value = "list" )
    public String airborneList(Model model) {
    	JSONArray airportList= cacheService.getOpts("dim_airport", "airport_code", "description_cn");
    	JSONArray actTypeList=cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
    	model.addAttribute("ACT_TYPE",actTypeList );
    	model.addAttribute("AIRPORT",airportList );
        return "prss/parameter/airborneHoursList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,String actType,String airport) {
    	actType = StringEscapeUtils.unescapeHtml4(actType);
		airport = StringEscapeUtils.unescapeHtml4(airport);
    	try {
    		actType = java.net.URLDecoder.decode(actType,"utf-8");
    		airport = java.net.URLDecoder.decode(airport,"utf-8");
		} catch (Exception e) {
			logger.error("/plan/planFilter/dataList转换失败->"+e.getMessage());
		}
    	//sql中in条件处理
    	if(!StringUtils.isBlank(actType))
    		actType = actType.replace(",", "','");
    	if(!StringUtils.isBlank(airport))
    		airport = airport.replace(",", "','");
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("actType", actType);
	    param.put("airport", airport);
        return airborneHoursService.getDataList(param);
    }
    
    @RequestMapping(value = "newRecord" )
    public String newRecord(Model model) {
    	JSONArray airportList= cacheService.getOpts("dim_airport", "airport_code", "description_cn");
    	JSONArray actTypeList=cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
    	model.addAttribute("ACT_TYPE",actTypeList );
    	model.addAttribute("AIRPORT",airportList );
        return "prss/parameter/newAirborneHours";
    }
    @RequestMapping(value = "save" )
    @ResponseBody
    public ResultByCus save(String string) {
    	boolean flag=false;
    	ResultByCus result=new ResultByCus();
    	JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
    	AirborneHours airborneHours=new AirborneHours();
    	airborneHours.setDepartApt3Code(aftJSON.getString("airport"));
    	airborneHours.setArrivalApt3Code("HET");
    	airborneHours.setActType(aftJSON.getString("actType"));
    	airborneHours.setStandardFlightTime(aftJSON.getBigDecimal("standardFlightTime"));
    	airborneHours.setDriftValue(aftJSON.getBigDecimal("driftValue"));
    	airborneHours.setBeginFlightDate(aftJSON.getString("beginFlightDate"));
    	airborneHours.setEndFlightDate(aftJSON.getString("endFlightDate"));
    	airborneHours.setOperUser(UserUtils.getUser().getId());
    	BigDecimal id=aftJSON.getBigDecimal("id");
    	if(id==null){
    		flag=airborneHoursService.insertNewRecord(airborneHours);
    	}else{
    		airborneHours.setId(id);
    		airborneHours.setCalcFltTime(aftJSON.getBigDecimal("calcFltTime"));
    		airborneHours.setOperType("1");
    		flag=airborneHoursService.editRecord(airborneHours);
    	}
    	
    	if(flag){
    		result.setCode("0000");
    		result.setMsg("操作成功");
    	}else{
    		result.setCode("0001");
    		result.setMsg("操作失败");
    	}
    	return result;
    }
    
    @RequestMapping(value = "deleteRow" )
    @ResponseBody
    public ResultByCus deleteRow(String id) {
    	boolean flag=false;
    	ResultByCus result=new ResultByCus();
    	String rowId=StringEscapeUtils.unescapeHtml4(id);
    	List<String> idList=Arrays.asList(rowId.split(","));
    	flag=airborneHoursService.deleteRow(idList);
    	if(flag){
    		result.setCode("0000");
    		result.setMsg("操作成功");
    	}else{
    		result.setCode("0001");
    		result.setMsg("操作失败");
    	}
    	return result;
    }
    
    @RequestMapping(value = "compute" )
    @ResponseBody
    public ResultByCus compute(String id) {
    	boolean flag=false;
    	ResultByCus result=new ResultByCus();
    	Integer i=new Integer(5);
    	String rowId=StringEscapeUtils.unescapeHtml4(id);
    	Map<String,Object> map=new HashMap<String,Object>();
    	map.put("id", rowId);
    	map.put("i", i);
    	flag=airborneHoursService.compute(id,i);
    	if(flag){
    		result.setCode("0000");
    		result.setMsg("操作成功");
    	}else{
    		result.setCode("0001");
    		result.setMsg("操作失败");
    	}
    	return result;
    }
    
    @RequestMapping(value = "editRow" )
    public String editRow(Model model,String id) {
    	JSONArray airportList= cacheService.getOpts("dim_airport", "airport_code", "description_cn");
    	JSONArray actTypeList=getActype();
    	JSONObject airborneHours=airborneHoursService.getRowById(id);
    	model.addAttribute("ACT_TYPE",actTypeList );
    	model.addAttribute("AIRPORT",airportList );
    	model.addAttribute("ROW",airborneHours );
        return "prss/parameter/editAirborneHours";
    }
    
    private JSONArray getActype(){
    	JSONArray actTypeList=cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
    	JSONObject jobj=new JSONObject();
    	jobj.put("id", "all");
    	jobj.put("text", "通用");
    	actTypeList.add(0, jobj);
    	return actTypeList;
    }
  
    
    
    
}
