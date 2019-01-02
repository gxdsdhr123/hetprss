/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 上午10:21:32
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.dao.ParamCommonDao;
import com.neusoft.prss.message.dao.MessageTemplDao;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.message.entity.MessageTemplFromVO;
import com.neusoft.prss.message.entity.MessageTemplToVO;
import com.neusoft.prss.message.entity.MessageTemplVO;
import com.neusoft.prss.message.entity.SRListVO;


@Service
@Transactional(readOnly=true)
public class MessageTemplService {

    @Resource
    private MessageTemplDao messageTemplDao;

    @Resource
    private ParamCommonDao paramCommonDao;

    public MessageTemplVO selectOne(Map<String, String> map) {
        return messageTemplDao.selectOne(map);
    }

    public void insertItem(MessageTemplVO vo) {
        messageTemplDao.insertItem(vo);
    }

    public void updateItem(MessageTemplVO vo) {
        messageTemplDao.updateItem(vo);
    }

    public MessageTemplVO selectOneByCode(Map<String, String> map) {
        return messageTemplDao.selectOneByCode(map);
    }

    public List<CommonVO> getParam() {
        return messageTemplDao.getParam();
    }

    // 发送人类型
    public List<CommonVO> getSenderType() {
        return messageTemplDao.getSenderType();

    }

    // 发送人范围列表
    public List<MessageTemplFromVO> getSenderList(Map<String, String> map) {
        return messageTemplDao.getSenderList(map);
    }

    public List<MessageTemplToVO> getReciverList(Map<String, String> map) {
        return messageTemplDao.getReciverList(map);
    }

    // 发送人范围新增
    public void inserSender(MessageTemplVO vo) {
        messageTemplDao.inserSender(vo);
    }

    // 接收人范围新增
    public void insertReciver(MessageTemplVO vo) {
        messageTemplDao.insertReciver(vo);
    }

    // 角色列表
    public List<SRListVO> getRoleList(Map<String, String> map) {
        return messageTemplDao.getRoleList(map);
    }
    //人员列表
    public List<SRListVO> getUserList(Map<String, String> map) {
        return messageTemplDao.getUserList(map);
    }

    //变量列表
    public List<SRListVO> getmMessageList(Map<String, String> map) {
        return messageTemplDao.getmMessageList(map);
    }
    //变量列表
    public List<SRListVO> searchEvent(Map<String, String> map) {
        return messageTemplDao.searchEvent(map);
    }

    //部门树
    public JSONArray getDepTree() {
        JSONArray arr = messageTemplDao.getDepTree();
        return arr;
    }
    //处理类下拉
    public List<CommonVO> getProcvars() {
        return messageTemplDao.getProcvars();
    }
    //获取发送人范围详细信息
    public MessageTemplFromVO getsenderMessage() {
        MessageTemplFromVO messagetemplfromVO = new MessageTemplFromVO();
        messagetemplfromVO = messageTemplDao.getsenderMessage();
        return messagetemplfromVO;
    }

    public String getTidSeq() {
        return messageTemplDao.getTidSeq();
    }
    public String senderseq() {
        return messageTemplDao.senderseq();
    }
    public String reciverseq() {
        return messageTemplDao.reciverseq();
    }

    @Transactional(readOnly=false)
    public boolean insert(MessageTemplVO vo) {
        try {
            String id=messageTemplDao.getTidSeq();
            vo.setId(id);
            //操作人
            messageTemplDao.inserSender(vo);
            List<MessageTemplToVO> list = vo.getTabdata1();
            for(int i=0;i<list.size();i++){
                MessageTemplToVO toVO = list.get(i);
                String expression = toVO.getDrlStr();
                if(expression != null && !"".equals(expression)){
                    String ruleId = paramCommonDao.getAutoRuleId();
                    toVO.setRuleId(ruleId);
                    messageTemplDao.insertReciverRule(toVO);
                }
            }
            messageTemplDao.insertReciver(vo);
            messageTemplDao.insertItem(vo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Transactional(readOnly=false)
    public void update(MessageTemplVO vo) {
        String id=vo.getId();
        messageTemplDao.deleteSender(id);
        messageTemplDao.deleteReciverRule(id);
        messageTemplDao.deleteReciver(id);
        
        List<MessageTemplToVO> list = vo.getTabdata1();
        for(int i=0;i<list.size();i++){
            MessageTemplToVO toVO = list.get(i);
            String expression = toVO.getDrools();
            if(expression != null && !"".equals(expression)){
                String ruleId = paramCommonDao.getAutoRuleId();
                toVO.setRuleId(ruleId);
                toVO.setUserId(UserUtils.getUser().getId());
                messageTemplDao.insertReciverRule(toVO);
            } else {
                toVO.setRuleId(null);
            }
        }
        messageTemplDao.inserSender(vo);
        messageTemplDao.insertReciver(vo);
        messageTemplDao.updateItem(vo);
    }

    public Map<String,Object> getList(Map<String,Object> param) {
      //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= messageTemplDao.getListCount(param);
        JSONArray rows=messageTemplDao.getList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }
    @Transactional(readOnly=false)
    public void delete(String ids) {
        messageTemplDao.deleteItem(ids);
        messageTemplDao.deleteSender(ids);
        messageTemplDao.deleteReciver(ids);
    }

    public JSONObject getTemplCount(String id) {
        return messageTemplDao.getTemplCount(id);
    }

    public JSONArray getKindJob() {
        JSONArray arr = messageTemplDao.getKindJob();
        return arr;
    }

    public int filterTemplate(Map<String,String> map) {
        return messageTemplDao.filterTemplate(map);
    }
    
}
