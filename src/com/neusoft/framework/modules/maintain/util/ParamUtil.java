/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月31日 下午2:27:05
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.framework.modules.maintain.util;

import java.util.HashMap;
import java.util.Map;

public class ParamUtil {
	/**
     *Discription:配置工具用到，解析展现值，格式为xxxx~~code1|code2~~value1|value2
     *@param Map<String,String>
     *@return
     *@return:返回值意义
     *@author:gaojingdan
     *@update:2017年8月31日 gaojingdan [变更描述]
     */
    public static Map<String,String> getDisplayValue(String val){
    	Map<String,String> res=new HashMap<String,String>();
    	String displayValue=val;
    	String pkParam="";
    	if(val!=null){
    		if(val.indexOf("~~")>=0){
				String[] temp=val.split("~~");
				String[] pkcodes=temp[1].split("\\|");
				String[] pkvalues=temp[2].split("\\|");
				for(int m=0;m<pkcodes.length;m++){
					pkParam+=pkcodes[m]+"="+pkvalues[m];
					if(m!=pkcodes.length-1){
						pkParam+="&";
					}
				}
				displayValue=temp[0];
    		}
    	}
    	res.put("value",displayValue);
    	res.put("pkParam",pkParam);
    	return res;
    }
}
