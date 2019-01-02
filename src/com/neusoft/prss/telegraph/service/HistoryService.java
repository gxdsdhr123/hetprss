/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:lr
 *@version:[v1.0]
 */
package com.neusoft.prss.telegraph.service;

import java.util.List;
import java.util.Map;

public interface HistoryService {
    

    public Map<String,Object> getListInfo(Map<String, Object> param) ;
    
    public void isRead(Map<String,Object> param) ;

    public List<Map<String,String>> getList(Map<String,Object> param);

    public void pigeonhole(Map<String,String> map);
}
