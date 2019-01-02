package com.neusoft.prss.taskmonitor.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.arrange.service.ArrangeService;
import com.neusoft.prss.arrange.service.WorkerArrangeService;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.common.entity.TaskOperLogEntity;
import com.neusoft.prss.common.service.OperLogWriteService;
import com.neusoft.prss.nodetime.service.NodeTimeService;
import com.neusoft.prss.scheduling.service.JobManageService;
import com.neusoft.prss.taskmonitor.dao.TaskMonitorDao;
import com.neusoft.prss.taskmonitor.entity.TaskInfo;
import com.neusoft.prss.taskmonitor.entity.TaskNode;
import com.neusoft.prss.taskmonitor.entity.TaskNode.Node;
import com.neusoft.prss.taskmonitor.service.TaskMonitorService;
import com.neusoft.prss.workflow.service.TaskExtraService;
import com.neusoft.prss.workflow.service.WFBtnEventService;
/**
 * 通用
 * @author xuhw
 *
 */
public abstract class TaskMonitorServiceImpl extends BaseService implements TaskMonitorService {

	@Autowired
	protected TaskMonitorDao taskMonitorDao;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private ArrangeService arrangeService;
	
	@Autowired
	private NodeTimeService nodeTimeService;
	
	@Autowired
	private WFBtnEventService btnService;
	
	@Autowired
	private  TaskExtraService taskExtraService;
	
	@Autowired
	private OperLogWriteService writeLogService;
	
	@Autowired
	private JobManageService jobService;
	
	/**
	 * @return 返回格式 a,b a为是否强制改变开关状态，b为当前开关状态
	 */
	@Override
	public String defSwitch() {
		String key = "DEF_SWITCH";
		Integer lastSate = (Integer)UserUtils.getSession().getAttribute(key);
		Integer nowSate = taskMonitorDao.getDefSwitch();
//		logger.info("defSwitch -> [lastSate]"+lastSate + ",[nowSate]" + nowSate);
		String state = "0,";
		if(lastSate == null){
			state = "1,";
		}else{
			if(lastSate.intValue() != nowSate.intValue()){
				state = "1,";
			}
		}
		state += nowSate;
		UserUtils.getSession().setAttribute(key, nowSate);
		return state;
	}

	@Override
	public List<String> highTasks(String taskId) throws Exception {
		logger.info("highTasks -> [taskId]"+taskId);
		return taskMonitorDao.highTasks(taskId);
	}

	@Override
	public List<String> getSelectPersons(String taskId)  throws Exception{
		List<String> resultList = new ArrayList<String>();
		// 没有作业类型无权限操作
		if(UserUtils.getCurrentJobKind() == null || UserUtils.getCurrentJobKind().size() == 0){
			return resultList;
		}
		
		Map<String, Object> taskInfo = taskMonitorDao.getJmTask(taskId);
		if(taskInfo == null){
			return resultList;
		}
		logger.info("getSelectPersons -> [taskInfo]"+taskInfo);
		// fltid
		String fltId = ((BigDecimal)taskInfo.get("FLTID")).toString();
		// 任务类型
		String jobType =  (String)taskInfo.get("JOB_TYPE");
		// 计算开始结束时间
		String startTime = "";
		String endTime = "";
		String startTm = (String)taskInfo.get("START_TM");
		String eStartTm = (String)taskInfo.get("E_START_TM");
		String eEndTm = (String)taskInfo.get("E_END_TM");
		if(!StringUtils.isEmpty(startTm)){
			// 开始时间即当前时间
			startTime = DateUtils.getDateTime();
			// 计算结束时间戳
			long endS = DateUtils.parseDate(eEndTm).getTime() - 
					DateUtils.parseDate(eStartTm).getTime() + DateUtils.parseDate(startTm).getTime();
			endTime = DateUtils.formatDateTime(new Date(endS));
			// 如果任务干超时了，直接传入计划时间
			if(endS <=  System.currentTimeMillis()){
				startTime = eStartTm;
				endTime = eEndTm;
			}
		}else{
			startTime = eStartTm;
			endTime = eEndTm;
		}
		// 调用接口，查询可分配的人及最优排序
		JSONObject json = new JSONObject();
		json.put("fltid", fltId);
		json.put("job_type", jobType);
		json.put("startTm",startTime);
		json.put("endTm",endTime);
		
		JSONArray jsonArr = JSONArray.parseArray(arrangeService.manualSelectDriver(json.toJSONString(),UserUtils.getCurrentJobKind().get(0).getKindCode()).toString());
		for(int i=0;i<jsonArr.size();i++){
			resultList.add(jsonArr.getJSONObject(i).getString("workerId"));
		}
		return resultList;
	}

	@Override
	public ResponseVO<String> operatorReturn(String operator, String posType,
			String posName) {
		if(UserUtils.getCurrentJobKind().size() == 0){
			return ResponseVO.<String>error().setMsg("对不起，您没有权限操作！");
		}
		if(StringUtils.isEmpty(operator) || StringUtils.isEmpty(posType) || StringUtils.isEmpty(posName)){
			return ResponseVO.<String>error().setMsg("参数错误！");
		}
		// 检查人员是否有未完成的任务
		int count = taskMonitorDao.getUnfinishTaskCount(operator);
		if(count > 0 ){
			return ResponseVO.<String>error().setMsg("该人员有未完成的任务，无法召回！");
		}
		// 人员位置入库
		Map<String, Object> newWorkerPos = new HashMap<String, Object>();
		newWorkerPos.put("workerId", operator);
		newWorkerPos.put("posType", posType);
		newWorkerPos.put("pos", posName);
		newWorkerPos.put("operator", UserUtils.getUser().getId());
		newWorkerPos.put("officeId", UserUtils.getUser().getOffice().getId());
		taskMonitorDao.saveWorkerPos(newWorkerPos);
		return ResponseVO.<String>build();
	}

	@Override
	public List<String> getOnlineUsers() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		// cp获取设备是否在线的接口url
		String uri = Global.getConfig("cp.onlines.url");
		// 登陆状态token前缀
		String tokenPrefix = Global.getConfig("TOKEN_PREFIX");
		// 登陆状态查询key
		String tokenKey = Global.getConfig("TOKEN_KEY");
		// 请求参数（格式）：{"Tokens":["P_1329","M_3219",.......]};(P:PC、M:手持机)
		JSONObject param = new JSONObject();
		// 取得登录状态map
		Map<String, String> loginMap = cacheService.getMap(tokenKey);
		JSONArray arr = new JSONArray();
		if(loginMap!=null){
			for(Map.Entry<String, String> entry : loginMap.entrySet()) {
				if(entry.getKey().startsWith(tokenPrefix)) {
					arr.add(entry.getKey().replace(tokenPrefix, "M_"));
				}
			}
		}
		param.put("Tokens", arr);
		// 调用查询接口（返回格式：{"onlines":["P_1329","M_3219"]}）
		JSONObject onlines = restTemplate.postForObject(uri, param,com.alibaba.fastjson.JSONObject.class);
		List<String> response = new ArrayList<String>();
		if(onlines.containsKey("onlines")) {
			JSONArray userList = onlines.getJSONArray("onlines");
			for(int i=0;i<userList.size();i++) {
				response.add(userList.getString(i).replace("P_", "").replaceAll("M_", ""));
			}
		}
		// 日志记录
		logger.debug("api:" + arr.toJSONString() + "    cp:"+response.toString());
		// 返回在线人员列表（格式：["1329","3219"]）
		return response;
	}

	@Override
	public JSONArray getShifts() throws Exception {
		String officeId = UserUtils.getUser().getOffice().getId();
		JSONArray shifts = taskMonitorDao.getShifts(officeId);
		return shifts;
	}
	
	private String getClosestShiftStartTime(String start,String s1,String s2,String s3) {
		List<String> arr = new ArrayList<String>();
		arr.add(s1);
		arr.add(s2);
		arr.add(s3);
		String time = "2359+";
		for(int i=0;i<arr.size();i++) {
			String t = arr.get(i);
			if(gt(time,t) && gt(t,start)) {
				time = t;
			}
		}
		return time;
	}
	
	@Override
	public String setOverWorkTime(String ids, String start, String end) {
		if(UserUtils.getCurrentJobKind().size() == 0){
			return "对不起，您没有权限操作！";
		}
		// 校验时间格式
		String regex = "^(0\\d|1\\d|2[0-3])[0-5]\\d\\+?$";
		if(!start.matches(regex)){
			return "开始时间格式不正确";
		}
		if(!end.matches(regex)){
			return "结束时间格式不正确";
		}
		if(gt(start, end)){
			return "结束时间不能小于开始时间";
		}
		String officeId = UserUtils.getUser().getOffice().getId();
		// 是否次日
		String isTomorrow = start.endsWith("+")?"1":"0";
		if("1".equals(isTomorrow)){
			// 如果是次日，则开始和结束时间都去掉加号，之后所有操作均为次日排班
			start = start.replace("+", "");
			end = end.replace("+", "");
		}
		String[] id = ids.split(",");
		String workerIds = "";
		for(int i=0;i<id.length;i++) {
			workerIds += "'"+id[i]+"',";
		}
		workerIds = workerIds.substring(0, workerIds.length()-1);
		// 校验加班时间是否重合
		if("1".equals(isTomorrow)){
			// 如果是次日排班则与第二天班制对比
			JSONArray workerInfo = taskMonitorDao.getWorkerShiftsInfo(workerIds,"1");
			for(int j=0;j<workerInfo.size();j++) {
				JSONObject w = workerInfo.getJSONObject(j);
				if(!StringUtils.isEmpty(w.getString("SHIFTS_ID")) && "-1".equals(w.getString("SHIFTS_ID")) && !gt(start,w.getString("ETIME1_LABEL")) && gt(end,w.getString("STIME1_LABEL"))) {
					return w.getString("NAME")+"已设置加班时间与该时间段有重合";
				}
				if(ifClash(start,end,w.getString("STIME1_LABEL"),w.getString("ETIME1_LABEL"),w.getString("STIME2_LABEL"),w.getString("ETIME2_LABEL"),w.getString("STIME3_LABEL"),w.getString("ETIME3_LABEL"))) {
					return w.getString("NAME")+"的开始加班时间在正常排班时间中";
				}
			}
		}else{
			// 如果开始时间没有加号则与当天班制对比
			JSONArray workerInfo = taskMonitorDao.getWorkerShiftsInfo(workerIds,"0");
			for(int j=0;j<workerInfo.size();j++) {
				JSONObject w = workerInfo.getJSONObject(j);
				if(!StringUtils.isEmpty(w.getString("SHIFTS_ID")) && "-1".equals(w.getString("SHIFTS_ID")) && !gt(start,w.getString("ETIME1_LABEL")) && gt(end,w.getString("STIME1_LABEL"))) {
					return w.getString("NAME")+"已设置加班时间与该时间段有重合";
				}
				if(ifClash(start,end,w.getString("STIME1_LABEL"),w.getString("ETIME1_LABEL"),w.getString("STIME2_LABEL"),w.getString("ETIME2_LABEL"),w.getString("STIME3_LABEL"),w.getString("ETIME3_LABEL"))) {
					return w.getString("NAME")+"的开始加班时间在正常排班时间中";
				}
			}
		}
		for(String idstr : id) {
			// 今日排班
			JSONArray warr = taskMonitorDao.getWorkerShiftsInfo("'"+idstr+"'","0");
			// 取次日排班
			JSONArray tomorrowWarr = taskMonitorDao.getWorkerShiftsInfo("'"+idstr+"'","1");
			
			String startTime = lableToTime(start);
			String finalStart = start;
			String endTime = lableToTime(end);
			String finalEnd = end;
			// 根据今日排班自动计算结束时间
			if("0".equals(isTomorrow) && warr.size() > 0) {
				for(int k=0;k<warr.size();k++) {
					JSONObject w = warr.getJSONObject(k);
					if(!"-1".equals(w.getString("SHIFTS_ID"))) {
						if((gt(end,w.getString("STIME1_LABEL")) && !gt(end,w.getString("ETIME1_LABEL"))) 
								|| (gt(end,w.getString("STIME2_LABEL")) && !gt(end,w.getString("ETIME2_LABEL")))
								|| (gt(end,w.getString("STIME3_LABEL")) && !gt(end,w.getString("ETIME3_LABEL")))) {
								finalEnd = getClosestShiftStartTime(start, w.getString("STIME1_LABEL"), w.getString("STIME2_LABEL"), w.getString("STIME3_LABEL"));
								endTime = lableToTime(finalEnd);
							}
					}
				}
			}
			// 如果结束时间是次日，或者今日未找到结束时间之后的班制，根据次日排班自动计算结束时间
			if(("1".equals(isTomorrow) || end.endsWith("+") && finalEnd.equals(end)) && tomorrowWarr.size() > 0){
				for(int k=0;k<tomorrowWarr.size();k++) {
					JSONObject w = tomorrowWarr.getJSONObject(k);
					if(!"-1".equals(w.getString("SHIFTS_ID"))) {
						if((gt(end.replace("+", ""),w.getString("STIME1_LABEL")) && !gt(end.replace("+", ""),w.getString("ETIME1_LABEL"))) 
								|| (gt(end.replace("+", ""),w.getString("STIME2_LABEL")) && !gt(end.replace("+", ""),w.getString("ETIME2_LABEL")))
								|| (gt(end.replace("+", ""),w.getString("STIME3_LABEL")) && !gt(end.replace("+", ""),w.getString("ETIME3_LABEL")))) {
							// 如果是次日排班
							if("1".equals(isTomorrow)){
								finalEnd = getClosestShiftStartTime(start, w.getString("STIME1_LABEL"), w.getString("STIME2_LABEL"), w.getString("STIME3_LABEL"));
							}else{
								finalEnd = getClosestShiftStartTime("0000", w.getString("STIME1_LABEL"), w.getString("STIME2_LABEL"), w.getString("STIME3_LABEL"));
								finalEnd += "+";
							}
							endTime = lableToTime(finalEnd);
						}
					}
				}
			}
			taskMonitorDao.setOverWorkTime(officeId,idstr,startTime,endTime,finalStart,finalEnd,isTomorrow);
		}
		return null;
	}
	
	private boolean ifClash(String start,String end,String s1,String e1,String s2,String e2,String s3,String e3) {
		boolean clash = false;
		if(((gt(start,s1)||start.equals(s1))&&!gt(start,e1)) || ((gt(start,s2)||start.equals(s2))&&!gt(start,e2)) || ((gt(start,s3)||start.equals(s3))&&!gt(start,e3))) {
			clash = true;
		}
		return clash;
	}
	private boolean gt(String t1,String t2) {
		if(StringUtils.isEmpty(t2)) {
			return false;
		}
		if(t1.contains("+") && !t2.contains("+")) {
			return true;
		}else if(!t1.contains("+") && t2.contains("+")) {
			return false;
		}else if(Integer.parseInt(t1.replace("+", "")) > Integer.parseInt(t2.replace("+", ""))) {
			return true;
		}else {
			return false;
		}
	}
	private String lableToTime(String label) {
		Date date=new Date();//取时间
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(date);
	    if(label.contains("+")) {
	    	calendar.add(Calendar.DATE,1);
	    }
	    date=calendar.getTime(); 
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = formatter.format(date);
	    label = label.replace("+", "");
	    return dateString+" "+label.substring(0, 2)+":"+label.substring(2)+":00";
	}

	/**
	 * 动态取任务节点
	 */
	@Override
	public TaskNode getTaskInfo(String taskId) throws Exception {
		// 查询实际时间
		TaskNode taskInfo =  taskMonitorDao.getTaskInfo(taskId);
		
		List<Node> nodes = getTaskNodes(taskId);
		taskInfo.setNodes(nodes);
		return taskInfo;
	}

	/**
	 * 根据任务ID查节点
	 * @param taskId
	 * @return
	 */
	protected List<Node> getTaskNodes(String taskId) {
		// 查询预计时间
		List<Node> nodes = taskMonitorDao.getNodeIds(taskId);
		
		// 查询预计时间
		List<Node> nodeVals = taskMonitorDao.getNodeVal(taskId);
		Map<String, String> valMap = new HashMap<String, String>();
		for(Node node : nodeVals){
			valMap.put(node.getId().toString(), node.getaVal());
		}
		
		for(Node node : nodes){
			String nodeId = node.getId().toString();
			String eVal = DateUtils.formatToFltDate(nodeTimeService.getNodeTime(taskId,nodeId));
			String aVal = valMap.get(nodeId);
			
			node.seteVal(eVal);
			node.setaVal(aVal);
		}
		return nodes;
	}

	@Override
	public List<String> getWorkingGroupMember(String id) throws Exception {
		String officeId = UserUtils.getUser().getOffice().getId();
		return taskMonitorDao.getWorkingGroupMember(officeId,id);
	}

	@Override
	public List<HashMap<String,Object>> selectFengong(String operator, String officeId,String schemaId) throws Exception {
		return taskMonitorDao.selectFengong(operator,officeId,schemaId);
	}
	
	@Override
	public List<HashMap<String,Object>> selectJiWei(String operator, String officeId,String schemaId,String fengongId) throws Exception {
		return taskMonitorDao.selectJiWei(operator,officeId,schemaId,fengongId);
	}
	
	@Override
	public String getUnreadErrorNum(String time,String officeId) throws Exception {
		return Integer.toString(taskMonitorDao.getUnreadErrorNum(time,officeId));
	}
	
	@Override
	public int saveFengongJiwei(String fengongId, String jiweiId,String schemaId,String operator,String officeId) throws Exception {
		int i = 0;
		if("5".equals(schemaId)){
			i = taskMonitorDao.saveFengongJiwei(fengongId,jiweiId,schemaId);
		}else if("6".equals(schemaId)){//航线需要同时修改多条分工
			i = taskMonitorDao.saveFengongJiwei(fengongId,jiweiId,schemaId);
			List<HashMap<String,Object>> fengongList = taskMonitorDao.selectFengong(operator,officeId,schemaId);
			if(fengongList.size() != 1){
				String name = taskMonitorDao.getJiWeiNameById(jiweiId);
				for (int j = 0; j < fengongList.size(); j++) {
					String otherFengongId = fengongList.get(j).get("ID").toString();
					if(!fengongId.equals(otherFengongId)){
						List<HashMap<String,Object>> otherJiWei = taskMonitorDao.selectJiWei(operator,officeId,schemaId,otherFengongId);
						for (int k = 0; k < otherJiWei.size(); k++) {
							if(name.equals(otherJiWei.get(k).get("AREA_NAME"))){
								taskMonitorDao.saveFengongJiwei(otherFengongId,otherJiWei.get(k).get("ID").toString(),schemaId);
							}
						}
					}
				}
				
			}
		}
		return i;
	}
	
	@Override
	public Map<String, Object> getNoCleanData(Map<String, Object> param) throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
        int total= taskMonitorDao.getListCount(param);
        List<Map<String, Object>> rows = taskMonitorDao.getList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
	}
	
	@Override
	public int saveNoClean(String inFltId,String type) throws Exception {
		inFltId = inFltId.split("\\|")[0];
		HashMap<String,Object> map = taskMonitorDao.getInFlightByFltId(inFltId);
		String userid = UserUtils.getUser().getId();
		map.put("operator", userid);
		map.put("status", "1");
		map.put("in_out_flag", "A");
		map.put("type", type);
		int num = taskMonitorDao.saveNoClean(map);
		return num;
	}
	
	@Override
	public int deleteNoCleanByFltId(String inFltId,String type) throws Exception {
		int num = taskMonitorDao.deleteNoCleanByFltId(inFltId,type);
		return num;
	}

	@Override
	public List<Map<String, Object>> getTaskIdsByJobKind(String jobKind,String fltid)
			throws Exception {
		return taskMonitorDao.getTaskIdsByJobKind(jobKind,fltid);
	}

	@Override
	public String flightOutScreen(String jobKind,String fltid) throws Exception {
		 taskMonitorDao.flightOutScreen(jobKind,fltid);
		 return null;
	}

	@Transactional(readOnly=true)
	@Override
	public String releaseVehicle(String id) throws Exception {
		if(StringUtils.isEmpty(id)){
			return "车辆编号不能是空";
		}
		// 释放车辆
		taskMonitorDao.logReleaseVehicle(id,UserUtils.getUser().getId(),UserUtils.getUser().getOffice().getId());
		// 释放人员
		taskMonitorDao.releaseVehicle(id);
		return null;
	}

	
	@Override
	public PageBean<TaskInfo> getTaskList(String operator) throws Exception {
		PageBean<TaskInfo> pageBean = new PageBean<TaskInfo>();
		// 获取保障类型
		String jobKind = UserUtils.getCurrentJobKind().get(0).getKindCode();
		// 查询任务列表
		List<TaskInfo> taskList = taskMonitorDao.getTaskList(operator,jobKind);
		
		for(TaskInfo task : taskList){
			// 查询预计到位时间
			String nodeTime = nodeTimeService.getNodeTime(task.getId(), task.getNodeId());
			if(!"".equals(nodeTime) && null != nodeTime){
				task.setSort(DateUtils.parseDate(nodeTime).getTime());
			}else{
				task.setSort(DateUtils.parseDate("2000-01-01").getTime());
			}
		}
		
		// 根据预计到位时间排序
		taskList.sort(new Comparator<TaskInfo>() {

			@Override
			public int compare(TaskInfo o1, TaskInfo o2) {
				return (int)(o1.getSort() - o2.getSort());
			}
			
		});
		
		pageBean.setRows(taskList);
		return pageBean;
	}

	@Override
	public List<String> startWalkthrough() throws Exception {
		List<String> errorList = new ArrayList<String>();
		// 获取保障类型
		String jobKind = UserUtils.getCurrentJobKind().get(0).getKindCode();
		// 查询所有未启动任务
		List<Map<String, String>> taskInfos = taskMonitorDao.getWalkthroughTask(jobKind);
		if(taskInfos != null){
			// 逐条启动
			for(Map<String, String> taskInfo : taskInfos){
				String msg = btnService.startInstance(taskInfo.get("jobTaskId"), taskInfo.get("procId"), 
						UserUtils.getUser().getId(), taskInfo.get("actor"), taskInfo);
				// 记录未成功ID，前台给予提示
				if(!"succeed".equals(msg)){
					errorList.add(taskInfo.get("jobTaskId"));
				}
			}
		}
		return errorList;
	}

	@Override
	public String releaseOperator(String taskId, String personId,
			boolean autoDispatch) throws Exception {
		// 判断任务状态是否是未开始
		String status = jobService.getTaskStatus(taskId);
		int result = -1;
		if(!"1".equals(status)){
			// 判断任务是否已经开始，开始后不能释放
			int c = jobService.checkTaskStarted(taskId);
			if(c == 0){
				return "不是执行中的任务不能释放人员！";
			}
			// 调用接口
			result = taskExtraService.releaseTaskOperator(taskId, autoDispatch);
		}else{
			int c = taskMonitorDao.releaseTaskOperator(taskId);
			if(c>0){
				result = 0;	
			}
		}
		//写日志
		if(result==0){
			TaskOperLogEntity log = new TaskOperLogEntity();
			log.setUserId(UserUtils.getUser().getId());//操作人
			log.setTaskId(taskId);//任务id
			log.setOperType(7);//操作类型：释放人员
			log.setTermType(1);//操作终端：PC
			log.setWorkerId(personId);//作业人
			writeLogService.writeTaskLog(log);
		}
		return "success";
	}

	
}
