/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月12日 下午6:24:23
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.web;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.rule.service.TaskAllotRuleService;
import com.neusoft.prss.workflow.access.QueryFilter;
import com.neusoft.prss.workflow.service.NodeService;
import com.neusoft.prss.workflow.service.ProcessService;
import com.neusoft.prss.workflow.entity.Process;

@Controller
@RequestMapping(value = "${adminPath}/workflow/process")
public class ProcessController extends BaseController {

	@Autowired
	private ProcessService processService;

	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private TaskAllotRuleService taskAllotRuleService;

	/**
	 * 跳转流程页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		JSONArray kindList = nodeService.getNodeKind();
		model.addAttribute("kindList", kindList);
		return "prss/workflow/processList";
	}

	/**
	 * 流程页面json数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getProcessData")
	public String getProcessData(String jobKind,String jobType) {
		QueryFilter filter = new QueryFilter();
		if(StringUtils.isNotEmpty(jobKind)){
			filter.setJobKind(jobKind);
		}
		if(StringUtils.isNotEmpty(jobType)){
			filter.setJobType(jobType);
		}
		return processService.getProcess(filter).toString();
	}

	/**
	 * 增加流程页面跳转和保障类型json
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "processForm")
	public String form(Model model,String id) {
		JSONArray kindList = nodeService.getNodeKind();
		JSONArray jobTypeList = new JSONArray();
		Process process = new Process();
		String processModel = "{}";
		if(StringUtils.isNotEmpty(id)){
			JSONObject procJson = processService.getProcessById(id);
			process = JSONObject.toJavaObject(procJson, Process.class);
			processModel = procJson.getString("modelJson");
			jobTypeList = nodeService.getNodeType(process.getJobKindId());
		}
		model.addAttribute("process", process);
		model.addAttribute("jobTypeList", jobTypeList);
		model.addAttribute("processModel", processModel);
		model.addAttribute("kindList", kindList);
		JSONArray template = nodeService.getTemplateList();
		model.addAttribute("template", template);
		return "prss/workflow/processForm";
	}

	/**
	 * 流程属性 工作类型 json数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "processProfile")
	public String profile(String resKind) {
		String result = JSON.toJSONString(nodeService.getNodeType(resKind), SerializerFeature.WriteMapNullValue);
		return result;
	}

	/**
	 * 获取节点
	 * 
	 * @param process
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "processNodeById")
	public String processnode(String jobType) {
		String result = JSON.toJSONString(nodeService.getNodeByType(jobType), SerializerFeature.WriteMapNullValue);
		return result;
	}

	/**
	 * 增加
	 * 
	 * @param process
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "deployXml")
	public boolean processDeployXml(String model, String id) {
		try {
			String xml = StringEscapeUtils.unescapeHtml4(model);
			if(StringUtils.isNotEmpty(id)){
				processService.redeploy(id, xml);
			} else {
				processService.deploy(xml, UserUtils.getUser().getName());
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 刪除
	 * 
	 * @param process
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "processRemove")
	public String processremove(String id) {
		String result = "successed";
		try {
			if(taskAllotRuleService.getRuleCountByProc(id)>0){
				result = "模板已绑定到规则，不能删除！";
			} else {
				processService.cascadeRemove(id);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			result = e.toString();
		}
		return result;
	}
	/**
	 * 获取流程模板所有节点
	 * @param process
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getProcessNodes")
	public JSONArray getProcessNodes(String id) {
		return processService.getProcessNodes(id);
	}
}
