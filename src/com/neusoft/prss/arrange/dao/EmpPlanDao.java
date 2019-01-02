
package com.neusoft.prss.arrange.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.arrange.entity.EmpPlanMain;
import com.neusoft.prss.arrange.entity.PlanVO;
import com.neusoft.prss.arrange.entity.ShiftsVO;

@MyBatisDao
public interface EmpPlanDao extends BaseDao {

    List<PlanVO> getEmpPlanList(Map<String,String> params);

    void deletePlan(Map<String,String> params);

    List<EmpPlanMain> getPlanGridList(Map<String,String> params);

    JSONArray getGroupInfo(Map<String,String> param);

    JSONArray getEmpInfoById(Map<String,String> param);

    List<ShiftsVO> getShiftsList(Map<String,String> params);

    void savePlanInfo(@Param("planDetails") List<EmpPlanMain> planDetails,@Param("officeId") String officeId);

    void deleteEmpPlan(String[] ids);

    JSONArray getPlanInfoById(String[] ids);

    void modifyPlanInfo(Map<String,String> params);

    JSONArray showOrderPlan(Map<String,String> param);

    void saveOrderInfo(Map<String,String> params);

    void saveStopInfo(Map<String,String> params);

    String checkTask(Map<String,String> params);

    void saveLog(Map<String,String> params);

    String checkStopState(Map<String,String> params);

    void deleteStopInfo(Map<String,String> params);

    int getLogListCount(Map<String,Object> param);

    List<Map<String,String>> getLogList(Map<String,Object> param);

    int getUnfixedListCount(Map<String,Object> param);

    List<Map<String,String>> getUnfixedList(Map<String,Object> param);

    JSONArray getTeamList(Map<String,String> param);

    JSONArray getUnfixedDim(Map<String,String> param);

    void saveUnfiexPlan(Map<String,String> param);

    void deleteUnfixedPlan(String id);

    JSONArray getWorkerHaveList(Map<String,String> param);

    JSONArray getShiftsHaveList(Map<String,String> param);

    void modifyUnfiexPlan(Map<String,String> param);

    JSONArray getAllEmpInfo(Map<String,String> param);

    int ifHavePlanInfo(Map<String,String> params);

    JSONArray getExportPlan(Map<String,Object> params);

	Integer getPlanCount(@Param("stime")String stime, @Param("etime")String etime, @Param("officeId")String officeId);

}
