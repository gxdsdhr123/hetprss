package com.neusoft.prss.taskmonitor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.nodetime.service.NodeTimeService;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Airport;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Flight;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Person;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Task;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Terminal;
import com.neusoft.prss.taskmonitor.dao.TaskMonitor13Dao;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
/**
 * 行李室
 * @author xuhw
 *
 */
@Service("taskMonitorService13")
public class TaskMonitor13ServiceImpl extends TaskMonitorServiceImpl {
	
	/**
	 * 日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TaskMonitor13Dao taskMonitorDao;
	
	@Autowired
	private NodeTimeService nodeTimeService;
	
	// 机坪排序列表
	private final List<String> AIRPORT_SORT_ARR = Arrays.asList(new String[]{"待排","停用"});
	
	@Override
	public TaskMonitorVO getTaskMonitorData(String dayOrNight,Integer isPart,Map<String, String> params) throws Exception {
		
		String inOrOut = params.get("inOrOut");
		
		TaskMonitorVO taskMonitor = new TaskMonitorVO();
		
		// 手持机在线人员
		List<String> onlineUsers = getOnlineUsers();
		
		/* 人员任务数据 */
		// 正常上屏人员任务
		List<HashMap<String, Object>> personDataMap = taskMonitorDao.getPersonTaskData(dayOrNight,isPart,inOrOut);
		getPersonData(personDataMap , taskMonitor, onlineUsers,false);
		
		// 白班夜班不更新加班人员，不管手持端
		if(StringUtils.isEmpty(dayOrNight)){
			
			// 获取手持机在线不满足上屏规则的人员信息
			if(onlineUsers.size() > 0){
				List<HashMap<String, Object>> onlinePersonDataMap = taskMonitorDao.getPersonTaskByPersonIds(onlineUsers,isPart,inOrOut);
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
		getFlightData(taskMonitor);
		
		/*计算机坪宽度及位置*/
		setNumToAirport(taskMonitor);
		
		return taskMonitor;
	}

	/**
	 * 航班任务数据
	 * @param taskMonitor
	 */
	private void getFlightData(TaskMonitorVO taskMonitor) {
		List<HashMap<String, Object>> flightDataMap = taskMonitorDao.getFlightTaskData();
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
			// 进港展示时间
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
			String inActstandCode = (String)data.get("IN_ACTSTAND_CODE");
			// 出港机位
			String outActstandCode = (String)data.get("OUT_ACTSTAND_CODE");
			// 进港机位类型
			String inActstandKind = (String)data.get("IN_ACTSTAND_KIND");
			// 出港机位类型
			String outActstandKind = (String)data.get("OUT_ACTSTAND_KIND");
			// 航班显示颜色
			Integer ifGreen = data.get("FLT_COLOR") == null ? 0 : ((BigDecimal)data.get("FLT_COLOR")).intValue();
			// 是否红字（0 不延误 1 进港延误 2 出港延误 3 都延误）
			Integer ifRed = data.get("IF_RED") == null ? 0 : ((BigDecimal)data.get("IF_RED")).intValue();
			// 显示颜色
			Integer ifColor = data.get("TASK_COLOR") == null ? 0 : ((BigDecimal)data.get("TASK_COLOR")).intValue();
			/*// 是否冲突
			Integer ifConflict = data.get("IF_CONFLICT") == null ? 0 : ((BigDecimal)data.get("IF_CONFLICT")).intValue();*/
			// 航班进出港标识（1为进港，2为出港，3为进出港）
			String inOutType = data.get("IN_OUT_TYPE") == null ? "" : data.get("IN_OUT_TYPE").toString();
			// 任务进出港标识（A为进港，D为出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务ID
			String taskId = data.get("ID") == null ? "" : data.get("ID").toString();
			// 任务名称
			String taskName = data.get("NAME") == null ? "" : data.get("NAME").toString();
			// 任务类别简称
			String jobTypeName = data.get("JOB_TYPE_NAME") == null ? "" : data.get("JOB_TYPE_NAME").toString();
			// 任务模板ID
			String procId = (String)data.get("PROC_ID");
			// 作业人ID
			String operator = (String)data.get("OPERATOR");
			// 作业人名称
			String operatorName = (String)data.get("OPERATOR_NAME");
			// 任务节点状态（文字）
			String status = (String)data.get("NODE_STATE");
			// 0 未分配 、4 待排
			String jobState = (String)data.get("JOB_STATE");
			// 流程ID
			String orderId =  (String)data.get("ORDER_ID2");
			// 作业类型
			String jobType = (String)data.get("JOB_TYPE");
			// 任务描述
			String taskDescribe = (String)data.get("DISPLAY_NAME");
			
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
				flight.setIfGreen(ifGreen);
				flight.setIfRed(ifRed);
				flight.setInFltid(inFltid);
				flight.setOutFltid(outFltid);
				flight.setInFltNum(inFltNum);
				flight.setOutFltNum(outFltNum);
				flight.setInTime(inTime);
				flight.setOutTime(outTime);
				flight.setInVipFlag(inVipFlag);
				flight.setOutVipFlag(outVipFlag);
				flight.setInOutFlag(inOutType);
				flight.setInSeat(inActstandCode);
				flight.setOutSeat(outActstandCode);
				flight.setInActKind(inActstandKind);
				flight.setOutActKind(outActstandKind);
				
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
				task.setTaskDescribe(taskDescribe);
				task.setOrderId(orderId);
				task.setProcId(procId);
				task.setPersonId(operator);
				task.setPersonName(operatorName);
				task.setInFltid(inFltid);
				task.setOutFltid(outFltid);
				task.setStatus(ifColor.toString());
				task.setTaskStatus(status);
				task.setJobState(jobState);
				task.setJobType(jobType);
				task.setInOutFlag(inOutFlag);
				task.setTaskStyle(jobTypeName);
				if("A".equals(inOutFlag)){
					task.setFltid(inFltid);
					task.setFlightName(inFltNum);
				}else if("D".equals(inOutFlag)){
					task.setFltid(outFltid);
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
	 * @param add 向前插入
	 */
	private void getPersonData(List<HashMap<String, Object>>personDataMap ,TaskMonitorVO taskMonitor,
			List<String> onlineUsers,boolean add) {
		
		for(HashMap<String, Object> data : personDataMap){
			// 任务ID
			String taskId = data.get("TASK_ID") == null ? "" : data.get("TASK_ID").toString();
			// 任务名称
			String taskName = data.get("NAME") == null ? "" : data.get("NAME").toString();
			// 任务类别简称
			String jobTypeName = data.get("JOB_TYPE_NAME") == null ? "" : data.get("JOB_TYPE_NAME").toString();
			// fltid
			String fltid = data.get("FLTID") == null ? "" : data.get("FLTID").toString();
			// 航班号
			String fltName = (String)data.get("FLIGHT_NUMBER");
			// 任务模板ID
			String procId = (String)data.get("PROC_ID");
			// 作业人ID
			String operator = (String)data.get("OPERATOR") ;
//			String operator = (String)data.get("ID") ;
			// 作业人名称
			String operatorName = (String)data.get("OPERATOR_NAME");
			// 机位区域
			String areaName = (String)data.get("POS_OP");
			// 车辆编号
			String carId = (String)data.get("CAR_ID");
			// 航班进出港标识（A为进港，D为出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务状态 (汉字：启、未等)
			String status = (String)data.get("STATUS");
			// 是否被停用
			Integer ifStop = data.get("IF_STOP") == null ? 0 : ((BigDecimal)data.get("IF_STOP")).intValue();
			// 人员完成作业数
			Integer finishNum = data.get("FINISH_NUM") == null ? 0 : ((BigDecimal)data.get("FINISH_NUM")).intValue();
			// 是否加星号（快下班的人）
			Integer ifStar = data.get("IF_STAR") == null ? 0 : ((BigDecimal)data.get("IF_STAR")).intValue();
			// 是否加好（加班人员）
			Integer ifPlus = data.get("IF_PLUS") == null ? 0 : ((BigDecimal)data.get("IF_PLUS")).intValue();
			// 机坪区域是否变红
			//Integer ifRed = data.get("IF_RED") == null ? 0 : ((BigDecimal)data.get("IF_RED")).intValue();
			// 是否加下划线（规则外手持机登陆但未设置加班）
			Integer ifUnderLine = data.get("IF_UNDERLINE") == null ? 0 : ((BigDecimal)data.get("IF_UNDERLINE")).intValue();
			// 任务状态
			String jobState = (String)data.get("JOB_STATE");
			
			// 航站楼/机坪名称
			String pos = "";
			String posId = "";
			if(add){
				posId = pos = "待排";
			}else if(ifStop==1){
				posId = pos = "停用";
			}
			
			// 机坪数据
			List<Airport> airportList = taskMonitor.getAirport();
			if(airportList == null){
				airportList = new ArrayList<TaskMonitorVO.Airport>();
				taskMonitor.setAirport(airportList);
			}
			Airport airport = null;
			if(airportList.contains(new Airport(posId))){
				airport = airportList.get(airportList.indexOf(new Airport(posId)));
			}else{
				airport = new Airport();
				airport.setId(posId);
				airport.setName(pos);
				airport.setType(2);
				airportList.add(airport);
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
				if(add){
					persons.add(0,person);
				}else{
					persons.add(person);
				}
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
				task.setTaskStatus(status);
				task.setInOutFlag(inOutFlag);
				task.setTaskName(taskName);
				task.setFltid(fltid);
				task.setJobState(jobState);
				task.setTaskStyle(jobTypeName);
				if("D".equals(inOutFlag)){
					task.setStatus("1");
				}else{
					task.setStatus("2");
				}
				
				tasks.add(task);
			}
		}
	}


	/**
	 * 设置机坪数量及位置
	 * @param taskMonitor
	 */
	private TaskMonitorVO setNumToAirport(TaskMonitorVO taskMonitor) {
		// 机坪列表
		List<Airport> airports = taskMonitor.getAirport();
		// 机坪排序
		if(airports == null){
			return taskMonitor;
		}
		
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
		
		// 平均分主机坪区
		Airport port1 = new Airport();
		port1.setName("");
		port1.setType(1);
		port1.setPersons(new ArrayList<TaskMonitorVO.Person>());
		Airport port2 = new Airport();
		port2.setName("");
		port2.setType(2);
		port2.setPersons(new ArrayList<TaskMonitorVO.Person>());
		Airport mainPort = airports.get(0);
		if("".equals(mainPort.getName())){
			for(int i = 0; i < mainPort.getPersons().size() ; i ++){
				if(i <= mainPort.getPersons().size()/2){
					port1.getPersons().add(mainPort.getPersons().get(i));
				}else{
					port2.getPersons().add(mainPort.getPersons().get(i));
				}
			}
			airports.remove(0);
			airports.add(0,port1);
			airports.add(0,port2);
		}
		
		// 人员排序
		if(taskMonitor.getAirport() != null){
			// 循环所有人员
			taskMonitor.getAirport().stream().forEach(airport -> {
				if(airport.getPersons()!=null){
					// 按人员任务数倒序
					airport.setPersons(airport.getPersons().stream().sorted(Comparator.comparing(Person::getIfStar).reversed()
							.thenComparing(Comparator.comparing(Person::getTaskSize))).collect(Collectors.toList()));
				}
			});
		}
		
		return taskMonitor;
	}
	
	@Override
	public TaskFlightInfo getFlightInfo(String fltid,String type) throws Exception {
		TaskFlightInfo taskFlightInfo = new TaskFlightInfo();
		if("1".equals(type)){
			taskFlightInfo = taskMonitorDao.getInFlight(fltid);
		}else if("2".equals(type)){
			taskFlightInfo = taskMonitorDao.getOutFlight(fltid);
		}else if("3".equals(type)){
			taskFlightInfo = taskMonitorDao.getInOutFlight(fltid);
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
		String jobKind = UserUtils.getCurrentJobKind().get(0).getKindCode();
		return taskMonitorDao.getRules(jobKind,jobType,inOut);
	}

}
