package com.neusoft.prss.mobile.web;

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

import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.mobile.service.MobileRecordService;

@Controller
@RequestMapping(value = "${adminPath}/mobileRecord")
public class MobileRecordController extends BaseController {

	@Resource
    private MobileRecordService mobileRecordService;
	
	@RequestMapping(value = "showPage")
    public String ShowPage() {
        return "prss/mobile/mobileRecord";
    }
	
	@RequestMapping(value = "searchBuMen" )
	@ResponseBody
    public List<HashMap<String, Object>> SearchBuMen() throws Exception {
		
		List<HashMap<String, Object>> buMenList = mobileRecordService.SearchBuMen();

        return buMenList;
    }
	
	@RequestMapping(value = "showTable" )
	@ResponseBody
    public Map<String, Object> ShowTable(
    		@RequestParam(value="buMen",required=false,defaultValue= "null") String buMen,
    		@RequestParam(value="zhuangTai",required=false,defaultValue= "null") String zhuangTai,
    		@RequestParam(value="pageSize",required=false,defaultValue= "10") int pageSize,
    		@RequestParam(value="pageNumber",required=false,defaultValue="1") int pageNumber) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("buMen", URLDecoder.decode(buMen, "utf-8"));
        param.put("zhuangTai", URLDecoder.decode(zhuangTai, "utf-8"));

		Map<String, Object> result = mobileRecordService.getTableData(param);

        return result;
    }
	
	@RequestMapping(value = "selectMobileLog" )
	@ResponseBody
    public List<HashMap<String, Object>> SelectMobileLog(String param,String pdaId) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", param);
		map.put("pdaId", pdaId);

		List<HashMap<String, Object>> mobileLogList = mobileRecordService.SelectMobileLog(map);

        return mobileLogList;
    }
	/**
     * 
     * Discription:手持机使用记录-打印功能.
     * 
     * @param request
     * @param response
     * @return:返回值意义
     * @author:yunwq
	 * @throws IOException 
     * @update:2018年02月03日 yunwq [变更描述]
     */
	@RequestMapping(value = "print")
	private void Print(HttpServletRequest request,HttpServletResponse response,
			String buMenDisplay,String zhuangTaiDisplay) throws Exception{
    	
		buMenDisplay = StringEscapeUtils.unescapeHtml4(buMenDisplay);
		zhuangTaiDisplay = StringEscapeUtils.unescapeHtml4(zhuangTaiDisplay);
    	
    	XSSFWorkbook wb = new XSSFWorkbook();
    	Sheet sheet = wb.createSheet();
    	XSSFCellStyle cellStyle = wb.createCellStyle(); 
    	cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中    
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("buMen", buMenDisplay);
    	map.put("zhuangTai", zhuangTaiDisplay);
    	   	
    	List<HashMap<String,Object>> dataList = mobileRecordService.getPrintList(map);
    	System.out.println(dataList);
    	Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("部门");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(1);
		cell.setCellValue("设备编号");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(2);
		cell.setCellValue("IMEI");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(3);
		cell.setCellValue("状态");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(4);
		cell.setCellValue("领用人");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(5);
		cell.setCellValue("领用时间");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(6);
		cell.setCellValue("归还人");
    	cell.setCellStyle(cellStyle);
    	cell = row.createCell(7);
		cell.setCellValue("归还时间");
    	cell.setCellStyle(cellStyle);
    	for(int i=0;i<dataList.size();i++){
    		row = sheet.createRow(i+1);
    		cell = row.createCell(0);
    		cell.setCellValue(dataList.get(i).get("NAME").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(1);
    		cell.setCellValue(dataList.get(i).get("PDA_NO").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(2);
    		cell.setCellValue(dataList.get(i).get("IMEI").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(3);
    		cell.setCellValue(dataList.get(i).get("PDA_STATUS").toString());
        	cell.setCellStyle(cellStyle);
        	cell = row.createCell(4);
        	if(dataList.get(i).get("USE_USER") != null){
        		cell.setCellValue(dataList.get(i).get("USE_USER").toString());
        		cell.setCellStyle(cellStyle);
        	}
        	cell = row.createCell(5);
        	if(dataList.get(i).get("USE_TIME") != null){
	    		cell.setCellValue(dataList.get(i).get("USE_TIME").toString());
	        	cell.setCellStyle(cellStyle);
        	}
        	cell = row.createCell(6);
        	if(dataList.get(i).get("BACK_USER") != null){
	    		cell.setCellValue(dataList.get(i).get("BACK_USER").toString());
	        	cell.setCellStyle(cellStyle);
        	}
        	cell = row.createCell(7);
        	if(dataList.get(i).get("BACK_TIME") != null){
	    		cell.setCellValue(dataList.get(i).get("BACK_TIME").toString());
	        	cell.setCellStyle(cellStyle);
        	}
    	}
    	
    	
    	
    	String fileName = "手持设备记录.xlsx";
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
