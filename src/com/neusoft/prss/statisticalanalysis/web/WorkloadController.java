/**
 *application name:hetprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年8月23日 上午09:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.Role;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.WorkloadService;


/**
 * 
 *Discription:员工工作量统计
 *@param 
 *@return:
 *@author:l.ran
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/workload/statistics")
public class WorkloadController extends BaseController {
	
	@Autowired
	private WorkloadService workloadService;
	private Logger logger=Logger.getLogger(WorkloadController.class);
	
	
    @RequestMapping(value = "list" )
    public String list(Model model) throws ParseException {
    	String defaultStart=DateUtils.getDate("yyyyMMdd");
    	Date date = DateUtils.getCalculateDate(defaultStart, -1);
    	defaultStart = new SimpleDateFormat("yyyyMMdd").format(date);
    	JSONArray jobKinds = workloadService.getJobKinds();
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
    	model.addAttribute("jobKinds", jobKinds);
        return "prss/statisticalanalysis/workloadList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String dateStart,String dateEnd,String name,String ifJobKind,String jobKind,String searchText) {
    	
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	ifJobKind=StringEscapeUtils.unescapeHtml4(ifJobKind);
    	jobKind=StringEscapeUtils.unescapeHtml4(jobKind);
    	name=StringEscapeUtils.unescapeHtml4(name);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	if("1".equals(ifJobKind)) {
    	} else if("undefined".equals(jobKind)) {
    		jobKind = "";
    		List<Role> roles = UserUtils.getRoleList();
    		List<JobKind> jobKinds = UserUtils.getJobKindByRoles(roles);
    		for(JobKind jk:jobKinds) {
    			jobKind += jk.getKindCode() + "','";
    		}
    		if(jobKind.endsWith("','")) {
    			jobKind = jobKind.substring(0,jobKind.length()-3);
    		}
    	}
    	try {
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
    		name = java.net.URLDecoder.decode(name,"utf-8");
		} catch (Exception e) {
			logger.error("/workload/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("jobKind", jobKind);
	    param.put("name", name);
	    param.put("searchText", searchText.toUpperCase());
        return workloadService.getDataList(param);
    }

    @RequestMapping(value = "exportExcel")
    public void printList(HttpServletRequest request, HttpServletResponse response,
    		String dateStart,String dateEnd,String name,String ifJobKind,String jobKind,String searchText,String jobKindName) {
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	ifJobKind=StringEscapeUtils.unescapeHtml4(ifJobKind);
    	jobKind=StringEscapeUtils.unescapeHtml4(jobKind);
    	name=StringEscapeUtils.unescapeHtml4(name);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	if("1".equals(ifJobKind)) {
    	} else if("undefined".equals(ifJobKind) || StringUtils.isBlank(ifJobKind) || "null".equals(ifJobKind)) {
    		jobKind = "";
        	jobKindName = "";
        	List<Role> roles = UserUtils.getRoleList();
    		List<JobKind> jobKinds = UserUtils.getJobKindByRoles(roles);
    		for(JobKind jk:jobKinds) {
    			jobKind += jk.getKindCode() + "','";
    			jobKindName += jk.getKindName() + ",";
    		}
    		if(jobKindName.endsWith(",")) {
    			jobKindName = jobKindName.substring(0,jobKindName.length()-1);
    		}
    		if(jobKind.endsWith("','")) {
    			jobKind = jobKind.substring(0,jobKind.length()-3);
    		}
    	} 
    	try {
    		name = java.net.URLDecoder.decode(name,"utf-8");
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
    		
		} catch (Exception e) {
			logger.error("/workload/statistics/exportExcel失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
	    param.put("jobKind", jobKind);
	    param.put("name", name);
	    param.put("jobKindName", jobKindName);
		param.put("searchText", searchText.toUpperCase());
		param.put("begin", 0);
	    param.put("end", 10000000);
		String[] titleArr = new String[] {};
		String columnName = "[{\"field\":\"FLIGHT_DATE\"},{\"field\":\"KINDNAME\"},"
				+ "{\"field\":\"OPERATOR_NAME\"},{\"field\":\"TYPENAME\"},"
				+ "{\"field\":\"BZ_TIME_NUM\"},{\"field\":\"BZ_TIME\"},"
				+ "{\"field\":\"FLIGHT_NUMBER\"},"
				+ "{\"field\":\"IN_OUT_FLAG\"},{\"field\":\"ACTSTAND_KIND\"},"
				+ "{\"field\":\"BZ_TIME_COUNT\"},{\"field\":\"BZ_TIME_TOTAL\"}]";
    		titleArr = new String[] {"日期","岗位","姓名","作业类型","保障次数","保障时长", 
    				"航班详情"
    				,"航班性质","机位"
    				,"保障总次数", "保障总时长"};
    	
		try {
			String fileName =dateStart+"-"+dateEnd +"员工工作量统计"+ ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			// 设置表格数据内容
			List<Map<String,String>> dataList=(List<Map<String, String>>) workloadService.getDataList(param).get("rows");
			filterData(dataList);
			excel.setWorkloadData(titleArr, JSON.parseArray(columnName),dataList,param);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("员工工作量导出"+ e.getMessage());
		}
	}
    
    private void filterData(List<Map<String,String>> dataList) {
    	String fieldName = "OPERATOR_NAME";
		String[] fieldNameArr = {"FLIGHT_DATE","KINDNAME","BZ_TIME_COUNT","BZ_TIME_TOTAL"};
		String secondFieldName = "TYPENAME";
		String[] secondFieldNameArr = {"BZ_TIME_NUM"};
		if (dataList.size() > 0) {
			int num = 0;
			String temp = dataList.get(0).get(fieldName);
			String flightDateTemp = dataList.get(0).get("FLIGHT_DATE");
			for (int i = 0; i < dataList.size(); i++) {
				String flightDateValue = dataList.get(i).get("FLIGHT_DATE");
				String value = dataList.get(i).get(fieldName);
				if(temp.equals(value) && flightDateTemp.equals(flightDateValue)) {
					if(i==0)
						continue;
					dataList.get(i).put(fieldName, "");
					for(String v:fieldNameArr) {
						dataList.get(i).put(v, "");
					}
					num++;
					continue;
				}else {
					String secondTemp = "";
					for(int j=i-num - 1;j<i;j++) {
						String secondValue = (String) dataList.get(j).get(secondFieldName);
						if(secondTemp.equals(secondValue)) {
							dataList.get(j).put(secondFieldName, "");
							for(String s:secondFieldNameArr) {
								dataList.get(j).put(s, "");
							}
							secondTemp = secondValue;
							continue;
						} 
						secondTemp = secondValue;
					}
					temp = value;
					flightDateTemp = flightDateValue;
					num =0;
				}
			}
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
