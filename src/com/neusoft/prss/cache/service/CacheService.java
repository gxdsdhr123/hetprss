/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.cache.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import redis.clients.jedis.Jedis;

public interface CacheService {
	/**
	 *Discription:获取缓存.
	 *@param key redis key
	 *@param coumns 需要返回的列
	 *@return JSON数组
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月7日 gaojingdan [变更描述]
	 */
	public JSONArray getList(String key,String... coumns);
	/**
     *Discription:获取下拉数据项.
     *@param key 表名
     *@param idCol 编码列
     *@param textCol 名称列
     *@param others 其他列
     *@return JSON数组
     *@return:返回值意义
     *@author:baochl
     *@update:2017年9月5日 baochl [变更描述]
    */
    public JSONArray getOpts(String key,String idCol,String textCol,String... others);
    /**
	 *Discription:获取字典.
	 *@param key redis key
	 *@return JSON数组
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月7日 gaojingdan [变更描述]
	 */
	public JSONArray getCommonDict(String key) ;
	/**
	 *Discription:增加缓存
	 *@param key redis key
	 *@param value
	 *@param cacheSeconds生命周期，0不受限制，单位为秒
	 *@return 字符串
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月7日 gaojingdan [变更描述]
	 */
	public String setCache(String key,String value,int cacheSeconds);
	
	/**
	 *Discription:增加缓存,值为list
	 *@param key redis key
	 *@param value List类型
	 *@param cacheSeconds生命周期，0不受限制，单位为秒
	 *@return long
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月7日 gaojingdan [变更描述]
	 */
	public long setCacheList(String key,List<String> valueList,int cacheSeconds);
	/**
	 *Discription:增加缓存,值为hash
	 *@param key redis key
	 *@param value 为Map<String,String)
	 *@param cacheSeconds生命周期，0不受限制，单位为秒
	 *@return String
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月7日 gaojingdan [变更描述]
	 */
	public String setCacheMap(String key, Map<String, String> valueMap,int cacheSeconds);
	/**
	 *Discription:增量增加缓存,值为hash，不覆盖
	 *@param key redis key
	 *@param value 为Map<String,String)
	 *@param cacheSeconds生命周期，0不受限制，单位为秒
	 *@return String
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月7日 gaojingdan [变更描述]
	 */
	public String addCacheMap(String key, Map<String, String> valueMap,int cacheSeconds);
	/**
	 * 
	 *Discription:取map值.
	 *@param key redis key
	 *@param mapKey 值map对应的key
	 *@return value
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月9日 gaojingdan [变更描述]
	 */
	public String mapGet(String key,String mapKey);
	/**
	 * 
	 *Discription:put map值.
	 *@param key redis key
	 *@param mapKey 值map对应的key
	 *@return long
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月9日 gaojingdan [变更描述]
	 */
	public long mapPut(String key,String mapKey,String value);
	/**
	 * 
	 *Discription:get all map值.
	 *@param key redis key
	 *@return Map<String, String>
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月9日 gaojingdan [变更描述]
	 */
	public Map<String, String> getMap(String key);
	
	/**
	 * 
	 *Discription:mapRemove
	 *@param key redis key
	 *@return long
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月9日 gaojingdan [变更描述]
	 */
	public long mapDel(String key,String mapKey);
	/**
	 * 
	 *Discription:删除缓存
	 *@param key redis key
	 *@return long
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月9日 gaojingdan [变更描述]
	 */
	public long deleCache(String key);
	/**
	 * 
	 *Discription:获取Jedis客户端.
	 *@return Jedis
	 *@author:gaojingdan
	 *@update:2017年9月14日 gaojingdan [变更描述]
	 */
	public Jedis getJedisClient();
	/**
	 *Discription:根据航空编码获取航空公司名称，2字码3字码都可以，根据长度判断
	 *@param alCode 航空公司编码
	 *@return:航空公司名称
	 *@author:gaojingdan
	 *@update:2017年9月15日][gaojingdan][变更描述]
	 */
	public String getAlnShortNameByCode(String alCode);
	/**
	 *Discription:获取航空公司名称通过二字码
	 *@param code2 二字码
	 *@return String 航空公司名称
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月15日 gaojingdan [变更描述]
	 */
	public String getAlnShortNameByCode2(String code2);
	/**
	 *Discription:获取航空公司名称通过三字码
	 *@param code3 三字码
	 *@return 航空公司名称
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月15日 gaojingdan [变更描述]
	 */
	public String getAlnShortNameByCode3(String code3);
	/**
	 *Discription:根据编码获取机场名称
	 *@param code机场编码
	 *@return 机场名称
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年9月15日 gaojingdan [变更描述]
	 */
	public String getAirportNameByCode(String code);
	
}
