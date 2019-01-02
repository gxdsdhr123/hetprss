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
import com.neusoft.prss.rule.dao.TaskAllotRuleDao;
import com.neusoft.prss.rule.entity.Drools;
import com.neusoft.prss.rule.entity.RuleExtend;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleRelProcVO;
import com.neusoft.prss.rule.entity.TimeParam;


@Service
public class TaskAllotRuleService {
	private Logger logger=Logger.getLogger(TaskAllotRuleService.class);
	
	@Autowired
	private TaskAllotRuleDao taskAllotRuleDao;

	/**
	 * 获取规则基本信息
	 * 
	 * @param ruleId
	 * @return
	 */
	public RuleInfo loadRuleInfo(String ruleId) {
		RuleInfo ruleInfo = taskAllotRuleDao.loadRuleInfo(ruleId);
		return ruleInfo;
	}
	
	/**
	 * 获取机型数据
	 * 
	 * @param ruleId
	 * @return
	 */
	public List<Map<String, Object>> loadAtcactype() {
		List<Map<String, Object>> sysAtcactypeList = taskAllotRuleDao.loadAtcactype();
		return sysAtcactypeList;
	}

	/**
	 * 获取航空公司数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> loadAirline() {
		List<Map<String, Object>> sysAirlineList = taskAllotRuleDao.loadAirline();
		return sysAirlineList;
	}

	/**
	 * 获取关联数据ID
	 * 
	 * @param ruleId
	 * @return
	 */
	public List<Map<String, Object>> loadRuleDataValueRel(String ruleId) {
		return taskAllotRuleDao.loadRuleDataValueRel(ruleId);
	}

	/**
	 * 获取进出港类型
	 * 
	 * @return
	 */
	public List<Map<String, String>> loadInOutPortType() {
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

	/**
	 * 获取机位类型
	 * 
	 * @return
	 */
	public List<Map<String, String>> loadApronType() {
		List<Map<String, String>> result = new ArrayList<>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "Y");
		map.put("desc", "远");
		result.add(map);
		map = new HashMap<String, String>();
		map.put("code", "N");
		map.put("desc", "近");
		result.add(map);
		map = new HashMap<String, String>();
        map.put("code", ParamUtils.Null);
        map.put("desc", "空");
        result.add(map);
		return result;
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
		List<Map<String, Object>> ruleDataValueRelList = taskAllotRuleDao.loadRuleDataValueRel(ruleId);
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
			} else if (Drools.TYPE_APRON.equals(rdType)) {// 机位
				//ruleExtend.setApronType(rdVal);
				ruleExtend.getApronType().add(rdVal.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", ""));
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
		if (StringUtils.isBlank(colids)) {
			colids = "";
		}
		String drools = formData.getString("drools");
		String condition = formData.getString("condition");
		//String airplaneType = formData.getString("airplaneType"); // 机型
		//String airlineType = formData.getString("airline"); // 航空公司
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
		// 机位,更改为多选
		//String ruleExtApronType = formData.getString("ruleExtApronType");
		JSONArray ruleExtApronType=formData.getJSONArray("ruleExtApronType");
		if (ruleExtApronType!=null &&!ruleExtApronType.isEmpty()) {
			drlBuffer.append(createApronDrl(ruleExtApronType)).append(",");
			colids = Drools.SYS_ACTSTANDKIND_ID + "," + colids;
		}
		// 机型
		if (airplaneTypeArr!=null&&!airplaneTypeArr.isEmpty()) {
			//String[] airplaneTypeArray = airplaneType.split(",");
			drlBuffer.append(createActtypeDrl(airplaneTypeArr)).append(",");
			colids = Drools.SYS_ACTTYPE_ID + "," + colids;
		}
		// 航空公司
		if (airlineTypeArr!=null&&!airlineTypeArr.isEmpty()) {
			//String[] airlineTypeArray = airlineType.split(",");
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
			taskAllotRuleDao.insertRuleInfo(ruleInfo);
			id = ruleInfo.getId();
			logger.info("获取新的规则ID：" + id);
		} else {
			taskAllotRuleDao.updateRuleInfo(ruleInfo);
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
		// 机位
		if (ruleExtApronType!=null &&!ruleExtApronType.isEmpty()) {
			//Map<String, Object> apronTypeMap = new HashMap<>();
			/*apronTypeMap.put("ruleId", id);
			apronTypeMap.put("dataType", Drools.TYPE_APRON);
			apronTypeMap.put("val", ruleExtApronType);
			relDataList.add(apronTypeMap);*/
			Map<String, Object> apronTypeMap = null;
			for (int i = 0; i < ruleExtApronType.size(); i++) {
				apronTypeMap = new HashMap<>();
				String apronCode = ruleExtApronType.getString(i).trim();
				apronTypeMap.put("ruleId", id);
				apronTypeMap.put("dataType", Drools.TYPE_APRON);
				apronTypeMap.put("val", apronCode);
				relDataList.add(apronTypeMap);
			}
		}
		// 机型
		if (airplaneTypeArr!=null &&!airplaneTypeArr.isEmpty()) {
			//String[] airplaneTypeArray = airplaneType.split(",");
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
			//String[] airlineTypeArray = airlineType.split(",");
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
			taskAllotRuleDao.deleteRuleDatavalueRel(id);
		}
		taskAllotRuleDao.insertRuleDatavalueRel(relDataList);
		/**规则流程关系数据处理*/
		String deleteRuleProc=formData.getString("deleteRuleProc");
		if(deleteRuleProc!=null&&!"".equals(deleteRuleProc)){
			taskAllotRuleDao.delRuleRelProcInfoBatch(deleteRuleProc);
			taskAllotRuleDao.delRuleRelNodeInfoBatch(deleteRuleProc);
		}
		JSONArray jobTypeArray=formData.getJSONArray("jobTypeArray");
		if(jobTypeArray!=null&&!jobTypeArray.isEmpty()){
			//baochl_20180718 add
			JSONArray varList = this.getVarList();
			for(int i=0;i<jobTypeArray.size();i++){
				JSONObject jobTypeObj=jobTypeArray.getJSONObject(i);
                JSONArray nodeArray=jobTypeObj.getJSONArray("nodeArray");
                if(nodeArray.size()<1)
                    continue;
				RuleRelProcVO vo=new RuleRelProcVO();
				vo.setProcId(jobTypeObj.getString("processId"));
				String taskTime=jobTypeObj.getString("taskTime");
				vo.setArrangeTmCN(taskTime);
				if(taskTime!=null&&!"".equals(taskTime)){
					vo.setArrangeTm(getEnName(varList,taskTime));
				}
				vo.setRuleId(id);
				vo.setSortNum(jobTypeObj.getString("jobSeq"));
				vo.setVariableId(jobTypeObj.getString("taskTimeVar"));
				String ruleProcId=jobTypeObj.getString("ruleProcId");//如果数据中为空再插入操作，不为空则更新
				if(ruleProcId==null||"".equals(ruleProcId)){
					taskAllotRuleDao.addRuleRelProcInfo(vo);
					ruleProcId=vo.getRuleProcId();//取回关系ID
				}else{
					vo.setRuleProcId(ruleProcId);
					taskAllotRuleDao.updateRuleRelProcInfo(vo);
				}
				
				/**规则与流程节点关系**/
				for(int j=0;j<nodeArray.size();j++){
					JSONObject nodeObj=nodeArray.getJSONObject(j);
					nodeObj.put("ruleProcId", ruleProcId);
					String nodeTimeCN=nodeObj.getString("nodeTime");
					nodeObj.put("nodeTimeCN", nodeTimeCN);
					if(nodeTimeCN!=null &&!"".equals(nodeTimeCN)){
						String nodeTimeEN=this.getEnName(varList,nodeTimeCN);
						nodeObj.put("nodeTime", nodeTimeEN);
					}
					String nodeRuleId=nodeObj.getString("nodeRuleId");
					if(nodeRuleId!=null&&!"".equals(nodeRuleId)){
						taskAllotRuleDao.updateRuleRelNodeInfo(nodeObj);
					}else{
						taskAllotRuleDao.addRuleRelNodeInfo(nodeObj);
					}
				}
				//taskAllotRuleDao.addRuleRelNodeInfo(nodeList);
			}
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

	private String createApronDrl(JSONArray apronArr) {
		//return Drools.ATTR_NAME + Drools.SYS_ACTSTANDKIND_CODE + " == \"" + value + "\"";
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		int len = apronArr.size();
		for (int i = 0; i < len; i++) {
		    String apron = apronArr.getString(i);
		    if(ParamUtils.Null.equals(apron)) {
		        apron = "";
		    }
			if (i == len - 1) {
				sb.append(Drools.ATTR_NAME + Drools.SYS_ACTSTANDKIND_CODE).append(" == \"").append(apron).append("\"");
			} else {
				sb.append(Drools.ATTR_NAME + Drools.SYS_ACTSTANDKIND_CODE).append(" == \"").append(apron).append("\"").append(" || ");
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
		taskAllotRuleDao.deleteRuleInfo(ruleId); //删除规则详细
		taskAllotRuleDao.deleteRuleDatavalueRel(ruleId); //删除关联数据
		taskAllotRuleDao.delRuleRelNodeInfo(ruleId);//规则流程
		taskAllotRuleDao.delRuleRelProcInfo(ruleId);//规则流程节点
	}
	/**
	 * 
	 *Discription:方法功能中文描述.
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月8日 gaojingdan [变更描述]
	 */
	public int checkRuleIfHaveTask(String ruleId){
		return taskAllotRuleDao.checkRuleIfHaveTask(ruleId);
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
			return " contains ";
		default:
			throw new Exception("标识为" + Drools.OPT + "数据错误，计算符类型超出计算范围");
		}
	}
	
	//----------------列表--------------------
	public Map<String,Object> getRuleList(Map<String, Object> param) {
        //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= taskAllotRuleDao.getRuleListCount(param);
        List<Map<String, Object>> rows = taskAllotRuleDao.getRuleList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }
	/**
	 *Discription:获取规则流程节点
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月4日 gaojingdan [变更描述]
	 */
	public List<JSONObject> getRuleProcNodeInfo(String ruleId){
		return taskAllotRuleDao.getRuleProcNodeInfo(ruleId);
	}
	/**
	 * 
	 *Discription:将中文表达式替换成英文表达式.
	 *@param cn
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年07月18日 baochl [从数据库获取]
	 */
	private String getEnName(JSONArray varList,String cn){
		String enName=cn;
		for(int i=0;i<varList.size();i++){
			JSONObject json=varList.getJSONObject(i);
			String name=json.getString("name");
			String en=json.getString("en");
			if(cn.indexOf("["+name+"]")>=0){
				enName=enName.replaceAll("\\["+name+"\\]", "["+en+"]");
			}
		}
		return enName;
	}
	/**
	 * 根据流程模板id获取绑定规则个数
	 * @param procId
	 * @return
	 */
	public int getRuleCountByProc(String procId){
		return taskAllotRuleDao.getRuleCountByProc(procId);
	}
	/**
	 * 
	 *Discription:获取任务分配时间所用参数列表
	 *@return
	 *@return:返回值意义	
	 *@author:gaojingdan
	 *@update:2018年1月12日 gaojingdan [变更描述]
	 */
	public JSONArray getVarList(){
		return taskAllotRuleDao.getVarList();
	}
}
