/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年7月10日 上午9:48:59
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.flightdynamic.entity.DepartPsgEntity;
import com.neusoft.prss.flightdynamic.service.DepartPsgInfoService;
import com.neusoft.prss.stand.entity.ResultByCus;

@Controller
@RequestMapping(value = "${adminPath}/departPsgInfo")
public class DepartPsgInfoController extends BaseController {
    @Autowired
    private DepartPsgInfoService departPsgInfoService;
    @Autowired
    private CacheService cacheService;

  /**
   * 获取离港信息录入表单
   * @param model
   * @param inFltId
   * @param outFltId
   * @param isOverStation 是否是过站航班
   * @param ioFlag I：进港 O：出港
   * @author wangtg
   * @update wangtg
   * @updatetime 2018-07-11
   * @return
   */
    @RequestMapping(value = "form")
    public String getForm(Model model,String inFltId,String outFltId,Boolean isOverStation,String ioFlag) {
    	//根据fltid获取离港信息
    	List<DepartPsgEntity> record=departPsgInfoService.getPassengerT(inFltId,outFltId,isOverStation, ioFlag);
    	JSONArray routeArr=new JSONArray();
    	List<String> inRoute=new ArrayList<String>();
    	List<String> outRoute=new ArrayList<String>();
    	String inRouteStr="";
    	String outRouteStr="";
    	if(record.size()!=0){
    		//说明是修改
    		DepartPsgEntity entity=record.get(0);
    		if(!StringUtils.isEmpty(entity.getLeg1AdPort())){
    			JSONObject obj=new JSONObject();
    			obj.put("routeIndex", "1");
				obj.put("routeEnName",entity.getLeg1AdPort() );
				routeArr.add(obj);
    		}
    		if(!StringUtils.isEmpty(entity.getLeg2AdPort())){
    			JSONObject obj=new JSONObject();
    			obj.put("routeIndex", "2");
				obj.put("routeEnName",entity.getLeg2AdPort() );
				routeArr.add(obj);
    		}
    		if(!StringUtils.isEmpty(entity.getLeg3AdPort())){
    			JSONObject obj=new JSONObject();
    			obj.put("routeIndex", "3");
				obj.put("routeEnName",entity.getLeg3AdPort() );
				routeArr.add(obj);
    		}
    		if(!StringUtils.isEmpty(entity.getLeg4AdPort())){
    			JSONObject obj=new JSONObject();
    			obj.put("routeIndex", "4");
				obj.put("routeEnName",entity.getLeg4AdPort() );
				routeArr.add(obj);
    		}
    		if(!StringUtils.isEmpty(entity.getLeg5AdPort())){
    			JSONObject obj=new JSONObject();
    			obj.put("routeIndex", "5");
				obj.put("routeEnName",entity.getLeg5AdPort() );
				routeArr.add(obj);
    		}
    		model.addAttribute("type", "edit");
    		model.addAttribute("entity", entity);
    		if(isOverStation){
    			model.addAttribute("inFltId", inFltId);
    			model.addAttribute("outFltId", outFltId);
    		}else if("I".equals(ioFlag)){
    			model.addAttribute("inFltId", inFltId);
    		}else {
    			model.addAttribute("outFltId", outFltId);
    		}
    	}else{
    		//说明是新增
    		model.addAttribute("type", "add");
    		//如果是过站航班 需要把进出港的航线都拿过来
        	if(isOverStation){
        		//x用来控制第X段航线
        		int x=1;
        		inRoute=departPsgInfoService.getRouteName(inFltId);
        		if(inRoute.size()!=0){
        			inRouteStr=inRoute.get(0);
        			String[] inStr=inRouteStr.split("-");
        			for(int i=0;i<inStr.length;i++){
        				JSONObject obj=new JSONObject();
        				obj.put("routeIndex", x);
        				obj.put("routeEnName", inStr[i]+"-"+"BAV");
        				routeArr.add(obj);
        				x++;
        			}
        			model.addAttribute("inFltId", inFltId);
        		}
        		outRoute=departPsgInfoService.getRouteName(outFltId);
        		if(outRoute.size()!=0){
        			outRouteStr=outRoute.get(0);
        			String[] outStr=outRouteStr.split("-");
        			for(int i=0;i<outStr.length;i++){
        				JSONObject obj=new JSONObject();
        				obj.put("routeIndex", x);
        				obj.put("routeEnName","BAV"+"-"+outStr[i]);
        				routeArr.add(obj);
        				x++;
        			}
        			model.addAttribute("outFltId", outFltId);
        		}
        	}else {
        		//配对的航班只有航班号相同时才算是过站航班  否则在录入离港信息时视为单进单出
        		if("I".equals(ioFlag)){
        			inRoute=departPsgInfoService.getRouteName(inFltId);
        			if(inRoute.size()!=0){
            			inRouteStr=inRoute.get(0);
            			String[] inStr=inRouteStr.split("-");
            			for(int i=0;i<inStr.length;i++){
            				JSONObject obj=new JSONObject();
            				obj.put("routeIndex", i+1);
            				obj.put("routeEnName", inStr[i]+"-"+"BAV");
            				routeArr.add(obj);
            			}
            			model.addAttribute("inFltId", inFltId);
            		}
            	}else {
            		outRoute=departPsgInfoService.getRouteName(outFltId);
            		outRouteStr=outRoute.get(0);
        			String[] outStr=outRouteStr.split("-");
        			for(int i=0;i<outStr.length;i++){
        				JSONObject obj=new JSONObject();
        				obj.put("routeIndex", i+1);
        				obj.put("routeEnName","BAV"+"-"+outStr[i]);
        				routeArr.add(obj);
        			}
        			model.addAttribute("outFltId", outFltId);
            	}
        	}
    		
    	}
    	model.addAttribute("isOverStation", isOverStation.toString());
    	model.addAttribute("ioFlag", ioFlag);
    	//加入航线的中文名用来显示
    	model.addAttribute("result", transRouteCnName(routeArr));
    	//只有过站航班才是将数据合并展示,否则为单进单出
        return "prss/flightdynamic/departPsgInfoForm";
    }
    
    /**
     * 新增或保存离港信息
     * @param entity
     * @author wangtg
     * @return
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ResultByCus saveForm(DepartPsgEntity entity) {
    	boolean flag=false;
		ResultByCus result=new ResultByCus();
		String userId=UserUtils.getUser().getId();
		if("add".equals(entity.getType())){
			entity.setCreatorId(userId);
			entity.setSource("0");
			flag=departPsgInfoService.saveDepartPag(entity);
		}else if("edit".equals(entity.getType())){
			entity.setUpdatorId(userId);
			flag=departPsgInfoService.updateDepartPag(entity);
		}
		if(flag){
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("1000");
			result.setMsg("操作失败");
		}
		return result;
    	
    }
    /**
     * 添加航线中文名用来在页面显示
     * @param routeArray
     * @author wangtg
     * @return
     */
    private JSONArray transRouteCnName(JSONArray routeArray){
    	 Map<String,String> aptMap = cacheService.getMap("dim_airport3_map");
    	 for(int i=0;i<routeArray.size();i++){
    		 JSONObject obj=(JSONObject) routeArray.get(i);
    		 String aptEnName=obj.getString("routeEnName");
    		 String[] str=aptEnName.split("-");
    		 String aptCnName="";
    		 if(str.length==2){
    			 aptCnName+=aptMap.get(str[0])+"-"+aptMap.get(str[1]);
    		 }
    		 obj.put("routeCnName", aptCnName);
    	 }
    	 return routeArray;
    }

}
