package com.neusoft.prss.plan.web;
/**
 * 国际航班计划
 * @author huanglj
 */
import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.plan.service.InternationalPlanService;
import com.neusoft.prss.produce.entity.ExportExcel;
@Controller
@RequestMapping(value = "${adminPath}/plan/interPlan")
public class InternationalPlanController extends BaseController{
	@Autowired
	private InternationalPlanService internationalPlanService;
	@Autowired
	private CacheService cacheService;
	/**
	 * 初始化国际航班列表页
	 * @return
	 */
	@RequestMapping(value = {"list"})
    public String list() {
        return "prss/plan/internationalPlanList";
    }
	@ResponseBody
    @RequestMapping(value = "getGridData")
    public JSONObject getData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String pageNumber = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
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
			rs = internationalPlanService.getGridData(paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
        return rs;
    }
	/**
     * 进入新增页面
     * @param model
     * @return
     */
    @RequestMapping(value = "form")
    public String form(Model model,String ids,String operateType) {
    	model = selectOptionData(model);
    	//add新增 copy拷贝新增 update更新新增
    	model.addAttribute("operateType",operateType);
    	if(ids!=null&&!ids.equals("")){
    		Map<String, Object> paramMap = new HashMap<String, Object>();
    		paramMap.put("ids", ids);
    		JSONArray dataRows = internationalPlanService.getGridDataByIds(paramMap);
    		model.addAttribute("dataRows",dataRows);
    	}else{
    		model.addAttribute("dataRows","");
    	}
        return "prss/plan/internationalPlanAdd";
    }
    /**
     * 新增修改保存
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public String save(String planDetail,String operateType) {
       planDetail = StringEscapeUtils.unescapeHtml4(planDetail);
       JSONArray mainArr = JSONArray.parseArray(planDetail);
       Map<String,Object> dataMap = new HashMap<String, Object>();
       dataMap.put("mainArr", mainArr);
       dataMap.put("userId", UserUtils.getUser().getId());
       dataMap.put("operateType", operateType);
       int countSuccess = 0;
       try {
    	   countSuccess = internationalPlanService.planAdd(dataMap);
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
     * 获取航班性质
     * @param arrivalApt
     * @param departApt
     * @return
     */
    public int getAttrCode(String departApt,String arrivalApt){
    	int attrCode = internationalPlanService.getAttCode(departApt,arrivalApt);//如果返回为0说明是国内航班，大于0为国际航班
    	return attrCode;
    }
    /**
     * 删除计划
     * @param ids
     * @return
     */
    @RequestMapping(value="delete")
    @ResponseBody
    public boolean delete (String ids) {
    	int i = 0;
        boolean result = false;
        try {
        	if (ids!=null&&!ids.equals("")) {
        		Map<String, Object> paramMap = new HashMap<String, Object>();
        		paramMap.put("ids", ids);
        		i = internationalPlanService.delete(paramMap);
        	}
            if (i>0) {
            	  result = true;
			}     
        } catch (Exception e) {
            logger.error("删除数据失败：{}",e.getMessage());
        }
        return result;
    }
    /**
     * 打印
     * @param request
     * @param response
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response) {
        try {
            String  title = StringEscapeUtils.unescapeHtml4(request.getParameter("printTitle"));
            title = URLDecoder.decode(title,"UTF-8");
            JSONArray titleArray = JSONArray.parseArray(title);
            String fileName = "国际航班计划" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
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

            String excelTitle = "国际航班计划" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportExcel excel = new ExportExcel(excelTitle);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            JSONObject planObj = internationalPlanService.getGridData(paramMap);
            JSONArray plans = planObj.getJSONArray("rows");
            excel.setTitleData(titleArray);
            excel.setData(titleArray,plans);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("国际航班计划导出失败" + e.getMessage());
        }
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
     * 进入查看报文
     * @param model
     * @return
     */
    @RequestMapping(value = "showMsg")
    public String showMsg(Model model,String msgId) {
    	String msg = internationalPlanService.getMsg(msgId);
    	if(msg!=null){
    		msg = msg.replace("\r", "--");
    		model.addAttribute("msg", msg);
    	}else{
    		model.addAttribute("msg", "");
    	}
        return "prss/plan/internationalPlanShow";
    }
    
    /**
   	 * 从缓存中获取机场三字码、名称，航空公司三字码、名称，公务机通航性质编码
   	 */
   	public Model selectOptionData(Model mode) {
   		// 机场代码(代码使用三字码)、名称
   		JSONArray airportCodeSource = new JSONArray();
   		JSONObject obj = new JSONObject();
   		obj.put("icao_code", "");
   		obj.put("id", "");
   		obj.put("text", "");
   		airportCodeSource.add(obj);
   		JSONArray tmpAirportCodeSource = cacheService.getOpts("dim_airport", "airport_code","description_cn","icao_code");
   		airportCodeSource.addAll(tmpAirportCodeSource);
   		// 航空公司代码(代码使用三字码)、名称
   		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code", "airline_shortname","airline_code");
   		// 公务机通航性质编码
   		JSONArray propertyCodeSourceData = cacheService.getOpts("dim_flight_property", "property_code","property_shortname");
   		
        //公务机通航属性包含： D/Y,G/F,X/L,N/M,P/F,U/H,Z/X,Y/H
   		JSONArray propertyCodeSource = new JSONArray();
   		for(int i=0;i<propertyCodeSourceData.size();i++){
           	String allPropertyCode = "D/Y,G/F,X/L,N/M,P/F,U/H,Z/X,Y/H";
           	JSONObject jsonObj = propertyCodeSourceData.getJSONObject(i);
           	String propertyCode = jsonObj.getString("id");
           	if(!allPropertyCode.contains(propertyCode))
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
    
}
