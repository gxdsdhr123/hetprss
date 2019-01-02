
package com.neusoft.prss.message.entity;

import com.neusoft.framework.common.persistence.DataEntity;

public class SRListVO extends DataEntity<SRListVO>{
	 private static final long serialVersionUID = 1L;
	 private String no;
	 private String name;
	 private String rownum;
	 private String ischg;
	 private String remark;
	 private String tempname;
	 
	 
	 
	
	public String getTempname() {
        return tempname;
    }
    public void setTempname(String tempname) {
        this.tempname = tempname;
    }
    public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIschg() {
		return ischg;
	}
	public void setIschg(String ischg) {
		this.ischg = ischg;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRownum() {
		return rownum;
	}
	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	
	 
	 
	 
	 

}
