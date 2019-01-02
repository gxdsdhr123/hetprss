package com.neusoft.prss.workflow.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.curator.shaded.com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.User;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.scheduling.service.JobManageService;
import com.neusoft.prss.workflow.service.TaskDetailService;
import com.neusoft.prss.workflow.service.TaskService;
import com.neusoft.prss.workflow.service.WFBtnEventService;

/**
 * 工作流按钮事件处理
 * 
 * @author baochl
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow/btnEvent")
public class WFBtnEventController extends BaseController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private WFBtnEventService btnService;
	@Autowired
	private JobManageService jobService;
	@Autowired
	private TaskDetailService taskDetailService;
	/**
	 * 启动实例
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doStart")
	public String startInstance(HttpServletRequest request) {
		String result = "succeed";
		try {
			String jobTaskId = request.getParameter("jobTaskId");// 作业任务id
			String procId = request.getParameter("procId");// 流程模板id
			String actor = request.getParameter("actor");// 处理人
			Map<String, String> args = getAllParameter(request);
			btnService.startInstance(jobTaskId, procId, UserUtils.getUser().getId(), actor, args);
		} catch (Exception e) {
			result = e.toString();
		}
		return result;
	}

	/**
	 * 下一步
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doNext")
	public String next(HttpServletRequest request) {
		String result = "succeed";
		try {
			String jobTaskId = request.getParameter("jobTaskId");
			JSONObject json = taskService.getFlowTaskByJob(jobTaskId);
			if (json.containsKey("taskId") && json.containsKey("actor")
					&& StringUtils.isNotEmpty(json.getString("taskId"))
					&& StringUtils.isNotEmpty(json.getString("actor"))) {
				Map<String, String> args = getAllParameter(request);
				Set<String> keys = json.keySet();
				for (String key : keys) {
					args.put(key, json.getString(key));
				}
				result = btnService.next(json.getString("taskId"), json.getString("actor"), args);

				//yunwq_20180928_需求：401，判断客舱清洁任务更新jm_task表actors字段，为员工工作量统计用
				JSONObject obj= taskDetailService.getTaskDetail(jobTaskId);
				if("KCQJ".equals(obj.getString("JOB_KIND"))) {
					List<String> actors = taskDetailService.getActors(json.getString("actor"));
					if(actors != null && actors.size() != 0) {
						actors.remove(json.getString("actor"));
						taskDetailService.updateTaskActors(jobTaskId,Joiner.on(",").join(actors));
					}
				}
				//PC端代点记录
				if("succeed".equals(result)){
					args.put("operation", UserUtils.getUser().getId());
					jobService.writeSurrogateLog(args);
				}
			} else {
				result = "处理失败！为找活动任务或处理人。";
			}
		} catch (Exception e) {
			result = e.toString();
		}
		return result;
	}

	/**
	 * 完成
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doComplete")
	public String complete(HttpServletRequest request) {
		return "succeed";
	}

	/**
	 * 转办
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doTransfer")
	public String transferMajor(HttpServletRequest request) {
		String result = "succeed";
		String jobTaskId = request.getParameter("jobTaskId");
		// 判断任务状态
		String status = jobService.getTaskStatus(jobTaskId);
		if(!"2".equals(status)){
			return "任务不是执行中，不能被转交！";
		}
		String targetActor = request.getParameter("targetActor");
		JSONObject json = taskService.getFlowTaskByJob(jobTaskId);
		Map<String, String> params = new HashMap<String, String>();
		User user = UserUtils.getUser();
		// 当前登录人部门
		params.put("officeId", user.getOffice().getId());
		// 当前登录人
		params.put("userId", user.getId());
		// 期望返回状态码
		params.put("state", "1");
		//查询状态
		String flag = jobService.getJmSemaState(params);
		if("1".equals(flag)){
			if (json.containsKey("taskId") && json.containsKey("actor") && StringUtils.isNotEmpty(json.getString("taskId"))
					&& StringUtils.isNotEmpty(json.getString("actor"))) {
				Map<String, String> args = getAllParameter(request);
				Set<String> keys = json.keySet();
				for (String key : keys) {
					args.put(key, json.getString(key));
				}
				args.put("userId", user.getId());
				btnService.transferMajor(json.getString("taskId"), json.getString("actor"), targetActor, args);
			} else {
				result = "处理失败！为找活动任务或处理人。";
			}
		} else {
			result = "您操作时间过长人员信息已发生变化，请重新选择作业人！";
		}
		return result;
	}
	
	/**
	 * 转办（预排）
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doTransferPre")
	public String transferMajorPre(HttpServletRequest request) {
		String result = "succeed";
		String jobTaskId = request.getParameter("jobTaskId");
		String targetActor = request.getParameter("targetActor");
		// 判断任务状态是否是未开始
		String status = jobService.getTaskStatus(jobTaskId);
		// 如果不是未开始则转调原来的方法
		if(!"1".equals(status)){
			return transferMajor(request);
		}else{
			// 更新操作人员
			Map<String, String> params = new HashMap<String, String>();
			// 当前登录人部门
			params.put("officeId", UserUtils.getUser().getOffice().getId());
			// 当前登录人
			params.put("userId", UserUtils.getUser().getId());
			// 期望返回状态码
			params.put("state", "1");
			//查询状态
			String flag = jobService.getJmSemaState(params);
			if("1".equals(flag)){
				jobService.updateTaskActor(jobTaskId,targetActor);
			} else {
				result = "您操作时间过长人员信息已发生变化，请重新选择作业人！";
			}
		}
		return result;
	}

	/**
	 * 驳回到上一步
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doBack")
	public String back(HttpServletRequest request) {
		return "succeed";
	}

	/**
	 * 终止
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doTermination")
	public String termination(HttpServletRequest request) {
		String result = "succeed";
		String jobTaskId = request.getParameter("jobTaskId");
		JSONObject json = taskService.getFlowTaskByJob(jobTaskId);
		if (json.containsKey("taskId") && json.containsKey("actor") && StringUtils.isNotEmpty(json.getString("taskId"))
				&& StringUtils.isNotEmpty(json.getString("actor"))) {
			Map<String, String> args = getAllParameter(request);
			Set<String> keys = json.keySet();
			for (String key : keys) {
				args.put(key, json.getString(key));
			}
			args.put("userId", UserUtils.getUser().getId());
			btnService.termination(json.getString("taskId"), json.getString("actor"), args);
		} else {
			result = "处理失败！为找活动任务或处理人。";
		}
		return result;
	}
	
}
