package com.neusoft.prss.produce.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 特车计费、计费详情单据
 */
public interface BillTcFwService{
	/**
	 * 获取特车计费列表数据
	 */
	public JSONObject getBillTcFwList(Map<String, Object> paramMap);
	
	/**
	 * 根据进出港航班号、航班日期获取航班详细信息
	 */
	public JSONObject getFlightDetail(Map<String, Object> paramMap);
	
	/**
	 * 获取服务单号，使用序列生成
	 */
	public String getServiceNumber();
	
	/**
	 * 保存特车计费单数据
	 */
	public int saveBillTcFw(Map<String, Object> paramMap);
	
	/**
	 * 删除特车计费单数据
	 */
	public int deleteBillTcFw(Map<String, Object> paramMap);
	
	/**
	 * 获取特车计费列表、详细数据总数
	 */
	public int getBillTcFwCount(Map<String, Object> paramMap);
	
	/**
	 * 导出、打印特车计费列表、特车计费单
	 */
	public byte[] exportExcel(Map<String, Object> paramMap);
}
