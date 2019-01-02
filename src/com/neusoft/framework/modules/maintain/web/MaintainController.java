/**
 *application name:prss
 *application describing:参数配置
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月28日 下午6:50:21
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.framework.modules.maintain.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.common.web.BaseController;
import com.neusoft.framework.modules.maintain.entity.ColumnVO;
import com.neusoft.framework.modules.maintain.entity.ConditionVO;
import com.neusoft.framework.modules.maintain.entity.TableVO;
import com.neusoft.framework.modules.maintain.service.MaintainService;
import com.neusoft.framework.modules.maintain.util.SqlCreator;
import com.neusoft.framework.modules.sys.entity.User;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.common.entity.JobKind;

@Controller
@RequestMapping(value = "${adminPath}/sys/maintain")
public class MaintainController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(MaintainController.class);
	@Autowired
	private MaintainService maintainService;
	
	/**
	 *Discription:初始化查询
	 *@param model
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = {"initTable",""})
	public String initTable(Model model,HttpServletRequest request){
		Map<String,String> paramMap=this.getParamMap(request);
		String pageNum = paramMap.get("pageNum");//当前页
        String pageCount = paramMap.get("pageCount");//每页显示行数
        String tabId=paramMap.get("tabId");
        
        Session session=UserUtils.getSession();
        TableVO tableVO=(TableVO)session.getAttribute("tableVO"+tabId);//先从session中取，如果为空再从数据库中取
        if(tableVO==null){
        	tableVO= maintainService.initTable(paramMap);//从数据库中获取配置
        	session.setAttribute("tableVO"+tabId, tableVO);
        }
		Map<String,List<ConditionVO>> condMap=tableVO.getConditionMap();//条件map
		
		this.initParam(paramMap, tableVO.getConditionList());//初始化参数
		// 初始化翻页参数
        if (pageNum == null)
            pageNum = "1";
        int page = Integer.parseInt(pageNum);
        if (pageCount == null)
            pageCount = String.valueOf(tableVO.getPageNumber());
        int count = Integer.parseInt(pageCount);
        int start = count * (page - 1) + 1;
        int end = count * page;
        paramMap.put("start", String.valueOf(start));
        paramMap.put("end", String.valueOf(end));
        
        String querySql = SqlCreator.getSelectSql(tableVO, paramMap);
        
        List<JSONObject> jsonList=maintainService.getDataList(querySql,paramMap);//取数据list
        List<List<String>> dataList=this.dealData(jsonList, tableVO.getColumnList());
        
        String totalCount = maintainService.getTotalCount(querySql);//获取总数
        int pages=(int)Math.ceil(Integer.parseInt(totalCount)*1.0/count);//总页数
        
        model.addAttribute("dataList", dataList);//数据
        model.addAttribute("totalRows", totalCount);//总行数
        model.addAttribute("tableVO", tableVO);//配置
        model.addAttribute("tabId", tabId);//配置ID
        model.addAttribute("condMap", condMap);//条件MAP
        model.addAttribute("pages",pages);//总页数
        model.addAttribute("pageNum", pageNum);//当前页
        model.addAttribute("pageCount", pageCount);//每页显示行数
        model.addAttribute("paramMap", paramMap);//参数map
        model.addAttribute("pageNumList", tableVO.getPageArray());//每页显示行数数组
		return "modules/maintain/maintainList";
	}
	
	/** 
	 *Discription:删除
	 *@param model
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String doDelete(Model model,HttpServletRequest request){
		Map<String,String> paramMap=this.getParamMap(request);
		String tabId=paramMap.get("tabId");
		
        Session session=UserUtils.getSession();
        TableVO tableVO=(TableVO)session.getAttribute("tableVO"+tabId);//先从session中取，如果为空再从数据库中取
        if(tableVO==null){
        	tableVO= maintainService.initTable(paramMap);//从数据库中获取配置
        	session.setAttribute("tableVO"+tabId, tableVO);
        }
        String deleteSql = SqlCreator.getDeleteSql(tableVO, paramMap); 
        //执行删除
        String result="1";
        try{
        	String selectSql = SqlCreator.getUpdateSelectSql(tableVO, paramMap);
            Map<String,String> oldMap = maintainService.getDataMap(selectSql);
            maintainService.doInsertLog(tableVO, oldMap,null,"3");
        	maintainService.doUpdate(deleteSql);
        	maintainService.doUpdateCache(tableVO.getDataTabName());//更新缓存
        }catch(Exception e){
        	result="0";
        }
		return result;
	}
	/**
	 *Discription:新增初始化
	 *@param model
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = "addInit")
	public String addInit(Model model,HttpServletRequest request){
		
		Map<String,String> paramMap=this.getParamMap(request);
		String tabId=paramMap.get("tabId");
		
        Session session=UserUtils.getSession();
        TableVO tableVO=(TableVO)session.getAttribute("tableVO"+tabId);//先从session中取，如果为空再从数据库中取
        if(tableVO==null){
        	tableVO= maintainService.initTable(paramMap);//从数据库中获取配置
        	session.setAttribute("tableVO"+tabId, tableVO);
        }
        
        model.addAttribute("tableVO", tableVO);//配置
        model.addAttribute("paramMap", paramMap);//参数map
        model.addAttribute("tabId", tabId);//配置
        return "modules/maintain/maintainAdd";
	}
	/**
	 *Discription:新增
	 *@param model
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = "add")
	@ResponseBody
	public String doAdd(Model model,HttpServletRequest request){
		String res="";
		Map<String,String> paramMap=this.getParamMap(request);
		String tabId=paramMap.get("tabId");
		
        Session session=UserUtils.getSession();
        TableVO tableVO=(TableVO)session.getAttribute("tableVO"+tabId);//先从session中取，如果为空再从数据库中取
        if(tableVO==null){
        	tableVO= maintainService.initTable(paramMap);//从数据库中获取配置
        	session.setAttribute("tableVO"+tabId, tableVO);
        }
        /** 唯一性验证 gaojd 20150723 --begin **/
        List<JSONArray> valueList = new ArrayList<JSONArray>();
        List<ColumnVO> columnList = tableVO.getColumnList();
        for (ColumnVO column : columnList) {
            if ("file".equalsIgnoreCase(column.getColShowType()) || "1".equals(column.getIfPk())
                    || !"".equals(StringUtils.nvl(column.getDefaultValue()))
                    || "0".equals(column.getIfModify())) {
                continue;
            }
            JSONArray array = new JSONArray();
            array.add(paramMap.get(column.getColNameEn()));
            valueList.add(array);
        }
        res = maintainService.doUniqueValidate(tableVO, valueList, null);// 唯一性验证
        if("true".equals(res)){
        	String insertSql = SqlCreator.getInsertSql(tableVO, paramMap);// 获取insert
            res=maintainService.doInsert(insertSql,paramMap);
            maintainService.doUpdateCache(tableVO.getDataTabName());//更新缓存
            maintainService.doInsertLog(tableVO, null,paramMap,"1");
        }
        
        return res;
	}
	
	/**
	 *Discription:修改初始化
	 *@param model
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = "modifyInit")
	public String modifyInit(Model model,HttpServletRequest request){
		
		Map<String,String> paramMap=this.getParamMap(request);
		String tabId=paramMap.get("tabId");
		
        Session session=UserUtils.getSession();
        TableVO tableVO=(TableVO)session.getAttribute("tableVO"+tabId);//先从session中取，如果为空再从数据库中取
        if(tableVO==null){
        	tableVO= maintainService.initTable(paramMap);//从数据库中获取配置
        	session.setAttribute("tableVO"+tabId, tableVO);
        }
        
        String selectSql = SqlCreator.getUpdateSelectSql(tableVO, paramMap);
        Map<String,String> dataMap = maintainService.getDataMap(selectSql);
        
        model.addAttribute("tableVO", tableVO);//配置
        model.addAttribute("paramMap", paramMap);//参数map
        model.addAttribute("tabId", tabId);//配置
        model.addAttribute("dataMap", dataMap);//数据
        return "modules/maintain/maintainModify";
	}
	
	/**
	 *Discription:修改
	 *@param model
	 *@param request
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	@RequestMapping(value = "modify")
	@ResponseBody
	public String doModify(Model model,HttpServletRequest request){
		String res="1";
		Map<String,String> paramMap=this.getParamMap(request);
		String tabId=paramMap.get("tabId");
		
        Session session=UserUtils.getSession();
        TableVO tableVO=(TableVO)session.getAttribute("tableVO"+tabId);//先从session中取，如果为空再从数据库中取
        if(tableVO==null){
        	tableVO= maintainService.initTable(paramMap);//从数据库中获取配置
        	session.setAttribute("tableVO"+tabId, tableVO);
        }
        String selectSql = SqlCreator.getUpdateSelectSql(tableVO, paramMap);
        Map<String,String> oldMap = maintainService.getDataMap(selectSql);
        
        /** 唯一性验证 gaojd 20150723 --begin **/
        List<JSONArray> valueList = new ArrayList<JSONArray>();
        List<ColumnVO> columnList = tableVO.getColumnList();
        for (ColumnVO column : columnList) {
            if ("file".equalsIgnoreCase(column.getColShowType()) || "1".equals(column.getIfPk())
                    || !"".equals(StringUtils.nvl(column.getDefaultValue()))
                    || "0".equals(column.getIfModify())) {
                continue;
            }
            JSONArray array = new JSONArray();
            array.add(paramMap.get(column.getColNameEn()));
            valueList.add(array);
        }
        String whereClause=""; 
        if(tableVO.getUpdateWhere()!=null&&!"".equals(tableVO.getUpdateWhere())){
        	whereClause+=tableVO.getUpdateWhere();
		}
        if(!"".equals(whereClause)){
        	whereClause=whereClause.replaceAll("=", "<>");
        }
        res = maintainService.doUniqueValidate(tableVO, valueList, SqlCreator.replaceParam(whereClause,paramMap));// 唯一性验证
        if("true".equals(res)){
        	String updateSql = SqlCreator.getUpdateSql(tableVO, paramMap);// 获取insert
            try{
            	maintainService.doUpdate(updateSql);
            	maintainService.doUpdateCache(tableVO.getDataTabName());//更新缓存
            	String newSelectSql = maintainService.getNewSelectSql(tableVO,paramMap,selectSql);
            	Map<String,String> newMap = maintainService.getDataMap(newSelectSql);
            	maintainService.doInsertLog(tableVO, oldMap,newMap,"2");
            	res="1";
            }catch(Exception e){
            	logger.error(e.getMessage());
            	res="0";
            }
        }
        
        return res;
	}
	/**
	 * 
	 *Discription:初始化参数.
	 *@param paramMap
	 *@param conditionList
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月29日 gaojingdan [变更描述]
	 */
	private Map<String,String> initParam(Map<String,String> paramMap,List<ConditionVO> conditionList) {
        for (ConditionVO conditionVO : conditionList) {
            String condeCode = conditionVO.getCondCode();
            if (!paramMap.containsKey(condeCode)) {
                paramMap.put(condeCode, conditionVO.getCondDefault());
            }
        }
        return paramMap;
    }
	/**
	 * 
	 *Discription:从request中获取参数map
	 *@param request
	 *@return
	 *@throws UnsupportedEncodingException
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月29日 gaojingdan [变更描述]
	 */
	private Map<String,String> getParamMap(HttpServletRequest request) {
        Map<String,String> paramMap = new HashMap<String,String>();
        Map<String,String[]> map = request.getParameterMap();
        if (map != null) {
        	for(Entry<String,String[]> entry:map.entrySet()){
        		String name=entry.getKey();
        		String value="";
				try {
					value = StringEscapeUtils.unescapeHtml4(StringUtils.array2String(entry.getValue(), ","));
				} catch (Exception e) {
					logger.error("MaintainController--getParamMap:URLDecoder decode error:"+e.getMessage());
				}
        		paramMap.put(name, value);
        	}
        }
        
        User user=UserUtils.getUser();//获取当前登录用户
        String workNO = user.getLoginName();// 登录账号
        String employeeName = user.getName();// 用户姓名
        String employeeID = user.getId();// 用户id
        paramMap.put("WOKER_NO", workNO);
        paramMap.put("EMPLOYEE_NAME", employeeName);
        paramMap.put("EMPLOYEE_ID", employeeID);
        //增加保障类型
        String jobKind="";
        List<JobKind> kindList=UserUtils.getCurrentJobKind();
        if(kindList!=null &&!kindList.isEmpty()){
        	jobKind=kindList.get(0).getKindCode();
        }
        paramMap.put("JOB_KIND", jobKind);
        
        /*HashMap<String,String> authParamMap = auth.getParamMap();// 用户权限信息
        paramMap.putAll(authParamMap);*/
        return paramMap;
    }
	
	private List<List<String>> dealData(List<JSONObject> jsonList,List<ColumnVO> colList){
		List<List<String>> dataList=new ArrayList<List<String>>();
		if(jsonList!=null&&!jsonList.isEmpty()){
			for(JSONObject json:jsonList){
				List<String> data=new ArrayList<String>();
				for(ColumnVO col:colList){
					data.add(json.getString(col.getColNameEn().toUpperCase()));
				}
				dataList.add(data);
			}
		}
		return dataList;
	}
}
