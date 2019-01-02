package com.neusoft.prss.produce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neusoft.prss.produce.dao.LargePieceOfPlumDao;
import com.neusoft.prss.produce.entity.LargePieceOfPlum;
/**
 * 大件行李， 行李下拉
 * @author lwg
 * @date 2017/12/23
 */
@Service
@Transactional(readOnly = true)
public class LargePieceOfPlumService {
	
	@Autowired
	private LargePieceOfPlumDao largePieceOfPlumDao;
	/**
	 * 查理列表
	 * @return
	 */
	public List<Map<String, Object>> getCharlieList() {
		return largePieceOfPlumDao.getCharlieList();
	}
	
	/**
	 * 获取航班数据  
	 * @param flightDate
	 * @param flightNumber
	 * @return
	 */
	public List<Map<String, Object>> getAirFligthInfo(String flightDate, String flightNumber) {
		Map<String, Object> map = new HashMap<>();
		map.put("flightDate", flightDate);
		map.put("flightNumber", flightNumber);
		return largePieceOfPlumDao.getAirFligthInfo(map);
	}

	/**
	 * 保存数据
	 * @param largePieceOfPlum
	 * @param param 
	 * @return
	 */
	public String save(LargePieceOfPlum largePieceOfPlum, String param) {
		largePieceOfPlum.setId("");
		if("bs".equals(param)) { 
			int s = largePieceOfPlumDao.save(largePieceOfPlum);
			if(s > 0) {
				return dealBSGoods(largePieceOfPlum, param);
			}
		} else if("cs".equals(param)){
			int s = largePieceOfPlumDao.saveCS(largePieceOfPlum);
			if(s > 0) {
				return dealBSGoods(largePieceOfPlum, param);
			}
		}
		return "faile";
	}

	private String dealBSGoods(LargePieceOfPlum largePieceOfPlum, String param) {
		String billId = largePieceOfPlum.getId();
		Map<String, Object> map = new HashMap<String, Object>();
		String bagNumber = largePieceOfPlum.getBagNumber();
		String[] bagNumbers = bagNumber.split(",");
		String dest = largePieceOfPlum.getDest();
		String[] dests = dest.split(",");
		List<Map<String, Object>> list = new ArrayList<>();
		// 获取最大id
//		String maxId = "";
//		if("bs".equals(param)) {
//			maxId = largePieceOfPlumDao.maxIdBS();
//		} else if("cs".equals(param)) {
//			maxId = largePieceOfPlumDao.maxIdCS();			
//		}
//		if(null == maxId) {
//			maxId = "0";
//		}
		for (int i = 0; i < bagNumbers.length; i++) {
			if(null == bagNumbers[i] || "".equals(bagNumbers[i])) {
				continue;
			}
			map = new HashMap<String, Object>();
//			map.put("id", Integer.valueOf(maxId)+i+1);
			map.put("dest", dests[i]);
			map.put("bagNumber", bagNumbers[i]);
			map.put("billId", billId);
			list.add(map);
		}
		if("bs".equals(param)) {			
			if(list.size() != 0) {
				return largePieceOfPlumDao.saveGoodsBatch(list) > 0? "success":"faile";				
			}
			return "success";
		} else if("cs".equals(param)) {
			if(list.size() != 0) {				
				return largePieceOfPlumDao.saveGoodsCSBatch(list) > 0? "success":"faile";
			}
			return "success";
		}
		return "faile";
	}

	/**
	 * 获取表格数据
	 * @param begin
	 * @param end
	 * @param param 
	 * @param searchData 
	 * @return
	 */
	public Map<String, Object> getdata(int begin, int end, String param, String searchData) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		map.put("totalSize", "0");
		if(null != searchData) {			
			map.put("searchData", "%" + searchData + "%");
		}
		if("bs".equals(param)) {
			result.put("total", largePieceOfPlumDao.getBSData(map).size());			
		} else if("cs".equals(param)) {
			result.put("total", largePieceOfPlumDao.getCSData(map).size());						
		}
		map.put("begin", begin);
		map.put("end", end);
		map.put("totalSize", "1");
		if("bs".equals(param)) {
			list = largePieceOfPlumDao.getBSData(map);
		} else if("cs".equals(param)) {
			list = largePieceOfPlumDao.getCSData(map);			
		}
		result.put("rows", list);
		return result;
	}

	/**
	 * 获取数据
	 * @param searchId
	 * @param sign 
	 * @return
	 */
	public List<Map<String, Object>> getFormData(String searchId, String sign) {
		Map<String, Object> map = new HashMap<>();
		map.put("searchId", searchId);
		map.put("totalSize", "0");
		List<Map<String, Object>> list;
		if("bs".equals(sign)) { // 大件行李
			list = largePieceOfPlumDao.getBSData(map);
			if(list.size() != 0) {
				List<Map<String, Object>> info = largePieceOfPlumDao.getBSGoods(map);
				map = new HashMap<>();
				map.put("goodsList", info);
				if(info.size() != 0) {				
					list.get(0).putAll(map);
				}
				return list;
			}
		} else if("cs".equals(sign)) {
			list = largePieceOfPlumDao.getCSData(map);
			if(list.size() != 0) {
				List<Map<String, Object>> info = largePieceOfPlumDao.getCSGoods(map);
				map = new HashMap<>();
				map.put("goodsList", info);
				if(info.size() != 0) {				
					list.get(0).putAll(map);
				}
				return list;
			}
		}
		return null;
	}

	/**
	 * 修改数据
	 * @param largePieceOfPlum
	 * @param param
	 * @return
	 */
	public String modify(LargePieceOfPlum largePieceOfPlum, String param) {
		Map<String, Object> map = new HashMap<String, Object>();
		if("bs".equals(param)) { // 大件行李
			int s = largePieceOfPlumDao.updateBSData(largePieceOfPlum);
			if(s > 0) {
				map.put("searchId", largePieceOfPlum.getId());
				List<Map<String,Object>> bsGoods = largePieceOfPlumDao.getBSGoods(map);
				if(bsGoods.size() > 0) {
					int j = largePieceOfPlumDao.delBSGoods(map);
					if(j > 0) {					
						return dealBSGoods(largePieceOfPlum, param);
					}
				} else {
					return dealBSGoods(largePieceOfPlum, param);
					
				}
			}
		} else if("cs".equals(param)) {
			int s = largePieceOfPlumDao.updateCSData(largePieceOfPlum);
			System.out.println(s);
			if(s > 0) {
				map.put("searchId", largePieceOfPlum.getId());
				List<Map<String,Object>> csGoods = largePieceOfPlumDao.getCSGoods(map);
				if(csGoods.size() > 0) {
					int j = largePieceOfPlumDao.delCSGoods(map);
					if(j > 0) {					
						return dealBSGoods(largePieceOfPlum, param);
					}
				} else {
					return dealBSGoods(largePieceOfPlum, param);
					
				}
			}
		}
		return null;
	}

	/**
	 * 删除
	 * @param searchId
	 * @param param
	 * @return
	 */
	public String delete(String searchId, String param) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchId", searchId);
		if("bs".equals(param)) { // 大件行李			
			int i = largePieceOfPlumDao.deleteBS(map);
			if(i > 0) {
				List<Map<String,Object>> bsGoods = largePieceOfPlumDao.getBSGoods(map);
				if(bsGoods.size() > 0) {
					return largePieceOfPlumDao.delBSGoods(map) > 0? "success" : "faile";
				} else {
					return "success";
					
				}
			}
		} else if("cs".equals(param)) {
			int i = largePieceOfPlumDao.deleteCS(map);
			if(i > 0) {
				List<Map<String,Object>> bsGoods = largePieceOfPlumDao.getCSGoods(map);
				if(bsGoods.size() > 0) {
					return largePieceOfPlumDao.delCSGoods(map) > 0? "success" : "faile";
				} else {
					return "success";
					
				}
			}
		}
		return null;
	}

	/**
	 * 当前航班是否已存在记录中
	 * @param flightDate
	 * @param flightNumber
	 * @param sign
	 * @return
	 */
	public List<Map<String, Object>> isExists(String flightDate, String flightNumber, String sign) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flightDate", flightDate);
		map.put("flightNumber", flightNumber);
		map.put("totalSize", 0);
		if("bs".equals(sign)) {
			return largePieceOfPlumDao.getBSData(map);			
		} else if("cs".equals(sign)) {
			return largePieceOfPlumDao.getCSData(map);					
		}
		return null;
	}



}
