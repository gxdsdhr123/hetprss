package com.neusoft.prss.common.service;

import com.neusoft.prss.common.dao.VideoCommonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoCommonService {

	@Autowired
	private VideoCommonDAO videoCommonDAO;
	
	public String getServerName() {
		return videoCommonDAO.getServerName();
	}

	public String getServerPort() {
		return videoCommonDAO.getServerPort();
	}

	public String getServerPassword() {
		return videoCommonDAO.getServerPassword();
	}

	public String getServerUser() {
		return videoCommonDAO.getServerUser();
	}

	public String getFilePath(String fType, String code) {
		return videoCommonDAO.getFilePath(fType, code);
	}
}
