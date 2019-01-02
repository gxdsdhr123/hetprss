package com.neusoft.prss.statisticalanalysis.web;

import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.statisticalanalysis.service.SaFltJiwuService;

/**
 * 机务统计表
 */
@Controller
@RequestMapping(value = "${adminPath}/saFltJiwu")
public class SaFltJiwuController extends BaseController{
	@Autowired
	private SaFltJiwuService saFltJiwuService;
	
	@Resource
    private CacheService cacheService;
	
	/**
	 * 机务统计列表页
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		return "prss/statisticalanalysis/saFltJiwuList";
	}
	
	/**
	 * 机务统计列表数据
	 */
	@RequestMapping(value="getData")
	@ResponseBody
	public JSONObject getData(HttpServletRequest request){
		JSONObject rs = new JSONObject();
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
		String pageNumber = "";
		String pageSize = "";
		String searchValue = "";
		String airlines = "";
		if(params!=null){
			if(params.containsKey("pageNumber"))
				pageNumber = params.get("pageNumber")==null?"":params.get("pageNumber").toString();
			if(params.containsKey("pageSize"))
				pageSize = params.get("pageSize")==null?"":params.get("pageSize").toString();
			if(params.containsKey("searchValue"))
				searchValue = params.get("searchValue")==null?"":params.get("searchValue").toString();
			if(params.containsKey("airlines"))
				airlines = params.get("airlines")==null?"":params.get("airlines").toString();
		}
		try {
			int begin = 1;
			int end = 21;
			if(pageNumber!=null&&!pageNumber.equals("")&&pageSize!=null&&!pageSize.equals("")){
				int intPageNum = Integer.parseInt(pageNumber);
				int intPageSize = Integer.parseInt(pageSize);
				begin=(intPageNum - 1) * intPageSize+1;
		        end=intPageNum * intPageSize+1;
			}
			params.put("begin", begin);
			params.put("end", end);
			if(searchValue!=null&&!searchValue.equals("")){
				searchValue = URLDecoder.decode(searchValue,"UTF-8");
				params.put("searchValue", searchValue);
			}
			if(airlines!=null&&!airlines.equals("")){
				params.put("airlines", airlines.split(","));
			}
			rs = saFltJiwuService.getDataList(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rs;
	}
	
	@RequestMapping(value = "exportExcel")
	public void printList(HttpServletRequest request, HttpServletResponse response) {
		String paramsStr = StringEscapeUtils.unescapeHtml4(request.getParameter("param"));
        Map<String,Object> params = JSON.parseObject(paramsStr);
		String searchValue = "";
		String airlines = "";
		if(params!=null){
			if(params.containsKey("searchValue"))
				searchValue = params.get("searchValue")==null?"":params.get("searchValue").toString();
			if(params.containsKey("airlines"))
				airlines = params.get("airlines")==null?"":params.get("airlines").toString();
		}
		String columnName = "[{\"field\":\"airlineShortName\"},{\"field\":\"inOutFlag\"},{\"field\":\"flightDate\"},"
				+ "{\"field\":\"flightNumber2\"},{\"field\":\"acttypeCode\"},{\"field\":\"aircraftNumber\"},"
				+ "{\"field\":\"atdOrAta\"},{\"field\":\"departApt3code\"},{\"field\":\"arrivalApt3code\"},"
				+ "{\"field\":\"fltAttrCode\"},{\"field\":\"propertyShortName\"},{\"field\":\"fxFlag\"},{\"field\":\"specialService\"}]";
		String[] title = { "运输公司", "进出属性", "航班日期", "航班号",  "机型", "机号", "起降时间", "起飞机场", "降落机场", "航段性质", "任务性质", "是否放行", "非例行" };
		JSONArray columnArr = JSON.parseArray(columnName);
		try {
			if(searchValue!=null&&!searchValue.equals("")){
				searchValue = URLDecoder.decode(searchValue,"UTF-8");
				params.put("searchValue", searchValue);
			}
			if(airlines!=null&&!airlines.equals("")){
				params.put("airlines", airlines.split(","));
			}
			String fileName = "机务统计" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
			setHeader(request, response, fileName);
			ExportExcel excel = new ExportExcel(fileName);
			excel.setDataList(title, columnArr, saFltJiwuService.getDownDataList(params));
			excel.write(response);
			excel.dispose();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("机务统计" + e.getMessage());
		}
	}
	
    private void setHeader(HttpServletRequest request,HttpServletResponse response,String fileName){
		try {
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
		}catch(Exception e){
			logger.error("set header error:"+e.toString());
		}
	}
    
    /**
     * 筛选弹出复选框.
     */
    @RequestMapping(value = "openCheck")
    public String openCheck(String type,String selectedId,String selectedText,Model model) {
        JSONArray resultArr = new JSONArray();
        if ("airline".equals(type)) {
        	JSONArray airLineArray = cacheService.getOpts("dim_airline", "icao_code","airline_shortname","airline_code");
          	for (int i = 0; i < airLineArray.size(); i++) {
           		String id = airLineArray.getJSONObject(i).getString("id");
           		String alncode = airLineArray.getJSONObject(i).getString("airline_code");
           		String text = airLineArray.getJSONObject(i).getString("text");
           		JSONObject airlinesObj = new JSONObject();
           		airlinesObj.put("id", id);
           		airlinesObj.put("selectText", id+";"+alncode+";"+text);
           		airlinesObj.put("text", text);
           		resultArr.add(airlinesObj);
           	}
           	JSONObject resultObj = getResultWithoutSelected(resultArr, selectedId);
           	model.addAttribute("items", resultObj.get("arr"));
           	model.addAttribute("sitems", resultObj.get("sArray"));
        }
        model.addAttribute("type", type);
        model.addAttribute("selectedId", selectedId);
        model.addAttribute("selectedText", selectedText);
        return "prss/flightdynamic/fdFilterCheck";
    }
    
    /**
     * 从给定的jsonarray中去除已选的值
     */
    public JSONObject getResultWithoutSelected(JSONArray arr,String selectedId) {
        JSONObject resultObj = new JSONObject();
        JSONArray sArray = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if(selectedId!=null&&selectedId.contains(obj.getString("id"))){
            	sArray.add(obj);
            }
        }
        arr.removeAll(sArray);
        resultObj.put("arr", arr);
        resultObj.put("sArray", sArray);
        return resultObj;
    }
}
