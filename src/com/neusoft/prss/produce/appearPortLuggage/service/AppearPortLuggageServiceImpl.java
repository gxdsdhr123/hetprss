package com.neusoft.prss.produce.appearPortLuggage.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.appearPortLuggage.dao.AppearPortLuggageDao;

/**
 * 
 * 出港行李交接单服务层
 * @author douxf
 *
 */
@Service
@Transactional(readOnly = true)
public class AppearPortLuggageServiceImpl implements AppearPortLuggageService{

	@Autowired
	AppearPortLuggageDao appearPortLuggageDao;
	
	@Override
	public Map<String, Object> listDouList(Map<String, Object> param) {
		//bootstrap-table要求服务器返回的json须包含：totlal，rows
		Map<String,Object> result = new HashMap<String,Object>();
		int total = appearPortLuggageDao.listDouCount(param);
		JSONArray rows = appearPortLuggageDao.listDou(param);
		for(int i=0;i<rows.size();i++){
			JSONObject job  = rows.getJSONObject(i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startTime = sdf.format(job.get("CREATE_DATE"));
			job.put("CREATE_DATE", startTime);
		}
		result.put("total", total);
		result.put("rows", rows);
		return result;
	}

	@Override
	public int saveMain(Map<String, Object> param,List<Map<String,Object>> list) {
		int resultI = 0;
		int resultII = 0;
		resultI = appearPortLuggageDao.saveMain(param);
		int idFake = Integer.parseInt((String)param.get("id"));
		int billId = idFake + 1;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map =  list.get(i);
			map.put("billId", billId);
			resultII = appearPortLuggageDao.saveB(map);
		}
		int result = resultI + resultII;
		return result;
	}

	@Override
	public int delMainAndB(String id) {
		int resultI = appearPortLuggageDao.delMian(id);
		int resultII = appearPortLuggageDao.delB(id);
		int result = resultI + resultII;
		return result;
	}
	
	@Override
	public int delB(String id) {
		int result = appearPortLuggageDao.delB(id);
		return result;
	}

	@Override
	public Map<String, Object> selMain(String id) {
		return appearPortLuggageDao.selMain(id);
		
	}

	@Override
	public List<Map<String, Object>> selB(String id) {
		return appearPortLuggageDao.selB(id);
	}

	@Override
	public int updateMinaB(Map<String, Object> param, List<Map<String, Object>> list) {
		int resultI = appearPortLuggageDao.revampMain(param);
		int resultII = 0;
		String id = String.valueOf(param.get("id"));
		for(int i=0;i<list.size();i++){
			Map<String,Object> map =  list.get(i);
			map.put("billId", id);
			appearPortLuggageDao.saveB(map);
		}
		int result = resultI + resultII;
		return result;
	}

	@Override
	public int delBGufl(String id) {
		return appearPortLuggageDao.delBGufl(id);
		
	}

	@Override
	public String countAppGufl(Map<String, Object> param) {
		return appearPortLuggageDao.countAppGufl(param);
		
	}

}
