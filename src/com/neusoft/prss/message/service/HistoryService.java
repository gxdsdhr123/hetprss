package com.neusoft.prss.message.service;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.message.entity.HistoryMessageVO;

public interface HistoryService {

    public Map<String,Object> getListInfo(Map<String, Object> param) ;

    public HistoryMessageVO searchHisDetail(Map<String,String> map);

    public List<HistoryMessageVO> reciverDetail(Map<String,String> map);

    public JSONArray getFileIds(Map<String,String> param);

    public String queryParentTrans(Map<String,String> param);

    public JSONArray queryFlow(Map<String,String> map);

    public JSONObject queryFlowTrans(Map<String,String> map);

    public Object queryFlowTransNum(Map<String,String> map);
}
