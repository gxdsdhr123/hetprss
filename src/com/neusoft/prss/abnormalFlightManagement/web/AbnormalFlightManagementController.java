package com.neusoft.prss.abnormalFlightManagement.web;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.abnormalFlightManagement.entity.AbnormalFlightVo;
import com.neusoft.prss.abnormalFlightManagement.entity.AbnormalFlightWordVo;
import com.neusoft.prss.abnormalFlightManagement.service.AbnormalFlightManagementService;
import com.neusoft.prss.common.util.ExportWordUtils;
import com.neusoft.prss.leadershipScheduling.entity.ExportLPExcel;
import com.neusoft.prss.produce.entity.MonitorReportEntity;
/**
 * 不正常航班管理
 * @author lwg
 * @ddate 2017/12/18
 */
@Controller
@RequestMapping("${adminPath}/abnormal/abnormalFlightManagement")
public class AbnormalFlightManagementController extends BaseController{
	
	 @Autowired
	 private AbnormalFlightManagementService abnormalFlightManagementService;
	
	/**
	 * 跳转不正常航班管理
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "prss/abnormalFlightManagement/abnormalFlightManagementList";
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
     * @param startTime				开始时间
     * @param endTime				结束时间
     * @param sendDept				涉及部门
     * @param feebBack				反馈状态
     * @param airFlightCompany		航空公司
     * @param airGroup				航空公司分组
     * @return
     */
    @RequestMapping(value = "getGridData")
    @ResponseBody
    public String getGridData(String searchData, String pageNumber, String pageSize) {
    	String officeId = UserUtils.getUser().getOffice().getId();
    	if(null == pageNumber) {
    		pageNumber = "1";
    	}
    	if(null == pageSize) {
    		pageSize = "10";
    	}
    	
    	int begin = (Integer.valueOf(pageNumber) - 1) * Integer.valueOf(pageSize) + 1;
        int end = Integer.valueOf(pageSize) + begin - 1;
    	return JSON.toJSONString(abnormalFlightManagementService.getGridData(searchData, begin, end, officeId), SerializerFeature.WriteMapNullValue);
    }
    
    /**
     *跳转增加不正常航班
     */
    @RequestMapping(value = "abnormalFlight/{param}")
    public String addAbnormalFlight(Model model, @PathVariable("param") String param, String searchId) {
    	model.addAttribute("sign", param);
    	if("add".equals(param)) { // 跳转增加
    		model.addAttribute("sendDeptList", abnormalFlightManagementService.getSenDepart());
    		model.addAttribute("dutyId", UserUtils.getUser().getId());
    		model.addAttribute("DUTYNAME", UserUtils.getUser().getName());
    		return "prss/abnormalFlightManagement/addAbnormalFlight";    		
    	} else if("show".equals(param)) { // 跳转查看
    		// 发送部门
    		model.addAttribute("sendDeptList", abnormalFlightManagementService.getSenDepart());
    		// 页面展示数据
    		List<Map<String, Object>> list = abnormalFlightManagementService.showAbnormalFlight(searchId);
    		dealAbnormalData(model, list);
    		// 动态部门列表
    		model.addAttribute("deptFeedBackInfo", abnormalFlightManagementService.getDepartFeedBackInfo(searchId, null));
    		model.addAttribute("abnormalInfoList", JSON.toJSONString(list, SerializerFeature.WriteMapNullValue));

    		return "prss/abnormalFlightManagement/addAbnormalFlight";    	
    	} else if("rep".equals(param)) { // 跳转反馈报告
    		// 当前登陆用户所在部门id
    		String userOfficeId = UserUtils.getUser().getOffice().getId();
    		// 页面展示数据
    		List<Map<String, Object>> list = abnormalFlightManagementService.showAbnormalFlight(searchId);
    		dealAbnormalData(model, list);
    		// 动态部门列表
    		model.addAttribute("deptFeedBackInfo", abnormalFlightManagementService.getDepartFeedBackInfo(searchId, userOfficeId));
    		// 当前登陆用户id    		
    		model.addAttribute("userId",  UserUtils.getUser().getId());
    		// 当前登陆用户名
    		model.addAttribute("userName", UserUtils.getUser().getName());
    		return "prss/abnormalFlightManagement/addAbnormalFlight";  
    	} else if("cdm".equals(param)) { // 跳转CDM判责
    		List<Map<String, Object>> list = abnormalFlightManagementService.showAbnormalFlight(searchId);
    		dealAbnormalData(model, list);
    		// 动态部门列表
    		model.addAttribute("deptFeedBackInfo", abnormalFlightManagementService.getDepartFeedBackInfo(searchId, null));
    		// 当前登陆用户id    		
    		model.addAttribute("cmdUserId",  UserUtils.getUser().getId());
    		// 当前登陆用户名
    		model.addAttribute("cmdUserName", UserUtils.getUser().getName());
    		return "prss/abnormalFlightManagement/addAbnormalFlight";  
    	} else if("search".equals(param)) { // 筛选
    		// 部门
    		model.addAttribute("sendDeptList", abnormalFlightManagementService.getSenDepart());
    		// 航空公司
    		model.addAttribute("airFlightList", abnormalFlightManagementService.getAirFlightList());
    		// 航空公司分组
    		model.addAttribute("airFlightInfoList", airFlightInfoList());
    		
    		return "prss/abnormalFlightManagement/seachAbnormalFlight";  
    	}
		return null;
    }
    /**
     * 航空公司分组
     * @return
     */
    private List<Map<String, Object>> airFlightInfoList() {
    	List<Map<String, Object>> list = new ArrayList<>();
    	Map<String, Object> map = new HashMap<>();
    	String[] names = new String[]{"东航", "南航", "海航", "BGS外航"};
    	for(int i = 0; i < names.length; i++) {
    		map = new HashMap<>();
    		map.put("ID", (i+1));
    		map.put("NAME", names[i]);
    		list.add(map);    		
    	}
		return list;
	}

	/**
     * 处理页面加载信息
     * @param model
     * @param list
     */
    private void dealAbnormalData(Model model, List<Map<String, Object>> list) {
    	if(list.size() != 0) {
    		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				for (String key : map.keySet()) {
					model.addAttribute(key, map.get(key));
			    }
			}
    	}
    }
    /**
     * 获取不正常航班信息
     * @param flightNumber
     * @param flightDate
     * @return
     */
    @RequestMapping(value = "addAbnormalFlightInfo")
    @ResponseBody
    public String addAbnormalFlightInfo(String flightNumber, String flightDate, Model model) {
    	model.addAttribute("flightNumber", flightNumber);
    	return JSON.toJSONString(abnormalFlightManagementService.getAbnormalFlightInfo(flightNumber, flightDate), SerializerFeature.WriteMapNullValue);
    }
    
    /**
     * 保存该航班信息
     * @param flightNumber
     * @param flightDate
     * @return
     */
    @RequestMapping(value = "addAbnormalFlightData")
    @ResponseBody
    public String addAbnormalFlightData(AbnormalFlightVo abnormalFlightVo) {  
    	return abnormalFlightManagementService.addAbnormalFlightData(abnormalFlightVo);
    }
    
    /**
     * 删除该不正常航班信息 
     * @param searchId
     * @return
     */
    @RequestMapping(value = "delAbnormalFlight")
    @ResponseBody
    public String delAbnormalFlight(String searchId) {    	
    	return abnormalFlightManagementService.delAbnormalFlight(searchId);
    }
    
    /**
     * 当前部门是否再此信息中存在
     * @param searchId
     * @return
     */
    @RequestMapping(value = "deptIsExist")
    @ResponseBody
    public String deptIsExist(String searchId) {    
    	String userOffice = UserUtils.getUser().getOffice().getId();
		List<Map<String, Object>> list = abnormalFlightManagementService.getDepartFeedBackInfo(searchId, null);
		List<String> dept = new ArrayList<String>();
		for (Iterator syso = list.iterator(); syso.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) syso.next();
			dept.add(map.get("OFFICE_ID")+"");
		}
		if(dept.contains(userOffice)) {
			return "1";
		} else {
			return "0";
		}
    }
    
    /**
     * 部门反馈信息录入
     * @param feedBackId        反馈表ID
     * @param feedBackOper		操作者ID
     * @param feedBackDate		操作时间
     * @param feedBackContent	回复内容
     * @return
     */
    @RequestMapping(value = "feedBackAirRep")
    @ResponseBody
    public String feedBackAirRep(String feedBackId, String feedBackOper, String feedBackDate, String feedBackContent) {   
    	if("".equals(feedBackId) || null == feedBackId) {
    		return "faile";
    	}
    	if("".equals(feedBackOper) || null == feedBackOper) {
    		feedBackOper = UserUtils.getUser().getId();
    	}
    	return abnormalFlightManagementService.feedBackAirRep(feedBackId, feedBackOper, feedBackDate, feedBackContent);
    }
    
	/**
	 * 
	 * @param feedBackId     不正常航班表id
	 * @param cdmId			 CDM操作人员id	
	 * @param cdmDate		 CDM操作时间
	 * @param cmdContent	 CDM填写内容
	 * @return
	 */
    @RequestMapping(value = "aCDMContractorResponsible")
    @ResponseBody
    public String aCDMContractorResponsible(String feedBackId, String cdmId, String cdmDate, String cmdContent) {   
    	if("".equals(feedBackId) || null == feedBackId) {
    		return "faile";
    	}
    	if("".equals(cdmId) || null == cdmId) {
    		cdmId = UserUtils.getUser().getId();
    	}
    	return abnormalFlightManagementService.aCDMContractorResponsible(feedBackId, cdmId, cdmDate, cmdContent);
    }
    

    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,String title,String data) {
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
            String fileName = "不正常航班管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
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

            String excelTitle = "不正常航班管理" + DateUtils.getDate("yyyy年MM月dd日 E");
//            String excelTitle = "领导值班计划" + sdf1.format(sdf.parse(searchTime));
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
        		map.put("NO", String.valueOf(i+1));
        		if(map.get("DEPTNAME") != null) {
    				valueStr = map.get("DEPTNAME").toString().replaceAll("[,，]", "\n");
    				map.put("DEPTNAME", valueStr);
    			}
        		if(map.get("DEPTSTA") != null) {
        			valueStr = map.get("DEPTSTA").toString();
        			String[] st = valueStr.split(",");
        			int restr = 0;
        			for(int j = 0; j < st.length; j ++) {
        				if("1".equals(st[j])) {
        					restr += 1;
        				}
        			}
        			if("0".equals(String.valueOf(restr))) {
        				valueStr = "未调查";
        			} else if(restr == st.length) {
        				valueStr = "已反馈";
        			} else {
        				valueStr = "调查中";
        			}
        			map.put("DEPTSTA", valueStr);
        		}
//        		CMDSTA=0, DEPTSTA=0,0,0}, 
        		if(map.get("CMDSTA") != null) {
        			valueStr = map.get("CMDSTA").toString();
        			if("0".equals(valueStr)) {
        				valueStr = "调查中";
        			} else {
        				valueStr = "已完成";
        			}
        			
        			map.put("CMDSTA", valueStr);
        		}
        		
        	}        	
        }
		return dataList;
	}
	/**
	 * 打印报表
	 */
	@RequestMapping(value="printword")
	public void printword(HttpServletRequest request,HttpServletResponse response,
			AbnormalFlightWordVo abnormalFlightWordVo){

		String officeNames = abnormalFlightManagementService.getOfficeNames(abnormalFlightWordVo.getOfficeId());
		
		abnormalFlightWordVo.setOfficeId(officeNames);
		
		String[] officeNameArr = abnormalFlightWordVo.getFeedBackOfficeName().split(",");
		String[] contentArr = abnormalFlightWordVo.getDeptFeedBackContent().split(",");
		String[] nameArr = abnormalFlightWordVo.getFeedBackOperName().split(",");
		String[] dateArr =  abnormalFlightWordVo.getOperDate1().split(",");

		List<String[]> tableList = new ArrayList<String[]>();
		for (int i = 0; i < officeNameArr.length; i++) {
			String content = "";
			if(contentArr.length != 0){
				content = contentArr[i];
			}
			String name = "";
			if(nameArr.length != 0){
				name = nameArr[i];
			}
			String date = "";
			if(dateArr.length != 0){
				date = dateArr[i];
			}
			String[] lineOne = {officeNameArr[i],content};
			String[] lineTwo = {"值班员",name,"日期",date};
			tableList.add(lineOne);
			tableList.add(lineTwo);
		}

		OutputStream out = null;
        try {
        	// 获取数据
        	Map<String, String> params = ExportWordUtils.objToMap(abnormalFlightWordVo);
        	String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
        	String wordTmpPath = wiPath + "/template/abnormalReport.docx";

        	String fileName = "不正常航班报告" + DateUtils.getDate("yyyyMMddHHmmss") + ".docx";
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
        	
            out = response.getOutputStream();
        	ExportWordUtils.changWord(wordTmpPath, params, tableList,out);
        } catch (Exception e) {
        	logger.error("数据流写入失败" + e.getMessage(),e);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e2) {
                logger.error("输出流关闭失败" + e2.getMessage());
            }
        }
	}
}
