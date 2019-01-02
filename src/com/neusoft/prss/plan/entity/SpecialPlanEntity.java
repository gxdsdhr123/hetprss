package com.neusoft.prss.plan.entity;

import java.io.Serializable;

/**
 * 公务机通航计划bean
 */
public class SpecialPlanEntity implements Serializable
{
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
	private String fltDate;
	private String attachmentId;
	private String attachmentName;
	private String planStatus;
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

	public String getFltDate()
	{
		return fltDate;
	}

	public void setFltDate(String fltDate)
	{
		this.fltDate = fltDate;
	}

	public String getAttachmentId()
	{
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId)
	{
		this.attachmentId = attachmentId;
	}

	public String getAttachmentName()
	{
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName)
	{
		this.attachmentName = attachmentName;
	}

	public String getPlanStatus()
	{
		return planStatus;
	}

	public void setPlanStatus(String planStatus)
	{
		this.planStatus = planStatus;
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
