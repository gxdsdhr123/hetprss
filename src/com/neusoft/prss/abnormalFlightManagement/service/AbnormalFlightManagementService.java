package com.neusoft.prss.abnormalFlightManagement.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.prss.abnormalFlightManagement.dao.AbnormalFlightManagementDao;
import com.neusoft.prss.abnormalFlightManagement.entity.AbnormalFlightVo;
import com.neusoft.prss.abnormalFlightManagement.entity.FdFltAbnormalFeedBack;
import com.neusoft.prss.abnormalFlightManagement.entity.FdFltAbnorrmal;
/**
 * 不正常航班服务层
 * @author lwg
 * @date 2017/12/19
 */
@Service
@Transactional(readOnly = true)
public class AbnormalFlightManagementService {
	
	@Autowired
	private AbnormalFlightManagementDao abnormalFlightManagementDao;
	/**
	 * 获取发送部门
	 * @return
	 */
	public List<Map<String, Object>> getSenDepart() {
		return abnormalFlightManagementDao.getSenDepart();
	}
	/**
	 * 增加不正常航班，获取该航班信息
	 * @param flightNumber
	 * @param flightDate
	 * @return
	 */
	public List<Map<String, Object>> getAbnormalFlightInfo(String flightNumber, String flightDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flightNumber", flightNumber);
		map.put("flightDate", flightDate);
		//List<Map<String, Object>> list = abnormalFlightManagementDao.getAbnormalFlightInfo(map);
		
		//单进港当前航班查询
		List<Map<String, Object>> list = abnormalFlightManagementDao.getAbnormalFlightInfoFromPrssA0(map);
		
		//单出港当前航班查询
		if(list.size() == 0){
			list = abnormalFlightManagementDao.getAbnormalFlightInfoFromPrssD0(map);
		}
		
		//进、出港当前航班查询
		if(list.size() == 0){
			list = abnormalFlightManagementDao.getAbnormalFlightInfoFromPrss(map);
		}
		
		//单进港历史航班查询
		if(list.size() == 0){
			list = abnormalFlightManagementDao.getAbnormalFlightInfoFromPrsspA0(map);
		}
		
		//单出港历史航班查询
		if(list.size() == 0){
			list = abnormalFlightManagementDao.getAbnormalFlightInfoFromPrsspD0(map);
		}
		
		//进、出港历史航班查询
		if(list.size() == 0){
			list = abnormalFlightManagementDao.getAbnormalFlightInfoFromPrssp(map);
		}
		return list;
	}
	/**
	 * 发送不正常航班数据
	 * @param abnormalFlightVo
	 * @return
	 */
	public String addAbnormalFlightData(AbnormalFlightVo abnormalFlightVo) {
		String operDate = null;
		if("".equals(abnormalFlightVo.getOperDate()) || null == abnormalFlightVo.getOperDate()) {
			 operDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm");
		} else {
			operDate = abnormalFlightVo.getOperDate();
		}
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		FdFltAbnorrmal fdFltAbnorrmal = new FdFltAbnorrmal();
		fdFltAbnorrmal.setFltid(abnormalFlightVo.getFltid());
		fdFltAbnorrmal.setFlightDate(abnormalFlightVo.getFlightDate());
		fdFltAbnorrmal.setFlightNumber(abnormalFlightVo.getFlightNumber());
		fdFltAbnorrmal.setInfoSource(abnormalFlightVo.getInfoSource());
		fdFltAbnorrmal.setRemark(abnormalFlightVo.getRemark());
		fdFltAbnorrmal.setOperatror(abnormalFlightVo.getOperatror());
		fdFltAbnorrmal.setAircraftNumber(abnormalFlightVo.getAircraftNumber());
		try {
			fdFltAbnorrmal.setOperDate(sdf.parse(operDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		fdFltAbnorrmal.setId("");
		// 不正常航班插入数据

		int i = abnormalFlightManagementDao.addAbnormalFlightAbnormal(fdFltAbnorrmal);
		
		if(i > 0) {
			FdFltAbnormalFeedBack fdFltAbnormalFeedBack;
//			Integer id = 0;
//			String backId = abnormalFlightManagementDao.getBackMaxId();
//			if(null != backId && !"".equals(backId)) {
//				id = Integer.valueOf(backId);
//			}
			List<FdFltAbnormalFeedBack> list = new ArrayList<FdFltAbnormalFeedBack>();
			String[] offices = abnormalFlightVo.getOfficeId().split(",");
			for(int index = 0; index < offices.length; index++) {
				fdFltAbnormalFeedBack = new FdFltAbnormalFeedBack();
				fdFltAbnormalFeedBack.setAbnormalId(fdFltAbnorrmal.getId());
				fdFltAbnormalFeedBack.setOfficeId(offices[index]);
//				fdFltAbnormalFeedBack.setId((id+index+1)+"");
				list.add(fdFltAbnormalFeedBack);
			}
			
			return abnormalFlightManagementDao.addAbnormalFlightAbnormalFeedackBatch(list) > 0?"success":"faile";
		} else {			
			return "faile";
		}
	}
	/**
	 * 获取表格数据
	 * @param searchData 
	 * @param officeId 
	 * @param pageSize 
	 * @param pageNumber 
	 * @return
	 */
	public Map<String, Object> getGridData(String searchData, int begin, int end, String officeId) {

		Map<String, Object> map = new HashMap<String, Object>();
		if(!"".equals(searchData)) {

			String[] str = searchData.split("&amp;");
			String[] dataStr;
			StringBuilder strList;
			String[] back;
			for (int i = 0; i < str.length; i++) {
				dataStr = str[i].split("=");
				if(dataStr.length > 1) {
					if("feebBack".equals(dataStr[0])) { // 反馈情况
						 back = dataStr[1].split("-");
						if("cdm".equals(back[0])) {
							map.put("cdm", back[1]);
						} else if("dept".equals(back[0])) {
							map.put("dept", "%" + back[1] + "%");						
						}
					} else { // 处理多条件选项
						if(map.containsKey(dataStr[0])) {
							strList = new StringBuilder();
							strList.append(map.get(dataStr[0]).toString());
							strList.append(","+dataStr[1]);					
							map.put(dataStr[0], strList);
						} else {
							map.put(dataStr[0], dataStr[1]);
						}
					}
				}
			}
			StringBuilder sql = new StringBuilder();
			if(null != map.get("airGroup")) {
				sql.append(" AND a.UNION_CODE IN ( ");	
				String[] airGroup = map.get("airGroup").toString().split(",");
				for (int i = 0; i < airGroup.length; i++) {
					String airStr = airGroup[i];
					sql.append(airStr);
					if(i != airGroup.length - 1) {
						sql.append(",");						
					}
				}
				sql.append(" ) ");
			}
			if(null != map.get("airFlightCompany")) {
				sql.append(" AND B.ALN_2CODE IN ( ");				
				String[] airFlight = map.get("airFlightCompany").toString().split(",");
				for (int i = 0; i < airFlight.length; i++) {
					String airStr = airFlight[i];
					sql.append("'"+airStr+"'");
					if(i != airFlight.length - 1) {
						sql.append(",");						
					}
				}
				sql.append(" ) ");
				
			}
			if(sql.length() != 0) {
				map.put("sql", sql);
			}
		}		
		map.put("officeId", officeId);
		map.put("pageNumber", begin);
		map.put("pageSize", end);
		map.put("totalSize", "0");

		Map<String, Object>  result = new HashMap<>();
		result.put("total",abnormalFlightManagementDao.getGridData(map).size());

		map.put("totalSize", "1");
		result.put("rows",abnormalFlightManagementDao.getGridData(map));
		
		return result;
	}
	/**
	 * 获取当前查看不正常航班的信息
	 * @param searchId
	 * @return
	 */
	public List<Map<String, Object>> showAbnormalFlight(String searchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchId", searchId);
		List<Map<String, Object>> list = abnormalFlightManagementDao.getAbnormalFlightById(map);
		if(list.size() != 0) {
			List<Map<String,Object>> info = getAbnormalFlightInfo(list.get(0).get("FLIGHT_NUMBER")+"", list.get(0).get("FLIGHT_DATE")+"");
			if(info.size() != 0) {				
				list.get(0).putAll(info.get(0));
			}
			return list;			
		}
		return null;
	}
	/**
	 * 获取该不正常航班所涉及的部门
	 * @param searchId
	 * @param userOfficeId 
	 * @return
	 */
	public List<Map<String, Object>> getDepartFeedBackInfo(String searchId, String userOfficeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchId", searchId);
		map.put("userOfficeId", userOfficeId);
		return abnormalFlightManagementDao.getDepartFeedBackInfo(map);
	}
	
	/**
	 * 删除不正常航班
	 * @param officeId
	 * @return
	 */
	public String delAbnormalFlight(String searchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchId", searchId);
		int i = abnormalFlightManagementDao.delAbnormalFlight(map);
		if(i > 0) {
			return abnormalFlightManagementDao.delAbnormalFeedBack(map) > 0? "success":"faile";
		}
		return "falie";
	}
	/**
	 * 不正常航班涉及部门信息反馈
	 * @param feedBackId
	 * @param feedBackOper
	 * @param feedBackDate
	 * @param feedBackContent
	 * @return
	 */
	public String feedBackAirRep(String feedBackId, String feedBackOper, String feedBackDate, String feedBackContent) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("feedBackId", feedBackId);
			map.put("feedBackOper", feedBackOper);
			map.put("feedBackContent", feedBackContent);
			if("".equals(feedBackDate) || null == feedBackDate) {
				feedBackDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm");
			} 
			map.put("feedBackDate", feedBackDate);
			return abnormalFlightManagementDao.feedBackAirRep(map) > 0?"success":"faile";

	}
	/**
	 * CDM判责信息录入
	 * @param feedBackId
	 * @param cdmId
	 * @param cdmDate
	 * @param cmdContent
	 * @return
	 */
	public String aCDMContractorResponsible(String feedBackId, String cdmId, String cdmDate, String cmdContent) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("feedBackId", feedBackId);
		map.put("cdmId", cdmId);
		map.put("cmdContent", cmdContent);
		if("".equals(cmdContent) || null == cmdContent) {
			cdmDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm");
		} 
		map.put("cdmDate", cdmDate);
		return abnormalFlightManagementDao.aCDMContractorResponsible(map) > 0?"success":"faile";	
		
	}
	/**
	 * 获取航空公司下拉选
	 * @return
	 */
	public List<Map<String, Object>> getAirFlightList() {
		return abnormalFlightManagementDao.getAirFlightList();
	}
	/**
	 * 打印word获取部门名称
	 * @return
	 */
	public String getOfficeNames(String officeId) {
		List<String> list = new ArrayList<String>();
		String[] arr = officeId.split(",");
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		List<String> officeNames = abnormalFlightManagementDao.getOfficeNames(list);
		String names = "";
		for (int i = 0; i < officeNames.size(); i++) {
			if("".equals(names)){
				names = officeNames.get(i);
			}else{
				names = names + "," + officeNames.get(i);
			}
		};
		return names;
	}
}
