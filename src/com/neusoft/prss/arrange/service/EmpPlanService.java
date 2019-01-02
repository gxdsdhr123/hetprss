
package com.neusoft.prss.arrange.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.arrange.dao.EmpPlanDao;
import com.neusoft.prss.arrange.entity.EmpPlanMain;
import com.neusoft.prss.arrange.entity.PlanVO;
import com.neusoft.prss.arrange.entity.ShiftsVO;

@Service
@Transactional(readOnly = true)
public class EmpPlanService extends BaseService {
    @Autowired
    private EmpPlanDao empplanDao;

    public List<PlanVO> getEmpPlanList(Map<String,String> params) {
        return empplanDao.getEmpPlanList(params);
    }

    public void deletePlan(Map<String,String> params) {
        empplanDao.deletePlan(params);
    }

    public List<EmpPlanMain> getPlanGridList(Map<String,String> params) {
        return empplanDao.getPlanGridList(params);
        
    }

    public JSONArray getGroupInfo(Map<String,String> param) {
        return empplanDao.getGroupInfo(param);
    }

    public JSONArray getEmpInfoById(Map<String,String> param) {
        return empplanDao.getEmpInfoById(param);
    }

    public List<ShiftsVO> getShiftsList(Map<String,String> params) {
        String id1 = params.get("id1");
        String id2 = params.get("id2");
        String id3 = params.get("id3");
        String type = params.get("type");
        List<String> list = new ArrayList<String>();
        String id = "";
        if ("1".equals(type)) {
            list = new ArrayList<String>();
            if (!"".equals(id2)) {
                list.add(id2);
            }
            if (!"".equals(id3)) {
                list.add(id3);
            }
        } else if ("2".equals(type)) {
            list = new ArrayList<String>();
            if (!"".equals(id1)) {
                list.add(id1);
            }
            if (!"".equals(id3)) {
                list.add(id3);
            }
        } else if ("3".equals(type)) {
            list = new ArrayList<String>();
            if (!"".equals(id2)) {
                list.add(id2);
            }
            if (!"".equals(id1)) {
                list.add(id1);
            }
        }
        id = StringUtils.join(list.toArray(), "','");
        String str = "";
        if (!"".equals(id)) {
            str = "AND shifts_id NOT IN('" + id + "')";
        }
        params.put("str", str);
        return empplanDao.getShiftsList(params);
    }

    public String savePlanInfo(List<EmpPlanMain> list,String officeId) {
        try {
            empplanDao.savePlanInfo(list, officeId);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public void deleteEmpPlan(String[] ids) {
        empplanDao.deleteEmpPlan(ids);
    }

    public JSONArray getPlanInfoById(String ids) {
    	String[] idss = ids.split(",");
        return empplanDao.getPlanInfoById(idss);
    }

    public String modifyPlanInfo(EmpPlanMain vo,String id) {
        try {
        	Map params = new HashMap();
	 /*     Map<String,String> params = new HashMap<String,String>();*/
            params.put("busyInterval", vo.getBusyInterval());
            params.put("idleInterval", vo.getIdleInterval());
            params.put("stime1Label", vo.getStime1Label());
            params.put("etime1Label", vo.getEtime1Label());
            params.put("stime2Label", vo.getStime2Label());
            params.put("etime2Label", vo.getEtime2Label());
            params.put("stime3Label", vo.getStime3Label());
            params.put("etime3Label", vo.getEtime3Label());
            params.put("stime1", vo.getStime1());
            params.put("etime1", vo.getEtime1());
            params.put("stime2", vo.getStime2());
            params.put("etime2", vo.getEtime2());
            params.put("stime3", vo.getStime3());
            params.put("etime3", vo.getEtime3());
            params.put("shiftsId", vo.getShiftsId());
            //params.put("id", id);
            String[] ids  = id.split(",");
            params.put("id", ids);
            empplanDao.modifyPlanInfo(params);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public JSONArray showOrderPlan(Map<String,String> param) {
        return empplanDao.showOrderPlan(param);
    }

    public String saveOrderInfo(List<EmpPlanMain> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                EmpPlanMain vo = list.get(i);
                Map<String,String> params = new HashMap<String,String>();
                params.put("id", vo.getId());
                params.put("sortnum", vo.getSortnum());
                empplanDao.saveOrderInfo(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public String saveStopInfo(Map<String,String> params) {
        try {
            empplanDao.deleteStopInfo(params);
            empplanDao.saveStopInfo(params);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public String checkTask(Map<String,String> params) {
        return empplanDao.checkTask(params);
    }

    public void saveLog(Map<String,String> params) {
        empplanDao.saveLog(params);
    }

    public String checkStopState(Map<String,String> params) {
        return empplanDao.checkStopState(params);
    }

    public void deleteStopInfo(Map<String,String> params) {
        empplanDao.deleteStopInfo(params);
    }

    public Map<String,Object> getLogList(Map<String,Object> param) {
        Map<String,Object> result = new HashMap<String,Object>();
        int total = empplanDao.getLogListCount(param);
        List<Map<String,String>> rows = empplanDao.getLogList(param);
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }

    public Map<String,Object> getUnfixedList(Map<String,Object> param) {
        Map<String,Object> result = new HashMap<String,Object>();
        int total = empplanDao.getUnfixedListCount(param);
        List<Map<String,String>> rows = empplanDao.getUnfixedList(param);
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }

    public JSONArray getTeamList(Map<String,String> param) {
        return empplanDao.getTeamList(param);
    }

    public JSONArray getUnfixedDim(Map<String,String> param) {
        return empplanDao.getUnfixedDim(param);
    }

    public String saveUnfiexPlan(Map<String,String> param) {
        try {
            empplanDao.saveUnfiexPlan(param);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public void deleteUnfixedPlan(String id) {
        empplanDao.deleteUnfixedPlan(id);
    }

    public JSONArray getWorkerHaveList(Map<String,String> param) {
        return empplanDao.getWorkerHaveList(param);
    }

    public JSONArray getShiftsHaveList(Map<String,String> param) {
        return empplanDao.getShiftsHaveList(param);
    }

    public String modifyUnfiexPlan(Map<String,String> param) {
        try {
            empplanDao.modifyUnfiexPlan(param);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public JSONArray getAllEmpInfo(Map<String,String> param) {
        return empplanDao.getAllEmpInfo(param);
    }

    public String ifHavePlanInfo(List<EmpPlanMain> list,String officeId,String id,String type) {
        String result = "ok";
        for (int i = 0; i < list.size(); i++) {
            EmpPlanMain vo = list.get(i);
            Map<String,String> params = new HashMap<String,String>();
            params.put("officeId", officeId);
            params.put("workerId", vo.getWorkerId());
            params.put("pdate", vo.getPdate());
            /*params.put("stime1", vo.getStime1());
            params.put("etime1", vo.getEtime1());
            params.put("stime2", vo.getStime2());
            params.put("etime2", vo.getEtime2());
            params.put("stime3", vo.getStime3());
            params.put("etime3", vo.getEtime3());*/
            int cnt = empplanDao.ifHavePlanInfo(params);
            if (cnt > 0) {
                result = vo.getLoginName();
                break;
            }
        }
        return result;
    }

    public JSONArray getExportPlan(String month,List<String> titleList) {
        Map<String,Object> params = new HashMap<String,Object>();
        List<String> dateList = new ArrayList<String>();
        for(int i =2; i < titleList.size(); i++){
        	String title = titleList.get(i);
        	dateList.add(title.split(":")[0]);
        }
        params.put("dateList", dateList);
        params.put("officeId", UserUtils.getUser().getOffice().getId());
        return empplanDao.getExportPlan(params);
    }

	public String getIfExistPlan(String stime, String etime, String officeId) {
		Integer count = empplanDao.getPlanCount(stime,etime,officeId);
		if(count == 0){
			return "success";
		}else{
			return stime + "到" + etime + "已存在"+count+"条计划，请确认是否覆盖？";
		}
	}
}
