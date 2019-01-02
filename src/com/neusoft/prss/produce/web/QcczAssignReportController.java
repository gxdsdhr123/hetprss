package com.neusoft.prss.produce.web;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.util.ExportWordUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.service.QcczAssignReportService;

/**
 * 清仓操作组勤务报告
 * @author yunwq
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/produce/qcczAssignReport")
public class QcczAssignReportController extends BaseController {

	@Autowired
    private QcczAssignReportService qcczAssignReportService;
	@Autowired
	private FileService fileService;
	
	/**
	 * 报表列表
	 * @return
	 */
	@RequestMapping(value="list")
	public String List(Model model){
//		model.addAttribute("restypes", JSON.toJSONString(monitorReportService.getOfficeRestype()));
		return "prss/produce/qcczAssignReportList";
	}
	
	/**
	 * 获取主页面列表数据
	 * @return
	 */
	@RequestMapping(value = "data")
    @ResponseBody
    public Map<String,Object> GetData(
            int pageSize,int pageNumber,String beginTime,String endTime,
            HttpServletRequest request, HttpServletResponse response) {
	 	beginTime = StringEscapeUtils.unescapeHtml4(beginTime);
	 	endTime = StringEscapeUtils.unescapeHtml4(endTime);
        try {
        	beginTime = java.net.URLDecoder.decode(beginTime,"utf-8");
            endTime = java.net.URLDecoder.decode(endTime,"utf-8");
        } catch (Exception e) {}

        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("beginTime", beginTime);
        param.put("endTime", endTime);
        return qcczAssignReportService.getDataList(param);
    }
	
	/**
	 * 打卡新增页面，并初始化人员select列表
	 * @return
	 */
	@RequestMapping(value="openAdd")
	public String OpenAdd(Model model){
		ArrayList<HashMap<String,Object>> peopleList = qcczAssignReportService.getPeople();
		model.addAttribute("peopleList", peopleList);
		return "prss/produce/qcczAssignReportInfo";
	}
	
	/**
	 * 新增页面保存
	 * @return
	 */
	@RequestMapping(value="doSave")
	public void Save(String ID,String FLTID,String flightNumber,String ACTTYPE_CODE,String AIRCRAFT_NUMBER,
			String lingban,String REMARK,String ATA,String ATD,String wsjqj,String fwt,String dtqj,
			String zjcz,String kcqj,String JOB_TYPE){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", ID);
		map.put("FLTID", FLTID);
		map.put("flightNumber", flightNumber);
		map.put("ACTTYPE_CODE", ACTTYPE_CODE);
		map.put("AIRCRAFT_NUMBER", AIRCRAFT_NUMBER);
		map.put("lingban", lingban);
		map.put("REMARK", REMARK);
		map.put("ATA", ATA);
		map.put("ATD", ATD);
		map.put("wsjqj", wsjqj);
		map.put("fwt", fwt);
		map.put("dtqj", dtqj);
		map.put("zjcz", zjcz);
		map.put("kcqj", kcqj);
		map.put("jobType", JOB_TYPE);
		if(ID == null || "".equals(ID)){
			qcczAssignReportService.save(map);
		}else{
			qcczAssignReportService.update(map);
		}
	}
	
	/**
	 * 获取航班详细信息
	 * @return
	 */
	@RequestMapping(value="searchFlightDetail")
	@ResponseBody
	public JSONArray SearchFlightDetail(String flightNumber,String flightDate){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("flightNumber", flightNumber);
		map.put("flightDate", flightDate);
		JSONArray obj = qcczAssignReportService.getFlightDetail(map);
		return obj;
	}
	/**
	 * 获取任务详细信息
	 * @return
	 */
	@RequestMapping(value="searchTaskDetail")
	@ResponseBody
	public JSONObject SearchTaskDetail(String fltId,String jobType){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fltId", fltId);
		map.put("jobType", jobType);
		JSONObject obj = qcczAssignReportService.getTaskDetail(map);
		return obj;
	}
	
	/**
	 * 打开修改页面
	 * @return
	 */
	@RequestMapping(value="openModify")
	public String OpenWin(Model model,String id){
		HashMap<String,Object> Map = qcczAssignReportService.getDataById(id);
		Map<String,Object> param = new HashMap<String,Object>();
		String fltId = Map.get("FLTID").toString();
		String jobType = Map.get("JOB_TYPE").toString();
		param.put("fltId", fltId);
		param.put("jobType", jobType);
		HashMap<String,Object> resultMap = qcczAssignReportService.getDataDetail(param);
		resultMap.put("JOB_TYPE", jobType);
		ArrayList<HashMap<String,Object>> selectPeople = qcczAssignReportService.getSelectPeople(Map.get("ID").toString());
		for (HashMap<String,Object> mapPeople: selectPeople) {
			model.addAttribute("selectPeople"+mapPeople.get("ITEM_ID").toString(),mapPeople.get("NAMES").toString());
		}
		ArrayList<HashMap<String,Object>> peopleList = qcczAssignReportService.getPeople();
		model.addAttribute("result",resultMap);
		model.addAttribute("peopleList", peopleList);
		return "prss/produce/qcczAssignReportInfo";
	}
	/**
	 * 删除一条报告
	 * @return
	 */
	@RequestMapping(value="delete")
	@ResponseBody
	public String delete(String reportId){
		String i = "0";
		try{
			qcczAssignReportService.delete(reportId);
			i = "1";
		}catch(Exception e){
			System.out.println(e);
		}
		return i;
	}
	
	/**
	 * 打印报表
	 */
	@RequestMapping(value="print")
	public void printword(HttpServletRequest request,HttpServletResponse response,
			String id){
		Map<String, String> params = new HashMap<String,String>();
		
		HashMap<String,Object> Map = qcczAssignReportService.getDataById(id);
		Map<String,Object> param = new HashMap<String,Object>();
		String fltId = Map.get("FLTID").toString();
		String jobType = Map.get("JOB_TYPE").toString();
		param.put("fltId", fltId);
		param.put("jobType", jobType);
		HashMap<String,Object> resultMap = qcczAssignReportService.getDataDetail(param);
		ArrayList<HashMap<String,Object>> selectPeople = qcczAssignReportService.getSelectPeople(Map.get("ID").toString());

		for (int i = 0; i < selectPeople.size(); i++) {
			String[] ids = selectPeople.get(i).get("NAMES").toString().split(",");
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < ids.length; j++) {
				String name = qcczAssignReportService.getPeopleName(ids[j]);
				if(j != 0){
					sb.append(","+name);
				}else{
					sb.append(name);
				}
			}
			if("1".equals(selectPeople.get(i).get("ITEM_ID").toString())){
				params.put("FWT", sb.toString());
			}else if("2".equals(selectPeople.get(i).get("ITEM_ID").toString())){
				params.put("DTQJ", sb.toString());
			}else if("3".equals(selectPeople.get(i).get("ITEM_ID").toString())){
				params.put("WSJQJ", sb.toString());
			}else if("4".equals(selectPeople.get(i).get("ITEM_ID").toString())){
				params.put("ZJCZ", sb.toString());
			}else if("5".equals(selectPeople.get(i).get("ITEM_ID").toString())){
				params.put("KCQJ", sb.toString());
			}
		}

		params.putAll(mapChange(Map));
		params.putAll(mapChange(resultMap));

		OutputStream out = null;
		try {
        	// 获取数据
        	String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
        	String wordTmpPath = wiPath + "/template/qcczAssignReport.docx";

        	String fileName = "清舱操作组勤务报告" + DateUtils.getDate("yyyyMMddHHmmss") + ".docx";
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
            //签名
            Map<String,byte[]> pictureMap = new HashMap<String,byte[]>();
            if(Map.containsKey("SIGNATORY")&&Map.get("SIGNATORY")!=null){
            	byte[] signature = fileService.doDownLoadFile(String.valueOf(Map.get("SIGNATORY")));
				if (signature != null && signature.length > 0) {
					pictureMap.put("SIGNATURE", signature);
				}
            }
        	ExportWordUtils.changeWord(wordTmpPath, params,pictureMap, null,out);
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
	/*
	 * 工具,Map<String, Object>转Map<String, String>
	 */
	private Map<String, String> mapChange(HashMap<String,Object> map){
		Map<String, String> params = new HashMap<String,String>();
		for(String key : map.keySet()){
			Object value = map.get(key);
			if(value != null){
				params.put(key, value.toString());
			}
		}
		return params;
	}
}