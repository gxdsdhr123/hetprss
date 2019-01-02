package com.neusoft.prss.flightdynamic.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.flightdynamic.service.FltProgressService;

/**
 * 航班保障进度图
 * 
 * @author baochl
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/fltProgress")
public class FltProgressController extends BaseController {
	@Autowired
	private FltProgressService progressService;

	/**
	 * 跳转到主页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "")
	public String main(Model model, HttpServletRequest request) {
		return "prss/flightdynamic/fltProgressMain";
	}

	/**
	 * 加载表格
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "gridData")
	public String gridData(Model model, HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		JSONArray dataList = progressService.getDataList(params);
		return dataList.toString();
	}
	/**
	 * 
	 * @param model
	 * @param colCode 列编号
	 * @param infltid 进港航班号
	 * @param outfltid 出港航班号
	 * @return
	 */
	@RequestMapping(value = "detail")
	public String detail(Model model,String colCode,String infltid,String outfltid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("colCode", colCode);
		String fltids = "";
		if(StringUtils.isNotEmpty(infltid)){
			fltids+=infltid;
		}
		if(StringUtils.isNotEmpty(outfltid)){
			if(StringUtils.isNotEmpty(fltids)){
				fltids+=",";
			}
			fltids+=outfltid;
		}
		params.put("fltids", fltids);
		JSONObject inFlight = new JSONObject();
		if(StringUtils.isNotEmpty(infltid)){
			inFlight = progressService.getFltById(infltid);
		}
		JSONObject outFlight = new JSONObject();
		if(StringUtils.isNotEmpty(outfltid)){
			outFlight = progressService.getFltById(outfltid);
		}
		JSONArray jobs = progressService.getJobTaskList(params);
		model.addAttribute("inFlight", inFlight);
		model.addAttribute("outFlight", outFlight);
		model.addAttribute("jobs",jobs);
		return "prss/flightdynamic/fltProgressDetail";
	}
	
	/**
	 * 保障图页面
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "openImage")
	public String ensureList(Model model,String inFltid, String outFltid) {
		model.addAttribute("inFltid", inFltid);
		model.addAttribute("outFltid", outFltid);
		return "prss/flightdynamic/ensureList";
	}
}
