package com.neusoft.prss.taskmonitor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.neusoft.prss.taskmonitor.dao.TaskMonitor102Dao;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
/**
 * 行李拉运任务分配图
 * @author yunwq
 *
 */
@Service("taskMonitorService102")
public class TaskMonitor102ServiceImpl extends TaskMonitorServiceImpl {
	
	/**
	 * 日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TaskMonitor102Dao taskMonitorDao;
	
	@Autowired
	private NodeTimeService nodeTimeService;
	
	// 机坪排序列表
	private final List<String> AIRPORT_SORT_ARR = Arrays.asList(new String[]{"待排","停用"});
	
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
//		setNumToAirport(taskMonitor);
		// 机坪列表
		List<Airport> airports = taskMonitor.getAirport();
		// 机坪排序
		if(airports != null){
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
		}
		// 如果海航则把海航放到最前边
		if(UserUtils.isPermitted("sc:monitor:hhzt")&&
				!UserUtils.isPermitted("sc:monitor:dhzt")&&!UserUtils.isPermitted("sc:monitor:nhzt")){
			if(airports != null){
				airports.sort(new Comparator<Airport>() {
					@Override
					public int compare(Airport o1, Airport o2) {
						if("海航".equals(o1.getName())){
							return -1;
						}else if("海航".equals(o2.getName())){
							return 1;
						}else{
							return 0;
						}
					}
				});
			}
		}
		
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
			// 航班机型
			String acttypeCode = data.get("ACTTYPE_CODE") == null ? "" : data.get("ACTTYPE_CODE").toString();
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
			// 机位
			String actstandCode = (String)data.get("ACTSTAND_CODE");
			// 行李转盘
			String bagCrsl = data.get("BAG_CRSL") == null ? "" : data.get("BAG_CRSL").toString();
			// 颜色设置 3 绿 2 红 1 灰 0蓝
			Integer ifColor = data.get("IF_COLOR") == null ? 0 : ((BigDecimal)data.get("IF_COLOR")).intValue();
			// 颜色设置 0未排，1以排
//			Integer ifColor2 = data.get("IF_COLOR2") == null ? 0 : ((BigDecimal)data.get("IF_COLOR2")).intValue();
			// 角标（机号判断）
			Integer ifOrange = data.get("SAME_AIRCRAFT_NUMBER") == null ? 0 : Integer.parseInt(data.get("SAME_AIRCRAFT_NUMBER").toString());
			/*// 机坪/登机口
			String title2  = (String)data.get("SECOND_SHOW");
			// ATA/ETA/STD
			String title3  = (String)data.get("THIRD_SHOW");
			// 是否有下划线
			Integer ifLine = data.get("IF_XLX") == null ? 0 : ((BigDecimal)data.get("IF_XLX")).intValue();*/
			//航班红框
			Integer ifBorderRed = data.get("IF_NO_ARRANGE_FLAG") == null ? 0 : ((BigDecimal)data.get("IF_NO_ARRANGE_FLAG")).intValue();
			// 是否红字（0 不延误 1 进港延误 2 出港延误 3 都延误）
			Integer ifRed = data.get("IF_RED") == null ? 0 : ((BigDecimal)data.get("IF_RED")).intValue();
			// 是否冲突
			Integer ifConflict = data.get("IF_CONFLICT") == null ? 0 : ((BigDecimal)data.get("IF_CONFLICT")).intValue();
			// 航班进出港标识（A为进港，D为出港 ，AD为进出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务进出港标识（A为进港，D为出港）
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
			/*// 车辆编号
			String carId = (String)data.get("CAR_ID");*/
			// 0 未分配 、4 待排
			String jobState = (String)data.get("JOB_STATE");
			/*// 旅客人数
			Integer paxNum = data.get("PAX_NUM") == null ? 0 : ((BigDecimal)data.get("PAX_NUM")).intValue();
			// 国际旅客人数
			Integer iPaxNum = data.get("I_PAX_NUM") == null ? 0 : ((BigDecimal)data.get("I_PAX_NUM")).intValue();
			// 国内旅客人数
			Integer dPaxNum = data.get("D_PAX_NUM") == null ? 0 : ((BigDecimal)data.get("D_PAX_NUM")).intValue();*/
			// 流程ID
			String orderId =  (String)data.get("ORDER_ID2");
			// 作业类型
			String jobType = (String)data.get("JOB_TYPE");
			// 任务描述
			String taskDescribe = (String)data.get("SHORT_NAME");
			/*// 车的容量是否满足旅客数量（0：满足，1：不满足）
			Integer ifCarOk = data.get("IF_CAR_OK") == null ? 0 : ((BigDecimal)data.get("IF_CAR_OK")).intValue();*/
			
			// 到位时间
			String dwTime = (String)data.get("DW_TIME");
			// 货关时间
			String hgTime = (String)data.get("HG_TIME");
			// 到位时间
			String eDwTime = (String)data.get("E_DW_TIME");
			// 货关时间
			String eHgTime = (String)data.get("E_HG_TIME");
			/*// 到位ID
			Integer dwNodeId = data.get("DW_NODE_ID") == null ? 0 : ((BigDecimal)data.get("DW_NODE_ID")).intValue();
			// 货关ID
			Integer hgNodeId = data.get("HG_NODE_ID") == null ? 0 : ((BigDecimal)data.get("HG_NODE_ID")).intValue();*/
			
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
				flight.setSeat(actstandCode);
				flight.setInOutFlag(inOutFlag);
				flight.setIfRed(ifRed);
				flight.setInFltid(inFltid);
				flight.setOutFltid(outFltid);
				flight.setInFltNum(inFltNum);
				flight.setOutFltNum(outFltNum);
				flight.setIfBorderRed(ifBorderRed);
				flight.setInTime(inTime);
				flight.setOutTime(outTime);
				flight.setInVipFlag(inVipFlag);
				flight.setOutVipFlag(outVipFlag);
				flight.setIfGreen(ifColor);
				flight.setActtypeCode(acttypeCode);
				flight.setBagCrsl(bagCrsl);
				flight.setIfOrange(ifOrange);
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
				task.setInOutFlag(inOutFlag2);
				task.setOrderId(orderId);
				task.setProcId(procId);
				task.setPersonId(operator);
				task.setPersonName(operatorName);
				task.setFltid(fltid);
				task.setInFltid(inFltid);
				task.setOutFltid(outFltid);
				task.setTaskStatus(status);
				if("6".equals(jobState) || "0".equals(jobState)){
					task.setStatus(jobState);
				}else{
					task.setStatus("0");
				}
				
				if("A".equals(inOutFlag2)){
					task.setFltid(inFltid);
					task.setFlightName(inFltNum);
				}else if("D".equals(inOutFlag2)){
					task.setFltid(outFltid);
					task.setFlightName(outFltNum);
				}
				
				task.setJobState(jobState);
				task.setJobType(jobType);
				task.setTaskDescribe(taskDescribe);
				task.setIfConflict(ifConflict);
				if(!StringUtils.isEmpty(operator)){
					if(!ifNodeOnTime(taskId, eDwTime, dwTime)){
						// 判断任务到达“到位”节点时是否点击了到位
						task.setTaskStyle("y");
					}else if(!ifNodeOnTime(taskId, eHgTime, hgTime)){
						// 判断任务到达“货关”节点时是否点击了货关
						task.setTaskStyle("y");
					}
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
			List<String> onlineUsers,boolean front) {
		
		for(HashMap<String, Object> data : personDataMap){
			// 任务ID
			String taskId = data.get("TASK_ID") == null ? "" : data.get("TASK_ID").toString();
			// 任务模板ID
			String procId = (String)data.get("PROC_ID");
			// 航站楼/机坪名称
			String pos = data.get("POS") == null ? " " : (String)data.get("POS");
			// 作业人ID
			String operator = (String)data.get("ID") ;
			// 作业人名称
			String operatorName = (String)data.get("OPERATOR_NAME");
			// 车辆编号
//			String carId = (String)data.get("CAR_ID");
			// 航班ID
			String fltid = data.get("FLTID") == null ? "" : data.get("FLTID").toString();
			// 航班号
			String fltName = (String)data.get("FLIGHT_NUMBER");
			// 航班进出港标识（A为进港，D为出港）
			String inOutFlag = (String)data.get("IN_OUT_FLAG");
			// 任务状态
			String status = (String)data.get("STATUS");
			// 是否被停用
			Integer ifStop = data.get("IF_STOP") == null ? 0 : ((BigDecimal)data.get("IF_STOP")).intValue();
			// 人员完成作业数
			Integer finishNum = data.get("FINISH_NUM") == null ? 0 : ((BigDecimal)data.get("FINISH_NUM")).intValue();
			// 是否加星号（快下班的人）
			Integer ifStar = data.get("IF_STAR") == null ? 0 : ((BigDecimal)data.get("IF_STAR")).intValue();
			// 是否加好（加班人员）
			Integer ifPlus = data.get("IF_PLUS") == null ? 0 : ((BigDecimal)data.get("IF_PLUS")).intValue();
			// 是否加下划线（规则外手持机登陆但未设置加班）
			Integer ifUnderLine = data.get("IF_UNDERLINE") == null ? 0 : ((BigDecimal)data.get("IF_UNDERLINE")).intValue();
			// 是否冲突
			Integer ifConflict = data.get("IF_CONFLICT") == null ? 0 : ((BigDecimal)data.get("IF_CONFLICT")).intValue();
			// 颜色
			Integer ifColor = data.get("IF_COLOR2") == null ? 0 : ((BigDecimal)data.get("IF_COLOR2")).intValue();
			
			// 到位时间
			String dwTime = (String)data.get("DW_TIME");
			// 货关时间
			String hgTime = (String)data.get("HG_TIME");
			// 到位时间
			String eDwTime = (String)data.get("E_DW_TIME");
			// 货关时间
			String eHgTime = (String)data.get("E_HG_TIME");
			// 机位
			String actstandCode = (String)data.get("ACTSTAND_CODE");
			/*// 货关ID
			Integer hgNodeId = data.get("HG_NODE_ID") == null ? 0 : ((BigDecimal)data.get("HG_NODE_ID")).intValue();*/
			
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
				person.setIfStop(ifStop);
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
				person.setActstandCode(actstandCode);
				if(front){
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
				task.setInOutFlag(inOutFlag);
				task.setFltid(fltid);
				task.setProcId(procId);
				task.setPersonId(operator);
				task.setPersonName(operatorName);
				task.setFlightName(fltName);
				task.setTaskStatus(status);
				task.setIfConflict(ifConflict);
				
				if(!StringUtils.isEmpty(operator)){
					if(!ifNodeOnTime(taskId, eDwTime, dwTime)){
						// 判断任务到达“到位”节点时是否点击了到位
						task.setTaskStyle("y");
					}else if(!ifNodeOnTime(taskId, eHgTime, hgTime)){
						// 判断任务到达“货关”节点时是否点击了货关
						task.setTaskStyle("y");
					}
				}
				
				tasks.add(task);
			}
		}
	}
	
	/**
	 * 判断任务是否按时到达指定节点
	 * @param taskId
	 * @param nodeId
	 * @param aTime
	 * @return
	 */
	private boolean ifNodeOnTime(String taskId , String eTime , String aTime){
		if(StringUtils.isEmpty(eTime)){
			return true;
		}
		Date eTimeDate = DateUtils.parseDate(eTime);
		if(new Date().compareTo(eTimeDate) >= 0){
			if(aTime == null){
				return false;
			}
		}
		return true;
	} 


	/**
	 * 设置机坪数量及位置
	 * @param taskMonitor
	 */
	private TaskMonitorVO setNumToAirport(TaskMonitorVO taskMonitor) {
		// 总宽度
		int totalWidthNum = 0;
		// 分组数
		int groupNum = 2;
		// 机坪列表
		List<Airport> airports = taskMonitor.getAirport();
		// 机坪排序
		if(airports == null){
			return taskMonitor;
		}
		
		// 循环计算机坪宽度
		for(Airport airport : airports){
			// 总数加和
			totalWidthNum += airport.getPersons().size();
		}
		// 根据机坪总数及机坪宽度计算机坪位置
		int currentWidthNum = 0;
		for(Airport airport : airports){
			int widthNum = airport.getPersons().size();
			if(currentWidthNum == 0){
				// 第一个放到上边
				airport.setType(1);
				currentWidthNum += widthNum;
			}else{
				for(int i = 1; i<= groupNum; i ++){
					if(currentWidthNum < totalWidthNum * ((float)i/groupNum)  - widthNum/2){
						airport.setType(i);
						break;
					}
				}
				currentWidthNum += widthNum;
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

	/*@Override
	public TaskInfo getTaskInfo(String taskId) throws Exception {
		// 查询实际时间
		TaskInfo taskInfo =  taskMonitorDao.getTaskInfo(taskId);
		if(taskInfo == null){
			taskInfo = new TaskInfo();
		}
		// 查询预计时间
		List<Integer> nodeIds = taskMonitorDao.getNodeIds(taskId);
		
		for(int i=0;i<nodeIds.size();i++){
			switch (i) {
			case 0:
				taskInfo.seteArrive(DateUtils.formatToFltDate(nodeTimeService.getNodeTime(taskId, nodeIds.get(i).toString())));
				break;
			case 1:
				taskInfo.seteStart(DateUtils.formatToFltDate(nodeTimeService.getNodeTime(taskId, nodeIds.get(i).toString())));
				break;
			case 2:
				taskInfo.seteStart2(DateUtils.formatToFltDate(nodeTimeService.getNodeTime(taskId, nodeIds.get(i).toString())));
				taskInfo.seteEnd(DateUtils.formatToFltDate(nodeTimeService.getNodeTime(taskId, nodeIds.get(i).toString())));
				break;
			case 3:
				taskInfo.seteEnd(DateUtils.formatToFltDate(nodeTimeService.getNodeTime(taskId, nodeIds.get(i).toString())));
				break;
				
			}
		}
		
		return taskInfo;
	}*/


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

}
