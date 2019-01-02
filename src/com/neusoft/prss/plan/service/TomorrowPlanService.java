package com.neusoft.prss.plan.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.plan.entity.TravelskyPlanEntity;

/**
 * 次日计划
 * @author baochl
 *
 */
public interface TomorrowPlanService {

    /**
     * 
     *Discription:获取计划列表.
     *@param params
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    public JSONArray getPlans(Map<String, String> params);

    /**
     * 
     *Discription:新增计划.
     *@param dataArray
     *@param userLoginName
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月19日 neusoft [变更描述]
     */
    public int insert(JSONArray saveDataArr,String userLoginName);

    /**
     * 
     *Discription:导入计划.
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月24日 neusoft [变更描述]
     */
    public void importPlan() throws Exception;

    /**
     * 
     *Discription:更新.
     *@param params
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月24日 neusoft [变更描述]
     */
    public int update(Map<String,String> params);

    /**
     * 
     *Discription:导入中航信计划到临时表中.
     *@param list
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月24日 neusoft [变更描述]
     */
    public void travelskyPlanAdd(List<TravelskyPlanEntity> list);

    /**
     * 
     *Discription:清空中航信临时表数据.
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月24日 neusoft [变更描述]
     */
    public void travelskyPlanDelete();

    /**
     * 
     *Discription:获取核对计划数据.
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月27日 neusoft [变更描述]
     */
    public JSONObject getFilterPlans();

    /**
     * 
     *Discription:对导入按钮校验.
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月27日 neusoft [变更描述]
     */
    public boolean filterImport();

    /**
     * 
     *Discription:批量删除.
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月1日 neusoft [变更描述]
     */
    public boolean deleteBatch(@Param("ids") String ids);
    
    /**
     * 批量删除次日计划临时表比对数据
     */
    public boolean deleteBatch(JSONArray dataArray);
    
    /**
     * 
     *Discription:批量保存.
     *@param dataArray
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月1日 neusoft [变更描述]
     */
    public void saveBatch(JSONArray dataArray);
    
    /**
     * 发布次日计划
     * @param departPlanData 起场次日计划
     * @param arrivalPlanData 落场次日计划
     * @return 返回发布的次日计划id，id1,id2...
     */
    public String publishTomorrowPlan(JSONArray departPlanData,JSONArray arrivalPlanData,String operUser) throws Exception;
    
    /**
     * @param departPlanData 起场次日计划
     * @param arrivalPlanData 落场次日计划
     */
    public int deleteTomorrowPlan(JSONArray departPlanData,JSONArray arrivalPlanData);
    
    /**
     * 导出次日计划
     */
    public byte[] exportTomorrowPlan(JSONArray arrivalPlans,JSONArray departPlans);
    
	/**
	 * 获取航班次日计划详情
	 */
	public JSONArray getFltPlanInfo(Map<String,String> paramMap);
	
	/**
	 * 保存次日计划
	 */
	public int saveTomorrowPlan(JSONArray addData,JSONObject editData,JSONObject removeData,Map<String,String> params);
	
	/**
	 * 更新驻场状态
	 */
	public void updateStationedState(JSONArray arrivalPlanData);
	
	/**
	 * 次日计划预测次日各时段出港航班数、出港旅客数、值机柜台建议开启数、安检通道建议开启数
	 */
	public Map<String,String> forecast();
	
	/**
	 * 获取次日计划预测配置
	 */
	public JSONObject getForecastConf();
	
	/**
	 * 保存次日计划预测配置
	 */
	public int saveForecastConf(JSONArray forecastConfData);
	
	/**
	 * 根据报文id获取报文原文数据
	 * @param msgId
	 */
	public String getMsgById(String msgId);
	
	/**
	 * 清空空管、中航信计划比对数据临时表数据
	 */
    public int tomorrowTempPlanDelete();
    
    /**
     * 获取次日已发布计划数
     */
    public int getTomorrowPublishedCount();
    
    /**
     * 清空次日计划表
     */
    public int tomorrowPlanDelete();
    
    /**
     * 删除已发布的次日动态记录
     */
    public int deleteTomorrowFltInfo(String fltDate);
}
