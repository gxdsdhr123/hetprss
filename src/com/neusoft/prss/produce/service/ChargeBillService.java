/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月15日 上午9:36:30
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.service;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.produce.dao.ChargeBillDao;
import com.neusoft.prss.produce.entity.ChargeBillEntity;
import com.neusoft.prss.produce.entity.XMBillEntity;

@Service
@Transactional(readOnly = true)
public class ChargeBillService extends BaseService {

	@Autowired
	private ChargeBillDao chargeBillDao;

	public JSONArray getChargeBillDate(String flag) {		
		return chargeBillDao.getChargeBillData(flag);
	}
	public JSONObject getFightInfo(Map<String,String> param) {
		return chargeBillDao.getFightInfo(param);
	}
	public JSONObject getBillSeq(String flag) {
		return chargeBillDao.getBillSeq(flag);
		
	}
	public int doAddZPKTCZPFW(ChargeBillEntity vo) {
		return chargeBillDao.doAddZPKTCZPFW(vo);		
	}
	public int delChargeBill(@Param("billId") String billId) {
		return chargeBillDao.delChargeBill(billId);		
	}
	public int delChargeGoodsBill(@Param("billId") String billId) {
		return chargeBillDao.delChargeGoodsBill(billId);		
	}
	public JSONObject getChargeBillDetail(Map<String,String> param) {
		return chargeBillDao.getChargeBillDetail(param);
	}
	public JSONArray getChargeBillGoodsDetail(Map<String,String> param) {
		return chargeBillDao.getChargeBillGoodsDetail(param);
	}
	public JSONObject getFlightInfo( @Param("fltId") String fltId) {
		return chargeBillDao.getFlightInfo(fltId);
	}

	public JSONObject getImgSrc(Map<String,String> param) {
		return chargeBillDao.getImgSrc(param);
	}
	public int doAddChargeBill(List<XMBillEntity> list) {
		return chargeBillDao.doAddChargeBill(list);
	}
	public int updateChargeBill(List<XMBillEntity> list) {
		return chargeBillDao.updateChargeBill(list);
	}
	public int doAddZTChargeBill(List<XMBillEntity> list) {
		return chargeBillDao.doAddZTChargeBill(list);
	}
	public int doAddZPZTZPFW(ChargeBillEntity vo) {
		return chargeBillDao.doAddZPZTZPFW(vo);
	}
	public int delZTChargeBill(String billId) {
		return chargeBillDao.delZTChargeBill(billId);
		
	}
	public int delZTChargeGoodsBill(String billId) {
		return chargeBillDao.delZTChargeGoodsBill(billId);
	}
	public int updateZTChargeBill(List<XMBillEntity> list) {
		return chargeBillDao.updateZTChargeBill(list);
	}
}
