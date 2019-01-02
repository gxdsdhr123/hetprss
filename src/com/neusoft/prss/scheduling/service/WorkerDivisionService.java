
package com.neusoft.prss.scheduling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.scheduling.dao.WorkerDivisionDao;

@Service
@Transactional(readOnly = true)
public class WorkerDivisionService extends BaseService {
    @Autowired
    private WorkerDivisionDao workerDivisionDao;

    public JSONArray getTemplateList(String officeId) {
        return workerDivisionDao.getTemplateList(officeId);
    }

    public JSONArray getTableHeader(Map<String,Object> params) {
        return workerDivisionDao.getTableHeader(params);
    }

    public String getDimLimit(Map<String,Object> params) {
        return workerDivisionDao.getDimLimit(params);
    }

    public JSONArray getAreaInfoByLimit(Map<String,Object> params) {
        return workerDivisionDao.getAreaInfoByLimit(params);
    }

    public Map<String,Object> getGridData(Map<String,Object> param) {
        Map<String,Object> result = new HashMap<String,Object>();
        JSONArray array = workerDivisionDao.getTableHeader(param);
        StringBuffer sql = new StringBuffer();
        StringBuffer rstypeSql = new StringBuffer();
        StringBuffer cols1 = new StringBuffer();
        StringBuffer cols2 = new StringBuffer();
        StringBuffer cols3 = new StringBuffer();
        StringBuffer cols4 = new StringBuffer();
        StringBuffer cols5 = new StringBuffer();
        StringBuffer cols6 = new StringBuffer();
        StringBuffer cols7 = new StringBuffer();
        StringBuffer cols8 = new StringBuffer();
        StringBuffer limitSql = new StringBuffer();
        String table1 = "", table2 = "", where1 = "";
        String templateId = (String) param.get("templateId");
        String group = (String) param.get("group");
        if ("all".equals(templateId)) {
            table1 = "AM_DIVISION_INFO";
            table2 = "AM_DIVISION_LIMITS B ";
            where1 = "";
        } else {
            table1 = "am_division_temp";
            table2 = "am_division_temp_limits B ";
            where1 = " and T1.TEMP_ID(+) = '" + templateId + "' ";
        }

        rstypeSql.append("SELECT ID, TT.GROUP_ID,TT.GROUP_NAME, LONGIN_ID,LOGIN_NAME,PARENT_APTITUDE_ID,APID ");
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.getString("FIELD");
            cols2.append(
                    ",DECODE(APTITUDE_ID||PRIMARY_FLAG, '" + id + "1', APTITUDE_ID, NULL) \"FIELD_" + id + "_1\" ");
            cols2.append(",DECODE(APTITUDE_ID||PRIMARY_FLAG, '" + id + "0', APTITUDE_ID, NULL) \"FIELD_" + id + "_0\"");
            cols1.append(",MAX(\"FIELD_" + id + "_1\") \"FIELD_" + id + "_1\"");
            cols1.append(",MAX(\"FIELD_" + id + "_0\") \"FIELD_" + id + "_0\"");
            if (i == 0) {
                cols6.append(",\"FIELD_" + id + "\"");
            } else {
                if (i == array.size() - 1) {
                    cols6.append("||','||\"FIELD_" + id + "\"");
                } else {
                    cols6.append("||','||\"FIELD_" + id + "\"");
                }
            }
            cols7.append(",MAX(\"FIELD_" + id + "\") \"FIELD_" + id + "\" ");
            cols8.append(",DECODE(APTITUDE_ID, '" + id + "', APTITUDE_ID, NULL) \"FIELD_" + id + "\" ");
        }
        rstypeSql.append(cols1);
        rstypeSql.append(" FROM (SELECT T1.ID,PARENT_APTITUDE_ID,APID ");
        rstypeSql.append(cols2);
        rstypeSql.append(",T2.*,T3.RESTYPE,T3.TYPENAME ");
        rstypeSql.append("FROM " + table1 + " T1,");
        rstypeSql.append("(");
        
        rstypeSql.append("SELECT A.GROUP_ID, A.GROUP_NAME, C.ID LONGIN_ID, C.NAME LOGIN_NAME ");
        rstypeSql.append("FROM AM_GROUP_INFO A, AM_GROUP_WORKER_REL B, SYS_USER C ");
        rstypeSql.append("WHERE A.GROUP_ID = B.GROUP_ID AND B.WORKER_ID = C.ID AND NAME LIKE '%"  + (String) param.get("searchName") + "%' AND A.GROUP_NAME LIKE '%"+ param.get("group").toString() +"%' ");
        rstypeSql.append("AND C.OFFICE_ID = '" + (String) param.get("officeId") + "'");
        
        /*
         * 增加班组查询
         */
        rstypeSql.append(" union ");
        // 班组查询
        rstypeSql.append("SELECT A.GROUP_ID, A.GROUP_NAME, C.ID LONGIN_ID, C.NAME LOGIN_NAME ");
        rstypeSql.append("FROM AM_GROUP_INFO A, AM_GROUP_WORKER_REL B, SYS_USER C ");
        rstypeSql.append("WHERE A.GROUP_ID = B.GROUP_ID AND B.WORKER_ID = C.ID AND NAME LIKE '%" + param.get("searchName").toString() + "%' AND A.GROUP_NAME LIKE '%"+ param.get("group").toString() +"%' ");
        rstypeSql.append("AND C.OFFICE_ID = '" + (String) param.get("officeId") + "'");
       
        rstypeSql.append(") T2,");
        rstypeSql.append("(SELECT B2.RESTYPE, B2.TYPENAME ");
        rstypeSql.append("FROM DIM_RESKIND A2, DIM_RESTYPE B2 ");
        rstypeSql.append(" WHERE A2.RESKIND = B2.RESKIND ");
        rstypeSql.append("AND A2.DEPID = '" + (String) param.get("officeId") + "' ");
        rstypeSql.append(" ORDER BY B2.RESTYPE) T3, ");
        rstypeSql.append("(SELECT ID ");
        if (!"".equals(cols6.toString())) {
            rstypeSql.append(cols6.toString());
        } else {
            rstypeSql.append(",''");
        }

        rstypeSql.append(" APID FROM (SELECT ID");
        rstypeSql.append(cols7.toString());
        rstypeSql.append(" FROM ( SELECT DISTINCT ID ");
        rstypeSql.append(cols8.toString());
        rstypeSql.append(" FROM AM_APTITUDE_INFO WHERE OFFICE_ID = '" + (String) param.get("officeId")
                + "') GROUP BY ID )) T4 ");

        rstypeSql.append("WHERE T1.WORKER_ID(+) = T2.LONGIN_ID " + where1 + " ");
        rstypeSql.append("AND T1.APTITUDE_ID = T3.RESTYPE(+) AND T1.PARENT_APTITUDE_ID =T4.ID(+)");
        // 新增按人查询的逻辑 by xuhw
        if(!StringUtils.isEmpty((String)param.get("operator"))){
        	rstypeSql.append(" AND T1.WORKER_ID = '" + (String)param.get("operator") + "'");
        }
        rstypeSql.append(" ) TT ");
        rstypeSql.append("GROUP BY ID, GROUP_ID,TT.GROUP_NAME, LONGIN_ID, LOGIN_NAME,PARENT_APTITUDE_ID,APID ");

        String limitStr = workerDivisionDao.getDimLimit(param);
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
            limitSql.append(" (SELECT ID ");
            String[] str = limitStr.split(",");
            //机位
            if ("1".equals(str[0])) {
                cols3.append(",MAX(JW) JW_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 0 THEN B.ELEMENTS END JW ");
                cols5.append(",TT2.JW_LIMIT ");
            }
            //机型
            if ("1".equals(str[1])) {
                cols3.append(",MAX(JX) JX_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 1 THEN B.ELEMENTS END JX ");
                cols5.append(",TT2.JX_LIMIT ");
            }
            //航空公司 
            if ("1".equals(str[2])) {
                cols3.append(",MAX(HS) HS_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 2 THEN B.ELEMENTS END HS ");
                cols5.append(",TT2.HS_LIMIT ");
            }
            limitSql.append(cols3).append(" FROM (SELECT B.ID ");
            limitSql.append(cols4).append("FROM " + table2 + " ");
            limitSql.append("WHERE B.OFFICE_ID = '"
                    + (String) param.get("officeId") + "' ");
            limitSql.append(" ) GROUP BY ID ) TT2 ");
            limitSql.append("WHERE TT1.ID=TT2.ID(+) ");
        }
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
            sql.append("SELECT TT1.* ").append(cols5).append(" FROM (");
            sql.append(rstypeSql);
            sql.append(") TT1,");
            sql.append(limitSql);
        } else {
            sql.append(rstypeSql);
        }
        sql.append(" ORDER BY GROUP_NAME, GROUP_ID, LONGIN_ID, LOGIN_NAME ");
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
            sql.append(",TT1.ID ");
        }
        param.put("sqlStr", sql.toString());
        System.out.println(sql.toString());
        List<Map<String,String>> rows = workerDivisionDao.getGridData(param);
        result.put("rows", rows);
        return result;
    }

    public String saveInfo(JSONArray dataArray,String officeId,String tempName,String tempId,String operator) {
        try {
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("officeId", officeId);
            param.put("workerId", operator);
            String userId = UserUtils.getUser().getId();
            String tempConfId = "";

            if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                //模板操作
                if (tempId != null && !"".equals(tempId)) {
                    tempConfId = tempId;
                    param.put("tempId", tempId);
                    workerDivisionDao.deleteTemp(param);
                    workerDivisionDao.deleteLimitTemp(param);
                } else {
                    param.put("tempName", tempName);
                    param.put("userId", userId);
                    tempConfId = workerDivisionDao.getTempConfId();
                    param.put("tempConfId", tempConfId);
                    workerDivisionDao.addTempName(param);
                }
            } else {
                //先把数据移到历史表中
                workerDivisionDao.insertDivisionInfoHis(param);
                workerDivisionDao.insertElementsHis(param);
                //删除当前分工
                workerDivisionDao.deleteDivisionInfo(param);
                workerDivisionDao.deleteElements(param);
                workerDivisionDao.deleteDivisionLimit(param);
            }

            //再增加记录
            JSONArray header = workerDivisionDao.getTableHeader(param);
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject obj = (JSONObject) dataArray.get(i);
                String ids1 = "", ids2 = "";
                Object object = obj.get("ID");
                String newId = "";
                if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                    newId = workerDivisionDao.getTempId();
                } else {
                    newId = workerDivisionDao.getId();
                }
                param.put("id", newId);
                for (int j = 0; j < header.size(); j++) {
                    JSONObject hh = header.getJSONObject(j);
                    String field1 = "FIELD_" + hh.getString("FIELD") + "_1";
                    String field2 = "FIELD_" + hh.getString("FIELD") + "_0";
                    String ff1 = (String) obj.get(field1);
                    String ff2 = (String) obj.get(field2);
                    if (ff1 != null && !"".equals(ff1)) {
                        ids1 += ff1 + ",";
                    }
                    if (ff2 != null && !"".equals(ff2)) {
                        ids2 += ff2 + ",";
                    }
                }
                if (ids1.lastIndexOf(",") > -1) {
                    ids1 = ids1.substring(0, ids1.length() - 1);
                }
                if (ids2.lastIndexOf(",") > -1) {
                    ids2 = ids2.substring(0, ids2.length() - 1);
                }
                List<String> sqlList = new ArrayList<String>();
                if (!"".equals(ids1)) {
                    String[] ids = ids1.split(",");
                    for (int t = 0; t < ids.length; t++) {
                        StringBuffer sqlDiv = new StringBuffer();
                        param.put("cid", ids[t]);
                        sqlDiv.append("SELECT ");
                        sqlDiv.append(param.get("id") + " A1,");
                        if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                            sqlDiv.append("'" + tempConfId + "' A2,");
                        }
                        sqlDiv.append("'" + obj.get("LONGIN_ID") + "' A3,");
                        sqlDiv.append("'" + ids[t] + "' A4,");
                        sqlDiv.append("1 A5,sysdate A6,");
                        sqlDiv.append("'" + userId + "' A7,");
                        sqlDiv.append("'" + officeId + "' A8,");
                        sqlDiv.append("'" + obj.get("PARENT_APTITUDE_ID") + "' A9 ");
                        sqlDiv.append("FROM DUAL ");
                        sqlList.add(sqlDiv.toString());
                    }
                }
                if (!"".equals(ids2)) {
                    String[] ids = ids2.split(",");
                    for (int t = 0; t < ids.length; t++) {
                        StringBuffer sqlDiv = new StringBuffer();
                        sqlDiv.append("SELECT ");
                        sqlDiv.append(param.get("id") + " A1,");
                        if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                            sqlDiv.append("'" + tempConfId + "' A2,");
                        }
                        sqlDiv.append("'" + obj.get("LONGIN_ID") + "' A3,");
                        sqlDiv.append("'" + ids[t] + "' A4,");
                        sqlDiv.append("0 A5,sysdate A6,");
                        sqlDiv.append("'" + userId + "' A7,");
                        sqlDiv.append("'" + officeId + "' A8,");
                        sqlDiv.append("'" + obj.get("PARENT_APTITUDE_ID") + "' A9 ");
                        sqlDiv.append("FROM DUAL ");
                        sqlList.add(sqlDiv.toString());
                    }
                }
                if (sqlList.size() > 0) {
                    StringBuffer sqlDiv = new StringBuffer();
                    if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                        sqlDiv.append("insert into am_division_temp(id,temp_id,worker_id,aptitude_id,primary_flag,");
                    } else {
                        sqlDiv.append("insert into am_division_info(id,worker_id,aptitude_id,primary_flag,");
                    }
                    sqlDiv.append("create_time,creator_id,office_id,parent_aptitude_id)");
                    sqlDiv.append("SELECT T.* from (");
                    for (int k = 0; k < sqlList.size(); k++) {
                        sqlDiv.append(sqlList.get(k));
                        if (k < sqlList.size() - 1) {
                            sqlDiv.append(" union all ");
                        }
                    }
                    sqlDiv.append(") T ");
                    param.put("divisionSql", sqlDiv.toString());
                    workerDivisionDao.addDivisionInfo(param);
                    //System.out.println(sqlDiv.toString());
                }

                object = obj.get("JW_LIMIT");
                String jl = getStr(object);
                if (jl != null && !"".equals(jl)) {
                    param.put("id2", jl);
                    param.put("id3", "0");
                    if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                        workerDivisionDao.addTempLimitInfo(param);
                    } else {
                        workerDivisionDao.addLimitInfo(param);
                        workerDivisionDao.addElementDetail(param);
                    }

                }
                object = obj.get("JX_LIMIT");
                jl = getStr(object);
                if (jl != null && !"".equals(jl)) {
                    param.put("id2", jl);
                    param.put("id3", "1");
                    if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                        workerDivisionDao.addTempLimitInfo(param);
                    } else {
                        workerDivisionDao.addLimitInfo(param);
                        workerDivisionDao.addElementDetail(param);
                    }
                }
                object = obj.get("HS_LIMIT");
                jl = getStr(object);
                if (jl != null && !"".equals(jl)) {
                    param.put("id2", jl);
                    param.put("id3", "2");
                    if ((tempName != null && !"".equals(tempName)) || (tempId != null && !"".equals(tempId))) {
                        workerDivisionDao.addTempLimitInfo(param);
                    } else {
                        workerDivisionDao.addLimitInfo(param);
                        workerDivisionDao.addElementDetail(param);
                    }
                }
            }
            if (tempConfId != "") {
                return tempConfId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public String getStr(Object object) {
        if (object == null) {
            return "";
        }
        String str = "";
        String type = object.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            str = String.valueOf(object);
        } else {
            str = String.valueOf(object);
        }
        return str;
    }

    public JSONArray getAptiduteGridData(Map<String,Object> param) {
        JSONArray array = workerDivisionDao.getTableHeader(param);
        StringBuffer sql = new StringBuffer();
        StringBuffer rstypeSql = new StringBuffer();
        StringBuffer cols1 = new StringBuffer();
        StringBuffer cols2 = new StringBuffer();
        StringBuffer cols3 = new StringBuffer();
        StringBuffer cols4 = new StringBuffer();
        StringBuffer cols5 = new StringBuffer();
        StringBuffer limitSql = new StringBuffer();

        rstypeSql.append("SELECT ID, TT.GROUP_ID,TT.GROUP_NAME, LONGIN_ID,LOGIN_NAME ");
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.getString("FIELD");
            cols2.append(",DECODE(APTITUDE_ID,'" + id + "',APTITUDE_ID,null) \"FIELD_" + id + "_0\" ");
            cols1.append(",MAX(\"FIELD_" + id + "_0\") \"FIELD_" + id + "_0\" ");
        }
        rstypeSql.append(cols1);
        rstypeSql.append(" FROM (SELECT T1.ID ");
        rstypeSql.append(cols2);
        rstypeSql.append(",T2.*,T3.RESTYPE,T3.TYPENAME ");
        rstypeSql.append("FROM AM_APTITUDE_INFO T1,");
        rstypeSql.append("(SELECT A.GROUP_ID, A.GROUP_NAME, C.ID LONGIN_ID, C.NAME LOGIN_NAME ");
        rstypeSql.append("FROM AM_GROUP_INFO A, AM_GROUP_WORKER_REL B, SYS_USER C ");
        rstypeSql.append("WHERE A.GROUP_ID = B.GROUP_ID AND B.WORKER_ID = C.ID AND C.ID = '"
                + (String) param.get("workId") + "' ");
        rstypeSql.append("AND C.OFFICE_ID = '" + (String) param.get("officeId") + "') T2,");
        rstypeSql.append("(SELECT B2.RESTYPE, B2.TYPENAME ");
        rstypeSql.append("FROM DIM_RESKIND A2, DIM_RESTYPE B2 ");
        rstypeSql.append(" WHERE A2.RESKIND = B2.RESKIND ");
        rstypeSql.append("AND A2.DEPID = '" + (String) param.get("officeId") + "' ");
        rstypeSql.append(" ORDER BY B2.RESTYPE) T3 ");
        rstypeSql.append("WHERE T1.WORK_ID(+) = T2.LONGIN_ID ");
        rstypeSql.append("AND T1.APTITUDE_ID = T3.RESTYPE(+)) TT ");
        rstypeSql.append("GROUP BY ID, GROUP_ID,TT.GROUP_NAME, LONGIN_ID, LOGIN_NAME ");

        String limitStr = workerDivisionDao.getDimLimit(param);
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
            limitSql.append(" (SELECT ID ");
            String[] str = limitStr.split(",");
            //机位
            if ("1".equals(str[0])) {
                cols3.append(",MAX(JW) JW_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 0 THEN B.ELEMENTS||','||AREA_NAME END JW ");
                cols5.append(",TT2.JW_LIMIT ");
            }
            //机型
            if ("1".equals(str[1])) {
                cols3.append(",MAX(JX) JX_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 1 THEN B.ELEMENTS||','||AREA_NAME  END JX ");
                cols5.append(",TT2.JX_LIMIT ");
            }
            //航空公司 
            if ("1".equals(str[2])) {
                cols3.append(",MAX(HS) HS_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 2 THEN B.ELEMENTS||','||AREA_NAME END HS ");
                cols5.append(",TT2.HS_LIMIT ");
            }
            limitSql.append(cols3).append(" FROM (SELECT B.ID ");
            limitSql.append(cols4).append("FROM AM_APTITUDE_LIMITS B, DIM_AREA_CONF D ");
            limitSql.append("WHERE B.ELEMENTS = D.ID AND B.OFFICE_ID = '" + (String) param.get("officeId") + "' ");
            limitSql.append(" ) GROUP BY ID ) TT2 ");
            limitSql.append("WHERE TT1.ID=TT2.ID(+) ");
        }
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
            sql.append("SELECT TT1.* ").append(cols5).append(" FROM (");
            sql.append(rstypeSql);
            sql.append(") TT1,");
            sql.append(limitSql);
        } else {
            sql.append(rstypeSql);
        }
        sql.append(" ORDER BY GROUP_ID, GROUP_NAME, LONGIN_ID, LOGIN_NAME ");
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
            sql.append(",TT1.ID ");
        }
        param.put("sqlStr", sql.toString());
        //System.out.println(sql.toString());
        JSONArray rows = workerDivisionDao.getAptiGridData(param);
        return rows;
    }

    public JSONArray getAreaInfoByParent(Map<String,Object> params) {
        return workerDivisionDao.getAreaInfoByParent(params);
    }

    public String getDeleteTemp(Map<String,Object> param) {
        try {
            workerDivisionDao.deleteTempConf(param);
            workerDivisionDao.deleteTemp(param);
            workerDivisionDao.deleteLimitTemp(param);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public JSONArray getAreaInfoByTempLimit(Map<String,Object> params) {
        return workerDivisionDao.getAreaInfoByTempLimit(params);
    }

    public Map<String,Object> getLimitList(Map<String,Object> params) {
        Map<String,Object> LimitMap = new HashMap<String,Object>();
        String limitStr = (String) params.get("limitStr");
        StringBuffer sql = new StringBuffer();
        StringBuffer cols3 = new StringBuffer();
        StringBuffer cols4 = new StringBuffer();
        StringBuffer cols5 = new StringBuffer();
        Map<String,JSONArray> limit0 = new HashMap<String,JSONArray>();
        Map<String,JSONArray> limit1 = new HashMap<String,JSONArray>();
        Map<String,JSONArray> limit2 = new HashMap<String,JSONArray>();
        Map<String,Map<String,String>> apti = new HashMap<String,Map<String,String>>();
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
            String[] str = limitStr.split(",");
            //机位
            if ("1".equals(str[0])) {
                cols3.append(",MAX(JW) JW_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 0 THEN B.ELEMENTS END JW ");
                cols5.append(",TT2.JW_LIMIT ");
            }
            //机型
            if ("1".equals(str[1])) {
                cols3.append(",MAX(JX) JX_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 1 THEN B.ELEMENTS END JX ");
                cols5.append(",TT2.JX_LIMIT ");
            }
            //航空公司 
            if ("1".equals(str[2])) {
                cols3.append(",MAX(HS) HS_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 2 THEN B.ELEMENTS END HS ");
                cols5.append(",TT2.HS_LIMIT ");
            }
            sql.append("SELECT tt1.id").append(cols5).append(" FROM (");
            sql.append("SELECT ID, TT.GROUP_ID,TT.GROUP_NAME, LONGIN_ID,LOGIN_NAME ");
            sql.append(" FROM (SELECT T1.ID ");
            sql.append(",T2.*,T3.RESTYPE,T3.TYPENAME ");
            sql.append("FROM AM_APTITUDE_INFO T1,");
            sql.append("(SELECT A.GROUP_ID, A.GROUP_NAME, C.ID LONGIN_ID, C.NAME LOGIN_NAME ");
            sql.append("FROM AM_GROUP_INFO A, AM_GROUP_WORKER_REL B, SYS_USER C ");
            sql.append("WHERE A.GROUP_ID = B.GROUP_ID AND B.WORKER_ID = C.ID ");
            sql.append("AND C.OFFICE_ID = '" + (String) params.get("officeId") + "') T2,");
            sql.append("(SELECT B2.RESTYPE, B2.TYPENAME ");
            sql.append("FROM DIM_RESKIND A2, DIM_RESTYPE B2 ");
            sql.append(" WHERE A2.RESKIND = B2.RESKIND ");
            sql.append("AND A2.DEPID = '" + (String) params.get("officeId") + "' ");
            sql.append(" ORDER BY B2.RESTYPE) T3 ");
            sql.append("WHERE T1.WORK_ID(+) = T2.LONGIN_ID ");
            sql.append("AND T1.APTITUDE_ID = T3.RESTYPE(+)) TT ");
            sql.append("GROUP BY ID, GROUP_ID,TT.GROUP_NAME, LONGIN_ID, LOGIN_NAME ");
            sql.append(") TT1,");
            sql.append(" (SELECT ID ");
            sql.append(cols3).append(" FROM (SELECT B.ID ");
            sql.append(cols4).append("FROM AM_APTITUDE_LIMITS B, DIM_AREA_CONF D ");
            sql.append("WHERE B.ELEMENTS = D.ID AND B.OFFICE_ID = '" + (String) params.get("officeId") + "' ");
            sql.append(" ) GROUP BY ID ) TT2 ");
            sql.append("WHERE TT1.ID=TT2.ID(+) ");
            params.put("sqlStr", sql.toString());
            //System.out.println(sql.toString());
            JSONArray limits = workerDivisionDao.getLimitList(params);
            for (int i = 0; i < limits.size(); i++) {
                JSONObject obj = limits.getJSONObject(i);
                Map<String,String> aptilimit = new HashMap<String,String>();
                if (obj != null) {
                    if ("1".equals(str[0])) {
                        String id = obj.getString("JW_LIMIT");
                        if (id != null && !"".equals(id)) {
                            params.put("type", "0");
                            params.put("limitId", id);
                            if (!limit0.containsKey(id)) {
                                JSONArray limit = workerDivisionDao.getAreaInfoByParent(params);
                                limit0.put(id, limit);
                            }
                            aptilimit.put("lm0", id);
                        }
                    }
                    if ("1".equals(str[1])) {
                        String id = obj.getString("JX_LIMIT");
                        if (id != null && !"".equals(id)) {
                            params.put("type", "1");
                            params.put("limitId", id);
                            if (!limit1.containsKey(id)) {
                                JSONArray limit = workerDivisionDao.getAreaInfoByParent(params);
                                limit1.put(id, limit);
                            }
                            aptilimit.put("lm1", id);
                        }
                    }
                    if ("1".equals(str[2])) {
                        String id = obj.getString("HS_LIMIT");
                        if (id != null && !"".equals(id)) {
                            params.put("type", "2");
                            params.put("limitId", id);
                            if (!limit2.containsKey(id)) {
                                JSONArray limit = workerDivisionDao.getAreaInfoByParent(params);
                                limit2.put(id, limit);
                            }
                            aptilimit.put("lm2", id);
                        }
                    }
                }
                String id = obj.getString("ID");
                if (id != null) {
                    apti.put(id, aptilimit);
                }

            }
            LimitMap.put("0", limit0);
            LimitMap.put("1", limit1);
            LimitMap.put("2", limit2);
            LimitMap.put("apti", apti);
        }
        return LimitMap;
    }

    
	public String changeName(String officeId, String tempName, String tempId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("officeId", officeId);
		param.put("tempName", tempName);
		param.put("tempId", tempId);
		workerDivisionDao.updateTempName(param);
		return tempId;
	}

	public JSONArray getGroups() {
		String officeId = UserUtils.getUser().getOffice().getId();
		return workerDivisionDao.getGroups(officeId);
	}

	/*public String updateWorkerDivision(String officeId,String operator){
		Map<String,Object> param = new HashMap<String,Object>();
        param.put("officeId", officeId);
        param.put("workerId", operator);
		//先把数据移到历史表中
        workerDivisionDao.insertDivisionInfoHis(param);
        workerDivisionDao.insertElementsHis(param);
        //删除当前分工
        workerDivisionDao.deleteElements(param);
        workerDivisionDao.deleteDivisionLimit(param);
        workerDivisionDao.deleteDivisionInfo(param);
		
		
		return "success";
	}*/

}
