/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年5月16日 下午15:00:00
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.actstand.service.DispatchActstandService;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.flightdynamic.bean.FDChangeFltDate;
import com.neusoft.prss.flightdynamic.service.FDChangeService;
import com.neusoft.prss.flightdynamic.utils.FDChangeUtil;
import com.neusoft.prss.fltinfo.service.UpdateFltinfoService;
import com.neusoft.prss.gate.service.DispatchGateService;
import com.neusoft.prss.stand.entity.ResultByCus;

/**
 * @Discription 航班动态列表双击单元格修改航班信息
 * @author wangtg
 * @version:v1.1
 */

@Controller
@RequestMapping(value = "${adminPath}/fdChange")
public class FDChangeController extends BaseController{
	
	@Resource
	private FDChangeService fdChangeService;
	
	@Resource
	private UpdateFltinfoService updateFltinfoService;
	
	@Autowired
    private CacheService cacheService;
	
	@Autowired
	private DispatchActstandService dispatchActstandService;
	
	@Autowired
	private DispatchGateService dispatchGateService;
	
	/**
	 * 展示机号修改页面（成对修改）
	 * @param in_aircraft_number
	 * @param out_aircraft_number
	 * @param in_fltid
	 * @param out_fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeActNumber")
	public String changeAircraftNumber(String in_aircraft_number,String out_aircraft_number,
			String in_fltid,String out_fltid,Model model) {
		JSONArray actNumberList = cacheService.getList("dim_acreg");
		if(StringUtils.isEmpty(in_aircraft_number)){
			model.addAttribute("old_aircraft_number", out_aircraft_number);
		}else{
			model.addAttribute("old_aircraft_number", in_aircraft_number);
		}
	    model.addAttribute("in_fltid", in_fltid);
	    model.addAttribute("out_fltid", out_fltid);
	    model.addAttribute("actNumber",actNumberList);
	    //下拉框
//	    return "prss/flightdynamic/changeActNumber";
	    //输入框
	    return "prss/flightdynamic/changeActNumberInput";
	}
	
	/**
	 * 校验新机号是否存在
	 */
	@RequestMapping(value = "checkActNumber")
	@ResponseBody
	public ResultByCus checkActNumber(String newActNumber) {
		ResultByCus result=new ResultByCus();
		if(newActNumber!=null){
			newActNumber = newActNumber.toUpperCase();
		}
		String info = cacheService.mapGet("dim_acreg_map", newActNumber);
		if(info!=null&&info.contains(newActNumber)){
			//存在此机号
			result.setCode("0000");
		}else{
			//不存在此机号
			result.setCode("0001");
		}
		return result;
	}
	
	/**
	 * 修改机号，联动修改机型、机位、登机口
	 * 单出港变更机号需要满足两个条件之一，否则不允许修改。1 变更后的机号为当日停场飞机；2 变更后的机号执行当日单出港航班且无实际起飞时间  3单进港且后续没有单出
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveActNumber")
	@ResponseBody
	public ResultByCus saveAircraftNumber(String string) {
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String in_fltid=aftJSON.getString("in_fltid");
		String out_fltid=aftJSON.getString("out_fltid");
		String newValue=aftJSON.getString("newValue");
		String userid=UserUtils.getUser().getId();
		//在此处调用金星的接口
		JSONObject resultObj=updateFltinfoService.updateStandInfoByFltNo(newValue, in_fltid, out_fltid, userid);
		result.setCode(resultObj.getString("code"));
		result.setMsg(resultObj.getString("msg"));
		return result;
	}
	/**
	 * 展示航班性质修改页面
	 * @param fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeProperty")
	public String changeProperty(String fltid, Model model) {
		JSONArray alnProperty = cacheService.getList("dim_task");
		List<Map<String, String>> proObj=fdChangeService.getDataById(fltid);
		if(proObj.size()>0){
			Map<String, String> map=proObj.get(0);
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("property_code", map.get("PROPERTY_CODE"));
		    model.addAttribute("alnProperty", alnProperty);
		}
	    return "prss/flightdynamic/changeAlnProperty";
	}
	/**
	 * 修改航班性质
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveAlnProperty")
	@ResponseBody
	public ResultByCus saveAlnProperty(String string) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String newValue=aftJSON.getString("newValue");
		String oldValue=aftJSON.getString("oldValue");
		String userid=UserUtils.getUser().getId();
		flag=fdChangeService.saveAlnProperty(fltid,newValue);
		if(flag){
			//将变更记录下来
			updateFltinfoService.insertEvent(fltid, oldValue, newValue, userid, "FD_FLT_INFO", "PROPERTY_CODE");
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	/**
	 * 展示航班属性（国内、国际、混合）修改页面
	 * @param fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeAttrCode")
	public String changeAttrCode(String fltid, Model model) {
		List<Map<String, String>> proObj=fdChangeService.getDataById(fltid);
		if(proObj.size()>0){
			Map<String, String> map=proObj.get(0);
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("oldValue", map.get("FLT_ATTR_CODE"));
		}
	    return "prss/flightdynamic/changeAttrCode";
	}
	/**
	 * 修改航班属性
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveAttrCode")
	@ResponseBody
	public ResultByCus saveAttrCode(String string) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String newValue=aftJSON.getString("newValue");
		String oldValue=aftJSON.getString("oldValue");
		String userid=UserUtils.getUser().getId();
		flag=fdChangeService.saveAttrCode(fltid,newValue);
		if(flag){
			//调用事件变更记录服务
			updateFltinfoService.insertEvent(fltid, oldValue, newValue, userid, "FD_FLT_INFO", "FLT_ATTR_CODE");
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	/**
	 * 展示机型修改页面（成对修改）
	 * @param in_acttype_code 进港机型
	 * @param out_acttype_code 出港机型
	 * @param in_fltid 进港ID
	 * @param out_fltid 出港ID
	 * @param in_actstand_code 进港机位
	 * @param out_actstand_code 出港机位
	 * @param refresh 是否处于刷新机型状态 Y:刷新 N:不刷新
	 * @param actNumber 机号 （如果是刷新机型 需要当前机号去维表中查询最新的关联机型）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeActType")
	public String changeActType(String in_acttype_code,String out_acttype_code,
			String in_fltid,String out_fltid, String in_actstand_code,
			String out_actstand_code,String refresh,String actNumber,Model model) {
		JSONArray actTypeList = cacheService.getList("dim_actype");
		//获取机型列表
		if(StringUtils.isEmpty(in_acttype_code)){
			model.addAttribute("old_acttype_code", out_acttype_code);
		}else{
			model.addAttribute("old_acttype_code", in_acttype_code);
		}
		if(StringUtils.isEmpty(in_actstand_code)){
			model.addAttribute("actstand_code", out_actstand_code);
		}else{
			model.addAttribute("actstand_code", in_actstand_code);
		}
	    model.addAttribute("in_fltid", in_fltid);
	    model.addAttribute("out_fltid", out_fltid);
	    model.addAttribute("actType",actTypeList);
	    model.addAttribute("refresh",refresh);
	    // 如果是刷新机型操作，根据当前机号去维表中获取最新的关联机型
	    if("Y".equals(refresh)){
	    	String refreshActNum=fdChangeService.getDimActType(actNumber);
	    	model.addAttribute("refreshActNum",refreshActNum);
	    }
	    return "prss/flightdynamic/changeActType";
	}
	/**
	 * 修改机型 （规则：检测变更后的机型是否和机位匹配，若不匹配也允许操作，但弹出提醒）
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveActType")
	@ResponseBody
	public ResultByCus saveActType(String string) {
		boolean flag=false;
		boolean isClash=false;
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String in_fltid=aftJSON.getString("in_fltid");
		String out_fltid=aftJSON.getString("out_fltid");
		String newActType=aftJSON.getString("newValue");
		String oldActType=aftJSON.getString("oldValue");
		//旧机位
		String actStandCode=aftJSON.getString("actstand_code");
		String userid=UserUtils.getUser().getId();
		List<String> list=new ArrayList<String> ();
		if(!StringUtils.isEmpty(in_fltid)){
			list.add(in_fltid);
		}
		if(!StringUtils.isEmpty(out_fltid)){
			list.add(out_fltid);
		}
		flag=fdChangeService.saveActType(list,newActType);
		if(flag){
			//调用事件变更服务
			if(!StringUtils.isEmpty(in_fltid)){
				updateFltinfoService. insertEvent(in_fltid,oldActType,newActType,userid,"FD_FLT_INFO","ACTTYPE_CODE");
			}
			if(!StringUtils.isEmpty(out_fltid)){
				updateFltinfoService. insertEvent(out_fltid,oldActType,newActType,userid,"FD_FLT_INFO","ACTTYPE_CODE");
			}
			//判断新机型与旧机位是否冲突
			if(!StringUtils.isEmpty(actStandCode)){
				isClash=fdChangeService.checkActStand(newActType,actStandCode);
			}
			if(isClash){
				result.setCode("1000");
				result.setMsg("操作成功！但机型"+newActType+"无法停靠在"+actStandCode+"机位上，请更改机位");
			}else{
				result.setCode("0000");
				result.setMsg("操作成功");
			}
			
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	/**
	 *  展示航班日期修改页面
	 * @param flightDate
	 * @param fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeFltDate")
	public String changeFltDate(String flightDate,String fltid, Model model) {
	    model.addAttribute("fltid", fltid);
	    model.addAttribute("oldValue", flightDate);
	    return "prss/flightdynamic/changeFltDate";
	}
	/**
	 * 修改航班日期 需要同时修改STD、ETD等
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveFltDate")
	@ResponseBody
	public ResultByCus saveFltDate(String string) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		String userid=UserUtils.getUser().getId();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String newValue=aftJSON.getString("newValue");
		String oldValue=aftJSON.getString("oldValue");
		//根据fltid获取STD、ETD等  
		//之所以从数据库中查询，是因为页面上的STD等时间格式在查询语句中被自定义函数TO_DAYTIME()转换过格式
		//为了准确起见，从数据库中获取原来格式的数据
		List<Map<String, String>> fltDateInfoList=fdChangeService.getDataById(fltid);
		Map <String,String> fltDateInfo=fltDateInfoList.get(0);
		String inOutFlag = fltDateInfo.get("IN_OUT_FLAG");
		String std=fltDateInfo.get("STD");
		String etd=fltDateInfo.get("ETD");
		String atd=fltDateInfo.get("ATD");
		String sta=fltDateInfo.get("STA");
		String eta=fltDateInfo.get("ETA");
		String ata=fltDateInfo.get("ATA");
		
		String newStd="";
		String newEtd="";
		String newAtd="";
		String newSta="";
		String newEta="";
		String newAta="";
		//根据新旧航班日期的差值，获取新的STD等
		try{
			if(!StringUtils.isEmpty(std)){
				newStd=FDChangeUtil.getParseFltDate(oldValue, newValue, std);
			}
			if(!StringUtils.isEmpty(etd)){
				newEtd=FDChangeUtil.getParseFltDate(oldValue, newValue, etd);
			}
			if(!StringUtils.isEmpty(atd)){
				newAtd=FDChangeUtil.getParseFltDate(oldValue, newValue, atd);
			}
			if(!StringUtils.isEmpty(sta)){
				newSta=FDChangeUtil.getParseFltDate(oldValue, newValue, sta);
			}
			if(!StringUtils.isEmpty(eta)){
				newEta=FDChangeUtil.getParseFltDate(oldValue, newValue, eta);
			}
			if(!StringUtils.isEmpty(ata)){
				newAta=FDChangeUtil.getParseFltDate(oldValue, newValue, ata);
			}
			
		}catch(Exception e){
			result.setCode("0001");
			result.setMsg("操作失败");
			return result;
		}
		//形参过多 封装到对象中 也可以使用JSON对象
		FDChangeFltDate changeFltDate=new FDChangeFltDate();
		changeFltDate.setFltid(fltid);
		changeFltDate.setNewFlightDate(newValue);
		changeFltDate.setOldFlightDate(oldValue);
		changeFltDate.setStd(newStd);
		changeFltDate.setEtd(newEtd);
		changeFltDate.setAtd(newAtd);
		changeFltDate.setSta(newSta);
		changeFltDate.setEta(newEta);
		changeFltDate.setAta(newAta);
		flag=fdChangeService.saveFltDate(changeFltDate);
		if(flag){
			if(inOutFlag.contains("A")){//进港航班
				if(!StringUtils.isEmpty(newEta)){//预落
					//修改机位动态
					dispatchActstandService.setFltXTA(fltid, newEta, 0);
				} else if(!StringUtils.isEmpty(newAta)){//计落
					//修改机位动态
					dispatchActstandService.setFltXTA(fltid, newAta,1);
				}
			}else{//出港航班
				if(!StringUtils.isEmpty(newEtd)){//预起
					//修改机位动态
					dispatchActstandService.setFltXTD(fltid,newEtd,0);
				} else if(!StringUtils.isEmpty(newAtd)){//实起
					//修改机位动态
					dispatchActstandService.setFltXTD(fltid,newAtd,1);
				} 
			}
			
			updateFltinfoService. insertEvent(fltid, oldValue, newValue, userid, "FD_FLT_INFO", "FLIGHT_DATE");
			updateFltinfoService. insertEvent(fltid, std, newStd, userid, "FD_FLT_INFO", "STD");
			updateFltinfoService. insertEvent(fltid, etd, newEtd, userid, "FD_FLT_INFO", "ETD");
			updateFltinfoService. insertEvent(fltid, atd, newAtd, userid, "FD_FLT_INFO", "ATD");
			updateFltinfoService. insertEvent(fltid, sta, newSta, userid, "FD_FLT_INFO", "STA");
			updateFltinfoService. insertEvent(fltid, eta, newEta, userid, "FD_FLT_INFO", "ETA");
			updateFltinfoService. insertEvent(fltid, ata, newAta, userid, "FD_FLT_INFO", "ATA");
			result.setCode("0000");
			result.setMsg("操作成功");
			
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	/**
	 * 延误原因变更
	 * @param fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeDelayReason")
	public String changeDelayReason(String fltid, Model model) {
		JSONArray delaySource = cacheService.getOpts("dim_delay", "delay_code","description_cn","delay_type");
		JSONArray alnDelaySource=new JSONArray();
		JSONArray releaseDelaySource=new JSONArray();
		for (Iterator<Object> iterator = delaySource.iterator(); iterator.hasNext();) { 
	          JSONObject job = (JSONObject) iterator.next(); 
	          if("01".equals(job.get("delay_type"))){
	        	  alnDelaySource.add(job);
	          }else if("02".equals(job.get("delay_type"))){
	        	  releaseDelaySource.add(job);
	          }
		}
		List<Map<String, String>> proObj=fdChangeService.getDataById(fltid);
		if(proObj.size()>0){
			Map<String, String> map=proObj.get(0);
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("FLIGHT_NUMBER", map.get("FLIGHT_NUMBER"));
		    model.addAttribute("FLIGHT_DATE", map.get("FLIGHT_DATE"));
		    model.addAttribute("AIRCRAFT_NUMBER", map.get("AIRCRAFT_NUMBER"));
		    model.addAttribute("ACTTYPE_CODE", map.get("ACTTYPE_CODE"));
		    model.addAttribute("RELEASE_REASON", map.get("RELEASE_REASON"));
		    model.addAttribute("RELEASE_REASON_DETAIL", map.get("RELEASE_REASON_DETAIL"));
		    model.addAttribute("DELAY_REASON", map.get("DELAY_REASON"));
		    model.addAttribute("DELAY_REASON_DETAIL", map.get("DELAY_REASON_DETAIL"));
		    model.addAttribute("DELAY_REASON_INNER", map.get("DELAY_REASON_INNER"));
		    model.addAttribute("alnDelaySource", alnDelaySource);
		    model.addAttribute("releaseDelaySource", releaseDelaySource);
		}
	    return "prss/flightdynamic/changeDelayReason";
	}
	/**
	 * 保存延误原因
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveDelayReason")
	@ResponseBody
	public ResultByCus saveDelayReason(String string) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String userid=UserUtils.getUser().getId();
		flag=fdChangeService.saveDelayReason(aftJSON);
		if(flag){
			String delayReason=aftJSON.getString("delayReason");
			String releaseReason=aftJSON.getString("releaseReason");
			String delayReasonOld=aftJSON.getString("delayReasonOld");
			String releaseReasonOld=aftJSON.getString("releaseReasonOld");
			String delayReasonDetail=aftJSON.getString("delayReasonDetail");
			String delayReasonDetailOld=aftJSON.getString("delayReasonDetailOld");
			String releaseReasonDetail=aftJSON.getString("releaseReasonDetail");
			String releaseReasonDetailOld=aftJSON.getString("releaseReasonDetailOld");
			String fltid=aftJSON.getString("fltid");
			//如果新值旧值不同时为空  并且不相等 说明值做了改变
			if((!(StringUtils.isEmpty(delayReason)&&StringUtils.isEmpty(delayReasonOld)))&&
					(!delayReason.equals(delayReasonOld))){
				updateFltinfoService. insertEvent(fltid, delayReasonOld, delayReason, userid, "FD_FLT_INFO", "DELAY_REASON");
			}
			if((!(StringUtils.isEmpty(releaseReason)&&StringUtils.isEmpty(releaseReasonOld)))&&
					(!releaseReason.equals(releaseReasonOld))){
				updateFltinfoService. insertEvent(fltid, releaseReasonOld, releaseReason, userid, "FD_FLT_INFO", "RELEASE_REASON");
			}
			if((!(StringUtils.isEmpty(delayReasonDetail)&&StringUtils.isEmpty(delayReasonDetailOld)))&&
					(!delayReasonDetail.equals(delayReasonDetailOld))){
				updateFltinfoService. insertEvent(fltid, delayReasonDetailOld, delayReasonDetail, userid, "FD_FLT_INFO", "DELAY_REASON_DETAIL");
			}
			if((!(StringUtils.isEmpty(releaseReasonDetail)&&StringUtils.isEmpty(releaseReasonDetailOld)))&&
					(!releaseReasonDetail.equals(releaseReasonDetailOld))){
				updateFltinfoService. insertEvent(fltid, releaseReasonDetailOld, releaseReasonDetail, userid, "FD_FLT_INFO", "RELEASE_REASON_DETAIL");
			}
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	/**
	 * 修改出港排序
	 * @param fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="changeDepartSort")
	public String changeDepartSort(String fltid,Model model) {
		List<Map<String, String>> proObj=fdChangeService.getDataById(fltid);
		if(proObj.size()>0){
			Map<String, String> map=proObj.get(0);
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("DEPART_SORT", map.get("DEPART_SORT"));
		}
	    return "prss/flightdynamic/changeDepartSort";
	}
	/**
	 * 保存出港排序
	 */
	@RequestMapping(value = "saveDepartSort")
	@ResponseBody
	public ResultByCus saveDepartSort(String string) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String oldValue=aftJSON.getString("oldValue");
		String newValue=aftJSON.getString("newValue");
		String userId=UserUtils.getUser().getId();
		flag=fdChangeService.saveDepartSort(fltid,newValue);
		if(flag){
			updateFltinfoService.insertEvent(fltid, oldValue, newValue, userId, "FD_FLT_INFO", "DEPART_SORT");
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	/**
	 * 备降变更
	 * @param fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeDiversion")
	public String changeDiversion(String fltid, Model model) {
		JSONArray delaySource = cacheService.getOpts("dim_delay", "delay_code","description_cn","delay_type");
		JSONArray diversionSource=new JSONArray();
		for (Iterator<Object> iterator = delaySource.iterator(); iterator.hasNext();) { 
	          JSONObject job = (JSONObject) iterator.next(); 
	          if("01".equals(job.get("delay_type"))){
	        	  diversionSource.add(job);
	          }
		}
		JSONArray airportCodeSource = cacheService.getOpts("dim_airport", "airport_code","description_cn","icao_code");
		List<Map<String, String>> divObj=fdChangeService.getDataById(fltid);
		if(divObj.size()>0){
			Map<String, String> map=divObj.get(0);
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("FLIGHT_NUMBER", map.get("FLIGHT_NUMBER"));
		    model.addAttribute("FLIGHT_DATE", map.get("FLIGHT_DATE"));
		    model.addAttribute("AIRCRAFT_NUMBER", map.get("AIRCRAFT_NUMBER"));
		    model.addAttribute("ACTTYPE_CODE", map.get("ACTTYPE_CODE"));
		    
		    model.addAttribute("DIVERSION_PORT", map.get("DIVERSION_PORT"));
		    model.addAttribute("DIVERSION_RES", map.get("DIVERSION_RES"));
		    model.addAttribute("DIVERSION_RES_DETAIL", map.get("DIVERSION_RES_DETAIL"));
		    model.addAttribute("DIVERSION_ATD", map.get("DIVERSION_ATD"));
		    
		    model.addAttribute("airportCodeSource", airportCodeSource);
		    model.addAttribute("diversionSource", diversionSource);
		}
	    return "prss/flightdynamic/changeDiversion";
	}
	/**
	 * 保存备降
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveDiversion")
	@ResponseBody
	public ResultByCus saveDiversion(String string) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String diversionPort=aftJSON.getString("diversionPort");
		String diversionPortOld=aftJSON.getString("diversionPortOld");
		String diversionRes=aftJSON.getString("diversionRes");
		String diversionResOld=aftJSON.getString("diversionResOld");
		String diversionATD=aftJSON.getString("diversionATD");
		String diversionATDOld=aftJSON.getString("diversionATDOld");
		String diversionResDetail=aftJSON.getString("diversionResDetail");
		String userId=UserUtils.getUser().getId();
		flag=fdChangeService.saveDiversion(fltid,diversionPort,diversionRes,diversionATD,diversionResDetail);
		if(flag){
			if(!diversionPort.equals(diversionPortOld)){
				updateFltinfoService.insertEvent(fltid, diversionPortOld, diversionPort, userId, "FD_FLT_INFO", "DIVERSION_PORT");
			}
			if(!diversionRes.equals(diversionResOld)){
				updateFltinfoService.insertEvent(fltid, diversionResOld, diversionRes, userId, "FD_FLT_INFO", "DIVERSION_RES");
			}
			if(!diversionATD.equals(diversionATDOld)){
				updateFltinfoService.insertEvent(fltid, diversionATDOld, diversionATD, userId, "FD_FLT_INFO", "DIVERSION_ATD");
			}
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	
	@RequestMapping(value="changePushoutTime")
	public String changePushoutTime(String fltid,Model model) {
		List<Map<String, String>> dataList=fdChangeService.getDepartDataById(fltid);
		String today=DateUtils.getDate("yyyyMMdd");
		Map<String,String> data=new HashMap<String,String>(1);
		if(dataList.size()!=0){
			data=dataList.get(0);
			if(data!=null){
				model.addAttribute("oldValue",data.get("ADMIN_PUSHOUT_TM"));
			}
		}
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("today", today);
		    
	    return "prss/flightdynamic/changePushoutTime";
	}
	
	/**
	 * 保存许可推出时间
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "savePushoutTime")
	@ResponseBody
	public ResultByCus savePushoutTime(String string) {
		boolean flag=false;
		String userid=UserUtils.getUser().getId();
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String pushoutTime=aftJSON.getString("newValue");
		String pushoutTimeOld=aftJSON.getString("oldValue");
		flag=fdChangeService.savePushoutTime(fltid,pushoutTime);
		if(flag){
			updateFltinfoService.insertEvent(fltid, pushoutTimeOld, pushoutTime, userid, "FD_FLT_CLA_DEPART", "ADMIN_PUSHOUT_TM");
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	
	@RequestMapping(value="changeOutGate")
	public String changeOutGate(String fltid,Model model) {
		List<Map<String, String>> dataList=fdChangeService.getDataById(fltid);
		JSONArray gateMap= cacheService.getOpts("dim_gate", "gate_code", "description_cn");
		Map<String,String> data=new HashMap<String,String>(1);
		if(dataList.size()!=0){
			data=dataList.get(0);
			model.addAttribute("oldValue",data.get("GATE"));
		}
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("type", "GATE");
		    model.addAttribute("selectData", gateMap);
		    
	    return "prss/flightdynamic/changeGateOrBagCrsl";
	}
	
	@RequestMapping(value="changeBagCrsl")
	public String changeBagCrsl(String fltid,Model model) {
		List<Map<String, String>> dataList=fdChangeService.getDataById(fltid);
		JSONArray crslMap=cacheService.getOpts("dim_carousel", "carousel_code", "description_cn");
		Map<String,String> data=new HashMap<String,String>(1);
		if(dataList.size()!=0){
			data=dataList.get(0);
			model.addAttribute("oldValue",data.get("BAG_CRSL"));
		}
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("type", "BAG_CRSL");
		    model.addAttribute("selectData", crslMap);
		    
	    return "prss/flightdynamic/changeGateOrBagCrsl";
	}
	
	/**
	 * 保存登机口/行李转盘
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveGateOrBagCrsl")
	@ResponseBody
	public ResultByCus saveGateOrBagCrsl(String string) {
		boolean flag=false;
		String userid=UserUtils.getUser().getId();
		ResultByCus result=new ResultByCus();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String type=aftJSON.getString("type");
		String newValue=aftJSON.getString("newValue");
		String oldValue=aftJSON.getString("oldValue");
		flag=fdChangeService.saveGateOrBagCrsl(fltid,newValue,type);
		if(flag){
			updateFltinfoService.insertEvent(fltid, oldValue, newValue, userid, "FD_FLT_INFO", type);
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	
	/**
	 *  展示地面移交时间修改页面
	 * @param flightDate
	 * @param fltid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changeGroundHOTime")
	public String changeGroundHOTime(String oldValue,String fltid, Model model) {
		List<Map<String, String>> dataList=fdChangeService.getDataById(fltid);
		Map<String,String> data=new HashMap<String,String>(1);
		if(dataList.size()!=0){
			data=dataList.get(0);
			if(data!=null&&data.containsKey("GROUND_HAND_OVER_TM")){
				model.addAttribute("oldValue",data.get("GROUND_HAND_OVER_TM"));
			}
			
		}
	    model.addAttribute("fltid", fltid);
	    return "prss/flightdynamic/changeGroundHOTime";
	}
	/**
	 * 修改地面移交时间 
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveGroundHOTim")
	@ResponseBody
	public ResultByCus saveGroundHOTim(String string) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		String userid=UserUtils.getUser().getId();
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=aftJSON.getString("fltid");
		String newValue=aftJSON.getString("newValue");
		String oldValue=aftJSON.getString("oldValue");
		flag=fdChangeService.saveGroundHOTim(fltid,newValue);
		if(flag){
			updateFltinfoService. insertEvent(fltid, oldValue, newValue, userid, "FD_FLT_INFO", "GROUND_HAND_OVER_TM");
			result.setCode("0000");
			result.setMsg("操作成功");
			
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	
	@RequestMapping(value = "listChangeStand")
    public String listChangeStand(String id,Model model) {
		List<String> standList = dispatchActstandService.manualSelectActStandById(id);
		model.addAttribute("standList", standList);
        return "prss/flightdynamic/changeStand";
    }
	
	@RequestMapping(value="changeBoardingStatus")
	public String changeBoardingStatus(String fltid,String oldval,Model model) {
		Map<String,String> dim = cacheService.getMap("dim_boarding_status_map");
		String oldvalStr = "";
		for(Entry<String, String> entry : dim.entrySet()) {
			if(entry.getValue().equals(oldval)) {
				oldvalStr = entry.getKey();
			}
		}
	    model.addAttribute("fltid", fltid);
	    model.addAttribute("oldval", oldvalStr);
	    return "prss/flightdynamic/changeBoardingStatus";
	}
	
	@RequestMapping(value = "listChangeGate")
    public String listChangeGate(String id,Model model) {
		List<String> fltList = new ArrayList<String>();
		fltList.add(id);
		List<String> gateList = dispatchGateService.manualSelectGateByFltid(fltList);
		model.addAttribute("gateList", gateList);
        return "prss/flightdynamic/changeGate";
    }
	
	@RequestMapping(value="saveBoardingStatus")
	@ResponseBody
	public String saveBoardingStatus(String fltid,String boardingStatus,String oldval) {
		try {
			String user = UserUtils.getUser().getId();
			fdChangeService.saveBoardingStatus(fltid,boardingStatus,oldval,user);
			return "success";
		} catch (Exception e) {
			logger.error("登机状态变更保存失败",e);
			return e.toString();
		}
	}
	
	@RequestMapping(value="saveGate")
	@ResponseBody
	public String saveGate(String fltid,String newVal,String oldval) {
		try {
			String user = UserUtils.getUser().getId();
			fdChangeService.saveGate(fltid,newVal,oldval,user);
			return "success";
		} catch (Exception e) {
			logger.error("登机口变更保存失败",e);
			return e.toString();
		}
	}
	
	@RequestMapping(value="changeCounter")
	public String changeCounter(String fltid,String oldval,Model model) {
		JSONObject data=fdChangeService.getCounters(fltid);
		    model.addAttribute("fltid", fltid);
		    model.addAttribute("oldval", oldval);
		    model.addAttribute("data", data);
	    return "prss/flightdynamic/changeCounter";
	}
	
	@RequestMapping(value="saveCounter")
	@ResponseBody
	public String saveCounter(String fltid,String counter,String oldval) {
		try {
			String user = UserUtils.getUser().getId();
			fdChangeService.saveCounter(fltid,counter,oldval,user);
			return "success";
		} catch (Exception e) {
			logger.error("值机柜台变更保存失败",e);
			return e.toString();
		}
	}
	
	@RequestMapping(value="changeCheckinStatus")
	public String changeCheckinStatus(String fltid,String oldval,Model model) {
		Map<String,String> dim = cacheService.getMap("dim_checkin_status_map");
		String oldvalStr = "";
		for(Entry<String, String> entry : dim.entrySet()) {
			if(entry.getValue().equals(oldval)) {
				oldvalStr = entry.getKey();
			}
		}
	    model.addAttribute("fltid", fltid);
	    model.addAttribute("oldval", oldvalStr);
	    return "prss/flightdynamic/changeCheckinStatus";
	}
	
	@RequestMapping(value="saveCounterStatus")
	@ResponseBody
	public String saveCounterStatus(String fltid,String counterStatus,String oldval) {
		try {
			String user = UserUtils.getUser().getId();
			fdChangeService.saveCounterStatus(fltid,counterStatus,oldval,user);
			return "success";
		} catch (Exception e) {
			logger.error("值机状态变更保存失败",e);
			return e.toString();
		}
	}
	
	@RequestMapping(value="changeTZDJKTime")
	public String changeTZDJKTime(String fltid,String field,Model model) {
		List<Map<String, String>> dataList=fdChangeService.getDataById(fltid);
		String today=DateUtils.getDate("yyyyMMdd");
		field = field.toUpperCase();
		if (field.startsWith("IN_")) {
			field = field.substring(3);
		} else if(field.startsWith("OUT_")) {
			field = field.substring(4);
		}
		Map<String,String> data=new HashMap<String,String>(1);
		if(dataList.size()!=0){
			data=dataList.get(0);
			if(data!=null){
				model.addAttribute("oldValue",data.get(field));
			}
		}
	    model.addAttribute("fltid", fltid);
	    model.addAttribute("field", field);
	    model.addAttribute("today", today);
		    
	    return "prss/flightdynamic/changeTZDJKTime";
	}
	
	/**
	 * 保存通知登机口时间
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveTZDJKTime")
	@ResponseBody
	public ResultByCus saveTZDJKTime(String string) {
		boolean flag=false;
		String userid=UserUtils.getUser().getId();
		ResultByCus result=new ResultByCus();
		JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=json.getString("fltid");
		String brdTime=json.getString("newValue");
		String old=json.getString("oldValue");
		String field = json.getString("field");
		flag=fdChangeService.saveTZDJKTime(fltid,field,brdTime);
		if(flag){
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
	
	@RequestMapping(value="changeBrdTime")
	public String changeBrdTime(String fltid,String field,Model model) {
		List<Map<String, String>> dataList=fdChangeService.getDataById(fltid);
		String today=DateUtils.getDate("yyyyMMdd");
		field = field.toUpperCase();
		if (field.startsWith("IN_")) {
			field = field.substring(3);
		} else if(field.startsWith("OUT_")) {
			field = field.substring(4);
		}
		Map<String,String> data=new HashMap<String,String>(1);
		if(dataList.size()!=0){
			data=dataList.get(0);
			if(data!=null){
				model.addAttribute("oldValue",data.get(field));
			}
		}
	    model.addAttribute("fltid", fltid);
	    model.addAttribute("field", field);
	    model.addAttribute("today", today);
		    
	    return "prss/flightdynamic/changeBrdTime";
	}
	
	/**
	 * 保存许可推出时间
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveBrdTime")
	@ResponseBody
	public ResultByCus saveBrdTime(String string) {
		boolean flag=false;
		String userid=UserUtils.getUser().getId();
		ResultByCus result=new ResultByCus();
		JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=json.getString("fltid");
		String brdTime=json.getString("newValue");
		String old=json.getString("oldValue");
		String field = json.getString("field");
		flag=fdChangeService.saveBrdTime(fltid,field,brdTime);
		if(flag){
			updateFltinfoService.insertEvent(fltid, brdTime, old, userid, "FD_FLT_INFO", field);
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	    
	}
}
