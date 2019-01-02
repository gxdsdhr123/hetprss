/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月29日 下午4:39:34
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.mobile.service;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.mobile.dao.MobileVersionDao;

@Service
@Transactional(readOnly = true)
public class MobileVersionService extends BaseService {
	
	@Autowired
    private MobileVersionDao mobileVersionDao;

    @Resource
    private FileService fileservice;
    
    public JSONArray getMobileVersionDate(){
    	return mobileVersionDao.getMobileVersionDate();
    }
    
    public JSONObject getMobileVersionById(String id){
    	return mobileVersionDao.getMobileVersionById(id);
    }
    
    public String getMaxVersion(){
    	return mobileVersionDao.getMaxVersion();
    }
    
    public void delVersion(String id){
    	String fileId = mobileVersionDao.getFileId(id);
    	try {
			fileservice.doDeleteFile(fileId);
		} catch (SocketException e) {
			logger.error(e.toString());
		} catch (IOException e) {
			logger.error(e.toString());
		}
    	mobileVersionDao.delVersion(id);
    }
    
    public void saveVersion(JSONObject versionDate){
    	mobileVersionDao.saveVersion(versionDate);
    }
    
    public void addVersion(JSONObject versionDate, MultipartFile apkFile){
    	
    	String userId = UserUtils.getUser().getId();
    	String fileId = "";
    	try {
			JSONObject fileInfo = fileservice.doUploadFile(apkFile.getBytes(), "41", userId, apkFile.getOriginalFilename());
			if("succeed".equals(fileInfo.getString("result"))){
				fileId = fileInfo.getString("info");
			}
		} catch (SocketException e) {
			logger.error(e.toString());
		} catch (IOException e) {
			logger.error(e.toString());
		}
    	
    	mobileVersionDao.insertVersion(versionDate, fileId);
    	
    }
    
    public String getFileName(String id){
    	return mobileVersionDao.getFileName(id);
    }
    
    public String getFileId(String id){
    	return mobileVersionDao.getFileId(id);
    }

}
