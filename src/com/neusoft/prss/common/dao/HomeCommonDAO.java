package com.neusoft.prss.common.dao;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface HomeCommonDAO {

	String hasDynamicRole(@Param("userId") String userId);

}
