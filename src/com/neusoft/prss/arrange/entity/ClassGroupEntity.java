/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月27日 下午5:12:29
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.entity;

public class ClassGroupEntity {
	private static final long serialVersionUID = 1L;
	private String id;
	private String cgname;
	private String smid;
	private String pmid;
	private String aPerson;
	private String sPerson;

	public String getCgname() {
		return cgname;
	}

	public void setCgname(String cgname) {
		this.cgname = cgname;
	}

	public String getaPerson() {
		return aPerson;
	}

	public void setaPerson(String aPerson) {
		this.aPerson = aPerson;
	}

	public String getsPerson() {
		return sPerson;
	}

	public void setsPerson(String sPerson) {
		this.sPerson = sPerson;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSmid() {
		return smid;
	}

	public void setSmid(String smid) {
		this.smid = smid;
	}

	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
}
