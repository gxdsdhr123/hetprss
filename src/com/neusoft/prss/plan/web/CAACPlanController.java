/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年4月10日 下午3:47:23
 *@author:neusoft
 *@version:[v1.0]
 */
package com.neusoft.prss.plan.web;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
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
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.service.OperationLogService;
import com.neusoft.prss.plan.entity.CAACMainEntity;
import com.neusoft.prss.plan.service.CAACPlanService;
import com.neusoft.prss.produce.entity.ExportExcel;

@Controller
@RequestMapping(value = "${adminPath}/caac/plan")
public class CAACPlanController extends BaseController {

    @Resource
    private CAACPlanService caacPlanService;
    
    @Resource
    private OperationLogService operLogService;
    
    @Autowired
    private CacheService cacheService;
    
    /**
     * 
     *Discription:跳转到计划列表.
     *@param model
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        initParam(model);
        model.addAttribute("type", "main");
        return "prss/plan/caacPlanMain";
    }
    
    private void initParam(Model model) {
        JSONObject jsonObject = new JSONObject();
        //属性
        JSONArray ATTR_CODE = cacheService.getCommonDict("alntype");
        jsonObject.put("ATTR_CODE", ATTR_CODE);
        //起、落场
        JSONArray APT = cacheService.getOpts("dim_airport", "icao_code", "description_cn","airport_code");
        jsonObject.put("APT", APT);
        //机型
        JSONArray ACT_TYPE = cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
        jsonObject.put("ACT_TYPE", ACT_TYPE);
        //航空公司
        JSONArray AIRLINE_CODE = cacheService.getOpts("dim_airline", "airline_code", "airline_shortname","icao_code");
        jsonObject.put("AIRLINE_CODE", AIRLINE_CODE);
        model.addAttribute("data", jsonObject);
    }
    
    /**
     * 
     *Discription:初始化列表数据.
     *@param request
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value="init")
    @ResponseBody
    public String getPlans(HttpServletRequest request) {
        long beginTime = System.currentTimeMillis();
        Enumeration<String> enu = request.getParameterNames();
        Map<String,String> params = new HashMap<String,String>();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            String paraVlaue = request.getParameter(paraName);
            params.put(paraName, paraVlaue);
        }
        JSONArray mainArr = caacPlanService.getPlans(params);
        String data = JSON.toJSONString(mainArr, SerializerFeature.WriteMapNullValue);
        logger.info("局方长期计划查询用{}秒。",(System.currentTimeMillis()-beginTime)/1000.0);
        System.err.println("局方长期计划查询用{}秒。" + (System.currentTimeMillis()-beginTime)/1000.0);
        return data;
    }
    
    /**
     * 
     *Discription:页面修改.
     *@param name
     *@param value
     *@param id
     *@param isMain
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value="update")
    @ResponseBody
    public String update (String name,String value,String id,Boolean isMain) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("id", id);
        params.put("name", name);
        params.put("value", value);
        params.put("isMain", String.valueOf(isMain));
        try {
            String filter = "";
            if("FLT_NO".equals(name) || "FLT_WEEK".equals(name)) {
                JSONObject mainObject = caacPlanService.getPlanById(params);
                mainObject.put(name, value);
                CAACMainEntity mainEntity = JSON.parseObject(mainObject.toJSONString(), new TypeReference<CAACMainEntity>() {});  
                List<CAACMainEntity> list = new ArrayList<CAACMainEntity>();
                list.add(mainEntity);
                filter = caacPlanService.filter(list);
            }
            if(!StringUtils.isBlank(filter)) {
                return filter;
            }
            caacPlanService.update(params);
            return "success";                    
        } catch (Exception e) {
            logger.error("局方长期计划，修改数据失败：{}",e.getMessage());
            return "操作失败";
        }
    }
    
    /**
     * 
     *Discription:删除数据.
     *@param ids
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value="delete")
    @ResponseBody
    public boolean delete (String ids) {
        boolean result = false;
        System.err.println(ids);
        try {
            if(!StringUtils.isBlank(ids))
                caacPlanService.delete(ids);
            result = true;
        } catch (Exception e) {
            logger.error("局方长期计划，删除数据失败：{}",e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     *Discription:跳转新增页面.
     *@param model
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月13日 neusoft [变更描述]
     */
    @RequestMapping(value="toAdd")
    public String toAdd(Model model) {
        initParam(model);
        model.addAttribute("type", "add");
        return "prss/plan/caacPlanAdd";
    }
    
    

    @ResponseBody
    @RequestMapping(value = "filterData",method = RequestMethod.POST)
    public String filterData(HttpServletRequest request) {
        try {
            String  data = StringEscapeUtils.unescapeHtml4(request.getParameter("data"));
            JSONArray dataArray = JSONArray.parseArray(data);
            
        } catch (Exception e) {
            logger.error("局方长期计划文本导入失败" + e.getMessage());
            e.printStackTrace();
            return "error";
        }

        return "success";
    }

    
    
    /**
     * 
     *Discription:新增保存.
     *@param request
     *@param response
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public String save(HttpServletRequest request,HttpServletResponse response) {
        String result = "操作失败";
        try {
            String  data = StringEscapeUtils.unescapeHtml4(request.getParameter("data"));
            JSONArray dataArray = JSONArray.parseArray(data);
            List<CAACMainEntity> list = new ArrayList<CAACMainEntity>();
            for(int i=0;i<dataArray.size();i++) {
                JSONObject object = dataArray.getJSONObject(i);
                String fltNo = object.getString("FLT_NO");
                //新增主表
                String time = new SimpleDateFormat("MMdd").format(new Date());
                if(time.compareTo(object.getString("PLAN_DATE_END")) > 0)
                    continue;
                CAACMainEntity mainEntity = new CAACMainEntity();
                mainEntity.setFltNo(fltNo);//航班号
                mainEntity.setFltWeek(object.getString("FLT_WEEK"));//班期
                mainEntity.setPlanDateBegin(object.getString("PLAN_DATE_BEGIN"));//计划开始时间
                mainEntity.setPlanDateEnd(object.getString("PLAN_DATE_END"));//计划结束时间
                mainEntity.setCreateUser(UserUtils.getUser().getId());//用户ID
                mainEntity.setActType(object.getString("ACT_TYPE"));//机型
                mainEntity.setArrivalApt(object.getString("ARRIVAL_APT"));//落场
                mainEntity.setAttrCode(object.getString("ATTR_CODE"));//属性
                mainEntity.setDepartApt(object.getString("DEPART_APT"));//落场
                mainEntity.setShareFltNo(object.getString("SHARE_FLT_NO"));//共享航班号
                mainEntity.setSta(object.getString("STA"));//计落
                mainEntity.setStd(object.getString("STD"));//计起
                mainEntity.setRemark(object.getString("REMARK"));//备注
                list.add(mainEntity);
            }
            
            if (list.size()<=0) {
                logger.warn("局方长期计划新增数据为空！");
            } else {
                String filter = caacPlanService.filter(list);
                if(StringUtils.isBlank(filter)) {
                    boolean insert = caacPlanService.insert(list);
                    if(insert) {
//                        OperationLogEntity log = new OperationLogEntity();
//                        operLogService.writeLog(log);
                    }
                } else {
                    return filter;
                }
            }
            return "success";
        } catch (Exception e) {
            logger.error("局方长期计划新增失败：{}", e.getMessage());
        }
        return result;
    }
    private String arrangePlanDateBegin(String planDateBegin,String string) {
        return planDateBegin.compareTo(string)>0?string:planDateBegin;
    }

    private String arrangePlanDateEnd(String planDateEnd,String string) {
        return planDateEnd.compareTo(string)>0?planDateEnd:string;
    }

    private String arrangeFltWeek(String fltWeek,String newFltWeek) {
        List<String> list = new ArrayList<String>();
        if(StringUtils.isBlank(fltWeek))
            return newFltWeek;
        for (int i = 0; i < fltWeek.length(); i++) {
            String oldStr = String.valueOf(fltWeek.charAt(i));
            if(!StringUtils.isBlank(oldStr) && !".".equals(oldStr))
                list.add(oldStr);
        }
        for (int j = 0; j < newFltWeek.length(); j++) {
            String newStr = String.valueOf(newFltWeek.charAt(j));
            if(!StringUtils.isBlank(newStr) && !".".equals(newStr))
                list.add(newStr);
        }
        HashSet<String> h = new HashSet<String>(list);      
        list.clear();      
        list.addAll(h);
        return StringUtils.join(list, ",");
    }

    /**
     * 
     *Discription:打印.
     *@param request
     *@param response
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月18日 neusoft [变更描述]
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response) {
        try {
            String title = StringEscapeUtils.unescapeHtml4(request.getParameter("printTitle"));
            title = URLDecoder.decode(title,"UTF-8");
            JSONArray titleArray = JSONArray.parseArray(title);
            String fileName = "局方长期计划" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
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

            String excelTitle = "局方长期计划" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportExcel excel = new ExportExcel(excelTitle);
            Map<String,String> params = new HashMap<String,String>();
            JSONArray plans = caacPlanService.getPlans(params);
            excel.setTitleData(titleArray);
            excel.setData(titleArray,plans);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("局方长期计划导出失败" + e.getMessage());
        }
    }
    
    /**
     * 
     *Discription:导入excel数据.
     *@param files
     *@param delFile
     *@param type
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年4月14日 neusoft [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "importPlan",method = RequestMethod.POST)
    public String importPlan(@RequestParam("file") MultipartFile[] files) {
        try {
            long beginTime = System.currentTimeMillis();
            for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getOriginalFilename();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    caacPlanService.importPlan(files[i].getBytes(), suffix, UserUtils.getUser().getId());
            }
            logger.info("局方长期计划文本导入公用{}秒。",(System.currentTimeMillis()-beginTime)/1000.0);
        } catch (Exception e) {
            logger.error("局方长期计划文本导入失败" + e.getMessage());
            e.printStackTrace();
            return "error";
        }

        return "success";
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
}
