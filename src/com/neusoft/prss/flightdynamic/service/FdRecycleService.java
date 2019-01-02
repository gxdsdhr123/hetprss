/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月19日 下午3:45:43
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.flightdynamic.dao.FdRecycleDao;
import com.neusoft.prss.flightdynamic.entity.FdFltGbak;

@Service
public class FdRecycleService {
	@Resource
	private FdRecycleDao fdRecycleDao;
	
	/**
	 * 
	 *Discription:获取航班动态主表信息.
	 *@param param
	 *@return
	 *@return:
	 *@author:yu-zd
	 *@update:2017年9月8日 yu-zd [变更描述]
	 */
	public JSONArray getLongtermMain(Map<String,String> param) {
		return fdRecycleDao.getFdMain(param);
	}
	/**
	 * 
	 *Discription:获取航班动态次表信息.
	 *@param param
	 *@return
	 *@return:
	 *@author:yu-zd
	 *@update:2017年9月8日 yu-zd [变更描述]
	 */
	public JSONArray getLongtermDetail(Map<String, String> param) {
		return fdRecycleDao.getFdDetail(param);
	}
	
	/**
	 * 
	 *Discription:批量插入gbak.
	 *@param param
	 *@return
	 *@return:
	 *@author:yu-zd
	 *@update:2017年9月8日 yu-zd [变更描述]
	 */
	@Transactional(readOnly = false)
	public void insertFdFltGbaks(List<FdFltGbak> gbakList) {
		fdRecycleDao.insertFdFltGbaks(gbakList);
	}
	/**
	 * 
	 *Discription:批量插入gbakrel.
	 *@param param
	 *@return
	 *@return:
	 *@author:yu-zd
	 *@update:2017年9月8日 yu-zd [变更描述]
	 */
	@Transactional(readOnly = false)
	public void insertFdFltGbakrels(List<Map<String, String>> gbakRelList) {
		fdRecycleDao.insertFdFltGbakrels(gbakRelList);
	}
	/**
	 * 
	 *Discription:批量插入info.
	 *@param param
	 *@return
	 *@return:
	 *@author:yu-zd
	 *@update:2017年9月8日 yu-zd [变更描述]
	 */
	@Transactional(readOnly = false)
	public void insertFltInfos(List<Map<String, String>> infoList) {
		fdRecycleDao.insertFltInfos(infoList);
	}
	/**
	 * 
	 *Discription:批量插入InfoOther.
	 *@param param
	 *@return
	 *@return:
	 *@author:yu-zd
	 *@update:2017年9月8日 yu-zd [变更描述]
	 */
	@Transactional(readOnly = false)
	public void insertFltInfoOthers(List<Map<String, String>> infoOtherList) {
		fdRecycleDao.insertFltInfoOthers(infoOtherList);
	}
	/**
	 * 
	 *Discription:批量插入Iorel.
	 *@param param
	 *@return
	 *@return:
	 *@author:yu-zd
	 *@update:2017年9月8日 yu-zd [变更描述]
	 */
	@Transactional(readOnly = false)
	public void insertFdFltIorels(List<Map<String, String>> ioRelList) {
		fdRecycleDao.insertFdFltIorels(ioRelList);
	}
}
