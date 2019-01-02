package com.neusoft.prss.statisticalanalysis.service;

import java.util.List;

import com.neusoft.prss.statisticalanalysis.entity.FwDelayInfo;

public interface FwDelayInfoService {

	/**
	 * 查询旅客服务航延信息统计条目
	 * @param startDate
	 * @param endDate
	 * @param airline
	 * @return
	 */
	Integer getDataListCount(String startDate, String endDate, String airline);
	
	/**
	 * 查询旅客服务航延信息统计列表
	 * @param pageNumber
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param airline
	 * @return
	 */
	List<FwDelayInfo> getDataList(Integer pageNumber, Integer pageSize,
			String startDate, String endDate, String airline);

	/**
	 * 查询旅客服务航延信息统计列表（不分页）
	 * @param startDate
	 * @param endDate
	 * @param airline
	 * @return
	 */
	List<FwDelayInfo> getAllDataList(String startDate, String endDate,
			String airline);


}
