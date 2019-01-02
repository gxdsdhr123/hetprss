package com.neusoft.prss.plan.web;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.plan.service.SpecialPlanService;

/**
 * 公务机通航计划
 */
@Controller
@RequestMapping(value = "${adminPath}/plan/specialPlan")
public class SpecialPlanController extends BaseController{
	@Autowired
	private SpecialPlanService specialPlanService;
	
	@Autowired
	private CacheService cacheService;
	
	@Resource
	private FileService fileservice;
	
	@RequestMapping(value = "mainPage")
	public String jumpMainPage(Model mode){
		return "prss/plan/specialPlanList";
	}
	
	@RequestMapping(value = "recyclePage")
	public String jumpRecyclePage(Model mode){
		return "prss/plan/specialPlanRecycle";
	}
	
	/**
	 * 跳转公务机/通航新增页
	 */
	@RequestMapping(value = "form")
	public String jumpFormPage(Model model,String ids,String operateType){
		model = selectOptionData(model);
		//add新增 update更新新增
    	model.addAttribute("operateType",operateType);
    	if(ids!=null&&!ids.equals("")){
    		Map<String, Object> paramMap = new HashMap<String, Object>();
    		paramMap.put("ids", ids);
    		JSONArray dataRows = specialPlanService.getSpecialPlanByIds(paramMap);
    		model.addAttribute("dataRows",dataRows);
    	}else{
    		model.addAttribute("dataRows","");
    	}
		return "prss/plan/specialPlanAdd";
	}
	
	/**
	 * 跳转公务机/通航预测页
	 */
	@RequestMapping(value = "forecastPage")
	public String jumpForecastPage(Model mode){
		mode = selectOptionData(mode);
		return "prss/plan/specialPlanForecast";
	}
	
	/**
	 * 查询公务机/通航计划列表数据
	 */
	@RequestMapping(value = "gridData")
	@ResponseBody
	public JSONObject gridData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String pageNumber = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		String planStatus = request.getParameter("planStatus");
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
			paramMap.put("planStatus", planStatus);
			rs = specialPlanService.getSpecialPlan(paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
        return rs;
	}
	
	/**
	 * 保存更新公务机/通航计划
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public String savePlan(HttpServletRequest request){
		int countSuccess = 0;
		JSONArray saveDataArr = new JSONArray();
		String saveData = request.getParameter("saveData");
		String operateType = request.getParameter("operateType");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			if(saveData!=null&&!saveData.equals("")){
				saveDataArr = JSON.parseArray(saveData);
				for(int i=0;i<saveDataArr.size();i++){
					JSONObject saveDataObj = saveDataArr.getJSONObject(i);
					String attachmentId = saveDataObj.getString("attachmentId");
					String tmpAttachmentId = saveDataObj.getString("tmpAttachmentId");
					String tmpAttachmentName = saveDataObj.getString("tmpAttachmentName");
					//处理上传文件
					if(operateType.equals("add")){
						//新增保存，有上传数据
						if(tmpAttachmentId!=null&&!tmpAttachmentId.equals("")){
							saveDataObj.put("attachmentId", tmpAttachmentId);
							saveDataObj.put("attachmentName", tmpAttachmentName);
						}
					}else if(operateType.equals("update")){
						//更新保存，有新上传数据
						if(tmpAttachmentId!=null&&!tmpAttachmentId.equals("")){
							if(attachmentId!=null&&!attachmentId.equals("")){
								//删除旧版文件
								fileservice.doDeleteFile(attachmentId);
							}
							saveDataObj.put("attachmentId", tmpAttachmentId);
							saveDataObj.put("attachmentName", tmpAttachmentName);
						}
					}
				}
			}
			
			//保存计划
			paramMap.put("saveDataArr", saveDataArr);
			paramMap.put("userId", UserUtils.getUser().getId());
			paramMap.put("operateType", operateType);
			countSuccess = specialPlanService.saveSpecialPlan(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			return "error";
		}
		if(countSuccess>0) {
			return "success";
	    }else {
			return "fail";
		}
	}
	
	/**
	 * 移动公务机/通航计划到回收站
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String deletePlan(HttpServletRequest request){
		int countSuccess = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			String ids = request.getParameter("ids");
			params.put("ids", ids);
			params.put("planStatus", 2);
			params.put("userId", UserUtils.getUser().getId());
			countSuccess = specialPlanService.delSpecialPlan(params);
		} catch (Exception e) {
			logger.error(e.toString());
			return "error";
		}
		if(countSuccess>0) {
			return "success";
	    }else {
			return "fail";
		}
	}
	
	/**
	 * 上传附件
	 */
	@ResponseBody
    @RequestMapping(value = "uploadAttachment")
    public JSONObject uploadAttachment(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file) {
		// {"result":"succeed","info":"FileId"}
		// {"result":"fail", "info":"失败原因"}
		JSONObject res = new JSONObject();
		String oldFileId = request.getParameter("oldFileId");
		try {
			//删除旧版文件
			if(oldFileId!=null&&!oldFileId.equals("")){
				fileservice.doDeleteFile(oldFileId);
			}
			//保存新版文件，计划上传文件配置fileTypeId是71，对应sys_upload_file_type
			if(file!=null){
				res = fileservice.doUploadFile(file.getBytes(), "71", UserUtils.getUser().getId(),file.getOriginalFilename());
				res.put("fileName", file.getOriginalFilename());
			}
		} catch (Exception e) {
        	res.put("result", "error");
            logger.error("上传附件异常" + e.getMessage());
        }
		return res;
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
				byte[] content = fileservice.doDownLoadFile(attachmentId);
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
	
	/**
	 * 恢复删除公务机/通航计划
	 */
	@RequestMapping(value = "operate")
	@ResponseBody
	public String operatePlan(HttpServletRequest request){
		int countSuccess = 0;
		String ids = request.getParameter("ids");
		String attachmentIds = request.getParameter("attachmentIds");
		String operateType = request.getParameter("operateType");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			paramMap.put("ids", ids);
			paramMap.put("operateType", operateType);
			paramMap.put("userId", UserUtils.getUser().getId());
			countSuccess = specialPlanService.operateSpecialPlan(paramMap);
			if(countSuccess>0&&operateType.equals("remove")&&attachmentIds!=null){
				//彻底删除上传的文件
				String[] tmpAttachmentIdsArr = attachmentIds.split(",");
				for(String attachmentId : tmpAttachmentIdsArr){
					//删除旧版文件
					if(attachmentId!=null&&!attachmentId.equals("")){
						fileservice.doDeleteFile(attachmentId);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return "error";
		}
		if(countSuccess>0) {
			return "success";
	    }else {
			return "fail";
		}
	}
	
	/**
	 * 删除取消、删除行、关闭操作列表中上传的文件
	 */
	@RequestMapping(value = "delTmpAttachmentFile")
	@ResponseBody
	public String delTmpAttachmentFile(HttpServletRequest request){
		String tmpAttachmentIds = request.getParameter("tmpAttachmentIds");
		try {
			if(tmpAttachmentIds!=null&&!tmpAttachmentIds.equals("")){
				//删除临时上传的文件
				String[] tmpAttachmentIdsArr = tmpAttachmentIds.split(",");
				for(String attachmentId : tmpAttachmentIdsArr){
					//删除旧版文件
					if(attachmentId!=null&&!attachmentId.equals("")){
						fileservice.doDeleteFile(attachmentId);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return "error";
		}
		return "success";
	}
	
	/**
	 * 从缓存中获取机场三字码、名称，航空公司三字码、名称，公务机通航性质编码
	 */
	public Model selectOptionData(Model mode) {
		// 机场代码(代码使用三字码)、名称
		JSONArray airportCodeSource = cacheService.getOpts("dim_airport", "airport_code","description_cn","icao_code");
		// 航空公司代码(代码使用三字码)、名称
		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname","airline_code");
		// 公务机通航性质编码
		JSONArray propertyCodeSourceData = cacheService.getOpts("dim_flight_property", "property_code","property_shortname");
		
        //公务机通航属性包含： D/Y,G/F,X/L,N/M,P/F,U/H,Z/X,Y/H
		JSONArray propertyCodeSource = new JSONArray();
		for(int i=0;i<propertyCodeSourceData.size();i++){
        	String allPropertyCode = "D/Y,G/F,X/L,N/M,P/F,U/H,Y/H";//gaojd 去掉要客 Z/X
        	JSONObject jsonObj = propertyCodeSourceData.getJSONObject(i);
        	String propertyCode = jsonObj.getString("id");
        	if(allPropertyCode.contains(propertyCode))
        		propertyCodeSource.add(jsonObj);
		}
		
		// 机号
		JSONArray aircraftNumberSource = cacheService.getOpts("dim_acreg", "acreg_code","acreg_code");
		// 机型
		JSONArray actTypeSource = cacheService.getOpts("dim_actype", "todb_actype_code","todb_actype_code");
        
        mode.addAttribute("airportCodeSource",airportCodeSource);
        mode.addAttribute("airlinesCodeSource",airlinesCodeSource);
        mode.addAttribute("propertyCodeSource",propertyCodeSource);
        mode.addAttribute("aircraftNumberSource",aircraftNumberSource);
        mode.addAttribute("actTypeSource",actTypeSource);
		return mode;
	}
	
	/**
	 * 根据航班号获取共享航班号
	 * @param fltNo 航班号
	 * @return
	 */
	@RequestMapping(value = "getShareFltNo")
    @ResponseBody
    public String getShareFltNo(String fltNo) {
		String res = cacheService.mapGet("dim_share_fltno_map", fltNo==null?"":fltNo.toUpperCase());
        if(res!=null)
        	return res.toUpperCase();
        return "";
    }
	
	/**
     * 根据机号获取机型
     * @param aftNum 机号
     * @return
     */
    @RequestMapping(value = "getActType")
    @ResponseBody
    public JSONObject getActType(String aftNum) {
    	JSONObject resStr = null;
    	if(aftNum!=null)
    		aftNum = aftNum.toUpperCase();
        String res = cacheService.mapGet("dim_acreg_map", aftNum);
        if(res!=null&&!res.equals("")){
        	resStr = JSON.parseObject(res);
        }
        return resStr;
    }
	
	/**
     * 右键查看报文
     */
    @RequestMapping(value = "showMsg")
    public String showMsg(Model model,String msgId) {
    	String msg = specialPlanService.getMsgById(msgId);
    	if(msg!=null){
    		msg = msg.replace("\r", "--");
    	}else{
    		msg = "";
    	}
    	model.addAttribute("msg", msg);
        return "prss/plan/specialPlanShow";
    }
}
