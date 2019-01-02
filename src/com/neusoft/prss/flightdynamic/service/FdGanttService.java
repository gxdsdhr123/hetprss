/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午2:28:25
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.flightdynamic.dao.FdGanttDao;

@Service
public class FdGanttService {
	@Resource
	private FdGanttDao fdGanttDao;

	public JSONArray getFdYData() {
		return fdGanttDao.getFdYData();
	}

	public String ganttData(Map<String, Object> params) {
		JSONArray result = new JSONArray();
		Map<String, Object> param = new HashMap<String, Object>();
		if (!params.isEmpty()) {
			for (String key : params.keySet()) {
				if (!"".equals(params.get(key)) && params.get(key) != null) {
					if ("aircraft".equals(key) || "apron".equals(key)
							|| "terminal".equals(key) || "airline".equals(key)) {
						JSONArray val = (JSONArray) params.get(key);
						String value = val.toString().replaceAll("\"", "'").replace("[", "").replace("]", "");
						if (!"''".equals(value)) {
							param.put(key, value);
						}
					} else {
						param.put(key, params.get(key));
					}
				}
			}
		}
		JSONArray res = fdGanttDao.getFdData(param);
		for(int i=0;i<res.size();i++){
			JSONObject r = res.getJSONObject(i);
			JSONObject json = new JSONObject();
			json.put("id",r.getString("id"));
			json.put("infltid",r.getString("in_fltid"));
			json.put("outfltid",r.getString("out_fltid"));
			json.put("field", r.getString("actstand")!=null?r.getString("actstand"):"DP");
			json.put("text",mergeFltNum(r.getString("in_fltno"),r.getString("out_fltno")));
			json.put("start", r.getString("start"));
			json.put("end", r.getString("end"));
			if("3".equals(r.getString("status"))){
				json.put("color", "green");
			}else if("2".equals(r.getString("status"))){
				json.put("color", "blue");
			}else if("1".equals(r.getString("status"))){
				json.put("color", "red");
			}else if("0".equals(r.getString("status"))){
				json.put("color", "gray");
			}
			result.add(json);
		}
		String jsonStr = JSON.toJSONString(result).replaceAll("null", "");
		return jsonStr;
	}
	
	public List<List<String>> getGanttDetail(String id, String inFltid, String outFltid) {
		String status = fdGanttDao.getGanttStatus(id);
		List<List<String>> detail = new ArrayList<List<String>>();
		List<String> page1 = new ArrayList<String>();
		List<String> page2 = new ArrayList<String>();
		List<String> page3 = new ArrayList<String>();
		if(outFltid == null){
			JSONObject r = fdGanttDao.getGanttInfo(inFltid);
			page1.add((r.getString("depApt")==null?"":r.getString("depApt"))+"-北京 ("+status+")");
			page1.add("机号："+r.getString("actNumber")+"    机型："+r.getString("actType"));
			page1.add("计落："+(r.getString("sta")!=null?r.getString("sta").substring(11):"")+" 预落："+(r.getString("eta")!=null?r.getString("eta").substring(11):"")+" 实落："+(r.getString("ata")!=null?r.getString("ata").substring(11):""));
			page2.add("到达口："+(r.getString("gate")!=null?r.getString("gate"):""));
			page2.add("进港包台："+(r.getString("crsl")!=null?r.getString("crsl"):""));
			page3.add("到达口视频");
			page3.add("进港包台视频");
			page3.add("机位视频");
		}else if(inFltid == null){
			JSONObject r = fdGanttDao.getGanttInfo(outFltid);
			page1.add("北京-"+(r.getString("arrApt")==null?"":r.getString("arrApt"))+" ("+status+")");
			page1.add("机号："+r.getString("actNumber")+"    机型："+r.getString("actType"));
			page1.add("计起："+(r.getString("std")!=null?r.getString("std").substring(11):"")+" 预起："+(r.getString("etd")!=null?r.getString("etd").substring(11):"")+" 实起："+(r.getString("atd")!=null?r.getString("atd").substring(11):""));
			page2.add("值机柜台："+(r.getString("counter")!=null?r.getString("counter"):""));
			page2.add("出港分拣口："+(r.getString("chute")!=null?r.getString("chute"):""));
			page2.add("登机口："+(r.getString("gate")!=null?r.getString("gate"):""));
			page3.add("机位视频");
			page3.add("值机柜台视频");
			page3.add("出港分拣口视频");
			page3.add("登机口视频");
		}else{
			JSONObject in = fdGanttDao.getGanttInfo(inFltid);
			JSONObject out = fdGanttDao.getGanttInfo(outFltid);
			page1.add((in.getString("depApt")==null?"":in.getString("depApt"))+"-北京-"+(out.getString("arrApt")==null?"":out.getString("arrApt"))+" ("+status+")");
			page1.add("计落："+(in.getString("sta")!=null?in.getString("sta").substring(11):"")+" 预落："+(in.getString("eta")!=null?in.getString("eta").substring(11):"")+" 实落："+(in.getString("ata")!=null?in.getString("ata").substring(11):""));
			page1.add("计起："+(out.getString("std")!=null?out.getString("std").substring(11):"")+" 预起："+(out.getString("etd")!=null?out.getString("etd").substring(11):"")+" 实起："+(out.getString("atd")!=null?out.getString("atd").substring(11):""));
			page2.add("到达口："+(in.getString("gate")!=null?in.getString("gate"):""));
			page2.add("进港包台： "+(in.getString("crsl")!=null?in.getString("crsl"):""));
			page2.add("值机柜台："+(out.getString("counter")!=null?out.getString("counter"):""));
			page2.add("出港分拣口："+(out.getString("chute")!=null?out.getString("chute"):""));
			page2.add("登机口："+(out.getString("gate")!=null?out.getString("gate"):""));
			page3.add("到达口视频");
			page3.add("进港包台视频");
			page3.add("机位视频");
			page3.add("值机柜台视频");
			page3.add("出港分拣口视频");
			page3.add("登机口视频");
		}
		detail.add(page1);
		detail.add(page2);
		detail.add(page3);
		return detail;
	}
	
	private String mergeFltNum(String a,String b){
		if(a == null){
			if(b==null){
				return "";
			}else{
				return b;
			}
		}else if(b==null){
			return a;
		}
		String o = a.length()>=b.length()?a:b;
		int index = 0;
		for(int i=0;i<o.length();i++){
			if(a.length()<i+1 || b.length()<i+1){
				index = i;
				break;
			}else if(a.charAt(i) != b.charAt(i)){
				index = i;
				break;
			}
		}
		String res = a+"/"+b.substring(index);
		return res;
	}
	public JSONArray getOneFltGanttY(String fltid) {
		return fdGanttDao.getOneFltGanttY(fltid);
	}

	public Map<String, Object> getSingleFlightInfo(Map<String,String> params) {
		Map<String, Object> map = new HashMap<String,Object>();
		if(fdGanttDao.getSingleFlightInfo(params)!=null && fdGanttDao.getSingleFlightInfo(params).size() !=0){
			map = fdGanttDao.getSingleFlightInfo(params).get(0);
		}
		return map;
	}

	public JSONArray getFdSingleYData(Map<String, String> params) {
		return fdGanttDao.getFdSingleYData(params);
	}

	public String ganttSingleData(Map<String, String> params) {
		JSONArray res = fdGanttDao.ganttSingleData(params);
		String jsonStr = JSON.toJSONString(res);
		return jsonStr;
	}
	
}
