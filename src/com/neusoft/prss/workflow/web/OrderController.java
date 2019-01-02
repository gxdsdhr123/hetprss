package com.neusoft.prss.workflow.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.scheduling.service.JobManageService;
import com.neusoft.prss.workflow.access.Page;
import com.neusoft.prss.workflow.access.QueryFilter;
import com.neusoft.prss.workflow.entity.HistoryOrder;
import com.neusoft.prss.workflow.entity.HistoryTask;
import com.neusoft.prss.workflow.service.OrderService;

@Controller
@RequestMapping(value = "${adminPath}/workflow/order")
public class OrderController extends BaseController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private JobManageService jobService;
	/**
	 * 获取全部流程实例
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(Model model, String orderState, String pageNo) {
		QueryFilter filter = new QueryFilter();
		if (StringUtils.isNotBlank(orderState) && !"-1".equals(orderState)) {
			filter.setState(Integer.valueOf(orderState));
		}
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		Page<HistoryOrder> page = new Page<HistoryOrder>();
		page.setPageNo(Integer.parseInt(pageNo));

		page = orderService.getHistoryOrders(page, filter);
		model.addAttribute("page", page);
		model.addAttribute("orderState", orderState);
		return "prss/workflow/orderList";
	}
	/**
	 * 打开流程监控图
	 * @param model
	 * @param processId
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "display", method = RequestMethod.GET)
	public String display(Model model, String processId, String orderId) {
		model.addAttribute("processId", processId);
		HistoryOrder order = orderService.getHistOrder(orderId);
		model.addAttribute("order", order);
		List<HistoryTask> tasks = orderService.getHistoryTasks(new QueryFilter().setOrderId(orderId));
		model.addAttribute("tasks", tasks);
		model.addAttribute("surrogateRecord", jobService.getTaskSurrogateByOrder(orderId));
		//获取回退过的节点
		Map<String,HistoryTask> rollbackTask = new HashMap<String,HistoryTask>();
		if(tasks!=null&&!tasks.isEmpty()){
			for (int i = tasks.size(); i > 0; i--) {
				HistoryTask task = tasks.get(i - 1);
				if((i - 2)>=0){
					//下一个节点
					HistoryTask nextTask = tasks.get(i - 2);
					//如果后一节点的父节点不是当前接点责是回退节点
					if(nextTask!=null&&!nextTask.getParentTaskId().equals(task.getId())){
						rollbackTask.put(task.getId(), task);
					}
				}
			}
		}
		model.addAttribute("rollbackTask", rollbackTask);
		return "prss/workflow/processView";
	}
	/**
	 * 流程实例json
	 * @param model
	 * @param processId
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "orderJson")
	public JSONObject getOrderJson(Model model, String processId, String orderId) {
		JSONObject orderJson = orderService.getOrderJson(processId, orderId);
		return orderJson;
	}
	/**
	 * 删除实例
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "removeOrder")
	public String removeOrder(String orderId) {
		String result = "succeed";
		try {
			orderService.cascadeRemove(orderId);
		} catch (Exception e) {
			result = e.toString();
		}
		return result;
	}
}
