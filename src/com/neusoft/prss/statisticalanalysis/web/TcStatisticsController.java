/**
 *application name:hetprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年8月17日 下午14:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.stand.entity.ResultByCus;
import com.neusoft.prss.statisticalanalysis.service.TcStatisticsService;


/**
 * 
 *Discription:特车统计页面
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/tc/statistics")
public class TcStatisticsController extends BaseController {
	
	@Autowired
	private TcStatisticsService tcStatisticsService;
	private Logger logger=Logger.getLogger(TcStatisticsController.class);
	
	
    @RequestMapping(value = "list" )
    public String flightList(Model model) {
    	String defaultStart=DateUtils.getDate("yyyy.MM.dd");
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
        return "prss/statisticalanalysis/tcStatisticsList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String dateStart,String dateEnd) {
    	
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8");
    		dateStart=DateUtils.formatDate(DateUtils.parseDate(dateStart), "yyyyMMdd");
    		dateEnd=DateUtils.formatDate(DateUtils.parseDate(dateEnd), "yyyyMMdd");
		} catch (Exception e) {
			logger.error("/tc/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
        return tcStatisticsService.getDataList(param);
    }
    /**
     * 特车参数设置
     * @param model
     * @return
     */
    @RequestMapping(value="getTcCoefficient")
    public String getTcCoefficient(Model model){
    	model.addAttribute("tcCoefficient", tcStatisticsService.getTcCoefficient());
    	return "prss/statisticalanalysis/tcCoefficientForm";
    }
    
    /**
     * 保存特车参数设置
     * @param formData
     * @return
     */
    @RequestMapping(value="saveTcCoefficient")
    @ResponseBody
    public ResultByCus saveTcCoefficient(String formData){
    	ResultByCus result=new ResultByCus();
    	try{
    		formData = StringEscapeUtils.unescapeHtml4(formData);
    		List<JSONObject> list = JSONObject.parseArray(formData,JSONObject.class);
        	tcStatisticsService.saveTcCoefficient(list);
        	result.setCode("0000");
        	result.setMsg("操作成功");
    	}catch(Exception e){
    		logger.error("/tc/statistics/saveTcCoefficient出现错误 "+e.getMessage());
    		result.setCode("1000");
        	result.setMsg("操作失败");
    	}
    	
    	return result;
    }
    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,
    		String dateStart,String dateEnd) {
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8");
    		dateStart=DateUtils.formatDate(DateUtils.parseDate(dateStart), "yyyyMMdd");
    		dateEnd=DateUtils.formatDate(DateUtils.parseDate(dateEnd), "yyyyMMdd");
    		
		} catch (Exception e) {
			logger.error("/tc/statistics/exportExcel转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
		param.put("begin", 0);
	    param.put("end", 10000000);
		String[] titleArr = new String[] {};
		String columnName = "[{\"field\":\"NAME\"},{\"field\":\"HE_JI\"},"
				+ "{\"field\":\"BD_NUM\"},{\"field\":\"BD_COUNT\"},{\"field\":\"QS_NUM\"},"
				+ "{\"field\":\"QS_COUNT\"},{\"field\":\"DY_NUM\"},{\"field\":\"DY_COUNT\"},"
				+ "{\"field\":\"PT_NUM\"},{\"field\":\"PT_COUNT\"},{\"field\":\"KET_NUM\"},"

				+ "{\"field\":\"KET_COUNT\"},{\"field\":\"QYI_NUM\"},{\"field\":\"QYI_COUNT\"},"
				+ "{\"field\":\"WS_NUM\"},{\"field\":\"WS_COUNT\"},{\"field\":\"CHS_NUM\"},"
				+ "{\"field\":\"CHS_COUNT\"},{\"field\":\"CAS_NUM\"},{\"field\":\"CAS_COUNT\"},"

				+ "{\"field\":\"KOT_NUM\"},{\"field\":\"KOT_COUNT\"},{\"field\":\"QYU_NUM\"},"
				+ "{\"field\":\"QYU_COUNT\"},{\"field\":\"CB_NUM\"},{\"field\":\"CB_COUNT\"},"
				+ "{\"field\":\"LJ_NUM\"},{\"field\":\"LJ_COUNT\"},{\"field\":\"CHNJC_NUM\"},"
				+ "{\"field\":\"CHNJC_COUNT\"},{\"field\":\"CHWJC_NUM\"},{\"field\":\"CHWJC_COUNT\"}]";
    		titleArr = new String[] {"姓名","合计", "摆渡", "小计","清水", "小计","电源", "小计","平台", "小计",
    		        "客梯", "小计","牵引", "小计","污水", "小计","传送", "小计","残登", "小计","空调", 
    		        "小计","气源", "小计","除冰", "小计","垃圾车", "小计","场内机车组", "小计","场外机车组", "小计"};
    	
		try {
			String fileName =dateStart+"-"+dateEnd + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String,String>> dataList=(List<Map<String, String>>) tcStatisticsService.getDataList(param).get("rows");
			excel.setDataList(titleArr, JSON.parseArray(columnName),dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("特车员工工作量数据导出"+ e.getMessage());
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
