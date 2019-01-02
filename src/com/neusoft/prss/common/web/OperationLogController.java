package com.neusoft.prss.common.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.service.OperationLogService;

/**
 * 操作日志
 * 
 * @author baochl
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/common/operLog")
public class OperationLogController extends BaseController {
	@Autowired
	private OperationLogService service;

	@RequestMapping(value = "")
	public String index() {
		return "prss/common/operLogList";
	}
	@RequestMapping(value = "gridData")
	@ResponseBody
	public String gridData(HttpServletRequest request) {
		int offset = Integer.valueOf(request.getParameter("offset"));
		int limit = Integer.valueOf(request.getParameter("limit"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String colName = request.getParameter("colName");
		String keyword = request.getParameter("keyword");
		Map<String,String> params = new HashMap<String,String>();
		params.put("begin", String.valueOf(offset+1));
		params.put("end", String.valueOf(offset+limit));
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("colName", colName);
		params.put("keyword", keyword);
		JSONArray rows = service.getOperationLog(params);
		int total = service.getTotalRow(params);
		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		return result.toString();
	}
}
