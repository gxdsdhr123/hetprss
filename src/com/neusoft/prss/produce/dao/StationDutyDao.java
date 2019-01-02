package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.produce.entity.BillZpqwbg;
import com.neusoft.prss.produce.entity.BillZpqwbgGoods;
import com.neusoft.prss.produce.entity.TimeInfo;

/**
 * 窄体装卸调度——站坪部装卸勤务单
 * @author xuhw
 *
 */
@MyBatisDao
public interface StationDutyDao {

	Integer getDataCount(@Param("offset")Integer offset, @Param("limit")Integer limit, @Param("searchText")String searchText, @Param("nwType")String nwType);
	
	List<BillZpqwbg> getData(@Param("offset")Integer offset, @Param("limit")Integer limit, @Param("searchText")String searchText, @Param("nwType")String nwType);
	
	BillZpqwbg getBillById(@Param("id")Integer id);
	
	List<BillZpqwbgGoods> getBillZpqwbgGoodsByBillId(@Param("id")Integer id);

	FltInfo getFlightInfo(@Param("inout")String inout, @Param("flightNumber")String flightNumber, @Param("flightDate")String flightDate);

	TimeInfo getTimeInfo(@Param("inout")String inout, @Param("flightNumber")String flightNumber, @Param("flightDate")String flightDate);

	List<Map<String, String>> getOperatorList(@Param("officeId")String officeId);

	int saveBill(BillZpqwbg billZpqwbg);
	
	int updateBill(BillZpqwbg billZpqwbg);
	
	BillZpqwbg getBillFlightInfo(@Param("inout")String inout, @Param("flightNumber")String flightNumber, @Param("flightDate")String flightDate);
	
	int saveBillZpqwbgGoods(BillZpqwbgGoods billZpqwbgGoods);
	
	int updateBillZpqwbgGood(BillZpqwbgGoods billZpqwbgGoods);

	int delBill(@Param("id")Integer id);
	
	int delBillZpqwbgGood(@Param("id")Integer id);
	
	List<String> getMembersByLeaderId(@Param("id")String id);
}
