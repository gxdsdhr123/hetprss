/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年2月7日 下午3:40:51
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.api.dao;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface SchedulingExportApiDAO {
	/**
	 *Discription:根据保证类型获取schema id
	 *@param jobKind
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年2月11日 gaojingdan [变更描述]
	 */
	public String getSchemaByReskind(@Param("jobKind")String jobKind);
}
