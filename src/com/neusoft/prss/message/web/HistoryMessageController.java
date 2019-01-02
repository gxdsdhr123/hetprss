package com.neusoft.prss.message.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.Log;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.flightdynamic.entity.ExportFDExcel;
import com.neusoft.prss.message.common.EchartsFactory;
import com.neusoft.prss.message.entity.HistoryMessageVO;
import com.neusoft.prss.message.service.HistoryMessageService;
import com.neusoft.prss.message.service.HistoryService;

@Controller
@RequestMapping(value = "${adminPath}/message/history")
public class HistoryMessageController extends BaseController {

    @Resource
    private HistoryMessageService historymessageService;

    @Resource
    private HistoryService mmHisService;

    /**
     * 
     * Discription:打开历史消息主页.
     * 
     * @param model
     * @return
     * @return:
     * @author:zhaol
     * @update:2017年9月8日 [变更描述]
     */
    @RequestMapping(value = "list")
    public String list(HttpServletRequest request,HttpServletResponse response,Model model) {
        String num = request.getParameter("num");
        String fltid = request.getParameter("fltid");
        model.addAttribute("num", num);
        model.addAttribute("fltid", fltid);
        return "prss/message/historyMessage";
    }

    /**
     * 
     * Discription:历史消息表格数据获取.
     * 
     * @param model
     * @return
     * @return:
     * @author:zhaol
     * @update:2017年9月8日 [变更描述]
     */
    @RequestMapping(value = "historyList")
    @ResponseBody
    public Map<String,Object> historyList(int pageSize,int pageNumber,String mflightdate,String flightnumber,
            String mtitle,String mtext,String sortOrder,String sortName,Log log,HttpServletRequest request,
            HttpServletResponse response,Model model) {
        String num = request.getParameter("num");
        String fltid = request.getParameter("fltid");
        Map<String,Object> param = new HashMap<String,Object>();
        int begin = (pageNumber - 1) * pageSize;
        int end = pageSize + begin;
        String userId = UserUtils.getUser().getId();

        try {
            mtext = java.net.URLDecoder.decode(mtext, "utf-8");
            mtitle = java.net.URLDecoder.decode(mtitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String loginName = UserUtils.getUser().getLoginName();
        param.put("loginName", loginName);
        param.put("begin", begin);
        param.put("end", end);
        param.put("mflightdate", mflightdate);
        param.put("flightnumber", flightnumber);
        param.put("mtitle", mtitle);
        param.put("mtext", mtext);
        param.put("userId", userId);
        param.put("sortOrder", sortOrder);
        param.put("sortName", sortName);
        param.put("fltid", fltid);
        List<String> roleIdList = UserUtils.getUser().getRoleIdList();
        String roleIds = StringUtils.join(roleIdList.toArray(), "','");
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("roleIds", roleIds);
        param.put("mtoType", "0");
        if ("0".equals(num)) {
            //已收
            param.put("receive", "receive");
        } else if ("1".equals(num)) {
            //已发、指令
            param.put("send", "send");
        } else if ("2".equals(num)) {
            return mmHisService.getListInfo(param);
            //		    param.put("history", "history");
            //历史纪录
        } else if ("3".equals(num)) {
            param.put("favorite", "favorite");
        } else if ("4".equals(num)) {
            //已收和已发
            param.put("rs", "rs");
        } else if ("64".equals(num)) {
            //动态历史已收和已发
            param.put("his", "his");
            return mmHisService.getListInfo(param);
        }
        return historymessageService.getListInfo(param);

    }

    /**
     * 
     * Discription:历史消息详情信息.
     * 
     * @param model
     * @return
     * @return:
     * @author:zhaol
     * @update:2017年9月8日 [变更描述]
     */
    @RequestMapping(value = "searchHisDetail")
    public String senderMessage(HttpServletRequest request,HttpServletResponse response,Model model) {
        String id = request.getParameter("id");
        String num = request.getParameter("num");
        Map<String,String> map = new HashMap<String,String>();
        map.put("id", id);
        if ("64".equals(num)) {
            map.put("num", "64");
        }
        HistoryMessageVO vo = null;
        List<HistoryMessageVO> list = null;
        if ("2".equals(num)) {
            vo = mmHisService.searchHisDetail(map);
            list = mmHisService.reciverDetail(map);
        } else {
            vo = mmHisService.searchHisDetail(map);
            list = mmHisService.reciverDetail(map);
        }
        model.addAttribute("id", id);
        model.addAttribute("vo", vo);
        model.addAttribute("list", list);
        return "prss/message/searchHisDetail";
    }

    /**
     * 
     * Discription:打印历史消息.
     * 
     * @param model
     * @return
     * @return:
     * @author:zhaol
     * @update:2017年9月8日 [变更描述]
     */
    @RequestMapping(value = "print")
    public void printData(HttpServletRequest request,HttpServletResponse response,String title,String mflightdate,
            String flightnumber,String sortOrder,String sortName,String mtitle,String mtext,String printnum) {
        title = StringEscapeUtils.unescapeHtml4(title);
        JSONArray titleArray = JSONArray.parseArray(title);

        Map<String,Object> param = new HashMap<String,Object>();
        String userId = UserUtils.getUser().getId();
        try {
            mtext = java.net.URLDecoder.decode(mtext, "utf-8");
            mtitle = java.net.URLDecoder.decode(mtitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        param.put("mflightdate", mflightdate);
        param.put("flightnumber", flightnumber);
        param.put("mtitle", mtitle);
        param.put("mtext", mtext);
        param.put("userId", userId);
        param.put("mtoType", 0);
        param.put("sortOrder", sortOrder);
        param.put("sortName", sortName);
        List<String> roleIdList = UserUtils.getUser().getRoleIdList();
        String roleIds = StringUtils.join(roleIdList.toArray(), "','");
        String officeId = UserUtils.getUser().getOffice().getId();
        param.put("officeId", officeId);
        param.put("roleIds", roleIds);
        List<Map<String,String>> dataList = null;
        if ("2".equals(printnum)) {
            dataList = (List<Map<String,String>>) mmHisService.getListInfo(param).get("rows");
        } else {
            dataList = historymessageService.gethistoryListPrint(param);
        }
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < titleArray.size(); i++) {
            if (titleArray.getJSONObject(i).getString("title") != null)
                titleList.add(titleArray.getJSONObject(i).getString("title"));
        }
        try {
            String fileName = "历史消息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
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
            String excelTitle = "历史消息" + DateUtils.getDate("yyyy年MM月dd日 E");
            ExportFDExcel excel = new ExportFDExcel(excelTitle, titleList);
            excel.setDataList(titleArray, dataList);
            excel.write(response);
            excel.dispose();
        } catch (Exception e) {
            logger.error("历史消息导出失败" + e.getMessage());
        }
    }

    /**
     * 
     * Discription:消息流转详情信息.
     * 
     * @param model
     * @return
     * @return:
     * @author:zhaol
     * @update:2017年9月8日 [变更描述]
     */
    @RequestMapping(value = "messageFlow")
    public String messageFlow(HttpServletRequest request,HttpServletResponse response,Model model) {
        String id = request.getParameter("id");
        String num = request.getParameter("num");
        Map<String,String> map = new HashMap<String,String>();
        map.put("MID", id);
        map.put("isHis", num);
        JSONObject data = new JSONObject();
        // data.put("title", "消息流转图");
        JSONArray list = historymessageService.createFlow(map);

        data.put("data", list);
        JSONObject option = EchartsFactory.creategraph(data);
        model.addAttribute("id", id);
        model.addAttribute("option", option);
        model.addAttribute("num", list.size());
        return "prss/message/messageFlow";
    }

    /**
     * 
     * Discription:获取历史消息附件列表.
     * 
     * @param model
     * @return
     * @return:
     * @author:zhaol
     * @update:2017年9月8日 [变更描述]
     */
    @RequestMapping(value = "getFileIds")
    @ResponseBody
    public JSONArray getFileIds(@RequestParam(value = "id",required = true) String id,String num) {
        Map<String,String> param = new HashMap<String,String>();
        param.put("mainId", id);
        if ("2".equals(num) || "64".equals(num)) {
            return mmHisService.getFileIds(param);
        } else {
            return historymessageService.getFileIds(param);
        }
    }

}
