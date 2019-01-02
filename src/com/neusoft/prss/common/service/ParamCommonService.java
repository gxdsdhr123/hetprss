/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月28日 下午5:11:33
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.common.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.common.dao.ParamCommonDao;
import com.neusoft.prss.common.util.ParamUtils;

@Service
public class ParamCommonService {

    @Resource
    private ParamCommonDao paramCommonDao;
    
    public String getColumn(HashMap<String,String> data) {
        String flag = data.get("schema");
        String text = data.get("text");
        String varcols = data.get("colids");
        if("99".equals(flag)){//报文
            String[] varArr = varcols.split(",");
            for(int i=0;i<varArr.length;i++){
                String colid= varArr[i];
                if(colid == null || "".equals(colid))
                    continue;
                JSONObject col = paramCommonDao.getColumn(colid);
                if(col == null)
                    continue;
                String colDes = col.getString("COL_DESC");//字段别名
                String id = col.getString("ID");
                String div = "<div class='div_class select' data-id="+id+">"+ 
                        ParamUtils.LEFT +colDes+ ParamUtils.RIGHT + "</div>";
                String reg = "\\[" + colDes.replace("(", "\\(").replace(")", "\\)") + "\\]";
                text = text.replaceFirst(reg, div);
            }
            text = text.replace("\n", "<br>");
        } else if("88".equals(flag)){//消息
            String[] varArr = varcols.split(",");
            for(int i=0;i<varArr.length;i++){
                String var = varArr[i];
                if(var.split("_").length<3) 
                    continue;
                String colid= var.split("_")[0];
                if(colid == null || "".equals(colid))
                    continue;
                JSONObject col = paramCommonDao.getColumn(colid);
                if(col == null)
                    continue;
                String ischg = var.split("_")[1];
                String chgval = var.split("_")[2];
                String colDes = col.getString("COL_DESC");//字段别名
                String id = col.getString("ID");
                if("1".equals(ischg)){
                    if("1".equals(chgval)){
                        colDes += "_旧值";
                    } else  if("2".equals(chgval)){
                        colDes += "_新值";
                    }
                    id += "_" + ischg + "_" + chgval;
                } else {
                    id += "_0_0";
                }
                String div = "<div class='div_class select' data-id="+id+">"+
                        ParamUtils.LEFT +colDes+ ParamUtils.RIGHT +"</div>";
                String reg = "\\[" + escapeExprSpecialWord(colDes) + "\\]";
                text = text.replaceFirst(reg, div);
            }
        } else if("RULE".equals(flag)){
            HashMap<String,String> map = ParamUtils.map;
            String expression = data.get("expression");
            String html = "";
            if(expression != null && !"".equals(expression)){
                Pattern pattern = Pattern.compile(ParamUtils.REG);
        		Matcher matcher = pattern.matcher(expression);
        		String split = String.valueOf(ParamUtils.SPLIT2);
        		while (matcher.find()) {
        			String group = matcher.group();
        			expression = expression.replace(group, group.replace("-", split));
        			
        		}
                String[] conditionArr = expression.split("\\|");
                for(int i=0;i<conditionArr.length;i++){
                    String conditionStr = conditionArr[i];
                    if(conditionStr.split("-").length<2)
                        continue;
                    String[] conArr = conditionStr.split("-");
                    String type = conArr[0];
                    String con_flag = conArr[1];
                    String tag_text = "";
                    switch (type) {
                        case "attr":
                            if(con_flag == null || "".equals(con_flag))
                                continue;
                            JSONObject col = paramCommonDao.getColumn(con_flag);
                            if(col == null)
                                continue;
                            String colDes = col.getString("COL_DESC");//字段别名
                            String col_name = col.getString("COL_ALIAS").toLowerCase();//字段名
                            String id = col.getString("ID");
                            tag_text = colDes;
                            html += "<div class='div_class select' data-id='"+id+"' data-en='"+col_name+"'>["+ 
                                    colDes + "]</div>";
                            break;
                        case "join":
                            tag_text = map.get(con_flag);
                            html += "<span class='span_class select' data-flag='"+
                                    con_flag+"' data-type='"+type+"'>"+ tag_text +"</span>";
                            break;
                        case "val":
                            if(conArr.length>2)
                                tag_text = conArr[2];
                            html += "<span class='span_class select' data-flag='"+
                                    con_flag+"' data-type='"+type+"'>"+ tag_text +"</span>";
                            break;
                        case "opt":
                            tag_text = map.get(con_flag);
                            html += "<span class='span_class select' data-flag='"+
                                    con_flag+"' data-type='"+type+"'>"+ tag_text +"</span>";
                            break;
                    }
                }
                html = html.replace(split, "-");
            }
            text = html;
        }
        text = text.replace(ParamUtils.LEFT + "" , "[").replace(ParamUtils.RIGHT + "", "]").replace("？", " ");
        return text;
    }

    public static String escapeExprSpecialWord(String keyword) {  
        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
        for (String key : fbsArr) {  
            if (keyword.contains(key)) {  
                keyword = keyword.replace(key, "\\" + key);  
            }  
        }  
        return keyword;  
    }  
    public JSONArray getVariable(Map<String,String> map) {
        return paramCommonDao.getVariable(map);
    }

    public String getAutoRuleId() {
        return paramCommonDao.getAutoRuleId();
    }
    
    public JSONArray getRuleList(Map<String,String> map){
        return paramCommonDao.getRuleList(map);
    }

    public JSONObject getRuleById(String id) {
        return paramCommonDao.getRuleById(id);
    }

}
