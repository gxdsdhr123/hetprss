/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年7月17日 上午9:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.statisticalanalysis.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.FltNomalFxStatsService;
/**
 * 
 *Discription:始发航班/机场放行正常率统计表共用
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticalanalysis/fltNomalFx")
public class FltNomalFxStatsController extends BaseController {
	@Autowired
	private FltNomalFxStatsService fltNomalFxStatsService;
	
    @RequestMapping(value = "list/{param}" )
    public String getList(Model model,@PathVariable("param") String param) {
//    	默认查询2天前的数据（因为是历史表）
    	String defaultDate=DateUtils.formatDate(DateUtils.getCalculateDate(-2),"yyyyMMdd");
    	model.addAttribute("defaultDate", defaultDate);
    	model.addAttribute("type", param);
    	if("jc".equals(param)){
    		model.addAttribute("title", "机场放行正常率统计表");
    	}
    	if("sf".equals(param)){
    		model.addAttribute("title", "始发航班放行正常率统计表");
    	}
        return "prss/statisticalanalysis/fltNomalFxList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public  Map<String, Object> getDataList(String startDate,String endDate,String type) {
	    List<Map<String,Object>> dataList=getData(startDate, endDate,type, false);
	    Map<String, Object> result=null;
	    if(dataList.size()!=0){
	    	result=dataList.get(0);
	    }
	    //统计数量的Sql有可能返回空对象  所以此处额外判断一下null
	    if(result==null){
		 result=new HashMap<String,Object>();
		 result.put("none", "没有数据");
	    }
        return result;
    }
    
    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate,String type) {
    	
    	String title="";
    	if("jc".equals(type)){
    		title="机场放行正常率统计表";
    	}
    	if("sf".equals(type)){
    		title="始发航班放行正常率统计表";
    	}	
    	startDate=StringEscapeUtils.unescapeHtml4(startDate);
    	endDate=StringEscapeUtils.unescapeHtml4(endDate);
    	String columnName = "[{\"field\":\"HE_JI\"},{\"field\":\"PLAN_FLT_NUM\"},{\"field\":\"NORMAL_FLT_NUM\"},"
    	       +"{\"field\":\"UNNORMAL_FLT_NUM0\"}{\"field\":\"NORMAL_FX\"},{\"field\":\"UNNORMAL_FLT_NUM1\"},{\"field\":\"UNNORMAL_FLT_NUM2\"},"
    	       + "{\"field\":\"UNNORMAL_FLT_NUM3\"},{\"field\":\"UNNORMAL_FLT_NUM4\"},{\"field\":\"UNNORMAL_FLT_NUM5\"},"
    		   + "{\"field\":\"UNNORMAL_FLT_NUM6\"},{\"field\":\"UNNORMAL_FLT_NUM7\"},{\"field\":\"UNNORMAL_FLT_NUM8\"},"
    		   + "{\"field\":\"UNNORMAL_FLT_NUM9\"},{\"field\":\"UNNORMAL_FLT_NUM10\"},{\"field\":\"UNNORMAL_FLT_NUM11\"},"
    		   + "{\"field\":\"UNNORMAL_FLT_NUM12\"},{\"field\":\"YOY\"},{\"field\":\"MOM\"}]";
    	String[] titleArr = {"", "计划", "正常", "不正常", "正常率","天气","航空公司", "流量", "军事活动", 
    		 "空管", "机场","联检","油料", "离港系统", "旅客", "公共安全", "航班时刻安排" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			String fileName = title + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置正常率统计表的表格布局和前两行title
			excel.setFltNomalTitles(title);
			excel.setDataList(titleArr, columnArr, getData(startDate, endDate, type, true));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error(title + e.getMessage());
		}
	}
    private List<Map<String,Object>> getData(String startDate,String endDate,String type,boolean printFlag){
    	List<Map<String,Object>> dataList=fltNomalFxStatsService.getDataList(startDate,endDate,type);
    	 Map<String,Object> data=new HashMap<String,Object>();
 	    if(dataList.size()!=0){
 	    	data=dataList.get(0);
 	    	if(data!=null){
 	    		DecimalFormat df=new DecimalFormat("0.00");
	    		int normalFltNum=((BigDecimal)data.get("NORMAL_FLT_NUM")).intValue();
		    	int planFltNum=((BigDecimal)data.get("PLAN_FLT_NUM")).intValue();
		    	float nomalFx=0f;
		    	//计算正常率
		    	if(planFltNum!=0){
		    		nomalFx=Float.parseFloat(df.format((float)normalFltNum/planFltNum));
		    	}
		    	float lastYearNomalFx=0f;
		    	float lastCycleNomalFx=0f;
		    	float yoy=0f;
		    	float mom=0f;
		    	try {
		    		//得到去年的正常率
					lastYearNomalFx=getLastYearNomalFx(startDate, endDate,type);
					//得到上一周期的正常率
					lastCycleNomalFx=getLastCycleNomalFx(startDate, endDate,type);
					//需求中对于没有正常率的数据   默认显示同比/环比为0
					if(lastYearNomalFx!=0f){
						//计算同比
						yoy=(nomalFx-lastYearNomalFx)/lastYearNomalFx;
					}
					if(lastCycleNomalFx!=0f){
						//计算环比
						mom=(nomalFx-lastCycleNomalFx)/lastCycleNomalFx;
					}
					
				} catch (ParseException e) {
					logger.error("计算同比/环比发生错误"+e.getMessage());
					e.printStackTrace();
				}
		    	data.put("UNNORMAL_FLT_NUM0", planFltNum-normalFltNum);
		    	data.put("NORMAL_FX",nomalFx);
		    	data.put("YOY",df.format((float)yoy));
		    	data.put("MOM",df.format((float)mom));
 	 	    	if(printFlag){
 		    		data.put("HE_JI", "合计");
 		    	}
 	    	}
 	    }
 	    return dataList;
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

    private float getLastYearNomalFx(String startDate,String endDate,String type) throws ParseException{
    	//获取去年的这一天
		startDate=DateUtils.formatDate(DateUtils.getCalculateYear(startDate,-1),"yyyyMMdd");
		endDate=DateUtils.formatDate(DateUtils.getCalculateYear(endDate,-1),"yyyyMMdd");
		List<Map<String,Object>> lastYearData=fltNomalFxStatsService.getDataList(startDate, endDate,type);
		float lastYearNomalFx=0f;
    	if(lastYearData.size()!=0){
    		Map<String,Object> data=lastYearData.get(0);
    		if(data!=null){
    			//计算正常率
    			int normalFltNum=((BigDecimal)data.get("NORMAL_FLT_NUM")).intValue();
 	 	    	int planFltNum=((BigDecimal)data.get("PLAN_FLT_NUM")).intValue();
 	 	    	DecimalFormat df=new DecimalFormat("0.00");
 	 	    	if(planFltNum!=0){
 	 	    		lastYearNomalFx=Float.parseFloat(df.format((float)normalFltNum/planFltNum));
 	 	    	}
 	 	    	
    		}
    	}
    	return lastYearNomalFx;
    }
    
    private float getLastCycleNomalFx(String startDate,String endDate,String type) throws ParseException{
    	//获取相差的天数
    	int days=(int) DateUtils.getDistanceOfTwoDate(startDate, endDate, "yyyyMMdd")+1;
		startDate=DateUtils.formatDate(DateUtils.getCalculateCycle(startDate, -days),"yyyyMMdd");
		endDate=DateUtils.formatDate(DateUtils.getCalculateCycle(endDate, -days),"yyyyMMdd");
		List<Map<String,Object>> lastCycleData=fltNomalFxStatsService.getDataList(startDate, endDate,type);
		float lastCycleNomalFx=0f;
    	if(lastCycleData.size()!=0){
    		Map<String,Object> data=lastCycleData.get(0);
    		if(data!=null){
    			//计算正常率
    			int normalFltNum=((BigDecimal)data.get("NORMAL_FLT_NUM")).intValue();
 	 	    	int planFltNum=((BigDecimal)data.get("PLAN_FLT_NUM")).intValue();
 	 	    	DecimalFormat df=new DecimalFormat("0.00");
 	 	    	if(planFltNum!=0){
 	 	    		lastCycleNomalFx=Float.parseFloat(df.format((float)normalFltNum/planFltNum));
 	 	    	}
 	 	    	
    		}
    	}
    	return lastCycleNomalFx;
    }
    
}
