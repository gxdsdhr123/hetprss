package com.neusoft.prss.plan.service;

import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 空管次日计划
 * 
 * @author baochl
 *
 */
public interface ATMBPlanService {
	/**
	 * 获取航班列表
	 * 
	 * @param paramMap
	 * @return
	 */
	public JSONArray getGridData(Map<String, String> paramMap);
	/**
	 * 获取报文库中空管次日计划个数，判断是否有次日计划
	 * @param paramMap
	 * @return
	 */
	public int getPalnCount();
	/**
	 * 导入
	 * @param paramMap
	 * @return
	 */
	public String importData(Map<String, String> paramMap);
	/**
	 * 保存
	 * @param addData 新增的行
	 * @param editData 修改的行
	 * @param removeData 删除的行
	 * @param params 其他参数
	 * @return
	 */
	public void save(JSONArray addData,JSONObject editData,JSONObject removeData,Map<String,String> params);
	/**
	 * 新增
	 * @return
	 */
	public int add(JSONObject row);
	/**
	 * 修改
	 * @return
	 */
	public int update(JSONObject row);
	/**
	 * 删除
	 * @return
	 */
	public int remove(JSONArray rowDatas);
	/**
	 * 获取航班详情
	 * @param paramMap
	 * @return
	 */
	public JSONArray getFltInfo(Map<String,String> paramMap);
	/**
	 * 发布
	 */
	public int publish();
	/**
	 * 导出
	 * @return
	 */
	public byte[] export(Map<String,String> paramMap);
	
	/**
	 * 导入excel计划
	 * @return 返回校验错误信息
	 */
	public Map<String,Object> importExcelData(byte[] fileByteArr,String userId,String fileType) throws IOException;
	
	/**
	 * 根据报文id获取报文原文数据
	 * @param msgId
	 */
	public String getMsgById(String msgId);
}
