package com.neusoft.prss.telegraph.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.message.entity.CommonVO;

@MyBatisDao
public interface TeleGraphTemplDao {

	public JSONArray getTree();

	public JSONArray getTelegraphList(Map<String, Object> param);

	public int getTelegraphListCount(Map<String, Object> map);

	public JSONObject queryTelegraphById(@Param("id") String id);

	public void deleteTemplList(@Param("array") String ids);

	public void deleteTemplFromList(@Param("array") String ids);

	public void deleteTemplToList(@Param("array") String ids);

	public JSONArray getTypeList();

	public List<CommonVO> getProcvars();

	public JSONObject getTelegraphTemplById(@Param("id") String id);

	public JSONArray getSiteList(Map<String, String> map);

	public String getTelegraphId();

	public void inserSender(@Param("vo") JSONObject vo, @Param("sendArray") JSONArray sendArray);

	public void insertReceiver(@Param("vo") JSONObject vo, @Param("receiveArray") JSONArray receiveArray);

	public void insertTelegraphInfo(@Param("vo") JSONObject vo);

	public void updateTelegraphInfo(@Param("vo") JSONObject vo);

	public void deleteSender(@Param("id") String id);

	public void deleteReveiver(@Param("id") String id);

	public JSONArray getListById(@Param("flag") String flag, @Param("id") String id);

	public JSONArray getAddressByTempId(@Param("id") String id);

    public JSONObject getTemplCount(@Param("id") String id);
    
    public JSONArray getPriorityList();


}
