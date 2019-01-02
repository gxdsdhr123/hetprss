package com.neusoft.prss.scheduling.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.scheduling.entity.JobTaskEntity;

@MyBatisDao
public interface JobManageDao extends BaseDao {
	/**
	 * 根据航班ID获取航班详情
	 * 
	 * @param fltid
	 * @return
	 */
	public JSONObject getFltById(String fltid);

	/**
	 * 根据航班ID获取对应的任务
	 * 
	 * @param fltid
	 * @return
	 */
	public JSONArray getJobItemByFltId(String fltid);

	/**
	 * 获取任务列表
	 * 
	 * @param params
	 * @return
	 */
	public List<JobTaskEntity> getJobTaskList(Map<String, String> params);
	
	/**
	 * 获取多个航班任务中相同流程模板任务数
	 * @param fltid
	 * @param processId
	 * @return
	 */
	public List<HashMap<String, Object>> getProcNum(@Param("fltids")Set<String> fltids,@Param("processIds")Set<String> processIds);
	/**
	 * 保存任务列表
	 * @param taskList
	 */
	public void doSave(JobTaskEntity entity);
	/**
	 * 删作业
	 * @param id
	 */
	public void removeJob(@Param("id")String id);
	/**
	 * 获取当前是否可做手动人员分配 
	 * @param params
	 * @return 0、是、1、否
	 */
	public String getJmSemaState(Map<String, String> params);
	/**
	 * 更新 jm_sema 表状态
	 * @param params
	 * @return
	 */
	public int updateJmSemaState(Map<String, String> params);
	/**
	 * 根据任务流程实例个数
	 * @param jobTaskId
	 * @return
	 */
	public int getInstCount(@Param("jobTaskId")String jobTaskId);
	/**
	 * 获取保障扩展列
	 * @param params
	 * @return
	 */
	public JSONArray getPlusColumns(Map<String, String> params);
	/**
	 * 增加或修改保障扩展列值
	 * @param params
	 */
	public void saveJobPlusData(Map<String, String> params);
	/**
	 * 获取任务扩展属性值
	 * @param params
	 */
	public JSONArray getPlusData(Map<String, String> params);
	/**
	 * 获取扩展列下拉框数据项
	 * @param params
	 * @return
	 */
	public JSONArray getPlusDimColData(Map<String, String> params);
	/**
	 * 任务扩展列值维度转换
	 * @param column
	 * @return
	 */
	public String plusDataFormat(JSONObject column);
	/**
	 * 代点原因录入
	 * @param params
	 * @return
	 */
	public void writeSurrogateLog(Map<String,String> params);
	
	/**
	 * 查询任务是否已经开始
	 * @param taskId
	 */
	public int checkTaskStarted(@Param("taskId")String taskId);
	
	/**
	 * 查询任务是否已经释放
	 * @param taskId
	 */
	public int checkTaskReleased(@Param("taskId")String taskId);
	
	/**
	 * 任务恢复
	 * @param taskId
	 */
	public int recoveryTask(@Param("taskId")String taskId);
	/**
	 * 根据流程实例获取代点日志
	 * @param orderId
	 * @return
	 */
	public JSONArray getTaskSurrogateByOrder(@Param("orderId")String orderId);

	/**
	 * 根据任务ID取job_status
	 * @param taskId
	 * @return
	 */
	public String getTaskStatus(@Param("taskId")String taskId);

	/**
	 * 更新作业人
	 * @param jobTaskId
	 * @param targetActor
	 */
	public void updateTaskActor(@Param("taskId")String taskId, @Param("targetActor")String targetActor);

	/**
	 * 检查任务是否被删除（1 代表被删除）
	 * @param taskId
	 * @return
	 */
	public Integer getTaskIsDel(@Param("taskId")String taskId);
}
