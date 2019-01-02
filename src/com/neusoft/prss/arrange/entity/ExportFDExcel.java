/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月27日 上午10:48:54
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.entity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

public class ExportFDExcel {

    private static Logger log = LoggerFactory.getLogger(ExportFDExcel.class);

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
    private Map<String,CellStyle> styles;

    /**
     * 当前行号
     */
    private int rownum;


    /**
     * 构造函数
     * @param title 表格标题，传“空值”，表示无标题
     * @param headerList 表头列表
     */
    public ExportFDExcel(String title,List<String> headerList) {
        initialize(title, headerList);
    }

    /**
     * 初始化函数
     * @param title 表格标题，传“空值”，表示无标题
     * @param headerList 表头列表
     */
    private void initialize(String title,List<String> headerList) {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet("Export");
        this.styles = createStyles(wb);
        // Create title
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            CellStyle cellStyle = styles.get("title");
            cellStyle.setWrapText(true);
            titleCell.setCellStyle(cellStyle);
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(),
                    headerList.size() - 1));
        }
        // Create header
        if (headerList == null) {
            throw new RuntimeException("headerList not null!");
        }
        Row headerRow = sheet.createRow(rownum++);
        headerRow.setHeightInPoints(25);
        for (int i = 0; i < headerList.size(); i++) {
            String str = headerList.get(i).split(":")[1];
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(str);
            sheet.autoSizeColumn(i);
        }
        Row headerRow2 = sheet.createRow(rownum++);
        headerRow2.setHeightInPoints(25);
        for (int i = 0; i < headerList.size(); i++) {
            String str = headerList.get(i).split(":")[0];
            Cell cell = headerRow2.createCell(i);
            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(str);
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
        log.debug("Initialize success.");
    }

    /**
     * 创建表格样式
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String,CellStyle> createStyles(Workbook wb) {
        Map<String,CellStyle> styles = new HashMap<String,CellStyle>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("微软雅黑");
        titleFont.setFontHeightInPoints((short) 11);
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
        dataFont.setFontName("微软雅黑");
        dataFont.setFontHeightInPoints((short) 11);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("微软雅黑");
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 添加一行
     * @return 行对象
     */
    public Row addRow() {
        return sheet.createRow(rownum++);
    }

    /**
     * 添加一个单元格
     * @param row 添加的行
     * @param column 添加列号
     * @param val 添加值
     * @return 单元格对象
     */
    public Cell addCell(Row row,int column,String val) {
        Cell cell = row.createCell(column);
        CellStyle style = styles.get("data");
        try {
            if (val == null) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(val);
            }
        } catch (Exception ex) {
            log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
            cell.setCellValue(val.toString());
        }
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 添加数据（通过annotation.ExportField添加数据）
     * @param titleList 
     * @return list 数据列表
     */
    public ExportFDExcel setDataList(List<String> titleList,JSONArray dataArray) {
        for (int i = 0; i < dataArray.size(); i++) {
            int colunm = 0;
            Row row = this.addRow();
            row.setHeightInPoints(20);
            JSONObject json = dataArray.getJSONObject(i);
            this.addCell(row, colunm++, json.getString("RM"));
            this.addCell(row, colunm++, json.getString("NAME"));
            for (int j = 2; j < titleList.size(); j++) {
                String d1 = titleList.get(j).split(":")[0];
                this.addCell(row, colunm++, json.getString(d1));
            }
        }
        return this;
    }

    /**
     * 输出数据流
     * @param os 输出数据流
     */
    public ExportFDExcel write(OutputStream os) throws IOException {
        wb.write(os);
        return this;
    }

    /**
     * 输出到客户端
     * @param fileName 输出文件名
     */
    public ExportFDExcel write(HttpServletResponse response) throws IOException {
        write(response.getOutputStream());
        return this;
    }

    /**
     * 输出到文件
     * @param fileName 输出文件名
     */
    public ExportFDExcel writeFile(String name) throws FileNotFoundException,IOException {
        FileOutputStream os = new FileOutputStream(name);
        this.write(os);
        return this;
    }

    /**
     * 清理临时文件
     */
    public ExportFDExcel dispose() {
        wb.dispose();
        return this;
    }

}
