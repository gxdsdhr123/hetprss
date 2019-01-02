package com.neusoft.prss.produce.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.proxy.jdbc.ClobProxy;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.produce.dao.MonitorReportDao;
import com.neusoft.prss.produce.entity.MonitorReportEntity;

/**
 * 航班监控报告
 * @author xuhw
 *
 */
@Service
public class MonitorReportService {

	@Autowired
	private MonitorReportDao monitorReportDao;
	@Autowired
	private  CacheService cacheService;
	
	private static Map<String,String> ACTTYPE = null;
	private static Map<String,String> AIRLINE = null;
	
	public PageBean<MonitorReportEntity> getData(Integer offset, Integer limit,
			String searchText, String startDate, String endDate) {
		PageBean<MonitorReportEntity> pageBean = new PageBean<MonitorReportEntity>();
		// 总数
		pageBean.setTotal(monitorReportDao.getDataCount(searchText,startDate,endDate));
		// 数据
		pageBean.setRows(monitorReportDao.getData(offset,limit,searchText,startDate,endDate));
		return pageBean;
	}

	public MonitorReportEntity getDataById(Integer id) {
		// 查询主表信息
		MonitorReportEntity entity = monitorReportDao.getDataById(id);
		
		return entity;
	}

	public void updateData(MonitorReportEntity data) {
		monitorReportDao.updateData(data);
	}
	
	public void delData(Integer id) {
		monitorReportDao.delData(id);
	}

	public ResponseVO<Integer> createData(String statDay, String flightNumber, String jobTypes) {
		if(StringUtils.isEmpty(statDay)){
			return ResponseVO.<Integer>error().setMsg("请输入航班日期");
		}
		if(StringUtils.isEmpty(flightNumber)){
			return ResponseVO.<Integer>error().setMsg("请输入航班号");
		}
		if(ACTTYPE == null){
			ACTTYPE = cacheService.getMap("dim_aircraft_type_map");
		}
		if(AIRLINE == null){
			AIRLINE = cacheService.getMap("dim_airline_map");
		}
		// 基础信息
		MonitorReportEntity entity = monitorReportDao.getDataFromView(flightNumber, statDay);
		if(entity == null){
			return ResponseVO.<Integer>error().setMsg("未查询到航班信息");
		}
		if(ACTTYPE!=null){
			// 航空公司
			entity.setAln2code(AIRLINE.get(entity.getAln2code()));
		}
		if(ACTTYPE!=null){
			// 机型
			entity.setInActtypeCode(ACTTYPE.get(entity.getInActtypeCode()));
			entity.setOutActtypeCode(ACTTYPE.get(entity.getOutActtypeCode()));
		}
		// 取fltid数组
		List<String> fltids = new ArrayList<String>();
		if(!StringUtils.isEmpty(entity.getInFltid())){
			fltids.add(entity.getInFltid());
		}
		if(!StringUtils.isEmpty(entity.getOutFltid())){
			fltids.add(entity.getOutFltid());
		}
		// 作业类型
		String[] jobTypeArr = jobTypes.split(",");
		// 流程记录
		List<String> procRecords = monitorReportDao.getProcRecords(fltids,statDay,Arrays.asList(jobTypeArr));
		String procRecord = "";
		if(procRecords!=null){
			for(String str:procRecords){
				procRecord += str + "\n";
			}
		}
		entity.setProcRecord(procRecord);
		// 事件记录
		List<String> eventRecords = monitorReportDao.getEventRecords(fltids, statDay);
		String eventRecord = "";
		if(eventRecords!=null){
			for(String str:eventRecords){
				eventRecord += str + "\n";
			}
		}
		entity.setEventRecord(eventRecord);
		// 操作人信息
		entity.setOperator(UserUtils.getUser().getId());
		entity.setOperatorName(UserUtils.getUser().getName());
		
		// 删除原来的记录
		monitorReportDao.deleteReport(statDay, flightNumber);
		// 入库
		int c = monitorReportDao.saveData(entity);
		if(c <= 0){
			return ResponseVO.<Integer>error().setMsg("生成报表失败");
		}
		return ResponseVO.<Integer>build().setData(entity.getId());
	}
	
	public Map<String, List<Map<String, Object>>> getOfficeRestype(){
		Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String,Object>>>();
		// 查询部门对应作业类型
		List<Map<String, Object>> dataList = monitorReportDao.getOfficeRestype();
		for(Map<String, Object> data : dataList){
			// 取数据
			String key = (String)data.get("NAME");
			String text = (String)data.get("TYPENAME");
			ClobProxy restype =(ClobProxy)data.get("RESTYPE");
			String value = "";
			try {
				value = new BufferedReader(restype.getCharacterStream()).readLine();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} 
			// 找到对应组的list
			List<Map<String, Object>> resultList = resultMap.get(key);
			if(resultList == null){
				resultList = new ArrayList<Map<String,Object>>();
				resultMap.put(key, resultList);
			}
			// 
			Map<String, Object> restypeObj = new HashMap<String, Object>();
			restypeObj.put("text", text);
			restypeObj.put("value", value);
			resultList.add(restypeObj);
		}
		return resultMap;
	}

	public ResponseVO<Integer> getReport(String statDay, String flightNumber) {
		List<Integer> ids = monitorReportDao.getReport(statDay,flightNumber);
		if(ids!=null && ids.size()>0){
			return ResponseVO.<Integer>error();
		}
		return ResponseVO.<Integer>build();
	}
}
