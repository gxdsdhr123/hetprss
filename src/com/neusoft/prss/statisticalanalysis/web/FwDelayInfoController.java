package com.neusoft.prss.statisticalanalysis.web;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.Reflections;
import com.neusoft.framework.common.utils.excel.ExportExcel;
import com.neusoft.framework.common.utils.excel.annotation.ExcelField;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.User;
import com.neusoft.framework.modules.sys.utils.DictUtils;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.statisticalanalysis.entity.FwDelayInfo;
import com.neusoft.prss.statisticalanalysis.service.FwDelayInfoService;

/**
 * 旅客服务航延信息统计
 * @author xuhw
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticalanalysis/fwDelayInfo")
public class FwDelayInfoController extends BaseController {

	@Autowired
	private FwDelayInfoService delayInfoService;
	
	@RequestMapping(value = {"list",""})
	public String list(Model model){
		return "prss/statisticalanalysis/fwDelayInfo";
	}
	
	
	@RequestMapping(value = "getData")
	@ResponseBody
	public PageBean<FwDelayInfo> getDataList(Integer pageNumber, Integer pageSize,
			String startDate, String endDate, String airline){
		airline = StringEscapeUtils.unescapeJava(airline.replaceAll("%", "\\\\"));
		PageBean<FwDelayInfo> pageBean = new PageBean<FwDelayInfo>();
		pageBean.setTotal(delayInfoService.getDataListCount(startDate, endDate, airline));
		pageBean.setRows(delayInfoService.getDataList(pageNumber, pageSize, startDate, endDate, airline));
		return pageBean;
	}
	
	/**
	 * 打印
	 * @param request
	 * @param response
	 * @param startDate
	 * @param endDate
	 * @param airline
	 * @throws URISyntaxException
	 */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,
    		String startDate, String endDate, String airline) throws Exception {
    	try {
    		// 获取模板路径
    		String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
    		String tempPath = wiPath + "/template/tmp_fw_delay.xlsx";
    		File tmpFile = new File(tempPath);
    		// 获取数据列表
    		List<FwDelayInfo> datalist = delayInfoService.getAllDataList(startDate, endDate, airline);
			ExportExcel excel = new ExportExcel(tmpFile, 2, 1);
			
			int rownum = 0;
			for(FwDelayInfo delay : datalist){
				rownum++;
				int colunm = 0;
				Row row = excel.addRow();
					
				// 手写列
				excel.addCell(row, colunm++, rownum);
				excel.addCell(row, colunm++, delay.getAirlines());
				excel.addCell(row, colunm++, delay.getArrangeTypeText());
				excel.addCell(row, colunm++, delay.geteBreakfast());
				excel.addCell(row, colunm++, delay.geteLunch());
				excel.addCell(row, colunm++, delay.geteDinner());
				excel.addCell(row, colunm++, delay.geteAccommodation());
				excel.addCell(row, colunm++, delay.geteTraffic());
				excel.addCell(row, colunm++, delay.geteDrinks());
				excel.addCell(row, colunm++, delay.geteNightSnack());
				excel.addCell(row, colunm++, delay.getaBreakfast());
				excel.addCell(row, colunm++, delay.getaLunch());
				excel.addCell(row, colunm++, delay.getaDinner());
				excel.addCell(row, colunm++, delay.getaAccommodation());
				excel.addCell(row, colunm++, delay.getaTraffic());
				excel.addCell(row, colunm++, delay.getaDrinks());
				excel.addCell(row, colunm++, delay.getaNightSnack());
				
			};
			
			String fileName = "旅客服务航延信息统计_" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			excel.write(response, fileName).dispose();
		} catch (Exception e) {
			logger.error("旅客服务航延信息统计。",e);
			throw e;
		}
    }
}
