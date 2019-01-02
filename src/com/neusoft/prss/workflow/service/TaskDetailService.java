package com.neusoft.prss.workflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.workflow.dao.TaskDetailDao;

@Service
public class TaskDetailService extends BaseService {

	@Autowired
	private TaskDetailDao taskDetailDao;

	public JSONObject getTaskDetail(String jobTaskId) {
		return taskDetailDao.getTaskDetail(jobTaskId);
	}

	public List<String> getActors(String leader) {
		return taskDetailDao.getActors(leader);
	}

	public void updateTaskActors(String jobTaskId, String actors) {
		taskDetailDao.updateTaskActors(jobTaskId,actors);
	}
}
