package com.neusoft.prss.equipmen.dao;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.TreeDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

/**
 * 设备资源接口
 * @author douxf
 * @version 2017-12-5
 */
@MyBatisDao
public interface EquipmentDao {
	JSONArray listFacility();
	JSONArray listFacilityPid();
	JSONArray listFacilityPidPid();
	
	int listFacilityCount(Map<String, Object> param);
	JSONArray listFacilityCountList(Map<String, Object> param);
	JSONArray listFacilityCountListScreen(Map<String, Object> param);
	
	List<Map<String,Object>> listFacilityII();
	/**
	 * 新增Ztree的选择
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> createType();
	/**
	 * 修改zTree
	 * @param map
	 * @return
	 */
	Map<String,Object> findEdit(String id);
	Map<String,Object> findEditDept(String id);
	
	
	int saveEquipment(Map<String,Object> map);
	int updateEquipment(Map<String,Object> map);
	int updateZtreeDept(Map<String,Object> map);
	int updateEquipmentDevice(Map<String,Object> map);
	int DelEquipment(Map<String,Object> map);
	int DelEquipmentDevice(Map<String,Object> map);
	int saveJmDevice(Map<String,Object> map);
	int updateRevamp(Map<String,Object> map);
	int delRevamp(String id);
	
	List<Map<String, Object>> equipmentFilterName();
	List<Map<String, Object>> equipmentFilterTypeName();
	List<Map<String, Object>> equipmentFilterTypeNameI();
	List<Map<String, Object>> equipmentFilterTypeNameII();
	
	Map<String,Object> compileFind(String id);
}
