/**
 *application name:hetprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年8月18日 上午09:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import com.alibaba.fastjson.JSON;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.JWCBJCStatisticsService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 
 *Discription:机务除冰架次统计页面
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/jwcbjc/statistics")
public class JWCBJCStatisticsController extends BaseController {
	
	@Autowired
	private JWCBJCStatisticsService jwcbjcStatisticsService;
	private Logger logger=Logger.getLogger(JWCBJCStatisticsController.class);
	
	
    @RequestMapping(value = "list" )
    public String list(Model model) {
    	String defaultStart=DateUtils.getDate("yyyyMMdd");
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
        return "prss/statisticalanalysis/jwcbjcStatisticsList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String dateStart,String dateEnd,String searchText) {
    	
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8");
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
		} catch (Exception e) {
			logger.error("/jwcbjc/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("searchText", searchText);

		List list = jwcbjcStatisticsService.getDataList(param);
		param = new HashMap();
		param.put("total", jwcbjcStatisticsService.getDataListCount(param));
		param.put("rows", list);
        return param;
    }

	@RequestMapping(value="CBPricePage")
	public String getCBPricePage(Model model){

		model.addAttribute("airLines", jwcbjcStatisticsService.getAirLines());
		return "prss/statisticalanalysis/jwcbjcConfig";
	}

	@RequestMapping(value = "CBPriceList" )
	@ResponseBody
	public Map<String,Object> getCBPriceList(int pageSize,int pageNumber) {

		Map<String, Object> param=new HashMap<String, Object>();
		int begin=(pageNumber-1)*pageSize;
		int end=pageSize + begin;
		param.put("begin", begin);
		param.put("end", end);

		List list = jwcbjcStatisticsService.getCBPriceConf(param);
		param = new HashMap();
		param.put("total", jwcbjcStatisticsService.getCBPriceConfCount(param));
		param.put("rows", list);
		return param;
	}

	@RequestMapping(value = "changeCBPrice" )
	@ResponseBody
	public void changeCBPrice(String ID, String ALN_2CODE, Double CBY_FEE, Double FBY_FEE) {

		Map<String, Object> param=new HashMap<>();

		param.put("ID", ID);
		param.put("ALN_2CODE", ALN_2CODE);
		param.put("CBY_FEE", CBY_FEE);
		param.put("FBY_FEE", FBY_FEE);
		param.put("begin", 0);
		param.put("end", 10000000);
		List list = jwcbjcStatisticsService.getCBPriceConf(param);
		if(null!=list&&list.size()>0) {

			Map<String, Object> data = (Map<String, Object>) list.get(0);
			param.put("ID", data.get("ID"));
			jwcbjcStatisticsService.updateCBPriceConf(param);
		} else {

			jwcbjcStatisticsService.addCBPriceConf(param);
		}
	}

	@RequestMapping(value = "delCBPrice" )
	@ResponseBody
	public void delCBPrice(String ID) {

		Map<String, Object> param=new HashMap<>();
		param.put("ID", ID);
		jwcbjcStatisticsService.deleteCBPriceConf(param);
	}

    @RequestMapping(value = "airLinesList" )
    @ResponseBody
    public List<Map<String,Object>> getAirLines() {

		return jwcbjcStatisticsService.getAirLines();
    }

    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,
    		String dateStart,String dateEnd,String searchText) {
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		dateStart = java.net.URLDecoder.decode(dateStart,"utf-8");
    		dateEnd = java.net.URLDecoder.decode(dateEnd,"utf-8");
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
    		dateStart=DateUtils.formatDate(DateUtils.parseDate(dateStart), "yyyyMMdd");
    		dateEnd=DateUtils.formatDate(DateUtils.parseDate(dateEnd), "yyyyMMdd");
    		
		} catch (Exception e) {
			logger.error("/jwcbjc/statistics/exportExcel失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
		param.put("searchText", searchText);
		param.put("begin", 0);
	    param.put("end", 10000000);
		String columnName = "[{\"field\":\"\"},{\"field\":\"\"},{\"field\":\"\"},{\"field\":\"\"},{\"field\":\"\"},"
				+ "{\"field\":\"FLIGHT_DATE\"},{\"field\":\"FLIGHT_NUMBER\"},"
				+ "{\"field\":\"AIRCRAFT_NUMBER\"},{\"field\":\"ACTTYPE_CODE\"},"
				+ "{\"field\":\"ROUTE_NAME\"},{\"field\":\"ROUTE_TYPE\"},{\"field\":\"LEG_NAME\"},{\"field\":\"LEG_TYPE\"},"
				+ "{\"field\":\"AIRPORT_NAME\"},{\"field\":\"START_TM\"},{\"field\":\"END_TM\"},{\"field\":\"DEP_ARR_TM\"},"
				+ "{\"field\":\"IN_OUT_FLAG\"},{\"field\":\"PROPERTY_CODE\"},{\"field\":\"FLT_TYPE\"},"
				+ "{\"field\":\"FEE_NAME\"},{\"field\":\"NUMBERS\"},{\"field\":\"SINGAL_FEE\"},"
				+ "{\"field\":\"ALL_FEE\"},{\"field\":\"\"}]";
		String[] titleArr = new String[] {"费用ID", "开账方", "实际运行", "付款公司", "运输公司", "航班日期","航班号","机号","机型",
				"航线","航线性质", "航段", "航段性质", "机场", "开始时间", "结束时间", "起降时间", "进出属性", "任务性质", "航班状态",
				"费用名称", "数量", "单价", "金额", "备注"};
    	
		try {
			String fileName =dateStart+"-"+dateEnd +"机务除冰架次统计"+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List dataList=(jwcbjcStatisticsService.getDataList(param));
			excel.setDataList(titleArr, JSON.parseArray(columnName),dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("机务除冰架次数据导出"+ e.getMessage());
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
