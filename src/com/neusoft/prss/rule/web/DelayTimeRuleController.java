package com.neusoft.prss.rule.web;

import java.io.UnsupportedEncodingException;
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
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.rule.entity.Drools;
import com.neusoft.prss.rule.entity.RuleExtend;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleSetUpVO;
import com.neusoft.prss.rule.service.DelayTimeRuleService;


@Controller
@RequestMapping(value = "${adminPath}/rule/delayTime")
public class DelayTimeRuleController extends BaseController {
	
	@Autowired
	private DelayTimeRuleService delayTimeRuleService;
	
	@RequestMapping(value = "list")
    public String list() {
        return "prss/rule/delaytime/delayTimeRuleList";
    }
	
    @RequestMapping(value = "ruleList")
    @ResponseBody
    public Map<String,Object> ruleList(
    		int pageSize,int pageNumber,String cnName,
    		HttpServletRequest request, HttpServletResponse response) {
    	cnName = StringEscapeUtils.unescapeHtml4(cnName);
    	try {
    		cnName = java.net.URLDecoder.decode(cnName,"utf-8");
		} catch (Exception e) {}
    	
        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("name", cnName);
        param.put("officeId",UserUtils.getUser().getOffice().getId());//部门ID
        param.put("userId", UserUtils.getUser().getId());//用户ID
        return delayTimeRuleService.getRuleList(param);
    }
    
    @RequestMapping(value = "detailList")
    @ResponseBody
    public List<Map<String,Object>> detailList(String id,
            HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("id", id);
        return delayTimeRuleService.getDetailList(param);
    }
	
	@RequestMapping(value = "form")
	public String form(Model model, @RequestParam(value = "id") String id,
			@RequestParam(value = "type", required = true) String type){
		
		//获取回显附加属性
		if("update".equals(type)){ //更新
		    JSONObject delayTimeVO = delayTimeRuleService.getDelayTimeInfo(id);
	        model.addAttribute("delayTimeVO", delayTimeVO);
		}
		//进出港选项列表
        List<Map<String, String>> inOutPortList = delayTimeRuleService.loadInOutPortType();
        model.addAttribute("inOutPortList", inOutPortList);
        
        model.addAttribute("type",type);
		return "prss/rule/delaytime/delayTimeRuleSet";
	}
	
	@RequestMapping(value = "createDrl")
	@ResponseBody
	public JSONObject createDrl(HttpServletRequest request) {
		String targetStr = request.getParameter("expression");
		JSONObject result = new JSONObject();
		try {
			String drl = delayTimeRuleService.createDrl(targetStr);
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
		    formData.put("creator", UserUtils.getUser().getId());
			result = delayTimeRuleService.save(formData);
		} catch (Exception e) {
			result.put("code", 100);
			result.put("msg", "操作失败");
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
			
			delayTimeRuleService.delete(ruleId);
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
     * 
     * Discription:筛选弹出复选框.
     * 
     * @return
     * @return:返回值意义
     * @update:2017年11月13日 yuzd [变更描述]
     */
    @RequestMapping(value = "openCheck")
    public String openCheck(String type,String selectedId,String selectedText,Model model) {
        JSONObject itemsObj = getOptions(null, "1", "0", type);
        JSONArray sitems = new JSONArray();
        if(StringUtils.isNotBlank(selectedText)) {
            String[] idStrings = selectedId.split(",");
            String[] textStrings = selectedText.split(",");
            for (int i = 0; i < idStrings.length; i++) {
                String idString = idStrings[i];
                String textString = textStrings[i];
                JSONObject object = new JSONObject();
                object.put("id", idString);
                object.put("text", textString);
                sitems.add(object);
            }
        }
        model.addAttribute("items", itemsObj.getJSONArray("item"));
        model.addAttribute("sitems", sitems);
        model.addAttribute("type", type);
        model.addAttribute("selectedId", selectedId);
        model.addAttribute("selectedText", selectedText);
        return "prss/rule/delaytime/fdFilterCheck";
    }
    
    
    /**
     * 从给定的jsonarray中去除已选的值
     *Discription:方法功能中文描述.
     *@return
     *@return:返回值意义
     *@author:yuzd
     *@update:2018年1月16日yuzd [变更描述]
     */
    public JSONObject getResultWithoutSelected(JSONArray arr,String selectedId) {
        String[] ids = selectedId.split(",");
        JSONObject resultObj = new JSONObject();
        JSONArray sArray = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (StringUtils.isNotBlank(selectedId)) {
                for(int j=0;j<ids.length;j++) {
                    if(ids[j].equals(obj.getString("CODE"))) {
                        sArray.add(obj);
                    }
                }
            }
        }
        arr.removeAll(sArray);
        /*for (int i = 0; i < sArray.size(); i++) {
            for (int j = 0; j < arr.size(); j++) {
                if (sArray.get(i).equals(arr.get(j))) {
                   arr.removeAll(c)
                }
            }
        }*/
        resultObj.put("arr", arr);
        resultObj.put("sArray", sArray);
        return resultObj;
    }
    
    /**
     * 
     * Discription:筛选-机型、机场、航空公司下拉选项内容.
     * 
     * @param q
     * @param page
     * @param limit
     * @param type
     * @return
     * @return:返回值意义
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "getOptions")
    @ResponseBody
    public JSONObject getOptions(String q,String page,String limit,String type) {
        page = page == null ? "1" : page;
        JSONObject result = new JSONObject();
        int start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit) + 1;
        int end = Integer.parseInt(page) * Integer.parseInt(limit);
        end = end == 0 ? 1000 : end;
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("q", q);
        param.put("start", start);
        param.put("end", end);
        if ("acttypeCode".equals(type)) {//机型
            JSONArray fligthNumbers = delayTimeRuleService.loadActtypeCode(param);
            int count = fligthNumbers.size();
            result.put("count", count);
            if (count != 0) {
                result.put("item", fligthNumbers);
            } else {
                result.put("item", new JSONArray());
            }
        } else if ("alnCode".equals(type)) {//航空公司
            JSONArray aircrafts = delayTimeRuleService.loadAlnCode(param);
            int count = aircrafts.size();
            result.put("count", count);
            if (count != 0) {
                result.put("item", aircrafts);
            } else {
                result.put("item", new JSONArray());
            }
        } 
        return result;
    }
    @RequestMapping(value = "filterInfo")
    @ResponseBody
    public boolean filterInfo(String data) {
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONObject formData = JSONObject.parseObject(data);
        boolean result = true;
        try {
            result = delayTimeRuleService.filterInfo(formData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @RequestMapping(value = "guide")
    public String guide(HttpServletRequest request,HttpServletResponse response,Model model) {
        String schema = request.getParameter("schema");//模块
        String colids = request.getParameter("colids");
        String condition = request.getParameter("condition");
        String expression = request.getParameter("expression");
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("schema", schema);
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
        condition = delayTimeRuleService.getColumn(data);
        String id = request.getParameter("id");
        model.addAttribute("condition", condition);
        model.addAttribute("id", id);
        model.addAttribute("schema", schema);
        return "prss/rule/delaytime/guide";
    }
}
