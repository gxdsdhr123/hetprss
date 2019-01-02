package com.neusoft.prss.statisticalanalysis.web;

import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
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
import com.neusoft.prss.statisticalanalysis.service.AbnormalClickRecordService;

/**
 * 手持机异常点击、违规点击统计,人员统计
 */
@Controller
@RequestMapping(value = "${adminPath}/abnormalClick")
public class AbnormalClickRecordController extends BaseController {

	@Resource
    private AbnormalClickRecordService abnormalClickRecordService;
	
	/**
	 * 手持机异常点击、违规点击统计页
	 */
	@RequestMapping(value="reskindClickList")
	public String reskindList(Model model){
		//获取作业类型
		model.addAttribute("reskinds", abnormalClickRecordService.getReskindList());
		return "prss/statisticalanalysis/abnormalReskindClickList";
	}
	
	/**
	 * 手持机异常点击、违规点击统计记录
	 */
	@RequestMapping(value="reskindClickData")
	@ResponseBody
	public JSONObject getReskindClickData(HttpServletRequest request){
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
			rs = abnormalClickRecordService.getReskindClickList(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 手持机异常点击、违规点击统计记录详情
	 */
	@RequestMapping(value="reskindClickDetail")
	@ResponseBody
	public JSONObject getReskindClickDetail(HttpServletRequest request){
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
			rs = abnormalClickRecordService.getReskindClickDetail(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 导出手持机异常点击、违规点击统计记录
	 */
	@RequestMapping(value = "exportReskindClickExcel")
	public void exportReskindClickExcel(HttpServletRequest request, HttpServletResponse response) {
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
		String columnName = "[{\"field\":\"startDate\"},{\"field\":\"endDate\"},"
				+ "{\"field\":\"kindName\"},{\"field\":\"clickBefore\"},{\"field\":\"clickAfter\"},"
				+ "{\"field\":\"safeguardTime\"},{\"field\":\"nodeUnnormal\"},{\"field\":\"noSafeGuard\"},{\"field\":\"clickUnnormal\"},"
				+ "{\"field\":\"jobAllnum\"},{\"field\":\"clickUnnormalRatio\"}]";
		String[] title = { "开始日期", "结束如期", "保障类型", "提前点击", "滞后点击", "保障时长异常", "节点采集异常","任务未保障", "不正常点击任务数", "任务总数", "不正常点击率" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			String fileName = "手持机异常点击、违规点击统计" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			excel.setDataList(title, columnArr, abnormalClickRecordService.getReskindClickDownList(params));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手持机异常点击、违规点击统计" + e.getMessage());
		}
	}
	
	/**
	 * 手持机异常点击、违规点击人员统计页
	 */
	@RequestMapping(value="userClickList")
	public String userClickList(Model model){
		//获取作业类型
		model.addAttribute("reskinds", abnormalClickRecordService.getReskindList());
		return "prss/statisticalanalysis/abnormalUserClickList";
	}
	
	/**
	 * 手持机异常点击、违规点击人员统计记录
	 */
	@RequestMapping(value="userClickData")
	@ResponseBody
	public JSONObject getUserClickData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
        String pageNumber = "";
		String pageSize = "";
		String searchValue = "";
		if(params!=null){
			if(params.containsKey("pageNumber"))
				pageNumber = params.get("pageNumber")==null?"":params.get("pageNumber").toString();
			if(params.containsKey("pageSize"))
				pageSize = params.get("pageSize")==null?"":params.get("pageSize").toString();
			if(params.containsKey("searchValue"))
				searchValue = params.get("searchValue")==null?"":params.get("searchValue").toString();
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
			if(searchValue!=null&&!searchValue.equals("")){
				searchValue = URLDecoder.decode(searchValue,"UTF-8");
				params.put("searchValue", searchValue);
			}
			params.put("begin", begin);
			params.put("end", end);
			rs = abnormalClickRecordService.getUserClickList(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 导出手持机异常点击、违规点击人员统计记录
	 */
	@RequestMapping(value = "exportUserClickExcel")
	public void exportUserClickExcel(HttpServletRequest request, HttpServletResponse response) {
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
        String searchValue = "";
		if(params!=null){
			if(params.containsKey("searchValue"))
				searchValue = params.get("searchValue")==null?"":params.get("searchValue").toString();
		}
		String columnName = "[{\"field\":\"startDate\"},{\"field\":\"endDate\"},"
				+ "{\"field\":\"groupName\"},{\"field\":\"userName\"},{\"field\":\"allTask\"},"
				+ "{\"field\":\"vgTask\"},{\"field\":\"clickNormalRatio\"}]";
		String[] title = { "开始日期", "结束日期", "班组", "姓名", "完成任务数", "违规任务数", "点击正常率" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			if(searchValue!=null&&!searchValue.equals("")){
				searchValue = URLDecoder.decode(searchValue,"UTF-8");
				params.put("searchValue", searchValue);
			}
			String fileName = "手持机异常点击、违规点击人员统计记录" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			excel.setDataList(title, columnArr, abnormalClickRecordService.getUserClickDownList(params));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手持机异常点击、违规点击人员统计记录" + e.getMessage());
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
