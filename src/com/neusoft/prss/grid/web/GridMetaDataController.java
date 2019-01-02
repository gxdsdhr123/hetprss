package com.neusoft.prss.grid.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.grid.service.GridMetaDataService;

@Controller
@RequestMapping(value = "${adminPath}/grid/metaData")
public class GridMetaDataController extends BaseController {
	@Autowired
	private GridMetaDataService service;

	@RequestMapping(value = "")
	public String index() {
		return "prss/gridmetadata/gridMetaDataList";
	}

	@RequestMapping(value = "schemas")
	@ResponseBody
	public JSONArray getSchemas() {
		return service.getSchemas();
	}
	@RequestMapping(value = "tables")
	@ResponseBody
	public JSONArray getTables(String schemaId) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("schemaId", schemaId);
		return service.getTables(params);
	}
}
