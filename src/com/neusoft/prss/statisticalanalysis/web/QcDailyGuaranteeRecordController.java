package com.neusoft.prss.statisticalanalysis.web;

import java.math.BigDecimal;
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
import com.neusoft.prss.statisticalanalysis.service.QcDailyGuaranteeRecordService;

/**
 * 清舱操作日航班保障记录统计分析
 * @author wangtg
 * @date 2018-03-28
 * @version v1.1
 * @update 因客户变更需求 2018-04-02
 */
@Controller
@RequestMapping(value = "${adminPath}/qcDailyGuaranteeRecord")
public class QcDailyGuaranteeRecordController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
    private QcDailyGuaranteeRecordService qcDailyGuaranteeRecordService;
	
    @RequestMapping(value = "showPage" )
    public String ShowPage() {
        return "prss/statisticalanalysis/qcDailyGuaranteeRecord";
    }
    
    
    @RequestMapping(value = "searchPage" )
    public String SearchPage(Model model,
    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime) {
    	
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("beginTime", beginTime);
    	
    	List<HashMap<String, Object>> totalList = qcDailyGuaranteeRecordService.getTotalData(paramMap);
    	if(totalList.size() == 0){
    		model.addAttribute("beginTime", beginTime);
        	model.addAttribute("msg", beginTime+"没有数据！");
            return "prss/statisticalanalysis/qcDailyGuaranteeRecord";
    	}
    	for(HashMap<String, Object> map : totalList){
    		
    		if("南航".equals(map.get("AIRLINE"))){
    			model.addAttribute("nh_map", map);
    		}
    		//业务要求BGS外航要包含福州航空
    		if("BGS外航".equals(map.get("AIRLINE"))){
    			model.addAttribute("bgs_map", map);
    		}
    		if("厦航".equals(map.get("AIRLINE"))){
    			model.addAttribute("xh_map", map);
    		}
    	}
    	
    	int highTime = qcDailyGuaranteeRecordService.getHighTime(beginTime);
    	paramMap.put("highTime", highTime);
    	List<HashMap<String, Object>> totalHighList = qcDailyGuaranteeRecordService.getTotalData(paramMap);
    	for(HashMap<String, Object> map : totalHighList){
    		
    		if("南航".equals(map.get("AIRLINE"))){
    			model.addAttribute("nh_high_map", map);
    		}
    		if("BGS外航".equals(map.get("AIRLINE"))){
    			model.addAttribute("bgs_high_map", map);
    		}
    		if("厦航".equals(map.get("AIRLINE"))){
    			model.addAttribute("xh_high_map", map);
    		}
    	}
    	
    	model.addAttribute("beginTime", beginTime);
    	model.addAttribute("highTime", highTime);
        return "prss/statisticalanalysis/qcDailyGuaranteeRecord";
    }
    
    
//    @RequestMapping(value = "showImage" )
//    @ResponseBody
//    public ResponseVO<ImaxIndexBean> ShowImage(
//    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime) {
//    	// 航班运行情况（图）
//    	Map<String, Integer[]> flightNums = new HashMap<String, Integer[]>();
//    	ImaxIndexBean bean = new ImaxIndexBean();
//		try {
//			// 查询航班运行情况
//			List<Map<String, Object>> dataList = qcDailyGuaranteeRecordService.getFlightNumsList_index(beginTime);
//			
//
//			Integer[] sfList = new Integer[24];	// 始发航班数
//			Integer[] gzList = new Integer[24];	// 过站航班数
//			Integer[] zzList = new Integer[24]; // 终止航班数
//			
//			for(Map<String, Object> data : dataList){
//				String hour = (String)data.get("hour");
//				int i = Integer.parseInt(hour);
//				
//				sfList[i] = (Integer)data.get("sf");
//				gzList[i] = (Integer)data.get("gz");
//				zzList[i] = (Integer)data.get("zz");
//			}
//			flightNums.put("sf", sfList);
//			flightNums.put("gz", gzList);
//			flightNums.put("zz", zzList);
//			bean.setFlightNums(flightNums);
//			
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//        return ResponseVO.<ImaxIndexBean>build().setData(bean);
//    }
    
    @RequestMapping(value = "showImage" )
    @ResponseBody
    public ResponseVO<ImaxIndexBean> ShowImage(
    		@RequestParam(value="beginTime",required=false,defaultValue="null") String beginTime) {
    	// 航班运行情况（图）
    	Map<String, Integer[]> flightNums = new HashMap<String, Integer[]>();
    	ImaxIndexBean bean = new ImaxIndexBean();
		try {
			// 查询航班运行情况
			List<Map<String, Object>> dataList = qcDailyGuaranteeRecordService.getFlightNumsList_index(beginTime);
			

			Integer[] nhList = new Integer[24];	// 南航系
			Integer[] bgsList = new Integer[24];// BGS外航
			
			
			for(Map<String, Object> data : dataList){
				String hour = (String)data.get("FLIGHT_HOUR");
				int i = Integer.parseInt(hour);
				if("南航系".equals(data.get("AIRLINE"))){
					BigDecimal c=(BigDecimal) data.get("COUNT");
					nhList[i]=((BigDecimal)data.get("COUNT")).intValue();
				}
				if("BGS外航".equals(data.get("AIRLINE"))){
					bgsList[i]= ((BigDecimal)data.get("COUNT")).intValue();
				}
			}
			flightNums.put("nh", nhList);
			flightNums.put("bgs", bgsList);
			bean.setFlightNums(flightNums);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        return ResponseVO.<ImaxIndexBean>build().setData(bean);
    }
}
