/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 上午10:21:32
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.message.dao.MessageTypeDao;
import com.neusoft.prss.message.entity.MessageTypeVO;

@Service
public class MessageTypeService {

    @Resource
    private MessageTypeDao messageTypeDao;
    
    public JSONArray getMessageType() {
        JSONArray arr = messageTypeDao.getMessageType();
        return arr;
    }
    
    public MessageTypeVO selectOne(Map<String,String> map){
        return messageTypeDao.selectOne(map);
    }
    
    
    public void insertItem(MessageTypeVO vo){
        messageTypeDao.insertItem(vo);
    }
    
    public void deleteItem(String id){
        messageTypeDao.deleteItem(id);
    }
    
    public void updateItem(MessageTypeVO vo){
        messageTypeDao.updateItem(vo);
    }
    
    
    public MessageTypeVO selectOneByCode(Map<String,String> map){
        return messageTypeDao.selectOneByCode(map);
    }
    
    public int selectCount(Map<String,String> map){
        return messageTypeDao.selectCount(map);
    }
    
    public List<MessageTypeVO> selectAll(Map<String,String> map){
        return messageTypeDao.selectAll(map);
    }

    public int getTemplCount(String id) {
        return messageTypeDao.getTemplCount(id);
    }
}
