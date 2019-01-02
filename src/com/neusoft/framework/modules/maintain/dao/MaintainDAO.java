/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月25日 下午7:23:03
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.framework.modules.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.framework.modules.maintain.entity.TableVO;

@MyBatisDao
public interface MaintainDAO extends BaseDao {
	/**
	 *Discription:初始化
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月28日 gaojingdan [变更描述]
	 */
	public TableVO initTable(Map<String,String> paramMap);
	/**
	 * 
	 *Discription:传入SQL，返回查询结果
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月29日 gaojingdan [变更描述]
	 */
	public List<JSONObject> getDataList(@Param("sql") String sql);
	/**
	 * 
	 *Discription:传入SQL，返回查询结果
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月29日 gaojingdan [变更描述]
	 */
	public Map<String,String> getDataMap(@Param("sql") String sql);
	
	/**
	 * 
	 *Discription:传入SQL，返回字符串
	 *@param sql
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月29日 gaojingdan [变更描述]
	 */
	public String getDataString(@Param("sql") String sql);
	
	/**
	 *Discription:传入SQL，执行UPDATE
	 *@param sql
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	public void doUpdate(@Param("sql") String sql);
	/**
	 *Discription:传入SQL，执行INSERT
	 *@param sql
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	public void doInsert(@Param("sql") String sql);
	/**
	 * 
	 *Discription：根据表名获取缓存配置
	 *@param sourceTable
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年1月19日 gaojingdan [变更描述]
	 */
	public List<JSONObject> getCacheConfByTableName(@Param("sourceTable")String sourceTable);
	/**
	 * 
	 *Discription：页面操作日志
	 *@param map
	 *@return
	 *@return:返回值意义
	 *@author:yunwq
	 *@update:2018年9月20日 yunwq [变更描述]
	 */
	public void doInsertLog(Map<String, String> map);
}
