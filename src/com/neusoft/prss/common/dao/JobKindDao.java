package com.neusoft.prss.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.CrudDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.framework.modules.sys.entity.Role;
import com.neusoft.prss.common.entity.JobKind;

@MyBatisDao
public interface JobKindDao extends CrudDao<JobKind> {
	/**
	 * 根据部门获取保障类型
	 * @param officeId 部门ID
	 * 
	 * @return
	 */
	List<JobKind> getJobKindByOffice(@Param("officeId")String officeId);
	
	/**
	 * 根据角色获取保障类型
	 * @param roles
	 * @return
	 */
	List<JobKind> getJobKindByRole(@Param("roles") List<Role> roles);
}
