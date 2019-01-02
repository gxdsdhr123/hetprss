package com.neusoft.prss.produce.entity;

import java.io.Serializable;

public class FltGuardianshipEntity implements Serializable
{
	// id bill_flt_log_s
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
	// 机位
	private String actstandCode;
	// 地面服务部
	private String dmfwNum;
	// 机务
	private String jwNum;
	// 清洁
	private String qjNum;
	// 航务保障部
	private String hwbzNum;
	// 货运
	private String hyNum;
	// 机组
	private String jzNum;
	// 其他部门
	private String otherNum;
	// 监护员id
	private String operator;
	// 0正常单据 1删除单据
	private String delFlag;
	// 监护员名称
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

	public String getActstandCode()
	{
		return actstandCode;
	}

	public void setActstandCode(String actstandCode)
	{
		this.actstandCode = actstandCode;
	}

	public String getDmfwNum()
	{
		return dmfwNum;
	}

	public void setDmfwNum(String dmfwNum)
	{
		this.dmfwNum = dmfwNum;
	}

	public String getJwNum()
	{
		return jwNum;
	}

	public void setJwNum(String jwNum)
	{
		this.jwNum = jwNum;
	}

	public String getQjNum()
	{
		return qjNum;
	}

	public void setQjNum(String qjNum)
	{
		this.qjNum = qjNum;
	}

	public String getHwbzNum()
	{
		return hwbzNum;
	}

	public void setHwbzNum(String hwbzNum)
	{
		this.hwbzNum = hwbzNum;
	}

	public String getHyNum()
	{
		return hyNum;
	}

	public void setHyNum(String hyNum)
	{
		this.hyNum = hyNum;
	}

	public String getJzNum()
	{
		return jzNum;
	}

	public void setJzNum(String jzNum)
	{
		this.jzNum = jzNum;
	}

	public String getOtherNum()
	{
		return otherNum;
	}

	public void setOtherNum(String otherNum)
	{
		this.otherNum = otherNum;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
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
