/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午4:39:47
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface ClassGroupDao extends BaseDao {
	JSONArray getListData(@Param("officeId") String officeId);

	JSONArray getMembersByDeptid(@Param("deptid") String deptid);
	
	JSONArray getMembersByDeptidCgid(@Param("deptid") String deptid,@Param("teamid") String teamid);
	
	JSONArray getUnselectedByDeptid(@Param("deptid") String deptid);

	JSONArray getMembersBycgid(@Param("teamid") String teamid);

	void insertGroupInfo(Map<String, String> parms);

	void insertWorkerRel(List<Map<String, String>> parmList);

	String getCgId();

	JSONObject getGroupInfoByid(@Param("teamid") String teamid);

	void delGroupInfo(@Param("groupid") String groupid);

	void delGroupWorkerRel(@Param("groupid") String groupid);
	
	String getCgNameCount(@Param("cgname") String cgname);
}
