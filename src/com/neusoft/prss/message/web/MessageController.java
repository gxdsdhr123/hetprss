/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月21日 上午9:39:47
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.web;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.config.Global;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.sys.entity.Role;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.common.entity.JobKind;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.message.service.MessageCommonService;
import com.neusoft.prss.message.service.MessageSendService;
import com.neusoft.prss.message.service.MessageService;
import com.neusoft.prss.message.service.MessageTemplService;
import com.neusoft.prss.message.service.MessageTypeService;
import com.neusoft.prss.message.util.NetUtil;
import com.neusoft.prss.message.util.TalkClient;

@Controller
@RequestMapping(value = "${adminPath}/message/common")
public class MessageController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Resource
	private MessageCommonService messageCommonService;

	@Resource
	private MessageTemplService messageTemplService;

	@Resource
	private MessageTypeService messageTypeService;

	@Resource
	private MessageService messageService;

	@Resource
	private FileService fileservice;

	@Resource
	private MessageSendService messageSendService;
	
	@Resource
    private CacheService cacheService;

	/**
	 * 
	 * Discription:跳转到发送页面.
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @return:返回值意义
	 * @author:l.ran@neusoft.com
	 * @update:2017年9月28日 Maxx [变更描述]
	 */
	@RequestMapping(value = "send")
	public String send(HttpServletRequest request, HttpServletResponse response, Model model) {
		// flag=add&fltid=100022
		String fltid = request.getParameter("fltid");
		String in_fltid = request.getParameter("in_fltid");
		String out_fltid = request.getParameter("out_fltid");
		String flightNumber = request.getParameter("flightNumber");
		String flightDate = request.getParameter("flightDate");
		String flag = request.getParameter("flag");
		String tid = request.getParameter("tid");
		String fiotype = request.getParameter("fiotype");
		String oth_fltid = "";
		if ("A0".equals(fiotype)) {
			oth_fltid = out_fltid;
		} else {
			oth_fltid = in_fltid;
		}
		List<Role> roles = UserUtils.getRoleList();
		boolean defaultSubs = false;
		for(Role role:roles) {
			if("b598054c0d3941c892236152bac842a8".equals(role.getId())) {
				defaultSubs = true;
				break;
			}
		}
		model.addAttribute("fltid", fltid);
		model.addAttribute("oth_fltid", oth_fltid);
		model.addAttribute("flightNumber", flightNumber);
		model.addAttribute("flightDate", flightDate);
		model.addAttribute("flag", flag);
		model.addAttribute("tid", tid);
		model.addAttribute("fiotype", fiotype);
		model.addAttribute("defaultSubs", defaultSubs);
		return "prss/message/message_main";
	}

	@RequestMapping(value = "getTemplateList")
	@ResponseBody
	public JSONObject getTemplateList(HttpServletRequest request) {
		String userId = UserUtils.getUser().getId();
		String officeId = UserUtils.getUser().getOffice().getId();
		List<String> roleIdList = UserUtils.getUser().getRoleIdList();
		String roleIds = StringUtils.join(roleIdList.toArray(), "','");
		String userType = UserUtils.getUser().getUserType();
		String text = request.getParameter("text");
		String ioFlag = request.getParameter("ioFlag");
		String fiotype = "";

		JSONObject info = new JSONObject();
		try {
			if (text != null)
				text = java.net.URLDecoder.decode(text, "utf-8");
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("userId", userId);
			dataMap.put("roleIds", roleIds);
			dataMap.put("officeId", officeId);
			dataMap.put("userType", userType);
			dataMap.put("mfromtype", "0");
			dataMap.put("text", text);
			dataMap.put("ioFlag", ioFlag);
			if ("I".equals(ioFlag)) {
				fiotype = "A0";
			} else if ("O".equals(ioFlag)) {
				fiotype = "D0";
			}
			dataMap.put("fiotype", fiotype);
			dataMap.put("mftype", 0);
			JSONArray templageList = messageService.getTemplate(dataMap);
			info.put("result", true);
			info.put("templateList", templageList);
		} catch (Exception e) {
			e.printStackTrace();
			info.put("result", false);
		}
		return info;
	}

	/**
	 * 
	 * Discription:刷新信息，已发消息、未读消息、.
	 * 
	 * @param request
	 * @return
	 * @return:返回值意义
	 * @author:l.ran@neusoft.com
	 * @update:2017年9月28日 Maxx [变更描述]
	 */
	@RequestMapping(value = "sendMessageList")
	@ResponseBody
	public JSONObject sendMessageList(HttpServletRequest request) {
		String userId = UserUtils.getUser().getId();
		String officeId = UserUtils.getUser().getOffice().getId();
		List<String> roleIdList = UserUtils.getUser().getRoleIdList();
		String roleIds = StringUtils.join(roleIdList.toArray(), "','");
		String userType = UserUtils.getUser().getUserType();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		String pageNum = request.getParameter("pageNum");
		int pageRow = 100;
		if(pageNum == null || "".equals(pageNum))
		    pageNum = "1";
		int num = Integer.parseInt(pageNum);
		int end = num * pageRow;
		int begin = (num -1) * pageRow;
        List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
        String jobKind = "";
        if(jobKinds.size()>0){
            jobKind = UserUtils.getCurrentJobKind().get(0).getKindCode();
        } 
		dataMap.put("userId", userId);
		dataMap.put("roleIds", roleIds);
		dataMap.put("officeId", officeId);
		dataMap.put("userType", userType);
		dataMap.put("mtotype", "0");
		dataMap.put("end", end);
		dataMap.put("begin", begin);
		dataMap.put("jobKind", jobKind);

		JSONObject info = new JSONObject();
		try {
			JSONArray sendMessageList = messageService.getSendMessage(dataMap);
			info.put("result", true);
			info.put("sendMessageList", sendMessageList);
		} catch (Exception e) {
			e.printStackTrace();
			info.put("result", false);
		}
		return info;
	}
	
	/**
     * 
     * Discription:刷新信息，待办消息、.
     * 
     * @param request
     * @return
     * @return:返回值意义
     * @author:l.ran@neusoft.com
     * @update:2017年9月28日 Maxx [变更描述]
     */
	@RequestMapping(value = "unReadList")
    @ResponseBody
    public JSONObject unReadList(HttpServletRequest request) {
        String userId = UserUtils.getUser().getId();
        String officeId = UserUtils.getUser().getOffice().getId();
        List<String> roleIdList = UserUtils.getUser().getRoleIdList();
        String roleIds = StringUtils.join(roleIdList.toArray(), "','");
        String userType = UserUtils.getUser().getUserType();
        String pageNum = request.getParameter("pageNum");
        int pageRow = 100;
        if(pageNum == null || "".equals(pageNum))
            pageNum = "1";
        int num = Integer.parseInt(pageNum);
        int end = num * pageRow;
        int begin = (num -1) * pageRow;
        String messtype = request.getParameter("messtype");

        List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
        String jobKind = "";
        if(jobKinds.size()>0){
            jobKind = UserUtils.getCurrentJobKind().get(0).getKindCode();
        } 
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("userId", userId);
        dataMap.put("roleIds", roleIds);
        dataMap.put("officeId", officeId);
        dataMap.put("userType", userType);
        dataMap.put("mtotype", "0");
        dataMap.put("messtype", messtype);
        dataMap.put("end", end);
        dataMap.put("begin", begin);
        dataMap.put("jobKind", jobKind);

        JSONObject info = new JSONObject();
        try {
            JSONArray unRead = messageService.getUnReadMessage(dataMap);
            info.put("result", true);
            info.put("unReadList", unRead);
        } catch (Exception e) {
            e.printStackTrace();
            info.put("result", false);
        }
        return info;
    }
	/**
     * 
     * Discription:刷新信息，待办消息、.
     * 
     * @param request
     * @return
     * @return:返回值意义
     * @author:l.ran@neusoft.com
     * @update:2017年9月28日 Maxx [变更描述]
     */
    @RequestMapping(value = "unReadListNum")
    @ResponseBody
    public JSONObject unReadListNum(HttpServletRequest request) {
        String userId = UserUtils.getUser().getId();
        String officeId = UserUtils.getUser().getOffice().getId();
        List<String> roleIdList = UserUtils.getUser().getRoleIdList();
        String roleIds = StringUtils.join(roleIdList.toArray(), "','");
        String userType = UserUtils.getUser().getUserType();
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("userId", userId);
        dataMap.put("roleIds", roleIds);
        dataMap.put("officeId", officeId);
        dataMap.put("userType", userType);
        dataMap.put("mtotype", "0");

        JSONObject info = new JSONObject();
        try {
            JSONArray unRead = messageService.getUnReadNum(dataMap);
            info.put("result", true);
            info.put("unReadNumList", unRead);
        } catch (Exception e) {
            e.printStackTrace();
            info.put("result", false);
        }
        return info;
    }

	/**
	 * 
	 * Discription:获取消息信息.
	 * 
	 * @param request
	 * @return
	 * @return:返回值意义
	 * @author:l.ran@neusoft.com
	 * @update:2017年9月28日 Maxx [变更描述]
	 */
	@RequestMapping(value = "getMessageInfo")
	@ResponseBody
	public JSONObject getMessageInfo(HttpServletRequest request) {
		String mid = request.getParameter("tid");
		String fltid = request.getParameter("fltid");
		String toListStr = request.getParameter("toListStr");
		String type = request.getParameter("type");
		String userId = UserUtils.getUser().getId();
		String officeId = UserUtils.getUser().getOffice().getId();
		JSONObject dataMap = new JSONObject();

		dataMap.put("USERID", userId);
		dataMap.put("OFFICEID", officeId);
		dataMap.put("TOLISTSTR", toListStr);
		dataMap.put("FLTID", fltid);
		dataMap.put("MID", mid);
		dataMap.put("TYPE", type);

		JSONObject info = new JSONObject();
		try {
			info = messageCommonService.queryMessageInfoById(dataMap);
			info.put("result", true);
		} catch (Exception e) {
			info.put("result", false);
		}
		return info;
	}

	@RequestMapping(value = "getTemplateInfo")
	@ResponseBody
	public JSONObject getTemplateInfo(HttpServletRequest request) {
		String tid = request.getParameter("tid");
		String fltid = request.getParameter("fltid");
		String oth_fltid = request.getParameter("oth_fltid");
		String fiotype = request.getParameter("fiotype");
		String toListStr = request.getParameter("toListStr");
		String type = request.getParameter("type");
		String userId = UserUtils.getUser().getId();
		String officeId = UserUtils.getUser().getOffice().getId();
		List<String> roleIdList = UserUtils.getUser().getRoleIdList();
		String roleIds = StringUtils.join(roleIdList.toArray(), "','");
		JSONObject dataMap = new JSONObject();

		dataMap.put("USERID", userId);
		dataMap.put("OFFICEID", officeId);
		dataMap.put("TOLISTSTR", toListStr);
		dataMap.put("FLTID", fltid);
		dataMap.put("TID", tid);
		dataMap.put("TYPE", type);
		dataMap.put("ROLEIDS", roleIds);
		dataMap.put("MTOTYPE", "0");
		dataMap.put("OTH_FLTID", oth_fltid);
		dataMap.put("FIOTYPE", fiotype);

		JSONObject info = new JSONObject();
		try {
			info = messageCommonService.queryTemplateInfoById(dataMap);
			info.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			info.put("result", false);
		}
		return info;
	}

	/**
	 * 
	 * Discription:发送消息.
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param files
	 * @return
	 * @return:返回值意义
	 * @author:l.ran@neusoft.com
	 * @update:2017年9月28日 Maxx [变更描述]
	 */
	@RequestMapping(value = "doSend", method = RequestMethod.POST)
	@ResponseBody
	public String doSend(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("file") MultipartFile[] files) {
		long beginTime = System.currentTimeMillis();
		String tid = request.getParameter("tid");
		String flightDate = request.getParameter("flightDate");
		String fltid = request.getParameter("fltid");
		String oth_fltid = request.getParameter("oth_fltid");
		String fiotype = request.getParameter("fiotype");
		String flightNumber = request.getParameter("flightNumber");

		String mtext = request.getParameter("mtext");
		String toListStr = request.getParameter("toListStr");
		String ifreply = request.getParameter("ifreply");
		String delFile = request.getParameter("delFile");
		String transsubId = request.getParameter("transsubId");
		String oldmid = request.getParameter("oldmid");
		String taskId = request.getParameter("taskId");
		String filenum = request.getParameter("filenum");

		// taskId = "1";
		List<String> roleIdList = UserUtils.getUser().getRoleIdList();
		String roleIds = StringUtils.join(roleIdList.toArray(), "','");
		String userId = UserUtils.getUser().getId();
		String loginName = UserUtils.getUser().getLoginName();
		String userName = UserUtils.getUser().getName();
		String officeId = UserUtils.getUser().getOffice().getId();
		String officeName = UserUtils.getUser().getOffice().getName();
		JSONObject dataMap = new JSONObject();
		dataMap.put("TID", tid);
		dataMap.put("MTEXT", mtext);
		dataMap.put("USERID", userId);
		dataMap.put("USERLOGINNAME", loginName);// 需要
		dataMap.put("USERNAME", userName);// 需要
		dataMap.put("OFFICEID", officeId);
		dataMap.put("OFFICENAME", officeName);
		dataMap.put("IFREPLY", "on".equals(ifreply) ? "1" : "0");
		dataMap.put("FLTID", fltid);
		dataMap.put("FLIGHTNUMBER", flightNumber);
		dataMap.put("FLIGHTDATE", flightDate);
		dataMap.put("TRANSSUBID", transsubId == null ? "0" : transsubId);
		dataMap.put("TOLISTSTR", toListStr);
		dataMap.put("SYS", "11");
		dataMap.put("OLDMID", oldmid);
		dataMap.put("ROLEIDS", roleIds);
		dataMap.put("MTOTYPE", "0");
		dataMap.put("OTH_FLTID", oth_fltid);
		dataMap.put("TASKID", taskId);
		dataMap.put("FIOTYPE", fiotype);
		// dataMap.put("files", files.length);//手持设备使用
		dataMap.put("IFFLIGHT", fiotype == null || "".equals(fiotype) ? "1" : "0");
		dataMap.put("MESSTYPE", "EVENT");
		// dataMap.put("TASKID", "639");
		// dataMap.put("IFAUTO","1");
		// dataMap.put("AUTOSENDTIME","1534");

		// dataMap.put("type", "add");
		// dataMap.put("mfromtype", "9");
		String str = "";
		String mid = "";
		JSONObject data = new JSONObject();
		try {
			JSONObject mess = messageCommonService.createMessageInfo(dataMap);
			if (mess != null) {
				logger.info("发送消息：创建消息成功");
				mid = mess.getString("MID");
				data.put("MID", mid);
				mess.put("FILESLEN", files.length);// 手持设备使用
				mess.put("SEND_FLAG", "AUTO");

				List<String> fileIds = new ArrayList<String>();
				if (filenum != null && !"".equals(filenum) && !"0".equals(filenum)) {
					for (int i = 0; i < files.length; i++) {
						JSONObject res = null;
						String fileName = files[i].getOriginalFilename();
						if (delFile.indexOf(fileName) == -1) {
							res = fileservice.doUploadFile(files[i].getBytes(), "21", UserUtils.getUser().getId(),
									files[i].getOriginalFilename());
							// {"result":"succeed",
							// "info":"FileId"}{"result":"fail", "info":"失败原因"}
							if ("succeed".equalsIgnoreCase(res.getString("result"))) {
								String fileId = res.getString("info");
								Map<String, String> params = new HashMap<String, String>();
								params.put("mid", mid);
								params.put("fileId", fileId);
								params.put("fileName", fileName);
								messageService.insertMessageFile(params);
								fileIds.add(fileId);
							} else {
								str += files[i].getOriginalFilename() + ",";
							}
						}
					}
				}
				if (!"".equals(str)) {
					str = str.substring(0, str.length() - 1);
					str = "文件" + str + "上传失败";
					messageCommonService.deleteMessage(data);
					if (fileIds.size() > 0) {
						for (String fileId : fileIds) {
							fileservice.doDeleteFile(fileId);
						}
					}
					logger.info("发送消息：上传消息附件失败");
				} else {
					logger.info("发送消息：上传消息附件成功");
					JSONObject sendmess = messageSendService.sendMessage(mess);
					String code = sendmess.getString("CODE");
					if ("0".equals(code)) {
						if (sendmess.containsKey("MSG") && !"".equals(sendmess.getString("MSG"))) {
							str = "接收人【" + sendmess.getString("MSG") + "】接收消息失败，请重新发送！";
						} else {
							str = "消息发送成功！";
						}
					} else if ("2001".equals(code)) {
						str = "给接收人【" + sendmess.getString("MSG") + "】发送消息失败！";

					} else {
						logger.info("发送消息失败：" + sendmess.getString("MSG"));
						if (!"".equals(mid))
							messageCommonService.deleteMessage(data);
						str = "发送消息失败！";
					}
				}
			} else {
				logger.info("发送消息：创建消息失败");
				if (!"".equals(mid))
					messageCommonService.deleteMessage(data);
				str = "发送消息失败！";
			}
		} catch (Exception e) {
			if (!"".equals(mid)) {
				messageCommonService.deleteMessage(data);
			}
			str = "发送消息失败！";
		}
		logger.info(str);
		logger.info("发送消息用时{}秒.", (System.currentTimeMillis() - beginTime) / 1000.0);
		return str;
	}

	@RequestMapping(value = "downloadFile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {

		JSONObject file = messageService.getFilesInfo(id);
		OutputStream out = null;
		byte[] is = null;
		String downloadFileName = "";
		try {
			if (file != null) {
				response.reset();// 重设response信息，解决部分浏览器文件名非中文问题
				response.setContentType("application/octet-stream; charset=utf-8");
				String fileId = file.getString("FILEID");
				String fileName = file.getString("FILENAME");
				is = fileservice.doDownLoadFile(fileId);
				String agent = (String) request.getHeader("USER-AGENT");
				try {
					if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
						downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8"))))
								+ "?=";
					} else {
						downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
					}
				} catch (Exception e) {
					logger.error("response信息设置失败" + e.getMessage());
				}
				response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
				out = response.getOutputStream();
				out.write(is);
			}
		} catch (Exception e) {
			logger.error("数据流写入失败" + e.getMessage());
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception e2) {
				logger.error("输出流关闭失败" + e2.getMessage());
			}
		}
	}

	/**
	 * 
	 * Discription:更新消息.
	 * 
	 * @param request
	 * @return
	 * @return:返回值意义
	 * @author:l.ran@neusoft.com
	 * @update:2017年9月28日 Maxx [变更描述]
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public boolean update(HttpServletRequest request) {
		String type = request.getParameter("type");
		try {
			JSONObject data = new JSONObject();
			data.put("TYPE", type);
			String userName = UserUtils.getUser().getName();
			List<String> roleIdList = UserUtils.getUser().getRoleIdList();
			String roleIds = StringUtils.join(roleIdList.toArray(), ",");
			String userId = UserUtils.getUser().getId();
			String officeId = UserUtils.getUser().getOffice().getId();

			data.put("USERID", userId);
			data.put("USERNAME", userName);
			data.put("ROLEIDS", roleIds);
			data.put("OFFICEID", officeId);
			data.put("SYS", "10");
			JSONArray list = new JSONArray();
			JSONObject midobj = new JSONObject();
			switch (type) {
			case "ignore":// 忽略
				String mids = request.getParameter("mids");
				String[] arr = mids.split(",");
				for (String mid : arr) {
					midobj = new JSONObject();
					midobj.put("MID", mid);
					list.add(midobj);
				}
				break;
			case "toreply":// 代回复
				midobj.put("MID", request.getParameter("mid"));
				midobj.put("MTOID", request.getParameter("mtoid"));
				midobj.put("REPLYTEXT", "代回复");
				list.add(midobj);
				break;
			case "reply":// 回复
				midobj.put("MID", request.getParameter("mid"));
				midobj.put("MTOID", request.getParameter("mtoid"));
				midobj.put("REPLYTEXT", request.getParameter("replytext"));
				list.add(midobj);
				break;
			case "isreceive":// 接收
				midobj.put("MID", request.getParameter("mid"));
				midobj.put("MTOID", request.getParameter("mtoid"));
				midobj.put("REPLYTEXT", "已收到");
				data.put("TYPE", "reply");
				list.add(midobj);
				break;
			}
			data.put("DATA", list);
			JSONArray reslut = messageCommonService.updateMassage(data);
			return reslut.size() > 0 ? false : true;
		} catch (Exception e) {
			logger.error("消息操作失败" + e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "trans", method = RequestMethod.POST)
	@ResponseBody
	public boolean trans(HttpServletRequest request) {
		String id = request.getParameter("id");
		String transtmplid = request.getParameter("transtmplid");
		String transsubid = request.getParameter("mtoid");
		JSONObject data = new JSONObject();
		String user = UserUtils.getUser().getName();
		List<String> roleIdList = UserUtils.getUser().getRoleIdList();
		String roleIds = StringUtils.join(roleIdList.toArray(), ",");
		String userId = UserUtils.getUser().getId();
		String officeId = UserUtils.getUser().getOffice().getId();
		String officeName = UserUtils.getUser().getOffice().getName();
		String userName = UserUtils.getUser().getName();
		String loginName = UserUtils.getUser().getLoginName();

		data.put("MID", id);
		data.put("TYPE", "reply");
		data.put("MTOID", transsubid);
		data.put("USERID", userId);
		data.put("USERNAME", user);
		data.put("ROLEIDS", roleIds);
		data.put("OFFICEID", officeId);
		data.put("REPLYTEXT", "已转发");
		data.put("TRANSSUBID", transsubid);
		data.put("USERLOGINNAME", loginName);
		data.put("USERNAME", userName);
		data.put("OFFICEID", officeId);
		data.put("OFFICENAME", officeName);// 职位

		String mid = "";
		try {
			JSONObject messVO = messageCommonService.queryMessageInfoById(data);
			JSONObject dataMap = new JSONObject();
			dataMap.put("TID", transtmplid);
			dataMap.put("MTEXT", messVO.getString("MTEXT"));
			dataMap.put("USERID", userId);
			dataMap.put("USERLOGINNAME", loginName);
			dataMap.put("USERNAME", userName);
			dataMap.put("OFFICEID", officeId);
			dataMap.put("OFFICENAME", officeName);// 职位
			dataMap.put("TOLISTSTR", "");
			dataMap.put("IFAUTO", "0");
			dataMap.put("TRANSSUBID", transsubid);
			dataMap.put("SYS", "11");

			JSONObject mess = messageCommonService.createMessageInfo(dataMap);
			if (mess != null) {
				logger.info("发送消息：创建转发消息成功");
				dataMap.put("TYPE", "trans");
				mid = mess.getString("MID");
				JSONObject sendmess = messageSendService.sendMessage(mess);
				String code = sendmess.getString("CODE");
				if ("0".equals(code)) {
					HashMap<String, String> data2 = (HashMap<String, String>) JSON.parseObject(dataMap.toJSONString(),
							Map.class);
					messageService.insertMessageFile(data2);
					JSONArray list = new JSONArray();
					JSONObject midobj = new JSONObject();
					midobj.put("MID", mid);
					midobj.put("OLDMID", id);
					list.add(midobj);
					dataMap.put("DATA", list);
					messageCommonService.updateMassage(dataMap);
				} else {
					logger.info("转发送消息失败：" + sendmess.getString("MSG"));
					data.put("MID", mid);
					messageCommonService.deleteMessage(data);
				}
			}
			logger.info("转发消息成功");
			return true;
		} catch (Exception e) {
			logger.error("转发消息失败" + e.getMessage());
			return false;
		}
	}

	@RequestMapping(value = "totalk")
	public String totalk(HttpServletRequest request, Model model) {
	    String names = request.getParameter("names");
	    String phones = request.getParameter("phones");
	    try {
	        if(!StringUtils.isBlank(names))
            names = java.net.URLDecoder.decode(names, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	    JSONArray list = new JSONArray();
	    String[] namesArr = names.split(",");
	    String[] phonesArr = phones.split(",");
	    String serverIp = Global.getConfig("talk.ip");
        int port = Integer.parseInt(Global.getConfig("talk.port"));
	    for(int i=0;i<namesArr.length;i++){
	        String name = namesArr[i];
            String phone = phonesArr[i];
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("phone", phone);
            list.add(jsonObject);
	    }
	    model.addAttribute("list", list);
	    model.addAttribute("port", port);
	    model.addAttribute("ip", serverIp);
	    return "prss/message/talk";
	}
	@RequestMapping(value = "talk")
	@ResponseBody
	public JSONObject talk(HttpServletRequest request, Model model) {
		String mtoer = request.getParameter("mtoer");
		String mtotype = request.getParameter("mtotype");
		JSONObject rt = new JSONObject();
		boolean result = true;
		try {
			String serverIp = Global.getConfig("talk.ip");
			int port = Integer.parseInt(Global.getConfig("talk.port"));
//			result = NetUtil.isLoclePortUsing(serverIp, port);
//			if (result && mtoer!=null) {
				JSONObject json = new JSONObject();
				json.put("mtoer", mtoer);
				json.put("mtotype", mtotype);
				JSONArray list = messageService.getUserList(json);
				String phones = "";
				String names = "";
				for (int i = 0; i < list.size(); i++) {
					JSONObject user = list.getJSONObject(i);
					String phone = user.getString("PHONE");
					String name = user.getString("NAME");
					phones += phone + ",";
					names += name + ",";
				}
				if (!"".equals(phones)) {
					phones = phones.substring(0, phones.length() - 1);
				}
				rt.put("host", serverIp);
				rt.put("port", port);
				rt.put("phones", phones);
                rt.put("names", names);
				rt.put("list", list);
//			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		rt.put("result", result);
		return rt;
	}

	@RequestMapping(value = "stoptalk")
	@ResponseBody
	public boolean stoptalk(HttpServletRequest request, Model model) {
		String phone = request.getParameter("phone");
		boolean result = false;
		try {
			String serverIp = Global.getConfig("talk.ip");
			result = NetUtil.isLoclePortUsing(serverIp, 6000);
			if (!result) {
				int port = Integer.parseInt(Global.getConfig("talk.port"));
				TalkClient client = new TalkClient(InetAddress.getByName(serverIp), port);
				result = client.stop(phone);
			}
		} catch (Exception e) {

		}

		return result;
	}

	@RequestMapping(value = "removemsgs")
	@ResponseBody
	public String removemsgs(HttpServletRequest request) {
	    
//		JSONObject obj = new JSONObject();
//		obj.put("AID", Global.getConfig("cp.appId"));
//		obj.put("CL", Global.getConfig("cp.cl"));
//		obj.put("DT", "P_" + UserUtils.getUser().getId());
//		 String msgId = request.getParameter("msgId");
//         String mid = request.getParameter("mid");
         String mtoid = request.getParameter("mtoid");
//
//		String str = obj.toJSONString();
		String resultString = "";
		try {
		    JSONObject data = new JSONObject();
		    data.put("MTOID", mtoid);
		    data.put("USERID", "P_"+UserUtils.getUser().getId());
//			resultString = MsgsUtil.remove(str);
//			JSONObject result = (JSONObject) JSON.parseObject(resultString);
//			resultString = result.getString("COD");
		    JSONArray list = new JSONArray();
		    list.add(data);
		    messageCommonService.deleteOfflineMessage(list);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return resultString;
	}

	@RequestMapping(value = "getOfflineCount")
	@ResponseBody
	public int getOfflineCount(HttpServletRequest request) {
		JSONObject data = new JSONObject();
		data.put("USERID", "P_" + UserUtils.getUser().getId());
		int count = 0;
		try {
			count = messageCommonService.getOfflineMessageCount(data);
		} catch (Exception e) {

		}
		return count;
	}


	@RequestMapping(value = "fdSendMessage")
	@ResponseBody
	public int fdSendMessage(HttpServletRequest request, Model model) {
		JSONObject data = new JSONObject();
		data.put("USERID", "P_" + UserUtils.getUser().getId());
		int count = 0;
		try {
			count = messageCommonService.getOfflineMessageCount(data);
		} catch (Exception e) {

		}
		return count;
	}
	/**
	 * 
	 *Discription:弹出框转发.
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2017年11月28日 Maxx [变更描述]
	 */
    @RequestMapping(value = "popTrans", method = RequestMethod.POST)
    @ResponseBody
    public boolean popTrans(HttpServletRequest request) {
        String id = request.getParameter("id");
        String transtmplid = request.getParameter("transtmplid");
        String transsubid = request.getParameter("mtoid");
        JSONObject data = new JSONObject();
        String userId = UserUtils.getUser().getId();
        String officeId = UserUtils.getUser().getOffice().getId();
        String officeName = UserUtils.getUser().getOffice().getName();
        String userName = UserUtils.getUser().getName();
        String loginName = UserUtils.getUser().getLoginName();
        String user = UserUtils.getUser().getName();
        List<String> roleIdList = UserUtils.getUser().getRoleIdList();
        String roleIds = StringUtils.join(roleIdList.toArray(), ",");
        try {
            data.put("MID", id);
            data.put("TYPE", "reply");
            data.put("MTOID", transsubid);
            data.put("USERID", userId);
            data.put("USERNAME", user);
            data.put("ROLEIDS", roleIds);
            data.put("OFFICEID", officeId);
            data.put("REPLYTEXT", "已转发");
            data.put("TRANSSUBID", transsubid);
            data.put("USERLOGINNAME", loginName);
            data.put("USERNAME", userName);
            data.put("OFFICEID", officeId);
            data.put("OFFICENAME", officeName);// 职位
            data.put("TRANSTEMPLID", transtmplid);//转发模板ID
            boolean result = messageSendService.transMassage(data);
            logger.info("转发消息结果:" + result);
            return result;
        } catch (Exception e) {
            logger.error("转发消息失败" + e.getMessage());
            return false;
        }
    }
	/**
	 * 
	 *Discription:收藏.
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:l.ran@neusoft.com
	 *@update:2017年12月13日 Maxx [变更描述]
	 */
	@RequestMapping(value = "favorite", method = RequestMethod.POST)
    @ResponseBody
    public boolean favorite(HttpServletRequest request) {
	    JSONObject formData = new JSONObject();
        try {
            String mid = request.getParameter("mid");
            String isFavorite = request.getParameter("isFavorite");
            String type = request.getParameter("type");
            List<JobKind> jobKinds = UserUtils.getCurrentJobKind();
            String jobKind = "";
            if(jobKinds.size()>0){
                jobKind = UserUtils.getCurrentJobKind().get(0).getKindCode();
            } 
            formData.put("jobKind", jobKind);
            formData.put("mid", mid);
            formData.put("type", type);
            if("1".equals(isFavorite)){
                messageService.insertFavorite(formData);
            } else {
                messageService.deleteFavorite(formData);
            }
            return true;
        } catch (Exception e) {
            logger.error("消息操作失败" + e.getMessage());
            return false;
        }
    }
}
