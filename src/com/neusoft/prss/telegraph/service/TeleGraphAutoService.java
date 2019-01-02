package com.neusoft.prss.telegraph.service;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.common.dao.ParamCommonDao;
import com.neusoft.prss.telegraph.dao.TeleGraphAutoDao;

@Transactional(readOnly = true)
@Service
public class TeleGraphAutoService {

	@Resource
	private TeleGraphAutoDao teleGraphAutoDao;
	
	@Resource
    private ParamCommonDao paramCommonDao;

    public Map<String,Object> getAutoList(JSONObject param) {
      //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= teleGraphAutoDao.getAutoListCount(param);
        JSONArray rows=teleGraphAutoDao.getAutoList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

    public JSONArray getEventList() {
        return teleGraphAutoDao.getEventList();
    }

    public JSONArray getAirlineList() {
        return teleGraphAutoDao.getAirlineList();
    }

    public JSONObject queryAutoById(String id) {
        return teleGraphAutoDao.queryAutoById(id);
    }

    public JSONArray getTelegraphTempl(JSONObject param) {
        return teleGraphAutoDao.getTelegraphTempl(param);
    }
    @Transactional(readOnly = false)
    public void insertAutoInfo(JSONObject vo) {
        String id = teleGraphAutoDao.getTelegraphAutoId();
        vo.put("id", id);
        String expression = vo.getString("expression");
        if(expression != null && !"".equals(expression)){
            String ruleId = paramCommonDao.getAutoRuleId();
            vo.put("ruleId", ruleId);
            paramCommonDao.insertAutoRule(vo);
        }
        teleGraphAutoDao.insertAuto(vo);
    }

    public void updateAutoInfo(JSONObject vo) {
        String expression = vo.getString("expression");
        String ruleId = vo.getString("ruleId");
        if(expression != null && !"".equals(expression)){
            if(ruleId != null && !"".equals(ruleId)){
                paramCommonDao.updateAutoRule(vo);
            } else {
                vo.put("ruleId", paramCommonDao.getAutoRuleId());
                paramCommonDao.insertAutoRule(vo);
            }
        } else{
            if(ruleId != null && !"".equals(ruleId)){
                String[] arr = {vo.getString("id")};
                vo.put("ruleId", null);
                paramCommonDao.deleteAutoRuleList(arr);
            }
        }
        teleGraphAutoDao.updateAuto(vo);
    }

    public String getTelegraphAutoId() {
        return teleGraphAutoDao.getTelegraphAutoId();
    }

    public String getAutoRuleId() {
        return teleGraphAutoDao.getAutoRuleId();
    }

    @Transactional(readOnly=false)
    public void deleteList(String[] ids) {
        try {
            
        teleGraphAutoDao.deleteAutoRuleList(ids);
        teleGraphAutoDao.deleteAutoList(ids);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateManualInfo(JSONObject vo) {
        teleGraphAutoDao.updateManualInfo(vo);
    }

    public void insertManualInfo(JSONObject vo) {
        teleGraphAutoDao.insertManualInfo(vo);
    }

    public void updateManualStop(JSONObject json) {
        teleGraphAutoDao.updateManualStop(json);
    }

    public JSONObject getFltInfo(String fltid) {
        return teleGraphAutoDao.getFltInfo(fltid);
    }

    public JSONObject getSendAuto(String fltid) {
        return teleGraphAutoDao.getSendAuto(fltid);
    }
	
	
}
