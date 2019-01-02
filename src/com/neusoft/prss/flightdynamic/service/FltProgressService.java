package com.neusoft.prss.flightdynamic.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.flightdynamic.dao.FltProgressDao;

@Service
public class FltProgressService extends BaseService {
	@Autowired
	private FltProgressDao progressDao;

	/**
	 * 获取航班及任务信息
	 * 
	 * @param params
	 * @return
	 */
	public JSONArray getDataList(Map<String, String> params) {
		JSONArray fltList = progressDao.getFltList(params);
		for (int i = 0; i < fltList.size(); i++) {
			JSONObject data = fltList.getJSONObject(i);
			// 机坪对应的航班列表
			JSONArray apronFltData = data.getJSONArray("fltData");
			for (int j = 0; j < apronFltData.size(); j++) {
				JSONObject fltInfo = apronFltData.getJSONObject(j);
				// 航班任务状态处理
				if (fltInfo.containsKey("columns")) {
					JSONArray columns = fltInfo.getJSONArray("columns");
					for (int k = 0; k < columns.size(); k++) {
						JSONObject cell = columns.getJSONObject(k);
						JSONArray tasks = cell.getJSONArray("tasks");
						if (tasks != null && !tasks.isEmpty()) {
							int noExecutoryTasKNum = 0;// 当前航班未分配人员的任务数
							int executoryTaskNum = 0;// 当前航班执行中的任务数
							int completeTaskNum = 0;// 当前航班完成的任务数
							for (int n = 0; n < tasks.size(); n++) {
								JSONObject task = tasks.getJSONObject(n);
								if (task.containsKey("jobState")) {
									// 未分配+已分配(分人但未启动实例)+待排
									if ("2".equals(task.getString("jobState"))&&StringUtils.isNoneEmpty(task.getString("jobStartTm"))) {// 执行中
										executoryTaskNum++;
									} else if ("3".equals(task.getString("jobState"))) {// 完成
										completeTaskNum++;
									} else {
										noExecutoryTasKNum++;
									}
								}
							}
							String colCode = "C" + cell.getString("colCode");// 列编码
							if (tasks.size() == noExecutoryTasKNum) {
								fltInfo.put(colCode, "<span class='label bg-gray'>未执行</span>");
							} else if (executoryTaskNum > 0 ||(noExecutoryTasKNum>0&&completeTaskNum>0)) {
								fltInfo.put(colCode, "<span class='label bg-red'>执行中</span>");
							} else if (completeTaskNum == tasks.size()) {
								fltInfo.put(colCode, "<span class='label bg-green'>完成</span>");
							}
						}
					}
				}
			}
		}
		return fltList;
	}

	/**
	 * 根据航班ID获取航班详情
	 * 
	 * @param fltid
	 * @return
	 */
	public JSONObject getFltById(String fltid) {
		return progressDao.getFltById(fltid);
	}
	/**
	 * 获取任务列表
	 * @param params
	 * @return
	 */
	public JSONArray getJobTaskList(Map<String, String> params) {
		JSONArray jobList = progressDao.getJobTaskList(params);
		/**
		 * 获取人员晚到状态
		 */
		StringBuffer jobIds = new StringBuffer("");
		for(int i=0;i<jobList.size();i++){
			JSONObject job = jobList.getJSONObject(i);
			jobIds.append(job.getString("id"));
			if(i!=jobList.size()-1){
				jobIds.append(",");
			}
		}
		if(StringUtils.isNotEmpty(jobIds)){
			Map<String,String> args = new HashMap<String,String>();
			args.put("jobIds", jobIds.toString());
			JSONArray jobsState = progressDao.getJobLateState(args);
			Map<String,String> jobStateMap = new HashMap<String,String>();
			for(int i=0;i<jobsState.size();i++){
				JSONObject jobState = jobsState.getJSONObject(i);
				//任务id
				String jobTaskId = jobState.getString("jobTaskId");
				//晚到分钟数
				String lateMinutes = jobState.getString("lateMinutes");
				if(StringUtils.isNotEmpty(lateMinutes)){
					if(Integer.valueOf(lateMinutes)>0){
						jobStateMap.put(jobTaskId, "是(晚"+lateMinutes+"分钟)");
					} else {
						jobStateMap.put(jobTaskId, "否");
					}
				}
			}
			for(int i=0;i<jobList.size();i++){
				JSONObject job = jobList.getJSONObject(i);
				if(jobStateMap.containsKey(job.getString("id"))){
					job.put("lateState", jobStateMap.get(job.getString("id")));
				}
			}
		}
		return jobList;
	}
	/**
	 * 获取人员晚到信息
	 * @param params
	 * @return
	 */
	public JSONArray getJobLateState(Map<String, String> params) {
		return progressDao.getJobLateState(params);
	}
}
