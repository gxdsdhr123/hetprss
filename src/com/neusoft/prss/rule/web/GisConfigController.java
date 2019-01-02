package com.neusoft.prss.rule.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.neusoft.prss.rule.service.GisConfigService;

/**
 * 电子围栏、节点配置规则
 */
@Controller
@RequestMapping(value = "${adminPath}/rule/gisConfig")
public class GisConfigController extends BaseController{
	@Autowired
	private GisConfigService gisConfigService;
	
	@Autowired
    private CacheService cacheService;
	
	/**
	 * 电子围栏、节点配置列表
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		//获取保障类型选项
		JSONArray reskindList = gisConfigService.getReskind();
		model.addAttribute("reskindSource", reskindList);
		return "prss/rule/gisConfigList";
	}
	
	/**
	 * 电子围栏、节点配置新增、修改页
	 */
	@RequestMapping(value="toForm")
	public String edit(Model model,String id,String operateType){
		if(operateType!=null&&operateType.equals("add")){
			model.addAttribute("operateType", operateType);
			model.addAttribute("modifyData", new JSONObject());
		}else{
			model.addAttribute("operateType", operateType);
			JSONObject data = gisConfigService.getDataById(id);
			model.addAttribute("modifyData", data);
			//获取已选项
			Map<String,Object> params = new HashMap<String, Object>();
			String reskind = data.getString("jobKind");
			String restype = data.getString("jobType");
			String processId = data.getString("procId");
			String targetType = data.getString("targetType");
			params.put("processId", processId);
			params.put("reskind", reskind);
			params.put("restype", restype);
			JSONArray targetAreaSource = targetArea(targetType);
			JSONArray restypeSource = gisConfigService.getRestype(params);
			JSONArray processSource = gisConfigService.getProcess(params);
			JSONArray nodeSource = gisConfigService.getNode(params);
			model.addAttribute("restypeSource", restypeSource);
			model.addAttribute("processSource", processSource);
			model.addAttribute("nodeSource", nodeSource);
			model.addAttribute("targetAreaSource", targetAreaSource);
		}
		//获取选项
		JSONArray reskindList = gisConfigService.getReskind();
		model.addAttribute("reskindSource", reskindList);
		return "prss/rule/gisConfigForm";
	}
	
	/**
	 * 电子围栏、节点配置列表数据
	 */
	@RequestMapping(value="getData")
	@ResponseBody
	public JSONObject getData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
        Map<String,Object> params = new HashMap<String, Object>();
		String pageNumber = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		//保障类型
		String reskind = request.getParameter("reskind");
		//作业类型
		String restype = request.getParameter("restype");
		try {
			int begin = 1;
			int end = 21;
			if(pageNumber!=null&&!pageNumber.equals("")&&pageSize!=null&&!pageSize.equals("")){
				int intPageNum = Integer.parseInt(pageNumber);
				int intPageSize = Integer.parseInt(pageSize);
				begin=(intPageNum - 1) * intPageSize+1;
		        end=intPageNum * intPageSize+1;
			}
			params.put("begin", begin);
			params.put("end", end);
			params.put("reskind", reskind);
			params.put("restype", restype);
			rs = gisConfigService.getDataList(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 保存电子围栏、节点配置信息
	 */
	@ResponseBody
	@RequestMapping(value ="save")
	public String save(HttpServletRequest request) {
		String result = "success";
		//modify更新 add新增
		String operateType = request.getParameter("operateType");
		//保障类型
		String reskind = request.getParameter("reskind");
		//作业类型
		String restype = request.getParameter("restype");
		//流程id
		String processId = request.getParameter("processId");
		//节点id
		String nodeId = request.getParameter("nodeId");
		//区域类型(任务类型)
		String targetType = request.getParameter("targetType");
		//区域编码(任务位置)
		String targetArea = request.getParameter("targetArea");
		//电子围栏区域
		String areaCodes = request.getParameter("areaCodesVal");
		//延迟时间
		String delaySecond = request.getParameter("delaySecond");
		//是否生效
		String inUse = request.getParameter("inUse");
		//流程节点围栏配置表、节点围栏关系表id
		String id = request.getParameter("id");
		String currDate = getAddDay("", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm", 0);
		String userId = UserUtils.getUser().getId();
		try {
			Map<String,Object> params = new HashMap<String, Object>();
			//modify更新 add新增
			params.put("operateType", operateType);
			//电子围栏区域ids
			params.put("areaCodes", areaCodes);
			//新增/更新属性
			params.put("jobKind", reskind);
			params.put("jobType", restype);
			params.put("procId", processId);
			params.put("nodeId", nodeId);
			params.put("targetType", targetType);
			params.put("targetArea", targetArea);
			params.put("delaySecond", delaySecond);
			params.put("inUse", inUse);
			params.put("id", id);
			params.put("creatorId", userId);
			params.put("createTm", currDate);
			params.put("operator", userId);
			params.put("updateTm", currDate);
			int countSave = gisConfigService.saveGisConfig(params);
			if(countSave==0){
				result = "fail";
			}
		} catch (Exception e) {
			result = "err";
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 删除电子围栏、节点配置信息
	 */
	@ResponseBody
	@RequestMapping(value="delete")
	public String delete(HttpServletRequest request) {
		String result = "success";
		int countDel = 0;
		String id = request.getParameter("id");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try{
			if(id!=null&&!id.equals("")){
				paramMap.put("ids", id);
				countDel = gisConfigService.deleteGisConfig(paramMap);
			}
			if(countDel==0){
				result = "nonDel";
			}
		} catch (Exception e) {
			result = "err";
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取作业类型选项
	 */
	@RequestMapping(value="getRestype")
	@ResponseBody
	public JSONArray getRestype(HttpServletRequest request){
		JSONArray rs = new JSONArray();
        Map<String,Object> params = new HashMap<String, Object>();
		//保障类型
		String reskind = request.getParameter("reskind");
		try {
			params.put("reskind", reskind);
			rs = gisConfigService.getRestype(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 获取流程选项
	 */
	@RequestMapping(value="getProcess")
	@ResponseBody
	public JSONArray getProcess(HttpServletRequest request){
		JSONArray rs = new JSONArray();
        Map<String,Object> params = new HashMap<String, Object>();
		//保障类型
		String reskind = request.getParameter("reskind");
		//作业类型
		String restype = request.getParameter("restype");
		try {
			params.put("reskind", reskind);
			params.put("restype", restype);
			rs = gisConfigService.getProcess(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 获取节点选项
	 */
	@RequestMapping(value="getNode")
	@ResponseBody
	public JSONArray getNode(HttpServletRequest request){
		JSONArray rs = new JSONArray();
        Map<String,Object> params = new HashMap<String, Object>();
		//流程id
		String processId = request.getParameter("processId");
		//保障类型
		String reskind = request.getParameter("reskind");
		//作业类型
		String restype = request.getParameter("restype");
		try {
			params.put("processId", processId);
			params.put("reskind", reskind);
			params.put("restype", restype);
			rs = gisConfigService.getNode(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 获取任务位置选项
	 */
	@RequestMapping(value="getTargetArea")
	@ResponseBody
	public JSONArray getTargetArea(HttpServletRequest request){
		//区域类型 0机位 1登机口 2到达口
		String targetType = request.getParameter("targetType");
		JSONArray rs = targetArea(targetType);
	    return rs;
	}
	
	private JSONArray targetArea(String targetType){
		JSONArray rs = new JSONArray();
		if(targetType!=null&&!targetType.equals("")){
			if(targetType.equals("0")){
				//机位
			    rs = cacheService.getOpts("dim_bay", "bay_code","description_cn");
			}else if(targetType.equals("1")){
				//登机口
			    rs = cacheService.getOpts("dim_gate", "gate_code", "description_cn");
			}if(targetType.equals("2")){
				//到达口
				JSONObject dGate = new JSONObject();//国内到达口
				dGate.put("id", "01");
				dGate.put("text", "国内到达口");
				JSONObject iGate = new JSONObject();//国际到达口
				iGate.put("id", "02");
				iGate.put("text", "国际到达口");
				rs.add(dGate);
				rs.add(iGate);
			}
		}
		return rs;
	}
	
	/**
	 * 获取日期加减天后日期字符串
	 * @param dateStr
	 * @param inPatterns
	 * @param outPatterns
	 * @param days
	 * @return
	 */
	private String getAddDay(String dateStr,String inPatterns,String outPatterns,int days){
		SimpleDateFormat fmIn = new SimpleDateFormat(inPatterns);
		SimpleDateFormat fmOut = new SimpleDateFormat(outPatterns);
		String re = "";
		try {
			Date date = null;
			if(dateStr.equals("")){
				date = new Date();
			}else{
				date = fmIn.parse(dateStr);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, days);
			if(outPatterns!=null&&!outPatterns.equals("")){
				re = fmOut.format(cal.getTime());
			}else{
				re = fmIn.format(cal.getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re;
	}
	
	/**
	 * 电子围栏选择列表
	 */
	@RequestMapping(value="getGisRailInfo")
	public String getGisRailInfo(Model model,String areaCodesVal){
		//已选电子围栏配置
		JSONArray selectedGisRailInfo = new JSONArray();
		//未选电子围栏配置
		JSONArray unSelectedGisRailInfo = new JSONArray();
		//所有电子围栏配置
		JSONArray allGisRailInfo = gisConfigService.getGisRailInfo();
		if(areaCodesVal!=null&&!areaCodesVal.equals("")){
			areaCodesVal = ","+areaCodesVal+",";
			//有选中的电子围栏
			for(int i=0;i<allGisRailInfo.size();i++){
				JSONObject gisRailInfo = allGisRailInfo.getJSONObject(i);
				String areaCode = gisRailInfo.getString("areaCode");
				if(areaCodesVal.contains(","+areaCode+",")){
					//已选的
					selectedGisRailInfo.add(gisRailInfo);
				}else{
					//未选的
					unSelectedGisRailInfo.add(gisRailInfo);
				}
			}
		}else{
			//没有选中的电子围栏
			unSelectedGisRailInfo = allGisRailInfo;
		}
		model.addAttribute("unSelectedGisRailInfo", unSelectedGisRailInfo);
       	model.addAttribute("selectedGisRailInfo", selectedGisRailInfo);
		return "prss/rule/areaCodesFilterCheck";
	}
	
	/**
	 * 设置电子围栏、节点配置信息一键生效、失效
	 */
	@ResponseBody
	@RequestMapping(value="setInUse")
	public String setInUse(HttpServletRequest request) {
		String result = "success";
		String inUse = request.getParameter("inUse");
		try{
			gisConfigService.updateGisConfigInUse(inUse);
		} catch (Exception e) {
			result = "err";
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
}
