package com.neusoft.prss.OSCARSecurityRep.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neusoft.prss.OSCARSecurityRep.dao.OSCARSecurityRepDao;

/**
 * OSCAR保障管理报告服务层
 * @author lwg
 * @date 2018/1/8
 */
@Service
@Transactional(readOnly = true)
public class OSCARSecurityRepService {
	// 进港
	@Autowired
	private OSCARSecurityRepDao oSCARSecurityRepDao;


	/**
	 * 获取进港货邮主表数据
	 * @param begin
	 * @param end
	 * @param pageSign 
	 * @param searchId 
	 * @param searchData 
	 */
	public Map<String, Object> getData(int begin, int end, String startDate, String endDate, String searchData) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		map.put("totalSize", "0");
		if(null != startDate && !"".equals(startDate)) {			
			map.put("startTime", startDate);
		}
		if(null != endDate && !"".equals(endDate)) {			
			map.put("endTime", endDate);
		}
		if(null != searchData && !"".equals(searchData)) {			
			map.put("searchData", "%"+searchData+"%");
		}
		result.put("total", oSCARSecurityRepDao.getData(map).size());			
		map.put("begin", begin);
		map.put("end", end);
		map.put("totalSize", "1");
		list = oSCARSecurityRepDao.getData(map);
		result.put("rows", list);
		return result;
		
	}


	public List<Map<String, Object>> getData(String searchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = null;
		map.put("totalSize", "0");
		map.put("searchId", searchId);
		// 航班信息
		resultList = oSCARSecurityRepDao.getData(map);
		if(resultList.size() != 0) {
			// 获取检查项信息
			List<Map<String, Object>> list = oSCARSecurityRepDao.getItemData(map);
			// 获取备注信息
			List<Map<String, Object>> list1 =  oSCARSecurityRepDao.getRemarkInfo(map);
			// 备注信息
			Map<String, Object> remarkMap;
			String strs;
			String[] str;
			String value = "0";
			for(int i = 0; i < list1.size(); i++) {
				remarkMap = list1.get(i);
				strs = remarkMap.get("FILETYPE")+"";
				if(!"".equals(strs)) {
					str = strs.split(",");
					value = str[0];
					for(int j = 0; j < str.length; j++) {
						if(value.equals(str[j])) {
							continue;
						} else {
							value = "9";
							break;
						}
					}
				}
				remarkMap.put("FILETYPE", value);
			}
			resultList.get(0).put("itemList",list);
			resultList.get(0).put("remarkList", list1);
			
		}
		
		return resultList;
		
	}

	/**
	 * 获取文件类型
	 * @param string
	 * @return
	 */
	public String getDownloadFileType(String fileId) {
		return oSCARSecurityRepDao.getDownloadFileType(fileId);
	}

	/**
	 * 查看图片/视频/音频
	 * @param searchId
	 * @param sign
	 * @param flag 
	 * @return
	 */
	public List<Map<String, Object>> getRemarkShow(String searchId, String sign, boolean flag) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!flag) {
			map.put("billId", searchId);			
		} else {
			map.put("remarkId", searchId);			
		}
		map.put("fileType", sign);
		return oSCARSecurityRepDao.getRemarkShow(map);
	}



	


}
