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
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.message.entity.MessageTypeVO;

@MyBatisDao
public interface MessageTypeDao {

    JSONArray getMessageType(); 
    
    void insertItem(MessageTypeVO vo);
    
    void deleteItem(String id);
    
    void updateItem(MessageTypeVO vo);
    
    MessageTypeVO selectOne(Map<String,String> map);
    
    MessageTypeVO selectOneByCode(Map<String,String> map);
    
    int selectCount(Map<String,String> map);
    
    List<MessageTypeVO> selectAll(Map<String,String> map);

    int getTemplCount(String id);

}
