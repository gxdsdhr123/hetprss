/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月12日 下午6:30:27
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.service;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.workflow.dao.NodeBtnDao;
import com.neusoft.prss.workflow.dao.NodeDao;
import com.neusoft.prss.workflow.entity.Node;
import com.neusoft.prss.workflow.entity.NodeBtn;

@Service
@Transactional(readOnly = true)
public class NodeService extends BaseService {
	@Autowired
	private NodeDao nodeDao;

	@Autowired
	private NodeBtnDao nodeBtnDao;
	
	@Autowired
	private ProcessService procService;

	public List<Node> getNodeList(String jobKind,String jobType) {
		return nodeDao.getNodeList(jobKind,jobType);
	}

	public Node getNodeById(String id) {
		return nodeDao.getNodeById(id);
	}

	public String doInsertNode(Node node,String btns) {
		nodeDao.doInsertNode(node);
		if(StringUtils.isNotEmpty(btns)){
			JSONArray btnsArr = JSONArray.parseArray(StringEscapeUtils.unescapeHtml4(btns));
			for(int i=0;i<btnsArr.size();i++){
				JSONObject btn = btnsArr.getJSONObject(i);
				btn.put("nodeId", node.getId());
				nodeBtnDao.doInsertNodeBtn(btn);
			}
		}
		return node.getId();
	}
	
	public void doInsertNodeBtn(JSONObject btns){
		nodeBtnDao.doInsertNodeBtn(btns);
	}

	public void doUpdateNode(Node node,String btns) {
		nodeDao.doUpdateNode(node);
		if(StringUtils.isNotEmpty(node.getId())){
			nodeBtnDao.delNodeBtn(node.getId());
		}
		if(StringUtils.isNotEmpty(btns)){
			JSONArray btnsArr = JSONArray.parseArray(StringEscapeUtils.unescapeHtml4(btns));
			for(int i=0;i<btnsArr.size();i++){
				JSONObject btn = btnsArr.getJSONObject(i);
				btn.put("nodeId", node.getId());
				nodeBtnDao.doInsertNodeBtn(btn);
			}
		}
	}

	public JSONArray getNodeKind() {
		return nodeDao.getNodeKindList();
	}

	public JSONArray getNodeType(String resKind) {
		return nodeDao.getNodeTypeByKind(resKind);
	}

	public List<Node> getNodeByType(String jobType) {
		return nodeDao.getNodeByType(jobType);
	}

	public List<NodeBtn> getNodeBtnList(String id) {
		List<NodeBtn> btns = nodeBtnDao.getNodeBtnByNodeID(id);
		for(NodeBtn btn : btns){
			if(StringUtils.isNotEmpty(btn.getLimitParm())&&btn.getLimitParm().startsWith("[")){
				JSONArray limitParms = JSONArray.parseArray(btn.getLimitParm());
				for(int i=0;i<limitParms.size();i++){
					JSONObject limitParm = limitParms.getJSONObject(i);
					if(limitParm.containsKey("processId")){
						JSONObject process = procService.getProcessById(limitParm.getString("processId"));
						limitParm.put("processName", process.getString("displayName"));
					}
					if(limitParm.containsKey("nodeId")){
						Node limitNode = getNodeById(limitParm.getString("nodeId"));
						limitParm.put("nodeName", limitNode.getLabel());
					}
				}
				btn.setLimitParm(limitParms.toString());
			}
		}
		return btns;
	}

	public JSONArray getTemplateList() {
		return nodeDao.getTemplateList();
	}

	public JSONArray getBtnEvent() {
		return nodeBtnDao.getBtnEvent();
	}

	public String delNode(String id) {
		String re = "success";
		try {
			nodeDao.delNode(id);
			nodeBtnDao.delNodeBtn(id);
		} catch (Exception e) {
			logger.error(e.toString());
			re = e.toString();
		}
		return re;
	}
	
	public void delNodeBtn(String id){
		nodeBtnDao.delNodeBtn(id);
	}
	
}
