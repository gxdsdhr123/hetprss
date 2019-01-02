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
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.statisticalanalysis.service.SaFltNormalService;

/**
 * 航班正常率统计表
 */
@Controller
@RequestMapping(value = "${adminPath}/saFltNormal")
public class SaFltNormalController extends BaseController{
	@Autowired
	private SaFltNormalService saFltNormalService;
	
	/**
	 * 航班正常率统计表列表页
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		return "prss/statisticalanalysis/saFltNormalList";
	}
	
	/**
	 * 航班正常率统计表数据
	 */
	@RequestMapping(value="getData")
	@ResponseBody
	public JSONObject getData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
		try {
			rs = saFltNormalService.getDataList(params);
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
        String startDate = "";
        String endDate = "";
        if(params!=null){
			if(params.containsKey("startDate"))
				startDate = params.get("startDate")==null?"":params.get("startDate").toString();
			if(params.containsKey("endDate"))
				endDate = params.get("endDate")==null?"":params.get("endDate").toString();
		}
		try {
			String fileName = "航班正常率统计表" + startDate+"-"+endDate+ ".xlsx";
			setHeader(request, response, fileName);
			byte[] content = saFltNormalService.getExcelByte(params);
			response.getOutputStream().write(content);
            response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("航班正常率统计表" + e.getMessage());
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
