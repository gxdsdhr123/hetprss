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
import com.neusoft.prss.common.util.ParamUtils;
import com.neusoft.prss.rule.dao.DelayTimeRuleDao;
import com.neusoft.prss.rule.entity.Drools;
import com.neusoft.prss.rule.service.DelayTimeRuleService;


@Service
public class DelayTimeRuleService {
	private Logger logger=Logger.getLogger(DelayTimeRuleService.class);
	
	@Autowired
	private DelayTimeRuleDao delayTimeRuleDao;

	
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
		JSONObject delayTimeInfo = new JSONObject(); // 基本信息
		String id = formData.getString("id");
		if (StringUtils.isBlank(id)) {
			ifInsert = true;
		}
		String enName = formData.getString("enName");
		String cnName = formData.getString("cnName");
		Integer ruleIfValid = formData.getInteger("ruleIfValid");
		delayTimeInfo.put("creator", formData.getString("creator"));
		delayTimeInfo.put("en_name", enName);
		delayTimeInfo.put("cn_name", cnName);
		delayTimeInfo.put("if_valid", ruleIfValid);
		delayTimeInfo.put("id", id);

		if (ifInsert) {
			delayTimeRuleDao.insertDelayTimeInfo(delayTimeInfo);
			id = delayTimeInfo.getString("id");
		} else {
			delayTimeRuleDao.updateDelayTimeInfo(delayTimeInfo);
	        delayTimeRuleDao.deleteRuleInfo(id); //删除规则详细
	        delayTimeRuleDao.deleteDelayTimeDetail(id);//删除详情表
		}
		delayTimeRuleDao.updateFdPlus(delayTimeInfo);
		JSONArray data = formData.getJSONArray("tableData");
		JSONArray varList = this.getVarList();
		for(int i=0;i<data.size();i++) {
		    JSONObject detail = data.getJSONObject(i);
		    detail.put("delay_time_id", id);
		    detail.put("creator", formData.getString("creator"));
	        StringBuffer drlBuffer = new StringBuffer();
	        String drools = detail.getString("extend_drools");
	        String colids = detail.getString("extend_colids");
	        String ids = colids;
	        String acttypeCode = detail.getString("acttypeCode");
	        String ruleExtInOutPort = detail.getString("ruleExtInOutPort");
	        String alnCode = detail.getString("alnCode");
	        // 进出港，更改为多选
	        //String ruleExtInOutPort = formData.getString("ruleExtInOutPort");
	        if (StringUtils.isNotBlank(ruleExtInOutPort)) {
	            drlBuffer.append(createInOutPortDrl(ruleExtInOutPort)).append(",");
	            ids = Drools.SYS_INOUTPORT_ID + "," + ids;
	        }
	        
	        // 机型
	        if (StringUtils.isNotBlank(acttypeCode)) {
	            String string = createActtypeCodeDrl(acttypeCode);
	            if(!StringUtils.isBlank(string)) {
	                drlBuffer.append(string).append(",");
	                ids = Drools.SYS_ACTTYPE_ID + "," + ids;
	            }
	        }
	        // 航空公司
	        if (StringUtils.isNotBlank(alnCode)) {
	            String string = createAlnCodeDrl(alnCode);
	            if(!StringUtils.isBlank(string)) {
	                drlBuffer.append(string).append(",");
	                ids = Drools.SYS_AIRLINE_ID + "," + ids;
	            }
	        }
	        
	        String drl = drlBuffer.toString();
	        if(StringUtils.isNotBlank(drools)){
	            drl = drl + drools;
	        }else{
	            drl = drl.substring(0, drl.length() - 1);
	        }
	        detail.put("extend_drools", drl);
	        detail.put("extend_colids", ids);
		    detail.put("type", "extend");
		    delayTimeRuleDao.insertRuleInfo(detail);
		    String extendRuleId = detail.getString("id");
		    detail.put("extend_rule_id", extendRuleId);
            detail.put("extend_drools", drools);
		    detail.put("extend_colids", colids);
		    detail.put("time_text", this.getEnName(varList, detail.getString("time_condition")));
		    delayTimeRuleDao.insertDelayTimeDetail(detail);
		}
		msg.put("code", 0);
		msg.put("msg", ifInsert ? "创建成功" : "更新成功");

		return msg;
	}
	
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
	private String createInOutPortDrl(String inout) {
        //return Drools.ATTR_NAME + Drools.SYS_INOUTPORT_CODE + " == \"" + value + "\"";
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        String[] inoutArr = inout.split(","); 
        int len = inoutArr.length;
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                sb.append(Drools.ATTR_NAME + Drools.SYS_INOUTPORT_CODE).append(" == \"").append(inoutArr[i]).append("\"");
            } else {
                sb.append(Drools.ATTR_NAME + Drools.SYS_INOUTPORT_CODE).append(" == \"").append(inoutArr[i]).append("\"").append(" || ");
            }
        }
        sb.append(")");
        return sb.toString();
    }


    private String createActtypeCodeDrl(String acttypeCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        String[] acttypeCodeArray = acttypeCode.split(",");
        int len = acttypeCodeArray.length;
        for (int i = 0; i < len; i++) {
            if(StringUtils.isBlank(acttypeCodeArray[i]))
                continue;
            if (i == len - 1) {
                sb.append(Drools.ATTR_NAME + Drools.SYS_FLIGHTNUMBER_CODE).append(" == \"").append(acttypeCodeArray[i]).append("\"");
            } else {
                sb.append(Drools.ATTR_NAME + Drools.SYS_FLIGHTNUMBER_CODE).append(" == \"").append(acttypeCodeArray[i]).append("\"").append(" || ");
            }
        }
        sb.append(")");
        return "()".equals(sb.toString())?"":sb.toString();
    }

    private String createAlnCodeDrl(String alnCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        String[] alnCodeArray = alnCode.split(",");
        int len = alnCodeArray.length;
        for (int i = 0; i < len; i++) {
            if(StringUtils.isBlank(alnCodeArray[i]))
                continue;
            if (i == len - 1) {
                sb.append(Drools.ATTR_NAME + Drools.SYS_AIRCRAFT_NUMBER).append(" == \"").append(alnCodeArray[i]).append("\"");
            } else {
                sb.append(Drools.ATTR_NAME + Drools.SYS_AIRCRAFT_NUMBER).append(" == \"").append(alnCodeArray[i]).append("\"").append(" || ");
            }
        }
        sb.append(")");
        return "()".equals(sb.toString())?"":sb.toString();
    }
	
	/**
	 * 删除规则
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(String ruleId) throws Exception {
		delayTimeRuleDao.deleteRuleInfo(ruleId); //删除规则详细
        delayTimeRuleDao.deleteFdPlus(ruleId);//删除航班扩展列信息
		delayTimeRuleDao.deleteDelayTimeInfo(ruleId);//删除主表
		delayTimeRuleDao.deleteDelayTimeDetail(ruleId);//删除详情表
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
        int total= delayTimeRuleDao.getRuleListCount(param);
        List<Map<String, Object>> rows = delayTimeRuleDao.getRuleList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

    public JSONObject getDelayTimeInfo(String ruleId) {
        return delayTimeRuleDao.getDelayTimeInfo(ruleId);
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

    public JSONArray loadActtypeCode(Map<String,Object> param) {
        return delayTimeRuleDao.loadActtypeCode(param);
    }

    public JSONArray loadAlnCode(Map<String,Object> param) {
        return delayTimeRuleDao.loadAlnCode(param);
    }

    public List<Map<String,Object>> getDetailList(Map<String,Object> param) {
        List<Map<String,Object>> list = delayTimeRuleDao.getDetailList(param);
        for(int i=0;i<list.size();i++) {
            Map<String,Object> detail = list.get(i);
            String inOut = String.valueOf(detail.get("ruleExtInOutPort"));
            if(StringUtils.isNotBlank(inOut)) {
                inOut = inOut.replace("A0", "单进");
                inOut = inOut.replace("A1", "进出-进");
                inOut = inOut.replace("D0", "单出");
                inOut = inOut.replace("D1", "进出-出");
                detail.put("ruleExtInOutPortText", inOut);
            }
            String alnCode = String.valueOf(detail.get("in_out"));
            if(StringUtils.isNotBlank(alnCode)) {
                
            }
            
            String colids = String.valueOf(detail.get("extend_colids"));
            //将扩展条件ID去掉
            if(detail.containsKey("ruleExtInOutPort")){
                colids=colids.replace(Drools.SYS_INOUTPORT_ID+",", "");
            }
            if(detail.containsKey("acttypeCode")){
                colids=colids.replace(Drools.SYS_ACTTYPE_ID+",", "");
            }
            if(detail.containsKey("alnCode")){
                colids=colids.replace(Drools.SYS_AIRLINE_ID+",", "");
            }
            detail.put("extend_colids", colids);
        }
        return list;
    }

    public boolean filterInfo(JSONObject formData) {
        int num = delayTimeRuleDao.filterInfo(formData);
        return num>0?false:true;
    }

    public JSONArray getVarList(){
        return delayTimeRuleDao.getVarList();
    }

    public String getColumn(HashMap<String,String> data) {
        String flag = data.get("schema");
        String text = data.get("text");
        String varcols = data.get("colids");
        if("RULE".equals(flag)){
            HashMap<String,String> map = ParamUtils.map;
            String expression = data.get("expression");
            String html = "";
            if(expression != null && !"".equals(expression)){
                String[] conditionArr = expression.split("\\|");
                for(int i=0;i<conditionArr.length;i++){
                    String conditionStr = conditionArr[i];
                    if(conditionStr.split("-").length<2)
                        continue;
                    String[] conArr = conditionStr.split("-");
                    String type = conArr[0];
                    String con_flag = conArr[1];
                    String tag_text = "";
                    switch (type) {
                        case "attr":
                            if(con_flag == null || "".equals(con_flag))
                                continue;
                            JSONObject col = delayTimeRuleDao.getColumn(con_flag);
                            if(col == null)
                                continue;
                            String colDes = col.getString("COL_DESC");//字段别名
                            String col_name = col.getString("COL_ALIAS").toLowerCase();//字段名
                            String id = col.getString("ID");
                            tag_text = colDes;
                            html += "<div class='div_class select' data-id='"+id+"' data-en='"+col_name+"'>["+ 
                                    colDes + "]</div>";
                            break;
                        case "join":
                            tag_text = map.get(con_flag);
                            html += "<span class='span_class select' data-flag='"+
                                    con_flag+"' data-type='"+type+"'>"+ tag_text +"</span>";
                            break;
                        case "val":
                            if(conArr.length>2)
                                tag_text = conArr[2];
                            html += "<span class='span_class select' data-flag='"+
                                    con_flag+"' data-type='"+type+"'>"+ tag_text +"</span>";
                            break;
                        case "opt":
                            tag_text = map.get(con_flag);
                            html += "<span class='span_class select' data-flag='"+
                                    con_flag+"' data-type='"+type+"'>"+ tag_text +"</span>";
                            break;
                    }
                }
            }
            text = html;
        }
        text = text.replace(ParamUtils.LEFT + "" , "[").replace(ParamUtils.RIGHT + "", "]").replace("？", " ");
        return text;
    }
}
