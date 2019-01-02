package com.neusoft.prss.scheduling.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.grid.service.GridColumnService;
import com.neusoft.prss.scheduling.dao.SchedulingListDao;

@Service
@Transactional(readOnly = true)
public class SchedulingListService extends BaseService {
	private static final String airportCode3 = Global.getConfig("airport_code3");
	
    @Autowired
    private SchedulingListDao listDao;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private GridColumnService gridService;

    public int getAirlineTotNum(Map<String,Object> param) {
        return listDao.getAirlineTotNum(param);
    }

    public JSONArray getAirports(Map<String,Object> param) {
        return listDao.getAirports(param);
    }

    public JSONArray getActTypes(Map<String,Object> param) {
        return listDao.getActTypes(param);
    }

    public JSONArray getAirlines(Map<String,Object> param) {
        return listDao.getAirlines(param);
    }

    public int getActTypeTotNum(Map<String,Object> param) {
        return listDao.getActTypeTotNum(param);
    }

    public int getAirportTotNum(Map<String,Object> param) {
        return listDao.getAirportTotNum(param);
    }

    @SuppressWarnings("unchecked")
    public JSONArray getDynamic(String switches,String flagBGS,String reskind,String schema,String suffix,String userId,
            Map<String,Object>... params) {
    	// 获取列 先获取user设置列 获取不到 获取默认列 两个都包括基本列和特定列
        JSONArray colArray = gridService.getColInfo(userId, schema);
        if (colArray == null || colArray.isEmpty()) {
            colArray = gridService.getDefColInfo(schema);
        }
        JSONArray result = getDynamic(switches, flagBGS, reskind, suffix,
				colArray, params);
        return result;
    }

	/**
	 * @param switches
	 * @param flagBGS
	 * @param reskind
	 * @param suffix
	 * @param colArray
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getDynamic(String switches, String flagBGS,
			String reskind, String suffix, JSONArray colArray,
			Map<String, Object>... params) {
		suffix = StringUtils.isBlank(suffix) ? "" : suffix;
        reskind = StringUtils.isBlank(reskind) ? "master" : reskind;
        JSONObject filterObj = listDao.getFiltConf(reskind);
        if(filterObj==null||filterObj.isEmpty()){//如果没保障类型获取航班动态的
        	filterObj = listDao.getFiltConf("master");
        }
        String order_by = StringUtils.isBlank(filterObj.getString("ORDER_BY")) ? "" : filterObj.getString("ORDER_BY");
        String airline_cond = StringUtils.isBlank(filterObj.getString("AIRLINE_COND")) ? ""
                : filterObj.getString("AIRLINE_COND");
        String timer_conf = StringUtils.isBlank(filterObj.getString("TIME_COND")) ? ""
                : filterObj.getString("TIME_COND");
        Map<String,Object> param = new HashMap<String,Object>();
        if (params.length > 0) {
            for (String key : params[0].keySet()) {
                if (!"".equals(params[0].get(key)) && params[0].get(key) != null) {
                    param.put(key, params[0].get(key));
                }
            }
        }
        JSONArray result = new JSONArray();
        List<Map<String,JSONObject>> otherList = new ArrayList<Map<String,JSONObject>>();
        
        // 将列按表分组
        Map<String,JSONArray> tabMap = new HashMap<String,JSONArray>();
        Map<String,Map<String,String>> dimMap = new HashMap<String,Map<String,String>>();
        for (int i = 0; i < colArray.size(); i++) {
            JSONObject col = colArray.getJSONObject(i);
            String tabId = col.getString("id");
            String tabName = col.getString("tabName");
            if ("fd_flt_info".equals(tabName)) {
                JSONArray colArr = tabMap.get("base") == null ? new JSONArray() : tabMap.get("base");
                colArr.add(col);
                tabMap.put("base", colArr);
            } else {
                JSONArray colArr = tabMap.get(tabId) == null ? new JSONArray() : tabMap.get(tabId);
                colArr.add(col);
                tabMap.put(tabId, colArr);
            }
            if (col.getString("dimKey") != null && col.getString("dimKey") != "") {
                dimMap.put(col.getString("alias"), cacheService.getMap(col.getString("dimKey")));
            }
        }
        // 拼接sql
        for (String key : tabMap.keySet()) {
            JSONArray columns = tabMap.get(key);
            if ("base".equals(key)) {
                String inTab = "";
                String outTab = "";
                for (int j = 0; j < columns.size(); j++) {
                    String colName = columns.getJSONObject(j).getString("colName");
                    String alias = columns.getJSONObject(j).getString("alias");
                    String tab_alias = columns.getJSONObject(j).getString("tab_alias");
                    String colFunc = "";
                    if (columns.getJSONObject(j).getString("colFunc") != null) {
                        colFunc = columns.getJSONObject(j).getString("colFunc");
                    }
                    if ("in_fd_flt_info".equalsIgnoreCase(tab_alias)) {
                        if (!"".equals(colFunc)) {
                            inTab = inTab + colFunc.replace("?", "in_tab." + colName) + " \"" + alias + "\", ";
                        } else {
                            inTab = inTab + "in_tab." + colName + " \"" + alias + "\", ";
                        }
                    } else if ("out_fd_flt_info".equalsIgnoreCase(tab_alias)) {
                        if (!"".equals(colFunc)) {
                            outTab = outTab + colFunc.replace("?", "out_tab." + colName) + " \"" + alias + "\", ";
                        } else {
                            outTab = outTab + "out_tab." + colName + " \"" + alias + "\", ";
                        }
                    }
                }
                param.put("detail", inTab + outTab.substring(0, outTab.length() > 0 ? outTab.length() - 1 : 0));
                param.put("suffix", suffix);
                param.put("flagBGS", flagBGS);
                param.put("reskind", reskind);
                param.put("switches", switches);
                param.put("order_by", order_by);
                param.put("timer_conf", timer_conf);
                param.put("airline_cond", airline_cond);
                result = listDao.getBaseData(param);
                // 配置列拼凑
            } else {
                String col = "";
                String tabName = columns.getJSONObject(0).getString("tabName");
                for (int j = 0; j < columns.size(); j++) {
                    String colName = columns.getJSONObject(j).getString("colName");
                    String alias = columns.getJSONObject(j).getString("alias");
                    String colFunc = "";
                    if (columns.getJSONObject(j).getString("colFunc") != null) {
                        colFunc = columns.getJSONObject(j).getString("colFunc");
                    }
                    if (!"".equals(colFunc)) {
                        col = col + colFunc.replace("?", "a." + colName) + " \"" + alias + "\"";
                    } else {
                        col = col + "a." + colName + " \"" + alias + "\"";
                    }
                    if (j != columns.size() - 1) {
                        col += ",";
                    }
                }
                Map<String,String> otherParam = new HashMap<String,String>();
                otherParam.put("col", col);
                otherParam.put("tabName", tabName + suffix);
                JSONArray res = listDao.getOtherData(otherParam);
                Map<String,JSONObject> map = new HashMap<String,JSONObject>();
                for (int i = 0; i < res.size(); i++) {
                    String fltid = res.getJSONObject(i).getString("fltid");

                    map.put(fltid, res.getJSONObject(i));
                }
                otherList.add(map);
            }
        }
        // 拼接其他列(摆渡车)
        for (int i = 0; i < result.size(); i++) {
            JSONObject res = result.getJSONObject(i);
            if (res == null) {
                result.remove(i);
                i--;
                continue;
            }
            // 基本列
            String inFltid = res.getString("in_fltid") != null ? res.getString("in_fltid") : "";
            String outFltid = res.getString("out_fltid") != null ? res.getString("out_fltid") : "";

            // 拼接其他列
            for (int j = 0; j < otherList.size(); j++) {
                Map<String,JSONObject> map = otherList.get(j);
                if (inFltid != "" && map.containsKey(inFltid)) {
                    JSONObject r = map.get(inFltid);
                    for (String key : r.keySet()) {
                        if (key.toLowerCase().startsWith("in_")) {
                            res.put(key, r.getString(key));
                        }else if(key.toLowerCase().startsWith("all_")){
                        	//针对部分进出港的统计项
                        	res.put(key, r.getString(key));
                        }
                    }
                }
                if (outFltid != "" && map.containsKey(outFltid)) {
                    JSONObject r = map.get(outFltid);
                    for (String key : r.keySet()) {
                        if (key.toLowerCase().startsWith("out_")) {
                            res.put(key, r.getString(key));
                        }else if(key.toLowerCase().startsWith("all_")){
                        	//针对部分进出港的统计项
                        	res.put(key, r.getString(key));
                        }
                    }
                }
            }
        }
        // 维度转换
        for (int i = 0; i < result.size(); i++) {
            JSONObject o = result.getJSONObject(i);
            for (String key : o.keySet()) {
                String val = o.getString(key);
                if (dimMap.get(key) != null && dimMap.get(key).get(val) != null) {
                    o.put(key, dimMap.get(key).get(val));
                }
            }
        }
		return result;
	}

    public JSONObject getSemaByOfficeId(String officeId) {
        return listDao.getSemaByOfficeId(officeId);
    }

    public void updateSema(JSONObject json) {
        JSONObject old = listDao.getSemaByOfficeId(json.getString("officeId"));
        boolean bulid = old.getString("TASK_FLAG").equals(json.getString("bulid"));
        boolean assign = old.getString("WORKER_FLAG").equals(json.getString("assign"));
        if (bulid && assign) {
            return;
        }
        String sql = "UPDATE jm_sema SET ";
        String str = "";
        if (!bulid) {
            sql += "task_flag = " + json.getString("bulid") + ", task_update_time = SYSDATE,";
            if (json.getString("bulid").equals("0")) {
                str += "开启自动生成任务，";
            } else {
                str += "关闭自动生成任务，";
            }
        }
        if (!assign) {
            sql += "worker_flag = " + json.getString("assign") + ", worker_update_time = SYSDATE,";
            if (json.getString("assign").equals("0")) {
                str += "开启自动分配任务，";
            } else {
                str += "关闭自动分配任务，";
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        str = str.substring(0, str.length() - 1);
        sql += " WHERE office_id = '" + json.getString("officeId") + "'";
        listDao.updateSema(sql);
        listDao.setSemaLog(json.getString("officeId"), str, UserUtils.getUser().getId());
    }

    public boolean isMix(String fltid) {
        String fltAttrCode = listDao.getAttrCode(fltid);
        if ("M".equals(fltAttrCode)) {
            return true;
        }
        return false;
    }

    public String getDGate(String fltid) {
        return listDao.getDGate(fltid);
    }

    public void updateDGate(String fileId,String dGate) {
        listDao.updateDGate(fileId, dGate);
    }

    
    public JSONArray getSheet2Title(String schema){
    	// 获取打印列
        JSONArray colArray = listDao.getPrintColInfo(schema);
    	return colArray;
    }
    
    @SuppressWarnings("unchecked")
    public JSONArray getPrintData(String switches,String flagBGS,String reskind,String schema,String suffix,
            Map<String,Object>... params) {
    	// 获取打印列
        JSONArray colArray = listDao.getPrintColInfo(schema);
        JSONArray result = null;
        if(params == null){
        	result = getDynamic(switches, flagBGS, reskind, suffix,
        			colArray);
        }else{
        	result = getDynamic(switches, flagBGS, reskind, suffix,
        			colArray, params);
        }
        return result;
    }
    
    /**
     * 获取装卸调用列表任务指派单航班信息
     */
    public JSONObject getFltInfo(Map<String,Object> paramMap) {
    	JSONObject dataObj = listDao.getFltInfo(paramMap);
        return dataObj;
    }
    
    /**
     * 获取装卸调用列表任务指派单进港货邮行、进港行李解析信息
     */
    public JSONArray getFltMailInfo(Map<String,Object> paramMap) {
    	JSONArray dataList = listDao.getFltMailInfo(paramMap);
    	//增加order排序,为前端删除数据行准备
    	for(int i=0;i<dataList.size();i++){
    		JSONObject dataObj = dataList.getJSONObject(i);
    		dataObj.put("order", i);
    	}
        return dataList;
    }
    
    /**
     * 获取装卸调用列表任务指派单进港货邮行、进港行李解析信息
     * @throws IOException 
     */
    public Map<String,Object> importFltMailInfo(Map<String,Object> paramMap) throws IOException {
    	//key:errInfo countSave
    	Map<String,Object> rsMap = new HashMap<String, Object>();
    	int countSave = 0;
    	String fltMail =(String)paramMap.get("fltMail");
    	String fltPackage =(String)paramMap.get("fltPackage");
    	//导入的数据集合
    	List<JSONObject> dataList = new ArrayList<JSONObject>();
    	//货邮行解析数据
    	Map<String,Object> fltMailMap = new HashMap<String, Object>();
    	//行李解析数据
    	Map<String,Object> fltPackageMap = new HashMap<String, Object>();
    	//航空公司缓存数据
    	JSONArray airlines = cacheService.getOpts("dim_airline", "icao_code", "airline_code");
    	Map<String,String> airlinesMap = new HashMap<String, String>();
		if(airlines!=null&&airlines.size()>0){
			for (int i = 0; i < airlines.size(); i++) {
				JSONObject obj = airlines.getJSONObject(i);
				airlinesMap.put(obj.getString("id"), obj.getString("text"));
			}
		}
		paramMap.put("airlinesMap", airlinesMap);
    	
    	//导入货邮行、行李解析数据
    	//解析货邮行信息
    	if(fltMail!=null&&!fltMail.equals("")){
    		fltMailMap = analysisFltMail(paramMap);
    	}
    	//解析行李信息
    	if(fltPackage!=null&&!fltPackage.equals("")){
    		fltPackageMap = analysisFltPackage(paramMap);
    	}
    	//关联进港航班对应货邮行、行李数据,根据是否过站、货邮行类型类型(B/BY/BT/T)关联货邮行、行李数据
    	List<JSONObject> fltMailList = (List<JSONObject>)fltMailMap.get("dataList");
    	List<JSONObject> fltPackageList = (List<JSONObject>)fltPackageMap.get("dataList");
    	//航班实际人数保存至fd_flt_passenger_t
    	List<JSONObject> fltPassengerList = (List<JSONObject>)fltPackageMap.get("passengerList");
    	String fltMailFailInfo = (String)fltMailMap.get("failInfo");
    	String fltPackageFailInfo = (String)fltPackageMap.get("failInfo");
    	if(fltMailList!=null&&fltMailList.size()>0&&fltPackageList!=null&&fltPackageList.size()>0){
    		//合并货邮行、行李数据
    		for(int i=0;i<fltMailList.size();i++){
    			JSONObject fltMailObj = fltMailList.get(i);
    			String mailPackageType = fltMailObj.getString("mailPackageType");
    			String aptType = fltMailObj.getString("aptType");
    			for(int j=0;j<fltPackageList.size();j++){
    				JSONObject fltPackageObj = fltPackageList.get(j);
        			String aptType1 = fltPackageObj.getString("aptType");
        			String packageWeight = fltPackageObj.getString("packageWeight");
        			String packageNumber = fltPackageObj.getString("packageNumber");
        			//货邮行行李信息并且都为本站或过站数据,进行合并
        			if(mailPackageType.startsWith("B")&&aptType.equals(aptType1)){
        				fltMailObj.put("packageWeight", packageWeight);
        				fltMailObj.put("packageNumber", packageNumber);
        				fltPackageList.remove(j);
        			}
    			}
    		}
    		dataList.addAll(fltMailList);
        	dataList.addAll(fltPackageList);
    	}else if(fltMailList!=null&&fltMailList.size()>0){
    		//只有货邮行数据,暂时不用单独导入
//    		dataList.addAll(fltMailList);
    	}else if(fltPackageList!=null&&fltPackageList.size()>0){
    		//只有行礼数据,暂时不用单独导入
//    		dataList.addAll(fltPackageList);
    	}
    	
    	//根据fltid删除对应解析数据,再保存解析数据
    	if(dataList!=null&&dataList.size()>0){
    		listDao.delFltMailInfo(paramMap);
    		countSave = listDao.importFltMailInfo(dataList);
    	}
    	
    	String failInfo = "";
    	if(fltMailFailInfo!=null&&!fltMailFailInfo.equals("")){
    		failInfo += fltMailFailInfo;
    	}
    	if(fltPackageFailInfo!=null&&!fltPackageFailInfo.equals("")){
    		failInfo += fltPackageFailInfo;
    	}
    	rsMap.put("failInfo", failInfo);
    	rsMap.put("countSave", countSave);
    	rsMap.put("fltPassengerList", fltPassengerList);
        return rsMap;
    }
    
    /**
     * 解析货邮行信息
     * @throws IOException 
     */
    private Map<String,Object> analysisFltMail(Map<String,Object> paramMap) throws IOException{
    	String inFltId = (String)paramMap.get("inFltId");
    	String outFltId = (String)paramMap.get("outFltId");
    	String inFltNum = (String)paramMap.get("inFltNum");
    	String inFltNum2 = (String)paramMap.get("inFltNum2");
    	String inFlightDate =(String)paramMap.get("inFlightDate");
    	String outFltNum = (String)paramMap.get("outFltNum");
    	String outFltNum2 = (String)paramMap.get("outFltNum2");
    	String outFlightDate =(String)paramMap.get("outFlightDate");
    	String fltMail =(String)paramMap.get("fltMail");
    	String controlUser = (String)paramMap.get("controlUser");
    	String supervisionUser = (String)paramMap.get("supervisionUser");
    	Map<String,String> airlinesMap = (Map<String,String>)paramMap.get("airlinesMap");
    	String tmpNo2 = airlinesMap.get(inFltNum.substring(0, 3));
    	if(tmpNo2==null)
    		tmpNo2 = "";
    	//三字码转的二字码航班号
    	String inFltNum2Convert = tmpNo2+inFltNum.substring(3, inFltNum.length());
    	
    	//key:errInfo dataList
    	Map<String,Object> rsMap = new HashMap<String, Object>();
    	
    	//合并货邮行相同货邮行类型数据C=货物，M=邮件，B/BY/BT/T=行李，E=航材
    	Map<String,JSONObject> tmpMap = new HashMap<String, JSONObject>();
    	
    	//解析结果数据
    	List<JSONObject> rsList = new ArrayList<JSONObject>();
    	//校验错误信息
    	String errInfoStr = "";
    	BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fltMail.getBytes(Charset.forName("utf-8"))), Charset.forName("utf-8")));
    	if(br!=null){
			//第一行可以解析进港航班号/日期（日月年）"LPAU:MF8028/09AUG18/CGO1  HRB  HET A/C B7826   Y184                 ETD 1525    "
			String[] tmpFirstRowArr = br.readLine().split(" ")[0].split("/");
			//二字码航班号
			String fltNo2 = tmpFirstRowArr[0].split(":")[1];
			//报文日期
			String date = tmpFirstRowArr[1];
			date = getFormatDate(date);
			
			if((inFltNum2.equals(fltNo2)||inFltNum2Convert.equals(fltNo2))&&inFlightDate.equals(date)){
				//2,3,4,5行无用信息
				for(int i=0;i<4;i++){
					br.readLine();
				}
				//解析货邮行数据
				String rowStr = "";
				while((rowStr = br.readLine()) != null){
					//目的站
					String arrivalApt = rowStr.substring(1, 4);
					if(arrivalApt==null||arrivalApt.equals("")||(arrivalApt!=null&&arrivalApt.replace(" ", "").length()<3))
						continue;
					//校验空行、无用数据行
					int countNotNull = 0;
					String[] tmpArr = rowStr.split(" ");
					for(int i=0;i<tmpArr.length;i++){
						if(StringUtils.isNotBlank(tmpArr[i])){
							countNotNull++;
						}
					}
					if(countNotNull<4)
						continue;
					
					//长度为55的行为货航邮数据行
					if(rowStr.length()>=55){
						JSONObject dataObj = new JSONObject();
				    	dataObj.put("inFltId", inFltId);
				    	dataObj.put("outFltId", outFltId);
						dataObj.put("inFltNum", inFltNum);
						dataObj.put("inFltNum2", inFltNum2);
						dataObj.put("inFltDate", inFlightDate);
						dataObj.put("outFltNum", outFltNum);
						dataObj.put("outFltNum2", outFltNum2);
						dataObj.put("outFltDate", outFlightDate);
						dataObj.put("controlUser", controlUser);
						dataObj.put("supervisionUser", supervisionUser);
						
						
						//当目的站为本场时,即为本站货邮行,否则为过站货邮行
						//0:本站 1:过站
						String aptType = "";
						if(arrivalApt.equals(airportCode3)){
							aptType = "0";
						}else{
							aptType = "1";
						}
						//实重
						String mailWeight = rowStr.substring(15, 20).replace(" ","").replace("_","");
						//货邮行类型 C=货物，M=邮件，B/BY/BT/T=行李，E=航材, X=压舱物
						String mailPackageType = rowStr.substring(21, 24).trim().replace("_","");
						//C、M、B、E
						if(mailPackageType.startsWith("C")){
							mailPackageType = "C";
						}else if(mailPackageType.startsWith("M")){
							mailPackageType = "M";
						}else if(mailPackageType.startsWith("B")||mailPackageType.startsWith("T")){
							mailPackageType = "BY";
						}else if(mailPackageType.startsWith("E")){
							mailPackageType = "E";
						}else if(mailPackageType.startsWith("X")){
							mailPackageType = "X";
						}
						//舱位
						String mailCabin = rowStr.substring(46, 49).replace(" ","").replace("_","");
						
						dataObj.put("mailPackageType", mailPackageType);
						dataObj.put("arrivalApt", arrivalApt);
						dataObj.put("aptType", aptType);
						dataObj.put("mailWeight", mailWeight);
						dataObj.put("mailCabin", mailCabin);
						dataObj.put("packageWeight", "");
						dataObj.put("packageNumber", "");
						dataObj.put("flag", "A");
						
						//合并货邮行行李相同数据 key:货邮行类型+目的站
						String key = mailPackageType+arrivalApt;
						if(mailPackageType.equals("BY")){
							if(tmpMap.containsKey(key)){
								JSONObject tmpDataObj = tmpMap.get(key);
								//计算实重
								int tmpMailWeight = tmpDataObj.getInteger("mailWeight");
								int tmpMailWeight1 = 0;
								if(mailWeight!=null&&!mailWeight.equals("")){
									tmpMailWeight1 = Integer.parseInt(mailWeight);
								}
								dataObj.put("mailWeight", tmpMailWeight+tmpMailWeight1);
								//计算舱位
								String tmpMailCabin = tmpDataObj.getString("mailCabin");
								if(!("/"+tmpMailCabin+"/").contains("/"+mailCabin+"/")){
									dataObj.put("mailCabin", tmpMailCabin+"/"+mailCabin);
								}
								tmpMap.put(key, dataObj);
							}else{
								tmpMap.put(key, dataObj);
							}
						}else{
							rsList.add(dataObj);
						}
					}
				}
			}else{
				if(!inFltNum2.equals(fltNo2)){
					errInfoStr+="进港航班号:"+inFltNum2+"("+inFltNum+"),进港货邮行报文航班号:"+fltNo2+"不匹配!";
				}
				if(!inFlightDate.equals(date)){
					errInfoStr+="进港航班日期:"+inFlightDate+",进港货邮行报文航班日期:"+date+"不匹配!";
				}
			}
		}
    	
    	for(String key : tmpMap.keySet()){
    		rsList.add(tmpMap.get(key));
    	}
    	
    	if(!errInfoStr.equals("")){
    		errInfoStr+="进港货邮行报文未导入。";
    	}
    	
    	rsMap.put("failInfo", errInfoStr);
    	rsMap.put("dataList", rsList);
    	return rsMap;
    }
    
    /**
     * 解析行李信息
     * @throws IOException 
     */
    private Map<String,Object> analysisFltPackage(Map<String,Object> paramMap) throws IOException{
    	String inFltId = (String)paramMap.get("inFltId");
    	String outFltId = (String)paramMap.get("outFltId");
    	String inFltNum = (String)paramMap.get("inFltNum");
    	String inFltNum2 = (String)paramMap.get("inFltNum2");
    	String inFlightDate =(String)paramMap.get("inFlightDate");
    	String outFltNum = (String)paramMap.get("outFltNum");
    	String outFltNum2 = (String)paramMap.get("outFltNum2");
    	String outFlightDate =(String)paramMap.get("outFlightDate");
    	String fltPackage =(String)paramMap.get("fltPackage");
    	String controlUser = (String)paramMap.get("controlUser");
    	String supervisionUser = (String)paramMap.get("supervisionUser");
    	Map<String,String> airlinesMap = (Map<String,String>)paramMap.get("airlinesMap");
    	String tmpNo2 = airlinesMap.get(inFltNum.substring(0, 3));
    	if(tmpNo2==null)
    		tmpNo2 = "";
    	//三字码转的二字码航班号
    	String inFltNum2Convert = tmpNo2+inFltNum.substring(3, inFltNum.length());
    	
    	//key:errInfo dataList
    	Map<String,Object> rsMap = new HashMap<String, Object>();
    	//合并行李过站、本站数据
    	Map<String,JSONObject> tmpMap = new HashMap<String, JSONObject>();
    	//解析结果数据
    	List<JSONObject> rsList = new ArrayList<JSONObject>();
    	//解析实际进港旅客数据
    	List<JSONObject> passengerList = new ArrayList<JSONObject>();
    	//校验错误信息
    	String errInfoStr = "";
    	//目的站三字码
    	String arrivalApt = "";
    	//航段起落场
    	String adPort = "";
    	BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fltPackage.getBytes(Charset.forName("utf-8"))), Charset.forName("utf-8")));
    	if(br!=null){
    		//第一行无用
    		br.readLine();
			//第二行可以解析进港航班号/日期（日月年）" SY: MU2891/09AUG18 LYG/0  CC1003/NAM                                           "
			String[] tmpSecondRowArr = br.readLine().split(" ")[2].split("/");
			//二字码航班号
			String fltNo2 = tmpSecondRowArr[0];
			//报文日期
			String date = tmpSecondRowArr[1];
			date = getFormatDate(date);
			
			if((inFltNum2.equals(fltNo2)||inFltNum2Convert.equals(fltNo2))&&inFlightDate.equals(date)){
				//3,4,5,6行无用信息
				for(int i=0;i<4;i++){
					br.readLine();
				}
				//解析行李数据
				String rowStr = "";
				while((rowStr = br.readLine()) != null){
					//目的站信息"*WUHHET R003/133   C003/133          B0076/000792 UB0001/000014                 "
					if(rowStr.length()<7)
						continue;
					
					String aptInfo = rowStr.substring(0, 7);
					if(aptInfo==null||aptInfo.equals("")||(aptInfo!=null&&(aptInfo.contains("TOTALS")||aptInfo.replace(" ", "").length()<6||aptInfo.replace(">", "").length()<6)))
						continue;
					
					String tmpAptInfo = aptInfo.replace("*", "");
					
					//校验空行、无用数据行
					int countNotNull = 0;
					String[] tmpArr = rowStr.split(" ");
					String[] tmpRowDataArr = new String[tmpArr.length];
					for(int i=0,j=0;i<tmpArr.length;i++){
						if(StringUtils.isNotBlank(tmpArr[i])){
							countNotNull++;
							tmpRowDataArr[j]=tmpArr[i];
							j++;
						}
					}
					if(countNotNull<4)
						continue;
					
					//长度为70的行为行李数据行
					if(rowStr.length()>=70){
						//本站行李 "*WUHHET R003/133   C003/133          B0076/000792 UB0001/000014                 "
						//过站行李 "TRANSIT R000/064   C000/063          B0022/000258                               "
						//总计不解析 "TOTALS* R000/106   C000/101          B0039/000429                               "
						
						//实际进港旅客人数信息
						JSONObject passengerDataObj = new JSONObject();
						passengerDataObj.put("inFltId", inFltId);
						passengerDataObj.put("outFltId", outFltId);
						
						//行李信息
						JSONObject dataObj = new JSONObject();
						dataObj.put("inFltId", inFltId);
				    	dataObj.put("outFltId", outFltId);
						dataObj.put("inFltNum", inFltNum);
						dataObj.put("inFltNum2", inFltNum2);
						dataObj.put("inFltDate", inFlightDate);
						dataObj.put("outFltNum", outFltNum);
						dataObj.put("outFltNum2", outFltNum2);
						dataObj.put("outFltDate", outFlightDate);
						dataObj.put("controlUser", controlUser);
						dataObj.put("supervisionUser", supervisionUser);
						
						//当目的站信息包含本场时,即为本站行李,否则为过站行李
						//0:本站 1:过站
						String aptType = "";
						if(aptInfo.contains(airportCode3)){
							aptType = "0";
							adPort = tmpAptInfo.substring(0, 3)+"-"+tmpAptInfo.substring(3, 6);
						}else{
							aptType = "1";
						}
						
						//这类数据无法获取目的站三字码"TRANSIT R000/064   C000/063          B0022/000258                               "
						if(aptInfo.equals("TRANSIT")){
							arrivalApt = "";
						}else{
							arrivalApt = tmpAptInfo.substring(3, 6);
						}
						
						//进港航班实际旅客人数 "C000/063","C000/000/073","C162" xxx/实际旅客人数
						int passengerNum = 0;
						if(tmpRowDataArr[2]!=null){
							String[] passengerInfoArr = tmpRowDataArr[2].split("/");
							String passengerNumStr = passengerInfoArr[passengerInfoArr.length-1];
							if(passengerNumStr!=null&&!passengerNumStr.equals("")){
								passengerNum = Integer.parseInt(passengerNumStr.replaceAll("[a-zA-Z]",""));
							}
						}
						passengerDataObj.put("aptType", aptType);
						passengerDataObj.put("passengerNum", passengerNum);
						passengerDataObj.put("updatorId", controlUser);
						passengerDataObj.put("adPort", adPort);
						passengerList.add(passengerDataObj);
						
						// B0076/000792 行李件数/行李重量
						String tmpPackageNumber = "";
						String tmpPackageWeigh = "";
						if(tmpRowDataArr[3]!=null){
							String[] packageInfoArr = tmpRowDataArr[3].split("/");
							if(packageInfoArr[0]!=null)
								tmpPackageNumber = packageInfoArr[0];
							if(packageInfoArr[1]!=null)
								tmpPackageWeigh = packageInfoArr[1];
						}
						//行李件数
						String packageNumber = "";
						//行李重量
						String packageWeight = "";
						//行李类型B/BY/BT/T
						String mailPackageType = "";
						if(tmpPackageNumber!=null&&!tmpPackageNumber.equals("")){
							packageNumber = Integer.parseInt(tmpPackageNumber.replaceAll("[a-zA-Z]",""))+"";
							mailPackageType = tmpPackageNumber.replaceAll("[0-9]","");
							if(mailPackageType.startsWith("B")||mailPackageType.startsWith("T")){
								mailPackageType = "BY";
							}
						}
						if(tmpPackageWeigh!=null&&!tmpPackageWeigh.equals("")){
							packageWeight = Integer.parseInt(tmpPackageWeigh.replaceAll("[a-zA-Z]",""))+"";
						}
						
						dataObj.put("mailPackageType", mailPackageType);
						dataObj.put("arrivalApt", arrivalApt);
						dataObj.put("aptType", aptType);
						dataObj.put("packageWeight", packageWeight);
						dataObj.put("packageNumber", packageNumber);
						dataObj.put("mailWeight", "");
						dataObj.put("mailCabin", "");
						dataObj.put("flag", "A");
						
						//合并行李相同数据 key:货邮行类型+目的站
						if(tmpMap.containsKey(aptType)){
							JSONObject tmpDataObj = tmpMap.get(aptType);
							//计算行李件数
							int tmpPackageNumber1 = tmpDataObj.getInteger("packageNumber");
							int tmpPackageNumber2 = 0;
							if(packageNumber!=null&&!packageNumber.equals("")){
								tmpPackageNumber2 = Integer.parseInt(packageNumber);
							}
							dataObj.put("packageNumber", tmpPackageNumber1+tmpPackageNumber2);
							//计算行李重量
							int tmpPackageWeight = tmpDataObj.getInteger("packageWeight");
							int tmpPackageWeight1 = 0;
							if(packageWeight!=null&&!packageWeight.equals("")){
								tmpPackageWeight1 = Integer.parseInt(packageWeight);
							}
							dataObj.put("packageWeight", tmpPackageWeight+tmpPackageWeight1);
							tmpMap.put(aptType, dataObj);
						}else{
							tmpMap.put(aptType, dataObj);
						}	
					}
				}
			}else{
				if(!inFltNum2.equals(fltNo2)){
					errInfoStr+="进港航班号:"+inFltNum2+"("+inFltNum+"),进港行李报文航班号:"+fltNo2+"不匹配!";
				}
				if(!inFlightDate.equals(date)){
					errInfoStr+="进港航班日期:"+inFlightDate+",进港行李报文航班日期:"+date+"不匹配!";
				}
			}
		}
    	
    	for(String key : tmpMap.keySet()){
    		rsList.add(tmpMap.get(key));
    	}
    	
    	if(!errInfoStr.equals("")){
    		errInfoStr+="进港行李报文未导入。";
    	}
    	
    	rsMap.put("failInfo", errInfoStr);
    	rsMap.put("dataList", rsList);
    	rsMap.put("passengerList", passengerList);
    	return rsMap;
    }
    
    /**
     * 删除进港货邮行、进港行李解析信息
     */
    public int delFltMailInfo(Map<String,Object> paramMap){
    	return listDao.delFltMailInfo(paramMap);
    }
    
    /**
     * 保存进港货邮行、进港行李解析信息
     */
    public Map<String,Integer> saveFltMailInfo(Map<String,Object> paramMap){
    	JSONArray dataArr = (JSONArray)paramMap.get("dataArr");
    	Map<String,Integer> rs = new HashMap<String, Integer>();
    	List<JSONObject> saveDataList = new ArrayList<JSONObject>();
    	List<JSONObject> updateDataList = new ArrayList<JSONObject>();
    	int saveCount = 0;
    	int updateCount = 0;
    	for(int i=0;i<dataArr.size();i++){
    		JSONObject dataObj = dataArr.getJSONObject(i);
    		String id = dataObj.getString("id");
    		//如果判断是行李重量，还是货邮重量
			String weight = dataObj.getString("weight");
			if(weight!=null&&!weight.equals("")){
				//C=货物，M=邮件，BY(B/BY/BT/T)=行李，E=航材, X=压舱物
				String mailPackageType = dataObj.getString("mailPackageType");
				if(mailPackageType!=null&&mailPackageType.equals("BY")){
					//weight为行李重量
					dataObj.put("packageWeight", weight);
				}else{
					//weight为货邮重量
					dataObj.put("mailWeight", weight);
				}
			}
    		
    		if(id!=null&&!id.equals("")){
    			//更新数据
    			updateDataList.add(dataObj);
    		}else{
    			//新增数据
    			dataObj.put("inFltId", paramMap.get("inFltId"));
				dataObj.put("outFltId", paramMap.get("outFltId"));
				dataObj.put("inFltNum", paramMap.get("inFltNum"));
				dataObj.put("inFltNum2", paramMap.get("inFltNum2"));
				dataObj.put("inFltDate", paramMap.get("inFlightDate"));
				dataObj.put("outFltNum", paramMap.get("outFltNum"));
				dataObj.put("outFltNum2", paramMap.get("outFltNum2"));
				dataObj.put("outFltDate", paramMap.get("outFlightDate"));
				dataObj.put("controlUser", paramMap.get("controlUser"));
				dataObj.put("supervisionUser", paramMap.get("supervisionUser"));
    			dataObj.put("mailPackageType", dataObj.getString("mailPackageType")==null?"":dataObj.getString("mailPackageType"));
				dataObj.put("arrivalApt", dataObj.getString("arrivalApt")==null?"":dataObj.getString("arrivalApt"));
				dataObj.put("aptType", dataObj.getString("aptType")==null?"":dataObj.getString("aptType"));
				dataObj.put("packageWeight", dataObj.getString("packageWeight")==null?"":dataObj.getString("packageWeight"));
				dataObj.put("packageNumber", dataObj.getString("packageNumber")==null?"":dataObj.getString("packageNumber"));
				dataObj.put("mailWeight", dataObj.getString("mailWeight")==null?"":dataObj.getString("mailWeight"));
				dataObj.put("mailCabin", dataObj.getString("mailCabin")==null?"":dataObj.getString("mailCabin"));
				dataObj.put("flag", "A");
    			saveDataList.add(dataObj);
    		}
    	}
    	if(saveDataList!=null&&saveDataList.size()>0){
    		saveCount = listDao.importFltMailInfo(saveDataList);
    	}
    	if(updateDataList!=null&&updateDataList.size()>0){
    		for(JSONObject updateObj:updateDataList){
    			updateCount += listDao.updateFltMailInfo(updateObj);
    		}
    	}
    	
    	rs.put("saveCount", saveCount);
    	rs.put("updateCount", updateCount);
    	return rs;
    }
    
    /**
     * 导出货邮行excel文件,由于要套打，所以不需要除数据外表格其他信息
     */
	public byte[] exportFltMailInfo(Map<String,Object> params){
    	//打印数据
    	//航班信息
    	String inFltDate = (String)params.get("inFltDate");
    	String outFltDate = (String)params.get("outFltDate");
    	String inFltNum2 = (String)params.get("inFltNum2");
    	String outFltNum2 = (String)params.get("outFltNum2");
    	String inAircraftNum = (String)params.get("inAircraftNum");
    	String inActtypeCode = (String)params.get("inActtypeCode");
    	
    	String outAircraftNum = (String)params.get("outAircraftNum");
    	String outActtypeCode = (String)params.get("outActtypeCode");
//    	String inEtd = (String)params.get("inEtd");
    	String inEta = (String)params.get("inEta");
    	String outEtd = (String)params.get("outEtd");
//    	String outEta = (String)params.get("outEta");
    	String inActstandCode = (String)params.get("inActstandCode");
    	String inRoute3code = (String)params.get("inRoute3code");
    	String outActstandCode = (String)params.get("outActstandCode");
    	String outRoute3code = (String)params.get("outRoute3code");
    	
    	String fltNum2 = "";
    	String fltDate = "";
    	String aircraftNum = "";
    	String route3code = "";
    	String actstandCode = "";
    	String acttypeCode = "";
    	if(inFltNum2!=null&&!inFltNum2.equals("")&&outFltNum2!=null&&!outFltNum2.equals("")){
    		//合并二字码航班号
    		if(inFltNum2.equals(outFltNum2)){
    			fltNum2 = inFltNum2;
    		}else{
    			String inPart = "";
        		String outPart = "";
        		String inCode2 = "";
        		String outCode2 = "";
        		inCode2 = inFltNum2.substring(0, 2);
        		outCode2 = outFltNum2.substring(0, 2);
        		inPart = inFltNum2.substring(2, inFltNum2.length());
        		outPart = outFltNum2.substring(2, outFltNum2.length());
        		if(inCode2.equals(outCode2)){
        			String tmpOutPart = "";
        			String tmpOutPart1 = "";
        			int i,j = 0;
        			i = j =outPart.length();
        			for(;i>0;i--){
        				tmpOutPart = outPart.substring(0, i);
        				tmpOutPart1 = outPart.substring(i, j);
        				if(inPart.startsWith(tmpOutPart)){
        					fltNum2 = inCode2+inPart+"/"+tmpOutPart1;
        					break;
        				}
        			}
        		}else{
        			fltNum2 = inFltNum2+"/"+outFltNum2;
        		}
    		}
    		
    		fltDate = inFltDate;
    		aircraftNum = inAircraftNum;
    		route3code = inRoute3code+"-"+airportCode3+"-"+outRoute3code;
    		actstandCode = inActstandCode;
    		acttypeCode = inActtypeCode;
    	}else if(inFltNum2!=null&&!inFltNum2.equals("")){
    		fltNum2 = inFltNum2;
    		fltDate = inFltDate;
    		aircraftNum = inAircraftNum;
    		route3code = inRoute3code+"-"+airportCode3;
    		actstandCode = inActstandCode;
    		acttypeCode = inActtypeCode;
    	}else{
    		fltNum2 = outFltNum2;
    		fltDate = outFltDate;
    		aircraftNum = outAircraftNum;
    		route3code = airportCode3+"-"+outRoute3code;
    		actstandCode = outActstandCode;
    		acttypeCode = outActtypeCode;
    	}
    	
    	String day = fltDate.substring(6, 8);
    	String month = fltDate.substring(4, 6);
    	
    	//货邮行、行李信息
    	//key 进港货邮inCM 进港行李inBY 过站货邮passCM 过站行李passBY 特货specialEX
    	Map<String,JSONObject> dataMap = new HashMap<String, JSONObject>();
    	String controlUserName = "";
    	String supervisionUserName = "";
    	String ids = "";
    	JSONArray dataArr = listDao.getFltMailInfo(params);
    	if(dataArr!=null&&dataArr.size()>0){
    		for(int i=0;i<dataArr.size();i++){
    			JSONObject dataObj = dataArr.getJSONObject(i);
    			ids += dataObj.getString("id")+",";
    			controlUserName = dataObj.getString("controlUserName");
    			supervisionUserName = dataObj.getString("supervisionUserName");
    			
    			//0:本站 1:过站
    			String aptType = dataObj.getString("aptType");
    			if(aptType!=null&&aptType.equals("0")){
    				aptType = "in";
    			}else{
    				aptType = "pass";
    			}
    			//C=货物，M=邮件，B/BY/BT/T=行李这里只使用BY，E=航材 X=压舱物 
    			//C、M是货邮 E、X是特货 BY是行李
    			String mailPackageType = dataObj.getString("mailPackageType");
    			String key = "";
    			if(mailPackageType!=null){
    				if(mailPackageType.equals("E")||mailPackageType.equals("X")){
    					//特货
    					key = "specialEX";
    				}else if(mailPackageType.equals("C")||mailPackageType.equals("M")){
    					//货邮
    					key = aptType+"CM";
    				}else{
    					//行李
    					key = aptType+"BY";
    				}
    			}
    			
    			if(dataMap.containsKey(key)){
    				JSONObject tmpDataObj = dataMap.get(key);
    				if(key.contains("BY")){
    					//合并行李信息
    					
    					//合并行李件数信息
        				int tmpPackageNumInt = 0;
        				int packageNumInt = 0;
        				String tmpPackageNum = tmpDataObj.getString("packageNum");
        				String packageNum = dataObj.getString("packageNum");
        				if(tmpPackageNum!=null&&!tmpPackageNum.equals("")){
        					tmpPackageNumInt = Integer.parseInt(tmpPackageNum);
        				}
        				if(packageNum!=null&&!packageNum.equals("")){
        					packageNumInt = Integer.parseInt(packageNum);
        				}
        				if((tmpPackageNumInt+packageNumInt)==0){
        					tmpDataObj.put("packageNum", "");
        				}else{
        					tmpDataObj.put("packageNum", tmpPackageNumInt+packageNumInt);
        				}
        				
        				//合并行李重量信息
        				int tmpPackageWeightInt = 0;
        				int packageWeightInt = 0;
        				String tmpPackageWeight = tmpDataObj.getString("packageWeight");
        				String packageWeight = dataObj.getString("packageWeight");
        				if(tmpPackageWeight!=null&&!tmpPackageWeight.equals("")){
        					tmpPackageWeightInt = Integer.parseInt(tmpPackageWeight);
        				}
        				if(packageWeight!=null&&!packageWeight.equals("")){
        					packageWeightInt = Integer.parseInt(packageWeight);
        				}
        				if((tmpPackageWeightInt+packageWeightInt)==0){
        					tmpDataObj.put("packageWeight", "");
        				}else{
        					tmpDataObj.put("packageWeight", tmpPackageWeightInt+packageWeightInt);
        				}
        				
        				//合并舱位信息
        				String tmpMailCabin = tmpDataObj.getString("mailCabin");
        				String mailCabin = dataObj.getString("mailCabin");
        				if(!("/"+tmpMailCabin+"/").contains("/"+mailCabin+"/")){
        					tmpDataObj.put("mailCabin", tmpMailCabin+"/"+mailCabin);
    					}
    				}else{
    					//合并货邮、特货信息
    					String showInfo = tmpDataObj.getString("showInfo");
    					String mailCabin = dataObj.getString("mailCabin");
        				String mailWeight = dataObj.getString("mailWeight");
        				String tmpShowInfo = mailCabin +"舱 "+ mailPackageType+":"+mailWeight;
    					if(showInfo!=null&&!showInfo.equals("")){
    						tmpDataObj.put("showInfo",showInfo+","+tmpShowInfo);
    					}else{
    						tmpDataObj.put("showInfo",tmpShowInfo);
    					}
    				}
    				dataMap.put(key, tmpDataObj);
    			}else{
    				String mailCabin = dataObj.getString("mailCabin");
    				String mailWeight = dataObj.getString("mailWeight");
    				dataObj.put("showInfo",mailCabin +"舱 "+ mailPackageType+":"+mailWeight);
    				dataMap.put(key, dataObj);
    			}
    		}
    	}
		
    	//读取打印excel模板,设置值
    	String filePath = (String)params.get("filePath");
    	File file = new File(filePath);
    	InputStream io = null;
    	HSSFWorkbook wb = null;
    	HSSFSheet sheet = null;
    	byte[] byteArr = null;
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			io = new FileInputStream(file);
			wb = new HSSFWorkbook(io);
			sheet = wb.getSheetAt(0);
			sheet.setForceFormulaRecalculation(true);
			//月
			HSSFCell cellMonth = sheet.getRow(2).getCell(0);
			cellMonth.setCellValue(month);
			//日
			HSSFCell cellDay = sheet.getRow(2).getCell(2);
			cellDay.setCellValue(day);
			//控制员
			HSSFCell cellControlUser = sheet.getRow(4).getCell(2);
			cellControlUser.setCellValue(controlUserName);
			//监装员
			HSSFCell cellSupervisionUser = sheet.getRow(4).getCell(4);
			cellSupervisionUser.setCellValue(supervisionUserName);
			//航班号
			HSSFCell cellFltNo2 = sheet.getRow(8).getCell(2);
			cellFltNo2.setCellValue(fltNum2);
			//飞机号
			HSSFCell cellAircraftNum = sheet.getRow(8).getCell(5);
			cellAircraftNum.setCellValue(aircraftNum);
			//航线
			HSSFCell cellRoute3code = sheet.getRow(9).getCell(2);
			cellRoute3code.setCellValue(route3code);
			//进港时间
			HSSFCell cellInEta = sheet.getRow(10).getCell(2);
			cellInEta.setCellValue(inEta);
			//出港时间
			HSSFCell cellOutEta = sheet.getRow(12).getCell(2);
			cellOutEta.setCellValue(outEtd);
			//机位
			HSSFCell cellActstandCode = sheet.getRow(10).getCell(6);
			cellActstandCode.setCellValue(actstandCode);
			//机型
			HSSFCell cellActtypeCode = sheet.getRow(12).getCell(6);
			cellActtypeCode.setCellValue(acttypeCode);
			
			//key 进港货邮inC 进港行李inBY 过站货邮passC 过站行李passBY
			//进港货邮 有 无
			HSSFCell cellIn = sheet.getRow(14).getCell(4);
			//进港本站货邮重量及舱位
			HSSFCell cellInMail = sheet.getRow(19).getCell(4);
	    	if(dataMap.containsKey("inCM")){
	    		//有进港货邮
	    		JSONObject dataObj = dataMap.get("inCM");
	    		String showInfo = dataObj.getString("showInfo");
	    		cellIn.setCellValue("有");
	    		cellInMail.setCellValue(showInfo);
	    	}else{
	    		//进港货邮 有 无
	    		cellIn.setCellValue("无");
	    		cellInMail.setCellValue("");
	    	}
	    	
	    	//进港本站行李件数及舱位
			HSSFCell cellInPackage = sheet.getRow(16).getCell(4);
	    	if(dataMap.containsKey("inBY")){
	    		JSONObject dataObj = dataMap.get("inBY");
	    		String mailCabin = dataObj.getString("mailCabin");
	    		if(mailCabin==null){
	    			mailCabin = "";
	    		}
	    		String mailPackageType = dataObj.getString("mailPackageType");
	    		String packageNum = dataObj.getString("packageNum");
	    		String packageWeight = dataObj.getString("packageWeight");
	    		cellInPackage.setCellValue(mailCabin+"舱 "+mailPackageType+":"+packageNum+"/"+packageWeight);
	    	}else{
	    		cellInPackage.setCellValue("");
	    	}
	    	
	    	//是否过站 是 否
			HSSFCell cellPass = sheet.getRow(22).getCell(4);
			//过站货邮重量及舱位
			HSSFCell cellPassMail = sheet.getRow(24).getCell(4);
			if(dataMap.containsKey("passCM")){
				//有过站货邮
				JSONObject dataObj = dataMap.get("passCM");
	    		String showInfo = dataObj.getString("showInfo");
	    		cellPass.setCellValue("有");
	    		cellPassMail.setCellValue(showInfo);
	    	}else{
	    		//进港货邮 有 无
	    		cellPass.setCellValue("无");
	    		cellPassMail.setCellValue("");
	    	}
			
			//过站行李件数及舱位
			HSSFCell cellPassPackage = sheet.getRow(23).getCell(4);
			if(dataMap.containsKey("passBY")){
	    		JSONObject dataObj = dataMap.get("passBY");
	    		String mailCabin = dataObj.getString("mailCabin");
	    		if(mailCabin==null){
	    			mailCabin = "";
	    		}
	    		String mailPackageType = dataObj.getString("mailPackageType");
	    		String packageNum = dataObj.getString("packageNum");
	    		String packageWeight = dataObj.getString("packageWeight");
	    		cellPassPackage.setCellValue(mailCabin+"舱 "+mailPackageType+":"+packageNum+"/"+packageWeight);
	    	}else{
	    		cellPassPackage.setCellValue("");
	    	}
			
			//备注 暂时空着
			HSSFCell cellMark = sheet.getRow(25).getCell(13);
//			cellMark.setCellValue("备注");
			
			//进港特货 暂时空着 X E是特货
			HSSFCell cellSpecialEX = sheet.getRow(15).getCell(4);
			if(dataMap.containsKey("specialEX")){
	    		JSONObject dataObj = dataMap.get("specialEX");
	    		String showInfo = dataObj.getString("showInfo");
	    		cellSpecialEX.setCellValue(showInfo);
	    	}else{
	    		cellSpecialEX.setCellValue("");
	    	}
			
			wb.write(os);
			byteArr = os.toByteArray();
		} catch (Exception e){
			ids = "";
			e.printStackTrace();
		} finally{
			if(os!=null){
    			try{
    				os.close();
				} catch (IOException e){
					ids = "";
					e.printStackTrace();
				}
    		}
			if(io!=null){
    			try{
					io.close();
				} catch (IOException e){
					ids = "";
					e.printStackTrace();
				}
    		}
    	}
		if(ids!=null&&!ids.equals("")){
			ids = ids.substring(0, ids.length()-1);
			//更新导出
			JSONObject dataObj = new JSONObject();
			dataObj.put("ids", ids);
			dataObj.put("printFlag", "1");
			listDao.updateFltMailInfo(dataObj);
		}
		return byteArr;
	}
    
	/**
     * 格式化月份
     * @param date
     * @return
     */
    private String getFormatDate(String date){
        String day = date.substring(0,2);
        String month = date.substring(2,5);
        String year = date.substring(5,7);
        String[] mm = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        for (int i = 0; i < mm.length; i++) {
            if(month.equals(mm[i])){
                month=String.valueOf(i+1);
            }
        }
        if (Integer.parseInt(month)<10) {
            month = "0"+month;
        }
        return "20"+year+month+day;
    }
    
    public boolean saveFilterConf(String type,String fieldName,String value){
    	try{
    		listDao.saveFilterConf(type,fieldName,value);
    	}catch(Exception e){
    		return false;
    	}
    	return true;
    }
    
    public JSONArray getFilterConf(){
    	return listDao.getFilterConf();
    }
}
