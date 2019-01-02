package com.neusoft.framework.modules.maintain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.modules.maintain.dao.RunningTimeDAO;

@Service
public class RunningTimeService extends BaseService {
	@Autowired
	private RunningTimeDAO runningTimeDAO;
	
	/**
	 *Discription:获取数据
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getDataList(Map<String,Object> param){
		return runningTimeDAO.getDataList(param);
	};
	/**
	 *Discription:获取总行数
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public int getTotalRows(Map<String,Object> param){
		return runningTimeDAO.getTotalRows(param);
	};
	/**
	 *Discription:获取登机口信息
	 *@param 
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getGateDataList(){
		return runningTimeDAO.getGateDataList();
	};
	
	/**
	 *Discription:根据员工ID获取作业类型
	 *@param 员工id
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getJobKindDataList(String id){
		return runningTimeDAO.getJobKindDataList(id);
	};
	/**
	 *Discription:新增数据
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void insertData(Map<String,String> param){
		runningTimeDAO.insertData(param);
	};
	/**
	 *Discription:检查数据是否冲突
	 *@param paramMap
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public int checkData(Map<String,String> param){
		return runningTimeDAO.checkData(param);
	};
	/**
	 *Discription:删除一条数据
	 *@param id
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void deleteOneData(String id){
		runningTimeDAO.deleteOneData(id);
	};
	/**
	 *Discription:查询详细信息
	 *@param id
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public List<JSONObject> getOneData(String id){
		return runningTimeDAO.getOneData(id);
	};
	/**
	 *Discription:修改数据
	 *@param string 
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void updateSql(String sql){
		runningTimeDAO.updateSql(sql);
	};
	/**
	 *Discription:批量删除数据
	 *@param 
	 *@return
	 *@return:返回值意义
	 *@author:tianjiliang
	 *@update:2017年8月28日 tianjiliang [变更描述]
	 */
	public void deleteMultiData(String[] idsArr){
		runningTimeDAO.deleteMultiData(idsArr);
	};
	
}
