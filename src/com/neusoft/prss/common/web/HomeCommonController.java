package com.neusoft.prss.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.service.HomeCommonService;

@Controller
@RequestMapping(value = "${adminPath}/home/common")
public class HomeCommonController {
	
	@Autowired
	private HomeCommonService homeCommonService; 
	
	@RequestMapping(value = "hasDynamicRole")
    @ResponseBody
    public String hasDynamicRole() {
		String userId = UserUtils.getUser().getId();
        String  res = homeCommonService.hasDynamicRole(userId);
        return res;
    }
}
