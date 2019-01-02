package com.neusoft.prss.scheduling.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.util.CustomXWPFDocument;
import com.neusoft.prss.common.util.ExportWordUtils;
import com.neusoft.prss.flightdynamic.entity.DepartPsgEntity;
import com.neusoft.prss.flightdynamic.entity.ExportFDExcel;
import com.neusoft.prss.flightdynamic.service.DepartPsgInfoService;
import com.neusoft.prss.grid.service.GridColumnService;
import com.neusoft.prss.scheduling.service.SchedulingListService;
import com.neusoft.prss.stand.entity.ResultByCus;

@Controller
@RequestMapping(value = "${adminPath}/scheduling/list")
public class SchedulingListController extends BaseController {
	private static final String airportCode3 = Global.getConfig("airport_code3");
	
    @Autowired
    private SchedulingListService listService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private GridColumnService gridService;
    
    @Autowired
    private DepartPsgInfoService departPsgInfoService;

    /**
     * 指挥调度公共列表
     * 
     * @param model
     * @param schemaId
     *            对应sys_grid_schema表id
     * @return
     */
    @RequestMapping(value = {"list",""})
    public String list(Model model,String schemaId) {
        model.addAttribute("schemaId", schemaId);
        dimAttributes(model,schemaId);
        return "prss/scheduling/schedulingList";
    }

    /**
     * 
     * Discription:航班动态-基本表格-默认数据.
     * 
     * @param request
     * @return
     * @return:返回值意义
     * @author:yu-zd
     * @update:2017年9月26日 yu-zd [变更描述]
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getDynamic")
    @ResponseBody
    public JSONArray getDynamic(String switches,String flagBGS,String schema,String param,String suffix) {
        String reskind = UserUtils.getCurrentJobKind().size() > 0 ? UserUtils.getCurrentJobKind().get(0).getKindCode() : "";
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            Map<String,Object> params = JSON.parseObject(param);
            if (params.get("vipFlags") != null && StringUtils.isNotBlank(params.get("vipFlags").toString())) {
                String vipFlags = params.get("vipFlags").toString();
                String vFlags[] = vipFlags.split(",");
                params.put("vipFlags", vFlags);
            }
            if (params.get("apron") != null && StringUtils.isNotBlank(params.get("apron").toString())) {
                String apron = params.get("apron").toString();
                String aprons[] = apron.split(",");
                params.put("aprons", aprons);
            }
            if (params.get("gate") != null && StringUtils.isNotBlank(params.get("gate").toString())) {
                String gate = params.get("gate").toString();
                String gates[] = gate.split(",");
                params.put("gates", gates);
            }
            if ("13".equals(schema)) {
            	params.put("schema", "13");
            }
            JSONArray jsonArr = listService.getDynamic(switches, flagBGS, reskind, schema, suffix,
                    UserUtils.getUser().getId(), params);
            return jsonArr;
        } else {
            JSONArray jsonArr = listService.getDynamic(switches, flagBGS, reskind, schema, suffix,
                    UserUtils.getUser().getId());
            return jsonArr;
        }
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
     * @author:yu-zd
     * @update:2017年11月2日 yu-zd [变更描述]
     */
    @RequestMapping(value = "getOptions")
    @ResponseBody
    public JSONObject getOptions(String q,String page,String limit,String type) {
        page = page == null ? "1" : page;
        JSONObject result = new JSONObject();
        int start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit) + 1;
        int end = Integer.parseInt(page) * Integer.parseInt(limit);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("q", q);
        param.put("start", start);
        param.put("end", end);
        if ("airline".equals(type)) {
            int count = listService.getAirlineTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray airlines = listService.getAirlines(param);
                result.put("item", airlines);
            } else {
                result.put("item", "[]");
            }
        } else if ("actType".equals(type)) {
            int count = listService.getActTypeTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray actTypes = listService.getActTypes(param);
                result.put("item", actTypes);
            } else {
                result.put("item", "[]");
            }
        } else if ("airport".equals(type)) {
            int count = listService.getAirportTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray airports = listService.getAirports(param);
                result.put("item", airports);
            } else {
                result.put("item", "[]");
            }
        }
        return result;
    }
    /**
     * 
     * Discription:指挥调度信息打印.
     * 
     * @param request
     * @param response
     * @param title
     * @param data
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月2日SunJ [变更描述]
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,String title,String data,
    		String switches,String flagBGS,String schema,String param,String suffix,String hasSheet2) {
        title = StringEscapeUtils.unescapeHtml4(title);
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray titleArray = JSONArray.parseArray(title);
        List<Map<String,String>> dataList = JSON.parseObject(data, new TypeReference<List<Map<String,String>>>() {
        });
        List<String> titleList1 = new ArrayList<String>();
        for (int i = 0; i < titleArray.size(); i++) {
            titleList1.add(titleArray.getJSONObject(i).getString("title"));
        }
       
        try {
            String fileName = "指挥调度" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            String agent = (String) request.getHeader("USER-AGENT");
            String downloadFileName = "";
            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
            } else {
                downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            
            
            String excelTitle = "指挥调度" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportFDExcel excel = null;
            
            /*新增sheet2数据*/
            if("yes".equals(hasSheet2)){
                List<String> titleList2 = new ArrayList<String>();
                JSONArray title2Array = listService.getSheet2Title(schema);
                for (int i = 0; i < title2Array.size(); i++) {
                	titleList2.add(title2Array.getJSONObject(i).getString("title"));
                }
                String reskind = UserUtils.getCurrentJobKind().size() > 0 ? UserUtils.getCurrentJobKind().get(0).getKindCode() : "";
                Map<String,Object> params = null;
                if (param != null) {
                    param = StringEscapeUtils.unescapeHtml4(param);
                    params = JSON.parseObject(param);
                    if (params.get("vipFlags") != null && StringUtils.isNotBlank(params.get("vipFlags").toString())) {
                        String vipFlags = params.get("vipFlags").toString();
                        String vFlags[] = vipFlags.split(",");
                        params.put("vipFlags", vFlags);
                    }
                }
                JSONArray data2 = listService.getPrintData(switches, flagBGS, reskind, schema, suffix, params);
                List<Map<String,String>> dataList2 = (List)data2;
                
            	excel = new ExportFDExcel(excelTitle, titleList1 ,titleList2);
            	
            	Sheet sheet = excel.getSheets().get(1);
                for(int i = 0 ; i < titleList2.size(); i++){
                	sheet.setColumnWidth(i, 2000);
                }
                PrintSetup printSetup = sheet.getPrintSetup();
                printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // 纸张
                printSetup.setLandscape(true);
                sheet.setMargin(HSSFSheet.TopMargin,( double ) 0.5 ); // 上边距
                sheet.setMargin(HSSFSheet.BottomMargin,( double ) 0.5 ); // 下边距
                sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.5 ); // 左边距
                sheet.setMargin(HSSFSheet.RightMargin,( double ) 0.5 ); // 右边距
                
            	excel.setDataList(titleArray, dataList,0);
            	excel.setDataList(title2Array, dataList2,1);
            }else{
            	excel = new ExportFDExcel(excelTitle, titleList1);
            	excel.setDataList(titleArray, dataList,0);
            }
            
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("指挥调度导出失败" + e.getMessage());
        }
    }

    /**
     * 
     * Discription:获取下拉选项.
     * 
     * @param model
     * @return:返回值意义
     * @author:yu-zd
     * @update:2017年9月20日 yu-zd [变更描述]
     */
    private void dimAttributes(Model model,String schemaId) {
        try {
            // 性质
            // JSONArray fltPropertys =
            // cacheService.getOpts("dim_flight_property", "property_code",
            // "property_shortname");
            //JSONArray fltPropertys = cacheService.getCommonDict("flight_property");
        	//延误原因
            JSONArray delyReson = cacheService.getOpts("dim_delay","delay_code","description_cn");
            model.addAttribute("delyReson", delyReson);
            //机位
            JSONArray bay = cacheService.getList("dim_bay");
            model.addAttribute("bay", bay);
            //延误原因类型
            JSONArray delay_type = cacheService.getList("dim_delay_type");
            model.addAttribute("delay_type", delay_type);
            //性质
            JSONArray fltPropertys = cacheService.getList("dim_task");
            model.addAttribute("fltPropertys", fltPropertys);
            // 外航/国内
            JSONArray alnFlags = cacheService.getCommonDict("airline_flag");
            model.addAttribute("alnFlags", alnFlags);
            // 航线性质
            JSONArray alntype = cacheService.getCommonDict("alntype");
            model.addAttribute("alntype", alntype);
            // 机型分类
            JSONArray actkind = cacheService.getList("dim_actkind");
            model.addAttribute("actkinds", actkind);

            // 是否为宽体机型
            JSONArray acttypeSizes = cacheService.getCommonDict("aln_flag");
            model.addAttribute("acttypeSizes", acttypeSizes);
            // 航站楼
            JSONArray terminals = cacheService.getCommonDict("terminal");
            model.addAttribute("terminals", terminals);
            // 航班范围
            JSONArray flightScopes = cacheService.getCommonDict("flight_scope");
            model.addAttribute("flightScopes", flightScopes);
            // 机坪区域
            JSONArray aprons = cacheService.getList("dim_bay_apron");
            model.addAttribute("aprons", aprons);
            // 廊桥位、外围航班
            JSONArray GAFlag = cacheService.getCommonDict("GAFlag");
            model.addAttribute("GAFlag", GAFlag);
            // 标识
            JSONArray identifying = cacheService.getCommonDict("identifying");
            model.addAttribute("identifying", identifying);
            // 状态1
            JSONArray actStatus = cacheService.getCommonDict("acfStatus");
            model.addAttribute("actStatus", actStatus);
            // 状态2
            JSONArray flyStatus = cacheService.getCommonDict("flyStatus");
            model.addAttribute("flyStatus", flyStatus);
            // 要客标识
            JSONArray vipFlag = cacheService.getCommonDict("vipFlag");
            model.addAttribute("vipFlags", vipFlag);
            // 跑道
            JSONArray runways = getDimFromOracle("dim_runway", "RUNWAY_CODE", "RUNWAY_CODE");
            model.addAttribute("runways", runways);
            // 除冰坪
            JSONArray dprks = gridService.getDimFromOracle("dim_dprk_code", "DPRK_CODE",
                    "DPRK_CODE||' '||RUNWAY||' '||W_OR_E");
            model.addAttribute("dprks", dprks);
            // 除冰位
            JSONArray dcnds = gridService.getDimFromOracle("dim_dcnd_code", "DCND_CODE",
                    "DCND_CODE||' '||DPRK_CODE||' '||(case when SlOW_FLAG=0 then '关车除冰' when slow_flag=1 then '慢车除冰' end)");
            model.addAttribute("dcnds", dcnds);
            // 除冰航班
            JSONArray iceFlags = cacheService.getCommonDict("iceFlag");
            model.addAttribute("iceFlags", iceFlags);
            //保障状态
            JSONArray guarantee = gridService.getDimFromOracle("dim_delay_type where parent is null","id","name");
            model.addAttribute("guarantee", guarantee);
            //如果是装卸调度  需要把额外的筛选配置加载进去
            if("11".equals(schemaId)){
            	JSONArray filterConfArr=listService.getFilterConf();
            	for(int i=0;i<filterConfArr.size();i++){
            		JSONObject o=filterConfArr.getJSONObject(i);
            		String attrType=o.getString("ATTR_TYPE");
            		String attrTypeCn="";
            		if(StringUtils.isNoneEmpty(attrType)){
            			attrTypeCn=attrType.replace("D", "国内").replace("I", "国际").replace("M", "混合");
            		}
            		o.put("ATTR_TYPE_CN", attrTypeCn);
            	}
            	model.addAttribute("filterConfArr", filterConfArr);
            }
        } catch (Exception e) {
            logger.error("添加下拉框选项失败" + e.getMessage());
        }
    }

    public JSONArray getDimFromOracle(String table,String idcol,String textcol) {
        return gridService.getDimFromOracle(table, idcol, textcol);
    }

    @RequestMapping(value = "autoManual")
    public String autoManual(Model model) {
        String officeId = UserUtils.getUser().getOffice().getId();
        JSONObject json = listService.getSemaByOfficeId(officeId);
        model.addAttribute("result", json);
        return "prss/scheduling/autoManual";
    }

    @ResponseBody
    @RequestMapping(value = "updateAutoManual")
    public String updateAutoManual(String autoManual) {
        JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(autoManual));
        listService.updateSema(json);
        return "success";
    }
    
    /**
     * 
     *Discription:混合航班判断.
     *@param fltId
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2018年1月21日 Heqg [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "isMix")
    public String isMix(String fltId) {
    	boolean isMix = listService.isMix(fltId);
    	if(isMix){
    		return "yes";
    	}
        return "no";
    }
    
    /**
     * 
     *Discription:混合航班国内登机口录入.
     *@param model
     *@param request
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2018年1月21日 Heqg [变更描述]
     */
    @RequestMapping(value = "dGateInput")
    public String dGateInput(Model model,String fltid) {
    	model.addAttribute("fltId", fltid);
    	String dGate = listService.getDGate(fltid);
    	model.addAttribute("dGate", dGate);
        return "prss/scheduling/dGateInput";
    }
    
    /**
     * 
     *Discription:保存国内登机口.
     *@param json
     *@return
     *@return:返回值意义
     *@author:Heqg
     *@update:2018年1月21日 Heqg [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "saveDGate")
    public String saveDGate(String json) {
    	JSONObject dGate = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(json));
        listService.updateDGate(dGate.getString("fltId"), dGate.getString("dGate"));
    	return "success";
    }
    
    /**
     * 跳转任务指派单页
     */
    @RequestMapping(value = "missionBillForm")
    public String missionBillForm(Model model,String inFltId,String outFltId) {
    	Map<String,Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("inFltId", inFltId);
    	paramMap.put("outFltId", outFltId);
    	JSONObject dataObj = listService.getFltInfo(paramMap);
    	if(dataObj!=null){
    		String inRoute3code = dataObj.getString("inRoute3code");
        	String outRoute3code = dataObj.getString("outRoute3code");
        	String inAircraftNum = dataObj.getString("inAircraftNum");
        	String outAircraftNum = dataObj.getString("outAircraftNum");
        	String inActtypeCode = dataObj.getString("inActtypeCode");
        	String outActtypeCode = dataObj.getString("outActtypeCode");
        	String inActstandCode = dataObj.getString("inActstandCode");
        	String outActstandCode = dataObj.getString("outActstandCode");
        	
        	String route3code = "";
        	String aircraftNum = "";
        	String acttypeCode = "";
        	String actstandCode = "";
        	if(inFltId!=null&&!inFltId.equals("")&&outFltId!=null&&!outFltId.equals("")){
        		route3code = inRoute3code +"-"+ airportCode3+"-"+outRoute3code;
        		aircraftNum = inAircraftNum;
        		acttypeCode = inActtypeCode;
        		actstandCode = inActstandCode;
        	}else if(inFltId!=null&&!inFltId.equals("")){
        		route3code = inRoute3code +"-"+ airportCode3;
        		aircraftNum = inAircraftNum;
        		acttypeCode = inActtypeCode;
        		actstandCode = inActstandCode;
        	}else if(outRoute3code!=null&&!outRoute3code.equals("")){
        		route3code = airportCode3+"-"+outRoute3code;
        		aircraftNum = outAircraftNum;
        		acttypeCode = outActtypeCode;
        		actstandCode = outActstandCode;
        	}
        	
        	dataObj.put("route3code", route3code);
        	dataObj.put("aircraftNum", aircraftNum);
        	dataObj.put("acttypeCode", acttypeCode);
        	dataObj.put("actstandCode", actstandCode);
    	}else{
    		dataObj = new JSONObject();
    	}
    	model.addAttribute("fltInfo", dataObj);
    	return "prss/scheduling/schedulingMissionBill";
    }
    
    /**
     * 获取进港货邮行、进港行李解析信息
     */
    @RequestMapping(value = "getFltMailInfo")
    @ResponseBody
    public JSONObject getFltMailInfo(HttpServletRequest request) {
    	JSONObject rs = new JSONObject();
    	String inFltNum = request.getParameter("inFltNum");
    	String inFltNum2 = request.getParameter("inFltNum2");
    	String inFlightDate = request.getParameter("inFltDate");
    	String outFltNum = request.getParameter("outFltNum");
    	String outFltNum2 = request.getParameter("outFltNum2");
    	String outFlightDate = request.getParameter("outFltDate");
    	
    	Map<String,Object> paramMap = new HashMap<String, Object>();
    	try {
    		paramMap.put("inFltNum", inFltNum);
    		paramMap.put("inFltNum2", inFltNum2);
    		paramMap.put("inFltDate", inFlightDate);
    		paramMap.put("outFltNum", outFltNum);
    		paramMap.put("outFltNum2", outFltNum2);
    		paramMap.put("outFltDate", outFlightDate);
    		JSONArray dataArr = listService.getFltMailInfo(paramMap);
    		rs.put("rows", dataArr);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	return rs;
    }
    
    /**
     * 导入进港货航邮、行李txt文件并解析
     */
    @ResponseBody
    @RequestMapping(value = "importFltMailInfo")
    public JSONObject importFltMailInfo(HttpServletRequest request) {
    	JSONObject rs = new JSONObject();
    	String result = "success";
    	String inFltId = request.getParameter("inFltId");
    	String outFltId = request.getParameter("outFltId");
    	String inFltNum = request.getParameter("inFltNum");
    	String inFltNum2 = request.getParameter("inFltNum2");
    	String inFlightDate = request.getParameter("inFltDate");
    	String outFltNum = request.getParameter("outFltNum");
    	String outFltNum2 = request.getParameter("outFltNum2");
    	String outFlightDate = request.getParameter("outFltDate");
    	String fltMail = request.getParameter("fltMail");
    	String fltPackage = request.getParameter("fltPackage");
    	String supervisionUser = request.getParameter("supervisionUser");
    	
    	if(inFltNum!=null&&!inFltNum.equals("")&&inFlightDate!=null&&!inFlightDate.equals("")&&((fltMail!=null&&!fltMail.equals(""))||(fltPackage!=null&&!fltPackage.equals("")))){
    		Map<String,Object> paramMap = new HashMap<String, Object>();
    		paramMap.put("inFltId", inFltId);
        	paramMap.put("outFltId", outFltId);
    		paramMap.put("inFltNum", inFltNum);
        	paramMap.put("inFltNum2", inFltNum2);
        	paramMap.put("inFlightDate", inFlightDate);
        	paramMap.put("outFltNum", outFltNum);
        	paramMap.put("outFltNum2", outFltNum2);
        	paramMap.put("outFlightDate", outFlightDate);
        	paramMap.put("fltMail", fltMail);
        	paramMap.put("fltPackage", fltPackage);
        	paramMap.put("controlUser", UserUtils.getUser().getId());
        	paramMap.put("supervisionUser", supervisionUser);
            try {
            	Map<String,Object> rsMap = listService.importFltMailInfo(paramMap);
            	List<JSONObject> fltPassengerList = (List<JSONObject>)rsMap.get("fltPassengerList");
            	String failInfo = (String)rsMap.get("failInfo");
            	if(failInfo!=null&&!failInfo.equals("")){
            		rs.put("failInfo", failInfo);
            		result = "fail";
            	}else{
            		//保存实际旅客信息
            		DepartPsgEntity entity = null;
            		if(fltPassengerList!=null&&fltPassengerList.size()>0){
            			entity = new DepartPsgEntity();
            			BigDecimal inFltIdB = null;
            			if(inFltId!=null&&!inFltId.equals("")){
            				inFltIdB = new BigDecimal(inFltId);
            			}
            			BigDecimal outFltIdB = null;
            			if(outFltId!=null&&!outFltId.equals("")){
            				outFltIdB = new BigDecimal(outFltId);
            			}
            			entity.setInFltid(inFltIdB);
            			entity.setOutFltid(outFltIdB);
            			entity.setSource("1");
            			
            			//判断旅客实际人数记录保存在哪个航段字段中
            			int tmpLegNum = 1;
            			for(int i=0;i<fltPassengerList.size();i++){
            				JSONObject passengerObj = fltPassengerList.get(i);
            				//0:本站 1:过站
            				String aptType = passengerObj.getString("aptType");
            				String passengerNum = passengerObj.getString("passengerNum");
            				String adPort = passengerObj.getString("adPort");
            				if(aptType.equals("1")){
            					entity.setTransitGpxnP(passengerNum);
            				}else{
            					if(tmpLegNum==1){
            						entity.setLeg1AdPort(adPort);
                					entity.setLeg1GpxnP(passengerNum);
            					}else if(tmpLegNum==2){
            						entity.setLeg2AdPort(adPort);
                					entity.setLeg2GpxnP(passengerNum);
            					}else if(tmpLegNum==3){
            						entity.setLeg3AdPort(adPort);
                					entity.setLeg3GpxnP(passengerNum);
            					}else if(tmpLegNum==4){
            						entity.setLeg4AdPort(adPort);
                					entity.setLeg4GpxnP(passengerNum);
            					}else if(tmpLegNum==5){
            						entity.setLeg5AdPort(adPort);
                					entity.setLeg5GpxnP(passengerNum);
            					}
            					tmpLegNum++;
            				}
            			}
            		}
            		
            		if(entity!=null){
            			DepartPsgEntity departPsgInfo = null;
                		List<DepartPsgEntity> departPsgInfoList = departPsgInfoService.getPassengerT(inFltId, outFltId, true, "");
                		if(departPsgInfoList!=null&&departPsgInfoList.size()>0){
                			//更新
                			departPsgInfo = departPsgInfoList.get(0);
                			departPsgInfo.getId();
                			entity.setId(departPsgInfo.getId());
                			entity.setUpdatorId(UserUtils.getUser().getId());
                			departPsgInfoService.updateDepartPag(entity);
                		}else{
                			//新增
                			entity.setCreatorId(UserUtils.getUser().getId());
                			departPsgInfoService.saveDepartPag(entity);
                		}
            		}
            	}
            } catch (Exception e) {
                logger.error("进港货航邮、行李文本导入失败" + e.getCause());
                result = "err";
            }
    	}else{
    		result = "non";
    	}
    	rs.put("result", result);
    	return rs;
    }
    
    /**
     * 删除进港货邮行、进港行李解析信息
     */
    @RequestMapping(value = "delFltMailInfo")
    @ResponseBody
    public String delFltMailInfo(HttpServletRequest request) {
    	String result = "success";
    	String id = request.getParameter("id");
    	Map<String,Object> paramMap = new HashMap<String, Object>();
    	try {
    		paramMap.put("id", id);
    		int count = listService.delFltMailInfo(paramMap);
    		if(count==0){
    			result = "non";
    		}
		} catch (Exception e) {
			result = "err";
			logger.error(e.getMessage());
		}
    	return result;
    }
    
    /**
     * 新增、更新进港货邮行、进港行李解析信息
     */
    @RequestMapping(value = "saveFltMailInfo")
    @ResponseBody
    public String saveFltMailInfo(HttpServletRequest request) {
    	String result = "success";
	  	String data = request.getParameter("data");
	  	String inFltId = request.getParameter("inFltId");
    	String outFltId = request.getParameter("outFltId");
    	String inFltNum = request.getParameter("inFltNum");
    	String inFltNum2 = request.getParameter("inFltNum2");
    	String inFlightDate = request.getParameter("inFltDate");
    	String outFltNum = request.getParameter("outFltNum");
    	String outFltNum2 = request.getParameter("outFltNum2");
    	String outFlightDate = request.getParameter("outFltDate");
    	String supervisionUser = request.getParameter("supervisionUser");
	  	try {
			JSONArray dataArr = JSONArray.parseArray(data);
	    	Map<String,Object> paramMap = new HashMap<String, Object>();
	    	paramMap.put("inFltId", inFltId);
	    	paramMap.put("outFltId", outFltId);
    		paramMap.put("dataArr", dataArr);
    		paramMap.put("inFltNum", inFltNum);
        	paramMap.put("inFltNum2", inFltNum2);
        	paramMap.put("inFlightDate", inFlightDate);
        	paramMap.put("outFltNum", outFltNum);
        	paramMap.put("outFltNum2", outFltNum2);
        	paramMap.put("outFlightDate", outFlightDate);
        	paramMap.put("controlUser", UserUtils.getUser().getId());
        	paramMap.put("supervisionUser", supervisionUser);
    		listService.saveFltMailInfo(paramMap);
		} catch (Exception e) {
			result = "err";
			logger.error(e.getMessage());
		}
    	return result;
    }
    
    /**
     * 打印任务指派单
     */
    @ResponseBody
    @RequestMapping(value = "exportFltMailInfo")
    public void exportFltMailInfo(HttpServletRequest request,HttpServletResponse response) {
    	String inFltNum = request.getParameter("inFltNum");
    	String inFltNum2 = request.getParameter("inFltNum2");
    	String inFlightDate = request.getParameter("inFlightDate");
    	String outFltNum = request.getParameter("outFltNum");
    	String outFltNum2 = request.getParameter("outFltNum2");
    	String outFlightDate = request.getParameter("outFlightDate");
    	String inAircraftNum = request.getParameter("inAircraftNum");
    	String inActtypeCode = request.getParameter("inActtypeCode");
    	String outAircraftNum = request.getParameter("outAircraftNum");
    	String outActtypeCode = request.getParameter("outActtypeCode");
    	String inEtd = request.getParameter("inEtd");
    	String inEta = request.getParameter("inEta");
    	String outEtd = request.getParameter("outEtd");
    	String outEta = request.getParameter("outEta");
    	String inActstandCode = request.getParameter("inActstandCode");
    	String inRoute3code = request.getParameter("inRoute3code");
    	String outActstandCode = request.getParameter("outActstandCode");
    	String outRoute3code = request.getParameter("outRoute3code");
    	String fltNum2 = "";
    	if(inFltNum2!=null&&!inFltNum2.equals("")){
    		fltNum2 = inFltNum2;
    	}else{
    		fltNum2 = outFltNum2;
    	}
    	
    	Map<String,Object> params = new HashMap<String,Object>();
    	try {
    		String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
        	String tempPath = wiPath + "/template/任务指派单套打模板.xls";
        	params.put("inFltNum", inFltNum);
        	params.put("inFltNum2", inFltNum2);
        	params.put("inFltDate", inFlightDate);
        	params.put("outFltNum", outFltNum);
        	params.put("outFltNum2", outFltNum2);
        	params.put("outFltDate", outFlightDate);
        	params.put("inAircraftNum", inAircraftNum);
        	params.put("inActtypeCode", inActtypeCode);
        	params.put("outAircraftNum", outAircraftNum);
        	params.put("outActtypeCode", outActtypeCode);
        	params.put("inEtd", inEtd);
        	params.put("inEta", inEta);
        	params.put("outEtd", outEtd);
        	params.put("outEta", outEta);
        	params.put("inActstandCode", inActstandCode);
        	params.put("inRoute3code", inRoute3code);
        	params.put("outActstandCode", outActstandCode);
        	params.put("outRoute3code", outRoute3code);
        	params.put("filePath", tempPath);
        	byte[] content = listService.exportFltMailInfo(params);
        	
   			String fileName = fltNum2+"任务指派单" + DateUtils.getDate("yyyyMMdd") + ".xls";
   			setHeader(request, response, fileName);
			response.getOutputStream().write(content);
            response.getOutputStream().flush();
		} catch (Exception e) {
			logger.error("任务指派单导出失败" + e.getMessage());
		}
    }
    
    /**
     * 打印服务项目确认书
     */
    @ResponseBody
	@RequestMapping(value = "exportServiceConfirm")
	public void exportServiceConfirm(HttpServletRequest request,HttpServletResponse response) throws URISyntaxException{
		String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
//    	String tempPath = wiPath + "/template/服务项目确认书套打模板.docx";
    	String tempPath = wiPath + "/template/服务项目确认书.docx";
    	File file = new File(tempPath);
		if (file != null && file.exists()) {
			String inFltId=request.getParameter("inFltId");
			String outFltId=request.getParameter("outFltId");
			Map<String,Object> paramMap = new HashMap<String, Object>();
		    paramMap.put("inFltId", inFltId);
			paramMap.put("outFltId", outFltId);
			JSONObject dataObj = listService.getFltInfo(paramMap);
			if (dataObj != null && !dataObj.isEmpty()) {
				String outFltNum2 = dataObj.getString("outFltNum2");
				if(outFltNum2==null){
					outFltNum2 = "";
				}
				String outRouteName = dataObj.getString("outRouteName");
				String airportName = cacheService.mapGet("dim_airport3_map", airportCode3);
				if(outRouteName==null){
					outRouteName = "";
				}
				dataObj.put("outRouteName", airportName+"-"+outRouteName);
				OutputStream out = null;
				try {
					Map<String,String> textMap = (Map<String, String>) JSON.parse(dataObj.toJSONString());
					CustomXWPFDocument document = ExportWordUtils.change(tempPath, textMap);
					String fileName = outFltNum2+"服务项目确认书" + DateUtils.getDate("yyyyMMdd") + ".docx";
		            setHeader(request,response,fileName);
		            out = response.getOutputStream();
		            document.write(out);
		            out.flush();
				} catch (IOException e) {
					logger.error(e.toString());
				} finally {
		            try {
		            	if(out!=null){
			                out.close();
		            	}
		            } catch (Exception e) {
		                logger.error("输出流关闭失败" + e.getMessage());
		            }
		        }
			}
			return;
		}
	}
	
	private void setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		try {
			response.reset();
	        response.setContentType("application/octet-stream; charset=utf-8");
	        String agent = (String) request.getHeader("USER-AGENT");
	        String downloadFileName = "";
	        if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
	            downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
	        } else {
	            downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
		}catch(Exception e){
			logger.error("set header error:"+e.toString());
		}
	}
    
    @ResponseBody
    @RequestMapping(value = "printFilter")
    public ResultByCus printFilter(String type,String fieldName,String value) {
    	ResultByCus result=new ResultByCus();
    	boolean flag=false;
    	flag=listService.saveFilterConf(type,fieldName,value);
    	if(flag){
    		result.setCode("0000");
    		result.setMsg("配置保存成功");
    	}else{
    		result.setCode("1000");
    		result.setMsg("配置保存失败");
    	}
		return result; 
    }
}
