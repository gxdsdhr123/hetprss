/**
 *application name:prss
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2018-04-03 13:34:50
 *@author:wangtg
 *@version:[v1.0]
 */
package com.neusoft.framework.common.utils.ztree;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ZTreeUtils {
	/**
	 * 此方法仅用于分工、资质模块
	 * @author wangtg
	 * @date 2018-04-03
	 * @version v1.0
	 */
	public static JSONArray removeZTreeNode(JSONArray ajson,JSONArray sjson){
		ajson.removeAll(sjson);
		if(ajson.size()>0){ 
			for(int i=0;i<ajson.size();i++){  
				boolean addPNode=true;
				JSONObject job = ajson.getJSONObject(i);  
				String pId=job.getString("pId");
				//跳过本身就是父节点的节点
				if(!("0".equals(pId))){
				//再次遍历数组 如果没有id为pId的节点 就增加一个父节点（ZTree展现需要父节点）
					for(int j=0;j<ajson.size();j++){ 
						JSONObject parentJob = ajson.getJSONObject(j);  
						if(pId.equals(parentJob.getString("id"))){
							addPNode=false;
							break;
						}
					}
					if(addPNode){
						JSONObject pNode=new JSONObject();
						if("unknow".equals(pId)){
							pNode.put("name","未知");
						}else{
							pNode.put("name",pId);
						}
						pNode.put("id", pId);
						pNode.put("pId", "0");
						ajson.add(pNode);
					}
				}
				
			}
			
		}
		return ajson;
		
	}

}
