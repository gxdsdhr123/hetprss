package com.neusoft.prss.statisticalanalysis.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.statisticalanalysis.service.QyCarEmployeeWorkService;

@Controller
@RequestMapping(value = "${adminPath}/qyCarEmployeeWork")
public class QyCarEmployeeWorkController extends BaseController {

	@Resource
    private QyCarEmployeeWorkService qyCarEmployeeWorkService;
	
	@RequestMapping(value = "showPage" )
    public String ShowPage() {
        return "prss/statisticalanalysis/qyCarEmployeeWork";
    }
	
	@RequestMapping(value = "showTable" )
	@ResponseBody
    public Map<String, Object> ShowTable(
    		@RequestParam(value="banZu",required=false,defaultValue= "null") String banZu,
    		@RequestParam(value="zuYuan",required=false,defaultValue= "null") String zuYuan,
    		@RequestParam(value="beginTime",required=false,defaultValue= "") String beginTime,
    		@RequestParam(value="endTime",required=false,defaultValue= "") String endTime,
    		@RequestParam(value="pageSize",required=false,defaultValue= "10") int pageSize,
    		@RequestParam(value="pageNumber",required=false,defaultValue="1") int pageNumber) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("banZu", URLDecoder.decode(banZu, "utf-8"));
        param.put("zuYuan", URLDecoder.decode(zuYuan, "utf-8"));
        param.put("beginTime", beginTime);
        param.put("endTime", endTime);

		Map<String, Object> result = qyCarEmployeeWorkService.getData(param);

        return result;
    }
	
	@RequestMapping(value = "searchBanZu" )
	@ResponseBody
    public JSONArray SearchBanZu(String name) throws Exception {
		String banZu = "";
		if(!"undefined".equals(name)){
			banZu = URLDecoder.decode(name , "utf-8");
		}
		List<HashMap<String,Object>> list = qyCarEmployeeWorkService.getBanZu(banZu);
        return JSONArray.parseArray(JSONArray.toJSONString(list));
    }
	
	@RequestMapping(value = "searchZuYuan" )
	@ResponseBody
    public JSONArray SearchZuYuan(@RequestParam(value="banZu",required=false,defaultValue="null") String banZu,
    		@RequestParam(value="name",required=false,defaultValue="null") String name) throws Exception {

		String zuYuan = "";
		if(!"undefined".equals(name) && !"null".equals(name)){
			zuYuan = URLDecoder.decode(name , "utf-8");
		}

		Map<String,Object> param = new HashMap<String,Object>();
		param.put("name", zuYuan);
		param.put("banZu", URLDecoder.decode(banZu , "utf-8"));

		List<HashMap<String,Object>> list = qyCarEmployeeWorkService.getZuYuan(param);
		
        return JSONArray.parseArray(JSONArray.toJSONString(list));
    }
	/**
     * 
     * Discription:牵引车员工工作量统计(按数量统计)-打印功能.
     * 
     * @param request
     * @param response
     * @return:返回值意义
     * @author:yunwq
	 * @throws IOException 
     * @update:2018年01月29日 yunwq [变更描述]
     */
	@RequestMapping(value = "print")
	private void Print(HttpServletRequest request,HttpServletResponse response,
			String beginTimeDisplay,String endTimeDisplay,String banZuDisplay,String zuYuanDisplay) throws Exception{
    	beginTimeDisplay = StringEscapeUtils.unescapeHtml4(beginTimeDisplay);
    	endTimeDisplay = StringEscapeUtils.unescapeHtml4(endTimeDisplay);
    	banZuDisplay = StringEscapeUtils.unescapeHtml4(banZuDisplay);
    	zuYuanDisplay = StringEscapeUtils.unescapeHtml4(zuYuanDisplay);
    	
    	XSSFWorkbook wb = new XSSFWorkbook();
    	Sheet sheet = wb.createSheet();
    	XSSFCellStyle cellStyle = wb.createCellStyle(); 
    	cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中    
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("beginTime", beginTimeDisplay);
    	map.put("endTime", endTimeDisplay);
    	map.put("banZu", banZuDisplay);
    	map.put("zuYuan", zuYuanDisplay);
    	
    	List<HashMap<String,Object>> dataList = qyCarEmployeeWorkService.getPrintList(map);

    	Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("班组");
    	cell.setCellStyle(cellStyle);
    	 cell = row.createCell(1);
		cell.setCellValue("姓名");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
		cell.setCellValue("推飞机架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(3);
		cell.setCellValue("拖飞机架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
		cell.setCellValue("国际总架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(5);
		cell.setCellValue("国内总架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
		cell.setCellValue("T3国际架次");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(7);
		cell.setCellValue("总计");
    	cell.setCellStyle(cellStyle);
    	for(int i=0;i<dataList.size();i++){
    		row = sheet.createRow(i+1);
    		cell = row.createCell(0);
    		cell.setCellValue(dataList.get(i).get("TEAM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(1);
    		cell.setCellValue(dataList.get(i).get("NAME").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(2);
    		cell.setCellValue(dataList.get(i).get("TUI_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(3);
    		cell.setCellValue(dataList.get(i).get("TUO_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(4);
    		cell.setCellValue(dataList.get(i).get("I_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(5);
    		cell.setCellValue(dataList.get(i).get("D_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(6);
    		cell.setCellValue(dataList.get(i).get("T3I_NUM").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(7);
    		cell.setCellValue(dataList.get(i).get("T3I_NUM").toString());
        	cell.setCellStyle(cellStyle);
    	}
    	
    	String fileName = "牵引车员工工作量统计(按数量统计).xlsx";
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
    	
    	wb.write(response.getOutputStream());
	}
}
