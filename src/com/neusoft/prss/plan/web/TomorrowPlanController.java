/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年4月10日 下午3:47:23
 *@author:neusoft
 *@version:[v1.0]
 */
package com.neusoft.prss.plan.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.actstand.service.DispatchActstandService;
import com.neusoft.prss.asup.service.FlightTimeService;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.counter.service.DispatchCounterService;
import com.neusoft.prss.flight.service.FlightPairService;
import com.neusoft.prss.fltinfo.service.UpdateFltinfoService;
import com.neusoft.prss.plan.entity.TravelskyPlanEntity;
import com.neusoft.prss.plan.service.TomorrowPlanService;
import com.neusoft.prss.plan.service.TravelskyPlanService;
/**
 * 次日计划
 */
@Controller
@RequestMapping(value = "${adminPath}/tomorrow/plan")
public class TomorrowPlanController extends BaseController {
	
    @Resource
    private TomorrowPlanService tomorrowPlanService;
    
    @Resource
    private TravelskyPlanService travelskyPlanService;
    
    //计算预落时间
    @Resource
    private FlightTimeService flightTimeService;
    
    @Autowired
    private CacheService cacheService;
    
    //机位分配
    @Resource
    private DispatchActstandService dispatchActstandService;
    
    //航班配对
    @Resource
    private FlightPairService flightPairService;
    
    //动态事件
    @Resource
    private UpdateFltinfoService updateFltinfoService;
    
    //值机柜台分配
    @Autowired
    private DispatchCounterService dispatchCounterService;

    /**
     * 
     *Discription:跳转次日计划列表.
     *@param model
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        //initParam(model);
    	//查询次日计划是否已发布，如果已发布新增、删除、发布功能不可用
    	boolean checkPublished = false;
    	int publishedCount = tomorrowPlanService.getTomorrowPublishedCount();
    	if(publishedCount>0){
    		checkPublished = true;
    	}
    	model.addAttribute("checkPublished",checkPublished);
    	//从缓存中获取下拉列表数据
    	model = selectOptionData(model);
        model.addAttribute("type", "main");
        return "prss/plan/tomorrowPlanMain";
    }

    /**
     * 
     *Discription:参数初始化.
     *@param model
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月10日 neusoft [变更描述]
     */
    private void initParam(Model model) {
        JSONObject jsonObject = new JSONObject();
        //性质
        JSONArray PROPERTY_CODE = cacheService.getOpts("dim_flight_property", "property_code","property_shortname");
        jsonObject.put("PROPERTY_CODE", PROPERTY_CODE);
        //属性
        JSONArray ATTR_CODE = cacheService.getCommonDict("alntype");
        jsonObject.put("ATTR_CODE", ATTR_CODE);
        //航空公司
        JSONArray AIRLINE_CODE = cacheService.getOpts("dim_airline", "icao_code", "airline_shortname","airline_code");
        jsonObject.put("AIRLINE_CODE", AIRLINE_CODE);
        //起、落场
        JSONArray APT = cacheService.getOpts("dim_airport", "airport_code", "description_cn","icao_code");
        jsonObject.put("APT", APT);
        //机号
//        JSONArray AIRCRAFT_NUMBER = planService.getSelect2Source("SELECT t.ACREG_CODE VALUE,t.ACREG_CODE text FROM dim_acreg t");
//        jsonObject.put("AIRCRAFT_NUMBER", AIRCRAFT_NUMBER);
        //机型
        JSONArray ACT_TYPE = cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
        jsonObject.put("ACT_TYPE", ACT_TYPE);
        model.addAttribute("data", jsonObject);
    }
    /**
     * 
     *Discription:初始化列表数据.
     *@param request
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value="init")
    @ResponseBody
    public String getPlans(HttpServletRequest request) {
        Map<String,String> params = new HashMap<String,String>();
        String ioType = request.getParameter("ioType");
        String planType =  request.getParameter("planType");
        params.put("ioType", ioType);
        params.put("planType", planType);
        JSONArray mainArr = tomorrowPlanService.getPlans(params);
        String data = JSON.toJSONString(mainArr, SerializerFeature.WriteMapNullValue);
        return data;
    }

    /**
     * 
     *Discription:更新次日计划.
     *@param request
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月10日 neusoft [变更描述]
     */
	@RequestMapping(value = "update")
	@ResponseBody
	public String update(HttpServletRequest request){
		String result = "succeed";
	   		try{
			//修改字段属性名
			String name = request.getParameter("name");
			//更新字段行json数据
			String rowJsonValue = request.getParameter("rowJsonValue");
			
			Map<String,String> params = new HashMap<String,String>();
			params.put("name", name);
			params.put("rowJsonValue", rowJsonValue);
			
			tomorrowPlanService.update(params);
	   		} catch (Exception e){
	   			result = e.toString();
				logger.error(e.toString());
			}
   		return result;
	}
	
    /**
     * 
     *Discription:导入数据.
     *@param files
     *@param delFile
     *@param type
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月14日 neusoft [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "importPlan",method = RequestMethod.POST)
    public String importPlan(@RequestParam("file") MultipartFile[] files,String delFile,String type) {
        try {
            long beginTime = System.currentTimeMillis();
            //清空航信计划表
            tomorrowPlanService.travelskyPlanDelete();
            //清空空管、航信对比临时表
            tomorrowPlanService.tomorrowTempPlanDelete();
            //清空次日计划表
            tomorrowPlanService.tomorrowPlanDelete();
            for (int i = 0; i < files.length; i++) {
                // 解析报文
                List<TravelskyPlanEntity> list = travelskyPlanService.travelskyPlanImport(files[i].getBytes(), UserUtils.getUser().getId(),type);
                if(list!=null&&list.size()>0){
                	// 报文信息入库
                    tomorrowPlanService.travelskyPlanAdd(list);
                }else{
                	String fileName = files[i].getOriginalFilename();
                	logger.warn(fileName+"解析无航信计划结果集合数据!");
                }
            }
            tomorrowPlanService.importPlan();
            logger.info("次日计划文本导入公用{}秒。",(System.currentTimeMillis()-beginTime)/1000.0);
            return "success";
        } catch (Exception e) {
            logger.error("次日计划文本导入失败" + e.getMessage());
            //清空航信计划表
            tomorrowPlanService.travelskyPlanDelete();
            //清空空管、航信对比临时表
            tomorrowPlanService.tomorrowTempPlanDelete();
         	//清空次日计划表
            tomorrowPlanService.tomorrowPlanDelete();
            return "fail";
        }
    }

    /**
     * 
     *Discription:跳转到核对页面.
     *@param model
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月1日 neusoft [变更描述]
     */
    @RequestMapping(value = "toFilterPlan")
    public String toFilterPlan(Model model) {
        JSONObject result = tomorrowPlanService.getFilterPlans();
        model.addAttribute("result", result);
        initParam(model);
        return "prss/plan/tomorrowPlanFilter";
        
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
    
    @RequestMapping(value = "filterImport",method = RequestMethod.POST)
    @ResponseBody
    public boolean filterImport() {
        return tomorrowPlanService.filterImport();
        
    }

    
    /**
     * 
     *Discription:批量删除.
     *@param ids
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月1日 neusoft [变更描述]
     */
    @RequestMapping(value = "deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteBatch(String data) {
    	try {
            data = StringEscapeUtils.unescapeHtml4(data);
            JSONArray dataArray = JSONArray.parseArray(data);
            return tomorrowPlanService.deleteBatch(dataArray);
        } catch (Exception e) {
            logger.error("比对次日计划 批量删除失败：{}" ,e.getMessage());
            return false;
        }
    }
    
    /**
     * 
     *Discription:批量保存.
     *@param ids
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月1日 neusoft [变更描述]
     */
    @RequestMapping(value = "saveBatch",method = RequestMethod.POST)
    @ResponseBody
    public boolean saveBatch(String data) {
        try {
            data = StringEscapeUtils.unescapeHtml4(data);
            JSONArray dataArray = JSONArray.parseArray(data);
            tomorrowPlanService.saveBatch(dataArray);
            return true;
        } catch (Exception e) {
            logger.error("比对次日计划 批量保存失败：{}" ,e.getMessage());
            return false;
        }
    }
    
    /**
     * 跳转次日计划编辑页
     */
    @RequestMapping(value="toForm")
    public String toForm(Model model, boolean isNew, String fltDate, String fltNo, String ioType, String ids) {
    	//从缓存中获取下拉列表数据
    	model = selectOptionData(model);
    	model.addAttribute("isNew", isNew);
		model.addAttribute("fltDate", fltDate);
		model.addAttribute("fltNo", fltNo);
		model.addAttribute("ioType", ioType);
		model.addAttribute("ids", ids);
		model.addAttribute("currentAirport", Global.getConfig("airport_code3"));//本场
        return "prss/plan/tomorrowPlanForm";
    }

    /**
     * 删除次日计划数据
     */
    @RequestMapping(value="delete")
    @ResponseBody
    public String delete (HttpServletRequest request) {
    	String result = "succeed";
   		try{
	   		//起场次日计划
	   		String departPlanData = request.getParameter("departPlanData");
	   		//落场次日计划
	   		String arrivalPlanData = request.getParameter("arrivalPlanData");
	   		JSONArray departPlanDataArray = JSONArray.parseArray(departPlanData);
	   		JSONArray arrivalPlanDataArray = JSONArray.parseArray(arrivalPlanData);
	   		//删除次日计划
	   		tomorrowPlanService.deleteTomorrowPlan(departPlanDataArray,arrivalPlanDataArray);
   		} catch (Exception e){
   			result = e.toString();
			logger.error(e.toString());
		}
   		return result;
    }

    /**
     * 保存次日计划
     * @param isNew 是否为新增保存
	 * @param newRow 新增的行
	 * @param editRow 修改的行
	 * @param removeRow 删除的行
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public String save(boolean isNew,String newRows,String editRows,String removeRows,String ioType) {
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
			params.put("userId", UserUtils.getUser().getId());
			params.put("ioType", ioType);
            tomorrowPlanService.saveTomorrowPlan(addData, editData, removeData, params);
		} catch (Exception e){
			result = e.getMessage();
			logger.error(e.toString());
		}
        
        return result;
    }
    
    /**
     * 编辑页数据
     */
    @RequestMapping(value="formGridData")
    @ResponseBody
    public JSONArray formGridData(boolean isNew, String fltDate, String fltNo,String ioType,String ids) {
    	JSONArray data = new JSONArray();
		if(!isNew){
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("fltDate", fltDate);
			paramMap.put("fltNo", fltNo);
			paramMap.put("flag", ioType);
			paramMap.put("ids", ids);
			data = tomorrowPlanService.getFltPlanInfo(paramMap);
		}
		return data;
    }
   
    /**
   	 * 发布次日计划
   	 */	
   	@RequestMapping(value = "publish")
   	@ResponseBody
   	public JSONObject publish(HttpServletRequest request){
   		JSONObject rs = new JSONObject();
   		String result = "succeed";
   		String msg = "";
   		//判断计划是否已发布
		int publishedCount = tomorrowPlanService.getTomorrowPublishedCount();
	    if(publishedCount==0){
	    	String date = getAddDay("","yyyyMMdd","",+1);
	    	//起场次日计划
		  	String departPlanData = request.getParameter("departPlanData");
			//落场次日计划
		  	String arrivalPlanData = request.getParameter("arrivalPlanData");
			JSONArray departPlanDataArray = JSONArray.parseArray(departPlanData);
			JSONArray arrivalPlanDataArray = JSONArray.parseArray(arrivalPlanData);
			String publishedFltIds = "";
			String currDate = "";
		   	try{
		   		//发布次日计划
		   		currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
		   		logger.info(currDate+"开始执行:发布计划到动态");
				publishedFltIds = tomorrowPlanService.publishTomorrowPlan(departPlanDataArray,arrivalPlanDataArray,UserUtils.getUser().getId());
				currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
				logger.info(currDate+"执行完成:发布计划到动态,生成的动态fltid:"+publishedFltIds);
		   	} catch (Exception e){
	   			msg += "发布计划到动态异常,";
	   			result = "err";
	   			publishedFltIds = "";
	   			//删除已发布到动态的数据
				tomorrowPlanService.deleteTomorrowFltInfo(date);
				logger.error("次日计划发布-发布计划到动态异常"+e.toString());
			}
		   	if(!publishedFltIds.equals("")){
		   		try{
		   			//次日计划发布后插入动态事件
		   			currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
		   			logger.info(currDate+"开始执行:插入动态事件接口insertSCHDEvent");
			   		updateFltinfoService.insertSCHDEvent(publishedFltIds, UserUtils.getUser().getId(), 0);
			   		currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
					logger.info(currDate+"执行完成:插入动态事件接口insertSCHDEvent");
		   		} catch (Exception e){
		   			msg += "插入动态事件接口异常,";
		   			result = "err";
					logger.error("次日计划发布-插入动态事件接口异常"+e.toString());
				}
		   		try{
		   			//航班配对
		   			currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
		   			logger.info(currDate+"开始执行:航班配对接口pairFlight");
			   		flightPairService.pairFlight(date);
			   		currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
					logger.info(currDate+"执行完成:航班配对接口pairFlight");
		   		} catch (Exception e){
		   			msg += "航班配对接口异常,";
		   			result = "err";
					logger.error("次日计划发布-航班配对接口异常"+e.toString());
				}
		   		try{
		   			//机位预排
		   			currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
		   			logger.info(currDate+"开始执行:机位预排接口dispatchActstandByDate");
			   		dispatchActstandService.dispatchActstandByDate(date);
			   		currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
					logger.info(currDate+"执行完成:机位预排接口dispatchActstandByDate");
		   		} catch (Exception e){
		   			msg += "机位预排接口异常,";
		   			result = "err";
					logger.error("次日计划发布-机位预排接口异常"+e.toString());
				}
		   		try{
		   			//值机柜台分配
		   			currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
		   			logger.info(currDate+"开始执行:值机柜台分配接口dispatchCounterByIds");
			   		dispatchCounterService.dispatchCounterByIds(publishedFltIds);
			   		currDate = getAddDay("", "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
					logger.info(currDate+"执行完成:值机柜台分配接口dispatchCounterByIds");
		   		} catch (Exception e){
		   			msg += "值机柜台分配接口异常,";
		   			result = "err";
					logger.error("次日计划发布-值机柜台分配接口异常"+e.toString());
				}
		   	}else{
		   		result = "nonPublished";
		   	}
	    }else{
	    	result = "alreadyPublished";
	    }
	    rs.put("result", result);
	    rs.put("msg", msg);
   		return rs;
   	}
   	
   	/**
   	 * 打印次日计划
   	 */
   	@RequestMapping(value = "exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response) {
   		try {
   			Map<String,String> params = new HashMap<String,String>();
   			String planType =  request.getParameter("planType");
   			params.put("airportCode", Global.getConfig("airport_code3"));
   	        params.put("planType", planType);
   			String fileName = "次日计划" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
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
	       
	        params.put("flag","A");
	        JSONArray arrivalPlans = tomorrowPlanService.getPlans(params);
	        params.put("flag","D");
	        JSONArray departPlans = tomorrowPlanService.getPlans(params);
	        
			byte[] content = tomorrowPlanService.exportTomorrowPlan(arrivalPlans,departPlans);
			response.getOutputStream().write(content);
            response.getOutputStream().flush();
		} catch (Exception e) {
			logger.error("次日计划导出失败" + e.getMessage());
		}
    }
   	
	/**
   	 * 跳转次日计划预测
   	 */
   	@RequestMapping(value = "toForecast")
    public String toForecast(Model model) {
   		JSONArray forecastConfArr = new JSONArray();
   		JSONObject forecastConf = tomorrowPlanService.getForecastConf();
   		forecastConfArr.add(forecastConf);
   		model.addAttribute("confData",forecastConfArr);
        return "prss/plan/tomorrowPlanForecast";
    }
   	
   	/**
   	 * 次日计划预测
   	 */
   	@ResponseBody
   	@RequestMapping(value = "forecast")
    public JSONObject forecast() {
   		JSONObject charData = null;
   		try{
   			//预测
   			Map<String,String> forecastDataMap = tomorrowPlanService.forecast();
   			charData = JSONObject.parseObject(forecastDataMap.get("forecastData"));
   			charData.put("showMsg", forecastDataMap.get("showMsg"));
	   	} catch (Exception e) {
			logger.error("次日计划预测失败" + e.getMessage());
		}
   		return charData;
	}
   	
   	/**
   	 * 次日计划预测配置保存
   	 */
   	@ResponseBody
   	@RequestMapping(value = "forecastConfSave")
    public String forecastConfSave(HttpServletRequest request,HttpServletResponse response) {
   		String result = "succeed";
   		try{
   			String saveData = request.getParameter("saveData");
   	   		JSONArray forecastConfData = JSONArray.parseArray(saveData);
   	   		tomorrowPlanService.saveForecastConf(forecastConfData);
	   	} catch (Exception e) {
	   		result = e.toString();
			logger.error("次日计划预测配置保存失败" + e.getMessage());
		}
        return result;
    }
   	
   	
	/**
	 * 从缓存中获取机场三字码、名称，航空公司三字码、名称，性质编码，机型、机号
	 */
	private Model selectOptionData(Model mode) {
		// 机场代码(代码使用三字码)、名称
		JSONArray airportCodeSource = cacheService.getOpts("dim_airport", "airport_code","description_cn","icao_code","d_or_i");
		// 机场对应属性,根据机场起落场判断此段航班属性是国内还是国际 I 国际 D 国内
		JSONObject aptAttrCode = new JSONObject();
		for(int i =0;i<airportCodeSource.size();i++){
			JSONObject obj = airportCodeSource.getJSONObject(i);
			String key = obj.getString("id");
			String val = obj.getString("d_or_i");
			aptAttrCode.put(key, val);
		}
		// 航空公司代码(代码使用三字码)、名称
		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname","airline_code");
		// 性质编码
		JSONArray propertyCodeSource = cacheService.getOpts("dim_flight_property", "property_code","property_shortname");
		// 机号
//		JSONArray aircraftNumberSource = cacheService.getOpts("dim_acreg", "acreg_code","acreg_code");
		// 机型
		JSONArray actTypeSource = cacheService.getOpts("dim_actype", "todb_actype_code","todb_actype_code");
        
        mode.addAttribute("airportCodeSource",airportCodeSource);
        mode.addAttribute("airlinesCodeSource",airlinesCodeSource);
        mode.addAttribute("propertyCodeSource",propertyCodeSource);
//        mode.addAttribute("aircraftNumberSource",aircraftNumberSource);
        mode.addAttribute("actTypeSource",actTypeSource);
        mode.addAttribute("aptAttrCode",aptAttrCode);
		return mode;
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
	
	/**
     * 根据机号获取机型
     * @param aftNum 机号
     * @return
     */
    @RequestMapping(value = "getActType")
    @ResponseBody
    public JSONObject getActType(String aftNum) {
    	JSONObject resStr = new JSONObject();
    	if(aftNum!=null)
    		aftNum = aftNum.toUpperCase();
        String res = cacheService.mapGet("dim_acreg_map", aftNum);
        if(res!=null&&!res.equals("")){
        	resStr = JSON.parseObject(res);
        }
        return resStr;
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
    	String msg = tomorrowPlanService.getMsgById(msgId);
    	if(msg!=null){
    		msg = msg.replace("\r", "--");
    	}else{
    		msg = "";
    	}
    	model.addAttribute("msg", msg);
        return "prss/plan/tomorrowPlanShow";
    }
    
    /**
     * 根据机型、目标机场、预起ETD时间获取计算的预落值
     * @param actType 机型
     * @param aptCode 对端机场
     * @param fltDate 航班日期yyyyMMddHHmm+
     * @param etd 预起时间
     * @return
     */
	@RequestMapping(value = "getEta")
    @ResponseBody
    public String getEta(String actType,String aptCode,String fltDate,String etd) {
		String eta = "";
		if(StringUtils.isNotBlank(etd)&&StringUtils.isNotBlank(fltDate)&&StringUtils.isNotBlank(aptCode)){
			String inTime = "";
			String tmpTime = "";
			if(etd.contains("+")){
				inTime = getAddDay(fltDate+etd.replace("+", ""), "yyyyMMddHHmm","yyyy-MM-dd HH:mm", +1);
				tmpTime = getAddDay(fltDate+etd.replace("+", ""), "yyyyMMddHHmm","", +1);
			}else{
				inTime = getAddDay(fltDate+etd, "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
				tmpTime = getAddDay(fltDate+etd, "yyyyMMddHHmm","", 0);
			}
			eta = flightTimeService.calculateETA(inTime, actType, aptCode,"yyyyMMddHHmm");
			if(StringUtils.isNotBlank(eta)){
				int intTmpTime = Integer.parseInt(tmpTime.substring(0, 8));
				int intOutTime = Integer.parseInt(eta.substring(0, 8));
				if(intOutTime>intTmpTime){
					//如果返回日期大于传入日期，添加"+"号
					eta = eta.substring(8, 12)+"+";
				}else{
					eta = eta.substring(8, 12);
				}
			}else{
				eta = "";
			}
		}
		return eta;
    }
}
