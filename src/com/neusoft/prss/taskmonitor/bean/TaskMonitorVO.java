package com.neusoft.prss.taskmonitor.bean;

import java.util.List;

public class TaskMonitorVO {

	// 进港航班
	private List<Flight> yFlight;
	// 出港航班
	private List<Flight> nFlight;
	// 机坪
	private List<Airport> airport;
	// 航站楼
	private List<Terminal> terminal;
	
	
	// 未设置加班时间的加班人员
	private List<Person> unsetPersons;
	// 是否弹出加班设置弹窗
	private String popUnset;
	
	
	public List<Flight> getyFlight() {
		return yFlight;
	}

	public void setyFlight(List<Flight> yFlight) {
		this.yFlight = yFlight;
	}

	public List<Flight> getnFlight() {
		return nFlight;
	}

	public void setnFlight(List<Flight> nFlight) {
		this.nFlight = nFlight;
	}

	public List<Airport> getAirport() {
		return airport;
	}

	public void setAirport(List<Airport> airport) {
		this.airport = airport;
	}

	public List<Terminal> getTerminal() {
		return terminal;
	}

	public void setTerminal(List<Terminal> terminal) {
		this.terminal = terminal;
	}

	public List<Person> getUnsetPersons() {
		return unsetPersons;
	}

	public void setUnsetPersons(List<Person> unsetPersons) {
		this.unsetPersons = unsetPersons;
	}

	public String getPopUnset() {
		return popUnset;
	}

	public void setPopUnset(String popUnset) {
		this.popUnset = popUnset;
	}

	/**
	 * 航班
	 * @author xuhw
	 *
	 */
	public static class Flight{
		
		// fltid
		private String id;
		// 航班号
		private String name;
		// 航班号
		private String acttypeCode;
		// 进出港类型
		private String inOutFlag;
		// 机位
		private String seat;
		//进港机位
		private String inSeat;
		//出港机位
		private String outSeat;
		//进港远近机位
		private String inActKind;
		//出港远近机位
		private String outActKind;
		// 机坪/登机口
		private String title2;
		// ATA/ETA/STD
		private String title3;
		// 是否变绿
		private Integer ifGreen;
		// 是否下划线
		private Integer ifLine;
		// 是否变红
		private Integer ifRed;
		// 进港fltid
		private String inFltid;
		// 出港fltid
		private String outFltid;
		// 旅客人数
		private Integer paxNum;
		// 国际旅客
		private Integer iPaxNum;
		// 国内旅客
		private Integer dPaxNum;
		// 要客标识
		private String vipFlags;
		// 车的容量是否满足旅客数量（0：满足，1：不满足）
		private Integer ifCarOk;
		// 保障人员数
		private Integer operNum;
		// setd
		private String setd;
		// 航班号颜色显示，国内1、2、3，国际4
		private String unionCode;
		// 红色框线
		private Integer ifBorderRed;
		// 橙色
		private Integer ifOrange;
		// 是否未完成
		private Integer ifNotFinish;
		// 是否超时
		private Integer ifOverWorkTime;
		// 黄色
		private Integer ifYellow;
		
		/***********以下针对进出港合并航班***********/
		
		// 进港航班号
		private String inFltNum;
		// 出港航班号
		private String outFltNum;
		// 进港展示时间
		private String inTime;
		// 出港展示时间
		private String outTime;
		// 进港要客
		private String inVipFlag;
		// 出港要客
		private String outVipFlag;
		// 行李转盘
		private String bagCrsl;
		
		public String getBagCrsl() {
			return bagCrsl;
		}
		public void setBagCrsl(String bagCrsl) {
			this.bagCrsl = bagCrsl;
		}
		private List<Task> task;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getInOutFlag() {
			return inOutFlag;
		}
		public void setInOutFlag(String inOutFlag) {
			this.inOutFlag = inOutFlag;
		}
		public List<Task> getTask() {
			return task;
		}
		public void setTask(List<Task> task) {
			this.task = task;
		}
		public String getSeat() {
			return seat;
		}
		public void setSeat(String seat) {
			this.seat = seat;
		}
		public String getInSeat() {
			return inSeat;
		}
		public void setInSeat(String inSeat) {
			this.inSeat = inSeat;
		}
		public String getOutSeat() {
			return outSeat;
		}
		public void setOutSeat(String outSeat) {
			this.outSeat = outSeat;
		}
		public String getInActKind() {
			return inActKind;
		}
		public void setInActKind(String inActKind) {
			this.inActKind = inActKind;
		}
		public String getOutActKind() {
			return outActKind;
		}
		public void setOutActKind(String outActKind) {
			this.outActKind = outActKind;
		}
		public Integer getOperNum() {
			return operNum;
		}
		public void setOperNum(Integer operNum) {
			this.operNum = operNum;
		}
		public Integer getIfGreen() {
			return ifGreen;
		}
		public void setIfGreen(Integer ifGreen) {
			this.ifGreen = ifGreen;
		}
		public Integer getIfLine() {
			return ifLine;
		}
		public void setIfLine(Integer ifLine) {
			this.ifLine = ifLine;
		}
		public String getTitle2() {
			return title2;
		}
		public void setTitle2(String title2) {
			this.title2 = title2;
		}
		public String getTitle3() {
			return title3;
		}
		public void setTitle3(String title3) {
			this.title3 = title3;
		}
		public Integer getIfRed() {
			return ifRed;
		}
		public void setIfRed(Integer ifRed) {
			this.ifRed = ifRed;
		}
		
		public String getInFltid() {
			return inFltid;
		}
		public void setInFltid(String inFltid) {
			this.inFltid = inFltid;
		}
		public String getOutFltid() {
			return outFltid;
		}
		public void setOutFltid(String outFltid) {
			this.outFltid = outFltid;
		}
		public Flight(){}
		
		public Flight(String id) {
			this.id = id;
		}
		public Integer getPaxNum() {
			return paxNum;
		}
		public void setPaxNum(Integer paxNum) {
			this.paxNum = paxNum;
		}
		public Integer getiPaxNum() {
			return iPaxNum;
		}
		public void setiPaxNum(Integer iPaxNum) {
			this.iPaxNum = iPaxNum;
		}
		public Integer getdPaxNum() {
			return dPaxNum;
		}
		public void setdPaxNum(Integer dPaxNum) {
			this.dPaxNum = dPaxNum;
		}
		public String getVipFlags() {
			return vipFlags;
		}
		public void setVipFlags(String vipFlags) {
			this.vipFlags = vipFlags;
		}
		public Integer getIfCarOk() {
			return ifCarOk;
		}
		public void setIfCarOk(Integer ifCarOk) {
			this.ifCarOk = ifCarOk;
		}
		public String getInFltNum() {
			return inFltNum;
		}
		public void setInFltNum(String inFltNum) {
			this.inFltNum = inFltNum;
		}
		public String getOutFltNum() {
			return outFltNum;
		}
		public void setOutFltNum(String outFltNum) {
			this.outFltNum = outFltNum;
		}
		public String getInTime() {
			return inTime;
		}
		public void setInTime(String inTime) {
			this.inTime = inTime;
		}
		public String getOutTime() {
			return outTime;
		}
		public void setOutTime(String outTime) {
			this.outTime = outTime;
		}
		public String getInVipFlag() {
			return inVipFlag;
		}
		public void setInVipFlag(String inVipFlag) {
			this.inVipFlag = inVipFlag;
		}
		public String getOutVipFlag() {
			return outVipFlag;
		}
		public void setOutVipFlag(String outVipFlag) {
			this.outVipFlag = outVipFlag;
		}
		public String getSetd() {
			return setd;
		}
		public void setSetd(String setd) {
			this.setd = setd;
		}
		public String getUnionCode() {
			return unionCode;
		}
		public void setUnionCode(String unionCode) {
			this.unionCode = unionCode;
		}
		public Integer getIfBorderRed() {
			return ifBorderRed;
		}
		public void setIfBorderRed(Integer ifBorderRed) {
			this.ifBorderRed = ifBorderRed;
		}
		public Integer getIfOrange() {
			return ifOrange;
		}
		public void setIfOrange(Integer ifOrange) {
			this.ifOrange = ifOrange;
		}
		
		public Integer getIfYellow() {
			return ifYellow;
		}
		public void setIfYellow(Integer ifYellow) {
			this.ifYellow = ifYellow;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj == null){
				return false;
			}
			if (obj instanceof Flight) {
				Flight inObj = (Flight) obj;
				return this.id.equals(inObj.getId());
			}else if(obj instanceof String){
				return this.id == (String)obj;
			}
			return super.equals(obj);
		}
		public Integer getIfNotFinish() {
			return ifNotFinish;
		}
		public void setIfNotFinish(Integer ifNotFinish) {
			this.ifNotFinish = ifNotFinish;
		}
		public Integer getIfOverWorkTime() {
			return ifOverWorkTime;
		}
		public void setIfOverWorkTime(Integer ifOverWorkTime) {
			this.ifOverWorkTime = ifOverWorkTime;
		}
		public String getActtypeCode() {
			return acttypeCode;
		}
		public void setActtypeCode(String acttypeCode) {
			this.acttypeCode = acttypeCode;
		}
		
	}
	
	/**
	 * 机坪
	 * @author xuhw
	 *
	 */
	public static class Airport{
		
		private String id;
		private String name;
		// 机坪区域宽度（数量）
		private Integer widthNum;
		// 机坪区域分类
		private Integer type;
		// 是否显示红框
		private Integer ifRed;
		
		private List<Person> persons;
		private List<Vehicle> vehicles;
		private List<Flight> flights;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Person> getPersons() {
			return persons;
		}
		public void setPersons(List<Person> persons) {
			this.persons = persons;
		}
		public Airport(){}
		
		public Airport(String id) {
			this.id = id;
		}
		public List<Vehicle> getVehicles() {
			return vehicles;
		}
		public void setVehicles(List<Vehicle> vehicles) {
			this.vehicles = vehicles;
		}
		
		public List<Flight> getFlights() {
			return flights;
		}
		public void setFlights(List<Flight> flights) {
			this.flights = flights;
		}
		public Integer getWidthNum() {
			return widthNum;
		}
		public void setWidthNum(Integer widthNum) {
			this.widthNum = widthNum;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Integer getIfRed() {
			return ifRed;
		}
		public void setIfRed(Integer ifRed) {
			this.ifRed = ifRed;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj == null){
				return false;
			}
			if (obj instanceof Airport) {
				Airport inObj = (Airport) obj;
				return this.id.equals(inObj.getId());
			}else if(obj instanceof String){
				return this.id == (String)obj;
			}
			return super.equals(obj);
		}
		
	}
	
	/**
	 * 航站楼
	 * @author xuhw
	 *
	 */
	public static class Terminal{
		
		private String id;
		private String name;
		private List<Person> persons;
		private List<Flight> flights;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Person> getPersons() {
			return persons;
		}
		public void setPersons(List<Person> persons) {
			this.persons = persons;
		}
		public List<Flight> getFlights() {
			return flights;
		}
		public void setFlights(List<Flight> flights) {
			this.flights = flights;
		}
		
		public Terminal(){}
		
		public Terminal(String id) {
			this.id = id;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null){
				return false;
			}
			if (obj instanceof Terminal) {
				Terminal inObj = (Terminal) obj;
				return this.id.equals(inObj.getId());
			}else if(obj instanceof String){
				return this.id == (String)obj;
			}
			return super.equals(obj);
		}
	}
	
	/**
	 * 保障人员
	 * @author xuhw
	 *
	 */
	public static class Person{
		// 人员ID
		private String personId;
		// 人员姓名
		private String personName;
		// 车牌号
		private String carId;
		// 车辆型号
		private String carType;
		// 机位区域
		private String areaName;
		// 是否被停用
		private Integer ifStop;
		// 登陆状态
		private Integer status;
		// 完成任务数
		private String finishNum;
		// 是否加星号（快下班的人）
		private Integer ifStar;
		// 是否加下划线（规则外手持机登陆但未设置加班）
		private Integer ifUnderLine;
		// 是否加好（加班人员）
		private Integer ifPlus;
		// 分类
		private Integer type;
		// 班组人数
		private Integer groupNum;
		// 组号
		private String teamName;
		// 工作时段
		private String seTime;
		// 当前工作机位
		private String actstandCode;
		public String getActstandCode() {
			return actstandCode;
		}
		public void setActstandCode(String actstandCode) {
			this.actstandCode = actstandCode;
		}
		// 任务列表
		private List<Task> task;
		
		
		public String getPersonId() {
			return personId;
		}
		public void setPersonId(String personId) {
			this.personId = personId;
		}
		public String getPersonName() {
			return personName;
		}
		public void setPersonName(String personName) {
			this.personName = personName;
		}
		public List<Task> getTask() {
			return task;
		}
		public void setTask(List<Task> task) {
			this.task = task;
		}
		public int getTaskSize() {
			return task==null?0:task.size();
		}

		public String getCarId() {
			return carId;
		}
		public void setCarId(String carId) {
			this.carId = carId;
		}
		public Person(){}
		
		public Person(String personId) {
			this.personId = personId;
		}
		public Integer getIfStop() {
			return ifStop;
		}
		public void setIfStop(Integer ifStop) {
			this.ifStop = ifStop;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public String getFinishNum() {
			return finishNum;
		}
		public void setFinishNum(String finishNum) {
			this.finishNum = finishNum;
		}
		public Integer getIfStar() {
			return ifStar;
		}
		public void setIfStar(Integer ifStar) {
			this.ifStar = ifStar;
		}
		public Integer getIfUnderLine() {
			return ifUnderLine;
		}
		public void setIfUnderLine(Integer ifUnderLine) {
			this.ifUnderLine = ifUnderLine;
		}
		public Integer getIfPlus() {
			return ifPlus;
		}
		public void setIfPlus(Integer ifPlus) {
			this.ifPlus = ifPlus;
		}
		public String getCarType() {
			return carType;
		}
		public String getSeTime() {
			return seTime;
		}
		public void setSeTime(String seTime) {
			this.seTime = seTime;
		}
		public void setCarType(String carType) {
			this.carType = carType;
		}
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Integer getGroupNum() {
			return groupNum;
		}
		public void setGroupNum(Integer groupNum) {
			this.groupNum = groupNum;
		}
		public String getTeamName() {
			return teamName;
		}
		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj == null){
				return false;
			}
			if (obj instanceof Person) {
				Person inObj = (Person) obj;
				return this.personId.equals(inObj.getPersonId());
			}else if(obj instanceof String){
				return this.personId == (String)obj;
			}
			return super.equals(obj);
		}
	}
	
	/**
	 * 任务
	 * @author xuhw
	 *
	 */
	public static class Task{
		// 任务ID
		private String id;
		// 任务名称
		private String taskName;
		// 进出港类型
		private String inOutFlag;
		// 作业人
		private String personId;
		// 作业人姓名
		private String personName;
		// 任务fltid
		private String fltid;
		// 进港fltid
		private String inFltid;
		// 出港fltid
		private String outFltid;
		// 航班号
		private String flightName;
		// 消息模板ID
		private String procId;
		// 流程ID
		private String orderId;
		// 任务块状态（主要控制颜色）
		private String status;
		// 任务节点状态（文字）
		private String taskStatus;
		// 任务其他样式
		private String taskStyle; 
		// 车牌号
		private String carId;
		// 任务状态
		private String jobState;
		// 作业类型
		private String jobType;
		// 任务描述
		private String taskDescribe;
		// 是否冲突
		private Integer ifConflict;
		// 班组人数
		private Integer groupNum;
		// 是否超时
		private Integer ifOverWorkTime;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTaskName() {
			return taskName;
		}
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
		public String getPersonName() {
			return personName;
		}
		public void setPersonName(String personName) {
			this.personName = personName;
		}
		public String getFlightName() {
			return flightName;
		}
		public void setFlightName(String flightName) {
			this.flightName = flightName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getPersonId() {
			return personId;
		}
		public void setPersonId(String personId) {
			this.personId = personId;
		}
		public String getInFltid() {
			return inFltid;
		}
		public void setInFltid(String inFltid) {
			this.inFltid = inFltid;
		}
		public String getOutFltid() {
			return outFltid;
		}
		public void setOutFltid(String outFltid) {
			this.outFltid = outFltid;
		}
		public String getProcId() {
			return procId;
		}
		public void setProcId(String procId) {
			this.procId = procId;
		}
		public String getTaskStatus() {
			return taskStatus;
		}
		public void setTaskStatus(String taskStatus) {
			this.taskStatus = taskStatus;
		}
		public String getCarId() {
			return carId;
		}
		public void setCarId(String carId) {
			this.carId = carId;
		}
		public String getJobState() {
			return jobState;
		}
		public void setJobState(String jobState) {
			this.jobState = jobState;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getJobType() {
			return jobType;
		}
		public void setJobType(String jobType) {
			this.jobType = jobType;
		}
		public String getFltid() {
			return fltid;
		}
		public void setFltid(String fltid) {
			this.fltid = fltid;
		}
		public String getTaskDescribe() {
			return taskDescribe;
		}
		public void setTaskDescribe(String taskDescribe) {
			this.taskDescribe = taskDescribe;
		}
		public String getInOutFlag() {
			return inOutFlag;
		}
		public void setInOutFlag(String inOutFlag) {
			this.inOutFlag = inOutFlag;
		}
		public Integer getIfConflict() {
			return ifConflict;
		}
		public void setIfConflict(Integer ifConflict) {
			this.ifConflict = ifConflict;
		}
		public Integer getGroupNum() {
			return groupNum;
		}
		public void setGroupNum(Integer groupNum) {
			this.groupNum = groupNum;
		}
		public String getTaskStyle() {
			return taskStyle;
		}
		public void setTaskStyle(String taskStyle) {
			this.taskStyle = taskStyle;
		}
		public Integer getIfOverWorkTime() {
			return ifOverWorkTime;
		}
		public void setIfOverWorkTime(Integer ifOverWorkTime) {
			this.ifOverWorkTime = ifOverWorkTime;
		}
		
	}
	
	/**
	 * 车辆信息
	 * @author xuhw
	 *
	 */
	public static class Vehicle{
		// 车辆ID
		private String id;
		// 车辆牌号
		private String vehicleNumber;
		// 车辆编号
		private String vehicleCode;
		// 车辆类型
		private String typeCode;
		// 车辆类型名称
		private String typeName;
		// 保障航班号
		private String flightNumber;
		// 车辆状态
		private String status;
		// 备注（停用原因）
		private String remark;
		// 航班延误标识
		private Integer ifFltRed;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getVehicleNumber() {
			return vehicleNumber;
		}
		public void setVehicleNumber(String vehicleNumber) {
			this.vehicleNumber = vehicleNumber;
		}
		public String getVehicleCode() {
			return vehicleCode;
		}
		public void setVehicleCode(String vehicleCode) {
			this.vehicleCode = vehicleCode;
		}
		public String getTypeCode() {
			return typeCode;
		}
		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getFlightNumber() {
			return flightNumber;
		}
		public void setFlightNumber(String flightNumber) {
			this.flightNumber = flightNumber;
		}
		public Integer getIfFltRed() {
			return ifFltRed;
		}
		public void setIfFltRed(Integer ifFltRed) {
			this.ifFltRed = ifFltRed;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj == null){
				return false;
			}
			if (obj instanceof Vehicle) {
				Vehicle inObj = (Vehicle) obj;
				return this.id.equals(inObj.getId());
			}else if(obj instanceof String){
				return this.id == (String)obj;
			}
			return super.equals(obj);
		}
		
	}
}
