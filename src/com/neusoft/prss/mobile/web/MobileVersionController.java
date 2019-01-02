/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月29日 下午4:40:09
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.mobile.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.common.util.MD5Util;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.mobile.service.MobileVersionService;

@Controller
@RequestMapping(value = "${adminPath}/mobile/mobileVersion")
public class MobileVersionController extends BaseController {
	
	@Autowired
    private MobileVersionService mobileVersionService;
	
	@Autowired
    private FileService fileService;
	
	@RequestMapping(value = {"list"})
    public String list() {
        return "prss/mobile/mobileVersionList";
    }
	
	@ResponseBody
	@RequestMapping(value = "getMobileVersionData")
	public String getMobileVersionData() {
		JSONArray json = mobileVersionService.getMobileVersionDate();
		String result = json.toJSONString();
		return result;
	}
	
	@RequestMapping(value = {"mobileVersionForm"})
    public String editMobileVersion(Model model, String id) {
		JSONObject json = mobileVersionService.getMobileVersionById(id);
		model.addAttribute("vsnJson", json);
        return "prss/mobile/mobileVersionForm";
    }
	
	@RequestMapping(value = {"toAddVersion"})
    public String addVersion(Model model) {
		String maxVsn = mobileVersionService.getMaxVersion();
		model.addAttribute("maxVsn", maxVsn);
        return "prss/mobile/addVersion";
    }
	
	@ResponseBody
	@RequestMapping(value = "delVersion")
	public String delVersion(String id) {
		mobileVersionService.delVersion(id);
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "saveVersion")
	public String saveVersion(String versionDate) {
		JSONObject json = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(versionDate));
		mobileVersionService.saveVersion(json);
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "addVersion")
	public String addVersion(@RequestParam(value = "apkFile", required = false)MultipartFile apkFile, String MD5, String updateversion, String updatedesc, String updateurl, String serverurl, String pushurl, HttpServletRequest request) {
		
		try {
			
			String MD5Java = MD5Util.getFileMD5String(apkFile);
			if(!MD5Java.equals(MD5)) {
				return "error";
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}
		
		JSONObject json = new JSONObject();
		json.put("updateversion", updateversion);
		json.put("updatedesc", updatedesc);
		json.put("updateurl", updateurl);
		json.put("serverurl", serverurl);
		json.put("pushurl", pushurl);
		
		mobileVersionService.addVersion(json, apkFile);
		
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "downloadApk")
	public void downloadApk(String id, HttpServletRequest request, HttpServletResponse response) {
		String fileId = mobileVersionService.getFileId(id);
		try {
			byte[] is = fileService.doDownLoadFile(fileId);
			response.reset();// 重设response信息，解决部分浏览器文件名非中文问题
	        response.setContentType("application/octet-stream; charset=utf-8");
	        String agent = (String) request.getHeader("USER-AGENT");
	        String downloadFileName = mobileVersionService.getFileName(fileId);
	        try {
	            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
	                downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(downloadFileName.getBytes("UTF-8")))) + "?=";
	            } else {
	                downloadFileName = java.net.URLEncoder.encode(downloadFileName, "UTF-8");
	            }
	        } catch (Exception e) {
	            logger.error("response信息设置失败" + e.getMessage());
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
	        OutputStream out = null;
	        try {
	            out = response.getOutputStream();
	            out.write(is);
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
		} catch (SocketException e1) {
			logger.error(e1.toString());
		} catch (IOException e1) {
			logger.error(e1.toString());
		}
	}
	
}
