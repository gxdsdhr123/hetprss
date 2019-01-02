/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年5月3日 上午09:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.stand.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.stand.entity.ResultByCus;
import com.neusoft.prss.stand.service.ParkingMutexRuleService;

/**
 * 
 *Discription:机位互斥规则
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/stand/parkingMutex")
public class ParkingMutexRuleController extends BaseController {


	@Resource
	private ParkingMutexRuleService parkingMutexRuleService;
	@Resource
	private CacheService cacheservice;
	
	
    @RequestMapping(value = "list" )
    public String getList() {
        return "prss/stand/parkingMutexList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,String sortName, String sortOrder) {
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("sortName", sortName);
        param.put("sortOrder", sortOrder);
        return parkingMutexRuleService.getDataList(param);
    }
    
    @RequestMapping(value = "addMutex" )
    public String addMutex(Model model) {
    	JSONArray  actStandList =cacheservice.getList("dim_bay");
    	model.addAttribute("ACTSTAND",actStandList );
    	JSONArray  airCraftList =cacheservice.getList("dim_actype");
    	model.addAttribute("AIRCRAFT",airCraftList );
        return "prss/stand/newMutex";
    }
    @RequestMapping(value = "editMutex" )
    public String editMutex(Model model,String id) {
    	JSONArray  actStandList =cacheservice.getList("dim_bay");
    	model.addAttribute("ACTSTAND",actStandList );
    	JSONArray  airCraftList =cacheservice.getList("dim_actype");
    	model.addAttribute("AIRCRAFT",airCraftList );
    	JSONObject data=parkingMutexRuleService.getDataById(id);
    	model.addAttribute("data",data);
        return "prss/stand/editMutex";
    }
	@RequestMapping(value = "save")
	@ResponseBody
	public ResultByCus saveOrEdit(String formData) {
		boolean flag =false;
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(formData));
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("ACTSTAND_CODE1", aftJSON.getString("ACTSTAND_CODE1"));
		param.put("ACTSTAND_CODE2", aftJSON.getString("ACTSTAND_CODE2"));
		param.put("AIRCRAFT_TYPE1", aftJSON.getString("AIRCRAFT_TYPE1"));
		param.put("AIRCRAFT_TYPE2", aftJSON.getString("AIRCRAFT_TYPE2"));
		param.put("TIME_MIN", aftJSON.getBigDecimal("TIME_MIN"));
		String id=aftJSON.getString("ID");
		if(StringUtils.isEmpty(id)){
			flag = parkingMutexRuleService.save(param);
		}else{
			param.put("ID", id);
			flag = parkingMutexRuleService.edit(param);
		}
		
		ResultByCus result = new ResultByCus();
		if (flag) {
			result.setCode("0000");
			result.setMsg("操作成功");
		} else {
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	} 
	
	@RequestMapping(value = "delMutex")
	@ResponseBody
	public ResultByCus delMutex(String id) {
		boolean flag =false;
		flag = parkingMutexRuleService.delMutex(id);
		ResultByCus result = new ResultByCus();
		if (flag) {
			result.setCode("0000");
			result.setMsg("操作成功");
		} else {
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	} 
}
