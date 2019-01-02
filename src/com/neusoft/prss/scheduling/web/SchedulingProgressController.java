package com.neusoft.prss.scheduling.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.scheduling.service.SchedulingProgressService;

/**
 * 调度图
 * 
 * @author baochl
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/scheduling/progress")
public class SchedulingProgressController extends BaseController {
	@Autowired
	private SchedulingProgressService progressService;

	/**
	 * 进入主页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "main", "" })
	public String main(Model model, HttpServletRequest request) {
		//作业类型
		String jobType = request.getParameter("jobType");
		if(StringUtils.isEmpty(jobType)){
			jobType = "JWBDCbdc";//目录参数中未配置作业类型默认为摆渡车
		}
		model.addAttribute("jobType", jobType);
		return "prss/scheduling/schedulingProgress";
	}

	@RequestMapping(value = "getTaskItmes")
	public String getTaskItmes(Model model, HttpServletRequest request) {
		String jobType = request.getParameter("jobType");//作业类型
		String ioType = request.getParameter("ioType");// 进出港类型
		String taskState = request.getParameter("taskState");// 任务状态
		String fltNo = request.getParameter("fltNo");// 航班号
		String airlineGroup = request.getParameter("airlineGroup");// 航空公司分组
		String alarmLevel = request.getParameter("alarmLevel");// 预警级别
		Map<String,Object> params = new HashMap<String,Object>();
		JSONArray taskList = new JSONArray();
		if (StringUtils.isNotEmpty(jobType)) {
			params.put("taskState", taskState);
			params.put("ioType", ioType);
			params.put("jobType", jobType);
			params.put("fltNo", fltNo);
			params.put("airlineGroup", airlineGroup);
			params.put("alarmLevel", alarmLevel);
			taskList = progressService.getTaskList(params);
		}
		model.addAttribute("ioType", ioType);
		model.addAttribute("taskState", taskState);
		model.addAttribute("taskList", taskList);
		return "prss/scheduling/schedulingProgressItems";
	}
	/**
	 * 根据航班获取任务
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getTasks")
	public String getTasks(Model model, HttpServletRequest request) {
		Map<String,Object> params = new HashMap<String,Object>();
		String jobType = request.getParameter("jobType");//作业类型
		String fltid = request.getParameter("fltid");//航班id
		params.put("jobType", jobType);
		params.put("fltid", fltid);
		JSONArray fltTasks = progressService.getTaskByFlt(params);
		model.addAttribute("fltTasks", fltTasks);
		return "prss/scheduling/schedulingProgressDetail";
	}
	/**
	 * 航班备注
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "fltRemarkForm")
	public String fltRemarkForm(Model model, String fltid) {
		List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
		if(jobKinds!=null&&!jobKinds.isEmpty()){
			String jobKind = jobKinds.get(0).getKindCode();
			Map<String,String> params = new HashMap<String,String>();
			params.put("jobKind", jobKind);
			params.put("attrName", "remark");
			params.put("fltid", fltid);
			String attrId = progressService.getFltPlusAttrId(params);
			if(StringUtils.isNotEmpty(attrId)){
				params.put("attrId", attrId);
				JSONObject remarkInfo = progressService.getFltRemark(params);
				if(remarkInfo==null){
					remarkInfo = new JSONObject();
				}
				remarkInfo.put("fltid", fltid);
				remarkInfo.put("attrId", attrId);
				model.addAttribute("remarkInfo", remarkInfo);
			} else {
				model.addAttribute("error", "当前岗位未增加备注配置，请联系管理员！");
			}
		} else {
			model.addAttribute("error", "当前账号没有权限添加备注，请联系管理员！");
		}
		return "prss/scheduling/fltRemarkForm";
	}
	/**
	 * 保存航班备注
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveFltRemark")
	public String saveFltRemark(Model model, HttpServletRequest request) {
		try{
			Map<String,String> params = new HashMap<String,String>();
			params.put("id", request.getParameter("id"));
			params.put("fltid", request.getParameter("fltid"));
			params.put("attrId", request.getParameter("attrId"));
			params.put("attrValue", request.getParameter("attrValue"));
			progressService.saveFltRemark(params);
		} catch(Exception e){
			logger.error(e.toString());
			return e.toString();
		}
		return "succeed";
	}
}
