package com.neusoft.prss.common.web;

import com.neusoft.prss.common.service.VideoCommonService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "${adminPath}/video/common")
public class VideoCommonController {
	
	@Autowired
	private VideoCommonService videoCommonService;
	
	@RequestMapping(value = "getServerInfo")
    @ResponseBody
    public String getServerInfo(String fType, String code) {

        JSONObject info = new JSONObject();

        info.put("serverName", videoCommonService.getServerName());
        info.put("serverPort", videoCommonService.getServerPort());
        info.put("serverPassword", videoCommonService.getServerPassword());
        info.put("serverUser", videoCommonService.getServerUser());
        info.put("avPath", videoCommonService.getFilePath(fType, code));

        return info.toString();
    }
}
