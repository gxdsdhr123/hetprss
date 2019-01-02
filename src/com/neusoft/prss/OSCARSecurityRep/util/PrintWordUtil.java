package com.neusoft.prss.OSCARSecurityRep.util;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;  
/**
 * 打印world
 * @author lwg
 *
 */
public class PrintWordUtil {
	// 行数指针
	private int rowIndex = 0;
	// 列数
	private int colIndex = 0;
	
	@SuppressWarnings("unchecked")
	public XWPFDocument createSimpleTable(List<Map<String, Object>> list) throws Exception {  
		Map<String, Object> listMap = list.get(0);
		
		
        XWPFDocument xdoc = new XWPFDocument();  
        XWPFParagraph xp = xdoc.createParagraph();  
        XWPFRun r1 = xp.createRun();  
        // 标题
        r1.setText(listMap.get("header")+"");  
        r1.setFontFamily("宋体");  
        r1.setFontSize(16);  
        r1.setTextPosition(10);  
        r1.setBold(true);  
        xp.setAlignment(ParagraphAlignment.CENTER);  
        
        // ====================================航班信息区域==================================================
        // 头部
        List<Map<String, Object>> headList = (List<Map<String, Object>>) listMap.get("headList");
        int headRow = headList.size();
        rowIndex = headRow;
        List<Map<String, Object>> colMap = (List<Map<String, Object>>) headList.get(0);
        int headCol = colMap.size()*2;
        colIndex = headCol;
        XWPFTable hTable = xdoc.createTable(headRow, headCol);  
        CTTbl hTtbl = hTable.getCTTbl();  
        CTTblPr hTblPr = hTtbl.getTblPr() == null ? hTtbl.addNewTblPr() : hTtbl.getTblPr();  
        CTTblWidth htblWidth = hTblPr.isSetTblW() ? hTblPr.getTblW() : hTblPr.addNewTblW();  
        htblWidth.setW(new BigInteger("8600"));  
        htblWidth.setType(STTblWidth.DXA);
        List<Map<String, Object>> colItemMap = null;
        String colItemValue = "";
        for(int i = 0; i < headRow; i++) {
        	colItemMap = (List<Map<String, Object>>) headList.get(i);
        	for(int j = 0, k = 0; j < headCol && k < headCol/2; j++) {
        		if(j % 2 == 0) {        			
        			setCellText(xdoc, hTable.getRow(i).getCell(j), colItemMap.get(k).get("name")+"", "CCCCCC", getCellWidth(1));  
        		} else {
        			if(null != listMap.get(colItemMap.get(k).get("value"))) {
        				colItemValue = listMap.get(colItemMap.get(k).get("value")) + "";
        			} else {
        				colItemValue = "";
        			}
        			setCellText(xdoc, hTable.getRow(i).getCell(j), colItemValue, null, getCellWidth(0));       
        			k++;
        		}

        	}
        }
        // =============================================检查项目=======================================================
        // 检查项目
        List<Map<String, Object>> itemHeadList =  (List<Map<String, Object>>) listMap.get("itemHeadList");
        List<Map<String, Object>> itemListArray = (List<Map<String, Object>>) listMap.get("itemList");
        
	    XWPFTableRow row = hTable.insertNewTableRow(rowIndex++);  
	    XWPFTableCell cell = null;  
	    CTTc cttc = null;  
	    CTTcPr cellPr = null;  

		for(int i = 0; i < itemHeadList.size(); i++) {			
			cell = row.addNewTableCell();  
			cttc = cell.getCTTc();  
			cellPr = cttc.addNewTcPr();  
			if("检查项目".equals(itemHeadList.get(i).get("name") + "")) {				
				cellPr.addNewGridSpan().setVal(BigInteger.valueOf(3));  
			}
			cellPr.addNewHMerge().setVal(STMerge.CONTINUE);  
			cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
			cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);  
			cttc.getPList().get(0).addNewR().addNewT().setStringValue(itemHeadList.get(i).get("name") + ""); 
			cell.setColor("CCCCCC");  
		}
		
		XWPFTableRow itemRow;
		List<Map<String, Object>> itemList = null;
		for(int i = 0; i < itemListArray.size(); i++) {
			itemList = (List<Map<String, Object>>) itemListArray.get(i);
			itemRow = hTable.insertNewTableRow(rowIndex++);  
			for(int j = 0; j < itemList.size(); j++) {		
				Map<String, Object> map = itemList.get(j);
				if(null == map.get("sort")) {
					break;
				}
				for(int k = 0; k < itemList.size(); k++) {
					map = itemList.get(k);
					if(j == Integer.valueOf(map.get("sort").toString())) {					
						cell = itemRow.addNewTableCell();  
						cttc = cell.getCTTc();  
						cellPr = cttc.addNewTcPr();  
						if("ITEMNAME".equals(itemList.get(k).get("name") + "")) {				
							cellPr.addNewGridSpan().setVal(BigInteger.valueOf(3));  
						}
						cellPr.addNewHMerge().setVal(STMerge.CONTINUE);  
						cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
						cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);  
						cttc.getPList().get(0).addNewR().addNewT().setStringValue(itemList.get(k).get(itemList.get(k).get("name")) + ""); 
					}
				}
			}
		}
		
		// =============================================备注区域==============================================
		// 备注表头
		XWPFTableRow remarkHeadRow;
		List<Map<String, Object>> remarkHeadList =  (List<Map<String, Object>>) listMap.get("remarkHeadList");
		for(int i = 0; i < remarkHeadList.size(); i++) {			
			remarkHeadRow = hTable.insertNewTableRow(rowIndex++); 
			cell = remarkHeadRow.addNewTableCell();  
			cttc = cell.getCTTc();  
			cellPr = cttc.addNewTcPr();  
			if("备注".equals(remarkHeadList.get(i).get("name") + "")) {				
				cellPr.addNewGridSpan().setVal(BigInteger.valueOf(colIndex));  
			}
			cellPr.addNewHMerge().setVal(STMerge.CONTINUE);  
			cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
			cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);  
			cttc.getPList().get(0).addNewR().addNewT().setStringValue(remarkHeadList.get(i).get("name") + ""); 
			cell.setColor("CCCCCC");  
		}
		// 备注内容区域
		XWPFTableRow remarkRow;
		List<Map<String, Object>> remarkListArray =  (List<Map<String, Object>>) listMap.get("remarkList");
		List<Map<String, Object>> remarkList = null;
		for(int i = 0; i < remarkListArray.size(); i++) {
			remarkList = (List<Map<String, Object>>) remarkListArray.get(i);
			remarkRow = hTable.insertNewTableRow(rowIndex++);  
			for(int j = 0; j < remarkList.size(); j++) {		
				Map<String, Object> map = remarkList.get(j);
				
				if(null != map.get("sort")) {
					for(int k = 0; k < remarkList.size(); k++) {
						map = remarkList.get(k);
						if(j == Integer.valueOf(map.get("sort").toString())) {					
							cell = remarkRow.addNewTableCell();  
							cttc = cell.getCTTc();  
							cellPr = cttc.addNewTcPr();  
							if("REMARKTEXT".equals(remarkList.get(k).get("name") + "")) {				
								cellPr.addNewGridSpan().setVal(BigInteger.valueOf(4));  
							}
							cellPr.addNewHMerge().setVal(STMerge.CONTINUE);  
							cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
							cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);  
							cttc.getPList().get(0).addNewR().addNewT().setStringValue(remarkList.get(k).get(remarkList.get(k).get("name")) + ""); 
						}
					}
				} 
			}
		}


        CTRPr rpr= r1.getCTR().isSetRPr() ?  r1.getCTR().getRPr() : r1.getCTR().addNewRPr();  
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();  
        fonts.setAscii("黑体");  
        fonts.setEastAsia("黑体");  
        fonts.setHAnsi("黑体");  
          
//        String savePath = "d:/test/测试---"+ System.currentTimeMillis() + ".docx";
//        FileOutputStream fos = new FileOutputStream(savePath);  
//        xdoc.write(fos);  
//        fos.close();  
        return xdoc;
    }  
  
    private static void setCellText(XWPFDocument xDocument, XWPFTableCell cell,  String text, String bgcolor, int width) {  
        CTTc cttc = cell.getCTTc();  
        CTTcPr cellPr = cttc.addNewTcPr();  
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));  
        cell.setColor(bgcolor);  
        cell.setVerticalAlignment(XWPFVertAlign.CENTER);  
        CTTcPr ctPr = cttc.addNewTcPr();  
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);  
        cell.setText(text);  
  
    }  
  
    private static int getCellWidth(int index) {  
        int cwidth = 1000;  
        if (index == 0) {  
            cwidth = 1600;  
        } else if (index == 1) {  
            cwidth = 3000;  
        } else if (index == 2) {  
            cwidth = 1200;  
        } else if (index == 3) {  
            cwidth = 900;  
        } else if (index == 4) {  
            cwidth = 600;  
        } else if (index == 5) {  
            cwidth = 600;  
        } else if (index == 6) {  
            cwidth = 700;  
        }  
        return cwidth;  
    }   
}
