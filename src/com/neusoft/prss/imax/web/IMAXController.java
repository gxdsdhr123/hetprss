package com.neusoft.prss.imax.web;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.imax.bean.IllegalBean;
import com.neusoft.prss.imax.bean.ImaxIndexBean;
import com.neusoft.prss.imax.bean.ImaxResourceBean;
import com.neusoft.prss.imax.bean.ImaxRun1Bean;
import com.neusoft.prss.imax.bean.ImaxRun2Bean;
import com.neusoft.prss.imax.bean.MonitorBean;
import com.neusoft.prss.imax.service.IMAXService;

@Controller
@RequestMapping("${imaxPath}")
public class IMAXController extends BaseController{

	@Resource(name="imaxService")
	private IMAXService imaxService;
	
	@RequestMapping("")
	public String index(){
		
		return "prss/imax/index";
	}
	
	@RequestMapping("{path}")
	public String getPage(@PathVariable("path") String path,Model model){
		switch (path) {
			case "resource1":
				model.addAttribute("depList", imaxService.getResource1List());
				break;
			case "resource2":
				model.addAttribute("depList", imaxService.getResource2List());
				break;
			case "illegal":
				model.addAttribute("depList",imaxService.getIllegalDept());
				model.addAttribute("nowMonth",DateUtils.formatDate(new Date(), "yyyyMM"));
				break;
		}
		return "prss/imax/" + path;
	}
	
	/**
	 * 大屏首页
	 * @return
	 */
	@RequestMapping("getIndexData")
	@ResponseBody
	public ResponseVO<ImaxIndexBean> getIndexData(){
		ImaxIndexBean bean = new ImaxIndexBean();
		try {
			bean = imaxService.getIndexData();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<ImaxIndexBean>exception();
		}
		return ResponseVO.<ImaxIndexBean>build().setData(bean);
	}
	
	/**
	 * 航班正常性分析
	 * @return
	 */
	@RequestMapping("getRun1Data")
	@ResponseBody
	public ResponseVO<ImaxRun1Bean> getRun1Data(){
		ImaxRun1Bean bean = new ImaxRun1Bean();
		try {
			bean = imaxService.getRun1Data();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<ImaxRun1Bean>exception();
		}
		return ResponseVO.<ImaxRun1Bean>build().setData(bean);
	}
	
	/**
	 * 航班运行情况
	 * @return
	 */
	@RequestMapping("getRun2Data")
	@ResponseBody
	public ResponseVO<ImaxRun2Bean> getRun2Data(){
		ImaxRun2Bean bean = new ImaxRun2Bean();
		try {
			bean = imaxService.getRun2Data();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<ImaxRun2Bean>exception();
		}
		return ResponseVO.<ImaxRun2Bean>build().setData(bean);
	}
	
	/**
	 * 人员保障情况
	 * @return
	 */
	@RequestMapping("getResource1Data")
	@ResponseBody
	public ResponseVO<ImaxResourceBean> getResource1Data(String depId){
		ImaxResourceBean bean = new ImaxResourceBean();
		try {
			bean = imaxService.getResource1Data(depId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<ImaxResourceBean>exception();
		}
		return ResponseVO.<ImaxResourceBean>build().setData(bean);
	}
	
	/**
	 * 设备资源情况
	 * @return
	 */
	@RequestMapping("getResource2Data")
	@ResponseBody
	public ResponseVO<ImaxResourceBean> getResource2Data(String depId){
		ImaxResourceBean bean = new ImaxResourceBean();
		try {
			bean = imaxService.getResource2Data(depId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<ImaxResourceBean>exception();
		}
		return ResponseVO.<ImaxResourceBean>build().setData(bean);
	}
	
	/**
	 * 部门违规情况
	 * @return
	 */
	@RequestMapping("getIllegalData")
	@ResponseBody
	public ResponseVO<IllegalBean> getIllegalData(String date , String officeId, String targetDate){
		IllegalBean bean = new IllegalBean();
		try {
			bean = imaxService.getIllegalData(date,officeId,targetDate);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<IllegalBean>exception();
		}
		return ResponseVO.<IllegalBean>build().setData(bean);
	}
	
	/**
	 * 航班保障标准
	 * @return
	 */
	@RequestMapping("getMonitorData")
	@ResponseBody
	public ResponseVO<MonitorBean> getMonitorData(String inOut){
		MonitorBean bean = new MonitorBean();
		try {
			bean = imaxService.getMonitorData(inOut);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseVO.<MonitorBean>exception();
		}
		return ResponseVO.<MonitorBean>build().setData(bean);
	}
}
