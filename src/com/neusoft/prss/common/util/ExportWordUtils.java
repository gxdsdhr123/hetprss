package com.neusoft.prss.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从模板导出成word
 * @author xuhw
 *
 */
public class ExportWordUtils {
	private static Logger logger = LoggerFactory.getLogger(ExportWordUtils.class);
	private static final int PICTURE_WIDTH = 200;
	private static final int PICTURE_HEIGHT = 50;
	/**
     * 根据模板生成新word文档
     * 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
     * @param inputUrl 模板存放地址
     * @param outPutUrl 新文档存放地址
     * @param textMap 需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     * @return 成功返回true,失败返回false
     */
    public static boolean changWord(String inputUrl, String outputUrl,
            Map<String, String> textMap, List<String[]> tableList) {

        //模板转换默认成功
        boolean changeFlag = true;
        try {
            CustomXWPFDocument document = changeWord(inputUrl, textMap, tableList);

            //生成新的word
            File file = new File(outputUrl);
            FileOutputStream stream = new FileOutputStream(file);
            document.write(stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            changeFlag = false;
        }

        return changeFlag;

    }
    
    /**
     * 根据模板生成新word文档
     * 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
     * @param inputUrl 模板存放地址
     * @param textMap 需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     * @throws IOException 
     */
    public static void changWord(String inputUrl, Map<String, String> textMap, List<String[]> tableList,OutputStream os) throws IOException {
    	
		CustomXWPFDocument document = changeWord(inputUrl, textMap, tableList);
		// 写入输出流
		document.write(os);
    }
    
    /**
     * 根据模板生成新word文档
     * 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
     * @param inputUrl 模板存放地址
     * @param textMap 需要替换的信息集合
     * @param Map<String, String> pictureMap 需要插入的图片信息集合
     * @param tableList 需要插入的表格信息集合
     * @throws IOException 
     */
    public static void changeWord(String inputUrl, Map<String, String> textMap, Map<String, byte[]> pictureMap,List<String[]> tableList,OutputStream os) throws IOException {
    	//获取docx解析对象
    	CustomXWPFDocument document = new CustomXWPFDocument(POIXMLDocument.openPackage(inputUrl));
		//写入图片
		if(pictureMap!=null&&!pictureMap.isEmpty()){
			changePicture(document,pictureMap);
		}
		//解析替换文本段落对象
		ExportWordUtils.changeText(document, textMap);
		//解析替换表格对象
		ExportWordUtils.changeTable(document, textMap, tableList);
		// 写入输出流
		document.write(os);
    }
    /**
     * 替换模板中的参数
     * @param inputUrl 文件路径
     * @param textMap 要替换的参数集合
     * @return
     * @throws IOException
     */
    public static CustomXWPFDocument change(String inputUrl, Map<String, String> textMap) throws IOException {
		// 获取docx解析对象
		CustomXWPFDocument document = new CustomXWPFDocument(POIXMLDocument.openPackage(inputUrl));
		// 解析替换文本段落对象
		changeText(document, textMap);
		// 解析替换表格对象
		List<XWPFTable> tables = document.getTables();
		for (int i = 0; i < tables.size(); i++) {
			XWPFTable table = tables.get(i);
			if (table.getRows().size() > 0) {
				//判断逻辑有$为替换
				if (checkText(table.getText())) {
					List<XWPFTableRow> rows = table.getRows();
					// 遍历表格,并替换模板
					eachTable(rows, textMap);
				}
			}
		}
		return document;
    }
    /**
     * 写入表格数据
     * @param document
     * @param gridData 表格数据 key为表格在模板中的索引（第几个）
     */
    public static void writeGridData(CustomXWPFDocument document,Map<Integer,List<String[]>> gridData){
		if (gridData != null && !gridData.isEmpty()) {
			List<XWPFTable> allGrid = document.getTables();
	        for (Map.Entry<Integer,List<String[]>> dataSet :gridData.entrySet()) {
	        	int index = dataSet.getKey();
	        	List<String[]> data = dataSet.getValue();
				if (index > 0 && index <= allGrid.size() && data != null && !data.isEmpty()) {
					XWPFTable grid = allGrid.get(index - 1);
					for (String[] rowData : data) {
						XWPFTableRow row = grid.createRow();
						for(int cellIndex=0;cellIndex<rowData.length;cellIndex++){
							XWPFTableCell cell = row.getCell(cellIndex);
							cell.setText(rowData[cellIndex]);
						}
					}
				}
	        }
		}
    }
	/**
	 * @param inputUrl
	 * @param textMap
	 * @param tableList
	 * @return
	 * @throws IOException
	 */
	private static CustomXWPFDocument changeWord(String inputUrl,
			Map<String, String> textMap, List<String[]> tableList)
			throws IOException {
		//获取docx解析对象
		CustomXWPFDocument document = new CustomXWPFDocument(POIXMLDocument.openPackage(inputUrl));
		//解析替换文本段落对象
		ExportWordUtils.changeText(document, textMap);
		//解析替换表格对象
		ExportWordUtils.changeTable(document, textMap, tableList);
		return document;
	}

    /**
     * 替换段落文本
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     */
    private static void changeText(CustomXWPFDocument document, Map<String, String> textMap){
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if(checkText(text)){
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    run.setText(changeValue(run.toString(), textMap),0);
                }
            }
        }
    }
    /**
     * 替换表格对象方法
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     */
    private static void changeTable(CustomXWPFDocument document, Map<String, String> textMap,
            List<String[]> tableList){
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格，且不循环表头
            XWPFTable table = tables.get(i);
            if(table.getRows().size()>1){
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if(checkText(table.getText())){
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                }else{
//                  System.out.println("插入"+table.getText());
                    insertTable(table, tableList);
                }
            }
        }
    }

    /**
     * 遍历表格
     * @param rows 表格行对象
     * @param textMap 需要替换的信息集合
     */
    private static void eachTable(List<XWPFTableRow> rows ,Map<String, String> textMap){
    	if(textMap == null){
    		return;
    	}
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
            	String cellText = cell.getText();
                //判断单元格是否需要替换
                if(checkText(cellText)){
                	changeValue(cell,textMap);
//                	cell.setText(changeValue(cellText, textMap));
                }
            }
        }
    }

    /**
     * 为表格插入数据，行数不够添加新行,目前只适应于不正常航班报告打印
     * @param table 需要插入数据的表格
     * @param tableList 插入数据集合
     * @author yunwq
     */
    private static void insertTable(XWPFTable table, List<String[]> tableList){
    	if(tableList == null){
    		return;
    	}
        //创建行,根据需要插入的数据添加新行，不处理表头
        for(int i = 2; i < tableList.size(); i++){
            XWPFTableRow row =table.createRow();
            row.addNewTableCell();
            row.addNewTableCell();
            row.addNewTableCell();
            row.addNewTableCell();
            
            if(i%2 == 0){
	            for (int cellIndex = 0; cellIndex <= 1; cellIndex++) {    
	            	XWPFTableCell cell =  table.getRow(i+1).getCell(cellIndex);    
	            	if ( cellIndex == 0 ) {    
	            		cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);    
	            	} else {    
	            		cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);   
	            	}    
	            }  
	            for (int cellIndex = 2; cellIndex <= 4; cellIndex++) {    
	            	XWPFTableCell cell =  table.getRow(i+1).getCell(cellIndex);    
	            	if ( cellIndex == 2 ) {    
	            		cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);    
	            	} else {    
	            		cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);    
	            	}    
	            }    
            }else{
            	for (int cellIndex = 1; cellIndex <= 2; cellIndex++) {    
	            	XWPFTableCell cell =  table.getRow(i+1).getCell(cellIndex);    
	            	if ( cellIndex == 1 ) {    
	            		cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);    
	            	} else {    
	            		cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);    
	            	}    
	            }  
            }
        }
        //遍历表格插入数据
        List<XWPFTableRow> rows = table.getRows();
        for(int i = 1; i < rows.size(); i++){
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            if(i<3){
	            for(int j = 0; j < cells.size(); j++){
	                XWPFTableCell cell = cells.get(j);
	                cell.setText(tableList.get(i-1)[j]);
	            }
            }else{
            	if(i%2 == 0){
	                XWPFTableCell cell = cells.get(0);
	                cell.setText(tableList.get(i-1)[0]);
	                cell = cells.get(1);
	                cell.setText(tableList.get(i-1)[1]);
	                cell = cells.get(3);
	                cell.setText(tableList.get(i-1)[2]);
	                cell = cells.get(4);
	                cell.setText(tableList.get(i-1)[3]);
            	}else{
	                XWPFTableCell cell = cells.get(0);
	                cell.setText(tableList.get(i-1)[0]);
	                cell = cells.get(2);
	                cell.setText(tableList.get(i-1)[1]);
            	}
            }
        }
    }
    /**
     * 判断文本中时候包含$
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    private static boolean checkText(String text){
        boolean check  =  false;
        if(text.indexOf("$")!= -1){
            check = true;
        }
        return check;

    }

    /**
     * 匹配传入信息集合与模板
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    private static String changeValue(String value, Map<String, String> textMap){
        Set<Entry<String, String>> textSets = textMap.entrySet();
        for (Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${"+textSet.getKey()+"}";
            if(value.indexOf(key)!= -1){
                value = value.replace(key, textSet.getValue());
            }
        }
        //模板未匹配到区域替换为空
        if(checkText(value)){
            value = "";
        }
        return value;
    }
    
    /**
     * 匹配传入信息集合与模板
     * @param cell 模板需要替换的列
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    private static String changeValue(XWPFTableCell cell, Map<String, String> textMap){
    	String value = cell.getText();
    	cell.removeParagraph(0);
    	Set<Entry<String, String>> textSets = textMap.entrySet();
    	for (Entry<String, String> textSet : textSets) {
    		//匹配模板与替换值 格式${key}
    		String key = "${"+textSet.getKey()+"}";
    		if(value.indexOf(key)!= -1){
    			value = value.replace(key, String.valueOf(textSet.getValue()));
    		}
    	}
    	//模板未匹配到区域替换为空
    	if(checkText(value)){
    		value = "";
    	}
    	if(value.indexOf("\n") >= 0){
    		String[] myStrings = value.split("\n");
    		cell.addParagraph();
    		XWPFParagraph para = cell.getParagraphs().get(0);
    		for(String text : myStrings){
    		    XWPFRun run = para.createRun();
    		    run.setText(text.trim());
    		    run.addBreak();
    		}
    	}else{
    		cell.setText(value);
    	}
    	return value;
    }
    /**
	 * @Description: 对象转Map<String,String>
	 * @param data
	 * @return
	 * @throws Exception
	 * @author Xuhw
	 * @date 2017年4月24日 下午1:27:27
	 */
	public static <T> Map<String, String> objToMap(T data)
			throws Exception {
		// 操作对象
		Map<String, String> dataMap = new HashMap<String, String>();
		// 遍历对象字段
		for (Field f : data.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			// 取得字段名
			String key = f.getName();
			Object val = f.get(data);
			// 取得字段值
			String value = "";
			if(val == null){
				value = "";
			}else if(val instanceof String){
				value  = (String) val;
			}else{
				value = val.toString();
			}
			// 加入对象
			dataMap.put(key, value);
		}
		return dataMap;
	}
	/**
	 * 替换模板中的图片
	 * @param document
	 * @param textMap
	 * @param tableList
	 */
	public static void changePicture(CustomXWPFDocument document, Map<String, byte[]> pictureMap) {
		// 获取段落集合
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		int pictureIndex = 0;
		for (XWPFParagraph paragraph : paragraphs) {
			// 判断此段落时候需要进行替换
			String text = paragraph.getText();
			if (text.indexOf("#")!= -1) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					if (run != null && run.toString().indexOf("#")!= -1) {
						String value = run.toString();
						// 替换模板原来位置
						for (Entry<String, byte[]> pictureSet : pictureMap.entrySet()) {
							// 匹配模板与替换值 格式${key}
							String key = "#[" + pictureSet.getKey() + "]";
							if (value.indexOf(key) != -1) {
								value = value.replace(key, "");
								byte[] picture = pictureSet.getValue();
								try {
									document.addPictureData(picture, CustomXWPFDocument.PICTURE_TYPE_JPEG);
									document.createPicture(pictureIndex, PICTURE_WIDTH, PICTURE_HEIGHT, paragraph);
									pictureIndex++;
								} catch (InvalidFormatException e) {
									logger.error(e.toString());
								}
							}
						}
					}
					if(run != null && run.toString().indexOf("#")!= -1){
						run.setText("");
					}
				}
			}
		}
		//获取表格对象集合
		List<XWPFTable> tables = document.getTables();
		for (int i = 0; i < tables.size(); i++) {
			// 只处理行数大于等于2的表格，且不循环表头
			XWPFTable table = tables.get(i);
			if (table.getRows().size() > 1) {
				// 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
				if (table.getText().indexOf("#")!= -1) {
					List<XWPFTableRow> rows = table.getRows();
					for (XWPFTableRow row : rows) {
						List<XWPFTableCell> cells = row.getTableCells();
						for (XWPFTableCell cell : cells) {
							String value = cell.getText();
							if (value != null && value.indexOf("#")!= -1) {
								for (Entry<String, byte[]> pictureSet : pictureMap.entrySet()) {
									// 匹配模板与替换值 格式${key}
									String key = "#[" + pictureSet.getKey() + "]";
									if (value.indexOf(key) != -1) {
										value = value.replace(key, "");
										byte[] picture = pictureSet.getValue();
										try {
											cell.removeParagraph(0);
											XWPFParagraph paragraph = cell.addParagraph();
											document.addPictureData(picture, CustomXWPFDocument.PICTURE_TYPE_JPEG);
											document.createPicture(pictureIndex, PICTURE_WIDTH, PICTURE_HEIGHT, paragraph);
											pictureIndex++;
										} catch (InvalidFormatException e) {
											logger.error(e.toString());
										}
									}
								}
							}
							if (value != null && value.indexOf("#")!= -1) {
								cell.removeParagraph(0);
								cell.addParagraph();
							}
						}
					}
				}
			}
		}
	}

    public static void main(String[] args) {
       //模板文件地址
        String inputUrl = "C:\\Users\\zhihe\\Desktop\\demo\\001.docx";
        //新生产的模板文件
        String outputUrl = "C:\\Users\\zhihe\\Desktop\\demo\\test.docx";

        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("name", "小明");
        testMap.put("sex", "男");
        testMap.put("address", "软件园");
        testMap.put("phone", "88888888");

        List<String[]> testList = new ArrayList<String[]>();
        testList.add(new String[]{"1","1AA","1BB","1CC"});
        testList.add(new String[]{"2","2AA","2BB","2CC"});
        testList.add(new String[]{"3","3AA","3BB","3CC"});
        testList.add(new String[]{"4","4AA","4BB","4CC"});

        ExportWordUtils.changWord(inputUrl, outputUrl, testMap, testList);
    }
}
