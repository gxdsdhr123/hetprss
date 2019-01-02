package com.neusoft.prss.statisticalanalysis.web;

import java.io.BufferedOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.prss.statisticalanalysis.service.InOutBillingStatisticsService;

@Controller
@RequestMapping(value = "${adminPath}/inOutBillingStatistics")
public class InOutBillingStatisticsController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private InOutBillingStatisticsService inOutBillingStatisticsService;
	
	@RequestMapping(value = "list" )
    public String list(Model model) {
        return "prss/statisticalanalysis/inOutBillingStatistics";
    }
	
	@RequestMapping(value = "getData")
	@ResponseBody
	public JSONObject getDataList(Integer pageNumber, Integer pageSize,
			String startDate, String endDate, String inOut){
		JSONObject result = new JSONObject();
		if(startDate == null || startDate == "" || endDate == null || endDate == "") {
			result.put("total", "0");
			result.put("rows", new JSONArray());
			return result;
		}else {
			String total = inOutBillingStatisticsService.getDataListCount(startDate,endDate,inOut);
			JSONArray rows = inOutBillingStatisticsService.getDataList(pageNumber, pageSize, startDate, endDate, inOut);
			result.put("total", total==null?"0":total);
			result.put("rows", rows==null?new JSONArray():rows);
			return result;
		}
	}
	
	@RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,
    		String startDate, String endDate, String inOut) {
		try {
			//文件名
			String fileName = "";
			if("A".equals(inOut)) {
				fileName = "DARR-"+ DateUtils.getDate("yyyy-MM-dd-HHmmss") + "-HET.txt";
			}else if("D".equals(inOut)) {
				fileName = "DDEP-"+ DateUtils.getDate("yyyy-MM-dd-HHmmss") + "-HET.txt";
			}
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
            //获取数据
            JSONArray dataList = inOutBillingStatisticsService.getAllDataList(startDate, endDate, inOut);
            //写入文件
            StringBuffer write = new StringBuffer();         
			String enter = "\r\n";                 
			ServletOutputStream outSTr = response.getOutputStream();  // 建立         
			BufferedOutputStream buff = new BufferedOutputStream(outSTr);  
			for (int i=0;i<dataList.size();i++){
				JSONObject data = dataList.getJSONObject(i);
				write.append(padding(data.getString("DATA_ID"),20));
				write.append(padding(data.getString("STAT_DAY") + " 000000" + (data.getString("FLIGHT_NUMBER")==null?"":data.getString("FLIGHT_NUMBER")),25));
				write.append(padding(data.getString("IN_OUT_FLAG")+(data.getString("AIRCRAFT_NUMBER")==null?"":data.getString("AIRCRAFT_NUMBER")),11));
				write.append(padding(data.getString("ACTTYPE_CODE"),16));
				write.append(padding(data.getString("PROPERTY_CODE"),4));
				write.append(padding(data.getString("FLT_ATTR_CODE"),4));
				write.append(padding(data.getString("ALN_2CODE"),2)+" ");
				if("A".equals(inOut)) {
					write.append(padding(data.getString("STA"),13)+"00");
					write.append(padding(data.getString("ETA"),13)+"00");
					write.append(padding(data.getString("ATA"),13)+"00");
				}else if("D".equals(inOut)) {
					write.append(padding(data.getString("STD"),13)+"00");
					write.append(padding(data.getString("ETD"),13)+"00");
					write.append(padding(data.getString("ATD"),13)+"00");
				}
				write.append(padding(data.getString("ACTSTAND_CODE"),6));
				write.append(padding(data.getString("DEPART_APT3CODE"),4));
				write.append(padding(data.getString("ARRIVAL_APT3CODE"),4));
				write.append(padding(data.getString("DELAY_CODE"),4));
				write.append(padding(data.getString("GATE"),4));
				write.append(padding(data.getString("RUNWAY"),4));
				write.append(padding(data.getString("LIGHT"),1));
				write.append(padding(data.getString("GUIDE"),1));
				write.append(padding(data.getString("STATE"),4));
				if("A".equals(inOut)) {
					write.append("        ");
				}else if("D".equals(inOut)) {
					write.append("    ");
				}
				write.append(padding(data.getString("STOP_OVER1"),4));
				write.append(padding(data.getString("STOP_OVER2"),64)+"T1  ");
				write.append(enter);
			}
			buff.write(write.toString().getBytes("UTF-8"));         
			buff.flush();         
			buff.close();    
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("进出港计费统计导出错误：",e);
		}
	}
	
	private String padding(String str,int len) {
		if(str == null) {
			str = "";
		}
		int count = len-str.length();
		for(int i=0;i<count;i++) {
			str += " ";
		}
		return str.substring(0, len);
	}
}
