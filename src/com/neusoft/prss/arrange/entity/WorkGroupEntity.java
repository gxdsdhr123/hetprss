/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月27日 下午9:15:02
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.entity;

public class WorkGroupEntity {
	private static final long serialVersionUID = 1L;
	private String teamId;
	private String teamName;
	private String offficId;
	private String leaderId;
	private String creator;
	private String sPerson;
	private String aPerson;

	public String getsPerson() {
		return sPerson;
	}

	public void setsPerson(String sPerson) {
		this.sPerson = sPerson;
	}

	public String getaPerson() {
		return aPerson;
	}

	public void setaPerson(String aPerson) {
		this.aPerson = aPerson;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getOffficId() {
		return offficId;
	}

	public void setOffficId(String offficId) {
		this.offficId = offficId;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}
