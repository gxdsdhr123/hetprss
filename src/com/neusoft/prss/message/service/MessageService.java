/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月21日 上午10:02:46
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.service;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.message.dao.MessageDao;

@Service
public class MessageService {

    @Resource
    private MessageDao messageDao;
    
    public JSONArray getUnReadMessage(HashMap<String,Object> dataMap){
        return messageDao.getMessageInfo(dataMap);
    }
    
    public JSONArray getTemplate(HashMap<String,Object> dataMap){
        return messageDao.getTemplate(dataMap);
    }

    public JSONArray getTemplateToList(HashMap<String,String> dataMap) {
        return messageDao.getTemplateToList(dataMap);
    }

    public JSONObject getTemplateById(HashMap<String,String> dataMap) {
        JSONObject templ =  messageDao.getTemplateById(dataMap);
//        String condition = templ.getString("CONDITION");
        
        
        return templ;
    }

    public JSONArray getSendMessage(HashMap<String,Object> dataMap) {
        return messageDao.getSendMessage(dataMap);
    }

    public void insertMessageFile(Map<String,String> dataMap) {
        messageDao.insertMessageFile(dataMap);
    }

    public JSONObject getFilesInfo(String fileid) {
        return messageDao.getFilesInfo(fileid);
    }

    public void deleteOfflineMessage(JSONObject data) {
        messageDao.deleteOfflineMessage(data);
    }

    public JSONArray getUserList(JSONObject json) {
        return messageDao.getUserList(json);
    }

    public void deleteOfflineMessageByMsgId(JSONObject data) {
        messageDao.deleteOfflineMessageByMsgId(data);
    }

    public JSONArray getDelay() {
        return messageDao.getDelay();
    }

    public JSONArray getUnReadNum(HashMap<String,Object> dataMap) {
        return messageDao.getUnReadNum(dataMap);
    }

    public void insertFavorite(JSONObject formData) {
        messageDao.insertFavorite(formData);
    }

    public void deleteFavorite(JSONObject formData) {
        messageDao.deleteFavorite(formData);
    }
}
