package com.neusoft.prss.message.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.message.dao.MessageSubscribeDao;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.message.entity.MessageSubscribeVO;
import com.neusoft.prss.message.entity.SRListVO;



@Service
@Transactional(readOnly=true)
public class MessageSubscribeService {

    @Resource
    private MessageSubscribeDao messagesubscribeDao;

    public Map<String,Object> getsubscribeList(Map<String, Object> param) {

        //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= messagesubscribeDao.getsubscribeListCount(param);
        List<MessageSubscribeVO> rows=messagesubscribeDao.getsubscribeList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }


    public MessageSubscribeVO getdetailPage(Map<String, String> map) {
        return messagesubscribeDao.getdetailPage(map);
    }

    public List<CommonVO> getDimSysEvents(String userType) {
        return messagesubscribeDao.getDimSysEvents(userType);
    }
    public List<CommonVO> getFiotype() {
        return messagesubscribeDao.getFiotype();
    }

    public List<SRListVO> varList(Map<String, String> map) {
        return messagesubscribeDao.varList(map);
    }

    public String getRmSeq() {
        return messagesubscribeDao.getRmSeq();
    }

    @Transactional(readOnly=false)
    public void insert(MessageSubscribeVO vo) {
        messagesubscribeDao.insertDimSysEvents(vo);
        messagesubscribeDao.insertRmRuleIfno(vo);
    }

    @Transactional(readOnly=false)
    public void update(MessageSubscribeVO vo) {
        messagesubscribeDao.updateDimSysEvents(vo);
        messagesubscribeDao.updateRmRuleIfno(vo);
    }

    @Transactional(readOnly=false)
    public void delete(String[] ids,String[] ruleids) {
        messagesubscribeDao.deleteDimSysEvents(ids);
        messagesubscribeDao.deleteRmRuleIfno(ruleids);
    }


    public String getScheSeq() {
        return messagesubscribeDao.getScheSeq();
    }

    public JSONArray loadJobKind() {
        return messagesubscribeDao.loadJobKind();
    }
}
