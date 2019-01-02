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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.arrange.dao.ClassGroupDao;
import com.neusoft.prss.arrange.dao.WorkGroupDao;

@Service
@Transactional(readOnly = true)
public class WorkGroupService extends BaseService {
	@Autowired
	private WorkGroupDao wgDao;

	public JSONArray getListData(String officeId) {
		return wgDao.getListData(officeId);
	}

	public JSONArray getMembersByDeptid(String deptid) {
		return wgDao.getMembersByDeptid(deptid);
	}

	public JSONArray getMembersByteamid(String teamid) {
		return wgDao.getMembersByteamid(teamid);
	}
	public JSONArray getUnselectedByDeptid(String teamid) {
		return wgDao.getUnselectedByDeptid(teamid);
	}

	public String getCgId() {
		return wgDao.getCgId();
	}

	public JSONObject getGroupInfoByid(String teamid) {
		return wgDao.getGroupInfoByid(teamid);
	}

	@Transactional(readOnly = false)
	public void savecg(Map<String, String> parms, List<Map<String, String>> parmList) {
		wgDao.insertTeamInfo(parms);
		wgDao.insertWorkerRel(parmList);
	}

	@Transactional(readOnly = false)
	public void delwg(String id) {
		wgDao.delTeamInfo(id);
		wgDao.delTeamWorkerRel(id);
	}

}
