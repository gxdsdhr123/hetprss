package com.neusoft.prss.taskmonitor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.nodetime.service.NodeTimeService;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Airport;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Flight;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Person;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Task;
import com.neusoft.prss.taskmonitor.dao.TaskMonitor7Dao;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
//import com.neusoft.prss.transform.service.DivisionTransformService;
/**
 * 特车保障
 * @author yunwq
 *
 */
@Service("taskMonitorService7")
public class TaskMonitor7ServiceImpl extends TaskMonitorServiceImpl {
	
	/**
	 * 日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 机坪排序列表
	private final List<String> AIRPORT_SORT_ARR = Arrays.asList(new String[]{"待排","停用"});

	@Autowired
	private TaskMonitor7Dao taskMonitorDao;
	
//	@Autowired
//	private CacheService cacheService;
	
//	@Autowired
//    private DivisionTransformService transformService;
	
//	@Autowired
//	private NodeTimeService nodeTimeService;
	
	@Override
	public TaskMonitorVO getTaskMonitorData(String dayOrNight,Integer isPart,Map<String, String> params) throws Exception {
		
		String yOrN = params.get("yOrN");
		String inOrOut = params.get("inOrOut");
		
		TaskMonitorVO taskMonitor = new TaskMonitorVO();
		
		// 手持机在线人员
		List<String> onlineUsers = getOnlineUsers();
		
		/* 人员任务数据 */
		// 正常上屏人员任务
		List<HashMap<String, Object>> personDataMap = taskMonitorDao.getPersonTaskData(dayOrNight,isPart);
		getPersonData(personDataMap , taskMonitor, onlineUsers,false);
		
		// 白班夜班不更新加班人员，不管手持端
		if(StringUtils.isEmpty(dayOrNight)){
			
			// 获取手持机在线不满足上屏规则的人员信息
			if(onlineUsers.size() > 0){
				List<HashMap<String, Object>> onlinePersonDataMap = taskMonitorDao.getPersonTaskByPersonIds(onlineUsers,isPart);
				getPersonData(onlinePersonDataMap ,taskMonitor, onlineUsers,true);
			}
			
			// 获取需设置加班时间的人员
			List<Person> unsetPersons = new ArrayList<Person>();
			List<String> unsetPersonIds = new ArrayList<String>();
			if(taskMonitor.getAirport() != null){
				for(Airport ap : taskMonitor.getAirport()){
					for(Person p : ap.getPersons()){
						// 标记下划线的即为
						if(p.getIfUnderLine() == 1){
							unsetPersonIds.add(p.getPersonId());
							unsetPersons.add(p);
						}
					}
				}
			}
			taskMonitor.setUnsetPersons(unsetPersons);
			
		}
		
		/*航班任务数据*/
		getFlightData(taskMonitor,yOrN,inOrOut);
		
		/*计算机坪宽度及位置*/
//		setNumToAirport(taskMonitor);
		
		return taskMonitor;
	}

	/**
	 * 航班任务数据
	 * @param taskMonitor
	 */
	private void getFlightData(TaskMonitorVO taskMonitor,String yOrN,String inOrOut) {
		List<HashMap<String, Object>> flightDataMap = taskMonitorDao.getFlightTaskData(yOrN,inOrOut);
		for(HashMap<String, Object> data : flightDataMap){
			
			// 航班IN_ID
			String inFltid = data.get("IN_FLTID") == null ? "" : data.get("IN_FLTID").toString();
			// 航班OUT_ID
			String outFltid = data.get("OUT_FLTID") == null ? "" : data.get("OUT_FLTID").toString();
			// 进港航班号
			String inFltNum = data.get("IN_FLIGHT_NUMBER") == null ? "" : data.get("IN_FLIGHT_NUMBER").toString();
			// 出港航班号
			String outFltNum = data.get("OUT_FLIGHT_NUMBER") == null ? "" : data.get("OUT_FLIGHT_NUMBER").toString();
			// 进港展示时间
			String inTime = data.get("IN_TIME") == null ? "" : data.get("IN_TIME").toString();
			// 出港展示时间
			String outTime = data.get("OUT_TIME") == null ? "" : data.get("OUT_TIME").toString();
			// 进港要客标识
			String inVipFlag = (String)data.get("IN_VIP_FLAG");
			// 出港要客标识
			String outVipFlag = (String)data.get("OUT_VIP_FLAG");
			// fltid
			String fltid = inFltid+"|"+outFltid;
			// 航班号
			String flightNumber = inFltNum + "|" + outFltNum;
			// 进港机位
			String inSeat = (String)data.get("IN_ACTSTAND_CODE");
			// 进港机位-远近机位
			String inActstandKind = (String)data.get("IN_ACTSTAND_KIND");
			// 出港机位
			String outSeat = (String)data.get("OUT_ACTSTAND_CODE");
			// 出港机位-远近机位
			String outActstandKind = (String)data.get("OUT_ACTSTAND_KIND");
			// 显示航班背景色
			Integer ifGreen = data.get("FLT_COLOR") == null ? 0 : ((BigDecimal)data.get("FLT_COLOR")).intValue();
			// 航班进出港标识（A为进港，D为出港，AD为进出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务进出港标识（A为进港，D为出港）
			String inOutFlag2 = (String)data.get("IN_OUT_FLAG2");
//			// 是否冲突
//			Integer ifConflict = data.get("IF_CONFLICT") == null ? 0 : ((BigDecimal)data.get("IF_CONFLICT")).intValue();
			// 任务ID
			String taskId = data.get("ID") == null ? "" : data.get("ID").toString();
			// 任务名称
			String taskName = data.get("NAME") == null ? "" : data.get("NAME").toString();
			// 任务模板ID
			String procId = (String)data.get("PROC_ID");
			// 作业人ID
			String operator = (String)data.get("OPERATOR");
			// 作业人名称
			String operatorName = (String)data.get("OPERATOR_NAME");
			// 任务节点状态颜色
//			String status = data.get("IF_COLOR2") == null ? "" : data.get("IF_COLOR2").toString();
			// 任务节点状态（文字）
			String taskStatus = data.get("NODE_STATE") == null ? "" : data.get("NODE_STATE").toString();
			// 车辆编号
			String carId = (String)data.get("NAME2");
			// 6 被释放的任务 、4 待排任务
			String jobState = "";
			if(data.get("JOB_STATE") != null){
				jobState = data.get("JOB_STATE").toString();
			}
			// 任务块颜色（0.白 1.蓝 2.绿 ）
			String taskColor = data.get("TASK_COLOR") == null ? "" : data.get("TASK_COLOR").toString();
			// 流程ID
			String orderId =  (String)data.get("ORDER_ID2");
			// 作业类型
			String jobType = (String)data.get("JOB_TYPE");
			// 任务描述
			String taskDescribe = (String)data.get("SHORT_NAME");
			
			// 航班数据
			List<Flight> flightList = taskMonitor.getyFlight();
			if(flightList == null){
				flightList = new ArrayList<TaskMonitorVO.Flight>();
				taskMonitor.setyFlight(flightList);
			}
			
			Flight flight = null;
			if(flightList.contains(new Flight(fltid))){
				flight = flightList.get(flightList.indexOf(new Flight(fltid)));
			}else{
				flight = new Flight();
				flight.setId(fltid);
				flight.setName(flightNumber);
				flight.setInSeat(inSeat);
				flight.setOutSeat(outSeat);
				if("N".equals(inActstandKind) && !"N".equals(outActstandKind)){
					flight.setIfRed(1);
				}else if(!"N".equals(inActstandKind) && "N".equals(outActstandKind)){
					flight.setIfRed(2);
				}else if("N".equals(inActstandKind) && "N".equals(outActstandKind)){
					flight.setIfRed(3);
				}else{
					flight.setIfRed(4);
				}
				flight.setInFltid(inFltid);
				flight.setOutFltid(outFltid);
				flight.setInFltNum(inFltNum);
				flight.setOutFltNum(outFltNum);
				flight.setInTime(inTime);
				flight.setOutTime(outTime);
				flight.setInVipFlag(inVipFlag);
				flight.setOutVipFlag(outVipFlag);
				flight.setIfGreen(ifGreen);
				flight.setInOutFlag(inOutFlag);
				flightList.add(flight);
			}
			// 任务
			List<Task> tasks = flight.getTask();
			if(tasks == null){
				tasks = new ArrayList<TaskMonitorVO.Task>();
				flight.setTask(tasks);
			}
			if(!StringUtils.isEmpty(taskId)){
				Task task = new Task();
				task.setId(taskId);
				task.setTaskName(taskName);
				task.setOrderId(orderId);
				task.setProcId(procId);
				task.setPersonId(operator);
				task.setPersonName(operatorName);
				task.setFltid(fltid);
				task.setInFltid(inFltid);
				task.setOutFltid(outFltid);
				task.setStatus(taskColor);
				task.setTaskStatus(taskStatus);
				task.setTaskDescribe(taskDescribe);
				task.setCarId(carId);
				task.setJobState(jobState);
				task.setJobType(jobType);
				task.setTaskDescribe(taskDescribe);
				if("A".equals(inOutFlag2)){
					task.setFlightName(inFltNum);
				}else if("D".equals(inOutFlag2)){
					task.setFlightName(outFltNum);
				}
				tasks.add(task);
			}
		}
	}

	/**
	 * 人员任务数据（原上屏规则）
	 * @param personDataMap
	 * @param taskMonitor
	 * @param onlineUsers
	 * @param front 向前插入
	 */
	private void getPersonData(List<HashMap<String, Object>>personDataMap ,TaskMonitorVO taskMonitor,
			List<String> onlineUsers,boolean add) {
		for(HashMap<String, Object> data : personDataMap){
			// 任务ID
			String taskId = data.get("TASK_ID") == null ? "" : data.get("TASK_ID").toString();
			// 任务名称
			String taskName = data.get("NAME") == null ? "" : data.get("NAME").toString();
			// fltId
			String fltid = data.get("FLTID") == null ? "" : data.get("FLTID").toString();
			// 航班号
			String fltName = (String)data.get("FLIGHT_NUMBER");
			// 任务模板ID
			String procId = (String)data.get("PROC_ID");
			// 航站楼/机坪名称
			String pos = data.get("POS") == null ? " " : (String)data.get("POS");
			// 作业人ID
			String operator = (String)data.get("OPERATOR") ;
			// 作业人名称
			String operatorName = (String)data.get("OPERATOR_NAME");
			// 机位区域
			String areaName = (String)data.get("AREA_NAME");
			// 车辆编号
			String carId = (String)data.get("CAR_ID");
			// 车辆型号
			String carType = (String)data.get("CAR_TYPE");
			// 航班进出港标识（A为进港，D为出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务块颜色（0.白 1.蓝 2.绿 ）
			String taskColor = data.get("TASK_COLOR") == null ? "" : data.get("TASK_COLOR").toString();
			// 是否被停用
			Integer ifStop = data.get("IF_STOP") == null ? 0 : ((BigDecimal)data.get("IF_STOP")).intValue();
			// 人员完成作业数
			Integer finishNum = data.get("FINISH_NUM") == null ? 0 : ((BigDecimal)data.get("FINISH_NUM")).intValue();
			// 是否加星号（快下班的人）
			Integer ifStar = data.get("IF_STAR") == null ? 0 : ((BigDecimal)data.get("IF_STAR")).intValue();
			// 是否加好（加班人员）
			Integer ifPlus = data.get("IF_PLUS") == null ? 0 : ((BigDecimal)data.get("IF_PLUS")).intValue();
			// 机坪区域是否变红
			Integer ifRed = data.get("IF_RED") == null ? 0 : ((BigDecimal)data.get("IF_RED")).intValue();
			// 是否加下划线（规则外手持机登陆但未设置加班）
			Integer ifUnderLine = data.get("IF_UNDERLINE") == null ? 0 : ((BigDecimal)data.get("IF_UNDERLINE")).intValue();
			// 任务节点状态颜色
			Integer ifConflict = data.get("IF_CONFLICT") == null ? 0 : ((BigDecimal)data.get("IF_CONFLICT")).intValue();
			// 任务节点状态（文字）
			String taskStatus = data.get("STATUS") == null ? "" : data.get("STATUS").toString();
			// 6 被释放的任务 、4 待排任务
			String jobState = "";
			if(data.get("JOB_STATE") != null){
				jobState = data.get("JOB_STATE").toString();
			}
			if(add){
				pos = "待排";
			}
			if("没有分工".equals(pos)){
				continue;
			}
			// 机坪数据
			List<Airport> airportList = taskMonitor.getAirport();
			if(airportList == null){
				airportList = new ArrayList<TaskMonitorVO.Airport>();
				taskMonitor.setAirport(airportList);
			}
			Airport airport = null;
			if(airportList.contains(new Airport(pos))){
				airport = airportList.get(airportList.indexOf(new Airport(pos)));
			}else{
				airport = new Airport();
				airport.setId(pos);
				airport.setName(pos);
				airport.setIfRed(ifRed);
				airportList.add(airport);
				if("摆渡车组".equals(pos)){
					airport.setType(1);
				}else if("客梯车组".equals(pos)){
					airport.setType(2);
				}else if("牵引车组".equals(pos)){
					airport.setType(3);
				}else{
					airport.setType(3);
				}
			}
			// 机坪人员数据
			List<Person> persons = airport.getPersons();
			if(persons == null){
				persons = new ArrayList<TaskMonitorVO.Person>();
				airport.setPersons(persons);
			}
			Person person = null;
			if(persons.contains(new Person(operator))){
				person = persons.get(persons.indexOf(new Person(operator)));
			}else{
				person = new Person();
				person.setPersonId(operator);
				person.setPersonName(operatorName);
				person.setFinishNum(finishNum.toString());
				person.setCarId(carId);
				person.setIfStop(ifStop);
				person.setAreaName(areaName);
				person.setCarType(carType);
				/*判断人员手持端登陆及车辆绑定状态   0 未登录 1 登陆但未绑定 2 登陆且绑定 */
				if(onlineUsers.contains(operator)){
					// 过滤上坪人员
					onlineUsers.remove(operator);
					person.setStatus(1);
				}else{
					person.setStatus(0);
				}
				person.setIfStar(ifStar);
				person.setIfPlus(ifPlus);
				person.setIfUnderLine(ifUnderLine);
				
				persons.add(person);
			}
			// 任务
			List<Task> tasks = person.getTask();
			if(tasks == null){
				tasks = new ArrayList<TaskMonitorVO.Task>();
				person.setTask(tasks);
			}
			if(!StringUtils.isEmpty(taskId)){
				Task task = new Task();
				task.setId(taskId);
				task.setProcId(procId);
				task.setPersonId(operator);
				task.setPersonName(operatorName);
				task.setCarId(carId);
				task.setFlightName(fltName);
				task.setTaskStatus(taskStatus);
				task.setStatus(taskColor);
				task.setJobState(jobState);
				task.setIfConflict(ifConflict);
				task.setInOutFlag(inOutFlag);
				task.setTaskName(taskName);
				task.setFltid(fltid);
				tasks.add(task);
			}
		}
	}


	/**
	 * 设置机坪数量及位置
	 * @param taskMonitor
	 */
	private TaskMonitorVO setNumToAirport(TaskMonitorVO taskMonitor) {
		// 作业区总宽度
		int totalWorkingWidthNum = 0;
		// 等待区总宽度
		int totalWaitingWidthNum = 0;
		// 分组数
		int groupNum = 3;
		// 机坪列表
		List<Airport> airports = taskMonitor.getAirport();
		// 机坪排序
		if(airports == null){
			return taskMonitor;
		}
		
		// 循环计算机坪宽度
		for(Airport airport : airports){
			String pos = airport.getName();
			// 根据名字区分工作区和待命区
			if(pos.matches("^[\u4e00-\u9fa5]*$")){
				airport.setType(2);
				// 待命区加和
				totalWaitingWidthNum += airport.getPersons().size();
			}else{
				airport.setType(1);
				// 工作区加和
				totalWorkingWidthNum += airport.getPersons().size();
			}
		}
		
		// 工作区行数
		int workingLine = (int)Math.ceil((double)totalWorkingWidthNum/(totalWaitingWidthNum+totalWorkingWidthNum));
		// 待命区行数
		int waitingLine = groupNum - workingLine;
		
		// 根据数量进行排序
		airports.sort(new Comparator<Airport>() {
			@Override
			public int compare(Airport o1, Airport o2) {
				return o2.getPersons().size() - o1.getPersons().size();
			}
		});
		// 根据type排序
		airports.sort(new Comparator<Airport>() {
			@Override
			public int compare(Airport o1, Airport o2) {
				return o1.getType() - o2.getType();
			}
		});
		// 待排、停用排到最后
		airports.sort(new Comparator<Airport>() {
			@Override
			public int compare(Airport o1, Airport o2) {
				int o1Index = AIRPORT_SORT_ARR.indexOf(o1.getName());
				int o2Index = AIRPORT_SORT_ARR.indexOf(o2.getName());
				if(o1Index < 0 && o2Index < 0){
					return 0;
				}else if(o1Index < 0){
					return -1;
				}else if(o2Index < 0){
					return 1;
				}
				return o1Index - o2Index;
			}
		});
		
		// 根据机坪总数及机坪宽度计算机坪位置
		int currentWidthNum = 0; // 当前总长度
		int currentLine = 1;	// 当前行号
		Integer lastType = 0; // 上一个类型
		for(Airport airport : airports){
			// 取区域类型
			Integer type = airport.getType();
			// 如果跟上次类型不同，则重置当前长度并判断换行
			if(type != lastType){
				currentWidthNum = 0;
				// 如果不是第一个则换行
				if(lastType != 0){
					currentLine ++;
				}
			}
			
			int widthNum = airport.getPersons().size();
			
			if(type == 1){
				// 工作区
				if((double)currentWidthNum / totalWorkingWidthNum * workingLine > currentLine){
					currentLine ++;
				}
			}else{
				// 待命区
				if((double)currentWidthNum / totalWaitingWidthNum * waitingLine > currentLine - workingLine){
					currentLine ++;
				}
			}
			airport.setType(currentLine);
			currentWidthNum += widthNum;
			lastType = type;
		}
		return taskMonitor;
	}
	
	@Override
	public TaskFlightInfo getFlightInfo(String fltid,String type) throws Exception {
		TaskFlightInfo taskFlightInfo = new TaskFlightInfo();
		String infltId = fltid.substring(0, fltid.indexOf("|"));
		String outfltId = fltid.substring(fltid.indexOf("|")+1, fltid.length());
		if("A".equals(type)){
			taskFlightInfo = taskMonitorDao.getInFlight(infltId);
		}else if("D".equals(type)){
			taskFlightInfo = taskMonitorDao.getOutFlight(outfltId);
		}else if("AD".equals(type)){
			taskFlightInfo = taskMonitorDao.getInOutFlight(infltId);
		}
		return taskFlightInfo;
	}

	@Override
	public ResponseVO<Map<String, String>> getIfTimeConflict(String operatorId, String taskId,String fltid)  throws Exception{
		String msg = "";
		Map<String, String> dataMap = new HashMap<String, String>();
		int c = taskMonitorDao.getIfOffWork(operatorId, taskId);
		if(c == 1){
			msg += "此人临近下班，不适宜保障该航班！<br/>";
		}
		// 判断任务时间与人员时间是否冲突
		Map<String, Object>  msgData= taskMonitorDao.getIfTimeConflict(operatorId,taskId);
		if( msgData != null ){
			String msg2 = (String) msgData.get("MSG");
			msg += msg2;
			dataMap.put("ids", (String) msgData.get("IDS"));
		}
		if(StringUtils.isEmpty(msg)){
			return ResponseVO.<Map<String, String>>build();
		}else{
			return ResponseVO.<Map<String, String>>error().setMsg(msg).setData(dataMap);
		}
	}


	@Override
	public List<Map<String, Object>> getRules(String jobType, String inOut, String fltid)
			throws Exception {
		return taskMonitorDao.getRules(jobType,inOut);
	}
	
	@Override
	public List<String> highTasks(String taskId) throws Exception {
		logger.info("highTasks -> [taskId]"+taskId);
		return taskMonitorDao.highTasks(taskId);
	}

}
