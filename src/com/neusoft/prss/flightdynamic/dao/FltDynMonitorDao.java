package com.neusoft.prss.flightdynamic.dao;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface FltDynMonitorDao {

	/**
	 * 获取航班监控数据
	 * @param sql
	 * @return
	 */
	JSONArray getFltData(@Param(value="sql")String sql);
	
}
