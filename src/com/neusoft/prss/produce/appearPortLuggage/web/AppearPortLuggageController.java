package com.neusoft.prss.produce.appearPortLuggage.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.prss.produce.appearPortLuggage.service.AppearPortLuggageService;
import com.neusoft.prss.produce.enterPortLuggage.service.EnterPortLuggageService;

/**
 * 出港行李交接单
 * @author douxf
 * @version 2018-1-3
 */

@Controller
@RequestMapping(value = "${adminPath}/appear/produce")
public class AppearPortLuggageController {
	
	@Autowired
	AppearPortLuggageService appearPortLuggageService;
	
	@Autowired
	EnterPortLuggageService enterPortLuggageService;
	
	@RequestMapping("skip")
	public String view(){
		return "prss/produce/appearPortLuggage/appearPortLuggage";
	}
	
	@RequestMapping("list")
	@ResponseBody
	public Map<String,Object> list(int pageSize, int pageNumber, String sortName, String sortOrder){
		Map<String,Object> param = new HashMap<String,Object>();
		int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("sortName", sortName);
        param.put("sortOrder", sortOrder);
        Map<String,Object> result = appearPortLuggageService.listDouList(param);
		return result;
	}
	
	@RequestMapping("newly")
	public String newly(Model model){
		List<Map<String,Object>> data = enterPortLuggageService.selChaLi();
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);
		
		
		model.addAttribute("newData", dateNowStr);
		model.addAttribute("data", data);
		return "prss/produce/appearPortLuggage/appearPortLuggageAdd";
	}
	
	@RequestMapping("jsonData")
	@ResponseBody
	public String jsonData(){
		List<Map<String,Object>> list = enterPortLuggageService.getReceiverListDou();
		return JSON.toJSONString(list,SerializerFeature.WriteMapNullValue);
		
	}
	
	
	
	@RequestMapping("save")
	@ResponseBody
	public int createSave(String data,String flightDate,String flightNumber,String aircraftNumber,String actstandCode,String chali){
		int result = 0;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("flightDate",flightDate.replace("-", ""));
		param.put("flightNumber",flightNumber);
		param.put("aircraftNumber",aircraftNumber);
		param.put("actstandCode",actstandCode);
		param.put("chali", chali);
		
		
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONArray datarray = JSONArray.parseArray(data);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<datarray.size();i++){
			map = new HashMap<String,Object>();
			if(datarray.getJSONObject(i).getString("bootCode")!=null && datarray.getJSONObject(i).getString("bootCode")!="" && datarray.getJSONObject(i).getString("bootCode").length()!=0){
				map.put("bootCode", datarray.getJSONObject(i).getString("bootCode"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("bootNumber")!=null && datarray.getJSONObject(i).getString("bootNumber")!="" && datarray.getJSONObject(i).getString("bootNumber").length()!=0){
				map.put("bootNumber", datarray.getJSONObject(i).getString("bootNumber"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("dest")!=null && datarray.getJSONObject(i).getString("dest")!="" && datarray.getJSONObject(i).getString("dest").length()!=0){
				map.put("dest", datarray.getJSONObject(i).getString("dest"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("status")!=null && datarray.getJSONObject(i).getString("status")!="" && datarray.getJSONObject(i).getString("status").length()!=0){
				map.put("status", datarray.getJSONObject(i).getString("status"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("numPack")!=null && datarray.getJSONObject(i).getString("numPack")!="" && datarray.getJSONObject(i).getString("numPack").length()!=0){
				map.put("numPack", datarray.getJSONObject(i).getString("numPack"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("receiver")!=null && datarray.getJSONObject(i).getString("receiver")!="" && datarray.getJSONObject(i).getString("receiver").length()!=0){
				map.put("receiver", datarray.getJSONObject(i).getString("receiver"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("operatorDate")!=null && datarray.getJSONObject(i).getString("operatorDate")!="" && datarray.getJSONObject(i).getString("operatorDate").length()!=0){
				map.put("operatorDate", datarray.getJSONObject(i).getString("operatorDate"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("receiveTime")!=null && datarray.getJSONObject(i).getString("receiveTime")!="" && datarray.getJSONObject(i).getString("receiveTime").length()!=0){
				map.put("receiveTime", datarray.getJSONObject(i).getString("receiveTime"));
			}else{
				return result;
			}
			
			list.add(map);
			
		}
		
		result = appearPortLuggageService.saveMain(param,list);
		return result;
	}
	
	
	@RequestMapping("DelRevamp")
	@ResponseBody
	public int delRevamp(String id){
		int result =  appearPortLuggageService.delMainAndB(id);
		return result;
	}
	
	@RequestMapping("DelRevampB")
	@ResponseBody
	public int delRevampB(String id){
		int result =  appearPortLuggageService.delB(id);
		return result;
	}
	
	
	
	@RequestMapping("revamp")
	public String modification(String id,Model model){
		List<Map<String,Object>> data = enterPortLuggageService.selChaLi();
		Map<String,Object> mainA = appearPortLuggageService.selMain(id);
		List<Map<String,Object>> listB = appearPortLuggageService.selB(id);
		List<Map<String,Object>> guflB = new ArrayList<Map<String,Object>>();
		for(int i=0;i<listB.size();i++){
			Map<String,Object> goods = listB.get(i);
			guflB.add(goods);
		}
		String bValues = JSON.toJSONString(guflB,SerializerFeature.WriteMapNullValue);
		model.addAttribute("data", data);
		model.addAttribute("detailList", bValues);
		model.addAttribute("mainA",mainA);
		return "prss/produce/appearPortLuggage/appearPortLuggageRevamp";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public int recompose(String data,String id,String flightDate,String flightNumber,String aircraftNumber,String actstandCode,String chali){
		Map<String,Object> param  = new HashMap<String,Object>();
		param.put("id", id);
		param.put("flightDate", flightDate);
		param.put("flightNumber", flightNumber);
		param.put("aircraftNumber", aircraftNumber);
		param.put("actstandCode", actstandCode);
		param.put("chali", chali);
		
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONArray datarray = JSONArray.parseArray(data);
		Map<String,Object> map = new HashMap<String,Object>();
		int result = 0;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<datarray.size();i++){
			map = new HashMap<String,Object>();
			
			if(datarray.getJSONObject(i).getString("ID")!=null && datarray.getJSONObject(i).getString("ID")!="" && datarray.getJSONObject(i).getString("ID").length()!=0){
				map.put("id", datarray.getJSONObject(i).getString("ID"));
			}else{
				map.put("id", "-1");
			}
			
			if(datarray.getJSONObject(i).getString("BOOT_CODE")!=null && datarray.getJSONObject(i).getString("BOOT_CODE")!="" && datarray.getJSONObject(i).getString("BOOT_CODE").length()!=0){
				map.put("bootCode", datarray.getJSONObject(i).getString("BOOT_CODE"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("BOOT_NUMBER")!=null && datarray.getJSONObject(i).getString("BOOT_NUMBER")!="" && datarray.getJSONObject(i).getString("BOOT_NUMBER").length()!=0){
				map.put("bootNumber", datarray.getJSONObject(i).getString("BOOT_NUMBER"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("DEST")!=null && datarray.getJSONObject(i).getString("DEST")!="" && datarray.getJSONObject(i).getString("DEST").length()!=0){
				map.put("dest", datarray.getJSONObject(i).getString("DEST"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("STATUS")!=null && datarray.getJSONObject(i).getString("STATUS")!="" && datarray.getJSONObject(i).getString("STATUS").length()!=0){
				map.put("status", datarray.getJSONObject(i).getString("STATUS"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("NUM_PACK")!=null && datarray.getJSONObject(i).getString("NUM_PACK")!="" && datarray.getJSONObject(i).getString("NUM_PACK").length()!=0){
				map.put("numPack", datarray.getJSONObject(i).getString("NUM_PACK"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("RECEIVER")!=null && datarray.getJSONObject(i).getString("RECEIVER")!="" && datarray.getJSONObject(i).getString("RECEIVER").length()!=0){
				map.put("receiver", datarray.getJSONObject(i).getString("RECEIVER"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("OPERATOR_DATE")!=null && datarray.getJSONObject(i).getString("OPERATOR_DATE")!="" && datarray.getJSONObject(i).getString("OPERATOR_DATE").length()!=0){
				map.put("operatorDate", datarray.getJSONObject(i).getString("OPERATOR_DATE"));
			}else{
				return result;
			}
			
			if(datarray.getJSONObject(i).getString("RECEIVE_TIME")!=null && datarray.getJSONObject(i).getString("RECEIVE_TIME")!="" && datarray.getJSONObject(i).getString("RECEIVE_TIME").length()!=0){
				map.put("receiveTime", datarray.getJSONObject(i).getString("RECEIVE_TIME"));
			}else{
				return result;
			}
			list.add(map);
		}
		result = appearPortLuggageService.updateMinaB(param, list);
		
		return result;
	}
	
	
	@RequestMapping("delGulf")
	@ResponseBody
	public void delGulf(String id){
		appearPortLuggageService.delBGufl(id);
	}
	
	@RequestMapping("countAppGufl")
	@ResponseBody
	public String countAppGufl(String flightDate,String flightNumber){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("flightDate", flightDate.replace("-", ""));
		param.put("flightNumber", flightNumber);
		String result = appearPortLuggageService.countAppGufl(param);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
