package com.neusoft.prss.passengerCarOperRecord.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.neusoft.prss.passengerCarOperRecord.dao.PassengerCarOperRecordDao;
import com.neusoft.prss.passengerCarOperRecord.entity.PassengerCarOperRecord;

/**
 * 客梯车操作记录
 * 
 * @author lwg
 * @date 2017/12/26
 */
@Service
@Transactional(readOnly = true)
public class PassengerCarOperRecordService {
	@Autowired
	private PassengerCarOperRecordDao passengerCarOperRecordDao;

	/**
	 * 获取增加页面表格头
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAddHeaderData() {
		return passengerCarOperRecordDao.getAddHeaderData();
	}

	/**
	 * 获取车辆编号
	 * 
	 * @param officeId
	 * @return
	 */
	public List<Map<String, Object>> getDeviceNumber(String officeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("officeId", officeId);
		List<Map<String, Object>> list = passengerCarOperRecordDao.getDeviceNumber(map);
		return list;
	}

	/**
	 * 得到操作人列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getOperList() {
		return passengerCarOperRecordDao.getOperList();
	}

	/**
	 * 保存勤务单数据
	 * 
	 * @param passengerCarOperRecord
	 * @param data
	 * @param officeId
	 */
	public String addData(PassengerCarOperRecord passengerCarOperRecord, String data, String officeId) {
		int i = passengerCarOperRecordDao.addDataHD(passengerCarOperRecord);
		if (i > 0) {
			String id = passengerCarOperRecord.getId();
			return dealGoodsToList(data, id, officeId);
		}
		return "faile";
	}

	/**
	 * 处理Goods列表
	 * 
	 * @param data
	 * @param id
	 * @param officeId
	 * @return
	 */
	private String dealGoodsToList(String data, String id, String officeId) {
		try {

			List<Map<String, String>> dataList = JSON.parseObject(data, new TypeReference<List<Map<String, String>>>() {
			});
			List<Map<String, Object>> list = new ArrayList<>();
			String[] status = null;
			String[] allStatus = null;

			if (dataList.size() == 0) {
				return "success";
			}

			// 处理选中数据
			for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
				Map<String, String> map = (Map<String, String>) iterator.next();
				if (null == map.get("deviceNo") || "".equals(map.get("deviceNo") + "".trim())) {
					continue;
				}
				status = map.get("checkStatus").toString().split(",");
				list = dealGoodsDataToMap(id, map.get("deviceNo"), list, status, "1");
				// 总数据
				List<Map<String, Object>> headerData = getAddHeaderData();
				allStatus = new String[headerData.size()];
				for (int i = 0; i < headerData.size(); i++) {
					allStatus[i] = (String) headerData.get(i).get("ITEM_CODE");
				}
				String[] minus = minus(status, allStatus);
				// 处理未选中数据
				list = dealGoodsDataToMap(id, map.get("deviceNo"), list, minus, "2");
				// 单独处理停车地，取车地
				// 加载停车地
				list = loadCar(list, "scd", map.get("scd"), map.get("deviceNo"), id);
				// 加载取车地
				list = loadCar(list, "qcd", map.get("qcd"), map.get("deviceNo"), id);

			}
			if(list.size() != 0) {
				return passengerCarOperRecordDao.saveGoodsBatch(list) > 0 ? "success" : "faile";				
			} else {
				return "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "faile";
		}

	}

	private List<Map<String, Object>> loadCar(List<Map<String, Object>> list, String car, String value, String deviceNo, String id) {
		Map<String, Object> listMap;
		Map<String, Object> dataMap;
		dataMap = new HashMap<>();
		listMap = new HashMap<String, Object>();
		listMap.put("deviceId", deviceNo);
		dataMap.put("name", car);
		dataMap.put("value", value);
		listMap.put("status", dataMap);
		// listMap.put("status", car);
		// listMap.put("statusValue", value);
		listMap.put("billId", id);
		list.add(listMap);
		return list;
	}

	// 处理数据
	private List<Map<String, Object>> dealGoodsDataToMap(String id, String deviceNo, List<Map<String, Object>> list,
			String[] status, String staValue) {
		Map<String, Object> listMap;
		Map<String, Object> dataMap;

		for (String s : status) {
			dataMap = new HashMap<>();
			listMap = new HashMap<String, Object>();
			listMap.put("deviceId", deviceNo);
			dataMap.put("name", s);
			dataMap.put("value", staValue);
			listMap.put("status", dataMap);
			// listMap.put("status", s);
			// listMap.put("statusValue", staValue);
			listMap.put("billId", id);
			list.add(listMap);
		}

		return list;
	}

	// 求两个数组的差集
	public static String[] minus(String[] arr1, String[] arr2) {
		LinkedList<String> list = new LinkedList<String>();
		LinkedList<String> history = new LinkedList<String>();
		String[] longerArr = arr1;
		String[] shorterArr = arr2;
		// 找出较长的数组来减较短的数组
		if (arr1.length > arr2.length) {
			longerArr = arr2;
			shorterArr = arr1;
		}
		for (String str : longerArr) {
			if (!list.contains(str)) {
				list.add(str);
			}
		}
		for (String str : shorterArr) {
			if (list.contains(str)) {
				history.add(str);
				list.remove(str);
			} else {
				if (!history.contains(str)) {
					list.add(str);
				}
			}
		}

		String[] result = {};
		return list.toArray(result);
	}

	/**
	 * 获取主表数据
	 * 
	 * @param begin
	 * @param end
	 * @param searchText
	 */
	public Map<String, Object> getMainData(int begin, int end, String searchText) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		map.put("totalSize", "0");
		if (null != searchText) {
			map.put("searchData", "%" + searchText + "%");
		}
		result.put("total", passengerCarOperRecordDao.getMainDataSize(map));
		map.put("begin", begin);
		map.put("end", end);
		list = passengerCarOperRecordDao.getMainData(map);
		result.put("rows", list);
		return result;
	}

	/**
	 * 获取修改数据
	 * 
	 * @param searchId
	 */
	public List<Map<String, Object>> getModifyData(String searchId) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String[] billId = searchId.split(",");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < billId.length; i++) {
			map.put("searchId", billId[i]);
			list.add(passengerCarOperRecordDao.getDetailData(map));
		}
		return list;
	}

	/**
	 * 获取表格数据
	 * 
	 * @param searchId
	 * @return
	 */
	public List<Map<String, String>> getTableData(String searchId) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		String[] billId = searchId.split(",");
		for (int i = 0; i < billId.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searchId", billId[i]);
			Map<String, String> carData = passengerCarOperRecordDao.getCarData(map);
			if(carData.get("DEVICE_NO") != null && !"".equals(carData.get("DEVICE_NO"))){
				map.put("fltId", carData.get("FLTID"));
				map.put("deviceNo", carData.get("DEVICE_NO"));
				Map<String, String> carTime = passengerCarOperRecordDao.getCarTime(map);
				if(carTime != null && carTime.size() != 0){
					carData.putAll(carTime);
				}
			}
			list.add(carData);
		}
		return list;
	}

	/**
	 * 修改数据
	 * 
	 * @param passengerCarOperRecord
	 * @param data
	 * @param officeId
	 * @return
	 */
	public String modify(PassengerCarOperRecord passengerCarOperRecord, String data, String officeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = passengerCarOperRecord.getId();
		map.put("operator", passengerCarOperRecord.getOperator());
		map.put("operatorName", passengerCarOperRecord.getOperatorName());
		map.put("scheduler", passengerCarOperRecord.getScheduler());
		map.put("post", passengerCarOperRecord.getPost());
		map.put("id", id);
		// 修改主表数据
		int i = passengerCarOperRecordDao.updateMainData(map);
		// 删除附表数据
		if (i > 0) {
			map = new HashMap<String, Object>();
			map.put("searchId", id);
			List<Map<String, String>> list = passengerCarOperRecordDao.getTableData(map);
			if (list.size() != 0) {
				int j = passengerCarOperRecordDao.delGoods(map);
				if (j > 0) {
					return dealGoodsToList(data, id, officeId);
				}
			} else {
				return dealGoodsToList(data, id, officeId);
			}
		}
		// 插入附表数据
		return "faile";
	}

	/**
	 * 删除数据
	 * 
	 * @param searchId
	 * @return
	 */
	public String del(String searchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchId", searchId);
		// 删除主表数据
		int i = passengerCarOperRecordDao.del(map);
		if (i > 0) {
			List<Map<String, String>> list = passengerCarOperRecordDao.getTableData(map);
			if (list.size() != 0) {
				return passengerCarOperRecordDao.delGoods(map) > 0 ? "success" : "faile";
			} else {
				return "success";
			}

		}
		return "faile";
	}

	/**
	 * 获取航班信息
	 * @param flightDate
	 * @param flightNumber
	 * @return
	 */
	public List<Map<String, Object>> getAirFligthInfo(String flightDate, String flightNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flightDate", flightDate);
		map.put("flightNumber", flightNumber);
		return passengerCarOperRecordDao.getAirFligthInfo(map);
	}
	
	/**
	 * 当前航班号是否已存在
	 * @param flightDate
	 * @param flightNumber
	 * @return
	 */
	public List<Map<String, Object>> isExists(String flightDate, String flightNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flightDate", flightDate);
		map.put("flightNumber", flightNumber);
		map.put("totalSize", 0);
		return passengerCarOperRecordDao.getMainData(map);
	}

	public List<String> getFilePath(Map<String, Object> map) {
		return passengerCarOperRecordDao.getFilePath(map);
	}

	public String updateData(String data) {
		String[] oneData =data.substring(0, data.length()-1).split("\\|");
    	for (int i = 0; i < oneData.length; i++) {
    		String[] billData = oneData[i].split(",");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("remarkId", 0);//mybatis用来存放remark表序列
			map.put("bill_id", 0);//mybatis用来存放remark表序列
			map.put("id", billData[0]);
			map.put("deviceNo", billData[1]);
			map.put("operator", billData[2]);
			map.put("remark", billData[3]);
			map.put("WG", billData[4]);
			map.put("HB", billData[5]);
			map.put("SJ", billData[6]);
			map.put("DG", billData[7]);
			map.put("LT", billData[8]);
			map.put("WS", billData[9]);
			map.put("ZD", billData[10]);
			map.put("ZJ", billData[11]);
			map.put("MHQ", billData[12]);
			map.put("QCD", billData[13]);
			map.put("SCD", billData[14]);
			map.put("flightNumber", billData[15]);
			map.put("flightDate", billData[16]);
			map.put("AIRCRAFTNUMBER", billData[17]);
			map.put("post", billData[18]);
			map.put("scheduler", billData[19]);
			map.put("ETA", billData[20]);
			map.put("ETD", billData[21]);
			map.put("ACTSTANDCODE", billData[22]);
			map.put("ACTTYPECODE", billData[23]);
			map.put("WG_ID", 0);//mybatis用来存放remark表序列
			map.put("HB_ID", 0);//mybatis用来存放remark表序列
			map.put("SJ_ID", 0);//mybatis用来存放remark表序列
			map.put("DG_ID", 0);//mybatis用来存放remark表序列
			map.put("LT_ID", 0);//mybatis用来存放remark表序列
			map.put("WS_ID", 0);//mybatis用来存放remark表序列
			map.put("ZD_ID", 0);//mybatis用来存放remark表序列
			map.put("ZJ_ID", 0);//mybatis用来存放remark表序列
			map.put("MHQ_ID", 0);//mybatis用来存放remark表序列
			if(map.get("id").toString().equals("undefined")){
				passengerCarOperRecordDao.insertOperator(map);
			}else{
				passengerCarOperRecordDao.updataOperator(map);
			}
			passengerCarOperRecordDao.updataRemark(map);
			passengerCarOperRecordDao.deleteGoods(map);
			passengerCarOperRecordDao.updataWG(map);
			passengerCarOperRecordDao.updataHB(map);
			passengerCarOperRecordDao.updataSJ(map);
			passengerCarOperRecordDao.updataDG(map);
			passengerCarOperRecordDao.updataLT(map);
			passengerCarOperRecordDao.updataWS(map);
			passengerCarOperRecordDao.updataZD(map);
			passengerCarOperRecordDao.updataZJ(map);
			passengerCarOperRecordDao.updataMHQ(map);
			passengerCarOperRecordDao.updataQCD(map);
			passengerCarOperRecordDao.updataSCD(map);
		}
		return "success";
	}

}
