/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年7月19日 上午9:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import java.util.List;
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
import com.neusoft.prss.statisticalanalysis.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.FltNomalFxStatsService;
import com.neusoft.prss.statisticalanalysis.service.FltWeeklyStatsService;
/**
 * 
 *Discription:周报统计表
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticalanalysis/fltWeekly")
public class FltWeeklyStatsController extends BaseController {
	@Autowired
	private FltWeeklyStatsService fltWeeklyStatsService;
	
	@Autowired
	private FltNomalFxStatsService fltNomalFxStatsService;
	
    @RequestMapping(value = "list" )
    public String getList(Model model) {
//    	默认查询2天前的数据（因为是历史表）
    	String defaultDate=DateUtils.formatDate(DateUtils.getCalculateDate(-2),"yyyyMMdd");
    	model.addAttribute("defaultDate", defaultDate);
        return "prss/statisticalanalysis/fltWeeklyList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public  JSONObject getDataList(String startDate,String endDate) {
    	//周报上部分数据
    	JSONArray weeklyDataArr=fltWeeklyStatsService.getDataList(startDate, endDate);
	    JSONObject result=new JSONObject();
	    result.put("weeklyData", weeklyDataArr);
	    //周报下部分数据
	    List<Map<String,Object>> sfDataList=fltNomalFxStatsService.getDataList(startDate,endDate,"sf");
	    List<Map<String,Object>> jcDataList=fltNomalFxStatsService.getDataList(startDate,endDate,"jc");
	    result.put("sfData", sfDataList.get(0));
	    result.put("jcData", jcDataList.get(0));
        return result;
    }
    
    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate) {
    	startDate=StringEscapeUtils.unescapeHtml4(startDate);
    	endDate=StringEscapeUtils.unescapeHtml4(endDate);
    	String weeklyColumn = "[{\"field\":\"DAY_OF_WEEK\"},{\"field\":\"FLT_TYPE0\"},{\"field\":\"FLT_TYPE1\"},"
     	       +"{\"field\":\"FLT_TYPE2\"}{\"field\":\"FLT_TYPE3\"},{\"field\":\"FLT_TYPE4\"},{\"field\":\"FLT_TYPE5\"},"
     	       + "{\"field\":\"FLT_TYPE6\"},{\"field\":\"FLT_TYPE7\"},{\"field\":\"FLT_TYPE8\"},"
     		   + "{\"field\":\"FLT_TYPE9\"},{\"field\":\"FLT_TYPE10\"},{\"field\":\"FLT_TYPE11\"},"
     		   + "{\"field\":\"FLT_TYPE12\"},{\"field\":\"FLT_TYPE13\"}]";
    	
    	String columnName = "[{\"field\":\"JIA_CI\"},{\"field\":\"UNNORMAL_FLT_NUM1\"},{\"field\":\"UNNORMAL_FLT_NUM2\"},"
    	       + "{\"field\":\"UNNORMAL_FLT_NUM3\"},{\"field\":\"UNNORMAL_FLT_NUM4\"},{\"field\":\"UNNORMAL_FLT_NUM5\"},"
    		   + "{\"field\":\"UNNORMAL_FLT_NUM6\"},{\"field\":\"UNNORMAL_FLT_NUM7\"},{\"field\":\"UNNORMAL_FLT_NUM8\"},"
    		   + "{\"field\":\"UNNORMAL_FLT_NUM9\"},{\"field\":\"UNNORMAL_FLT_NUM10\"},{\"field\":\"UNNORMAL_FLT_NUM11\"},"
    		   + "{\"field\":\"UNNORMAL_FLT_NUM12\"},{\"field\":\"UNNORMAL_COUNT\"},{\"field\":\"EMPTY_CELL\"}]";
    	String[] sfTitleArr = {"航班延误原因","天气","航空公司", "流量", "军事活动", 
    		 "空管", "机场","联检","油料", "离港系统", "旅客", "公共安全", "航班时刻安排","合计","" };
    	String[] jcTitleArr = {"放行不正常原因","天气","航空公司", "流量", "军事活动", 
       		 "空管", "机场","联检","油料", "离港系统", "旅客", "公共安全", "航班时刻安排","合计","" };
		try {
			String fileName = "周报统计表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置周报统计表的表格布局和前三行title
			excel.setWeeklyTitles();
			// 设置表格数据内容
			excel.setDataList(JSON.parseArray(weeklyColumn),fltWeeklyStatsService.getDataList(startDate, endDate) );
			excel.setBetweenTitles();
			List<Map<String,Object>> sfDataList =fltNomalFxStatsService.getDataList(startDate, endDate, "sf");
			sfDataList.get(0).put("JIA_CI", "架次");
			sfDataList.get(0).put("EMPTY_CELL", "");
			excel.setDataList(sfTitleArr, JSON.parseArray(columnName),sfDataList );
			List<Map<String,Object>> jcDataList =fltNomalFxStatsService.getDataList(startDate, endDate, "jc");
			jcDataList.get(0).put("JIA_CI", "架次");
			jcDataList.get(0).put("EMPTY_CELL", "");
			excel.setDataList(jcTitleArr, JSON.parseArray(columnName),jcDataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("周报统计表" + e.getMessage());
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
