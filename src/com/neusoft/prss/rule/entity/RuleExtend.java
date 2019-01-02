package com.neusoft.prss.rule.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于规则处理的附加字段
 * @author XC
 */
public class RuleExtend {
	
	/**
	 * 进出港类型
	 */
	private List<String> inOutPort = new ArrayList<String>();
	
	/**
	 * 机位类型
	 */
	private List<String> apronType =  new ArrayList<>();
	
	/**
	 * 机型列表
	 */
	private List<String> acttypeList = new ArrayList<>();
	
	/**
	 * 航空公司列表
	 */
	private List<String> airlineList = new ArrayList<>();
	
	/**
     * 航班号列表
     */
    private List<String> flightNumberList = new ArrayList<>();
    
    /**
     * 机号列表
     */
    private List<String> aircraftNumberList = new ArrayList<>();
    
    /**
     * 子分公司
     */
    private String branch = "";
	
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<String> getAircraftNumberList() {
        return aircraftNumberList;
    }

    public void setAircraftNumberList(List<String> aircraftNumberList) {
        this.aircraftNumberList = aircraftNumberList;
    }

    /**
     * @return the flightNumberList
     */
	public List<String> getFlightNumberList() {
        return flightNumberList;
    }

    public void setFlightNumberList(List<String> flightNumberList) {
        this.flightNumberList = flightNumberList;
    }

    /**
	 *@return the inOutPort
	 */
	public List<String> getInOutPort() {
		return inOutPort;
	}

	/**
	 *@param inOutPort the inOutPort to set
	 */
	public void setInOutPort(List<String> inOutPort) {
		this.inOutPort = inOutPort;
	}

	/**
	 *@return the apronType
	 */
	public List<String> getApronType() {
		return apronType;
	}

	/**
	 *@param apronType the apronType to set
	 */
	public void setApronType(List<String> apronType) {
		this.apronType = apronType;
	}

	public List<String> getActtypeList() {
		return acttypeList;
	}

	public void setActtypeList(List<String> acttypeList) {
		this.acttypeList = acttypeList;
	}

	public List<String> getAirlineList() {
		return airlineList;
	}

	public void setAirlineList(List<String> airlineList) {
		this.airlineList = airlineList;
	}
}
