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
import com.neusoft.framework.common.persistence.CrudDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.workflow.entity.Node;

@MyBatisDao
public interface NodeDao extends CrudDao<Node> {

	List<Node> getNodeList(@Param("jobKind")String jobKind,@Param("jobType")String jobType);

	void doInsertNode(Node node);
	
	void doUpdateNode(Node node);

	Node getNodeById(@Param("id") String id);

	JSONArray getNodeKindList();

	JSONArray getNodeTypeByKind(@Param("resKind") String resKind);

	List<Node> getNodeByType(@Param("resType") String resType);
	
	JSONArray getTemplateList();

	void delNode(@Param("id") String id);

}
