/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午5:00:34
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.arrange.entity.AmbulatoryShiftsType;

@MyBatisDao
public interface AmbulatoryShiftsTypeDao extends BaseDao{
	
	List<AmbulatoryShiftsType> getAmbulatoryShiftsTypeList(String officeId);
	
	List<AmbulatoryShiftsType> getASTypeById(String id);
	
	void doInsertAST(AmbulatoryShiftsType ast);
	
	int getSeq();//取序列
	
	void delAST(String id);
	
	List<String> getFltByDay(@Param("day") String day);
	
	JSONArray getFltNotDay(@Param("day") String day);

}
