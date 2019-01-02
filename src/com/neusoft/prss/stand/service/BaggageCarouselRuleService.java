/**
 *application name:aodb-web-service
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年5月5日 下午5:16:12
 *@author:neusoft
 *@version:[v1.0]
 */
package com.neusoft.prss.stand.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface BaggageCarouselRuleService {

    /**
     * 
     *Discription:获取规则列表.
     *@param param
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月9日 neusoft [变更描述]
     */
    Map<String,Object> getRuleList(Map<String,Object> param);

    /**
     * 
     *Discription:获取规则信息.
     *@param id
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月9日 neusoft [变更描述]
     */
    JSONObject loadRuleInfo(String id);

 

    /**
     * 
     *Discription:规则保存.
     *@param formData
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月9日 neusoft [变更描述]
     */
    JSONObject save(JSONObject formData);

    /**
     * 
     *Discription:删除规则.
     *@param ruleId
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月9日 neusoft [变更描述]
     */
    void delete(String ruleId);

    /**
     * 
     *Discription:增加参数.
     *@param formData
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月9日 neusoft [变更描述]
     */
    void saveParam(JSONArray formData);



}
