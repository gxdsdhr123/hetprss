package com.neusoft.prss.common.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.common.entity.OperationLogEntity;

public interface OperationLogService {
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
	 * 操作日志查询
	 * 
	 * @return
	 */
	JSONArray getOperationLog(Map<String, String> params);
	/**
	 * 获取总行数
	 * @param params
	 * @return
	 */
	int getTotalRow(Map<String, String> params);
}
