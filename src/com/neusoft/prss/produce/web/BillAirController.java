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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.util.CustomXWPFDocument;
import com.neusoft.prss.common.util.ExportWordUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.entity.BillAirEntity;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.stand.entity.ResultByCus;
import com.neusoft.prss.produce.service.BillAirService;

@Controller
@RequestMapping(value = "${adminPath}/produce/air")
public class BillAirController extends BaseController {
	
	@Autowired
	private BillAirService billAirService;
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value ="list")
	public String list(Model model) {
		return "prss/produce/billAirList";
	}
	
	@ResponseBody
	@RequestMapping(value = "dataList")
	public String getDataList(@RequestParam("dateStart") String dateStart) {
		JSONArray json = billAirService.getDataList(dateStart);
		String result = json.toJSONString();
		return result;
	}
	
	@RequestMapping(value ="add")
	public String add(Model model,String type) {
		JSONArray resTypeArr=billAirService.getResType();
		String userid=UserUtils.getUser().getId();
		String username=UserUtils.getUser().getName();
//		JSONArray userArr=billAirService.getSysUser();
		model.addAttribute("type", type);
		model.addAttribute("restype",resTypeArr);
		model.addAttribute("userid",userid);
		model.addAttribute("username",username);
//		model.addAttribute("userArr",userArr);
		return "prss/produce/billAirForm";
	}
	
	@RequestMapping(value = { "edit" })
	public String edit(Model model,String type,String id) {
	    JSONArray billArr=billAirService.getBillById(id);
	    JSONArray userArr=billAirService.getSysUser();
	    if(billArr.size()==1){
	    	JSONObject result=(JSONObject) billArr.get(0);
	    	model.addAttribute("result",result);
	    }
		model.addAttribute("type", type);
		model.addAttribute("userArr",userArr);
		return "prss/produce/billAirForm";
	}
	
	@ResponseBody
	@RequestMapping(value =  "getFltInfo" )
	public ResultByCus getFltInfo(String flightDate,String flightNumber,String inOutFlag) {
		flightDate=StringEscapeUtils.unescapeHtml4(flightDate);
		flightNumber=StringEscapeUtils.unescapeHtml4(flightNumber);
		inOutFlag=StringEscapeUtils.unescapeHtml4(inOutFlag);
		Map<String,String> param = new HashMap<String,String>();
		param.put("flightDate", flightDate);
		param.put("flightNumber", flightNumber);
		param.put("inOutFlag", inOutFlag);
		JSONArray jsonArr = billAirService.getFltInfo(param);
		ResultByCus result=new ResultByCus();
		if(jsonArr.size()>0){
			if(jsonArr.size()>1){
				//说明有两条  应该再填写进出港状态加以区分
				result.setCode("0002");
				result.setMsg("找到多条记录，请输入进出港状态加以区分");
			}else{
				result.setCode("0001");
				JSONObject json=(JSONObject)jsonArr.get(0);
				json.put("STATION",Global.getConfig("airport_code3"));
				result.setData((JSONObject)jsonArr.get(0));
			}
		}else{
			//没有找到相关记录
			result.setCode("0000");
			result.setMsg("没有找到相关的航班动态");
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "save" )
		public ResultByCus save(BillAirEntity entity) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		String userId=UserUtils.getUser().getId();
		entity.setCreateUser(userId);
		if(entity.getOperator()==null||StringUtils.isEmpty(entity.getOperator().toString()) ){
			entity.setOperator(userId);
		}
		if(entity.getUpdateTime()==null||StringUtils.isEmpty(entity.getUpdateTime().toString()) ){
			entity.setUpdateTime(DateUtils.getDateTime());
		}
		if ("add".equals(entity.getType())){
			flag=billAirService.saveAdd(entity);
		}else if ("edit".equals(entity.getType())){
			flag=billAirService.saveEdit(entity);
		}
		if(flag){
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("1000");
			result.setMsg("操作失败");
		}
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping(value =  "del" )
		public ResultByCus delChargeBill(String id) {
		ResultByCus result=new ResultByCus();
		boolean flag=billAirService.delBillById(id);
		if(flag){
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("1000");
			result.setMsg("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value = "exportWord")
	public void printData(HttpServletRequest request,HttpServletResponse response) throws URISyntaxException{
	 String id=request.getParameter("id");
	 String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
	 String tempPath = "";
	 JSONObject json = (JSONObject) billAirService.getExportWordData(id).get(0);
	 if (json != null && !json.isEmpty()) {
		 OutputStream out = null;
		try {
		@SuppressWarnings("unchecked")
			Map<String,String> textMap = (Map<String, String>) JSON.parse(json.toJSONString());
			 //签名
			Map<String,byte[]> pictureMap = new HashMap<String,byte[]>();
			if(json.containsKey("SIGN")&&json.get("SIGN")!=null){
				byte[] signature = fileService.doDownLoadFile(json.getString("SIGN"));
				if (signature != null && signature.length > 0) {
					pictureMap.put("SIGN", signature);
				}
				tempPath = wiPath + "/template/BillAirServiceTemplet.docx";
			}else{
				tempPath = wiPath + "/template/BillAirServiceSignMarkTemplet.docx";
			}
			CustomXWPFDocument document = ExportWordUtils.change(tempPath, textMap);
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
}

	@RequestMapping(value = "exportExcel")
	public void printList(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate) {
		startDate=StringEscapeUtils.unescapeHtml4(startDate);
		endDate=StringEscapeUtils.unescapeHtml4(endDate);
		String columnName = "[{\"field\":\"FLIGHT_DATE\"},{\"field\":\"FLIGHT_NUMBER\"},{\"field\":\"AIRLINE_SHORTNAME\"},"
				+"{\"field\":\"ACTTYPE_CODE\"}{\"field\":\"AIRCRAFT_NUMBER\"},{\"field\":\"ROUTE_NAME\"},{\"field\":\"FX_FLAG\"},"
				+ "{\"field\":\"PROPERTY\"},{\"field\":\"OPERATOR\"},{\"field\":\"REMARK\"}]";
		String[] title = { "日期", "航班号", "航空公司", "机型","机号","航段", "飞机放行", "航班性质", "操作人", "备注" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			String fileName = "飞机勤务收费单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			excel.setDataList(title, columnArr, billAirService.getDataTotal(startDate,endDate));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("飞机勤务收费单" + e.getMessage());
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
}
