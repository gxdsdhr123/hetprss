package com.neusoft.prss.telegraph.web;

import java.io.BufferedOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.Log;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.telegraph.entity.LocatePrint;
import com.neusoft.prss.telegraph.service.HistoryService;
import com.neusoft.prss.telegraph.service.TeleGraphTemplService;
import com.neusoft.prss.telegraph.service.TelegraphHistoryService;

@Controller
@RequestMapping(value = "${adminPath}/telegraph/history")
public class TelegraphHistoryController extends BaseController {

    @Resource
    private TelegraphHistoryService telegraphHistoryService;
    
    @Resource
    private LocatePrint locatePrint;
    
    @Resource
    private HistoryService tmHisService;
    
    @Resource
    private TeleGraphTemplService teleGraphTemplService;

    @RequestMapping(value = "list")
    public String list(HttpServletRequest request,HttpServletResponse response,Model model) {
        model.addAttribute("isHis", request.getParameter("isHis"));
        
        //航空公司列表
        List<Map<String, Object>> airlineList = telegraphHistoryService.loadAirline();
        model.addAttribute("airlineList", airlineList);
        
        //报文类型列表
        List<Map<String, Object>> telegraphTypeList = telegraphHistoryService.loadTelegraphType();
        model.addAttribute("telegraphTypeList", telegraphTypeList);
        
        String fltId = request.getParameter("fltId");
        model.addAttribute("fltId", fltId);
        return "prss/telegraph/telegraphhistory";
    }

    @RequestMapping(value = "historyList")
    @ResponseBody
    public Map<String,Object> historyList(int pageSize,int pageNumber,String mflightdate,String flightnumber,int isHis,
            String sortOrder,String sortName,Log log,HttpServletRequest request,
            HttpServletResponse response,Model model) {
        Map<String,Object> param = new HashMap<String,Object>();
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        String userId = UserUtils.getUser().getId();
        String unioncode = request.getParameter("unioncode");
        String sendorrecieve = request.getParameter("sendorrecieve");
        String airplane = request.getParameter("airplane");
        String status = request.getParameter("status");
        String isfavoriter = request.getParameter("isfavoriter");
        String telegraphType = request.getParameter("telegraphType");
        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        String loginName = UserUtils.getUser().getLoginName();
        String fltId = request.getParameter("fltId");
        if(!StringUtils.isBlank(airplane))
            airplane = airplane.replace(",", "','");
        param.put("loginName", loginName);
        param.put("begin", begin);
        param.put("end", end);
        param.put("mflightdate", mflightdate);
        param.put("flightnumber", flightnumber);
        param.put("userId", userId);
        param.put("sortOrder", sortOrder);
        param.put("sortName", sortName);
        param.put("unioncode", unioncode);//航空公司分组
        param.put("beginTime", beginTime);//发送时间开始
        param.put("endTime", endTime);//发送时间结束
        param.put("sendorrecieve", sendorrecieve);// 已收/已发
        param.put("airplane", airplane);//航空公司
        param.put("status", status);// 状态
        param.put("isfavoriter", isfavoriter);//标识
        param.put("telegraphType", telegraphType);//报文类型
        param.put("isHis", isHis);
        param.put("fltId", fltId);
        if(isHis == 2 || isHis ==3 || isHis == 4){
            return telegraphHistoryService.getListInfo(param);//1 历史  2 收件箱 3 发件箱 4 航班动态报文
        } else {
            return tmHisService.getListInfo(param);
        }

    }

    @RequestMapping(value = "dofavorite")
    @ResponseBody
    public JSONObject dofavorite(HttpServletRequest request, HttpServletResponse response,Model model) {
        Map<String,Object> param = new HashMap<String,Object>();
        JSONObject object = new JSONObject();
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        String sendorrecieve = request.getParameter("sendorrecieve");
        String userId = UserUtils.getUser().getId();
        param.put("id", id);
        param.put("sendorrecieve", "1".equals(sendorrecieve)?"已收":"已发");
        param.put("userId", userId);
        if("1".equals(type)){
            telegraphHistoryService.insertFavorite(param);
        } else {
            telegraphHistoryService.deleteFavorite(param);
        }
        
        return object;
    }
    
    @RequestMapping(value = "isread")
    @ResponseBody
    public JSONObject isread(HttpServletRequest request, HttpServletResponse response,Model model) {
        Map<String,Object> param = new HashMap<String,Object>();
        JSONObject object = new JSONObject();
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        String isHis = request.getParameter("isHis");
        param.put("id", id);
        param.put("type", type);
        if("1".equals(isHis)){
            tmHisService.isRead(param);
        } else {
            telegraphHistoryService.isRead(param);
        }
        return object;
    }
    
    @RequestMapping(value = "searchHisDetail")
    public String senderMessage(HttpServletRequest request,HttpServletResponse response,Model model) {
        String id = request.getParameter("id");
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", id);
        JSONObject vo = telegraphHistoryService.searchHisDetail(map);
        model.addAttribute("id", id);
        model.addAttribute("vo", vo);
        return "prss/telegraph/historydetail";
    }

    @RequestMapping(value = "printData")
    @ResponseBody
    public JSONObject printData(HttpServletRequest request,HttpServletResponse response) {
        Map<String,Object> param = new HashMap<String,Object>();
        String isHis = request.getParameter("isHis");
        String userId = UserUtils.getUser().getId();
        String unioncode = request.getParameter("unioncode");
        String sendorrecieve = request.getParameter("sendorrecieve");
        String airplane = request.getParameter("airplane");
        String status = request.getParameter("status");
        String isfavoriter = request.getParameter("isfavoriter");
        String telegraphType = request.getParameter("telegraphType");
        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        String mflightdate = request.getParameter("mflightdate");
        String flightnumber = request.getParameter("flightnumber");
        String ids = request.getParameter("ids");
        if(!StringUtils.isBlank(airplane))
            airplane = airplane.replace(",", "','");
        param.put("mflightdate", mflightdate);
        param.put("flightnumber", flightnumber);
        param.put("userId", userId);
        param.put("unioncode", unioncode);//航空公司分组
        param.put("beginTime", beginTime);//发送时间开始
        param.put("endTime", endTime);//发送时间结束
        param.put("sendorrecieve", sendorrecieve);// 已收/已发
        param.put("airplane", airplane);//航空公司
        param.put("status", status);// 状态
        param.put("isfavoriter", isfavoriter);//标识
        param.put("telegraphType", telegraphType);//报文类型
        param.put("ids", ids);
        JSONObject object = new JSONObject();
        try {
            List<Map<String,String>> list =  null;    
            param.put("isHis", isHis);
            if("1".equals(isHis) )
                list = tmHisService.getList(param); 
            else 
                list = telegraphHistoryService.getList(param) ;
            locatePrint.print(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
    
    @RequestMapping(value = "export")
    public void export(HttpServletRequest request,HttpServletResponse response) {
        Map<String,Object> param = new HashMap<String,Object>();
        String loginName = UserUtils.getUser().getLoginName();
        String userId = UserUtils.getUser().getId();
        String mflightdate = request.getParameter("mflightdate");
        String flightnumber = request.getParameter("flightnumber");
        String isHis = request.getParameter("isHis");
        String id = request.getParameter("id");
        param.put("loginName", loginName);
        param.put("mflightdate", mflightdate);
        param.put("flightnumber", flightnumber);
        param.put("userId", userId);
        param.put("ids", id);
        BufferedOutputStream buff = null;  
        ServletOutputStream outSTr = null; 
        try {
            String fileName = "报文" + DateUtils.getDate("yyyyMMddHHmmss") + ".txt";
            response.reset();
            //导出txt文件  
            response.setContentType("text/plain");     
            String agent = (String) request.getHeader("USER-AGENT");
            String downloadFileName = "";
            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
            } else {
                downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);

            
            List<Map<String,String>> list =  null;  
            param.put("isHis", isHis);  
            if("1".equals(isHis))
                list = tmHisService.getList(param); 
            else 
                list = telegraphHistoryService.getList(param) ;
            StringBuffer write = new StringBuffer();         
            String enter = "\r\n";                 
             outSTr = response.getOutputStream();  // 建立         
             buff = new BufferedOutputStream(outSTr);  
            //把内容写入文件  
            for (int i = 0; i < list.size(); i++) {   
                String text = list.get(i).get("TEXT");
                if(text.indexOf("\r\n")<0){
                    text = text.replaceAll("\\n", "\r\n");
                }
                write.append(text); 
                write.append(enter + enter);
            }   
             buff.write(write.toString().getBytes("UTF-8"));         
             buff.flush();         
             buff.close();         
        } catch (Exception e) {
            logger.error("报文导出失败" + e.getMessage());
        }finally {         
            try {         
                buff.close();         
                outSTr.close();         
            } catch (Exception e) {         
                e.printStackTrace();         
           }         
       }  
    }
    
  
    
    @RequestMapping(value = "toIsPlane")
    public String toIsPlane(HttpServletRequest request ,HttpServletResponse response,Model model){
        String id = request.getParameter("id");
        String isHis = request.getParameter("isHis");
        model.addAttribute("id", id);
        model.addAttribute("isHis", isHis);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String time = sdf.format(new Date());
        model.addAttribute("time", time);
        return  "prss/telegraph/flightList";
    }
    
    @RequestMapping(value = "getFlightList")
    @ResponseBody
    public JSONArray getFlightList(HttpServletRequest request,HttpServletResponse response,Model model) {
        Map<String,Object> param = new HashMap<String,Object>();
        String flightnumber = request.getParameter("flightnumber");
        String flightdate = request.getParameter("flightdate");
        String fiotype = request.getParameter("fiotype");

        param.put("flightdate", flightdate);
        param.put("flightnumber", flightnumber);
        param.put("fiotype", fiotype);
        return telegraphHistoryService.getFlightList(param);
    }
    
    @RequestMapping(value = "pigeonhole")
    @ResponseBody
    public JSONObject pigeonhole(HttpServletRequest request ,HttpServletResponse response ){
        String flightDate = request.getParameter("flightDate");
        String flightNumber = request.getParameter("flightNumber");
        String aircraftNumber = request.getParameter("aircraftNumber");
        String id = request.getParameter("id");
        String fltId = request.getParameter("fltId");
        String isHis = request.getParameter("isHis");
        Map<String,String> map = new HashMap<String,String>();
        map.put("flightDate", flightDate);
        map.put("flightNumber", flightNumber);
        map.put("aircraftNumber", aircraftNumber);
        map.put("id", id);
        map.put("fltId", fltId);
        JSONObject result = new JSONObject();
        try {
            if("2".equals(isHis) || "4".equals(isHis))
                telegraphHistoryService.pigeonhole(map);
            else if("1".equals(isHis))
                tmHisService.pigeonhole(map);
            result.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }
    
    @RequestMapping(value = "tosend")
    public String tosend(HttpServletRequest request ,HttpServletResponse response,Model model){
        JSONArray priorityList = teleGraphTemplService.getPriorityList();
        model.addAttribute("priorityList", priorityList);
        JSONArray telegraphTypeList = teleGraphTemplService.getTypeList();
        model.addAttribute("telegraphTypeList", telegraphTypeList);
        String id = request.getParameter("id");
        String isHis = request.getParameter("isHis");
        if(!StringUtils.isBlank(id)){
            Map<String,String> map = new HashMap<String,String>();
            map.put("id", id);
            map.put("isHis", isHis);
            JSONObject info = telegraphHistoryService.getInfo(map);
            model.addAttribute("info", info);
        }
        String sendAddress = Global.getConfig("sendAddress");
        model.addAttribute("sendAddress", sendAddress);
        model.addAttribute("id", id);
        model.addAttribute("isHis", isHis);
        return  "prss/telegraph/sendTelegraph";
    }
    
    @RequestMapping(value = "sendManual")
    @ResponseBody
    public JSONObject sendManual(HttpServletRequest request ,HttpServletResponse response ){
        String flightDate = request.getParameter("flightDate");
        String flightNumber = request.getParameter("flightNumber");
        String fiotype = request.getParameter("fiotype");
        String telegraphType = request.getParameter("telegraphType");
        String mtext = request.getParameter("mtext");
        String sita = request.getParameter("sita");
        String sendAddress = request.getParameter("sendAddress");
        String priority = request.getParameter("priority");
        String userId = UserUtils.getUser().getId();
        Map<String,String> map = new HashMap<String,String>();
        map.put("fltDate", flightDate);
        map.put("fltNumber", flightNumber);
        map.put("IOflag", fiotype);
        map.put("tgTypeID", telegraphType);
        map.put("tgText", mtext);
        map.put("receiveAddress", sita);
        map.put("sendAddress", sendAddress);
        map.put("priority", priority);
        map.put("userID", userId);
        
        JSONObject result = new JSONObject();
        try {
//            tmHisService
            result.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }
}
