package com.neusoft.prss.ktc.entity;

public class TaskEntity {
	private static final long serialVersionUID = 1L;
	private int taskId;  // 任务编号
	private String operator;  // 作业人
	private String startTm;  // 任务开始时间
	private String endTm;  //任务结束时间
	private String acttypeCode;   // 机型
	private String actstandCode;  // 机位
	private String area;  //区域
	private String autoFlag;  // 0：手推 ，1：机动
	private String ktcNo;  // 车辆编号
	private String typeCode; // 0：靠， 1：撤，2：靠撤
	private String jobType;  // 作业类型
	private String aln2Code;  // 航空公司编码
	private String apronCode;  // 机坪编码
	private String flightNumber;  // 航班名
	private String gate;    // 登机口
	private String iotype;  //进出港标识
	private String jobState; // 任务状态
	private String actors;   // 随从人员
	private String fltId;    // 航班号
	private String aircraftNumber;  // 机号
	private String actKtcNo;   // 实际车辆编号
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getStartTm() {
		return startTm;
	}
	
	public void setStartTm(String startTm) {
		this.startTm = startTm;
	}
	
	public String getEndTm() {
		return endTm;
	}
	
	public void setEndTm(String endTm) {
		this.endTm = endTm;
	}
	
	
	public String getActtypeCode() {
		return acttypeCode;
	}
	
	public void setActtypeCode(String acttypeCode) {
		this.acttypeCode = acttypeCode;
	}
	
	public String getActstandCode() {
		return actstandCode;
	}
	
	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}
	
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getAutoFlag() {
		return autoFlag;
	}
	
	public void setAutoFlag(String autoFlag) {
		this.autoFlag = autoFlag;
	}
	
	public String getKtcNo() {
		return ktcNo;
	}
	
	public void setKtcNo(String ktcNo) {
		this.ktcNo = ktcNo;
	}
	
	public String getTypeCode() {
		return typeCode;
	}
	
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getAln2Code() {
		return aln2Code;
	}

	public void setAln2Code(String aln2Code) {
		this.aln2Code = aln2Code;
	}

	public String getApronCode() {
		return apronCode;
	}

	public void setApronCode(String apronCode) {
		this.apronCode = apronCode;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getGate() {
		return gate;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}
	
	
	public String getIotype() {
		return iotype;
	}

	public void setIotype(String iotype) {
		this.iotype = iotype;
	}
	
	public String getJobState() {
		return jobState;
	}

	public void setJobState(String jobState) {
		this.jobState = jobState;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getFltId() {
		return fltId;
	}

	public void setFltId(String fltId) {
		this.fltId = fltId;
	}

	public String getAircraftNumber() {
		return aircraftNumber;
	}

	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}

	public String getActKtcNo() {
		return actKtcNo;
	}

	public void setActKtcNo(String actKtcNo) {
		this.actKtcNo = actKtcNo;
	}

	/**
	 * 
	 * @Title: getEndPos 
	 * @Description: 获取任务结束位置
	 * @param @return
	 * @return String[]
	 * @throws
	 */
	public String[] getEndPos(){
		String[] result = new String[2];
		if(this.getIotype().startsWith("A")){
			result[0] = "1";// 1：登机口
			result[1] =  this.getGate();
		}else{
			result[0] = "2";// 2：机坪
			result[1] = this.getApronCode();
		}
		return result;
	}
	
	/**
	 * 
	 * Title: setEndPos
	 * @Description:  设置任务结束位置
	 * @param pos
	 */
	public void setEndPos(String pos) {
		if(this.getIotype().startsWith("A")){
			this.setGate(pos);
		}else{
			this.setApronCode(pos);
		}
	}
	
	/**
	 * 
	 * @Title: getStartPos 
	 * @Description:  获取任务开始位置
	 * @param @return
	 * @return String[]
	 * @throws
	 */
	public String[]  getStartPos(){
		String[] result = new String[2];
		if(this.getIotype().startsWith("A")){
			result[0] = "2";// 2：机坪
			result[1] =  this.getApronCode();
		}else{
			result[0] = "1";// 1：登机口
			result[1] = this.getGate();
		}
		return result;
	}
	
	/**
	 * 
	 * Title: setStartPos
	 * @Description:  设置任务开始位置
	 * @param pos
	 */
	public void setStartPos(String pos) {
		if(this.getIotype().startsWith("A")){
			this.setApronCode(pos);
		}else{
			this.setGate(pos);
		}
	}
	
}
