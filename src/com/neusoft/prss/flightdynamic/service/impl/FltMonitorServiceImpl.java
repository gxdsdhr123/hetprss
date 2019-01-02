package com.neusoft.prss.flightdynamic.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.framework.common.utils.DateUtils;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.common.util.BeanUtils;
import com.neusoft.prss.common.util.tree.TreeNode;
import com.neusoft.prss.flightdynamic.bean.FlitMonitorData;
import com.neusoft.prss.flightdynamic.bean.FlitMonitorData.MainNode;
import com.neusoft.prss.flightdynamic.bean.FlitMonitorData.OtherNode;
import com.neusoft.prss.flightdynamic.bean.FlitMonitorData.TimeNode;
import com.neusoft.prss.flightdynamic.dao.FltMonitorDao;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.flightdynamic.entity.NodeInfo;
import com.neusoft.prss.flightdynamic.entity.PassengerInfo;
import com.neusoft.prss.flightdynamic.entity.PersonEvent;
import com.neusoft.prss.flightdynamic.entity.PersonEvent.TimeData;
import com.neusoft.prss.flightdynamic.service.FdHisService;
import com.neusoft.prss.flightdynamic.service.FltMonitorService;

@Service
public class FltMonitorServiceImpl implements FltMonitorService {

	@Autowired
	private FltMonitorDao fltMonitorDao;
	@Autowired
	private FdHisService fdHisService;
	
	// 是否跳过状态设置
	private boolean passStatus = false;
	
	@Override
	public FltInfo getFltInfo(String inFltid, String outFltid, String hisFlag) throws Exception{
		FltInfo fltInfo = new FltInfo();
		if(!StringUtils.isEmpty(inFltid)){
			BeanUtils.copyPropertiesIgnoreNull(getInFltInfo(inFltid,hisFlag),fltInfo);
		}
		if(!StringUtils.isEmpty(outFltid)){
			BeanUtils.copyPropertiesIgnoreNull(getOutFltInfo(outFltid,hisFlag),fltInfo);
		}
		return fltInfo;
	}


	@Override
	public Map<String, Object> getFltmonitorData(String inFltid,
			String outFltid, String isY, String hisFlag)  throws Exception{
		Map<String, Object> responseMap = new HashMap<String, Object>();
		// 判断进出港类型及远近机位
		String fltType = getFltType(inFltid, outFltid, isY);
		switch(fltType){
			// 单进港 近
			case "a0n":
			// 单进港 远
			case "a0y":
			{
				responseMap.put("data",getFlitMonitorData_a0(inFltid, isY,hisFlag));
				responseMap.put("code", "0");
				return responseMap;
			}
			// 单出港 近
			case "d0n":
			// 单出港 远
			case "d0y":
			{
				responseMap.put("data",getFlitMonitorData_d0(outFltid, isY,hisFlag));
				responseMap.put("code", "0");
				return responseMap;
			}
			// 进出港 近
			case "a1n":
			// 进出港 远
			case "a1y":
			{
				responseMap.put("data",getFlitMonitorData_a1(inFltid,outFltid, isY,hisFlag));
				responseMap.put("code", "0");
				return responseMap;
			}
			default:
			{
				responseMap.put("code", "2");
				responseMap.put("desc", "参数不正确，未找到进出港类型！");
				return responseMap;
			}
		}
	}
	
	/**
	 * 单进港
	 * @param fltid
	 * @param hisFlag 
	 * @return
	 * @throws Exception
	 */
	private FlitMonitorData getFlitMonitorData_a0(String fltid, String isY, String hisFlag) throws Exception{
		FlitMonitorData resultData = new  FlitMonitorData();
		Map<String, Object> dataMap = getFltmonitorData(fltid , null ,hisFlag);
		if(dataMap == null){
			return resultData;
		}
		/******** 时间添加 *******/
		
		///////////主流程时间
		createTextTimeNode("mainNode_1", "FLY_TIME_TEXT", dataMap,resultData); //航程
		createTextTimeNode("mainNode_2", "HX_TIME_TEXT", dataMap,resultData); //滑行时间
		if("Y".equals(isY)){
			createTimeNode("mainNode_3", "KTCKJ_ECOST", "KTCKJ_ACOST", dataMap, resultData); //客梯车靠接
		}else{
			createTimeNode("mainNode_3", "ABLQ_KJ_ECOST", "ABLQ_KJ_ACOST", dataMap, resultData); //廊桥靠接
		}
		createTimeNode("mainNode_4", "KCM_ECOST", "KCM_ACOST", dataMap, resultData); //开客舱门
		createTimeNode("mainNode_5", "XK_ECOST", "XK_ACOST", dataMap, resultData); //下客完成
		createTimeNode("mainNode_6", "QJWC_ECOST", "QJWC_ACOST", dataMap, resultData); //清洁完成
		if("Y".equals(isY)){
			createTimeNode("mainNode_7", "KTCCL_ECOST", "KTCCL_ACOST", dataMap, resultData); //客梯车撤离
		}else{
			createTimeNode("mainNode_7", "ABLQ_CL_ECOST", "ABLQ_CL_ACOST", dataMap, resultData); //廊桥撤离
		}
		
		///////////分支流程时间
		createTimeNode("otherNode_0", "FXWC_ECOST", "FXWC_ACOST", dataMap, resultData); //航线放行
		createTimeNode("otherNode_1", "QSC_ECOST", "QSC_ACOST", dataMap, resultData); //清水车完成
		createTimeNode("otherNode_2", "WSC_ECOST", "WSC_ACOST", dataMap, resultData); //污水车完成
		createTimeNode("otherNode_3", "YLJY_ECOST", "YLJY_ACOST", dataMap, resultData); //加油完成
		createTimeNode("otherNode_4", "PCC_ECOST", "PCC_ACOST", dataMap, resultData); //配餐完成
		createTimeNode("otherNode_5", "KHCM_ECOST", "KHCM_ACOST", dataMap, resultData); //货舱开门
		createTimeNode("otherNode_6", "GHCM_ECOST", "GHCM_ACOST", dataMap, resultData); //货舱关门
		if("Y".equals(isY)){
			createTimeNode("otherNode_7", "JGBDC_ECOST", "JGBDC_ACOST", dataMap, resultData); //进港摆渡车完成
		}
		/******** 主流程节点 *******/
		passStatus = false;
		if("Y".equals(isY)){
			createMainNode("mainNode_8", "TC_KTCCL_FTM", dataMap, resultData);	//结束
			createMainNode("mainNode_7", "TC_KTCCL_FTM", dataMap, resultData);	//客梯车撤离
		}else{
			createMainNode("mainNode_8", "ABLQ_CL_FTM", dataMap, resultData);	//结束
			createMainNode("mainNode_7", "ABLQ_CL_FTM", dataMap, resultData);	//廊桥撤离
		}
		createMainNode("mainNode_6", "QJ_WC_FTM", dataMap, resultData);	//清洁完成
		createMainNode("mainNode_5", "DFJJ_XK_FTM", dataMap, resultData);	//下客完成
		createMainNode("mainNode_4", "DIFUFUWU_KCM_FTM", dataMap, resultData);	//开客舱门
		if("Y".equals(isY)){
			createMainNode("mainNode_3", "TC_KTCKJ_FTM", dataMap, resultData);	//客梯车靠接
		}else{
			createMainNode("mainNode_3", "ABLQ_KJ_FTM", dataMap, resultData);	//廊桥靠接
		}
		createMainNode("mainNode_2", "JIWU_DLD_FTM", dataMap, resultData);	//挡轮档
		createMainNode("mainNode_1", "ATA", dataMap, resultData);	//落地
		createMainNode("mainNode_0", "ATD", dataMap, resultData);	//起飞
		
		/******** 分支流程节点 *******/
		createOtherNode("otherNode_0", "JIWU_FXWC_FTM", dataMap, resultData);	//航线放行
		createOtherNode("otherNode_1", "TC_QSC_FTM", dataMap, resultData);	//清水车完成
		createOtherNode("otherNode_2", "TC_WSC_FTM", dataMap, resultData);	//污水车完成
		createOtherNode("otherNode_3", "YOULIAOJIAYOU_WC_FTM", dataMap, resultData);	//加油完成
		createOtherNode("otherNode_4", "TC_PCC_FTM", dataMap, resultData);	//配餐完成
		createOtherNode("otherNode_5", "ZX_KHCM_FTM", dataMap, resultData);	//货舱开门
		createOtherNode("otherNode_6", "ZX_GHCM_FTM", dataMap, resultData);	//货舱关门
		if("Y".equals(isY)){
			createOtherNode("otherNode_7", "TC_JGBDC_FTM", dataMap, resultData);	//进港摆渡车完成
		}
		return resultData;
	}

	
	/**
	 * 进出港近机位
	 * @param inFltid
	 * @param outFltid
	 * @param hisFlag 
	 * @return
	 * @throws Exception
	 */
	private FlitMonitorData getFlitMonitorData_a1(String inFltid,	String outFltid, String isY, String hisFlag) throws Exception{
		FlitMonitorData resultData = new  FlitMonitorData();
		Map<String, Object> dataMap = getFltmonitorData(inFltid, outFltid ,hisFlag);
		if(dataMap == null){
			return resultData;
		}
		/******** 时间添加 *******/
		
		///////////主流程时间
		createTextTimeNode("mainNode_1", "FLY_TIME_TEXT", dataMap,resultData); //航程
		createTextTimeNode("mainNode_2", "HX_TIME_TEXT", dataMap,resultData); //滑行时间
		if("Y".equals(isY)){
			createTimeNode("mainNode_3", "KTCKJ_ECOST", "KTCKJ_ACOST", dataMap, resultData); //客梯车靠接
		}else{
			createTimeNode("mainNode_3", "ABLQ_KJ_ECOST", "ABLQ_KJ_ACOST", dataMap, resultData); //廊桥靠接
		}
		createTimeNode("mainNode_4", "KCM_ECOST", "KCM_ACOST", dataMap, resultData); //开客舱门
		createTimeNode("mainNode_5", "XK_ECOST", "XK_ACOST", dataMap, resultData); //下客完成
		createTimeNode("mainNode_6", "QJWC_ECOST", "QJWC_ACOST", dataMap, resultData); //清洁完成
		createTimeNode("mainNode_7", "DJKS_ECOST", "DJKS_ACOST", dataMap, resultData); //登机开始
		createTimeNode("mainNode_8", "DJJS_ECOST", "DJJS_ACOST", dataMap, resultData); //登机结束
		createTimeNode("mainNode_9", "GCM_ECOST", "GCM_ACOST", dataMap, resultData); //关客舱门
		if("Y".equals(isY)){
			createTimeNode("mainNode_10", "KTCCL_ECOST", "KTCCL_ACOST", dataMap, resultData); //客梯车撤离
		}else{
			createTimeNode("mainNode_10", "ABLQ_CL_ECOST", "ABLQ_CL_ACOST", dataMap, resultData); //廊桥撤离
		}
		createTimeNode("mainNode_11", "CLD_ECOST", "CLD_ACOST", dataMap, resultData); //撤轮档
		createTextTimeNode("mainNode_12", "QF_TIME_TEXT", dataMap,resultData); //起飞
		///////////分支流程时间
		createTimeNode("otherNode_0", "FXWC_ECOST", "FXWC_ACOST", dataMap, resultData); //航线放行
		createTimeNode("otherNode_1", "QSC_ECOST", "QSC_ACOST", dataMap, resultData); //清水车完成
		createTimeNode("otherNode_2", "WSC_ECOST", "WSC_ACOST", dataMap, resultData); //污水车完成
		createTimeNode("otherNode_3", "YLJY_ECOST", "YLJY_ACOST", dataMap, resultData); //加油完成
		createTimeNode("otherNode_4", "PCC_ECOST", "PCC_ACOST", dataMap, resultData); //配餐完成
		createTimeNode("otherNode_5", "KHCM_ECOST", "KHCM_ACOST", dataMap, resultData); //货舱开门
		createTimeNode("otherNode_6", "GHCM_ECOST", "GHCM_ACOST", dataMap, resultData); //货舱关门
		if("Y".equals(isY)){
			createTimeNode("otherNode_7", "JGBDC_ECOST", "JGBDC_ACOST", dataMap, resultData); //进港摆渡车完成
			createTimeNode("otherNode_8", "CGBDC_ECOST", "CGBDC_ACOST", dataMap, resultData); //出港摆渡车完成
		}
		
		/******** 主流程节点 *******/
		passStatus = false;
		createMainNode("mainNode_12", "OUT_ATD", dataMap, resultData);	//起飞
		createMainNode("mainNode_11", "JIWU_CLD_FTM", dataMap, resultData);	//撤轮档
		if("Y".equals(isY)){
			createMainNode("mainNode_10", "TC_KTCCL_FTM", dataMap, resultData);	//客梯车撤离
		}else{
			createMainNode("mainNode_10", "ABLQ_CL_FTM", dataMap, resultData);	//廊桥撤离
		}
		createMainNode("mainNode_9", "DIFUFUWU_GCM_FTM", dataMap, resultData);	//关客舱门
		createMainNode("mainNode_8", "BRD_ETM", dataMap, resultData);	//登机结束
		createMainNode("mainNode_7", "BRD_BTM", dataMap, resultData);	//登机开始
		createMainNode("mainNode_6", "QJ_WC_FTM", dataMap, resultData);	//清洁完成
		createMainNode("mainNode_5", "DFJJ_XK_FTM", dataMap, resultData);	//下客完成
		createMainNode("mainNode_4", "DIFUFUWU_KCM_FTM", dataMap, resultData);	//开客舱门
		if("Y".equals(isY)){
			createMainNode("mainNode_3", "TC_KTCKJ_FTM", dataMap, resultData);	//客梯车靠接
		}else{
			createMainNode("mainNode_3", "ABLQ_KJ_FTM", dataMap, resultData);	//廊桥靠接
		}
		createMainNode("mainNode_2", "JIWU_DLD_FTM", dataMap, resultData);	//挡轮档
		createMainNode("mainNode_1", "IN_ATA", dataMap, resultData);	//落地
		createMainNode("mainNode_0", "IN_ATD", dataMap, resultData);	//起飞
		
		/******** 分支流程节点 *******/
		createOtherNode("otherNode_0", "JIWU_FXWC_FTM", dataMap, resultData);	//航线放行
		createOtherNode("otherNode_1", "TC_QSC_FTM", dataMap, resultData);	//清水车完成
		createOtherNode("otherNode_2", "TC_WSC_FTM", dataMap, resultData);	//污水车完成
		createOtherNode("otherNode_3", "YOULIAOJIAYOU_WC_FTM", dataMap, resultData);	//加油完成
		createOtherNode("otherNode_4", "TC_PCC_FTM", dataMap, resultData);	//配餐完成
		createOtherNode("otherNode_5", "ZX_KHCM_FTM", dataMap, resultData);	//货舱开门
		createOtherNode("otherNode_6", "ZX_GHCM_FTM", dataMap, resultData);	//货舱关门
		if("Y".equals(isY)){
			createOtherNode("otherNode_7", "TC_JGBDC_FTM", dataMap, resultData);	//进港摆渡车完成
			createOtherNode("otherNode_8", "TC_CGBDC_FTM", dataMap, resultData);	//出港摆渡车完成
		}
		
		return resultData;
	}

	
	/**
	 * 单出港
	 * @param fltid
	 * @param hisFlag 
	 * @return
	 * @throws Exception
	 */
	private FlitMonitorData getFlitMonitorData_d0(String fltid, String isY, String hisFlag) throws Exception{
		FlitMonitorData resultData = new  FlitMonitorData();
		Map<String, Object> dataMap = getFltmonitorData(null, fltid ,hisFlag);
		if(dataMap == null){
			return resultData;
		}
		/******** 时间添加 *******/
		
		///////////主流程时间
		createTimeNode("mainNode_1", null, null, dataMap, resultData); //廊桥靠接/客梯车靠接
		createTimeNode("mainNode_2", "QJWC_ECOST", "QJWC_ACOST", dataMap, resultData); //清洁完成
		createTimeNode("mainNode_3", "DJKS_ECOST", "DJKS_ACOST", dataMap, resultData); //登机开始
		createTimeNode("mainNode_4", "DJJS_ECOST", "DJJS_ACOST", dataMap, resultData); //登机结束
		createTimeNode("mainNode_5", "GCM_ECOST", "GCM_ACOST", dataMap, resultData); //关客舱门
		if("Y".equals(isY)){
			createTimeNode("mainNode_6", "KTCCL_ECOST", "KTCCL_ACOST", dataMap, resultData); //客梯车撤离
		}else{
			createTimeNode("mainNode_6", "ABLQ_CL_ECOST", "ABLQ_CL_ACOST", dataMap, resultData); //廊桥撤离
		}
		createTimeNode("mainNode_7", "CLD_ECOST", "CLD_ACOST", dataMap, resultData); //撤轮档
		createTextTimeNode("mainNode_8", "QF_TIME_TEXT", dataMap,resultData); //起飞
		///////////分支流程时间
		createTimeNode("otherNode_0", "FXWC_ECOST", "FXWC_ACOST", dataMap, resultData); //航线放行
		createTimeNode("otherNode_1", "QSC_ECOST", "QSC_ACOST", dataMap, resultData); //清水车完成
		createTimeNode("otherNode_2", "WSC_ECOST", "WSC_ACOST", dataMap, resultData); //污水车完成
		createTimeNode("otherNode_3", "YLJY_ECOST", "YLJY_ACOST", dataMap, resultData); //加油完成
		createTimeNode("otherNode_4", "PCC_ECOST", "PCC_ACOST", dataMap, resultData); //配餐完成
		createTimeNode("otherNode_5", "KHCM_ECOST", "KHCM_ACOST", dataMap, resultData); //货舱开门
		createTimeNode("otherNode_6", "GHCM_ECOST", "GHCM_ACOST", dataMap, resultData); //货舱关门
		if("Y".equals(isY)){
			createTimeNode("otherNode_8", "CGBDC_ECOST", "CGBDC_ACOST", dataMap, resultData); //出港摆渡车完成
		}
		
		/******** 主流程节点 *******/
		passStatus = false;
		createMainNode("mainNode_8", "ATD", dataMap, resultData);	//起飞
		createMainNode("mainNode_7", "JIWU_CLD_FTM", dataMap, resultData);	//撤轮档
		if("Y".equals(isY)){
			createMainNode("mainNode_6", "TC_KTCCL_FTM", dataMap, resultData);	//客梯车撤离
		}else{
			createMainNode("mainNode_6", "ABLQ_CL_FTM", dataMap, resultData);	//廊桥撤离
		}
		createMainNode("mainNode_5", "DIFUFUWU_GCM_FTM", dataMap, resultData);	//关客舱门
		createMainNode("mainNode_4", "BRD_ETM", dataMap, resultData);	//登机结束
		createMainNode("mainNode_3", "BRD_BTM", dataMap, resultData);	//登机开始
		createMainNode("mainNode_2", "QJ_WC_FTM", dataMap, resultData);	//清洁完成
		if("Y".equals(isY)){
			createMainNode("mainNode_1", "TC_KTCKJ_FTM", dataMap, resultData);	//客梯车靠接
		}else{
			createMainNode("mainNode_1", "ABLQ_KJ_FTM", dataMap, resultData);	//廊桥靠接
		}
		createMainNode("mainNode_0", null, dataMap, resultData);	//开始
		
		/******** 分支流程节点 *******/
		createOtherNode("otherNode_0", "JIWU_FXWC_FTM", dataMap, resultData);	//航线放行
		createOtherNode("otherNode_1", "TC_QSC_FTM", dataMap, resultData);	//清水车完成
		createOtherNode("otherNode_2", "TC_WSC_FTM", dataMap, resultData);	//污水车完成
		createOtherNode("otherNode_3", "YOULIAOJIAYOU_WC_FTM", dataMap, resultData);	//加油完成
		createOtherNode("otherNode_4", "TC_PCC_FTM", dataMap, resultData);	//配餐完成
		createOtherNode("otherNode_5", "ZX_KHCM_FTM", dataMap, resultData);	//货舱开门
		createOtherNode("otherNode_6", "ZX_GHCM_FTM", dataMap, resultData);	//货舱关门
		if("Y".equals(isY)){
			createOtherNode("otherNode_7", "TC_CGBDC_FTM", dataMap, resultData);	//出港摆渡车完成
		}
		
		return resultData;
	}
	

	/**
	 * 判断进出港类型及远近机位
	 * @param inFltid
	 * @param outFltid
	 * @param isY
	 * @return
	 */
	private String getFltType(String inFltid,String outFltid, String isY) throws Exception{
		if( StringUtils.isEmpty(outFltid)){
			if("Y".equals(isY)){
				return "a0y";
			}else{
				return "a0n";
			}
		}else if( StringUtils.isEmpty(inFltid)){
			if("Y".equals(isY)){
				return "d0y";
			}else{
				return "d0n";
			}
		}else{
			if("Y".equals(isY)){
				return "a1y";
			}else{
				return "a1n";
			}
		}
	}
	
	/**
	 * 创建文本类型时间节点
	 * @param id
	 * @param nodeTextStr
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	private TimeNode createTextTimeNode(String id, String nodeTextStr, Map<String, Object> dataMap,FlitMonitorData resultData) throws Exception{
		TimeNode timeNode = new TimeNode();
		timeNode.setId(id);
		if(dataMap!=null){
			timeNode.setNodeText(((String)dataMap.get(nodeTextStr)));
		}
		resultData.getTimeDatas().put(timeNode.getId(), timeNode);
		return timeNode;
	}
	
	/**
	 * 创建时间节点
	 * @param id
	 * @param eTimeStr
	 * @param aTimeStr
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	private TimeNode createTimeNode(String id, String eTimeStr, String aTimeStr, Map<String, Object> dataMap,FlitMonitorData resultData) throws Exception{
		TimeNode timeNode = new TimeNode();
		timeNode.setId(id);
		if(eTimeStr!=null && dataMap!=null){
			Object eTime = dataMap.get(eTimeStr);
			if(eTime!=null){
				timeNode.seteTime(Integer.parseInt(eTime.toString()));
			}
		}
		if(aTimeStr!=null && dataMap!=null){
			Object aTime = dataMap.get(aTimeStr);
			if(aTime!=null){
				timeNode.setaTime(Integer.parseInt(aTime.toString()));
			}
		}
		// 根据预计时间和实际时间设置状态
		timeNode.setStatus(getStatus(timeNode.geteTime(), timeNode.getaTime()));
		resultData.getTimeDatas().put(timeNode.getId(), timeNode);
		return timeNode;
	}
	
	/**
	 * 创建主流程节点
	 * @param id
	 * @param nodeTimeStr
	 * @param dataMap
	 * @param resultData
	 * @return
	 * @throws Exception
	 */
	private MainNode createMainNode(String id,String nodeTimeStr,Map<String, Object> dataMap,
			FlitMonitorData resultData) throws Exception{
		MainNode mainNode = new MainNode();
		mainNode.setId(id);
		if(nodeTimeStr!=null && dataMap!=null){
			mainNode.setNodeTime(DateUtils.formatToFltDate((String)dataMap.get(nodeTimeStr)));
		}
		setMainNodeStatus(resultData, mainNode);
		resultData.getMainDatas().put(mainNode.getId(), mainNode);
		return mainNode;
	}
	
	/**
	 * 创建主流程节点
	 * @param id
	 * @param nodeTimeStr
	 * @param dataMap
	 * @param resultData
	 * @return
	 * @throws Exception
	 */
	private MainNode createMainNode(String id,String nodeTimeStr,Map<String, Object> inDataMap,
			Map<String, Object> outDataMap,FlitMonitorData resultData) throws Exception{
		MainNode mainNode = new MainNode();
		mainNode.setId(id);
		if(nodeTimeStr!=null && inDataMap != null){
			String nodeTime = (String)inDataMap.get(nodeTimeStr);
			if(nodeTime == null && outDataMap != null){
				nodeTime = (String)outDataMap.get(nodeTimeStr);
			}
			mainNode.setNodeTime(DateUtils.formatToFltDate(nodeTime));
		}
		setMainNodeStatus(resultData, mainNode);
		resultData.getMainDatas().put(mainNode.getId(), mainNode);
		return mainNode;
	}

	/**
	 * 设置主节点状态
	 * @param resultData
	 * @param mainNode
	 */
	private void setMainNodeStatus(FlitMonitorData resultData, MainNode mainNode) {
		if(mainNode.getNodeTime() == null){
			if(!passStatus){
				// 主节点倒序添加，空数据先默认未到达
				mainNode.setStatus(3);
			}else{
				// 已到达节点时无数据默认给0000
				mainNode.setNodeTime("0000");
			}
		}else{
			// 如果有数据则标记为到达节点
			passStatus = true;
		}
		
		TimeNode timeNode = resultData.getTimeDatas().get(mainNode.getId());
		if(timeNode!=null){
			if(!passStatus){
				//如果未到达节点
				timeNode.setStatus(3);
			}else{
				// 如果到达节点，统一状态
				if(timeNode.getStatus() != null){
					if(timeNode.getStatus() != 3){
						mainNode.setStatus(timeNode.getStatus());
					}else{
						timeNode.setStatus(null);
					}
				}
			}
		}
	}
	
	/**
	 * 创建分支流程节点
	 * @param id
	 * @param nodeTimeStr
	 * @param dataMap
	 * @param resultData
	 * @return
	 * @throws Exception
	 */
	private OtherNode createOtherNode(String id,String nodeTimeStr,Map<String, Object> dataMap,
			FlitMonitorData resultData , TreeNode... treeNodes) throws Exception{
		OtherNode otherNode = new OtherNode();
		otherNode.setId(id);
		if(nodeTimeStr!=null && dataMap!=null){
			otherNode.setNodeTime(DateUtils.formatToFltDate((String)dataMap.get(nodeTimeStr)));
		}
		otherNode = setOtherNodeStatus(id, resultData, otherNode, treeNodes);
		resultData.getOtherDatas().put(otherNode.getId(), otherNode);
		return otherNode;
	}
	
	/**
	 * 创建分支流程节点
	 * @param id
	 * @param nodeTimeStr
	 * @param dataMap
	 * @param resultData
	 * @return
	 * @throws Exception
	 */
	private OtherNode createOtherNode(String id,String nodeTimeStr,Map<String, Object> inDataMap,
			Map<String, Object> outDataMap,FlitMonitorData resultData , TreeNode... treeNodes) throws Exception{
		OtherNode otherNode = new OtherNode();
		otherNode.setId(id);
		if(nodeTimeStr!=null && inDataMap!=null){
			String nodeTime = (String)inDataMap.get(nodeTimeStr);
			if(nodeTime == null && outDataMap!=null){
				nodeTime = (String)outDataMap.get(nodeTimeStr);
			}
			otherNode.setNodeTime(DateUtils.formatToFltDate(nodeTime));
		}
		otherNode = setOtherNodeStatus(id, resultData, otherNode, treeNodes);
		resultData.getOtherDatas().put(otherNode.getId(), otherNode);
		return otherNode;
	}

	/**
	 * 设置分支节点状态
	 * @param id
	 * @param resultData
	 * @param otherNode
	 * @param treeNodes
	 * @throws NumberFormatException
	 */
	private OtherNode setOtherNodeStatus(String id, FlitMonitorData resultData,
			OtherNode otherNode, TreeNode... treeNodes)
			throws NumberFormatException {
		// 没有时间默认0000
		if(otherNode.getNodeTime() == null){
			otherNode.setNodeTime("0000");
			otherNode.setStatus(3);
		}
		// 查询该节点的时间节点
		TimeNode timeNode = resultData.getTimeDatas().get(otherNode.getId());
		if(timeNode!=null){
			Integer status = timeNode.getStatus();
			if(!"0000".equals(otherNode.getNodeTime())){
				// 如果该节点有完成时间，将未完成状态强制改为已完成
				if(status == 2){
					otherNode.setStatus(2);
				}else{
					otherNode.setStatus(1);
					timeNode.setStatus(1);
				}
			}else{
				// 根据时间节点设置任务节点状态
				otherNode.setStatus(status);
			}
		}else{
			if(!"0000".equals(otherNode.getNodeTime())){
				// 如果该节点有完成时间，将未完成状态强制改为已完成
				otherNode.setStatus(1);
			}
		}
		
		
		// 如果当前节点为确定的完成节点
		if(otherNode.getStatus() != null && otherNode.getStatus() == 1){
			// 根据树结构改变父节点状态
			if(treeNodes!=null && treeNodes.length > 0){
				for(TreeNode treeNode : treeNodes){
					int nodeId = Integer.parseInt(id.replace("otherNode_", ""));
					// 取得当前节点
					TreeNode thisNode = treeNode.findTreeNodeById(nodeId);
					if(thisNode == null){
						continue;
					}
					// 遍历父节点
					for(TreeNode ptn : thisNode.getElders()){
						// 父节点ID
						String pId = "otherNode_" + ptn.getSelfId();
						// 根据ID查找父节点 并改变节点状态
						resultData.getOtherDatas().get(pId).setStatus(1);
						TimeNode ptime = resultData.getTimeDatas().get(pId);
						if(ptime != null){
							ptime.setStatus(1);
						}
					}
				}
			}
		}
		return otherNode;
	}
	
	private Integer getStatus(Integer eTime,Integer aTime){
		 if (aTime == null){
			 // 如果实际时间都为空，则状态为未完成
			return 3;
		}else if(eTime == null){
			// 如果实际时间不为空、预计时间为空，则状态为未超时完成
			return 1;
		}else if (aTime > eTime){
			// 如果实际时间>预计时间，则状态为超时完成
			return 2;
		}else if (aTime <= eTime){
			// 如果实际时间<=预计时间，则状态为未超时完成
			return 1;
		}else{
			return null;
		}
	}


	@Override
	public Map<String, Object> getNodeData(String inFltid,String outFltid, String nodeId, String hisFlag)
			throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("is_infltid", inFltid);
		paramMap.put("is_outfltid", outFltid);
		paramMap.put("is_id", nodeId);
		paramMap.put("on_if_success", -1);
		paramMap.put("out_result_cursor", new ArrayList<NodeInfo>());
		HashMap<String, Object> resultMap = getNodeData(paramMap ,hisFlag);
		if((Integer)resultMap.get("on_if_success") == 1){
			List<NodeInfo> resultList = (List<NodeInfo>)resultMap.get("out_result_cursor");
			Collections.sort(resultList);
			responseMap.put("data",resultList);
			responseMap.put("code", "0");
		}else{
			responseMap.put("data",new ArrayList<NodeInfo>());
			responseMap.put("code", "0");
		}
		return responseMap;
	}


	@Override
	public Map<String, Object> getPassengerInfo(String fltid, String inout, String hisFlag)
			throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		if("in".equals(inout)){
			// 进港旅客
			PassengerInfo passengerInfo = getInPassengerInfo(fltid ,hisFlag);
			responseMap.put("data",passengerInfo);
			responseMap.put("code", "0");
		}else if("out".equals(inout)){
			// 出港旅客
			PassengerInfo passengerInfo = getOutPassengerInfo(fltid ,hisFlag);
			responseMap.put("data",passengerInfo);
			responseMap.put("code", "0");
		}else{
			responseMap.put("desc","未指定旅客类型！");
			responseMap.put("code", "2");
		}
		return responseMap;
	}


	@Override
	public Map<String, Object> getPersonEvent(String fltid, String personCode,
			String taskId, String hisFlag) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		PersonEvent pe = new PersonEvent();
		// 流程事件
		List<TimeData> flowList= new ArrayList<TimeData>();
		
		Map<String, Object> flowMap = getPersonFlow(taskId ,hisFlag);
		if(flowMap != null){
			flowList.add(new TimeData((String)flowMap.get("T1"),"任务创建"));
			flowList.add(new TimeData((String)flowMap.get("T2"),"任务发布"));
			flowList.add(new TimeData((String)flowMap.get("T3"),"到位"));
			flowList.add(new TimeData((String)flowMap.get("T4"),"开始保障"));
			flowList.add(new TimeData((String)flowMap.get("T5"),"保障结束"));
			pe.setFlow(flowList);
		}
		
		// 消息事件
		pe.setMessage(getTaskMsg(taskId ,hisFlag));
		// 不正常事件
		pe.setAbnormal(getAbnormalityEvent(taskId ,hisFlag));
		
		responseMap.put("data",pe);
		responseMap.put("code", "0");
		return responseMap;
	}

	/*************************************取数据，判断是否取历史*******************************************/
	
	/**
	 * @param inFltid
	 * @return
	 */
	private FltInfo getInFltInfo(String inFltid , String hisFlag) {
		if("his".equals(hisFlag)){
			return fdHisService.getInFltInfo(inFltid);
		}
		return fltMonitorDao.getInFltInfo(inFltid);
	}
	
	/**
	 * @param outFltid
	 * @param hisFlag 
	 * @return
	 */
	private FltInfo getOutFltInfo(String outFltid, String hisFlag) {
		if("his".equals(hisFlag)){
			return fdHisService.getOutFltInfo(outFltid);
		}
		return fltMonitorDao.getOutFltInfo(outFltid);
	}
	
	/**
	 * @param viewName
	 * @param fltid
	 * @return
	 */
	private HashMap<String, Object> getFltmonitorData(String inFltid, String outFltid , String hisFlag) {
		if("his".equals(hisFlag)){
			if(!StringUtils.isEmpty(inFltid) && !StringUtils.isEmpty(outFltid)){
				return fdHisService.getFltmonitorInOutData(inFltid, outFltid);
			}else if(StringUtils.isEmpty(outFltid)){
				return fdHisService.getFltmonitorInData(inFltid);
			}else if(StringUtils.isEmpty(inFltid)){
				return fdHisService.getFltmonitorOutData(outFltid);
			}
		}else{
			if(!StringUtils.isEmpty(inFltid) && !StringUtils.isEmpty(outFltid)){
				return fltMonitorDao.getFltmonitorInOutData(inFltid, outFltid);
			}else if(StringUtils.isEmpty(outFltid)){
				return fltMonitorDao.getFltmonitorInData(inFltid);
			}else if(StringUtils.isEmpty(inFltid)){
				return fltMonitorDao.getFltmonitorOutData(outFltid);
			}
		}
		return null;
	}
	
	/**
	 * @param paramMap
	 */
	private HashMap<String, Object> getNodeData(HashMap<String, Object> paramMap, String hisFlag) {
		if("his".equals(hisFlag)){
			paramMap = fdHisService.getNodeData(paramMap);
			return paramMap;
		}
		fltMonitorDao.getNodeData(paramMap);
		return paramMap;
	}

	/**
	 * @param fltid
	 * @return
	 */
	private PassengerInfo getOutPassengerInfo(String fltid, String hisFlag) {
		if("his".equals(hisFlag)){
			return fdHisService.getOutPassengerInfo(fltid);
		}
		return fltMonitorDao.getOutPassengerInfo(fltid);
	}

	/**
	 * @param fltid
	 * @return
	 */
	private PassengerInfo getInPassengerInfo(String fltid, String hisFlag) {
		if("his".equals(hisFlag)){
			return fdHisService.getInPassengerInfo(fltid);
		}
		return fltMonitorDao.getInPassengerInfo(fltid);
	}

	/**
	 * @param taskId
	 * @return
	 */
	private Map<String, Object> getPersonFlow(String taskId, String hisFlag) {
		if("his".equals(hisFlag)){
			return fdHisService.getPersonFlow(taskId);
		}
		return fltMonitorDao.getPersonFlow(taskId);
	}
	
	/**
	 * @param taskId
	 * @return
	 */
	private List<TimeData> getAbnormalityEvent(String taskId, String hisFlag) {
		if("his".equals(hisFlag)){
			return fdHisService.getAbnormalityEvent(taskId);
		}
		return fltMonitorDao.getAbnormalityEvent(taskId);
	}

	/**
	 * @param taskId
	 * @return
	 */
	private List<TimeData> getTaskMsg(String taskId, String hisFlag) {
		if("his".equals(hisFlag)){
			return fdHisService.getTaskMsg(taskId);
		}
		return fltMonitorDao.getTaskMsg(taskId);
	}


	@Override
	public String getPhoneByUserId(String userId) throws Exception {
		return fltMonitorDao.getPhoneByUserId(userId);
	}
}
