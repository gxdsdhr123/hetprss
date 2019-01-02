package com.neusoft.prss.scheduling.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.TaskOperLogEntity;
import com.neusoft.prss.common.service.OperLogWriteService;
import com.neusoft.prss.scheduling.dao.JobManageDao;
import com.neusoft.prss.scheduling.entity.JobTaskEntity;

@Service
@Transactional(readOnly = true)
public class JobManageService extends BaseService {
	@Autowired
	private JobManageDao jobDao;
	@Autowired
	private OperLogWriteService writeLogService;

	/**
	 * 根据航班ID获取航班详情
	 * 
	 * @param fltid
	 * @return
	 */
	public JSONObject getFltById(String fltid) {
		return jobDao.getFltById(fltid);
	}

	/**
	 * 根据航班ID获取对应的任务
	 * 
	 * @param fltid
	 * @return
	 */
	public JSONArray getJobItemByFltId(String fltid) {
		return jobDao.getJobItemByFltId(fltid);
	}

	/**
	 * 获取保障任务列表
	 * 
	 * @param fltid
	 * @return
	 */
	public List<JobTaskEntity> getJobTaskList(Map<String, String> params) {
		List<JobTaskEntity> jobs = jobDao.getJobTaskList(params);
		StringBuffer taskIds = new StringBuffer("");
		for(int i=0;i<jobs.size();i++){
			taskIds.append(jobs.get(i).getId());
			if(i!=jobs.size()-1){
				taskIds.append(",");
			}
		}
		if(StringUtils.isNotEmpty(taskIds)){
			params.put("taskIds", taskIds.toString());
			JSONArray allPulsData = jobDao.getPlusData(params);
			if(allPulsData!=null&&!allPulsData.isEmpty()){
				for(JobTaskEntity entity : jobs){
					JSONObject plusData = new JSONObject();
					for(int i=0;i<allPulsData.size();i++){
						JSONObject pulsDataItem = allPulsData.getJSONObject(i);
						String taskId = pulsDataItem.getString("taskId");
						if(entity.getId().equals(taskId)){
							plusData.put(pulsDataItem.getString("attrCode"), pulsDataItem.getString("attrValue"));
						}
					}
					entity.setPlusData(plusData);
				}
			}
		}
		return jobs;
	}

	/**
	 * 获取保障任务列表
	 * 
	 * @param fltid
	 * @return
	 */
	public List<JobTaskEntity> doSave(List<JobTaskEntity> taskItem) {
		// 统计航班种类及模板种类
		Set<String> fltidSet = new HashSet<String>();
		Set<String> procIdSet = new HashSet<String>();
		for (JobTaskEntity entity : taskItem) {
			fltidSet.add(entity.getFltid());
			procIdSet.add(entity.getProcessId());
		}
		// 获取航班任务中相同流程模板任务数
		List<HashMap<String, Object>> numList = jobDao.getProcNum(fltidSet, procIdSet);
		Map<String, Integer> numMap =new HashMap<String, Integer>();
		for(HashMap<String, Object> m : numList){
			String key = m.get("FLTID") +"|" + m.get("PROCID");
			Integer value = ((BigDecimal)m.get("NUM")).intValue();
			numMap.put(key, value);
		}
		// 插入任务
		for (JobTaskEntity entity : taskItem) {
			// 获取当前流程模板在航班中任务数
			String key = entity.getFltid()+"|"+entity.getProcessId();
			Integer num = numMap.get(key);
			if(num == null){
				num = 0;
			}
			// 给任务名称加上编号
			entity.setName(entity.getProcessName() + (++num));
			numMap.put(key, num);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StringUtils.isNotEmpty(entity.getPerson())) {
				// 已分配
				entity.setState("1");
				entity.setActArrangeTime(sdf.format(new Date()));
			} else {
				// 未分配
				entity.setState("0");
			}
			boolean isNew = StringUtils.isEmpty(entity.getId());//是否新建
			jobDao.doSave(entity);
			//插入扩展列值到jm_task_plus
			if(entity.getPlusData()!=null&&StringUtils.isNotEmpty(entity.getId())){
				JSONObject plusData = entity.getPlusData();
				for (Map.Entry<String, Object> entry : plusData.entrySet()) {
					if (StringUtils.isNotEmpty(entry.getKey()) && entry.getKey().startsWith("jmplus_")) {
						Map<String,String> params = new HashMap<String,String>();
						params.put("taskId", entity.getId());
						params.put("attrCode",entry.getKey().replace("jmplus_", ""));
						params.put("attrValue",entry.getValue()==null?"":entry.getValue().toString());
						jobDao.saveJobPlusData(params);
					}
				}
			}
			if(isNew){//写日志
				TaskOperLogEntity log = new TaskOperLogEntity();
				log.setUserId(UserUtils.getUser().getId());//操作人
				log.setTaskId(entity.getId());//任务id
				log.setOperType(1);//操作类型：创建
				log.setTermType(1);//操作终端：PC
				writeLogService.writeTaskLog(log);
			}
		}
		return taskItem;
	}

	/**
	 * 删除作业
	 * 
	 * @param fltid
	 * @return
	 */
	public void removeJob(String id, String orderId) {
		if (StringUtils.isNotEmpty(id)) {
			jobDao.removeJob(id);
		}
	}

	/**
	 * 获取当前是否可做手动人员分配
	 * 
	 * @param params
	 * @return 0、是、1、否
	 */
	public String getJmSemaState(Map<String, String> params) {
		return jobDao.getJmSemaState(params);
	}

	/**
	 * 更新 jm_sema 表状态
	 * 
	 * @param params
	 * @return
	 */
	public int updateJmSemaState(Map<String, String> params) {
		return jobDao.updateJmSemaState(params);
	}
	/**
	 * 更新 jm_sema 表状态
	 * 
	 * @param params
	 * @return
	 */
	public int getInstCount(String jobTaskId) {
		return jobDao.getInstCount(jobTaskId);
	}
	/**
	 * 获取扩展列头
	 * @param params
	 * @return
	 */
	public JSONArray getPlusColumns(Map<String, String> params){
		JSONArray columns = jobDao.getPlusColumns(params);
		for(int i=0;i<columns.size();i++){
			JSONObject column = columns.getJSONObject(i);
			String disType = column.getString("disType");
			if("select".equals(disType)){
				Map<String,String> sqlParams = new HashMap<String,String>();
				sqlParams.put("dimTab", column.getString("dimTab"));
				sqlParams.put("codeCol", column.getString("codeCol"));
				sqlParams.put("nameCol", column.getString("nameCol"));
				JSONArray source = jobDao.getPlusDimColData(sqlParams);
				column.put("source", source);
			}
		}
		return columns;
	}
	/**
	 * 扩展列维度转换
	 * @param value
	 * @param column
	 * @return
	 */
	public String plusDataFormat(String value,JSONObject column){
		column.put("colValue", value);
		return jobDao.plusDataFormat(column);
	}
	/**
	 * 代点原因录入
	 * @param params
	 * @return
	 */
	public void writeSurrogateLog(Map<String,String> params){
		jobDao.writeSurrogateLog(params);
	}
	
	/**
	 * 任务恢复
	 * @param taskId
	 * @return
	 */
	public String recoveryTask(String taskId) {
		int c = jobDao.checkTaskReleased(taskId);
		if(c>0){
			jobDao.recoveryTask(taskId);
			//写日志
			TaskOperLogEntity log = new TaskOperLogEntity();
			log.setUserId(UserUtils.getUser().getId());//操作人
			log.setTaskId(taskId);//任务id
			log.setOperType(8);//操作类型：恢复
			log.setTermType(1);//操作终端：PC
			writeLogService.writeTaskLog(log);
		}else{
			return "任务不是【人员释放】状态，不能执行恢复操作！";
		}
		return "success";
	}

	/**
	 * 检查任务是否开始
	 * @param taskId
	 * @return
	 */
	public int checkTaskStarted(String taskId) {
		return jobDao.checkTaskStarted(taskId);
	}
	/**
	 * 根据流程实例获取代点日志
	 * @param orderId
	 * @return
	 */
	public Map<String,JSONObject> getTaskSurrogateByOrder(String orderId){
		Map<String,JSONObject> result = new HashMap<String,JSONObject>();
		JSONArray surrogateLogs = jobDao.getTaskSurrogateByOrder(orderId);
		for(int i=0;i<surrogateLogs.size();i++){
			JSONObject json = surrogateLogs.getJSONObject(i);
			if(json.containsKey("taskId")){
				result.put(json.getString("taskId"), json);
			}
		}
		return result;
	}

	public String getTaskStatus(String jobTaskId) {
		return jobDao.getTaskStatus(jobTaskId);
	}
	/**
	 * 预排转办
	 * @param jobTaskId
	 * @param targetActor
	 */
	public void updateTaskActor(String jobTaskId, String targetActor) {
		jobDao.updateTaskActor(jobTaskId,targetActor);
		//写日志
		TaskOperLogEntity log = new TaskOperLogEntity();
		log.setUserId(UserUtils.getUser().getId());//操作人
		log.setTaskId(jobTaskId);//任务id
		log.setOperType(5);//操作类型：转办
		log.setTermType(1);//操作终端：PC
		log.setWorkerId(targetActor);//作业人
		writeLogService.writeTaskLog(log);
	}

	public Integer getTaskIsDel(String jobTaskId) {
		return jobDao.getTaskIsDel(jobTaskId);
	}
}
