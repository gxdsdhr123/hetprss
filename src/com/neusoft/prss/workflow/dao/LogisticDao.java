/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月25日 下午5:02:34
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.dao;

import org.apache.ibatis.annotations.Param;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface LogisticDao extends BaseDao {
    JSONArray getListData(@Param("kindMainId") String kindMainId);

    JSONArray getAllReskind();

    void deleteKind(@Param("reskind") String reskind);

    void deleteTypeByKindId(@Param("reskind") String reskind);

    void deleteType(@Param("reskind") String reskind);

    void deleteType(@Param("reskind") String reskind,@Param("restype") String restype);

    void saveKind(@Param("kindid") String kindid,@Param("reskind") String reskind,@Param("kindname") String kindname,
            @Param("deptId") String deptId,@Param("depname") String depname,@Param("tab") String tab);

    void updateKind(@Param("kindid") String kindid,@Param("reskind") String reskind,@Param("kindname") String kindname,
            @Param("deptId") String deptId,@Param("depname") String depname,@Param("tab") String tab);

    void saveType(@Param("typeid") String typeid,@Param("kindname") String kindname,@Param("restype") String restype,
            @Param("typename") String typename,@Param("displayname") String displayname,
            @Param("kindcode") String kindcode,@Param("bindCar") String bindCar);

    void updateType(@Param("typeid") String typeid,@Param("kindname") String kindname,@Param("restype") String restype,
            @Param("typename") String typename,@Param("displayname") String displayname,
            @Param("kindcode") String kindcode,@Param("bindCar") String bindCar);

    JSONObject getTypeByTypeid(@Param("typeId") String typeId);

    JSONObject getKindByKindid(@Param("kindId") String kindId);

    JSONArray getDeptHasNoSon();

    JSONArray getWorkFlowByTypeid(@Param("typeMainId") String typeMainId);

    JSONArray vaildOnlyRestype(@Param("restype") String restype);

    JSONArray vaildOnlyReskind(@Param("reskind") String reskind);

    String getKindId();

    String getTypeId();

    void insertOfficeLimit(@Param("officeId") String officeId);
}
