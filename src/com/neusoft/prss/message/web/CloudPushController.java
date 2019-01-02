/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 上午10:20:13
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.web;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.prss.message.common.MsgsUtil;
import com.neusoft.prss.message.service.MessageService;

@RestController 
@RequestMapping(value = "${adminPath}/cloudpush/rest")
public class CloudPushController {

    @Resource
    private MessageService messageService;
    
    private static Logger logger = LoggerFactory.getLogger(CloudPushController.class);

    @RequestMapping(value = "pushresult" ,method= RequestMethod.POST)
    @ResponseBody
    public JSONArray pushresult(JSONArray datas){
        for(int i=0;i<datas.size();i++){
            JSONObject data = datas.getJSONObject(i);
            String push = data.getString("push");
            if("success".equals(push)){
                try {
                    messageService.deleteOfflineMessageByMsgId(data);
                } catch (Exception e) {
                    logger.info("CloudPush系统调用回执接口失败！数据： 【{}】 ",data);
                }
            } else {
                JSONObject obj = new JSONObject();
                obj.put("AID", Global.getConfig("cp.appId"));
                obj.put("CL", Global.getConfig("cp.cl"));
                obj.put("DT", Global.getConfig("cp.appId"));
                String str = obj.toJSONString();
                String resultString = "";
                try {
                    resultString = MsgsUtil.remove(str);
                    logger.info("CloudPush系统调用删除离线消息接口结果！ 【{}】 ",resultString);
                } catch (Exception e) {
                    logger.info("CloudPush系统调用删除离线消息接口失败！数据： 【{}】 ",data);
                }
            }
        }
        return null;
    }

}
