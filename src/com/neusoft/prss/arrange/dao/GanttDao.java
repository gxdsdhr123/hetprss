/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月6日 下午7:43:48
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface GanttDao extends BaseDao{

	List<Map<String, String>> ganttData(Map<String, Object> params);

}
