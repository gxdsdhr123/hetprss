/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月27日 下午6:35:22
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.neusoft.framework.common.persistence.Page;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.flightdynamic.entity.ExportFDExcel;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.flightdynamic.service.FdHisService;
import com.neusoft.prss.flightdynamic.service.FdHistoricalService;
import com.neusoft.prss.grid.entity.GridColumn;
import com.neusoft.prss.grid.service.GridColumnService;

@Controller
@RequestMapping(value = "${adminPath}/fdHistorical")
public class FdHistoricalController extends BaseController {
    @Resource
    private FdHistoricalService fdHistoricalService;

    @Resource
    private CacheService cacheService;

    @Resource
    private FdHisService fdHisService;

    @Resource
    private GridColumnService gridService;

    /**
     * 
     * Discription:获取航班动态页面.
     * 
     * @return
     * @return:历史航班动态页面
     * @author:yuzd
     * @update:2017年9月18日 yuzd [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        Page<T> page = new Page<>();
        String pageHtml = page.toString();
        model.addAttribute("pageHtml", pageHtml);
        dimAttributes(model);
        return "prss/flightdynamic/fdHistoricalList";
    }

    /**
     * 
     * Discription:查看时判断gbak是否存在.
     * 
     * @param fltid
     * @param suffix
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月18日 SunJ [变更描述]
     */
    @RequestMapping(value = "ifGbakExist")
    @ResponseBody
    public String ifGbakExist(String fltid,String suffix) {
      return null;
    }

    /**
     * 
     * Discription:根据fltid获取详细航程内容.
     * 
     * @param model
     * @param id
     * @param type
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "getFdById")
    public String getFdById(Model model,@RequestParam(value = "id",required = true) String id,
            @RequestParam(value = "type",required = true) String type,String suffix) {
        return "prss/flightdynamic/flightDynForm";
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
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("q", q);
        param.put("start", start);
        param.put("end", end);
        if ("airline".equals(type)) {
            int count = gridService.getAirlineTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray airlines = gridService.getAirlines(param);
                result.put("item", airlines);
            } else {
                result.put("item", "[]");
            }
        } else if ("actType".equals(type)) {
            int count = gridService.getActTypeTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray actTypes = gridService.getActTypes(param);
                result.put("item", actTypes);
            } else {
                result.put("item", "[]");
            }
        } else if ("airport".equals(type)) {
            int count = gridService.getAirportTotNum(param);
            result.put("count", count);
            if (count != 0) {
                JSONArray airports = gridService.getAirports(param);
                result.put("item", airports);
            } else {
                result.put("item", "[]");
            }
        }
        return result;
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
    public JSONObject getDynamic(String schema,String param,
            String pageNumber,String pageSize,String sortName,String sortOrder) {
        JSONObject resObj = new JSONObject();
        param = StringEscapeUtils.unescapeHtml4(param);
        Map<String,Object> params = JSON.parseObject(param);
        try {
            if (params != null&&params.entrySet().size()>0) {
                //排序字段名
                params.put("sortName", sortName);
                //排序方式
                params.put("sortOrder", sortOrder);
                //用户名
                params.put("userId", UserUtils.getUser().getId());
                //历史动态列配置类型值 schema=3
                params.put("schema", schema);
                //分页参数
                if (StringUtils.isNotBlank(pageNumber) && StringUtils.isNotBlank(pageSize)) {
                	int intPageNum = Integer.parseInt(pageNumber);
                	int intPageSize = Integer.parseInt(pageSize);
                    params.put("resB", (intPageNum - 1) * intPageSize+1);
                    params.put("resE", intPageNum * intPageSize+1);
                }else{
                    params.put("resB", 1);
                    params.put("resE", 101);
                }
                JSONArray jsonArr = fdHisService.getDynamic(params);
                int total = 0;
                if(jsonArr!=null){
                	total = jsonArr.size() > 0 ? jsonArr.getJSONObject(0).getInteger("total") : 0;
                	jsonArr.remove(0);
                }
                resObj.put("total", total);
                resObj.put("rows", jsonArr);
                resObj.put("page", pageNumber);
    
                return resObj;
            } else {
                return new JSONObject();
            } 
        } catch (Exception e) {
            logger.info("历史动态查询失败：{}",e.getMessage());
            return new JSONObject();
        }

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
     * @author:yuzd
     * @update:2018年01月2日 yuzd [变更描述]
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,String title,String data,String schema,String sortName,String sortOrder) {
        title = StringEscapeUtils.unescapeHtml4(title);
        String filter = StringEscapeUtils.unescapeHtml4(data);
        Map<String,Object> params = JSON.parseObject(filter);
        JSONObject resultObj = getDynamic(schema,filter,null,null,sortName,sortOrder);
        String titleDate = "";
        if (params.get("dateB") != null && StringUtils.isNotBlank(params.get("dateB").toString())) {
            titleDate += params.get("dateB").toString();
        }
        if (params.get("dateE") != null && StringUtils.isNotBlank(params.get("dateE").toString())) {
            titleDate += "-" + params.get("dateE").toString();
        }

        JSONArray resultArr = new JSONArray();
        data = "";
        if (resultObj != null) {
            resultArr = resultObj.getJSONArray("rows");
            data = JSONObject.toJSONString(resultArr);
        }
        JSONArray titleArray = JSONArray.parseArray(title);
        List<Map<String,String>> dataList = JSON.parseObject(data, new TypeReference<List<Map<String,String>>>() {
        });
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < titleArray.size(); i++) {
            titleList.add(titleArray.getJSONObject(i).getString("title"));
        }
        try {
            String fileName = "历史航班动态" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
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

            String excelTitle = "历史航班动态" + titleDate;
            ExportFDExcel excel = new ExportFDExcel(excelTitle, titleList);
            excel.setDataList(titleArray, dataList);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("航班动态导出失败" + e.getMessage());
        }
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
        List<GridColumn> colList = gridService.getColumnList(UserUtils.getUser().getId(), schema);
        String columns = JSON.toJSONString(colList);
        return columns;
    }

    /**
     * 
     * Discription:获取报文数据.
     * 
     * @param id
     * @return
     * @return:返回值意义
     * @author:SunJ
     * @update:2017年11月2日 SunJ [变更描述]
     */
    @RequestMapping(value = "getMessage")
    @ResponseBody
    public JSONArray getMessage(String id) {
        JSONArray arr = new JSONArray();
        if (id != null && !"".equals(id)) {
            Map<String,String> data = new HashMap<String,String>();
            data.put("fltId", id);
            arr = fdHisService.getTelegraphXmlData(data);
        }
        return arr;
    }

    /**
     * 
     * Discription:获取下拉选项.
     * 
     * @param model
     * @return:返回值意义
     * @author:yuzd
     * @update:2017年9月20日 yuzd [变更描述]
     */
    private void dimAttributes(Model model) {
        try {
        	// 性质
            JSONArray fltPropertys = cacheService.getList("dim_task");
            model.addAttribute("fltPropertys", fltPropertys);
            // 外航/国内
//            JSONArray alnFlags = cacheService.getCommonDict("airline_flag");
//            model.addAttribute("alnFlags", alnFlags);
            // 航线性质
            JSONArray alntype = cacheService.getCommonDict("alntype");
            model.addAttribute("alntype", alntype);
            // 机型分类
            JSONArray actkind = cacheService.getList("dim_actkind");
            model.addAttribute("actkinds", actkind);

            // 是否为宽体机型
//            JSONArray acttypeSizes = cacheService.getCommonDict("aln_flag");
//            model.addAttribute("acttypeSizes", acttypeSizes);
            // 航站楼
//            JSONArray terminals = cacheService.getCommonDict("terminal");
//            model.addAttribute("terminals", terminals);
            // 航班范围
//            JSONArray flightScopes = cacheService.getCommonDict("flight_scope");
//            model.addAttribute("flightScopes", flightScopes);
            // 机坪区域
            JSONArray aprons = cacheService.getList("dim_bay_apron");
            model.addAttribute("aprons", aprons);
            // 廊桥位、外围航班
            JSONArray GAFlag = cacheService.getCommonDict("GAFlag");
            model.addAttribute("GAFlag", GAFlag);
            // 标识
//            JSONArray identifying = cacheService.getCommonDict("identifying");
//            model.addAttribute("identifying", identifying);
            // 状态
            JSONArray actStatus = cacheService.getCommonDict("acfStatus");
            model.addAttribute("actStatus", actStatus);
            //延误原因
            JSONArray delyReson = cacheService.getOpts("dim_delay","delay_code","description_cn");
            model.addAttribute("delyReson", delyReson);
            // 要客标识
//            JSONArray vipFlag = cacheService.getCommonDict("vipFlag");
//            model.addAttribute("vipFlags", vipFlag);
            // 跑道
//            JSONArray runways = cacheService.getOpts("dim_runway", "RUNWAY_CODE", "RUNWAY_CODE");
//            model.addAttribute("runways", runways);
            // 除冰坪
//            JSONArray dprks = gridService.getDimFromOracle("dim_dprk_code", "DPRK_CODE",
//                    "DPRK_CODE||' '||RUNWAY||' '||W_OR_E");
//            model.addAttribute("dprks", dprks);
            // 除冰位
//            JSONArray dcnds = gridService.getDimFromOracle("dim_dcnd_code", "DCND_CODE",
//                    "DCND_CODE||' '||DPRK_CODE||' '||(case when SlOW_FLAG=0 then '关车除冰' when slow_flag=1 then '慢车除冰' end)");
//            model.addAttribute("dcnds", dcnds);
        } catch (Exception e) {
            logger.error("添加下拉框选项失败" + e.getMessage());
        }
    }

    public JSONArray getDimFromOracle(String table,String idcol,String textcol) {
        return gridService.getDimFromOracle(table, idcol, textcol);
    }

    /**
     * 保障图页面
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "")
    public String ensureList(Model model,String inFltid,String outFltid) {
        model.addAttribute("inFltid", inFltid);
        model.addAttribute("outFltid", outFltid);
        return "prss/flightdynamic/ensureList";
    }

    /**
     * 航班监控图页面
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "fltmonitor")
    public String fltmonitorList(Model model,String inFltid,String outFltid) {
        FltInfo fltInfo = fdHisService.getFltInfo(inFltid, outFltid);
        fltInfo.setInFltid(inFltid);
        fltInfo.setOutFltid(outFltid);
        model.addAttribute("fltInfo", fltInfo);
        model.addAttribute("hisFlag", "his");
        return "prss/flightdynamic/fltmonitorList";
    }

    @ResponseBody
    @RequestMapping(value = "getVipDate")
    public String getVipDate(String inFltid,String outFltid) {
        String fltid = "";
        if (!StringUtils.isEmpty(inFltid) && !inFltid.equals("undefined")) {
            fltid += inFltid + ",";
        }
        if (!StringUtils.isEmpty(outFltid) && !outFltid.equals("undefined")) {
            fltid += outFltid + ",";
        }
        fltid = fltid.substring(0, fltid.length() - 1);
        JSONArray json = fdHisService.getVipInfo(fltid);
        String result = json.toJSONString();
        return result;
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
        JSONObject data = fdHisService.getFltTimeData(params);
        model.addAttribute("data", data);
        model.addAttribute("ioType", ioType);
        return "prss/flightdynamic/fltDataInput";
    }
    
    /**
     * 获取历史航班动态航班信息
     * @param inFltId
     * @param outFltId
     * @return
     */
    @RequestMapping(value = "toHisform")
    public String toHisform(Model model,String inFltId,String outFltId) {
    	model.addAttribute("inFltId", inFltId);
		model.addAttribute("outFltId", outFltId);
    	return "prss/flightdynamic/fdHistoricalForm";
    }
    
    /**
     * 获取历史航班动态航班信息
     * @param inFltId
     * @param outFltId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getHisFltInfo")
    public JSONArray getHisFltInfo(String inFltId,String outFltId) {
    	return fdHisService.getHisFltInfo(inFltId, outFltId);
    }
}
