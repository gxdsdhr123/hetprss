/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.neusoft.framework.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.CrudDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.framework.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/* 删除用户相关操作  2018-03-17 SunJ */

	/**
	 * 判断用户是否为班组领班、副领班（0：领班；1：副领班；2：成员）
	 * @param id
	 * @return
	 */
	public List<Map<String,String>> getWorkerLevel(@Param("id") String id);

	/**
	 * 判断用户是否为作业组组长（0：组员；1：组长）
	 * @param id
	 * @return
	 */
	public List<Map<String,String>> getIfLeader(@Param("id") String id);

	/**
	 * 删除人员班组
	 * @param id
	 */
	public void deleteUserFromGroup(@Param("id") String id);

	/**
	 * 删除人员作业组
	 * @param id
	 */
	public void deleteUserFromTeam(@Param("id") String id);

	/**
	 * 删除资质限制
	 * @param id
	 */
	public void deleteUserFromAptitudeLimit(@Param("id") String id);

	/**
	 * 删除资质
	 * @param id
	 */
	public void deleteUserFromAptitudeInfo(@Param("id") String id);

	/**
	 * 删除分工限制
	 * @param id
	 */
	public void deleteUserFromDivisionLimit(@Param("id") String id);
	
	/**
	 * 删除分工明细
	 * @param id
	 */
	public void deleteUserFromDivisionDetail(@Param("id") String id);

	/**
	 * 删除分工
	 * @param id
	 */
	public void deleteUserFromDivisionInfo(@Param("id") String id);

	/**
	 * 删除分工模板限制
	 * @param id
	 */
	public void deleteUserFromDivisionTempLimit(@Param("id") String id);

	/**
	 * 删除分工模板
	 * @param id
	 */
	public void deleteUserFromDivisionTemp(@Param("id") String id);

	/**
	 * 删除人员排班
	 * @param id
	 */
	public void deleteUserFromWorkerPlan(@Param("id") String id);

	/**
	 * 删除设备绑定
	 * @param id
	 */
	public void deleteVehicleBound(@Param("id") String id);

}
