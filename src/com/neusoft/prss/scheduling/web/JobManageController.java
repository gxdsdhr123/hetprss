package com.neusoft.prss.scheduling.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.arrange.service.ArrangeService;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.common.entity.JobType;
import com.neusoft.prss.common.entity.TaskOperLogEntity;
import com.neusoft.prss.common.service.OperLogWriteService;
import com.neusoft.prss.message.service.MessageSendService;
import com.neusoft.prss.scheduling.entity.JobTaskEntity;
import com.neusoft.prss.scheduling.service.JobManageService;
import com.neusoft.prss.scheduling.service.SchedulingHisListService;
import com.neusoft.prss.taskarrange.service.TaskArrangeService;
import com.neusoft.prss.workflow.access.QueryFilter;
import com.neusoft.prss.workflow.service.ProcessService;
import com.neusoft.prss.workflow.service.TaskExtraService;
import com.neusoft.prss.workflow.service.TaskService;
import com.neusoft.prss.workflow.service.WFBtnEventService;

/**
 * 作业管理
 * 
 * @author baochl
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/scheduling/jobManage")
public class JobManageController extends BaseController {
	@Autowired
	private JobManageService jobService;
	@Autowired
	private ProcessService processService;// 流程模板service
	@Autowired
	private TaskService taskService;// 节点实例service
	@Autowired
	private WFBtnEventService wfBtnService;// 流程按钮
	@Autowired
	private ArrangeService arrangeService;// 人员分工
	@Autowired
	private MessageSendService messageSendService;// 消息发送
	@Autowired
	private TaskArrangeService taskArrangeService;// 消息发送
	@Autowired
	private TaskExtraService taskExtraService;	// 任务释放
	@Autowired
	private SchedulingHisListService schedulingHisListService;
	@Autowired
	private OperLogWriteService writeLogService;

	/**
	 * 
	 * @param model
	 * @param schemaId
	 *            模块ID
	 * @param inFltId
	 *            进港航班ID
	 * @param outFltId
	 *            出港航班ID
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(Model model, String schemaId, String inFltId, String outFltId, String hisFlag) {
		// 进港航班
		JSONObject inFlight = null;
		if(StringUtils.isBlank(hisFlag)){
			inFlight = jobService.getFltById(inFltId);
		} else {
			inFlight = schedulingHisListService.getFltById(inFltId);
		}
		// 出港航班
		JSONObject outFlight = null;
		if(StringUtils.isBlank(hisFlag)){
			outFlight = jobService.getFltById(outFltId);
		} else {
			outFlight = schedulingHisListService.getFltById(outFltId);
		}
		String fltids = "";// 进出港航班ID
		if (StringUtils.isNotEmpty(inFltId)) {
			fltids += inFltId;
		}
		if (StringUtils.isNotEmpty(outFltId)) {
			if (StringUtils.isNotEmpty(fltids)) {
				fltids += ",";
			}
			fltids += outFltId;
		}
		// 当前登录人作业类型
		StringBuffer jobTypes = new StringBuffer("");
		List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
		for (int i = 0; i < jobKinds.size(); i++) {
			JobKind jobKind = jobKinds.get(i);
			List<JobType> typeList = jobKind.getTypeList();
			for (JobType jobType : typeList) {
				jobTypes.append("'" + jobType.getTypeCode() + "'");
				jobTypes.append(",");
			}
		}
		List<JobTaskEntity> jobs = new ArrayList<JobTaskEntity>();
		if (StringUtils.isNotEmpty(fltids) && StringUtils.isNotEmpty(jobTypes)) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("fltids", fltids);
			params.put("jobTypes", jobTypes.substring(0, jobTypes.length() - 1));
			if(StringUtils.isBlank(hisFlag)){
				jobs = jobService.getJobTaskList(params);
			} else {
				jobs = schedulingHisListService.getJobTaskList(params);
			}
		}
		model.addAttribute("inFlight", inFlight);
		model.addAttribute("outFlight", outFlight);
		model.addAttribute("schemaId", schemaId);
		model.addAttribute("jobs", jobs);
		if(jobKinds!=null&&!jobKinds.isEmpty()){
			Map<String, String> params = new HashMap<String, String>();
			String jobKind = jobKinds.get(0).getKindCode();
			params.put("jobKind",jobKind);
			model.addAttribute("jobKind", jobKind);
			JSONArray plusColumns = jobService.getPlusColumns(params);
			if(jobs!=null&&!jobs.isEmpty()){
				Map<String,JSONObject> plusColumnMap = new HashMap<String,JSONObject>();
				for(int i=0;i<plusColumns.size();i++){
					JSONObject plusColumn = plusColumns.getJSONObject(i);
					plusColumnMap.put(plusColumn.getString("attrCode"), plusColumn);
				}
				for(JobTaskEntity entity : jobs){
					if(entity.getPlusData()!=null){
						JSONObject plusData = entity.getPlusData();
						for (Map.Entry<String, Object> entry : plusData.entrySet()) {
							String key = entry.getKey().replace("jmplus", "");
							if (plusColumnMap.containsKey(key)
									&& "select".equals(plusColumnMap.get(key).getString("disType"))) {
								if(entry.getValue()!=null&&StringUtils.isNotEmpty(entry.getValue().toString())){
									plusData.put(entry.getKey(), jobService.plusDataFormat(entry.getValue().toString(), plusColumnMap.get(key)));
								}
							}
						}
					}
				}
			}
			model.addAttribute("plusColumns", plusColumns);
		}
		model.addAttribute("hisFlag", hisFlag);
		return "prss/scheduling/jobManage";
	}

	/**
	 * 
	 * @param model
	 * @param schemaId
	 *            模块ID
	 * @param inFltId
	 *            进港航班ID
	 * @param outFltId
	 *            出港航班ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "gridData")
	public String gridData(Model model, String schemaId, String inFltId, String outFltId, String jobTaskId) {
		JSONArray data = new JSONArray();
		if (StringUtils.isNotEmpty(jobTaskId)) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", jobTaskId);
			List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
			JSONArray plusColumns = null;
			if (jobKinds != null && !jobKinds.isEmpty()) {
				params.put("jobKind",jobKinds.get(0).getKindCode());
				plusColumns = jobService.getPlusColumns(params);
			}
			List<JobTaskEntity> jobs = jobService.getJobTaskList(params);
			if (jobs != null && !jobs.isEmpty()) {
				data = JSONArray.parseArray(JSON.toJSONString(jobs));
				/**任务扩展属性转换****/
				for(int i=0;i<data.size();i++){
					JSONObject dataItem = data.getJSONObject(i);
					if (jobKinds != null && !jobKinds.isEmpty()) {
						if(plusColumns!=null&&!plusColumns.isEmpty()){
							JSONObject plusData = dataItem.getJSONObject("plusData");
							for (int j=0;j<plusColumns.size();j++) {
								JSONObject plusColumn = plusColumns.getJSONObject(j);
								String attrCode = plusColumn.getString("attrCode");
								if (plusData != null && plusData.containsKey(attrCode)) {
									dataItem.put("jmplus_" + attrCode, plusData.getString(attrCode));
								} else {
									dataItem.put("jmplus_" + attrCode, "");
								}
							}
						}
					}
				}
				/**任务扩展属性转换****/
			}
		}
		return data.toString();
	}

	/**
	 * 新建任务表单
	 * 
	 * @param model
	 * @param jobTaskId
	 *            作业任务ID
	 * @param schemaId
	 *            模块ID
	 * @param inFltId
	 *            进港航班ID
	 * @param outFltId
	 *            出港航班ID
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(Model model, String jobTaskId, String schemaId, String inFltId, String outFltId) {
		// 进港航班
		JSONObject inFlight = jobService.getFltById(inFltId);
		// 出港航班
		JSONObject outFlight = jobService.getFltById(outFltId);
		List<JobType> jobTypes = new ArrayList<JobType>();
		List<JobKind> kindList = UserUtils.getCurrentJobKind();
		for (JobKind jobKind : kindList) {
			jobTypes.addAll(jobKind.getTypeList());
		}
		JSONArray jobTypeJson = new JSONArray();// 当前登录用户所有作业类型
		JSONArray processSource = new JSONArray();// 所有流程模板
		for (JobType jobType : jobTypes) {
			JSONObject jobJson = new JSONObject();
			jobJson.put("text", jobType.getTypeName());
			jobJson.put("value", jobType.getTypeCode());
			jobTypeJson.add(jobJson);
			// 获取模板
			QueryFilter filter = new QueryFilter();
			filter.setJobType(jobType.getTypeCode());
			JSONArray processList = processService.getProcess(filter);
			for (int i = 0; i < processList.size(); i++) {
				JSONObject proc = processList.getJSONObject(i);
				JSONObject json = new JSONObject();
				json.put("text", proc.getString("displayName"));
				json.put("value", proc.getString("id"));
				json.put("jobType", proc.getString("jobType"));
				processSource.add(json);
			}
		}
		model.addAttribute("inFlight", inFlight);
		model.addAttribute("outFlight", outFlight);
		model.addAttribute("schemaId", schemaId);
		model.addAttribute("jobTypes", jobTypes);
		model.addAttribute("jobTypeJson", jobTypeJson);
		model.addAttribute("processSource", processSource);
		model.addAttribute("jobTaskId", jobTaskId);
		return "prss/scheduling/jobTaskForm";
	}

	/**
	 * 新增行
	 * 
	 * @param model
	 * @param schemaId
	 *            模块ID
	 * @param inFltId
	 *            进港航班ID
	 * @param outFltId
	 *            出港航班ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addRow")
	public String addRow(Model model, String inFltId, String outFltId, String jobTypes,String ruleId) {
		/* 调用手动规则 */
		JSONObject prams = new JSONObject();
		String fltids = "";
		if (StringUtils.isNotEmpty(inFltId)) {
			fltids += inFltId;
		}
		if (StringUtils.isNotEmpty(outFltId)) {
			if (StringUtils.isNotEmpty(fltids)) {
				fltids += ",";
			}
			fltids += outFltId;
		}
		prams.put("fltids", fltids);
		prams.put("jobTypes", jobTypes);
		prams.put("ruleId", ruleId);		//新增ruleId
		JSONArray data = taskArrangeService.manualTaskArrange(prams);
		
		/* end 调用手动规则 */
		// 判断规则是否返回了航班对应的任务
		Set<String> dataSet = new HashSet<String>();
		for (int i = 0; i < data.size(); i++) {
			JSONObject json = data.getJSONObject(i);
			if (json.containsKey("fltid") && json.containsKey("jobType")) {
				dataSet.add(json.getString("fltid") + json.getString("jobType"));
			}
			// 如果jobTypes入参是空，则根据返回结果回填，实际这种情况只会有一条
			if(StringUtils.isEmpty(jobTypes)){
				jobTypes = json.getString("jobType");
			}
		}
		String[] typeItem = jobTypes.split(",");
		String jobKind = "";
		List<JobKind> kindList = UserUtils.getCurrentJobKind();
		if (kindList != null && !kindList.isEmpty()) {
			jobKind = kindList.get(0).getKindCode();
		}
		for (String type : typeItem) {
			// 进港航班
			if (StringUtils.isNotEmpty(inFltId)) {
				// 规则已经返回
				if (dataSet.contains(inFltId + type)) {
					continue;
				} else {
					JSONObject inFlight = jobService.getFltById(inFltId);
					inFlight.put("jobTypeId", type);
					inFlight.put("jobKind", jobKind);
					data.add(inFlight);
				}
			}
			// 出港航班
			if (StringUtils.isNotEmpty(outFltId)) {
				// 规则已经返回
				if (dataSet.contains(outFltId + type)) {
					continue;
				} else {
					JSONObject outFlight = jobService.getFltById(outFltId);
					outFlight.put("jobTypeId", type);
					outFlight.put("jobKind", jobKind);
					data.add(outFlight);
				}
			}
		}
		//任务扩展属性
		if(StringUtils.isNotEmpty(jobKind)&&data!=null&&!data.isEmpty()){
			Map<String, String> params = new HashMap<String, String>();
			params.put("jobKind",jobKind);
			JSONArray result = jobService.getPlusColumns(params);
			if(result!=null&&!result.isEmpty()){
				for(int i=0;i<data.size();i++){
					JSONObject dataItem = data.getJSONObject(i);
					for(int j=0;j<result.size();j++){
						JSONObject plusColumn = result.getJSONObject(j);
						dataItem.put("jmplus_"+plusColumn.getString("attrCode"), "");
					}
				}
			}
		}
		return data.toString();
	}

	/**
	 * 选择作业人
	 * 
	 * @param model
	 * @param fltId
	 * @param jobType
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "jobTaskUserForm")
	public String jobTaskUserForm(Model model, String fltId, String jobType, String startTime, String endTime) {
		JSONObject param = new JSONObject();
		param.put("fltid", fltId);
		param.put("startTm", startTime);
		param.put("endTm", endTime);
		param.put("job_type", jobType);
		JSONArray data = new JSONArray();
		// 没有作业类型无权限操作
		List<String> personList = null;
		try {
			if(UserUtils.getCurrentJobKind() != null && UserUtils.getCurrentJobKind().size() > 0){
				personList = arrangeService.manualSelectDriver(param.toString(),UserUtils.getCurrentJobKind().get(0).getKindCode());
			}
			if (personList != null && !personList.isEmpty()) {
				for (String str : personList) {
					data.add(JSONObject.parse(str));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("persons", data);
		return "prss/scheduling/jobTaskUserForm";
	}

	/**
	 * 发布任务
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(Model model, HttpServletRequest request) {
		String result = "succeed";
		try {
			String taskListStr = request.getParameter("taskList");
			if (StringUtils.isNotEmpty(taskListStr)) {
				taskListStr = StringEscapeUtils.unescapeHtml(taskListStr);
				JSONArray tasksJSON = JSON.parseArray(taskListStr);
				//转换任务扩展属性值
				for(int i=0;i<tasksJSON.size();i++){
					JSONObject rowData = tasksJSON.getJSONObject(i);
					JSONObject plusData = new JSONObject();
					for (Map.Entry<String, Object> entry : rowData.entrySet()) {
			           if(StringUtils.isNotEmpty(entry.getKey())&&entry.getKey().startsWith("jmplus_")){
			        	   plusData.put(entry.getKey(),entry.getValue());
			           }
			        }
					rowData.put("plusData", plusData);
				}
				List<JobTaskEntity> taskItem = JSON.parseArray(tasksJSON.toString(), JobTaskEntity.class);
				int instCount = 0;//任务实例个数，修改时判断是否
				int personCount = 0;//判断是否给任务分配了人员
				for (int i = 0; i < taskItem.size(); i++) {
					JobTaskEntity entity = taskItem.get(i);
					if (StringUtils.isNotEmpty(entity.getPerson()) && StringUtils.isNotEmpty(entity.getId())) {
						instCount += jobService.getInstCount(entity.getId());
					}
					if (StringUtils.isNotEmpty(entity.getPerson())) {
						personCount ++;
					}
				}
				if (instCount > 0) {
					result = "任务已经分配人员，不能重复分配！";
				} else {
					if(personCount>0){//如果选择了人，校验是否选择人员超时
						Map<String, String> params = getJmSemaState("1");
						if (params.containsKey("flag") && "1".equals(params.get("flag"))) {
							taskItem = jobService.doSave(taskItem);
							// 启动流程实例
							for (JobTaskEntity entity : taskItem) {
								if (StringUtils.isNotEmpty(entity.getPerson())) {
									// 检查任务是否被删除
									Integer delFlag = jobService.getTaskIsDel(entity.getId());
									if(delFlag == 1){
										return "任务"+entity.getName() + "已被删除！";
									}
									String userId = UserUtils.getUser().getId();//操作人
									wfBtnService.startInstance(entity.getId(), entity.getProcessId(),userId, entity.getPersonId(), null);
									//写日志
									TaskOperLogEntity log = new TaskOperLogEntity();
									log.setUserId(userId);
									log.setTaskId(entity.getId());//任务id
									log.setOperType(2);//操作类型：分配
									log.setTermType(1);//操作终端：PC
									log.setWorkerId(entity.getPersonId());//作业人
									writeLogService.writeTaskLog(log);
								}
							}
							params.put("flag", "0");
							jobService.updateJmSemaState(params);
						} else {
							result = "您操作时间过长人员信息已发生变化，请重新选择作业人后发布！";
						}
					} else {//没有设置人员时直接保存
						jobService.doSave(taskItem);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			result = e.toString();
		}
		return result;
	}

	/**
	 * 
	 * @param model
	 * @param id
	 *            任务id
	 * @param orderId
	 *            流程实例id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "remove")
	public String removeTask(Model model, String id, String orderId, String procId,String personId) {
		String result = "succeed";
		try {
			// 判断任务状态是否是未开始
			String status = jobService.getTaskStatus(id);
			// 如果不是未开始则转调原来的方法
			if("2".equals(status)){
				return "任务执行中，不可以删除！";
			}else if("3".equals(status)){
				return "任务已执行完成，不可以删除！";
			}
			jobService.removeJob(id, orderId);
			// 发送删除消息
			if (StringUtils.isNotEmpty(procId)) {
				JSONObject procObj = processService.getProcessById(procId);
				if (procObj.containsKey("removeMsg") && StringUtils.isNotEmpty(procObj.getString("removeMsg"))) {
					String msgId = procObj.getString("removeMsg");// 删除消息
					JSONObject data = taskService.getFltByJob(id);
					if (data != null) {
						data.put("MTOTYPE", "9");// 固定参数
						data.put("SYS", "11");// 固定参数
						data.put("TASKID", id);// 作业任务ID
						data.put("TID", msgId);// 消息模板
						JSONObject returnVal = messageSendService.sendMessage(data);
						if (returnVal.containsKey("CODE") && "0".equals(returnVal.getString("CODE"))) {
							logger.info(returnVal.getString("MSG"));
						} else {
							logger.error("删除任务发送消息【" + msgId + "】失败：" + returnVal.getString("MSG"));
						}
					}
				}
			}
			//写日志
			TaskOperLogEntity log = new TaskOperLogEntity();
			log.setUserId(UserUtils.getUser().getId());//操作人
			log.setTaskId(id);//任务id
			log.setOrderId(orderId);
			log.setOperType(10);//操作类型：删除
			log.setTermType(1);//操作终端：PC
			log.setWorkerId(personId);
			writeLogService.writeTaskLog(log);
		} catch (Exception e) {
			result = "删除失败！";
			logger.error(e.getMessage(),e);
		}
		return result;
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
		int result = -1;
		Map<String, String> params = getJmSemaState("0");
		if (params.containsKey("flag") && "0".equals(params.get("flag"))) {
			params.put("flag", "1");
			int num = jobService.updateJmSemaState(params);
			if (num > 0) {
				result = 0;
			}
		}
		return String.valueOf(result);
	}
	/**
	 * 判断是否可手动分配人员
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
	/**
	 * 获取任务扩展属性
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getPlusColumns")
	public JSONArray getPlusColumns(){
		JSONArray result = new JSONArray();
		List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
		if(jobKinds!=null&&!jobKinds.isEmpty()){
			Map<String, String> params = new HashMap<String, String>();
			params.put("jobKind",jobKinds.get(0).getKindCode());
			result = jobService.getPlusColumns(params);
		}
		return result;
	}
	/**
	 * 代点原因录入
	 * @return
	 */
	@RequestMapping(value = "jobSurrogateForm")
	public String jobSurrogateForm(){
		return "prss/scheduling/jobSurrogateForm";
	}
	
	/**
	 * 释放人员
	 * @param taskId
	 * @param workerId 作业人
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "releaseOperator")
	public String releaseOperator(String taskId,String personId,boolean autoDispatch ) {
		try {
			// 判断任务是否已经开始，开始后不能释放
			int c = jobService.checkTaskStarted(taskId);
			if(c == 0){
				return "不是执行中的任务不能释放人员！";
			}
			// 调用接口
			int result = taskExtraService.releaseTaskOperator(taskId, autoDispatch);
			if(result==0){
				//写日志
				TaskOperLogEntity log = new TaskOperLogEntity();
				log.setUserId(UserUtils.getUser().getId());//操作人
				log.setTaskId(taskId);//任务id
				log.setOperType(7);//操作类型：释放人员
				log.setTermType(1);//操作终端：PC
				log.setWorkerId(personId);//作业人
				writeLogService.writeTaskLog(log);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "释放人员失败，请稍后重试！";
		}
		return "success";
	}
	
	
	/**
	 * 任务恢复
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "recoveryTask")
	public String recoveryTask(String taskId) {
		try {
			return jobService.recoveryTask(taskId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "任务恢复失败，请稍后重试！";
		}
	}
	
	@RequestMapping(value = "showTaskTime")
	public String showTaskTime(String id,String jobKind,Model model){
		List<Map<String,Object>> nodeTimeList = new ArrayList<Map<String,Object>>();
		int idInt=Integer.parseInt(id);
		nodeTimeList = schedulingHisListService.getNodeTime(idInt,jobKind);
		model.addAttribute("nodeTimeList", nodeTimeList);
		return "prss/scheduling/taskNodeTime";
	}
}
