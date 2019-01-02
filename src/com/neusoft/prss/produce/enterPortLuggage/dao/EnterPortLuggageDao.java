package com.neusoft.prss.produce.enterPortLuggage.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface EnterPortLuggageDao {
	JSONArray listBillList(Map<String,Object> param);
	int listBillListCount(Map<String,Object> param);
	/**
	 * 保存B表的表格数据
	 */
	int saveBillGoos(Map<String,Object> param);
	int saveBJH(Map<String,Object> bgh);
	/**
	 * 获取查理下拉选的数据
	 */
	List<Map<String,Object>> selChaLi();
	
	/**
	 * 自动填充表格
	 */
	Map<String,Object> findInfo(Map<String,Object> parem);
	/**
	 * 修改
	 */
	Map<String,Object> findMainI(String mainId);
	List<Map<String,Object>> findB(String id);
	/**
	 * 修改保存
	 */
	
	int updateMain(Map<String,Object> parem);
	int updateGoods(Map<String,Object> parem);
	/**
	 * 删除
	 */
	int delDoods(String id);
	int delDoodsMain(String id);
	/**
	 * 查询组合表的调解书
	 * @param id
	 * @return
	 */
	String findMainAndB(String id);
	/**
	 * 修改确认之前要删掉附表的数据
	 */
	int delgoodsB(String id);
	
	/**
	 * 获取货运司机列表
	 * @return
	 */
	List<Map<String, Object>> getReceiverListDou();
	/**
	 * 判断同一天是否出现相同的航班号
	 */
	String countGulf(Map<String,Object> parem);
	
	
}
