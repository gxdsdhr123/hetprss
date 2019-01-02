package com.neusoft.prss.produce.enterPortLuggage.service;

import java.util.List;
import java.util.Map;

public interface EnterPortLuggageService {
	Map<String,Object> listBillList(Map<String,Object> param);
	int saveBillGoos(List<Map<String,Object>> list,Map<String,Object> bgh);
	int updateBillGoos(List<Map<String,Object>> list,Map<String,Object> bgh);
	List<Map<String,Object>> selChaLi();
	Map<String,Object> findInfo(Map<String,Object> parem);
	Map<String,Object> findMainI(String mainId);
	List<Map<String,Object>> findB(String id);
	int delDoods(String id);
	int delDoodsMain(String id);
	String findMainAndB(String id);
	int delgoodsB(String id);
	List<Map<String, Object>> getReceiverListDou();
	String countGulf(Map<String,Object> parem);
}
