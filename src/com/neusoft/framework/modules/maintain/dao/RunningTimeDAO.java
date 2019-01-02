package com.neusoft.framework.modules.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface RunningTimeDAO extends BaseDao {

	/**
	 *Discription:获取数据
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getDataList(Map<String,Object> param); 
	/**
	 *Discription:获取登机口信息
	 *@param 
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getGateDataList(); 
	/**
	 *Discription:根据员工ID获取作业类型
	 *@param 
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getJobKindDataList(@Param("id") String id);
	/**
	 *Discription:获取总行数
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public int getTotalRows(Map<String,Object> param);
	/**
	 *Discription:新增数据
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void insertData(Map<String,String> param);
	/**
	 *Discription:检查数据是否冲突
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public int checkData(Map<String,String> param);
	/**
	 *Discription:删除一条数据
	 *@param id
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void deleteOneData(@Param("id") String id);
	/**
	 *Discription:查询详细信息
	 *@param id
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getOneData(@Param("id") String id);
	/**
	 *Discription:修改数据
	 *@param string 
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void updateSql(@Param("sql") String sql);
	/**
	 *Discription:批量删除数据
	 *@param 
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void deleteMultiData(String[] idsArr);
}
