/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月18日 下午7:51:57
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.flightdynamic.service.FdGridService;
import com.neusoft.prss.flightdynamic.service.FltMonitorService;

/**
 * 保障图/航班监控图
 * @author Think
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/fltmonitor")
public class FltMonitorController extends BaseController {

	@Autowired
	private FltMonitorService fltMonitorService;
	
	@Autowired
    private FdGridService fdGridService;

	/**
	 * 保障图页面
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "")
	public String ensureList(Model model, String inFltid, String outFltid) {
		model.addAttribute("inFltid", inFltid);
		model.addAttribute("outFltid", outFltid);
		return "prss/flightdynamic/ensureList";
	}
	
	/**
	 * 
	 *Discription:历史保障图页面.
	 *@param model
	 *@param inFltid
	 *@param outFltid
	 *@return
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年12月14日 SunJ [变更描述]
	 */
	@RequestMapping(value = "his")
	public String ensureHisList(Model model, String inFltid, String outFltid) {
		model.addAttribute("inFltid", inFltid);
		model.addAttribute("outFltid", outFltid);
		return "prss/flightdynamic/ensureHisList";
	}

	/**
	 * 航班监控图页面
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "fltmonitorList")
	public String fltmonitorList(Model model, String inFltid, String outFltid,String hisFlag) {
		FltInfo fltInfo = new FltInfo();
		try {
			fltInfo = fltMonitorService.getFltInfo(inFltid, outFltid,hisFlag);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		fltInfo.setInFltid(inFltid);
		fltInfo.setOutFltid(outFltid);
		model.addAttribute("fltInfo", fltInfo);
		model.addAttribute("hisFlag", hisFlag);
		return "prss/flightdynamic/fltmonitorList";
	}

	/**
	 * 
	 * 返回保障信息
	 * 
	 * @param model
	 * @return:新增计划页面
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@ResponseBody
	@RequestMapping(value = "getData")
	public Map<String, Object> getFltmonitorData(String inFltid, String outFltid, String isY,String hisFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			responseMap = fltMonitorService.getFltmonitorData(inFltid, outFltid, isY,hisFlag);
		} catch (Exception e) {
			responseMap.put("code", "-1");
			responseMap.put("desc", "服务器发生异常，请联系管理员！");
			logger.error(e.getMessage(), e);
		}
		return responseMap;
	}

	/**
	 * 节点弹出数据
	 * 
	 * @param fltid
	 * @param nodeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getNodeData")
	public Map<String, Object> getNodeData(String inFltid, String outFltid, String nodeId,String hisFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			responseMap = fltMonitorService.getNodeData(inFltid, outFltid, nodeId,hisFlag);
		} catch (Exception e) {
			responseMap.put("code", "-1");
			responseMap.put("desc", "服务器发生异常，请联系管理员！");
			logger.error(e.getMessage(), e);
		}
		return responseMap;
	}

	/**
	 * 旅客详细信息
	 * 
	 * @param fltid
	 * @param nodeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getPassengerInfo")
	public Map<String, Object> getPassengerInfo(String fltid, String inout,String hisFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			responseMap = fltMonitorService.getPassengerInfo(fltid, inout,hisFlag);
		} catch (Exception e) {
			responseMap.put("code", "-1");
			responseMap.put("desc", "服务器发生异常，请联系管理员！");
			logger.error(e.getMessage(), e);
		}
		return responseMap;
	}

	/**
	 * 人员事件详情
	 * 
	 * @param fltid
	 * @param nodeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getPersonEvent")
	public Map<String, Object> getPersonEvent(String fltid, String personCode, String taskId,String hisFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			responseMap = fltMonitorService.getPersonEvent(fltid, personCode, taskId,hisFlag);
		} catch (Exception e) {
			responseMap.put("code", "-1");
			responseMap.put("desc", "服务器发生异常，请联系管理员！");
			logger.error(e.getMessage(), e);
		}
		return responseMap;
	}
	
	/**
	 * 取得要客详情对照表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getVipFlagMap")
	public JSONArray getVipFlagMap(){
		return fdGridService.getVipFlag();
	}
	
	/**
	 * 根据用户ID获取电话号码，用于通信
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getPhoneByUserId")
	public String getPhoneByUserId(String userId) {
		try {
			return fltMonitorService.getPhoneByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

}
