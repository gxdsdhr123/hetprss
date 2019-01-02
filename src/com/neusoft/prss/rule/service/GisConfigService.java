package com.neusoft.prss.rule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.rule.dao.GisConfigDao;

/**
 * 电子围栏、节点配置规则service
 */
@Service
public class GisConfigService extends BaseService{
	
	@Autowired
	private GisConfigDao gisConfigDao;
	
	/**
	 * 根据id获取电子围栏、节点配置信息
	 */
	public JSONObject getDataById(String id){
		JSONObject rs = new JSONObject();
		if(id!=null&&!id.equals("")){
			Map<String,Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("ids", id);
			List<JSONObject> rows = gisConfigDao.getDataList(paramsMap);
			//合并围栏信息记录
			rows = processAreaInfo(rows);
			if(rows!=null&&rows.size()==1){
				rs = rows.get(0);
			}
		}
		return rs;
	}
	
	/**
	 * 获取电子围栏、节点配置信息列表
	 */
	public JSONObject getDataList(Map<String,Object> paramsMap){
		JSONObject rs = new JSONObject();
		int total= gisConfigDao.getDataCount(paramsMap);
		List<JSONObject> rows = gisConfigDao.getDataList(paramsMap);
		//合并围栏信息记录
		rows = processAreaInfo(rows);
		rs.put("total", total);
		rs.put("rows", rows);
		return rs;
	}
	
	/**
	 * 保存，修改电子围栏、节点配置信息
	 */
	public int saveGisConfig(Map<String, Object> paramsMap){
		//modify更新 add新增
		String operateType = (String)paramsMap.get("operateType");
		if(operateType.equals("add")){
			int id = gisConfigDao.getGisProcNodeRelId();
			paramsMap.put("id", id);
		}
		//围栏编码id areaCode,areaCode1,areaCode2...
		String areaCodes = (String)paramsMap.get("areaCodes");
		//流程节点围栏配置
		List<Map<String, Object>> confDataList = new ArrayList<Map<String, Object>>();
		confDataList.add(paramsMap);
		//节点围栏关系
		List<Map<String, Object>> relDataList = null;
		if(areaCodes!=null&&!areaCodes.equals("")){
			relDataList = new ArrayList<Map<String, Object>>();
			String[] areaCodeArr = areaCodes.split(",");
			for(int i=0;i<areaCodeArr.length;i++){
				String areaCode = areaCodeArr[i];
				Map<String, Object> relParamsMap = new HashMap<String, Object>();
				relParamsMap.put("id", paramsMap.get("id"));
				relParamsMap.put("areaCode", areaCode);
				relParamsMap.put("updateTm", paramsMap.get("updateTm"));
				relParamsMap.put("operator", paramsMap.get("operator"));
				relDataList.add(relParamsMap);
			}
		}
		
		int count = 0;
		if(operateType.equals("add")){
			//新增电子围栏、节点配置信息
			count = gisConfigDao.insertGisConfig(confDataList);
			if(relDataList!=null&&relDataList.size()>0){
				//新增电子围栏、节点关系
				gisConfigDao.insertGisProcNodeRel(relDataList);
			}
		}else if(operateType.equals("modify")){
			String ids = (String)paramsMap.get("id");
			if(ids!=null&&!ids.equals("")){
				//删除电子围栏、节点关系
				gisConfigDao.delGisProcNodeRel(ids);
			}
			//新增电子围栏、节点配置信息
			count = gisConfigDao.updateGisConfig(paramsMap);
			if(relDataList!=null&&relDataList.size()>0){
				//新增电子围栏、节点关系
				gisConfigDao.insertGisProcNodeRel(relDataList);
			}
		}
		return count;
	}

	/**
	 * 删除电子围栏、节点配置信息
	 */
	public int deleteGisConfig(Map<String, Object> paramMap){
		int delCount = 0;
		String ids = (String)paramMap.get("ids");
		if(ids!=null&&!ids.equals("")){
			//删除电子围栏、节点关系
			delCount += gisConfigDao.delGisProcNodeRel(ids);
			//删除电子围栏、节点配置信息
			delCount += gisConfigDao.deleteGisConfig(ids);
		}
		return delCount;
	}
	
	/**
	 * 获取保障类型选项
	 */
	public JSONArray getReskind(){
		return gisConfigDao.getReskind();
	}
	
	/**
	 * 获取作业类型选项
	 */
	public JSONArray getRestype(Map<String,Object> params){
		return gisConfigDao.getRestype(params);
	}
	
	/**
	 * 获取流程选项
	 */
	public JSONArray getProcess(Map<String,Object> params){
		return gisConfigDao.getProcess(params);
	}
	
	/**
	 * 获取节点选项
	 */
	public JSONArray getNode(Map<String,Object> params){
		return gisConfigDao.getNode(params);
	}
	
	/**
	 * 获取电子围栏选项
	 */
	public JSONArray getGisRailInfo(){
		return gisConfigDao.getGisRailInfo();
	}
	
	/**
	 * 合并围栏信息
	 */
	private List<JSONObject> processAreaInfo(List<JSONObject> rows){
		List<JSONObject> rsList = new ArrayList<JSONObject>();
		Map<String,JSONObject> tmpRowMap = new HashMap<String, JSONObject>();
		if(rows!=null&&rows.size()>0){
			for(int i=0;i<rows.size();i++){
				JSONObject row = rows.get(i);
				String id = row.getString("id");
				String areaCode = row.getString("areaCode");
				if(areaCode==null){
					row.put("areaCode", "");
				}
				String areaName = row.getString("areaName");
				if(areaName==null){
					row.put("areaName", "");
				}
				String areaCodeName = "("+areaCode+")"+areaName;
				if(areaCode==null){
					row.put("areaCodeName", "");
				}else{
					row.put("areaCodeName", areaCodeName);
				}
				if(tmpRowMap.containsKey(id)){
					JSONObject tmpRow = tmpRowMap.get(id);
					String tmpAreaCode = tmpRow.getString("areaCode");
					String tmpAreaName = tmpRow.getString("areaName");
					String tmpAreaCodeName = tmpRow.getString("areaCodeName");
					tmpRow.put("areaCode", tmpAreaCode+","+areaCode);
					tmpRow.put("areaName", tmpAreaName+","+areaName);
					tmpRow.put("areaCodeName", tmpAreaCodeName+","+areaCodeName);
					tmpRowMap.put(id, tmpRow);
				}else{
					tmpRowMap.put(id, row);
				}
			}
			
			for(String key:tmpRowMap.keySet()){
				rsList.add(tmpRowMap.get(key));
			}
		}
		return rsList;
	}
	
	/**
	 * 一键生效、失效
	 */
	public int updateGisConfigInUse(String inUse){
		return gisConfigDao.updateGisConfigInUse(inUse);
	}
}
