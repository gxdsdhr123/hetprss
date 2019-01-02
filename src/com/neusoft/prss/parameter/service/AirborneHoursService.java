package com.neusoft.prss.parameter.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.parameter.dao.AirborneHoursDao;
import com.neusoft.prss.parameter.entity.AirborneHours;

@Service
@Transactional
public class AirborneHoursService  extends BaseService{
	
	@Autowired 
	private AirborneHoursDao airborneHoursDao;
	private Logger logger=Logger.getLogger(AirborneHoursService.class);
	
	public List<Map<String, Object>> getActType(){
		return airborneHoursDao.getActType();
	}
	
	public List<Map<String, Object>> getAirport(){
		return airborneHoursDao.getAirport();
	}
	
	public Map<String,Object> getDataList(Map<String,Object> param){
		Map<String,Object> result = new HashMap<String,Object>();
        int total= airborneHoursDao.getListCount(param);
        List<Map<String, Object>> rows = airborneHoursDao.getDataList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
	}
	public boolean insertNewRecord(AirborneHours airborneHours){
		int count=airborneHoursDao.insertNewRecord(airborneHours);
		if(count==1){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean deleteRow(List<String> id){
		try{
			airborneHoursDao.deleteRow(id);
		}catch(Exception e){
			logger.error("AirborneHoursService->deleteRow()"+e.getMessage());
			return false;
		}
			return true;
	}
	public boolean editRecord(AirborneHours airborneHours){
	int count=airborneHoursDao.editRecord(airborneHours);
	if(count==1){
		return true;
	}else{
		return false;
	}
	
}
	public boolean compute(String id,Integer i){
		List<String> idList=Arrays.asList(id.split(","));
		try{
			String count=airborneHoursDao.compute(id,i);
			airborneHoursDao.updateOperType(idList);
		}catch(Exception e){
			return false;
		}
			return true;
		
	}
	
	public JSONObject getRowById(String id){
		return airborneHoursDao.getRowById(id);
		
	}

}
