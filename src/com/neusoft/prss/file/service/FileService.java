package com.neusoft.prss.file.service;

import java.io.IOException;
import java.net.SocketException;

import com.alibaba.fastjson.JSONObject;

public interface FileService {
	/**
	 * 
	 * Discription:文档上传服务接口.
	 * 
	 * @param file
	 * @param fileTypeId
	 * @param userName
	 * @return
	 * @throws IOException
	 * @return:返回值意义
	 * @author:Heqg
	 * @throws SocketException
	 * @update:2017年8月28日 Heqg [变更描述]
	 */
	public JSONObject doUploadFile(byte[] inByteArray, String fileTypeId, String fileUploadUser, String fileName)
			throws SocketException, IOException;

	public byte[] doDownLoadFile(String fileId) throws SocketException, IOException;

	public void doDeleteFile(String fileId) throws SocketException, IOException;
}
