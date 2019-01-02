/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月12日 下午6:21:25
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.workflow.entity.Node;
import com.neusoft.prss.workflow.entity.NodeBtn;
import com.neusoft.prss.workflow.service.NodeService;
import com.neusoft.prss.workflow.service.ProcessService;

@Controller
@RequestMapping(value = "${adminPath}/workflow/node")
public class NodeController extends BaseController {

	@Autowired
	private NodeService nodeService;
	@Autowired
	private ProcessService procService;

	@RequestMapping(value = "list")
	public String list(Model model) {
		JSONArray nodeKind = nodeService.getNodeKind();
		model.addAttribute("kindList", nodeKind);
		return "prss/workflow/nodeList";
	}

	@ResponseBody
	@RequestMapping(value = "getNodeData")
	public String getNodeData(String jobKind,String jobType) {
		List<Node> list = nodeService.getNodeList(jobKind,jobType);
		String result = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
		return result;
	}

	@RequestMapping(value = "nodeForm")
	public String form(Model model, String id) {
		Node node = new Node();
		if (StringUtils.isNotEmpty(id)) {
			node = nodeService.getNodeById(id);
			if (StringUtils.isNotEmpty(node.getJobKind())) {
				JSONArray nodeType = nodeService.getNodeType(node.getJobKind());
				model.addAttribute("type", nodeType);
			}
		}
		model.addAttribute("node", node);
		JSONArray nodeKind = nodeService.getNodeKind();
		model.addAttribute("kind", nodeKind);
		JSONArray template = nodeService.getTemplateList();
		model.addAttribute("template", template);
		return "prss/workflow/nodeForm";
	}

	@ResponseBody
	@RequestMapping(value = "save")
	public String save(Node node, String btns) {
		String re = "error";
		if (StringUtils.isNotEmpty(node.getId())) {
			nodeService.doUpdateNode(node,btns);
			try {
				procService.redeployByNode(node.getId(), node.getJobKind(), node.getJobType());
			} catch (Exception e) {
				logger.error(e.toString());
			}
			re = "success";
		} else {
			nodeService.doInsertNode(node,btns);
			re = "success";
		}
		return re;
	}

	@ResponseBody
	@RequestMapping(value = "getNodeBtnData")
	public String getNodeBtnData(String id) {
		List<NodeBtn> btnList = nodeService.getNodeBtnList(id);
		String result = JSON.toJSONString(btnList, SerializerFeature.WriteMapNullValue);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "getTypeByKind")
	public JSONArray getTypeByKind(String jobKind) {
		JSONArray jobType = nodeService.getNodeType(jobKind);
		return jobType;
	}

	@ResponseBody
	@RequestMapping(value = "getBtnEvent")
	public JSONArray getBtnEvent() {
		JSONArray btnEvent = nodeService.getBtnEvent();
		return btnEvent;
	}

	@ResponseBody
	@RequestMapping(value = "delNode")
	public String delNode(String id,String jobKind,String jobType) {
		String re = "";
		if(!procService.isContainNode(id, jobKind, jobType)){
			re = nodeService.delNode(id);
		} else {
			re = "节点已配置到流程模板，不能删除！";
		}
		return re;
	}
	
	@RequestMapping(value = "variable")
	public String variable(Model model, String id) {
		model.addAttribute("id", id);
		return "prss/workflow/variable";
	}
	/**
	 * 限制参数
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "limitParm")
	public String limitParm(Model model, String id) {
		model.addAttribute("id", id);
		return "prss/workflow/limitParm";
	}
	/**
	 * 限制参数编辑
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "limitParmForm")
	public String limitParmForm(Model model, String id) {
		JSONArray jobKind = nodeService.getNodeKind();
		model.addAttribute("jobKind", jobKind);
		return "prss/workflow/limitParmForm";
	}
}
