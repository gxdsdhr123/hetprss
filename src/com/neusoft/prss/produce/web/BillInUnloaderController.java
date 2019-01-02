package com.neusoft.prss.produce.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.produce.service.BillInUnloaderService;

/**
 * 进港卸机记录单
 * @author pei.g
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/produce/inUnloaderBill")
public class BillInUnloaderController extends BaseController{
	
	@Autowired
    private BillInUnloaderService billInUnloaderService;
	
	@RequestMapping(value="list")
	public String List(Model model){
		String today = new SimpleDateFormat( "yyyyMMdd").format(new Date());
		model.addAttribute("fltDate", today);
		return "prss/produce/billInUnloaderList";
	}
	//表格数据
	@RequestMapping(value = "data")
    @ResponseBody
    public Map<String,Object> GetData( int pageSize,int pageNumber,String operator,String fltDate,String fltNum,
            HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("fltDate", fltDate);
        param.put("fltNum", fltNum);
        param.put("operator", operator);
        return billInUnloaderService.getDataList(param);
    }
	//查看页面
	@RequestMapping(value="openSub")
	public String OpenWin(Model model,String fltid){
		List<Map<String, Object>> detailMap = billInUnloaderService.getDataById(fltid);
		List<Map<String, Object>> detailXL = billInUnloaderService.getXIData(fltid);
		List<Map<String, Object>> detailHY = billInUnloaderService.getHYData(fltid);
		model.addAttribute("detailMap", detailMap);
		model.addAttribute("detailXL", detailXL);
		model.addAttribute("detailHY", detailHY);		
		return "prss/produce/billInUnloaderForm";
	}
}
