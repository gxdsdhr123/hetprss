package com.neusoft.prss.rule.web;

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
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.rule.entity.Drools;
import com.neusoft.prss.rule.entity.RuleExtend;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleWaterFillVO;
import com.neusoft.prss.rule.service.WaterFillRuleService;


@Controller
@RequestMapping(value = "${adminPath}/rule/waterFillRule")
public class WaterFillRuleController extends BaseController {
	
	@Autowired
	private WaterFillRuleService waterFillRuleService;
	
	@RequestMapping(value = "list")
    public String list() {
        return "prss/rule/waterfill/waterFillRuleList";
    }
	
    @RequestMapping(value = "ruleList")
    @ResponseBody
    public Map<String,Object> ruleList(
    		int pageSize,int pageNumber,String ruleName,
    		HttpServletRequest request, HttpServletResponse response) {
    	ruleName = StringEscapeUtils.unescapeHtml4(ruleName);
    	try {
    		ruleName = java.net.URLDecoder.decode(ruleName,"utf-8");
		} catch (Exception e) {}
    	
        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("name", ruleName);
        param.put("officeId",UserUtils.getUser().getOffice().getId());//部门ID
        param.put("userId", UserUtils.getUser().getId());//用户ID
        return waterFillRuleService.getRuleList(param);
    }
	
	@RequestMapping(value = "form")
	public String form(Model model, @RequestParam(value = "id") String id,
			@RequestParam(value = "type", required = true) String type){
		
		
		//规则基本信息
		RuleInfo ruleInfo = new RuleInfo();
		//获取回显附加属性
		RuleExtend ruleExtend = new RuleExtend();
		if("update".equals(type)){ //更新
			ruleInfo = waterFillRuleService.loadRuleInfo(id);
			ruleExtend = waterFillRuleService.loadRuleExtend(id);
			String colids = ruleInfo.getColids();
			//将扩展条件ID去掉
			if(ruleExtend.getAirlineList()!=null&&!ruleExtend.getAirlineList().isEmpty()){
				colids=colids.replace(Drools.SYS_AIRLINE_ID+",", "");
			}
			if(ruleExtend.getActtypeList()!=null&&!ruleExtend.getActtypeList().isEmpty()){
				colids=colids.replace(Drools.SYS_ACTTYPE_ID+",", "");
			}
			
			ruleInfo.setColids(colids);
			
			model.addAttribute("ruleInfo", ruleInfo);
			
			model.addAttribute("ruleExtend", ruleExtend);
			model.addAttribute("defaultAirLine",ruleExtend.getAirlineList());//已选航空公司
			model.addAttribute("defaultAtcactype",ruleExtend.getActtypeList());//已选的机型
			RuleWaterFillVO waterFillVO = waterFillRuleService.getWaterFill(id);
			model.addAttribute("waterFillVO", waterFillVO);
		}
		//机型列表
		List<Map<String, Object>> atcactypeList = waterFillRuleService.loadAtcactype();
		model.addAttribute("atcactypeList", atcactypeList);
		
		//航空公司列表
		List<Map<String, Object>> airlineList = waterFillRuleService.loadAirline();
		model.addAttribute("airlineList", airlineList);
		
		//进出港选项列表
        List<Map<String, String>> inOutPortList = waterFillRuleService.loadInOutPortType();
        model.addAttribute("inOutPortList", inOutPortList);
		
		model.addAttribute("type",type);
		return "prss/rule/waterfill/waterFillRuleSet";
	}
	
	@RequestMapping(value = "createDrl")
	@ResponseBody
	public JSONObject createDrl(HttpServletRequest request) {
		String targetStr = request.getParameter("expression");
		JSONObject result = new JSONObject();
		try {
			String drl = waterFillRuleService.createDrl(targetStr);
			result.put("code", 0);
			result.put("msg", "");
			result.put("result", drl);
		} catch (Exception e) {
			result.put("code", 100);
			result.put("msg", e.getMessage());
			result.put("result", "");
		}
		return result;
	}
	
	@RequestMapping(value = "save")
	@ResponseBody
	public JSONObject save(String data) {
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONObject formData = JSONObject.parseObject(data);
		JSONObject result = new JSONObject();
		try {
			result = waterFillRuleService.save(formData);
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
	public JSONObject delete(String ruleId){
		JSONObject result = new JSONObject();
		try {
			
			waterFillRuleService.delete(ruleId);
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
	/**
	 * 
	 *Discription:显示公式编辑器
	 *@return
	 *@return:返回值意义
	 *@author:
	 *@update:2017年11月5日  [变更描述]
	 */
	@RequestMapping("guide")
	public String showGuide(Model model,String varId,String express){
		model.addAttribute("varId",varId);
		String temp="";
		if(express!=null){
			if(express.indexOf("+")>0){
				int pos=express.indexOf("+");
				temp+=getVarString(express.substring(0,pos),varId);
				temp+=getOptString("+","add");
				temp+=getValString(express.substring(pos+1));
			}else if(express.indexOf("-")>0){
				int pos=express.indexOf("-");
				temp+=getVarString(express.substring(0,pos),varId);
				temp+=getOptString("-","sub");
				temp+=getValString(express.substring(pos+1));
			}else{
				temp+=getVarString(express,varId);
			}
		}
		model.addAttribute("express",temp);
		return "prss/rule/guide";
	}
	
	private String getVarString(String str,String varId){
		return  "<div class='div_class select' data-id="+varId+">"+ 
				str + "</div>";
	}
	
	private String getValString(String str){
		return  "<div class='div_class select' data-flag='int' data-type='val'>"+ 
				str + "</div>";
	}
	private String getOptString(String str,String opt){
		return  "<div class='div_class select' data-flag='"+opt+"' data-type='opt'>"+ 
				str + "</div>";
	}
}
