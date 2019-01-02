package com.neusoft.prss.produce.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.entity.BillPickupEntity;
import com.neusoft.prss.produce.entity.BillPickupGoodsEntity;

public interface BillPickupService {

	List<BillPickupEntity> getPickupAllList(String startDate, String endDate,
			String ids);
	
	int getPickupListCount(String startDate, String endDate, String searchText);
	
	List<BillPickupEntity> getPickupListData(String startDate, String endDate,
			String searchText, Integer pageNumber, Integer pageSize);

	BillPickupEntity getPickupForm(String id);

	List<BillPickupGoodsEntity> getPickupGoods(String id);

	void savePickup(JSONObject pickupJSON, JSONArray goodsJSON);

	String getPickupId();

	void delPickup(String ids);

	JSONArray getUserList();

	

	

}
