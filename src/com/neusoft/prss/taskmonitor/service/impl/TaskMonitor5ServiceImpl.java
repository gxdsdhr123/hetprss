package com.neusoft.prss.taskmonitor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.nodetime.service.NodeTimeService;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Airport;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Flight;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Person;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO.Task;
import com.neusoft.prss.taskmonitor.dao.TaskMonitor5Dao;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
import com.neusoft.prss.taskmonitor.entity.TaskNode;
import com.neusoft.prss.taskmonitor.entity.TaskNode.Node;
/**
 * 牵引车
 * @author xuhw
 *
 */
@Service("taskMonitorService5")
public class TaskMonitor5ServiceImpl extends TaskMonitorServiceImpl {
	
	// 机坪排序列表
	private final List<String> AIRPORT_SORT_ARR = Arrays.asList(new String[]{"01-17","33-42","61-70","18-32","43-52","待排","停用"});

	@Autowired
	private TaskMonitor5Dao taskMonitorDao;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private NodeTimeService nodeTimeService;
	
	@Override
	public TaskMonitorVO getTaskMonitorData(String dayOrNight,Integer isPart,Map<String, String> params) throws Exception {
		
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
		getFlightData(taskMonitor);
		
		/*计算机坪宽度及位置*/
		setNumToAirport(taskMonitor);
		
		/*判断机坪是否变红*/
		setAirportIfRed(taskMonitor);
		
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
			// 航班背景色（ 1.绿 2.黄 0.默认 ）
			Integer ifGreen = data.get("FLT_COLOR") == null ? 0 : ((BigDecimal)data.get("FLT_COLOR")).intValue();
			// 任务块背景色（0.白 1.蓝 2.绿 ）
			Integer taskColor = data.get("TASK_COLOR") == null ? 0 : ((BigDecimal)data.get("TASK_COLOR")).intValue();
			/*// 是否有下划线
			Integer ifLine = data.get("IF_XLX") == null ? 0 : ((BigDecimal)data.get("IF_XLX")).intValue();*/
			// etd是否红字 （1.变红 0.不变）
			Integer ifRed = data.get("IF_RED") == null ? 0 : ((BigDecimal)data.get("IF_RED")).intValue();
			// 航班进出港标识（A为进港，D为出港，AD为进出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务进出港类型
			String inOutFlag2 = (String)data.get("IN_OUT_FLAG2");
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
			// 任务节点状态（文字）
			String status = (String)data.get("NODE_STATE");
			// 0 未分配 淡蓝 、4 待排 橙色、6 暂停自动分配任务 黄色
			String jobState = (String)data.get("JOB_STATE");
			// 流程ID
			String orderId =  (String)data.get("ORDER_ID2");
			// 作业类型
			String jobType = (String)data.get("JOB_TYPE");
			// 任务简称
			String shortName = (String)data.get("SHORT_NAME");
			// 机坪
			String apronCode = data.get("POS") == null ? "" : data.get("POS").toString();
			
			
			if(StringUtils.isEmpty(apronCode)){
				continue;
			}
			// 机坪数据
			List<Airport> airportList = taskMonitor.getAirport();
			if(airportList == null){
				airportList = new ArrayList<TaskMonitorVO.Airport>();
				taskMonitor.setAirport(airportList);
			}
			Airport airport = null;
			if(airportList.contains(new Airport(apronCode))){
				airport = airportList.get(airportList.indexOf(new Airport(apronCode)));
			}else{
				airport = new Airport();
				airport.setId(apronCode);
				airport.setName(apronCode);
				airportList.add(airport);
			}
			// 航班数据
			List<Flight> flightList = null;
			flightList = airport.getFlights();
			if(flightList == null){
				flightList = new ArrayList<TaskMonitorVO.Flight>();
				airport.setFlights(flightList);
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
				flight.setInOutFlag(inOutFlag);
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
				task.setOrderId(orderId);
				task.setProcId(procId);
				task.setPersonId(operator);
				task.setPersonName(operatorName);
				task.setInFltid(inFltid);
				task.setOutFltid(outFltid);
				task.setInOutFlag(inOutFlag2);
				task.setStatus(taskColor.toString());
				if("A".equals(inOutFlag2)){
					task.setFltid(inFltid);
					task.setFlightName(inFltNum);
				}else if("D".equals(inOutFlag2)){
					task.setFltid(outFltid);
					task.setFlightName(outFltNum);
				}
				task.setTaskStatus(status);
				task.setJobState(jobState);
				task.setJobType(jobType);
				task.setTaskDescribe(shortName);
				tasks.add(task);
			}
		}
	}

	/**
	 * 人员任务数据（原上屏规则）
	 * @param personDataMap
	 * @param taskMonitor
	 * @param onlineUsers
	 * @param add 加班人员
	 */
	private void getPersonData(List<HashMap<String, Object>>personDataMap ,TaskMonitorVO taskMonitor,
			List<String> onlineUsers,boolean add) {
		
		for(HashMap<String, Object> data : personDataMap){
			// 任务ID
			String taskId = data.get("TASK_ID") == null ? "" : data.get("TASK_ID").toString();
			// 任务名称
			String taskName = data.get("NAME") == null ? "" : data.get("NAME").toString();
			// fltid
			String fltid = data.get("FLTID") == null ? "" : data.get("FLTID").toString();
			// 航班号
			String fltName = (String)data.get("FLIGHT_NUMBER");
			// 任务模板ID
			String procId = (String)data.get("PROC_ID");
			// 航站楼/机坪名称
			String pos = data.get("POS") == null ? " " : (String)data.get("POS");
			// 作业人ID
			String operator = (String)data.get("OPERATOR") ;
//			String operator = (String)data.get("ID") ;
			// 作业人名称
			String operatorName = (String)data.get("OPERATOR_NAME");
			// 机位区域
			String areaName = (String)data.get("FG_POS");
			// 车辆编号
			String carId = (String)data.get("CAR_ID");
			// 航班进出港标识（A为进港，D为出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务状态 (汉字：启、未等)
			String status = (String)data.get("STATUS");
			// 任务块颜色（0.白 1.蓝 2.绿 ）
			Integer taskColor = data.get("TASK_COLOR") == null ? 0 : ((BigDecimal)data.get("TASK_COLOR")).intValue();
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
			
			if(add){
				pos = "待排";
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
				task.setStatus(taskColor.toString());
				task.setInOutFlag(inOutFlag);
				task.setTaskName(taskName);
				task.setFltid(fltid);
				task.setJobState(jobState);
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
		airports.sort(new Comparator<Airport>() {
			@Override
			public int compare(Airport o1, Airport o2) {
				int o1Index = AIRPORT_SORT_ARR.indexOf(o1.getId());
				int o2Index = AIRPORT_SORT_ARR.indexOf(o2.getId());
				if(o1Index < 0){
					return 1;
				}else if(o2Index < 0){
					return -1;
				}
				return o1Index - o2Index;
			}
		});
		
		// 循环计算机坪宽度
		for(Airport airport : airports){
			int fNum = airport.getFlights() == null ? 0 : airport.getFlights().size();
			int pNum = airport.getPersons() == null ? 0 : airport.getPersons().size();
			int widthNum = 0;
			if(pNum >= fNum){
				// 如果人的数量大于航班数则直接取人的数量
				widthNum = pNum;
			}else{
				widthNum = fNum;
			}
			airport.setWidthNum(widthNum);
		}
		// 固定机坪位置
		for(Airport airport : airports){
			if("01-17".equals(airport.getName()) || "33-42".equals(airport.getName()) || "61-70".equals(airport.getName())){
				airport.setType(1);
			}else{
				airport.setType(2);
			}
		}
		return taskMonitor;
	}
	
	/**
	 * 设置机坪数量及位置
	 * @param taskMonitor
	 */
	private TaskMonitorVO setAirportIfRed(TaskMonitorVO taskMonitor) {
		List<Airport> airports = taskMonitor.getAirport();
		for (Airport airport : airports) {
			
			List<Flight> flights = airport.getFlights();
			List<Person> persons = airport.getPersons();
			List<String> idList = new ArrayList<String>(); 
			if(persons == null || persons.size() == 0){
				airport.setIfRed(1);
				continue;
			}else{
				for (Person person : persons) {
					idList.add(person.getPersonId());
				}
				if(flights != null && flights.size() != 0){
					for (Flight flight : flights) {
						List<Task> tasks = flight.getTask();
						for (Task task : tasks) {
							HashMap<String, Object> map = taskMonitorDao.getIfCarDetail(task.getId());
							map.put("taskId", task.getId());
							map.put("idList", idList);
							int c = taskMonitorDao.getIfCarHaveAptitudeOne(map);
							int b = taskMonitorDao.getIfCarHaveAptitudeTwo(map);
							if(c == 0 && b == 0){//只要有一个任务没有人能干，就变红
								airport.setIfRed(1);
								break;
							}
						}
					}
				}
			}
		}
		
		return taskMonitor;
	}
	
	@Override
	public TaskFlightInfo getFlightInfo(String fltid, String type)
			throws Exception {
		if("A".equals(type)){
			// 进港
			return taskMonitorDao.getInFlight(fltid);
		}else if ("D".equals(type)){
			// 出港
			return taskMonitorDao.getOutFlight(fltid);
		}else if("AD".equals(type)){
			// 进出港
			return taskMonitorDao.getInOutFlight(fltid);
		}
		return null;
	}


	@Override
	public ResponseVO<Map<String, String>> getIfTimeConflict(String operatorId, String taskId,String fltid)  throws Exception{
		String msg = "";
		Map<String, String> dataMap = new HashMap<String, String>();
		// 判断人员下班时间与任务时间是否冲突
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

}
