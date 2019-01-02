/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月15日 上午9:22:54
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface BillDao extends BaseDao {

    JSONArray getFeeTable(@Param("officeId") String officeId);

    JSONArray getBillData(@Param("sql") String sql);

    JSONArray getAlnBillData();

    List<Map<String,String>> getBillDataDouxf(@Param("sql") String sql);

    JSONObject getTableName(@Param("type") String type);

    /**
     * @author Douxf
     * @param type
     * @return
     */
    Map<String,String> getTableNameDou(@Param("type") String type);

    JSONObject getBillInfo(@Param("sql") String sql);

    /**
     * dou
     * @param sql
     */
    List<Map<String,String>> getBillInfoDou(@Param("sql") String sql);

    void updateBill(@Param("sql") String sql);

    void delBill(@Param("sql") String sql);

    JSONArray getJobTypeByOfficeId(@Param("officeId") String officeId);

    JSONArray getTypeCodeByJobType(@Param("jobType") String jobType);

    void insertBill(@Param("sql") String sql);

    void delAlnBill(@Param("id") String id);

    void saveBill(@Param("billJSON") JSONObject billJSON);

    String getActType(@Param("airNum") String airNum);

    String getNextAlnBillId();

    JSONObject getAlnBillInfo(@Param("id") String id);

    JSONArray getFeeTableByJobKind(@Param("jobkind")String jobkind);

    List<Map<String,Object>> loadAtcactype();

    List<Map<String,Object>> loadUserList(@Param("officeId")String officeId);

}
