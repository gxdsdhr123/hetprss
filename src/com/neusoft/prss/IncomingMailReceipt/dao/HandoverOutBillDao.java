package com.neusoft.prss.IncomingMailReceipt.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.IncomingMailReceipt.entity.HandoverBill;
/**
 * 出港邮货交接单DAO层
 * @author lwg
 * @date 2018/01/05
 */
@MyBatisDao
public interface HandoverOutBillDao {
	
	List<Map<String, Object>> getOutData(Map<String, Object> map);

	List<Map<String, Object>> getOutDetailData(Map<String, Object> map);

	int saveFlight(HandoverBill handoverBill);

	List<Map<String, Object>> getAirFligthInfo(Map<String, Object> map);

	int saveGoods(Map<String, Object> goodsMap);

	int saveAtta(Map<String, Object> attaMap);

	int delAttaData(Map<String, Object> map);

	int delGoodsData(Map<String, Object> map);

	int delMain(Map<String, Object> map);

	int updateMainData(Map<String, Object> map);
}
