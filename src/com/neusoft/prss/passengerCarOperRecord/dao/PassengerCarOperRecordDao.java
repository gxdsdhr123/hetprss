package com.neusoft.prss.passengerCarOperRecord.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.passengerCarOperRecord.entity.PassengerCarOperRecord;

/**
 * 客梯车操作记录
 * @author lwg
 * @date 2017/12/26
 */
@MyBatisDao
public interface PassengerCarOperRecordDao {

	/**
	 * 获取增加页面表格头
	 * @return
	 */
	List<Map<String, Object>> getAddHeaderData();

	/**
	 * 获取车辆信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getDeviceNumber(Map<String, Object> map);

	/**
	 * 获取操作人
	 * @return
	 */
	List<Map<String, Object>> getOperList();

	/**
	 * 保存勤务单数据
	 * @param passengerCarOperRecord
	 * @return
	 */
	int addDataHD(PassengerCarOperRecord passengerCarOperRecord);

	/**
	 * 获取最大id值
	 * @return
	 */
//	String getGoodsMaxId();

	/**
	 * 批量保存从表数据
	 * @param list
	 * @return
	 */
	int saveGoodsBatch(List<Map<String, Object>> list);

	/**
	 * 获取主表数据
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getMainData(Map<String, Object> map);

	/**
	 * 获取表格数据
	 * @param map
	 * @return
	 */
	List<Map<String, String>> getTableData(Map<String, Object> map);

	/**
	 * 修改主表数据
	 * @param map
	 * @return
	 */
	int updateMainData(Map<String, Object> map);

	/**
	 * 删除附表数据
	 * @param map
	 * @return
	 */
	int delGoods(Map<String, Object> map);

	/**
	 * 删除主表数据
	 * @param map
	 * @return
	 */
	int del(Map<String, Object> map);

	/**
	 * 获取航班信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getAirFligthInfo(Map<String, Object> map);

	/**
	 * 当前航班号是否已存在
	 * @param map
	 * @return
	 */
	String isExists(Map<String, Object> map);

	List<String> getFilePath(Map<String, Object> map);

	int getMainDataSize(Map<String, Object> map);

	Map<String, Object> getDetailData(Map<String, Object> map);

	Map<String, String> getCarData(Map<String, Object> map);

	Map<String, String> getCarTime(Map<String, Object> map);

	void updataOperator(Map<String, Object> map);

	void updataRemark(Map<String, Object> map);

	void updataWG(Map<String, Object> map);

	void updataHB(Map<String, Object> map);

	void updataSJ(Map<String, Object> map);

	void updataDG(Map<String, Object> map);

	void updataLT(Map<String, Object> map);

	void updataWS(Map<String, Object> map);

	void updataZD(Map<String, Object> map);

	void updataZJ(Map<String, Object> map);

	void updataMHQ(Map<String, Object> map);

	void deleteGoods(Map<String, Object> map);

	void updataQCD(Map<String, Object> map);

	void updataSCD(Map<String, Object> map);

	void insertOperator(Map<String, Object> map);

}
