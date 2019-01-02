/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年2月4日 下午2:49:45
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.mobile.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.mobile.service.MobileManageService;

@Controller
@RequestMapping(value = "${adminPath}/mobile/mobileManage")
public class MobileManageController extends BaseController {
	
	@Autowired
    private MobileManageService mobileManageService;
	
	@RequestMapping(value = {"list"})
    public String list() {
        return "prss/mobile/mobileManageList";
    }
	
	@ResponseBody
	@RequestMapping(value = "getMobileManageData")
	public String getMobileVersionData() {
		JSONArray json = mobileManageService.getMobileManageDate();
		String result = json.toJSONString();
		return result;
	}
	
	/**
	 * 
	 *Discription:删除.
	 *@param id
	 *@return
	 *@return:返回值意义
	 *@author:Heqg
	 *@update:2018年2月5日 Heqg [变更描述]
	 */
	@ResponseBody
	@RequestMapping(value = "delPDA")
	public String delVersion(String id) {
		mobileManageService.delPDA(id);
		return "success";
	}
	
	@RequestMapping(value = {"toEditForm"})
    public String toEditForm(Model model, String id) {
		JSONArray jsonArr = mobileManageService.getPADStatus();
		model.addAttribute("status", jsonArr);
		JSONObject json = mobileManageService.getPDAInfo(id);
		model.addAttribute("pdaJson", json);
        return "prss/mobile/mobileManageForm";
    }
	
	
	@ResponseBody
	@RequestMapping(value = "savePDA")
	public String savePDA(String id, String officeId, String pdaNo, String imei, String status) {
		mobileManageService.savePDA(id, officeId, pdaNo, imei, status);
		return "success";
	}

}
