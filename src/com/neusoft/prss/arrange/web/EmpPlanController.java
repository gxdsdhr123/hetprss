package com.neusoft.prss.arrange.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.arrange.entity.EmpPlanMain;
import com.neusoft.prss.arrange.entity.ExportFDExcel;
import com.neusoft.prss.arrange.service.EmpPlanService;
import com.neusoft.prss.arrange.service.WorkerArrangePlanService;

@Controller
@RequestMapping(value = "${adminPath}/arrange/empplan")
public class EmpPlanController extends BaseController {

    @Autowired
    private EmpPlanService empplanService;

    @Autowired
    private WorkerArrangePlanService workerArrangePlanService;

    /**
     * 
     *Discription:员工排班计划列表.
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月26日 lirr [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("currDate", df.format(new Date()));
        return "prss/arrange/empPlanList";
    }

    /**
     * 
     *Discription:选择月份的排班计划.
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月27日 lirr [变更描述]
     */
    @RequestMapping(value = "getEmpPlan")
    @ResponseBody
    public String getEmpPlan(HttpServletRequest request) {
        Map<String,String> params = getAllParameter(request);
        String officeId = UserUtils.getUser().getOffice().getId();
        params.put("officeId", officeId);
        String result = JSON.toJSONString(empplanService.getEmpPlanList(params), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:删除,批量删除页面.
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月27日 lirr [变更描述]
     */
    @RequestMapping(value = "getDelBatchEmpPlan")
    @ResponseBody
    public String getDelEmpPlan(HttpServletRequest request) {
        Map<String,String> params = getAllParameter(request);
        String officeId = UserUtils.getUser().getOffice().getId();
        params.put("officeId", officeId);
        try {
            empplanService.deletePlan(params);
        } catch (Exception e) {
            logger.error("删除计划失败" + e.getMessage());
            return "faild";
        }
        return "success";
    }

    /**
     * 
     *Discription:工作计划-页面
     *@param model
     *@param selDate
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月27日 lirr [变更描述]
     */
    @RequestMapping(value = "showPlanPage")
    public String showPlanPage(Model model,String selDate,String currDate) {
        model.addAttribute("selDate", selDate);
        model.addAttribute("currentDate", currDate);
        return "prss/arrange/empPlanPage";
    }

    /**
     * 
     *Discription:工作计划-页面列表.
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月27日 lirr [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "getGridData")
    public String getGridData(HttpServletRequest request) {
        Map<String,String> params = getAllParameter(request);    //用来获取当前请求提交过来的所有表单项
        
        
        String officeId = UserUtils.getUser().getOffice().getId();
        params.put("officeId", officeId);
        String result = JSON.toJSONString(empplanService.getPlanGridList(params), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:工作计划-点击增加.
     *@param model
     *@param selDate
     *@param type
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月27日 lirr [变更描述]
     */
    
    @RequestMapping(value = "addPlanList")
    public String getPlanById(Model model,@RequestParam(value = "selDate",required = true) String selDate,
            @RequestParam(value = "type",required = true) String type,
            @RequestParam(value = "id",required = true) String id) {
        Map<String,String> param = new HashMap<String,String>();
        param.put("selDate", selDate);
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        JSONArray groupInfo = new JSONArray();
        JSONArray arr = new JSONArray();
        EmpPlanMain vo = new EmpPlanMain();
        List<EmpPlanMain> list = new ArrayList<EmpPlanMain>();
        if ("modify".equals(type)) {
            arr = empplanService.getPlanInfoById(id);
            list = arr.toJavaList(EmpPlanMain.class);
            if (list.size() > 0) {
                vo = list.get(0);
            }
        } else {
            groupInfo = empplanService.getGroupInfo(param);
        }
        if(id.contains(",")){
        	vo = new EmpPlanMain();
        }
        model.addAttribute("empPlanVO", vo);
        model.addAttribute("groupInfos", groupInfo);
        model.addAttribute("selDate", selDate);
        model.addAttribute("flag", type);
        return "prss/arrange/empPlanAdd";
    }

    /**
     * 
     *Discription:选择班组信息.
     *@param model
     *@param groupId
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "getEmpInfoById")
    @ResponseBody
    public String getEmpInfoById(Model model,@RequestParam(value = "groupId",required = true) String groupId,String personName) {
        Map<String,String> param = new HashMap<String,String>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("groupId", groupId);
        param.put("personName", personName);
        String result = JSON.toJSONString(empplanService.getEmpInfoById(param), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:默认班组取得所有员工信息.
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月8日 lirr [变更描述]
     */
    @RequestMapping(value = "getAllEmpInfo")
    @ResponseBody
    public String getAllEmpInfo(Model model,String personName) {
        Map<String,String> param = new HashMap<String,String>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("personName", personName);
        String result = JSON.toJSONString(empplanService.getAllEmpInfo(param), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     *Discription:班制展开.
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月27日 lirr [变更描述]
     */
    @RequestMapping(value = "showShifts")
    public String showShifts(Model model,HttpServletRequest request) {
        String type = request.getParameter("type");
        String id1 = request.getParameter("id1");
        String id2 = request.getParameter("id2");
        String id3 = request.getParameter("id3");
        model.addAttribute("type", type.replace("bzBtn", ""));
        model.addAttribute("id1", id1);
        model.addAttribute("id2", id2);
        model.addAttribute("id3", id3);
        return "prss/arrange/shiftsPage";
    }

    /**
     * 
     *Discription:班制列表.
     *@param request
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月27日 lirr [变更描述]
     */
    @ResponseBody
    @RequestMapping(value = "getShiftGridData")
    public String getShiftGridData(HttpServletRequest request) {
        Map<String,String> params = getAllParameter(request);
        params.put("userId", UserUtils.getUser().getId());//获取用户ID gaojd 20180109增加，只取本部门的班制
        String result = JSON.toJSONString(empplanService.getShiftsList(params), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:判断员工排班计划是否重复
     *@param data
     *@param type
     *@param id
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年11月9日 lirr [变更描述]
     */
    @RequestMapping(value = "ifHavePlanInfo")
    @ResponseBody
    public String ifHavePlanInfo(String data,String type,String id) {
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray dataArray = JSONArray.parseArray(data);
        List<EmpPlanMain> list = dataArray.toJavaList(EmpPlanMain.class);
        String officeId = UserUtils.getUser().getOffice().getId();
        String msg = "";
        msg = empplanService.ifHavePlanInfo(list, officeId, id, type);
        return msg;
    }

    /**
     * 
     *Discription:保存人员排班信息.
     *@param data
     *@param schema
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月28日 lirr [变更描述]
     */
    @RequestMapping(value = "savePlanInfo")
    @ResponseBody
    public String savePlanInfo(String data,String type,String id) {
    	System.out.println(data+"\b"+id);
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray dataArray = JSONArray.parseArray(data);
        List<EmpPlanMain> list = dataArray.toJavaList(EmpPlanMain.class);
        String officeId = UserUtils.getUser().getOffice().getId();
        String msg = "";
        if ("create".equals(type)) {
            msg = empplanService.savePlanInfo(list, officeId);
        } else {
            if (list.size() > 0) {
                EmpPlanMain vo = list.get(0);
                msg = empplanService.modifyPlanInfo(vo, id);
            }
        }
        return msg;
    }

    /**
     * 
     *Discription:批量删除人员排班计划.
     *@param ids
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月28日 lirr [变更描述]
     */
    @RequestMapping(value = "deletePlan")
    @ResponseBody
    public String deletePlan(@RequestParam(value = "ids[]",required = true) String[] ids) {
        try {
            empplanService.deleteEmpPlan(ids);
        } catch (Exception e) {
            logger.error("删除人员排班失败" + e.getMessage());
            return "faild";
        }
        return "success";
    }

    /**
     * 
     *Discription:显示工作排序列表.
     *@param model
     *@param selDate
     *@param type
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月28日 lirr [变更描述]
     */
    @RequestMapping(value = "showOrderPlan")
    public String showOrderPlan(Model model,@RequestParam(value = "selDate",required = true) String selDate) {
        Map<String,String> param = new HashMap<String,String>();
        param.put("selDate", selDate);
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        JSONArray planInfos = empplanService.showOrderPlan(param);
        model.addAttribute("planInfos", planInfos);
        model.addAttribute("selDate", selDate);
        model.addAttribute("flag", "order");
        return "prss/arrange/empPlanEdit";
    }

    /**
     * 
     *Discription:保存工作顺序
     *@param data
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月29日 lirr [变更描述]
     */
    @RequestMapping(value = "saveOrder")
    @ResponseBody
    public String saveOrder(String data) {
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray dataArray = JSONArray.parseArray(data);
        List<EmpPlanMain> list = dataArray.toJavaList(EmpPlanMain.class);
        String msg = empplanService.saveOrderInfo(list);
        return msg;
    }

    /**
     * 
     *Discription:保存停用人员信息
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月29日 lirr [变更描述]
     */
    @RequestMapping(value = "saveStopPlan")
    @ResponseBody
    public String saveStopPlan(String times,String reason,@RequestParam(value = "ids[]",required = true) String[] ids) {
        String msg = "";
        for (int i = 0; i < ids.length; i++) {
            Map<String,String> params = new HashMap<String,String>();
            String id = ids[i];
            String[] ts = times.split(",");
            String[] workInfo = id.split("@");
            params.put("workId", workInfo[0]);
            params.put("workName", workInfo[1]);
            params.put("blockupStime", ts[0]);
            params.put("blockupEtime", ts[1]);
            params.put("blockupReason", reason);
            String officeId = UserUtils.getUser().getOffice().getId();
            String curWorkId = UserUtils.getUser().getId();
            params.put("officeId", officeId);
            params.put("curWorkId", curWorkId);
            params.put("optType", "0");
            //判断是否有未完成的任务
            msg = empplanService.checkTask(params);
            if ("0".equals(msg)) {
                msg = empplanService.saveStopInfo(params);
                //写日志
                empplanService.saveLog(params);
            } else {
                msg = "check";
                break;
            }
        }
        return msg;
    }

    /**
     * 
     *Discription:恢复人员工作
     *@param id
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月29日 lirr [变更描述]
     */
    @RequestMapping(value = "resumePlan")
    @ResponseBody
    public String resumePlan(@RequestParam(value = "ids[]",required = true) String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Map<String,String> params = new HashMap<String,String>();
            String id = ids[i];
            String[] workInfo = id.split("@");
            params.put("workId", workInfo[0]);
            params.put("workName", workInfo[1]);
            String officeId = UserUtils.getUser().getOffice().getId();
            String curWorkId = UserUtils.getUser().getId();
            params.put("officeId", officeId);
            params.put("curWorkId", curWorkId);
            params.put("optType", "1");
            params.put("blockupStime", "");
            params.put("blockupEtime", "");
            try {
                //删除停用信息
                empplanService.deleteStopInfo(params);
                //写日志
                empplanService.saveLog(params);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }
        return "success";
    }

    /**
     * 
     *Discription:跳转日志页面
     *@param request
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月29日 lirr [变更描述]
     */
    @RequestMapping(value = "showLogPage")
    public String showLogPage(Model model) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        model.addAttribute("startDate", date.substring(0, 7) + "-01");
        model.addAttribute("endDate", date);
        model.addAttribute("flag", "log");
        return "prss/arrange/empPlanEdit";
    }

    /**
     * 
     *Discription:显示日志列表
     *@param pageSize
     *@param pageNumber
     *@param timeStart
     *@param timeEnd
     *@param sortOrder
     *@param sortName
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月29日 lirr [变更描述]
     */
    @RequestMapping(value = "showLogList")
    @ResponseBody
    public Map<String,Object> showLogList(int pageSize,int pageNumber,String timeStart,String timeEnd,String sortOrder,
            String sortName,String empName) {
        Map<String,Object> param = new HashMap<String,Object>();
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("begin", begin);
        param.put("end", end);
        param.put("timeStart", timeStart);
        param.put("timeEnd", timeEnd);
        param.put("sortOrder", sortOrder);
        param.put("sortName", sortName);
        param.put("empName", empName);//员工姓名
        return empplanService.getLogList(param);
    }

    /**
     * 
     *Discription:跳转非固定班制页面.
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "showUnfixedPage")
    public String showUnfixedPage(Model model) {
        return "prss/arrange/empUnfixedPage";
    }

    /**
     * 
     *Discription:非固定班制表格数据.
     *@param pageSize
     *@param pageNumber
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "showUnfixedList")
    @ResponseBody
    public Map<String,Object> showUnfixedList(int pageSize,int pageNumber) {
        Map<String,Object> param = new HashMap<String,Object>();
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("begin", begin);
        param.put("end", end);
        return empplanService.getUnfixedList(param);
    }

    /**
     * 
     *Discription:生成非固定班制计划,直接调用服务
     *@param times
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "saveMakePlan")
    @ResponseBody
    public String saveMakePlan(String times) {
        String officeId = UserUtils.getUser().getOffice().getId();
        String[] time = times.split(",");
        try {
            //调用服务
            workerArrangePlanService.doWorkerAmbulatoryArrange(time[0], time[1], officeId);
        } catch (Exception e) {
            logger.error("生成非固定班制计划失败" + e.getMessage());
            return "faild";
        }
        return "success";
    }

    /**
    * 
    *Discription:得到作业组信息
    *@param model
    *@return
    *@return:返回值意义
    *@author:lirr
    *@update:2017年10月30日 lirr [变更描述]
    */
    @RequestMapping(value = "getTeamList")
    @ResponseBody
    public String getTeamList(Model model,String teamName) {
        Map<String,String> param = new HashMap<String,String>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("teamName", teamName);
        String result = JSON.toJSONString(empplanService.getTeamList(param), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:得到班组列表.
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "getGroupInfo")
    @ResponseBody
    public String getGroupInfo(Model model) {
        Map<String,String> param = new HashMap<String,String>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        String result = JSON.toJSONString(empplanService.getGroupInfo(param), SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 
     *Discription:非固定班制列表
     *@param model
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "getUnfixedDim")
    @ResponseBody
    public String getUnfixedDim(Model model,String unfixedName) {
        Map<String,String> param = new HashMap<String,String>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("unfixedName", unfixedName);
        String result = JSON.toJSONString(empplanService.getUnfixedDim(param), SerializerFeature.WriteMapNullValue);
        return result;
    }

    
    /**
     * 
     *Discription:保存非固定班制计划.
     *@param ids1
     *@param ids2
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "saveUnfiexPlan")
    @ResponseBody
    public String saveUnfiexPlan(String ids1,String ids2,String planId,String flag) {
        Map<String,String> param = new HashMap<String,String>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("workerIds", ids1);
        param.put("shiftsIds", ids2);
        param.put("id", planId);
        String msg = "";
        if (flag != null && "modify".equals(flag)) {
            msg = empplanService.modifyUnfiexPlan(param);
        } else {
            msg = empplanService.saveUnfiexPlan(param);
        }
        return msg;
    }

    /**
     * 
     *Discription:删除非固定班制计划.
     *@param id
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */

    @RequestMapping(value = "deleteUnfixedPlan")
    @ResponseBody
    public String deleteUnfixedPlan(String id) {
        try {
            empplanService.deleteUnfixedPlan(id);
        } catch (Exception e) {
            logger.error("删除非固定班制计划失败" + e.getMessage());
            return "faild";
        }
        return "success";
    }

    /**
     * 
     *Discription:修改固定班制-显示已存的人员和班组
     *@param id
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "getUnfixedHaveList")
    @ResponseBody
    public String getUnfixedHaveList(String id) {
        Map<String,String> param = new HashMap<String,String>();
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("id", id);
        JSONObject obj = new JSONObject();
        JSONArray arr1 = empplanService.getWorkerHaveList(param);
        JSONArray arr2 = empplanService.getShiftsHaveList(param);
        obj.put("workerHaveList", arr1);
        obj.put("shiftsHaveList", arr2);

        String result = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        return result;
    }

    /**
     * 判断时间范围内是否已存在计划
     * @param stime 开始时间
     * @param etime 结束时间
     * @return 不存在返回success，存在返回提示信息
     */
    @RequestMapping(value = "getIfExistPlan")
    @ResponseBody
    public String getIfExistPlan(String stime,String etime) {
        String officeId = UserUtils.getUser().getOffice().getId();
        return empplanService.getIfExistPlan(stime,etime,officeId);
    }
    
    /**
     * 
     *Discription:保存批量生成
     *@param stime1
     *@param etime1
     *@param stime2
     *@param etime2
     *@return
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "saveBatchAddPlan")
    @ResponseBody
    public String saveBatchAddPlan(String stime1,String etime1,String stime2,String etime2) {
        String officeId = UserUtils.getUser().getOffice().getId();
        try {
            //调用服务
            workerArrangePlanService.doWorkerArrange(stime1, etime1, stime2, etime2, officeId);
        } catch (Exception e) {
            logger.error("生成批量生成计划失败" + e.getMessage());
            return "faild";
        }
        return "success";
    }

    /**
     * 
     *Discription:导出
     *@param request
     *@param response
     *@return:返回值意义
     *@author:lirr
     *@update:2017年10月30日 lirr [变更描述]
     */
    @RequestMapping(value = "exportPlan")
    public void exportPlan(HttpServletRequest request,HttpServletResponse response) {
        try {
            String officeName = UserUtils.getUser().getOffice().getName();
            String month = request.getParameter("exportMonth");
            String[] s = month.split("-");
            month = s[0] + "/" + s[1];
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.valueOf(s[0]));
            cal.set(Calendar.MONTH, Integer.valueOf(s[1]) - 1);
            int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            sdf1.setLenient(false);
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");
            List<String> titleList = new ArrayList<String>();
            titleList.add("序号:序号");
            titleList.add("人员:人员");
            for (int i = 1; i < dateOfMonth + 1; i++) {
                Date date = sdf1.parse(month + "/" + i);
                titleList.add(sdf1.format(date) + ":" + sdf2.format(date));
            }
            JSONArray dataArray = empplanService.getExportPlan(month.replace("/", "-"),titleList);
            String fileName = "员工排班计划" + month + ".xlsx";
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
            String excelTitle = officeName + " 员工排班计划 " + DateUtils.getDate();
            ExportFDExcel excel = new ExportFDExcel(excelTitle, titleList);
            excel.setDataList(titleList,dataArray);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("员工排班计划导出失败" + e.getMessage());
        }
    }
}
