package com.neusoft.prss.IncomingMailReceipt.web;

import java.io.OutputStream;
import java.util.ArrayList;
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
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.IncomingMailReceipt.dao.HandoverOutBillDao;
import com.neusoft.prss.IncomingMailReceipt.entity.HandoverBill;
import com.neusoft.prss.IncomingMailReceipt.service.HandoverBillService;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.service.LargePieceOfPlumService;
/**
 * 进出港邮货交接单
 * @author lwg
 * @date 2017/12/30
 */
@Controller
@RequestMapping("${adminPath}/handoverBill/bill")
public class HandoverBillController extends BaseController {
	
	@Autowired
	private HandoverBillService handoverBillService;
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 跳转港邮货交接单
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list/{param}")
	public String list(Model model, @PathVariable("param") String param) {
		model.addAttribute("sign", param);
		return "prss/handoverBill/handoverBill";
	}
	
    /**
     * 
     *Discription:得到展现表格的表头
     *@param request
     *@return:返回值意义
     */
    @RequestMapping(value = "getTableHeader/{param}/{sign}")
    @ResponseBody
    public String getTableHeader(HttpServletRequest request, @PathVariable("param") String param, @PathVariable("sign") String sign) {
    	boolean flag = true;
    	if("in".equals(sign)) {
    		flag = true;
    	} else if("out".equals(sign)) {
    		flag = false;
    	}
    	List<Map<String, String>> headerList = null;
    	List<Map<String, String>> selectList = null;
    	Map<String, Object> map = new HashMap<String, Object>();
    	if("main".equals(param)) { // 主页面请求表格头部
    		if(flag) { // 进港邮货    			
    			return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);
    		} else { // 出港邮货
    			return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);    			
    		}
    	} else { // 增加修改页面请求表格头部
    		// 表头
    		headerList = dealInHandoverBillHeader(flag);
    		// 内容区域下拉选
    		selectList = dealInHandoverBillSelect(flag);
    		List<Map<String, Object>> list = dealCarList(handoverBillService.getReceiverList());
    		// 表格头部
    		map.put("headerList", headerList);
    		// 内容下拉
    		map.put("selectList", selectList);
    		// 货运司机下拉选
    		map.put("receiverList", list);
    		
    		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
    	}
    }
    /**
     * 查询数据处理
     * @param list
     * @return
     */
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
     * 进港货运表头
     * @return
     */
    private List<Map<String, String>> dealInHandoverBillHeader(boolean flag) {
    	String[] codeStr = null;
    	String[] titleStr = null;
    	if(flag) { // 进港
    		codeStr = new String[]{"COLL_CODE", "INC_GOODS", "REMARK", "SIGNATORY", "SIGNATORYDATE", "RECEIVER", "RECEIVERDATE"};
    		titleStr = new String[]{"集装器/散装斗代码", "内含", "备注", "货运交接人", "时间", "货运司机", "时间"};
    	} else { // 出港
    		codeStr = new String[]{"COLL_CODE", "INC_GOODS", "WEIGHT", "REMARK", "A", "B", "RECEIVER", "RECEIVERDATE"};
    		titleStr = new String[]{"非机动设备号", "类型", "重量", "备注", "重复司机", "时间", "运输司机", "时间"};    		
    	}
    	List<Map<String, String>> resultList = new ArrayList<>();
    	Map<String, String> map;
    	for(int i = 0; i < codeStr.length; i++) {
    		map = new HashMap<String, String>();
    		map.put("CODE", codeStr[i]);
    		map.put("TITLE", titleStr[i]);
    		resultList.add(map);
    	}
    	
    	return resultList;
    }
    /**
     * 进港货运下拉内容
     * @return
     */
    private List<Map<String, String>> dealInHandoverBillSelect(boolean flag) {
    	String[] codeStr = null;
    	if(flag) { // 进港
    		codeStr = new String[]{"C", "M", "C/M"};
    	} else { // 出港
    		codeStr = new String[]{"箱", "板", "斗"};    		
    	}
    	List<Map<String, String>> resultList = new ArrayList<>();
    	Map<String, String> map;
    	for(int i = 0; i < codeStr.length; i++) {
    		map = new HashMap<String, String>();
    		map.put("value", codeStr[i]);
    		map.put("text", codeStr[i]);
    		resultList.add(map);
    	}
    	return resultList;
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
    public String getGridData(@PathVariable("param") String param, String pageNumber, String pageSize, String pageSign, String searchId, String searchText) {
    	if(null == pageNumber) {
    		pageNumber = "1";
    	}
    	if(null == pageSize) {
    		pageSize = "10";
    	}
    	if(null == param || "".equals(param)) {
    		return JSON.toJSONString(null, SerializerFeature.WriteMapNullValue);
    	}
    	if(null == pageSign || "".equals(pageSign)) {
    		return JSON.toJSONString(null, SerializerFeature.WriteMapNullValue);    		
    	}
    	int begin = (Integer.valueOf(pageNumber) - 1) * Integer.valueOf(pageSize) + 1;
        int end = Integer.valueOf(pageSize) + begin - 1;
        
    	if("".equals(searchId)) {
    		return JSON.toJSONString("", SerializerFeature.WriteMapNullValue);
    	}
    	Map<String, Object> inData = handoverBillService.getData(begin, end, pageSign, searchId, param, searchText);
    	if(null != inData.get("detailList")) { // 数据详情
    		return JSON.toJSONString(inData.get("detailList"), SerializerFeature.WriteMapNullValue);
    	} else { // 主页面数据
    		return JSON.toJSONString(inData, SerializerFeature.WriteMapNullValue);
    	}

    }

    /**
     * 跳转总控
     * @param searchTime
     * @param param
     * @param sign
     * @param searchId
     * @param pdate
     * @param model
     * @return
     */
    @RequestMapping(value = "pageJump/{param}/{sign}")
    public String pageJump(String searchTime, @PathVariable("param") String param, @PathVariable("sign") String sign, String searchId, String pdate, Model model) {
    	model.addAttribute("sign", sign);
		if("add".equals(param)) { 
			// 获取查理列表
			model.addAttribute("operList", handoverBillService.getCharlieList());
			return "prss/handoverBill/addHandoverBill";
		} else if("modify".equals(param)) {
			model.addAttribute("paramModify", param);
			// 获取查理列表
			model.addAttribute("operList", handoverBillService.getCharlieList());
			// 获取表单数据
			List<Map<String, Object>> list = handoverBillService.getInFormData(searchId, sign);
			dealHandoverBillData(model, list);
			return "prss/handoverBill/addHandoverBill";
		}
    		
    	
    	return pdate;
    	
    }
	/**
     * 处理页面加载信息
     * @param model
     * @param list
     */
    private void dealHandoverBillData(Model model, List<Map<String, Object>> list) {
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
    public String getAirFligthInfo(String flightDate, String flightNumber, String sign) {
    	if(null == flightDate || "".equals(flightDate)) {
    		return "";
    	}
    	if(null == flightNumber || "".equals(flightNumber)) {
    		return "";
    	}
    	if(null == sign || "".equals(sign)) {
    		return "";
    	}
    	
    	return JSON.toJSONString(handoverBillService.getAirFligthInfo(flightDate, flightNumber, sign), SerializerFeature.WriteMapNullValue);
    	
    }
    
    /**
     * 新增数据
     * @param param
     * @param largePieceOfPlum
     * @return
     */
    @RequestMapping(value = "add/{param}")
    @ResponseBody
    public String add(@PathVariable("param") String param, HandoverBill handoverBill) {
    	if("".equals(param) || null == param) {
    		return "faile";
    	}
    	if(!param.equals(handoverBill.getSign())) {
    		return "faile";
    	}
      	String data = StringEscapeUtils.unescapeHtml4(handoverBill.getData());
    	handoverBill.setData("");
    	return handoverBillService.save(handoverBill, data);
	
    }
    
    /**
     * 修改数据
     * @param param
     * @param handoverBill
     * @return
     */
    @RequestMapping(value = "modify/{param}")
    @ResponseBody
    public String modify(@PathVariable("param") String param, HandoverBill handoverBill) {
    	if("".equals(param) || null == param) {
    		return "faile";
    	}
    	if(!param.equals(handoverBill.getSign())) {
    		return "faile";
    	}
    	String data = StringEscapeUtils.unescapeHtml4(handoverBill.getData());
    	handoverBill.setData("");
    	return handoverBillService.modify(handoverBill, data);
    	
    }
    /**
     * 删除数据
     * @param param
     * @param handoverBill
     * @return
     */
    @RequestMapping(value = "del/{param}")
    @ResponseBody
    public String del(@PathVariable("param") String param, String searchId) {
    	if(null == searchId || "".equals(searchId)) {
    		return "faile";
    	}
    	if(null == param || "".equals(param.trim())) {
    		return "faile";    		
    	}
    	
    	return handoverBillService.del(searchId, param);
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
    @RequestMapping(value = "isExists/{sign}")
    @ResponseBody
    public String isExists(String flightDate, String flightNumber, @PathVariable("sign") String sign) {
    	if(null == flightDate || "".equals(flightDate)) {
    		return "faile";
    	}
    	if(null == flightNumber || "".equals(flightNumber)) {
    		return "faile";
    	}
    	if(null == sign || "".equals(sign)) {
    		return "faile";
    	}
    	List<Map<String,Object>> list = handoverBillService.isExists(flightDate, flightNumber, sign);
    	return list.size() > 0? "1" : "0";
    }
}
