/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 上午10:20:13
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.common.web;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.service.ParamCommonService;

@Controller
@RequestMapping(value = "${adminPath}/param/common")
public class ParamCommonController extends BaseController {

    @Resource
    private ParamCommonService paramCommonService;
    
    @RequestMapping(value = "getVariableData")
    @ResponseBody
    public JSONArray getVariableData(HttpServletRequest request) {
        String schema = request.getParameter("schema");//模块 MM TM FD 
        String text = request.getParameter("text");
        Map<String,String> map = new HashMap<String,String>();
        map.put("schema", schema);
        try {
            if(text != null && !"".equals(text))
                text = java.net.URLDecoder.decode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("text", text);
        JSONArray data =paramCommonService.getVariable(map);
        return data;
    }
    
    /**
     * 
     *Discription:向导页面信息.
     *@param model
     *@return
     *@return:
     *@author:zhaol
     *@update:2017年9月8日  [变更描述]
     */
    @RequestMapping(value = "guide")
    public String guide(HttpServletRequest request,HttpServletResponse response,Model model) {
        String ruleId = request.getParameter("ruleId");
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
        condition = paramCommonService.getColumn(data);
        String id = request.getParameter("id");
        model.addAttribute("condition", condition);
        model.addAttribute("id", id);
        model.addAttribute("schema", schema);
        return "prss/common/guide";
    }
  
    @RequestMapping(value = "transtext")
    @ResponseBody
    public String transtext(HttpServletRequest request,HttpServletResponse response,Model model) {
        String result = "";
        String mtext = request.getParameter("mtext");
        String varcols = request.getParameter("varcols");
        String schema = request.getParameter("schema");
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("schema",schema);
        data.put("colids", varcols);
        data.put("text", mtext);
        result = paramCommonService.getColumn(data);
        
        return result ;
        
    }
    
    
    @RequestMapping(value = "toRuleList")
    public String toRuleList(HttpServletRequest request,HttpServletResponse response,Model model) {
        return "prss/common/ruleList";
    }
    
    @RequestMapping(value = "getRuleList")
    @ResponseBody
    public JSONArray getRuleList(HttpServletRequest request) {
        String schema = request.getParameter("schema");//模块 MM TM FD 
        String text = request.getParameter("text");
        Map<String,String> map = new HashMap<String,String>();
        map.put("schema", schema);
        try {
            if(text != null && !"".equals(text))
                text = java.net.URLDecoder.decode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("text", text);
        JSONArray data =paramCommonService.getRuleList(map);
        return data;
    }
    @RequestMapping(value = "getRule")
    @ResponseBody
    public String getRule(HttpServletRequest request){
        HashMap<String,String> data = new HashMap<String,String>();
        String id = request.getParameter("id");
        String ruleStr = "";
        data.put("id", id);
        data.put("schema", "RULE");
        JSONObject rule = paramCommonService.getRuleById(id);
        String colids = rule.getString("COLIDS");
        String expression = rule.getString("DRL_STR");
        data.put("colids", colids);
        data.put("expression", expression);
        ruleStr = paramCommonService.getColumn(data);
        return ruleStr;
        
    }
     
}
