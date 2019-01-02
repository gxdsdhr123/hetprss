/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月8日 下午3:07:44
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.scheduling.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.neusoft.prss.arrange.service.ArrangeService;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.prearrange.service.PrearrangeService;
import com.neusoft.prss.scheduling.service.JobManageService;
import com.neusoft.prss.scheduling.service.SchedulingGanttService;
import com.neusoft.prss.workflow.service.OrderService;
import com.neusoft.prss.workflow.service.TaskService;
import com.neusoft.prss.workflow.service.WFBtnEventService;

@Controller
@RequestMapping(value = "${adminPath}/scheduling/gantt")
public class SchedulingGanttController  extends BaseController{
	
	@Autowired
	private SchedulingGanttService schedulingGanttService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ArrangeService arrangeService;
	@Autowired
	private JobManageService jobService;
	@Autowired
	private WFBtnEventService wfBtnService;// 流程按钮
	@Autowired
	private PrearrangeService prearrangeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private WFBtnEventService btnService;
	@Autowired
	private OrderService orderService;
	
	/**
	 * 
	 *Discription:返回指挥调度（甘特图）页面.
	 *@param model
	 *@return
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年11月8日 SunJ [变更描述]
	 */
	@RequestMapping(value = { "listGantt", "" })
	public String listGantt(Model model, String schemaId) {
		String limitTypes = schedulingGanttService.getLimitTypes();
		model.addAttribute("types", limitTypes);
		model.addAttribute("schemaId", schemaId);
		String reskind = schedulingGanttService.getReskind();
		model.addAttribute("reskind", reskind);
		dimAttributes(model);
		return "prss/scheduling/schedulingGantt";
    }
	
	/**
	 * 返回指挥调度预排（甘特图）页面.
	 * @param model
	 * @param schemaId
	 * @return
	 */
	@RequestMapping(value = { "listWthGantt" })
	public String listWthGantt(Model model, String schemaId) {
		String reskind = schedulingGanttService.getReskind();
		model.addAttribute("reskind", reskind);
		model.addAttribute("schemaId", schemaId);
		dimAttributes(model);
		return "prss/scheduling/schedulingWthGantt";
    }
	
	/**
	 * 返回客梯车资源（早高峰）甘特图页面
	 * @param model
	 * @param schemaId
	 * @return
	 */
	@RequestMapping(value = { "listResourceGantt" })
	public String listResourceGantt(Model model,String schemaId) {
		JSONObject times = schedulingGanttService.getResDefaultTimes();
		model.addAttribute("start",times.getString("start"));
		model.addAttribute("end",times.getString("end"));
		model.addAttribute("schemaId",schemaId);
		return "prss/scheduling/resourceGanttOfKTC";
	}
	
	/**
	 * 返回客梯车资源（早高峰）甘特图区域信息页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "listResourceGanttArea" })
	public String listResourceGanttArea(Model model) {
		return "prss/scheduling/schedulingArea";
	}
	
	/**
	 * 返回客梯车资源（早高峰）甘特图区域配置页面
	 * @param model
	 * @param id
	 * @param name
	 * @param type
	 * @return
	 */
	@RequestMapping(value = { "listResOperationArea" })
	public String listResOperationArea(Model model,String id,String name,String type) {
		name = StringEscapeUtils.unescapeHtml4(name);
		if(type == null) {
			type = "0";
		}
		model.addAttribute("id",id);
		model.addAttribute("name",name);
		model.addAttribute("type",type);
		return "prss/scheduling/schedulingAreaOperation";
	}
	
	/**
	 * 返回客梯车资源（早高峰）甘特图车辆配置页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "listResourceGanttSet" })
	public String listResourceGanttSet(Model model) {
		JSONArray areas = schedulingGanttService.getAreaByType("0");
		JSONArray actypes = schedulingGanttService.getAreaByType("1");
		model.addAttribute("areas",areas);
		model.addAttribute("actypes", actypes);
		return "prss/scheduling/schedulingResSet";
	}
	
	/**
	 * 
	 *Discription:获取摆渡车甘特图Y轴数据（分工、姓名）.
	 *@return
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年11月9日 SunJ [变更描述]
	 */
	@RequestMapping(value = "ganttYData")
	@ResponseBody
	public String ganttYData(String type,String switches) {
		Map<String,String> param = new HashMap<String,String>();
		param.put("type", type);
		param.put("switches", switches);
		JSONArray arr = schedulingGanttService.getSdYData(param);
		return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
	}
	
	/**
	 * 获取指挥调度预排甘特图Y轴数据
	 * @param reskind
	 * @return
	 */
	@RequestMapping(value = "ganttWthYData")
	@ResponseBody
	public String ganttWthYData() {
		Map<String,String> param = new HashMap<String,String>();
		param.put("officeId", UserUtils.getUser().getOffice().getId());
		JSONArray arr = schedulingGanttService.getWthYData(param);
		return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
	}
	
	/**
	 * 
	 *Discription:获取摆渡车甘特图数据.
	 *@param param
	 *@return
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年11月9日 SunJ [变更描述]
	 */
	@RequestMapping(value = "ganttData")
	@ResponseBody
	public String ganttData(String param) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (param != null) {
			param = StringEscapeUtils.unescapeHtml4(param);
			params = JSON.parseObject(param);
		}
		String arr = schedulingGanttService.ganttData(params);
		return arr;
	}
	
	/**
	 * 获取指挥调度预排甘特图数据
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "ganttWthData")
	@ResponseBody
	public String ganttWthData(String reskind) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("reskind", reskind);
		JSONArray arr = schedulingGanttService.ganttWthData(param);
		return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
	}
	/**
	 * 获取甘特条详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getGanttDetail")
    @ResponseBody
    public List<List<String>> getGanttDetail(String id, String schemaId) {
        List<List<String>> detail = schedulingGanttService.getGanttDetail(id,schemaId);
        return detail;
    }
	
	/**
	 * 根据人员id获取全部同班组组员
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getMembers")
    @ResponseBody
    public JSONArray getMembers(String id) {
        return schedulingGanttService.getMembers(id);
    }
	
	/**
	 * 
	 *Discription:取消任务.
	 *@param id
	 *@return
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年12月5日 SunJ [变更描述]
	 */
	@RequestMapping(value = "cancleTask")
	@ResponseBody
	public String cancleTask(String id) {
		try {
			schedulingGanttService.cancleTask(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 
	 *Discription:分配任务.
	 *@param targetActor
	 *@param jobTaskId
	 *@param procId
	 *@return
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年12月5日 SunJ [变更描述]
	 */
	@RequestMapping(value = "allocationTask")
	@ResponseBody
	public String allocationTask(String targetActor,String jobTaskId,String procId) {
		try {
			String msg = schedulingGanttService.allocationTask(targetActor,jobTaskId,procId);
			if("success".equals(msg)){
				wfBtnService.startInstance(jobTaskId, procId, UserUtils.getUser().getId(),
						targetActor, null);
			}
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统忙，任务分配失败，请稍后再试！";
		}
	}
	
	@RequestMapping(value = "restoreTask")
	@ResponseBody
	public String restoreTask(HttpServletRequest request) {
		String result = "succeed";
		String jobTaskId = request.getParameter("jobTaskId");
		JSONObject json = taskService.getFlowTaskByJob(jobTaskId);
		if (json.containsKey("taskId") && json.containsKey("actor") && StringUtils.isNotEmpty(json.getString("taskId"))
				&& StringUtils.isNotEmpty(json.getString("actor"))) {
			Map<String, String> args = getAllParameter(request);
			Set<String> keys = json.keySet();
			for (String key : keys) {
				args.put(key, json.getString(key));
			}
			args.put("userId", UserUtils.getUser().getId());
			btnService.termination(json.getString("taskId"), json.getString("actor"), args);
			orderService.cascadeRemove(request.getParameter("orderId"));
			schedulingGanttService.restoreTask(jobTaskId);
		} else {
			result = "处理失败！为找活动任务或处理人。";
		}
		return result;
	}
	
	/**
	 * 任务预排甘特图分配人员
	 * @param targetActor
	 * @param jobTaskId
	 * @return
	 */
	@RequestMapping(value = "wthAllocationTask")
	@ResponseBody
	public String wthAllocationTask(String targetActor,String jobTaskId) {
		try {
			String msg = schedulingGanttService.wthAllocationTask(targetActor,jobTaskId);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统忙，任务分配失败，请稍后再试！";
		}
	}
	
	/**
	 * 任务预排还原任务
	 * @param jobTaskId
	 * @return
	 */
	@RequestMapping(value = "wthResetTask")
	@ResponseBody
	public String wthResetTask(String jobTaskId) {
		try {
			String msg = schedulingGanttService.wthResetTask(jobTaskId);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统忙，任务分配失败，请稍后再试！";
		}
	}
	
	/**
	 * 任务预排 自动分配任务
	 * @param start
	 * @param end
	 * @param reskind
	 * @return
	 */
	@RequestMapping(value = "walkthroughTask")
	@ResponseBody
	public String walkthroughTask(String start, String end, String reskind) {
		try {
			String msg = prearrangeService.doDispatch(start, end, reskind);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统忙，任务分配失败，请稍后再试！";
		}
	}
	
	/**
	 * 保存任务预排结果（启动实例）
	 * @return
	 */
	@RequestMapping(value = "saveWalkthrough")
	@ResponseBody
	public String saveWalkthrough(String reskind) {
		try {
			String msg = prearrangeService.startFlow(reskind);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统忙，任务分配失败，请稍后再试！";
		}
	}
	
	/**
	 * 获取当前是否可做手动人员分配
	 * 
	 * @param params
	 * @return 0、是、1、否
	 */
	@ResponseBody
	@RequestMapping(value = "setJmSemaState")
	public String setJmSemaState() {
		try {
			return schedulingGanttService.setJmSemaState();
		} catch (Exception e) {
			e.printStackTrace();
			return "服务器发生异常，请稍后重试！";
		}
	}
	
	/**
	 * 检查任务调度信号量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkJmSemaState")
	public String checkJmSemaState() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("officeId", UserUtils.getUser().getOffice().getId());
		params.put("userId", UserUtils.getUser().getId());
		params.put("state", "1");
		String param = jobService.getJmSemaState(params);
		if("1".equals(param)){
			return "success";
		}else{
			return "您操作时间过长人员信息已发生变化，请重新选择！";
		}
	}
	
	/**
	 * 还原任务调度信号量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "recoverJmSemaState")
	public String recoverJmSemaState() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("officeId", UserUtils.getUser().getOffice().getId());
		params.put("userId", UserUtils.getUser().getId());
		params.put("flag", "0");
		try {
			jobService.updateJmSemaState(params);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 
	 *Discription:手动选择司机.
	 * @param fltid 航班id
     * @param job_type 作业类型
     * @param startTm  预计任务开始时间
     * @param endTm   预计任务结束时间
	 *@return:司机列表
	 *@author:SunJ
	 *@update:2017年11月10日 SunJ [变更描述]
	 */
	@RequestMapping(value = "manualSelectDriver")
	@ResponseBody
	public String manualSelectDriver(String fltId,String jobType,String startTm,String endTm){
		JSONObject json = new JSONObject();
		json.put("fltid", fltId);
		json.put("job_type", jobType);
		json.put("startTm",startTm);
		json.put("endTm",endTm);
		JSONArray jsonArr = new JSONArray();
		try {
			jsonArr = JSONArray.parseArray(arrangeService.manualSelectDriver(json.toJSONString(),UserUtils.getCurrentJobKind().get(0).getKindCode()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray arr = new JSONArray();
		for(int i=0;i<jsonArr.size();i++){
			arr.add(jsonArr.getJSONObject(i).getString("workerId"));
		}
		return arr.toString();
	}
	
	/**
	 * 
	 *Discription:获取筛选条件中下拉选项.
	 *@param model
	 *@return:返回值意义
	 *@author:SunJ
	 *@update:2017年11月8日 SunJ [变更描述]
	 */
	private void dimAttributes(Model model) {
		try {
			// 性质
			JSONArray fltPropertys = cacheService.getList("dim_task");
			model.addAttribute("fltPropertys", fltPropertys);
			// 外航/国内
			JSONArray alnFlags = cacheService.getCommonDict("airline_flag");
			model.addAttribute("alnFlags", alnFlags);
			// 是否为宽体机型
			JSONArray acttypeSizes = cacheService.getCommonDict("aln_flag");
			model.addAttribute("acttypeSizes", acttypeSizes);
			// 航站楼
			JSONArray terminals = cacheService.getCommonDict("terminal");
			model.addAttribute("terminals", terminals);
			// 航班范围
			JSONArray flightScopes = cacheService.getCommonDict("flight_scope");
			model.addAttribute("flightScopes", flightScopes);
			// 机坪区域
			JSONArray aprons = cacheService.getList("dim_bay_apron");
			model.addAttribute("aprons", aprons);
			// 廊桥位、外围航班
			JSONArray GAFlag = cacheService.getCommonDict("GAFlag");
			model.addAttribute("GAFlag", GAFlag);
			// 标识
			JSONArray identifying = cacheService.getCommonDict("identifying");
			model.addAttribute("identifying", identifying);
			// 状态
			JSONArray actStatus = cacheService.getCommonDict("acfStatus");
			model.addAttribute("actStatus", actStatus);
		} catch (Exception e) {
			logger.error("添加下拉框选项失败" + e.getMessage());
		}
	}
	/**
	 * 获取客梯车保障区域列表
	 * @return
	 */
	@RequestMapping(value = "getResGanttArea")
	@ResponseBody
	public JSONArray getResGanttArea(){
		return schedulingGanttService.getResGanttArea();
	}
	/**
	 * 获取所有未被选择过的机位
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getAllJW")
	@ResponseBody
	public JSONObject getAllJW(String id){
		JSONObject result = new JSONObject();
		JSONArray stree = new JSONArray();
		if(StringUtils.isNoneEmpty(id)) {
			stree = schedulingGanttService.getJWById(id);
		}
		JSONArray json = schedulingGanttService.getAllJW();
		result.put("atree", json);
		result.put("stree", stree);
		return result;
	}
	/**
	 * 获取全部机型
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getAllJX")
	@ResponseBody
	public JSONObject getAllJX(String id){
		JSONObject result = new JSONObject();
		JSONArray stree = new JSONArray();
		JSONArray json = new JSONArray();
		if(StringUtils.isNoneEmpty(id)) {
			stree = schedulingGanttService.getJXById(id);
			json = schedulingGanttService.getJXExceptId(id);
		}else {
			json = schedulingGanttService.getAllJX();
		}
		result.put("atree", json);
		result.put("stree", stree);
		return result;
	}
	/**
	 * 保存客梯车保障区域
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "saveResGanttArea")
	@ResponseBody
	public String saveResGanttArea(String data){
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONObject param = JSONObject.parseObject(data);
		try {
			schedulingGanttService.saveResGanttArea(param);
		} catch (Exception e) {
			logger.error("车辆保障区域保存出错：",e);
			return e.getMessage();
		}
		return "success";
	}
	/**
	 * 删除客梯车保障区域
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteResArea")
	@ResponseBody
	public String deleteResArea(String id){
		try {
			schedulingGanttService.deleteResArea(id);
		} catch (Exception e) {
			logger.error("车辆保障区域删除出错：",e);
			return e.getMessage();
		}
		return "success";
	}
	/**
	 * 获取客梯车保障区域设置列表
	 * @return
	 */
	@RequestMapping(value = "getResGanttSet")
	@ResponseBody
	public JSONArray getResGanttSet(){
		return schedulingGanttService.getResGanttSet();
	}
	/**
	 * 根据车辆自编号获取车辆可保障的机位
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "getAreaByNum")
	@ResponseBody
	public JSONArray getAreaByNum(String num){
		return schedulingGanttService.getAreaByNum(num);
	}
	/**
	 * 根据车辆自编号获取车辆可保障的机位
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "getDefAreaByNum")
	@ResponseBody
	public JSONArray getDefAreaByNum(String num){
		return schedulingGanttService.getDefAreaByNum(num);
	}
	/**
	 * 获取车辆保障全量区域和已选区域
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "getAreaById")
	@ResponseBody
	public JSONObject getAreaById(String id,String num){
		return schedulingGanttService.getAreaById(id,num);
	}
	/**
	 * 根据区域id获取机位
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getJWById")
	@ResponseBody
	public JSONArray getJWById(String id){
		return schedulingGanttService.getJWById(id);
	}
	
	/**
	 * 保存客梯车资源设置
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "saveResSet")
	@ResponseBody
	public String saveResSet(String data){
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONObject param = JSONObject.parseObject(data);
		try {
			schedulingGanttService.saveResSet(param);
		} catch (Exception e) {
			logger.error("车辆保障设置保存出错：",e);
			return e.getMessage();
		}
		return "success";
	}
	
	/**
	 * 保存默认客梯车资源设置
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "saveDefResSet")
	@ResponseBody
	public String saveDefResSet(String data){
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONObject param = JSONObject.parseObject(data);
		try {
			return schedulingGanttService.saveDefResSet(param);
		} catch (Exception e) {
			logger.error("车辆保障设置保存出错：",e);
			return "保存失败";
		}
	}
	
	/**
	 * 获取客梯车资源甘特图区域及车辆信息
	 * @return
	 */
	@RequestMapping(value = "getResGanttYData")
	@ResponseBody
	public JSONArray getResGanttYData(){
		return schedulingGanttService.getResGanttYData();
	}
	
	/**
	 * 客梯车资源甘特图，释放任务
	 * @param start
	 * @param end
	 * @return
	 */
	@RequestMapping(value = "truncateTasks")
	@ResponseBody
	public String truncateTasks(String start,String end) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("start", start);
		params.put("end", end);
		try {
			schedulingGanttService.truncateTasks(params);
		} catch (Exception e) {
			logger.error("任务释放出错：",e);
			return e.getMessage();
		}
		return "success";
	}
	
	/**
	 * 客梯车资源甘特图，更新车辆区域
	 * @param area
	 * @param num
	 * @param tasks
	 * @return
	 */
	@RequestMapping(value = "updateResCarArea")
	@ResponseBody
	public String updateResCarArea(String area,String num,String tasks) {
		String msg = "success";
		Map<String, String> params = new HashMap<String, String>();
		params.put("area", area);
		params.put("num", num);
		if(StringUtils.isNotEmpty(tasks)) {
			params.put("tasks", tasks);
		}
		try {
			schedulingGanttService.updateResCarArea(params);
		} catch (Exception e) {
			logger.error("客梯车变更区域失败：",e);
			return e.getMessage();
		}
		return msg;
	}
	
	/**
	 * 客梯车资源甘特图，获取任务可分配车辆
	 * @param actType
	 * @param stand
	 * @return
	 */
	@RequestMapping(value = "manualSelectCars")
	@ResponseBody
	public JSONArray manualSelectCars(String actType,String stand){
		return schedulingGanttService.manualSelectCars(actType,stand);
	}
	
	/**
	 * 客梯车资源甘特图，任务改变车辆
	 * @param kTaskId
	 * @param cTaskId
	 * @param car
	 * @return
	 */
	@RequestMapping(value = "changeCar")
	@ResponseBody
	public String changeCar(String kTaskId,String cTaskId,String car) {
		String msg = "success";
		Map<String, String> params = new HashMap<String, String>();
		params.put("kTaskId", kTaskId);
		params.put("cTaskId", cTaskId);
		params.put("car", car);
		try {
			schedulingGanttService.changeCar(params);
		} catch (Exception e) {
			logger.error("客梯车任务变更车辆失败：",e);
			return e.getMessage();
		}
		return msg;
	}
	
	/**
	 * 客梯车资源甘特图，客梯车任务释放
	 * @param tasks
	 * @return
	 */
	@RequestMapping(value = "releaseTasks")
	@ResponseBody
	public String releaseTasks(String tasks) {
		String msg = "success";
		Map<String, String> params = new HashMap<String, String>();
		params.put("tasks", tasks);
		try {
			schedulingGanttService.releaseTasks(params);
		} catch (Exception e) {
			logger.error("客梯车任务释放失败：",e);
			return e.getMessage();
		}
		return msg;
	}
	
	/**
	 * 客梯车资源甘特图，客梯车车辆恢复原区域（任务跟随）
	 * @param id
	 * @param tasks
	 * @return
	 */
	@RequestMapping(value = "resetWithTask")
	@ResponseBody
	public String resetWithTask(String id,String tasks) {
		String msg = "success";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("tasks", tasks);
		try {
			schedulingGanttService.resetWithTask(params);
		} catch (Exception e) {
			logger.error("客梯车任务恢复失败：",e);
			return e.getMessage();
		}
		return msg;
	}
	
	/**
	 * 客梯车资源甘特图，客梯车车辆恢复原区域（任务不跟随）
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "resetWithoutTask")
	@ResponseBody
	public String resetWithoutTask(String id) {
		String msg = "success";
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		try {
			schedulingGanttService.resetWithoutTask(params);
		} catch (Exception e) {
			logger.error("客梯车任务恢复失败：",e);
			return e.getMessage();
		}
		return msg;
	}
	
	/*---------------客梯车操作----------------*/
	
	/**
	 * 客梯车停用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "stopVehicle")
	public ResponseVO<String> stopVehicle(String id,String reason) {
		try {
			String msg = schedulingGanttService.stopVehicle(id,reason);
			if(msg == null){
				return ResponseVO.<String>build();
			}else{
				return ResponseVO.<String>error().setMsg(msg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<String>exception();
		}
	}
	
	/**
	 * 加班设置默认结束时间（与当前时间最接近的）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "resumeVehicle")
	public ResponseVO<String> resumeVehicle(String id) {
		try {
			schedulingGanttService.resumeVehicle(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<String>exception();
		}
		return ResponseVO.<String>build();
	}
}
