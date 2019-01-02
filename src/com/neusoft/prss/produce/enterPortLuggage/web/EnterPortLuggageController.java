package com.neusoft.prss.produce.enterPortLuggage.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.prss.produce.enterPortLuggage.service.EnterPortLuggageService;

/**
 * 进港行李交接单
 * @author douxf
 * @version 2017-12-23
 */
@Controller
@RequestMapping(value = "${adminPath}/arrival/produce")
public class EnterPortLuggageController {
	
	@Autowired
	EnterPortLuggageService enterPortLuggageService;
	
	@RequestMapping(value = "skip")
	public String view(){
		return "prss/produce/enterPortLuggage/enterPortLuggage";
	}
	
	@RequestMapping(value = "list")
	@ResponseBody
	public Map<String,Object> getList(int pageSize, int pageNumber, String sortName, String sortOrder){
		Map<String,Object> param = new HashMap<String,Object>();
		int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("sortName", sortName);
        param.put("sortOrder", sortOrder);
        Map<String,Object> result = enterPortLuggageService.listBillList(param);
		return result;
	}
	
	@RequestMapping(value = "form")
	public String from(Model model){
		List<Map<String,Object>> data = enterPortLuggageService.selChaLi();

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);
		model.addAttribute("data", data);
		model.addAttribute("newData", dateNowStr);
		
		
		return "prss/produce/enterPortLuggage/enterPortLuggageAdd";
	}
	
	@RequestMapping(value = "initTable")
	@ResponseBody
	public String initTable(){
		List<Map<String, Object>> list = enterPortLuggageService.getReceiverListDou();
		String result= JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
		return result;
	}
	
	
	
	@RequestMapping(value = "findInfo")
	@ResponseBody
	public Map<String,Object> findInfo(Model model, String flightDate,String flightNumber){
		Map<String,Object> parem = new HashMap<String,Object>();
		parem.put("flightDate",flightDate);
		parem.put("flightNumber", flightNumber);
		Map<String,Object> result =  enterPortLuggageService.findInfo(parem);
		
		return result;
	}
	
	
	@RequestMapping(value = "save")
	@ResponseBody
	public int  createSave(String data,String flightDate,String flightNumber,String aircraftNumber,String actstandCode,String ata,String chali){
		int id = 0;
		int result = 0;
		Map<String,Object> bgh = new HashMap<String,Object>();
		bgh.put("id",id);
		bgh.put("flightDate",flightDate.replace("-", ""));
		bgh.put("flightNumber",flightNumber);
		bgh.put("aircraftNumber",aircraftNumber);
		bgh.put("actstandCode",actstandCode);
		bgh.put("ata",ata);
		bgh.put("chali",chali);
		
		
		
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONArray dataArray = JSONArray.parseArray(data);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < dataArray.size(); i++){
			
			map = new HashMap<String,Object>();
			if(dataArray.getJSONObject(i).getString("collCode") != null && dataArray.getJSONObject(i).getString("collCode") !="" && dataArray.getJSONObject(i).getString("collCode").length()!=0){
				map.put("collCode", dataArray.getJSONObject(i).getString("collCode"));
			}else{
				return result;
			}
			if("[]".equals(dataArray.getJSONObject(i).getString("incGoods"))){
				return result;
			}else if("/".equals(dataArray.getJSONObject(i).getString("incGoods"))){
				return result;
			}else if(dataArray.getJSONObject(i).getString("incGoods") != null && dataArray.getJSONObject(i).getString("incGoods")!="" && dataArray.getJSONObject(i).getString("incGoods").length()!=0){
				map.put("incGoods", dataArray.getJSONObject(i).getString("incGoods"));
			}else{
				return result;
			}
			if(dataArray.getJSONObject(i).getString("renark") != null && dataArray.getJSONObject(i).getString("renark")!="" && dataArray.getJSONObject(i).getString("renark").length()!=0){
				map.put("renark", dataArray.getJSONObject(i).getString("renark"));
			}else{
				return result;
			}
			if(dataArray.getJSONObject(i).getString("operatorDate") != null && dataArray.getJSONObject(i).getString("operatorDate")!="" && dataArray.getJSONObject(i).getString("operatorDate").length()!=0){
				map.put("operatorDate", dataArray.getJSONObject(i).getString("operatorDate"));
			}else{
				return result;
			}
			if(dataArray.getJSONObject(i).getString("receiver") != null && dataArray.getJSONObject(i).getString("receiver")!="" && dataArray.getJSONObject(i).getString("receiver").length()!=0){
				map.put("receiver", dataArray.getJSONObject(i).getString("receiver"));
			}else{
				return result;
			}
			if(dataArray.getJSONObject(i).getString("receiveTime") != null && dataArray.getJSONObject(i).getString("receiveTime")!="" && dataArray.getJSONObject(i).getString("receiveTime").length()!=0){
				map.put("receiveTime", dataArray.getJSONObject(i).getString("receiveTime"));
			}else{
				return result;
			}
			
			list.add(map);
		}
		
		result =  enterPortLuggageService.saveBillGoos(list,bgh);
		return result;
	}
	
	@RequestMapping(value = "revamp")
	public String skipRevamp(Model model,String mainId){
		List<Map<String,Object>> data = enterPortLuggageService.selChaLi();
		Map<String,Object> mianData =  enterPortLuggageService.findMainI(mainId);
		List<Map<String,Object>> bData =  enterPortLuggageService.findB(mainId);
		List<Map<String,Object>> mainList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<bData.size();i++){
			Map<String,Object> goods = bData.get(i);
			mainList.add(goods);
		}
		String mainValue  = JSON.toJSONString(mainList,SerializerFeature.WriteMapNullValue);
		model.addAttribute("data", data);
		model.addAttribute("mianData", mianData);
		model.addAttribute("detailList", mainValue);
		return "prss/produce/enterPortLuggage/enterPortLuggageRevamp";
	}
	
	@RequestMapping(value = "update")
	@ResponseBody
	public int renewal(String data,String flightDate,String flightNumber,String aircraftNumber,String actstandCode,String ata,String chali,String id){
		Map<String,Object> bgh = new HashMap<String,Object>();
		bgh.put("id",id);
		bgh.put("flightDate",flightDate.replace("-", ""));
		bgh.put("flightNumber",flightNumber);
		bgh.put("aircraftNumber",aircraftNumber);
		bgh.put("actstandCode",actstandCode);
		bgh.put("ata",ata);
		bgh.put("chali",chali);
		int result = 0;
		
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONArray dataArray = JSONArray.parseArray(data);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < dataArray.size(); i++){
			map = new HashMap<String,Object>();
			if(dataArray.getJSONObject(i).getString("ID")!=null){
				map.put("id", dataArray.getJSONObject(i).getString("ID"));
			}
			
			if(dataArray.getJSONObject(i).getString("COLLCODE") != null && dataArray.getJSONObject(i).getString("COLLCODE")!="" && dataArray.getJSONObject(i).getString("COLLCODE").length()!=0){
				map.put("collCode", dataArray.getJSONObject(i).getString("COLLCODE"));
			}else{
				return result;
			}
			
			if("[]".equals(dataArray.getJSONObject(i).getString("INCGOODS"))){
				return result;
			}else if("/".equals(dataArray.getJSONObject(i).getString("INCGOODS"))){
				return result;
			}else if(dataArray.getJSONObject(i).getString("INCGOODS") != null && dataArray.getJSONObject(i).getString("INCGOODS")!="" && dataArray.getJSONObject(i).getString("INCGOODS").length()!=0){
				map.put("incGoods", dataArray.getJSONObject(i).getString("INCGOODS"));
			}else{
				return result;
			}
			
			if(dataArray.getJSONObject(i).getString("RENARK") != null && dataArray.getJSONObject(i).getString("RENARK")!="" && dataArray.getJSONObject(i).getString("RENARK").length()!=0){
				map.put("renark", dataArray.getJSONObject(i).getString("RENARK"));
			}else{
				return result;
			}
			
			if(dataArray.getJSONObject(i).getString("OPERATORDATE") != null && dataArray.getJSONObject(i).getString("OPERATORDATE")!="" && dataArray.getJSONObject(i).getString("OPERATORDATE").length()!=0){
				map.put("operatorDate", dataArray.getJSONObject(i).getString("OPERATORDATE"));
			}else{
				return result;
			}
			
			if(dataArray.getJSONObject(i).getString("RECEIVER") != null && dataArray.getJSONObject(i).getString("RECEIVER")!="" && dataArray.getJSONObject(i).getString("RECEIVER").length()!=0){
				map.put("receiver", dataArray.getJSONObject(i).getString("RECEIVER"));
			}else{
				return result;
			}
			
			if(dataArray.getJSONObject(i).getString("RECEIVETIME") != null && dataArray.getJSONObject(i).getString("RECEIVETIME")!="" && dataArray.getJSONObject(i).getString("RECEIVETIME").length()!=0){
				map.put("receiveTime", dataArray.getJSONObject(i).getString("RECEIVETIME"));
			}else{
				return result;
			}
			
			list.add(map);
		}
		result = enterPortLuggageService.updateBillGoos(list,bgh);
		return result;
		
	}
	
	@RequestMapping(value = "DelRevamp")
	@ResponseBody
	public int delRevamp(String id){
		int result =  enterPortLuggageService.delDoods(id);
		return result;
	}
	
	@RequestMapping(value ="DelRevampMamin")
	@ResponseBody
	public int DelRevampMamin(String id,String douId){
		int resultI =  enterPortLuggageService.delDoodsMain(id);
		int resultII = enterPortLuggageService.delDoods(douId);
		int result = resultI + resultII;
		return result;
	}
	
	@RequestMapping(value ="findMainAndB")
	@ResponseBody
	public String findMainAndB(String id){
		String result = enterPortLuggageService.findMainAndB(id);
		return result;
	}
	
	
	
	@RequestMapping(value ="delgoodsB")
	@ResponseBody
	public int delgoodsB(String id){
		int result = enterPortLuggageService.delgoodsB(id);
		return  result;
	}
	
	
	@RequestMapping(value ="countGulf")
	@ResponseBody
	public String countGulf(String flightDate,String flightNumber){
		Map<String,Object> parem = new HashMap<String,Object>();
		parem.put("flightDate", flightDate.replace("-", ""));
		parem.put("flightNumber", flightNumber);
		String result = enterPortLuggageService.countGulf(parem);
		return  result;
	}
	
	
}










