package com.neusoft.prss.produce.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.produce.entity.BillZpqwbg;
import com.neusoft.prss.produce.entity.TimeInfo;
import com.neusoft.prss.produce.service.StationDutyService;

/**
 * 窄体装卸调度——站坪部装卸勤务单
 * @author xuhw
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/produce/narrowDuty")
public class StationDutyController extends BaseController{

	@Autowired
	private StationDutyService stationDutyService;
	
	/**
	 * 单据列表
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Model model,String nwType){
		model.addAttribute("nwType",nwType);
		return "prss/produce/narrowDutyList";
	}
	
	/**
	 * 新增单据
	 * @return
	 */
	@RequestMapping(value="addProduce")
	public String addProduce(Model model){
		model.addAttribute("operatorList", getOperatorList());
		return "prss/produce/narrowDutyForm";
	}
	
	/**
	 * 修改单据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="editProduce")
	public String editProduce(Model model,Integer id){
		model.addAttribute("operatorList", getOperatorList());
		model.addAttribute("bill", stationDutyService.getBillById(id));
		
		return "prss/produce/narrowDutyForm";
	}
	
	private List<Map<String, String>> getOperatorList(){
		String officeId = UserUtils.getUser().getOffice().getId();
		return stationDutyService.getOperatorList(officeId);
	}
	
	/**
	 * 删除单据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="delProduce")
	@ResponseBody
	public ResponseVO<String> delProduce(Integer id){
		try {
			stationDutyService.delProduce(id);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<String>exception();
		}
		return ResponseVO.<String>build();
	}
	
	/**
	 * 列表数据
	 * @param offset
	 * @param limit
	 * @param searchText
	 * @return
	 */
	@RequestMapping(value="getData")
	@ResponseBody
	public PageBean<BillZpqwbg> getData(Integer offset,Integer limit,String searchText,String nwType){
		PageBean<BillZpqwbg> pageBean = new PageBean<BillZpqwbg>();
		try {
			pageBean = stationDutyService.getData(offset,limit,searchText,nwType);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return pageBean;
	}
	
	/**
	 * 查询航班信息
	 * @param inout
	 * @param flightNumber
	 * @return
	 */
	@RequestMapping(value="getFlightInfo")
	@ResponseBody
	public ResponseVO<FltInfo> getFlightInfo(String inout,String flightNumber, String flightDate){
		FltInfo fltInfo = new FltInfo();
		try {
			fltInfo = stationDutyService.getFlightInfo(inout,flightNumber,flightDate);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<FltInfo>exception();
		}
		return ResponseVO.<FltInfo>build().setData(fltInfo);
	}
	
	/**
	 * 查询各种时间
	 * @param inout
	 * @param flightNumber
	 * @return
	 */
	@RequestMapping(value="getTimeInfo")
	@ResponseBody
	public ResponseVO<TimeInfo> getTimeInfo(String inout,String flightNumber, String flightDate){
		TimeInfo timeInfo = new TimeInfo();
		try {
			timeInfo = stationDutyService.getTimeInfo(inout,flightNumber,flightDate);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<TimeInfo>exception();
		}
		return ResponseVO.<TimeInfo>build().setData(timeInfo);
	}
	
	/**
	 * 保存
	 * @param billZpqwbg
	 * @return
	 */
	@RequestMapping(value="saveBill")
	@ResponseBody
	public ResponseVO<String> saveBill(BillZpqwbg billZpqwbg){
		try {
			return stationDutyService.saveBill(billZpqwbg);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<String>exception();
		}
	}
	
	/**
	 * 查询组员
	 * @param billZpqwbg
	 * @return
	 */
	@RequestMapping(value="getMembers")
	@ResponseBody
	public ResponseVO<String> getMembers(String id){
		String members = "";
		try {
			members = stationDutyService.getMembers(id);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<String>exception();
		}
		return ResponseVO.<String>build().setData(members);
	}
	
	
}
