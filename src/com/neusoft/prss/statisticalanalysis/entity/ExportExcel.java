package com.neusoft.prss.statisticalanalysis.entity;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.EscherGraphics;
import org.apache.poi.hssf.usermodel.EscherGraphics2d;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFShapeGroup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.neusoft.framework.common.utils.StringUtils;

public class ExportExcel {

	
	private static Logger log = LoggerFactory.getLogger(ExportExcel.class);
			
	/**
	 * 工作薄对象
	 */
	private HSSFWorkbook wb;
	
	/**
	 * 工作表对象
	 */
	private HSSFSheet sheet;
	
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
	
	public ExportExcel(String title){
	    this.wb = new HSSFWorkbook();
        this.sheet = wb.createSheet("Export");
        this.styles = createStyles(wb);
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
		style.setAlignment(CellStyle.ALIGN_CENTER);
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
	 * 设置机场放行/实发航班正常率的表格布局和title
	 */
	public ExportExcel setFltNomalTitles(String title){
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,18));
		sheet.addMergedRegion(new CellRangeAddress(1,1,1,4));
		sheet.addMergedRegion(new CellRangeAddress(1,1,5,16));
		sheet.addMergedRegion(new CellRangeAddress(1,2,17,17));
		sheet.addMergedRegion(new CellRangeAddress(1,2,18,18));
		Row row = this.addRow();
		CellStyle style = styles.get("title");
		Cell cell=row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(title);
		CellStyle style2 = wb.createCellStyle();
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Row row2 = this.addRow();
		Cell cell1Row2=row2.createCell(1);
		cell1Row2.setCellStyle(style2);
		Cell cell5Row2=row2.createCell(5);
		cell5Row2.setCellStyle(style2);
		Cell cell17Row2=row2.createCell(17);
		cell17Row2.setCellStyle(style2);
		Cell cell18Row2=row2.createCell(18);
		cell18Row2.setCellStyle(style2);
		row2.createCell(0).setCellValue("");
		cell1Row2.setCellValue("正常率统计");
		cell5Row2.setCellValue("不正常原因");
		cell17Row2.setCellValue("同比");
		cell18Row2.setCellValue("环比");
		return this;
	}
	/**
	 * 设置周报统计表的表格布局和title
	 */
	public ExportExcel setWeeklyTitles(){
		//大标题布局
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,14));
		//第一行布局
		sheet.addMergedRegion(new CellRangeAddress(1,3,0,0));
		sheet.addMergedRegion(new CellRangeAddress(1,1,1,5));
		sheet.addMergedRegion(new CellRangeAddress(1,1,6,13));
		sheet.addMergedRegion(new CellRangeAddress(1,3,14,14));
		//第二行布局
		sheet.addMergedRegion(new CellRangeAddress(2,2,1,2));
		sheet.addMergedRegion(new CellRangeAddress(2,2,3,4));
		for(int i=5;i<14;i++){
			sheet.addMergedRegion(new CellRangeAddress(2,3,i,i));
		}
		//第一行标题
		Row row = this.addRow();
		CellStyle style = styles.get("title");
		Cell cell=row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue("周报统计表");
		//第二行标题
		CellStyle style2 = styles.get("data");
//		style2.setAlignment(CellStyle.ALIGN_CENTER);
//		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Row row2 = this.addRow();
		
		//设置单元格样式
		for(int i=0;i<15;i++){
			row2.createCell(i).setCellStyle(style2);
		}
		
		int x1 = 86, y1 = 77;
		int x2 = 144, y2 = 48;
		int[] xys = { x1, y1, x2, y2 };
		drawLine(row, 1, 0, 144, 77, xys);
		
		Cell cell1Row2=row2.createCell(1);
		cell1Row2.setCellStyle(style2);
		Cell cell6Row2=row2.createCell(6);
		cell6Row2.setCellStyle(style2);
		Cell cell14Row2=row2.createCell(14);
		cell14Row2.setCellStyle(style2);
		
		style2.setWrapText(true);//先设置为自动换行   
		Cell cell0Row2=row2.createCell(0);
		cell0Row2.setCellStyle(style2);                          
		cell0Row2.setCellValue("                     类别"+"\n\n"+"日期        架次");
		cell1Row2.setCellValue("运输飞行");
		cell6Row2.setCellValue("通用飞行");
		cell14Row2.setCellValue("取消");
		//第三行标题
		Row row3 = this.addRow();
		
		//设置单元格样式
		for(int i=0;i<15;i++){
			row3.createCell(i).setCellStyle(style2);
		}
		Cell cell1Row3=row3.createCell(1);
		cell1Row3.setCellStyle(style2);
		cell1Row3.setCellValue("国内");
		Cell cell3Row3=row3.createCell(3);
		cell3Row3.setCellStyle(style2);
		cell3Row3.setCellValue("国际");
		String[] titleArr = {"备降","公务","航拍", "专机", "校飞", 
	       		 "调机", "训练","呼伦通航","其他"};
		int i=5;
		for(String title:titleArr){
			Cell titleCell=row3.createCell(i);
			titleCell.setCellStyle(style2);
			titleCell.setCellValue(title);
			i++;
		}
		//第四行标题
		Row row4 = this.addRow();
		for(int j=0;j<15;j++){
			row4.createCell(j).setCellStyle(style2);
		}
		Cell cell1Row4=row4.createCell(1);
		cell1Row4.setCellStyle(style2);
		cell1Row4.setCellValue("进港");
		Cell cell2Row4=row4.createCell(2);
		cell2Row4.setCellStyle(style2);
		cell2Row4.setCellValue("出港");
		Cell cell3Row4=row4.createCell(3);
		cell3Row4.setCellStyle(style2);
		cell3Row4.setCellValue("进港");
		Cell cell4Row4=row4.createCell(4);
		cell4Row4.setCellStyle(style2);
		cell4Row4.setCellValue("出港");
		return this;
	}
	
	/**
	 * 设置上下两表格之间的大标题
	 */
	public ExportExcel setBetweenTitles(){
		sheet.addMergedRegion(new CellRangeAddress(rownum,rownum,0,14));
		Row row = this.addRow();
		CellStyle style = styles.get("data");
		Cell cell=row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue("延误统计");
		for(int i=1;i<15;i++){
			row.createCell(i).setCellStyle(style);
		}
		return this;
	}
	
	 
	// draw cell line
	
	private void drawLine(Row row, int i, int j, int width, int height,
	    int[] xys) {
	int PERCENT_WIDTH = 50;
	int PERCENT_HEIGHT = 20; 
	float PXTOPT = 0.75f;
	int cellWidth = (int) (PERCENT_WIDTH * PXTOPT * width);
	short cellHeight = (short) (PERCENT_HEIGHT * PXTOPT * height);
	sheet.setColumnWidth(j, cellWidth);
	row.setHeight(cellHeight);
	
	HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
	HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 1023, 255, (short) j, i, (short) 0, 3);
	HSSFShapeGroup group = patriarch.createGroup(a);
	float verticalPointsPerPixel = a.getAnchorHeightInPoints((HSSFSheet) sheet);
	EscherGraphics g = new EscherGraphics(group, (HSSFWorkbook) sheet.getWorkbook(), Color.black,
	        verticalPointsPerPixel);
	EscherGraphics2d g2d = new EscherGraphics2d(g);
	 
	for (int l = 0; l < xys.length; l += 2) {
	    int x = (int) ((PERCENT_WIDTH * 0.75 * xys[l] / cellWidth) * 1023);
	    int y = (int) ((PERCENT_HEIGHT * 0.75 * xys[l + 1] / cellHeight) * 255);
	    g2d.drawLine(0, 0, x, y);
	  }
	}
	
	
	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
//	public ExportExcel setDataList(JSONArray titleArray, List<Map<String,String>> dataList){
//		for (int i=0;i<dataList.size();i++){
//			int colunm = 0;
//			Row row = this.addRow();
//			
//			StringBuilder sb = new StringBuilder();
//			for(int j=0;j<titleArray.size();j++){
//				String field = titleArray.getJSONObject(j).getString("field");
//				val = dataList.get(i).get(field);
//				this.addCell(row, colunm++, val);
//				sb.append(val + ", ");
//			}
//			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
//		}
//		return this;
//	}
	public ExportExcel setDataList(JSONArray titleArray, JSONArray dataList){
	for (int i=0;i<dataList.size();i++){
		int colunm = 0;
		Row row = this.addRow();
		
		StringBuilder sb = new StringBuilder();
		for(int j=0;j<titleArray.size();j++){
			String field = titleArray.getJSONObject(j).getString("field");
			val = ((JSONObject)dataList.get(i)).get(field).toString();
			this.addCell(row, colunm++, val);
			sb.append(val + ", ");
		}
		log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
	}
	return this;
}
	
	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @param titleList 列表的title
	 * @param columnArray 数据的columnName 
	 * @param dataList 数据集合
	 * @return
	 */
	public ExportExcel setDataList(String[] titleList,JSONArray columnArray, List<Map<String,Object>> dataList){
		Row titleRow = this.addRow();
		CellStyle style = styles.get("data");
		for (int i = 0; i < titleList.length; i++) {
       	 Cell cell = titleRow.createCell(i);
       	 cell.setCellValue(titleList[i]);
       	 cell.setCellStyle(style);
        }
		for (int i=0;i<dataList.size();i++){
			int colunm = 0;
			Row row = this.addRow();
			
			StringBuilder sb = new StringBuilder();
			for(int j=0;j<columnArray.size();j++){
				String field = columnArray.getJSONObject(j).getString("field");
				Map<String,Object> data=dataList.get(i);
				if(data!=null){
					val =  data.get(field).toString();
				}
				this.addCell(row, colunm++, val);
				sb.append(val + ", ");
			}
		}
		return this;
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
//		wb.dispose();
		return this;
	} 
    private Cell getCell(Row headerRow,int begin,int end,String type,String value) {
        Cell cell = headerRow.createCell(begin);
        CellRangeAddress craKey =new CellRangeAddress( rownum-1, rownum-1,begin, end ); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(craKey); 
        if(!StringUtils.isBlank(type)) {
            cell.setCellStyle(styles.get(type));
        }
        cell.setCellValue(value );
        return cell;
    }
    
    public void exportPic(byte[] data) {
        // 关于HSSFClientAnchor(dx1,dy1,dx2,dy2,col1,row1,col2,row2)的参数，有必要在这里说明一下：
        // dx1：起始单元格的x偏移量，
        // dy1：起始单元格的y偏移量，
        // dx2：终止单元格的x偏移量，
        // dy2：终止单元格的y偏移量，
        // col1：起始单元格列序号，从0开始计算；
        // row1：起始单元格行序号，从0开始计算，
        // col2：终止单元格列序号，从0开始计算；
        // row2：终止单元格行序号，从0开始计算，
        //添加多个图片时:多个pic应该share同一个DrawingPatriarch在同一个sheet里面。
        Drawing patriarch = sheet.createDrawingPatriarch();
        // anchor主要用于设置图片的属性
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 250, (short) 1, rownum, (short) 5, rownum +4);
        // 插入图片
        patriarch.createPicture(anchor, wb.addPicture(data, HSSFWorkbook.PICTURE_TYPE_JPEG));
    }

    public void setTitleData(JSONArray titleArray) {
        Row headerRow = sheet.createRow(rownum++);
        for (int i = 0; i < titleArray.size(); i++) {
            JSONObject title = titleArray.getJSONObject(i);
            getCell(headerRow, i, i,"header", title.getString("title"));
        }
    }


}
