package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface TractorRecordDao {

	
	public List<Map<String, Object>> getList(Map<String, Object> param);
	
	public int getListCount(Map<String, Object> param);

    public JSONArray getDataList(JSONObject param);

    public void updateSql(JSONArray formData);

    public void updateSCSql(JSONObject formData);

    public void updateJCSql(JSONObject formData);

    public void updateCHSql(JSONObject formData);

    public List<String> getFileId(JSONObject data);

    public String getFileName(String fileId);

}
