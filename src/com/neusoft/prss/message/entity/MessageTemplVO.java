/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 下午3:44:09
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.entity;


import java.sql.Date;
import java.util.List;
import com.neusoft.framework.common.utils.StringUtils;

public class MessageTemplVO {

    private String  id  ;   //  ID
    private String  mtype   ;   //  消息类型
    private String mttypecn ; //消息类型中文名称
    private String  tempname    ;   //  消息模板名称
    private String  mtitle  ;   //  标题
    private String  mtime   ;   //  模板时间
    private String  mtext   ;   //  模板内容
    private String fiotype ;   //  进出港类型0全部1进港2出港
    private int ifreply ;   //  是否回复
    private int ifflight    ;   //  关联航班
    private String  defreply    ;   //  回复默认内容
    private int ifsound ;   //  是否语音提示
    private String  soundtxt    ;   //  语音提示内容
    private int ifspeak ;   //  可否语音通话
    private int mftype  ;   //  消息源类型(原创,转发)
    private int mnumb   ;   //  排序号
    private String  extact  ;   // 功能链接 
    private int issys   ;   //  系统
    private int ifpopm;//右键菜单
    private String eventid;  //关联
    private String eventname;  //关联
    private String fiotypename;
    private Date mmtime;
    private String rownum;
    private int start;
    private int limit;
    private String tid;
    private String varcols;
    private int ifpopup;//询问框
    private int senddef;//默认发送 add 20171213 l.ran
    private int eventRecord;//事件记录 add 20180117 l.ran
    private List<MessageTemplFromVO> tabdata;
    private List<MessageTemplToVO> tabdata1;
    
    
	public int getEventRecord() {
        return eventRecord;
    }
    public void setEventRecord(int eventRecord) {
        this.eventRecord = eventRecord;
    }
    public int getSenddef() {
        return senddef;
    }
    public void setSenddef(int senddef) {
        this.senddef = senddef;
    }
    public int getIfpopup() {
        return ifpopup;
    }
    public void setIfpopup(int ifpopup) {
        this.ifpopup = ifpopup;
    }
    public Date getMmtime() {
		return mmtime;
	}
	public void setMmtime(Date mmtime) {
		this.mmtime = mmtime;
	}
	public String getFiotypename() {
		return fiotypename;
	}
	public void setFiotypename(String fiotypename) {
		this.fiotypename = fiotypename;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public String getVarcols() {
		return varcols;
	}
	public void setVarcols(String varcols) {
		this.varcols = varcols;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public List<MessageTemplFromVO> getTabdata() {
		return tabdata;
	}
	public void setTabdata(List<MessageTemplFromVO> tabdata) {
		this.tabdata = tabdata;
	}
	
	public List<MessageTemplToVO> getTabdata1() {
		return tabdata1;
	}
	public void setTabdata1(List<MessageTemplToVO> tabdata1) {
		this.tabdata1 = tabdata1;
	}
	public String getMttypecn() {
        return mttypecn;
    }
    public void setMttypecn(String mttypecn) {
        this.mttypecn = mttypecn;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMtype() {
        return StringUtils.nvl(mtype);
    }
    public void setMtype(String mtype) {
        this.mtype = mtype;
    }
    public String getMtitle() {
        return StringUtils.nvl(mtitle);
    }
    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }
    public String getMtime() {
        return StringUtils.nvl(mtime);
    }
    public void setMtime(String mtime) {
        this.mtime = mtime;
    }
    public String getMtext() {
        return StringUtils.nvl(mtext);
    }
    public void setMtext(String mtext) {
        this.mtext = mtext;
    }
    
    public String getFiotype() {
		return fiotype;
	}
	public void setFiotype(String fiotype) {
		this.fiotype = fiotype;
	}
	public String getTempname() {
        return StringUtils.nvl(tempname);
    }
    public void setTempname(String tempname) {
        this.tempname = tempname;
    }
    public int getIfreply() {
        return ifreply;
    }
    public void setIfreply(int ifreply) {
        this.ifreply = ifreply;
    }
    public int getIfflight() {
        return ifflight;
    }
    public void setIfflight(int ifflight) {
        this.ifflight = ifflight;
    }
    public String getDefreply() {
        return StringUtils.nvl(defreply);
    }
    public void setDefreply(String defreply) {
        this.defreply = defreply;
    }
    public int getIfsound() {
        return ifsound;
    }
    public void setIfsound(int ifsound) {
        this.ifsound = ifsound;
    }
    public String getSoundtxt() {
        return StringUtils.nvl(soundtxt);
    }
    public void setSoundtxt(String soundtxt) {
        this.soundtxt = soundtxt;
    }
    public int getIfspeak() {
        return ifspeak;
    }
    public void setIfspeak(int ifspeak) {
        this.ifspeak = ifspeak;
    }
    public int getMftype() {
        return mftype;
    }
    public void setMftype(int mftype) {
        this.mftype = mftype;
    }
    public int getMnumb() {
        return mnumb;
    }
    public void setMnumb(int mnumb) {
        this.mnumb = mnumb;
    }
    public String getExtact() {
        return StringUtils.nvl(extact);
    }
    public void setExtact(String extact) {
        this.extact = extact;
    }
    public int getIssys() {
        return issys;
    }
    public void setIssys(int issys) {
        this.issys = issys;
    }
    public int getIfpopm() {
        return ifpopm;
    }
    public void setIfpopm(int ifpopm) {
        this.ifpopm = ifpopm;
    }
	public String getRownum() {
		return rownum;
	}
	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
   
}
