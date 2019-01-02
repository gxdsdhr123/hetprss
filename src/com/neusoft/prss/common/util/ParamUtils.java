/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月30日 上午8:51:49
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.common.util;

import java.util.HashMap;

public class ParamUtils {

    
    public final static char LEFT = 1;

    public final static char RIGHT = 2;
    
    public final static char SPLIT1 = 0x03;
    
    public final static char SPLIT2 = 0x04;
    
    public final static String Null = "!";
    
    public final static String REG = "'([\\u4e00-\\u9fa50-9a-zA-Z\\-\\s]*)'";
    
    public static HashMap<String,String> map = new HashMap<String,String>();
    
    static{
        map.put("and", "&&");
        map.put("or", "||");
        map.put("lb", "(");
        map.put("rb", ")");
        map.put("add","+");
        map.put("sub","-");
        map.put("mul","*");
        map.put("div","/");
        map.put("eq","==");
        map.put("uneq","!=");
        map.put("gt","&gt;");
        map.put("gte","&gt;=");
        map.put("lt","&lt;");
        map.put("lte","&lt;=");
        map.put("match","match");
        map.put("contain","contain");
        map.put("lmatch","lmatch");
        map.put("rmatch","rmatch");
    }
    
}
