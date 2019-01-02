package com.neusoft.prss.abnormalFlightManagement.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.abnormalFlightManagement.entity.FdFltAbnormalFeedBack;
import com.neusoft.prss.abnormalFlightManagement.entity.FdFltAbnorrmal;
/**
 * 不正常航班持久层
 * @author lwg
 * @date 2017/12/19
 */
@MyBatisDao
public interface AbnormalFlightManagementDao {

	/**
	 * 获取发送部门
	 * @return
	 */
	public List<Map<String, Object>> getSenDepart();

	/**
	 * 获取航班信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAbnormalFlightInfo(Map<String, Object> map);

	/*
	 * 不正常航班管理数据增加
	 * @param map
	 * @return
	 */
	public int addAbnormalFlightAbnormal(FdFltAbnorrmal fdFltAbnorrmal);

	/**
	 * 不正常航班反馈表信息插入
	 * @param map
	 */
	public int addAbnormalFlightAbnormalFeedackBatch(List<FdFltAbnormalFeedBack> list);
	/**
	 * 获取反馈表中最大id值
	 * @return
	 */
//	public String getBackMaxId();

	/**
	 * 获取表格数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGridData(Map<String, Object> map);

	/**
	 * 获取不正常航班信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAbnormalFlightById(Map<String, Object> map);

	/**
	 * 查询该不正常航班所涉及的部门
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepartFeedBackInfo(Map<String, Object> map);

	/**
	 * 删除不正常航班信息
	 * @param map
	 * @return
	 */
	public int delAbnormalFlight(Map<String, Object> map);

	/**
	 * 删除不正常航班反馈信息
	 * @param map
	 * @return
	 */
	public int delAbnormalFeedBack(Map<String, Object> map);

	/**
	 * 不正常航班涉及当前部门反馈
	 * @param map
	 * @return
	 */
	public int feedBackAirRep(Map<String, Object> map);

	/**
	 * CDM判责信息录入
	 * @param map
	 * @return
	 */
	public int aCDMContractorResponsible(Map<String, Object> map);

	/**
	 * 获取航空公司
	 * @return
	 */
	public List<Map<String, Object>> getAirFlightList();
	
	//获取航班信息    --begin--
	public List<Map<String, Object>> getAbnormalFlightInfoFromPrss(Map<String, Object> map);

	public List<Map<String, Object>> getAbnormalFlightInfoFromPrssp(Map<String, Object> map);

	public List<Map<String, Object>> getAbnormalFlightInfoFromPrssA0(Map<String, Object> map);

	public List<Map<String, Object>> getAbnormalFlightInfoFromPrssD0(Map<String, Object> map);

	public List<Map<String, Object>> getAbnormalFlightInfoFromPrsspA0(Map<String, Object> map);

	public List<Map<String, Object>> getAbnormalFlightInfoFromPrsspD0(Map<String, Object> map);
	//获取航班信息    --end--

	public List<String> getOfficeNames(List<String> list);
}
