package com.neusoft.prss.OSCARSecurityRep.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.IncomingMailReceipt.entity.HandoverBill;
/**
 * OSCAR保障管理报告DAO层
 * @author lwg
 * @date 2018/1/8
 */
@MyBatisDao
public interface OSCARSecurityRepDao {


	/**
	 * 获取进港货邮主表数据
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getData(Map<String, Object> map);

	/**
	 * 获取检查项目
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getItemData(Map<String, Object> map);

	/**
	 * 获取备注信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getRemarkInfo(Map<String, Object> map);

	/**
	 * 获取文件类型
	 * @param fileId
	 */
	String getDownloadFileType(String fileId);

	/**
	 * 获取备注图片/音频/视频组
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getRemarkShow(Map<String, Object> map);


}
