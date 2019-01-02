package com.neusoft.prss.statisticalanalysis.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.SaFltGcmWaitingService;

/**
 * 关舱门后机坪等待超60分钟以上航班情况即时报告表
 */
@Controller
@RequestMapping(value = "${adminPath}/saFltGcmWaiting")
public class SaFltGcmWaitingController extends BaseController{
	@Autowired
	private SaFltGcmWaitingService saFltGcmWaitingService;
	
	/**
	 * 航班情况列表页
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		return "prss/statisticalanalysis/saFltGcmWaitingList";
	}
	
	/**
	 * 航班情况列表数据
	 */
	@RequestMapping(value="getData")
	@ResponseBody
	public JSONObject getData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
        String pageNumber = "";
		String pageSize = "";
		if(params!=null){
			if(params.containsKey("pageNumber"))
				pageNumber = params.get("pageNumber")==null?"":params.get("pageNumber").toString();
			if(params.containsKey("pageSize"))
				pageSize = params.get("pageSize")==null?"":params.get("pageSize").toString();
		}
		try {
			int begin = 1;
			int end = 21;
			if(pageNumber!=null&&!pageNumber.equals("")&&pageSize!=null&&!pageSize.equals("")){
				int intPageNum = Integer.parseInt(pageNumber);
				int intPageSize = Integer.parseInt(pageSize);
				begin=(intPageNum - 1) * intPageSize+1;
		        end=intPageNum * intPageSize+1;
			}
			params.put("begin", begin);
			params.put("end", end);
			rs = saFltGcmWaitingService.getDataList(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	@RequestMapping(value = "exportExcel")
	public void printList(HttpServletRequest request, HttpServletResponse response) {
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
		String columnName = "[{\"field\":\"flightDate\"},{\"field\":\"flightNumber\"},"
				+ "{\"field\":\"routeName\"},{\"field\":\"std\"},{\"field\":\"difufuwuGcmTm\"},"
				+ "{\"field\":\"atd\"},{\"field\":\"waitingTm\"},{\"field\":\"waitingReason\"},"
				+ "{\"field\":\"remark\"}]";
		String[] title = { "日期", "航班号", "航段", "计划离港时间", "关舱门时间", "实际起飞时间", "关舱门候机坪等待时间(分钟)", "等待原因", "备注" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			String fileName = "关舱门后机坪等待超60分钟以上航班情况即时报告表" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			excel.setDataList(title, columnArr, saFltGcmWaitingService.getDownDataList(params));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("关舱门后机坪等待超60分钟以上航班情况即时报告表" + e.getMessage());
		}
	}
	
    private void setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		try {
			response.reset();
	        response.setContentType("application/octet-stream; charset=utf-8");
	        String agent = (String) request.getHeader("USER-AGENT");
	        String downloadFileName = "";
	        if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
	            downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
	        } else {
	            downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
		}catch(Exception e){
			logger.error("set header error:"+e.toString());
		}
	}
}
