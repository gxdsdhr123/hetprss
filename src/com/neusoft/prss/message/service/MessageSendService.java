/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.message.service;

import com.alibaba.fastjson.JSONObject;

public interface MessageSendService {
	
    /**
     * 
     *Discription:发送新消息.
     *@param data
     *@param TID//消息模板ID
     *@param MTEXT//消息正文，手动发送消息时传参
     *@param USERID//发送消息的用户ID
     *@param USERLOGINNAME// 用户登录名
     *@param USERNAME// 用户中文名
     *@param OFFICEID//部门
     *@param OFFICENAME//部门名
     *@param IFREPLY//是否需要回复，手动发送消息时传参
     *@param FLTID//航班动态ID
     *@param FLIGHTNUMBER//航班号
     *@param FLIGHTDATE//航班日期
     *@param TRANSSUBID//转发消息的人ID，这个数据取自消息接收人表mm_info_to中的ID
     *@param TOLISTSTR//消息接收人ID列表
     *@param SYS//接收消息的设备 分为PC和手持
     *@param OLDMID//转发消息的消息ID
     *@param ROLEIDS//用户角色IDS
     *@param MTOTYPE//消息发送者类型
     *@param OTH_FLTID//配对航班的另一个航班ID
     *@param TASKID//任务ID，任务相关的消息需要传递TASKID，在接收人为作业人时，需要使用TASKID查询具体的接收人
     *@param FIOTYPE//进出港类型
     *@param IFFLIGHT//是否关联航班
     *@param MESSTYPE//发送的消息类型，现在分为EVENT（普通消息）、SUBS（订阅的消息）、WARN（预警消息）
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年9月15日 Maxx [变更描述]
     */
    public JSONObject sendMessage(JSONObject data) ;
    /**
     * 
     *Discription:发送短信.
     *@param mobile：接收人电话
     *@param mtext：信息
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年9月15日 Maxx [变更描述]
     */
    public JSONObject sendMsm(JSONObject data);
    
    /**
     * 
     *Discription:取消发送.
     *@param CPMsgId：CloudPush系统中消息ID MSGID
     *@param AppId：消息的appid，必须和要终止的消息的一致
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年9月15日 Maxx [变更描述]
     */
    public JSONObject sendStop(JSONObject data);
    /**
     * 
     *Discription:消息转发.
     *@param data
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年10月25日 Maxx [变更描述]
     */
    public boolean transMassage(JSONObject data);
    /**
     * 
     *Discription:方法功能中文描述.
     *@param data
     *@param TID//消息模板ID
     *@param MTEXT//消息正文，手动发送消息时传参
     *@param USERID//发送消息的用户ID
     *@param USERLOGINNAME// 用户登录名
     *@param USERNAME// 用户中文名
     *@param OFFICEID//部门
     *@param OFFICENAME//部门名
     *@param IFREPLY//是否需要回复，手动发送消息时传参
     *@param FLTID//航班动态ID
     *@param FLIGHTNUMBER//航班号
     *@param FLIGHTDATE//航班日期
     *@param TRANSSUBID//转发消息的人ID，这个数据取自消息接收人表mm_info_to中的ID
     *@param TOLISTSTR//消息接收人ID列表
     *@param SYS//接收消息的设备 分为PC和手持
     *@param OLDMID//转发消息的消息ID
     *@param ROLEIDS//用户角色IDS
     *@param MTOTYPE//消息发送者类型
     *@param OTH_FLTID//配对航班的另一个航班ID
     *@param TASKID//任务ID，任务相关的消息需要传递TASKID，在接收人为作业人时，需要使用TASKID查询具体的接收人
     *@param FIOTYPE//进出港类型
     *@param IFFLIGHT//是否关联航班
     *@param MESSTYPE//发送的消息类型，现在分为EVENT（普通消息）、SUBS（订阅的消息）、WARN（预警消息）
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年3月7日 Maxx [变更描述]
     */
    public JSONObject sendSubsMessage(JSONObject data) ;
    
    /**
     * 
     *Discription:发送消息，没有模板.
     *@param TOKENLIST：接收人参数，格式为JSONArray的字符, CONTENT：消息内容，格式为JSONObject，MSGTITLE：消息的标题：可有可无，
     *@param TYPE：接收人类型，1：CP 2 ：手持 3：全部，USERID:发送ID,USERNAME:发送人名称
     *@param 例子{TOKENLIST:["1","2"],MSGTITLE,TYPE:1,"消息标题-可无",USERID:"",USERNAME:"",
     *@param CONTENT:{"MTITLE":"消息标题","MTEXT":"正文","TASKID":"任务ID","TYPE":"消息类型"}}
     *@return
     *@throws Exception
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月31日 neusoft [变更描述]
     */
    public boolean sendNoMouldMessage(JSONObject data) throws Exception ;
    
}
