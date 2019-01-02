package com.neusoft.prss.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface TaskDetailDao extends BaseDao  {

	JSONObject getTaskDetail(@Param(value="jobTaskId")String jobTaskId);

	List<String> getActors(@Param(value="leader")String leader);

	void updateTaskActors(@Param(value="jobTaskId")String jobTaskId, @Param(value="actors")String actors);

}
