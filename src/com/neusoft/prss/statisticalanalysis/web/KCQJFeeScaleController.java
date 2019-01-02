/**
 *application name:hetprss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年8月18日 下午13:56:35
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.prss.statisticalanalysis.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.prss.cache.service.CacheService;
import com.neusoft.prss.stand.entity.ResultByCus;
import com.neusoft.prss.statisticalanalysis.service.KCQJFeeScaleService;

@Controller
@RequestMapping(value = "${adminPath}/kcqj/feeScale")
public class KCQJFeeScaleController extends BaseController {
	
	@Autowired
	private KCQJFeeScaleService kcqjFeeScaleService;
	
	@Autowired
	private CacheService cacheService;
	
	@RequestMapping(value ="list")
	public String list(Model model) {
		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname");
		JSONArray actType=cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
		model.addAttribute("airlines",airlinesCodeSource);
		model.addAttribute("actType",actType);
		return "prss/statisticalanalysis/kcqjFeeScaleList";
	}
	
	@RequestMapping(value = "dataList" )
    @ResponseBody
    public Map<String,Object> getDataList(int pageSize,int pageNumber,
    		String alnCode,String actType) {
    	
		alnCode = StringEscapeUtils.unescapeHtml4(alnCode);
		actType = StringEscapeUtils.unescapeHtml4(actType);
    	try {
    		alnCode = java.net.URLDecoder.decode(alnCode,"utf-8");
    		actType = java.net.URLDecoder.decode(actType,"utf-8");
		} catch (Exception e) {
			logger.error("/kcqj/feeScale/dataList转换失败->"+e.getMessage());
		}
    	Map<String, Object> param=new HashMap<String, Object>();
    	int begin=(pageNumber-1)*pageSize;
        int end=pageSize + begin;
        param.put("begin", begin);
	    param.put("end", end);
	    param.put("alnCode", alnCode);
	    param.put("actType", actType);
        return kcqjFeeScaleService.getDataList(param);
    }
	
	@RequestMapping(value ="add")
	public String add(Model model,String type) {
		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname");
//		JSONArray actType=getActTypeByAlnCode("",null);
		model.addAttribute("airlines",airlinesCodeSource);
//		model.addAttribute("actType",actType);
		model.addAttribute("type",type);
		return "prss/statisticalanalysis/kcqjFeeScaleForm";
	}
	@RequestMapping(value ="edit")
	public String edit(Model model,String id,String type) {
		JSONArray dataArr=kcqjFeeScaleService.getDataById(id);
		JSONObject o=(JSONObject) dataArr.get(0);
		if(dataArr.size()!=0){
			model.addAttribute("data",o);
		}
		//机型的下拉列表中，同一航空公司 同一机型不能重复 ，此处将此航空公司已经选过的机型过滤掉
		JSONArray airlinesCodeSource = cacheService.getOpts("dim_airline", "icao_code","airline_shortname");
		JSONArray allActType=cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
		JSONArray selectcdData=new JSONArray();
		if(o!=null&&!o.isEmpty()){
			//由于是修改  所以本条数据的机型不能被过滤
			selectcdData=kcqjFeeScaleService.getActTypeByAlnCode(o.getString("ALN_3CODE"),o.getString("ID"));
		}	
		if(selectcdData!=null){
			if(allActType!=null){
				allActType.removeAll(selectcdData);
			}
		}
		model.addAttribute("airlines",airlinesCodeSource);
		model.addAttribute("actType",allActType);
		model.addAttribute("type",type);
		return "prss/statisticalanalysis/kcqjFeeScaleForm";
	}
    @RequestMapping(value="save")
    @ResponseBody
    public ResultByCus save(String formData,String type){
    	ResultByCus result=new ResultByCus();
		formData = StringEscapeUtils.unescapeHtml4(formData);
		//处理机型数组 转换成逗号分隔的字符串
		JSONObject o=JSON.parseObject(formData);
		JSONArray array=o.getJSONArray("actType");
		o.put("actType", StringUtils.join(array, ","));
		boolean flag=false;
		if("edit".equals(type)){
			flag=kcqjFeeScaleService.saveEdit(o);
		}else {
			flag=kcqjFeeScaleService.save(o);
		}
		if(flag){
			result.setCode("0000");
	    	result.setMsg("操作成功");
		}else{
			result.setCode("1000");
	    	result.setMsg("操作失败");
		}
    	return result;
    }
    
	@ResponseBody
	@RequestMapping(value =  "del" )
		public ResultByCus delChargeBill(String id) {
		ResultByCus result=new ResultByCus();
		boolean flag=kcqjFeeScaleService.delBillById(id);
		if(flag){
			result.setCode("0000");
			result.setMsg("操作成功");
		}else{
			result.setCode("1000");
			result.setMsg("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value="getActType")
	@ResponseBody
	public JSONArray getActTypeByAlnCode(String alnCode,String id){
		JSONArray allActType=cacheService.getOpts("dim_actype", "todb_actype_code", "todb_actype_code");
		JSONArray selectcdData=kcqjFeeScaleService.getActTypeByAlnCode(alnCode,id);		
		if(selectcdData!=null){
			if(allActType!=null){
				allActType.removeAll(selectcdData);
			}
		}
		return allActType;
	}

}
