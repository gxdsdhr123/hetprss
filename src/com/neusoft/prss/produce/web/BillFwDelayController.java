package com.neusoft.prss.produce.web;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.utils.excel.ExportExcel;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.produce.entity.DelayInfo;
import com.neusoft.prss.produce.service.BillFwDelayService;
import com.neusoft.prss.statisticalanalysis.entity.FwDelayInfo;

@Controller
@RequestMapping(value="${adminPath}/bill/fwDelay")
public class BillFwDelayController extends BaseController{

	@Autowired
	private BillFwDelayService delayService;
	
	/**
     * 航延信息录入
     * 
     * @param model
     * @param fltid
     * @return
     */
    @RequestMapping(value = "edit")
    public String delay(Model model,String fltid) {
        model.addAttribute("fltInfo", delayService.getDelayFltInfo(fltid));
        model.addAttribute("fltid",fltid);
        model.addAttribute("delay", delayService.getDelayInfo(fltid));
        model.addAttribute("operatorName",UserUtils.getUser().getName());
        return "prss/produce/addDelayInfo";
    }

    /**
     * 保存航延信息
     */
    @ResponseBody
    @RequestMapping(value = "saveDelay")
    public ResponseVO<String> saveDelay(String delayInfo) {
    	try {
			DelayInfo delay = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(delayInfo), DelayInfo.class);
			// 默认当前登录用户为操作人
			delay.setProducer(UserUtils.getUser().getId());
			delay.setUpdateOperator(UserUtils.getUser().getId());
			String msg = delayService.saveDelay(delay);
			if(!StringUtils.isEmpty(msg)){
				return ResponseVO.<String>error().setMsg(msg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<String>exception();
		}
		return ResponseVO.<String>build(); 
    }
    
    /**
     * 航延信息列表
     */
    @RequestMapping(value = "list")
    public String list(Model model) {
    	
    	return "prss/produce/fwDelayList";
    }
    
    /**
     * 获取数据
     * @param pageNumber
     * @param pageSize
     * @param startDate
     * @param endDate
     * @param airline
     * @return
     */
    @RequestMapping(value = "getData")
	@ResponseBody
	public PageBean<DelayInfo> getDataList(Integer pageNumber, Integer pageSize,
			String startDate, String endDate, String searchText){
    	searchText = StringEscapeUtils.unescapeJava(searchText.replaceAll("%", "\\\\"));
		PageBean<DelayInfo> pageBean = new PageBean<DelayInfo>();
		pageBean.setTotal(delayService.getDataListCount(startDate, endDate, searchText));
		pageBean.setRows(delayService.getDataList(pageNumber, pageSize, startDate, endDate, searchText));
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
    		String startDate, String endDate, String ids) throws Exception {
    	try {
    		// 获取模板路径
    		String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
    		String tempPath = wiPath + "/template/tmp_bill_fw_delay.xlsx";
    		File tmpFile = new File(tempPath);
    		// 获取数据列表
    		List<DelayInfo> datalist = delayService.getExportDataList(startDate, endDate, ids);
    		// 需要复制区域行数
    		int copyrows = 7;
			ExportExcel excel = new ExportExcel(tmpFile, 1, 1);
			// 复制开始行号
			int startRow = 3;
			// 复制结束行号
			int endRow = startRow + copyrows - 1;
			for(int i = 0; i < datalist.size() - 1; i++){
				// 复制行
				excel.copyRows(startRow, endRow , startRow + (i+1)*copyrows - 1);
			}
			// 遍历结果
			for(int i = 0; i < datalist.size(); i++){
				// 计算行开始/结束
				int sRow = startRow + i*copyrows;
				int eRow = sRow + copyrows - 1;
				// 取对应表格中的所有行
				List<Row> rows = excel.getRows(sRow, eRow);
				DelayInfo delay = datalist.get(i);
				
				// 向表格中填值
				setValToExcel(rows,delay,i+1);
			}
			
			String fileName = "旅客服务航延信息单据_" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			excel.write(response, fileName).dispose();
		} catch (Exception e) {
			logger.error("导出旅客服务航延信息单据。",e);
			throw e;
		}
    }
    
    /**
     * 向EXCEL表格中添值
     * @param rows
     * @param delay
     * @param index 
     */
    private void setValToExcel(List<Row> rows, DelayInfo delay, int index){
    	// 序号
    	rows.get(2).getCell(0).setCellValue(index);
    	// 日期
    	rows.get(2).getCell(1).setCellValue(delay.getFlightDate());
    	// 航班号
    	rows.get(3).getCell(2).setCellValue(delay.getFlightNumber());
    	// 机号
    	rows.get(5).getCell(2).setCellValue(delay.getAircraftNumber());
    	// 预计时间
    	String eTime = delay.geteTime();
    	if(eTime.length() > 4){
    		eTime = eTime.substring(eTime.length()-4, eTime.length());
    	}
    	rows.get(3).getCell(3).setCellValue(eTime);
    	// 实际时间
    	String aTime = delay.getaTime();
    	if(aTime.length() > 4){
    		aTime = aTime.substring(aTime.length()-4, aTime.length());
    	}
    	rows.get(5).getCell(3).setCellValue(aTime);
    	// 航程
    	rows.get(3).getCell(4).setCellValue(delay.getRoute());
    	// 延误原因
    	rows.get(5).getCell(4).setCellValue(delay.getDelayReason());
    	
    	// 预计早餐（旅客当日）
    	rows.get(2).getCell(7).setCellValue(delay.getDetails().get(0).geteBreakfast()==null?"":delay.getDetails().get(0).geteBreakfast().toString());
    	// 预计午餐（旅客当日）
    	rows.get(2).getCell(8).setCellValue(delay.getDetails().get(0).geteLunch()==null?"":delay.getDetails().get(0).geteLunch().toString());
    	// 预计晚餐（旅客当日）
    	rows.get(2).getCell(9).setCellValue(delay.getDetails().get(0).geteDinner()==null?"":delay.getDetails().get(0).geteDinner().toString());
    	// 预计住宿（旅客当日）
    	rows.get(2).getCell(10).setCellValue(delay.getDetails().get(0).geteAccommodation()==null?"":delay.getDetails().get(0).geteAccommodation().toString());
    	// 预计交通（旅客当日）
    	rows.get(2).getCell(11).setCellValue(delay.getDetails().get(0).geteTraffic()==null?"":delay.getDetails().get(0).geteTraffic().toString());
    	// 预计饮料（旅客当日）
    	rows.get(2).getCell(12).setCellValue(delay.getDetails().get(0).geteDrinks()==null?"":delay.getDetails().get(0).geteDrinks().toString());
    	// 预计夜宵（旅客当日）
    	rows.get(2).getCell(13).setCellValue(delay.getDetails().get(0).geteNightSnack()==null?"":delay.getDetails().get(0).geteNightSnack().toString());
    	// 实际早餐（旅客当日）
    	rows.get(2).getCell(14).setCellValue(delay.getDetails().get(0).getaBreakfast()==null?"":delay.getDetails().get(0).getaBreakfast().toString());
    	// 实际午餐（旅客当日）
    	rows.get(2).getCell(15).setCellValue(delay.getDetails().get(0).getaLunch()==null?"":delay.getDetails().get(0).getaLunch().toString());
    	// 实际晚餐（旅客当日）
    	rows.get(2).getCell(16).setCellValue(delay.getDetails().get(0).getaDinner()==null?"":delay.getDetails().get(0).getaDinner().toString());
    	// 实际住宿（旅客当日）
    	rows.get(2).getCell(17).setCellValue(delay.getDetails().get(0).getaAccommodation()==null?"":delay.getDetails().get(0).getaAccommodation().toString());
    	// 实际交通（旅客当日）
    	rows.get(2).getCell(18).setCellValue(delay.getDetails().get(0).getaTraffic()==null?"":delay.getDetails().get(0).getaTraffic().toString());
    	// 实际饮料（旅客当日）
    	rows.get(2).getCell(19).setCellValue(delay.getDetails().get(0).getaDrinks()==null?"":delay.getDetails().get(0).getaDrinks().toString());
    	// 实际夜宵（旅客当日）
    	rows.get(2).getCell(20).setCellValue(delay.getDetails().get(0).getaNightSnack()==null?"":delay.getDetails().get(0).getaNightSnack().toString());
    	
    	// 预计早餐（旅客次日）
    	rows.get(3).getCell(7).setCellValue(delay.getDetails().get(1).geteBreakfast()==null?"":delay.getDetails().get(1).geteBreakfast().toString());
    	// 预计午餐（旅客次日）
    	rows.get(3).getCell(8).setCellValue(delay.getDetails().get(1).geteLunch()==null?"":delay.getDetails().get(1).geteLunch().toString());
    	// 预计晚餐（旅客次日）
    	rows.get(3).getCell(9).setCellValue(delay.getDetails().get(1).geteDinner()==null?"":delay.getDetails().get(1).geteDinner().toString());
    	// 预计住宿（旅客次日）
    	rows.get(3).getCell(10).setCellValue(delay.getDetails().get(1).geteAccommodation()==null?"":delay.getDetails().get(1).geteAccommodation().toString());
    	// 预计交通（旅客次日）
    	rows.get(3).getCell(11).setCellValue(delay.getDetails().get(1).geteTraffic()==null?"":delay.getDetails().get(1).geteTraffic().toString());
    	// 预计饮料（旅客次日）
    	rows.get(3).getCell(12).setCellValue(delay.getDetails().get(1).geteDrinks()==null?"":delay.getDetails().get(1).geteDrinks().toString());
    	// 预计夜宵（旅客次日）
    	rows.get(3).getCell(13).setCellValue(delay.getDetails().get(1).geteNightSnack()==null?"":delay.getDetails().get(1).geteNightSnack().toString());
    	// 实际早餐（旅客次日）
    	rows.get(3).getCell(14).setCellValue(delay.getDetails().get(1).getaBreakfast()==null?"":delay.getDetails().get(1).getaBreakfast().toString());
    	// 实际午餐（旅客次日）
    	rows.get(3).getCell(15).setCellValue(delay.getDetails().get(1).getaLunch()==null?"":delay.getDetails().get(1).getaLunch().toString());
    	// 实际晚餐（旅客次日）
    	rows.get(3).getCell(16).setCellValue(delay.getDetails().get(1).getaDinner()==null?"":delay.getDetails().get(1).getaDinner().toString());
    	// 实际住宿（旅客次日）
    	rows.get(3).getCell(17).setCellValue(delay.getDetails().get(1).getaAccommodation()==null?"":delay.getDetails().get(1).getaAccommodation().toString());
    	// 实际交通（旅客次日）
    	rows.get(3).getCell(18).setCellValue(delay.getDetails().get(1).getaTraffic()==null?"":delay.getDetails().get(1).getaTraffic().toString());
    	// 实际饮料（旅客次日）
    	rows.get(3).getCell(19).setCellValue(delay.getDetails().get(1).getaDrinks()==null?"":delay.getDetails().get(1).getaDrinks().toString());
    	// 实际夜宵（旅客次日）
    	rows.get(3).getCell(20).setCellValue(delay.getDetails().get(1).getaNightSnack()==null?"":delay.getDetails().get(1).getaNightSnack().toString());
    	
    	// 预计早餐（机组当日）
    	rows.get(4).getCell(7).setCellValue(delay.getDetails().get(2).geteBreakfast()==null?"":delay.getDetails().get(2).geteBreakfast().toString());
    	// 预计午餐（机组当日）
    	rows.get(4).getCell(8).setCellValue(delay.getDetails().get(2).geteLunch()==null?"":delay.getDetails().get(2).geteLunch().toString());
    	// 预计晚餐（机组当日）
    	rows.get(4).getCell(9).setCellValue(delay.getDetails().get(2).geteDinner()==null?"":delay.getDetails().get(2).geteDinner().toString());
    	// 预计住宿（机组当日）
    	rows.get(4).getCell(10).setCellValue(delay.getDetails().get(2).geteAccommodation()==null?"":delay.getDetails().get(2).geteAccommodation().toString());
    	// 预计交通（机组当日）
    	rows.get(4).getCell(11).setCellValue(delay.getDetails().get(2).geteTraffic()==null?"":delay.getDetails().get(2).geteTraffic().toString());
    	// 预计饮料（机组当日）
    	rows.get(4).getCell(12).setCellValue(delay.getDetails().get(2).geteDrinks()==null?"":delay.getDetails().get(2).geteDrinks().toString());
    	// 预计夜宵（机组当日）
    	rows.get(4).getCell(13).setCellValue(delay.getDetails().get(2).geteNightSnack()==null?"":delay.getDetails().get(2).geteNightSnack().toString());
    	// 实际早餐（机组当日）
    	rows.get(4).getCell(14).setCellValue(delay.getDetails().get(2).getaBreakfast()==null?"":delay.getDetails().get(2).getaBreakfast().toString());
    	// 实际午餐（机组当日）
    	rows.get(4).getCell(15).setCellValue(delay.getDetails().get(2).getaLunch()==null?"":delay.getDetails().get(2).getaLunch().toString());
    	// 实际晚餐（机组当日）
    	rows.get(4).getCell(16).setCellValue(delay.getDetails().get(2).getaDinner()==null?"":delay.getDetails().get(2).getaDinner().toString());
    	// 实际住宿（机组当日）
    	rows.get(4).getCell(17).setCellValue(delay.getDetails().get(2).getaAccommodation()==null?"":delay.getDetails().get(2).getaAccommodation().toString());
    	// 实际交通（机组当日）
    	rows.get(4).getCell(18).setCellValue(delay.getDetails().get(2).getaTraffic()==null?"":delay.getDetails().get(2).getaTraffic().toString());
    	// 实际饮料（机组当日）
    	rows.get(4).getCell(19).setCellValue(delay.getDetails().get(2).getaDrinks()==null?"":delay.getDetails().get(2).getaDrinks().toString());
    	// 实际夜宵（机组当日）
    	rows.get(4).getCell(20).setCellValue(delay.getDetails().get(2).getaNightSnack()==null?"":delay.getDetails().get(2).getaNightSnack().toString());
    	
    	// 预计早餐（机组次日）
    	rows.get(5).getCell(7).setCellValue(delay.getDetails().get(3).geteBreakfast()==null?"":delay.getDetails().get(3).geteBreakfast().toString());
    	// 预计午餐（机组次日）
    	rows.get(5).getCell(8).setCellValue(delay.getDetails().get(3).geteLunch()==null?"":delay.getDetails().get(3).geteLunch().toString());
    	// 预计晚餐（机组次日）
    	rows.get(5).getCell(9).setCellValue(delay.getDetails().get(3).geteDinner()==null?"":delay.getDetails().get(3).geteDinner().toString());
    	// 预计住宿（机组次日）
    	rows.get(5).getCell(10).setCellValue(delay.getDetails().get(3).geteAccommodation()==null?"":delay.getDetails().get(3).geteAccommodation().toString());
    	// 预计交通（机组次日）
    	rows.get(5).getCell(11).setCellValue(delay.getDetails().get(3).geteTraffic()==null?"":delay.getDetails().get(3).geteTraffic().toString());
    	// 预计饮料（机组次日）
    	rows.get(5).getCell(12).setCellValue(delay.getDetails().get(3).geteDrinks()==null?"":delay.getDetails().get(3).geteDrinks().toString());
    	// 预计夜宵（机组次日）
    	rows.get(5).getCell(13).setCellValue(delay.getDetails().get(3).geteNightSnack()==null?"":delay.getDetails().get(3).geteNightSnack().toString());
    	// 实际早餐（机组次日）
    	rows.get(5).getCell(14).setCellValue(delay.getDetails().get(3).getaBreakfast()==null?"":delay.getDetails().get(3).getaBreakfast().toString());
    	// 实际午餐（机组次日）
    	rows.get(5).getCell(15).setCellValue(delay.getDetails().get(3).getaLunch()==null?"":delay.getDetails().get(3).getaLunch().toString());
    	// 实际晚餐（机组次日）
    	rows.get(5).getCell(16).setCellValue(delay.getDetails().get(3).getaDinner()==null?"":delay.getDetails().get(3).getaDinner().toString());
    	// 实际住宿（机组次日）
    	rows.get(5).getCell(17).setCellValue(delay.getDetails().get(3).getaAccommodation()==null?"":delay.getDetails().get(3).getaAccommodation().toString());
    	// 实际交通（机组次日）
    	rows.get(5).getCell(18).setCellValue(delay.getDetails().get(3).getaTraffic()==null?"":delay.getDetails().get(3).getaTraffic().toString());
    	// 实际饮料（机组次日）
    	rows.get(5).getCell(19).setCellValue(delay.getDetails().get(3).getaDrinks()==null?"":delay.getDetails().get(3).getaDrinks().toString());
    	// 实际夜宵（机组次日）
    	rows.get(5).getCell(20).setCellValue(delay.getDetails().get(3).getaNightSnack()==null?"":delay.getDetails().get(3).getaNightSnack().toString());
    	
    	// 旅客酒店
    	rows.get(2).getCell(21).setCellValue(delay.getLkHotel());
    	// 机组酒店
    	rows.get(4).getCell(21).setCellValue(delay.getJzHotel());
    	// 领班
    	rows.get(2).getCell(22).setCellValue(delay.getProducer());
    	// 备注
    	rows.get(6).getCell(1).setCellValue("备注：" + delay.getNotes());
    }
}
