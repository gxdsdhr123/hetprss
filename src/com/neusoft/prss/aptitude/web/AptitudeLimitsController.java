/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月29日 上午11:37:42
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.aptitude.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.neusoft.framework.common.utils.ztree.ZTreeUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.aptitude.entity.AreaConfEntity;
import com.neusoft.prss.aptitude.entity.OfficeLimConf;
import com.neusoft.prss.aptitude.service.AptitudeLimitsService;

@Controller
@RequestMapping(value = "${adminPath}/aptitude/aptitudeLimits")
public class AptitudeLimitsController extends BaseController {

	@Autowired
	private AptitudeLimitsService aptitudeLimitsService;

	@RequestMapping(value = "list")
	public String list(Model model) {
		String officeId = UserUtils.getUser().getOffice().getId();
		JSONArray offices = aptitudeLimitsService.getOffice(officeId);
		model.addAttribute("offices", offices);
		return "prss/aptitude/aptitudeLimitsList";
	}

	@ResponseBody
	@RequestMapping(value = "getAreaConfData")
	public String getAreaConfData() {
		String officeId = UserUtils.getUser().getOffice().getId();
		List<String> officeIds = aptitudeLimitsService.getOfficeId(officeId);
		List<AreaConfEntity> list = null;
		if (officeIds.size() > 0) {
			list = aptitudeLimitsService.getAreaList(officeIds);
			for (int i = 0; i < list.size(); i++) {
				switch (Integer.parseInt(list.get(i).getLimitType())) {
				case 0:
					list.get(i).setLimitType("机位");
					break;
				case 1:
					list.get(i).setLimitType("机型");
					break;
				case 2:
					list.get(i).setLimitType("航空公司");
					break;
				}
				switch (Integer.parseInt(list.get(i).getLimitLevel())) {
				case 0:
					list.get(i).setLimitLevel("资质");
					break;
				case 1:
					list.get(i).setLimitLevel("分工");
					break;
				}
			}
		}
		String result = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "changeData")
	public String changeData(String officeId) {
		List<String> officeIds = new ArrayList<>();
		officeIds.add(officeId);
		List<AreaConfEntity> list = aptitudeLimitsService.getAreaList(officeIds);
		for (int i = 0; i < list.size(); i++) {
			switch (Integer.parseInt(list.get(i).getLimitType())) {
			case 0:
				list.get(i).setLimitType("机位");
				break;
			case 1:
				list.get(i).setLimitType("机型");
				break;
			case 2:
				list.get(i).setLimitType("航空公司");
				break;
			}
			switch (Integer.parseInt(list.get(i).getLimitLevel())) {
			case 0:
				list.get(i).setLimitLevel("资质");
				break;
			case 1:
				list.get(i).setLimitLevel("分工");
				break;
			}
		}
		String result = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
		return result;
	}
	
	@RequestMapping(value = "officeArr")
	public String officeArr(Model model, String id) {
		OfficeLimConf officeLimConf = aptitudeLimitsService.getOfficeLim(id);
		if(officeLimConf == null){
			officeLimConf = new OfficeLimConf();
			officeLimConf.setOfficeId(id);
			officeLimConf.setJwLimit(0);
			officeLimConf.setJxLimit(0);
			officeLimConf.setHsLimit(0);
		}
		model.addAttribute("officeLimConf", officeLimConf);
		return "prss/aptitude/officeAttForm";
	}
	
	/**
	 * 
	 *Discription:保存部门属性.
	 *@param officeLimConf
	 *@return
	 *@return:返回值意义
	 *@author:Heqg
	 *@update:2017年10月31日 Heqg [变更描述]
	 */
	@ResponseBody
	@RequestMapping(value = "saveOfficeArr")
	public String saveOfficeArr(String officeLimConf) {
		JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(officeLimConf));
		OfficeLimConf conf = new OfficeLimConf();
		conf.setOfficeId(json.getString("officeId"));
		conf.setJwLimit(Integer.parseInt(json.getString("jwLimit")));
		conf.setJxLimit(Integer.parseInt(json.getString("jxLimit")));
		conf.setHsLimit(Integer.parseInt(json.getString("hsLimit")));
		aptitudeLimitsService.updateOfficeLim(conf);
		return "success";
	}
	
	@RequestMapping(value = "newAptitude")
	public String newAptitude(Model model, String id) {
		JSONObject office = aptitudeLimitsService.getOfficeInfo(id);
		model.addAttribute("office", office);
		OfficeLimConf officeLimConf = aptitudeLimitsService.getOfficeLim(id);
		if(officeLimConf == null){
			officeLimConf = new OfficeLimConf();
			officeLimConf.setOfficeId(id);
			officeLimConf.setJwLimit(0);
			officeLimConf.setJxLimit(0);
			officeLimConf.setHsLimit(0);
		}
		model.addAttribute("officeLimConf", officeLimConf);
		return "prss/aptitude/newAptitude";
	}
	
	@RequestMapping(value = "aptitudeJW")
	public String aptitudeJW(Model model) {
		JSONArray json = aptitudeLimitsService.getAllJW();
		model.addAttribute("treeData", json);
		model.addAttribute("stree", "[]");
		return "prss/aptitude/aptitudeJW";
	}
	
	@RequestMapping(value = "aptitudeJX")
	public String aptitudeJX(Model model) {
		JSONArray json = aptitudeLimitsService.getAllJX();
		model.addAttribute("ajson", json);
		return "prss/aptitude/aptitudeJX";
	}
	
	@RequestMapping(value = "aptitudeHS")
	public String aptitudeHS(Model model) {
		JSONArray json = aptitudeLimitsService.getAllHS();
		model.addAttribute("ajson", json);
		return "prss/aptitude/aptitudeHS";
	}
	
	/**
	 * 
	 *Discription:删除区域.
	 *@param id
	 *@return
	 *@return:返回值意义
	 *@author:Heqg
	 *@update:2017年10月31日 Heqg [变更描述]
	 */
	@ResponseBody
	@RequestMapping(value = "delArea")
	public String delArea(String id) {
		if(aptitudeLimitsService.isUsed(id)){
			return "used";
		}
		aptitudeLimitsService.delArea(id);
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(String string) {
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		AreaConfEntity areaConfEntity = new AreaConfEntity();
		int id = aptitudeLimitsService.getSeq();
		areaConfEntity.setId(String.valueOf(id));
		areaConfEntity.setOfficeId(aftJSON.getString("id"));
		areaConfEntity.setAreaName(aftJSON.getString("name"));
		areaConfEntity.setLimitType(aftJSON.getString("att"));
		if(aftJSON.getString("type").equals("1")){
			areaConfEntity.setLimitLevel("1");
			areaConfEntity.setParentId(aftJSON.getString("areType"));
		} else {
			areaConfEntity.setLimitLevel("0");
			areaConfEntity.setParentId("");
		}
		areaConfEntity.setCreatorId(UserUtils.getUser().getId());
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		areaConfEntity.setCreateTime(dateformat.format(new Date()));
		String info = aftJSON.getString("info");
		aptitudeLimitsService.doInsertAreaConf(areaConfEntity, info);
		return "success";
	}
	
	@RequestMapping(value = "editAptitude")
	public String editAptitude(Model model, String id) {
		AreaConfEntity areaConf = aptitudeLimitsService.getAreaById(id);
		model.addAttribute("areaConf", areaConf);
		JSONObject office = aptitudeLimitsService.getOfficeInfo(areaConf.getOfficeId());
		model.addAttribute("office", office);
		if(areaConf.getLimitLevel().equals("1")){
			JSONArray select = aptitudeLimitsService.getSelect(areaConf.getLimitType(), areaConf.getOfficeId());
			model.addAttribute("select", select);
		}
		return "prss/aptitude/editAptitude";
	}
	
	@ResponseBody
	@RequestMapping(value = "getSelect")
	public JSONArray getSelect(String type, String id) {
		return aptitudeLimitsService.getSelect(type, id);
	}
	
	@RequestMapping(value = "getAreaInfo")
	public String getAreaInfo(Model model, String id) {
		AreaConfEntity areaConfEntity = aptitudeLimitsService.getAreaById(id);
		if(areaConfEntity.getLimitType().equals("0")){
			JSONArray json = aptitudeLimitsService.getJWElementsById(id);
			model.addAttribute("treeData", json);
			model.addAttribute("stree", "[]");
			return "prss/aptitude/aptitudeJW";
		}else if(areaConfEntity.getLimitType().equals("1")){
			JSONArray json = aptitudeLimitsService.getAreaElementsById(id);
			model.addAttribute("ajson", json);
			return "prss/aptitude/aptitudeJX";
		}else if(areaConfEntity.getLimitType().equals("2")){
			JSONArray json = aptitudeLimitsService.getAreaElementsById(id);
			model.addAttribute("ajson", json);
			return "prss/aptitude/aptitudeHS";
		}else{
			return null;
		}
	}
	
	@RequestMapping(value = "editLevel0")
	public String editLevel0(Model model, String id) {
		AreaConfEntity areaConfEntity = aptitudeLimitsService.getAreaById(id);
		if(areaConfEntity.getLimitType().equals("0")){
			JSONArray jwData = aptitudeLimitsService.getAllJW();
			JSONArray json = aptitudeLimitsService.getJWElementsById(id);
//			jwData.removeAll(json);
			jwData=ZTreeUtils.removeZTreeNode(jwData, json);
			model.addAttribute("treeData", jwData);
			model.addAttribute("stree", json);
			return "prss/aptitude/aptitudeJW";
		}else if(areaConfEntity.getLimitType().equals("1")){
			JSONArray jxData = aptitudeLimitsService.getAllJX();
			JSONArray json = aptitudeLimitsService.getAreaElementsById(id);
			jxData.removeAll(json);
			model.addAttribute("ajson", jxData);
			model.addAttribute("sjson", json);
			return "prss/aptitude/aptitudeJX";
		}else if(areaConfEntity.getLimitType().equals("2")){
			JSONArray hsData = aptitudeLimitsService.getAllHS();
			JSONArray json = aptitudeLimitsService.getAreaElementsById(id);
			hsData.removeAll(json);
			model.addAttribute("ajson", hsData);
			model.addAttribute("sjson", json);
			return "prss/aptitude/aptitudeHS";
		}else{
			return null;
		}
	}
	
	@RequestMapping(value = "editLevel1")
	public String editLevel1(Model model, String id, String type) {
		AreaConfEntity areaConfEntity = aptitudeLimitsService.getAreaById(id);
		if(areaConfEntity.getLimitType().equals("0")){
			JSONArray ajson = aptitudeLimitsService.getJWElementsById(type);
			JSONArray sjson = aptitudeLimitsService.getJWElementsById(id);
			ajson=ZTreeUtils.removeZTreeNode(ajson, sjson);
			model.addAttribute("treeData", ajson);
			model.addAttribute("stree", sjson);
			return "prss/aptitude/aptitudeJW";
		}else if(areaConfEntity.getLimitType().equals("1")){
			JSONArray json = aptitudeLimitsService.getAreaElementsById(id);
			JSONArray ajson = aptitudeLimitsService.getAreaElementsById(type);
			ajson.removeAll(json);
			model.addAttribute("ajson", ajson);
			model.addAttribute("sjson", json);
			return "prss/aptitude/aptitudeJX";
		}else if(areaConfEntity.getLimitType().equals("2")){
			JSONArray json = aptitudeLimitsService.getAreaElementsById(id);
			JSONArray ajson = aptitudeLimitsService.getAreaElementsById(type);
			ajson.removeAll(json);
			model.addAttribute("ajson", ajson);
			model.addAttribute("sjson", json);
			return "prss/aptitude/aptitudeHS";
		}else{
			return null;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "saveEdit")
	public String saveEdit(String string) {
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(string));
		AreaConfEntity areaConfEntity = new AreaConfEntity();
		areaConfEntity.setId(aftJSON.getString("id"));
		areaConfEntity.setAreaName(aftJSON.getString("name"));
		if(aftJSON.getString("type").equals("1")){
			areaConfEntity.setParentId(aftJSON.getString("areType"));
		} else {
			areaConfEntity.setParentId("");
		}
		areaConfEntity.setCreatorId(UserUtils.getUser().getId());
		String info = aftJSON.getString("info");
		aptitudeLimitsService.updateArea(areaConfEntity, info);
		return "success";
	}
	
}
