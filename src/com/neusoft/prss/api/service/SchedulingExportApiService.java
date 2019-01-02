/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年2月7日 下午3:40:38
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.prss.api.dao.SchedulingExportApiDAO;

@Service
public class SchedulingExportApiService {
	@Autowired
	private SchedulingExportApiDAO schedulingExportApiDAO;
	/**
	 *Discription:根据保证类型获取schema id
	 *@param jobKind
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年2月11日 gaojingdan [变更描述]
	 */
	public String getSchemaByReskind(String jobKind){
		return schedulingExportApiDAO.getSchemaByReskind(jobKind);
	}
}
