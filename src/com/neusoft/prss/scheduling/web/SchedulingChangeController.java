/**
 *application name:hetprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年8月28日 上午9:48:59
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.scheduling.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.scheduling.service.SchedulingChangeService;
import com.neusoft.prss.stand.entity.ResultByCus;

@Controller
@RequestMapping(value = "${adminPath}/scheduling/change")
public class SchedulingChangeController extends BaseController{
	@Autowired
    private SchedulingChangeService schedulingChangeService;
	
	@RequestMapping(value = "changeZXInInfo")
	public String changeZXInInfo(String fltid,Model model){
		
		List<Map<String,String>> zxPlusInfo=schedulingChangeService.getPlusInfo(fltid);
		if(zxPlusInfo.size()!=0){
			model.addAttribute("plusInfo", zxPlusInfo.get(0));
		}
		model.addAttribute("fltid", fltid);
		return "prss/scheduling/changeZXInInfo";
	}
	   
	@RequestMapping(value = "saveZXInInfo")
	@ResponseBody
	public ResultByCus saveZXInInfo(String string){
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=json.getString("fltid");
		String inSpecialCargo=json.getString("inSpecialCargo");
		String inZxRemark=json.getString("inZxRemark");
		flag=schedulingChangeService.saveZXInInfo(fltid,inSpecialCargo,inZxRemark);
		if(flag){
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("1000");
			result.setMsg("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value = "changeZXOutInfo")
	public String changeZXOutInfo(String fltid,Model model){
		
		List<Map<String,String>> zxPlusInfo=schedulingChangeService.getPlusInfo(fltid);
		if(zxPlusInfo.size()!=0){
			model.addAttribute("plusInfo", zxPlusInfo.get(0));
		}
		model.addAttribute("fltid", fltid);
		return "prss/scheduling/changeZXOutInfo";
	}
	   
	@RequestMapping(value = "saveZXOutInfo")
	@ResponseBody
	public ResultByCus saveZXOutInfo(String string){
		boolean flag=false;
		ResultByCus result=new ResultByCus();
		JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		String fltid=json.getString("fltid");
		String outBaggageReal=json.getString("outBaggageReal");
		String outLargeBaggage=json.getString("outLargeBaggage");
		String outZxRemark=json.getString("outZxRemark");
		flag=schedulingChangeService.saveZXOutInfo(fltid,outBaggageReal,outLargeBaggage,outZxRemark);
		if(flag){
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("1000");
			result.setMsg("操作失败");
		}
		return result;
	}


	
	@RequestMapping(value="changeMaintenance")
	public String changeMaintenance(String inFltid,String outFltid,String field,Model model) {
		String fltid = "";
		if(StringUtils.isBlank(outFltid)) {//单进
			fltid = inFltid;
		} else if(StringUtils.isBlank(inFltid)){//单出
			fltid = outFltid;
		} else {//进出港
			fltid = inFltid;
		}
		String attrId = "8";
		List<Map<String, String>> dataList=schedulingChangeService.getPlusInfo(fltid);
		Map<String,String> data=new HashMap<String,String>(1);
		if(dataList.size()!=0){
			data=dataList.get(0);
			if(data!=null){
				model.addAttribute("ATTR_VAL",data.get("MAINTENANCE_REMARK"));
			}
		}
	    model.addAttribute("fltid", fltid);
	    model.addAttribute("field", field);
	    model.addAttribute("attrId", attrId);
		    
	    return "prss/scheduling/changeMaintenance";
	}
	
	/**
	 * 保存机务备注
	 * @param string
	 * @return
	 */
	@RequestMapping(value = "saveMaintenance")
	@ResponseBody
	public ResultByCus saveMaintenance(String string) {
		ResultByCus result=new ResultByCus();
		JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		try {
			schedulingChangeService.savePlusData(json);
			result.setCode("0000");
			result.setMsg("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode("0001");
			result.setMsg("操作失败");
		}
		return result;
	}
}
