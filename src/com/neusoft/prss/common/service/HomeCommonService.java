package com.neusoft.prss.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.prss.common.dao.HomeCommonDAO;

@Service
public class HomeCommonService {

	@Autowired
	private HomeCommonDAO homeCommonDAO;
	
	public String hasDynamicRole(String userId) {
		return homeCommonDAO.hasDynamicRole(userId);
	}

}
