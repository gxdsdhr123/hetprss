package com.neusoft.prss.produce.entity;

import java.io.Serializable;

public class FltCleanServiceEntity implements Serializable
{
	// id bill_qj_service_s
	private String id;
	// 动态id
	private String fltId;
	// 航班日期
	private String flightDate;
	// 航班号
	private String flightNumber;
	// 航空公司2字码
	private String aln2code;
	// 机型
	private String acttypeCode;
	// 机号
	private String aircraftNumber;
	// 起场
	private String departAptCode;
	// 进港实落
	private String ata;
	// 清洁人员id
	private String operator;
	// 乘务长签字
	private String sign;
	// 备注
	private String remark;
	// 0正常单据 1删除单据
	private String delFlag;
	// 清洁人员名称
	private String operatorName;
	// 创建单据用户id
	private String createUser;
	// 航空公司3字码
	private String aln3code;
	// 进出港标识
	private String inOutFlag;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getFltId()
	{
		return fltId;
	}

	public void setFltId(String fltId)
	{
		this.fltId = fltId;
	}

	public String getFlightDate()
	{
		return flightDate;
	}

	public void setFlightDate(String flightDate)
	{
		this.flightDate = flightDate;
	}

	public String getFlightNumber()
	{
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber)
	{
		this.flightNumber = flightNumber;
	}

	public String getAln2code()
	{
		return aln2code;
	}

	public void setAln2code(String aln2code)
	{
		this.aln2code = aln2code;
	}

	public String getActtypeCode()
	{
		return acttypeCode;
	}

	public void setActtypeCode(String acttypeCode)
	{
		this.acttypeCode = acttypeCode;
	}

	public String getAircraftNumber()
	{
		return aircraftNumber;
	}

	public void setAircraftNumber(String aircraftNumber)
	{
		this.aircraftNumber = aircraftNumber;
	}

	public String getDepartAptCode()
	{
		return departAptCode;
	}

	public void setDepartAptCode(String departAptCode)
	{
		this.departAptCode = departAptCode;
	}

	public String getAta()
	{
		return ata;
	}

	public void setAta(String ata)
	{
		this.ata = ata;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getDelFlag()
	{
		return delFlag;
	}

	public void setDelFlag(String delFlag)
	{
		this.delFlag = delFlag;
	}

	public String getOperatorName()
	{
		return operatorName;
	}

	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public String getAln3code()
	{
		return aln3code;
	}

	public void setAln3code(String aln3code)
	{
		this.aln3code = aln3code;
	}

	public String getInOutFlag()
	{
		return inOutFlag;
	}

	public void setInOutFlag(String inOutFlag)
	{
		this.inOutFlag = inOutFlag;
	}
}
