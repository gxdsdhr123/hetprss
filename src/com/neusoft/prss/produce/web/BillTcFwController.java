/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年6月22日 上午8:56:35
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.service.BillTcFwService;

/**
 * 特车计费、计费详情单据
 */
@Controller
@RequestMapping(value = "${adminPath}/produce/billTcFw")
public class BillTcFwController extends BaseController {
	
	@Autowired
	private BillTcFwService BillTcFwService;
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 特车计费列表页
	 */
	@RequestMapping(value ="list")
	public String list(Model model) {
		return "prss/produce/billTcFwList";
	}
	
	/**
	 * 特车计费新增,修改、查看页
	 */
	@RequestMapping(value="toDetailForm")
	public String toDetailForm(Model model,String id,String operateType,String fltDate){
		JSONObject data = null;
		JSONArray dataList = null;
		//modify修改、查看 add新增
		model.addAttribute("operateType", operateType);
		if(operateType!=null&&operateType.equals("modify")){
			//修改、查看
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ids", id);
			paramMap.put("fltDate", fltDate);
			data = BillTcFwService.getBillTcFwList(paramMap);
			if(data!=null){
				dataList = data.getJSONArray("rows");
				if(dataList!=null&&dataList.size()==1){
					data = dataList.getJSONObject(0);
				}
			}
		}else if(operateType!=null&&operateType.equals("add")){
			//新增
			data = new JSONObject();
		}
		model.addAttribute("modifyData", data);
		return "prss/produce/billTcFwDetailForm";
	}
	
	/**
	 * 获取特车计费列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "getData")
	public JSONObject getData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String pageNumber = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		String fltDate = request.getParameter("fltDate");
		String searchValue = request.getParameter("searchValue");
		if(searchValue!=null){
			searchValue = searchValue.toUpperCase();
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			int begin = 1;
			int end = 21;
			if(pageNumber!=null&&!pageNumber.equals("")&&pageSize!=null&&!pageSize.equals("")){
				int intPageNum = Integer.parseInt(pageNumber);
				int intPageSize = Integer.parseInt(pageSize);
				begin=(intPageNum - 1) * intPageSize+1;
		        end=intPageNum * intPageSize+1;
			}
			paramMap.put("begin", begin);
			paramMap.put("end", end);
			paramMap.put("fltDate", fltDate);
			paramMap.put("fltNo", searchValue);
			rs = BillTcFwService.getBillTcFwList(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
        return rs;
	}
	
	/**
	 * 获取航班详细信息
	 */
	@RequestMapping(value="flightDetail")
	@ResponseBody
	public JSONObject flightDetail(HttpServletRequest request){
		JSONObject reFltDetailData = new JSONObject();
		String flightDate = request.getParameter("flightDate");
		String inFlightNumber = request.getParameter("inFlightNumber");
		String outFlightNumber = request.getParameter("outFlightNumber");
		if(inFlightNumber!=null){
			inFlightNumber = inFlightNumber.toUpperCase();
		}
		if(outFlightNumber!=null){
			outFlightNumber = outFlightNumber.toUpperCase();
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("inFlightNumber", inFlightNumber);
			param.put("outFlightNumber", outFlightNumber);
			param.put("flightDate", flightDate);
			param.put("operatorName", UserUtils.getUser().getName());
			//查询是否已经存在此航班计费单
			int countBill = BillTcFwService.getBillTcFwCount(param);
			if(countBill!=0){
				//已经存在此航班计费单
				reFltDetailData.put("result", "allreadyHaveBill");
			}else{
				//查找航班信息
				JSONObject fltDetailData = BillTcFwService.getFlightDetail(param);
				if(fltDetailData!=null){
					reFltDetailData.put("fltDetailData", fltDetailData);
					reFltDetailData.put("result", "found");
				}else{
					reFltDetailData.put("result", "notfound");
				}
			}
		} catch (Exception e) {
			reFltDetailData.put("result", "err");
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return reFltDetailData;
	}
	
	/**
	 * 获取服务单号
	 */
	@RequestMapping(value="serviceNumber")
	@ResponseBody
	public String getServiceNumber(){
		return BillTcFwService.getServiceNumber();
	}
	
	/**
	 * 保存特车计费单
	 */
	@ResponseBody
	@RequestMapping(value ="save")
	public String save(HttpServletRequest request) {
		String result = "success";
		//modify更新 add新增
		String operateType = request.getParameter("operateType");
		String id = request.getParameter("id");
		
		String flightDate = request.getParameter("flightDate");
		String inFlightNumber = request.getParameter("inFlightNumber");
		String outFlightNumber = request.getParameter("outFlightNumber");
		if(operateType.equals("modify")){
			flightDate = request.getParameter("flightDateUnedit");
			inFlightNumber = request.getParameter("inFlightNumberUnedit");
			outFlightNumber = request.getParameter("outFlightNumberUnedit");
		}
		
		String inFltId = request.getParameter("inFltId");
		String outFltId = request.getParameter("outFltId");
		String inFlightDate = request.getParameter("inFlightDate");
		String outFlightDate = request.getParameter("outFlightDate");
		String serviceStart = request.getParameter("serviceStart");
		String serviceEnd = request.getParameter("serviceEnd");
		String serviceNumber = request.getParameter("serviceNumber");
		String serviceCode = request.getParameter("serviceCode");
		String qianyin = request.getParameter("qianyin");
		String cansheng = request.getParameter("cansheng");
		String chuansong = request.getParameter("chuansong");
		String lkbdcIn = request.getParameter("lkbdcIn");
		String lkbdcOut = request.getParameter("lkbdcOut");
		String jzbdcIn = request.getParameter("jzbdcIn");
		String jzbdcOut = request.getParameter("jzbdcOut");
		String laji = request.getParameter("laji");
		String qingshui = request.getParameter("qingshui");
		String wushui = request.getParameter("wushui");
		String ktcIn = request.getParameter("ktcIn");
		String ktcOut = request.getParameter("ktcOut");
		String ptcIn = request.getParameter("ptcIn");
		String ptcOut = request.getParameter("ptcOut");
		String qiyuan = request.getParameter("qiyuan");
		String dianyuan = request.getParameter("dianyuan");
		String kongtiao = request.getParameter("kongtiao");
		String remark = request.getParameter("remark");
		String chubing = request.getParameter("chubing");
		
		if(inFlightNumber!=null){
			inFlightNumber = inFlightNumber.toUpperCase();
		}
		if(outFlightNumber!=null){
			outFlightNumber = outFlightNumber.toUpperCase();
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("operateType", operateType);
			param.put("id", id);
			param.put("flightDate", flightDate);
			param.put("inFltId", inFltId);
			param.put("outFltId", outFltId);
			param.put("inFlightDate", inFlightDate);
			param.put("outFlightDate", outFlightDate);
			param.put("serviceStart", serviceStart);
			param.put("serviceEnd", serviceEnd);
			param.put("serviceNumber", serviceNumber);
			param.put("serviceCode", serviceCode);
			param.put("inFlightNumber", inFlightNumber);
			param.put("outFlightNumber", outFlightNumber);
			param.put("qianyin", qianyin);
			param.put("cansheng", cansheng);
			param.put("chuansong", chuansong);
			param.put("lkbdcIn", lkbdcIn);
			param.put("lkbdcOut", lkbdcOut);
			param.put("jzbdcIn", jzbdcIn);
			param.put("jzbdcOut", jzbdcOut);
			param.put("laji", laji);
			param.put("qingshui", qingshui);
			param.put("wushui", wushui);
			param.put("ktcIn", ktcIn);
			param.put("ktcOut", ktcOut);
			param.put("ptcIn", ptcIn);
			param.put("ptcOut", ptcOut);
			param.put("qiyuan", qiyuan);
			param.put("dianyuan", dianyuan);
			param.put("kongtiao", kongtiao);
			param.put("remark", remark);
			param.put("chubing", chubing);
			param.put("operator", UserUtils.getUser().getId());
			int countSave = BillTcFwService.saveBillTcFw(param);
			if(countSave==0){
				result = "non";
			}
		} catch (Exception e) {
			result = "err";
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 删除特车计费单
	 */
	@ResponseBody
	@RequestMapping(value="delete")
	public String delete(HttpServletRequest request) {
		String result = "success";
		int countDel = 0;
		String id = request.getParameter("id");
		String inFltId = request.getParameter("inFltId");
		String outFltId = request.getParameter("outFltId");
		String fltDate = request.getParameter("fltDate");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try{
			if(id!=null&&!id.equals("")){
				paramMap.put("ids", id);
			}
			if(inFltId!=null&&!inFltId.equals("")){
				paramMap.put("inFltId", inFltId);
			}
			if(outFltId!=null&&!outFltId.equals("")){
				paramMap.put("outFltId", outFltId);
			}
			if(paramMap.keySet().size()>0){
				paramMap.put("fltDate", fltDate);
				countDel = BillTcFwService.deleteBillTcFw(paramMap);
			}
			if(countDel==0){
				result = "nonDel";
			}
		} catch (Exception e) {
			result = "err";
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 导出特车计费单
	 */
	@ResponseBody
	@RequestMapping(value = "exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
		String fltDate = request.getParameter("fltDate");
		String fltNo = request.getParameter("fltNo");
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	try {
    		//export导出列表  print导出详情
    		paramMap.put("operatType", "export");
        	paramMap.put("fltDate", fltDate);
        	paramMap.put("fltNo", fltNo);
        	JSONObject rs = BillTcFwService.getBillTcFwList(paramMap);
        	JSONArray rows = rs.getJSONArray("rows");
        	paramMap.put("exportData", rows);
        	byte[] content = BillTcFwService.exportExcel(paramMap);
   			String fileName = "特车计费单-列表" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
   			String downloadFileName = setHeader(request, response, fileName);
			response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
			response.getOutputStream().write(content);
            response.getOutputStream().flush();
		} catch (Exception e) {
			logger.error("打印特车计费单导出失败" + e.getMessage());
		}
	}
	
	/**
	 * 打印特车计费单
	 */
	@ResponseBody
	@RequestMapping(value = "printExcel")
	public void printExcel(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids");
		String fltDate = request.getParameter("fltDate");
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	try {
    		if(ids!=null&&!ids.equals("")){
    			String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
            	//特车计费单模板
    			String tempPath = wiPath + "/template/billTcFwTemplet.xlsx";
    			byte[] templetByteArr = getBytes(tempPath);
            	//export导出列表  print导出详情
        		paramMap.put("operatType", "print");
            	paramMap.put("fileTempletByteArr", templetByteArr);
            	paramMap.put("ids", ids);
            	paramMap.put("fltDate", fltDate);
            	JSONObject rs = BillTcFwService.getBillTcFwList(paramMap);
            	JSONArray rows = rs.getJSONArray("rows");
            	paramMap.put("printData", rows);
            	byte[] content = BillTcFwService.exportExcel(paramMap);
       			String fileName = "特车计费单-详情" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
       			String downloadFileName = setHeader(request, response, fileName);
    			response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
    			response.getOutputStream().write(content);
                response.getOutputStream().flush();
    		}
		} catch (Exception e) {
			logger.error("打印特车计费单导出失败" + e.getMessage());
		}
	}
	 
	private String setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		String downloadFileName = "";
		try {
			response.reset();
	        response.setContentType("application/octet-stream; charset=utf-8");
	        String agent = (String) request.getHeader("USER-AGENT");
	        if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
	            downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
	        } else {
	            downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
		}catch(Exception e){
			logger.error("set header error:"+e.toString());
		}
		return downloadFileName;
	}

	@RequestMapping(value = { "outputPicture" })
	public void outputPicture(HttpServletResponse res, String id){
		OutputStream out = null;
        try {
        	byte[] is = fileService.doDownLoadFile(id);
        	out = res.getOutputStream();
            out.write(is);
		} catch (Exception e) {
			logger.error("数据流写入失败"+e.getMessage());
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception e2) {
				logger.error("输出流关闭失败"+e2.getMessage());
			}
		}
	}
	
	/** 
     * 获得指定文件的byte数组 
     */  
    private byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
}
