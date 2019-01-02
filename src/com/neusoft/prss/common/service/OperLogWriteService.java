package com.neusoft.prss.common.service;

import java.util.List;

import com.neusoft.prss.common.entity.OperationLogEntity;
import com.neusoft.prss.common.entity.TaskOperLogEntity;

public interface OperLogWriteService {
	/**
	 * 记录操作日志
	 * 
	 * @param entity
	 */
	void writeLog(OperationLogEntity log);

	/**
	 * 批量记录操作日志
	 * 
	 * @param entity
	 */
	void writeLogBatch(List<OperationLogEntity> logs);
	/**
	 * 记录任务操作日志
	 * 
	 * @param entity
	 */
	void writeTaskLog(TaskOperLogEntity log);
}
