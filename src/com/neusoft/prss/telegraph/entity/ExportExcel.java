package com.neusoft.prss.telegraph.entity;

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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

public class ExportExcel {

	
	private static Logger log = LoggerFactory.getLogger(ExportExcel.class);
			
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

	public ExportExcel(String title){
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
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public ExportExcel write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportExcel write(HttpServletResponse response) throws IOException{
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param fileName 输出文件名
	 */
	public ExportExcel writeFile(String name) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}
	
	/**
	 * 清理临时文件
	 */
	public ExportExcel dispose(){
		wb.dispose();
		return this;
	}

    public void setData(List<Map<String,String>> list,JSONArray title,JSONObject object) {
        Row headerRow = sheet.createRow(rownum++);
        int row =0;
        for(int i=0;i<title.size();i++){
            String value1 = title.getJSONObject(i).getString("name");
            Cell cell = headerRow.createCell(row++);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(value1);
            sheet.autoSizeColumn(1, true);
        }
        for(int j=0;j<list.size();j++){
            headerRow = sheet.createRow(rownum++);
            Map<String,String> jsonObject = list.get(j);
            row =0;
            for(int i=0;i<title.size();i++){
                String key = title.getJSONObject(i).getString("code");
                String value = jsonObject.get(key);
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("data"));
                cell.setCellValue(value);
            }
        }
    }

}
