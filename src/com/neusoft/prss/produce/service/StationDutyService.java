package com.neusoft.prss.produce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.common.util.BeanUtils;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.produce.dao.StationDutyDao;
import com.neusoft.prss.produce.entity.BillZpqwbg;
import com.neusoft.prss.produce.entity.BillZpqwbgGoods;
import com.neusoft.prss.produce.entity.TimeInfo;

/**
 * 窄体装卸调度——站坪部装卸勤务单
 * @author xuhw
 *
 */
@Service
public class StationDutyService {

	@Autowired
	private StationDutyDao stationDutyDao;
	
	private Map<String, String> carGoodsMap = new HashMap<String, String>();
	
	public StationDutyService(){
		carGoodsMap.put("外观", "wg");
		carGoodsMap.put("轮胎", "lt");
		carGoodsMap.put("灯光", "dg");
		carGoodsMap.put("制动", "zd");
		carGoodsMap.put("液压系统", "yyxt");
		carGoodsMap.put("转向", "zx");
		carGoodsMap.put("喇叭", "lb");
		carGoodsMap.put("仪表", "yb");
		carGoodsMap.put("燃油", "ry");
		carGoodsMap.put("冷却液", "lqy");
		carGoodsMap.put("警示灯", "jsd");
		carGoodsMap.put("灭火器", "mhq");
	}
	
	public PageBean<BillZpqwbg> getData(Integer offset, Integer limit,
			String searchText,String nwType) {
		PageBean<BillZpqwbg> pageBean = new PageBean<BillZpqwbg>();
		// 总数
		pageBean.setTotal(stationDutyDao.getDataCount(offset, limit, searchText,nwType));
		// 数据
		pageBean.setRows(stationDutyDao.getData(offset,limit,searchText,nwType));
		return pageBean;
	}

	public FltInfo getFlightInfo(String inout, String flightNumber,String flightDate) {
		return stationDutyDao.getFlightInfo(inout,flightNumber,flightDate);
	}

	public TimeInfo getTimeInfo(String inout, String flightNumber,
			String flightDate) {
		return stationDutyDao.getTimeInfo(inout,flightNumber,flightDate);
	}

	public ResponseVO<String> saveBill(BillZpqwbg billZpqwbg) throws Exception{
		
		Integer billId = billZpqwbg.getId();
		
		// 验证
		if(billId == null){
			if(StringUtils.isEmpty(billZpqwbg.getFlightDate())){
				return ResponseVO.<String>error().setMsg("请填写航班日期！");
			}
			if(StringUtils.isEmpty(billZpqwbg.getFlightNumber())){
				return ResponseVO.<String>error().setMsg("请填写航班号！");
			}
			if(StringUtils.isEmpty(billZpqwbg.getInout())){
				return ResponseVO.<String>error().setMsg("请选择进出港类型！");
			}
			if(StringUtils.isEmpty(billZpqwbg.getOperator())){
				return ResponseVO.<String>error().setMsg("请选择查理！");
			}
			
			// 查询航班信息
			BillZpqwbg fltInfo = stationDutyDao.getBillFlightInfo(billZpqwbg.getInout(),billZpqwbg.getFlightNumber(),billZpqwbg.getFlightDate());
			if(fltInfo.getFltid() == null){
				return ResponseVO.<String>error().setMsg("没有找到这个航班！");
			}
			
			// 回填航班信息
			BeanUtils.copyPropertiesIgnoreNull(fltInfo, billZpqwbg);
			
			// 查询各种时间信息
			TimeInfo timeInfo = stationDutyDao.getTimeInfo(billZpqwbg.getInout(),billZpqwbg.getFlightNumber(),billZpqwbg.getFlightDate());
			
			// 回填时间信息
			billZpqwbg.setLqdkTime(timeInfo.getDockingTime());
			billZpqwbg.setHcmkqTime(timeInfo.getDoorOpenTime());
			billZpqwbg.setZcwcTime(timeInfo.getFinishLodingTime());
			billZpqwbg.setXcwcTime(timeInfo.getFinishOffTime());
			billZpqwbg.setZhhcmgbTime(timeInfo.getLastCloseTime());
			billZpqwbg.setScmgbTime(timeInfo.getDoorClosedTime());
		}
		
		
		
		//保存数据
		if(billId == null){
			billZpqwbg.setCreateDate(DateUtils.getDateTime());
			
			stationDutyDao.saveBill(billZpqwbg);
			billId = billZpqwbg.getId();
		}else{
			stationDutyDao.updateBill(billZpqwbg);
		}
		String goodsStr = billZpqwbg.getBillZpqwbgGoodsStr();
		
		// -------------车辆信息
		saveGoods(billId, goodsStr);
		
		return ResponseVO.<String>build();
	}

	/**
	 * 保存车辆信息
	 * @param billId
	 * @param goodsStr
	 */
	private void saveGoods(Integer billId, String goodsStr) {
		if(StringUtils.isEmpty(goodsStr)){
			return;
		}
		goodsStr = StringEscapeUtils.unescapeHtml4(goodsStr);
		// -------------车辆信息
		
		// json格式传入{carId:[{jcxmId:'wg',jcxmName:'外观',jcxmVal:'0',id:1231},...],...}
		Map<String,List<Map<String,Object>>> jsonMap = JSONObject.parseObject(goodsStr , HashMap.class);
		for(Entry<String,List<Map<String,Object>>> json: jsonMap.entrySet()){
			for(Map<String,Object> map : json.getValue()){
				
				BillZpqwbgGoods good = new BillZpqwbgGoods();
				
				String id = (String)map.get("id");
				good.setId(StringUtils.isEmpty(id)?null:Integer.parseInt(id));
				good.setBillId(billId.toString());
				good.setJcxmId((String)map.get("jcxmId"));
				good.setJcxmName((String)map.get("jcxmName"));
				good.setJcxmVal((String)map.get("jcxmVal"));
				good.setDeviceNo(json.getKey());
				
				if(good.getId() == null){
					stationDutyDao.saveBillZpqwbgGoods(good);
				}else{
					stationDutyDao.updateBillZpqwbgGood(good);
				}
				
			}
		}
		
	}

	public List<Map<String, String>> getOperatorList(String officeId) {
		return stationDutyDao.getOperatorList(officeId);
	}

	public void delProduce(Integer id) {
		
		stationDutyDao.delBill(id);
		stationDutyDao.delBillZpqwbgGood(id);
		
	}

	public BillZpqwbg getBillById(Integer id) {
		// 查询主表信息
		BillZpqwbg bill = stationDutyDao.getBillById(id);
		// 判断进出港
		String inFltId = bill.getInFltid();
		String outFltId = bill.getOutFltid();
		String fltId = bill.getFltid().toString();
		if(fltId.equals(inFltId)){
			bill.setInout("A");
		}else if (fltId.equals(outFltId)){
			bill.setInout("D");
		}
		// 查询
		List<BillZpqwbgGoods> goods = stationDutyDao.getBillZpqwbgGoodsByBillId(id);
		// 组装成jsonMap  {carId:[{jcxmId:'wg',jcxmName:'外观',jcxmVal:'0',id:1231},...],...}
		Map<String,List<BillZpqwbgGoods>> jsonMap = new HashMap<String, List<BillZpqwbgGoods>>();
		for(BillZpqwbgGoods good : goods){
			List<BillZpqwbgGoods> jsonList = new ArrayList<BillZpqwbgGoods>();
			
			String key  = good.getDeviceNo();
			if(jsonMap.containsKey(key)){
				jsonList = jsonMap.get(key);
			}else{
				jsonMap.put(key, jsonList);
			}
			// 手持端没存jcxmId，这里手动传一下
			if(StringUtils.isEmpty(good.getJcxmId())){
				good.setJcxmId(carGoodsMap.get(good.getJcxmName()));
			}
			jsonList.add(good);
		}
		bill.setBillZpqwbgGoodsStr(JSON.toJSONString(jsonMap));
		return bill;
	}

	public String getMembers(String id) {
		List<String> members = stationDutyDao.getMembersByLeaderId(id);
		String membersStr = "";
		for(String member : members){
			membersStr += ("," + member );
		}
		return membersStr.substring(1);
	}

}
