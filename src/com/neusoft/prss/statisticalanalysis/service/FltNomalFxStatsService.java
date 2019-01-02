package com.neusoft.prss.statisticalanalysis.service;

import java.util.List;
import java.util.Map;

public interface FltNomalFxStatsService{
	
	public List<Map<String,Object>> getDataList(String startDate,String endDate,String type);

}
