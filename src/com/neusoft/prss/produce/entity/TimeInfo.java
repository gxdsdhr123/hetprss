package com.neusoft.prss.produce.entity;

/**
 * 各种时间
 * @author xuhw
 *
 */
public class TimeInfo {

	/**
	 * 廊桥/客梯车对靠时间
	 */
	private String dockingTime;
	
	/**
	 * 货舱开门时间
	 */
	private String doorOpenTime;
	
	/**
	 * 装舱完成时间
	 */
	private String finishLodingTime;
	
	/**
	 * 卸舱完毕时间
	 */
	private String finishOffTime;
	
	/**
	 * 最后舱门关闭时间
	 */
	private String lastCloseTime;
	
	/**
	 * 装舱指令取得时间
	 */
	private String orderGetTime;
	
	/**
	 * 散舱门关闭时间
	 */
	private String doorClosedTime;
	
	/**
	 * 开始装舱时间
	 */
	private String startLodingTime;

	public String getDockingTime() {
		return dockingTime;
	}

	public void setDockingTime(String dockingTime) {
		this.dockingTime = dockingTime;
	}

	public String getDoorOpenTime() {
		return doorOpenTime;
	}

	public void setDoorOpenTime(String doorOpenTime) {
		this.doorOpenTime = doorOpenTime;
	}

	public String getFinishLodingTime() {
		return finishLodingTime;
	}

	public void setFinishLodingTime(String finishLodingTime) {
		this.finishLodingTime = finishLodingTime;
	}

	public String getFinishOffTime() {
		return finishOffTime;
	}

	public void setFinishOffTime(String finishOffTime) {
		this.finishOffTime = finishOffTime;
	}

	public String getLastCloseTime() {
		return lastCloseTime;
	}

	public void setLastCloseTime(String lastCloseTime) {
		this.lastCloseTime = lastCloseTime;
	}

	public String getOrderGetTime() {
		return orderGetTime;
	}

	public void setOrderGetTime(String orderGetTime) {
		this.orderGetTime = orderGetTime;
	}

	public String getDoorClosedTime() {
		return doorClosedTime;
	}

	public void setDoorClosedTime(String doorClosedTime) {
		this.doorClosedTime = doorClosedTime;
	}

	public String getStartLodingTime() {
		return startLodingTime;
	}

	public void setStartLodingTime(String startLodingTime) {
		this.startLodingTime = startLodingTime;
	}
	
	
	
}
