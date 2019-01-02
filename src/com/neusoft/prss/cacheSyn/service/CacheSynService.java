/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.cacheSyn.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface CacheSynService {
	/**
	 * 
	 *Discription:获取需要生成缓存的key列表
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月26日 gaojingdan [变更描述]
	 */
	public List<String> getNeedGenerateCacheList();
	/**
	 * 
	 *Discription:根据缓存名称获取缓存配置
	 *@param cacheName
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月26日 gaojingdan [变更描述]
	 */
	public JSONObject getCacheConfByName(String cacheName);
	
	/**
	 *Discription:根据配置生成缓存
	 *@param cacheConf
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月26日 gaojingdan [变更描述]
	 */
	public void doGenerateCache(JSONObject cacheConf);
	/**
	 * 运行
	 *Discription:方法功能中文描述.
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月28日 gaojingdan [变更描述]
	 */
	public void run();
}
