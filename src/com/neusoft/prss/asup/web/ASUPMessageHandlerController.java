package com.neusoft.prss.asup.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.asup.service.ASUPMessageHandlerService;
/**
 * 报文消息弹窗按钮回调处理
 * @author baochl
 *
 */
@RestController 
@RequestMapping(value = "${adminPath}/asup/messageHandler")
public class ASUPMessageHandlerController extends BaseController{
	@Autowired
	private ASUPMessageHandlerService messageHandlerService;
	/**
	 * 查看报文原文
	 * @param eventRecord
	 * @return
	 */
	@RequestMapping(value = "source")
	@ResponseBody
	public String source(String eventRecord){
		String result = "";
		if(!StringUtils.isEmpty(eventRecord)){
			eventRecord = StringEscapeUtils.unescapeHtml4(eventRecord);
			result = messageHandlerService.getSource(JSONObject.parseObject(eventRecord));
		}
		return result;
	}
	/**
	 * 更新动态
	 * @param eventRecord
	 * @return
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	public String update(String eventRecord){
		String result = "";
		if(!StringUtils.isEmpty(eventRecord)){
			eventRecord = StringEscapeUtils.unescapeHtml4(eventRecord);
			JSONObject recordObj = JSONObject.parseObject(eventRecord);
			recordObj.put("userId", UserUtils.getUser().getId());
			result = messageHandlerService.update(recordObj);
		}
		return result;
	}
	/**
	 * 新增航班
	 * @param eventRecord
	 * @return
	 */
	@RequestMapping(value = "newFltData")
	@ResponseBody
	public JSONArray newFltData(String sourceId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("sourceId", sourceId);
		return messageHandlerService.getNewFltData(params);
	}
	/**
	 * 更新事件状态
	 * @param eventRecord
	 * @return
	 */
	@RequestMapping(value = "updateEventStatus")
	@ResponseBody
	public String updateEventStatus(String eventRecordId,String fltId){
		String result = "succeed";
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("fltid", fltId);
			params.put("id", eventRecordId);
			params.put("handleState", "1");
			params.put("userId", UserUtils.getUser().getId());
			messageHandlerService.updateEventStatus(params);
		} catch (Exception e) {
			result = e.toString();
			logger.error(e.toString());
		}
		return result;
	}
}
