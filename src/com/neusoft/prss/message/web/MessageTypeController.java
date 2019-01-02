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

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.message.entity.MessageTypeVO;
import com.neusoft.prss.message.entity.MessageUtils;
import com.neusoft.prss.message.service.MessageTypeService;

@Controller
@RequestMapping(value = "${adminPath}/message/type")
public class MessageTypeController extends BaseController {

    @Resource
    private MessageTypeService messageTypeService;

    @RequestMapping(value = "list")
    public String list() {
        return "prss/message/messageManager";
    }
    /**
     * 
     *Discription:获取模板类型.
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年8月29日 Maxx [变更描述]
     */
    @RequestMapping(value = "getMessageType")
    @ResponseBody
    public JSONArray getMessageType() {
        return messageTypeService.getMessageType();
    }
    /**
     * 
     *Discription:获取消息模板类型新增、查看、修改页面.
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年8月31日 Maxx [变更描述]
     */
    @RequestMapping(value = "info")
    public String info(HttpServletRequest request,HttpServletResponse response,Model model) {
        String id = request.getParameter("id");
        String flag = request.getParameter("flag");
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", id);
        MessageTypeVO vo = messageTypeService.selectOne(map);

        model.addAttribute("id", id);
        model.addAttribute("flag", flag);
        model.addAttribute("vo", vo);
        return "prss/message/messageTypeInfo";
    }

    @RequestMapping(value = "getType")
    @ResponseBody
    public MessageTypeVO getType(HttpServletRequest request,HttpServletResponse response ,Model model){
        String id= request.getParameter("id");
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", id);
        return messageTypeService.selectOne(map);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public String save(MessageTypeVO vo,HttpServletRequest request,HttpServletResponse response ,Model model) {
        String flag = request.getParameter("flag");
        try {
            String type = vo.getTcode();
            String name = vo.getTname();
            String disc = vo.getDisc();
            if(type == null || "".equals(type))
                return MessageUtils.ERROR_MSG;
            if(name == null || "".equals(name))
                return MessageUtils.ERROR_MSG;
            if(disc == null || "".equals(disc))
                return MessageUtils.ERROR_MSG;
            
            if("add".equals(flag)){
                messageTypeService.insertItem(vo);
            } else if("upd".equals(flag)){
                messageTypeService.updateItem(vo);
            }
            return MessageUtils.SUCCESS_MSG;
        } catch (Exception e) {
            return MessageUtils.ERROR_MSG;
        }
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(String id){
        try {
            int count = messageTypeService.getTemplCount(id);
            if(count<=0){
                messageTypeService.deleteItem(id);
                return MessageUtils.SUCCESS_MSG;
            } else {
                return "类型中有模板不能被删除";
            }
        } catch (Exception e) {
            return MessageUtils.ERROR_MSG;
        }
    }
}
