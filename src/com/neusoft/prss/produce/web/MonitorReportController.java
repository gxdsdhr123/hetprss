package com.neusoft.prss.produce.web;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.common.util.ExportWordUtils;
import com.neusoft.prss.produce.entity.MonitorReportEntity;
import com.neusoft.prss.produce.service.MonitorReportService;

/**
 * 航班监控报告
 * @author xuhw
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/produce/monitor")
public class MonitorReportController extends BaseController{

	@Autowired
	private MonitorReportService monitorReportService;
	
	/**
	 * 报表列表
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		model.addAttribute("restypes", JSON.toJSONString(monitorReportService.getOfficeRestype()));
		return "prss/produce/monitorReportList";
	}
	
	/**
	 * 修改报表
	 * @param id
	 * @return
	 */
	@RequestMapping(value="edit")
	public String edit(Model model,Integer id){
		model.addAttribute("entity", monitorReportService.getDataById(id));
		
		return "prss/produce/monitorReportForm";
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
	public PageBean<MonitorReportEntity> getData(Integer offset,Integer limit,String searchText,String startDate, String endDate){
		PageBean<MonitorReportEntity> pageBean = new PageBean<MonitorReportEntity>();
		try {
			pageBean = monitorReportService.getData(offset,limit,searchText,startDate,endDate);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return pageBean;
	}
	
	/**
	 * 获取是否已有记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value="getReport")
	@ResponseBody
	public ResponseVO<Integer> getReport(String statDay, String flightNumber){
		try {
			return monitorReportService.getReport(statDay,flightNumber);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<Integer>exception();
		}
	}
	
	/**
	 * 生成报表
	 * @param id
	 * @return
	 */
	@RequestMapping(value="create")
	@ResponseBody
	public ResponseVO<Integer> create(String statDay, String flightNumber, String jobTypes){
		try {
			return monitorReportService.createData(statDay,flightNumber,jobTypes);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<Integer>exception();
		}
	}
	
	/**
	 * 修改报表
	 * @param id
	 * @return
	 */
	@RequestMapping(value="update")
	@ResponseBody
	public ResponseVO<String> update(MonitorReportEntity data){
		try {
			monitorReportService.updateData(data);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<String>exception();
		}
		return ResponseVO.<String>build();
	}
	
	/**
	 * 删除报表
	 * @param id
	 * @return
	 */
	@RequestMapping(value="delete")
	@ResponseBody
	public ResponseVO<String> delete(Integer id){
		try {
			monitorReportService.delData(id);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ResponseVO.<String>exception();
		}
		return ResponseVO.<String>build();
	}
	
	/**
	 * 打印报表
	 * @param id
	 * @return
	 */
	@RequestMapping(value="print")
	public void print(HttpServletRequest request,HttpServletResponse response,Integer id){
		OutputStream out = null;
        try {
        	// 获取数据
        	MonitorReportEntity entity = monitorReportService.getDataById(id);
        	Map<String, String> params = ExportWordUtils.objToMap(entity);
        	String wiPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()).getParentFile().getPath();
        	String wordTmpPath = wiPath + "/template/monitorReport.docx";
        	System.out.println("word url:" + wordTmpPath);
//        	logger.error("word url:" + wordTmpPath);
        	String fileName = "航班监控报告" + DateUtils.getDate("yyyyMMddHHmmss") + ".docx";
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
        	ExportWordUtils.changWord(wordTmpPath, params, null,out);
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
