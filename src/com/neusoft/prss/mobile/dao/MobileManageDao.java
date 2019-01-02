/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年2月4日 下午2:51:25
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.mobile.dao;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface MobileManageDao extends BaseDao {
	
	JSONArray getMobileManageDate();
	
	void delPDA(@Param("id") String id);
	
	JSONArray getPADStatus();
	
	JSONObject getPDAInfo(@Param("id") String id);
	
	void updatePDA(@Param("id") String id, @Param("officeId") String officeId, @Param("pdaNo") String pdaNo, @Param("imei") String imei, @Param("status") String status, @Param("userId") String userId);
	
	void addPDA(@Param("id") String id, @Param("officeId") String officeId, @Param("pdaNo") String pdaNo, @Param("imei") String imei, @Param("status") String status, @Param("userId") String userId);
	
	void addLog(@Param("id") String id, @Param("officeId") String officeId, @Param("pdaNo") String pdaNo, @Param("imei") String imei, @Param("status") String status, @Param("userId") String userId);
	
	String getId();
	
	JSONArray getLog(@Param("id") String id);

}
