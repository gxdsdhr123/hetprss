package com.neusoft.prss.produce.enterPortLuggage.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.enterPortLuggage.dao.EnterPortLuggageDao;

/**
 * 进港行李交接单
 * @author douxf
 */
@Service
@Transactional(readOnly = true)
public class EnterPortLuggageServiceImpl implements EnterPortLuggageService{

	@Autowired
	EnterPortLuggageDao enterPortLuggageDao; 
	
	@Override
	public Map<String, Object> listBillList(Map<String,Object> param) {
		//bootstrap-table要求服务器返回的json须包含：totlal，rows
		Map<String,Object> result = new HashMap<String,Object>();
		int total= enterPortLuggageDao.listBillListCount(param);
		JSONArray rows = enterPortLuggageDao.listBillList(param);
		for(int i=0;i<rows.size();i++){
			JSONObject job  = rows.getJSONObject(i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startTime = sdf.format(job.get("CREATE_DATE"));
			job.put("CREATE_DATE", startTime);
		}
		result.put("total",total);
        result.put("rows",rows);
		return result;
	}

	@Override
	public int saveBillGoos(List<Map<String, Object>> list,Map<String,Object> bgh) {
		
		enterPortLuggageDao.saveBJH(bgh);
		int idFake = Integer.parseInt((String) bgh.get("id"));
		int id = idFake+1;
		
		int reteurn = 0;
		for(int i=0;i<list.size();i++){
			Map<String,Object> param =  list.get(i);
			param.put("billId", id);
			reteurn  = enterPortLuggageDao.saveBillGoos(param);
		}
		
		return reteurn;
	}

	@Override
	public List<Map<String, Object>> selChaLi() {
		List<Map<String, Object>> list =  enterPortLuggageDao.selChaLi();
		return list;
	}

	@Override
	public Map<String, Object> findInfo(Map<String, Object> parem) {
		String str = String.valueOf(parem.get("flightDate"));
		String newStr =  str.replace("-","");
		parem.put("flightDate", newStr);
		Map<String, Object> result =  enterPortLuggageDao.findInfo(parem);
		return result;
	}

	@Override
	public Map<String, Object> findMainI(String mainId) {
		return enterPortLuggageDao.findMainI(mainId);
		
	}

	@Override
	public List<Map<String, Object>> findB(String id) {
		List<Map<String, Object>> map = enterPortLuggageDao.findB(id);
		for(int i=0;i<map.size();i++){
		 	Map<String,Object> tableB = map.get(i);
			String neihan = String.valueOf(tableB.get("INCGOODS"));
			int length = neihan.length();
			int start = 1;
			int end = length-1;
			String newStr =  neihan.substring(start, end);
			String newNeihan =  newStr.replaceAll("\"","");  
			tableB.put("INCGOODS", newNeihan);
		}
		
		return map;
		 
	}

	@Override
	public int updateBillGoos(List<Map<String, Object>> list, Map<String, Object> bgh) {
		
		int resultII = 0;
		String id =  String.valueOf(bgh.get("id"));
		int resultI = enterPortLuggageDao.updateMain(bgh);
		
//		int num = -1;
//		int sum = list.size();
		for(int i=0;i<list.size();i++){
//			num++;
			Map<String,Object> parem =  list.get(i);
			String str = String.valueOf(parem.get("incGoods")).substring(0, 1);
			if(!"[".equals(str)){
				String nh =  String.valueOf(parem.get("incGoods"));
				String newStr =  nh.replace(",", "\",\"");
				StringBuffer builder = new StringBuffer(newStr);
				StringBuffer newBuilder =  builder.insert(0,"[\"");
				String newIncGoods = newBuilder.append("\"]").toString();
				parem.put("incGoods", newIncGoods);
			}
			/*if(num < sum){
				resultII = enterPortLuggageDao.updateGoods(parem);
				if(resultII==0){
					parem.put("billId", id);
					resultII = enterPortLuggageDao.saveBillGoos(parem);
				}
			}*/
			parem.put("billId", id);
			resultII = enterPortLuggageDao.saveBillGoos(parem);
			
		}
		int result = resultI + resultII;
		
		
		return result;
	}

	@Override
	public int delDoods(String id) {
		int resultI = enterPortLuggageDao.delDoods(id);
		int resultII = enterPortLuggageDao.delDoodsMain(id);
		int result = resultI + resultII;
		return result;
	}

	@Override
	public int delDoodsMain(String id) {
		return enterPortLuggageDao.delDoodsMain(id);
	}

	@Override
	public String findMainAndB(String id) {
		String num = enterPortLuggageDao.findMainAndB(id);
		return num;
	}

	@Override
	public int delgoodsB(String id) {
		return enterPortLuggageDao.delgoodsB(id);
		
	}

	@Override
	public List<Map<String, Object>> getReceiverListDou() {
		return enterPortLuggageDao.getReceiverListDou();
	}

	@Override
	public String countGulf(Map<String, Object> parem) {
		return enterPortLuggageDao.countGulf(parem);
	}

}











