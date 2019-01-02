/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月6日 下午7:43:34
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.arrange.dao.GanttDao;

@Service
@Transactional(readOnly = true)
public class GanttService extends BaseService{
	@Autowired
	private GanttDao ganttDao;

	public List<Map<String,String>> ganttData(Map<String, Object> params) {
		List<Map<String,String>> result = ganttDao.ganttData(params);
		for(int i=0;i<result.size();i++){
			result.get(i).put("text", mergeFltNum(result.get(i).get("inFltno"),result.get(i).get("outFltno")));
		}
		return result;
	}
	
	private String mergeFltNum(String a,String b){
		if(a == null){
			if(b==null){
				return "";
			}else{
				return b;
			}
		}else if(b==null){
			return a;
		}
		String o = a.length()>=b.length()?a:b;
		int index = 0;
		for(int i=0;i<o.length();i++){
			if(a.length()<i+1 || b.length()<i+1){
				index = i;
				break;
			}else if(a.charAt(i) != b.charAt(i)){
				index = i;
				break;
			}
		}
		String res = a+"/"+b.substring(index);
		return res;
	}
}
