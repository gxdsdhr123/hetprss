package com.neusoft.prss.rule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.util.ParamUtils;
import com.neusoft.prss.rule.dao.WaterFillRuleDao;
import com.neusoft.prss.rule.entity.Drools;
import com.neusoft.prss.rule.entity.RuleExtend;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleWaterFillVO;


@Service
public class WaterFillRuleService {
	private Logger logger=Logger.getLogger(WaterFillRuleService.class);
	
	@Autowired
	private WaterFillRuleDao waterFillRuleDao;

	/**
	 * 获取规则基本信息
	 * 
	 * @param ruleId
	 * @return
	 */
	public RuleInfo loadRuleInfo(String ruleId) {
		RuleInfo ruleInfo = waterFillRuleDao.loadRuleInfo(ruleId);
		return ruleInfo;
	}
	
	/**
	 * 获取机型数据
	 * 
	 * @param ruleId
	 * @return
	 */
	public List<Map<String, Object>> loadAtcactype() {
		List<Map<String, Object>> sysAtcactypeList = waterFillRuleDao.loadAtcactype();
		return sysAtcactypeList;
	}

	/**
	 * 获取航空公司数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> loadAirline() {
		List<Map<String, Object>> sysAirlineList = waterFillRuleDao.loadAirline();
		return sysAirlineList;
	}

	/**
	 * 获取关联数据ID
	 * 
	 * @param ruleId
	 * @return
	 */
	public List<Map<String, Object>> loadRuleDataValueRel(String ruleId) {
		return waterFillRuleDao.loadRuleDataValueRel(ruleId);
	}

	/**
	 * 获取回显附加值
	 * 
	 * @param ruleId
	 * @return
	 */
	public RuleExtend loadRuleExtend(String ruleId) {
		RuleExtend ruleExtend = new RuleExtend();
		// 关系数据值
		List<Map<String, Object>> ruleDataValueRelList = waterFillRuleDao.loadRuleDataValueRel(ruleId);
		for (Map<String, Object> ruleDataValueMap : ruleDataValueRelList) {
			String rdType = String.valueOf(ruleDataValueMap.get("DATA_TYPE"));
			String rdVal = String.valueOf(ruleDataValueMap.get("VAL"));
			if (Drools.TYPE_ACTTYPE.equals(rdType)) {// 机型
				ruleExtend.getActtypeList().add(rdVal.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", ""));
			} else if (Drools.TYPE_AIRLINE.equals(rdType)) {// 航空公司
				ruleExtend.getAirlineList().add(rdVal.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", ""));
			} else if (Drools.TYPE_INOUT.equals(rdType)) {// 进出港类型
                //ruleExtend.setInOutPort(rdVal);
                ruleExtend.getInOutPort().add(rdVal.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", ""));
            } 
		}
		return ruleExtend;
	}

	/**
	 * 保存/更新
	 * 
	 * @param schema
	 *            1-新建、2-更新
	 * @param formData
	 * @param airplaneType
	 * @param airlineType
	 * @param condition
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public JSONObject save(JSONObject formData) throws Exception {
		JSONObject msg = new JSONObject();
		boolean ifInsert = false; // 是-更新 否-新建
		List<Map<String, Object>> relDataList = new ArrayList<>(); // 关联数据存储
		StringBuffer drlBuffer = new StringBuffer();

		RuleInfo ruleInfo = new RuleInfo(); // 基本信息
		String id = formData.getString("id");
		if (StringUtils.isBlank(id)) {
			ifInsert = true;
		}
		String ruleName = formData.getString("ruleName");
		String ruleDesc = formData.getString("ruleDesc");
		Integer ruleIfManual = formData.getInteger("ruleIfManual");
		Integer ruleIfValid = formData.getInteger("ruleIfValid");
		String expression = formData.getString("expression");
		String colids = formData.getString("colids");
		String addedWater = formData.getString("addedWater");
		if (StringUtils.isBlank(colids)) {
			colids = "";
		}
		String drools = formData.getString("drools");
		String condition = formData.getString("condition");
		JSONArray airplaneTypeArr=formData.getJSONArray("airplaneType");
		JSONArray airlineTypeArr=formData.getJSONArray("airline");
		
		ruleInfo.setId(id);
		ruleInfo.setIfManual(ruleIfManual);
		ruleInfo.setIfValid(ruleIfValid);
		ruleInfo.setCreateUser(UserUtils.getUser().getId());
		ruleInfo.setUpdateUser(UserUtils.getUser().getId());
		ruleInfo.setName(ruleName);
		ruleInfo.setDescription(ruleDesc);
		ruleInfo.setText(condition);
		ruleInfo.setDrlStr(expression);
		ruleInfo.setRuleExt(drools);
		// 进出港，更改为多选
        //String ruleExtInOutPort = formData.getString("ruleExtInOutPort");
        JSONArray ruleExtInOutPort=formData.getJSONArray("ruleExtInOutPort");
        if (ruleExtInOutPort!=null &&!ruleExtInOutPort.isEmpty()) {
            drlBuffer.append(createInOutPortDrl(ruleExtInOutPort)).append(",");
            colids = Drools.SYS_INOUTPORT_ID + "," + colids;
        }
		// 机型
		if (airplaneTypeArr!=null&&!airplaneTypeArr.isEmpty()) {
			drlBuffer.append(createActtypeDrl(airplaneTypeArr)).append(",");
			colids = Drools.SYS_ACTTYPE_ID + "," + colids;
		}
		// 航空公司
		if (airlineTypeArr!=null&&!airlineTypeArr.isEmpty()) {
			drlBuffer.append(createairlineDrl(airlineTypeArr)).append(",");
			colids = Drools.SYS_AIRLINE_ID + "," + colids;
		}

		String drl = drlBuffer.toString();
		if(StringUtils.isNotBlank(drools)){
			drl = drl + drools;
		}else{
			drl = drl.substring(0, drl.length() - 1);
		}
		logger.info("生成的DRL：" + drl);
		ruleInfo.setColids(colids);
		ruleInfo.setRule(drl);

		if (ifInsert) {
			waterFillRuleDao.insertRuleInfo(ruleInfo);
			id = ruleInfo.getId();
			logger.info("获取新的规则ID：" + id);
		} else {
			waterFillRuleDao.updateRuleInfo(ruleInfo);
		}

		// 进出港
        if (ruleExtInOutPort!=null&&!ruleExtInOutPort.isEmpty()) {
            /*Map<String, Object> inOutPortMap = new HashMap<>();
            inOutPortMap.put("ruleId", id);
            inOutPortMap.put("dataType", Drools.TYPE_INOUT);
            inOutPortMap.put("val", ruleExtInOutPort);
            relDataList.add(inOutPortMap);*/
            Map<String, Object> inOutMap = null;
            for (int i = 0; i < ruleExtInOutPort.size(); i++) {
                inOutMap = new HashMap<>();
                String inoutCode = ruleExtInOutPort.getString(i).trim();
                inOutMap.put("ruleId", id);
                inOutMap.put("dataType", Drools.TYPE_INOUT);
                inOutMap.put("val", inoutCode);
                relDataList.add(inOutMap);
            }
        }
		// 机型
		if (airplaneTypeArr!=null &&!airplaneTypeArr.isEmpty()) {
			Map<String, Object> airplaneMap = null;
			for (int i = 0; i < airplaneTypeArr.size(); i++) {
				airplaneMap = new HashMap<>();
				String airplaneCode = airplaneTypeArr.getString(i).trim();
				airplaneMap.put("ruleId", id);
				airplaneMap.put("dataType", Drools.TYPE_ACTTYPE);
				airplaneMap.put("val", airplaneCode);
				relDataList.add(airplaneMap);
			}
		}
		// 航空公司
		if (airlineTypeArr!=null &&!airlineTypeArr.isEmpty()) {
			Map<String, Object> airlineMap = null;
			for (int i = 0; i < airlineTypeArr.size(); i++) {
				airlineMap = new HashMap<>();
				String airlineCode = airlineTypeArr.getString(i).trim();
				airlineMap.put("ruleId", id);
				airlineMap.put("dataType", Drools.TYPE_AIRLINE);
				airlineMap.put("val", airlineCode);
				relDataList.add(airlineMap);
			}
		}

		if (!ifInsert) { // 更新先删除再插入
			waterFillRuleDao.deleteRuleDatavalueRel(id);
		}
		waterFillRuleDao.insertRuleDatavalueRel(relDataList);
		RuleWaterFillVO waterFillVO = new RuleWaterFillVO();
		waterFillVO.setRuleId(id);
		waterFillVO.setAddedWater(addedWater);
		if(ifInsert){
	        waterFillRuleDao.insertWaterFill(waterFillVO);
		} else {
	        waterFillRuleDao.updateWaterFill(waterFillVO);
		}
		msg.put("code", 0);
		msg.put("msg", ifInsert ? "创建成功" : "更新成功");

		return msg;
	}

	private String createInOutPortDrl(JSONArray inoutArr) {
        //return Drools.ATTR_NAME + Drools.SYS_INOUTPORT_CODE + " == \"" + value + "\"";
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        int len = inoutArr.size();
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                sb.append(Drools.ATTR_NAME + Drools.SYS_INOUTPORT_CODE).append(" == \"").append(inoutArr.getString(i)).append("\"");
            } else {
                sb.append(Drools.ATTR_NAME + Drools.SYS_INOUTPORT_CODE).append(" == \"").append(inoutArr.getString(i)).append("\"").append(" || ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

	
	private String createairlineDrl(JSONArray airplaneTypeArray) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		int len = airplaneTypeArray.size();
		for (int i = 0; i < len; i++) {
			if (i == len - 1) {
				sb.append(Drools.ATTR_NAME + Drools.SYS_AIRLINE_CODE).append(" == \"").append(airplaneTypeArray.getString(i)).append("\"");
			} else {
				sb.append(Drools.ATTR_NAME + Drools.SYS_AIRLINE_CODE).append(" == \"").append(airplaneTypeArray.getString(i)).append("\"").append(" || ");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	private String createActtypeDrl(JSONArray airlineTypeArray) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		int len = airlineTypeArray.size();
		for (int i = 0; i < len; i++) {
			if (i == len - 1) {
				sb.append(Drools.ATTR_NAME + Drools.SYS_ACTTYPE_CODE).append(" == \"").append(airlineTypeArray.getString(i)).append("\"");
			} else {
				sb.append(Drools.ATTR_NAME + Drools.SYS_ACTTYPE_CODE).append(" == \"").append(airlineTypeArray.getString(i)).append("\"").append(" || ");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 删除规则
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(String ruleId) throws Exception {
		waterFillRuleDao.deleteRuleInfo(ruleId); //删除规则详细
		waterFillRuleDao.deleteRuleDatavalueRel(ruleId); //删除关联数据
		waterFillRuleDao.deleteWaterFillInfo(ruleId);//删除加水量信息
	}

	/**
	 * -------------------------------------------------------------------------
	 * ----------------------------------- 根据解析成drl
	 * 
	 * @param targetStr
	 * @return
	 */
	public String createDrl(String targetStr) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(targetStr)) {
			String[] parts = targetStr.split("\\|");
			for (int i = 0; i < parts.length; i++) {
				String analysisPart = parts[i].trim();
				if (StringUtils.isNotBlank(analysisPart)) {
					if (analysisPart.startsWith(Drools.ATTR)) { // 属性
						sb.append(getAttr(analysisPart));
					} else if (analysisPart.startsWith(Drools.VAL)) { // 值
						sb.append(getVal(analysisPart));
					} else if (analysisPart.startsWith(Drools.JOIN)) { // 连接符
						sb.append(getJoin(analysisPart));
					} else if (analysisPart.startsWith(Drools.OPT)) { // 运算符
						sb.append(getOpt(analysisPart));
					}
				}
			}
		}
		
		String res="";
		if(!"".equals(sb.toString())){
			res="(" + sb.toString() + ")";
		}
		return res;
	}

	private String getAttr(String attrStr) throws Exception {
		Pattern pattern = Pattern.compile(ParamUtils.REG);
		Matcher matcher = pattern.matcher(attrStr);
		String split = String.valueOf(ParamUtils.SPLIT2);
		while (matcher.find()) {
			String group = matcher.group();
			String replace = group.replace("-", split);
			attrStr = attrStr.replaceAll("(" + group + ")", replace);
			
		}
		String[] parts = attrStr.split("-");
		if (parts.length != 3) {
			throw new Exception("标识为" + Drools.ATTR + "前缀数据错误，参考形式：attr_属性ID_属性名(英文)");
		}
		//String attrId = parts[1];
		String attrName = parts[2];
		attrName = attrName.replace(split, "-");
		return Drools.ATTR_NAME + attrName;
	}

	private String getVal(String valStr) throws Exception {
		Pattern pattern = Pattern.compile(ParamUtils.REG);
		Matcher matcher = pattern.matcher(valStr);
		String split = String.valueOf(ParamUtils.SPLIT2);
		while (matcher.find()) {
			String group = matcher.group();
			String replace = group.replace("-", split);
			valStr = valStr.replaceAll("(" + group + ")", replace);
			
		}
		String[] parts = valStr.split("-");
		if (parts.length != 3) {
			throw new Exception("标识为" + Drools.VAL + "数据错误，参考形式：val_int_100");
		}
		String value = parts[2];
		if (StringUtils.isBlank(value)) {
			throw new Exception("标识为" + Drools.VAL + "数据错误，数据值为空");
		}
		value = value.replace(split, "-");
		if (Drools.VAL_INT.equals(parts[1])) {
			return value;
		} else if (Drools.VAL_STRING.equals(parts[1])) {
			value = value.replace("'", "");
			return "\"" + value + "\"";
		} else {
			throw new Exception("标识为" + Drools.VAL + "数据错误，数据类型超出计算范围");
		}
	}

	private String getJoin(String joinStr) throws Exception {
		String[] parts = joinStr.split("-");
		if (parts.length != 2) {
			throw new Exception("标识为" + Drools.JOIN + "数据错误，参考形式：join_rb");
		}
		switch (parts[1]) {
		case Drools.JOIN_LB:
			return "(";
		case Drools.JOIN_RB:
			return ")";
		case Drools.JOIN_AND:
			return " && ";
		case Drools.JOIN_OR:
			return " || ";
		default:
			throw new Exception("标识为" + Drools.JOIN + "数据错误，连接符类型超出计算范围");
		}
	}

	private String getOpt(String optStr) throws Exception {
		String[] parts = optStr.split("-");
		if (parts.length != 2) {
			throw new Exception("标识为" + Drools.OPT + "数据错误，参考形式：opt_mul");
		}
		switch (parts[1]) {
		case Drools.OPT_ADD:
			return " + ";
		case Drools.OPT_SUB:
			return " - ";
		case Drools.OPT_MUL:
			return " * ";
		case Drools.OPT_DIV:
			return " / ";
		case Drools.OPT_GT:
			return " > ";
		case Drools.OPT_GTE:
			return " >= ";
		case Drools.OPT_LT:
			return " < ";
		case Drools.OPT_LTE:
			return " <= ";
		case Drools.OPT_EQ:
			return " == ";
		case Drools.OPT_UNEQ:
			return " != ";
		case Drools.OPT_MATCH:
			return " match ";
		case Drools.OPT_LMATCH:
			return " match ";
		case Drools.OPT_RMATCH:
			return " match ";
		case Drools.OPT_CONTAIN:
			return " conatin ";
		default:
			throw new Exception("标识为" + Drools.OPT + "数据错误，计算符类型超出计算范围");
		}
	}
	
	//----------------列表--------------------
	public Map<String,Object> getRuleList(Map<String, Object> param) {
        //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= waterFillRuleDao.getRuleListCount(param);
        List<Map<String, Object>> rows = waterFillRuleDao.getRuleList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

    public RuleWaterFillVO getWaterFill(String ruleId) {
        return waterFillRuleDao.getWaterFill(ruleId);
    }

    public List<Map<String,String>> loadInOutPortType() {
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "A0");
        map.put("desc", "单进");
        result.add(map);
        map = new HashMap<String, String>();
        map.put("code", "D0");
        map.put("desc", "单出");
        result.add(map);
        map = new HashMap<String, String>();
        map.put("code", "A1");
        map.put("desc", "进出-进");
        result.add(map);
        map = new HashMap<String, String>();
        map.put("code", "D1");
        map.put("desc", "进出-出");
        result.add(map);
        return result;
    }
}
