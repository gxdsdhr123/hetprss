/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月14日 下午8:56:35
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.common.util.CustomXWPFDocument;
import com.neusoft.prss.common.util.ExportWordUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.flightdynamic.entity.ExportFDExcel;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.produce.service.BillService;

@Controller
@RequestMapping(value = "${adminPath}/produce/bill")
public class BillController extends BaseController {

    @Autowired
    private BillService billService;

    @Autowired
    private FileService fileService;

    /**
     * 
     *Discription:打开列表页.
     *@param model
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年11月14日 Heqg [变更描述]
     */
    @RequestMapping(value = {"list"})
    public String list() {
        return "prss/produce/billList";
    }

    /**
     * 
     *Discription:航线保障收费单列表.
     *@return
     *@return:返回值意义
     *@author:yuzd
     *@update:2018年1月5日yuzd [变更描述]
     */
    @RequestMapping(value = {"alnBillList"})
    public String alnBillList() {
        return "prss/produce/alnBillList";
    }

    /**
     * 
     *Discription:获取列表数据.
     *@param request
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年11月14日 Heqg [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "getBillData")
    public String getBillData() {
        String officeId = UserUtils.getUser().getOffice().getId();
        JobKind jobKind = UserUtils.getCurrentJobKind().get(0);
        JSONArray json = billService.getBillDate(officeId,jobKind);
        String result = json.toJSONString();
        return result;
    }

    /**
     * 
     *Discription:航线保障收费单列表数据.
     *@return
     *@return:返回值意义
     *@author:yuzd
     *@update:2018年1月5日yuzd [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "getAlnBillData")
    public String getAlnBillData() {
        JSONArray json = billService.getAlnBillData();
        String result = json.toJSONString();
        return result;
    }

    /**
     * 
     *Discription:新增单据.
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年11月15日 Heqg [变更描述]
     */
    @RequestMapping(value = {"newBill"})
    public String newBill(Model model) {
        String officeId = UserUtils.getUser().getOffice().getId();
        JSONArray jobType = billService.getJobTypeByOfficeId(officeId);
        model.addAttribute("jobType", jobType);
        return "prss/produce/newBill";
    }

    /**
     * 
     *Discription:新增航线保障收费单.
     *@param model
     *@return
     *@return:返回值意义
     *@author:yuzd
     *@update:2018年1月5日yuzd [变更描述]
     */
    @RequestMapping(value = {"newAlnBill"})
    public String newAlnBill(Model model) {
        String id = billService.getNextAlnBillId();
        model.addAttribute("alnBillId", id);
        // 操作人员
        String officeId = UserUtils.getUser().getOffice().getId();
        List<Map<String, Object>> operators = billService.loadUserList(officeId);
        model.addAttribute("operatorsList", operators);
        return "prss/produce/newAlnBill";
    }

    @ResponseBody
    @RequestMapping(value = "getSelect")
    public String getSelect(String type) {
        JSONArray json = billService.getTypeCodeByJobType(type);
        String result = json.toJSONString();
        return result;
    }

    @RequestMapping(value = "togo")
    public String togo(String jobCode,Model model) {
        if (jobCode.equals("jwbdcbaiduche_free")) {
            return "prss/produce/bdch";
        } else if (
//                jobCode.equals("jwbdckaosite_free") || jobCode.equals("jwbdccanshengche_free")
//                || jobCode.equals("jwqcsbzljc_free") || jobCode.equals("jwqcsbzqsc_free") //垃圾车 	清水车 
//                || jobCode.equals("jwqcsbzwsc_free") // 污水车
//                || jobCode.equals("jwqcczzgnqc_free") || jobCode.equals("jwqcczzgjqc_free")//国内清舱 国际清舱
//                || jobCode.equals("jwqcczzsdqc_free") || jobCode.equals("jwqcczzbp_free")// 深度清舱 补配
//                || jobCode.equals("jwqyctzfj_free") || jobCode.equals("jwqyctfj_free")//牵引车拖拽飞机 牵引车推飞机 
                jobCode.startsWith("jwqyc") //牵引车
                || jobCode.startsWith("jwqccz") //清舱操作
                || jobCode.startsWith("jwbdc") //摆渡车
                || jobCode.startsWith("jwqcsb") //清舱设备
        ) {
            JSONObject json = new JSONObject();
            json.put("TYPE_CODE", jobCode.startsWith("jwqyc")?"jwqyc":jobCode);
            json.put("OPERATOR", UserUtils.getUser().getName());
            model.addAttribute("billInfo", json);
            
            //机型列表
            List<Map<String, Object>> atcactypeList = billService.loadAtcactype();
            model.addAttribute("atcactypeList", atcactypeList);
            
            //操作人员
            String officeId = UserUtils.getUser().getOffice().getId();
            List<Map<String, Object>> operators = billService.loadUserList(officeId);
            model.addAttribute("operatorsList", operators);
            return "prss/produce/kstcshch";
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "addBill") //add
    public String addBill(String bill,String code) {
        JSONObject billJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(bill));
        billJSON.remove("TYPE_CODE");
        billJSON.remove("ID");
        billService.insertBill(billJSON, code);
        return "success";
    }

    /**
     * 航线保障保存
     *Discription:方法功能中文描述.
     *@param bill
     *@param code
     *@return
     *@return:返回值意义
     *@author:yuzd
     *@update:2018年1月8日yuzd [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "saveBill") //add
    public String saveBill(String bill) {
        JSONObject billJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(bill));
//        billJSON.put("operator", UserUtils.getUser().getId());
//        billJSON.put("operatorName", UserUtils.getUser().getName());
//        billJSON.put("operator", UserUtils.getUser().getId());
        billService.saveBill(billJSON);
        return "success";

    }

    /**
     * 
     *Discription:编辑单据.
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年11月15日 Heqg [变更描述] douxf
     */
    @RequestMapping(value = {"editBill"})
    public String editBill(Model model,String id,String type,String tableName) {
        JSONObject json = billService.getBillInfo(id, type,tableName);
        model.addAttribute("billInfo", json);
        model.addAttribute("flag", "edit");
        if (type.equals("jwbdcbaiduche_free")) {
            return "prss/produce/bdch";
        } else if (
//                type.equals("jwbdckaosite_free") || type.equals("jwbdccanshengche_free")
//                || type.equals("jwqcsbzljc_free") || type.equals("jwqcsbzqsc_free") //垃圾车 清水车 
//                || type.equals("jwqcsbzwsc_free")//污水车
//                || type.equals("jwqcczzgnqc_free") || type.equals("jwqcczzgjqc_free")//国内清舱 国际清舱
//                || type.equals("jwqcczzsdqc_free") || type.equals("jwqcczzbp_free")// 深度清舱 补配
//                || type.equals("jwqyctzfj_free") || type.equals("jwqyctfj_free")//牵引车拖拽飞机 牵引车推飞机
                type.startsWith("jwqyc") //牵引车
                || type.startsWith("jwqccz") //清舱操作
                || type.startsWith("jwbdc") //摆渡车
                || type.startsWith("jwqcsb") //清舱设备
                

        ) {
          //机型列表
            List<Map<String, Object>> atcactypeList = billService.loadAtcactype();
            model.addAttribute("atcactypeList", atcactypeList);
            
          //操作人员
            String officeId = UserUtils.getUser().getOffice().getId();
            List<Map<String, Object>> operators = billService.loadUserList(officeId);
            model.addAttribute("operatorsList", operators);
            return "prss/produce/kstcshch";
        }
        return null;
    }

    @RequestMapping(value = {"editAlnBill"})
    public String editAlnBill(Model model,String id) {
        JSONObject json = billService.getAlnBillInfo(id);
        model.addAttribute("billInfo", json);
        model.addAttribute("alnBillId", id);
        // 操作人员
        String officeId = UserUtils.getUser().getOffice().getId();
        List<Map<String, Object>> operators = billService.loadUserList(officeId);
        model.addAttribute("operatorsList", operators);
        model.addAttribute("flag","edit");
        return "prss/produce/newAlnBill";
    }

    /**
     * 
     *Discription:加载图片流.
     *@param res
     *@param id
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年11月16日 Heqg [变更描述]
     */
    @RequestMapping(value = {"outputPicture"})
    public void outputPicture(HttpServletResponse res,String id) {
        OutputStream out = null;
        try {
            byte[] is = fileService.doDownLoadFile(id);
            out = res.getOutputStream();
            out.write(is);
        } catch (Exception e) {
            logger.error("数据流写入失败" + e.getMessage());
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e2) {
                logger.error("输出流关闭失败" + e2.getMessage());
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "save") //upe
    public String save(String bill) {
        JSONObject billJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(bill));
//        billJSON.remove("OPERATOR");
        billJSON.remove("CREATE_DATE");
        billService.updateBill(billJSON);
        return "success";
    }

    /**
     * 
     *Discription:删除单据.
     *@param id
     *@param type
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2017年11月16日 Heqg [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "delBill")
    public String delBill(String id,String type) {
        billService.dellBill(id, type);
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "delAlnBill")
    public String delAlnBill(String id) {
        billService.delAlnBill(id);
        return "success";
    }

    /**
     * 
     * Discription:打印历史消息.
     * 
     * @param model
     * @return
     * @return:
     * @author:douxf
     * @throws URISyntaxException 
     * @update:2017年12月22日 [下载]
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,String id,String type,String title,String tableName) throws URISyntaxException {
    	/*根据Word模板导出 baochl_20180331*/
//    	String separator = SystemPath.getSeparator();
//    	String tempPath = SystemPath.getSysPath() + "WEB-INF"+separator+"template"+separator+type+".docx";
    	String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
    	String tempPath = wiPath + "/template/"+type+".docx";
    	File file = new File(tempPath);
		if (file != null && file.exists()) {
			JSONObject json = billService.getBillInfo(id, type,tableName);
			if (json != null && !json.isEmpty()) {
				OutputStream out = null;
				try {
					@SuppressWarnings("unchecked")
					Map<String,String> textMap = (Map<String, String>) JSON.parse(json.toJSONString());
					CustomXWPFDocument document = ExportWordUtils.change(tempPath, textMap);
					 //签名
		            Map<String,byte[]> pictureMap = new HashMap<String,byte[]>();
		            if(json.containsKey("SIGNATORY")&&json.get("SIGNATORY")!=null){
		            	byte[] signature = fileService.doDownLoadFile(json.getString("SIGNATORY"));
						if (signature != null && signature.length > 0) {
							pictureMap.put("SIGNATORY", signature);
						}
		            }
		            ExportWordUtils.changePicture(document, pictureMap);
		            
		            String fileName = DateUtils.getDate("yyyyMMddHHmmss") + ".docx";
		            setHeader(request,response,fileName);
		            out = response.getOutputStream();
		            document.write(out);
				} catch (IOException e) {
					logger.error(e.toString());
				} finally {
		            try {
		            	if(out!=null){
		            		out.flush();
			                out.close();
		            	}
		            } catch (Exception e) {
		                logger.error("输出流关闭失败" + e.getMessage());
		            }
		        }
			}
			return;
		}
    	/*end 根据Word模板导出 baochl_20180331*/

        if("jwhxbz".equals(type)) {
            JSONObject json = billService.getAlnBillInfo(id);
            try {
                String fileName = "收费单管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
                setHeader(request,response,fileName);
                String excelTitle = "收费单管理" + DateUtils.getDate("yyyy年MM月dd日 E");
                ExportExcel excel = new ExportExcel(excelTitle);
                excel.setHXDataList(json);
                String signatory = json.getString("signatory");//导出图片到excel中
                if(!StringUtils.isBlank(signatory)) {
                    byte[] is = fileService.doDownLoadFile(signatory);
                    excel.exportPic(is);
                }
                excel.write(response);
                excel.dispose();
                return;
            } catch (Exception e) {
                logger.error("收费单管理导出失败" + e.getMessage());
            }
            return;
        }
        title = StringEscapeUtils.unescapeHtml4(title);
        JSONArray titleArray = JSONArray.parseArray(title);
        List<Map<String,String>> dataList = billService.getBillInfoDou(id, type,tableName);
        for (int i = 0; i < dataList.size(); i++) {

            Object IDI = dataList.get(i).get("ID");
            String ID = String.valueOf(IDI);
            Object numI = dataList.get(i).get("NUM");
            String num = String.valueOf(numI);
            dataList.get(i).put("ITEM_DATE", dataList.get(i).get("ITEM_DATE").toString());
            dataList.get(i).put("CREATE_DATE", dataList.get(i).get("CREATE_DATE").toString());
            dataList.get(i).put("START_TM", dataList.get(i).get("START_TM").toString());
            dataList.get(i).put("END_TM", dataList.get(i).get("END_TM").toString());
            dataList.get(i).put("R", ID);
            dataList.get(i).put("NUM", num);
        }
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < titleArray.size(); i++) {
            if (titleArray.getJSONObject(i).getString("title") != null)
                titleList.add(titleArray.getJSONObject(i).getString("title"));
        }
        try {
            String fileName = "收费单管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            setHeader(request,response,fileName);
            String excelTitle = "收费单管理" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportFDExcel excel = new ExportFDExcel(excelTitle, titleList);
            excel.setDataList(titleArray, dataList);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("收费单管理导出失败" + e.getMessage());
        }

    }

	private void setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		try {
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
		}catch(Exception e){
			logger.error("set header error:"+e.toString());
		}
	}
}
