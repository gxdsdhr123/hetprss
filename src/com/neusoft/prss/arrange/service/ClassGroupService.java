/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午4:37:21
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.fb303.FacebookService.Processor.getCounter;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.arrange.dao.ClassGroupDao;

import jdk.nashorn.internal.scripts.JS;

@Service
@Transactional(readOnly = true)
public class ClassGroupService extends BaseService {
	@Autowired
	private ClassGroupDao cgDao;

	public JSONArray getListData(String officeId) {
		return cgDao.getListData(officeId);
	}

	public JSONArray getMembersByDeptid(String deptid) {
		return cgDao.getMembersByDeptid(deptid);
	}

	public JSONArray getMembersByDeptidCgid(String deptid,String teamid){
		return cgDao.getMembersByDeptidCgid(deptid, teamid);
	}
	
	public JSONArray getUnselectedByDeptid(String deptid) {
		return cgDao.getUnselectedByDeptid(deptid);
	}
	
	public JSONArray getMembersBycgid(String cgid) {
		return cgDao.getMembersBycgid(cgid);
	}

	public String getCgId() {
		return cgDao.getCgId();
	}

	public JSONObject getGroupInfoByid(String groupid) {
		return cgDao.getGroupInfoByid(groupid);
	}
	
	public String getCgNameCount(String cgname){
		return cgDao.getCgNameCount(cgname);		
	}
	
	@Transactional(readOnly = false)
	public void savecg(Map<String, String> parms, List<Map<String, String>> parmList) {
		cgDao.insertGroupInfo(parms);
		cgDao.insertWorkerRel(parmList);
	}

	@Transactional(readOnly = false)
	public void delcg(String groupid) {
		cgDao.delGroupWorkerRel(groupid);
		cgDao.delGroupInfo(groupid);
	}

}
