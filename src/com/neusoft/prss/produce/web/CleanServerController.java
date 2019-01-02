package com.neusoft.prss.produce.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.entity.ExportExcel;
import com.neusoft.prss.produce.service.CleanServerService;

/**
 * 清洁服务单
 * @author yunwq
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/produce/cleanServerBill")
public class CleanServerController extends BaseController {

	@Autowired
    private CleanServerService cleanServerService;
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value="list")
	public String List(Model model){
		String today = new SimpleDateFormat( "yyyyMMdd").format(new Date());
		model.addAttribute("fltDate", today);
		ArrayList<HashMap<String,Object>> peopleList = cleanServerService.getPeople();
		model.addAttribute("peopleList", peopleList);
		return "prss/produce/cleanServerList";
	}
	
	/**
	 * 打开新增页面，并初始化人员select列表
	 * @return
	 */
	@RequestMapping(value="openAdd")
	public String OpenAdd(Model model){
		ArrayList<HashMap<String,Object>> peopleList = cleanServerService.getPeople();
		model.addAttribute("peopleList", peopleList);
		ArrayList<HashMap<String,Object>> airlineList = cleanServerService.getAirline();
		model.addAttribute("airlineList", airlineList);
		return "prss/produce/cleanServerForm";
	}
	
	/**
	 * 获取主页面列表数据
	 * @return
	 */
	@RequestMapping(value = "data")
    @ResponseBody
    public Map<String,Object> GetData(String operator,
            int pageSize,int pageNumber,String fltDate,String fltNum,
            HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
        param.put("end", end);
        param.put("fltDate", fltDate);
        param.put("fltNum", fltNum);
        param.put("operator", operator);
        return cleanServerService.getDataList(param);
    }
	
	/**
	 * 新增、修改页面保存
	 * @return
	 */
	@RequestMapping(value="doSave")
	public void Save(String hidId,String airline,String fltStatus,String flightDate,String flightNum,String AirNum,
			String atcType,String bTime,String eTime,String remark,String scj,String ljd,String bj,String sb,
			String xcq,String mj,String other,String cleaners){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("hidId", hidId);
		map.put("airline", airline);
		map.put("fltStatus", fltStatus);
		map.put("flightDate", flightDate);
		map.put("flightNum", flightNum);
		map.put("AirNum", AirNum);
		map.put("atcType", atcType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		map.put("remark", remark);
		map.put("other", other);
		map.put("cleaners", cleaners);
		String carryTools = "";
		if(scj != null){
			carryTools += "1,";
		}else if(ljd != null){
			carryTools += "2,";
		}else if(bj != null){
			carryTools += "3,";
		}else if(sb != null){
			carryTools += "4,";
		}else if(xcq != null){
			carryTools += "5,";
		}else if(mj != null){
			carryTools += "6";
		}
		map.put("carryTools", carryTools);
		String operator = UserUtils.getUser().getId();
		map.put("operator", operator);
		if(StringUtils.isEmpty(hidId)){
			cleanServerService.save(map);
		}else{
			cleanServerService.update(map);
		}
	}
	
	/**
	 * 删除一条数据
	 * @return
	 */
	@RequestMapping(value="delete")
	@ResponseBody
	public String delete(String id){
		String i = "0";
		try{
			cleanServerService.delete(id);
			i = "1";
		}catch(Exception e){
			System.out.println(e);
		}
		return i;
	}
	
	/**
	 * 打开修改页面
	 * @return
	 */
	@RequestMapping(value="openModify")
	public String OpenWin(Model model,String id){
		ArrayList<HashMap<String,Object>> peopleList = cleanServerService.getPeople();
		model.addAttribute("peopleList", peopleList);
		ArrayList<HashMap<String,Object>> airlineList = cleanServerService.getAirline();
		model.addAttribute("airlineList", airlineList);
		HashMap<String,Object> detailMap = cleanServerService.getDataById(id);
		model.addAttribute("detailMap", detailMap);
		return "prss/produce/cleanServerForm";
	}
	
	/**
	 * 打印报表
	 */
	@RequestMapping(value="print")
	public void printword(HttpServletRequest request,HttpServletResponse response,
			String id){
		if(id.endsWith(",")){
			id = id.substring(0,id.length()-1);
		}

    	//查询打印数据
    	List<HashMap<String, Object>> totalList = cleanServerService.getTotalData(id);
		System.out.println(totalList);

        try {
            String fileName = "清洁服务单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            setHeader(request,response,fileName);
            String excelTitle = "清洁服务单" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportExcel excel = new ExportExcel(excelTitle);
            excel.setQJFWDataList(totalList);
            List<byte[]> str = new ArrayList<byte[]>();
            for (HashMap<String, Object> hashMap : totalList) {
				String signatory = hashMap.get("SIGN")==null?"":hashMap.get("SIGN").toString();
				if(!StringUtils.isBlank(signatory)) {
					byte[] is = fileService.doDownLoadFile(signatory);
					if(is!=null){
						str.add(is);
					}
				}else{
					str.add(new byte[0]);
				}
			}
            excel.exportQJFWPic(str);
            excel.write(response);
            excel.dispose();
            return;
        } catch (Exception e) {
            logger.error("清洁服务单导出失败" + e.getMessage());
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
}
