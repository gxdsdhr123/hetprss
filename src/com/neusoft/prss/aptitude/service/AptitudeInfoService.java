
package com.neusoft.prss.aptitude.service;

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
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.aptitude.dao.AptitudeInfoDao;
import com.neusoft.prss.scheduling.dao.WorkerDivisionDao;

@Service
@Transactional(readOnly = true)
public class AptitudeInfoService extends BaseService {
    @Autowired
    private AptitudeInfoDao aptitudeInfoDao;
    @Autowired
    private WorkerDivisionDao workerDivisionDao;

    public JSONArray getTableHeader(Map<String,Object> params) {
        return aptitudeInfoDao.getTableHeader(params);
    }

    public String getDimLimit(Map<String,Object> params) {
        return aptitudeInfoDao.getDimLimit(params);
    }

    public JSONArray getAreaInfoByLimit(Map<String,Object> params) {
        return aptitudeInfoDao.getAreaInfoByLimit(params);
    }

    public List<Map<String,Object>> getGridData(Map<String,Object> param) {
        //Map<String,Object> result = new HashMap<String,Object>();
        JSONArray array = aptitudeInfoDao.getTableHeader(param);
        param.put("fieldArray", array);
        /*rstypeSql.append("SELECT ID, TT.GROUP_ID,TT.GROUP_NAME, LONGIN_ID,LOGIN_NAME ");
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.getString("FIELD");
            cols2.append(",DECODE(APTITUDE_ID,'" + id + "',APTITUDE_ID,null) \"FIELD_" + id + "\" ");
            cols1.append(",MAX(\"FIELD_" + id + "\") \"FIELD_" + id + "\" ");
        }
        rstypeSql.append(cols1);
        rstypeSql.append(" FROM (SELECT T1.ID ");
        rstypeSql.append(cols2);
        rstypeSql.append(",T2.*,T3.RESTYPE,T3.TYPENAME ");
        rstypeSql.append("FROM AM_APTITUDE_INFO T1,");
        rstypeSql.append("(");
        
        rstypeSql.append("SELECT A.GROUP_ID, A.GROUP_NAME, C.ID LONGIN_ID, C.NAME LOGIN_NAME ");
        rstypeSql.append("FROM AM_GROUP_INFO A, AM_GROUP_WORKER_REL B, SYS_USER C ");
        rstypeSql.append("WHERE A.GROUP_ID = B.GROUP_ID AND B.WORKER_ID = C.ID AND (NAME LIKE '%"
                + (String) param.get("searchName") + "%'  OR A.GROUP_NAME LIKE '%" + param.get("searchName").toString() + "%') ");
        rstypeSql.append("AND C.OFFICE_ID = '" + (String) param.get("officeId") + "'");*/
        
        /*
         * 增加班组查询
         */
        /*rstypeSql.append(" union ");
        // 班组查询
        rstypeSql.append("SELECT A.GROUP_ID, A.GROUP_NAME, C.ID LONGIN_ID, C.NAME LOGIN_NAME ");
        rstypeSql.append("FROM AM_GROUP_INFO A, AM_GROUP_WORKER_REL B, SYS_USER C ");
        rstypeSql.append("WHERE A.GROUP_ID = B.GROUP_ID AND B.WORKER_ID = C.ID AND A.GROUP_NAME LIKE '%" + param.get("searchName").toString() + "%' ");
        rstypeSql.append("AND C.OFFICE_ID = '" + (String) param.get("officeId") + "'");*/
        
        
        /*rstypeSql.append(") T2,");
        rstypeSql.append("(SELECT B2.RESTYPE, B2.TYPENAME ");
        rstypeSql.append("FROM DIM_RESKIND A2, DIM_RESTYPE B2 ");
        rstypeSql.append(" WHERE A2.RESKIND = B2.RESKIND ");
        rstypeSql.append("AND A2.DEPID = '" + (String) param.get("officeId") + "' ");
        rstypeSql.append(" ORDER BY B2.RESTYPE) T3 ");
        rstypeSql.append("WHERE T1.WORK_ID(+) = T2.LONGIN_ID ");
        rstypeSql.append("AND T1.APTITUDE_ID = T3.RESTYPE(+)) TT ");
        rstypeSql.append("GROUP BY ID, GROUP_ID,TT.GROUP_NAME, LONGIN_ID, LOGIN_NAME ");*/

        String limitStr = aptitudeInfoDao.getDimLimit(param);
        if (limitStr != null && !"0,0,0".equals(limitStr)) {
        	String[] str = limitStr.split(",");
        	param.put("limitArr", str);
        }
        /*if (limitStr != null && !"0,0,0".equals(limitStr)) {
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
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 1 THEN B.ELEMENTS  END JX ");
                cols5.append(",TT2.JX_LIMIT ");
            }
            //航空公司 
            if ("1".equals(str[2])) {
                cols3.append(",MAX(HS) HS_LIMIT ");
                cols4.append(",CASE WHEN B.LIMIT_TYPE = 2 THEN B.ELEMENTS END HS ");
                cols5.append(",TT2.HS_LIMIT ");
            }
            limitSql.append(cols3).append(" FROM (SELECT B.ID ");
            limitSql.append(cols4).append("FROM AM_APTITUDE_LIMITS B ");
            limitSql.append("WHERE B.OFFICE_ID = '" + (String) param.get("officeId") + "' ");
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
        System.out.println(sql.toString());
        param.put("sqlStr", sql.toString());*/
        //System.out.println(sql.toString());
        //int total = aptitudeInfoDao.getGridDataCount(param);
        List<Map<String,Object>> rows = aptitudeInfoDao.getGridData(param);
        //result.put("total", total);
        //result.put("rows", rows);
        return rows;
    }

    public String saveInfo(JSONArray dataArray,JSONArray editDataArray,String officeId) {
        try {
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("officeId", officeId);
            String userId = UserUtils.getUser().getId();
            JSONArray header = aptitudeInfoDao.getTableHeader(param);
            
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject obj = (JSONObject) dataArray.get(i);
                String id = "";
                String ids1 = "";
                Object object = obj.get("ID");
                id = getStr(object);
                if (!"".equals(id)) {
                    param.put("id", id);
                } else {
                    String newId = aptitudeInfoDao.getId();
                    param.put("id", newId);
                }
                for (int j = 0; j < header.size(); j++) {
                    JSONObject hh = header.getJSONObject(j);
                    String field = "FIELD_" + hh.getString("FIELD");
                    String ff = (String) obj.get(field);
                    if (!"".equals(ff)) {
                        ids1 += ff + ",";
                    }
                }
                if (ids1.lastIndexOf(",") > -1) {
                    ids1 = ids1.substring(0, ids1.length() - 1);
                }

                param.put("curworkId", UserUtils.getUser().getId());
                param.put("workId", obj.get("LONGIN_ID"));
                if (!"".equals(id)) {
                    aptitudeInfoDao.deleteInfo(param);
                    aptitudeInfoDao.deleteLimitInfo(param);
                }

                List<String> sqlList = new ArrayList<String>();
                if (!"".equals(ids1)) {
                    String[] ids = ids1.split(",");
                    for (int t = 0; t < ids.length; t++) {
                        StringBuffer sqlDiv = new StringBuffer();
                        sqlDiv.append("SELECT ");
                        sqlDiv.append(param.get("id") + " as c1,");
                        sqlDiv.append("'" + obj.get("LONGIN_ID") + "' as c2,");
                        sqlDiv.append("'" + ids[t] + "' as c3,");
                        sqlDiv.append("sysdate as c4,");
                        sqlDiv.append("'" + userId + "' as c5,");
                        sqlDiv.append("'" + officeId + "' as c6 ");
                        sqlDiv.append("FROM DUAL ");
                        sqlList.add(sqlDiv.toString());
                    }
                }else {
                	String[] ids = {id};
                	deleteApti(ids);
                }
                if (sqlList.size() > 0) {
                    StringBuffer sqlDiv = new StringBuffer();
                    sqlDiv.append("insert into am_aptitude_info(id,work_id,aptitude_id,"
                            + "create_time,creator_id,office_id)");
                    sqlDiv.append("SELECT T.* from (");
                    for (int k = 0; k < sqlList.size(); k++) {
                        sqlDiv.append(sqlList.get(k));
                        if (k < sqlList.size() - 1) {
                            sqlDiv.append(" union all ");
                        }
                    }
                    sqlDiv.append(") T ");
                    param.put("aptiduteSql", sqlDiv.toString());
                    aptitudeInfoDao.addAptiInfo(param);
                }
                object = obj.get("JW_LIMIT");
                String jl = getStr(object);
                if (jl != null && !"".equals(jl)) {
                    param.put("id2", jl);
                    param.put("id3", "0");
                    aptitudeInfoDao.addLimitInfo(param);
                }
                object = obj.get("JX_LIMIT");
                jl = getStr(object);
                if (jl != null && !"".equals(jl)) {
                    param.put("id2", jl);
                    param.put("id3", "1");
                    aptitudeInfoDao.addLimitInfo(param);
                }
                object = obj.get("HS_LIMIT");
                jl = getStr(object);
                if (jl != null && !"".equals(jl)) {
                    param.put("id2", jl);
                    param.put("id3", "2");
                    aptitudeInfoDao.addLimitInfo(param);
                }
            }
            
            
          //修改了人员资质后维护人员分工
            
            for(int i = 0; i < editDataArray.size(); i++){
            	String aptitudeStr = "";
            	//人员资质修改
                Map<String,Object> editParam = new HashMap<String,Object>();
                editParam.put("officeId", officeId);
            	
            	JSONObject obj = (JSONObject) editDataArray.get(i);
            	editParam.put("workId", obj.get("LONGIN_ID"));
            	//取消掉的人员资质
            	String id = "";
                Object object = obj.get("ID");
                id = getStr(object);
                if (!"".equals(id)) {
                	editParam.put("id", id);
                } 
            	for (int j = 0; j < header.size(); j++) {
                    JSONObject hh = header.getJSONObject(j);
                    String field = "FIELD_" + hh.getString("FIELD");
                    String ff = (String) obj.get(field);
                    if ("".equals(ff)) {
                        aptitudeStr += hh.getString("FIELD") + ",";
                    }
                }
                if (aptitudeStr.lastIndexOf(",") > -1) {
                    aptitudeStr = aptitudeStr.substring(0, aptitudeStr.length() - 1);
                }
                
                if (!"".equals(aptitudeStr)) {
                    String[] aptitudeArr = aptitudeStr.split(",");
                    editParam.put("aptitude_id", aptitudeArr);
                    
//                    System.out.println("workid="+editParam.get("workId")+";aptitude_id="+aptitudeStr+";office_id="+editParam.get("officeId")+";parent_aptitude_id="+editParam.get("id"));
                    //当有取消资质的时候，才执行同步人员分工的信息
                    workerDivisionDao.updateDivisionInfo(editParam);
                }
            }
            
//            System.out.println("workid="+editParam.get("workId")+";aptitude_id="+aptitudeStr+";office_id="+editParam.get("officeId")+";parent_aptitude_id="+editParam.get("id"));
            
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public String getStr(Object object) {
        if(object==null){
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
/**
 * @author wangtg
 * @param ids
 * 删除资质时 应按顺序删除关联的子表、多对多关联限制表、对应的模
 * 板表和分工表
 */
    public void deleteApti(String[] ids) {
    	
    	aptitudeInfoDao.deleteDivEleDetail(ids);
    	
    	aptitudeInfoDao.deleteDivLimit(ids);
    	
    	aptitudeInfoDao.deleteDivInfo(ids);
    	
    	aptitudeInfoDao.deleteDivTempLimit(ids);
    	
    	aptitudeInfoDao.deleteDivTemp(ids);
    	
    	aptitudeInfoDao.deleteAptiLimit(ids);
    	
        aptitudeInfoDao.deleteApti(ids);
        
    }
}
