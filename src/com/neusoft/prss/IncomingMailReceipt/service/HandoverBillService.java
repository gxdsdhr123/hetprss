package com.neusoft.prss.IncomingMailReceipt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.neusoft.prss.IncomingMailReceipt.dao.HandoverBillDao;
import com.neusoft.prss.IncomingMailReceipt.dao.HandoverOutBillDao;
import com.neusoft.prss.IncomingMailReceipt.entity.HandoverBill;

/**
 * 邮货交接单服务层
 * @author lwg
 * @date 2018/01/03
 */
@Service
@Transactional(readOnly = true)
public class HandoverBillService {
	// 进港
	@Autowired
	private HandoverBillDao handoverBillDao;
	// 出港
	@Autowired
	private HandoverOutBillDao handoverOutBillDao;

	/**
	 * 获取查理
	 * @return
	 */
	public List<Map<String, Object>> getCharlieList() {
		return handoverBillDao.getCharlieList();
	}

	/**
	 * 获取货运司机列表
	 * @return
	 */
	public List<Map<String, Object>> getReceiverList() {
		return handoverBillDao.getReceiverList();
	}

	/**
	 * 获取航班信息
	 * @param flightDate
	 * @param flightNumber
	 * @param sign
	 * @return
	 */
	public List<Map<String, Object>> getAirFligthInfo(String flightDate, String flightNumber, String sign) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flightDate", flightDate);
		map.put("flightNumber", flightNumber);
		if ("in".equals(sign)) {
			return handoverBillDao.getAirFligthInfo(map);
		} else if ("out".equals(sign)) {
			return handoverOutBillDao.getAirFligthInfo(map);
		}
		return null;
	}

	/**
	 * 保存数据
	 * 
	 * @param handoverBill
	 * @param data
	 * @return
	 */
	public String save(HandoverBill handoverBill, String data) {
		int i = 0;
		if ("in".equals(handoverBill.getSign())) {
			i = handoverBillDao.saveFlight(handoverBill);
		} else if ("out".equals(handoverBill.getSign())) {
			i = handoverOutBillDao.saveFlight(handoverBill);
		}
		if (i > 0) {
			return dealGoodsToList(data, handoverBill);
		} else {
			return "faile";
		}
	}

	/**
	 * 处理Goods列表
	 * 
	 * @param data
	 * @param id
	 * @return
	 */
	private String dealGoodsToList(String data, HandoverBill handoverBill) {
		String id = handoverBill.getId();
		String sign = handoverBill.getSign();
		List<Map<String, String>> dataList = JSON.parseObject(data, new TypeReference<List<Map<String, String>>>() {
		});
		// 辅表数据
		List<Map<String, Object>> goodsList = new ArrayList<>();
		// 小表数据
		List<Map<String, Object>> attaList = new ArrayList<>();

		Map<String, Object> goodsMap = null;
		Map<String, Object> attaMap = new HashMap<>();

		if (dataList.size() == 0) {
			return "success";
		}
		for (int in = 0; in < dataList.size(); in++) {
			Map<String, String> map = dataList.get(in);
			// 没有代码值 与内容 默认当前数据不存在
//			if (null == map.get("COLL_CODE") || "".equals(String.valueOf(map.get("COLL_CODE")).trim())) {
			// 内容不空即可录入
			if (null == map.get("INC_GOODS") || "".equals(map.get("INC_GOODS")+"")) {
				continue;
			}
			goodsMap = new HashMap<>();
			// 接收状态
			goodsMap.put("receiveStatus", 2);
			// 集斗器代码
			goodsMap.put("collCode", map.get("COLL_CODE"));
			// 内含物品
			goodsMap.put("incGoods", map.get("INC_GOODS"));
			// 操作人
			goodsMap.put("operator", handoverBill.getOperator());
			// 操作人姓名
			// listMap.put("operatorName", handoverBill.getOperatorName());
			// 操作时间
			goodsMap.put("operateDate", handoverBill.getCreateDate());
			// 交接单
			goodsMap.put("billId", id);
			// 备注
			goodsMap.put("remark", map.get("REMARK"));
			// 接收人
			goodsMap.put("receiver", "");
			// 接收时间
			goodsMap.put("receiveTime", "");

			// 重量
			goodsMap.put("weight", map.get("WEIGHT"));

			if (null != map.get("RECEIVER")) {
				attaMap = new HashMap<>();

				goodsMap.put("receiveStatus", 1);
				// 接收人
				attaMap.put("receiver", map.get("RECEIVER"));
				goodsMap.put("receiver", map.get("RECEIVER"));
				// 接收时间
				attaMap.put("receiveTime", map.get("RECEIVERDATE"));
				goodsMap.put("receiveTime", map.get("RECEIVERDATE"));
				attaMap.put("billId", id);
				attaMap.put("signatory", map.get("SIGNATORY"));
				attaMap.put("signatoryDate", map.get("SIGNATORYDATE"));
			}
			if (null != map.get("SIGNATORY")) {
				// 交接时间
				attaMap.put("signatoryDate", map.get("SIGNATORYDATE"));
			}
			if (!attaMap.isEmpty()) {
				attaList.add(attaMap);
			}
			goodsList.add(goodsMap);
		}

		if (goodsList.size() != 0) {
			goodsMap = new HashMap<>();
			goodsMap.put("goodsList", goodsList);
			int i = 0;
			if ("in".equals(sign)) {
				i = handoverBillDao.saveGoods(goodsMap);
			} else if ("out".equals(sign)) {
				i = handoverOutBillDao.saveGoods(goodsMap);
			}
			if (i > 0) {
				if (attaList.size() != 0) {
					// 数据去重
					List<Map<String, Object>> list = dealAttaData(attaList);
					attaMap = new HashMap<>();
					attaMap.put("attaList", list);
					int j = 0;
					if ("in".equals(sign)) {
						j = handoverBillDao.saveAtta(attaMap);
					} else if ("out".equals(sign)) {
						j = handoverOutBillDao.saveAtta(attaMap);
					}
					if (j > 0) {
						return "success";
					} else {
						return "faile";
					}
				} else {
					return "success";
				}
			} else {
				return "faile";
			}

		} else {
			return "success";
		}
	}

	/**
	 * 签署表数据去重
	 * 
	 * @param attaList
	 * @return
	 */
	private List<Map<String, Object>> dealAttaData(List<Map<String, Object>> attaList) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> resultMap = null;
		List<String> keys = new ArrayList<>();
		for (Map<String, Object> map : attaList) {
			resultMap = new HashMap<>();
			if (keys.contains(map.get("receiver").toString())) {
				continue;
			}
			keys.add(map.get("receiver").toString());
			for (String key : map.keySet()) {
				resultMap.put(key, map.get(key));
			}
			resultList.add(resultMap);
		}
		return resultList;
	}

	/**
	 * 获取进港货邮主表数据
	 * 
	 * @param begin
	 * @param end
	 * @param pageSign
	 * @param searchId
	 */
	public Map<String, Object> getInData(int begin, int end, String pageSign, String searchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		if ("main".equals(pageSign)) { // 进港 主表数据
			map.put("totalSize", "0");
			result.put("total", handoverBillDao.getInData(map).size());
			map.put("begin", begin);
			map.put("end", end);
			map.put("totalSize", "1");
			list = handoverBillDao.getInData(map);
			result.put("rows", list);
			return result;
		} else if ("mainAdd".equals(pageSign)) { // 进港 详情数据
			map.put("searchId", searchId);
			result.put("detailList", handoverBillDao.getInDetailData(map));
			return result;
		}
		return null;

	}

	public Map<String, Object> getData(int begin, int end, String pageSign, String searchId, String param,
			String searchText) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		if ("main".equals(pageSign)) { // 进/出港 主表数据
			map.put("totalSize", "0");
			if (null != searchText && !"".equals(searchText)) {
				map.put("searchData", "%" + searchText + "%");
			}
			if ("in".equals(param)) {
				result.put("total", handoverBillDao.getInData(map).size());
			} else if ("out".equals(param)) {
				result.put("total", handoverOutBillDao.getOutData(map).size());
			}
			map.put("begin", begin);
			map.put("end", end);
			map.put("totalSize", "1");
			if ("in".equals(param)) {
				list = handoverBillDao.getInData(map);
			} else if ("out".equals(param)) {
				list = handoverOutBillDao.getOutData(map);
			}

			result.put("rows", list);
			return result;
		} else if ("mainAdd".equals(pageSign)) { // 进/出港 详情数据
			map.put("searchId", searchId);
			List<Map<String, Object>> detailData = null;
			if ("in".equals(param)) {
				detailData = handoverBillDao.getInDetailData(map);
			} else if ("out".equals(param)) {
				detailData = handoverOutBillDao.getOutDetailData(map);
			}
			result.put("detailList", detailData);
			return result;
		}
		return null;
	}

	/**
	 * 获取进港货邮单据数据
	 * 
	 * @param searchId
	 * @param sign
	 * @return
	 */
	public List<Map<String, Object>> getInFormData(String searchId, String sign) {

		Map<String, Object> map = new HashMap<>();
		map.put("searchId", searchId);
		map.put("totalSize", "0");
		List<Map<String, Object>> list = null;
		if ("in".equals(sign)) { // 进港
			list = handoverBillDao.getInData(map);
		} else if ("out".equals(sign)) { // 出港
			list = handoverOutBillDao.getOutData(map);
		}
		return list;

	}

	/**
	 * 修改数据
	 * 
	 * @param handoverBill
	 * @param data
	 * @return
	 */
	public String modify(HandoverBill handoverBill, String data) {
		// handoverBillDao.modify();
		String id = handoverBill.getId();
		Map<String, Object> map = new HashMap<>();
		map.put("searchId", id);
		map.put("operator", handoverBill.getOperator());
		map.put("operatorName", handoverBill.getOperatorName());
		map.put("createDate", handoverBill.getCreateDate());

		Boolean upMain = false;
		Boolean attaFlag = false;
		Boolean goodsFlag = true;
		Boolean dealFlag = false;
		// 修改主表数据
		if ("in".equals(handoverBill.getSign())) {
			upMain = handoverBillDao.updateMainData(map) > 0;
		} else if ("out".equals(handoverBill.getSign())) {
			upMain = handoverOutBillDao.updateMainData(map) > 0;
		}
		if (upMain) {
			// 删除数据
			dealFlag = delDetail(map, attaFlag, goodsFlag, dealFlag, handoverBill.getSign());
		}
		// 更新数据
		if (dealFlag) {
			return dealGoodsToList(data, handoverBill);
		} else {
			return "faile";
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param map
	 * @param attaFlag
	 * @param goodsFlag
	 * @param dealFlag
	 * @return
	 */
	private Boolean delDetail(Map<String, Object> map, Boolean attaFlag, Boolean goodsFlag, Boolean dealFlag,
			String sign) {
		int i;
		boolean signFlag = true;
		if ("in".equals(sign)) {
			signFlag = true;
		} else if ("out".equals(sign)) {
			signFlag = false;
		}

		List<Map<String, Object>> inDetailData = new ArrayList<>();
		if ("in".equals(sign)) {
			inDetailData = handoverBillDao.getInDetailData(map);
		} else if ("out".equals(sign)) {
			inDetailData = handoverOutBillDao.getOutDetailData(map);
		}
		if (inDetailData.size() != 0) {
			for (Map<String, Object> map2 : inDetailData) {
				if (null != map2.get("RECEIVER")) {
					attaFlag = true;
					break;
				}
			}
			// 删除签署表
			if (attaFlag) {
				if (signFlag) {
					i = handoverBillDao.delAttaData(map);
				} else {
					i = handoverOutBillDao.delAttaData(map);
				}
				if (i <= 0) {
					goodsFlag = false;
				}
			}
			// 删除单据
			if (goodsFlag) {
				if (signFlag) {
					i = handoverBillDao.delGoodsData(map);
				} else {
					i = handoverOutBillDao.delGoodsData(map);
				}
				if (i <= 0) {
					dealFlag = false;
				} else {
					dealFlag = true;
				}
			}
		} else {
			dealFlag = true;
		}
		return dealFlag;
	}

	/**
	 * 删除
	 * 
	 * @param searchId
	 * @param sign
	 * @return
	 */
	public String del(String searchId, String sign) {
		Boolean attaFlag = false;
		Boolean goodsFlag = true;
		Boolean dealFlag = true;
		Map<String, Object> map = new HashMap<>();
		map.put("searchId", searchId);

		// 删除数据
		dealFlag = delDetail(map, attaFlag, goodsFlag, dealFlag, sign);
		if (dealFlag) {
			int i = 0;
			if ("in".equals(sign)) {
				i = handoverBillDao.delMain(map);
			} else if ("out".equals(sign)) {
				i = handoverOutBillDao.delMain(map);
			}
			if (i > 0) {
				return "success";
			}
		}
		return "faile";
	}

	public List<Map<String, Object>> isExists(String flightDate, String flightNumber, String sign) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flightDate", flightDate);
		map.put("flightNumber", flightNumber);
		map.put("totalSize", 0);
		if("in".equals(sign)) {
			return handoverBillDao.getInData(map);			
		} else if("out".equals(sign)) {
			return handoverOutBillDao.getOutData(map);					
		}
		return null;
	}

}
