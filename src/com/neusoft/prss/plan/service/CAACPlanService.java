package com.neusoft.prss.plan.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.plan.entity.CAACMainEntity;

/**
 * 局方长期计划service
 * @author baochl
 *
 */
public interface CAACPlanService {

    /**
     * 
     *Discription:获取局方计划.
     *@param params
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月12日 neusoft [变更描述]
     */
	public JSONArray getPlans(Map<String, String> params);
	
	/**
	 * 
	 *Discription:更新计划.
	 *@param params
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2018年4月12日 neusoft [变更描述]
	 */
	public void update(Map<String,String> params);
	
	/**
	 * 
	 *Discription:删除计划.
	 *@param ids
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2018年4月12日 neusoft [变更描述]
	 */
	public boolean delete(String ids);

	/**
	 * 
	 *Discription:新增、导入航班计划.
	 *@param dataArray
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2018年4月13日 neusoft [变更描述]
	 */
    public boolean insert(List<CAACMainEntity> dataArray);

    /**
     * 
     *Discription:导入计划.
     *@param bytes
     *@param suffix
     *@param name
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月14日 neusoft [变更描述]
     */
    public Boolean importPlan(byte[] bytes,String suffix,String name);

    /**
     * 
     *Discription:校验航班号班期.
     *@param list
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月28日 neusoft [变更描述]
     */
    public String filter(List<CAACMainEntity> list);

    /**
     * 
     *Discription:根据ID获取局方计划.
     *@param params
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月28日 neusoft [变更描述]
     */
    public JSONObject getPlanById(Map<String,String> params);

}
