/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年2月4日 下午2:51:00
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.mobile.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.mobile.dao.MobileManageDao;

@Service
@Transactional(readOnly = true)
public class MobileManageService extends BaseService {
	
	@Autowired
    private MobileManageDao mobileManageDao;
	
	public JSONArray getMobileManageDate(){
		return mobileManageDao.getMobileManageDate();
	}
	
	public void delPDA(String id){
		mobileManageDao.delPDA(id);
	}
	
	public JSONArray getPADStatus(){
		return mobileManageDao.getPADStatus();
	}
	
	public JSONObject getPDAInfo(String id){
		return mobileManageDao.getPDAInfo(id);
	}
	
	public void savePDA(String id, String officeId, String pdaNo, String imei, String status){
		String userId = UserUtils.getUser().getId();
		if(StringUtils.isNotBlank(id) && !"undefined".equals(id)){
			JSONObject json = mobileManageDao.getPDAInfo(id);
			if(officeId.equals(json.getString("office")) && pdaNo.equals(json.getString("pdaNo")) && imei.equals(json.getString("imei")) && status.equals(json.getString("status"))){
				return;
			}
			mobileManageDao.updatePDA(id, officeId, pdaNo, imei, status, userId);
			mobileManageDao.addLog(id, officeId, pdaNo, imei, status, userId);
		} else {
			String newId = mobileManageDao.getId();
			mobileManageDao.addPDA(newId, officeId, pdaNo, imei, status, userId);
			mobileManageDao.addLog(newId, officeId, pdaNo, imei, status, userId);
		}
	}

}
