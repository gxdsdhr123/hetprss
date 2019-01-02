package com.neusoft.prss.telegraph.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.telegraph.dao.TeleGraphTemplDao;

@Transactional(readOnly = true)
@Service
public class TeleGraphTemplService {

	@Resource
	private TeleGraphTemplDao teleGraphTemplDao;
	
	public JSONArray getTree(){
		return teleGraphTemplDao.getTree();
	}
	
	 public Map<String,Object> getTelegraphList(Map<String, Object> param) {
		 //bootstrap-table要求服务器返回的json须包含：totlal，rows
         Map<String,Object> result = new HashMap<String,Object>();
         int total= teleGraphTemplDao.getTelegraphListCount(param);
         JSONArray rows=teleGraphTemplDao.getTelegraphList(param);
         result.put("total",total);
         result.put("rows",rows);
         return result;
	 }

    public JSONObject queryTelegraphById(String id) {
        return teleGraphTemplDao.queryTelegraphById(id);
    }
    
    @Transactional(readOnly=false)
    public void deleteList(String ids) {
        teleGraphTemplDao.deleteTemplList(ids);
        teleGraphTemplDao.deleteTemplFromList(ids);
        teleGraphTemplDao.deleteTemplToList(ids);
    }

    public JSONArray getTypeList() {
        return teleGraphTemplDao.getTypeList();
    }

    public List<CommonVO> getProcvars() {
        return teleGraphTemplDao.getProcvars();
    }

    public JSONObject getTelegraphTemplById(String id) {
        return teleGraphTemplDao.getTelegraphTemplById(id);
    }

    public JSONArray getSiteList(Map<String,String> map) {
        return teleGraphTemplDao.getSiteList(map);
    }

    public String getTelegraphId() {
        return teleGraphTemplDao.getTelegraphId();
    }

    public void insertTelegraphAddress(JSONObject vo,JSONArray sendArray,JSONArray receiveArray) {
        teleGraphTemplDao.insertReceiver(vo,receiveArray);
        teleGraphTemplDao.inserSender(vo,sendArray);
    }

    @Transactional(readOnly=false)
    public void insertTelegraphInfo(JSONObject vo,JSONArray sendArray,JSONArray receiveArray) {
        teleGraphTemplDao.insertTelegraphInfo(vo);
        teleGraphTemplDao.insertReceiver(vo,receiveArray);
        teleGraphTemplDao.inserSender(vo,sendArray);
    }

    @Transactional(readOnly=false)
    public void updateTelegraphInfo(JSONObject vo,JSONArray sendArray,JSONArray receiveArray) {
        String id=vo.getString("id");
        teleGraphTemplDao.deleteSender(id);
        teleGraphTemplDao.deleteReveiver(id);
        teleGraphTemplDao.updateTelegraphInfo(vo);
        teleGraphTemplDao.insertReceiver(vo,receiveArray);
        teleGraphTemplDao.inserSender(vo,sendArray);
    }

    public JSONArray getSenderListById(String id) {
        return teleGraphTemplDao.getListById("sender",id);
    }

    public JSONArray getReveiverListById(String id) {
        return teleGraphTemplDao.getListById("receiver",id);
    }
    public JSONArray getAddressByTempId(String id) {
    	return teleGraphTemplDao.getAddressByTempId(id);
    }

    public JSONObject getTemplCount(String id) {
        return teleGraphTemplDao.getTemplCount(id);
    }

    public JSONArray getPriorityList() {
        return teleGraphTemplDao.getPriorityList();
    }

}
