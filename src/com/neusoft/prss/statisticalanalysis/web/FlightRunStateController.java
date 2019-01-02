package com.neusoft.prss.statisticalanalysis.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.statisticalanalysis.service.FlightRunStateService;

@Controller
@RequestMapping(value = "${adminPath}/flightRunState")
public class FlightRunStateController extends BaseController {

	@Resource
    private FlightRunStateService flightRunStateService;
	
	@RequestMapping(value = "showPage" )
    public String ShowPage(Model model) {
		Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -1);
    	String yesterday = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime());
    	HashMap<String,Object> totalMap = flightRunStateService.getPageData(yesterday);
    	int highTime = flightRunStateService.getHighTime(yesterday);
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("beginTime", yesterday);
    	paramMap.put("highTime", highTime);
    	HashMap<String,Object> totalHighMap = flightRunStateService.getHighData(paramMap);
    	
    	model.addAttribute("beginTime", yesterday);
    	model.addAttribute("totalMap", totalMap);
    	model.addAttribute("highTime", highTime);
    	model.addAttribute("totalHighMap", totalHighMap);
        return "prss/statisticalanalysis/flightRunState";
    }

	/**
     * 
     * Discription:航班运行状况页面.
     * 
     * @param request
     * @param response
     * @return:返回值意义
     * @author:yunwq
     * @update:2018年01月23日 yunwq [变更描述]
     */
    @RequestMapping(value = "searchPage" )
    public String SearchPage(Model model,
    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime) {
    	
    	HashMap<String,Object> totalMap = flightRunStateService.getPageData(beginTime);
    	if(totalMap == null ){
    		model.addAttribute("beginTime", beginTime);
    		model.addAttribute("msg", beginTime+"没有航班信息！");
    		return "prss/statisticalanalysis/flightRunState";
    	}
    	
    	int highTime = flightRunStateService.getHighTime(beginTime);
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("beginTime", beginTime);
    	paramMap.put("highTime", highTime);
    	HashMap<String,Object> totalHighMap = flightRunStateService.getHighData(paramMap);
    	
    	model.addAttribute("beginTime", beginTime);
    	model.addAttribute("totalMap", totalMap);
    	model.addAttribute("highTime", highTime);
    	model.addAttribute("totalHighMap", totalHighMap);

		return "prss/statisticalanalysis/flightRunState";
    }
    
    /**
     * 
     * Discription:航班运行状况折线图.
     * 
     * @param request
     * @param response
     * @return:返回值意义
     * @author:yunwq
     * @update:2018年01月23日 yunwq [变更描述]
     */
    @RequestMapping(value = "SearchImage" )
    @ResponseBody
    public JSONObject SearchImage(Model model,
    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime) {
    	
    	List<Map<String, Object>> dataList = flightRunStateService.getImageData(beginTime);
    	
    	Integer[] cgList = new Integer[24];	// 出港
		Integer[] jgList = new Integer[24];	// 进港
		Integer[] zjList = new Integer[24]; // 总计
		
		for(Map<String, Object> data : dataList){
			String hour = (String)data.get("HOUR");
			int i = Integer.parseInt(hour);
			
			cgList[i] = Integer.valueOf(data.get("OUT_NUM").toString());
			jgList[i] = Integer.valueOf(data.get("IN_NUM").toString());
			zjList[i] = Integer.valueOf(data.get("TOTAL").toString());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cg", cgList);
		resultMap.put("jg", jgList);
		resultMap.put("zj", zjList);

    	
		return JSONObject.parseObject(JSONObject.toJSONString(resultMap));
    }
}
