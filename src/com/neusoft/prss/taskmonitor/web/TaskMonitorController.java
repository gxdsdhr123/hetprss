package com.neusoft.prss.taskmonitor.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.common.entity.JobType;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
import com.neusoft.prss.taskmonitor.entity.TaskInfo;
import com.neusoft.prss.taskmonitor.entity.TaskNode;
import com.neusoft.prss.taskmonitor.service.TaskMonitorService;

/**
 * 任务分配监控图
 * @author xuhw
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/taskmonitor")
public class TaskMonitorController extends BaseController{

	// 机务
	@Resource(name="taskMonitorService5")
	private TaskMonitorService taskMonitorService5;
	// 廊桥
	@Resource(name="taskMonitorService6")
	private TaskMonitorService taskMonitorService6;
	// 特车
	@Resource(name="taskMonitorService7")
	private TaskMonitorService taskMonitorService7;
	// 客舱清洁
	@Resource(name="taskMonitorService9")
	private TaskMonitorService taskMonitorService9;
	// 监装（装卸）
	@Resource(name="taskMonitorService11")
	private TaskMonitorService taskMonitorService11;
	// 旅客服务
	@Resource(name="taskMonitorService13")
	private TaskMonitorService taskMonitorService13;
	// 行李拉运
	@Resource(name="taskMonitorService102")
	private TaskMonitorService taskMonitorService102;
	
	
	/**
	 * 分配图页面
	 * @param model
	 * @param request
	 * @return  
	 */
	@RequestMapping(value = "")
	public String monitor(Model model,String schemaId) {
		List<JobType> jobTypes = new ArrayList<JobType>();
		List<JobKind> kindList = UserUtils.getCurrentJobKind();
		for (JobKind jobKind : kindList) {
			jobTypes.addAll(jobKind.getTypeList());
		}
		model.addAttribute("jobTypes", JSONArray.toJSONString(jobTypes));
		model.addAttribute("schemaId", schemaId);
		return "prss/taskmonitor/taskMonitor";
	}
	
	/**
	 * 分配图图例
	 * @param model
	 * @param request
	 * @return  
	 */
	@RequestMapping(value = "legend")
	public String legend(Model model,String schemaId) {
		return "prss/taskmonitor/include/taskMonitorLegend"+schemaId;
	}
	
	
	
	/**
	 * 任务分配监控界面默认数据加载
	 * @param inFltid
	 * @param outFltid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getData")
	public ResponseVO<TaskMonitorVO> getTaskMonitorData(String dayOrNight,Integer isPart,
			String schemaId,HttpServletRequest request) {
		TaskMonitorVO taskMonitor = new TaskMonitorVO();
		try {
				taskMonitor = getTaskMonitorService(schemaId).getTaskMonitorData(dayOrNight,isPart,getAllParameter(request));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<TaskMonitorVO>exception();
		}
		return ResponseVO.<TaskMonitorVO>build().setData(taskMonitor);
	}
	
	
	/**
	 * 获取开关状态
	 * @param checkState
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "defSwitch")
	public ResponseVO<String> defSwitch(String schemaId)  throws Exception{
		String state = "";
		try {
			state = getTaskMonitorService(schemaId).defSwitch();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<String>exception();
		}
		return ResponseVO.<String>build().setData(state); 
	}
	
	/**
	 * 航班详情
	 * @param fltid
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFlightInfo")
	public ResponseVO<TaskFlightInfo> getFlightInfo(String fltid , String type,String schemaId) {
		TaskFlightInfo taskFlightInfo = new TaskFlightInfo();
		try {
			taskFlightInfo = getTaskMonitorService(schemaId).getFlightInfo(fltid,type);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<TaskFlightInfo>exception();
		}
		return ResponseVO.<TaskFlightInfo>build().setData(taskFlightInfo);
	}
	
	
	/**
	 * 任务详情
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getTaskInfo")
	public ResponseVO<TaskNode> getTaskInfo(String taskId,String schemaId) {
		TaskNode taskInfo = new TaskNode();
		try {
			taskInfo = getTaskMonitorService(schemaId).getTaskInfo(taskId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<TaskNode>exception();
		}
		return ResponseVO.<TaskNode>build().setData(taskInfo);
	}
	
	
	
	/**
	 * 高亮任务
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "highTasks")
	public ResponseVO<List<String>> highTasks(String taskId,String schemaId) {
		List<String> tasks = new ArrayList<String>();
		try {
			tasks = getTaskMonitorService(schemaId).highTasks(taskId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<String>>exception();
		}
		return ResponseVO.<List<String>>build().setData(tasks);
	}
	
	/**
	 * 任务下落时的判断
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getIfTimeConflict")
	public ResponseVO<Map<String, String>> getIfTimeConflict(String operatorId,String taskId,String fltid,String schemaId) {
		try {
			return getTaskMonitorService(schemaId).getIfTimeConflict(operatorId,taskId,fltid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<Map<String, String>>exception();
		}
	}
	
	/**
	 * 查询可分配任务的人员列表，按最优顺序排序
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSelectPersons")
	public ResponseVO<List<String>> getSelectPersons(String taskId,String schemaId) {
		List<String> resultList = new ArrayList<String>();
		try {
			resultList = getTaskMonitorService(schemaId).getSelectPersons(taskId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<String>>exception();
		}
		return ResponseVO.<List<String>>build().setData(resultList);
	}
	
	/**
	 * 获取规则列表（右键创建任务）
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getRules")
	public ResponseVO<List<Map<String, Object>>> getRules(String fltid,String jobType,String inOut,String schemaId) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			resultList = getTaskMonitorService(schemaId).getRules(jobType,inOut,fltid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<Map<String, Object>>>exception();
		}
		return ResponseVO.<List<Map<String, Object>>>build().setData(resultList);
	}
	
	
	/**
	 * 回到待命区（右键菜单）
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "operatorReturn")
	public ResponseVO<String> operatorReturn(String operator,String posType,String posName,String schemaId) {
		try {
			return getTaskMonitorService(schemaId).operatorReturn(operator,posType,posName);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<String>exception();
		}
	}
	
	/**
	 * 获取班制
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getShifts")
	public ResponseVO<JSONArray> getShifts(String schemaId) {
		JSONArray shifts = new JSONArray();
		try {
			shifts = getTaskMonitorService(schemaId).getShifts();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<JSONArray>exception();
		}
		return ResponseVO.<JSONArray>build().setData(shifts);
	}
	
	/**
	 * 设置加班
	 * @param ids
	 * @param start
	 * @param end
	 * @param schemaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "setOverWorkTime")
	public ResponseVO<String> setOverWorkTime(String ids,String start,String end,String schemaId) {
		String msg = "";
		try {
			msg = getTaskMonitorService(schemaId).setOverWorkTime(ids,start,end);
			if(StringUtils.isEmpty(msg)){
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
	 * 取作业组所有成员
	 * @param id
	 * @param schemaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getWorkingGroupMember")
	public ResponseVO<List<String>> getWorkingGroupMember(String id,String schemaId) {
		try {
			List<String> resultList = getTaskMonitorService(schemaId).getWorkingGroupMember(id);
			return ResponseVO.<List<String>>build().setData(resultList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<String>>exception();
		}
	}
	
	/**
	 * 修改员工分工，查找相关分工
	 * @param operator
	 * @param schemaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectFenGong")
	public ResponseVO<List<HashMap<String,Object>>> selectFengong(String schemaId,String operator) {
		try {
			String officeId = UserUtils.getUser().getOffice().getId();
			List<HashMap<String,Object>> resultList = getTaskMonitorService(schemaId).selectFengong(operator,officeId,schemaId);
			return ResponseVO.<List<HashMap<String,Object>>>build().setData(resultList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<HashMap<String,Object>>>exception();
		}
	}
	/**
	 * 修改员工分工，查找相关资质机位
	 * @param operator
	 * @param schemaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectJiWei")
	public ResponseVO<List<HashMap<String,Object>>> selectJiWei(String schemaId,String operator,String fengongId) {
		try {
			String officeId = UserUtils.getUser().getOffice().getId();
			List<HashMap<String,Object>> resultList = getTaskMonitorService(schemaId).selectJiWei(operator,officeId,schemaId,fengongId);
			return ResponseVO.<List<HashMap<String,Object>>>build().setData(resultList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<HashMap<String,Object>>>exception();
		}
	}
	
	/**
	 * 修改员工分工，保存机位修改后的结果
	 * @param operator
	 * @param schemaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveFengongJiwei")
	public ResponseVO<String> saveFengongJiwei(String schemaId,String fengongId,String jiweiId,String operator) {
		try {
			String officeId = UserUtils.getUser().getOffice().getId();
			int resultList = getTaskMonitorService(schemaId).saveFengongJiwei(fengongId,jiweiId,schemaId,operator,officeId);
			return ResponseVO.<String>build().setData(String.valueOf(resultList));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<String>error().setMsg(e.toString());
		}
	}
	
	/**
	 * 打开清舱未做清舱列表页面
	 * @param operator
	 * @param schemaId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "openNoClean")
	public String openNoClean(Model model,String schemaId) throws Exception {
		model.addAttribute("schemaId",schemaId);
		return "prss/taskmonitor/include/noClean07and08";
	}
	
	/**
	 * 保存清舱未做清舱航班信息
	 * @param operator
	 * @param schemaId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "saveNoClean")
	@ResponseBody
	public ResponseVO<String> saveNoClean(String schemaId,String inFltId,String type) throws Exception {
		try {
			int num = getTaskMonitorService(schemaId).saveNoClean(inFltId,type);
			return ResponseVO.<String>build().setData(String.valueOf(num));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<String>error().setMsg("该航班已经标记为未清舱航班！");
		}
	}
	
	/**
	 * 获取未做清舱列表
	 * @param operator
	 * @param schemaId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "data")
	@ResponseBody
	public Map<String,Object> getNoCleanData(String schemaId,int pageSize,int pageNumber,String searchTime,
			String type,HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("searchTime", searchTime);
        param.put("type", type);
        Map<String,Object> map = getTaskMonitorService(schemaId).getNoCleanData(param);
        return map;
	}
	
	/**
	 * 删除未做清舱列表中一条数据
	 * @param operator
	 * @param schemaId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "deleteNoCleanByFltId")
	@ResponseBody
	public String deleteNoCleanByFltId(String schemaId,String inFltId,String type) throws Exception {
        int num = getTaskMonitorService(schemaId).deleteNoCleanByFltId(inFltId,type);
        return String.valueOf(num);
	}
	
	/**
	 * 获取用户未读异常信息的条数
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getUnreadErrorNum")
	public String getUnreadErrorNum(String time) {
		String officeId = UserUtils.getUser().getOffice().getId();
		try {
			return getTaskMonitorService(null).getUnreadErrorNum(time,officeId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 查询航班下未完成的任务
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFlightUnfinishedTasks")
	public ResponseVO<List<Map<String, Object>>> getFlightUnfinishedTasks(String fltid,String schemaId) {
		try {
			List<JobKind> kindList = UserUtils.getCurrentJobKind();
			return ResponseVO.<List<Map<String, Object>>>build().setData(getTaskMonitorService(schemaId).getTaskIdsByJobKind(kindList.get(0).getKindCode(),fltid));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<Map<String, Object>>>exception();
		}
	}
	
	/**
	 * 航班手动下屏
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "flightOutScreen")
	public ResponseVO<String> flightOutScreen(String fltid,String schemaId) {
		try {
			List<JobKind> kindList = UserUtils.getCurrentJobKind();
			String msg = getTaskMonitorService(schemaId).flightOutScreen(kindList.get(0).getKindCode(),fltid);
			if(StringUtils.isEmpty(msg)){
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
	 * 释放车辆
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "releaseVehicle")
	public ResponseVO<String> releaseVehicle(String id,String schemaId) {
		try {
			
			String msg = getTaskMonitorService(schemaId).releaseVehicle(id);
			if(StringUtils.isEmpty(msg)){
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
	 * 查任务列表
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getTaskList")
	public PageBean<TaskInfo> getTaskList(String operator,String schemaId) {
		PageBean<TaskInfo> pageBean = new PageBean<TaskInfo>();
		try {
			pageBean = getTaskMonitorService(schemaId).getTaskList(operator);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pageBean;
	}
	
	/**
	 * 启动预排任务
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "startWalkthrough")
	public ResponseVO<List<String>> startWalkthrough(String id,String schemaId) {
		try {
			List<String> errorList = getTaskMonitorService(schemaId).startWalkthrough();
			if(errorList == null || errorList.size() == 0){
				return ResponseVO.<List<String>>build();
			}else{
				return ResponseVO.<List<String>>error().setData(errorList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<List<String>>exception();
		}
	}
	

	/**
	 * 释放人员(可以释放状态是1的任务)
	 * @param taskId
	 * @param workerId 作业人
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "releaseOperator")
	public String releaseOperator(String taskId,String personId,boolean autoDispatch , String schemaId ) {
		String msg = "success";
		try {
			msg = getTaskMonitorService(schemaId).releaseOperator(taskId,personId,autoDispatch);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "释放人员失败，请稍后重试！";
		}
		return msg;
	}
	
	/**
	 * 根据模块ID获取对应service
	 * @param schemaId
	 * @return
	 * @throws Exception 
	 */
	private TaskMonitorService getTaskMonitorService(String schemaId) throws Exception{
		// 默认 模块3（摆渡车）
		TaskMonitorService taskMonitorService = taskMonitorService9;
		if(!StringUtils.isEmpty(schemaId)){
			String varName = "taskMonitorService" + schemaId;
			taskMonitorService = (TaskMonitorService)this.getClass().getDeclaredField(varName).get(this);
		}
		return taskMonitorService;
	}
}
