/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月19日 下午3:48:05
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.flightdynamic.entity.FdFltGbak;

@MyBatisDao
public interface FdRecycleDao {

	/**
	 * 
	 * Discription:获取长期计划主表信息.
	 * 
	 * @param param
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	JSONArray getFdMain(Map<String, String> param);

	/**
	 * 
	 * Discription:获取长期计划次表信息.
	 * 
	 * @param param
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	JSONArray getFdDetail(Map<String, String> param);

	/**
	 * 
	 * Discription:批量插入gbak.
	 * 
	 * @param param
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	void insertFdFltGbaks(List<FdFltGbak> gbakList);

	/**
	 * 
	 * Discription:批量插入gbakrel.
	 * 
	 * @param param
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	void insertFdFltGbakrels(List<Map<String, String>> gbakRelList);

	/**
	 * 
	 * Discription:批量插入info.
	 * 
	 * @param param
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	void insertFltInfos(List<Map<String, String>> infoList);

	/**
	 * 
	 * Discription:批量插入InfoOther.
	 * 
	 * @param param
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	void insertFltInfoOthers(List<Map<String, String>> infoOtherList);

	/**
	 * 
	 * Discription:批量插入Iorel.
	 * 
	 * @param param
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	void insertFdFltIorels(List<Map<String, String>> ioRelList);

	/**
	 * 
	 * Discription:获取iorel新id.
	 * 
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年10月20日 yu-zd [变更描述]
	 */
	String getNewIorelId();
}
