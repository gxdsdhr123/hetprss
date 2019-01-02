/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月8日 下午3:09:59
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.scheduling.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.nodetime.service.NodeTimeService;
import com.neusoft.prss.scheduling.dao.SchedulingGanttDao;
import com.neusoft.prss.taskmonitor.dao.TaskMonitorDao;
import com.neusoft.prss.taskmonitor.entity.TaskNode.Node;

@Service
@Transactional(readOnly = true)
public class SchedulingGanttService extends BaseService {
	@Autowired
	private SchedulingGanttDao schedulingGanttDao;
	@Autowired
	private JobManageService jobService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	protected TaskMonitorDao taskMonitorDao;
	@Autowired
	private NodeTimeService nodeTimeService;

	public JSONArray getSdYData(Map<String, String> param) {
		String officeId = UserUtils.getUser().getOffice().getId();
		param.put("officeId", officeId);
		//按排班获取人员
		JSONArray result = new JSONArray();
		if("0".equals(param.get("switches"))) {
			result = schedulingGanttDao.getSdYData(param);
		}else if("1".equals(param.get("switches"))) {
			result = schedulingGanttDao.getMonitorYData(param);
		}else if("2".equals(param.get("switches"))) {
			result = schedulingGanttDao.getWthYData(param);
		}
		//获取在线人员
		String onlineStr = "";
		List<String> onlines = new ArrayList<String>();
		try {
			onlines = getOnlineUsers();
			for(int i=0;i<onlines.size();i++) {
				onlineStr += "'"+onlines.get(i)+"'";
				if(i!= onlines.size()-1) {
					onlineStr += ",";
				}
			}
			onlineStr = onlineStr == ""?"''":onlineStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray onlineResult = schedulingGanttDao.getOnlineYData(officeId,onlineStr);
		JSONArray userArr = new JSONArray();
		for(int j=0;j<result.size();j++) {
			JSONObject o = result.getJSONObject(j);
			if(onlines.contains(o.getString("id"))) {
				o.put("online", "1");
			}else {
				o.put("online", "0");
			}
			userArr.add(o.getString("id"));
		}
		for(int k=0;k<onlineResult.size();k++) {
			if(!userArr.contains(onlineResult.getJSONObject(k).getString("id"))) {
				result.add(onlineResult.getJSONObject(k));
			}
		}
		//获取当前处于停用状态的作业人 baochl_20180630
		List<String> blockupList = schedulingGanttDao.getCurrentBlockupWorker(param);
		if(blockupList!=null&&!blockupList.isEmpty()){
			for(int i=0;i<result.size();i++){
				JSONObject item = result.getJSONObject(i);
				if(item!=null&&item.containsKey("id")){
					String workerId = item.getString("id");
					if(blockupList.contains(workerId)){
						//停用状态
						item.put("blockup", "1");
					}
				}
			}
		}
		return result;
	}
	
	public List<String> getOnlineUsers() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		// cp获取设备是否在线的接口url
		String uri = Global.getConfig("cp.onlines.url");
		// 登陆状态token前缀
		String tokenPrefix = Global.getConfig("TOKEN_PREFIX");
		// 登陆状态查询key
		String tokenKey = Global.getConfig("TOKEN_KEY");
		// 请求参数（格式）：{"Tokens":["P_1329","M_3219",.......]};(P:PC、M:手持机)
		JSONObject param = new JSONObject();
		// 取得登录状态map
		Map<String, String> loginMap = cacheService.getMap(tokenKey);
		JSONArray arr = new JSONArray();
		if(loginMap!=null){
			for(Map.Entry<String, String> entry : loginMap.entrySet()) {
				if(entry.getKey().startsWith(tokenPrefix)) {
					arr.add(entry.getKey().replace(tokenPrefix, "M_"));
				}
			}
		}
		param.put("Tokens", arr);
		// 调用查询接口（返回格式：{"onlines":["P_1329","M_3219"]}）
		JSONObject onlines = restTemplate.postForObject(uri, param,com.alibaba.fastjson.JSONObject.class);
		List<String> response = new ArrayList<String>();
		if(onlines.containsKey("onlines")) {
			JSONArray userList = onlines.getJSONArray("onlines");
			for(int i=0;i<userList.size();i++) {
				response.add(userList.getString(i).replace("P_", "").replaceAll("M_", ""));
			}
		}
		// 返回在线人员列表（格式：["1329","3219"]）
		return response;
	}

	public String ganttData(Map<String, Object> params) {
		JSONArray result = new JSONArray();
		List<JobKind> jobKind = UserUtils.getCurrentJobKind();
		String jobKinds = "";
		for(int i=0;i<jobKind.size();i++){
			jobKinds += "'"+jobKind.get(i).getKindCode()+"',";
		}
		jobKinds += "'gaojd'";//防止jobkind为空sql报错
		params.put("jobKinds", jobKinds);
		JSONArray res = new JSONArray();
		if(!UserUtils.getCurrentJobKind().isEmpty() && "JWQCSBZ".equals(UserUtils.getCurrentJobKind().get(0).getKindCode())){
			res = schedulingGanttDao.getSdQCSBData(params);
		}else if(!UserUtils.getCurrentJobKind().isEmpty() && "JWQCCZZ".equals(UserUtils.getCurrentJobKind().get(0).getKindCode())) {
			res = schedulingGanttDao.getSdQCCZData(params);
		}else{
			res = schedulingGanttDao.getSdData(params);
		}
		for(int i=0;i<res.size();i++){
			JSONObject r = res.getJSONObject(i);
			JSONObject json = new JSONObject();
			json.put("id", r.getString("id"));
			json.put("proc", r.getString("proc"));
			json.put("fltid",r.getString("fltid"));
			json.put("in_fltid", r.getString("in_fltid"));
			json.put("out_fltid", r.getString("out_fltid"));
			json.put("status",r.getString("fltStatus"));
			json.put("type",r.getString("type"));
			json.put("field", r.getString("operator"));
			json.put("text",r.getString("text"));
			json.put("start", r.getString("start"));
			json.put("estart", r.getString("estart"));
			json.put("eend", r.getString("eend"));
			json.put("end", r.getString("end"));
			json.put("color", r.getString("status"));
			json.put("icon", r.getString("icon"));
			json.put("order_id", r.getString("order_id"));
			json.put("ptId", r.getString("ptId"));
			result.add(json);
		}
		String jsonStr = JSON.toJSONString(result).replaceAll("null", "");
		return jsonStr;
	}
	
	@Transactional(readOnly = false)
	public void cancleTask(String id) {
		schedulingGanttDao.cancleTask(id);
		try {
			String user = UserUtils.getUser().getId();
			schedulingGanttDao.logCancleTask(id,user);
		} catch (Exception e) {
			logger.error("删除任务日志记录错误",e);
		}
	}
	
	@Transactional(readOnly = false)
	public String allocationTask(String targetActor, String jobTaskId,String procId) {
		// 检查信号量一致性
		Map<String, String> params = getJmSemaState("1");
		if (! "1".equals(params.get("flag"))) {
			return "人员信息已发生变化，请刷新后重试！";
		}
		// 检查任务是否被删除
		Integer delFlag = jobService.getTaskIsDel(jobTaskId);
		if(delFlag == 1){
			return "任务已经被删除，不能分配！";
		}
		/*// 检查任务是否被分配
		int operatorCount = schedulingGanttDao.getOperatorCount(targetActor);
		if(operatorCount >= 5){
			return "每个人最多5个待办任务！";
		}*/
		// 检查任务是否被分配
		int instCount = schedulingGanttDao.getTaskCount(jobTaskId);
		if(instCount > 0){
			return "任务已经分配人员，不能重复分配！";
		}
		// 检查是否重复分配任务
		/*int count = schedulingGanttDao.getTaskOperatorCount(jobTaskId, targetActor);
		if(count > 0 ){
			return "任务已经分配给该人员，不能重复分配！";
		}*/
		try {
			// 任务分配
			schedulingGanttDao.allocationTask(jobTaskId,targetActor);
		} catch (Exception e) {
			throw e;
		}finally{
			// 更改信号量
			params.put("flag", "0");
			int num = jobService.updateJmSemaState(params);
			if (num <= 0) {
				return  "人员信息已发生变化，请刷新后重试！";
			}
		}
		return "success";
	}
	
	/**
	 * 获取当前是否可做手动人员分配
	 * 
	 * @param params
	 * @return 0、是、1、否
	 */
	@Transactional(readOnly = false)
	public String setJmSemaState(){
		String result = "系统或其他用户正在进行人员分配，请稍后重试！";
		if(UserUtils.getCurrentJobKind() == null || UserUtils.getCurrentJobKind().isEmpty()){
			return "当前用户没有操作权限！";
		}
		Map<String, String> params = getJmSemaState("0");
		if (params.containsKey("flag") && "0".equals(params.get("flag"))) {
			params.put("flag", "1");
			int num = jobService.updateJmSemaState(params);
			if (num > 0) {
				result = "success";
			}
		}
		return result;
	}
	
	/**
	 * 信号量检查
	 * @param state
	 * @return
	 */
	private Map<String, String> getJmSemaState(String state) {
		Map<String, String> params = new HashMap<String, String>();
		// 当前登录人部门
		params.put("officeId", UserUtils.getUser().getOffice().getId());
		// 当前登录人
		params.put("userId", UserUtils.getUser().getId());
		// 期望返回状态码
		params.put("state", state);
		//查询状态
		params.put("flag", jobService.getJmSemaState(params));
		return params;
	}

	public String getLimitTypes() {
		Map<String,String> param = new HashMap<String,String>();
		param.put("officeId", UserUtils.getUser().getOffice().getId());
		List<Map<String,String>> result = schedulingGanttDao.getLimitTypes(param);
		String res = "";
		for(int i=0;i<result.size();i++){
			res += String.valueOf(result.get(i).get("type"));
			if(i!=result.size()-1){
				res += ",";
			}
		}
		return res;
	}

	public List<List<String>> getGanttDetail(String id, String schemaId) {
		List<List<String>> detail = new ArrayList<List<String>>();
		List<String> page1 = new ArrayList<String>();
		JSONObject r = schedulingGanttDao.getGanttInfo(id);
		page1.add(cn(r.getString("taskName"))+"   "+cn(r.getString("fltNum")));
		page1.add("机号："+cn(r.getString("actNum"))+"   机型："+cn(r.getString("actType"))+"   机位："+cn(r.getString("stand")));
		page1.add("STA："+cn(r.getString("sta"))+"   ETA："+cn(r.getString("eta"))+"   ATA："+cn(r.getString("ata")));
		page1.add("STD："+cn(r.getString("std"))+"   ETD："+cn(r.getString("etd"))+"   ATD："+cn(r.getString("atd")));
		page1.add("进港要客："+cn(r.getString("inVip"))+"   出港要客："+cn(r.getString("outVip")));
		if(StringUtils.isNotBlank(schemaId) && "8".equals(schemaId)) {
			page1.add("登机口："+cn(r.getString("outGate"))+"   到达口："+cn(r.getString("inGate"))+"	   车号："+cn(r.getString("carNum")));
		}
		detail.add(page1);
		List<String> page2 = new ArrayList<String>();
		//JSONArray arr = schedulingGanttDao.getGanttNodeTime(id);
		List<Node> arr = getTaskNodes(id);
		for(int i=0;i<arr.size();i++) {
			page2.add("预计"+cn(arr.get(i).getName())+":"+cn(arr.get(i).geteVal())+"   实际"+cn(arr.get(i).getName())+":"+cn(arr.get(i).getaVal()));
		}
		detail.add(page2);
		return detail;
	}
	
	/**
	 * 根据任务ID查节点
	 * @param taskId
	 * @return
	 */
	protected List<Node> getTaskNodes(String taskId) {
		// 查询预计时间
		List<Node> nodes = taskMonitorDao.getNodeIds(taskId);
		
		// 查询预计时间
		List<Node> nodeVals = taskMonitorDao.getNodeVal(taskId);
		Map<String, String> valMap = new HashMap<String, String>();
		for(Node node : nodeVals){
			valMap.put(node.getId().toString(), node.getaVal());
		}
		
		for(Node node : nodes){
			String nodeId = node.getId().toString();
			String eVal = DateUtils.formatToFltDate(nodeTimeService.getNodeTime(taskId,nodeId));
			String aVal = valMap.get(nodeId);
			
			node.seteVal(eVal);
			node.setaVal(aVal);
		}
		return nodes;
	}
	
	private String cn(String s) {
		if("null".equals(s) || s == null) {
			return "-";
		}else {
			return s;
		}
	}
	
	private boolean gt(String t1,String t2) {
		if("--".equals(t2)) {
			return false;
		}
		if("--".equals(t1)) {
			return true;
		}
		if(t1.contains("(") && !t2.contains("(")) {
			return false;
		}else if(!t1.contains("(") && t2.contains("(")) {
			return true;
		}else if(t1.contains("(") && t2.contains("(")) {
			if(Integer.parseInt(t1.substring(1, 3)) > Integer.parseInt(t2.substring(1, 3))) {
				return true;
			}else if (Integer.parseInt(t1.substring(1, 3)) < Integer.parseInt(t2.substring(1, 3))) {
				return false;
			}else {
				if(Integer.parseInt(t1.substring(4)) > Integer.parseInt(t2.substring(4))) {
					return true;
				}else {
					return false;
				}
			}
		}else {
			if(Integer.parseInt(t1) > Integer.parseInt(t2)) {
				return true;
			}else {
				return false;
			}
		}
	}

	public JSONArray getMembers(String id) {
		return schedulingGanttDao.getMembers(id);
	}

	public JSONArray getResGanttArea() {
		return schedulingGanttDao.getResGanttArea();
	}

	@Transactional(readOnly = false)
	public void saveResGanttArea(JSONObject param) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", param.getString("name"));
		params.put("type", param.getString("type"));
		params.put("officeId", UserUtils.getUser().getOffice().getId());
		params.put("user", UserUtils.getUser().getId());
		if(StringUtils.isNotEmpty(param.getString("id"))) {
			params.put("areaId", param.getString("id"));
			schedulingGanttDao.updateResGanttArea(params);
			schedulingGanttDao.deleteResGanttAreaEle(param.getString("id"));
		}else {
			schedulingGanttDao.saveResGanttArea(params);
		}
		Map<String,Object> datas = new HashMap<String,Object>();
		datas.put("id", params.get("areaId"));
		datas.put("data", param.getJSONArray("data"));
		schedulingGanttDao.saveResGanttAreaEle(datas);
	}

	public JSONArray getAllJW() {
		return schedulingGanttDao.getAllJW();
	}

	public JSONArray getJWById(String id) {
		return schedulingGanttDao.getJWById(id);
	}

	public JSONArray getJXById(String id) {
		return schedulingGanttDao.getJXById(id);
	}

	public JSONArray getAllJX() {
		return schedulingGanttDao.getAllJX();
	}
	
	public JSONArray getJXExceptId(String id) {
		return schedulingGanttDao.getJXExceptId(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteResArea(String id) {
		schedulingGanttDao.deleteResGanttArea(id);
		schedulingGanttDao.deleteResGanttAreaEle(id);
	}

	public JSONArray getResGanttSet() {
		return schedulingGanttDao.getResGanttSet();
	}

	public JSONArray getAreaByNum(String num) {
		String standsStr =  schedulingGanttDao.getAreaByNum(num);
		String stands = "'"+standsStr.replaceAll(",", "','")+"'";
		return schedulingGanttDao.getAreaByStands(stands);
	}
	
	public JSONArray getDefAreaByNum(String num) {
		String standsStr =  schedulingGanttDao.getDefAreaByNum(num);
		String stands = "'"+standsStr.replaceAll(",", "','")+"'";
		return schedulingGanttDao.getAreaByStands(stands);
	}

	public JSONObject getAreaById(String id, String num) {
		JSONObject res = schedulingGanttDao.getAreaInfoByNum(num);
		JSONArray atree = schedulingGanttDao.getJWById(id);
		res.put("atree", atree);
		return res;
	}

	public JSONArray getAreaByType(String type) {
		return schedulingGanttDao.getAreaByType(type);
	}

	@Transactional(readOnly = false)
	public void saveResSet(JSONObject param) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("num", param.getString("num"));
		params.put("user", UserUtils.getUser().getId());
		if(StringUtils.isNotEmpty(param.getString("area"))) {
			params.put("area", param.getString("area"));
		}
		if(StringUtils.isNotEmpty(param.getString("actype"))) {
			params.put("actype", param.getString("actype"));
		}
		params.put("nodeList", param.getJSONArray("nodeList").toString().replaceAll("\"", "").replace("[", "").replace("]", ""));
		int n = schedulingGanttDao.updateResSet(params);
		if(n == 0){
			schedulingGanttDao.saveResSet(params);
		}
	}
	
	@Transactional(readOnly = false)
	public String saveDefResSet(JSONObject param) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("num", param.getString("num"));
		params.put("user", UserUtils.getUser().getId());
		if(StringUtils.isNotEmpty(param.getString("area"))) {
			params.put("areaDef", param.getString("area"));
		}
		params.put("nodeListDef", param.getJSONArray("nodeList").toString().replaceAll("\"", "").replace("[", "").replace("]", ""));
		int n = schedulingGanttDao.updateResSet(params);
		if(n == 0){
			return "修改默认机位区域失败，请先设置车辆机位、机型等信息";
		}
		return "success";
	}

	public JSONArray getResGanttYData() {
		return schedulingGanttDao.getResGanttYData();
	}

	public String getResGanttData(Map<String, String> params) {
		JSONArray res = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("inFltId", "001");
		json.put("outFltId", "002");
		json.put("inFltnum", "CZXXXX");
		json.put("outFltnum", "CZXXXX");
		json.put("actType", "733");
		json.put("actNum", "BXXXX");
		json.put("kTaskId", "1001");
		json.put("cTaskId", "1002");
		json.put("startTm", "2018-02-10 07:00:00");
		json.put("endTm", "2018-02-10 07:30:00");
		json.put("ktcNo", "A16");
		json.put("stand", "806");
		json.put("areaId", "1");
		json.put("ifSameArea", "1");
		json.put("ifError", "1");
		json.put("actKtcNo", "");
		res.add(json);
		return res.toJSONString();
	}

	@Transactional(readOnly = false)
	public String stopVehicle(String id, String reason) {
		int c = schedulingGanttDao.getVehicleFltCount(id);
		if( c > 0 ){
			return "该客梯车正在保障中，无法停用！";
		}
		schedulingGanttDao.stopVehicle(id,reason);
		return null;
	}

	@Transactional(readOnly = false)
	public void resumeVehicle(String id) {
		schedulingGanttDao.resumeVehicle(id);
	}

	@Transactional(readOnly = false)
	public void updateResCarArea(Map<String, String> params) {
		JSONArray stands = schedulingGanttDao.getStandsByArea(params.get("area"));
		String standsStr = "";
		for(int i=0;i<stands.size();i++) {
			standsStr += ","+stands.getJSONObject(i).getString("stand");
		}
		params.put("stands", standsStr.substring(1));
		schedulingGanttDao.updateResCarArea(params);
		if(params.containsKey("tasks")) {
			schedulingGanttDao.releaseTasks(params.get("tasks"));
		}
	}

	public JSONArray manualSelectCars(String actType,String stand) {
		JSONArray res = new JSONArray();
		JSONArray cars = schedulingGanttDao.manualSelectCars(actType,stand);
		for(int i=0;i<cars.size();i++) {
			res.add(cars.getJSONObject(i).getString("cars"));
		}
		return res;
	}
	
	@Transactional(readOnly = false)
	public void changeCar(Map<String, String> params) {
		JSONArray check = schedulingGanttDao.checkCar(params);
		if(check.size() == 0) {
			schedulingGanttDao.insertCar(params.get("kTaskId"),params.get("car"));
			schedulingGanttDao.insertCar(params.get("cTaskId"),params.get("car"));
		}
		schedulingGanttDao.changeCar(params);
	}
	
	@Transactional(readOnly = false)
	public void releaseTasks(Map<String, String> params) {
		schedulingGanttDao.releaseTasks(params.get("tasks"));
	}
	
	@Transactional(readOnly = false)
	public void saveResDefaultTimes(Map<String, String> params) {
		schedulingGanttDao.saveResDefaultStartTime(params.get("start"));
		schedulingGanttDao.saveResDefaultEndTime(params.get("end"));
	}

	public JSONObject getResDefaultTimes() {
		JSONObject res = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = sdf.format(date);
		JSONArray arr = schedulingGanttDao.getResDefaultTimes();
		for(int i=0;i<arr.size();i++) {
			if("resStart".equals(arr.getJSONObject(i).getString("type"))) {
				res.put("start", today+" "+arr.getJSONObject(i).getString("time"));
			}else if("resEnd".equals(arr.getJSONObject(i).getString("type"))) {
				res.put("end", today+" "+arr.getJSONObject(i).getString("time"));
			}
		}
		return res;
	}

	public void truncateTasks(Map<String, String> params) {
		schedulingGanttDao.truncateTasks(params);
	}

	public void resetWithoutTask(Map<String, String> params) {
		schedulingGanttDao.resetWithoutTask(params);
	}

	public void resetWithTask(Map<String, String> params) {
		schedulingGanttDao.resetWithoutTask(params);
		schedulingGanttDao.releaseTasks(params.get("tasks"));
	}

	public String getReskind() {
		String officeId = UserUtils.getUser().getOffice().getId();
		return schedulingGanttDao.getReskind(officeId);
	}

	public JSONArray getWthYData(Map<String, String> param) {
		return schedulingGanttDao.getWthYData(param);
	}

	public JSONArray ganttWthData(Map<String, Object> param) {
		return schedulingGanttDao.ganttWthData(param);
	}

	public String wthAllocationTask(String targetActor, String jobTaskId) {
		// 检查信号量一致性
		Map<String, String> params = getJmSemaState("1");
		if (! "1".equals(params.get("flag"))) {
			return "人员信息已发生变化，请刷新后重试！";
		}
		try {
			// 任务分配
			schedulingGanttDao.allocationTask(jobTaskId,targetActor);
		} catch (Exception e) {
			throw e;
		}finally{
			// 更改信号量
			params.put("flag", "0");
			int num = jobService.updateJmSemaState(params);
			if (num <= 0) {
				return  "人员信息已发生变化，请刷新后重试！";
			}
		}
		return "success";
	}

	public String wthResetTask(String jobTaskId) {
		// 检查信号量一致性
		Map<String, String> params = getJmSemaState("1");
		if (! "1".equals(params.get("flag"))) {
			return "人员信息已发生变化，请刷新后重试！";
		}
		try {
			// 任务分配
			schedulingGanttDao.wthResetTask(jobTaskId);
		} catch (Exception e) {
			throw e;
		}finally{
			// 更改信号量
			params.put("flag", "0");
			int num = jobService.updateJmSemaState(params);
			if (num <= 0) {
				return  "人员信息已发生变化，请刷新后重试！";
			}
		}
		return "success";
	}

	public void restoreTask(String jobTaskId) {
		schedulingGanttDao.restoreTask(jobTaskId);
	}
}
