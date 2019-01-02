/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年3月5日 上午9:21:55
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.flight.service.FlightPairService;
import com.neusoft.prss.flightdynamic.service.FdRelService;

@Controller
@RequestMapping(value = "${adminPath}/fdRel")
public class FdRelController extends BaseController {
	
	@Autowired
    private FdRelService fdRelService;
	@Autowired
	private FlightPairService flightPairService;
	@Autowired
	private CacheService cacheService;
	/**
	 * 
	 *Discription:机号拆分.
	 *@param model
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:2018年3月5日 Heqg [变更描述]
	 */
	@RequestMapping(value = "list")
    public String list(Model model,String fltIds,String ... fltNum) {
		model.addAttribute("fltIds", fltIds);
		if(fltNum!=null && fltNum.length != 0) {
			model.addAttribute("fltNum", fltNum[0]);
		}
		//机场
		JSONArray airports = cacheService.getOpts("dim_airport", "airport_code", "description_cn","icao_code");//机场展示为中文简称
		//状态
		JSONArray acfStatus = cacheService.getCommonDict("acfStatus");
		//登机口
		JSONArray gates = cacheService.getOpts("dim_gate", "gate_code", "description_cn");
		/*//机号
		JSONArray acregs = cacheService.getOpts("dim_acreg", "acreg_code", "acreg_code");*/
		model.addAttribute("airports", airports);
		model.addAttribute("acfStatus", acfStatus);
		model.addAttribute("gates", gates);
		return "prss/flightdynamic/fdRel";
    }
	/**
	 * Discription 机号拆分页面加载航班
	 * @param fltNo
	 * @param aircraftNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getIOSet")
    public JSONArray getIOSet(String fltIds,String aircraftNo,String timeCond) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("timeCond", timeCond);
		JSONArray result= new JSONArray();
		if (!StringUtils.isEmpty(fltIds)&&fltIds.endsWith(",")) {
			fltIds = fltIds.substring(0, fltIds.length()-1);
			params.put("fltIds", fltIds);
			params.put("aircraftNo", aircraftNo);
			params.put("fltNo", "");
			result = fdRelService.getFltIO(params);
		}else if(!StringUtils.isEmpty(aircraftNo)) {
			params.put("fltIds", "");
			params.put("aircraftNo", aircraftNo);
			params.put("fltNo", "");
			result = fdRelService.getFltIO(params);
		}
        return result;
    }
	/**
	 * Discription 机号拆分页面检索
	 * @param fltNo
	 * @param aircraftNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFltIO")
    public JSONArray getFltIO(String fltNo, String aircraftNo,String timeCond) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("timeCond", timeCond);
		params.put("fltNo", fltNo);
		params.put("aircraftNo", aircraftNo);
		params.put("fltIds", "");
        return fdRelService.getFltIO(params);
    }
	/**
	 * 自动配对
	 * @param inFltid
	 * @param outFltid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "autoRel")
    public JSONArray autoRel(String fltData) {
		fltData = StringEscapeUtils.unescapeHtml4(fltData);
		JSONArray fltDataList = JSONArray.parseArray(fltData);
		JSONArray relList = flightPairService.pairSpecifiedFlight(fltDataList);
        return relList;
    }
	/**
	 * 根据机号获取机型
	 * @param aftNum 机号
	 * @return
	 */
	@RequestMapping(value = "getActType")
    @ResponseBody
    public String getActType(String aftNum) {
        String res = cacheService.mapGet("dim_acreg_map", aftNum);
        JSONObject resStr = JSON.parseObject(res);
        return resStr.getString("actype_code");
    }
	/**
	 * 校验机号
	 * @param aftNum 机号
	 * @return
	 */
	@RequestMapping(value = "validAircraftNo")
    @ResponseBody
    public String validAircraftNo(String aftNum,String fltid,String ioType) {
        String res = cacheService.mapGet("dim_acreg_map", aftNum);
        JSONObject result = new JSONObject();
        if(StringUtils.isEmpty(res)||JSON.parseObject(res).isEmpty()){
        	result.put("status", "-2");
        	result.put("error", "未匹配到相关机号");
        	return result.toString();
        } else {
        	result.put("status", "1");
        	result.put("actType", JSON.parseObject(res).getString("actype_code"));
        }
        /*JSONObject alertInfo = null;
        if("A".equalsIgnoreCase(ioType)){
        	alertInfo = updateFltService.getAlertInfo(aftNum, fltid, null);
        } else {
        	alertInfo = updateFltService.getAlertInfo(aftNum, null, fltid);
        }
        if(alertInfo!=null&&alertInfo.containsKey("code")){
        	String code = alertInfo.getString("code");
        	if("0000".equals(code)){
        		result.put("status", "1");
        	} else {
        		result.put("status", "-1");
            	result.put("error", alertInfo.getString("msg"));
        		return result.toString();
        	}
        } else {
        	result.put("status", "-1");
        	result.put("error", "机号校验失败！");
        }*/
        return result.toString();
    }
	/**
	 * 保存配对
	 * @param data
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "saveRel")
    public String saveRel(String data) {
    	data = StringEscapeUtils.unescapeHtml4(data); 
    	JSONArray dataList = JSONArray.parseArray(data);
    	JSONObject result = fdRelService.saveRel(dataList,UserUtils.getUser().getId());
    	return result.toString();
    }
}
