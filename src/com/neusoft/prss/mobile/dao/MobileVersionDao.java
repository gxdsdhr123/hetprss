/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月29日 下午4:38:59
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
public interface MobileVersionDao extends BaseDao {
	
	JSONArray getMobileVersionDate();
	
	JSONObject getMobileVersionById(@Param("id") String id);
	
	String getMaxVersion();
	
	void delVersion(@Param("id") String id);
	
	void saveVersion(@Param("versionDate") JSONObject versionDate);
	
	void insertVersion(@Param("versionDate") JSONObject versionDate, @Param("id") String id);
	
	String getFileName(@Param("id") String id);
	
	String getFileId(@Param("id") String id);

}
