/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年5月1日 下午15:35:09
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.stand.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.stand.entity.ResultByCus;
import com.neusoft.prss.stand.service.ParkingSpaceRuleService;




/**
 * 
 *Discription:停靠机型规则
 *@param 
 *@return:
 *@author:wangtg
 *@version:v1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/stand/parkingSpace")
public class ParkingSpaceRuleController extends BaseController {


	@Resource
	private ParkingSpaceRuleService parkingSpaceRuleService;
    @RequestMapping(value = "list" )
    public String flightList() {
        return "prss/stand/parkingSpaceList";
    }
    
    @RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber) {
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
        return parkingSpaceRuleService.getLeftDataList(param);
    }
    
    @RequestMapping(value = "getTreeData" )
    @ResponseBody
    public Map<String,Object> getTreeData(String code) {
    	Map<String,Object> result=new HashMap<String,Object>();
    	JSONArray leftData = parkingSpaceRuleService.getAllActType();
		JSONArray rightData = parkingSpaceRuleService.getActTypeByCode(code);
		leftData.removeAll(rightData);
		result.put("leftData", leftData);
		result.put("rightData", rightData);
        return result;
    }
    
    @RequestMapping(value = "saveEdit" )
    @ResponseBody
    public ResultByCus saveEdit(String code,String selectData) {
    	ResultByCus result=new ResultByCus();
    	List<String> selectDataList=new ArrayList<String>();
    	if(!"".equals(selectData)){
    		String[] dataArr=selectData.split(",");
    		selectDataList= Arrays.asList(dataArr);
    	}
    	boolean flag=parkingSpaceRuleService.saveEdit(code,selectDataList);
    	if(flag){
    		result.setCode("0000");
    		result.setMsg("修改成功");
    	}else{
    		result.setCode("0001");
    		result.setMsg("修改失败");
    	}
        return result;
    }
    

    
   
}
