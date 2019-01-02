package com.neusoft.prss.stand.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.service.ParamCommonService;
import com.neusoft.prss.stand.service.ConditionRuleService;


@Controller
@RequestMapping(value = "${adminPath}/stand/condition")
public class ConditionRuleController extends BaseController {
	
	@Autowired
	private ConditionRuleService conditionRuleService;
	
	@Resource
    private ParamCommonService paramCommonService;
	
	@Autowired
    private CacheService cacheService;
	
	@RequestMapping(value = "list")
    public String list() {
        return "prss/stand/conditionRuleList";
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
        return conditionRuleService.getRuleList(param);
    }
	
	@RequestMapping(value = "form")
	public String form(Model model, @RequestParam(value = "id") String id,
			@RequestParam(value = "type", required = true) String type){
		
		//规则基本信息
		JSONObject ruleInfo = new JSONObject();
		if("update".equals(type)){ //更新
			ruleInfo = conditionRuleService.loadRuleInfo(id);
			String condition = ruleInfo.getString("TEXT");
            String expression = ruleInfo.getString("DRL_STR");
            String colids = ruleInfo.getString("COLIDS");
            if(!StringUtils.isBlank(condition)) {
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("schema", "RULE");
                data.put("colids", colids);
                try {
                    condition = java.net.URLDecoder.decode(condition,"utf-8");
                    condition = java.net.URLDecoder.decode(condition,"utf-8");
                    expression = java.net.URLDecoder.decode(expression,"utf-8");
                    expression = java.net.URLDecoder.decode(expression,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                data.put("expression", expression);
                data.put("text", condition);
    	        condition = paramCommonService.getColumn(data);
    	        ruleInfo.put("condition", condition);
            }
			model.addAttribute("ruleInfo", ruleInfo);
			String actstand = ruleInfo.getString("ACTSTAND_STR");
			model.addAttribute("fixedList", !StringUtils.isBlank(actstand)?actstand.split(","):null);
		}
        //常用参数
        List<Map<String, String>> paramList = conditionRuleService.loadParam();
        model.addAttribute("paramList", paramList);
        //机位
        JSONArray actstandList = cacheService.getOpts("dim_bay", "bay_code", "description_cn");
        JSONArray list = new JSONArray();
        String actstandStr = ruleInfo.getString("ACTSTAND_STR");
        if(!StringUtils.isBlank(actstandStr)) {
            String[] arr = actstandStr.split(",");
            for (int i = 0; i < actstandList.size(); i++) {
                JSONObject actstandObj = actstandList.getJSONObject(i);
                int flag = 0;
                for (int j = 0; j < arr.length; j++) {
                    if(arr[j].equals(actstandObj.get("id"))) {
                        flag ++;
                        break;
                    }
                }
                if(flag == 0)
                    list.add(actstandObj);
            }
        } else {
            list = actstandList;
        }
        model.addAttribute("actstandList", list);
        model.addAttribute("type",type);
		return "prss/stand/conditionRuleSet";
	}
	
	
	/**
	 * 
	 *Discription:新增、修改保存规则.
	 *@param data
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2018年5月9日 neusoft [变更描述]
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public JSONObject save(String data) {
		data = StringEscapeUtils.unescapeHtml4(data);
		JSONObject formData = JSONObject.parseObject(data);
		JSONObject result = new JSONObject();
		try {
		    formData.put("userId", UserUtils.getUser().getId());
			result = conditionRuleService.save(formData);
		} catch (Exception e) {
			result.put("code", 100);
			result.put("msg", e.getMessage());
			result.put("result", "");
			logger.error("保存规则失败：{}",e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 *Discription:删除规则.
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2018年5月9日 neusoft [变更描述]
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONObject delete(String ruleId){
		JSONObject result = new JSONObject();
		try {
			conditionRuleService.delete(ruleId);
			result.put("code", 0);
			result.put("msg", "操作成功");
		} catch (Exception e) {
			result.put("code", 100);
			result.put("msg", e.getMessage());
			result.put("result", "");
			logger.error("删除规则失败：{}",e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 *Discription:跳转常用参数选择页面.
	 *@param type
	 *@param selectedId
	 *@param selectedText
	 *@param model
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2018年5月9日 neusoft [变更描述]
	 */
    @RequestMapping(value = "openParam")
    public String openParam(String type,String selectedId,String selectedText,Model model) {
        try {
	        //常用参数
	        List<Map<String, String>> paramList = conditionRuleService.loadParam();
	        model.addAttribute("paramList", paramList);
	        //参数
	        List<Map<String, String>> list = conditionRuleService.loadParamConf();
            String string = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
            JSONArray array = JSONArray.parseArray(string);
            model.addAttribute("list", array);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "prss/stand/conditionParam";
    }
    
    /**
     * 
     *Discription:新增保存常用参数.
     *@param data
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月9日 neusoft [变更描述]
     */
    @RequestMapping(value = "saveParam")
    @ResponseBody
    public JSONObject saveParam(String data) {
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray formData = JSONArray.parseArray(data);
        JSONObject result = new JSONObject();
        try {
            conditionRuleService.saveParam(formData);
            result.put("code", 0);
            result.put("msg", "操作成功！");
            List<Map<String, String>> paramList = conditionRuleService.loadParam();
            result.put("list", paramList);
        } catch (Exception e) {
            result.put("code", 100);
            result.put("msg", e.getMessage());
            result.put("result", "");
            logger.error("保存表达式失败：{}", e.getMessage());
        }
        return result;
    }
    
}
