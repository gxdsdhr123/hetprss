/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月16日 上午8:59:12
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.workflow.entity.NodeBtn;

@MyBatisDao
public interface NodeBtnDao extends BaseDao {
	
	List<NodeBtn> getNodeBtnByNodeID(@Param("id") String id);
	
	JSONArray getBtnEvent();
	
	void delNodeBtn(@Param("id") String id);
	
	void doInsertNodeBtn(JSONObject btns);
	
}
