package com.neusoft.prss.statisticalanalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.statisticalanalysis.dao.KCQJFeeScaleDao;

@Service
public class KCQJFeeScaleService extends BaseService{
	
	@Autowired
	private KCQJFeeScaleDao kcqjFeeScaleDao;
	
	public  Map<String,Object> getDataList(Map<String,Object> param){
		Map<String,Object> result = new HashMap<String,Object>();
		int total= kcqjFeeScaleDao.getListCount(param);
		List<Map<String, String>> rows= kcqjFeeScaleDao.getDataList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
	}
	
	public boolean save(JSONObject o){
		int count=kcqjFeeScaleDao.save(o);
		if(count==1){
			return true;
		}else{
			return false;
		}
	}
	public JSONArray getDataById(String id){
		return kcqjFeeScaleDao.getDataById(id);
	}
	
	public boolean delBillById(String id){
		int count=kcqjFeeScaleDao.delBillById(id);
		if(count==1){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean saveEdit(JSONObject o){
		int count=kcqjFeeScaleDao.saveEdit(o);
		if(count==1){
			return true;
		}else{
			return false;
		}
	}
	
	public JSONArray getActTypeByAlnCode(String alnCode,String id){
		String[] actTypeArr=kcqjFeeScaleDao.getActTypeByAlnCode(alnCode,id);
		JSONArray result=new JSONArray();
		if(actTypeArr.length!=0){
			String actTypeStr=actTypeArr[0];
			String[] arr=actTypeStr.split(",");
			for(int i=0;i<arr.length;i++){
				JSONObject o=new JSONObject();
				o.put("id", arr[i]);
				o.put("text", arr[i]);
				result.add(o);
			}
		}
		return result;
	}
}
