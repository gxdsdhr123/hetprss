package com.neusoft.prss.stats.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
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
import com.neusoft.prss.stats.service.DragPlaneRecordService;

/**
 * 拖飞机记录
 */
@Controller
@RequestMapping(value = "${adminPath}/stats/dragPlaneRecord")
public class DragPlaneRecordController extends BaseController{
	@Autowired
	private DragPlaneRecordService dragPlaneRecordService;
	
	/**
	 * 拖飞机记录列表页
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		return "prss/stats/dragPlaneRecordList";
	}
	
	/**
	 * 拖飞机记录列表数据
	 */
	@RequestMapping(value="getData")
	@ResponseBody
	public JSONObject getData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String fltNo = request.getParameter("fltNo");
		String aircraftNumber = request.getParameter("aircraftNumber");
		String startDate = request.getParameter("startDate");
		String userName = request.getParameter("userName");
		String pageNumber = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			int begin = 1;
			int end = 21;
			if(pageNumber!=null&&!pageNumber.equals("")&&pageSize!=null&&!pageSize.equals("")){
				int intPageNum = Integer.parseInt(pageNumber);
				int intPageSize = Integer.parseInt(pageSize);
				begin=(intPageNum - 1) * intPageSize+1;
		        end=intPageNum * intPageSize+1;
			}
			userName = URLDecoder.decode(userName,"UTF-8");
			param.put("begin", begin);
			param.put("end", end);
			param.put("fltNo", fltNo==null?"":fltNo.toUpperCase());
			param.put("aircraftNumber", aircraftNumber==null?"":aircraftNumber.toUpperCase());
			param.put("createTm", startDate);
			param.put("userName", userName);
			rs = dragPlaneRecordService.getDataList(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	@RequestMapping(value = "exportExcel")
	public void printList(HttpServletRequest request, HttpServletResponse response) {
		String fltNo = request.getParameter("fltNo");
		String aircraftNumber = request.getParameter("aircraftNumber");
		String startDate = request.getParameter("startDate");
		String userName = request.getParameter("userName");
		Map<String, Object> param = new HashMap<String, Object>();
		String columnName = "[{\"field\":\"rn\"},{\"field\":\"fltNo\"},{\"field\":\"aircraftNumber\"},"
				+ "{\"field\":\"fromStand\"},{\"field\":\"toStand\"},{\"field\":\"eStartTm\"},"
				+ "{\"field\":\"eEndTm\"},{\"field\":\"sStartTm\"},{\"field\":\"sEndTm\"},"
				+ "{\"field\":\"createTm\"},{\"field\":\"createUserName\"},{\"field\":\"stateTmConfirm\"},{\"field\":\"stateTmDel\"},{\"field\":\"operUserName\"}]";
		String[] title = { "序号", "航班号", "机号", "原机位", "拖至机位", "预计开始时间", "预计结束时间", "实际开始时间", "实际结束时间", "创建任务时间", "新增操作人", "确认时间", "删除时间", "确认/删除操作人" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			userName = URLDecoder.decode(userName,"UTF-8");
			param.put("fltNo", fltNo==null?"":fltNo.toUpperCase());
			param.put("aircraftNumber", aircraftNumber==null?"":aircraftNumber.toUpperCase());
			param.put("createTm", startDate);
			param.put("userName", userName);
			String fileName = "拖飞机记录" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			excel.setDataList(title, columnArr, dragPlaneRecordService.getDownDataList(param));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("拖飞机记录" + e.getMessage());
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
