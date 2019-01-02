package com.neusoft.prss.produce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.dao.QcczAssignReportDao;

@Service
@Transactional(readOnly = true)
public class QcczAssignReportService {

	@Autowired
	private QcczAssignReportDao qcczAssignReportDao;

	public Map<String, Object> getDataList(Map<String, Object> param) {
		//bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= qcczAssignReportDao.getListCount(param);
        List<Map<String, Object>> rows = qcczAssignReportDao.getList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
	}

	public HashMap<String, Object> getDataById(String id) {
		return qcczAssignReportDao.getDataById(id);
	}

	public JSONArray getFlightDetail(Map<String, Object> map) {
		JSONArray arr = qcczAssignReportDao.getFlightDetail(map);
		if(arr == null || arr.size() == 0){
			arr = qcczAssignReportDao.getFlightDetailPrssp(map);
		}
		return arr;
	}
	
	public JSONObject getTaskDetail(Map<String, Object> map) {
		JSONObject arr = qcczAssignReportDao.getTaskDetail(map);
		String nameId = qcczAssignReportDao.getOperator(map);
		if (arr != null && arr.size() != 0) {
			arr.put("OPERATOR", nameId);
		}
		if(arr == null || arr.size() == 0){
			arr = qcczAssignReportDao.getTaskDetailPrssp(map);
			String nameIdPrssp = qcczAssignReportDao.getOperatorPrssp(map);
			if (arr != null && arr.size() != 0) {
				arr.put("OPERATOR", nameIdPrssp);
			}
		}
		return arr;
	}

	public ArrayList<HashMap<String, Object>> getPeople() {
		return qcczAssignReportDao.getPeople();
	}

	public void save(Map<String, Object> map) {
		qcczAssignReportDao.saveReport(map);
		updatePeople(map);
	}

	public HashMap<String, Object> getDataDetail(Map<String, Object> param) {
		HashMap<String, Object> map = qcczAssignReportDao.getDataDetail(param);
		if(map == null || map.size() == 0){
			map = qcczAssignReportDao.getDataDetailPrssp(param);
		}
		return map;
	}

	public ArrayList<HashMap<String, Object>> getSelectPeople(String id) {
		return qcczAssignReportDao.getSelectPeople(id);
	}

	public void delete(String reportId) {
		qcczAssignReportDao.deleteReport(reportId);
		qcczAssignReportDao.deleteHis(reportId);
	}

	public void update(Map<String, Object> map) {
		qcczAssignReportDao.updateReport(map);
		updatePeople(map);
		
	}
	private void updatePeople(Map<String, Object> map){
		String reportId = map.get("id").toString();
		qcczAssignReportDao.deleteHis(reportId);
		Map<String,Object> fwtMap = new HashMap<String,Object>();
		if(map.get("fwt") != null){
			String[] str = map.get("fwt").toString().split(",");
			for (int i = 0; i < str.length; i++) {
				fwtMap.put("userId", str[i]);
				fwtMap.put("itemId","1");
				fwtMap.put("reportId",reportId);
				qcczAssignReportDao.saveHis(fwtMap);
			}
		}
		Map<String,Object> dtqjMap = new HashMap<String,Object>();
		if(map.get("dtqj") != null){
			String[] str = map.get("dtqj").toString().split(",");
			for (int i = 0; i < str.length; i++) {
				dtqjMap.put("userId", str[i]);
				dtqjMap.put("itemId","2");
				dtqjMap.put("reportId",reportId);
				qcczAssignReportDao.saveHis(dtqjMap);
			}
		}
		Map<String,Object> wsjqjMap = new HashMap<String,Object>();
		if(map.get("wsjqj") != null){
			String[] str = map.get("wsjqj").toString().split(",");
			for (int i = 0; i < str.length; i++) {
				wsjqjMap.put("userId", str[i]);
				wsjqjMap.put("itemId","3");
				wsjqjMap.put("reportId",reportId);
				qcczAssignReportDao.saveHis(wsjqjMap);
			}
		}
		Map<String,Object> zjczMap = new HashMap<String,Object>();
		if(map.get("zjcz") != null){
			String[] str = map.get("zjcz").toString().split(",");
			for (int i = 0; i < str.length; i++) {
				zjczMap.put("userId", str[i]);
				zjczMap.put("itemId","4");
				zjczMap.put("reportId",reportId);
				qcczAssignReportDao.saveHis(zjczMap);
			}
		}
		Map<String,Object> kcqjMap = new HashMap<String,Object>();
		if(map.get("kcqj") != null){
			String[] str = map.get("kcqj").toString().split(",");
			for (int i = 0; i < str.length; i++) {
				kcqjMap.put("userId", str[i]);
				kcqjMap.put("itemId","5");
				kcqjMap.put("reportId",reportId);
				qcczAssignReportDao.saveHis(kcqjMap);
			}
		}
	}

	public String getPeopleName(String id) {
		return qcczAssignReportDao.getPeopleName(id);
	}

}
