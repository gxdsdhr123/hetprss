/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月16日 下午6:56:38
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

public class EchartsFactory {
    
    public static JSONObject creategraph(JSONObject obj){
            
        JSONObject option = new JSONObject();
        JSONObject title = new JSONObject();
        title.put("text", obj.getString("title"));
        option.put("title", title);
        option.put("animationDurationUpdate", 1500);
        option.put("animationEasingUpdate", "quinticInOut");
        JSONArray series = new JSONArray();
        JSONObject serie = new JSONObject();
        serie.put("type", "graph");
        serie.put("layout", "none");
        serie.put("symbolSize", 90);
        serie.put("symbol", "roundRect");
        serie.put("roam", false);
        serie.put("label", JSON.parseObject("{normal:{show:true}}"));
        serie.put("edgeSymbol", new String[]{"circle", "arrow"});
        serie.put("edgeSymbolSize", new int[]{4, 10});
        serie.put("lineStyle", JSON.parseObject("{normal:{width:2}}"));
        serie.put("itemStyle", JSON.parseObject("{normal:{color:'rgb(60, 141, 188)'}}"));
        JSONArray serie_datas = new JSONArray();
        JSONArray serie_links = new JSONArray();
        JSONArray datas = (JSONArray) JSONPath.eval(obj, "$.data");
        for(int i=0;i<datas.size();i++){
            JSONObject data = datas.getJSONObject(i);//{name:'1',x:1,y:2,target:'4'}
            JSONObject serie_data = new JSONObject();
            JSONObject serie_link = new JSONObject();
            serie_data.put("name", data.get("name"));
            serie_data.put("x", data.get("x"));
            serie_data.put("y", data.get("y"));
            serie_data.put("value", data.get("value"));
            serie_datas.add(serie_data);
            serie_link.put("target", data.get("name"));
            serie_link.put("source", data.get("target"));
            serie_links.add(serie_link);
        }
        serie.put("links", serie_links);
        serie.put("data", serie_datas);
        
        series.add(serie);
        option.put("series", series);
//        JSONObject tooltip = new JSONObject();
//        option.put("tooltip", tooltip);
        return option;
        
    }

}
