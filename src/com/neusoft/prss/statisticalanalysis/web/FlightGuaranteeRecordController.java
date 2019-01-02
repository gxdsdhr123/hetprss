package com.neusoft.prss.statisticalanalysis.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.imax.bean.ImaxIndexBean;
import com.neusoft.prss.statisticalanalysis.service.FlightGuaranteeRecordService;

@Controller
@RequestMapping(value = "${adminPath}/flightGuaranteeRecord")
public class FlightGuaranteeRecordController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
    private FlightGuaranteeRecordService flightGuaranteeRecordService;
	
    @RequestMapping(value = "showPage" )
    public String ShowPage(Model model) {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -1);
    	String yesterday = new SimpleDateFormat( "yyyyMMdd").format(cal.getTime());
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("beginTime", yesterday);
    	
    	List<HashMap<String, Object>> totalList = flightGuaranteeRecordService.getTotalData(paramMap);
    	if(totalList.size() == 0){
    		model.addAttribute("beginTime", yesterday);
        	model.addAttribute("msg", yesterday+"没有数据！");
            return "prss/statisticalanalysis/flightGuaranteeRecord";
    	}
    	for(HashMap<String, Object> map : totalList){
    		if("东航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("dh_map", map);
    		}
    		if("南航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("nh_map", map);
    		}
    		if("海航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("hh_map", map);
    		}
    		if("BGS外航".equals(map.get("AIRLINE"))){
    			model.addAttribute("bgs_map", map);
    		}
    	}
    	
    	int highTime = flightGuaranteeRecordService.getHighTime(yesterday);
    	paramMap.put("highTime", highTime);
    	List<HashMap<String, Object>> totalHighList = flightGuaranteeRecordService.getTotalData(paramMap);
    	for(HashMap<String, Object> map : totalHighList){
    		if("东航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("dh_high_map", map);
    		}
    		if("南航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("nh_high_map", map);
    		}
    		if("海航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("hh_high_map", map);
    		}
    		if("BGS外航".equals(map.get("AIRLINE"))){
    			model.addAttribute("bgs_high_map", map);
    		}
    	}
    	
    	model.addAttribute("beginTime", yesterday);
    	model.addAttribute("highTime", highTime);
        return "prss/statisticalanalysis/flightGuaranteeRecord";
    }
    
    @RequestMapping(value = "searchPage" )
    public String SearchPage(Model model,
    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime) {
    	
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("beginTime", beginTime);
    	
    	List<HashMap<String, Object>> totalList = flightGuaranteeRecordService.getTotalData(paramMap);
    	if(totalList.size() == 0){
    		model.addAttribute("beginTime", beginTime);
        	model.addAttribute("msg", beginTime+"没有数据！");
            return "prss/statisticalanalysis/flightGuaranteeRecord";
    	}
    	for(HashMap<String, Object> map : totalList){
    		if("东航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("dh_map", map);
    		}
    		if("南航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("nh_map", map);
    		}
    		if("海航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("hh_map", map);
    		}
    		if("BGS外航".equals(map.get("AIRLINE"))){
    			model.addAttribute("bgs_map", map);
    		}
    	}
    	
    	int highTime = flightGuaranteeRecordService.getHighTime(beginTime);
    	paramMap.put("highTime", highTime);
    	List<HashMap<String, Object>> totalHighList = flightGuaranteeRecordService.getTotalData(paramMap);
    	for(HashMap<String, Object> map : totalHighList){
    		if("东航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("dh_high_map", map);
    		}
    		if("南航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("nh_high_map", map);
    		}
    		if("海航系".equals(map.get("AIRLINE"))){
    			model.addAttribute("hh_high_map", map);
    		}
    		if("BGS外航".equals(map.get("AIRLINE"))){
    			model.addAttribute("bgs_high_map", map);
    		}
    	}
    	
    	model.addAttribute("beginTime", beginTime);
    	model.addAttribute("highTime", highTime);
        return "prss/statisticalanalysis/flightGuaranteeRecord";
    }
    
    @RequestMapping(value = "showImage" )
    @ResponseBody
    public ResponseVO<ImaxIndexBean> ShowImage(
    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime) {
    	// 航班运行情况（图）
    	Map<String, Integer[]> flightNums = new HashMap<String, Integer[]>();
    	ImaxIndexBean bean = new ImaxIndexBean();
		try {
			// 查询航班运行情况
			List<Map<String, Object>> dataList = flightGuaranteeRecordService.getFlightNumsList_index(beginTime);
			List<Map<String, Object>> gjDataList = flightGuaranteeRecordService.getgjDataList(beginTime);

			Integer[] cgList = new Integer[24];	// 出港
			Integer[] jgList = new Integer[24];	// 进港
			Integer[] gjList = new Integer[24];	// 外航
			
			for(Map<String, Object> data : dataList){
				String hour = (String)data.get("hour");
				int i = Integer.parseInt(hour);
				
				cgList[i] = (Integer)data.get("cg");
				jgList[i] = (Integer)data.get("jg");
			}
			for(Map<String, Object> data : gjDataList){
				String hour = (String)data.get("hour");
				int i = Integer.parseInt(hour);
				
				gjList[i] = (Integer)data.get("gj");
			}
			
			flightNums.put("cg", cgList);
			flightNums.put("jg", jgList);
			flightNums.put("gj", gjList);
			bean.setFlightNums(flightNums);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        return ResponseVO.<ImaxIndexBean>build().setData(bean);
    }
}
