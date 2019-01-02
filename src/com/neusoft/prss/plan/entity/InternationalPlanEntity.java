package com.neusoft.prss.plan.entity;

import java.io.Serializable;

public class InternationalPlanEntity implements Serializable
{
	/**
	 * 国际航班计划bean
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String fltNo;
	private String std;
	private String sta;
	private String departApt;
	private String arrivalApt;
	private String otherApt;
	private String otherStd;
	private String otherSta;
	private String actType;
	private String aircraftNumber;
	private String shareFltNo;
	private String propertyCode;
	private String attrCode;
	private String alnCode;
	private String fltPeriodTime;
	private String fltWeek;
	private String fltDays;
	private String fltDates;
	private String flag;
	private String msgId;
	private String acceptState;
	private String operatorDate;
	private String operatorUser;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getFltNo()
	{
		return fltNo;
	}

	public void setFltNo(String fltNo)
	{
		this.fltNo = fltNo;
	}

	public String getStd()
	{
		return std;
	}

	public void setStd(String std)
	{
		this.std = std;
	}

	public String getSta()
	{
		return sta;
	}

	public void setSta(String sta)
	{
		this.sta = sta;
	}

	public String getDepartApt()
	{
		return departApt;
	}

	public void setDepartApt(String departApt)
	{
		this.departApt = departApt;
	}

	public String getArrivalApt()
	{
		return arrivalApt;
	}

	public void setArrivalApt(String arrivalApt)
	{
		this.arrivalApt = arrivalApt;
	}

	public String getOtherApt()
	{
		return otherApt;
	}

	public void setOtherApt(String otherApt)
	{
		this.otherApt = otherApt;
	}

	public String getOtherStd()
	{
		return otherStd;
	}

	public void setOtherStd(String otherStd)
	{
		this.otherStd = otherStd;
	}

	public String getOtherSta()
	{
		return otherSta;
	}

	public void setOtherSta(String otherSta)
	{
		this.otherSta = otherSta;
	}

	public String getActType()
	{
		return actType;
	}

	public void setActType(String actType)
	{
		this.actType = actType;
	}

	public String getAircraftNumber()
	{
		return aircraftNumber;
	}

	public void setAircraftNumber(String aircraftNumber)
	{
		this.aircraftNumber = aircraftNumber;
	}

	public String getShareFltNo()
	{
		return shareFltNo;
	}

	public void setShareFltNo(String shareFltNo)
	{
		this.shareFltNo = shareFltNo;
	}

	public String getPropertyCode()
	{
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode)
	{
		this.propertyCode = propertyCode;
	}

	public String getAttrCode()
	{
		return attrCode;
	}

	public void setAttrCode(String attrCode)
	{
		this.attrCode = attrCode;
	}

	public String getAlnCode()
	{
		return alnCode;
	}

	public void setAlnCode(String alnCode)
	{
		this.alnCode = alnCode;
	}

	public String getFltPeriodTime()
	{
		return fltPeriodTime;
	}

	public void setFltPeriodTime(String fltPeriodTime)
	{
		this.fltPeriodTime = fltPeriodTime;
	}

	public String getFltWeek()
	{
		return fltWeek;
	}

	public void setFltWeek(String fltWeek)
	{
		this.fltWeek = fltWeek;
	}

	public String getFltDays()
	{
		return fltDays;
	}

	public void setFltDays(String fltDays)
	{
		this.fltDays = fltDays;
	}

	public String getFltDates()
	{
		return fltDates;
	}

	public void setFltDates(String fltDates)
	{
		this.fltDates = fltDates;
	}

	public String getFlag()
	{
		return flag;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
	}

	public String getAcceptState()
	{
		return acceptState;
	}

	public void setAcceptState(String acceptState)
	{
		this.acceptState = acceptState;
	}

	public String getOperatorDate()
	{
		return operatorDate;
	}

	public void setOperatorDate(String operatorDate)
	{
		this.operatorDate = operatorDate;
	}

	public String getOperatorUser()
	{
		return operatorUser;
	}

	public void setOperatorUser(String operatorUser)
	{
		this.operatorUser = operatorUser;
	}
}
