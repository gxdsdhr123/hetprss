package com.neusoft.framework.modules.maintain.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;

import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.modules.maintain.service.RunningTimeService;
import com.neusoft.framework.modules.sys.entity.User;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.sun.org.apache.bcel.internal.generic.Select;

@Controller
@RequestMapping(value = "${adminPath}/sys/RuningTime")
public class RunningTimeController {
	@Resource
	private CacheService cacheService;
	@Autowired
	private RunningTimeService runningTimeService;
	
	/**
	 * 车辆行驶时间
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list",""})
	public String getQueryDataList(Model model,HttpServletRequest request){
		User currentUser = UserUtils.getUser();
		String id=currentUser.getId();
		String jobKind=request.getParameter("jobKind");
		String aprons=request.getParameter("aprons");
		String gate=request.getParameter("gate");
		String stype=request.getParameter("stype");
		String etype=request.getParameter("etype");
		String pageNum=request.getParameter("pageNum"); //当前页
		String pageCount = request.getParameter("pageCount");//每页显示行数
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("jobtypecode", jobKind);
		param.put("stype", stype);
		param.put("etype", etype);
		List<String> pos= new ArrayList<>();
		if (StringUtils.isNoneBlank(aprons)) {
			pos.add(aprons);
		};
		if (StringUtils.isNoneBlank(gate)) {
			pos.add(gate);
		}
		param.put("pos", pos);
        if (pageNum == null)
            pageNum = "1";
        int page = Integer.parseInt(pageNum);
        if (pageCount == null)
            pageCount = "10";
        int count = Integer.parseInt(pageCount);
        int start = count * (page - 1);
        int end = count * page;
        param.put("start", String.valueOf(start));
        param.put("end", String.valueOf(end));
        int totalCount=runningTimeService.getTotalRows(param);
        int pages=(int)Math.ceil(totalCount*1.0/count);//总页数
		List<JSONObject> runningTimeData=runningTimeService.getDataList(param);
		JSONArray runningTime = new JSONArray();
		for (JSONObject jsonobj : runningTimeData) {
			runningTime.add(jsonobj);
		};
		List<JSONObject> gateData=runningTimeService.getGateDataList();
		JSONArray gateArray = new JSONArray();
		for (JSONObject jsonobj : gateData) {
			gateArray.add(jsonobj);
		};
        JSONArray apronsArray = cacheService.getList("dim_bay_apron");
        List<JSONObject> jobKindData=runningTimeService.getJobKindDataList(id);
		JSONArray jobKindArray = new JSONArray();
		for (JSONObject jsonobj : jobKindData) {
			jobKindArray.add(jsonobj);
		};
		model.addAttribute("totalRows", totalCount);//总行数
        model.addAttribute("pages",pages);//总页数
        model.addAttribute("pageNum", pageNum);//当前页
        model.addAttribute("pageCount", pageCount);//每页显示行数
		model.addAttribute("jobKind",jobKind);
		model.addAttribute("aprons",aprons);
		model.addAttribute("gate",gate);
		model.addAttribute("stype",stype);
		model.addAttribute("etype",etype);
		model.addAttribute("runningTime",runningTime);
		model.addAttribute("gatedata",gateArray);
		model.addAttribute("apronsdata",apronsArray);
		model.addAttribute("jobKinddata",jobKindArray);
		return "modules/maintain/runningTimeList";
	};
	
	/**
	 * 新增车辆行驶时间
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"save"})
	public String saveData(Model model,HttpServletRequest request){
		String jobKind=request.getParameter("jobKind");
		String stype=request.getParameter("stype");
		String etype=request.getParameter("etype");
		String spos=request.getParameter("spos");
		String epos=request.getParameter("epos");
		String stime=request.getParameter("stime");
		String etime=request.getParameter("etime");
		Map<String,String> param = new HashMap<String, String>();
		param.put("jobKind", jobKind);
		param.put("stype", stype);
		param.put("etype", etype);
		param.put("spos", spos);
		param.put("epos", epos);
		int total = runningTimeService.checkData(param);
		if (total>0) {
			return "1";
		}
		User currentUser = UserUtils.getUser();
		String user=StringUtils.isNotBlank(currentUser.getName())?currentUser.getName():"";
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("user", user);
		runningTimeService.insertData(param);
		return "success";
	};
	
	/**
	 * 删除一条记录
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"deleteOneData"})
	public String deleteOneData(HttpServletRequest request){
		String id=request.getParameter("id");
		runningTimeService.deleteOneData(id);
		return "success";
	};
	
	/**
	 * 批量删除记录
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"deleteMultiData"})
	public String deleteMultiData(@RequestParam(value = "idsArr[]",required = true) String[] idsArr){
		runningTimeService.deleteMultiData(idsArr);
		return "success";
	};
	
	/**
	 * 修改记录
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"update"})
	public String updateData(Model model,HttpServletRequest request){
		String id=request.getParameter("id");
		String jobKind=request.getParameter("jobKind");
		String stype=request.getParameter("stype");
		String etype=request.getParameter("etype");
		String spos=request.getParameter("spos");
		String epos=request.getParameter("epos");
		String stime=request.getParameter("stime");
		String etime=request.getParameter("etime");
		Map<String,String> param = new HashMap<String, String>();
		param.put("jobKind", jobKind);
		param.put("stype", stype);
		param.put("etype", etype);
		param.put("spos", spos);
		param.put("epos", epos);
		User currentUser = UserUtils.getUser();
		String user=StringUtils.isNotBlank(currentUser.getName())?currentUser.getName():"";
		List<JSONObject> msgList=runningTimeService.getOneData(id);
		if (msgList.size()<=0) {
			return "error_1";
		}
		JSONObject msg=msgList.get(0);
		String jobKindOld=msg.getString("JOB_TYPE_CODE");
		String stypeOld=msg.getString("STYPE");
		String etypeOld=msg.getString("ETYPE");
		String sposOld=msg.getString("SPOS");
		String eposOld=msg.getString("EPOS");
		String sql=null;
		if (jobKindOld.equals(jobKind) && stypeOld.equals(stype) && etypeOld.equals(etype) && sposOld.equals(spos) && eposOld.equals(epos)) {
			sql="UPDATE DIM_PARAM_TIME_CONF SET FTIME='"+stime+"',RTIME='"+etime+"',UPDATE_TIME=SYSDATE,OPERATOR='"+user+"' WHERE ID="+id;
		}else{
			int total = runningTimeService.checkData(param);
			if (total>0) {
				return "error_2";
			}else {
				sql="UPDATE DIM_PARAM_TIME_CONF SET JOB_TYPE_CODE='"+jobKind+"',EPOS='"+epos+"',SPOS='"+spos+"',ETYPE='"+etype+"',STYPE='"+stype+"',FTIME='"+stime+"',RTIME='"+etime+"',UPDATE_TIME=SYSDATE,OPERATOR='"+user+"' WHERE ID="+id;
			}
		}
		runningTimeService.updateSql(sql);
		return "success";
	}
	
}
