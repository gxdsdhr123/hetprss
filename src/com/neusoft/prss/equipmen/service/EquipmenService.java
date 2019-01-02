package com.neusoft.prss.equipmen.service;

import com.alibaba.fastjson.JSONArray;
import java.util.List;
import java.util.Map;

public interface EquipmenService {
	
	JSONArray transform();
	/**
	 * 点击ZTREE选择
	 * @param param
	 * @return
	 */
	Map<String,Object> transformIV(Map<String, Object> param);
	/**
	 * 筛选框
	 * @return
	 */
	Map<String,Object> transformScreen(Map<String, Object> param);
	List<Map<String, Object>> transformII();
	int newSave(Map<String,Object> map);
	int newUpe(Map<String,Object> map);
	int newDel(Map<String,Object> map);
	List<Map<String,Object>> getselDeot();
	List<Map<String,Object>> equipmentFilterTypeName();
	List<Map<String,Object>> equipmentFilterTypeNameI();
	List<Map<String,Object>> equipmentFilterTypeNameII();
	int updateZtreeDept(Map<String, Object> map);
	/**
	 * 按钮增加
	 */
	int saveJmDevice(Map<String,Object> map);
	/**
	 * 按钮更新
	 */
	int updateRevamp(Map<String,Object> map);
	/**
	 * 按钮删除
	 */
	int delRevamp(String[] id);
	/**
	 * 显示修改页面的值
	 */
	Map<String,Object> compileFind(String id);
	/**
	 * 新增ztree
	 * @return
	 */
	List<Map<String,Object>> createType();
	/**
	 * 修改ztree节点
	 */
	Map<String,Object> findEdit(String id);
	Map<String,Object> findEditDept(String id);
	
	

}
