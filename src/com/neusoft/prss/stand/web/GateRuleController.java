package com.neusoft.prss.stand.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.service.ParamCommonService;
import com.neusoft.prss.grid.entity.GridColumn;
import com.neusoft.prss.grid.service.GridColumnService;
import com.neusoft.prss.stand.service.GateRuleService;

@Controller
@RequestMapping(value = "${adminPath}/stand/gate")
public class GateRuleController extends BaseController {
	
	@Autowired
	private GateRuleService gateRuleService;
	
	@Resource
    private ParamCommonService paramCommonService;
	
	@Autowired
	private GridColumnService gridService;
	
	@Autowired
    private CacheService cacheService;
	
	@RequestMapping(value = "list")
    public String list() {
        return "prss/stand/gateRuleList";
    }
	
    @RequestMapping(value = "ruleList")
    @ResponseBody
    public Map<String,Object> ruleList(
    		int pageSize,int pageNumber,String ruleName,
    		HttpServletRequest request, HttpServletResponse response) {
    	ruleName = StringEscapeUtils.unescapeHtml4(ruleName);
    	try {
    		ruleName = java.net.URLDecoder.decode(ruleName,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("name", ruleName);
        return gateRuleService.getRuleList(param);
    }
	
	@RequestMapping(value = "form")
	public String form(Model model, @RequestParam(value = "id") String id,@RequestParam(value = "type", required = true) String type,@RequestParam(value = "schemaId")String schemaId){
		//规则基本信息
		JSONObject ruleInfo = new JSONObject();
		 //登机口
        JSONArray gateList = cacheService.getOpts("dim_gate", "gate_code", "description_cn");
        //登机口可选列表
        JSONArray gateSelectableList = new JSONArray();
        //登机口已选列表
        JSONArray gateSelectedList = new JSONArray();
        
		if("update".equals(type)){ //更新
			ruleInfo = gateRuleService.loadRuleInfo(id);
			String condition = ruleInfo.getString("TEXT");
            String expression = ruleInfo.getString("DRL_STR");
            String colids = ruleInfo.getString("COLIDS");
            if(!StringUtils.isBlank(condition)) {
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("schema", "RULE");
                data.put("colids", colids);
                try {
                    condition = java.net.URLDecoder.decode(condition,"utf-8");
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
			//登机口选择列表
			String gateStr = ruleInfo.getString("GATE_STR");
			if(!StringUtils.isBlank(gateStr)) {
				Map<String,JSONObject> tmpGateSelectedMap = new HashMap<String, JSONObject>();
	            for (int i = 0; i < gateList.size(); i++) {
	            	//更新时保证选择的登机口按照保存的顺序展示
	                JSONObject gateObj = gateList.getJSONObject(i);
	                String gate = gateObj.getString("id");
	                if(gateStr.contains(gate)){
	                	tmpGateSelectedMap.put(gate, gateObj);
	                }else{
	                	gateSelectableList.add(gateObj);
	                }
	            }
	            String[] gateArr = gateStr.split(",");
	            for(String gate : gateArr){
	            	gateSelectedList.add(tmpGateSelectedMap.get(gate));
	            }
	        }else{
	        	gateSelectableList = gateList;
	        }
			model.addAttribute("gateSelectedList", gateSelectedList);
			model.addAttribute("gateSelectableList", gateSelectableList);
		}else{
			model.addAttribute("gateSelectableList", gateList);
		}
        //常用表达式参数 通过SchedulingListService获取
        //List<Map<String, String>> paramList = gateRuleService.loadParam();//loadRuleParam通过配置获取表达式字段，loadParam自定临时表关联查询表达式
		//schemaId = 4 登机口模块表达式
		List<GridColumn> paramList = gridService.getSelectedList(UserUtils.getUser().getId(), schemaId);
		model.addAttribute("paramList", paramList);
        model.addAttribute("type",type);
		return "prss/stand/gateRuleSet";
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
			result = gateRuleService.save(formData);
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
			gateRuleService.delete(ruleId);
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
    public String openParam(String type,String selectedId,String selectedText,String schemaId,Model model) {
    	//用户已选列
    	List<GridColumn> selectedList = gridService.getSelectedList(UserUtils.getUser().getId(), schemaId);
    	model.addAttribute("selectedList", selectedList);
    	//用户可选列
    	List<GridColumn> selectableList = gridService.getSelectableList(UserUtils.getUser().getId(), schemaId);
    	model.addAttribute("selectableList", selectableList);
//        //已选列
//        List<Map<String, String>> paramList = gateRuleService.loadParam();
//        model.addAttribute("paramList", paramList);
//        //可选列
//        List<Map<String, String>> list = gateRuleService.loadParamConf();
//        model.addAttribute("list", list);
        return "prss/stand/gateParam";
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
    public JSONObject saveParam(String data,String schemaId) {
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray formData = JSONArray.parseArray(data);
        JSONObject result = new JSONObject();
        String userId = UserUtils.getUser().getId();
        List<Map<String,String>> params = new ArrayList<Map<String,String>>();
        Map<String,String> paramsMap = null;
        //编辑表达式id
        String id = "";
        //编辑表达式tilte
        String title = "";
        try {
//        	gateRuleService.saveParam(formData);
//          List<Map<String, String>> paramList = gateRuleService.loadParam();
        	int sortNum = 1;
        	for(int i=0;i<formData.size();i++){
        		String keyValStr = formData.getString(i);
        		if(keyValStr!=null&&!keyValStr.equals("")){
        			String[] keyValArr = keyValStr.split(";");
            		paramsMap = new HashMap<String, String>();
            		if(keyValArr.length==2){
            			id = keyValArr[0];
            			title = keyValArr[1];
            			paramsMap.put("user", userId);
            			paramsMap.put("id", id);
            			paramsMap.put("name", title);
            			paramsMap.put("desc", title);
            			paramsMap.put("sort", sortNum+"");
            			paramsMap.put("schema", schemaId);
            			paramsMap.put("class", "");
            			paramsMap.put("width", "");
            			sortNum++;
            		}
            		params.add(paramsMap);
        		}
        	}
        	String re = gridService.saveHeadInfo(params, schemaId, userId);
            if(re.equals("success")){
            	result.put("code", 0);
                result.put("msg", "操作成功！");
            }else{
            	 result.put("code", 100);
                 result.put("msg", "保存表达式失败！");
            }
            List<GridColumn> selectedList = gridService.getSelectedList(userId, schemaId);
            result.put("paramList", selectedList);
        } catch (Exception e) {
            result.put("code", 100);
            result.put("msg", e.getMessage());
            result.put("result", "");
            logger.error("保存表达式失败：{}", e.getMessage());
        }
        return result;
    }
    
}
