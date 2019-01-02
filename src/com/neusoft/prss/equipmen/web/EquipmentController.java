package com.neusoft.prss.equipmen.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.equipmen.service.EquipmenService;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 资源管理的controller
 * @author douxf
 * @version 2017-12-05
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/manage/equipment")
public class EquipmentController {
	@Autowired
	EquipmenService equipmenService;
	
	@RequestMapping(value = "skip")
	public String view(Model model,Model modelI){
		equipmentFilterSkip(model);
		List<Map<String,Object>> data = equipmenService.createType();
		modelI.addAttribute("data", data);
		return "prss/equipment/equipment";
	}
	
	
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public JSONArray treeData(){	
		JSONArray list = equipmenService.transform();
		return list;
	}
	
	@RequestMapping(value = "treeDataII")
	public String treeDataII(Model model){	
		List<Map<String, Object>> list = equipmenService.transformII();
		model.addAttribute("row",list);
		return "prss/message/equipmentII";
	}
	
	@RequestMapping(value = "list")
    @ResponseBody
	public Map<String, Object> getList(int pageSize, int pageNumber, String sortName, String sortOrder,String typeId,String typeName,String deviceStatus,String deptId,String deviceNo,String deviceModel,String carNumber){
		if("e2caa5db3b68".equals(typeId)){
			typeId = "";
			deptId = "";
		}
		Map<String, Object> param = new HashMap<String, Object>();
		int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("sortName", sortName);
        param.put("sortOrder", sortOrder);
        param.put("mtype", typeId);
        param.put("typeName", typeName);
        param.put("typeName", typeName);
        param.put("deviceStatus", deviceStatus);
        param.put("deptId", deptId);
        
        param.put("deviceNo", deviceNo);
        param.put("deviceModel", deviceModel);
        param.put("carNumber", carNumber);
		Map<String, Object> result = equipmenService.transformIV(param);
		return result;
	}
	
	@RequestMapping(value = "create")
    @ResponseBody
	public int getCreate(String typeName,String deviceNo,String deviceModel,String carNumber,String deviceStatus,String seatingNum, String remark,String typeId,String innerNumber){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeName", typeName);
		param.put("deviceNo", deviceNo);
		param.put("deviceModel", deviceModel);
		param.put("carNumber", carNumber);
		param.put("deviceStatus", deviceStatus);
		param.put("seatingNum", seatingNum);
		param.put("remark", remark);
		param.put("creater",UserUtils.getUser().getId());
		param.put("typeId",typeId);
		param.put("innerNumber",innerNumber);
		int num = equipmenService.saveJmDevice(param);
		if(num==1){
			num = Integer.parseInt(param.get("typeId").toString());
		}else{
			num = 0;
		}
		
		return num;
	}
	
	@RequestMapping(value = "revamp")
    @ResponseBody
	public int getRevamp(String id,String typeName,String deviceNo,String deviceModel,String carNumber,String deviceStatus,String seatingNum, String remark,String innerNumber){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("typeName", typeName);
		param.put("deviceNo", deviceNo);
		param.put("deviceModel", deviceModel);
		param.put("carNumber", carNumber);
		param.put("deviceStatus", deviceStatus);
		param.put("seatingNum", seatingNum);
		param.put("remark", remark);
		param.put("innerNumber", innerNumber);
		int result = equipmenService.updateRevamp(param);
		return result;
	}
	
	
	@RequestMapping(value = "DelRevamp")
    @ResponseBody
	public int getDelRevamp(String [] id){
		int result = equipmenService.delRevamp(id);
		return result;
	}
	
	
	
	@RequestMapping(value = "newNode")
    @ResponseBody
	public int newNote(String name,String pid,String reskind){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", name);
		map.put("pid", pid);
		map.put("reskind", reskind);
		int result = equipmenService.newSave(map);
		return result;
	}
	
	@RequestMapping(value = "renameNode")
    @ResponseBody
	public int renameNode(String typeId,String typeName,String reskind){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("typeName", typeName);
		map.put("typeId", typeId);
		map.put("reskind", reskind);
		int result = equipmenService.newUpe(map);
		return result;
	}
	
	@RequestMapping(value = "renameDept")
    @ResponseBody
	public int renameDept(String id,String name){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("name", name);
		int result = equipmenService.updateZtreeDept(map);
		return result;
	}
	
	
	@RequestMapping(value = "RemoveNode")
    @ResponseBody
	public int RemoveNode(String typeId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("typeId", typeId);
		int result = equipmenService.newDel(map);
		return result; 
	}
	
	
	@RequestMapping(value = "equipmentFilterSkip")
	public String equipmentFilterSkip(Model model){
//		List<Map<String,Object>> dept = equipmenService.getselDeot();
		List<Map<String,Object>> result = equipmenService.equipmentFilterTypeName();
		List<Map<String,Object>> resultI = equipmenService.equipmentFilterTypeNameI();
		List<Map<String,Object>> resultII = equipmenService.equipmentFilterTypeNameII();
//		model.addAttribute("dept",dept);
		model.addAttribute("result",result);
		model.addAttribute("resultI",resultI);
		model.addAttribute("resultII",resultII);
		return "prss/equipment/equipmentFilter";
	}
	
	@RequestMapping(value = "revampVal")
	public String revampVal(Model model,String id){
		Map<String,Object> data = equipmenService.compileFind(id);
		model.addAttribute("device", data);
		return "prss/equipment/equipmentRevamp";
	}
	
	@RequestMapping(value = "editName")
	public String editName(Model model,String id){
		List<Map<String,Object>> data = equipmenService.createType();
		Map<String,Object> result =  equipmenService.findEdit(id);
		model.addAttribute("result", result);
		model.addAttribute("data", data);
		return "prss/equipment/equipmentZtreeRevamp";
	}
}
