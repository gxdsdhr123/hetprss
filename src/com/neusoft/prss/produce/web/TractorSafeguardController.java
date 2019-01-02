package com.neusoft.prss.produce.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.produce.service.TractorSafeguardService;


@Controller
@RequestMapping(value = "${adminPath}/produce/sageguard")
public class TractorSafeguardController extends BaseController {
	
	@Autowired
	private TractorSafeguardService tractorSafeguardService;
	
	@RequestMapping(value = "list")
    public String list() {
        return "prss/produce/tractorSafeguardList";
    }
	
    @RequestMapping(value = "ruleList")
    @ResponseBody
    public Map<String,Object> ruleList(
    		int pageSize,int pageNumber,String desc,
    		HttpServletRequest request, HttpServletResponse response) {
        desc = StringEscapeUtils.unescapeHtml4(desc);
    	try {
    	    desc = java.net.URLDecoder.decode(desc,"utf-8");
		} catch (Exception e) {}
    	
        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("desc", desc);
        param.put("officeId",UserUtils.getUser().getOffice().getId());//部门ID
        param.put("userId", UserUtils.getUser().getId());//用户ID
        return tractorSafeguardService.getList(param);
    }
	
	@RequestMapping(value = "form")
	public String form(Model model, @RequestParam(value = "id") String id,
			@RequestParam(value = "type", required = true) String type){
		
		//牵引车类型
		JSONObject object = new JSONObject();
		object.put("type", "1");
		JSONArray dimData = tractorSafeguardService.getDimData(object);
		model.addAttribute("dimData", dimData);
		
		//机型列表
        List<Map<String, Object>> atcactypeList = tractorSafeguardService.loadAtcactype();
        model.addAttribute("atcactypeList", atcactypeList);
        
        //航空公司列表
        List<Map<String, Object>> airlineList = tractorSafeguardService.loadAirline();
        model.addAttribute("airlineList", airlineList);
        
        //机位选项列表
        List<Map<String,Object>> apronTypeList = tractorSafeguardService.loadApronType();
        model.addAttribute("apronTypeList", apronTypeList);
        
        JSONObject info = tractorSafeguardService.loadRuleInfo(id);
        model.addAttribute("info", info);
        model.addAttribute("type", type);
        
		return "prss/produce/tractorSafeguardSet";
	}
	
	@RequestMapping(value = "getOptions")
    @ResponseBody
    public JSONArray getOptions(String type,String typeId,String deviceModel,String deviceNo) {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("deviceModel", deviceModel);
        object.put("typeId", typeId);
        object.put("deviceNo", deviceNo);
        JSONArray result = tractorSafeguardService.getDimData(object);
        return result;
    }
	@RequestMapping(value = "save")
	@ResponseBody
	public JSONObject save(String data) {
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONObject formData = JSONObject.parseObject(data);
		JSONObject result = new JSONObject();
		try {
			result = tractorSafeguardService.save(formData);
		} catch (Exception e) {
			result.put("code", 100);
			result.put("msg", e.getMessage());
			result.put("result", "");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONObject delete(String id){
		JSONObject result = new JSONObject();
		try {
			
			tractorSafeguardService.delete(id);
			result.put("code", 0);
			result.put("msg", "操作成功");
			
		} catch (Exception e) {
			result.put("code", 100);
			result.put("msg", e.getMessage());
			result.put("result", "");
			e.printStackTrace();
		}
		return result;
	}
}
