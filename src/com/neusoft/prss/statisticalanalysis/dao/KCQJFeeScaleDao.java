package com.neusoft.prss.statisticalanalysis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface KCQJFeeScaleDao {
	
	int getListCount(Map<String,Object> param);
	
	List<Map<String, String>> getDataList(Map<String,Object> param);
	
	int save(JSONObject o);
	
	JSONArray getDataById(@Param("id") String id);
	
	int delBillById(@Param("id") String id);
	
	int saveEdit(JSONObject o);
	
	String[] getActTypeByAlnCode(@Param("alnCode") String alnCode,@Param("id") String id);

}
