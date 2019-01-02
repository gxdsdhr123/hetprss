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
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.produce.entity.BillTcChargeEntity;

@MyBatisDao
public interface BillTcChargeDao extends BaseDao {
	
	JSONArray getDataList();
	
	List<Map<String ,String>> getDataTotal(@Param("startDate")String startDate,@Param("endDate")String endDate);
	
	JSONArray getResType();
	
	JSONArray getSysUser();
	
	JSONArray getFltInfo(Map<String,String> param);
	
	int saveAdd(BillTcChargeEntity entity);
	
	int saveEdit(BillTcChargeEntity entity);
	
	JSONArray getBillById(@Param("id")String id);
	
	JSONArray getExportWordData(@Param("id")String id);
	
	int delBillById(@Param("id")String id);

}
