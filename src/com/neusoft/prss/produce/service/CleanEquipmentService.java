package com.neusoft.prss.produce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.dao.CleanEquipmentDao;

@Service
public class CleanEquipmentService {
	private Logger logger=Logger.getLogger(CleanEquipmentService.class);
	
	@Autowired
	private CleanEquipmentDao cleanEquipmentDao;

	
	//----------------列表--------------------
	public Map<String,Object> getRuleList(Map<String, Object> param) {
        //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= cleanEquipmentDao.getListCount(param);
        List<Map<String, Object>> rows = cleanEquipmentDao.getList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }
	


    /**
     * 保存/更新
     * 
     * @param schema
     * @param formData
     * @param airplaneType
     * @param airlineType
     * @param condition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject save(JSONObject formData) throws Exception {
        JSONObject msg = new JSONObject();
        
        msg.put("code", 0);
        msg.put("msg", "");

        return msg;
    }



    public JSONArray getDataList(JSONObject param) {
        return cleanEquipmentDao.getDataList(param);
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateSql(JSONObject formData) {
        if(!StringUtils.isBlank(formData.getString("jc_id"))){
            cleanEquipmentDao.updateJCSql(formData);
        }
        if(!StringUtils.isBlank(formData.getString("sc_id"))){
            cleanEquipmentDao.updateSCSql(formData);
        }
       
    }



    public List<String> getFileId(JSONObject data) {
        return cleanEquipmentDao.getFileId(data);
    }



    public String getFileName(String fileId) {
        return cleanEquipmentDao.getFileName(fileId);
    }

}
