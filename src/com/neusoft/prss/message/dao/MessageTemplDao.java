/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 上午10:22:24
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.message.entity.MessageTemplFromVO;
import com.neusoft.prss.message.entity.MessageTemplToVO;
import com.neusoft.prss.message.entity.MessageTemplVO;
import com.neusoft.prss.message.entity.SRListVO;


@MyBatisDao
public interface MessageTemplDao {

    public List<MessageTemplVO> getMessageTmpl();
    
    void insertItem(MessageTemplVO vo);
    
    void deleteItem(@Param("ids")String ids);
    
    void updateItem(MessageTemplVO vo);
    
    MessageTemplVO selectOne(Map<String,String> map);
    
    public MessageTemplVO selectOneByCode(Map<String,String> map);

    public List<CommonVO> getParam();
    
  
    public List<CommonVO> getSenderType();
    
    //发送人范围列表
    public List<MessageTemplFromVO> getSenderList(Map<String,String> map);
    
    //接受人范围列表
    public List<MessageTemplToVO> getReciverList(Map<String,String> map);
    
    //发送人范围数据插入
    void inserSender(MessageTemplVO vo);
    
    //接收人范围数据插入
    void insertReciver(MessageTemplVO vo);
    //删除发送人范围
    void deleteSender(@Param("ids")String ids);
    //删除接收人范围
    void deleteReciver(@Param("ids")String ids);
    //修改发送人范围
    void updateSender(MessageTemplFromVO vo);
    //修改接收人范围
    void updateReciver(MessageTemplToVO vo);
    
    //角色列表数据
    public List<SRListVO> getRoleList(Map<String,String> map);
    
    //人员列表数据
    public List<SRListVO> getUserList(Map<String,String> map);
    
    //模板信息表 
    public List<SRListVO> getmMessageList(Map<String,String> map);
    
    public List<SRListVO>  searchEvent(Map<String,String> map);
   
    JSONArray getDepTree(); 
    
    //处理类下拉
    public List<CommonVO> getProcvars();
    //获取发送人信息
   
    public MessageTemplFromVO getsenderMessage();
     
    //获取模板序列
     
    @Select("select MM_TEMPLATE_S.nextval id from dual")
    public String getTidSeq();
    @Select("select MM_TEMPLATE_FROM_S.nextval id from dual")
    public String senderseq();
    @Select("select MM_TEMPLATE_TO_S.nextval id from dual")
    public String reciverseq();

    public void insertReciverRule(MessageTemplToVO vo);

    public void deleteReciverRule(String id);

    public int getListCount(Map<String,Object> param);

    public JSONArray getList(Map<String,Object> param);

    public JSONObject getTemplCount(@Param("id")String id);

    public JSONArray getKindJob();

    public int filterTemplate(Map<String,String> map);
   
}
