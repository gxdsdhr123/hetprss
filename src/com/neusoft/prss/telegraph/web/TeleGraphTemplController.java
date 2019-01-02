/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 上午10:20:13
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.telegraph.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.service.ParamCommonService;
import com.neusoft.prss.getfltinfo.service.GetFltInfoService;
import com.neusoft.prss.message.entity.CommonVO;
import com.neusoft.prss.message.entity.MessageUtils;
import com.neusoft.prss.message.service.MessageCommonService;
import com.neusoft.prss.telegraph.service.TeleGraphTemplService;

@Controller
@RequestMapping(value = "${adminPath}/telegraph/templ")
public class TeleGraphTemplController extends BaseController {

	@Resource
	private TeleGraphTemplService teleGraphTemplService;

	@Resource
	private ParamCommonService paramCommonService;
	
    @Resource
    private GetFltInfoService getFltInfoService;
    
    @Resource
    private MessageCommonService messageCommonService;

	@RequestMapping(value = "list")
	public String templManager() {
		return "prss/telegraph/telegraphmanager";
	}

	/***
	 * 获取数据库模板数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "getTree")
	@ResponseBody
	public JSONArray getTeleGraphTempl() {
		JSONArray jsonArray = teleGraphTemplService.getTree();
		return jsonArray;
	}

	/**
	 * 添加模板页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "info")
	public String info(HttpServletRequest request, HttpServletResponse response, Model model) {
		String flag = request.getParameter("flag");
		String id = request.getParameter("id");
		String typeId = request.getParameter("typeId");

		JSONObject vo = new JSONObject();
		vo.put("ALN_2CODE", typeId);
		if (!"add".equals(flag)) {
			vo = teleGraphTemplService.queryTelegraphById(id);

            String varcols = vo.getString("VARCOLS");
            String tg_text = vo.getString("TG_TEXT");
            tg_text = tg_text.replace(" ", "&nbsp;");
            if(varcols !=null && !"".equals(varcols)){
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("schema", "99");
                data.put("colids", varcols);
                data.put("text", tg_text);
                tg_text = paramCommonService.getColumn(data);
            }
            tg_text = tg_text.replace("\n", "<br>");
            vo.put("TG_TEXT", tg_text);
		}
		JSONArray typeList = teleGraphTemplService.getTypeList();
        JSONArray priorityList = teleGraphTemplService.getPriorityList();
		model.addAttribute("flag", flag);
		model.addAttribute("id", id);
		model.addAttribute("typeId", typeId);
		model.addAttribute("vo", vo);
		model.addAttribute("typeList", typeList);
		model.addAttribute("priorityList", priorityList);
		return "prss/telegraph/telegraphinfo";
	}

	/**
	 * 模板列表数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "getTelegraphListList")
	@ResponseBody
	public Map<String, Object> getTelegraphList(int pageSize, int pageNumber, String sortName, String sortOrder,
			String typeId) {
		Map<String, Object> param = new HashMap<String, Object>();
		int begin = (pageNumber - 1) * pageSize;
		int end = pageSize + begin;
		param.put("begin", begin);
		param.put("end", end);
		param.put("sortName", sortName);
		param.put("sortOrder", sortOrder);
		param.put("typeId", typeId);
		return teleGraphTemplService.getTelegraphList(param);
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(@RequestParam(value = "ids[]", required = true) String[] ids) {
		try {
		    
            String msg = "";
            for(String id : ids){
                JSONObject obj = teleGraphTemplService.getTemplCount(id);
                int count = obj==null?0:obj.getInteger("NUM");
                if(count<=0){
                    teleGraphTemplService.deleteList(id);
                } else {
                    String name = obj.getString("NAME");
                    msg += name + ",";
                }
            }
            if(!"".equals(msg)){
                msg = msg.substring(0, msg.length()-1);
                return "模板【"+msg+"】正在使用，不能删除";
            } else {
                return MessageUtils.SUCCESS_MSG;
            }
		} catch (Exception e) {
			return MessageUtils.ERROR_MSG;
		}
	}

	@RequestMapping(value = "senderSite")
	public String senderMessage(HttpServletRequest request, HttpServletResponse response, Model model) {
		String id = request.getParameter("id");
		String flag = request.getParameter("flag");
		String selectIds = request.getParameter("selectIds");

		String sitaType = request.getParameter("sitaType");
		String sitaTypeName = request.getParameter("sitaTypeName");
		String tg_site_id = request.getParameter("tg_site_id");
		String tg_site_name = request.getParameter("tg_site_name");
		String tg_address = request.getParameter("tg_address");
		String proceclsfrom = request.getParameter("proceclsfrom");
		String proceclsfromname = request.getParameter("proceclsfromname");
		String procdefparamfrom = request.getParameter("procdefparamfrom");
		String ifprocfrom = request.getParameter("ifprocfrom");
		String ifprocfromname = request.getParameter("ifprocfromname");
		String senderSitaType = request.getParameter("senderSitaType");
		String tableName = request.getParameter("tableName");
		String ids = request.getParameter("ids");

		try {
			if (sitaType != null && !"".equals(sitaType))
				sitaTypeName = java.net.URLDecoder.decode(sitaTypeName, "utf-8");
			if (sitaType != null && !"".equals(sitaType))
				tg_address = java.net.URLDecoder.decode(tg_address, "utf-8");
			if (tg_site_name != null && !"".equals(tg_site_name))
				tg_site_name = java.net.URLDecoder.decode(tg_site_name, "utf-8");
			if (proceclsfromname != null && !"".equals(proceclsfromname))
				proceclsfromname = java.net.URLDecoder.decode(proceclsfromname, "utf-8");
			if (ifprocfromname != null && !"".equals(ifprocfromname))
				ifprocfromname = java.net.URLDecoder.decode(ifprocfromname, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<CommonVO> listPS = teleGraphTemplService.getProcvars();
		model.addAttribute("id", id);
		model.addAttribute("flag", flag);
		model.addAttribute("listPS", listPS);
		model.addAttribute("sitaType", sitaType);
		model.addAttribute("sitaTypeName", sitaTypeName);
		model.addAttribute("tg_site_id", tg_site_id);
		model.addAttribute("tg_site_name", tg_site_name);
		model.addAttribute("tg_address", tg_address);
		model.addAttribute("proceclsfrom", proceclsfrom);
		model.addAttribute("proceclsfromname", proceclsfromname);
		model.addAttribute("procdefparamfrom", procdefparamfrom);
		model.addAttribute("ifprocfrom", ifprocfrom);
		model.addAttribute("ifprocfromname", ifprocfromname);
		model.addAttribute("selectIds", selectIds);
		model.addAttribute("ids", ids);
		model.addAttribute("tableName", tableName);
		model.addAttribute("senderSitaType", senderSitaType);
		return "prss/telegraph/sendersite";
	}

	@RequestMapping(value = "srList")
	public String srList(HttpServletRequest request, HttpServletResponse response, Model model) {
		String sitaType = request.getParameter("sitaType");
		String ids = request.getParameter("ids");
		model.addAttribute("ids", ids);
		model.addAttribute("sitaType", sitaType);
		return "prss/telegraph/sitelist";
	}

	@RequestMapping(value = "getListData")
	@ResponseBody
	public JSONArray getSrListData(HttpServletRequest request) {
		String sitaType = request.getParameter("sitaType");
		String ids = request.getParameter("ids");
		Map<String, String> map = new HashMap<String, String>();
		map.put("sitaType", sitaType);
		map.put("ids", ids);
		JSONArray srList = teleGraphTemplService.getSiteList(map);
		return srList;
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, Model model) {
		String flag = request.getParameter("flag");
		String typeId = request.getParameter("typeId");
		String tgType = request.getParameter("tgType");
		String tg_name = request.getParameter("tg_name");
		String mtext = request.getParameter("mtext");
		String varcols = request.getParameter("varcols");
		String fiotype = request.getParameter("fiotype");
		String id = request.getParameter("id");
		String eventid = request.getParameter("eventid");
		String priority = request.getParameter("priority");

		String sendList = request.getParameter("sendList");
		String receiveList = request.getParameter("receiveList");
		String ifanalysis = request.getParameter("ifanalysis");
		JSONObject vo = new JSONObject();

		// values
		// (#{id},#{typeId},#{tgType},#{tg_name},#{mtext},#{varcols},sysdate,#{userId},#{fiotype})

		try {
			String userId = UserUtils.getUser().getId();
			if(mtext.startsWith(" "))
			    mtext = mtext.substring(1);
//            mtext = mtext.replace("？", " ").replace("?", " ");//.replace(" ", "").replace("&nbsp;", " ").
			vo.put("userId", userId);
			vo.put("typeId", typeId);
			vo.put("tgType", tgType);
			vo.put("tg_name", tg_name);
			vo.put("mtext", mtext);
			vo.put("varcols", varcols);
			vo.put("fiotype", fiotype);
			vo.put("eventid", eventid);
			vo.put("priority", priority);
			vo.put("ifanalysis", ifanalysis);
			sendList = StringEscapeUtils.unescapeHtml4(sendList);
			JSONArray sendArray = JSONArray.parseArray(sendList);
			receiveList = StringEscapeUtils.unescapeHtml4(receiveList);
			JSONArray receiveArray = JSONArray.parseArray(receiveList);
			if ("add".equals(flag)) {
				id = teleGraphTemplService.getTelegraphId();
				vo.put("id", id);
				teleGraphTemplService.insertTelegraphInfo(vo, sendArray, receiveArray);
			} else {
				vo.put("id", id);
				teleGraphTemplService.updateTelegraphInfo(vo, sendArray, receiveArray);
			}
			return MessageUtils.SUCCESS_MSG;
		} catch (Exception e) {
			e.printStackTrace();
			return MessageUtils.ERROR_MSG;
		}
	}

	@RequestMapping(value = "getSenderList")
	@ResponseBody
	public JSONArray getSenderList(HttpServletRequest request) {
		String id = request.getParameter("id");
		return teleGraphTemplService.getSenderListById(id);
	}

	@RequestMapping(value = "getReceiverList")
	@ResponseBody
	public JSONArray getReceiverList(HttpServletRequest request) {
		String id = request.getParameter("id");
		return teleGraphTemplService.getReveiverListById(id);
	}

	@RequestMapping(value = "getVariable")
	public String getVariable(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("schema", "99");
		return "prss/common/varList";
	}

	@RequestMapping(value = "getAddressByTempId")
	@ResponseBody
	public JSONObject getAddressByTempId(HttpServletRequest request) {
	    JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		String fltid = request.getParameter("fltid");
		JSONObject templ = teleGraphTemplService.getTelegraphTemplById(id);
		if(templ!=null){
    		String varcols = templ.getString("VARCOLS");
    		String oldtext = templ.getString("TG_TEXT");
    		JSONArray oldTextArr = new JSONArray();
    		JSONObject object = new JSONObject();
    		object.put("TG_TEXT", oldtext);
    		oldTextArr.add(object);
    		if (StringUtils.isNotBlank(varcols)) {
    		    String finaltext = oldtext;
    		    JSONObject colObj = new JSONObject();
    		    colObj = getFltInfoService.queryParamVars(fltid, varcols);
    		    oldTextArr = messageCommonService.transMessageVars(colObj, oldTextArr);
    		    finaltext = oldTextArr.getJSONObject(0).getString("TG_TEXT");
    		    templ.put("TG_TEXT", finaltext);
    //		    HashMap<String,String> data = new HashMap<String,String>();
    //		    data.put("schema", "99");
    //            data.put("text", oldtext);
    //            data.put("colids", varcols);
    //		    finaltext = paramCommonService.getColumn(data);
    
//    		    finaltext = finaltext.replace("\n", "<br>");
                templ.put("TG_TEXT", finaltext);
    		} 
		}
		result.put("templ", templ);
		JSONArray address = teleGraphTemplService.getAddressByTempId(id);
		result.put("address", address);
		JSONArray templFrom = teleGraphTemplService.getSenderListById(id);
		if(templFrom.size()>0){
	        result.put("sender", templFrom.getJSONObject(0));
		}
		return result;
	}
}
