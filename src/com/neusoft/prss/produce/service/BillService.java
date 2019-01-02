/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月15日 上午9:36:30
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.produce.dao.BillDao;

@Service
@Transactional(readOnly = true)
public class BillService extends BaseService {

    @Autowired
    private BillDao billDao;

    public JSONArray getBillDate(String officeId, JobKind jobKind) {
        String jobkind = jobKind.getKindCode();
        String sql = "";
        if("JWQYC".equals(jobkind)) {
            JSONArray feeTable = billDao.getFeeTableByJobKind(jobkind);
            sql += "SELECT * FROM (";
            for (int i = 0; i < feeTable.size(); i++) {
                JSONObject json = feeTable.getJSONObject(i);
                sql += "SELECT 'jwqyc' type_code, ID, TO_CHAR(CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, flight_number, aircraft_number, "
                        + "item_name job_type, '" + json.getString("JOB_KIND")
                        + "' job_kind,'"+json.getString("DATA_TABLE")+"' DATA_TABLE,"
                        + " (SELECT NAME FROM Sys_User WHERE ID = OPERATOR) OPERATOR FROM "
                        + json.getString("DATA_TABLE");
                if (i != feeTable.size() - 1) {
                    sql += " UNION ALL ";
                }
            }
            sql += ") order by CREATE_DATE desc";
            sql += "";
        } else {
            JSONArray feeTable = billDao.getFeeTable(officeId);
            sql += "SELECT * FROM (";
            for (int i = 0; i < feeTable.size(); i++) {
                JSONObject json = feeTable.getJSONObject(i);
                sql += "SELECT '" + json.getString("TYPE_CODE")
                        + "' type_code, ID, TO_CHAR(CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, flight_number, aircraft_number, '"
                        + json.getString("JOB_TYPE") + "' job_type, '" + json.getString("JOB_KIND")
                        + "' job_kind,'"+json.getString("DATA_TABLE")+"' DATA_TABLE,"
                        + " (SELECT NAME FROM Sys_User WHERE ID = OPERATOR) OPERATOR FROM "
                        + json.getString("DATA_TABLE");
                if (i != feeTable.size() - 1) {
                    sql += " UNION ALL ";
                }
            }
            sql += ") order by CREATE_DATE desc";

        }
        
        return billDao.getBillData(sql);
    }

    public JSONArray getAlnBillData() {
        return billDao.getAlnBillData();
    }

    public List<Map<String,String>> getBillDateDou(String officeId) {
        JSONArray feeTable = billDao.getFeeTable(officeId);
        String sql = "";
        sql += "SELECT * FROM (";
        for (int i = 0; i < feeTable.size(); i++) {
            JSONObject json = feeTable.getJSONObject(i);
            sql += "SELECT '" + json.getString("TYPE_CODE")
                    + "' type_code, ID, TO_CHAR(CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, flight_number, aircraft_number, '"
                    + json.getString("JOB_TYPE") + "' job_type, '" + json.getString("JOB_KIND")
                    + "' job_kind, (SELECT NAME FROM Sys_User WHERE ID = OPERATOR) OPERATOR FROM "
                    + json.getString("DATA_TABLE");
            if (i != feeTable.size() - 1) {
                sql += " UNION ALL ";
            }
        }
        sql += ") order by CREATE_DATE desc";
        return billDao.getBillDataDouxf(sql);
    }

    public JSONArray getJobTypeByOfficeId(String officeId) {
        return billDao.getJobTypeByOfficeId(officeId);
    }

    public String getNextAlnBillId() {
        return billDao.getNextAlnBillId();
    }

    public JSONArray getTypeCodeByJobType(String type) {
        return billDao.getTypeCodeByJobType(type);
    }

    public JSONObject getBillInfo(String id,String type, String tableName) {
        JSONObject json = new JSONObject();
        String sql = "";
        if("jwqyc".equals(type)) {
            json.put("DATA_TABLE", tableName);
            json.put("JOB_TYPE", type);
        } else {
            json = billDao.getTableName(type);
        }
        if (type.equals("jwbdcbaiduche_free")) {
            sql = "SELECT ID, FLTID, FLIGHT_NUMBER, ACT_TYPE, AIRCRAFT_NUMBER, ITEM_NAME, ITEM_DATE, NUM, (select name from sys_user where id = OPERATOR) OPERATOR, SIGNATORY, to_char(CREATE_DATE,'hh24mi') CREATE_DATE, REMARK,SIGNATORY FROM "
                    + json.getString("DATA_TABLE") + " WHERE ID = " + id;
        }
        if (type.equals("jwbdckaosite_free") || type.equals("jwbdccanshengche_free") || type.equals("jwqcsbzljc_free")
                || type.equals("jwqcsbzqsc_free")//垃圾车 清水车 
                || type.equals("jwqcsbzwsc_free")//污水车
                || type.equals("jwqcczzgnqc_free") || type.equals("jwqcczzgjqc_free")//国内清舱 国际清舱
                || type.equals("jwqcczzsdqc_free") || type.equals("jwqcczzbp_free")// 深度清舱 补配
                || type.equals("jwqyctzfj_free") || type.equals("jwqycbltzfj_free")//牵引车拖拽飞机  牵引车抱轮拖拽飞机
                || type.equals("jwqycygtzfj_free") || type.equals("jwqyctfj_free")//牵引车有杆拖拽飞机  牵引车推飞机
                || type.equals("jwqycygtfj_free") || type.equals("jwqycbltfj_free")//牵引车有杆推飞机  牵引车抱轮推飞机
                || type.startsWith("jwqyc")
        ) {
            sql = "SELECT ID, FLTID, FLIGHT_NUMBER, ACT_TYPE, AIRCRAFT_NUMBER, ITEM_NAME, ITEM_DATE, START_TM, END_TM, NUM, (select name from sys_user where id = OPERATOR) OPERATOR, SIGNATORY, to_char(CREATE_DATE,'hh24mi') CREATE_DATE, REMARK,SIGNATORY ";
            if(type.startsWith("jwqyc"))
                sql += ",SIGN";
            sql += " FROM " + json.getString("DATA_TABLE") + " WHERE ID = " + id;
        }
        JSONObject result = billDao.getBillInfo(sql);
        result.put("JOB_TYPE", json.getString("JOB_TYPE"));
        result.put("TYPE_CODE", type);
        result.put("DATA_TABLE", tableName); 
        return result;
    }

    public JSONObject getAlnBillInfo(String id) {
        return billDao.getAlnBillInfo(id);
    }

    public List<Map<String,String>> getBillInfoDou(String id,String type, String tableName) {
        Map<String,String> json = new HashMap<String,String>();
        String sql = "";
        if("jwqyc".equals(type)) {
            json.put("DATA_TABLE", tableName);
            json.put("JOB_TYPE", type);
        } else {
            json = billDao.getTableNameDou(type);
        }
        
        if (type.equals("jwbdcbaiduche_free")) {
            sql = "SELECT ID, FLTID, FLIGHT_NUMBER, ACT_TYPE, AIRCRAFT_NUMBER, ITEM_NAME, ITEM_DATE, NUM, (select name from sys_user where id = OPERATOR) OPERATOR, SIGNATORY, to_char(CREATE_DATE,'hh24mi') CREATE_DATE, REMARK,SIGNATORY FROM "
                    + json.get("DATA_TABLE") + " WHERE ID = " + id;
        }
        if (type.equals("jwbdckaosite_free") || type.equals("jwbdccanshengche_free") || type.equals("jwqcsbzljc_free")
                || type.equals("jwqcsbzqsc_free")//垃圾车 清水车 
                || type.equals("jwqcsbzwsc_free")//污水车
                || type.equals("jwqcczzgnqc_free") || type.equals("jwqcczzgjqc_free")//国内清舱 国际清舱
                || type.equals("jwqcczzsdqc_free") || type.equals("jwqcczzbp_free")// 深度清舱 补配
                || type.equals("jwqyctzfj_free") || type.equals("jwqycbltzfj_free")//牵引车拖拽飞机  牵引车抱轮拖拽飞机
                || type.equals("jwqycygtzfj_free") || type.equals("jwqyctfj_free")//牵引车有杆拖拽飞机  牵引车推飞机
                || type.equals("jwqycygtfj_free") || type.equals("jwqycbltfj_free")//牵引车有杆推飞机  牵引车抱轮推飞机
                || type.startsWith("jwqyc")
        ) {
            //			sql = "SELECT ID,FLIGHT_NUMBER, ACT_TYPE, AIRCRAFT_NUMBER, ITEM_DATE, NUM, (select name from sys_user where id = OPERATOR) OPERATOR,to_char(CREATE_DATE,'hh24mi') CREATE_DATE, REMARK FROM "
            //					+ json.get("DATA_TABLE") + " WHERE ID = " + id;
            sql = "SELECT ID, FLTID, FLIGHT_NUMBER, ACT_TYPE, AIRCRAFT_NUMBER, ITEM_NAME, ITEM_DATE, START_TM, END_TM, NUM, (select name from sys_user where id = OPERATOR) OPERATOR, SIGNATORY, to_char(CREATE_DATE,'hh24mi') CREATE_DATE, REMARK,SIGNATORY FROM "
                    + json.get("DATA_TABLE") + " WHERE ID = " + id;
        }
        List<Map<String,String>> result = billDao.getBillInfoDou(sql);
        /*for(int i=0;i<result.size();i++){
        	result.get(i).put("JOB_TYPE", json.get("JOB_TYPE"));
        	result.get(i).put("TYPE_CODE", type);
        }*/
        /*result.put("JOB_TYPE", json.get("JOB_TYPE"));
        result.put("TYPE_CODE", type);*/
        return result;
    }

    public void updateBill(JSONObject billJson) {
        String typeCode = billJson.getString("TYPE_CODE");
        String tableName = "";
        if("jwqyc".equals(typeCode)) {
            tableName = billJson.getString("DATA_TABLE");
        } else {
            billJson.remove("OPERATOR");
            tableName = billDao.getTableName(billJson.getString("TYPE_CODE")).getString("DATA_TABLE");
        }
        billJson.remove("TYPE_CODE");
        String id = billJson.getString("ID");
        billJson.remove("ID");
        String sql = "UPDATE " + tableName + " SET ";
        Set<String> keys = billJson.keySet();
        for (String key : keys) {
            if(!"DATA_TABLE".equals(key))
                sql += key + " = '" + billJson.getString(key) + "',";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " WHERE ID = '" + id + "'";
        billDao.updateBill(sql);
    }

    public void dellBill(String id,String type) {
        String tableName = billDao.getTableName(type).getString("DATA_TABLE");
        String sql = "DELETE FROM " + tableName + " WHERE ID = " + id;
        billDao.delBill(sql);
    }

    public void delAlnBill(String id) {
        billDao.delAlnBill(id);
    }

    public void insertBill(JSONObject billJson,String code) {
        String tableName = billDao.getTableName(code).getString("DATA_TABLE");
        String actType = billDao.getActType(billJson.getString("AIRCRAFT_NUMBER"));
        if (actType == null) {
            actType = "";
        }
        String sql = "INSERT INTO " + tableName + " (";
        Set<String> keys = billJson.keySet();
        for (String key : keys) {
            if(!"DATA_TABLE".equals(key))
                sql += key + ",";
        }
        sql += "ID,ACT_TYPE,OPERATOR,CREATE_DATE) VALUES (";
        for (String key : keys) {
            if(!"DATA_TABLE".equals(key))
                sql += "'" + billJson.getString(key) + "',";
        }
        sql += tableName + "_s.nextval,'" + actType + "','" + UserUtils.getUser().getId() + "',sysdate)";
        billDao.insertBill(sql);
    }

    public void saveBill(JSONObject billJSON) {
        billDao.saveBill(billJSON);
    }

    public List<Map<String,Object>> loadAtcactype() {
        return billDao.loadAtcactype();
    }

    public List<Map<String,Object>> loadUserList(String officeId) {
        return billDao.loadUserList(officeId);
    }

}
