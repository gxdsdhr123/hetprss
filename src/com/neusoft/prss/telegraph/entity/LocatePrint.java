/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年1月10日 下午2:12:18
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.telegraph.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neusoft.prss.telegraph.dao.TelegraphHistoryDao;

/**
 * java定位打印，把打印内容打到指定的地方。
 *
 * @author Harvey_Huang
 *
 */
@Service
public class LocatePrint implements Printable {
    
    @Resource
    private TelegraphHistoryDao telegraphHistoryDao;
    private int fontsize = 12;
    private double locx;
    private double locy;
    private List<String>  list;
    private String message="OK";
    Font font = new Font("Arial", Font.PLAIN, fontsize);  //创建字体
    /*
     * Graphic指明打印的图形环境；PageFormat指明打印页格式（页面大小以点为计量单位，
     * 1点为1英寸的1/72，1英寸为25.4毫米。A4纸大致为595×842点）；page指明页号
     */
    public int print(Graphics gp, PageFormat pf, int page)
            throws PrinterException {
        try {
            Graphics2D g2 = (Graphics2D) gp;
            g2.setPaint(Color.black); // 设置打印颜色为黑色
            g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
            //打印起点坐标  
            locx = pf.getImageableX() + 50;  
            locy = pf.getImageableY() + 50;  
            int COUNT = 60;
            g2.setFont(font);//设置字体  
            float heigth = font.getSize2D();//字体高度   
            if(list.size()<(page+1) * COUNT )
                COUNT = list.size() - page * COUNT;
            if(COUNT <=0)
                return NO_SUCH_PAGE;
            for (int i = 0; i < COUNT; i++) {
                if(i!=0 && i% COUNT==0){
                    locy = pf.getImageableY() + 50;
                    return PAGE_EXISTS;
                } 
                
                String text = list.get(page * COUNT + i );
                locy = locy + 1*heigth;
                g2.drawString(text, (float)locx, (float)locy);
//                if(list.size()>= (page * COUNT + i + 1))
//                    return NO_SUCH_PAGE;
            }
            return PAGE_EXISTS;  
        } catch (Exception e) {
            e.printStackTrace();
            return Printable.NO_SUCH_PAGE;
        }   
    }
    // 打印内容到指定位置
    public String printContent(List<String> list,String Printq) {
        this.list=list;
        if (list.size() > 0) // 当打印内容不为空时
        {
            try {
                DocPrintJob job = null;
                // 指定打印输出格式
                DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
                // 定位默认的打印服务
                HashAttributeSet hs = new HashAttributeSet();  
                String printerName=Printq;  
                hs.add(new PrinterName(printerName,null));
//                PrintService[] printService =PrintServiceLookup.lookupPrintServices(null, hs);  
                 PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
                // 创建打印作业
//                if(printService.length>0){
//                    job = printService[0].createPrintJob();
                 if(printService!=null){
                     job = printService.createPrintJob();
                    // 设置打印属性
                    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                    // 设置纸张大小,也可以新建MediaSize类来自定义大小
                    pras.add(MediaSizeName.ISO_A4);
                    DocAttributeSet das = new HashDocAttributeSet();
                    // 指定打印内容
                    Doc doc = new SimpleDoc(this, flavor, das);
                    // 不显示打印对话框，直接进行打印工作
                    job.print(doc, pras); // 进行每一页的具体打印操作
                    //System.err.print("分析标识点5");
                }else{
                    message="E002";
                }
            } catch (PrintException pe) {
                pe.printStackTrace();
                message="E001";
            }
        } else {
            // 如果打印内容为空时，提示用户打印将取消
            JOptionPane.showConfirmDialog(null,
                    "Sorry, Printer Job is Empty, Print Cancelled!",
                    "Empty", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            message="E003";
        }

        return message;
    }
    
    @Transactional(readOnly = true)
    public boolean print(List<Map<String,String>> list){
        boolean result = false;
        List<String> list2 = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Map<String,String> map = list.get(i);
            String text = map.get("TEXT");
            String[] arr = text.split("\n");
            for (int j = 0; j < arr.length; j++) {
                String s = arr[j];
                int k; 
                while(s.length() > 0 ) {
                    k = 60; // 获取每一个回车符的位置
                    String drawText = "";
                    if (s.length()>k)  {
                        drawText = s.substring(0, k); // 获取每一行文本
                        list2.add(drawText);
                        if (s.substring(k + 1).length() > 0){
                            s = s.substring(k + 1); // 截取尚未打印的文本
                        }
                    }  else {
                        drawText = s; // 获取每一行文本
                        list2.add(drawText);
                        s = ""; // 文本已结束
                    }
                } 
            }
            list2.add("\n");
            telegraphHistoryDao.updatePrint(map);
        }
        this.printContent(list2,"报文");
        return result;
    }
}
