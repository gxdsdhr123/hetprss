package com.neusoft.prss.telegraph.service;

import com.alibaba.fastjson.JSONArray;

public interface TelegraphService {

	/**
	 * 匹配报文模板解析报文
	 * 
	 * @author shu.yx
	 * @update 2017年10月25日09:23:38
	 * @param telegraphs
	 */
	JSONArray analysisTelegraphMatchTm(String[] telegraphs);
	
	/**
	 * 报文解析
	 * 
	 * @param telegraphs
	 * @return
	 */
	JSONArray analysisTelegraph(String[] telegraphs);
	
	/** 
	* @Description 手动解析报文 针对MVT下的AD报
	* @param
	* @return JSONArray
	* @update 2017年12月23日 下午2:44:36 hujl 
	*/
	JSONArray analysisTelegraphManual(String[] telegraphs);

}
