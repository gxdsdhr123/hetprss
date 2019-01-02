package com.neusoft.prss.plan.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * 国际航班计划service
 * @author huanglj
 *
 */
public interface InternationalPlanService {
	/**
	 * 获取国际航班计划列表
	 * @return
	 */
	public JSONObject getGridData(Map<String, Object> paramMap);
	/**
	 * 根据ids获取国际航班计划列表
	 * @return
	 */
	public JSONArray getGridDataByIds(Map<String, Object> paramMap);
	/**
	 * 新增更新保存
	 * @param list
	 */
	public int planAdd(Map<String,Object> dataMap);
	/**
	 * 判断航班性质
	 * @param departApt
	 * @param arrivalApt
	 * @return
	 */
	public int getAttCode(String departApt, String arrivalApt);
	/**
	 * 删除计划
	 */
	public int delete(Map<String, Object> paramMap);
	/**
	 * 获取国际航班计划报文
	 * @param msgId
	 * @return
	 */
	public String getMsg(String msgId);

}
