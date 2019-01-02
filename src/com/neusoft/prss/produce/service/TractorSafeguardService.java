package com.neusoft.prss.produce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.dao.TractorSafeguardDao;


@Service
public class TractorSafeguardService {
	private Logger logger=Logger.getLogger(TractorSafeguardService.class);
	
	@Autowired
	private TractorSafeguardDao tractorSafeguardDao;

	/**
	 * 获取基本信息
	 * 
	 * @param ruleId
	 * @return
	 */
	public JSONObject loadRuleInfo(String id) {
	    JSONObject info = tractorSafeguardDao.loadInfo(id);
		return info;
	}
	

	/**
	 * 获取航空公司数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> loadAirline() {
		List<Map<String, Object>> sysAirlineList = tractorSafeguardDao.loadAirline();
		return sysAirlineList;
	}


	/**
	 * 保存/更新
	 * 
	 * @param schema
	 *            1-新建、2-更新
	 * @param formData
	 * @param airplaneType
	 * @param airlineType
	 * @param condition
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public JSONObject save(JSONObject formData) throws Exception {
		JSONObject msg = new JSONObject();
		boolean ifInsert = false; // 是-更新 否-新建
		String id = formData.getString("id");
		if (StringUtils.isBlank(id)) {
			ifInsert = true;
		}

		if(ifInsert){
		    tractorSafeguardDao.insert(formData);
		} else {
		    tractorSafeguardDao.update(formData);
		}
		msg.put("code", 0);
		msg.put("msg", ifInsert ? "创建成功" : "更新成功");

		return msg;
	}

	
	/**
	 * 删除
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id) throws Exception {
		tractorSafeguardDao.delete(id);//
	}


    public JSONArray getDimData(JSONObject object) {
        return tractorSafeguardDao.getDimData(object);
    }


    public List<Map<String,Object>> loadAtcactype() {
        return tractorSafeguardDao.loadAtcactype();
    }


    public List<Map<String,Object>> loadApronType() {
//        List<Map<String, String>> result = new ArrayList<>();
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("CODE", "Y");
//        map.put("DESCRIPTION", "远");
//        result.add(map);
//        map = new HashMap<String, String>();
//        map.put("CODE", "N");
//        map.put("DESCRIPTION", "近");
//        result.add(map);
//        return result;
        return tractorSafeguardDao.loadAircraft();
    }


    public Map<String,Object> getList(Map<String,Object> param) {
            //bootstrap-table要求服务器返回的json须包含：totlal，rows
            Map<String,Object> result = new HashMap<String,Object>();
            int total= tractorSafeguardDao.getListCount(param);
            List<Map<String, Object>> rows = tractorSafeguardDao.getList(param);
            result.put("total",total);
            result.put("rows",rows);
            return result;
        }
}
