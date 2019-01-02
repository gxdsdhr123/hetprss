/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年1月3日 下午4:44:13
 *@author:neusoft
 *@version:[v1.0]
 */
package com.neusoft.prss.getflthis.service;

import java.util.List;
import com.alibaba.fastjson.JSONObject;

public interface GetFltHisService {
    /** 
    * @Description 获取参数值
    * @param
    * @return JSONObject
    * @update 2017年10月29日 上午10:07:48 hujl 
    */
    JSONObject queryParamVars(String fltid,String varcols);

    /** 
    * @Description 查询colID所对应的参数值
    * @param colID :(List<String> 列ID)
    * @param fltID : 航班号ID
    * @return JSONObject :{"fltID":航班号,"列别名1":value1,"列别名2:value2}
    * @update 2017年10月30日 上午8:56:13 hujl 
    */
    JSONObject getFltInfo(List<String> colID,String fltID,String planID);

}
