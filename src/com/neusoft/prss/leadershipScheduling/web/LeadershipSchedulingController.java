package com.neusoft.prss.leadershipScheduling.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.leadershipScheduling.entity.AmLeaderPlan;
import com.neusoft.prss.leadershipScheduling.entity.ExportLPExcel;
import com.neusoft.prss.leadershipScheduling.service.LeadershipSchedulingService;
/**
 * 领导排班管理
 * @author lwg
 * @date 2017/12/08
 */
@Controller
@RequestMapping("${adminPath}/leaderPlan/leader")
public class LeadershipSchedulingController extends BaseController{
	 
	 @Autowired
	 private LeadershipSchedulingService leadershipSchedulingService;
	/**
	 * 跳转领导管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "prss/leadershipScheduling/LeadershipScheduling";
	}
	
    /**
     * 
     *Discription:得到展现表格的表头
     *@param request
     *@return:返回值意义
     */
    @RequestMapping(value = "getTableHeader")
    @ResponseBody
    public String getTableHeader(HttpServletRequest request) {
    	return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);
    }
    
    /**
     * 
     *Discription:得到表格数据
     *@param pageSize
     *@param pageNumber
     *@return:返回值意义
     */
    @RequestMapping(value = "getGridData/{param}")
    @ResponseBody
    public String getGridData(String searchTime, @PathVariable("param") String param, String officeId, String pdate, Model model) {
    	if("leader".equals(param)) { // 排班计划
    		model.addAttribute("searchTime", searchTime);
    		return JSON.toJSONString(leadershipSchedulingService.getGriData(searchTime), SerializerFeature.WriteMapNullValue);
    	} else if("leaderDetail".equals(param)) { // 该单位当天的详细计划
    		return JSON.toJSONString(leadershipSchedulingService.queryLeaderDetail(officeId, null, pdate), SerializerFeature.WriteMapNullValue);
    	} else if("TPlan".equals(param)) { // 所有部门当前日期三天计划
    		String date1 = DateUtils.getDate();
    		Calendar c = Calendar.getInstance(); 
    		try {
				Date date = new SimpleDateFormat("yy-MM-dd").parse(date1);
				c.setTime(date); 
				int day=c.get(Calendar.DATE); 
				c.set(Calendar.DATE,day-1); 
				String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
				
				c.set(Calendar.DATE,day + 1);
				String dayafter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
				
				return JSON.toJSONString(leadershipSchedulingService.getGriData(dayBefore, dayafter), SerializerFeature.WriteMapNullValue);
			} catch (ParseException e) {
				e.printStackTrace();
			} 

    	} 
		return "";
    }
    /*
     *跳转增加部门计划 
     */
    @RequestMapping(value = "addDepartment")
    public String addDepartment(Model model) {
    	model.addAttribute("officeList", getOffice("select"));
    	model.addAttribute("flag", "0");
		return "prss/leadershipScheduling/addDepartment";
    }

    /**
     * 获取部门下拉选
     * @param flag
     * @return
     */
	private List<Map<String, Object>> getOffice(String flag) {
    	return leadershipSchedulingService.getOfficeSelect(flag);
	}
    /*
     * 增加部门计划
     */
    @RequestMapping(value = "saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(String officeId, String seqNum, String flag) {
    	return leadershipSchedulingService.saveOrUpdate(officeId, seqNum, "1", flag);
    }
    /*
     * 跳转修改部门计划排序
     */
    @RequestMapping(value = "toModifyDepartment")
    public String modifyDepartment(String officeId, String seqNum, Model model) {
    	model.addAttribute("officeList", getOffice("all"));
    	model.addAttribute("flag", "1");
    	model.addAttribute("officeId", officeId);
    	model.addAttribute("seqNum", seqNum);
		return "prss/leadershipScheduling/addDepartment";
    }
    
    /**
     * 删除部门计划
     */
    @RequestMapping(value = "delDepartment/{param}")
    @ResponseBody
    public String delDepartment(String officeId, @PathVariable("param") String param, String staffId, String pdate) {
    	if("dept".equals(param)) { // 删除当前部门计划
    		return leadershipSchedulingService.delDept(officeId);    		
    	} else if("staff".equals(param)){ // 删除当前部门当前员工计划  pdate为null
    		return leadershipSchedulingService.delStaff(officeId, staffId, pdate);    		
    	} else if("staffPdate".equals(param)){ // 删除当前部门选中一天的计划  staffId为null
    		return leadershipSchedulingService.delStaff(officeId, staffId, pdate);
    	}
    	return "";
    }
    
    /**
     * 编辑部门计划
     */
    @RequestMapping(value = "leaderPlan")
    public String leaderPlan(String officeId, String time, Model model) {
    	model.addAttribute("officeId", officeId);
    	model.addAttribute("time", time);
		return "prss/leadershipScheduling/leaderPlan";
    }
    /**
     * 人员计划
     * @param officeId
     * @param time
     * @param model
     * @return
     */
    @RequestMapping(value = "addDepartmentStaff/{param}")
    public String addDepartmentStaff(String officeId, Model model, @PathVariable("param") String param, String staffId, String pdate) {
    	if("add".equals(param)) { // 增加页面
    		model.addAttribute("officeId", officeId);
    		model.addAttribute("staffList", leadershipSchedulingService.queryStaffNameByOffice(officeId));
    		return "prss/leadershipScheduling/addDepartmentStaff";    		
    	} else if("modify".equals(param)) { // 修改页面
    		model.addAttribute("officeId", officeId);
    		List<Map<String,Object>> list = leadershipSchedulingService.queryLeaderDetail(officeId, staffId, pdate);
    		model.addAttribute("staffName", list.get(0).get("OFFICENAME"));
    		model.addAttribute("startTm", list.get(0).get("STARTTM"));
    		model.addAttribute("endTm", list.get(0).get("ENDTM"));
    		model.addAttribute("endTm", list.get(0).get("ENDTM"));
    		model.addAttribute("staffId", list.get(0).get("ID"));
    		model.addAttribute("selectId", list.get(0).get("WORKERID"));
    		model.addAttribute("staffList", leadershipSchedulingService.queryStaffNameByOffice(officeId));
    		return "prss/leadershipScheduling/addDepartmentStaff";  
    	}
		return "";
    }
    
    /**
     * 查询领导名称
     * @param officeId
     * @param staffName
     * @param model
     * @return
     */
    @RequestMapping(value = "searchStaff")
    @ResponseBody
    public String searchStaff(String officeId, String staffName, Model model) {
    	if(officeId.isEmpty() || staffName.isEmpty()) {
    		return "";
    	}
    	List<Map<String, Object>> list = leadershipSchedulingService.queryStaffNameByOffice(officeId, staffName);
    	return JSON.toJSONString(list.size()> 0? list: null, SerializerFeature.WriteMapNullValue);
    }
    
    /**
     * 增加人员
     * @param officeId
     * @param staffId
     * @param startTmc
     * @param endTm
     * @param param
     * @return
     */
    @RequestMapping(value = "addStaff/{param}")
    @ResponseBody
    public String addStaff(AmLeaderPlan amLeaderPlan, @PathVariable("param") String param) {
    	return JSON.toJSONString(leadershipSchedulingService.insertStaff(amLeaderPlan), SerializerFeature.WriteMapNullValue);
    }
    
    /**
     * 打印计划
     * @param request
     * @param response
     * @param title
     * @param data
     * @param searchTime
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,String title,String data, String searchTime) {
        title = StringEscapeUtils.unescapeHtml4(title);
        data = StringEscapeUtils.unescapeHtml4(data);
        JSONArray titleArray = JSONArray.parseArray(title);
        List<Map<String,String>> dataList = JSON.parseObject(data, new TypeReference<List<Map<String,String>>>() {
        });
        dataList = dealExcelData(dataList);
        
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < titleArray.size(); i++) {
            titleList.add(titleArray.getJSONObject(i).getString("title"));
        }
        try {
            String fileName = "领导值班计划" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            String agent = (String) request.getHeader("USER-AGENT");
            String downloadFileName = "";
            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
            } else {
                downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

            SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");    
            SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy年MM月dd日 E");    
            String excelTitle = "领导值班计划" + sdf1.format(sdf.parse(searchTime));
            ExportLPExcel excel = new ExportLPExcel(excelTitle, titleList);
            excel.setDataList(titleArray, dataList);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("领导值班计划导出失败" + e.getMessage());
        }
    }

    /**
     * 处理excel表格数据 
     * @param dataList
     * @return
     */
	private List<Map<String,String>>  dealExcelData(List<Map<String, String>> dataList) {
		String valueStr = "";
        if(dataList.size() != 0) {
        	Map<String, String> map;
        	for(int i = 0; i < dataList.size(); i++) {
        		map = dataList.get(i);
        		if(map.get("MON") != null) {
    				valueStr = map.get("MON").toString().replaceAll(":", " ").replaceAll(",", "\n");
    				map.put("MON", valueStr);
    			}
        		if(map.get("TUE") != null) {
        			valueStr = map.get("TUE").toString().replaceAll(":", " ").replaceAll(",", "\n");
        			map.put("TUE", valueStr);
        		}
        		if(map.get("WED") != null) {
        			valueStr = map.get("WED").toString().replaceAll(":", " ").replaceAll(",", "\n");
        			map.put("WED", valueStr);
        		}
        		if(map.get("THU") != null) {
        			valueStr = map.get("THU").toString().replaceAll(":", " ").replaceAll(",", "\n");
        			map.put("THU", valueStr);
        		}
        		if(map.get("FRI") != null) {
        			valueStr = map.get("FRI").toString().replaceAll(":", " ").replaceAll(",", "\n");
        			map.put("FRI", valueStr);
        		}
        		if(map.get("SAT") != null) {
        			valueStr = map.get("SAT").toString().replaceAll(":", " ").replaceAll(",", "\n");
        			map.put("SAT", valueStr);
        		}
        		if(map.get("SUN") != null) {
        			valueStr = map.get("SUN").toString().replaceAll(":", " ").replaceAll(",", "\n");
        			map.put("SUN", valueStr);
        		}
        	}        	
        }
		return dataList;
	}
    
	/**
	 * 当前日期的三天计划时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tlist")
	public String tlist(Model model) {
		return "prss/leadershipScheduling/LeadershipSchedulingTList";
	}
}
