package com.neusoft.prss.scheduling.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.scheduling.service.WorkerDivisionService;

@Controller
@RequestMapping(value = "${adminPath}/division/info")
public class WorkerDivisionController extends BaseController {

    @Autowired
    private WorkerDivisionService workerDivisionService;

    /**
    * 
    *Discription:跳转人员分工页面
    *@param model
    *@return
    *@return:返回值意义
    *@author:lirr
    *@update:2017年11月2日 lirr [变更描述]
    */
    @RequestMapping(value = "list")
    public String list(Model model,String operator) {
        JSONArray templateList = new JSONArray();
        String officeId = UserUtils.getUser().getOffice().getId();
        templateList = workerDivisionService.getTemplateList(officeId);
        JSONArray group = workerDivisionService.getGroups();
        model.addAttribute("groups", group);
        model.addAttribute("templateList", templateList);
        model.addAttribute("operator", operator);
        return "prss/scheduling/workerDivisionList";
    }

    /**
     * 
     *Discription:得到展现表格的表头
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月2日 lirr [变更描述]
     */
    @RequestMapping(value = "getTableHeader")
    @ResponseBody
    public String getTableHeader(HttpServletRequest request) {
        Map<String,Object> params = new HashMap<String,Object>();
        String officeId = UserUtils.getUser().getOffice().getId();
        params.put("officeId", officeId);
        JSONObject obj = new JSONObject();
        JSONArray arr1 = workerDivisionService.getTableHeader(params);
        String limit = workerDivisionService.getDimLimit(params);
        params.put("limitStr", limit);
        Map<String,Object> limitMap = workerDivisionService.getLimitList(params);
        obj.put("rstypeArr", arr1);
        obj.put("limitStr", limit);
        obj.put("limit0", limitMap.get("0"));
        obj.put("limit1", limitMap.get("1"));
        obj.put("limit2", limitMap.get("2"));
        obj.put("aptiLimt", limitMap.get("apti"));
        String result = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:得到表格数据
     *@param pageSize
     *@param pageNumber
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月2日 lirr [变更描述]
     */
    @RequestMapping(value = "getGridData")
    @ResponseBody
    public String getGridData(String searchName,String templateId,String group,String operator) {
        Map<String,Object> param = new HashMap<String,Object>();
        String officeId = UserUtils.getUser().getOffice().getId();
        String groupStr = "";
        String decodeSearchNma = "";
        try {
        	if("%".equals(group)){
        		groupStr = group;
        	}else{
        		groupStr = java.net.URLDecoder.decode(group,"UTF-8");
        	}
        	
        	decodeSearchNma = java.net.URLDecoder.decode(searchName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        param.put("officeId", officeId);
        param.put("searchName", decodeSearchNma == "" ? "%" : decodeSearchNma);
        param.put("templateId", templateId == "" ? "all" : templateId);
        param.put("operator", operator);
        param.put("group", groupStr == "" ? "%" : groupStr);
        Map<String,Object> mp = workerDivisionService.getGridData(param);
        String result = JSON.toJSONString(mp.get("rows"), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:保存数据
     *@param data
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月3日 lirr [变更描述]
     */
    @RequestMapping(value = "saveInfo")
    @ResponseBody
    public String saveInfo(String data,String tempName,String tempId, String templateConf , String addTemp,String operator) {
        String msg = "";
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray dataArray = JSONArray.parseArray(data);
        String officeId = UserUtils.getUser().getOffice().getId();
        // 判断是否是添加模板
        boolean selectTemplate = StringUtils.isEmpty(addTemp);
        JSONArray dataIntegration = toDataIntegration(dataArray, templateConf,selectTemplate,operator);
//	        msg = workerDivisionService.saveInfo(dataArray, officeId, tempName, tempId);        	
        	msg = workerDivisionService.saveInfo(dataIntegration, officeId, tempName, tempId,operator);
        //调用服务
//        transformService.doTransform(officeId, 0);
        return msg;
    }
    
    /**
     * 
     *Discription:修改模板名称
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月3日 lirr [变更描述]
     */
    @RequestMapping(value = "changeName")
    @ResponseBody
    public String changeName(String tempName,String tempId, String templateConf ) {
        String msg = "";
        String officeId = UserUtils.getUser().getOffice().getId();
        	msg = workerDivisionService.changeName(officeId, tempName, tempId);
        return msg;
    }

    /**
     * 数据整合
     * @param dataArray 
     * @param operator 
     * @date 2017/12/05
     * @return
     */
    private JSONArray toDataIntegration(JSONArray dataArray, String templateConf , boolean selectTemlate, String operator) {
         // 数据库查询出来的数据
         JSONObject oData;
         // 页面得到的数据
         JSONObject nData;
         // 修改前数据
         if("new".equals(templateConf)) { // 针对空模版
        	 templateConf = "";
         }
         JSONArray data = new JSONArray();
         if(selectTemlate){
	         String result = getGridData("", templateConf,"%",operator);
	         data = JSONArray.parseArray(result);
         }
         // 数据ID
         Object dataId;
         // 整合数据
         if(!dataArray.isEmpty()) {
			 for (int i = 0; i < dataArray.size(); i++) {
				 nData = (JSONObject) dataArray.get(i);
				 dataId = nData.get("ID");
				 if(null == dataId || "".equals(dataId)) { // 新增数据
					 data.add(nData);
				 } else { // 修改数据
					 for (int j = 0; j < data.size(); j++) {
						 oData = (JSONObject) data.get(j);
						 if(null != oData.get("ID")) {
							 if(oData.get("ID").toString().equals(dataId.toString())) {
								 data.remove(j);
								 data.add(nData);
								 break;
							 }
						 }
					 }
				 }
			 }
         }
		return data;
    }
    /**
     * 
     *Discription:分工增加时,根据用户ID得到资质列表
     *@param workId
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月3日 lirr [变更描述]
     */
    @RequestMapping(value = "getAptiduteGridData")
    @ResponseBody
    public JSONArray getAptiduteGridData(String workId) {
        Map<String,Object> param = new HashMap<String,Object>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("workId", workId);
        return workerDivisionService.getAptiduteGridData(param);
    }

    /**
     * 
     *Discription:跳转分工维护页面
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月4日 lirr [变更描述]
     */
    @RequestMapping(value = "showMaintainPage")
    public String showMaintainPage(Model model) {
        JSONArray templateList = new JSONArray();
        String officeId = UserUtils.getUser().getOffice().getId();
        templateList = workerDivisionService.getTemplateList(officeId);
        JSONArray group = workerDivisionService.getGroups();
        model.addAttribute("groups", group);
        model.addAttribute("templateList", templateList);
        model.addAttribute("haveTemp", templateList.size());
        return "prss/scheduling/workerDivisionMaintain";
    }

    /**
     * 
     *Discription:得到模板数据列表
     *@param pageSize
     *@param pageNumber
     *@param searchName
     *@param templateId
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月4日 lirr [变更描述]
     */
    @RequestMapping(value = "getTempGridData")
    @ResponseBody
    public String getTempGridData(String searchName,String templateId,String group) {
    	String groupStr = "";
    	String decodeSearchName = "";
        try {
        	groupStr = java.net.URLDecoder.decode(group,"UTF-8");
        	decodeSearchName = java.net.URLDecoder.decode(searchName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Map<String,Object> param = new HashMap<String,Object>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("searchName", decodeSearchName == "" ? "%" : decodeSearchName);
        param.put("templateId", "new".equals(templateId) ? "" : templateId);
        param.put("group", groupStr == "" ? "%" : groupStr);
        Map<String,Object> mp = workerDivisionService.getGridData(param);
        String result = JSON.toJSONString(mp.get("rows"), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     *Discription:查询模板
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月4日 lirr [变更描述]
     */
    @RequestMapping(value = "getTempList")
    @ResponseBody
    public String getTempList(HttpServletRequest request) {
        String officeId = UserUtils.getUser().getOffice().getId();
        return JSON.toJSONString(workerDivisionService.getTemplateList(officeId), SerializerFeature.WriteMapNullValue);
    }

    /**
     * 
     *Discription:删除模板
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月4日 lirr [变更描述]
     */
    @RequestMapping(value = "deleteTemp")
    @ResponseBody
    public String deleteTemp(String tempId) {
        String officeId = UserUtils.getUser().getOffice().getId();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("officeId", officeId);
        param.put("tempId", tempId);
        return workerDivisionService.getDeleteTemp(param);
    }
    
    
  /*  @RequestMapping(value = "workerDivisionEdit")
    public String workerDivisionEdit(Model model,String operator) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	String officeId = UserUtils.getUser().getOffice().getId();
        params.put("officeId", officeId);
        // 调用查询表头方法，查询需要展示的信息
        JSONArray arr1 = workerDivisionService.getTableHeader(params);
        String limit = workerDivisionService.getDimLimit(params);
        params.put("limitStr", limit);
        Map<String,Object> limitMap = workerDivisionService.getLimitList(params);
        model.addAttribute("rstypeArr", arr1);
        model.addAttribute("limitStr", limit.split(","));
        model.addAttribute("limit0", limitMap.get("0"));
        model.addAttribute("limit1", limitMap.get("1"));
        model.addAttribute("limit2", limitMap.get("2"));
        model.addAttribute("aptiLimt", limitMap.get("apti"));
        // 查询员工分工信息
        params.put("searchName", "%");
        params.put("templateId", "all");
        params.put("operator", operator);
        Map<String,Object> mp = workerDivisionService.getGridData(params);
        model.addAttribute("entityData", ((List)mp.get("rows")).get(0));
        
    	return "prss/scheduling/workerDivisionEdit";
    }*/
    
    
}
