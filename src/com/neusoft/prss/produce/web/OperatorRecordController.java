package com.neusoft.prss.produce.web;

import java.util.HashMap;
import java.util.Map;
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
import com.alibaba.fastjson.JSONPath;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.produce.entity.OperatorExportExcel;
import com.neusoft.prss.produce.service.OperatorRecordService;

@Controller
@RequestMapping(value = "${adminPath}/produce/operator")
public class OperatorRecordController extends BaseController {
	
	@Autowired
	private OperatorRecordService operatorRecordService;
	
	@Autowired
	private com.neusoft.prss.operatorrecord.service.OperatorRecordService operatorRecordServiceHis;
	
	@RequestMapping(value = "list")
    public String list(Model model,HttpServletRequest request) {
	    String reskind = request.getParameter("reskind");
	    model.addAttribute("reskind", reskind);
        return "prss/produce/operatorRecordList";
    }
	
	@RequestMapping(value = "data")
    @ResponseBody
    public Map<String,Object> data(
            int pageSize,int pageNumber,String operatorName,String beginTime,String endTime,String innerNumber,String reskind,
            HttpServletRequest request, HttpServletResponse response) {
	    operatorName = StringEscapeUtils.unescapeHtml4(operatorName);
        try {
            operatorName = java.net.URLDecoder.decode(operatorName,"utf-8");
        } catch (Exception e) {}

        Map<String, Object> param=new HashMap<String, Object>();
        int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("reskind", reskind);
        param.put("begin", begin);
        param.put("end", end);
        param.put("operatorName", operatorName);
        param.put("innerNumber", innerNumber);
        param.put("beginTime", beginTime);
        param.put("endTime", endTime);
        param.put("officeId",UserUtils.getUser().getOffice().getId());//部门ID
        param.put("userId", UserUtils.getUser().getId());//用户ID
        return operatorRecordService.getList(param);
    }
	@RequestMapping(value = "from")
    public String from(String id,String reskind,Model model) {
	    model.addAttribute("bondId", id);
	    model.addAttribute("reskind", reskind);
        return "prss/produce/operatorRecordInfo";
    }
	
    @RequestMapping(value = "info")
    @ResponseBody
    public JSONArray ruleList(String bondId,String insType,String reskind,
    		HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("bondId", bondId);
        param.put("ins_type", insType);
        param.put("reskind", reskind);
        return operatorRecordService.queryData(param);
    }
    
    @RequestMapping(value = "allInfo")
    @ResponseBody
    public JSONObject allInfo(String bondId,String reskind,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("bondId", bondId);
        param.put("reskind", reskind);
        
        param.put("ins_type", "5");
        result.put("cheArray", operatorRecordService.queryData(param));

        param.put("ins_type", "3");
        result.put("queryDataGZ", operatorRecordService.queryData(param));
        
        param.put("ins_type", "4");
        result.put("queryDataTP", operatorRecordService.queryData(param));
        return result;
    }
    
    @RequestMapping(value = "allInfoHis")
    @ResponseBody
    public JSONObject allInfoHis(String bondId,String reskind,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("bondId", bondId);
        param.put("reskind", reskind);
        param.put("ins_type", "99");
        result.put("list", operatorRecordServiceHis.queryData(param));//这里从历史服务中查询
        
        return result;
    }
    
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response) {
       String ids = request.getParameter("bondId");
       String reskind = request.getParameter("reskind");
       String name = "";
        try {
            switch (reskind) {
                case "JWBDC":
                    name = "员工操作记录";
                    break;
                case "ZPT1XLS":
                    name = "T1行李拖车员工操作记录";
                    break;
                case "ZPT2GJXLS":
                    name = "T2国际行李拖车员工操作记录";
                    break;
                case "ZPT3XLS":
                    name = "T3行李拖车员工操作记录";
                    break;
                case "ZPT2GNXLS":
                    name = "T2国内行李拖车员工操作记录";
                    break;
            }
            String fileName = name + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
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

            String excelTitle = name + DateUtils.getDate("yyyy年MM月dd日 E");
            OperatorExportExcel excel = new OperatorExportExcel(excelTitle);
            
            JSONObject result = new JSONObject();
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("bondId", ids);
            param.put("reskind", reskind);
            
            param.put("ins_type", "5");
            result.put("cheArray", operatorRecordService.queryData(param));

            param.put("ins_type", "3");
            result.put("queryDataGZ", operatorRecordService.queryData(param));
            
            param.put("ins_type", "4");
            result.put("queryDataTP", operatorRecordService.queryData(param));
            switch (reskind) {
                case "JWBDC":
                    param.put("ins_type", "99");
                    result.put("list", operatorRecordServiceHis.queryData(param));//这里从历史服务中查询
                break;
            }
            
            String[] arr = ids.split(",");
            for (int i = 0; i < arr.length; i++) {
                String id = arr[i];
//                JSONArray data1 = operatorRecordService.queryData(JSON.parseObject("{ins_type:1,bondId:"+id+",reskind:'"+reskind+"'}"));
                JSONArray data = (JSONArray) JSONPath.eval(result, "$.cheArray[BOUND_ID="+id+"]");
                
                JSONArray data1 = (JSONArray) JSONPath.eval(data, "[INS_TYPE in ('1')]");
                excel.setData(data1,1,i,reskind);
//                JSONArray data2 = operatorRecordService.queryData(JSON.parseObject("{ins_type:2,bondId:"+id+",reskind:'"+reskind+"'}"));
                
                JSONArray data2 = (JSONArray) JSONPath.eval(data, "[INS_TYPE in ('2')]");
                
                excel.setData(data2,2,i,reskind);
                switch (reskind) {
                    case "JWBDC":
//                        JSONArray list = operatorRecordService.queryData(JSON.parseObject("{ins_type:99,bondId:"+id+",reskind:'"+reskind+"'}"));
                        JSONArray list = (JSONArray) JSONPath.eval(result, "$.list[ID="+id+"]");
                        excel.setUpdate(list);
                    break;
                }
            }
            
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error(name + "导出失败" + e.getMessage());
        }
    }
    
    @RequestMapping(value = "downAtta")
    public String downAtta(Model model,String fileId,String type,HttpServletRequest request,HttpServletResponse response) {
        model.addAttribute("type", type);
        model.addAttribute("fileIds", fileId);
        return "prss/flightdynamic/exceptionalAtta";
    }
}
