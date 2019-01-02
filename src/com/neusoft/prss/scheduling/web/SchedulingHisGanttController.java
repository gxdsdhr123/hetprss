/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月2日 下午3:45:33
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.scheduling.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.scheduling.service.SchedulingHisGanttService;

@Controller
@RequestMapping(value = "${adminPath}/schedulingHis/gantt")
public class SchedulingHisGanttController extends BaseController {
    @Autowired
    private SchedulingHisGanttService schedulingHisGanttService;

    @Autowired
    private CacheService cacheService;

    /**
     * 
     *Discription:返回指挥调度历史（甘特图）页面.
     *@param model
     *@param schemaId
     *@return
     *@return:返回值意义
     *@author:SunJ
     *@update:2017年12月4日 SunJ [变更描述]
     */
    @RequestMapping(value = {"listGantt",""})
    public String listGantt(Model model,String schemaId) {
        model.addAttribute("schemaId", schemaId);
        dimAttributes(model);
        return "prss/scheduling/schedulingHisGantt";
    }

    /**
     * 
     *Discription:获取摆渡车历史甘特图Y轴数据（分工、姓名）.
     *@return
     *@return:返回值意义
     *@author:SunJ
     *@update:2017年12月4日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttYData")
    @ResponseBody
    public String ganttYData(String hisDate) {
        String officeId = UserUtils.getUser().getOffice().getId();
        JSONArray arr = schedulingHisGanttService.getSdYData(officeId, hisDate);
        return JSON.toJSONString(arr, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 
     *Discription:获取摆渡车历史甘特图数据.
     *@param param
     *@return
     *@return:返回值意义
     *@author:SunJ
     *@update:2017年12月4日 SunJ [变更描述]
     */
    @RequestMapping(value = "ganttData")
    @ResponseBody
    public String ganttData(String hisDate) {
        Map<String,Object> params = new HashMap<String,Object>();
        if (hisDate != null) {
            params.put("hisDate", hisDate);
        }
        List<JobKind> jobKind = UserUtils.getCurrentJobKind();
		String jobKinds = "";
		for(int i=0;i<jobKind.size();i++){
			jobKinds += "'"+jobKind.get(i).getKindCode()+"'";
			if(i!=jobKind.size()-1){
				jobKinds += ",";
			}
		}
		params.put("jobKinds", jobKinds);
        String arr = schedulingHisGanttService.ganttData(params);
        return arr;
    }
    
    /**
     * 获取甘特条详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "getGanttDetail")
    @ResponseBody
    public List<List<String>> getGanttDetail(String id, String schemaId) {
        List<List<String>> detail = schedulingHisGanttService.getGanttDetail(id,schemaId);
        return detail;
    }

    /**
     * 
     *Discription:获取筛选条件中下拉选项.
     *@param model
     *@return:返回值意义
     *@author:SunJ
     *@update:2017年11月8日 SunJ [变更描述]
     */
    private void dimAttributes(Model model) {
        try {
            // 性质
            JSONArray fltPropertys = cacheService.getList("dim_task");
            model.addAttribute("fltPropertys", fltPropertys);
            // 外航/国内
            JSONArray alnFlags = cacheService.getCommonDict("airline_flag");
            model.addAttribute("alnFlags", alnFlags);
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
        } catch (Exception e) {
            logger.error("添加下拉框选项失败" + e.getMessage());
        }
    }
}
