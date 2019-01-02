/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年2月7日 下午3:35:31
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.api.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.api.service.SchedulingExportApiService;
import com.neusoft.prss.grid.service.GridColumnService;
import com.neusoft.prss.scheduling.service.SchedulingListService;

@Controller
@RequestMapping(value="/api/rest/schedulingAutoExport")
public class SchedulingExportApiController {
	@Autowired
	private SchedulingListService listService;
	@Autowired
	private SchedulingExportApiService schedulingExportApiService;
	@Autowired
    private GridColumnService gridService;
	/**
	 *Discription:获取用户定义的列.
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年2月8日 gaojingdan [变更描述]
	 */
	@RequestMapping("getSheet1ExportColumn")
	@ResponseBody
	public String getSheet1ExportColumn(HttpServletRequest request){
		String userId=request.getParameter("userId");//用户ID
		String reskind=request.getParameter("reskind");
		String schema=schedulingExportApiService.getSchemaByReskind(reskind);
		String columns = gridService.getColumns(userId, schema);//默认表头
		return columns;
	}
	/**
	 * 
	 *Discription:获取需要导出的数据.
	 *@param request
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年2月7日 gaojingdan [变更描述]
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("getSheet1ExportData")
	@ResponseBody
	public String getSheet1ExportData(HttpServletRequest request){
		String reskind=request.getParameter("reskind");
		String switches=request.getParameter("switches");
		String flagBGS=request.getParameter("flagBGS");
		String param=request.getParameter("param");
		String suffix=request.getParameter("suffix");
		String userId=request.getParameter("userId");//用户ID
		
		String schema=schedulingExportApiService.getSchemaByReskind(reskind);
		//第一个sheet页数据
		JSONArray jsonArr=new JSONArray();
		if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            Map<String,Object> params = JSON.parseObject(param);
            if (params.get("vipFlags") != null && StringUtils.isNotBlank(params.get("vipFlags").toString())) {
                String vipFlags = params.get("vipFlags").toString();
                String vFlags[] = vipFlags.split(",");
                params.put("vipFlags", vFlags);
            }
            jsonArr = listService.getDynamic(switches, flagBGS, reskind, schema, suffix,
            		userId, params);
        } else {
        	jsonArr = listService.getDynamic(switches, flagBGS, reskind, schema, suffix,
        			userId);
        }
		return JSON.toJSONString(jsonArr, SerializerFeature.WriteMapNullValue);
	}
	/**
	 *Discription:获取sheet2表头
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年2月8日 gaojingdan [变更描述]
	 */
	@RequestMapping("getSheet2ExportColumn")
	@ResponseBody
	public String getSheet2ExportColumn(HttpServletRequest request){
		String reskind=request.getParameter("reskind");
		String schema=schedulingExportApiService.getSchemaByReskind(reskind);
		JSONArray jsonArr = listService.getSheet2Title(schema);//默认表头
		return jsonArr.toString();
	}
	/**
	 *Discription:获取第二个sheet页数据.
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年2月8日 gaojingdan [变更描述]
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("getSheet2ExportData")
	@ResponseBody
	public String getSheet2ExportData(HttpServletRequest request){
		String reskind=request.getParameter("reskind");
		String switches=request.getParameter("switches");
		String flagBGS=request.getParameter("flagBGS");
		String param=request.getParameter("param");
		String suffix=request.getParameter("suffix");
		String schema=schedulingExportApiService.getSchemaByReskind(reskind);
		Map<String,Object> params = null;
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            params = JSON.parseObject(param);
            if (params.get("vipFlags") != null && StringUtils.isNotBlank(params.get("vipFlags").toString())) {
                String vipFlags = params.get("vipFlags").toString();
                String vFlags[] = vipFlags.split(",");
                params.put("vipFlags", vFlags);
            }
        }
		JSONArray data2 = listService.getPrintData(switches, flagBGS, reskind, schema, suffix, params);
		return JSON.toJSONString(data2, SerializerFeature.WriteMapNullValue);
	}
}
