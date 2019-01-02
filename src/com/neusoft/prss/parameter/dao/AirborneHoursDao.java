package com.neusoft.prss.parameter.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.parameter.entity.AirborneHours;

@MyBatisDao
public interface AirborneHoursDao extends BaseDao{
	
	public List<Map<String, Object>> getAirport();
	
	public List<Map<String, Object>> getActType();
	
	public int getListCount(Map<String, Object> param);
	
	public List<Map<String, Object>> getDataList(Map<String, Object> param);
	
	public int insertNewRecord(AirborneHours airborneHours);
	
	public int deleteRow(@Param("id")List<String> id);
	
	public void updateOperType(@Param("id")List<String> id);
	
	public int editRecord(AirborneHours airborneHours);
//	public int compute(Map<String,Object> map);
	public String compute(@Param("id")String id,@Param("i")Integer i);
	
	public JSONObject getRowById(@Param("id") String id);

}
