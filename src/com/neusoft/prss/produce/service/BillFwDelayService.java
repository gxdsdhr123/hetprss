package com.neusoft.prss.produce.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.entity.DelayInfo;
import com.neusoft.prss.produce.entity.DelayInfoDetail;

public interface BillFwDelayService {

	/**
	 * 查询旅客服务航延信息统计条目
	 * @param startDate
	 * @param endDate
	 * @param airline
	 * @return
	 */
	Integer getDataListCount(String startDate, String endDate, String searchText);
	
	/**
	 * 查询旅客服务航延信息统计列表
	 * @param pageNumber
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param airline
	 * @return
	 */
	List<DelayInfo> getDataList(Integer pageNumber, Integer pageSize,
			String startDate, String endDate, String searchText);

	/**
	 * 查询航班信息
	 * @param fltid
	 * @return
	 */
	JSONObject getDelayFltInfo(String fltid);
	
	/**
	 * 根据fltid查询单据详情
	 * @param id
	 * @return
	 */
	DelayInfo getDelayInfo(String fltid);
	
	/**
	 * 保存航延信息
	 * @param delay
	 * @return
	 * @throws Exception
	 */
	String saveDelay(DelayInfo delay) throws Exception;
	
	/**
	 * 查询旅客服务航延信息统计列表（导出用）
	 * @param startDate
	 * @param endDate
	 * @param ids
	 * @return
	 */
	List<DelayInfo> getExportDataList(String startDate, String endDate,
			String ids);


}
