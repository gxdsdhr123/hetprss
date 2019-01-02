package com.neusoft.prss.produce.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.produce.entity.FltChargeServiceEntity;
import com.neusoft.prss.produce.service.FltChargeService;

/**
 * 航空器加清排污记录表单据
 */
@Controller
@RequestMapping(value = "${adminPath}/produce/fltChargeService")
public class FltChargeServiceController extends BaseController{
	//本场三字码
	private final static String airportCode3 = Global.getConfig("airport_code3");

	@Autowired
	private FltChargeService fltChargeService;
	
	@Autowired
    private CacheService cacheService;
	
	@Autowired
	private FileService fileService;
	/**
	 * 航空器加清排污记录列表页
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		return "prss/produce/fltChargeServiceList";
	}
	
	/**
	 * 航空器加清排污记录新增、修改页
	 */
	@RequestMapping(value="toForm")
	public String edit(Model model,String id,String operateType){
		if(operateType!=null&&operateType.equals("add")){
			model.addAttribute("operateType", operateType);
			JSONObject data = new JSONObject();
			data.put("operator", UserUtils.getUser().getId());
			data.put("operatorName", UserUtils.getUser().getName());
			model.addAttribute("modifyData", data);
		}else{
			model.addAttribute("operateType", operateType);
			JSONObject data = fltChargeService.getDataById(id);
			model.addAttribute("modifyData", data);
		}
		//获取人员信息
		JSONArray sysUserSource = fltChargeService.getSysUser();
		model.addAttribute("sysUserSource", sysUserSource);
		return "prss/produce/fltChargeServiceForm";
	}
	
	/**
	 * 航空器加清排污记录列表数据
	 * @param offset
	 * @param limit
	 * @param searchText
	 * @return
	 */
	@RequestMapping(value="getData")
	@ResponseBody
	public JSONObject getData(Integer pageSize,Integer pageNumber,String startDate, String endDate){
		JSONObject rs = new JSONObject();
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			int begin=(pageNumber-1)*pageSize;
	        int end=pageSize + begin;
			param.put("begin", begin);
			param.put("end", end);
			param.put("startDate", startDate);
			param.put("endDate", endDate);
			rs = fltChargeService.getDataList(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	/**
	 * 新增航空器加清排污记录单据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public String save(FltChargeServiceEntity entity){
		String result = "succeed";
		try {
			if(entity!=null){
				String id = entity.getId();
				if(id!=null&&!id.equals("")){
					//更新
					fltChargeService.update(entity);
				}else{
					//新增
					List<FltChargeServiceEntity> dataList = new ArrayList<FltChargeServiceEntity>();
					entity.setCreateUser(UserUtils.getUser().getId());
					dataList.add(entity);
					fltChargeService.save(dataList);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString();
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 删除航空器加清排污记录单据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="delete")
	@ResponseBody
	public String delete(String id){
		String result = "succeed";
		try {
			fltChargeService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString();
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 航班详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="flightDetail")
	@ResponseBody
	public JSONObject flightDetail(String flightNumber,String flightDate,String inOutFlag){
		JSONObject reFltDetailData = new JSONObject();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
				param.put("flightNumber", flightNumber);
				param.put("flightDate", flightDate);
				param.put("inOutFlag", inOutFlag);
				JSONObject fltDetailData = fltChargeService.getFlightDetail(param);
				if(fltDetailData!=null){
					reFltDetailData.put("fltDetailData", fltDetailData);
					reFltDetailData.put("result", "found");
				}else{
					reFltDetailData.put("result", "notfound");
				}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return reFltDetailData;
	}
	
	@RequestMapping(value = "exportExcel")
	public void printList(HttpServletRequest request, HttpServletResponse response) {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		
		String columnName = "[{\"field\":\"FLIGHT_DATE\"},{\"field\":\"FLIGHT_NUMBER\"},"
				+ "{\"field\":\"AIRCRAFT_NUMBER\"},{\"field\":\"ACTTYPE_CODE\"},"
				+ "{\"field\":\"ADD_CLEAN_STR\"},{\"field\":\"SEWERAGE_STR\"},{\"field\":\"OPERATOR_NAME\"},{\"field\":\"REMARK\"}]";
		String[] title = { "日期", "航班号","机号","机型","加清","排污", "操作人", "备注" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			String fileName = "航空器加清排污记录表" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			excel.setDataList(title, columnArr, fltChargeService.getDownDataList(param));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("航空器加清排污记录表" + e.getMessage());
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
    
    /**
	 * 获取日期加减天后日期字符串
	 * @param dateStr
	 * @param inPatterns
	 * @param outPatterns
	 * @param days
	 * @return
	 */
	private String getAddDay(String dateStr,String inPatterns,String outPatterns,int days){
		SimpleDateFormat fmIn = new SimpleDateFormat(inPatterns);
		SimpleDateFormat fmOut = new SimpleDateFormat(outPatterns);
		String re = "";
		try {
			Date date = null;
			if(dateStr.equals("")){
				date = new Date();
			}else{
				date = fmIn.parse(dateStr);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, days);
			if(outPatterns!=null&&!outPatterns.equals("")){
				re = fmOut.format(cal.getTime());
			}else{
				re = fmIn.format(cal.getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re;
	}
}
