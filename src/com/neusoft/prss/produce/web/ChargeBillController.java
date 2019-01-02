/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月14日 下午8:56:35
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.entity.ChargeBillEntity;
import com.neusoft.prss.produce.entity.XMBillEntity;
import com.neusoft.prss.produce.service.ChargeBillService;

@Controller
@RequestMapping(value = "${adminPath}/produce/chargeBill")
public class ChargeBillController extends BaseController {
	
	@Autowired
	private ChargeBillService chargeBillService;
	@Resource
	private FileService fileService;
	/**
	 * 
	 *Discription:打开列表页.
	 *@param model
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update: [变更描述]
	 */
	@RequestMapping(value = { "list" })
	public String list(Model model,String flag) {
		model.addAttribute("flag",flag);
		return "prss/produce/chargeBillList";
	}
	
	/**
	 * 
	 *Discription:获取列表数据.
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:Heqg
	 *@update:2017年11月14日 Heqg [变更描述]
	 */
	@ResponseBody
	@RequestMapping(value = "getChargeBillData")
	public String getBillData(String flag) {
		JSONArray json = chargeBillService.getChargeBillDate(flag);
		String result = json.toJSONString();
		return result;
	}
	/**
	 * 
	 *Discription:新增单据.
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:[变更描述]
	 */
	@RequestMapping(value = { "addChargeBill" })
	public String addChargeBill(Model model,String flag) {
		ChargeBillEntity vo = new ChargeBillEntity();
		model.addAttribute("vo", vo);
		JSONObject json = chargeBillService.getBillSeq(flag);
		model.addAttribute("billId",json.getString("BILLID"));
		model.addAttribute("flag",flag);
		return "prss/produce/chargeBillAdd";
	}
	/**
	 * 
	 *Discription:获取航班信息.
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:[变更描述]
	 */
	@ResponseBody
	@RequestMapping(value =  "getChargeBillDetail" )
	public JSONObject getChargeBillDetail(String flightDate,String flightNumber,String inOutFlag) {
		Map<String,String> param = new HashMap<String,String>();
		param.put("flightDate", flightDate);
		param.put("flightNumber", flightNumber);
		param.put("inOutFlag", inOutFlag);
		JSONObject json = chargeBillService.getFightInfo(param);
		if (json==null) {
			json=new JSONObject();
		}
		return json;
	}
	/**
	 * 
	 *Discription:新增航班收费单页面初始化
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:[变更描述]
	 */
	@RequestMapping(value = { "initAdd" })
	public String initAdd(Model model) {
		return "prss/produce/chargeBillDetail";
	}
	/**
	 * 
	 *Discription:获取航班信息.
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:[变更描述]
	 */
	@ResponseBody
	@RequestMapping(value =  "doAdd" )
	public String doAdd(String billId,ChargeBillEntity vo,String flag){
		String rep="success";
		List<XMBillEntity> list = new ArrayList<>();
		if (("ZPKTC").equals(flag)) {
			XMBillEntity ktcVO = new XMBillEntity();
			ktcVO.setBillId(billId);
			ktcVO.setXmCode("ktc");
			ktcVO.setXmName("客梯车");
			ktcVO.setNum(vo.getKtc1());
			ktcVO.setTaskTime(vo.getKtc2());
			ktcVO.setXmVal(vo.getKtc3());		
			list.add(ktcVO);
		}
		else {
		XMBillEntity czryVO = new XMBillEntity();
		czryVO.setBillId(billId);
		czryVO.setXmCode("czry");
		czryVO.setXmName("操作人员");
		czryVO.setNum(vo.getCzry1());
		czryVO.setTaskTime(vo.getCzry2());
		czryVO.setXmVal(vo.getCzry3());		
		list.add(czryVO);
		
		XMBillEntity csdcVO = new XMBillEntity();
		csdcVO.setBillId(billId);
		csdcVO.setXmCode("csdc");
		csdcVO.setXmName("传送带车");
		csdcVO.setNum(vo.getCsdc1());
		csdcVO.setTaskTime(vo.getCsdc2());
		csdcVO.setXmVal(vo.getCsdc3());		
		list.add(csdcVO);
		
		XMBillEntity zxtcVO = new XMBillEntity();
		zxtcVO.setBillId(billId);
		zxtcVO.setXmCode("zxtc");
		zxtcVO.setXmName("装卸拖车");
		zxtcVO.setNum(vo.getZxtc1());
		zxtcVO.setTaskTime(vo.getZxtc2());
		zxtcVO.setXmVal(vo.getZxtc3());		
		list.add(zxtcVO);
		
		XMBillEntity xlystcVO = new XMBillEntity();
		xlystcVO.setBillId(billId);
		xlystcVO.setXmCode("xlystc");
		xlystcVO.setXmName("行李运输拖车");
		xlystcVO.setNum(vo.getXlystc1());
		xlystcVO.setTaskTime(vo.getXlystc2());
		xlystcVO.setXmVal(vo.getXlystc3());		
		list.add(xlystcVO);
		
		XMBillEntity tzhwptc35VO = new XMBillEntity();
		tzhwptc35VO.setBillId(billId);
		tzhwptc35VO.setXmCode("tzhwptc35");
		tzhwptc35VO.setXmName("特种货物平台车(35t)");
		tzhwptc35VO.setNum(vo.getTzhwptc351());
		tzhwptc35VO.setTaskTime(vo.getTzhwptc352());
		tzhwptc35VO.setXmVal(vo.getTzhwptc353());		
		list.add(tzhwptc35VO);
		
		XMBillEntity tzhwptc20VO = new XMBillEntity();
		tzhwptc20VO.setBillId(billId);
		tzhwptc20VO.setXmCode("tzhwptc20");
		tzhwptc20VO.setXmName("特种货物平台车(20t)");
		tzhwptc20VO.setNum(vo.getTzhwptc201());
		tzhwptc20VO.setTaskTime(vo.getTzhwptc202());
		tzhwptc20VO.setXmVal(vo.getTzhwptc203());		
		list.add(tzhwptc20VO);
		
		XMBillEntity ptc136VO = new XMBillEntity();
		ptc136VO.setBillId(billId);
		ptc136VO.setXmCode("ptc13.6");
		ptc136VO.setXmName("平台车(13.6t)");
		ptc136VO.setNum(vo.getPtc1361());
		ptc136VO.setTaskTime(vo.getPtc1362());
		ptc136VO.setXmVal(vo.getPtc1363());		
		list.add(ptc136VO);
		
		XMBillEntity ptc68VO = new XMBillEntity();
		ptc68VO.setBillId(billId);
		ptc68VO.setXmCode("ptc6.8");
		ptc68VO.setXmName("平台车(6.8t)");
		ptc68VO.setNum(vo.getPtc681());
		ptc68VO.setTaskTime(vo.getPtc682());
		ptc68VO.setXmVal(vo.getPtc683());		
		list.add(ptc68VO);
		
		XMBillEntity zzjzbVO = new XMBillEntity();
		zzjzbVO.setBillId(billId);
		zzjzbVO.setXmCode("zzjzb");
		zzjzbVO.setXmName("组装集装板");
		zzjzbVO.setNum(vo.getZzjzb1());
		zzjzbVO.setTaskTime(vo.getZzjzb2());
		zzjzbVO.setXmVal(vo.getZzjzb3());		
		list.add(zzjzbVO);
		
		XMBillEntity fjjzbVO = new XMBillEntity();
		fjjzbVO.setBillId(billId);
		fjjzbVO.setXmCode("fjjzb");
		fjjzbVO.setXmName("分解集装板");
		fjjzbVO.setNum(vo.getFjjzb1());
		fjjzbVO.setTaskTime(vo.getFjjzb2());
		fjjzbVO.setXmVal(vo.getFjjzb3());		
		list.add(fjjzbVO);
		
		XMBillEntity zzjzxVO = new XMBillEntity();
		zzjzxVO.setBillId(billId);
		zzjzxVO.setXmCode("zzjzx");
		zzjzxVO.setXmName("组装集装箱");
		zzjzxVO.setNum(vo.getZzjzx1());
		zzjzxVO.setTaskTime(vo.getZzjzx2());
		zzjzxVO.setXmVal(vo.getZzjzx3());		
		list.add(zzjzxVO);
		
		XMBillEntity fjjzxVO = new XMBillEntity();
		fjjzxVO.setBillId(billId);
		fjjzxVO.setXmCode("fjjzx");
		fjjzxVO.setXmName("分解集装箱");
		fjjzxVO.setNum(vo.getFjjzx1());
		fjjzxVO.setTaskTime(vo.getFjjzx2());
		fjjzxVO.setXmVal(vo.getFjjzx3());		
		list.add(fjjzxVO);
		
		XMBillEntity cfzcVO = new XMBillEntity();
		cfzcVO.setBillId(billId);
		cfzcVO.setXmCode("cfzc");
		cfzcVO.setXmName("重复装舱");
		cfzcVO.setNum(vo.getCfzc1());
		cfzcVO.setTaskTime(vo.getCfzc2());
		cfzcVO.setXmVal(vo.getCfzc3());		
		list.add(cfzcVO);
		}	
		XMBillEntity qtfwVO = new XMBillEntity();
		qtfwVO.setBillId(billId);
		qtfwVO.setXmCode("qtfw");
		qtfwVO.setXmName("其它服务");
		qtfwVO.setNum(vo.getQtfw1());
		qtfwVO.setTaskTime(vo.getQtfw2());
		qtfwVO.setXmVal(vo.getQtfw3());		
		list.add(qtfwVO);
		if ((("ZPKTC").equals(flag))) {// 站坪客梯车
			 if (chargeBillService.doAddChargeBill(list)<=0) {//说明执行有误，删除插入数据			 
				 rep="error";
			}
			 else {
				// 插入站坪客梯车服务确认书
				 if(chargeBillService.doAddZPKTCZPFW(vo)>0){
					 rep="success";
				 }
				 else {
					 chargeBillService.delChargeBill(vo.getBillId());
					 chargeBillService.delChargeGoodsBill(vo.getBillId());
					 rep="error";
				}
			}
		}
		else {// 站坪窄体
			 if (chargeBillService.doAddZTChargeBill(list)<=0) {//说明执行有误，删除插入数据			 
				 rep="error";
			}
			 else {
				// 插入站坪窄体服务确认书
				 if(chargeBillService.doAddZPZTZPFW(vo)>0){
					 rep="success";
				 }
				 else {
					 chargeBillService.delZTChargeBill(vo.getBillId());
					 chargeBillService.delZTChargeGoodsBill(vo.getBillId());
					 rep="error";
				}
			}
		}
		return rep;
	}
	/**
	 * 
	 *Discription:删除单据
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:[变更描述]
	 */
	@ResponseBody
	@RequestMapping(value =  "delChargeBill" )
		public String delChargeBill(String id, String type,String flag) {
		String req = "false";
		if (("ZPKTC").equals(flag)) {
			if (chargeBillService.delChargeBill(id)>0&&chargeBillService.delChargeGoodsBill(id)>0) {
				req="success";
			}
		}
		else {
			if (chargeBillService.delZTChargeBill(id)>0&&chargeBillService.delZTChargeGoodsBill(id)>0) {
				req="success";
			}
		}
		return req;
	}
	/**
	 * 
	 *Discription:修改单据初始化.
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:[变更描述]
	 */
	@RequestMapping(value = { "updateChargeBill" })
	public String updateChargeBill(Model model,String billId,String flag) {
		ChargeBillEntity vo = new ChargeBillEntity();
		//根据单据号获取信息
		Map<String,String> map = new HashMap<String,String>();
		map.put("billId", billId);
		map.put("flag", flag);
		JSONArray goodList =chargeBillService.getChargeBillGoodsDetail(map);
		JSONObject goodJson = new JSONObject();
		for (int i = 0; i < goodList.size(); i++) {
			goodJson = goodList.getJSONObject(i);
			if(!("ZPKTC").equals(flag)){
			if (i==0) {
				vo.setCzry3(goodJson.getString("XM_VAL"));
				vo.setCzry1(goodJson.getString("NUM"));
				vo.setCzry2(goodJson.getString("TASK_TIME"));
			}
			if (i==1) {
				vo.setCsdc3(goodJson.getString("XM_VAL"));
				vo.setCsdc1(goodJson.getString("NUM"));
				vo.setCsdc2(goodJson.getString("TASK_TIME"));
			}
			if (i==2) {
				vo.setZxtc3(goodJson.getString("XM_VAL"));
				vo.setZxtc1(goodJson.getString("NUM"));
				vo.setZxtc2(goodJson.getString("TASK_TIME"));
			}
			if (i==3) {
				vo.setXlystc3(goodJson.getString("XM_VAL"));
				vo.setXlystc1(goodJson.getString("NUM"));
				vo.setXlystc2(goodJson.getString("TASK_TIME"));
			}
			if (i==4) {
				vo.setTzhwptc353(goodJson.getString("XM_VAL"));
				vo.setTzhwptc351(goodJson.getString("NUM"));
				vo.setTzhwptc352(goodJson.getString("TASK_TIME"));
			}
			if (i==5) {
				vo.setTzhwptc203(goodJson.getString("XM_VAL"));
				vo.setTzhwptc201(goodJson.getString("NUM"));
				vo.setTzhwptc202(goodJson.getString("TASK_TIME"));
			}
			if (i==6) {
				vo.setPtc1363(goodJson.getString("XM_VAL"));
				vo.setPtc1361(goodJson.getString("NUM"));
				vo.setPtc1362(goodJson.getString("TASK_TIME"));
			}
			if (i==7) {
				vo.setPtc683(goodJson.getString("XM_VAL"));
				vo.setPtc681(goodJson.getString("NUM"));
				vo.setPtc682(goodJson.getString("TASK_TIME"));
			}
			if (i==8) {
				vo.setZzjzb3(goodJson.getString("XM_VAL"));
				vo.setZzjzb1(goodJson.getString("NUM"));
				vo.setZzjzb2(goodJson.getString("TASK_TIME"));
			}
			if (i==9) {
				vo.setFjjzb3(goodJson.getString("XM_VAL"));
				vo.setFjjzb1(goodJson.getString("NUM"));
				vo.setFjjzb2(goodJson.getString("TASK_TIME"));
			}
			if (i==10) {
				vo.setZzjzx3(goodJson.getString("XM_VAL"));
				vo.setZzjzx1(goodJson.getString("NUM"));
				vo.setZzjzx2(goodJson.getString("TASK_TIME"));
			}
			if (i==11) {
				vo.setFjjzx3(goodJson.getString("XM_VAL"));
				vo.setFjjzx1(goodJson.getString("NUM"));
				vo.setFjjzx2(goodJson.getString("TASK_TIME"));
			}
			if (i==12) {			
			vo.setCfzc3(goodJson.getString("XM_VAL"));
			vo.setCfzc1(goodJson.getString("NUM"));
			vo.setCfzc2(goodJson.getString("TASK_TIME"));
			}
			if (i==13) {
				vo.setQtfw3(goodJson.getString("XM_VAL"));
				vo.setQtfw1(goodJson.getString("NUM"));
				vo.setQtfw2(goodJson.getString("TASK_TIME"));
			}	
		  }
			else {
				if (i==0) {			
					vo.setKtc3(goodJson.getString("XM_VAL"));
					vo.setKtc1(goodJson.getString("NUM"));
					vo.setKtc2(goodJson.getString("TASK_TIME"));
					}
				if (i==1) {
					vo.setQtfw3(goodJson.getString("XM_VAL"));
					vo.setQtfw1(goodJson.getString("NUM"));
					vo.setQtfw2(goodJson.getString("TASK_TIME"));
				}	
			}
		}
		Map<String,String> param = new HashMap<String,String>();
		param.put("billId", billId);
		param.put("flag", flag);
		JSONObject json = chargeBillService.getChargeBillDetail(param);		
		vo.setActTypeCode(json.getString("ACT_TYPE"));
		vo.setActstandCode(json.getString("ACTSTAND_CODE"));
		vo.setFlightDate(json.getString("FLIGHT_DATE"));
		vo.setPost(json.getString("POST"));
		vo.setAta(json.getString("ATA"));
		vo.setAtd(json.getString("ATD"));
		vo.setEta(json.getString("ETA"));
		vo.setEtd(json.getString("ETD"));
		vo.setOperator(json.getString("OPERATOR"));
		vo.setScheduler(json.getString("SCHEDULER"));
		vo.setFlightNumber(json.getString("FLIGHT_NUMBER"));
		JSONObject flightJson = chargeBillService.getFlightInfo(json.getString("FLTID"));	
		vo.setSta(flightJson.getString("STA"));
		vo.setStd(flightJson.getString("STD"));
		vo.setInOutFlag(flightJson.getString("IN_OUT_FLAG"));
		vo.setBillId(billId);
		model.addAttribute("vo", vo);
		model.addAttribute("billId",billId);
		model.addAttribute("flag", flag);
		// 获取签名图片路径
		JSONObject imgJson  = chargeBillService.getImgSrc(param);
//		String imgSrc = imgJson.getString("FILE_PATH")+"/"+imgJson.getString("FILE_NEW_NAME"); 
		if (imgJson!=null&&imgJson.containsKey("FILE_ID")) {
			model.addAttribute("fileId", imgJson.getString("FILE_ID"));
		}
		else {
			model.addAttribute("fileId","");
		}	
		return "prss/produce/chargeBillEdit";
	}
	 @RequestMapping(value = "readExpAtta")
	 public void readExpAtta(Model model,String fileId,String type,HttpServletRequest request,
	            HttpServletResponse response) {
	        BufferedInputStream is = null;
	        OutputStream os = null;
	        if (StringUtils.isNotEmpty(fileId)) {
	            try {
	                byte[] data = fileService.doDownLoadFile(fileId);
	                byte[] content = new byte[1024];
	                is = new BufferedInputStream(new ByteArrayInputStream(data));
	                os = response.getOutputStream();
	                while (is.read(content) != -1) {
	                    os.write(content);
	                }
	            } catch (Exception e) {
	                logger.error("数据流写入失败" + e.getMessage());
	            } finally {
	                try {
	                    if (is != null) {
	                        is.close();
	                    }
	                    if (os != null) {
	                        os.close();
	                    }
	                } catch (IOException e) {
	                    logger.error(e.toString());
	                }
	            }
	        }
	    }
	/**
	 * 
	 *Discription:修改航班信息.
	 *@return
	 *@return:返回值意义
	 *@author:huanglj
	 *@update:[变更描述]
	 */
	@ResponseBody
	@RequestMapping(value =  "doUpdate" )
	public String doUpdate(ChargeBillEntity vo,String flag){
		String rep="fail";		
		List<XMBillEntity> list = new ArrayList<>();
		if (("ZPKTC").equals(flag)) {
			XMBillEntity ktcVO = new XMBillEntity();
			ktcVO.setBillId(vo.getBillId());
			ktcVO.setXmCode("ktc");
			ktcVO.setXmName("客梯车");
			ktcVO.setNum(vo.getKtc1());
			ktcVO.setTaskTime(vo.getKtc2());
			ktcVO.setXmVal(vo.getKtc3());		
			list.add(ktcVO);
		}
		else{
		XMBillEntity czryVO = new XMBillEntity();
		czryVO.setBillId(vo.getBillId());
		czryVO.setXmCode("czry");
		czryVO.setXmName("操作人员");
		czryVO.setNum(vo.getCzry1());
		czryVO.setTaskTime(vo.getCzry2());
		czryVO.setXmVal(vo.getCzry3());		
		list.add(czryVO);
		
		XMBillEntity csdcVO = new XMBillEntity();
		csdcVO.setBillId(vo.getBillId());
		csdcVO.setXmCode("csdc");
		csdcVO.setXmName("传送带车");
		csdcVO.setNum(vo.getCsdc1());
		csdcVO.setTaskTime(vo.getCsdc2());
		csdcVO.setXmVal(vo.getCsdc3());		
		list.add(csdcVO);
		
		XMBillEntity zxtcVO = new XMBillEntity();
		zxtcVO.setBillId(vo.getBillId());
		zxtcVO.setXmCode("zxtc");
		zxtcVO.setXmName("装卸拖车");
		zxtcVO.setNum(vo.getZxtc1());
		zxtcVO.setTaskTime(vo.getZxtc2());
		zxtcVO.setXmVal(vo.getZxtc3());		
		list.add(zxtcVO);
		
		XMBillEntity xlystcVO = new XMBillEntity();
		xlystcVO.setBillId(vo.getBillId());
		xlystcVO.setXmCode("xlystc");
		xlystcVO.setXmName("行李运输拖车");
		xlystcVO.setNum(vo.getXlystc1());
		xlystcVO.setTaskTime(vo.getXlystc2());
		xlystcVO.setXmVal(vo.getXlystc3());		
		list.add(xlystcVO);
		
		XMBillEntity tzhwptc35VO = new XMBillEntity();
		tzhwptc35VO.setBillId(vo.getBillId());
		tzhwptc35VO.setXmCode("tzhwptc35");
		tzhwptc35VO.setXmName("特种货物平台车(35t)");
		tzhwptc35VO.setNum(vo.getTzhwptc351());
		tzhwptc35VO.setTaskTime(vo.getTzhwptc352());
		tzhwptc35VO.setXmVal(vo.getTzhwptc353());		
		list.add(tzhwptc35VO);
		
		XMBillEntity tzhwptc20VO = new XMBillEntity();
		tzhwptc20VO.setBillId(vo.getBillId());
		tzhwptc20VO.setXmCode("tzhwptc20");
		tzhwptc20VO.setXmName("特种货物平台车(20t)");
		tzhwptc20VO.setNum(vo.getTzhwptc201());
		tzhwptc20VO.setTaskTime(vo.getTzhwptc202());
		tzhwptc20VO.setXmVal(vo.getTzhwptc203());		
		list.add(tzhwptc20VO);
		
		XMBillEntity ptc136VO = new XMBillEntity();
		ptc136VO.setBillId(vo.getBillId());
		ptc136VO.setXmCode("ptc13.6");
		ptc136VO.setXmName("平台车(13.6t)");
		ptc136VO.setNum(vo.getPtc1361());
		ptc136VO.setTaskTime(vo.getPtc1362());
		ptc136VO.setXmVal(vo.getPtc1363());		
		list.add(ptc136VO);
		
		XMBillEntity ptc68VO = new XMBillEntity();
		ptc68VO.setBillId(vo.getBillId());
		ptc68VO.setXmCode("ptc6.8");
		ptc68VO.setXmName("平台车(6.8t)");
		ptc68VO.setNum(vo.getPtc681());
		ptc68VO.setTaskTime(vo.getPtc682());
		ptc68VO.setXmVal(vo.getPtc683());		
		list.add(ptc68VO);

		XMBillEntity zzjzbVO = new XMBillEntity();
		zzjzbVO.setBillId(vo.getBillId());
		zzjzbVO.setXmCode("zzjzb");
		zzjzbVO.setXmName("组装集装板");
		zzjzbVO.setNum(vo.getZzjzb1());
		zzjzbVO.setTaskTime(vo.getZzjzb2());
		zzjzbVO.setXmVal(vo.getZzjzb3());		
		list.add(zzjzbVO);
		
		XMBillEntity fjjzbVO = new XMBillEntity();
		fjjzbVO.setBillId(vo.getBillId());
		fjjzbVO.setXmCode("fjjzb");
		fjjzbVO.setXmName("分解集装板");
		fjjzbVO.setNum(vo.getFjjzb1());
		fjjzbVO.setTaskTime(vo.getFjjzb2());
		fjjzbVO.setXmVal(vo.getFjjzb3());		
		list.add(fjjzbVO);
		
		XMBillEntity zzjzxVO = new XMBillEntity();
		zzjzxVO.setBillId(vo.getBillId());
		zzjzxVO.setXmCode("zzjzx");
		zzjzxVO.setXmName("组装集装箱");
		zzjzxVO.setNum(vo.getZzjzx1());
		zzjzxVO.setTaskTime(vo.getZzjzx2());
		zzjzxVO.setXmVal(vo.getZzjzx3());		
		list.add(zzjzxVO);
		
		XMBillEntity fjjzxVO = new XMBillEntity();
		fjjzxVO.setBillId(vo.getBillId());
		fjjzxVO.setXmCode("fjjzx");
		fjjzxVO.setXmName("分解集装箱");
		fjjzxVO.setNum(vo.getFjjzx1());
		fjjzxVO.setTaskTime(vo.getFjjzx2());
		fjjzxVO.setXmVal(vo.getFjjzx3());		
		list.add(fjjzxVO);
		
		XMBillEntity cfzcVO = new XMBillEntity();
		cfzcVO.setBillId(vo.getBillId());
		cfzcVO.setXmCode("cfzc");
		cfzcVO.setXmName("重复装舱");
		cfzcVO.setNum(vo.getCfzc1());
		cfzcVO.setTaskTime(vo.getCfzc2());
		cfzcVO.setXmVal(vo.getCfzc3());		
		list.add(cfzcVO);
		}
		XMBillEntity qtfwVO = new XMBillEntity();
		qtfwVO.setBillId(vo.getBillId());
		qtfwVO.setXmCode("qtfw");
		qtfwVO.setXmName("其它服务");
		qtfwVO.setNum(vo.getQtfw1());
		qtfwVO.setTaskTime(vo.getQtfw2());
		qtfwVO.setXmVal(vo.getQtfw3());	
		list.add(qtfwVO);
		try {
			if(("ZPKTC").equals(flag)){
				chargeBillService.updateChargeBill(list);
			}
			else {
				chargeBillService.updateZTChargeBill(list);
			}
			rep="success";
		} catch (DataAccessException e) {
			System.out.println(e);
		}			
	
		return rep;
	}
}
