/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月25日 下午5:01:49
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.workflow.dao.LogisticDao;

@Service
public class LogisticService extends BaseService {
    @Autowired
    private LogisticDao logisticDao;

    public JSONArray getListData(String kindMainId) {
        return logisticDao.getListData(kindMainId);
    }

    public JSONArray getAllReskind() {
        return logisticDao.getAllReskind();
    }

    @Transactional(readOnly = false)
    public void deleteKind(String reskind) {
        logisticDao.deleteTypeByKindId(reskind);
        logisticDao.deleteKind(reskind);
    }

    public void deleteType(String reskind,String restype) {
        logisticDao.deleteType(reskind, restype);
    }

    public void saveKind(String kindid,String reskind,String kindname,String deptId,String depname,String tab) {
        logisticDao.saveKind(kindid, reskind, kindname, deptId, depname, tab);
    }

    public void updateKind(String kindid,String reskind,String kindname,String deptId,String depname,String tab) {
        logisticDao.updateKind(kindid, reskind, kindname, deptId, depname, tab);
    }

    public void saveType(String typeid,String kindname,String restype,String typename,String displayname,
            String kindcode,String bindCar) {
        logisticDao.saveType(typeid, kindname, restype, typename, displayname, kindcode, bindCar);
    }

    public void updateType(String typeid,String kindname,String restype,String typename,String displayname,
            String kindcode,String bindCar) {
        logisticDao.updateType(typeid, kindname, restype, typename, displayname, kindcode, bindCar);
    }

    public JSONObject getTypeByTypeid(String typeId) {
        return logisticDao.getTypeByTypeid(typeId);
    }

    public JSONObject getKindByKindid(String kindId) {
        return logisticDao.getKindByKindid(kindId);
    }

    public JSONArray getDeptHasNoSon() {
        return logisticDao.getDeptHasNoSon();
    }

    public JSONArray getWorkFlowByTypeid(String typeMainId) {
        return logisticDao.getWorkFlowByTypeid(typeMainId);
    }

    public JSONArray vaildOnlyRestype(String restype) {
        return logisticDao.vaildOnlyRestype(restype);
    }

    public JSONArray vaildOnlyReskind(String reskind) {
        return logisticDao.vaildOnlyReskind(reskind);
    }

    public String getKindId() {
        return logisticDao.getKindId();
    }

    public String getTypeId() {
        return logisticDao.getKindId();
    }

    public void insertOfficeLimit(String officeId) {
        logisticDao.insertOfficeLimit(officeId);
    }
}
