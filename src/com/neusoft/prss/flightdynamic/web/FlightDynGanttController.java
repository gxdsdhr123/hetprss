
/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017-8-17 下午3:02:24
 *@author:baochl
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.actstand.service.DispatchActstandService;
import com.neusoft.prss.fltinfo.service.UpdateFltinfoService;
import com.neusoft.prss.gate.service.DispatchGateService;
import com.neusoft.prss.gate.service.GateWebService;
import com.neusoft.prss.stand.service.StandWebService;
@Controller
@RequestMapping(value = "${adminPath}/flightdynamic/gantt")
public class FlightDynGanttController extends BaseController {
   
	@Autowired
    private StandWebService standWebService;
	
	@Autowired
	private GateWebService gateWebService;
	
	@Autowired
    private DispatchActstandService dispatchActstandService;
	
	@Autowired
	private UpdateFltinfoService updateFltinfoService;
	
	/**
	 * 获取行李转盘甘特图页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "listxlzpGantt")
    public String listxlzpGantt(Model model) {
        return "prss/flightdynamic/fdxlzpGantt";
    }
	
	/**
     * 获取航班动态预排甘特图Y轴机位数据
     * @return
     */
    @RequestMapping(value = "getDim")
    @ResponseBody
    public JSONObject getDim(String idCol, String textCol, String tableName) {
        JSONArray arr = standWebService.getDim(idCol,textCol,tableName);
        JSONObject res = new JSONObject();
        res.put("results", arr);
        return res;
    }
	
	/**
     * 获取航班动态预排甘特图Y轴机位数据
     * @return
     */
    @RequestMapping(value = "fdWalkthroughGanttYData")
    @ResponseBody
    public String fdWalkthroughGanttYData() {
    	String userId = UserUtils.getUser().getId();
        JSONArray arr = standWebService.fdWalkthroughGanttYData(userId);
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }
    
	/**
     * 获取航班动态预排甘特图航班数据
     * @param param
     * @return
     */
    @RequestMapping(value = "fdWalkthroughGanttData")
    @ResponseBody
    public JSONArray fdWalkthroughGanttData(String param) {
        Map<String,Object> params = new HashMap<String,Object>();
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            params = JSON.parseObject(param);
        }
        JSONArray arr = standWebService.fdWalkthroughGanttData(params);
        return arr;
    }
    
    /**
     * 获取航班动态留存预排甘特图Y轴机位数据
     * @return
     */
    @RequestMapping(value = "keepStandGanttYData")
    @ResponseBody
    public String keepStandGanttYData() {
        JSONArray arr = standWebService.keepStandGanttYData();
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }
    
    /**
     * 获取航班动态留存预排甘特图航班数据
     * @param param
     * @return
     */
    @RequestMapping(value = "keepStandGanttData")
    @ResponseBody
    public JSONArray keepStandGanttData() {
    	Map<String, Object> params = new HashMap<String,Object>();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String baseDate = sdf.format(new Date());
    	params.put("baseDate", baseDate);
        return standWebService.fdWalkthroughGanttData(params);
    }
    
    /**
     * 获取航班动态远机位登机口预排甘特图Y轴数据
     * @return
     */
    @RequestMapping(value = "fdGateGanttYData")
    @ResponseBody
    public String fdGateGanttYData() {
    	String userId = UserUtils.getUser().getId();
        JSONArray arr = gateWebService.fdGateGanttYData(userId);
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }
    /**
     * 获取航班动态远机位登机口预排甘特图航班数据
     * @param param
     * @return
     */
    @RequestMapping(value = "fdGateGanttData")
    @ResponseBody
    public JSONArray fdGateGanttData(String param) {
        Map<String,Object> params = new HashMap<String,Object>();
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            params = JSON.parseObject(param);
        }
        JSONArray arr = gateWebService.fdGateGanttData(params);
        return arr;
    }
    
    /**
     * 获取航班动态行李转盘甘特图Y轴行李转盘数据
     * @return
     */
    @RequestMapping(value = "fdxlzpGanttYData")
    @ResponseBody
    public String fdxlzpGanttYData() {
    	String userId = UserUtils.getUser().getId();
        JSONArray arr = standWebService.fdxlzpGanttYData(userId);
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }
    
    @RequestMapping(value = "fdxlzpGanttData")
    @ResponseBody
    public JSONArray fdxlzpGanttData(String param) {
        Map<String,Object> params = new HashMap<String,Object>();
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            params = JSON.parseObject(param);
        }
        JSONArray arr = standWebService.fdxlzpGanttData(params);
        return arr;
    }
    
    /**
     * 锁定航班
     * @param id
     * @return
     */
    @RequestMapping(value = "lockFlight")
    @ResponseBody
    public String lockFlight(String id) {
        return standWebService.lockFlight(id);
    }
    
    /**
     * 解锁航班
     * @param id
     * @return
     */
    @RequestMapping(value = "unlockFlight")
    @ResponseBody
    public String unlockFlight(String id) {
        return standWebService.unlockFlight(id);
    }
    
    /**
     * 置空航班机位
     * @param id
     * @return
     */
    @RequestMapping(value = "removeStand")
    @ResponseBody
    public String removeStand(String id) {
        return standWebService.removeStand(id);
    }
    
    /**
     * 立即置空航班机位
     * @param id
     * @return
     */
    @RequestMapping(value = "immediateRemoveStand")
    @ResponseBody
    public String immediateRemoveStand(String id) {
    	String user = UserUtils.getUser().getId();
        return standWebService.immediateRemoveStand(id,user);
    }
    
    /**
     * 置空登机口
     * @param id
     * @return
     */
    @RequestMapping(value = "removeGate")
    @ResponseBody
    public String removeGate(String fltid) {
    	String userId=UserUtils.getUser().getId();
        return gateWebService.removeGate(fltid,userId);
    }
    
    /**
     * 分配机位
     * @param id
     * @return
     */
    @RequestMapping(value = "setStand")
    @ResponseBody
    public String setStand(String id, String stand) {
    	String result = "";
    	//分配机位前校验机位机型是否互斥
    	JSONObject reObj = dispatchActstandService.manualJudgeActStand(id, stand);
    	boolean flag = reObj.getBoolean("flag");
    	String code = reObj.getString("code")==null?"":reObj.getString("code");
    	String aircraftNum = reObj.getString("aircraftNum")==null?"":reObj.getString("aircraftNum");
    	String acttype = reObj.getString("acttype")==null?"":reObj.getString("acttype");
    	if(flag){
    		result = standWebService.setStand(id,stand);
    	}else{
    		result = "机号："+aircraftNum+",机型："+acttype+",与机位："+code+"互斥冲突,不可分配";
    	}
        return result;
    }
    
    @RequestMapping(value = "immediateSetStand")
    @ResponseBody
    public String immediateSetStand(String id, String stand) {
    	String result = "";
    	String user = UserUtils.getUser().getId();
    	//分配机位前校验机位机型是否互斥
    	JSONObject reObj = dispatchActstandService.manualJudgeActStand(id, stand);
    	boolean flag = reObj.getBoolean("flag");
    	String code = reObj.getString("code")==null?"":reObj.getString("code");
    	String aircraftNum = reObj.getString("aircraftNum")==null?"":reObj.getString("aircraftNum");
    	String acttype = reObj.getString("acttype")==null?"":reObj.getString("acttype");
    	if(flag){
    		result = standWebService.immediateSetStand(id,stand,user);
    	}else{
    		result = "机号："+aircraftNum+",机型："+acttype+",与机位："+code+"互斥冲突,不可分配";
    	}
        return result;
    }
    
    /**
     * 分配登机口
     * @param id
     * @return
     */
    @RequestMapping(value = "setGate")
    @ResponseBody
    public String setGate(String fltid, String gate) {
        return gateWebService.setGate(fltid,gate);
    }
    
    /**
     * 增加驻场
     * @param actNum
     * @param stand
     * @return
     */
    @RequestMapping(value = "addStay")
    @ResponseBody
    public String addStay(String actNum, String stand) {
    	String userId = UserUtils.getUser().getId();
        return standWebService.addStay(actNum,stand,userId);
    }
    
    /**
     * 删除驻场
     * @param id
     * @param fltid
     * @return
     */
    @RequestMapping(value = "delStay")
    @ResponseBody
    public String delStay(String id, String fltid) {
        return standWebService.delStay(id,fltid);
    }
    
    /**
     * 增加停用
     * @param stand
     * @param start
     * @param end
     * @param remark
     * @return
     */
    @RequestMapping(value = "addStop")
    @ResponseBody
    public String addStop(String id,String stand, String start, String end, String remark) {
    	String userId = UserUtils.getUser().getId();
    	Map<String,String> param = new HashMap<String,String>();
    	param.put("stand", stand);
    	param.put("start", start);
    	param.put("end", end);
    	param.put("remark", remark);
    	param.put("userId", userId);
    	param.put("id", id);
        return standWebService.addStop(param);
    }
    /**
     * 获取停用
     * @param stand
     * @param start
     * @param end
     * @param remark
     * @return
     */
    @RequestMapping(value = "getStopStand")
    @ResponseBody
    public String getStopStand(String id) {
    	JSONObject result = new JSONObject();
    	Map<String,String> param = new HashMap<String,String>();
    	param.put("id", id);
    	JSONArray stopStand = standWebService.getStopStand(param);
    	if(stopStand!=null&&!stopStand.isEmpty()){
    		result = stopStand.getJSONObject(0);
    	}
    	return result.toString();
    }
    /**
     * 修改驻场机号
     * @param id
     * @param stayActnum
     * @return
     */
    @RequestMapping(value = "editStay")
    @ResponseBody
    public String editStay(String id,String stayActnumOld,String stayActnumNew,String fltid) {
    	String userId = UserUtils.getUser().getId();
    	Map<String,String> param = new HashMap<String,String>();
    	param.put("stayActnumOld", stayActnumOld);
    	param.put("stayActnumNew", stayActnumNew);
    	param.put("userId", userId);
    	param.put("id", id);
    	param.put("fltid", fltid);
    	String result="";
    	try{
    		result=standWebService.editStay(param);
    	}catch(Exception e){
    		result="error";
    		return result;
    	}
    	updateFltinfoService.insertEvent(fltid, stayActnumOld, stayActnumNew, userId, "BD_BAYS_INFO", "AIRCRAFT_NUMBER");
    	updateFltinfoService.insertEvent(fltid, stayActnumOld, stayActnumNew, userId, "BD_STATIONED_IN_FIELD", "AIRCRAFT_NUMBER");
    	updateFltinfoService.insertEvent(fltid, stayActnumOld, stayActnumNew, userId, "FD_FLT_INFO", "AIRCRAFT_NUMBER");
        return result;
    }
    
    /**
     * 隐藏机位
     * @param stand
     * @return
     */
    @RequestMapping(value = "hideStand")
    @ResponseBody
    public String hideStand(String stand) {
    	String userId = UserUtils.getUser().getId();
        return standWebService.hideStand(stand,userId);
    }
    
    /**
     * 获取已隐藏的机位
     * @return
     */
    @RequestMapping(value = "getHidedStand")
    @ResponseBody
    public JSONArray getHidedStand() {
    	String userId = UserUtils.getUser().getId();
        return standWebService.getHidedStand(userId);
    }
    
    /**
     * 显示机位
     * @param stand
     * @return
     */
    @RequestMapping(value = "showStand")
    @ResponseBody
    public String showStand(String stand) {
    	String userId = UserUtils.getUser().getId();
        return standWebService.showStand(stand,userId);
    }
    
    /**
     * 保存机位
     * @param stand
     * @return
     */
    @RequestMapping(value = "saveStand")
    @ResponseBody
    public String saveStand() {
    	String userId = UserUtils.getUser().getId();
        return standWebService.saveStand(userId);
    }
    
    @RequestMapping(value = "saveCarousel")
    @ResponseBody
    public String saveCarousel(String param) {
    	JSONArray params = new JSONArray();
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            params = JSONArray.parseArray(param);
        }
    	String userId = UserUtils.getUser().getId();
        return standWebService.saveCarousel(params,userId);
    }
    
    @RequestMapping(value = "saveInFieldStand")
    @ResponseBody
    public String saveInFieldStand(String id,String stand,String oldStand, String fltid) {
    	String userId = UserUtils.getUser().getId();
        return standWebService.saveInFieldStand(id,fltid,stand,oldStand,userId);
    }
    
    /**
     * 保存登机口
     * @param stand
     * @return
     */
    @RequestMapping(value = "saveGate")
    @ResponseBody
    public String saveGate() {
    	String userId = UserUtils.getUser().getId();
        return gateWebService.saveGate(userId);
    }
    
    /**
     * 获取登机口设置
     * @return
     */
    @RequestMapping(value = "getGateSettings")
    @ResponseBody
    public JSONObject getGateSettings() {
        return gateWebService.getGateSettings();
    }
    
    @RequestMapping(value = "saveGateSettings")
    @ResponseBody
    public String saveGateSettings(String startAttr,String startRel,String startMinute,String endAttr,String endRel,String endMinute) {
    	String userId = UserUtils.getUser().getId();
    	Map<String,String> param = new HashMap<String,String>();
    	param.put("startAttr", startAttr);
    	param.put("startRel", startRel);
    	param.put("startMinute", startMinute);
    	param.put("endAttr", endAttr);
    	param.put("endRel", endRel);
    	param.put("endMinute", endMinute);
    	param.put("user", userId);
    	try {
    		gateWebService.saveGateSettings(param);
    		return "success";
		} catch (Exception e) {
			logger.error("保存登机口起止时间设置错误",e);
			return e.toString();
		}
    }
    
    /**
     * 取消调整
     * @param stand
     * @return
     */
    @RequestMapping(value = "cancelStand")
    @ResponseBody
    public String cancelStand() {
    	try {
    		standWebService.cancelStand();
			return "success";
		} catch (Exception e) {
			logger.error("预排甘特取消调整错误", e);
			return "error";
		}
    }
    
    /**
     * 取消调整登机口
     * @param stand
     * @return
     */
    @RequestMapping(value = "cancelGate")
    @ResponseBody
    public String cancelGate() {
    	try {
    		gateWebService.cancelGate();
			return "success";
		} catch (Exception e) {
			logger.error("预排甘特登机口取消调整错误", e);
			return "error";
		}
    }
    
    /**
     * 航班动态预排甘特图页面--拖飞机页面弹窗
     * @param model
     * @return
     */
    @RequestMapping(value = "dragFlightPage")
    public String dragFlightPage(Model model,String inFltid,String outFltid,String inFltNum
    		,String outFltNum,String actNum,String stand,String type,String id,
    		String start,String end) {
    	model.addAttribute("start", start);
		model.addAttribute("end", end);
		if("2".equals(type)){
    		model.addAttribute("saveActstand", stand);
    		model.addAttribute("fltId", inFltid);
    		model.addAttribute("airCode", actNum);
    		model.addAttribute("fltNo", actNum);
    		model.addAttribute("id", id);
    	}else {
    		if(!"".equals(outFltid)){
        		model.addAttribute("fltNum", outFltNum);
        		model.addAttribute("airCode", actNum);
        		model.addAttribute("saveActstand", stand);
        		model.addAttribute("fltId", outFltid);
        		model.addAttribute("fltNo", outFltNum);
        		model.addAttribute("id", id);
        	}else if("".equals(outFltid) && !"".equals(inFltid)){
        		model.addAttribute("fltNum", inFltNum);
        		model.addAttribute("airCode", actNum);
        		model.addAttribute("saveActstand", stand);
        		model.addAttribute("fltId", inFltid);
        		model.addAttribute("fltNo", inFltNum);
        		model.addAttribute("id", id);
        	}
    	}
        return "prss/flightdynamic/fdWalkthroughGanttDragFlight";
    }
    
    /**
     * 保存拖飞机任务
     * @return
     */
    @RequestMapping(value = "saveDragFlight")
    @ResponseBody
    public String saveDragFlight(String id,String tempActstand,String sBeginTime
    		,String sEndTime,String fltId,String fltNo,String airCode,String saveActstand) {
    	try {
    		String result = "";
    		Map<String,Object> param = new HashMap<String,Object>();
    		param.put("id", id);
    		param.put("fltId", fltId);
    		param.put("fltNo", fltNo);
    		param.put("airCode", airCode);
    		param.put("saveActstand", saveActstand);
    		param.put("tempActstand", tempActstand);
    		param.put("sBeginTime", sBeginTime);
    		param.put("sEndTime", sEndTime);
    		param.put("userId", UserUtils.getUser().getId());
    		if("".equals(sBeginTime) && "".equals(sEndTime)){
    			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    			Date date = new Date();
    			param.put("state", 2);
    			param.put("sBeginTime", form.format(date));
        		param.put("sEndTime", form.format(date));
    			standWebService.saveDragFlight(param);
    			standWebService.saveDragFlightHis(param);
    			result = "success";
    		}else if(!"".equals(sBeginTime) && !"".equals(sEndTime)){
    			param.put("state", 0);
    			standWebService.saveDragFlightHis(param);
    			result = "success";
    		}
    		return result;
		} catch (Exception e) {
			logger.error("拖飞机任务保存错误", e);
			return "error";
		}
    }
    
    /**
     * 获取人工确认任务列表
     * @return
     */
    @RequestMapping(value = "getTaskTable")
    @ResponseBody
    public JSONArray getTaskTable() {
    	JSONArray arr = standWebService.getTaskTable();
    	return arr;
    }
    
    /**
     * 人工确认任务
     * @return
     */
    @RequestMapping(value = "updateTaskState")
    @ResponseBody
    public String updateTaskState(String id,String daysId,String fltId,String stand,String sTm,String eTm) {
    	if(stand.length() == 1) {
    		stand = "0"+stand;
    	}
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("id", daysId);//机位动态id
    	map.put("towId", id);//拖飞机历史id
    	map.put("fltId", fltId);
    	map.put("stand", stand);
    	map.put("sBeginTime", sTm);
		map.put("sEndTime", eTm);
		map.put("tempActstand", stand);
    	map.put("state", 1);//确认状态
    	map.put("userId", UserUtils.getUser().getId());
    	try{
    		standWebService.saveDragFlight(map);
    		standWebService.updateDragFlightHis(map);
    		return "success";
    	}catch (Exception e) {
			logger.error("人工确认任务错误", e);
			return "error";
		}
    }
    
    /**
     * 人工取消任务
     * @return
     */
    @RequestMapping(value = "deleteDragFlight")
    @ResponseBody
    public String deleteDragFlight(String id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("towId", id);
    	map.put("state", 2);//删除状态
    	map.put("userId", UserUtils.getUser().getId());
    	try{
    		standWebService.updateDragFlightHis(map);
    		return "success";
    	}catch (Exception e) {
			logger.error("人工删除任务错误", e);
			return "error";
		}
    }
    /**
     * 机位调整
     * @return
     */
    @RequestMapping(value = "takeStand")
    @ResponseBody
    public String takeStand(String beginTm,String endTm) {
    	try{
    		dispatchActstandService.dispatchActstandByTime(beginTm,endTm);
    		return "success";
    	}catch (Exception e) {
			logger.error("机位调整错误", e.getMessage());
			return "error";
		}
    }
    /**
     * 机位调整
     * @return
     */
    @RequestMapping(value = "cancelStop")
    @ResponseBody
    public String cancelStop(String id) {
    	try{
    		standWebService.cancelStop(id);
    		return "success";
    	}catch (Exception e) {
			logger.error("取消机位停用错误", e.getMessage());
			return "error";
		}
    }
    /**
     * 根据航班id获取进出港航班
     * @param fltId
     * @return
     */
    @RequestMapping(value = "getInOutFlight")
    @ResponseBody
    public JSONArray getInOutFlight(String fltId) {
    	return standWebService.getInOutFlight(fltId);
    }
    
    /**
     * 根据机位动态id获取可分配机位列表
     * @param id
     * @return
     */
    @RequestMapping(value = "manualSelectActStand")
    @ResponseBody
    public List<String> manualSelectActStand(String id) {
    	return dispatchActstandService.manualSelectActStandById(id);
    }
}
