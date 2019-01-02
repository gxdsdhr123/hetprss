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

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.entity.ExportTxt;
import com.neusoft.prss.statisticalanalysis.service.BridgeChargingService;


/**
 * 
 *Discription:廊桥统计
 *@param 
 *@return:
 *@author:l.ran
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/bridge/statistics")
public class BridgeChargingController extends BaseController {
	
	@Autowired
	private BridgeChargingService bridgeChargingService;
	private Logger logger=Logger.getLogger(BridgeChargingController.class);
	
	
    @RequestMapping(value = "list" )
    public String list(Model model) throws ParseException {
    	String defaultStart=DateUtils.getDate("yyyyMMdd");
    	Date date = DateUtils.getCalculateDate(defaultStart, -1);
    	defaultStart = new SimpleDateFormat("yyyyMMdd").format(date);
    	model.addAttribute("defaultStart", defaultStart);
    	model.addAttribute("defaultEnd", defaultStart);
        return "prss/statisticalanalysis/bridgeChargingList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String dateStart,String dateEnd,String ioType,String searchText) {
    	
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	ioType=StringEscapeUtils.unescapeHtml4(ioType);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
		} catch (Exception e) {
			logger.error("/bridge/statistics/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("dateStart", dateStart);
	    param.put("dateEnd", dateEnd);
	    param.put("ioType", ioType);
	    param.put("searchText", searchText.toUpperCase());
        return bridgeChargingService.getDataList(param);
    }

    @RequestMapping(value = "exportTxt")
    public void printList(HttpServletRequest request, HttpServletResponse response,
    		String dateStart,String dateEnd,String ioType,String searchText) {
    	dateStart = StringEscapeUtils.unescapeHtml4(dateStart);
    	dateEnd = StringEscapeUtils.unescapeHtml4(dateEnd);
    	ioType = StringEscapeUtils.unescapeHtml4(ioType);
    	searchText=StringEscapeUtils.unescapeHtml4(searchText);
    	try {
    		searchText = java.net.URLDecoder.decode(searchText,"utf-8");
    		
		} catch (Exception e) {
			logger.error("/bridge/statistics/exportTxt失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
		param.put("dateStart", dateStart);
		param.put("dateEnd", dateEnd);
		param.put("ioType", ioType);
		param.put("searchText", searchText.toUpperCase());
		param.put("begin", 0);
	    param.put("end", 10000000);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
			String fileName ="DTAX-"+ sdf.format(new Date())+ "-HET.txt";
			setHeader(request, response, fileName);
			ExportTxt txt = new ExportTxt();
			// 设置表格数据内容
			List<Map<String,String>> dataList=(List<Map<String, String>>) bridgeChargingService.getDataList(param).get("rows");
			txt.setBridgeChargingData(dataList,response);
		} catch (Exception e) {
			logger.error("廊桥计费统计导出"+ e.getMessage());
		}
	}
 
    private void setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		try {
			response.reset();
	        response.setContentType("text/plain; charset=utf-8");
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

//    @RequestMapping(value = "export")
//    public void export(HttpServletRequest request,HttpServletResponse response) {
//        Map<String,Object> param = new HashMap<String,Object>();
//        String loginName = UserUtils.getUser().getLoginName();
//        String userId = UserUtils.getUser().getId();
//        String mflightdate = request.getParameter("mflightdate");
//        String flightnumber = request.getParameter("flightnumber");
//        String isHis = request.getParameter("isHis");
//        String id = request.getParameter("id");
//        param.put("loginName", loginName);
//        param.put("mflightdate", mflightdate);
//        param.put("flightnumber", flightnumber);
//        param.put("userId", userId);
//        param.put("ids", id);
//        BufferedOutputStream buff = null;  
//        ServletOutputStream outSTr = null; 
//        try {
//            String fileName = "报文" + DateUtils.getDate("yyyyMMddHHmmss") + ".txt";
//            response.reset();
//            //导出txt文件  
//            response.setContentType("text/plain");     
//            String agent = (String) request.getHeader("USER-AGENT");
//            String downloadFileName = "";
//            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
//                downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
//            } else {
//                downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
//            }
//            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
//
//            
//             
//        } catch (Exception e) {
//            logger.error("报文导出失败" + e.getMessage());
//        }finally {         
//            try {         
//                buff.close();         
//                outSTr.close();         
//            } catch (Exception e) {         
//                e.printStackTrace();         
//           }         
//       }  
//    }
   
}
