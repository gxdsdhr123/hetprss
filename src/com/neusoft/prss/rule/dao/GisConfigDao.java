package com.neusoft.prss.rule.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

/**
 * 电子围栏配置规则Dao
 */
@MyBatisDao
public interface GisConfigDao extends BaseDao {
	/**
	 * 获取电子围栏配置信息列表
	 */
	public List<JSONObject> getDataList(Map<String,Object> paramsMap);
	
	/**
	 * 获取电子围栏配置信息总数
	 */
	public int getDataCount(Map<String,Object> paramsMap);
	
	/**
	 * 保存电子围栏配置信息
	 */
	public int insertGisConfig(List<Map<String, Object>> dataList);
	
	/**
	 * 修改电子围栏配置信息
	 */
	public int updateGisConfig(Map<String, Object> gisData);
	
	/**
	 * 获取新增电子围栏配置、关系id
	 */
	public int getGisProcNodeRelId();
	
	/**
	 * 保存电子围栏配置关系
	 */
	public int insertGisProcNodeRel(List<Map<String, Object>> dataList);

	/**
	 * 删除电子围栏配置关系
	 */
	public int delGisProcNodeRel(@Param("ids")String ids);
	
	/**
	 * 删除电子围栏配置信息
	 */
	public int deleteGisConfig(@Param("ids")String ids);
	
	/**
	 * 获取保障类型选项
	 */
	public JSONArray getReskind();
	
	/**
	 * 获取作业类型选项
	 */
	public JSONArray getRestype(Map<String,Object> params);
	
	/**
	 * 获取流程选项
	 */
	public JSONArray getProcess(Map<String,Object> params);
	
	/**
	 * 获取节点选项
	 */
	public JSONArray getNode(Map<String,Object> params);
	
	/**
	 * 获取围栏信息
	 */
	public JSONArray getGisRailInfo();
	
	/**
	 * 一键生效、失效
	 */
	public int updateGisConfigInUse(@Param("inUse")String inUse);
}
