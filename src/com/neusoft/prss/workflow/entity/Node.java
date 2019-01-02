/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月16日 上午8:56:14
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.workflow.entity;

public class Node {
	
	private String id;
    private String name;
    private String label;
    private String jobKind;
    private String jobType;
    private String jobKindId;
    private String jobTypeId;
    private String aftMsg1;
    private String aftMsg2;
    private String notifyMsg;
    private String alarmMsgLv1;
    private String alarmMsgLv2;
    private String alarmMsgLv3;
    private String alarmMsgLv4;
    private String alarmMsgLv5;
    private String notifyTm;
    private String alarmTm1;
    private String alarmTm2;
    private String alarmTm3;
    private String alarmTm4;
    private String alarmTm5;
    private String icon;

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
        this.name = name == null ? null : name.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getJobKind() {
        return jobKind;
    }

    public void setJobKind(String jobKind) {
        this.jobKind = jobKind == null ? null : jobKind.trim();
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType == null ? null : jobType.trim();
    }

    public String getAftMsg1() {
        return aftMsg1;
    }

    public void setAftMsg1(String aftMsg1) {
        this.aftMsg1 = aftMsg1;
    }

    public String getAftMsg2() {
        return aftMsg2;
    }

    public void setAftMsg2(String aftMsg2) {
        this.aftMsg2 = aftMsg2;
    }

    public String getNotifyMsg() {
        return notifyMsg;
    }

    public void setNotifyMsg(String notifyMsg) {
        this.notifyMsg = notifyMsg;
    }

    public String getAlarmMsgLv1() {
        return alarmMsgLv1;
    }

    public void setAlarmMsgLv1(String alarmMsgLv1) {
        this.alarmMsgLv1 = alarmMsgLv1;
    }

    public String getAlarmMsgLv2() {
        return alarmMsgLv2;
    }

    public void setAlarmMsgLv2(String alarmMsgLv2) {
        this.alarmMsgLv2 = alarmMsgLv2;
    }

    public String getAlarmMsgLv3() {
        return alarmMsgLv3;
    }

    public void setAlarmMsgLv3(String alarmMsgLv3) {
        this.alarmMsgLv3 = alarmMsgLv3;
    }

    public String getAlarmMsgLv4() {
        return alarmMsgLv4;
    }

    public void setAlarmMsgLv4(String alarmMsgLv4) {
        this.alarmMsgLv4 = alarmMsgLv4;
    }

    public String getAlarmMsgLv5() {
        return alarmMsgLv5;
    }

    public void setAlarmMsgLv5(String alarmMsgLv5) {
        this.alarmMsgLv5 = alarmMsgLv5;
    }

    public String getNotifyTm() {
        return notifyTm;
    }

    public void setNotifyTm(String notifyTm) {
        this.notifyTm = notifyTm == null ? null : notifyTm.trim();
    }

    public String getAlarmTm1() {
        return alarmTm1;
    }

    public void setAlarmTm1(String alarmTm1) {
        this.alarmTm1 = alarmTm1 == null ? null : alarmTm1.trim();
    }

    public String getAlarmTm2() {
        return alarmTm2;
    }

    public void setAlarmTm2(String alarmTm2) {
        this.alarmTm2 = alarmTm2 == null ? null : alarmTm2.trim();
    }

    public String getAlarmTm3() {
        return alarmTm3;
    }

    public void setAlarmTm3(String alarmTm3) {
        this.alarmTm3 = alarmTm3 == null ? null : alarmTm3.trim();
    }

    public String getAlarmTm4() {
        return alarmTm4;
    }

    public void setAlarmTm4(String alarmTm4) {
        this.alarmTm4 = alarmTm4 == null ? null : alarmTm4.trim();
    }

    public String getAlarmTm5() {
        return alarmTm5;
    }

    public void setAlarmTm5(String alarmTm5) {
        this.alarmTm5 = alarmTm5 == null ? null : alarmTm5.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

	public String getJobKindId() {
		return jobKindId;
	}

	public void setJobKindId(String jobKindId) {
		this.jobKindId = jobKindId;
	}

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
	}
}
