package com.neusoft.prss.plan.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.plan.entity.TravelskyPlanEntity;
import com.neusoft.prss.plan.service.TravelskyPlanService;
@Controller
@RequestMapping(value = "${adminPath}/plan/travelskyPlan")
public class TravelskyPlanController extends BaseController{
	@Autowired
	private TravelskyPlanService travelskyPlanService;
	@Autowired
	private CacheService cacheService;
	/**
     * 
     *Discription:打开列表页.
     *@param model
     *@return
     *@return:返回值意义
     *@author:huanglj
     *@update:2018年4月16日 huanglj [变更描述]
     */
    @RequestMapping(value = {"list"})
    public String list() {
        return "prss/plan/travelskyPlanList";
    }
    /**
     * 获取进出港列表
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "getData")
    public String getData(HttpServletRequest request) throws ParseException {
    	int offset = Integer.valueOf(request.getParameter("offset"));
		int limit = Integer.valueOf(request.getParameter("limit"));
		String ioType = request.getParameter("ioType");
		String startDate = request.getParameter("start");
		String endDate = request.getParameter("end");
        JSONArray json = travelskyPlanService.getGridData(String.valueOf(offset+1),String.valueOf(offset+limit),ioType,startDate,endDate);
		int total = travelskyPlanService.getTotalRow("","",ioType,startDate,endDate);
		JSONObject result = new JSONObject();
		result.put("rows", json);
		result.put("total", total);
        return result.toJSONString();
    }
    /**
     * 计划导入
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "importPlan",method = RequestMethod.POST)
    public String importPlan(@RequestParam("file") MultipartFile[] files,String delFile,String type) {
    	String msg="success";
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getOriginalFilename();
                if (delFile.indexOf(fileName) == -1) {
                	// 解析报文
                    List<TravelskyPlanEntity> list = travelskyPlanService.travelskyPlanImport(files[i].getBytes(), UserUtils.getUser().getName(),type);
                    // 报文信息入库
                    for (int j = 0; j < list.size(); j++) {
						String fltDate = list.get(j).getFltDate();
						String std = list.get(j).getStd();
						String sta = list.get(j).getSta();
						fltDate = formatDate(fltDate);
						list.get(j).setFltDate(fltDate.split("-")[0]+fltDate.split("-")[1]+fltDate.split("-")[2]);
						std = fltDate+" "+std;
						sta = fltDate+" "+sta;
						Date a = sdf.parse(sta);	
			  			Date d = sdf.parse(std);
			  			if (a.getTime()<d.getTime()) {  				
			  		        Calendar c = Calendar.getInstance();  
			  		        c.setTime(a);  
			  		        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  			  		   
			  		        a = c.getTime();  
			  		     std = sdf.format(d);
			  		     sta = sdf.format(a);
			  			}
			  			list.get(j).setSta(sta);
			  			list.get(j).setStd(std);
					}
                    travelskyPlanService.travelskyPlanAdd(list);
                }
            }
        } catch (Exception e) {
            logger.error("计划文本导入失败" + e.getMessage());
            e.printStackTrace();
            msg = "error";
            return msg;
        }
        return msg;
    }
    /**
     * 进入新增页面
     * @param model
     * @return
     */
    @RequestMapping(value = "form")
    public String form(Model model, boolean isNew, String fltDate, String fltNo) {
    	model = selectOptionData(model);
    	model.addAttribute("isNew", isNew);
		model.addAttribute("fltDate", fltDate);
		model.addAttribute("fltNo", fltNo);
        return "prss/plan/travelskyPlanForm";
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
	public JSONArray formGridData(boolean isNew, String fltDate, String fltNo) {
		JSONArray data = new JSONArray();
		if(!isNew){
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("fltDate", fltDate);
			paramMap.put("fltNo", fltNo);
			data = travelskyPlanService.getFltInfo(paramMap);
		}
		return data;
	}
    /**
     * 保存
     * @param planDetail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public String save(String planDetail) {
    	String result = "success";
    	try{
    		planDetail = StringEscapeUtils.unescapeHtml4(planDetail);
            JSONArray mainArr = JSONArray.parseArray(planDetail);
            List<TravelskyPlanEntity> list = new ArrayList<>();
            for (int i = 0; i < mainArr.size(); i++) {    
        		TravelskyPlanEntity entity = new TravelskyPlanEntity();
        	    entity.setActType(mainArr.getJSONObject(i).getString("actType"));
        	    entity.setArrivalApt(mainArr.getJSONObject(i).getString("arrivalApt"));
        	    entity.setDepartApt(mainArr.getJSONObject(i).getString("departApt"));
        	    entity.setShareFltNo(mainArr.getJSONObject(i).getString("shareFltNo"));
        	    entity.setSta(mainArr.getJSONObject(i).getString("sta"));
        	    entity.setStd(mainArr.getJSONObject(i).getString("std"));
        	    entity.setFltNo(mainArr.getJSONObject(i).getString("fltNo").toUpperCase());
        	    entity.setFltDate(mainArr.getJSONObject(i).getString("fltDate"));
        	    entity.setCreateUser(UserUtils.getUser().getLoginName());
        	    list.add(entity);
            }
            travelskyPlanService.travelskyPlanAdd(list);
    	}catch(Exception e){
    		result = e.toString();
			logger.error(e.toString());
    	}
        return result;
    }
    /**
     * 格式化月份
     * @param date
     * @return
     */
    public String formatDate(String date){
    	String year = date.substring(0,2);
		String month = date.substring(2,5);
		String day = date.substring(5,7);
		System.out.println(year+"-"+month+"-"+day);
		String[] mm = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
		for (int i = 0; i < mm.length; i++) {
			if(month.equals(mm[i])){
				month=String.valueOf(i+1);
			}
		}
		if (Integer.parseInt(month)<10) {
			month = "0"+month;
		}
		return "20"+year+"-"+month+"-"+day;
    }
    /**
     * 删除计划
     * @param ids
     * @return
     */
    @RequestMapping(value="delete")
    @ResponseBody
    public boolean delete (String ids,String _ids) {
    	int i = 0;
        boolean result = false;
        String delIds = "";
        if(StringUtils.isNotBlank(ids)){
        	delIds += ids;
        }
        if(StringUtils.isNotBlank(_ids)){
        	if(!delIds.equals("")){
        		delIds += ","+_ids;
        	}else{
        		delIds += _ids;
        	}
        }
//        System.err.println(ids+","+_ids);
//        if (("").equals(ids)) {
//			ids = _ids;
//		}
//        if (!("").equals(ids)&&!("").equals(_ids)) {
//			ids = ids+_ids;
//		}
        try {
            if(!StringUtils.isBlank(delIds))
            	i = travelskyPlanService.delete(delIds);
            if (i>0) {
            	  result = true;
			}     
        } catch (Exception e) {
            logger.error("删除数据失败：{}",e.getMessage());
        }
        return result;
    }
    public static void main(String[] args) {  
    	String a ="2018-04-19 01:30";
    	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");  
   
        Date today;
		try {
			today = f.parse(a);
		
        System.out.println("今天是:" + f.format(today));  
   
        Calendar c = Calendar.getInstance();  
        c.setTime(today);  
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
   
        Date tomorrow = c.getTime();  
        System.out.println("明天是:" + f.format(tomorrow));  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
    
    /**
     * 计划打印
     * @param request
     * @param response
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response) {
    	try{
    	String startDate = request.getParameter("sDate");
    	String endDate = request.getParameter("eDate");
    	byte[] buffer = travelskyPlanService.printData(startDate,endDate);
    	InputStream inputStream = new ByteArrayInputStream(buffer);
    	HSSFWorkbook wb = new HSSFWorkbook(inputStream);
    	String fileName = "中航信计划" + DateUtils.getDate("yyyyMMddHHmmss") + ".xls";
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
    	wb.write(response.getOutputStream());
    	}catch(IOException e){
    		logger.error("中航信计划导出失败" + e.getMessage());
    	}
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
        	String allPropertyCode = "D/Y,G/F,X/L,N/M,P/F,U/H,Z/X,Y/H";
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
	@RequestMapping(value = "edit")
	@ResponseBody
	public String edit(boolean isNew,String newRows,String editRows,String removeRows) {
		String result = "success";
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
			travelskyPlanService.save(addData, editData, removeData, params);
		}catch(Exception e){
			result = e==null?"":e.getMessage();
			logger.error(e.toString());
		}
		return result;
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
}
