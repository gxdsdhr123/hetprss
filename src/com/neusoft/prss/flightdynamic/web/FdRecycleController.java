/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月19日 下午3:43:41
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.JedisUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.flightdynamic.dao.FdRecycleDao;
import com.neusoft.prss.flightdynamic.entity.FdFltGbak;
import com.neusoft.prss.flightdynamic.service.FdGridService;
import com.neusoft.prss.flightdynamic.service.FdRecycleService;

@Controller
@RequestMapping(value = "${adminPath}/fdRecycle")
public class FdRecycleController extends BaseController {
	@Resource
	private FdRecycleService fdRecycleService;
	@Resource
	private FdGridService fdGridService;
	@Resource
	private FdRecycleDao fdRecycleDao;
	@Resource
	private CacheService cacheService;

	/**
	 * 
	 * Discription:获取航班动态回收站页面.
	 * 
	 * @return
	 * @return:航班动态页面
	 * @author:yu-zd
	 * @update:2017年9月18日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "fdRecycleList")
	public String list(Model model) {
		dimAttributes(model);
		return "prss/flightdynamic/fdRecycleList";
	}

	/**
	 * 
	 * Discription:获取航班动态回收站列表.
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "getRecycleList")
	@ResponseBody
	public String getRecycleList(Model model) {
		Map<String, String> recycleMap = cacheService.getMap("recycle_fd");
		if (recycleMap == null) {
			return "";
		}
		JSONArray dataArray = new JSONArray();
		for (String key : recycleMap.keySet()) {
			JSONObject recycleMain = JSONObject.parseObject(recycleMap.get(key));
			JSONArray detailArray = recycleMain.getJSONArray("detail");
			JSONObject dataObj = new JSONObject();

			String depart_apt4code = "";
			String arrival_apt4code = "";
			String out_fltid = "";
			String in_fltid = "";
			String aircraft_number = "";
			String remark = "";
			String fltno = "";
			String in_property_code = "";
			String out_property_code = "";
			String airline_2code = "";
			String eta = "";
			String etd = "";
			String sta = "";
			String std = "";

			dataObj.put("id", key);

			for (int i = 0; i < detailArray.size(); i++) {
				JSONObject detailObjo = detailArray.getJSONObject(i);
				aircraft_number = detailObjo.get("aircraft_number") == null ? ""
						: detailObjo.get("aircraft_number").toString();
				if (StringUtils.isNotBlank("aln_2code")) {
					airline_2code = detailObjo.get("aln_2code").toString();
				}
				// 北京为出港航班 北京-大连
				if (detailObjo.get("depart_apt4code") != null && detailObjo.get("depart_apt4code").equals("ZBAA")) {
					arrival_apt4code = detailObjo.get("arrival_apt4code") == null ? ""
							: detailObjo.get("arrival_apt4code").toString();
					out_fltid = detailObjo.get("fltid") == null ? "" : detailObjo.get("fltid").toString();
					remark = detailObjo.get("remark") == null ? "" : detailObjo.get("remark").toString();
					fltno = detailObjo.get("flight_number") == null ? "" : detailObjo.get("flight_number").toString();
					etd = detailObjo.get("etd") == null ? "" : detailObjo.get("etd").toString();
					std = detailObjo.get("std") == null ? "" : detailObjo.get("std").toString();
					out_property_code = recycleMain.get("go_property_code") == null ? ""
							: recycleMain.get("go_property_code").toString();
					dataObj.put("out_property_code", out_property_code);
					dataObj.put("out_etd", etd);
					dataObj.put("out_std", std);
					dataObj.put("out_fltno", fltno);
					dataObj.put("out_fltid", out_fltid);
					dataObj.put("out_remark", remark);
					dataObj.put("arrival_apt4code", arrival_apt4code);

				}
				// 北京为进港航班 大连-北京
				for (int j = 0; j < detailArray.size(); j++) {
					JSONObject detailObji = detailArray.getJSONObject(i);
					// 1.如果北京为中间经停站&&找配对时候找相同航班号的 比如 北京为进港 呼和浩特-北京
					// 2.如果北京不为中间经停站&&找配对时候找不同航班号的 比如 大连-北京（北京在两头）
					if ((fdGridService.ifStopOver(detailArray)
							&& StringUtils.isNotBlank(detailObji.getString("flight_number"))
							&& StringUtils.isNotBlank(detailObjo.getString("flight_number"))
							&& detailObji.getString("flight_number").equals(detailObjo.getString("flight_number")))
							|| (!fdGridService.ifStopOver(detailArray)
									&& StringUtils.isNotBlank(detailObji.getString("flight_number"))
									&& StringUtils.isNotBlank(detailObjo.getString("flight_number"))
									&& !detailObji.getString("flight_number")
											.equals(detailObjo.getString("flight_number")))
							|| detailArray.size() == 1) {
						if (detailObji.get("arrival_apt4code") != null
								&& detailObji.get("arrival_apt4code").equals("ZBAA")) {
							arrival_apt4code = detailObji.get("depart_apt4code") == null ? ""
									: detailObji.get("depart_apt4code").toString();
							in_fltid = detailObji.get("fltid") == null ? "" : detailObji.get("fltid").toString();
							remark = detailObji.get("remark") == null ? "" : detailObji.get("remark").toString();
							fltno = detailObji.get("flight_number") == null ? ""
									: detailObji.get("flight_number").toString();
							eta = detailObji.get("eta") == null ? "" : detailObji.get("eta").toString();
							etd = detailObji.get("etd") == null ? "" : detailObji.get("etd").toString();
							sta = detailObji.get("sta") == null ? "" : detailObji.get("sta").toString();
							std = detailObji.get("std") == null ? "" : detailObji.get("std").toString();
							in_property_code = recycleMain.get("bak_property_code") == null ? ""
									: recycleMain.get("bak_property_code").toString();

							dataObj.put("in_property_code", in_property_code);
							dataObj.put("in_fltno", fltno);
							dataObj.put("in_eta", eta);
							dataObj.put("in_etd", etd);
							dataObj.put("in_sta", sta);
							dataObj.put("in_std", std);
							dataObj.put("in_remark", remark);
							dataObj.put("in_fltid", in_fltid);
							dataObj.put("depart_apt4code", depart_apt4code);
						}
					}
					// 北京不为中转站

				}

				dataObj.put("aircraft_number", aircraft_number);
				dataObj.put("airline_2code", airline_2code);
			}
			dataArray.add(dataObj);
		}
		String data = JSON.toJSONString(dataArray, SerializerFeature.WriteMapNullValue);
		return data;
	}

	/**
	 * 
	 * Discription:获取航班动态回收站动态
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */

	@RequestMapping(value = "getRecycleFd")
	@ResponseBody
	public String getRecycleFd(HttpServletRequest request) {
		Map<String, String> recycleMap = JedisUtils.getMap("pm_recycle_longterm");
		if (recycleMap == null) {
			return "";
		}
		JSONArray dataArray = new JSONArray();
		for (String key : recycleMap.keySet()) {
			JSONObject recyclePlan = JSONObject.parseObject(recycleMap.get(key));
			dataArray.add(recyclePlan);
		}
		String data = JSON.toJSONString(dataArray, SerializerFeature.WriteMapNullValue);
		return data;
	}

	/**
	 * 
	 * Discription:获取航班动态回收站总条数
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "getRecycleFdNum")
	@ResponseBody
	public String getRecycleFdNum(HttpServletRequest request) {
		String num = "0";
		Enumeration<String> enu = request.getParameterNames();
		Map<String, String> params = new HashMap<String, String>();
		while (enu.hasMoreElements()) {
			String paraName = enu.nextElement();
			String paraVlaue = request.getParameter(paraName);
			params.put(paraName, paraVlaue);
		}
		if (params.get("ifall").equals("yes") && JedisUtils.getMap("pm_recycle_longterm") != null) {
			num = JedisUtils.getMap("pm_recycle_longterm").size() + "";
		}
		return num;
	}

	/**
	 * 
	 * Discription:根据选定的长期计划id 查出长期计划 加上删除日期和删除人 插入到redis中
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "addFdToRecycle")
	@ResponseBody
	public String addFdToRecycle(@RequestParam(value = "ids[]", required = true) String ids) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		Map<String, String> mainIdMap = new HashMap<String, String>();
		mainIdMap.put("mainId", ids);
		JSONArray recyclePlans = fdRecycleService.getLongtermMain(mainIdMap);
		Map<String, String> recyclePlansMap = new HashMap<String, String>();
		for (int i = 0; i < recyclePlans.size(); i++) {
			String mainId = recyclePlans.getJSONObject(i).getString("id");
			mainIdMap.put("id", mainId);
			JSONArray detailArray = fdRecycleService.getLongtermDetail(mainIdMap);
			JSONObject recycleObj = recyclePlans.getJSONObject(i);
			recycleObj.put("operator", UserUtils.getUser().getId());
			recycleObj.put("operatorTime", time);
			recycleObj.put("detail", detailArray);
			recyclePlans.set(i, recycleObj);
			recyclePlansMap.put(mainId,
					JSON.toJSONString(recyclePlans.getJSONObject(i), SerializerFeature.WriteMapNullValue));
		}
		JedisUtils.mapPut("pm_recycle_longterm", recyclePlansMap);
		//fdRecycleService.deletePlan(ids.split(","));
		return "success";
	}

	/**
	 * 
	 * Discription:根据id获取航班动态主表 (查看)
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "getFdRecycleMainById")
	public String getFdRecycleMainById(Model model, @RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "type", required = true) String type) {
		Map<String, String> recycleMap = new HashMap<String, String>();
		recycleMap = cacheService.getMap("pm_recycle_longterm");
		String recycleJsonString = recycleMap.get(id);
		JSONObject recycleJsonObj = JSON.parseObject(recycleJsonString);
		//PlanMain planMain = JSONObject.toJavaObject(recycleJsonObj, PlanMain.class);
		dimAttributes(model);
		//model.addAttribute("plan", planMain);
		model.addAttribute("mainId", id);
		model.addAttribute("type", type);
		return "prss/flightdynamic/fdRecycleForm";
	}

	/**
	 * 
	 * Discription:根据id获取航班动态次表
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "getFdDetailById")
	@ResponseBody
	public String getFdDetailById(Model model, @RequestParam(value = "id", required = true) String id) {
		return null;
	}

	/**
	 * 
	 * Discription:航班动态 根据id删除redis中对应的数据
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "delFdRecycle")
	@ResponseBody
	public String delFdRecycle(@RequestParam(value = "ids[]", required = true) String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			cacheService.mapDel("recycle_fd", ids[i]);
		}
		return "success";
	}

	/**
	 * 
	 * Discription:还原航班动态回收站 在这里不区分是否为全部删除或者是删除半程 只要有info 和 infoother就插入 gbak
	 * gbakrel
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "recoveryFdRecycle")
	@ResponseBody
	@Transactional(readOnly = false)
	public String recoveryFdRecycle(@RequestParam(value = "ids[]", required = true) String ids) {
		Map<String, String> recycleMap = new HashMap<String, String>();
		recycleMap = cacheService.getMap("recycle_fd");
		// 批量插入预备
		List<FdFltGbak> gbakList = new ArrayList<FdFltGbak>();
		List<Map<String, String>> infoList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> infoOtherList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> gbakRelList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> ioRelList = new ArrayList<Map<String, String>>();
		String idArr[] = ids.split(",");
		for (int i = 0; i < idArr.length; i++) {
			if (recycleMap.keySet().contains(idArr[i])) {
				Map<String, String> infoMap = new HashMap<String, String>();
				Map<String, String> gbakRelMap = new HashMap<String, String>();
				Map<String, String> ioRelMap = new HashMap<String, String>();
				FdFltGbak gbakEntity = new FdFltGbak();

				JSONObject mainObj = JSONObject.parseObject(recycleMap.get(idArr[i]));
				JSONArray detailArray = mainObj.getJSONArray("detail");
				String gbakid = mainObj.getString("id");
				String terminal = StringUtils.isNotBlank(mainObj.getString("terminal_code"))
						? mainObj.getString("terminal_code") : "";
				String alnFlag = StringUtils.isNotBlank(mainObj.getString("aln_flag_code"))
						? mainObj.getString("aln_flag_code") : "";
				String bakProperty = StringUtils.isNotBlank(mainObj.getString("bak_property_code"))
						? mainObj.getString("bak_property_code") : "";
				String goProperty = StringUtils.isNotBlank(mainObj.getString("go_property_code"))
						? mainObj.getString("go_property_code") : "";
				String goFltno = StringUtils.isNotBlank(mainObj.getString("go_fltno")) ? mainObj.getString("go_fltno")
						: "";
				String bakFltno = StringUtils.isNotBlank(mainObj.getString("bak_fltno"))
						? mainObj.getString("bak_fltno") : "";
				String createUser = StringUtils.isNotBlank(mainObj.getString("create_user"))
						? mainObj.getString("create_user") : "";
				String createTime = StringUtils.isNotBlank(mainObj.getString("create_time"))
						? mainObj.getString("create_time") : "";
				String updateUser = StringUtils.isNotBlank(mainObj.getString("update_user"))
						? mainObj.getString("update_user") : "";
				String updateTime = StringUtils.isNotBlank(mainObj.getString("update_time"))
						? mainObj.getString("update_time") : "";
				gbakEntity.setId(gbakid);
				gbakEntity.setAlnFlag(alnFlag);
				gbakEntity.setTerminal(terminal);
				gbakEntity.setBakProperty(bakProperty);
				gbakEntity.setGoProperty(goProperty);
				gbakEntity.setCreateTime(createTime);
				gbakEntity.setCreateUser(createUser);
				gbakEntity.setUpdateTime(updateTime);
				gbakEntity.setUpdateUser(updateUser);
				gbakEntity.setGoFltno(goFltno);
				gbakEntity.setBakFltno(bakFltno);
				for (int j = 0; j < detailArray.size(); j++) {
					JSONObject detailObj = detailArray.getJSONObject(i);
					String fltid = StringUtils.isNotBlank(detailObj.getString("fltid")) ? detailObj.getString("fltid")
							: "";
					String arrivalApt4code = StringUtils.isNotBlank(detailObj.getString("arrival_apt4code"))
							? detailObj.getString("arrival_apt4code") : "";
					String departApt4code = StringUtils.isNotBlank(detailObj.getString("depart_apt4code"))
							? detailObj.getString("depart_apt4code") : "";
					String std = StringUtils.isNotBlank(detailObj.getString("std")) ? detailObj.getString("std") : "";
					String sta = StringUtils.isNotBlank(detailObj.getString("sta")) ? detailObj.getString("sta") : "";
					String etd = StringUtils.isNotBlank(detailObj.getString("etd")) ? detailObj.getString("etd") : "";
					String eta = StringUtils.isNotBlank(detailObj.getString("eta")) ? detailObj.getString("eta") : "";
					String remark = StringUtils.isNotBlank(detailObj.getString("remark"))
							? detailObj.getString("remark") : "";
					String flightNumber = StringUtils.isNotBlank(detailObj.getString("flight_number"))
							? detailObj.getString("flight_number") : "";
					String aircraftNumber = StringUtils.isNotBlank(detailObj.getString("aircraft_number"))
							? detailObj.getString("aircraft_number") : "";
					String actTypeCode = StringUtils.isNotBlank(detailObj.getString("acttype_code"))
							? detailObj.getString("acttype_code") : "";
					String aln2code = StringUtils.isNotBlank(detailObj.getString("aln_2code"))
							? detailObj.getString("aln_2code") : "";

					infoMap.put("fltid", fltid);
					infoMap.put("arrivalApt4code", arrivalApt4code);
					infoMap.put("departApt4code", departApt4code);
					infoMap.put("std", std);
					infoMap.put("sta", sta);
					infoMap.put("etd", etd);
					infoMap.put("eta", eta);
					infoMap.put("remark", remark);
					infoMap.put("flightNumber", flightNumber);
					infoMap.put("aircraftNumber", aircraftNumber);
					infoMap.put("actTypeCode", actTypeCode);
					infoMap.put("aln2code", aln2code);
					gbakRelMap.put("fltId", fltid);
					gbakRelMap.put("gbakId", gbakid);
					// 北京相关 info表
					if ("ZBAA".equals(arrivalApt4code) || "ZBAA".equals(departApt4code)) {
						infoList.add(infoMap);
						ioRelMap.put("iorelId", fdRecycleDao.getNewIorelId());
						if ("ZBAA".equals(arrivalApt4code)) {
							ioRelMap.put("inFltid", fltid);
						} else {
							ioRelMap.put("outFltid", fltid);
						}
					} else {
						infoOtherList.add(infoMap);
					}
					// 如果是全删 则都恢复
					if (isGoBak(detailArray)) {
						gbakRelList.add(gbakRelMap);
						ioRelList.add(ioRelMap);
					}
				}
				if (isGoBak(detailArray)) {
					gbakList.add(gbakEntity);
				}
			}
			if (gbakList.size() > 0) {
				fdRecycleService.insertFdFltGbaks(gbakList);
			}
			if (gbakRelList.size() > 0) {
				fdRecycleService.insertFdFltGbakrels(gbakRelList);
			}
			if (infoList.size() > 0) {
				fdRecycleService.insertFltInfos(infoList);
			}
			if (infoOtherList.size() > 0) {
				fdRecycleService.insertFltInfoOthers(infoOtherList);
			}
			if (ioRelList.size() > 0) {
				fdRecycleService.insertFdFltIorels(ioRelList);
			}
		}
		delFdRecycle(ids.split(","));
		return "success";
	}

	// 判断放入回收站的是否为全部航班 还是单进单出航班
	public boolean isGoBak(JSONArray detailArray) {
		int times = 0;
		for (int i = 0; i < detailArray.size(); i++) {
			JSONObject detailObj = new JSONObject();
			// 计ZBAA出现的次数
			if ((detailObj.keySet().contains("depart_apt4code")
					&& StringUtils.isNotBlank(detailObj.getString("depart_apt4code"))
					&& detailObj.getString("depart_apt4code").equals("ZBAA"))
					|| (detailObj.keySet().contains("arrival_apt4code")
							&& StringUtils.isNotBlank(detailObj.getString("arrival_apt4code"))
							&& detailObj.getString("arrival_apt4code").equals("ZBAA"))) {
				times++;
			}
		}
		if (times > 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Discription:清空长期计划回收站
	 * 
	 * @param model
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年9月8日 yu-zd [变更描述]
	 */
	@RequestMapping(value = "clearFd")
	@ResponseBody
	public String clearFd() {
		cacheService.deleCache("fd_recycle");
		return "success";
	}

	/**
	 * 
	 * Discription:获取下拉选项.
	 * 
	 * @param model
	 * @return:返回值意义
	 * @author:SunJ
	 * @update:2017年9月20日 SunJ [变更描述]
	 */
	private void dimAttributes(Model model) {
		try {
			// 性质
			JSONArray fltPropertys = cacheService.getOpts("dim_flight_property", "property_code", "property_shortname");
			model.addAttribute("fltPropertys", fltPropertys);
			// 机场
			// JSONArray airports = cacheService.getOpts("dim_airport",
			// "airport_code4",
			// "airport_name","airport_pinyin","airpor_pinyin_first_spell","airport_code4","airport_code3");
			// model.addAttribute("airports", airports);
			// 航空公司
			// JSONArray airlines = cacheService.getOpts("dim_airlines",
			// "airlines_code2",
			// "airlines_shortname","airlines_code2","airlines_code3","name_pinyin","name_pinyin_first_spell");
			// model.addAttribute("airlines", airlines);
			// 机型
			// JSONArray actTypes = cacheService.getOpts("dim_aircraft_type",
			// "aircraft_type","aircraft_type3", "aircraft_type_shortname");
			// model.addAttribute("actTypes", actTypes);
			// 机号
			// JSONArray aircrafts = cacheService.getOpts("dim_aircraft",
			// "aircraft", "aircraft");
			// model.addAttribute("aircrafts", aircrafts);
			// 外航/国内
			JSONArray alnFlags = cacheService.getCommonDict("airline_flag");
			model.addAttribute("alnFlags", alnFlags);
			// 是否为宽体机型
			JSONArray acttypeSizes = cacheService.getCommonDict("aln_flag");
			model.addAttribute("acttypeSizes", acttypeSizes);
			// 航站楼
			JSONArray terminals = cacheService.getCommonDict("terminal");
			model.addAttribute("terminals", terminals);
			// 航班范围
			JSONArray flightScopes = cacheService.getCommonDict("flight_scope");
			model.addAttribute("flightScopes", flightScopes);
			// 机坪区域
			JSONArray aprons = cacheService.getCommonDict("apron");
			model.addAttribute("aprons", aprons);
			// 廊桥位、外围航班
			JSONArray GAFlag = cacheService.getCommonDict("GAFlag");
			model.addAttribute("GAFlag", GAFlag);
			// 标识
			JSONArray identifying = cacheService.getCommonDict("identifying");
			model.addAttribute("identifying", identifying);
			// 状态
			JSONArray actStatus = cacheService.getCommonDict("acfStatus");
			model.addAttribute("actStatus", actStatus);
		} catch (Exception e) {
			logger.error("添加下拉框选项失败" + e.getMessage());
		}
	}
}
