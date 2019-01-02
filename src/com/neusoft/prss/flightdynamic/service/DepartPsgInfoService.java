/**
 *application name:btprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年7月10日 上午9:48:59
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.service;

import java.util.List;

import com.neusoft.prss.flightdynamic.entity.DepartPsgEntity;

public interface DepartPsgInfoService {
	
    public List<String> getRouteName(String fltid);
    
    public boolean saveDepartPag(DepartPsgEntity entity);
    
    public List<DepartPsgEntity> getPassengerT(String inFltid,String outFltid,Boolean isOverStation,String ioFlag);
    
    public boolean updateDepartPag(DepartPsgEntity entity);

}
