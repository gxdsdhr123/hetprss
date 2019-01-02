package com.neusoft.prss.plan.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.plan.entity.TravelskyPlanEntity;

/**
 * 中航信长期计划
 * @author huanglj
 *
 */
public interface TravelskyPlanService {
	/**
	 * 中航信进出港报文解析
	 */
	public List<TravelskyPlanEntity> travelskyPlanImport(byte[] buffer,String userId,String type);
	/**
	 * 计划新增
	 * @param list
	 * @return
	 */
	public int travelskyPlanAdd(List<TravelskyPlanEntity> list);
	/**
	 * 删除计划
	 * @param ids
	 */
	public int delete(String ids);
	/**
	 * 获取中航信计划
	 * @param params
	 * @return
	 */
	public JSONArray getGridData(String begin,String end,String ioType,String startDate,String endDate );
	/**
	 * 获取总行数
	 * @param params
	 * @return
	 */
	public int getTotalRow(String begin,String end,String ioType,String startDate,String endDate);
	public byte[] printData(String startDate,String endDate);
	public JSONArray getFltInfo(Map<String, String> paramMap);
	public void save(JSONArray addData, JSONObject editData, JSONObject removeData, Map<String, String> params);
}
