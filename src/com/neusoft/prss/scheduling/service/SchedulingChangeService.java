package com.neusoft.prss.scheduling.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.scheduling.dao.SchedulingChangeDao;

/**
 * 航班动态列表双击单元格处理Service
 * @author baochl
 *
 */
@Service
public class SchedulingChangeService {
	
	@Autowired
	private SchedulingChangeDao schedulingChangeDao;
	
	
	public List<Map<String,String>> getPlusInfo(String fltid){
		return schedulingChangeDao.getPlusInfo(fltid);
	}
	
	public boolean saveZXInInfo(String fltid,String inSpecialCargo,String inZxRemark){
		try{
			schedulingChangeDao.saveZXInCargo(fltid, inSpecialCargo);
			schedulingChangeDao.saveZXInRemark(fltid, inZxRemark);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public boolean saveZXOutInfo(String fltid,String outBaggageReal,
			String outLargeBaggage,String outZxRemark){
		try{
			schedulingChangeDao.saveZXOutBagReal(fltid, outBaggageReal);
			schedulingChangeDao.saveZXOutLarge(fltid, outLargeBaggage);
			schedulingChangeDao.saveZXOutRemark(fltid, outZxRemark);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	public void savePlusData(JSONObject json) {
		schedulingChangeDao.savePlusData(json);
	}
	
}
