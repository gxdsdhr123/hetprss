
package com.neusoft.prss.scheduling.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface WorkerDivisionDao extends BaseDao {

    JSONArray getTemplateList(String officeId);

    JSONArray getTableHeader(Map<String,Object> params);

    String getDimLimit(Map<String,Object> params);

    JSONArray getAreaInfoByLimit(Map<String,Object> params);

    int getGridDataCount(Map<String,Object> param);

    List<Map<String,String>> getGridData(Map<String,Object> param);

    String getId();

    void deleteInfo(Map<String,Object> param);

    void deleteLimitInfo(Map<String,Object> param);

    void addLimitInfo(Map<String,Object> param);

    void addDivisionInfo(Map<String,Object> param);

    JSONArray getAptiGridData(Map<String,Object> param);

    JSONArray getAreaInfoByParent(Map<String,Object> params);

    void insertDivisionInfoHis(Map<String,Object> param);

    void insertElementsHis(Map<String,Object> param);

    void addElementDetail(Map<String,Object> param);

    void deleteDivisionInfo(Map<String,Object> param);

    void deleteElements(Map<String,Object> param);

    void deleteDivisionLimit(Map<String,Object> param);

    void addTempName(Map<String,Object> params);

    String getTempId();

    String getTempConfId();

    void addTempLimitInfo(Map<String,Object> param);

    void deleteTemp(Map<String,Object> param);

    void deleteLimitTemp(Map<String,Object> param);

    void deleteTempConf(Map<String,Object> param);

    JSONArray getAreaInfoByTempLimit(Map<String,Object> params);

    JSONArray getLimitList(Map<String,Object> params);

	void updateTempName(Map<String, Object> param);

	JSONArray getGroups(@Param("officeId") String officeId);
	
	/**
	 * 根据人员资质的修改，更新人员分工的信息
	 * @param param
	 */
	void updateDivisionInfo(Map<String,Object> param);


}
