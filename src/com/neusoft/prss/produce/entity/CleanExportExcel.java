package com.neusoft.prss.produce.entity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

public class CleanExportExcel {

	
	private static Logger log = LoggerFactory.getLogger(CleanExportExcel.class);
			
	/**
	 * 工作薄对象
	 */
	private SXSSFWorkbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;
	
	/**
	 * 当前行号
	 */
	private int rownum;
	
	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	List<Object[]> annotationList = Lists.newArrayList();

	private String val;
	
	public CleanExportExcel(String title){
	    this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet("Export");
        this.styles = createStyles(wb);
        // Create title
//        if (StringUtils.isNotBlank(title)){
//            Row titleRow = sheet.createRow(rownum++);
//            titleRow.setHeightInPoints(30);
//            Cell titleCell = titleRow.createCell(0);
//            CellStyle cellStyle = styles.get("title");
//            cellStyle.setWrapText(true);
//            titleCell.setCellStyle(cellStyle);
//            titleCell.setCellValue(title);
////            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
////                    titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
//        }
	}
	
	/**
	 * 创建表格样式
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("微软雅黑");
		titleFont.setFontHeightInPoints((short) 14);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);
		
		return styles;
	}

	/**
	 * 添加一行
	 * @return 行对象
	 */
	public Row addRow(){
		return sheet.createRow(rownum++);
	}
	
	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, String val){
		Cell cell = row.createCell(column);
		CellStyle style = styles.get("data");
		try {
			if (val == null){
				cell.setCellValue("");
			} else {
				cell.setCellValue(val);
			} 
		} catch (Exception ex) {
			log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
			cell.setCellValue(val.toString());
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	public CleanExportExcel setDataList(JSONArray titleArray, List<Map<String,String>> dataList){
		for (int i=0;i<dataList.size();i++){
			int colunm = 0;
			Row row = this.addRow();
			
			StringBuilder sb = new StringBuilder();
			for(int j=0;j<titleArray.size();j++){
				String field = titleArray.getJSONObject(j).getString("field");
				val = dataList.get(i).get(field);
				this.addCell(row, colunm++, val);
				sb.append(val + ", ");
			}
			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
		}
		return this;
	}
	
	public CleanExportExcel setUpdate(JSONArray data){
	    JSONObject jc = new JSONObject();
        JSONObject sc = new JSONObject();
        if(data.size()>0)
	    jc = data.getJSONObject(0);
        if(data.size()>1)
	    sc = data.getJSONObject(1);
	    Row headerRow = sheet.createRow(rownum++);
        String[][] row1 = {{"日期","OPER_DATE"},{"驾驶员","NAME"},{"车号","VEHICLE_NUMBER"},{"车辆类型","TYPENAME"},{"随车人员","SC_PERSON"}};
        String[][] row2 = {{"接车",""},{"出勤时间","OPER_BOUND_DATE"},{"公里数","VALUE1"},{"小时数","VALUE2"},{"燃油","VALUE3"},{"加油","VALUE4"}};
        String[][] row3 = {{"车辆状态","JCXM_NAME"}};
        String[][] row4 = {{"收车",""},{"收车时间","OPER_UNBOUND_DATE"},{"公里数","VALUE1"},{"小时数","VALUE2"},{"燃油","VALUE3"},{"加油","VALUE4"}};
        int row =0;
        for(int i=0;i<row1.length;i++){
            String value1 = row1[i][0];
            String value2 = jc.getString(row1[i][1]);
            Cell cell = headerRow.createCell(row++);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(value1);
            Cell cell1 = headerRow.createCell(row++);
            cell1.setCellStyle(styles.get("data"));
            cell1.setCellValue(value2);
            if("日期".equals(value1)){
                // 合并单元格  
                   CellRangeAddress cra =new CellRangeAddress( rownum-1, rownum-1,row-1, row); // 起始行, 终止行, 起始列, 终止列  
                   sheet.addMergedRegion(cra);  
                   row++;
            }
            
        }
        
        row =0;
        headerRow = sheet.createRow(rownum++);
        for(int i=0;i<row2.length;i++){
            String value1 = row2[i][0];
            String value2 = jc.getString(row2[i][1]);
            if("接车".equals(value1)){
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(value1);
                CellRangeAddress cra =new CellRangeAddress( rownum-1, rownum,row-1, row-1); // 起始行, 终止行, 起始列, 终止列  
                sheet.addMergedRegion(cra);  
            } else {
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(value1);
                Cell cell1 = headerRow.createCell(row++);
                cell1.setCellStyle(styles.get("data"));
                cell1.setCellValue(value2);
            }
        }
        row =1;
        headerRow = sheet.createRow(rownum++);
        for(int i=0;i<row3.length;i++){
            String value1 = row3[i][0];
            String value2 = jc.getString(row3[i][1]);
            Cell cell = headerRow.createCell(row++);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(value1);
            Cell cell1 = headerRow.createCell(row);
            CellRangeAddress cra =new CellRangeAddress( rownum-1, rownum-1,row, row+8); // 起始行, 终止行, 起始列, 终止列  
            sheet.addMergedRegion(cra);  
            cell1.setCellStyle(styles.get("data"));
            cell1.setCellValue(value2);
        }
        
        row =0;
        headerRow = sheet.createRow(rownum++);
        for(int i=0;i<row4.length;i++){
            String value1 = row4[i][0];
            String value2 = sc.getString(row4[i][1]);
            if("收车".equals(value1)){
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(value1);
                CellRangeAddress cra =new CellRangeAddress( rownum-1, rownum,row-1, row-1); // 起始行, 终止行, 起始列, 终止列  
                sheet.addMergedRegion(cra);  
            } else {
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("header"));
                cell.setCellValue(value1);
                Cell cell1 = headerRow.createCell(row++);
                cell1.setCellStyle(styles.get("data"));
                cell1.setCellValue(value2);
            }
        }
        row =1;
        headerRow = sheet.createRow(rownum++);
        for(int i=0;i<row3.length;i++){
            String value1 = row3[i][0];
            String value2 = sc.getString(row3[i][1]);
            Cell cell = headerRow.createCell(row++);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(value1);
            Cell cell1 = headerRow.createCell(row);
            CellRangeAddress cra =new CellRangeAddress( rownum-1, rownum-1,row, row+8); // 起始行, 终止行, 起始列, 终止列  
            sheet.addMergedRegion(cra); 
            cell1.setCellStyle(styles.get("data"));
            cell1.setCellValue(value2);
            row++;
        }
	    return this;
	}
	
	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public CleanExportExcel write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public CleanExportExcel write(HttpServletResponse response) throws IOException{
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param fileName 输出文件名
	 */
	public CleanExportExcel writeFile(String name) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}
	
	/**
	 * 清理临时文件
	 */
	public CleanExportExcel dispose(){
		wb.dispose();
		return this;
	}

    public void setData(JSONArray data,int flag) {
        Row headerRow = sheet.createRow(rownum++);
        String[][] row1 = {{"序号",""},{"航班号","FLIGHT_NUMBER"},{"注册号","AIRCRAFT_NUMBER"},{"ETA","ETA"},
                {"机位","ACTSTAND_CODE"},{"收到时间","ACT_ARRANGE_TM"},{"到位时间","TIME1"},{"舱门是否有划痕","TIME1"},
                {"开始作业时间","TIME2"},{"结束时间","TIME3"}};
        String[][] row2 = {{"序号",""},{"航班号","FLIGHT_NUMBER"},{"注册号","AIRCRAFT_NUMBER"},{"ETA","ETA"},
                {"机位","ACTSTAND_CODE"},{"收到时间","ACT_ARRANGE_TM"},{"到位时间","TIME1"},{"舱门是否有划痕","划痕"},
                {"开始作业时间","TIME2"},{"结束时间","TIME3"},{"加水量","WATER"}};
        if(flag==19)
            row1 = row2;
        Cell tcell = headerRow.createCell(0);
        tcell.setCellStyle(styles.get("header"));
        tcell.setCellValue("工作记录");
        CellRangeAddress cra1 =new CellRangeAddress( rownum-1, rownum-1,0, row1.length-1); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra1); 

        headerRow = sheet.createRow(rownum++);
        int row =0;
        for(int i=0;i<row1.length;i++){
            String value1 = row1[i][0];
            Cell cell = headerRow.createCell(row++);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(value1);
            sheet.autoSizeColumn(1, true);
        }
        for(int j=0;j<data.size();j++){
            headerRow = sheet.createRow(rownum++);
            JSONObject jsonObject = data.getJSONObject(j);
            row =0;
            for(int i=0;i<row1.length;i++){
                String value = (j+1) +"";
                if(!"序号".equals(row1[i][0])){
                    value = jsonObject.getString(row1[i][1]);
                }
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("data"));
                cell.setCellValue(value);
            }
        }
    }


}
