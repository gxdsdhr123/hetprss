/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午7:07:57
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.arrange.dao.AmbulatoryShiftsTypeDao;
import com.neusoft.prss.arrange.entity.AmbulatoryShiftsType;

import oracle.sql.DATE;

@Service
@Transactional(readOnly = true)
public class AmbulatoryShiftsTypeService extends BaseService{

	@Autowired
	private AmbulatoryShiftsTypeDao ambulatoryShiftsTypeDao;

	public List<AmbulatoryShiftsType> getAmbulatoryShiftsTypeList(String officeId) {
		return ambulatoryShiftsTypeDao.getAmbulatoryShiftsTypeList(officeId);
	}

	public List<AmbulatoryShiftsType> getASTypeById(String id) {
		return ambulatoryShiftsTypeDao.getASTypeById(id);
	}

	public void doInsertAST(String id, List<AmbulatoryShiftsType> list) {
		ambulatoryShiftsTypeDao.delAST(id);
		for (int i = 0; i < list.size(); i++) {
			ambulatoryShiftsTypeDao.doInsertAST(list.get(i));
		}
	}

	public int getSeq() {
		return ambulatoryShiftsTypeDao.getSeq();
	}

	public void delAST(String id) {
		ambulatoryShiftsTypeDao.delAST(id);
	}

	public JSONArray getFltByDay(String day) {
		JSONArray result = new JSONArray();
		List<String> list = ambulatoryShiftsTypeDao.getFltByDay(day);
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i));
		}
		JSONArray json = ambulatoryShiftsTypeDao.getFltNotDay(day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < json.size(); i++) {
			JSONObject jsonO = json.getJSONObject(i);
			if (jsonO.getString("PLANDATE") != null) {
				List<String> dateList = Arrays.asList(jsonO.getString("PLANDATE").split(","));
				for(int j=0; j<dateList.size(); j++){
					try {
						Date date = sdf.parse(dateList.get(j));
						Calendar cal = Calendar.getInstance();
				        cal.setTime(date);
				        int w = cal.get(Calendar.DAY_OF_WEEK)-1;
				        if(w==0){
				        	w = 7;
				        }
				        if(w==Integer.parseInt(day)){
				        	result.add(jsonO.getString("FLTNO"));
				        }
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

}
