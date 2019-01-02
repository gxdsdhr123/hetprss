package com.neusoft.prss.flightdynamic.service;

import com.alibaba.fastjson.JSONArray;

public interface FltDynMonitorService {

	/**
	 * 获取航班数据
	 * @return
	 * @throws Exception 
	 */
	JSONArray getFltData() throws Exception;

}
