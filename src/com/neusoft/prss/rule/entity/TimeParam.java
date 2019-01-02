/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月7日 上午9:00:58
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.rule.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TimeParam {
	public static JSONArray jsonArray=new JSONArray();
	static{
		jsonArray.add(JSONObject.parse("{name:'进港预落',en:'in_eta',no:'230102'}"));
		jsonArray.add(JSONObject.parse("{name:'进港计落',en:'in_sta',no:'230101'}"));
		jsonArray.add(JSONObject.parse("{name:'进港实落',en:'in_ata',no:'230103'}"));
		jsonArray.add(JSONObject.parse("{name:'出港预起',en:'out_etd',no:'230202'}"));
		jsonArray.add(JSONObject.parse("{name:'出港计起',en:'out_std',no:'230201'}"));
		jsonArray.add(JSONObject.parse("{name:'TAST跳变次数',en:'acdm_tast_chg',no:'230405'}"));
		jsonArray.add(JSONObject.parse("{name:'COBT',en:'acdm_cobt',no:'230404'}"));
		jsonArray.add(JSONObject.parse("{name:'EOBT',en:'acdm_eobt',no:'230403'}"));
		jsonArray.add(JSONObject.parse("{name:'T_TOBT',en:'acdm_t_tobt',no:'230401'}"));
		jsonArray.add(JSONObject.parse("{name:'A_TOBT',en:'acdm_a_tobt',no:'230402'}"));
		jsonArray.add(JSONObject.parse("{name:'入位时间',en:'acdm_stand_tm',no:'230301'}"));
        jsonArray.add(JSONObject.parse("{name:'计算ETD',en:'out_cetd',no:'230203'}"));
	}
	
}
