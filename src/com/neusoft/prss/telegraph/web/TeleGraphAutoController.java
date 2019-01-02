package com.neusoft.prss.telegraph.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.service.ParamCommonService;
import com.neusoft.prss.getfltinfo.service.GetFltInfoService;
import com.neusoft.prss.message.entity.MessageUtils;
import com.neusoft.prss.message.service.MessageCommonService;
import com.neusoft.prss.telegraph.service.TeleGraphAutoService;
import com.neusoft.prss.telegraph.service.TeleGraphTemplService;

@Controller
@RequestMapping(value = "${adminPath}/telegraph/auto")
public class TeleGraphAutoController extends BaseController {

	@Resource
	private TeleGraphAutoService teleGraphAutoService;

	@Resource
	private ParamCommonService paramCommonService;
	
	@Resource
    private TeleGraphTemplService teleGraphTemplService;

	/*@Resource
	private TelegraphSendService teleSendService;*/
	
	@Resource
	private GetFltInfoService getFltInfoService;

	@Resource
	private MessageCommonService messageCommonService;
	
	@RequestMapping(value = "list")
	public String list() {
		return "prss/telegraph/telegraphautomain";
	}

	@RequestMapping(value = "autoList")
	@ResponseBody
	public Map<String, Object> autoList(int pageSize, int pageNumber, String tg_name, String crtime, String sortName,
			String sortOrder, HttpServletRequest request, HttpServletResponse response) {
		JSONObject param = new JSONObject();
		int begin = (pageNumber - 1) * pageSize;
		int end = pageSize + begin;
		param.put("begin", begin);
		param.put("end", end);
		param.put("tg_name", tg_name);
		param.put("crtime", crtime);
		param.put("sortName", sortName);
		param.put("sortOrder", sortOrder);
		return teleGraphAutoService.getAutoList(param);
	}

	@RequestMapping(value = "info")
	public String info(HttpServletRequest request, HttpServletResponse response, Model model) {
		String flag = request.getParameter("flag");
		String id = request.getParameter("id");

		JSONObject vo = new JSONObject();
		if (!"add".equals(flag)) {
			vo = teleGraphAutoService.queryAutoById(id);
			String varcols = vo.getString("VARCOLS");
			String tg_text = vo.getString("TG_TEXT");
			if (varcols != null && !"".equals(varcols)) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("schema", "99");
				data.put("colids", varcols);
				data.put("text", tg_text);
				tg_text = paramCommonService.getColumn(data);
			}
            tg_text = tg_text.replace("\n", "<br>");
			vo.put("TG_TEXT", tg_text);

			String text = vo.getString("TEXT");
			if(text != null)
			vo.put("TEXT", text.replace(">", "&gt;").replace("<", "&lt;").replace("？", " "));

		}
		JSONArray eventList = teleGraphAutoService.getEventList();
		JSONArray airlineList = teleGraphAutoService.getAirlineList();

		model.addAttribute("flag", flag);
		model.addAttribute("id", id);
		model.addAttribute("vo", vo);
		model.addAttribute("eventList", eventList);
		model.addAttribute("airlineList", airlineList);
		return "prss/telegraph/autoinfo";
	}

	@RequestMapping(value = "autoTempl")
	public String autoTempl(HttpServletRequest request, HttpServletResponse response, Model model) {
		String eventId = request.getParameter("eventId");
		String airline_code = request.getParameter("airline_code");
		model.addAttribute("eventId", eventId);
		model.addAttribute("airline_code", airline_code);
		return "prss/telegraph/telegraphList";
	}

	@RequestMapping(value = "getTelegraphTempl")
	@ResponseBody
	public JSONArray getTelegraphTempl(HttpServletRequest request) {
		String eventId = request.getParameter("eventId");
		String airline_code = request.getParameter("airline_code");
		JSONObject param = new JSONObject();
		param.put("eventId", eventId);
		param.put("airline_code", airline_code);
		param.put("flag", "send");
		JSONArray data = teleGraphAutoService.getTelegraphTempl(param);
		return data;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(@RequestParam(value = "ids[]", required = true) String[] ids) {
		try {
			teleGraphAutoService.deleteList(ids);
			return MessageUtils.SUCCESS_MSG;
		} catch (Exception e) {
			return MessageUtils.ERROR_MSG;
		}
	}


	@RequestMapping(value = "save")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, Model model) {
		String submitType = request.getParameter("submitType");

		String flag = request.getParameter("flag");
		String id = request.getParameter("id");
		String eventId = request.getParameter("eventId");
		String condition = request.getParameter("condition");
		String airline_code = request.getParameter("airline_code");
		String colids = request.getParameter("colids");
		String mtemplid = request.getParameter("mtemplid");
		String varcols = request.getParameter("varcols");
		String sendType = request.getParameter("sendType");
		String ruleId = request.getParameter("ruleId");
		String expression = request.getParameter("expression");
		String drools = request.getParameter("drools");
		String stopcondition = request.getParameter("stopcondition");
		String sendtime = request.getParameter("sendtime");
		String whiletime = request.getParameter("whiletime");
		String stoptime = request.getParameter("stoptime");
		String flightnumber = request.getParameter("fltno");
		String mail = request.getParameter("mail");
		String sita = request.getParameter("sita");
		String fltid = request.getParameter("fltid");

        drools = StringEscapeUtils.unescapeHtml4(drools);
		String mtext = "";
		if ("send".equals(submitType)) {
			mtext = request.getParameter("msgtext");
		} else {
			mtext = request.getParameter("mtext");
		}
		if (mtext != null && !"".equals(mtext))
			mtext = mtext.replace("&nbsp;", " ").replace("？", " ").replace(" ", " ");

        if(mtext.startsWith(" "))
            mtext = mtext.substring(1);
		if (condition != null && !"".equals(condition))
			condition = condition.replace("？", " ").replace("&nbsp;", " ").replace(" ", " ");
		// 发送报文
		if ("send".equals(submitType)) {
			JSONObject parms = new JSONObject();
			parms.put("tg_text", mtext.trim());
			parms.put("email_address", mail);
			parms.put("sita_address", sita);
			parms.put("user_id", UserUtils.getUser().getId());
			parms.put("fltID", fltid);
			parms.put("tgtmID",mtemplid);

			/*if (teleSendService.sendTelegraph(parms)) {
				return "success";
			}*/
		}

		JSONObject vo = new JSONObject();

		try {
			String userId = UserUtils.getUser().getId();
			vo.put("userId", userId);
			vo.put("eventId", eventId);
			vo.put("condition", condition);
			vo.put("airline_code", airline_code);
			vo.put("mtext", mtext);
			vo.put("varcols", varcols);
			vo.put("colids", colids);
			vo.put("mtemplid", mtemplid);
			vo.put("sendType", sendType);
			vo.put("expression", expression);
			vo.put("drools", drools);

			vo.put("stopcondition", stopcondition);
			vo.put("sendtime", sendtime);
			vo.put("whiletime", whiletime);
			vo.put("stoptime", stoptime);
			vo.put("flightnumber", flightnumber);
			if ("add".equals(flag)) {
				teleGraphAutoService.insertAutoInfo(vo);
			} else {
				vo.put("id", id);
				vo.put("ruleId", ruleId);
				teleGraphAutoService.updateAutoInfo(vo);
			}
			return MessageUtils.SUCCESS_MSG;
		} catch (Exception e) {
			e.printStackTrace();
			return MessageUtils.ERROR_MSG;
		}
	}


    @RequestMapping(value = "sendMessageList")
    public String sendMessageList(HttpServletRequest request, Model model) {
        String fltid = request.getParameter("fltid");
        JSONObject param = new JSONObject();
        param.put("fltId", fltid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JSONArray priorityList = teleGraphTemplService.getPriorityList();
        JSONObject auto = teleGraphAutoService.getSendAuto(fltid);
        JSONObject fltInfo = teleGraphAutoService.getFltInfo(fltid);

        String airline_code = fltInfo.getString("FLIGHT_NUMBER");
        if(!StringUtils.isBlank(airline_code))
            airline_code = airline_code.substring(0, 2);
        param.put("airline_code", airline_code);
        model.addAttribute("fltid", fltid);
        model.addAttribute("fltdate", sdf.format(new Date()));
        model.addAttribute("priorityList", priorityList);
        if(auto != null){
            String varcols = auto.getString("VARCOLS");
            String oldtext = auto.getString("TG_TEXT");
            JSONObject colObj = new JSONObject();
            JSONArray oldTextArr = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("TG_TEXT", oldtext);
            oldTextArr.add(object);
            colObj = getFltInfoService.queryParamVars(fltid, varcols);
            oldTextArr = messageCommonService.transMessageVars(colObj, oldTextArr);
            oldtext = oldTextArr.getJSONObject(0).getString("TG_TEXT");
            auto.put("TG_TEXT", oldtext);
        }
        param.put("flag", "send");
        JSONArray data = teleGraphAutoService.getTelegraphTempl(param);
        model.addAttribute("templateList", data);
        model.addAttribute("auto", auto);
        model.addAttribute("fltInfo", fltInfo);
        return "prss/telegraph/sendMessageList";
    }
    
    @RequestMapping(value = "stop")
    @ResponseBody
    public String stop(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            String manualId = request.getParameter("manualId");
            JSONObject json = new JSONObject();
            json.put("manualId", manualId);
            teleGraphAutoService.updateManualStop(json);
            return MessageUtils.SUCCESS_MSG;
        } catch (Exception e) {
            e.printStackTrace();
            return MessageUtils.ERROR_MSG;
        }
    }
    
    @RequestMapping(value = "send")
    @ResponseBody
    public String send(HttpServletRequest request, HttpServletResponse response, Model model) {
        String submitType = request.getParameter("submitType");
        String condition = request.getParameter("condition");
        String mtemplid = request.getParameter("mtemplid");
        String address = request.getParameter("address");
        String tgSiteType = request.getParameter("tgSiteType");
        String fltid = request.getParameter("fltid");
        String mtext =  request.getParameter("mtext");
        if (mtext != null && !"".equals(mtext))
            mtext = mtext.replace("&nbsp;", " ").replace("？", " ").replace(" ", " ");
        if (condition != null && !"".equals(condition))
            condition = condition.replace("？", " ").replace("&nbsp;", " ").replace(" ", " ");
        
        try {
            // 发送报文
            if ("send".equals(submitType)) {
                JSONObject parms = new JSONObject();
                parms.put("tg_text", mtext);
                if("1".equals(tgSiteType)) {
                    parms.put("sita_address", address);
                } else {
                    parms.put("email_address", address);
                }
                parms.put("user_id", UserUtils.getUser().getId());
                parms.put("fltID", fltid);
                parms.put("tgtmID",mtemplid);
               /* if (!teleSendService.sendTelegraph(parms)) {
                    return MessageUtils.ERROR_MSG;
                }*/
            }
            return MessageUtils.SUCCESS_MSG;
        } catch (Exception e) {
            e.printStackTrace();
            return MessageUtils.ERROR_MSG;
        }
    }
    
    @RequestMapping(value = "saveTime")
    @ResponseBody
    public String saveTime(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        String eventId = request.getParameter("eventId");
        String airline_code = request.getParameter("airline_code");
        String mtemplid = request.getParameter("mtemplid");
        String varcols = request.getParameter("varcols");
        String stopcondition = request.getParameter("stopcondition");
        String sendtime = request.getParameter("beginTime");
        String whiletime = request.getParameter("recycleTime");
        String stoptime = request.getParameter("endTime");
        String flightnumber = request.getParameter("fltno");
        String manualId = request.getParameter("manualId");
        String fltid = request.getParameter("fltid");
        String sendType = request.getParameter("sendType");
        String fltdate = request.getParameter("fltdate");
        String mtext =  request.getParameter("mtext");
        String address = request.getParameter("address");
        if (mtext != null && !"".equals(mtext))
            mtext = mtext.replace("&nbsp;", " ").replace("？", " ").replace(" ", " ");
        JSONObject vo = new JSONObject();

        try {
            String userId = UserUtils.getUser().getId();
            vo.put("userId", userId);
            vo.put("eventId", eventId);
            vo.put("aln2code", airline_code);
            vo.put("mtext", mtext);
            vo.put("varcols", varcols);
            vo.put("mtemplid", mtemplid);
            vo.put("manualId", manualId);
            vo.put("stopcondition", stopcondition);
            vo.put("sendtime", sendtime);
            vo.put("whiletime", whiletime);
            vo.put("stoptime", stoptime);
            vo.put("flightnumber", flightnumber);
            vo.put("sendType", sendType);
            vo.put("sendstatus", "1");
            vo.put("fltid", fltid);
            vo.put("autoid", "");
            vo.put("flightdate", fltdate);
            vo.put("address", address);
            if (manualId != null && !"".equals(manualId)) {
                teleGraphAutoService.updateManualInfo(vo);
            } else {
                vo.put("id", id);
                teleGraphAutoService.insertManualInfo(vo);
            }
            return MessageUtils.SUCCESS_MSG;
        } catch (Exception e) {
            e.printStackTrace();
            return MessageUtils.ERROR_MSG;
        }
    }
   
}
