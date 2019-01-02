package com.neusoft.prss.message.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.message.entity.HistoryMessageVO;


@MyBatisDao
public interface HistoryMessageDao {
	
    public List<Map<String,String>> getList(Map<String, Object> param);
	
	public int getCount(Map<String, Object> param);
	
	public HistoryMessageVO searchHisDetail(Map<String,String> map);
	
	List<HistoryMessageVO> reciverDetail(Map<String,String> map);
  
    
    JSONArray getFileIds(Map<String, String> param);
    
    public List<Map<String,String>> gethistoryListPrint(@Param("mflightdate") String mflightdate,@Param("flightnumber") String flightnumber,@Param("mtitle") String mtitle,@Param("mtext") String mtext);

    public JSONArray queryFlow(Map<String,String> map);

    public JSONObject queryFlowTrans(Map<String,String> map);

    public List<Map<String,String>> gethistoryListPrint(Map<String,Object> param);

    public String queryParentTrans(Map<String,String> param);

    public int queryFlowTransNum(Map<String,String> map);
    
}
