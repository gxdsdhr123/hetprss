package com.neusoft.prss.plan.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.plan.service.ATMBPlanService;

/**
 * 空管次日计划
 * 
 * @author baochl 
 */
@Controller
@RequestMapping(value = "${adminPath}/plan/atmbPlan")
public class ATMBPlanController extends BaseController {
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ATMBPlanService atmbService;

	/**
	 * 进入列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(Model model) {
		//服务器可以发布计划的日期
		model.addAttribute("servicePublishDate", getAddDay("","yyyyMMdd",+1));
		return "prss/plan/atmbPlanList";
	}

	/**
	 * 获取列表数据
	 * 
	 * @param ioType
	 *            进出港标识
	 * @return
	 */
	@RequestMapping(value = "gridData")
	@ResponseBody
	public JSONArray gridData(String ioType) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ioType", ioType);
		return atmbService.getGridData(paramMap);
	}

	/**
	 * 计划导入
	 * 
	 * @return
	 */
	@RequestMapping(value = "importData")
	@ResponseBody
	public String importData() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", UserUtils.getUser().getId());
		String result = atmbService.importData(paramMap);
		return result;
	}
	
	/**
	 * 空管次日计划excel文件导入
	 * @param files
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "importExcelPlan",method = RequestMethod.POST)
	public JSONArray importExcelPlan(@RequestParam("file") MultipartFile files) {
		JSONArray rsJsonArr = new JSONArray();
		JSONArray errMsgJsonArr = null;
		JSONArray infoMsgJsonArr = null;
		JSONObject importRsJsonObj = new JSONObject();
		Map<String,Object> reMap = null;
		String fileName = files.getOriginalFilename();
		String fileType = "xls";
		if(fileName!=null&&fileName.toLowerCase().endsWith("xlsx")){
			fileType = "xlsx";
		}
		try {
			reMap = atmbService.importExcelData(files.getBytes(),UserUtils.getUser().getId(),fileType);
			String importRs = reMap.get("importRs").toString();
			if(importRs.equals("err")){
				importRsJsonObj.put("rs", importRs);
				rsJsonArr.add(importRsJsonObj);
				errMsgJsonArr = (JSONArray)reMap.get("err");
				rsJsonArr.add(errMsgJsonArr);
				logger.info("空管次日计划excel存在校验失败数据或解析文件错误，没有执行导入");
			}else{
				importRsJsonObj.put("rs", importRs);
				rsJsonArr.add(importRsJsonObj);
				infoMsgJsonArr = (JSONArray)reMap.get("info");
				rsJsonArr.add(infoMsgJsonArr);
				logger.info("空管次日计划excel导入执行完成");
			}
		} catch (Exception e) {
			JSONObject errMsgRowJsonData = new JSONObject();
			errMsgRowJsonData.put("errMsg", "空管次日计划excel导入失败，请联系管理员");
			errMsgJsonArr.add(errMsgRowJsonData);
			rsJsonArr.add(errMsgJsonArr);
			logger.error("空管次日计划excel导入失败" + e.getMessage());
		}
		return rsJsonArr;
	}

	/**
	 * 计划编辑页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(Model model, boolean isNew, String fltDate, String fltNo, String ioType, String ids) {
		//机场
		JSONArray airports = cacheService.getOpts("dim_airport", "airport_code", "description_cn","icao_code");//机场展示为中文简称
		//航空公司
		JSONArray airlines = cacheService.getOpts("dim_airline", "icao_code", "airline_shortname","airline_code");//航空公司展示为中文简称
		//机型
		JSONArray actTypes = cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
		//性质
		JSONArray propertys = cacheService.getOpts("dim_flight_property", "property_code", "property_shortname");
		model.addAttribute("airports", airports);
		model.addAttribute("airlines", airlines);
		model.addAttribute("actTypes", actTypes);
		model.addAttribute("propertys", propertys);
		model.addAttribute("isNew", isNew);
		model.addAttribute("fltDate", fltDate);
		model.addAttribute("fltNo", fltNo);
		model.addAttribute("ioType", ioType);
		model.addAttribute("ids", ids);
		return "prss/plan/atmbPlanForm";
	}

	/**
	 * 计划编辑表格数据
	 * 
	 * @param model
	 * @param isNew
	 * @param fltDate
	 * @param fltNo
	 * @return
	 */
	@RequestMapping(value = "formGridData")
	@ResponseBody
	public JSONArray formGridData(boolean isNew, String fltDate, String fltNo, String ioType, String ids) {
		JSONArray data = new JSONArray();
		if(!isNew){
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("fltDate", fltDate);
			paramMap.put("fltNo", fltNo);
			paramMap.put("flag", ioType);
			paramMap.put("ids", ids);
			data = atmbService.getFltInfo(paramMap);
		}
		return data;
	}
	/**
	 * 保存
	 * @param isNew 是否为新增保存
	 * @param newRow 新增的行
	 * @param editRow 修改的行
	 * @param removeRow 删除的行
	 * @return
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(boolean isNew,String newRows,String editRows,String removeRows) {
		String result = "succeed";
		try{
			newRows = StringEscapeUtils.unescapeHtml4(newRows);
			editRows = StringEscapeUtils.unescapeHtml4(editRows);
			removeRows = StringEscapeUtils.unescapeHtml4(removeRows);
			JSONArray addData = new JSONArray();//新增的数据
			JSONObject editData = new JSONObject();//修改的数据
			JSONObject removeData = new JSONObject();//删除的数据
			if(StringUtils.isNotEmpty(newRows)){
				addData = JSONArray.parseArray(newRows);
			}
			if(StringUtils.isNotEmpty(editRows)){
				editData = JSONObject.parseObject(editRows);
			}
			if(StringUtils.isNotEmpty(removeRows)){
				removeData = JSONObject.parseObject(removeRows);
			}
			Map<String,String> params = new HashMap<String,String>();
			params.put("userId", "1");
			atmbService.save(addData, editData, removeData, params);
		}catch(Exception e){
			result = e==null?"":e.getMessage();
			logger.error(e.toString());
		}
		return result;
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = "remove")
	@ResponseBody
	public String remove(String rows) {
		String result = "succeed";
		try {
			rows = StringEscapeUtils.unescapeHtml4(rows);
			JSONArray rowDatas = new JSONArray();
			if(StringUtils.isNotEmpty(rows)){
				rowDatas = JSONArray.parseArray(rows);
				if(rowDatas!=null&&rowDatas.size()>0){
					atmbService.remove(rowDatas);
				}
			}
		} catch (Exception e) {
			result = e.toString();
			logger.error(e.toString());
		}
		return result;
	}
	/**
	 * 发布
	 * @return
	 */
	@RequestMapping(value = "publish")
	@ResponseBody
	public String publish() {
		String result = "succeed";
		try {
			int count = atmbService.publish();
			if (count == 0) {
				result = String.valueOf(count);
			}
		} catch (Exception e) {
			result = e.toString();
			logger.error(e.toString());
		}
		return result;
	}
	/**
	 * 打印
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "export")
	public void export(HttpServletRequest request, HttpServletResponse response) {
		try {
			String fileName = "空管次日计划.xls";
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
			Map<String,String> paramMap = new HashMap<String,String>();
			byte[] content = atmbService.export(paramMap);
			response.getOutputStream().write(content);
            response.getOutputStream().flush();
		} catch (Exception e) {
			logger.error("空管次日计划导出失败" + e.getMessage());
		}
	}
	/**
	 * 根据机号获取机型
	 * @param aftNum 机号
	 * @return
	 */
	@RequestMapping(value = "getActType")
    @ResponseBody
    public String getActType(String aftNum) {
        String res = cacheService.mapGet("dim_acreg_map", aftNum);
        JSONObject resStr = JSON.parseObject(res);
        if(resStr!=null)
        	return resStr.getString("actype_code");
        return "";
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
     * 右键查看报文
     */
    @RequestMapping(value = "showMsg")
    public String showMsg(Model model,String msgId) {
    	String msg = atmbService.getMsgById(msgId);
    	if(msg!=null){
    		msg = msg.replace("\r", "--");
    	}else{
    		msg = "";
    	}
    	model.addAttribute("msg", msg);
        return "prss/plan/atmbPlanShow";
    }
    
    /**
	 * 获取日期加减天后日期字符串
	 * @param dateStr
	 * @param patterns
	 * @param days
	 * @return
	 */
	private String getAddDay(String dateStr,String patterns,int days){
	    SimpleDateFormat fm = new SimpleDateFormat(patterns);
	    String re = "";
	    try {
	        Date date = null;
	        if(dateStr.equals("")){
	            date = new Date();
	        }else{
	            date = fm.parse(dateStr);
	        }

	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.DAY_OF_MONTH, days);
	        re = fm.format(cal.getTime());
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return re;
	}
}
