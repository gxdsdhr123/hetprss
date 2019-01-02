package com.neusoft.prss.leadershipScheduling.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neusoft.prss.leadershipScheduling.dao.LeadershipSchedulingDao;
import com.neusoft.prss.leadershipScheduling.entity.AmLeaderPlan;

@Service
@Transactional(readOnly = true)
public class LeadershipSchedulingService{

	@Autowired
	private LeadershipSchedulingDao leadershipSchedulingDao;
	/**
	 * 保存
	 * @param flag 
	 * @param officeConf
	 * @return
	 */
	public String saveOrUpdate(String officeId, String seqNum, String activeFlag, String flag) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("officeId", officeId);
		if(null == seqNum || "".equals(seqNum)) {	
			if(null == leadershipSchedulingDao.getSeqMaxNum()) {				
				map.put("seqNum", 1);
			} else {
				map.put("seqNum", Integer.valueOf(leadershipSchedulingDao.getSeqMaxNum())+1);				
			}
		} else {			
			map.put("seqNum", Integer.valueOf(seqNum));
		}
		map.put("activeFlag", activeFlag);
		try {
			List<Map<String,Object>> list;
			if("add".equals(flag)) { // 增加
				list = leadershipSchedulingDao.queryForOfficeId(map);
				if(list.size() != 0) {
					return "f1";
				} else {
					return leadershipSchedulingDao.saveDept(map) > 0?"s1": "f1";
				}
			} else if("modify".equals(flag)) { // 修改
				return leadershipSchedulingDao.updateDept(map) > 0?"s2": "f2";
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			return "f";
		}
		return "f";
	}

	/**
	 * 删除当前部门及其所有计划
	 * @param officeId
	 * @return
	 */
	public String delDept(String officeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("officeId", officeId);
		// 删除当前部门下的所有计划
		// 删除部门计划之前查询是否该部门存在计划
		List<Map<String,Object>> list = leadershipSchedulingDao.queryLeaderDetail(map);
		int i = 0;
		if(list.size() != 0) {
			i = leadershipSchedulingDao.delStaff(map);			
		}
		int j = leadershipSchedulingDao.delDept(map);
		return i + j > 0? "success": "faile";
	}
	/**
	 * 得到表格数据
	 * @param searchTime 
	 * @return
	 */
	public List<Map<String, Object>> getGriData(String searchTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchTime", searchTime);
		return leadershipSchedulingDao.getGriData(map);
	}

	/**
	 * 模糊查询该部门下的员工
	 * @param officeId
	 * @param staffName
	 * @return
	 */
	public List<Map<String, Object>> queryStaffNameByOffice(String officeId, String staffName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("officeId", officeId);
		map.put("staffName", "%"+staffName+"%");
		return leadershipSchedulingDao.queryStaffNameByOffice(map);
	}
	/**
	 * 查询该部门下的所有人员
	 * @param officeId
	 * @return
	 */
	public List<Map<String, Object>> queryStaffNameByOffice(String officeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("officeId", officeId);
		return leadershipSchedulingDao.queryStaffNameByOffice(map);
	}

	/**
	 * 增加人员计划
	 * @param amLeaderPlan
	 */
	public String insertStaff(AmLeaderPlan amLeaderPlan) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("workerId", amLeaderPlan.getWorkerId());
		map.put("startTm", amLeaderPlan.getStartTm());
		map.put("endTm", amLeaderPlan.getEndTm());
		map.put("pdate", amLeaderPlan.getPdate());
		map.put("officeId", amLeaderPlan.getOfficeId());
//		Integer ids;
		if(null == amLeaderPlan.getId() || "".equals(amLeaderPlan.getId().trim())) { // id 不存在默认新增
//			String id = leadershipSchedulingDao.queryWorkerId();
//			if(null == id || id.isEmpty() || "".equals(id)) {
//				ids = 1;
//			} else {
//				ids = Integer.valueOf(id) + 1;
//			}
//			map.put("id", ids);			
			return leadershipSchedulingDao.insertStaff(map) > 1? "success": "faile";
		} else { // 修改
			map.put("id", amLeaderPlan.getId());
			return leadershipSchedulingDao.updateStaff(map)> 1? "success": "faile";
		}
		
	}

	/**
	 * 获取当前数据
	 * @param officeId
	 * @param pdate 
	 * @param workerId 
	 * @return
	 */
	public List<Map<String, Object>> queryLeaderDetail(String officeId, String flag, String pdate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("officeId", officeId);
		map.put("flag", flag);
		map.put("id", flag);
		map.put("pdate", pdate);
		return leadershipSchedulingDao.queryLeaderDetail(map);
	}

	/**
	 * 删除当前员工计划
	 * @param officeId
	 * @param staffId
	 * @param pdate 
	 * @return
	 */
	public String delStaff(String officeId, String staffId, String pdate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("officeId", officeId);
		// staffIfd 存在则删除选中部门选中天当前员工计划
		map.put("staffId", staffId);
		// pdate 存在则删除选中部门选中天计划
		if(null != pdate) {
			map.put("pdate", pdate);
			// 查询该部门该天是否存在计划
			List<Map<String,Object>> list = leadershipSchedulingDao.queryLeaderDetail(map);
			if(list.size() == 0) {
				return "success";
			}
		}
		return leadershipSchedulingDao.delStaff(map) > 0? "success": "faile";
	}

	/**
	 * 取得当前日期前后共三天计划
	 * @param dayBefore 前一天
	 * @param dayafter  后一天
	 * @return
	 */
	public Object getGriData(String dayBefore, String dayafter) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dayBefore", dayBefore);
		map.put("dayafter", dayafter);
		return leadershipSchedulingDao.getGriData(map);
	}

	/**
	 * 获取部门下拉选
	 * @param flag 
	 * @return
	 */
	public List<Map<String, Object>> getOfficeSelect(String flag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		return leadershipSchedulingDao.getOfficeSelect(map);
	}
}
