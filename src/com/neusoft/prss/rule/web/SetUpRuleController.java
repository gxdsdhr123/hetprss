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
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.rule.entity.Drools;
import com.neusoft.prss.rule.entity.RuleExtend;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleSetUpVO;
import com.neusoft.prss.rule.service.SetUpRuleService;


@Controller
@RequestMapping(value = "${adminPath}/rule/setUpRule")
public class SetUpRuleController extends BaseController {
	
	@Autowired
	private SetUpRuleService setUpRuleService;
	
	@RequestMapping(value = "list")
    public String list() {
        return "prss/rule/setup/setUpRuleList";
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
        return setUpRuleService.getRuleList(param);
    }
	
	@RequestMapping(value = "form")
	public String form(Model model, @RequestParam(value = "id") String id,
			@RequestParam(value = "type", required = true) String type){
		
		//规则基本信息
		RuleInfo ruleInfo = new RuleInfo();
		//获取回显附加属性
		RuleExtend ruleExtend = new RuleExtend();
		if("update".equals(type)){ //更新
			ruleInfo = setUpRuleService.loadRuleInfo(id);
			ruleExtend = setUpRuleService.loadRuleExtend(id);
			String colids = ruleInfo.getColids();
			//将扩展条件ID去掉
			if(ruleExtend.getAircraftNumberList()!=null&&!ruleExtend.getAircraftNumberList().isEmpty()){
				colids=colids.replace(Drools.SYS_ACTTYPE_ID+",", "");
			}
			if(ruleExtend.getFlightNumberList()!=null&&!ruleExtend.getFlightNumberList().isEmpty()){
				colids=colids.replace(Drools.SYS_FLIGHTNUMBER_ID+",", "");
			}
//			if(ruleExtend.getBranch()!=null&&!ruleExtend.getBranch().isEmpty()){
//                colids=colids.replace(Drools.SYS_BRANCH_ID+",", "");
//            }
			
			ruleInfo.setColids(colids);
			
			model.addAttribute("ruleInfo", ruleInfo);
			
			model.addAttribute("ruleExtend", ruleExtend);
			model.addAttribute("defaultFlightNumber",ruleExtend.getFlightNumberList());//已选航班号
			model.addAttribute("defaultAtcactype",ruleExtend.getAircraftNumberList());//已选的机号 注册号
//            model.addAttribute("branch",ruleExtend.getBranch());//已选的机号 注册号
	        RuleSetUpVO setUpVO = setUpRuleService.getSetUp(id);
	        model.addAttribute("setUpVO", setUpVO);
		}
		//进出港选项列表
        List<Map<String, String>> inOutPortList = setUpRuleService.loadInOutPortType();
        model.addAttribute("inOutPortList", inOutPortList);
        
        model.addAttribute("type",type);
		return "prss/rule/setup/setUpRuleSet";
	}
	
	@RequestMapping(value = "createDrl")
	@ResponseBody
	public JSONObject createDrl(HttpServletRequest request) {
		String targetStr = request.getParameter("expression");
		JSONObject result = new JSONObject();
		try {
			String drl = setUpRuleService.createDrl(targetStr);
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
			result = setUpRuleService.save(formData);
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
			
			setUpRuleService.delete(ruleId);
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
        model.addAttribute("items", itemsObj.getJSONArray("item"));
        model.addAttribute("sitems", StringUtils.isBlank(selectedId)?"":selectedId.split(","));
        model.addAttribute("type", type);
        model.addAttribute("selectedId", selectedId);
        model.addAttribute("selectedText", selectedText);
        return "prss/rule/setup/fdFilterCheck";
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
        if ("flightNumber".equals(type)) {
            JSONArray fligthNumbers = setUpRuleService.loadFlightNumber(param);
            int count = fligthNumbers.size();
            result.put("count", count);
            if (count != 0) {
                result.put("item", fligthNumbers);
            } else {
                result.put("item", new JSONArray());
            }
        } else if ("aircraft".equals(type)) {
            JSONArray aircrafts = setUpRuleService.loadAircraftNumber(param);
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
}
