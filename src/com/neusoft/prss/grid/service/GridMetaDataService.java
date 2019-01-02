package com.neusoft.prss.grid.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.grid.dao.GridMetaDataDao;

@Service
public class GridMetaDataService extends BaseService {
	@Autowired
	private GridMetaDataDao dao;

	public JSONArray getSchemas() {
		return dao.getSchemas();
	}
	
	public JSONArray getTables(Map<String,String> params) {
		return dao.getTables(params);
	}
}
