package com.neusoft.prss.flightdynamic.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.FileUtils;
import com.neusoft.prss.flightdynamic.dao.FltDynMonitorDao;
import com.neusoft.prss.flightdynamic.service.FltDynMonitorService;

@Service
public class FltDynMonitorServiceImpl implements FltDynMonitorService {

	@Autowired
	private FltDynMonitorDao fltDynMonitorDao;
	
	@Override
	public JSONArray getFltData() throws Exception {
		
		String dataSql = "SELECT * FROM SYS_USER";
		fltDynMonitorDao.getFltData(dataSql);
		
		String path = this.getClass().getResource("data.json").getPath();
		File file = new File(path);
        String content = FileUtils.readFileToString(file, "UTF-8");
        
        JSONArray dataArr = JSONObject.parseArray(content);
		return dataArr;
	}
}
