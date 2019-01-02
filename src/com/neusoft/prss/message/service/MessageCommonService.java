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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface MessageCommonService {

    /**
     * 
     *Discription:查询消息模板信息.
     *@param id：模板ID
     *@param toListStr：页面传递过来的发送人列表，传递的ID中间用逗号分隔
     *@param fromType：发送者类型{'0':'个人','1':'角色','2':'部门','9':'系统' }
     *@param fromerId：发送者Id
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年9月15日 Maxx [变更描述]
     */
    public JSONObject queryTemplateInfoById(JSONObject data);

    /**
     * 
     *Discription:创建消息信息.
     *@param templ:模板信息
     *@param mtext：页面或者其他途径正文
     *@param senderuid：发送人ID
     *@param sender：发送人EN
     *@param sendercn：发送人中文
     *@param senderDep：发送人部分
     *@param soundtxt：语音信息
     *@param aa：接收人列表
     *@param ifreply：是否回复
     *@param ifflight：是否关联航班
     *@param fltid：航班动态ID
     *@param flightNumber：航班号
     *@param flightDate：航班日期
     *@param ifauto：是否定时
     *@param autoSendTime：定时时间
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年9月15日 Maxx [变更描述]
     */
    public JSONObject createMessageInfo(JSONObject data) ;
	
    /**
     * 
     *Discription:手持查询消息信息.
     *@param id：消息ID
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年9月15日 Maxx [变更描述]
     */
	public JSONObject queryMessageInfoById(JSONObject data);
	
	/**
     * 
     *Discription:查询消息信息.
     *@param id：消息ID
     *@param status：消息状态
     *@param flightNumber：航班号
     *@param mtext：消息正文
     *@param flightDate：航班日期
     *@param userId：用户ID
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年9月15日 Maxx [变更描述]
     */
	public JSONObject queryMessage(JSONObject data);
	
	/**
	 * 
	 *Discription:更新消息信息.
	 *@param type 1:回复 2：代回复 3：忽略 4：取消
	 *@param dataMap 1:mid\operatorId 2:mid/userId\operatorId 3:mid\operatorId 4:mid
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2017年9月15日 Maxx [变更描述]
	 */
	public JSONArray updateMassage(JSONObject data);
	
	/**
	 * 
	 *Discription:删除消息.
	 *@param date 删除消息日期
	 *@param ifHis 是否保存到历史表中
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2017年9月15日 Maxx [变更描述]
	 */
	public Boolean deleteMessage(JSONObject data);

	/**
	 * 
	 *Discription:创建订阅消息.
	 *@param data
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2017年10月26日 Maxx [变更描述]
	 */
    public JSONObject createSubscribeMess(JSONObject data);
	
    /**
     * 
     *Discription:离线消息数量.
     *@param data
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年11月1日 Maxx [变更描述]
     */
    public int getOfflineMessageCount(JSONObject data);
    /**
     * 
     *Discription:替换参数.
     *@param values
     *@param datas
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年11月13日 Maxx [变更描述]
     */
    public JSONArray transMessageVars( JSONObject values,JSONArray datas);
    
    /**
     * 
     *Discription:删除离线表中消息记录.
     *@param list
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年11月14日 Maxx [变更描述]
     */
    public void deleteOfflineMessage(JSONArray list);
    
    /**
     * 
     *Discription:手持未读消息个数查询.
     *@param data
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年11月22日 Maxx [变更描述]
     */
    public int queryMessageUnreadCount(JSONObject data);

    
}
