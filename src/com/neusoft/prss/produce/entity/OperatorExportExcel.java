package com.neusoft.prss.produce.entity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

public class OperatorExportExcel {

	
	private static Logger log = LoggerFactory.getLogger(OperatorExportExcel.class);
			
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
	
	public OperatorExportExcel(String title){
	    this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet("sheet");
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
	public OperatorExportExcel setDataList(JSONArray titleArray, List<Map<String,String>> dataList){
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
	
	public OperatorExportExcel setUpdate(JSONArray data){
	    rownum ++;
	    Row headerRow = sheet.createRow(rownum++);
        String[][] row1 = {{"序号",""},{"航班号","FLIGHT_NUMBER"},{"注册号","AIRCRAFT_NUMBER"},{"出发地","ACTSTAND_CODE"},{"目的地","GATE"},{"操作人","NAME"},
                {"车号","INNER_NUMBER"},{"ETA\\STD","ETA"},{"任务派发时间","ACT_ARRANGE_TM"},{"任务接收时间","RECEIVTIME"},{"到位时间","TIME1"},
                {"上客时间","TIME2"},{"发车时间","TIME3"},{"完成时间","TIME4"}};
        
        int row =0;
        for(int i=0;i<row1.length;i++){
            String value1 = row1[i][0];
            Cell cell = headerRow.createCell(row++);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(value1);
        }
        for(int j=0;j<data.size();j++){
            headerRow = sheet.createRow(rownum++);
            JSONObject jsonObject = data.getJSONObject(j);
            row =0;
            for(int i=0;i<row1.length;i++){
                String value = "";
                value = jsonObject.getString(row1[i][1]);
                if("ETA\\STD".equals(row1[i][0])){
                    String fioType = jsonObject.getString("IN_OUT_FLAG");
                    if(fioType.indexOf("D")>-1)
                        value = jsonObject.getString("STD");
                } else if("出发地".equals(row1[i][0])){
                    String fioType = jsonObject.getString("IN_OUT_FLAG");
                    if(fioType.indexOf("D")>-1)
                        value = jsonObject.getString("GATE");
                } else if("目的地".equals(row1[i][0])){
                    String fioType = jsonObject.getString("IN_OUT_FLAG");
                    if(fioType.indexOf("D")>-1)
                        value = jsonObject.getString("ACTSTAND_CODE");
                } else if("序号".equals(row1[i][0])){
                    value = (j+1) + "";
                }
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("data"));
                cell.setCellValue(value);
            }
        }
	    return this;
	}
	
	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public OperatorExportExcel write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public OperatorExportExcel write(HttpServletResponse response) throws IOException{
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param fileName 输出文件名
	 */
	public OperatorExportExcel writeFile(String name) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}
	
	/**
	 * 清理临时文件
	 */
	public OperatorExportExcel dispose(){
		wb.dispose();
		return this;
	}

    public void setData(JSONArray data,int flag,int num,String reskind) {
        if(flag ==1 && num>0){
            this.sheet = wb.createSheet("sheet"+ num);
            rownum=0;
        }
        Row headerRow = sheet.createRow(rownum++);
        String[][] row1 = {{"项目","状态"},{"外观",""},{"灯光",""},{"转向",""},
                {"车门",""},{"卫生",""},{"喇叭",""},{"刹车",""},{"消防锤",""},
                {"仪表",""},{"灭火器",""}};
        String[][] row2 = {{"轮胎",""},{"气压表",""},{"毛巾",""},{"麂皮",""},
                {"掸子",""},{"水桶",""},{"公里数","VALUE0"},{"加油量","VALUE1"},{"小时数","VALUE2"},
                {"燃油数","VALUE3"},{"行驶公里数","VALUE4"}};
        String[][] row3 = {{"项目","状态"},{"外观卫生",""},{"灭火器",""},{"反光镜",""},
                {"油箱盖",""},{"轮挡",""},{"轮胎",""},{"仪表",""}};
        String[][] row4 = {{"机油",""},{"燃油",""},{"喇叭",""},{"灯光",""},
                {"转向",""},{"制动",""},{"里程数","VALUE0"},{"燃油数","VALUE3"}};
        if(!"JWBDC".equals(reskind)){
            row1 = row3;
            row2 = row4;
        }

        Cell tcell = headerRow.createCell(0);
        
        if(flag ==1){
            String title = "";
            if(data.size()>0){
                JSONObject jsonObject = data.getJSONObject(0);
                title = "创建时间：" + jsonObject.getString("CREATE_DATE") + " 车号：" + jsonObject.getString("INNER_NUMBER");
            }
            tcell.setCellStyle(styles.get("header"));
            tcell.setCellValue(title);
            CellRangeAddress cra1 =new CellRangeAddress( rownum-1, rownum-1,0, row1.length-1); // 起始行, 终止行, 起始列, 终止列  
            sheet.addMergedRegion(cra1); 
            headerRow = sheet.createRow(rownum++);
        }
        Cell tcell2 = headerRow.createCell(0);
        tcell2.setCellStyle(styles.get("header"));
        tcell2.setCellValue(flag ==1 ?"接车项目":"收车项目");
        CellRangeAddress cra2 =new CellRangeAddress( rownum-1, rownum-1,0, row1.length-1); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra2); 

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
            JSONObject jsonObject = data.getJSONObject(j);
            headerRow = sheet.createRow(rownum++);
            row =0;
            for(int i=0;i<row1.length;i++){
                String value = "";
                if("项目".equals(row1[i][0])){
                    value = row1[i][1];
                } else if("".equals(row1[i][1])){
                    value = "1".equals(jsonObject.getString(row1[i][0] + "_INS_TYPE"))?"√":"×";
                } 
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("data"));
                cell.setCellValue(value);
            }
        }
        headerRow = sheet.createRow(rownum++);
        row =0;
        for(int i=0;i<row2.length;i++){
            String value1 = row2[i][0];
            if("行驶公里数".equals(value1)){
                if(flag==1)
                    value1 = "";
            }
            Cell cell = headerRow.createCell(row++);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(value1);
            sheet.autoSizeColumn(1, true);
        }
        
        for(int j=0;j<data.size();j++){
            JSONObject jsonObject = data.getJSONObject(j);
            headerRow = sheet.createRow(rownum++);
            row =0;
            for(int i=0;i<row2.length;i++){
                String value = "";
               if("".equals(row2[i][1])){
                    value = "1".equals(jsonObject.getString(row2[i][0] + "_INS_TYPE"))?"√":"×";
                } else {
                    if("行驶公里数".equals(row2[i][0])){
                        if(flag==1)
                            value = "";
                    } else {
                        value = jsonObject.getString(row2[i][1]);
                    }
                }
                Cell cell = headerRow.createCell(row++);
                cell.setCellStyle(styles.get("data"));
                cell.setCellValue(value);
            }
        }
    }


}
