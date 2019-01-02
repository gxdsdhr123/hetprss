/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午4:44:33
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.web;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.arrange.entity.AmbulatoryShiftsType;
import com.neusoft.prss.arrange.service.AmbulatoryShiftsTypeService;

@Controller
@RequestMapping(value = "${adminPath}/arrange/ambulatoryShifts")
public class AmbulatoryShiftsController extends BaseController {

	@Autowired
	private AmbulatoryShiftsTypeService ambulatoryShiftsTypeService;

	@RequestMapping(value = "list")
	public String list(Model model) {
		return "prss/arrange/ambulatoryShiftsList";
	}

	@ResponseBody
	@RequestMapping(value = "getAmbulatoryShiftsTypeData")
	public String getAmbulatoryShiftsTypeData() {
		String officeId = UserUtils.getUser().getOffice().getId();
		List<AmbulatoryShiftsType> list = ambulatoryShiftsTypeService.getAmbulatoryShiftsTypeList(officeId);
		String result = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
		return result;
	}

	@RequestMapping(value = "form")
	public String form(Model model, String id) {
		List<AmbulatoryShiftsType> list = ambulatoryShiftsTypeService.getASTypeById(id);
		model.addAttribute("AST", list);
		return "prss/arrange/ambulatoryShiftsForm";
	}

	@ResponseBody
	@RequestMapping(value = "save")
	public String save(String aft) {
		JSONObject aftJSON = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(aft));
		List<AmbulatoryShiftsType> list = new ArrayList<AmbulatoryShiftsType>();
		String id = "";
		String officeId = UserUtils.getUser().getOffice().getId();
		if (StringUtils.isNotEmpty(aftJSON.getString("shiftsId"))) {
			id = aftJSON.getString("shiftsId");
		} else {
			id = String.valueOf(ambulatoryShiftsTypeService.getSeq());
		}
		for (int i = 0; i < 7; i++) {
			AmbulatoryShiftsType a = new AmbulatoryShiftsType();
			a.setShiftsId(id);
			a.setShiftsName(aftJSON.getString("shiftsName"));
			a.setOfficeId(officeId);
			a.setWeekCode("W" + (i + 1));
			list.add(a);
			list.get(i).setStartime(aftJSON.getString("startime" + (i + 1)));
			list.get(i).setEndtime(aftJSON.getString("endtime" + (i + 1)));
			list.get(i).setBindFlt(aftJSON.getString("bindFlt" + (i + 1)));
		}
		ambulatoryShiftsTypeService.doInsertAST(id, list);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "del")
	public String del(String id) {
		ambulatoryShiftsTypeService.delAST(id);
		return "success";
	}

	@RequestMapping(value = "fln")
	public String fln(Model model, String day, String sFltNo) {
		model.addAttribute("day", day);
		model.addAttribute("sFltNo", sFltNo);
		JSONArray fltNo = ambulatoryShiftsTypeService.getFltByDay(day);
		List<String> aFltNo = Arrays.asList(sFltNo.split(","));
		fltNo.removeAll(aFltNo);
		model.addAttribute("aFltNo", fltNo);
		return "prss/arrange/asFltForm";
	}

}
