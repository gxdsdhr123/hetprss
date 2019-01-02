package com.neusoft.prss.produce.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
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
import com.neusoft.prss.produce.entity.BillInPickEntity;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.stand.entity.ResultByCus;
import com.neusoft.prss.produce.service.BillInPickService;

@Controller
@RequestMapping(value = "${adminPath}/produce/inPick")
public class BillInPickController extends BaseController{
		
	@Autowired
	private BillInPickService billInPickService;
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value ="list")
	public String list(Model model) {
		return "prss/produce/billInPickList";
	}
	
	@ResponseBody
	@RequestMapping(value = "dataList")
	public String getDataList(@RequestParam("dateStart") String dateStart) {
		JSONArray json = billInPickService.getDataList(dateStart);
		String result = json.toJSONString();
		return result;
	}
	
	@RequestMapping(value ="add")
	public String add(Model model,String type) {
		JSONArray resTypeArr=billInPickService.getResType();
		String userid=UserUtils.getUser().getId();
		String username=UserUtils.getUser().getName();
//		JSONArray userArr=billAirService.getSysUser();
		model.addAttribute("type", type);
		model.addAttribute("restype",resTypeArr);
		model.addAttribute("userid",userid);
		model.addAttribute("username",username);
//		model.addAttribute("userArr",userArr);
		return "prss/produce/billInPickForm";
	}
	
	@RequestMapping(value = { "edit" })
	public String edit(Model model,String type,String id) {
	    JSONArray billArr=billInPickService.getBillById(id);
//	    JSONArray userArr=billInPickService.getSysUser();
	    if(billArr.size()==1){
	    	JSONObject result=(JSONObject) billArr.get(0);
	    	model.addAttribute("result",result);
	    }
		model.addAttribute("type", type);
//		model.addAttribute("userArr",userArr);
		return "prss/produce/billInPickForm";
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
		JSONArray jsonArr = billInPickService.getFltInfo(param);
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
		public ResultByCus save(BillInPickEntity entity) {
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		String userName=UserUtils.getUser().getName();
		entity.setCreateUser(userName);
		if(entity.getOperatorName()==null||StringUtils.isEmpty(entity.getOperatorName().toString()) ){
			entity.setOperatorName(userName);
		}
		if(entity.getUpdateTm()==null||StringUtils.isEmpty(entity.getUpdateTm().toString()) ){
			entity.setUpdateTm(DateUtils.getDateTime());
		}
		if ("add".equals(entity.getType())){
			flag=billInPickService.saveAdd(entity);
		}else if ("edit".equals(entity.getType())){
			flag=billInPickService.saveEdit(entity);
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
		boolean flag=billInPickService.delBillById(id);
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
    	String tempPath =  wiPath + "/template/BillInPickServiceTemplet.docx";
    	File file = new File(tempPath);
		if (file != null && file.exists()) {
			JSONObject json = (JSONObject) billInPickService.getExportWordData(id).get(0);
			if (json != null && !json.isEmpty()) {
				OutputStream out = null;
				try {
					@SuppressWarnings("unchecked")
					Map<String,String> textMap = (Map<String, String>) JSON.parse(json.toJSONString());
					CustomXWPFDocument document = ExportWordUtils.change(tempPath, textMap);

					Map<String,byte[]> pictureMap = new HashMap<>();

					byte[] is;
					try {
						String fwySign = json.getString("FWY_SIGN");
						if(null!=fwySign) {
							is = fileService.doDownLoadFile(fwySign);
							pictureMap.put("FWY_SIGN", is);
						}

						String zpsjSign = json.getString("ZPSJ_SIGN");
						if(null!=zpsjSign) {
							is = fileService.doDownLoadFile(zpsjSign);
							pictureMap.put("ZPSJ_SIGN", is);
						}

						String jjrSign = json.getString("JJR_SIGN");
						if(null!=jjrSign) {
							is = fileService.doDownLoadFile(jjrSign);
							pictureMap.put("JJR_SIGN", is);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

					/*JSONArray fileList = billInPickService.getFileID(id);
					for(int j=0;j<fileList.size();j++) {
						JSONObject fileObj = (JSONObject) fileList.get(j);
						String pId = fileObj.getString("FILE_ID");
						String pType = fileObj.getString("FILE_TYPE");
						String media = "PICTURE";
						if("2".equals(pType)) {

							media = "VIDEO";
						} else if("3".equals(pType)) {

							media = "SOUND";
						}

						if (!StringUtils.isEmpty(pId)) {
							// 获取字节
							try {
								byte[] is = fileService.doDownLoadFile(pId);
								if (is != null && is.length > 0) {
									pictureMap.put(media, is);
								}
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}*/

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
 }

	@RequestMapping(value = "exportExcel")
	public void printList(HttpServletRequest request, HttpServletResponse response,String startDate,String endDate) {
		startDate=StringEscapeUtils.unescapeHtml4(startDate);
		endDate=StringEscapeUtils.unescapeHtml4(endDate);
		String columnName = "[{\"field\":\"FLIGHT_DATE\"},{\"field\":\"FLIGHT_NUMBER\"},{\"field\":\"AIRLINE_SHORTNAME\"},"
				+ "{\"field\":\"ACTTYPE_CODE\"},{\"field\":\"AIRCRAFT_NUMBER\"},{\"field\":\"ROUTE\"},{\"field\":\"FLT_TYPE\"},{\"field\":\"OPERATOR_NAME\"}]";
		String[] title = { "日期", "航班号","航空公司","机型","机号", "航程","航班类型","操作人"};
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			String fileName = "进港航班接机核对表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			List<Map<String ,String>> dataList = billInPickService.getDataTotal(startDate,endDate);
			excel.setDataList(title, columnArr, dataList);
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			logger.error("进港航班接机核对表" + e.getMessage());
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
		/**
		 * 下载附件
		 */
		@ResponseBody
		@RequestMapping(value = "downloadAttachment")
		public void downloadAttachment(HttpServletRequest request, HttpServletResponse response) {
			OutputStream out = null;
			String attachmentId = "";
			String attachmentName = "";
			try {
				//attachmentId,attachmentName 文件id，文件名称
				String downFileInfo = request.getParameter("downFileInfo");
				if(downFileInfo!=null&&!downFileInfo.equals("")){
					String[] tmpArr = downFileInfo.split(",");
					attachmentId = tmpArr[0];
					attachmentName = tmpArr[1];
				}
				if(attachmentName==null||attachmentName.equals("")){
					attachmentName = "附件";
				}
				if (attachmentId != null&&!attachmentId.equals("")) {
					response.reset();// 重设response信息，解决部分浏览器文件名非中文问题
					response.setContentType("application/octet-stream; charset=utf-8");
					out = response.getOutputStream();
					byte[] content = fileService.doDownLoadFile(attachmentId);
					String agent = (String) request.getHeader("USER-AGENT");
					try {
						if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
							attachmentName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(attachmentName.getBytes("UTF-8"))))
									+ "?=";
						} else {
							attachmentName = java.net.URLEncoder.encode(attachmentName, "UTF-8");
						}
					} catch (Exception e) {
						logger.error("response信息设置失败" + e.getMessage());
					}
					response.setHeader("Content-Disposition", "attachment; filename=" + attachmentName);
					out.write(content);
					out.flush();
				}
			} catch (Exception e) {
				logger.error("数据流写入失败" + e.getMessage());
			} finally {
				try {
					if(out!=null)
						out.close();
				} catch (Exception e2) {
					logger.error("输出流关闭失败" + e2.getMessage());
				}
			}
		}
	}
