package com.neusoft.prss.leadershipScheduling.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface LeadershipSchedulingDao {

	/**
	 * 保存或修改当前部门计划
	 * @param map
	 * @return
	 */
	public int saveDept(Map<String, Object> map);

	/**
	 * 获取当前部门最大排序
	 * @return
	 */
	public String getSeqMaxNum();

	/**
	 * 修改当前部门排序
	 * @param map
	 * @return
	 */
	public int updateDept(Map<String, Object> map);
	
	/**
	 * 删除当前部门计划
	 */
	public int delDept(Map<String, Object> map);

	/**
	 * 获取表格数据
	 * @param map 
	 * @return
	 */
	public List<Map<String, Object>> getGriData(Map<String, Object> map);

	/**
	 * 根据部门id查询部门计划
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryForOfficeId(Map<String, Object> map);

	/**
	 * 查询该部门所有计划
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryOfficePlan(Map<String, Object> map);

	/**
	 * 搜索该部门下的该员工
	 * @param map
	 */
	public List<Map<String, Object>> queryStaffNameByOffice(Map<String, Object> map);

	/**
	 * 得到当前表最大id值
	 * @return
	 */
//	public String queryWorkerId();

	/**
	 * 保存人员计划
	 * @param map
	 * @return
	 */
	public int insertStaff(Map<String, Object> map);

	/**
	 * 获取该部门当天计划
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryLeaderDetail(Map<String, Object> map);

	/**
	 * 修改当前人员计划
	 * @param map
	 * @return
	 */
	public int updateStaff(Map<String, Object> map);

	/**
	 * 删除当前人员计划
	 * @param map
	 * @return
	 */
	public int delStaff(Map<String, Object> map);

	/**
	 * 查询部门下拉选， 忽略已添加的部门
	 * @param map 
	 * @return
	 */
	public List<Map<String, Object>> getOfficeSelect(Map<String, Object> map);

}
