/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月15日 上午9:22:54
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.produce.entity.ChargeBillEntity;
import com.neusoft.prss.produce.entity.XMBillEntity;

@MyBatisDao
public interface ChargeBillDao extends BaseDao {
	
	JSONArray getChargeBillData(@Param(value="flag") String  flag);

	JSONObject getFightInfo(Map<String,String> param);

	JSONObject getBillSeq(@Param("flag") String flag);
	int doAddZPKTCZPFW(ChargeBillEntity vo);
	int delChargeBill(String billId);
	JSONObject getChargeBillDetail(Map<String,String> param);
	JSONArray getChargeBillGoodsDetail(Map<String,String> param);
	JSONObject getFlightInfo(String fltId);
	int delChargeGoodsBill(String billId);
	JSONObject getImgSrc(Map<String,String> param);
	int doAddChargeBill(List<XMBillEntity> list);

	int updateChargeBill(List<XMBillEntity> list);

	int doAddZTChargeBill(List<XMBillEntity> list);

	int doAddZPZTZPFW(ChargeBillEntity vo);

	int delZTChargeBill(String billId);

	int delZTChargeGoodsBill(String billId);

	int updateZTChargeBill(List<XMBillEntity> list);
	

}
