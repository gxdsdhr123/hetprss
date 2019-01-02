
package com.neusoft.prss.aptitude.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface AptitudeInfoDao extends BaseDao {

    JSONArray getTableHeader(Map<String,Object> params);

    String getDimLimit(Map<String,Object> params);

    JSONArray getAreaInfoByLimit(Map<String,Object> params);

    List<Map<String, Object>> getGridData(Map<String,Object> param);

    int getGridDataCount(Map<String,Object> param);

    void deleteInfo(Map<String,Object> param);

    void addLimitInfo(Map<String,Object> param);

    void deleteLimitInfo(Map<String,Object> param);

    void addAptiInfo(Map<String,Object> param);

    String getId();

    void deleteDivEleDetail(String[] ids);
    
    void deleteDivLimit(String[] ids);
    
    void deleteDivInfo(String[] ids);
    
    void deleteDivTempLimit(String[] ids);
    
    void deleteDivTemp(String[] ids);
    
    void deleteAptiLimit(String[] ids);
    
    void deleteApti(String[] ids);
    

}
