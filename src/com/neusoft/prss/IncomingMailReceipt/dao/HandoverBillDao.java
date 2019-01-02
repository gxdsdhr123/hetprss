package com.neusoft.prss.IncomingMailReceipt.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.IncomingMailReceipt.entity.HandoverBill;
/**
 * 进港邮货交接单DAO层
 * @author lwg
 * @date 2018/01/03
 */
@MyBatisDao
public interface HandoverBillDao {

	/**
	 * 获取查理
	 * @return
	 */
	List<Map<String, Object>> getCharlieList();

	/**
	 * 获取货运司机列表
	 * @return
	 */
	List<Map<String, Object>> getReceiverList();

	/**
	 * 保存航班信息
	 * @param handoverBill
	 * @return
	 */
	int saveFlight(HandoverBill handoverBill);

	
	
	/**
	 * 保存进港货邮物品
	 * @param goodsMap
	 * @return
	 */
	int saveGoods(Map<String, Object> goodsMap);

	/**
	 * 保存进港货邮接收签字表
	 * @param attaMap
	 * @return
	 */
	int saveAtta(Map<String, Object> attaMap);

	/**
	 * 获取进港货邮主表数据
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getInData(Map<String, Object> map);

	// ==================================进港货邮====================================
	/**
	 * 单据详情
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getInDetailData(Map<String, Object> map);

	/**
	 * 删除签署表
	 * @param map
	 * @return
	 */
	int delAttaData(Map<String, Object> map);

	/**
	 * 删除单据
	 * @param map
	 * @return
	 */
	int delGoodsData(Map<String, Object> map);

	/**
	 * 删除航班数据
	 * @param map
	 * @return
	 */
	int delMain(Map<String, Object> map);

	/**
	 * 获取进港航班
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getAirFligthInfo(Map<String, Object> map);

	/**
	 * 修改主表数据
	 * @param map
	 */
	int updateMainData(Map<String, Object> map);


}
