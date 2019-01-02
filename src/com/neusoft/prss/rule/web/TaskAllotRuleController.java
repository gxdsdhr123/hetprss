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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.rule.entity.Drools;
import com.neusoft.prss.rule.entity.RuleExtend;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.service.TaskAllotRuleService;
import com.neusoft.prss.workflow.access.QueryFilter;
import com.neusoft.prss.workflow.service.ProcessService;


@Controller
@RequestMapping(value = "${adminPath}/rule/taskAllotRule")
public class TaskAllotRuleController extends BaseController {
	
	@Autowired
	private TaskAllotRuleService taskAllotRuleService;
	
	@Autowired
	private ProcessService processService;
	
	@RequestMapping(value = "list")
    public String list() {
        return "prss/rule/taskAllotRuleList";
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
        return taskAllotRuleService.getRuleList(param);
    }
	
	@RequestMapping(value = "form")
	public String form(Model model, @RequestParam(value = "id") String id,
			@RequestParam(value = "type", required = true) String type){
		
		//机位选项列表
		List<Map<String, String>> apronTypeList = taskAllotRuleService.loadApronType();
		model.addAttribute("apronTypeList", apronTypeList);
		
		//进出港选项列表
		List<Map<String, String>> inOutPortList = taskAllotRuleService.loadInOutPortType();
		model.addAttribute("inOutPortList", inOutPortList);
		
		//规则基本信息
		RuleInfo ruleInfo = new RuleInfo();
		//获取回显附加属性
		RuleExtend ruleExtend = new RuleExtend();
		if("update".equals(type)){ //更新
			ruleInfo = taskAllotRuleService.loadRuleInfo(id);
			ruleExtend = taskAllotRuleService.loadRuleExtend(id);
			String colids = ruleInfo.getColids();
			//将扩展条件ID去掉
			if(ruleExtend.getApronType()!=null&&!ruleExtend.getApronType().isEmpty()){
				colids=colids.replace(Drools.SYS_ACTSTANDKIND_ID+",", "");
			}
			if(ruleExtend.getAirlineList()!=null&&!ruleExtend.getAirlineList().isEmpty()){
				colids=colids.replace(Drools.SYS_AIRLINE_ID+",", "");
			}
			if(ruleExtend.getActtypeList()!=null&&!ruleExtend.getActtypeList().isEmpty()){
				colids=colids.replace(Drools.SYS_ACTTYPE_ID+",", "");
			}
			if(ruleExtend.getInOutPort()!=null&&!ruleExtend.getInOutPort().isEmpty()){
				colids=colids.replace(Drools.SYS_INOUTPORT_ID+",", "");
			}
			
			ruleInfo.setColids(colids);
			
			model.addAttribute("ruleInfo", ruleInfo);
			
			model.addAttribute("ruleExtend", ruleExtend);
			model.addAttribute("defaultAirLine",ruleExtend.getAirlineList());//已选航空公司
			model.addAttribute("defaultAtcactype",ruleExtend.getActtypeList());//已选的机型
			List<JSONObject> ruleProcList=taskAllotRuleService.getRuleProcNodeInfo(id);//获取规则流程节点关系
			model.addAttribute("ruleProcList", ruleProcList);
		}
		//机型列表
		List<Map<String, Object>> atcactypeList = taskAllotRuleService.loadAtcactype();
		model.addAttribute("atcactypeList", atcactypeList);
		
		//航空公司列表
		List<Map<String, Object>> airlineList = taskAllotRuleService.loadAirline();
		model.addAttribute("airlineList", airlineList);
		model.addAttribute("type",type);
		return "prss/rule/taskAllotRuleSet";
	}
	
	@RequestMapping(value = "createDrl")
	@ResponseBody
	public JSONObject createDrl(HttpServletRequest request) {
		String targetStr = request.getParameter("expression");
		JSONObject result = new JSONObject();
		try {
			String drl = taskAllotRuleService.createDrl(targetStr);
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
			result = taskAllotRuleService.save(formData);
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
			
			taskAllotRuleService.delete(ruleId);
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
	 *Discription:删除修改前进行验证
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月8日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = "checkRule")
	@ResponseBody
	public JSONObject checkRule(String ruleId){
		JSONObject result = new JSONObject();
		int taskCount=taskAllotRuleService.checkRuleIfHaveTask(ruleId);
		if(taskCount>0){
			result.put("code", 100);
			result.put("msg","规则已生成任务，不能修改/删除！");
		}else{
			result.put("code", 0);
			result.put("msg","");
		}
		return result;
	}
	/**
	 * 
	 *Discription:根据作业类型获取工作流程及其节点.
	 *@param jobKind
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月2日 gaojingdan [变更描述]
	 */
	@RequestMapping("getProcessByJobType")
	@ResponseBody
	public JSONArray getProcessByJobType(String jobType){
		QueryFilter filter=new QueryFilter();
		filter.setJobType(jobType);
		JSONArray processArray=processService.getProcess(filter);//根据作业类型获取工作流程
		if(processArray!=null){
			for(int i=0;i<processArray.size();i++){
				JSONObject processObject=processArray.getJSONObject(i);//流程对象
				String processId=processObject.getString("id");//流程ID
				JSONArray nodeArray=processService.getProcessNodes(processId);
				processObject.put("nodeArray", nodeArray);
			}
		}
		return processArray;
	}
	/**
	 * 
	 *Discription:根据流程模板ID获取节点
	 *@param processId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月6日 gaojingdan [变更描述]
	 */
	@RequestMapping("getNodesByProcessId")
	@ResponseBody
	public JSONArray getNodesByProcessId(String processId){
		JSONArray nodesArray=processService.getProcessNodes(processId);
		return nodesArray;
	}
	/**
	 * 
	 *Discription:显示公式编辑器
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月5日 gaojingdan [变更描述]
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
	/**
	 * 获取变量list
	 *Discription:方法功能中文描述.
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月5日 gaojingdan [变更描述]
	 */
	@RequestMapping("getVarList")
	@ResponseBody
	public JSONArray getVarList(){
		//return TimeParam.jsonArray;
		return taskAllotRuleService.getVarList();
	}
}
