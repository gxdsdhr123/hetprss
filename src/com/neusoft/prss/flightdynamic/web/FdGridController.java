/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月18日 下午7:48:59
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.Log;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.actstand.service.DispatchActstandService;
import com.neusoft.prss.asup.service.FlightTimeService;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.entity.OperationLogEntity;
import com.neusoft.prss.common.service.OperLogWriteService;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.flightdynamic.entity.ExportFDExcel;
import com.neusoft.prss.flightdynamic.entity.FdFltGbak;
import com.neusoft.prss.flightdynamic.service.FDChangeService;
import com.neusoft.prss.flightdynamic.service.FdGanttService;
import com.neusoft.prss.flightdynamic.service.FdGridService;
import com.neusoft.prss.flightdynamic.service.FdHisService;
import com.neusoft.prss.fltinfo.service.UpdateFltinfoService;
import com.neusoft.prss.getflthis.service.GetFltHisService;
import com.neusoft.prss.getfltinfo.service.GetFltInfoService;
import com.neusoft.prss.grid.entity.GridColumn;
import com.neusoft.prss.grid.entity.GridColumnGroup;
import com.neusoft.prss.grid.service.GridColumnService;
import com.neusoft.prss.scheduling.service.SchedulingHisGanttService;
import com.neusoft.prss.scheduling.service.SchedulingHisListService;

@Controller
@RequestMapping(value = "${adminPath}/flightDynamic")
public class FdGridController extends BaseController {
    @Autowired
    private GridColumnService gridService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private FdGridService fdGridService;

    @Resource
    private FdGanttService fdGanttService;

    @Autowired
    private GetFltInfoService getFltInfoService;

    @Autowired
    private GetFltHisService fltInfoHisService;

    @Resource
    private FileService fileService;

    @Autowired
    private SchedulingHisGanttService schedulingHisGanttService;
    
    @Autowired
    private SchedulingHisListService schedulingHisListService;

    @Resource
    private FdHisService fdHisService;
    
    @Autowired
	private OperLogWriteService LogWriteservice;
    
    @Autowired
    private DispatchActstandService dispatchActstandService;
    
    @Resource
    private FlightTimeService flightTimeService;
    
    @Resource
	private FDChangeService fdChangeService;
    
    @Resource
	private UpdateFltinfoService updateFltinfoService;

    /**
     * 
     * Discription:获取航班动态页面.
     * 
     * @return
     * @return:航班动态页面
     * @author:SunJ
     * @update:2017年9月18日 SunJ [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        dimAttributes(model);
        return "prss/flightdynamic/flightDynList";
    }

    /**
     * 
     * Discription:航班动态甘特图页面.
     * 
     * @param model
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "listGantt")
    public String listGantt(Model model) {
        dimAttributes(model);
        return "prss/flightdynamic/flightDynGantt";
    }
    
    /**
     * 航班动态预排甘特图页面
     * @param model
     * @return
     */
    @RequestMapping(value = "listWalkthroughGantt")
    public String listWalkthroughGantt(Model model) {
        dimAttributes(model);
        return "prss/flightdynamic/fdWalkthroughGantt";
    }
    
    /**
     * 航班动态远机位登机口预排页面
     * @param model
     * @return
     */
    @RequestMapping(value = "listGateGantt")
    public String listGateGantt(Model model) {
        dimAttributes(model);
        return "prss/flightdynamic/fdGateGantt";
    }
    

    /**
     * 
     * Discription:航班动态单航班甘特图页面.
     * 
     * @param model
     * @param inFltid
     * @param outFltid
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年12月1日 SunJ [变更描述]
     */
    @RequestMapping(value = "listSingleGantt")
    public String listSingleGantt(Model model,String inFltid,String outFltid) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("inFltid", inFltid.length() == 0 ? outFltid : inFltid);
        params.put("outFltid", outFltid.length() == 0 ? inFltid : outFltid);
        Map<String,Object> info = fdGanttService.getSingleFlightInfo(params);
        model.addAllAttributes(info);
        model.addAttribute("inFltid", inFltid);
        model.addAttribute("outFltid", outFltid);
        return "prss/flightdynamic/fdSingleGantt";
    }

    /**
     * 
     * Discription:历史单航班甘特图.
     * 
     * @param model
     * @param inFltid
     * @param outFltid
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年12月14日 SunJ [变更描述]
     */
    @RequestMapping(value = "listSingleHisGantt")
    public String listSingleHisGantt(Model model,String inFltid,String outFltid) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("inFltid", inFltid.length() == 0 ? outFltid : inFltid);
        params.put("outFltid", outFltid.length() == 0 ? inFltid : outFltid);
        Map<String,Object> info = schedulingHisGanttService.getSingleHisFlightInfo(params);
        model.addAllAttributes(info);
        model.addAttribute("inFltid", inFltid);
        model.addAttribute("outFltid", outFltid);
        return "prss/flightdynamic/fdSingleHisGantt";
    }

    /**
     * 
     * Discription:获取航班动态Y轴数据（机坪机位）.
     * 
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttYData")
    @ResponseBody
    public String ganttYData() {
        JSONArray arr = fdGanttService.getFdYData();
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 
     * Discription:获取航班动态单航班甘特图Y轴数据（流程节点）.
     * 
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年12月1日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttSingleYData")
    @ResponseBody
    public String ganttSingleYData(String inFltid,String outFltid) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("inFltid", inFltid);
        params.put("outFltid", outFltid);
        JSONArray arr = fdGanttService.getFdSingleYData(params);
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 
     * Discription:历史单航班甘特Y轴数据.
     * 
     * @param inFltid
     * @param outFltid
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年12月14日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttSingleHisYData")
    @ResponseBody
    public String ganttSingleHisYData(String inFltid,String outFltid) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("inFltid", inFltid);
        params.put("outFltid", outFltid);
        JSONArray arr = schedulingHisGanttService.getFdSingleHisYData(params);
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 
     * Discription:获取航班动态甘特图数据.
     * 
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttData")
    @ResponseBody
    public String ganttData(String param) {
        Map<String,Object> params = new HashMap<String,Object>();
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            params = JSON.parseObject(param);
        }
        String arr = fdGanttService.ganttData(params);
        return arr;
    }

    /**
     * 
     * Discription:航班动态单航班甘特图数据.
     * 
     * @param inFltid
     * @param outFltid
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年12月14日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttSingleData")
    @ResponseBody
    public String ganttSingleData(String inFltid,String outFltid) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("inFltid", inFltid);
        params.put("outFltid", outFltid);
        String arr = fdGanttService.ganttSingleData(params);
        return arr;
    }

    /**
     * 
     * Discription:历史单航班甘特数据.
     * 
     * @param inFltid
     * @param outFltid
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年12月14日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttSingleHisData")
    @ResponseBody
    public String ganttSingleHisData(String inFltid,String outFltid) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("inFltid", inFltid);
        params.put("outFltid", outFltid);
        String arr = schedulingHisGanttService.ganttSingleHisData(params);
        return arr;
    }

    /**
     * 
     * Discription:获取航班动态甘特条详细信息.
     * 
     * @param id
     * @param inFltid
     * @param outFltid
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年12月14日 SunJ [变更描述]
     */
    @RequestMapping(value = "getGanttDetail")
    @ResponseBody
    public List<List<String>> getGanttDetail(String id,String inFltid,String outFltid) {
        List<List<String>> detail = fdGanttService.getGanttDetail(id, inFltid, outFltid);
        return detail;
    }

    @RequestMapping(value = "columnUpdate")
    @ResponseBody
    public String columnUpdate(String fltid,String col,String value) {
        Map<String,String> map = gridService.getTableByCol(col);
        if (map.isEmpty()) {
            return "error";
        }
        Map<String,String> param = new HashMap<String,String>();
        param.put("fltid", fltid);
        param.put("col", map.get("col"));
        param.put("table", map.get("table"));
        param.put("val", value);
        return gridService.columnUpdate(param);
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
     * @author:SunJ
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "getOptions")
    @ResponseBody
    public JSONObject getOptions(String q,String page,String limit,String type) {
        page = page == null ? "1" : page;
        JSONObject result = new JSONObject();
        int start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit) + 1;
        int end = Integer.parseInt(page) * Integer.parseInt(limit);
        end = end == 0 ? 100000 : end;
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("q", q);
        param.put("start", start);
        param.put("end", end);
//        if ("airline".equals(type)) {
//            int count = gridService.getAirlineTotNum(param);
//            result.put("count", count);
//            if (count != 0) {
//                JSONArray airlines = gridService.getAirlines(param);
//                result.put("item", airlines);
//            } else {
//                result.put("item", "[]");
//            }
//        } else 
        if ("actType".equals(type)) {
            int count = gridService.getActTypeTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray actTypes = gridService.getActTypes(param);
                result.put("item", actTypes);
            } else {
                result.put("item", "[]");
            }
        } else if ("departAirport".equals(type) || "arriveAirport".equals(type)) {
            int count = gridService.getAirportTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray airports = gridService.getAirports(param);
                result.put("item", airports);
            } else {
                result.put("item", "[]");
            }
        } else if ("guarantee".equals(type)) {
        	JSONArray guarantee = gridService.getGuarantee();
            result.put("item", guarantee);
        }
        return result;
    }

    /**
     * 
     * Discription:航班动态-基本表格-初始列头.
     * 
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年9月23日 SunJ [变更描述]
     */
    @RequestMapping(value = "getDefaultColumns")
    @ResponseBody
    public String getDefaultColumns(HttpServletRequest request) {
        String schema = request.getParameter("schema");
        String columns = gridService.getColumns(UserUtils.getUser().getId(), schema);
        return columns;
    }

    /**
     * 
     * Discription:航班动态-基本表格-默认数据.
     * 
     * @param request
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年9月26日 SunJ [变更描述]
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getDynamic")
    @ResponseBody
    public JSONArray getDynamic(String switches,String flagBGS,String schema,String param,String suffix,String time) {
        if (suffix == null) {
            suffix = "";
        }
        if (param != null) {
            param = StringEscapeUtils.unescapeHtml4(param);
            Map<String,Object> params = JSON.parseObject(param);
            if (params.get("gate") != null && StringUtils.isNotBlank(params.get("gate").toString())) {
                String gate = params.get("gate").toString();
                String gates[] = gate.split(",");
                params.put("gates", gates);
            }
            JSONArray jsonArr = gridService.getDynamic(switches, flagBGS, "master", schema, suffix,
                    UserUtils.getUser().getId(),time, params);
            return jsonArr;
        } else {
            JSONArray jsonArr = gridService.getDynamic(switches, flagBGS, "master", schema, suffix,
                    UserUtils.getUser().getId(),time);
            return jsonArr;
        }

    }

    /**
     * 
     * Discription:航班动态-列头设置-表格.
     * 
     * @param request
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年9月23日 SunJ [变更描述]
     */
    @RequestMapping(value = "getColumnsSetting")
    @ResponseBody
    public JSONArray getColumnsSetting(HttpServletRequest request) {
        String schema = request.getParameter("schema");
        List<GridColumn> columns = gridService.getSelectedList(UserUtils.getUser().getId(), schema);
        JSONArray array = new JSONArray();
        for (int i = 0; i < columns.size(); i++) {
            JSONObject json = (JSONObject) JSON.toJSON(columns.get(i));
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * Discription:航班动态-列头设置-字段选择.
     * 
     * @param model
     * @param request
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年9月26日 SunJ [变更描述]
     */
    @RequestMapping(value = "settingList")
    public String settinglist(Model model,HttpServletRequest request) { 
    	String schema = request.getParameter("schema");
	    List<GridColumn> selected = gridService.getSelectedList(UserUtils.getUser().getId(), schema);
	    Set<String> selectedColIds = new HashSet<String>();
	    for(GridColumn column : selected){//已选列id用于判断开关默认选中
	    	selectedColIds.add(column.getId());
	    }
	    List<GridColumnGroup> columns = gridService.getColumnBySchema(schema);
	    model.addAttribute("selected", selected);
	    model.addAttribute("schema", schema);
	    model.addAttribute("columns", columns);
	    model.addAttribute("selectedColIds", selectedColIds);
    return "prss/flightdynamic/flightDynSet";}

    /**
     * 
     * Discription:航班动态-列头设置-保存.
     * 
     * @param data
     * @param schema
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年9月26日 SunJ [变更描述]
     */
    @RequestMapping(value = "saveHeadInfo")
    @ResponseBody
    public String saveHeadInfo(String data,String schema) {
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray dataArray = JSONArray.parseArray(data);
        List<Map<String,String>> params = new ArrayList<Map<String,String>>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject j = dataArray.getJSONObject(i);
            Map<String,String> map = new HashMap<String,String>();
            map.put("user", UserUtils.getUser().getId());
            map.put("id", j.getString("id"));
            map.put("name", j.getString("title"));
            map.put("desc", j.getString("colDesc"));
            map.put("width", j.getString("width"));
            map.put("sort", i + "");
            map.put("schema", schema);
            map.put("class", j.getString("class"));
            params.add(map);
        }
        String msg = gridService.saveHeadInfo(params, schema, UserUtils.getUser().getId());
        return msg;
    }

    /**
     * 
     * Discription:航班动态信息打印.
     * 
     * @param request
     * @param response
     * @param title
     * @param data
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,String title,String data) {
        title = StringEscapeUtils.unescapeHtml4(title);
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray titleArray = JSONArray.parseArray(title);
        List<Map<String,String>> dataList = JSON.parseObject(data, new TypeReference<List<Map<String,String>>>() {
        });
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < titleArray.size(); i++) {
            titleList.add(titleArray.getJSONObject(i).getString("title"));
        }
        try {
            String fileName = "航班动态" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
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

            String excelTitle = "航班动态" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportFDExcel excel = new ExportFDExcel(excelTitle, titleList);
            excel.setDataList(titleArray, dataList);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("航班动态导出失败" + e.getMessage());
        }
    }
    
    @RequestMapping(value = "printPassenger")
    public void printPassenger(HttpServletRequest request,HttpServletResponse response) {
        try {
            String fileName = DateUtils.getDate("yyyyMMdd HHmmss") + "离港信息总表.xls";
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

            List<String> titleList = Arrays.asList("信息编号","航班日期","承运方","机场","地区序号","航班号","机型","航班性质","航线","航线简称","航线类别","航段","航段分类","最大业载","最大座位","配额业载","配额座位","可供业载","可供座位","进出港属性","架次","起降时间","成人数","儿童数","婴儿数","过站成人数","过站儿童数","过站婴儿数","成人数后续","儿童数后续","婴儿数后续","货物重量","邮件重量","行李重量","行李件数","过站货物重量","过站邮件重量","过站行李重量","过站行李件数","货物重量后续","邮件重量后续","行李重量后续","行李件数后续","机号","外籍护照数");
            JSONArray data = fdGridService.getPassengerData();
            
            Map<String,String> propertyMap = cacheService.getMap("dim_flight_property_map");
            Map<String,String> airportMap = cacheService.getMap("dim_airport_map");
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("离港信息总表");
            HSSFRow head = sheet.createRow(0);
            for(int i=0;i<titleList.size();i++) {
            	head.createCell(i).setCellValue(titleList.get(i));
            }
            int index = 1;
            HSSFRow row = null;
            for(int i=0;i<data.size();i++) {
            	JSONObject o = data.getJSONObject(i);
            	String io = o.getString("INOUTFLAG");
            	if("A0".equals(io) || "A1".equals(io) || "D1".equals(io)) {
            		String[] rous = o.getString("INROUTE3CODE").split("-");
            		for(int p=0;p<rous.length;p++) {
            			row = sheet.createRow(index);
                		//信息编号
                		row.createCell(0).setCellValue(index);
                		index++;
                		//航班日期
                		row.createCell(1).setCellValue(o.getString("INFLTDATE"));
                		//承运方
                		row.createCell(2).setCellValue(o.getString("INALN3CODE"));
                		//机场
                		row.createCell(3).setCellValue("BAV");
                		//地区序号
                		row.createCell(4).setCellValue("西北地区");
                		//航班号
                		row.createCell(5).setCellValue(o.getString("INFLTNO2"));
                		//机型
                		row.createCell(6).setCellValue(o.getString("INACTTYPECODE"));
                		//航班性质
                		row.createCell(7).setCellValue(propertyMap.get(o.getString("INPROPERTYCODE")));
                		//航线
                		String route = "";
                		if(o.getString("INFLTNO2")!= null && o.getString("INFLTNO2").equals(o.getString("OUTFLTNO2"))) {//过站航班
                			route = o.getString("INROUTE3CODE")+"-BAV-"+o.getString("OUTROUTE3CODE");
                		}else {
                			route = o.getString("INROUTE3CODE")+"-BAV";
                		}
                		row.createCell(8).setCellValue(route);
                		//航线简称
                		String[] routes = route.split("-");
                		String dim = "DOM";
                		String shortRoute = "";
                		for(int j=0;j<routes.length;j++) {
                			JSONObject airportInfo = JSONObject.parseObject(airportMap.get(routes[j]));
                			if(airportInfo != null && airportInfo.getString("d_or_i") != null && airportInfo.getString("d_or_i").equals("I")) {
                				dim = "INT";
                			}else if(airportInfo != null && airportInfo.getString("d_or_i") != null && airportInfo.getString("d_or_i").equals("R")) {
                				dim = "REG";
                			}
                			if(airportInfo != null && airportInfo.getString("airport_shortname") != null) {
                				if(j == 0) {
                					shortRoute += airportInfo.getString("airport_shortname");
                				}else {
                					shortRoute += "-"+airportInfo.getString("airport_shortname");
                				}
                			}
                		}
                		row.createCell(9).setCellValue(shortRoute);
                		//航线类别
                		row.createCell(10).setCellValue(dim);
                		//航段
                		String rou = rous[p];
                		row.createCell(11).setCellValue(rou+"-BAV");
                		//航段分类
                		String rouDim = "DOM";
                		JSONObject prevInfo = JSONObject.parseObject(airportMap.get(rou));
                		String prevDI = null;
                		if(prevInfo != null) {
                			prevDI = prevInfo.getString("d_or_i");
                		}
                		if(prevDI != null && prevDI.equals("I")) {
                			rouDim = "INT";
                		}else if(prevDI != null && prevDI.equals("R")) {
                			rouDim = "REG";
                		}
                		row.createCell(12).setCellValue(rouDim);
                		//最大业载
                		row.createCell(13).setCellValue(o.getString("INMAXFREIGHT"));
                		//最大座位
                		row.createCell(14).setCellValue(o.getString("INMAXPASSENGER"));
                		//配额业载
                		row.createCell(15).setCellValue("");
                		//配额座位
                		row.createCell(16).setCellValue("");
                		//可供业载
                		row.createCell(17).setCellValue("");
                		//可供座位
                		row.createCell(18).setCellValue("");
                		//进出港属性
                		row.createCell(19).setCellValue("0101");
                		//架次
                		if(p == rous.length-1) {
                			row.createCell(20).setCellValue("1");
                		}else {
                			row.createCell(20).setCellValue("0");
                		}
                		//起降时间
                		row.createCell(21).setCellValue(o.getString("INATA") != null?(o.getString("INATA")+":00"):"");
                		String n = String.valueOf(p+1);
                		//成人数
                		row.createCell(22).setCellValue(o.getString("IP"+n));
                		//儿童数
                		row.createCell(23).setCellValue(o.getString("IC"+n));
                		//婴儿数
                		row.createCell(24).setCellValue(o.getString("II"+n));
                		//过站成人数
                		row.createCell(25).setCellValue(o.getString("ITP"));
                		//过站儿童数
                		row.createCell(26).setCellValue(o.getString("ITC"));
                		//过站婴儿数
                		row.createCell(27).setCellValue(o.getString("ITI"));
                		//成人数后续
                		row.createCell(28).setCellValue("");
                		//儿童数后续
                		row.createCell(29).setCellValue("");
                		//婴儿数后续
                		row.createCell(30).setCellValue("");
                		//货物重量
                		row.createCell(31).setCellValue(o.getString("IH"+n));
                		//邮件重量
                		row.createCell(32).setCellValue(o.getString("IM"+n));
                		//行李重量
                		row.createCell(33).setCellValue(o.getString("IB"+n));
                		//行李件数
                		row.createCell(34).setCellValue("");
                		//过站货物重量
                		row.createCell(35).setCellValue(o.getString("ITH"));
                		//过站邮件重量
                		row.createCell(36).setCellValue(o.getString("ITM"));
                		//过站行李重量
                		row.createCell(37).setCellValue(o.getString("ITB"));
                		//过站行李件数
                		row.createCell(38).setCellValue("");
                		//货物重量后续
                		row.createCell(39).setCellValue("");
                		//邮件重量后续
                		row.createCell(40).setCellValue("");
                		//行李重量后续
                		row.createCell(41).setCellValue("");
                		//行李件数后续
                		row.createCell(42).setCellValue("");
                		//机号
                		row.createCell(43).setCellValue(o.getString("INACTNO"));
                		//外籍护照数
                		row.createCell(44).setCellValue("");
            		}
            	}
            	if("D0".equals(io) || "A1".equals(io) || "D1".equals(io)) {
            		String[] rous = o.getString("OUTROUTE3CODE").split("-");
            		for(int q=0;q<rous.length;q++) {
            			row = sheet.createRow(index);
                		//信息编号
                		row.createCell(0).setCellValue(index);
                		index++;
                		//航班日期
                		row.createCell(1).setCellValue(o.getString("OUTFLTDATE"));
                		//承运方
                		row.createCell(2).setCellValue(o.getString("OUTALN3CODE"));
                		//机场
                		row.createCell(3).setCellValue("BAV");
                		//地区序号
                		row.createCell(4).setCellValue("西北地区");
                		//航班号
                		row.createCell(5).setCellValue(o.getString("OUTFLTNO2"));
                		//机型
                		row.createCell(6).setCellValue(o.getString("OUTACTTYPECODE"));
                		//航班性质
                		row.createCell(7).setCellValue(propertyMap.get(o.getString("OUTPROPERTYCODE")));
                		//航线
                		String route = "";
                		if(o.getString("INFLTNO2")!= null && o.getString("INFLTNO2").equals(o.getString("OUTFLTNO2"))) {//过站航班
                			route = o.getString("INROUTE3CODE")+"-BAV-"+o.getString("OUTROUTE3CODE");
                		}else {
                			route = "BAV-"+o.getString("OUTROUTE3CODE");
                		}
                		row.createCell(8).setCellValue(route);
                		//航线简称
                		String[] routes = route.split("-");
                		String dim = "DOM";
                		String shortRoute = "";
                		for(int j=0;j<routes.length;j++) {
                			JSONObject airportInfo = JSONObject.parseObject(airportMap.get(routes[j]));
                			if(airportInfo != null && airportInfo.getString("d_or_i") != null && airportInfo.getString("d_or_i").equals("I")) {
                				dim = "INT";
                			}else if(airportInfo != null && airportInfo.getString("d_or_i") != null && airportInfo.getString("d_or_i").equals("R")) {
                				dim = "REG";
                			}
                			if(airportInfo != null && airportInfo.getString("airport_shortname") != null) {
                				if(j == 0) {
                					shortRoute += airportInfo.getString("airport_shortname");
                				}else {
                					shortRoute += "-"+airportInfo.getString("airport_shortname");
                				}
                			}
                		}
                		row.createCell(9).setCellValue(shortRoute);
                		//航线类别
                		row.createCell(10).setCellValue(dim);
                		//航段
                		
                		String rou = rous[q];
                		row.createCell(11).setCellValue("BAV-"+rou);
                		//航段分类
                		String rouDim = "DOM";
                		JSONObject prevInfo = JSONObject.parseObject(airportMap.get(rou));
                		String prevDI = null;
                		if(prevInfo != null) {
                			prevDI = prevInfo.getString("d_or_i");
                		}
                		if(prevDI != null && prevDI.equals("I")) {
                			rouDim = "INT";
                		}else if(prevDI != null && prevDI.equals("R")) {
                			rouDim = "REG";
                		}
                		row.createCell(12).setCellValue(rouDim);
                		//最大业载
                		row.createCell(13).setCellValue(o.getString("OUTMAXFREIGHT"));
                		//最大座位
                		row.createCell(14).setCellValue(o.getString("OUTMAXPASSENGER"));
                		//配额业载
                		row.createCell(15).setCellValue("");
                		//配额座位
                		row.createCell(16).setCellValue("");
                		//可供业载
                		row.createCell(17).setCellValue("");
                		//可供座位
                		row.createCell(18).setCellValue("");
                		//进出港属性
                		row.createCell(19).setCellValue("0102");
                		//架次
                		if(q == 0) {
                			row.createCell(20).setCellValue("1");
                		}else {
                			row.createCell(20).setCellValue("0");
                		}
                		//起降时间
                		row.createCell(21).setCellValue(o.getString("OUTATD")!=null?(o.getString("OUTATD")+":00"):"");
                		//成人数
                		String m = String.valueOf(q+1);
                		row.createCell(22).setCellValue(o.getString("OP"+m));
                		//儿童数
                		row.createCell(23).setCellValue(o.getString("OC"+m));
                		//婴儿数
                		row.createCell(24).setCellValue(o.getString("OI"+m));
                		//过站成人数
                		row.createCell(25).setCellValue(o.getString("OTP"));
                		//过站儿童数
                		row.createCell(26).setCellValue(o.getString("OTC"));
                		//过站婴儿数
                		row.createCell(27).setCellValue(o.getString("OTI"));
                		//成人数后续
                		row.createCell(28).setCellValue("");
                		//儿童数后续
                		row.createCell(29).setCellValue("");
                		//婴儿数后续
                		row.createCell(30).setCellValue("");
                		//货物重量
                		row.createCell(31).setCellValue(o.getString("OH"+m));
                		//邮件重量
                		row.createCell(32).setCellValue(o.getString("OM"+m));
                		//行李重量
                		row.createCell(33).setCellValue(o.getString("OB"+m));
                		//行李件数
                		row.createCell(34).setCellValue("");
                		//过站货物重量
                		row.createCell(35).setCellValue(o.getString("OTH"));
                		//过站邮件重量
                		row.createCell(36).setCellValue(o.getString("OTM"));
                		//过站行李重量
                		row.createCell(37).setCellValue(o.getString("OTB"));
                		//过站行李件数
                		row.createCell(38).setCellValue("");
                		//货物重量后续
                		row.createCell(39).setCellValue("");
                		//邮件重量后续
                		row.createCell(40).setCellValue("");
                		//行李重量后续
                		row.createCell(41).setCellValue("");
                		//行李件数后续
                		row.createCell(42).setCellValue("");
                		//机号
                		row.createCell(43).setCellValue(o.getString("OUTACTNO"));
                		//外籍护照数
                		row.createCell(44).setCellValue("");
            		}
            	}
            }
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error("离港信息总表导出失败",e);
        }
    }

    /**
     * 新增航班
     * @param model
     * @param inFltId
     * @param outFltId
     * @param ifShow
     * @param dataSource新增航班来源（1、页面手动新增 2、来源于空管报文消息）
     * @param sourceId 空管报文ID（dataSource=2时有值）
     * @return
     */
     @RequestMapping(value = "form")
 	public String form(Model model, String inFltId, String outFltId, String ifShow,
 			@RequestParam(required = false) String his,
 			@RequestParam(required = false) String dataSource,
 			@RequestParam(required = false) String sourceId) {
    	// 机场代码(代码使用三字码)、名称
		JSONArray airportCodeSource = cacheService.getOpts("dim_airport", "airport_code","description_cn","icao_code");
		// 航空公司代码(代码使用三字码)、名称
		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname");
		//登机口代码、名称
		JSONArray gateCodeSource = cacheService.getOpts("dim_gate", "gate_code","description_cn");
		// 机号
		JSONArray aircraftNumberSource = cacheService.getOpts("dim_acreg", "acreg_code","acreg_code");
		// 机型
		JSONArray actTypeSource = cacheService.getOpts("dim_actype", "todb_actype_code","todb_actype_code");
        //机位
		JSONArray bayList = cacheService.getOpts("dim_bay", "bay_code","description_cn");
		// 状态
        JSONArray statusSource = cacheService.getCommonDict("acfStatus");
        //要修改取消状态只能通过航班动态列表上方的取消功能
//        for(int i=0;i<statusSource.size();i++){
//        	JSONObject o=statusSource.getJSONObject(i);
//        	if(o.containsKey("text")&&"取消".equals(o.getString("text"))){
//        		o.put("disabled", "true");
//        		break;
//        	}
//        }
        JSONObject jsonobj = new JSONObject();
        jsonobj.put("text", "");
        jsonobj.put("type", "acfStatus");
        jsonobj.put("value", "-1");
        statusSource.add(0,jsonobj);
		// 延误原因
		JSONArray delaySource = cacheService.getOpts("dim_delay", "delay_code","description_cn","delay_type");
		JSONArray alnDelaySource=new JSONArray();
		JSONArray releaseDelaySource=new JSONArray();
		//航班性质
		JSONArray flightPropertyList=cacheService.getOpts("dim_task", "value", "text");
		for (Iterator<Object> iterator = delaySource.iterator(); iterator.hasNext();) { 
	          JSONObject job = (JSONObject) iterator.next(); 
	          if("01".equals(job.get("delay_type"))){
	        	  alnDelaySource.add(job);
	          }else if("02".equals(job.get("delay_type"))){
	        	  releaseDelaySource.add(job);
	          }
		}
		JSONObject emptyObj = new JSONObject();  
		emptyObj.put("delay_type", "01");
		emptyObj.put("id", "");
		emptyObj.put("text", "");
		alnDelaySource.add(0, emptyObj);
		releaseDelaySource.add(0, emptyObj);
		boolean isNew = true;//是否新增
        if(!StringUtils.isEmpty(inFltId)||!StringUtils.isEmpty(outFltId)){
        	isNew = false;
        }
        model.addAttribute("flightPropertyList", flightPropertyList);
        model.addAttribute("statusSource", statusSource);
        model.addAttribute("airportCodeSource",airportCodeSource);
        model.addAttribute("airlinesCodeSource",airlinesCodeSource);
        model.addAttribute("aircraftNumberSource",aircraftNumberSource);
        model.addAttribute("alnDelaySource",alnDelaySource);
        model.addAttribute("releaseDelaySource",releaseDelaySource);
        model.addAttribute("actTypeSource",actTypeSource);
        model.addAttribute("gateCodeSource", gateCodeSource);
        model.addAttribute("bayList", bayList);
        model.addAttribute("currentAirport", Global.getConfig("airport_code3"));//本场
        model.addAttribute("inFltId",inFltId);
        model.addAttribute("outFltId",outFltId);
        model.addAttribute("isNew",isNew);
        model.addAttribute("his",his);
        model.addAttribute("dataSource",dataSource);
        model.addAttribute("sourceId",sourceId);
        return "prss/flightdynamic/flightDynForm";
    }
    /**
     * 航班动态修改页面
     * @param inFltId
     * @param outFltId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "formGridData")
    public JSONArray formGridData(String inFltId,String outFltId,String isHis) {
    	Map<String,String> param = new HashMap<String,String>();
    	JSONArray result = new JSONArray();
    	if(!StringUtils.isEmpty(inFltId)){//进港航班
    		param.put("fltids", inFltId);
    		JSONArray inFlights = new JSONArray();
    		if(isHis != null && "true".equals(isHis)) {
    			inFlights = fdHisService.getFormGridData(param);
    			if(inFlights.size() == 0) {
    				inFlights = fdGridService.getFormGridData(param);
    			}
    		}else {
    			inFlights = fdGridService.getFormGridData(param);
    			if(inFlights.size() == 0) {
    				inFlights = fdHisService.getFormGridData(param);
    			}
    		}
    		if(inFlights!=null&&!inFlights.isEmpty()){
    			result.addAll(inFlights);
    		}
    	}
    	if(!StringUtils.isEmpty(outFltId)){//出港航班
    		param.put("fltids", outFltId);
    		JSONArray outFlights = new JSONArray();
    		if(isHis != null && "true".equals(isHis)) {
    			outFlights = fdHisService.getFormGridData(param);
    			if(outFlights.size() == 0) {
    				outFlights = fdGridService.getFormGridData(param);
    			}
    		}else {
    			outFlights = fdGridService.getFormGridData(param);
    			if(outFlights.size() == 0) {
    				outFlights = fdHisService.getFormGridData(param);
    			}
    		}
    		if(outFlights!=null&&!outFlights.isEmpty()){
    			result.addAll(outFlights);
    		}
    	}
        return result;
    }
   
    public Model selectOptionData(Model mode) {
		// 机场代码(代码使用三字码)、名称
		JSONArray airportCodeSource = cacheService.getOpts("dim_airport", "airport_code","description_cn","icao_code");
		// 航空公司代码(代码使用三字码)、名称
		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname");
		//登机口代码、名称
		JSONArray gateCodeSource = cacheService.getOpts("dim_gate", "gate_code","description_cn");
		// 机号
		JSONArray aircraftNumberSource = cacheService.getOpts("dim_acreg", "acreg_code","acreg_code");
		// 机型
		JSONArray actTypeSource = cacheService.getOpts("dim_actype", "todb_actype_code","todb_actype_code");
        // 延误原因
		JSONArray delaySource = cacheService.getOpts("dim_delay", "delay_code","description_cn","delay_type");
		JSONArray alnDelaySource=new JSONArray();
		JSONArray releaseDelaySource=new JSONArray();
		//航班性质
		JSONArray flightPropertyList=cacheService.getOpts("dim_task", "value", "text");
		for (Iterator<Object> iterator = delaySource.iterator(); iterator.hasNext();) { 
	          JSONObject job = (JSONObject) iterator.next(); 
	          if("01".equals(job.get("delay_type"))){
	        	  alnDelaySource.add(job);
	          }else if("02".equals(job.get("delay_type"))){
	        	  releaseDelaySource.add(job);
	          }
		}
		 // 状态
        JSONArray statusSource = cacheService.getCommonDict("acfStatus");
        mode.addAttribute("flightPropertyList", flightPropertyList);
        mode.addAttribute("statusSource", statusSource);
        mode.addAttribute("airportCodeSource",airportCodeSource);
        mode.addAttribute("airlinesCodeSource",airlinesCodeSource);
        mode.addAttribute("aircraftNumberSource",aircraftNumberSource);
        mode.addAttribute("alnDelaySource",alnDelaySource);
        mode.addAttribute("releaseDelaySource",releaseDelaySource);
        mode.addAttribute("actTypeSource",actTypeSource);
        mode.addAttribute("gateCodeSource", gateCodeSource);
		return mode;
	}
    /**
     * 保存航班动态
     * @param data
     * @param isNew
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public String save(String data,boolean isNew) {
    	data = StringEscapeUtils.unescapeHtml4(data);
    	JSONObject dataObj = JSON.parseObject(data);
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("userId", UserUtils.getUser().getId());
        JSONObject result = fdGridService.saveFlt(dataObj,params);
        return result.toString();
    }

    /**
     * 
     * Discription:是否为手工动态.
     * 
     * @param fltId
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年11月29日 Heqg [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "isByHand")
    public String isByHand(String fltId) {
        if (fdGridService.isByHand(fltId)) {
            return "true";
        }
        return "false";
    }

    @RequestMapping(value = "listDetailInfo")
    public String listDetailInfo(Model model,@RequestParam(value = "infltid",required = true) String infltid,
            @RequestParam(value = "outfltid",required = true) String outfltid) {
        model.addAttribute("infltid", infltid);
        model.addAttribute("outfltid", outfltid);
        return "prss/flightdynamic/flightDynView";
    }

    @RequestMapping(value = "showDetailInfo")
    @ResponseBody
    public JSONArray showDetailInfo(@RequestParam(value = "infltid",required = true) String infltid,
            @RequestParam(value = "outfltid",required = true) String outfltid) {
        Map<String,String> param = new HashMap<String,String>();
        param.put("infltid", infltid);
        param.put("outfltid", outfltid);
        JSONArray result = fdGridService.showDetailInfo(param);
        return result;
    }
    /**
     * 航班取消
     * @param fltId
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cancleFlt")
    public String cancleFlt(String inFltId,String outFltId,String cancleType, String type) {
    	List<Map<String, String>> inObj=fdChangeService.getDataById(inFltId);
    	List<Map<String, String>> outObj=fdChangeService.getDataById(outFltId);
    	String userId=UserUtils.getUser().getId();
        try {
        	if("1".equals(cancleType)){//进港取消
        		fdGridService.cancleFlt(inFltId,type);
        		if(inObj!=null&&!inObj.isEmpty()){
        			String oldCancleType=inObj.get(0).get("CANCEL_TYPE");
        			String oldStatus=inObj.get(0).get("STATUS");
        			updateFltinfoService.insertEvent(inFltId, oldCancleType, type, userId, "FD_FLT_INFO", "CANCEL_TYPE");
        			updateFltinfoService.insertEvent(inFltId, oldStatus, "1", userId, "FD_FLT_INFO", "STATUS");
        		}
        		dispatchActstandService.cancleFlt(inFltId, null, userId);
        	} else if("2".equals(cancleType)){//出港取消
        		fdGridService.cancleFlt(outFltId,type);
        		if(outObj!=null&&!outObj.isEmpty()){
        			String oldCancleType=outObj.get(0).get("CANCEL_TYPE");
        			String oldStatus=outObj.get(0).get("STATUS");
        			updateFltinfoService.insertEvent(outFltId, oldCancleType, type, userId, "FD_FLT_INFO", "CANCEL_TYPE");
        			updateFltinfoService.insertEvent(outFltId, oldStatus, "1", userId, "FD_FLT_INFO", "STATUS");
        		}
        		dispatchActstandService.cancleFlt(null, outFltId, userId);
        	} else if("3".equals(cancleType)){//进出港取消
        		fdGridService.cancleFlt(inFltId,type);
        		fdGridService.cancleFlt(outFltId,type);
        		if(inObj!=null&&!inObj.isEmpty()){
        			String oldCancleType=inObj.get(0).get("CANCEL_TYPE");
        			String oldStatus=inObj.get(0).get("STATUS");
        			updateFltinfoService.insertEvent(inFltId, oldCancleType, type, userId, "FD_FLT_INFO", "CANCEL_TYPE");
        			updateFltinfoService.insertEvent(inFltId, oldStatus, "1", userId, "FD_FLT_INFO", "STATUS");
        		}
        		if(outObj!=null&&!outObj.isEmpty()){
        			String oldCancleType=outObj.get(0).get("CANCEL_TYPE");
        			String oldStatus=outObj.get(0).get("STATUS");
        			updateFltinfoService.insertEvent(outFltId, oldCancleType, type, userId, "FD_FLT_INFO", "CANCEL_TYPE");
        			updateFltinfoService.insertEvent(outFltId, oldStatus, "1", userId, "FD_FLT_INFO", "STATUS");
        		}
        		dispatchActstandService.cancleFlt(inFltId, outFltId, userId);
        	}
        	return "success";
		} catch (Exception e) {
			logger.error(e.toString());
			return "error";
		}
    }
    /**
     * 删除航班
     * @param inFltId
     * @param outFltId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delFlt")
    public String delFlt(String inFltId,String outFltId) {
    	Map<String,String> params = new HashMap<String,String>();
    	String userId=UserUtils.getUser().getId();
    	params.put("inFltId", inFltId);
    	params.put("outFltId", outFltId);
        // 删除
        JSONObject result = fdGridService.delFlt(params);
        if(result!=null&&!result.isEmpty()){
        	try {//清理机位甘特图数据
				if(result.containsKey("status")&&"1".equals(result.getString("status"))){
					dispatchActstandService.deleteBDInfo(inFltId, outFltId, userId);
					String fltids="";
					if(StringUtils.isNotEmpty(inFltId)){
						fltids+=inFltId;
					}
					if(StringUtils.isNotEmpty(outFltId)){
						if(StringUtils.isNotEmpty(fltids)){
							fltids+=","+outFltId;
						}else{
							fltids+=outFltId;
						}
					}
					updateFltinfoService.insertSCHDEvent(fltids, userId, 2);
				}
			} catch (Exception e) {
				result.put("status", "-1");
				result.put("error", e.toString());
				logger.error(e.toString());
			}
        }
        return result.toString();
    }

    /**
     * 
     * Discription:获取单航班甘特图数据.
     * 
     * @param param
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年11月15日 yu-zd [变更描述]
     */
    @RequestMapping(value = "getOneFltData")
    @ResponseBody
    public String getOneFltData(String fltid) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("fltid", fltid);
        String arr = fdGanttService.ganttData(params);
        return arr;
    }

    /**
     * 
     * Discription:单航班甘特图Y轴数据
     * 
     * @param planMain
     * @param planDetail
     * @return:"success"成功；"error"失败
     * @author:yuzd
     * @update:2017年9月8日 yuzd [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "getOneFltGanttY")
    public String getOneFltGanttY(String fltid) {
        JSONArray resultArr = fdGanttService.getOneFltGanttY(fltid);
        return JSON.toJSONString(resultArr, SerializerFeature.WriteMapNullValue);
    }
    /**
     * 
     * Discription:要客页面跳转.
     * 
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年12月17日 Heqg [变更描述]
     */
    @RequestMapping(value = "openImportvip")
    public String openImportvip() {
        return "prss/flightdynamic/fdVipImport";
    }

    @ResponseBody
    @RequestMapping(value = "getVipImportDate")
    public String getVipImportDate() {
        JSONArray json = fdGridService.getVipImportDate();
        String result = json.toJSONString();
        return result;
    }

    @RequestMapping(value = "openVipHis")
    public String openVipHis() {
        return "prss/flightdynamic/fdVipHis";
    }

    @ResponseBody
    @RequestMapping(value = "getVipHisDate")
    public String getVipHisDate(String hisDate) {
        JSONArray json = fdHisService.getVipHis(hisDate);
        String result = json.toJSONString();
        return result;
    }
    /**
     * 
     * Discription:同步要客计划.
     * 
     * @param planMain
     * @param planDetail
     * @return:"success"成功；"error"失败
     * @author:yuzd
     * @update:2017年9月14日 yuzd [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "synchroVip")
    public String synchroVip(FdFltGbak fdMain,String planDetail) {
        return "success";
    }

    /**
     * 
     * Discription:同步要客计划.
     * 
     * @param planMain
     * @param planDetail
     * @return:"success"成功；"error"失败
     * @author:yuzd
     * @update:2017年9月14日 yuzd [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "readVipExcel",method = RequestMethod.POST)
    public String readVipExcel(@RequestParam("file") MultipartFile[] files) {
        String dataArray = "error";
        if (files.length > 0) {
            String fileName = files[0].getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            try {
                String userId = UserUtils.getUser().getId();
                //dataArray = planimportservice.readVip(files[0].getBytes(), fileName, suffix, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataArray;
    }
    /**
     * 
     * Discription:获取下拉选项.
     * 
     * @param model
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年9月20日 SunJ [变更描述]
     */
    private void dimAttributes(Model model) {
        try {
            
            JSONArray fltPropertys = cacheService.getList("dim_task");
            model.addAttribute("fltPropertys", fltPropertys);
            //延误原因
            JSONArray delyReson = cacheService.getOpts("dim_delay","delay_code","description_cn");
            model.addAttribute("delyReson", delyReson);
            // 外航/国内
            JSONArray alnFlags = cacheService.getCommonDict("airline_flag");
            model.addAttribute("alnFlags", alnFlags);
            // 航线性质
            JSONArray alntype = cacheService.getCommonDict("alntype");
            model.addAttribute("alntype", alntype);
            // 机型分类
            JSONArray actkind = cacheService.getList("dim_actkind");
            model.addAttribute("actkinds", actkind);
            //机位
            JSONArray bay = cacheService.getList("dim_bay");
            model.addAttribute("bay", bay);
            //延误原因类型
            JSONArray delay_type = cacheService.getList("dim_delay_type");
            model.addAttribute("delay_type", delay_type);
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
            // 状态
            JSONArray actStatus = cacheService.getCommonDict("acfStatus");
            model.addAttribute("actStatus", actStatus);
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
          
        } catch (Exception e) {
            logger.error("添加下拉框选项失败" + e.getMessage());
        }
    }

    public JSONArray getDimFromOracle(String table,String idcol,String textcol) {
        return gridService.getDimFromOracle(table, idcol, textcol);
    }

    /**
     * 
     * Discription:右键菜单-货邮行.
     * 
     * @param model
     * @return
     * @return:返回值意义
     * @author:yuzd
     * @update:2017年11月4日 yuzd [变更描述]
     */
    @RequestMapping(value = "baggage")
    public String baggage(Model model,String infltid,String outfltid,String his) {
        if (StringUtils.isBlank(his)) {
            if (StringUtils.isNotBlank(infltid)) {
                model.addAttribute("inArray", gridService.getCargoData(infltid));
            }
            if (StringUtils.isNotBlank(outfltid)) {
                model.addAttribute("outArray", gridService.getCargoData(outfltid));
            }
        } else {
            if (StringUtils.isNotBlank(infltid)) {
                model.addAttribute("inArray", fdHisService.getCargoData(infltid));
            }
            if (StringUtils.isNotBlank(outfltid)) {
                model.addAttribute("outArray", fdHisService.getCargoData(outfltid));
            }
        }

        return "prss/flightdynamic/fdbaggage";
    }

    /**
     * 
     * Discription:右键菜单-旅客信息，跳转.
     * 
     * @param model
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年11月4日 Heqg [变更描述]
     */
    @RequestMapping(value = "passenger")
    public String passenger(Model model,String inFltId,String outFltId,String his) {
        model.addAttribute("inFltId", inFltId);
        model.addAttribute("outFltId", outFltId);
        String dOri = "";
        String attrCode = "";
        if (StringUtils.isNotBlank(outFltId) && !"undefined".equals(outFltId)) {
            JSONObject dOriAndAttrCode = new JSONObject();
            if (StringUtils.isBlank(his)) {
                dOriAndAttrCode = fdGridService.dOriAndAttrCode(outFltId);
            } else {
                dOriAndAttrCode = fdHisService.dOriAndAttrCode(outFltId);
            }
            dOri = dOriAndAttrCode.getString("dOri");
            attrCode = dOriAndAttrCode.getString("attrCode");
            String[] string = {"1","2","133","134","135","136","137","138","139","140","158"};
            List<String> colID = Arrays.asList(string);
            JSONObject json = new JSONObject();
            if (StringUtils.isBlank(his)) {
                json = getFltInfoService.getFltInfo(colID, outFltId, null);
            } else {
                json = fltInfoHisService.getFltInfo(colID, outFltId, null);
            }
            json.put("aircraft_number", json.getString("out_aircraft_number"));
            model.addAttribute("passenger", json);
        } else if (StringUtils.isNotBlank(inFltId) && !"undefined".equals(inFltId)) {
            JSONObject dOriAndAttrCode = new JSONObject();
            if (StringUtils.isBlank(his)) {
                dOriAndAttrCode = fdGridService.dOriAndAttrCode(inFltId);
            } else {
                dOriAndAttrCode = fdHisService.dOriAndAttrCode(inFltId);
            }
            dOri = dOriAndAttrCode.getString("dOri");
            attrCode = dOriAndAttrCode.getString("attrCode");
            String[] string = {"1","2","6","133","134","135","136","137","138","139","140"};
            List<String> colID = Arrays.asList(string);
            JSONObject json = new JSONObject();
            if (StringUtils.isBlank(his)) {
                json = getFltInfoService.getFltInfo(colID, inFltId, null);
            } else {
                json = fltInfoHisService.getFltInfo(colID, inFltId, null);
            }
            json.put("aircraft_number", json.getString("in_aircraft_number"));
            model.addAttribute("passenger", json);
        }
        model.addAttribute("attrCode", attrCode);
        if (StringUtils.isBlank(his)) {
            if (StringUtils.isNotBlank(outFltId) && !"undefined".equals(outFltId)) {
                JSONObject nop = fdGridService.getNumOfPassenger(outFltId);
                model.addAttribute("nopO", nop);
            }
            if (StringUtils.isNotBlank(inFltId) && !"undefined".equals(inFltId)) {
                JSONObject nop = fdGridService.getNumOfPassenger(inFltId);
                model.addAttribute("nopI", nop);
            }
            model.addAttribute("dOri", dOri);
            if (dOri.equals("I")) {
                JSONArray transferJson = new JSONArray();
                if (StringUtils.isNotBlank(outFltId) && !"undefined".equals(outFltId)) {
                    transferJson = fdGridService.getTransferInfo(outFltId);
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM21_" + i, fdGridService.getLDMPerson(outFltId, "21", i + ""));
                        model.addAttribute("LDM22_" + i, fdGridService.getLDMPerson(outFltId, "22", i + ""));
                        model.addAttribute("LDM23_" + i, fdGridService.getLDMPerson(outFltId, "23", i + ""));
                    }
                } else if (StringUtils.isNotBlank(inFltId) && !"undefined".equals(inFltId)) {
                    transferJson = fdGridService.getTransferInfo(inFltId);
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM21_" + i, fdGridService.getLDMPerson(inFltId, "21", i + ""));
                        model.addAttribute("LDM22_" + i, fdGridService.getLDMPerson(inFltId, "22", i + ""));
                        model.addAttribute("LDM23_" + i, fdGridService.getLDMPerson(inFltId, "23", i + ""));
                    }
                }
                for (int i = 0; i < transferJson.size(); i++) {
                    model.addAttribute("transfer" + i, transferJson.getJSONObject(i));
                }
                if (StringUtils.isNotBlank(outFltId) && !"undefined".equals(outFltId)) {
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM12_" + i, fdGridService.getLDMPerson(outFltId, "12", i + ""));
                        model.addAttribute("LDM33_" + i, fdGridService.getLDMPerson(outFltId, "33", i + ""));
                        model.addAttribute("LDM34_" + i, fdGridService.getLDMPerson(outFltId, "34", i + ""));
                    }
                    model.addAttribute("plusInfo", fdGridService.getPlusInfo(outFltId));
                }
                if (StringUtils.isNotBlank(inFltId) && !"undefined".equals(inFltId)) {
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM11_" + i, fdGridService.getLDMPerson(inFltId, "11", i + ""));
                        model.addAttribute("LDM31_" + i, fdGridService.getLDMPerson(inFltId, "31", i + ""));
                        model.addAttribute("LDM32_" + i, fdGridService.getLDMPerson(inFltId, "32", i + ""));
                    }
                }
                return "prss/flightdynamic/passengerI";
            } else {
                return "prss/flightdynamic/passengerD";
            }
        } else {
            if (StringUtils.isNotBlank(outFltId) && !"undefined".equals(outFltId)) {
                JSONObject nop = fdHisService.getNumOfPassenger(outFltId);
                model.addAttribute("nopO", nop);
            }
            if (StringUtils.isNotBlank(inFltId) && !"undefined".equals(inFltId)) {
                JSONObject nop = fdHisService.getNumOfPassenger(inFltId);
                model.addAttribute("nopI", nop);
            }
            model.addAttribute("dOri", dOri);
            if (dOri.equals("I")) {
                JSONArray transferJson = new JSONArray();
                if (StringUtils.isNotBlank(outFltId) && !"undefined".equals(outFltId)) {
                    transferJson = fdHisService.getTransferInfo(outFltId);
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM21_" + i, fdHisService.getLDMPerson(outFltId, "21", i + ""));
                        model.addAttribute("LDM22_" + i, fdHisService.getLDMPerson(outFltId, "22", i + ""));
                        model.addAttribute("LDM23_" + i, fdHisService.getLDMPerson(outFltId, "23", i + ""));
                    }
                } else if (StringUtils.isNotBlank(inFltId) && !"undefined".equals(inFltId)) {
                    transferJson = fdHisService.getTransferInfo(inFltId);
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM21_" + i, fdHisService.getLDMPerson(inFltId, "21", i + ""));
                        model.addAttribute("LDM22_" + i, fdHisService.getLDMPerson(inFltId, "22", i + ""));
                        model.addAttribute("LDM23_" + i, fdHisService.getLDMPerson(inFltId, "23", i + ""));
                    }
                }
                for (int i = 0; i < transferJson.size(); i++) {
                    model.addAttribute("transfer" + i, transferJson.getJSONObject(i));
                }
                if (StringUtils.isNotBlank(outFltId) && !"undefined".equals(outFltId)) {
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM12_" + i, fdHisService.getLDMPerson(outFltId, "12", i + ""));
                        model.addAttribute("LDM33_" + i, fdHisService.getLDMPerson(outFltId, "33", i + ""));
                        model.addAttribute("LDM34_" + i, fdHisService.getLDMPerson(outFltId, "34", i + ""));
                    }
                    model.addAttribute("plusInfo", fdHisService.getPlusInfo(outFltId));
                }
                if (StringUtils.isNotBlank(inFltId) && !"undefined".equals(inFltId)) {
                    for (int i = 0; i < 5; i++) {
                        model.addAttribute("LDM11_" + i, fdHisService.getLDMPerson(inFltId, "11", i + ""));
                        model.addAttribute("LDM31_" + i, fdHisService.getLDMPerson(inFltId, "31", i + ""));
                        model.addAttribute("LDM32_" + i, fdHisService.getLDMPerson(inFltId, "32", i + ""));
                    }
                }
                return "prss/flightdynamic/passengerI";
            } else {
                return "prss/flightdynamic/passengerD";
            }
        }
    }

    /**
     * 
     * Discription:右键菜单-时间动态，跳转.
     * 
     * @param model
     * @param id
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年11月6日 Heqg [变更描述]
     * @update:2018年07月10日 wangtg [包头的保障任务节点不固定,没有建立视图和宽表,改成SQL查询]
     */
    @RequestMapping(value = "timeDynamic")
    public String timeDynamic(Model model,String id,String his) {
        String[] string = {"14","15","16","1027","1028","22","23","24","1043","1048","1052","1059","1060","1077",
        		"2662","2663","2664","2665","2667","2668","2669","2670","2671","2672","2673","2674","2675",
        		"2676","2677","2678","2679"};
        List<String> colID = Arrays.asList(string);
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(his)) {
            json = getFltInfoService.getFltInfo(colID, id, null);
        } else {
            json = fltInfoHisService.getFltInfo(colID, id, null);
        }
        model.addAttribute("result", json);
        //获取任务节点时间
        JSONArray jobNodeArr=new JSONArray();
        if (StringUtils.isBlank(his)) {
        	jobNodeArr = getFltInfoService.getJobNode(id);
        } else {
//        	jobNodeArr = fltInfoHisService.getFltInfo(colID3, id, null);
        }
        model.addAttribute("job", jobNodeArr);
        return "prss/flightdynamic/timeDynamic";
    }

    /**
     * 
     * Discription:右键菜单-资源状态，跳转.
     * 
     * @param model
     * @param id
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年11月6日 Heqg [变更描述]
     */
    @RequestMapping(value = "resourseState")
    public String resourseState(Model model,String id,String his) {
//        String[] string = {"6","8","30","57","64","157","163","167","175"};
    	 String[] string = {"6","8","1030","497","499","1034","256","167","175"};
        List<String> colID = Arrays.asList(string);
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(his)) {
            json = getFltInfoService.getFltInfo(colID, id, null);
        } else {
            json = fltInfoHisService.getFltInfo(colID, id, null);
        }
        JSONArray terminal = cacheService.getCommonDict("terminal");
        if (json.containsKey("out_terminal_code")) {
            for (int i = 0; i < terminal.size(); i++) {
                JSONObject stjson = terminal.getJSONObject(i);
                if (stjson.containsValue(json.getString("out_terminal_code"))) {
                    json.put("out_terminal_code", stjson.getString("text"));
                }
            }
        }
        JSONArray status = cacheService.getCommonDict("acfStatus");
        if (json.containsKey("out_status")) {
            for (int i = 0; i < status.size(); i++) {
                JSONObject stjson = status.getJSONObject(i);
                if (stjson.containsValue(json.getString("out_status"))) {
                    json.put("out_status", stjson.getString("text"));
                }
            }
        }
        Map<String,String> delayReason=new HashMap<String,String>();
        Map<String,String> releaseReason=new HashMap<String,String>();
        delayReason=cacheService.getMap("dim_delay_reason_map");
        releaseReason=cacheService.getMap("dim_release_reason_map");
        if (json.containsKey("out_delay_reason")) {
        	if(!StringUtils.isEmpty(json.getString("out_delay_reason"))){
        		json.put("out_delay_reason", delayReason.get(json.getString("out_delay_reason")));
        	}
        }
        
        if (json.containsKey("out_release_reason")) {
        	if(!StringUtils.isEmpty(json.getString("out_release_reason"))){
        		json.put("out_release_reason", releaseReason.get(json.getString("out_release_reason")));
        	}
        }
        model.addAttribute("result", json);
        return "prss/flightdynamic/resourseState";
    }

    /**
     * 
     * Discription:消息变更轨迹.
     * 
     * @param model
     * @param id
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年11月9日 Heqg [变更描述]
     */
    @RequestMapping(value = "massageChange")
    public String massageChange(Model model,String id,String his,String ioFlag) {
        String string1 = "12,13,16,17";
        String string2 = "8,9,21,28,29,31";
        String string3 = "4,5,18,27";
        JSONArray result1 = new JSONArray();
        if (StringUtils.isBlank(his)) {
            result1 = fdGridService.getChg(id, string1);
        } else {
            result1 = fdHisService.getChg(id, string1);
        }
        string1 = "";
        for (int i = 0; i < result1.size(); i++) {
            String newValue = result1.getJSONObject(i).getString("NEW_VALUE") == null ? "空"
                    : result1.getJSONObject(i).getString("NEW_VALUE");
            String oldValue = result1.getJSONObject(i).getString("OLD_VALUE") == null ? "空"
                    : result1.getJSONObject(i).getString("OLD_VALUE");
            string1 += result1.getJSONObject(i).getString("UPDATE_TIME") + "，"
                    + result1.getJSONObject(i).getString("FLIGHT_NUMBER")
                    + result1.getJSONObject(i).getString("COLUMN_CNNAME") + "由" + oldValue + "变更为" + newValue + "。"
                    + result1.getJSONObject(i).getString("UPDATE_USER") + "<br>";
        }
        JSONArray result2 = new JSONArray();
        if (StringUtils.isBlank(his)) {
            result2 = fdGridService.getChg(id, string2);
        } else {
            result2 = fdHisService.getChg(id, string2);
        }
        string2 = "";
        for (int i = 0; i < result2.size(); i++) {
            if ("31".equals(result2.getJSONObject(i).getString("COL_ID"))) {
                String colName = "I".equals(ioFlag) ? "到达口" : "登机口";
                result2.getJSONObject(i).put("COLUMN_CNNAME", colName);
            }
            String newValue = result2.getJSONObject(i).getString("NEW_VALUE") == null ? "空"
                    : result2.getJSONObject(i).getString("NEW_VALUE");
            String oldValue = result2.getJSONObject(i).getString("OLD_VALUE") == null ? "空"
                    : result2.getJSONObject(i).getString("OLD_VALUE");
            string2 += result2.getJSONObject(i).getString("UPDATE_TIME") + "，"
                    + result2.getJSONObject(i).getString("FLIGHT_NUMBER")
                    + result2.getJSONObject(i).getString("COLUMN_CNNAME") + "由"+ oldValue +"变更为" + newValue + "。"
                    + result2.getJSONObject(i).getString("UPDATE_USER") + "<br>";
        }
        JSONArray result3 = new JSONArray();
        if (StringUtils.isBlank(his)) {
            result3 = fdGridService.getChg(id, string3);
        } else {
            result3 = fdHisService.getChg(id, string3);
        }
        string3 = "";
        for (int i = 0; i < result3.size(); i++) {
            if ("18".equals(result3.getJSONObject(i).getString("COL_ID"))) {
                Map<String,String> statusMap = cacheService.getMap("dim_fltstatus_map");
                String acfStatus = statusMap.get(result3.getJSONObject(i).getString("NEW_VALUE"));
                result3.getJSONObject(i).put("NEW_VALUE", acfStatus);
            }
            String newValue = result3.getJSONObject(i).getString("NEW_VALUE") == null ? "空"
                    : result3.getJSONObject(i).getString("NEW_VALUE");
            String oldValue = result3.getJSONObject(i).getString("OLD_VALUE") == null ? "空"
                    : result3.getJSONObject(i).getString("OLD_VALUE");
            string3 += result3.getJSONObject(i).getString("UPDATE_TIME") + "，"
                    + result3.getJSONObject(i).getString("FLIGHT_NUMBER")
                    + result3.getJSONObject(i).getString("COLUMN_CNNAME") + "由"+ oldValue +"变更为" + newValue + "。"
                    + result3.getJSONObject(i).getString("UPDATE_USER") + "<br>";
        }
        model.addAttribute("result1", string1);
        model.addAttribute("result2", string2);
        model.addAttribute("result3", string3);
        return "prss/flightdynamic/massageChange";
    }

    @RequestMapping(value = "delay")
    public String delay(Model model,String fltid, String hisFlag) {
        model.addAttribute("fltid", fltid);
        String airline2Code = fdGridService.getAirline2Code(fltid);
        model.addAttribute("airline", airline2Code);
        JSONArray tmCode = fdGridService.getTmCode(airline2Code);
        JSONObject tmCodeJson = new JSONObject();
        JSONObject tmReasonJson = new JSONObject();
        for (int i = 0; i < tmCode.size(); i++) {
            tmCodeJson.put(tmCode.getJSONObject(i).getString("value"), tmCode.getJSONObject(i).getString("value"));
            tmReasonJson.put(tmCode.getJSONObject(i).getString("value"), tmCode.getJSONObject(i).getString("key"));
        }
        model.addAttribute("tmCode", tmCodeJson);
        model.addAttribute("tmReason", tmReasonJson);
        model.addAttribute("hisFlag", hisFlag);
        JSONArray json = fdGridService.getDelayType();
        JSONObject dimType = new JSONObject();
        for (int i = 0; i < json.size(); i++) {
            dimType.put(json.getJSONObject(i).getString("ID"), json.getJSONObject(i).getString("NAME"));
        }
        model.addAttribute("dimType", json);
        return "prss/flightdynamic/fdDelay";
    }

    @ResponseBody
    @RequestMapping(value = "getDelayCode")
    public String getDelayCode(String airline) {
        JSONArray tmCode = fdGridService.getTmCode(airline);
        JSONObject tmCodeJson = new JSONObject();
        JSONObject tmReasonJson = new JSONObject();
        for (int i = 0; i < tmCode.size(); i++) {
            tmCodeJson.put(tmCode.getJSONObject(i).getString("value"), tmCode.getJSONObject(i).getString("value"));
            tmReasonJson.put(tmCode.getJSONObject(i).getString("value"), tmCode.getJSONObject(i).getString("key"));
        }
        JSONObject json = new JSONObject();
        json.put("tmCode", tmCodeJson);
        json.put("tmReason", tmReasonJson);
        String result = json.toJSONString();
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "getDelayDate")
    public String getDelayDate(String fltid, String hisFlag) {
        JSONArray json = null;
        if(StringUtils.isBlank(hisFlag)){
        	json = fdGridService.getDelayInfo(fltid);
        } else {
        	json = fdHisService.getDelayInfo(fltid);
        }
        String result = json.toJSONString();
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "saveDelay")
    public String saveDelay(String fltid,String delayResult) {
        String userId = UserUtils.getUser().getId();
        JSONArray json = JSONArray.parseArray(StringEscapeUtils.unescapeHtml4(delayResult));
        JSONArray oldJson = fdGridService.getDelayInfo(fltid);
        fdGridService.delDelayInfo(fltid);
        for (int i = 0; i < json.size(); i++) {
            JSONObject jsoni = json.getJSONObject(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("delay_code", jsoni.containsKey("delay_code") ? jsoni.getString("delay_code") : "");
            jsonObject.put("delay_reason", jsoni.containsKey("delay_reason") ? jsoni.getString("delay_reason") : "");
            jsonObject.put("delay_tm", jsoni.containsKey("delay_tm") ? jsoni.getString("delay_tm") : "");
            jsonObject.put("delay_type", jsoni.containsKey("delay_type") ? jsoni.getString("delay_type") : "");
            jsonObject.put("remark", jsoni.containsKey("remark") ? jsoni.getString("remark") : "");
            fdGridService.insertDelayInfo(fltid, jsonObject, userId);
        }
        String menuPath = "航班动态";
        List<OperationLogEntity> operLogs = new ArrayList<OperationLogEntity>();
        Map<String, String> propertyNameMap = new HashMap<String, String>();
        propertyNameMap.put("delay_code", "报文延误代码");
        propertyNameMap.put("delay_reason", "报文延误原因");
        propertyNameMap.put("delay_tm", "延误时间");
        propertyNameMap.put("delay_type", "延误类型");
        propertyNameMap.put("remark", "备注");
        Map<String, JSONObject> newMap = new HashMap<String, JSONObject>();
        for(int i=0; i<json.size(); i++){
        	JSONObject obj = json.getJSONObject(i);
        	if(StringUtils.isBlank(obj.getString("delayId"))){
        		for (Map.Entry<String, Object> entry : obj.entrySet()) {
        			if(propertyNameMap.containsKey(entry.getKey())){
        				OperationLogEntity newDelayCode = new OperationLogEntity(fltid, 1, menuPath, "延误信息录入", propertyNameMap.get(entry.getKey()), "", entry.getValue()!=null?entry.getValue().toString():null, userId);
            			operLogs.add(newDelayCode);
        			}
                }
        		continue;
        	}
        	newMap.put(obj.getString("delayId"), obj);
        }
        Map<String, JSONObject> oldMap = new HashMap<String, JSONObject>();
        for(int i=0; i<oldJson.size(); i++){
        	JSONObject obj = oldJson.getJSONObject(i);
        	oldMap.put(obj.getString("delayId"), obj);
        }
        for(Map.Entry<String, JSONObject> entry : oldMap.entrySet()){
        	if(newMap.get(entry.getKey())==null){
        		for (Map.Entry<String, Object> ent : entry.getValue().entrySet()) {
        			if(propertyNameMap.containsKey(ent.getKey())){
        				OperationLogEntity delDelayCode = new OperationLogEntity(fltid, 3, menuPath, "延误信息录入", propertyNameMap.get(ent.getKey()), ent.getValue()!=null?ent.getValue().toString():null, "", userId);
            			operLogs.add(delDelayCode);
        			}
                }
        	} else {
        		for (Map.Entry<String, Object> ent : entry.getValue().entrySet()) {
        			if(propertyNameMap.containsKey(ent.getKey())){
        				OperationLogEntity updateDelayCode = new OperationLogEntity(fltid, 2, menuPath, "延误信息录入", propertyNameMap.get(ent.getKey()), ent.getValue()!=null?ent.getValue().toString():null, newMap.get(entry.getKey()).getString(ent.getKey()), userId);
            			operLogs.add(updateDelayCode);
        			}
                }
        	}
        }
        LogWriteservice.writeLogBatch(operLogs);
        return "success";
    }

    /**
     * 
     * Discription:异常情况查看跳转.
     * 
     * @param model
     * @param id
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年11月13日 Heqg [变更描述]
     */
    @RequestMapping(value = "error")
    public String error(Model model,String fltid,String hisFlag, String hisDate) {
        model.addAttribute("fltid", fltid == null ? "" : fltid);
        model.addAttribute("hisFlag", hisFlag);
        model.addAttribute("hisDate", hisDate);
        return "prss/flightdynamic/exceptional";
    }

    @ResponseBody
    @RequestMapping(value = "getErrorDate")
    public String getErrorDate(String fltid,String hisFlag,String hisDate, String img,String vol,String vid) {
        String officeId = UserUtils.getUser().getOffice().getId();
        JSONArray json = new JSONArray();
        if (StringUtils.isNotBlank(hisFlag)) {
            json = fdHisService.getExp(officeId, fltid, hisDate, img, vol, vid);
        } else {
            json = fdGridService.getExp(officeId, fltid, img, vol, vid);
        }
        String result = json.toJSONString();
        return result;
    }

    @RequestMapping(value = "downAtta")
    public String downAtta(Model model,String id,String type, String hisFlag, HttpServletRequest request,HttpServletResponse response) {
        /*
         * List<String> fileId = fdGridService.getFileId(id, type); File[] files
         * = new File[fileId.size()]; for (int i = 0; i < fileId.size(); i++) {
         * try { BufferedOutputStream stream = null; byte[] is =
         * fdGridService.downloadFile(fileId.get(i)); String downloadFileName =
         * fdGridService.getFileName(fileId.get(i)); files[i] = new
         * File(downloadFileName); FileOutputStream fstream = new
         * FileOutputStream(files[i]); stream = new
         * BufferedOutputStream(fstream); stream.write(is); stream.close(); }
         * catch (IOException e) { e.printStackTrace(); } } response.reset();//
         * 重设response信息，解决部分浏览器文件名非中文问题 response.setContentType(
         * "application/octet-stream; charset=utf-8");
         * response.setHeader("Content-Disposition",
         * "attachment; filename=exceptional.zip"); try { ZipOutputStream zos =
         * new ZipOutputStream(response.getOutputStream()); zipFile(files,
         * "exceptional.zip", zos); zos.flush(); zos.close(); } catch
         * (IOException e) { e.printStackTrace(); }
         */
    	List<String> fileIds = null;
    	if(StringUtils.isBlank(hisFlag)){
    		fileIds = fdGridService.getFileId(id, type);
    	} else {
    		fileIds = schedulingHisListService.getFileId(id, type);
    	}
        model.addAttribute("type", type);
        model.addAttribute("fileIds", fileIds);
        return "prss/flightdynamic/exceptionalAtta";
    }

    /**
     * 异常附件查看
     * @param model
     * @param id
     * @param type
     * @param request
     * @param response
     */
    @RequestMapping(value = "readExpAtta")
    public void readExpAtta(Model model,String fileId,String type,HttpServletRequest request,
            HttpServletResponse response) {
        BufferedInputStream is = null;
        OutputStream os = null;
        if (StringUtils.isNotEmpty(fileId)) {
            try {
                byte[] data = fileService.doDownLoadFile(fileId);
                byte[] content = new byte[1024];
                is = new BufferedInputStream(new ByteArrayInputStream(data));
                os = response.getOutputStream();
                while (is.read(content) != -1) {
                    os.write(content);
                }
            } catch (Exception e) {
                logger.error("数据流写入失败" + e.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
        }
    }

    @SuppressWarnings("unused")
	private void zipFile(File[] subs,String baseName,ZipOutputStream zos) throws IOException {
        for (int i = 0; i < subs.length; i++) {
            File f = subs[i];
            zos.putNextEntry(new ZipEntry(f.getName()));
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, r);
            }
            fis.close();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getAttrCode")
    public String getAttrCode(String id) {
        return fdGridService.getAttrCode(id);
    }
    /**
     * 
     * Discription:筛选弹出复选框.
     * 
     * @return
     * @return:返回值意义
     * @author:yuzd
     * @update:2017年11月13日 yuzd [变更描述]
     */
    @RequestMapping(value = "openCheck")
    public String openCheck(String type,String selectedId,String selectedText,Model model) {
        JSONObject itemsObj = getOptions(null, "1", "0", type);
        JSONArray resultArr = new JSONArray();
        if (itemsObj.isEmpty()) {
            if ("apron".equals(type)) {
                JSONArray apronArray = cacheService.getList("dim_bay_apron");

                for (int i = 0; i < apronArray.size(); i++) {
                    String apron = apronArray.getJSONObject(i).getString("code");
                    JSONObject apronObj = new JSONObject();
                    apronObj.put("id", apron);
                    apronObj.put("text", apron);
                    resultArr.add(apronObj);
                }

                JSONObject resultObj = getResultWithoutSelected(resultArr, selectedId);
                model.addAttribute("items", resultObj.get("arr"));
                model.addAttribute("sitems", resultObj.get("sArray"));
            }
            if ("aircraftNo".equals(type)) {
           	 JSONArray apronArray = cacheService.getList("dim_acreg");
                for (int i = 0; i < apronArray.size(); i++) {
                    String id = apronArray.getJSONObject(i).getString("acreg_code");
                    String text = apronArray.getJSONObject(i).getString("acreg_code");
                    JSONObject aircraftObj = new JSONObject();
                    aircraftObj.put("id", id);
                    aircraftObj.put("selectText", id+";"+text);
                    aircraftObj.put("text", text);
                    resultArr.add(aircraftObj);
                }

                JSONObject resultObj = getResultWithoutSelected(resultArr, selectedId);
                model.addAttribute("items", resultObj.get("arr"));
                model.addAttribute("sitems", resultObj.get("sArray"));
   		}
           if ("gate".equals(type)) {
           	JSONArray gateArray = cacheService.getList("dim_gate");
           	for (int i = 0; i < gateArray.size(); i++) {
           		String id = gateArray.getJSONObject(i).getString("gate_code");
           		String text = gateArray.getJSONObject(i).getString("description_cn");
           		JSONObject gateObj = new JSONObject();
           		gateObj.put("id", id);
           		gateObj.put("selectText", id+";"+text);
           		gateObj.put("text", text);
           		resultArr.add(gateObj);
           	}
           	
           	JSONObject resultObj = getResultWithoutSelected(resultArr, selectedId);
           	model.addAttribute("items", resultObj.get("arr"));
           	model.addAttribute("sitems", resultObj.get("sArray"));
           }
           if ("airline".equals(type)) {
           	JSONArray airLineArray = cacheService.getOpts("dim_airline", "icao_code","airline_shortname","airline_code");
           	for (int i = 0; i < airLineArray.size(); i++) {
           		String id = airLineArray.getJSONObject(i).getString("id");
           		String alncode = airLineArray.getJSONObject(i).getString("airline_code");
           		String text = airLineArray.getJSONObject(i).getString("text");
           		JSONObject airlinesObj = new JSONObject();
           		airlinesObj.put("id", id);
           		airlinesObj.put("selectText", id+";"+alncode+";"+text);
           		airlinesObj.put("text", text);
           		resultArr.add(airlinesObj);
           	}
           	
           	JSONObject resultObj = getResultWithoutSelected(resultArr, selectedId);
           	model.addAttribute("items", resultObj.get("arr"));
           	model.addAttribute("sitems", resultObj.get("sArray"));
           }
           if ("HHXairline".equals(type)||"GHXairline".equals(type)
        		   ||"GJairline".equals(type)||"QTairline".equals(type)) {
           	JSONArray airLineArray = cacheService.getOpts("dim_airline", "airline_code","airline_code");
           	for (int i = 0; i < airLineArray.size(); i++) {
           		String id = airLineArray.getJSONObject(i).getString("id");
           		JSONObject airlinesObj = new JSONObject();
           		airlinesObj.put("id", id);
           		airlinesObj.put("selectText", id);
           		airlinesObj.put("text", id);
           		resultArr.add(airlinesObj);
           	}
           	
           	JSONObject resultObj = getResultWithoutSelected(resultArr, selectedId);
           	model.addAttribute("items", resultObj.get("arr"));
           	model.addAttribute("sitems", resultObj.get("sArray"));
           }
           if ("HHXalntype".equals(type)||"GHXalntype".equals(type)
        		   ||"GJalntype".equals(type)||"QTalntype".equals(type)) {
        	JSONArray alntypeArr = cacheService.getCommonDict("alntype");
           	for (int i = 0; i < alntypeArr.size(); i++) {
           		String id = alntypeArr.getJSONObject(i).getString("value");
           		String text = alntypeArr.getJSONObject(i).getString("text");
           		JSONObject alntypeObj = new JSONObject();
           		alntypeObj.put("id", id);
           		alntypeObj.put("selectText", id+";"+text);
           		alntypeObj.put("text", text);
           		resultArr.add(alntypeObj);
           	}
           	
           	JSONObject resultObj = getResultWithoutSelected(resultArr, selectedId);
           	model.addAttribute("items", resultObj.get("arr"));
           	model.addAttribute("sitems", resultObj.get("sArray"));
           }
        }       
        else {
            JSONObject resultObj = getResultWithoutSelected(itemsObj.getJSONArray("item"), selectedId);
            model.addAttribute("items", resultObj.get("arr"));
            model.addAttribute("sitems", resultObj.get("sArray"));
        }
        model.addAttribute("type", type);
        model.addAttribute("selectedId", selectedId);
        model.addAttribute("selectedText", selectedText);
        return "prss/flightdynamic/fdFilterCheck";
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
            		if(ids[j].equals(obj.getString("id"))) {
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
     * 外航数据录入
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "fltDataInput")
    public String fltDataInput(Model model,HttpServletRequest request) {
        String fltid = request.getParameter("fltid");// 航班id
        String ioType = request.getParameter("ioType");// 进出港类型
        Map<String,String> params = new HashMap<String,String>();
        params.put("fltid", fltid);
        JSONObject data = fdGridService.getFltTimeData(params);
        model.addAttribute("data", data);
        model.addAttribute("ioType", ioType);
        return "prss/flightdynamic/fltDataInput";
    }

    /**
     * 校验录入机号是否与机型匹配
     * @param model
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "validActNumber")
    public int validActNumber(Model model,String fltid,String actNumber) {
        Map<String,String> param = new HashMap<String,String>();
        param.put("fltid", fltid);
        param.put("actNumber", actNumber);
        return fdGridService.validActNumber(param);
    }

    /**
     * 外航数据录入
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "saveFltData")
    @ResponseBody
    public String saveFltData(HttpServletRequest request) {
        String fltid = request.getParameter("fltid");
        String ioType = request.getParameter("ioType");
        String aircraftNumber = request.getParameter("aircraftNumber");
        Map<String,String> params = new HashMap<String,String>();
        params.put("fltid", fltid);
        params.put("ioType", ioType);
        params.put("aircraftNumber", aircraftNumber);
        JSONObject oldData = fdGridService.getFltTimeData(params);//原始值
        String userId = UserUtils.getUser().getId();
        String menuPath = "航班动态";
        if ("I".equals(ioType)) {// 进港
            String etaDate = request.getParameter("etaDate");
            String eta = request.getParameter("eta");// 报文ETA
            String standDate = request.getParameter("standDate");
            String standTm = request.getParameter("standTm");// 入位时间
            if (StringUtils.isNotEmpty(eta)) {
                eta = etaDate + " " + eta.substring(0, 2) + ":" + eta.substring(2);
            }
            if (StringUtils.isNotEmpty(standTm)) {
                standTm = standDate + " " + standTm.substring(0, 2) + ":" + standTm.substring(2);
            }
            params.put("eta", eta);
            params.put("standTm", standTm);
            fdGridService.saveFltData(params);

            /*增加操作日志*/
            List<OperationLogEntity> operLogs = new ArrayList<OperationLogEntity>();
            //报文ETA
            String oldEta = oldData.getString("eta");
            if (StringUtils.isNotEmpty(oldEta)) {
                oldEta = oldData.getString("etaDate") + " " + oldEta.substring(0, 2) + ":" + oldEta.substring(2);
            }
            OperationLogEntity etaLog = new OperationLogEntity(fltid, 2, menuPath, "外航数据录入", "报文ETA", oldEta, eta,
                    userId);
            operLogs.add(etaLog);
            //入位时间
            String oldStandTm = oldData.getString("standTm");
            if (StringUtils.isNotEmpty(oldStandTm)) {
                oldStandTm = oldData.getString("standDate") + " " + oldStandTm.substring(0, 2) + ":"
                        + oldStandTm.substring(2);
            }
            OperationLogEntity standTmLog = new OperationLogEntity(fltid, 2, menuPath, "外航数据录入", "入位时间", oldStandTm,
                    standTm, userId);
            operLogs.add(standTmLog);
            //机号
            OperationLogEntity actNumberLog = new OperationLogEntity(fltid, 2, menuPath, "外航数据录入", "机号",
                    oldData.getString("aircraftNumber"), aircraftNumber, userId);
            operLogs.add(actNumberLog);
            LogWriteservice.writeLogBatch(operLogs);
        } else {// 出港
            String relsStandDate = request.getParameter("relsStandDate");
            String relsStandTm = request.getParameter("relsStandTm");// 离位时间
            String htchCloDate = request.getParameter("htchCloDate");
            String htchCloTm = request.getParameter("htchCloTm");// 客舱门关闭
            if (StringUtils.isNotEmpty(relsStandTm)) {
                relsStandTm = relsStandDate + " " + relsStandTm.substring(0, 2) + ":" + relsStandTm.substring(2);
            }
            if (StringUtils.isNotEmpty(htchCloTm)) {
                htchCloTm = htchCloDate + " " + htchCloTm.substring(0, 2) + ":" + htchCloTm.substring(2);
            }
            params.put("relsStandTm", relsStandTm);
            params.put("htchCloTm", htchCloTm);
            fdGridService.saveFltData(params);

            /*增加操作日志*/
            List<OperationLogEntity> operLogs = new ArrayList<OperationLogEntity>();
            //离位时间
            String oldRelsStandTm = oldData.getString("relsStandTm");
            if (StringUtils.isNotEmpty(oldRelsStandTm)) {
                oldRelsStandTm = oldData.getString("relsStandDate") + " " + oldRelsStandTm.substring(0, 2) + ":"
                        + oldRelsStandTm.substring(2);
            }
            OperationLogEntity relsStandTmLog = new OperationLogEntity(fltid, 2, menuPath, "外航数据录入", "离位时间",
                    oldRelsStandTm, relsStandTm, userId);
            operLogs.add(relsStandTmLog);
            //入位时间
            String oldHtchCloTm = oldData.getString("htchCloTm");
            if (StringUtils.isNotEmpty(oldHtchCloTm)) {
                oldHtchCloTm = oldData.getString("htchCloDate") + " " + oldHtchCloTm.substring(0, 2) + ":"
                        + oldHtchCloTm.substring(2);
            }
            OperationLogEntity htchCloTmLog = new OperationLogEntity(fltid, 2, menuPath, "外航数据录入", "客舱门关闭",
                    oldHtchCloTm, htchCloTm, userId);
            operLogs.add(htchCloTmLog);
            //机号
            OperationLogEntity actNumberLog = new OperationLogEntity(fltid, 2, menuPath, "外航数据录入", "机号",
                    oldData.getString("aircraftNumber"), aircraftNumber, userId);
            operLogs.add(actNumberLog);
            LogWriteservice.writeLogBatch(operLogs);
        }
        return "succeed";
    }

    /**
     * 
     * Discription:要客详情跳转.
     * 
     * @param model
     * @param inFltId
     * @param outFltId
     * @return
     * @return:返回值意义
     * @author:Heqg
     * @update:2017年12月16日 Heqg [变更描述]
     */
    @RequestMapping(value = "vipInfo")
    public String vipInfo(Model model,String inFltId,String outFltId,String inFltNo,String outFltNo,String hisFlag) {
        model.addAttribute("inFltid", inFltId);
        model.addAttribute("outFltid", outFltId);
        model.addAttribute("inFltNo", inFltNo);
        model.addAttribute("outFltNo", outFltNo);
        JSONObject ioFlag = new JSONObject();
        if (StringUtils.isNotBlank(inFltId)) {
            ioFlag.put("A", "进港");
        }
        if (StringUtils.isNotBlank(outFltId)) {
            ioFlag.put("D", "出港");
        }
        model.addAttribute("ioFlag", ioFlag);
        JSONArray json = fdGridService.getVipFlag();
        JSONObject vipFlag = new JSONObject();
        for (int i = 0; i < json.size(); i++) {
            JSONObject jsoni = json.getJSONObject(i);
            vipFlag.put(jsoni.getString("id"), jsoni.getString("text"));
        }
        model.addAttribute("vipFlag", vipFlag);
        model.addAttribute("hisFlag", hisFlag);
        return "prss/flightdynamic/fdVipInfo";
    }

    @ResponseBody
    @RequestMapping(value = "getVipDate")
    public String getVipDate(String inFltid,String outFltid) {
        String fltid = "";
        if (StringUtils.isNotBlank(inFltid) && !"undefined".equals(inFltid)) {
            fltid += inFltid + ",";
        }
        if (StringUtils.isNotBlank(outFltid) && !"undefined".equals(outFltid)) {
            fltid += outFltid + ",";
        }
        fltid = fltid.substring(0, fltid.length() - 1);
        JSONArray json = fdGridService.getVipInfo(fltid);
        String result = json.toJSONString();
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "saveVipInfo")
    public String saveVipInfo(String inFltId,String outFltId,String vipResult) {
        String userId = UserUtils.getUser().getId();
        JSONArray json = JSONArray.parseArray(StringEscapeUtils.unescapeHtml4(vipResult));
        String fltid = "";
        if (StringUtils.isNotBlank(inFltId)) {
            fltid += inFltId + ",";
        }
        if (StringUtils.isNotBlank(outFltId)) {
            fltid += outFltId + ",";
        }
        fltid = fltid.substring(0, fltid.length() - 1);
        JSONArray oldJson = fdGridService.getVipInfo(fltid);
        fdGridService.delVipInfo(fltid);
        for (int i = 0; i < json.size(); i++) {
            JSONObject jsoni = json.getJSONObject(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("flightNumber", jsoni.containsKey("flightNumber") ? jsoni.getString("flightNumber") : "");
            jsonObject.put("vipFlag", jsoni.containsKey("vipFlag") ? jsoni.getString("vipFlag") : "");
            jsonObject.put("ioFlag", jsoni.containsKey("ioFlag") ? jsoni.getString("ioFlag") : "");
            jsonObject.put("vipInfo", jsoni.containsKey("vipInfo") ? jsoni.getString("vipInfo") : "");
            fdGridService.insertVipInfo(jsonObject.getString("ioFlag").equals("A") ? inFltId : outFltId, jsonObject,
                    userId);
        }
        
        String menuPath = "航班动态";
        List<OperationLogEntity> operLogs = new ArrayList<OperationLogEntity>();
        Map<String, String> propertyNameMap = new HashMap<String, String>();
        propertyNameMap.put("vipFlag", "要客标识");
        propertyNameMap.put("vipInfo", "要客详情");
        Map<String, JSONObject> newMap = new HashMap<String, JSONObject>();
        for(int i=0; i<json.size(); i++){
        	JSONObject obj = json.getJSONObject(i);
        	if(StringUtils.isBlank(obj.getString("vipId"))){
        		String logFltid = "";
        		if(obj.getString("ioFlag").equals("A")){
        			logFltid = inFltId;
        		} else if(obj.getString("ioFlag").equals("D")){
        			logFltid = outFltId;
        		}
        		for (Map.Entry<String, Object> entry : obj.entrySet()) {
        			if(propertyNameMap.containsKey(entry.getKey())){
        				OperationLogEntity newVipInfo = new OperationLogEntity(logFltid, 1, menuPath, "要客详情", propertyNameMap.get(entry.getKey()), "", entry.getValue()!=null?entry.getValue().toString():null, userId);
            			operLogs.add(newVipInfo);
        			}
                }
        		continue;
        	}
        	newMap.put(obj.getString("vipId"), obj);
        }
        Map<String, JSONObject> oldMap = new HashMap<String, JSONObject>();
        for(int i=0; i<oldJson.size(); i++){
        	JSONObject obj = oldJson.getJSONObject(i);
        	oldMap.put(obj.getString("vipId"), obj);
        }
        for(Map.Entry<String, JSONObject> entry : oldMap.entrySet()){
        	String logFltid = "";
    		if(entry.getValue().getString("ioFlag").equals("A")){
    			logFltid = inFltId;
    		} else if(entry.getValue().getString("ioFlag").equals("D")){
    			logFltid = outFltId;
    		}
        	if(newMap.get(entry.getKey())==null){
        		for (Map.Entry<String, Object> ent : entry.getValue().entrySet()) {
        			if(propertyNameMap.containsKey(ent.getKey())){
        				OperationLogEntity delVipInfo = new OperationLogEntity(logFltid, 3, menuPath, "要客详情", propertyNameMap.get(ent.getKey()), ent.getValue()!=null?ent.getValue().toString():null, "", userId);
            			operLogs.add(delVipInfo);
        			}
                }
        	} else {
        		if(newMap.get(entry.getKey()).getString("ioFlag").equals(entry.getValue().getString("ioFlag"))){
        			for (Map.Entry<String, Object> ent : entry.getValue().entrySet()) {
            			if(propertyNameMap.containsKey(ent.getKey())){
            				OperationLogEntity updateVipInfo = new OperationLogEntity(logFltid, 2, menuPath, "要客详情", propertyNameMap.get(ent.getKey()), ent.getValue()!=null?ent.getValue().toString():null, newMap.get(entry.getKey()).getString(ent.getKey()), userId);
                			operLogs.add(updateVipInfo);
            			}
                    }
        		} else {
        			OperationLogEntity delVipFlag = new OperationLogEntity(entry.getValue().getString("ioFlag").equals("A") ? inFltId : outFltId, 3, menuPath, "要客详情", "要客标识", entry.getValue().getString("vipFlag"), "", userId);
        			operLogs.add(delVipFlag);
        			OperationLogEntity delVipInfo = new OperationLogEntity(entry.getValue().getString("ioFlag").equals("A") ? inFltId : outFltId, 3, menuPath, "要客详情", "要客详情", entry.getValue().getString("vipInfo"), "", userId);
        			operLogs.add(delVipInfo);
        			OperationLogEntity addVipFlag = new OperationLogEntity(newMap.get(entry.getKey()).getString("ioFlag").equals("A") ? inFltId : outFltId, 1, menuPath, "要客详情", "要客标识", "", newMap.get(entry.getKey()).getString("vipFlag"), userId);
        			operLogs.add(addVipFlag);
        			OperationLogEntity addVipInfo = new OperationLogEntity(newMap.get(entry.getKey()).getString("ioFlag").equals("A") ? inFltId : outFltId, 1, menuPath, "要客详情", "要客详情", "", newMap.get(entry.getKey()).getString("vipInfo"), userId);
        			operLogs.add(addVipInfo);
        			
        		}
        	}
        }
        LogWriteservice.writeLogBatch(operLogs);
        return "success";
    }

    /**
     * 
     *Discription:跳转到报文.
     *@param request
     *@param response
     *@param model
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月15日 neusoft [变更描述]
     */
    @RequestMapping(value = "telegraph")
    public String telegraph(HttpServletRequest request,HttpServletResponse response,Model model) {
        model.addAttribute("isHis", request.getParameter("isHis"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        model.addAttribute("date", date);
        return "prss/flightdynamic/fdTelegraphList";
    }

    /**
     * 
     *Discription:报文列表数据查询.
     *@param pageSize
     *@param pageNumber
     *@param mflightdate
     *@param flightnumber
     *@param isHis
     *@param sortOrder
     *@param sortName
     *@param log
     *@param request
     *@param response
     *@param model
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月15日 neusoft [变更描述]
     */
    @RequestMapping(value = "fdTelegraphList")
    @ResponseBody
    public Map<String,Object> fdTelegraphList(int pageSize,int pageNumber,String search,int isHis,
            String sortOrder,String sortName,Log log,HttpServletRequest request,
            HttpServletResponse response,Model model) {
        Map<String,String> param = new HashMap<String,String>();
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        param.put("begin", String.valueOf(begin));
        param.put("end", String.valueOf(end));
        param.put("sortOrder", sortOrder);
        param.put("sortName", sortName);
        param.put("search", search);
        if(isHis == 0){
            return fdGridService.getTelegraphList(param);
        } else {
            String beginTime = request.getParameter("beginTime");
            String endTime = request.getParameter("endTime");
            if(StringUtils.isNotBlank(beginTime))
            	beginTime = beginTime.replace("-","");
            if(StringUtils.isNotBlank(endTime))
            	endTime = endTime.replace("-","");
            param.put("beginTime", beginTime);//发送时间开始
            param.put("endTime", endTime);//发送时间结束
            return fdHisService.getTelegraphList(param);//1 历史  
        }

    }
    
   /**
    * 
    *Discription:获取报文详细信息.
    *@param id
    *@return
    *@return:返回值意义
    *@author:l.ran@neusoft.com
    *@update:2018年5月15日 neusoft [变更描述]
    */
    @RequestMapping(value = "getTelegraphInfo")
    public String getTelegraphInfo(String id,int isHis,Model model) {
        Map<String,String> param = new HashMap<String,String>();
        param.put("id", id);
        JSONObject info = new JSONObject();
        if(isHis == 0){
            info = fdGridService.getTelegraphInfo(param);
        } else {
            info = fdHisService.getTelegraphInfo(param);
        }
        model.addAttribute("info", info);
        return "prss/flightdynamic/fdTelegraphInfo";
    }
    /**
     * 根据机号获取机型
     * @param aftNum
     * @return
     */
    @RequestMapping(value = "getActType")
    @ResponseBody
    public String getActType(String aftNum){
    	 String res = cacheService.mapGet("dim_acreg_map", aftNum);
    	 if(!StringUtils.isEmpty(res)){
    		 return JSON.parseObject(res).getString("actype_code");
    	 } else {
    		 return "";
    	 }
    }
    /**
     * 校验航班号
     * @param fltNo 航班号
     * @param aircraftNo 机号
     * @return
     */
    @RequestMapping(value = "getAlnCode")
    @ResponseBody
    public String getAlnCode(String fltNo,String aircraftNo){
    	String result = "";
    	//根据航班号获取航空公司
    	if(!StringUtils.isEmpty(fltNo)&&fltNo.length()>=3){
    		String alnCode = fltNo.substring(0,3).toUpperCase();
    		String res = cacheService.mapGet("dim_airline_map", alnCode);
        	if(!StringUtils.isEmpty(res)){
        		result = alnCode;
        	}
    	}
    	//根据机号获取航空公司
    	if(!StringUtils.isEmpty(aircraftNo)){
    		String res = cacheService.mapGet("dim_acreg_map", aircraftNo.toUpperCase());
        	if(!StringUtils.isEmpty(res)){
        		result = JSON.parseObject(res).getString("airline_code");
        	}
    	}
    	return result;
    }
    
    /**
   	 * 根据航班号获取共享航班号
   	 * @param fltNo 航班号
   	 * @return
   	 */
   	@RequestMapping(value = "getShareFltNo")
   	@ResponseBody
	public String getShareFltNo(String fltNo) {
   		String res = cacheService.mapGet("dim_share_fltno_map", fltNo==null?"":fltNo.toUpperCase());
   		if(res!=null)
   			return res.toUpperCase();
   		return "";
   	}
    
    /**
     * 根据机型、目标机场、预起ETD时间获取计算的预落值
     * @param actType 机型
     * @param aptCode 对端机场
     * @param fltDate 航班日期yyyyMMddHHmm+
     * @param etd 预起时间
     * @return
     */
	@RequestMapping(value = "getEta")
    @ResponseBody
    public String getEta(String actType,String aptCode,String fltDate,String etd) {
		String eta = "";
		if(StringUtils.isNotBlank(etd)&&StringUtils.isNotBlank(fltDate)&&StringUtils.isNotBlank(aptCode)){
			String inTime = "";
			String tmpTime = "";
			if(etd.contains("+")){
				inTime = getAddDay(fltDate+etd.replace("+", ""), "yyyyMMddHHmm","yyyy-MM-dd HH:mm", +1);
				tmpTime = getAddDay(fltDate+etd.replace("+", ""), "yyyyMMddHHmm","", +1);
			}else{
				inTime = getAddDay(fltDate+etd, "yyyyMMddHHmm","yyyy-MM-dd HH:mm", 0);
				tmpTime = getAddDay(fltDate+etd, "yyyyMMddHHmm","", 0);
			}
			eta = flightTimeService.calculateETA(inTime, actType, aptCode,"yyyyMMddHHmm");
			if(StringUtils.isNotBlank(eta)){
				int intTmpTime = Integer.parseInt(tmpTime.substring(0, 8));
				int intOutTime = Integer.parseInt(eta.substring(0, 8));
				if(intOutTime>intTmpTime){
					//如果返回日期大于传入日期，添加"+"号
					eta = eta.substring(8, 12)+"+";
				}else{
					eta = eta.substring(8, 12);
				}
			}else{
				eta = "";
			}
		}
		return eta;
    }
	/**
	 * 获取日期加减天后日期字符串
	 * @param dateStr
	 * @param patterns
	 * @param days
	 * @return
	 */
	private String getAddDay(String dateStr,String inPatterns,String outPatterns,int days){
		SimpleDateFormat fmIn = new SimpleDateFormat(inPatterns);
		SimpleDateFormat fmOut = new SimpleDateFormat(outPatterns);
		String re = "";
		try {
			Date date = null;
			if(dateStr.equals("")){
				date = new Date();
			}else{
				date = fmIn.parse(dateStr);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, days);
			if(outPatterns!=null&&!outPatterns.equals("")){
				re = fmOut.format(cal.getTime());
			}else{
				re = fmIn.format(cal.getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re;
	}
	/**
	 * 校验航班是否存在
	 * @param departApt 起场
	 * @param arriveApt 落场
	 * @param fltNo 航班号
	 * @param fltDate 航班日期
	 * @return
	 */
	@RequestMapping(value = "isFltExist")
    @ResponseBody
	public String isFltExist(String departApt, String arriveApt, String fltNo, String fltDate) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("departApt", departApt);
		param.put("arriveApt", arriveApt);
		param.put("fltNo", fltNo);
		param.put("fltDate", fltDate);
		int count = fdGridService.isFltExist(param);
		return String.valueOf(count);
	}
	
	/**
     * 返回值机岛配置页面
     * @param model
     * @return
     */
    @RequestMapping(value = "counterDefultAllotList")
    public String counterDefultAllotList(String dim,String island,Model model) {
    	JSONArray islands = fdGridService.getIslands();
    	model.addAttribute("islands", islands);
    	JSONArray counters = fdGridService.getCounters();
    	model.addAttribute("counters", counters);
    	JSONObject res = getIslandAllot(dim,island);
    	model.addAttribute("choose", res.getJSONArray("choose"));
    	model.addAttribute("choosed", res.getJSONArray("choosed"));
    	model.addAttribute("dim", dim);
    	model.addAttribute("island", island);
        return "prss/flightdynamic/counterDefultAllot";
    }
    
    /**
	 * 获取值机岛配置
	 * @param dim
	 * @param island
	 * @return
	 */
	@RequestMapping(value = "getIslandAllot")
    @ResponseBody
	public JSONObject getIslandAllot(String dim, String island) {
		JSONObject res = new JSONObject();
		String param = "I".equals(dim)?"":dim;
		JSONArray airlines = fdGridService.getAirlines(param);
    	Map<String,JSONObject> airlineMap = new HashMap<String,JSONObject>();
    	for(int j=0;j<airlines.size();j++) {
    		airlineMap.put(airlines.getJSONObject(j).getString("code"), airlines.getJSONObject(j));
    	}
    	String choosedString = fdGridService.getChoosedAirlines(dim,island);
    	JSONArray choose = new JSONArray();
    	JSONArray choosed = new JSONArray();
    	if(choosedString != null) {
    		String[] choosedCode = choosedString.split(",");
    		for(int i=0;i<choosedCode.length;i++) {
        		if(airlineMap.get(choosedCode[i]) != null) {
        			choosed.add(airlineMap.get(choosedCode[i]));
        			airlineMap.remove(choosedCode[i]);
        		}
        	}
    	}
    	for(String m : airlineMap.keySet()) {
    		choose.add(airlineMap.get(m));
    	}
    	res.put("choose", choose);
    	res.put("choosed", choosed);
    	return res;
	}
	
	/**
	 * 保存值机岛设置
	 * @param dim
	 * @param island
	 * @param airlines
	 * @return
	 */
	@RequestMapping(value = "saveIslandAllot")
    @ResponseBody
	public String saveIslandAllot(String dim, String island, String airlines) {
		try {
			String user = UserUtils.getUser().getId();
			fdGridService.saveIslandAllot(dim,island,airlines,user);
			return "success";
		} catch (Exception e) {
			logger.error("保存值机岛配置失败",e);
			return e.toString();
		}
	}
	
	@RequestMapping(value = "saveMIslandAllot")
    @ResponseBody
	public String saveMIslandAllot(String data,String delStr) {
		data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray dataArray = JSONArray.parseArray(data);
        String user = UserUtils.getUser().getId();
		try {
			fdGridService.saveMIslandAllot(dataArray,delStr,user);
			return "success";
		} catch (Exception e) {
			logger.error("保存值机岛配置失败",e);
			return e.toString();
		}
	}
	
	/**
	 * 获取混装航班值机柜台设置
	 * @param dim
	 * @param island
	 * @param airlines
	 * @return
	 */
	@RequestMapping(value = "getMData")
    @ResponseBody
	public JSONArray getMData() {
		return fdGridService.getMData();
	}
	
	/**
     * 返回值机岛配置页面
     * @param model
     * @return
     */
    @RequestMapping(value = "counterDefultAllotTable")
    public String counterDefultAllotTable(Model model) {
        return "prss/flightdynamic/counterDefultAllotTable";
    }
    
    @RequestMapping(value = "getCounterAllotTable")
    @ResponseBody
	public JSONArray getCounterAllotTable() {
		return fdGridService.getCounterAllotTable();
	}
    
    
    @RequestMapping(value = "deleteCounterAllotById")
    @ResponseBody
	public String deleteCounterAllotById(String ids) {
		try {
			fdGridService.deleteCounterAllotById(ids);
			return "success";
		} catch (Exception e) {
			logger.error("值机柜台配置删除失败",e);
			e.printStackTrace();
			return e.getMessage();
		}
	}
    
    /**
     * 打印配载初始航班、登机口信息
     */
    @ResponseBody
	@RequestMapping(value = "exportInitialInfo")
	public void exportInitialInfo(HttpServletRequest request,HttpServletResponse response) throws URISyntaxException{
    	//导出初始航班txt：initialFlt 初始登机口txt：initialGate
		String exportType=request.getParameter("exportType");
		String outFltIds=request.getParameter("outFltIds");
		Map<String,Object> paramMap = new HashMap<String,Object>();
    	try {
    		if(outFltIds!=null&&!outFltIds.equals("")){
    			//初始登机口模板
    			String[] gateTemplet = {"<SOE>FU {flightNumber2}/./HET/GATE/{gate}<XMIT>"};
    			//初始航班模板，进港航班号与出港航班号相同
    			String[] fltTemplet = {"<SOE>FU {flightNumber2}/+/HET/ETD/{etd}<XMIT>","<SOE>BKO {flightNumber2}/+/HET/A<XMIT>"};
    			//初始登机口模板,进港航班号与出港航班号不同，或只有出港航班号
    			String[] fltTemplet1 = {"<SOE>FU {flightNumber2}/+/HET/CTN/{tableNo}/ARN/{acregCode}<XMIT>","<SOE>FU {flightNumber2}/+/HET/ETD/{etd}<XMIT>","<SOE>IF {flightNumber2}/+<XMIT>","<SOE>BKO {flightNumber2}/+/HET/A<XMIT>"};
    			
    			//导出文件名
    			String fileName = "";
    			if(exportType.equals("initialFlt")){
    				fileName = "初始航班" + DateUtils.getDate("yyyyMMdd") + ".txt";
    			}else{
    				fileName = "初始登机口" + DateUtils.getDate("yyyyMMdd") + ".txt";
    			}
    			//查询需要导出的出港航班数据
            	paramMap.put("outFltIds", outFltIds);
            	JSONArray dataArr = fdGridService.getOutFltInfo(paramMap);
            	//处理出港航班信息为字节数组
            	StringBuffer sb = new StringBuffer();
            	if(dataArr!=null&&dataArr.size()>0){
            		for(int i=0;i<dataArr.size();i++){
                		JSONObject data = dataArr.getJSONObject(i);
                		//二字码航班号
                		String inFlightNumber2 = data.getString("inFlightNumber2");
                		String outFlightNumber2 = data.getString("outFlightNumber2");
                		if(outFlightNumber2==null){
                			outFlightNumber2 = "";
                		}
                		//机号
                		String outAircraftNumber = data.getString("outAircraftNumber");
                		if(outAircraftNumber==null){
                			outAircraftNumber = "";
                		}
                		//登机口
                		String gate = data.getString("gate");
                		if(gate==null){
                			gate = "";
                		}
                		//预起
                		String etd = data.getString("etd");
                		if(etd==null){
                			etd = "";
                		}
                		//表号
                		String tableNo = data.getString("tableNo");
                		if(tableNo==null||tableNo.equals("")){
                			tableNo = "XXX";
                		}
                		//换行
                		String xx = "\r\n";
                		if(exportType.equals("initialGate")){
                			String info = gateTemplet[0];
            				info = info.replace("{flightNumber2}", outFlightNumber2);
            				info = info.replace("{gate}", gate);
            				sb.append(info).append(xx);	
                		}else if(inFlightNumber2!=null&&inFlightNumber2.equals(outFlightNumber2)){
                			String info = fltTemplet[0];
            				info = info.replace("{flightNumber2}", outFlightNumber2);
            				info = info.replace("{etd}", etd);
            				
            				String info1 = fltTemplet[1];
            				info1 = info1.replace("{flightNumber2}", outFlightNumber2);
            				sb.append(info).append(xx).append(info1).append(xx);
                		}else{
                			String info = fltTemplet1[0];
            				info = info.replace("{flightNumber2}", outFlightNumber2);
            				info = info.replace("{tableNo}", tableNo);
            				info = info.replace("{acregCode}", outAircraftNumber);
            				
            				String info1 = fltTemplet1[1];
            				info1 = info1.replace("{flightNumber2}", outFlightNumber2);
            				info1 = info1.replace("{etd}", etd);
            				
            				String info2 = fltTemplet1[2];
            				info2 = info2.replace("{flightNumber2}", outFlightNumber2);
            				
            				String info3 = fltTemplet1[3];
            				info3 = info3.replace("{flightNumber2}", outFlightNumber2);
            				sb.append(info).append(xx).append(info1).append(xx).append(info2).append(xx).append(info3).append(xx);
                		}
                	}
            	}
            	
            	byte[] content = sb.toString().getBytes("utf-8");
       			String downloadFileName = setHeader(request, response, fileName);
    			response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
    			response.getOutputStream().write(content);
                response.getOutputStream().flush();
    		}
		} catch (Exception e) {
			logger.error("导出初始航班、登机口信息失败" + e.getMessage());
		}
	}
	
    private String setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		String downloadFileName = "";
		try {
			response.reset();
	        response.setContentType("application/octet-stream; charset=utf-8");
	        String agent = (String) request.getHeader("USER-AGENT");
	        if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
	            downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
	        } else {
	            downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
		}catch(Exception e){
			logger.error("set header error:"+e.toString());
		}
		return downloadFileName;
	}
}
