package com.neusoft.prss.progress.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.common.entity.JobType;
import com.neusoft.prss.progress.dao.ProgressDao;
@Service
public class ProgressService extends BaseService{

	@Autowired
	private ProgressDao progressDao;

	public JSONArray getTaskList(Map<String, Object> params) {
		JSONArray result = new JSONArray();
		List<String> jobTypeList = jobTypeToList(String.valueOf(params.get("jobType")));//作业类型
		if(jobTypeList==null||jobTypeList.isEmpty()){
			return result;
		}
		params.put("jobTypeList", jobTypeList);
		JSONArray taskList = progressDao.getTaskList(params);
		String taskState = String.valueOf(params.get("taskState"));//任务状态
		String alarmLevel = String.valueOf(params.get("alarmLevel"));//预警级别
		/*获取各个航班任务数信息*/
		for(int i=0;i<taskList.size();i++){
			JSONObject fltInfo = taskList.getJSONObject(i);
			JSONArray tasks = fltInfo.getJSONArray("tasks");
			int state0 = 0;//未分配任务数
			int state2 = 0;//已分配任务数
			int state3 = 0;//完成任务数
			int state4 = 0;//待排任务数
			
			for(int j=0;j<tasks.size();j++){
				JSONObject task = tasks.getJSONObject(j);
				String state = task.getString("state");
				if("0".equals(state)){
					state0++;
				} else if("2".equals(state)){
					state2++;
				} else if("3".equals(state)){
					state3++;
				} else if("4".equals(state)){
					state4++;
				}
			}
			if("0".equals(taskState)){//未分配
				//未分配+待排 = 任务总数
				if ((state0+state4) == tasks.size()) {
					fltInfo.put("taskNum",String.valueOf(tasks.size()));
					result.add(fltInfo);
				}
			} else if("2".equals(taskState)){//已排
				if ((state2+state3)>0&&(state3!=tasks.size())) {
					if((state2+state3) == tasks.size()){
						fltInfo.put("taskNum",tasks.size());
					}else {
						//格式：已排/总任务数
						fltInfo.put("taskNum","<font class='text-red'>"+(state2+state3)+"</font>/"+tasks.size());
					}
					result.add(fltInfo);
				}
			} else if("3".equals(taskState)){//完成
				if (state3 == tasks.size()) {
					fltInfo.put("taskNum",state3);
					result.add(fltInfo);
				}
			}
		}
		/*获取任务预警信息*/
		if(!result.isEmpty()){
			JSONArray taskAlarms = new JSONArray();
			List<String> fltids = new ArrayList<String>();
			for (int i = 0; i < result.size(); i++) {
				JSONObject fltInfo = result.getJSONObject(i);
				fltids.add(fltInfo.getString("infltid"));
				fltids.add(fltInfo.getString("outfltid"));
				//500条提交一次
				if((i>0&&fltids.size()%500==0)||i==result.size()-1){
					taskAlarms.addAll(getTaskAlarms(jobTypeList,fltids));
					fltids = new ArrayList<String>();
				}
			}
			//转换为map
			if (!taskAlarms.isEmpty()) {
				Map<String,JSONArray> taskAlarmMap = new HashMap<String,JSONArray>();
				for (int i = 0; i < taskAlarms.size(); i++) {
					JSONObject alarm = taskAlarms.getJSONObject(i);
					taskAlarmMap.put(alarm.getString("fltid"), alarm.getJSONArray("alarms"));
				}
				for (int i = 0; i < result.size(); i++) {
					JSONObject fltInfo = result.getJSONObject(i);
					String infltid = fltInfo.getString("infltid");
					String outfltid = fltInfo.getString("outfltid");
					JSONArray alarms = new JSONArray();
					if(taskAlarmMap.containsKey(infltid)){
						alarms.addAll(taskAlarmMap.get(infltid));
					}
					if(taskAlarmMap.containsKey(outfltid)){
						alarms.addAll(taskAlarmMap.get(outfltid));
					}
					if (!alarms.isEmpty()) {
						int alarm1 = 0;//一级预警
						int alarm2 = 0;//二级预警
						int alarm3 = 0;//三级预警
						for (int j = 0; j < alarms.size(); j++) {
							JSONObject alarmInfo = alarms.getJSONObject(j);
							if(alarmInfo!=null){
								if (alarmInfo.containsKey("alarm1")
										&& StringUtils.isNotEmpty(alarmInfo.getString("alarm1"))
										&& alarmInfo.getInteger("alarm1") > 0) {
									alarm1++;
								}
								if (alarmInfo.containsKey("alarm2")
										&& StringUtils.isNotEmpty(alarmInfo.getString("alarm2"))
										&& alarmInfo.getInteger("alarm2") > 0) {
									alarm2++;
								}
								if (alarmInfo.containsKey("alarm3")
										&& StringUtils.isNotEmpty(alarmInfo.getString("alarm3"))
										&& alarmInfo.getInteger("alarm3") > 0) {
									alarm3++;
								}
							}
						}
						if(alarm3>0){
							fltInfo.put("alarm", 3);
						} else if(alarm2>0){
							fltInfo.put("alarm", 2);
						} else if(alarm1>0){
							fltInfo.put("alarm", 1);
						} else {
							fltInfo.put("alarm", 0);
						}
					} else {
						fltInfo.put("alarm", 0);
					}
				}
			}
			//预警筛选条件
			if(StringUtils.isNotEmpty(alarmLevel)){
				List<String> alarmLevels = Arrays.asList(alarmLevel.split(","));
				JSONArray data = new JSONArray();
				for (int i = 0; i < result.size(); i++) {
					JSONObject fltInfo = result.getJSONObject(i);
					if(fltInfo.containsKey("alarm")&&alarmLevels.contains(fltInfo.getString("alarm"))){
						data.add(fltInfo);
					}
				}
				result = data;
			}
		}
		return result;
	}
	/**
	 * 获取任务预警信息
	 * @param params
	 * @return
	 */
	public JSONArray getTaskAlarms(List<String> jobType,List<String> fltids){
		return progressDao.getTaskAlarms(jobType,fltids);
	}
	/**
	 * 获取但航班任务状态
	 * @param params
	 * @return
	 */
	public JSONArray getTaskByFlt(Map<String, Object> params) {
		List<String> jobTypeList = jobTypeToList(String.valueOf(params.get("jobType")));//作业类型
		params.put("jobTypeList", jobTypeList);
		String fltid = String.valueOf(params.get("fltid"));
		List<String> fltids = Arrays.asList(fltid.split(","));
		params.put("fltids", fltids);
		JSONArray tasks = progressDao.getTaskByFlt(params);
		JSONArray alarms = progressDao.getTaskAlarms(jobTypeList,fltids);
		if(!tasks.isEmpty()&&!alarms.isEmpty()){
			JSONArray fltAlarms = new JSONArray();
			for (int i = 0; i < alarms.size(); i++) {
				JSONObject alarm = alarms.getJSONObject(i);
				String alarmFltid = alarm.getString("fltid");
				if(fltids.contains(alarmFltid)){
					fltAlarms = alarm.getJSONArray("alarms");
				}
			}
			for (int i = 0; i < tasks.size(); i++) {
				JSONObject task = tasks.getJSONObject(i);
				String taskId = task.getString("id");
				for (int j = 0; j < fltAlarms.size(); j++) {
					JSONObject taskAlarms = fltAlarms.getJSONObject(j);
					String alarmTaskId = taskAlarms.getString("id");
					if(taskId.equals(alarmTaskId)){
						if (taskAlarms.containsKey("alarm3") 
								&& StringUtils.isNotEmpty(taskAlarms.getString("alarm3"))
								&& taskAlarms.getInteger("alarm3") > 0) {
							task.put("alarm", 3);
						} else if (taskAlarms.containsKey("alarm2")
								&& StringUtils.isNotEmpty(taskAlarms.getString("alarm2"))
								&& taskAlarms.getInteger("alarm2") > 0) {
							task.put("alarm", 2);
						} else if (taskAlarms.containsKey("alarm1")
								&& StringUtils.isNotEmpty(taskAlarms.getString("alarm1"))
								&& taskAlarms.getInteger("alarm1") > 0) {
							task.put("alarm", 1);
						} else {
							task.put("alarm", 0);
						}
						break;
					}
				}
			}
		}
		return tasks;
	}
	/**
	 * 获取航班扩展属性配置ID
	 * @param params
	 * @return
	 */
	public String getFltPlusAttrId(Map<String,String> params){
		return progressDao.getFltPlusAttrId(params);
	}
	/**
	 * 获取航班备注
	 * @param params
	 * @return
	 */
	public JSONObject getFltRemark(Map<String,String> params){
		return progressDao.getFltRemark(params);
	}
	/**
	 * 保存航班备注
	 * @param params
	 */
	public void saveFltRemark(Map<String,String> params){
		progressDao.saveFltRemark(params);
	}
	/**
	 * 逗号分隔作业类型增加单引号拼接IN
	 * @param jobType
	 * @return
	 */
	private List<String> jobTypeToList(String jobTypeInput) {
		List<String> typeList = new ArrayList<String>();
		//如果外部传入了作业类型责获取作业类型否则获取默认登录用户作业类型
		if (StringUtils.isNotEmpty(jobTypeInput)) {
			typeList = Arrays.asList(jobTypeInput.split(","));
		} else {
			List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
			if (jobKinds!=null&&!jobKinds.isEmpty()) {
				JobKind jobKind = jobKinds.get(0);
				List<JobType> jobTypes = jobKind.getTypeList();
				if(jobTypes!=null&&!jobTypes.isEmpty()){
					for(JobType jobType : jobTypes){
						typeList.add(jobType.getTypeCode());
					}
				}
			}
		}
		return typeList;
	}

}
