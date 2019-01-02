package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.produce.entity.LargePieceOfPlum;
/**
 * 大件行李， 行李下拉
 * @author lwg
 * @date 2017/12/23
 */
@MyBatisDao
public interface LargePieceOfPlumDao {
	// ==============公共部分=====================
	/**
	 * 获取查理
	 * @return
	 */
	public List<Map<String, Object>> getCharlieList();
	/**
	 * 获取航班数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAirFligthInfo(Map<String, Object> map);
	
	// =============大件行李部分=================
	/**
	 * 保存主表数据
	 * @param largePieceOfPlum
	 * @return
	 */
	public int save(LargePieceOfPlum largePieceOfPlum);
	
	/**
	 * 获取最大id值
	 * @param map 
	 * @return
	 */
//	public String maxIdBS();
	/**
	 * 批量插入大件行李数据
	 * @param list
	 * @return
	 */
	public int saveGoodsBatch(List<Map<String, Object>> list);
	/**
	 * 获取大件行李表格数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBSData(Map<String, Object> map);
	/**
	 * 获取大件行李行李数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBSGoods(Map<String, Object> map);
	/**
	 * 修改大件行李
	 * @param largePieceOfPlum
	 * @return
	 */
	public int updateBSData(LargePieceOfPlum largePieceOfPlum);
	/**
	 * 删除大件行李列表
	 * @param map
	 */
	public int delBSGoods(Map<String, Object> map);
	/**
	 * 删除大件行李
	 * @param map
	 * @return
	 */
	public int deleteBS(Map<String, Object> map);
	
	// =====================行李单下拉部分========================
	
	public List<Map<String, Object>> getCSData(Map<String, Object> map);
	public int saveCS(LargePieceOfPlum largePieceOfPlum);
//	public String maxIdCS();
	public int saveGoodsCSBatch(List<Map<String, Object>> list);
	public List<Map<String, Object>> getCSGoods(Map<String, Object> map);
	public int updateCSData(LargePieceOfPlum largePieceOfPlum);
	public int delCSGoods(Map<String, Object> map);
	public int deleteCS(Map<String, Object> map);
	


}
