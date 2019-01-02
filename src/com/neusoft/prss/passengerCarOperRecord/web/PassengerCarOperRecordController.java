package com.neusoft.prss.passengerCarOperRecord.web;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.passengerCarOperRecord.entity.PassengerCarOperRecord;
import com.neusoft.prss.passengerCarOperRecord.service.PassengerCarOperRecordService;
/**
 * 客梯车操作记录
 * @author lwg
 * @date 2017/12/26
 */
@Controller
@RequestMapping("${adminPath}/passengerCar/operationRecord")
public class PassengerCarOperRecordController extends BaseController{
	
	@Autowired
	private PassengerCarOperRecordService passengerCarOperRecordService;
	 
	 @Autowired
	 private FileService fileService;
	/**
	 * 跳转客梯车操作操作页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "prss/passengerCar/operationRecord";
	}
	
    /**
     * 
     *Discription:得到展现表格的表头
     *@param request
     *@return:返回值意义
     */
    @RequestMapping(value = "getTableHeader")
    @ResponseBody
    public String getTableHeader(HttpServletRequest request, String pageSign) {
    	if("".equals(pageSign) || null == pageSign) {
    		return JSON.toJSONString(null, SerializerFeature.WriteMapNullValue);    		
    	}
    	if("main".equals(pageSign)) {    		
    		return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);
    	} else {
    		// 获取数据
    		String officeId = UserUtils.getUser().getOffice().getId();
    		List<Map<String, Object>> list1 = passengerCarOperRecordService.getDeviceNumber(officeId);
    		Map<String, Object> map = new HashMap<>();
    		List<Map<String, Object>> list = passengerCarOperRecordService.getAddHeaderData();
    		
//    		List<Map<String, Object>> list2 = simulationData();
//    		map.put("carList", list2);
    		
    		map.put("carList", dealCarList(list1));
    		map.put("headerList", list);
    		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
    	}
    }
    private List<Map<String, Object>> dealCarList(List<Map<String, Object>> list) {
    	 List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    	 Map<String, Object> resultMap = new HashMap<String, Object>();
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) iterator.next();
			resultMap = new HashMap<String, Object>();
			for (String key : map.keySet()) {
				resultMap.put(key.toLowerCase(), map.get(key));
		    }
			resultList.add(resultMap);
		}
		return resultList;
    }
    
    /**
     * 模拟车辆编号列表， 空数据, 仅供测试使用
     * @return
     */
    private List<Map<String, Object>> simulationData() {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	Map<String, Object> map = new HashMap<>();
    	for(int i = 0; i < 10; i++) {
    		 map = new HashMap<>();
    		 map.put("value", 11*i+"no");
    		 map.put("text", 11*i+"no");
    		 list.add(map);
    	}
    	return list;
    }
    
    
    
    /**
     * 
     *Discription:得到表格数据
     *@param pageSize
     *@param pageNumber
     *@return:返回值意义
     */
    @RequestMapping(value = "getGridData")
    @ResponseBody
    public String getGridData(String pageNumber, String pageSize, String pageSign, String searchId, String searchText) {
    	if(null == pageNumber) {
    		pageNumber = "1";
    	}
    	if(null == pageSize) {
    		pageSize = "10";
    	}
    	int begin = (Integer.valueOf(pageNumber) - 1) * Integer.valueOf(pageSize) + 1;
        int end = Integer.valueOf(pageSize) + begin - 1;
    	if("".equals(pageSize) ) { 
    		return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);
    	}
    	if("".equals(pageSign) || null == pageSign) {
    		return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);    		
    	}
    	
    	
    	if("main".equals(pageSign)) { // 主表格数据
    		// 带有分页
    		return JSON.toJSONString(passengerCarOperRecordService.getMainData(begin, end, searchText), SerializerFeature.WriteMapNullValue);    		
    		
    	} else { // 增加 或 修改获取数据
    		
    		// 获取表单数据
    		List<Map<String, String>> list = passengerCarOperRecordService.getTableData(searchId);
    		return JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);   
    		
    	}

    }

    
    @RequestMapping(value = "pageJump/{param}")
    public String pageJump(String searchTime, @PathVariable("param") String param, String searchId, String pdate, Model model) {
		if("add".equals(param)) { 
			// 获取操作人员列表
			model.addAttribute("operList", passengerCarOperRecordService.getOperList());
			return "prss/passengerCar/addOperationRecord";
		} else if("modify".equals(param)) {
			model.addAttribute("paramModify", param);
			// 获取操作人员列表
			model.addAttribute("operList", passengerCarOperRecordService.getOperList());
			// 获取当前选中相关数据
			List<Map<String,Object>> data = passengerCarOperRecordService.getModifyData(searchId);
			dealPassengetCarData(model, data);
			//获取车辆信息
			List<Map<String, String>> list = passengerCarOperRecordService.getTableData(searchId);
			model.addAttribute("carList",list);
			return "prss/passengerCar/addOperationRecord";
		}
    	return pdate;
    	
    }
    
	/**
     * 处理页面加载信息
     * @param model
     * @param list
     */
    private void dealPassengetCarData(Model model, List<Map<String, Object>> list) {
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
     * 获取航空数据
     * @param flightDate
     * @param flightNumber
     * @return
     */
    @RequestMapping(value = "getAirFligthInfo")
    @ResponseBody
    public String getAirFligthInfo(String flightDate, String flightNumber) {
    	if(null == flightDate || "".equals(flightDate)) {
    		return "";
    	}
    	if(null == flightNumber || "".equals(flightNumber)) {
    		return "";
    	}
    	return JSON.toJSONString(passengerCarOperRecordService.getAirFligthInfo(flightDate, flightNumber), SerializerFeature.WriteMapNullValue);
    	
    }
    
    /**
     * 添加数据
     * @param flightDate
     * @param flightNumber
     * @param data
     * @param passengerCarOperRecord
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public String add(String flightDate, String flightNumber, String data, PassengerCarOperRecord passengerCarOperRecord) {
    	if(null == flightDate || "".equals(flightDate)) {
    		return "";
    	}
    	if(null == flightNumber || "".equals(flightNumber)) {
    		return "";
    	}
    	if("1".equals(isExists(flightDate, flightNumber))) {
    		return "faile";
    	}
    	data = StringEscapeUtils.unescapeHtml4(data);
    	passengerCarOperRecord.setData("");
    	passengerCarOperRecord.setCreateDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
    	String officeId = UserUtils.getUser().getOffice().getId();
    	return passengerCarOperRecordService.addData(passengerCarOperRecord, data, officeId);
    }
    /**
     * 修改数据
     * @param flightDate
     * @param flightNumber
     * @param data
     * @param passengerCarOperRecord
     * @return
     */
    @RequestMapping(value = "modify")
    @ResponseBody
    public String modify(String data) {
    	return passengerCarOperRecordService.updateData(data);
    }
    
    /**
     * 删除数据
     * @param searchId
     * @return
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public String del(String searchId) {
    	if(null == searchId || "".equals(searchId)) {
    		return "faile";
    	}
    	
    	return passengerCarOperRecordService.del(searchId);
    }
    
    /**
	 * 
	 *Discription:加载图片流.
	 *@param res
	 *@param id
	 */
	@RequestMapping(value = { "outputPicture" })
	public void outputPicture(HttpServletResponse res, String id){
		OutputStream out = null;
        try {
        	byte[] is = fileService.doDownLoadFile(id);
        	out = res.getOutputStream();
            out.write(is);
		} catch (Exception e) {
			logger.error("数据流写入失败"+e.getMessage());
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception e2) {
				logger.error("输出流关闭失败"+e2.getMessage());
			}
		}
	}
    
	 /**
     * 当前航班书否存在
     * @param searchId
     * @return
     */
    @RequestMapping(value = "isExists")
    @ResponseBody
    public String isExists(String flightDate, String flightNumber) {
    	if(null == flightDate || "".equals(flightDate)) {
    		return "faile";
    	}
    	if(null == flightNumber || "".equals(flightNumber)) {
    		return "faile";
    	}
    	
    	List<Map<String,Object>> list = passengerCarOperRecordService.isExists(flightDate, flightNumber);
    	return list.size() > 0? "1" : "0";
    }
    
    /**
     * 打开异常窗口
     * @return
     */
    @RequestMapping(value = "openExceptionWin")
    public String openExceptionWin(Model model,String remarkId, String type) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("remarkId", remarkId);
    	map.put("type", type);
    	System.out.println(map);
    	List<String> idList = passengerCarOperRecordService.getFilePath(map);
    	model.addAttribute("type", type);
    	model.addAttribute("idList", idList);
    	return "prss/passengerCar/operationRecordException";
    }
    /**
     * 新增一行初始化操作员下拉框
     * @param searchId
     * @return
     */
    @RequestMapping(value = "getOperList")
    @ResponseBody
    public List<Map<String,Object>> getOperList() {
    	List<Map<String,Object>> list = passengerCarOperRecordService.getOperList();
    	return list;
    }
}
