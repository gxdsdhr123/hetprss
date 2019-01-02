package com.neusoft.prss.scheduling.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface SchedulingChangeDao extends BaseDao {
	 List<Map<String,String>> getPlusInfo(@Param("fltid")String fltid);
	 
	 int saveZXInRemark(@Param("fltid") String fltid,@Param("inZxRemark") String inZxRemark);
	 
	 int saveZXInCargo(@Param("fltid") String fltid,@Param("inSpecialCargo") String inSpecialCargo);
	 
	 int saveZXOutBagReal(@Param("fltid") String fltid,@Param("outBaggageReal") String outBaggageReal);
	 
	 int saveZXOutLarge(@Param("fltid") String fltid,@Param("outLargeBaggage") String outLargeBaggage);
	 
	 int saveZXOutRemark(@Param("fltid") String fltid,@Param("outZxRemark") String outZxRemark);

	void savePlusData(JSONObject json);
}
