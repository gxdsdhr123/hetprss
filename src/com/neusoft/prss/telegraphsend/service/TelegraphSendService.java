package com.neusoft.prss.telegraphsend.service;

import com.alibaba.fastjson.JSONObject;

public interface TelegraphSendService {
	
	// boolean sendTelegraphByEmail(String id,String fltID,JSONObject json);
	
	/** 
	* @Description 发送邮件
	* @param
	* @return boolean
	* @update 2017年10月25日 上午9:19:36 hujl 
	*/
	boolean sendEmail(JSONObject json,String[] toAddressArr,boolean ifAuto);
	
	/** 
	* @Description 将报文信息插入TM_INFO表
	* @param
	* @return void
	* @update 2017年10月25日 上午9:20:26 hujl 
	*/
	void insertTmInfo(JSONObject json);

	/** 
	* @Description 将报文信息插入TM_INFO_TO表
	* @param
	* @return void
	* @update 2017年10月25日 上午9:20:46 hujl 
	*/
	void insertTmInfoTo(JSONObject json);
	
	/** 
	* @Description 将手动发送报文信息插入TM_SEND_MANUAL表
	* @param
	* @return void
	* @update 2017年10月25日 上午9:21:02 hujl 
	*/
	void insertTmSendManual(JSONObject json);
	
	/**
	 *Discription 获取报文数据
	 *@param jsonobject("tgtmID",value)
	 *@return JSONObject{("TM_TEMPLATE",JSONObject),
	 *					("TM_TEMPLATE_TO",JSONArray),
	 *					("TM_TEMPLATE_FROM",JSONObject)}
	 *@author hujl
	 *@update 2017年10月21日 hujl [变更描述]
	 */
	JSONObject getTelegraphInfo(JSONObject json);
	
	/** 
	* @Description 发送报文接口
	* @param
	* @return boolean
	* @update 2017年10月25日 上午9:22:24 hujl 
	*/
	boolean sendTelegraph(JSONObject json) ;
	
	/** 
	* @Description 发送邮件类型报文接口
	* @param
	* @return boolean
	* @update 2017年10月25日 上午9:22:44 hujl 
	*/
	JSONObject sendTelegraphByEmail(JSONObject json,String[] toEmailarr);
	
	/** 
	* @Description 发送Sita类型报文接口
	* @param
	* @return boolean
	* @update 2017年10月25日 上午9:23:14 hujl 
	*/
	JSONObject sendTelegraphBySita(JSONObject json,String[] toSitaArr);
	
	/** 
	* @Description 通过自动发送表执行自动发送
	* @param json : {"auto_id" : TM_SEND_AUTO表ID , "fltID" : 航班ID}
	* @return JSONObject
	* @update 2017年11月15日 下午5:26:47 hujl 
	*/
	JSONObject autoSendTest(JSONObject json);
	
	/** 
	* @Description 报文参数替换
	* @param 
	* @return String
	* @update 2017年11月30日 下午3:18:30 hujl 
	*/
	String paramReplace(JSONObject json,String content,String varcols);

}
