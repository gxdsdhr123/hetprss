package com.neusoft.prss.imax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.prss.imax.bean.IllegalBean;
import com.neusoft.prss.imax.bean.ImaxIndexBean;
import com.neusoft.prss.imax.bean.ImaxResourceBean;
import com.neusoft.prss.imax.bean.ImaxRun1Bean;
import com.neusoft.prss.imax.bean.ImaxRun2Bean;
import com.neusoft.prss.imax.bean.MonitorBean;
import com.neusoft.prss.imax.dao.IMAXDao;

@Service("imaxService")
public class IMAXService {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ImaxHisService imaxHisService;
	
	@Resource(name="imaxDao")
	private IMAXDao imaxDao;
	
	public ImaxIndexBean getIndexData() throws Exception {
		
		ImaxIndexBean bean = new ImaxIndexBean();
		// 当前时间、安全运行时间
		try {
			bean.setNowTime(DateUtils.getDate("yyyy-MM-dd HH:mm"));
			bean.setSafeTime(imaxDao.getSafeRunTime_index());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// 值班领导
		try {
			bean.setLeader(imaxDao.getLeader_index());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// 车辆占用情况
		try {
			Map<String, Map<String, Integer>> carNums = new HashMap<String, Map<String, Integer>>();
			List<Map<String, Object>> dataList = imaxDao.getCarNums_index();
			for(Map<String, Object> data : dataList){
				String reskind = (String)data.get("TYPE_NAME");
				
				Map<String, Integer> nums = new HashMap<String, Integer>();
				nums.put("use", ((BigDecimal)data.get("ZY_NUM")).intValue());
				nums.put("unuse", ((BigDecimal)data.get("KX_NUM")).intValue());
				
				switch (reskind) {
					case "牵引车":
						carNums.put("car1", nums);
						break;
					case "摆渡车":
						carNums.put("car2", nums);
						break;
					case "行李车":
						carNums.put("car3", nums);
						break;
				}
			}
			
			// 防止有空项
			if(!carNums.keySet().contains("car1")){
				Map<String, Integer> nums = new HashMap<String, Integer>();
				nums.put("use", 0);
				nums.put("unuse", 0);
				carNums.put("car1", nums);
			}
			if(!carNums.keySet().contains("car2")){
				Map<String, Integer> nums = new HashMap<String, Integer>();
				nums.put("use", 0);
				nums.put("unuse", 0);
				carNums.put("car2", nums);
			}
			if(!carNums.keySet().contains("car3")){
				Map<String, Integer> nums = new HashMap<String, Integer>();
				nums.put("use", 0);
				nums.put("unuse", 0);
				carNums.put("car3", nums);
			}
			
			bean.setCarNums(carNums);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员占用
		try {
			Map<String, Map<String, Integer>> personNums = new HashMap<String, Map<String,Integer>>();
			// 查询各部门人员占用数据
			List<Map<String, Object>> dataList =  imaxDao.getPersonNums_index();
			for(Map<String,Object> data : dataList){
				// 规范数据格式
				Map<String, Integer> num = new HashMap<String, Integer>();
				num.put("zg", (Integer)data.get("zg"));
				num.put("kx", (Integer)data.get("kx"));
				// 格式化占比
				BigDecimal oc = (BigDecimal)data.get("oc");
				oc = oc.multiply(new BigDecimal(100));
				num.put("oc", oc.intValue());
				personNums.put((String)data.get("id"), num);
			}
			bean.setPersonNums(personNums);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 航班运行情况（图）
		try {
			Map<String, Integer[]> flightNums = new HashMap<String, Integer[]>();
			// 查询航班运行情况
			List<Map<String, Object>> dataList = imaxDao.getFlightNumsList_index();
			
			Integer[] cgList = new Integer[24];	// 出港
			Integer[] jgList = new Integer[24];	// 进港
			
			for(Map<String, Object> data : dataList){
				String hour = (String)data.get("hour");
				int i = Integer.parseInt(hour);
				
				cgList[i] = (Integer)data.get("cg");
				jgList[i] = (Integer)data.get("jg");
			}
			
			flightNums.put("cg", cgList);
			flightNums.put("jg", jgList);
			
			bean.setFlightNums(flightNums);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 航班运行情况
		try {
			Map<String, Object> flightMap = new HashMap<String, Object>();
			// 进出港及备降航班数
			flightMap.putAll(imaxDao.getFlightNums_index());
			// 延误
			flightMap.putAll(imaxDao.getFlightYw_index());
			// 高峰
			flightMap.putAll(imaxDao.getFlightGf_index());
			
			bean.setFlightMap(flightMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 航班占比
		try {
			bean.setFlightRate(imaxDao.getFlightRate_index());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 航班保障进度
		try {
			bean.setMonitorNums(imaxDao.getMonitorNums_index());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 部门违规
		try {
			bean.setDepartmentIllegal(imaxDao.getDepartmentIllegal_index());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员违规
		try {
			bean.setPersonIllegal(imaxDao.getPersonIllegal_index());;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return bean;
	}

	public ImaxRun1Bean getRun1Data() {
		ImaxRun1Bean bean = new ImaxRun1Bean();
		// 航班运行情况图
		try {
			Map<String, Map<String, Integer[]>> flightChart = new HashMap<String, Map<String,Integer[]>>();
			Integer[] chart1Arr_zc = new Integer[24];	// 东航-正常
			Integer[] chart1Arr_yw = new Integer[24];	// 东航-延误
			Integer[] chart2Arr_zc = new Integer[24];	// 南航-正常
			Integer[] chart2Arr_yw = new Integer[24];	// 南航-延误
			Integer[] chart3Arr_zc = new Integer[24];	// 海航-正常
			Integer[] chart3Arr_yw = new Integer[24];	// 海航-延误
			Integer[] chart4Arr_zc = new Integer[24];	// 外航-正常
			Integer[] chart4Arr_yw = new Integer[24];	// 外航-延误
			
			List<Map<String, Object>> dataList = imaxDao.flightChart_run1();
			for(Map<String, Object> data : dataList){
				// 1-东航 2-南航 3-海航 4-外航  
				String unionCode = (String)data.get("UNION_CODE");
				
				String hour = (String)data.get("FLIGHT_HOUR");
				int i = Integer.parseInt(hour);
				
				switch(unionCode){
					case "1":
						chart1Arr_zc[i] = ((BigDecimal)data.get("ZC_NUM")).intValue();
						chart1Arr_yw[i] = ((BigDecimal)data.get("YW_NUM")).intValue();
						break;
					case "2":
						chart2Arr_zc[i] = ((BigDecimal)data.get("ZC_NUM")).intValue();
						chart2Arr_yw[i] = ((BigDecimal)data.get("YW_NUM")).intValue();
						break;
					case "3":
						chart3Arr_zc[i] = ((BigDecimal)data.get("ZC_NUM")).intValue();
						chart3Arr_yw[i] = ((BigDecimal)data.get("YW_NUM")).intValue();
						break;
					case "4":
						chart4Arr_zc[i] = ((BigDecimal)data.get("ZC_NUM")).intValue();
						chart4Arr_yw[i] = ((BigDecimal)data.get("YW_NUM")).intValue();
						break;
				}
				
			}
			
			// 组合数据
			Map<String, Integer[]> chart1Map = new HashMap<String, Integer[]>();
			Map<String, Integer[]> chart2Map = new HashMap<String, Integer[]>();
			Map<String, Integer[]> chart3Map = new HashMap<String, Integer[]>();
			Map<String, Integer[]> chart4Map = new HashMap<String, Integer[]>();
			
			chart1Map.put("zc", chart1Arr_zc);
			chart1Map.put("yw", chart1Arr_yw);
			chart2Map.put("zc", chart2Arr_zc);
			chart2Map.put("yw", chart2Arr_yw);
			chart3Map.put("zc", chart3Arr_zc);
			chart3Map.put("yw", chart3Arr_yw);
			chart4Map.put("zc", chart4Arr_zc);
			chart4Map.put("yw", chart4Arr_yw);
			
			flightChart.put("chart1", chart1Map);
			flightChart.put("chart2", chart2Map);
			flightChart.put("chart3", chart3Map);
			flightChart.put("chart4", chart4Map);
			
			bean.setFlightChart(flightChart);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// 航班运行情况文字
		try {
			Map<String, Map<String, Object>> flightText = new HashMap<String, Map<String,Object>>();
			
			List<Map<String, Object>> dataList = imaxDao.flightText_run1();
			for(Map<String, Object> data : dataList){
				// 1-东航 2-南航 3-海航 4-外航  
				String unionCode = (String)data.get("UNION_CODE");
				flightText.put("flight" + unionCode, data);
			}
			bean.setFlightText(flightText);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// 表格
		try {
			bean.setFlightTable(imaxDao.flightTable_run1());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return bean;
	}

	public ImaxRun2Bean getRun2Data() {
		ImaxRun2Bean bean = new ImaxRun2Bean();
		// 航班运行情况占比左侧文字
		try {
			bean.setRunText(imaxDao.runText_run2());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 航班运行情况占比图
		try {
			bean.setRunChart(imaxDao.runChart_run2());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 航空公司占比
		try {
			bean.setRunTable(imaxDao.runTable_run2());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		// 航班延误图形
		String[] titleArr = new String[]{"航班晚到","流量控制","机械故障","旅客原因","航空公司","联检原因","机场设施","天气原因","BGS原因","其它原因"};
		Map<String, Object> ywChartMap = new HashMap<String, Object>();
		for(String title : titleArr){
			ywChartMap.put(title, 0);
		}
		List<Map<String, Object>> resList = new ArrayList<Map<String,Object>>();
		try {
			resList = imaxDao.ywChart_run2();
			for(Map<String, Object> res : resList){
				String key = (String)res.get("REASON");
				Integer value = ((BigDecimal)res.get("NUM")).intValue();
				ywChartMap.put(key, value);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		bean.setYwChart(ywChartMap);
		
		// 航班延误文字
		try {
			bean.setYwText(imaxDao.ywText_run2());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bean;
	}

	public List<String> getResource1List() {
		return imaxDao.getDepList_resource1();
	}
	
	public ImaxResourceBean getResource1Data(String depId) {
		ImaxResourceBean bean = new ImaxResourceBean();
		// 人员保障图
		try {
			Map<String, Integer[]> monitorChart = new HashMap<String, Integer[]>();
			Integer[] yj = new Integer[24];
			Integer[] sj = new Integer[24];
			Arrays.fill(yj, 0);
			Arrays.fill(sj, 0);
			List<Map<String, Object>> dataList = imaxDao.getMonitorChart_resource1(depId);
			for(Map<String, Object> data : dataList){
				String hour = (String)data.get("FLIGHT_HOUR");
				int i = Integer.parseInt(hour);
				
				sj[i] = data.get("A_NUM")!=null?((BigDecimal)data.get("A_NUM")).intValue():0;
				yj[i] = data.get("E_NUM")!=null?((BigDecimal)data.get("E_NUM")).intValue():0;
			}
			
			monitorChart.put("sj", sj);
			monitorChart.put("yj", yj);
			
			bean.setMonitorChart(monitorChart);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员保障文字
		try {
			Map<String, Object> monitorText = imaxDao.getMonitorText_resource1(depId);
			bean.setMonitorText(monitorText);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员占用图
		try {
			Map<String, Object> occupyChart = imaxDao.occupyChart_resource1(depId);
			bean.setOccupyChart(occupyChart);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员占用列表
		try {
			List<Map<String, Object>> occupyTable = imaxDao.occupyTable_resource1(depId);
			bean.setOccupyTable(occupyTable);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bean;
	}

	public List<String> getResource2List() {
		return imaxDao.getDepList_resource2();
	}

	public ImaxResourceBean getResource2Data(String depId) {
		ImaxResourceBean bean = new ImaxResourceBean();
		// 人员保障图
		try {
			Map<String, Integer[]> monitorChart = new HashMap<String, Integer[]>();
			Integer[] yj = new Integer[24];
			Integer[] sj = new Integer[24];
			Arrays.fill(yj, 0);
			Arrays.fill(sj, 0);
			
			List<Map<String, Object>> dataList = imaxDao.getMonitorChart_resource2(depId);
			for(Map<String, Object> data : dataList){
				String hour = (String)data.get("FLIGHT_HOUR");
				int i = Integer.parseInt(hour);
				
				sj[i] = ((BigDecimal)data.get("CAR_NUM")).intValue();
				yj[i] = sj[i] + new Random().nextInt(5);
			}
			
			monitorChart.put("sj", sj);
			monitorChart.put("yj", yj);
			
			bean.setMonitorChart(monitorChart);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员保障文字
		try {
			Map<String, Object> monitorText = imaxDao.getMonitorText_resource2(depId);
			bean.setMonitorText(monitorText);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员占用图
		try {
			Map<String, Object> occupyChart = imaxDao.occupyChart_resource2(depId);
			bean.setOccupyChart(occupyChart);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员占用列表
		try {
			List<Map<String, Object>> occupyTable = imaxDao.occupyTable_resource2(depId);
			bean.setOccupyTable(occupyTable);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bean;
	}

	
	public IllegalBean getIllegalData(String date, String officeId,
			String targetDate) {
		IllegalBean bean = new IllegalBean();
		// 人员违规折线图
		try {
			bean.setLineChart(imaxHisService.getLineChart_illegal(date, officeId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员违规折线图下方文字
		try {
			bean.setLineText(imaxHisService.getLineText_illegal(date, officeId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 违规柱状图
		try {
			bean.setBarChart(imaxHisService.getBarChart_illegal(date, officeId, targetDate));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 人员违规列表
		try {
			bean.setPersonList(imaxHisService.getPersonList_illegal(date, officeId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bean;
	}

	public List<Map<String, Object>> getIllegalDept() {
		return imaxDao.getDept_illegal();
	}

	public MonitorBean getMonitorData(String inOut) {
		MonitorBean bean = new MonitorBean();
		// 条状图
		try {
			List<Map<String, Object>> resList = imaxDao.getBarList_monitor();
			List<String> titleList = Arrays.asList(new String[]{"60座以下","61-150座","151-250座","251-500座","500座以上"});
			List<Integer> barList = Arrays.asList(new Integer[]{0,0,0,0,0});
			if(resList!=null){
				for(Map<String,Object> o : resList){
					String title = (String)o.get("PLANE_SEAT");
					int index= titleList.indexOf(title);
					if(index >= 0){
						barList.set(index, ((BigDecimal)o.get("BZ_TIME")).intValue());
					}
				}
			}
			bean.setBarList(barList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 表格
		try {
			bean.setTableList(imaxDao.getTableList_monitor());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 标准块
		try {
			List<Map<String, Object>> resList= imaxDao.getBzObj_monitor(inOut);
			Map<String, Double> bzObj = new HashMap<String, Double>();
			if(resList!=null){
				for(Map<String, Object> o : resList){
					String key = (String)o.get("JOB_TYPE");
					Double value = ((BigDecimal)o.get("D_TIME")).doubleValue();
					bzObj.put(key, value);
				}
			}
			bean.setBzObj(bzObj);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bean;
	}

	

}
