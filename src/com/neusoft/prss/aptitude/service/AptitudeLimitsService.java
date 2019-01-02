/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月29日 上午11:35:03
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.aptitude.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.aptitude.dao.AreaConfDao;
import com.neusoft.prss.aptitude.entity.AreaConfEntity;
import com.neusoft.prss.aptitude.entity.AreaElements;
import com.neusoft.prss.aptitude.entity.OfficeLimConf;

@Service
@Transactional(readOnly = true)
public class AptitudeLimitsService extends BaseService{
	
	@Autowired
	private AreaConfDao areaConfDao;
	
	public JSONArray getOffice(String officeId) {
		return areaConfDao.getOffice(officeId);
	}
	
	public List<String> getOfficeId(String officeId) {
		return areaConfDao.getOfficeId(officeId);
	}
	
	public List<AreaConfEntity> getAreaList(List<String> officeId) {
		return areaConfDao.getAreaListByOfficeId(officeId);
	}
	
	public OfficeLimConf getOfficeLim(String officeId) {
		return areaConfDao.getOfficeLim(officeId);
	}
	
	public void updateOfficeLim(OfficeLimConf officeLimConf) {
		areaConfDao.delOfficeLim(officeLimConf.getOfficeId());
		areaConfDao.doInsterOfficeLim(officeLimConf);
	}
	
	public void delArea(String id) {
		areaConfDao.delAreaById(id);
		areaConfDao.delAreaElements(id);
	}
	
	public JSONObject getOfficeInfo(String officeId) {
		return areaConfDao.getOfficeInfo(officeId);
	}
	
	public JSONArray getAllJW() {
		return areaConfDao.getAllJW();
	}
	
	public JSONArray getAllJX() {
		JSONArray result = new JSONArray();
		List<String> list = areaConfDao.getAllJX();
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i));
		}
		return result;
	}
	
	public JSONArray getAllHS() {
		JSONArray result = new JSONArray();
		List<String> list = areaConfDao.getAllHS();
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i));
		}
		return result;
	}
	
	public void doInsertAreaConf(AreaConfEntity areaConfEntity, String info) {
		areaConfDao.doInsertAreaConf(areaConfEntity);
		List<String> infoList = Arrays.asList(info.split(","));
		List<AreaElements> areaElementsList = new ArrayList<AreaElements>();
		for(int i=0; i<infoList.size(); i++){
			AreaElements areaElements = new AreaElements();
			areaElements.setId(areaConfEntity.getId());
			areaElements.setElementCode(infoList.get(i));
			areaElements.setUpdateTime(areaConfEntity.getCreateTime());
			areaElementsList.add(areaElements);
		}
		areaConfDao.doInsertAreaElements(areaElementsList);
	}
	
	public int getSeq() {
		return areaConfDao.getSeq();
	}
	
	public AreaConfEntity getAreaById(String id) {
		return areaConfDao.getAreaById(id);
	}
	
	public JSONArray getAreaElementsById(String id) {
		JSONArray result = new JSONArray();
		List<String> list = areaConfDao.getAreaElementsById(id);
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i));
		}
		return result;
	}
	
	public JSONArray getSelect(String type, String id) {
		return areaConfDao.getSelect(type, id);
	}
	
	public boolean isUsed(String id) {
		int num = areaConfDao.getNumOfChild(id);
		if(num != 0){
			return true;
		}else{
			return false;
		}
	}
	
	public void updateArea(AreaConfEntity areaConfEntity, String info) {
		areaConfDao.updateArea(areaConfEntity);
		areaConfDao.delAreaElements(areaConfEntity.getId());
		List<String> infoList = Arrays.asList(info.split(","));
		List<AreaElements> areaElementsList = new ArrayList<AreaElements>();
		for(int i=0; i<infoList.size(); i++){
			AreaElements areaElements = new AreaElements();
			areaElements.setId(areaConfEntity.getId());
			areaElements.setElementCode(infoList.get(i));
			areaElements.setUpdateTime(areaConfEntity.getCreateTime());
			areaElementsList.add(areaElements);
		}
		areaConfDao.doInsertAreaElements(areaElementsList);
	}
	
	public JSONArray getJWElementsById(String id) {
		return areaConfDao.getJWElementsById(id);
	}
	
}
