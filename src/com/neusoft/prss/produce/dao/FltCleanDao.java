package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.produce.entity.FltCleanServiceEntity;

@MyBatisDao
public interface FltCleanDao {
	/**
	 * 获取航空器客舱保洁服务记录表数据总数
	 */
	int getDataCount(Map<String, Object> map);
	
	/**
	 * 获取航空器客舱保洁服务记录表数据
	 */
	List<JSONObject> getDataList(Map<String, Object> param);
	
	/**
	 * 根据id获取航空器客舱保洁服务记录表数据
	 */
	JSONObject getDataById(@Param("id")String id);
	
	/**
	 * 获取航空器客舱保洁服务记录表导出数据
	 */
	List<Map<String ,String>> getDownDataList(Map<String, Object> param);
	
	/**
	 * 根据航班日期、航班号、进出港标识，获取航班动态详细信息
	 */
	JSONObject getFlightDetail(Map<String, Object> param);

	/**
	 * 获取用户人员信息
	 */
	JSONArray getSysUser();
	
	/**
	 * 保存航空器客舱保洁服务记录表数据
	 */
	int save(List<FltCleanServiceEntity> saveDataList);

	/**
	 * 删除航空器客舱保洁服务记录表数据
	 */
	int delete(@Param("id")String id);

	/**
	 * 更新航空器客舱保洁服务记录表数据
	 */
	int update(FltCleanServiceEntity dataObj);
}
