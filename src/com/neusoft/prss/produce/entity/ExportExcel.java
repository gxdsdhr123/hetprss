package com.neusoft.prss.produce.entity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import org.apache.poi.ss.util.RegionUtil;
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
	
	public ExportExcel(String title){
	    this.wb = new SXSSFWorkbook(500);
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
	public ExportExcel setDataList(JSONArray titleArray, List<Map<String,String>> dataList){
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
	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @param titleList 列表的title
	 * @param columnArray 数据的columnName 
	 * @param dataList 数据集合
	 * @return
	 */
	public ExportExcel setDataList(String[] titleList,JSONArray columnArray, List<Map<String,String>> dataList){
		Row titleRow = this.addRow();
		for (int i = 0; i < titleList.length; i++) {
       	 Cell cell = titleRow.createCell(i);
       	 cell.setCellValue(titleList[i]);
        }
		for (int i=0;i<dataList.size();i++){
			int colunm = 0;
			Row row = this.addRow();
			
			StringBuilder sb = new StringBuilder();
			for(int j=0;j<columnArray.size();j++){
				String field = columnArray.getJSONObject(j).getString("field");
				val = (String) dataList.get(i).get(field);
				this.addCell(row, colunm++, val);
				sb.append(val + ", ");
			}
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
	public ExportExcel setTimeSlotWorkloadData(String[] titleList,JSONArray columnArray, List<Map<String,String>> dataList,Map<String, Object> param){
		Row headerRow = this.addRow();
		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("员工时段内工作量统计");
		headerCell.setCellStyle(styles.get("title"));
		CellRangeAddress cra =new CellRangeAddress( 0, 0,0, columnArray.size()-1); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra);  
		Row conditionRow = this.addRow();
		Cell conditionCell = conditionRow.createCell(0);
		conditionCell.setCellValue("开始日期：");
		conditionCell = conditionRow.createCell(1);
		String begin = String.valueOf(param.get("dateStart"));
		begin = begin.substring(0,4) + "/" + begin.substring(4,6) + "/" + begin.substring(6);
		conditionCell.setCellValue(begin);
		
		conditionCell = conditionRow.createCell(2);
		conditionCell.setCellValue("结束日期：");
		conditionCell = conditionRow.createCell(3);
		String end = String.valueOf(param.get("dateEnd"));
		end = end.substring(0,4) + "/" + end.substring(4,6) + "/" + end.substring(6);
		conditionCell.setCellValue(end);
		
		conditionCell = conditionRow.createCell(4);
		conditionCell.setCellValue("岗位：");
		conditionCell = conditionRow.createCell(5);
		String jobKindName = String.valueOf(param.get("jobKind"));
		if("null".equals(jobKindName))
			jobKindName = "";
		conditionCell.setCellValue(StringUtils.nvl(jobKindName));
		
		conditionCell = conditionRow.createCell(6);
		conditionCell.setCellValue("姓名：");
		Row titleRow = this.addRow();
		for (int i = 0; i < titleList.length; i++) {
       	 Cell cell = titleRow.createCell(i);
       	 cell.setCellValue(titleList[i]);
       	 CellStyle style = styles.get("data");
       	 cell.setCellStyle(style);
        }
		int num = 0;
		for (int i=0;i<dataList.size();i++){
			int colunm = 0;
			Row row = this.addRow();
			for(int j=0;j<columnArray.size();j++){
				String field = columnArray.getJSONObject(j).getString("field");
				val = StringUtils.nvl(String.valueOf(dataList.get(i).get(field)));
				if("总计".equals(val)) {
					num++;
				}
				if("null".equals(val))
					val = "";
				this.addCell(row, colunm++, val);
			}
		}
		CellRangeAddress cra1 =new CellRangeAddress( dataList.size()+3-num, dataList.size()+2,0, 0); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra1);  
        CellRangeAddress cra2 =new CellRangeAddress( dataList.size()+3-num, dataList.size()+2,1, 1); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra2);  
		return this;
	}
	
	public ExportExcel setUpdate(JSONArray data){
	    JSONObject jc = new JSONObject();
        JSONObject sc = new JSONObject();
        if(data.size()>0)
	    jc = data.getJSONObject(0);
        if(data.size()>1)
	    sc = data.getJSONObject(1);
	    Row headerRow = sheet.createRow(rownum++);
        String[][] row1 = {{"日期","OPER_DATE"},{"驾驶员","NAME"},{"车号","VEHICLE_NUMBER"},{"车辆型号","DEVICE_MODEL"}};
        String[][] row2 = {{"接车",""},{"出勤时间","OPER_BOUND_DATE"},{"公里数","VALUE1"},{"小时数","VALUE2"},{"燃油","VALUE3"},{"加油","VALUE4"}};
        String[][] row3 = {{"异常系统记录","JCXM_NAME"}};
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
            if("日期".equals(value1) || "车号".equals(value1) || "车辆型号".equals(value1)){
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

    public void setData(JSONArray data,int flag) {
        Row headerRow = sheet.createRow(rownum++);
        String[][] row1 = {{"序号",""},{"航班号","FLIGHT_NUMBER"},{"注册号","AIRCRAFT_NUMBER"},{"STD","STD"},
                {"机位","ACTSTAND_CODE"},{"收到时间","ACT_ARRANGE_TM"},{"到位时间","TIME1"},{"开始作业时间","TIME2"},{"结束时间","TIME3"}};
        String[][] row2 = {{"序号",""},{"航班号","FLIGHT_NUMBER"},{"注册号","AIRCRAFT_NUMBER"},{"起始位置","DEVICE_MODEL"},
                {"拖至位置位",""},{"收到时间","ACT_ARRANGE_TM"},{"到位时间","TIME1"},{"开始作业时间","TIME2"},{"结束时间","TIME3"}};
        if(flag==2)
            row1 = row2;
        Cell tcell = headerRow.createCell(0);
        tcell.setCellStyle(styles.get("header"));
        tcell.setCellValue(flag ==1 ?"推飞机工作记录":"拖拽飞机工作记录");
        CellRangeAddress cra1 =new CellRangeAddress( rownum-1, rownum-1,0, 8); // 起始行, 终止行, 起始列, 终止列  
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

    public void setDataGZ(JSONArray data) {
        JSONObject jc = new JSONObject();
        JSONObject sc = new JSONObject();
        if(data.size()>0)
        jc = data.getJSONObject(0);
        if(data.size()>1)
        sc = data.getJSONObject(1);
        Row headerRow = sheet.createRow(rownum++);
        Cell cell = headerRow.createCell(0);
        cell.setCellStyle(styles.get("header"));
        cell.setCellValue("车辆故障记录");
        CellRangeAddress cra =new CellRangeAddress( rownum-1, rownum-1,0, 8); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra); 
        
        headerRow = sheet.createRow(rownum++);
        Cell cell2 = headerRow.createCell(0);
        CellRangeAddress cra1 =new CellRangeAddress( rownum-1, rownum-1,0, 4); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra1); 
        cell2.setCellStyle(styles.get("data"));
        cell2.setCellValue(jc.getString("CLGZ_DESC"));

        Cell cell3 = headerRow.createCell(5);
        CellRangeAddress cra2 =new CellRangeAddress( rownum-1, rownum-1,5,8); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra2); 
        cell3.setCellStyle(styles.get("data"));
        cell3.setCellValue(sc.getString("CLGZ_DESC"));
        sheet.autoSizeColumn(1, true);
    }
    /**
     * 
     *Discription:清洁服务单导出.
     *@param json
     *@return:返回值意义
     *@author:yunwq@neusoft.com
     *@update:2018年8月30日  [变更描述]
     */
    public void setQJFWDataList(List<HashMap<String, Object>> totalList) {
		for (int i = 0; i < totalList.size(); i++) {
			HashMap<String, Object> map = totalList.get(i);
			Row headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 0, 7 ,"header", "客舱清洁服务单");
			headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 0, 0 ,"value", "航空公司");
			getCell(headerRow, 1, 3 ,"value", map.get("ALN_NAME")==null?"":map.get("ALN_NAME").toString());
			getCell(headerRow, 4, 4 ,"value", "航班性质");
			getCell(headerRow, 5, 7 ,"value", map.get("FLT_TYPE")==null?"":map.get("FLT_TYPE").toString());
			headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 0, 0 ,"value", "日期");
			getCell(headerRow, 1, 1 ,"value", "航班号");
			getCell(headerRow, 2, 2 ,"value", "机号");
			getCell(headerRow, 3, 3 ,"value", "机型");
			getCell(headerRow, 4, 5 ,"value", "保障时间");
			getCell(headerRow, 6, 7 ,"value", "备注");
			headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 0, 0 ,"value", map.get("FLIGHT_DATE")==null?"":map.get("FLIGHT_DATE").toString());
			getCell(headerRow, 1, 1 ,"value", map.get("FLIGHT_NUMBER")==null?"":map.get("FLIGHT_NUMBER").toString());
			getCell(headerRow, 2, 2 ,"value", map.get("AIRCRAFT_NUMBER")==null?"":map.get("AIRCRAFT_NUMBER").toString());
			getCell(headerRow, 3, 3 ,"value", map.get("ACTTYPE_CODE")==null?"":map.get("ACTTYPE_CODE").toString());
			getCell(headerRow, 4, 4 ,"value", map.get("JOB_BEGIN")==null?"":map.get("JOB_BEGIN").toString());
			getCell(headerRow, 5, 5 ,"value", map.get("JOB_END")==null?"":map.get("JOB_END").toString());
			getCell(headerRow, 1, 6, 7 ,"value", map.get("REMARK")==null?"":map.get("REMARK").toString());
			headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 1, 0, 0 ,"value", "携带工具");
			getCell(headerRow, 1, 5 ,"value", map.get("CARRY_TOOLS")==null?"":map.get("CARRY_TOOLS").toString());
			headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 1, 1 ,"value", "其它工具");
			getCell(headerRow, 2, 5 ,"value", map.get("OTHER_TOOLS")==null?"":map.get("OTHER_TOOLS").toString());
			getCell(headerRow, 6, 7 ,"value", "乘务签字及评语");
			headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 0, 5 ,"value", "清洁员");
			getCell(headerRow, 1, 6, 7 ,"value", "");
			headerRow = sheet.createRow(rownum++);
			getCell(headerRow, 0, 5 ,"value", map.get("CLEANERS")==null?"":map.get("CLEANERS").toString());
		}
	}
    /**
     * 
     *Discription:航线保障收费单据导出.
     *@param json
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年3月30日 Maxx [变更描述]
     */
    public void setHXDataList(JSONObject json) {
        String[][] first = {{"STATION","北京"},{"NO.","id"},{"FLT.NO.","flightNumber"},{"A/C TYPE","actType"},{"A/C REGN","aircraftNumber"},
                {"DATE","flightDate"},{"PARKING BAY","actstandCode"},{"航班性质","fltAttrCode"}};
        Row headerRow = sheet.createRow(rownum++);
        int begin = 0;
        for(int i=0;i<first.length;i++) {
            String key = first[i][0];
            String value = first[i][1];
            if(i > 0 && i%2 == 0) {
                headerRow = sheet.createRow(rownum++);
                begin = 0;
            }
            getCell(headerRow, begin, ++begin ,"header", key);
            if("航班性质".equals(key)) {
                value = StringUtils.nvl(json.getString(value));
                switch (value) {
                    case "D":
                        value = "国内";
                        break;
                    case "I":
                        value = "国际";
                        break;
                    case "M":
                        value = "混装";
                        break;
                    default:
                        value = "";
                        break;
                }
            } else if(!"STATION".equals(key)) {
                value = json.getString(value);
            }
            begin ++;
            getCell(headerRow, begin, ++begin ,null, value);
            begin ++;
        }

        String[][] second = {{"1","arrival"},{"1","transit"},{"1","nightstop"},
                            {"1","departure"},{"1","returntoramp"},{"1","others"},
                            {"2","TOWING"},{"FROM","towingFrom"},{"TO","towingTo"}};
        headerRow = sheet.createRow(rownum++);
        begin = 0;
        for(int i=0;i<second.length;i++) {
            String key = second[i][0];
            String value = second[i][1];
            if(i > 0 && i%3 == 0) {
                headerRow = sheet.createRow(rownum++);
                begin = 0;
            }
            switch (key) {
                case "1":
                    getCell(headerRow, begin, ++begin ,"header", value.toUpperCase());
                    begin++;
                    
                    getCell(headerRow, begin, begin ,null, ("1".equals(json.getString(value))?"√":"×"));
                    begin++;
                    break;
                case "2":
                    getCell(headerRow, begin, ++begin ,"header", value.toUpperCase());
                    begin++;
                    break;
                default :
                    getCell(headerRow, begin, begin ,"header", key.toUpperCase());
                    begin++;
                    
                    getCell(headerRow, begin, begin ,null, StringUtils.nvl(json.getString(value)));
                    begin++;
                    break;
            }
        }
        
        String[] thrid = {"STA","STD","ATA","ATD"};
        headerRow = sheet.createRow(rownum++);
        begin = 0;
        for(int i=0;i<thrid.length;i++) {
            String key = thrid[i];
            getCell(headerRow, begin, begin ,"header", key);
            begin++;
            
            getCell(headerRow, begin, begin ,null, StringUtils.nvl(json.getString(key.toLowerCase())));
            begin++;
        }
        String[][] fouth = {{"1","ENGINE NUMBER"},{"1","1"},{"1","2"},{"1","3"},{"1","4"},{"1","APU"},
                {"1","OIL UPLIFT(QTS/PINTS)"},{"2","oilUpliftOne"},{"2","oilUpliftTwo"},{"2","oilUpliftThree"},{"2","oilUpliftFour"},{"2","oilUpliftApu"},
                {"1","C S D OIL UPLIFT"},{"2","csdoilUpliftOne"},{"2","csdoilUpliftTwo"},{"2","csdoilUpliftThree"},{"2","csdoilUpliftFour"},{"2","csdoilUpliftApu"},
                {"1","HYDRAULIC OIL UPLIFT"},{"2","hydraulicoilUpliftOne"},{"2","hydraulicoilUpliftTwo"},{"2","hydraulicoilUpliftThree"},{"2","hydraulicoilUpliftFour"},{"2","hydraulicoilUpliftApu"}};
        headerRow = sheet.createRow(rownum++);
        begin = 0;
        for(int i=0;i<fouth.length;i++) {
            String key = fouth[i][0];
            String value = fouth[i][1];
            if(i > 0 && i%6 == 0) {
                headerRow = sheet.createRow(rownum++);
                begin = 0;
            }
            switch (key) {
                case "1":
                    getCell(headerRow, begin, begin ,"header", value.toUpperCase());
                    begin++;
                    break;
                case "2":
                    getCell(headerRow, begin, begin ,null, StringUtils.nvl(json.getString(value)));
                    begin++;
                    break;
                default :
                    break;
            }
        }
        String[][] fifth = {{"ENGINE OIL TYPE",""},{"STOCK","engineoilStock"},{"QTY","engineoilQty"},
                {"HYD TYPE",""},{"STOCK","hydStock"},{"QTY","hydQty"},
                {"APU OIL TYPE",""},{"STOCK","apuoilStock"},{"QTY","apuoilQty"}};
        
        headerRow = sheet.createRow(rownum++);
        begin = 0;
        for(int i=0;i<fifth.length;i++) {
            String key = fifth[i][0];
            String value = fifth[i][1];
            if(i > 0 && i%3 == 0) {
                headerRow = sheet.createRow(rownum++);
                begin = 0;
            }
            if(begin ==0 ) {
                    getCell(headerRow, begin, ++begin ,"header", key);
                    begin++;
            } else {
                getCell(headerRow, begin, begin ,"header", key);
                begin++;

                value = StringUtils.nvl(json.getString(value));
                if(!StringUtils.isBlank(value)) {
                    value = "OPERATOR".equals(value)?"OPERATOR":value;
                    value = "BGS".equals(value)?"BGS":value;
                }
                getCell(headerRow, begin, begin ,null, value);
                begin++;
            }
        }
        
        String[][] sixth = {{"GROUND EQUIPMENT","HOUR/SERVICE"},
                {"AIRCRAFT PUSH-OUT","aircraftPushout"},
                {"AIRCRAFT TOWING","aircraftTowing"},
                {"WATER SERVICING","waterServicing"},
                {"TOILET SERVICING","toiletServicing"},
                {"GROUND POWER UNIT","groundPowerUnit"},
                {"AIR CONDITIONG UNIT","airConditiongUnit"},
                {"GAS TURBINE STARTER UNIT","gasTurbineStarterUnit"},
                {"OXYGEN CHARGING","oxygenCharging"},
                {"NITROGEN CHARGING","nitrogenCharging"},
                {"MAINTENANCE STEPS","maintenanceSteps"},
                {"MAINT.PLATFORM","maintPlatform"},
                {"WHEEL JACKS","wheelJacks"},
                {"EQUIPMENT TOW TUG","equipmentTowTug"}};
        begin = 0;
        for(int i=0;i<sixth.length;i++) {
            headerRow = sheet.createRow(rownum++);
            String key = sixth[i][0];
            String value = sixth[i][1];
            getCell(headerRow, begin, begin + 2,"header", key);
            begin = begin + 3;
            
            if(i > 0)
                value = StringUtils.nvl(json.getString(value));
            getCell(headerRow, begin, begin + 2,i==0?"header":null, value);
            begin = 0;
        }
        headerRow = sheet.createRow(rownum++);
        begin = 0;
        Cell cell = headerRow.createCell(begin);
        CellRangeAddress craKey =new CellRangeAddress( rownum-1, rownum,begin, begin + 2); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(craKey); 
        cell.setCellStyle(styles.get("header"));
        cell.setCellValue("RECTIFICATION/ADDITIONAL SERVICE" );
        begin = begin + 3;
        
        getCell(headerRow, begin, begin + 2,"header", "MAN HOURS");
        
        headerRow = sheet.createRow(rownum++);
        begin = 3;
        getCell(headerRow, begin, begin+1,"header", "ENGR");
        begin = begin + 2;
        
        getCell(headerRow, begin, begin,"header", "MECH");

        headerRow = sheet.createRow(rownum++);
        begin = 0;
        getCell(headerRow, begin, begin,"header", "操作人");
        begin = begin + 1;
        
        getCell(headerRow, begin, ++begin,null, json.getString("operatorName"));
        begin = begin + 1;

        getCell(headerRow, begin, begin,"header", "创建时间");
        begin = begin + 1;
        getCell(headerRow, begin, ++begin,null, json.getString("createDate"));

        headerRow = sheet.createRow(rownum++);
        begin = 0;
        getCell(headerRow, begin, begin,"header", "签名");
        begin = begin + 1;
        getCell(headerRow, begin, ++begin,null, json.getString("sign"));
    }
    
    private Cell getCell(Row headerRow,int begin,int end,String type,String value) {
        Cell cell = headerRow.createCell(begin);
        CellRangeAddress craKey =new CellRangeAddress( rownum-1, rownum-1,begin, end ); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(craKey); 
        if("header".equals(type)) {
            cell.setCellStyle(styles.get(type));
        }else if("value".equals(type)){
    		RegionUtil.setBorderBottom(1, craKey, sheet,wb); // 下边框
    		RegionUtil.setBorderLeft(1, craKey, sheet,wb); // 左边框
    		RegionUtil.setBorderRight(1, craKey, sheet,wb); // 有边框
    		RegionUtil.setBorderTop(1, craKey, sheet,wb); // 上边框
        }
        cell.setCellValue(value );
        return cell;
    }
    
    private Cell getCell(Row headerRow,int bottom,int begin,int end,String type,String value) {
        Cell cell = headerRow.createCell(begin);
        CellRangeAddress craKey =new CellRangeAddress( rownum-1, rownum-1+bottom,begin, end ); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(craKey); 
        if("header".equals(type)) {
            cell.setCellStyle(styles.get(type));
        }else if("value".equals(type)){
    		RegionUtil.setBorderBottom(1, craKey, sheet,wb); // 下边框
    		RegionUtil.setBorderLeft(1, craKey, sheet,wb); // 左边框
    		RegionUtil.setBorderRight(1, craKey, sheet,wb); // 有边框
    		RegionUtil.setBorderTop(1, craKey, sheet,wb); // 上边框
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
    
    public void exportQJFWPic(List<byte[]> data) {
    	//添加多个图片时:多个pic应该share同一个DrawingPatriarch在同一个sheet里面。
        Drawing patriarch = sheet.createDrawingPatriarch();
        for (int i = 0; i < data.size(); i++) {
        	// anchor主要用于设置图片的属性
        	XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 250, (short) 6, (1+i)*8-2, (short) 8, (1+i)*8 );
        	// 插入图片
        	patriarch.createPicture(anchor, wb.addPicture(data.get(i), HSSFWorkbook.PICTURE_TYPE_JPEG));
		}
	}

    public void setTitleData(JSONArray titleArray) {
        Row headerRow = sheet.createRow(rownum++);
        for (int i = 0; i < titleArray.size(); i++) {
            JSONObject title = titleArray.getJSONObject(i);
            getCell(headerRow, i, i,"header", title.getString("title"));
        }
    }

    public void setData(JSONArray titleArray,JSONArray datas) {
        for (int j = 0; j < datas.size(); j++) {
            JSONObject data = datas.getJSONObject(j);
            Row headerRow = sheet.createRow(rownum++);
            for (int i = 0; i < titleArray.size(); i++) {
                JSONObject title = titleArray.getJSONObject(i);
                String key = title.getString("field");
                String value =  data.getString(key);
                if("ATTR_CODE".equals(key)) {
                    value = "D".equals(value)?"国内":"国际";
                }
                getCell(headerRow, i, i,"data",value);
            }
        }
    }
    
    /**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @param titleList 列表的title
	 * @param columnArray 数据的columnName 
	 * @param dataList 数据集合
	 * @return
	 */
	public ExportExcel setWorkloadData(String[] titleList,JSONArray columnArray, List<Map<String,String>> dataList,Map<String, Object> param){
		Row headerRow = this.addRow();
		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("员工工作量统计");
		headerCell.setCellStyle(styles.get("title"));
		CellRangeAddress cra =new CellRangeAddress( 0, 0,0, columnArray.size()-1); // 起始行, 终止行, 起始列, 终止列  
        sheet.addMergedRegion(cra);  
		Row conditionRow = this.addRow();
		Cell conditionCell = conditionRow.createCell(0);
		conditionCell.setCellValue("航班日期：");
		conditionCell = conditionRow.createCell(1);
		String begin = String.valueOf(param.get("dateStart"));
		begin = begin.substring(0,4) + "/" + begin.substring(4,6) + "/" + begin.substring(6);
		String end = String.valueOf(param.get("dateEnd"));
		end = end.substring(0,4) + "/" + end.substring(4,6) + "/" + end.substring(6);
		conditionCell.setCellValue(begin + "-" + end );
		conditionCell = conditionRow.createCell(2);
		conditionCell.setCellValue("岗位：");
		conditionCell = conditionRow.createCell(3);
		String jobKind = String.valueOf(param.get("jobKindName"));
		if("null".equals(jobKind))
			jobKind = "";
		conditionCell.setCellValue(StringUtils.nvl(jobKind));
		conditionCell = conditionRow.createCell(4);
		conditionCell.setCellValue("姓名：");
		Row titleRow = this.addRow();
		for (int i = 0; i < titleList.length; i++) {
       	 Cell cell = titleRow.createCell(i);
       	 cell.setCellValue(titleList[i]);
       	 CellStyle style = styles.get("data");
       	 cell.setCellStyle(style);
        }
		for (int i=0;i<dataList.size();i++){
			int colunm = 0;
			Row row = this.addRow();
			for(int j=0;j<columnArray.size();j++){
				String field = columnArray.getJSONObject(j).getString("field");
				val = StringUtils.nvl(String.valueOf(dataList.get(i).get(field)));
				if("null".equals(val))
					val = "";
				this.addCell(row, colunm++, val);
			}
		}
		return this;
	}

}
