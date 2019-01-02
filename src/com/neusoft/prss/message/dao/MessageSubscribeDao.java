package com.neusoft.prss.message.dao;

import java.util.List;




import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.message.entity.MessageSubscribeVO;
import com.neusoft.prss.message.entity.MessageTemplFromVO;
import com.neusoft.prss.message.entity.MessageTemplVO;
import com.neusoft.prss.message.entity.SRListVO;


@MyBatisDao
public interface MessageSubscribeDao {
	
	public List<MessageSubscribeVO> getsubscribeList(Map<String, Object> map);
	
	public int getsubscribeListCount(Map<String, Object> map);
	 
	public MessageSubscribeVO getdetailPage(Map<String,String> map);
	
	public List<CommonVO> getDimSysEvents(@Param("userType") String userType );
	
	public List<CommonVO> getFiotype();
	
	void deleteDimSysEvents(String[] ids);
	
	void deleteRmRuleIfno(String[] ruleids);
	
	void insertDimSysEvents(MessageSubscribeVO vo);
	
	void insertRmRuleIfno(MessageSubscribeVO vo);
	
	void updateDimSysEvents(MessageSubscribeVO vo);
	
	void updateRmRuleIfno(MessageSubscribeVO vo);
	
	public List<SRListVO> varList(Map<String, String> map);
	
	 @Select("select RM_RULE_INFO_S.nextval rmid from dual")
	 public String getRmSeq();
	 
	 @Select("select MM_SCHE_S.nextval rmid from dual")
     public String getScheSeq();

    public JSONArray loadJobKind();

}
