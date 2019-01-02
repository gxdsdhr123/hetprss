/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月21日 上午10:03:21
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.dao;

import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface MessageDao {

    public JSONArray getMessageInfo(HashMap<String,Object> dataMap) ;

    public JSONArray getTemplate(HashMap<String,Object> dataMap);

    public JSONArray getTemplateToList(HashMap<String,String> dataMap);

    public JSONObject getTemplateById(HashMap<String,String> dataMap);

    public JSONArray getSendMessage(HashMap<String,Object> dataMap);

    public void insertMessageFile(Map<String,String> dataMap);

    public JSONObject getFilesInfo(@Param("fileid")String tid);

    public void deleteOfflineMessage(JSONObject data);

    public JSONArray getUserList(@Param("vo") JSONObject json);

    public void deleteOfflineMessageByMsgId(JSONObject data);

    public JSONArray getDelay();

    public JSONArray getUnReadNum(HashMap<String,Object> dataMap);

    public void insertFavorite(JSONObject formData);

    public void deleteFavorite(JSONObject formData);

}
