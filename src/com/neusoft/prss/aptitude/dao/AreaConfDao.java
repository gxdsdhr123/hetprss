/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月29日 下午2:47:19
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.aptitude.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.aptitude.entity.AreaConfEntity;
import com.neusoft.prss.aptitude.entity.AreaElements;
import com.neusoft.prss.aptitude.entity.OfficeLimConf;

@MyBatisDao
public interface AreaConfDao extends BaseDao{
	
	List<AreaConfEntity> getAreaListByOfficeId(List<String> officeId);
	
	JSONArray getOffice(@Param("officeId") String officeId);
	
	JSONObject getOfficeInfo(@Param("officeId") String officeId);
	
	List<String> getOfficeId(@Param("officeId") String officeId);
	
	OfficeLimConf getOfficeLim(@Param("officeId") String officeId);
	
	void delOfficeLim(@Param("officeId") String officeId);
	
	void doInsterOfficeLim(OfficeLimConf officeLimConf);
	
	AreaConfEntity getAreaById(@Param("id") String id);
	
	void delAreaById(@Param("id") String id);
	
//	void doInsterAreaHis(AreaConfEntity areaConfEntity);
	
	List<String> getAllJX();
	
	List<String> getAllHS();
	
	JSONArray getAllJW();
	
	void doInsertAreaConf(AreaConfEntity areaConfEntity);
	
	int getSeq();//取序列
	
	void doInsertAreaElements(List<AreaElements> areaElements);
	
	List<String> getAreaElementsById(@Param("id") String id);
	
	JSONArray getJWElementsById(@Param("id") String id);
	
	JSONArray getSelect(@Param("type") String type, @Param("id") String id);
	
	void delAreaElements(@Param("id") String id);
	
	int getNumOfChild(@Param("id") String id);
	
	void updateArea(AreaConfEntity areaConfEntity);

}
