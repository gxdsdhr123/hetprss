package com.neusoft.prss.common.dao;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface VideoCommonDAO {

	String getServerName();

	String getServerPort();

	String getServerPassword();

	String getServerUser();

	String getFilePath(@Param("fType") String fType, @Param("code") String code);
}
