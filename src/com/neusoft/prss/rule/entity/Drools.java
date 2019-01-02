package com.neusoft.prss.rule.entity;

import java.util.HashMap;
import java.util.Map;

public class Drools {
	
	public static final String ATTR_NAME = "attrs.";

	/**
	 * 运算符标识
	 */
	public static final String OPT = "opt";
	/**
	 * 加
	 */
	public static final String OPT_ADD = "add"; 
	/**
	 * 减
	 */
	public static final String OPT_SUB = "sub";  
	/**
	 * 乘
	 */
	public static final String OPT_MUL = "mul";  
	/**
	 * 除
	 */
	public static final String OPT_DIV = "div";  
	/**
	 * 大于
	 */
	public static final String OPT_GT = "gt";  
	/**
	 * 大于等于
	 */
	public static final String OPT_GTE = "gte";  
	/**
	 * 小于
	 */
	public static final String OPT_LT = "lt";  
	/**
	 * 小于等于
	 */
	public static final String OPT_LTE = "lte";  
	/**
	 * 等于
	 */
	public static final String OPT_EQ = "eq";  
	/**
	 * 不等于
	 */
	public static final String OPT_UNEQ = "uneq";  
	/**
	 * 匹配
	 */
	public static final String OPT_MATCH = "match";  
	/**
	 * 前匹配
	 */
	public static final String OPT_LMATCH = "lmatch";  
	/**
	 * 后匹配
	 */
	public static final String OPT_RMATCH = "rmatch";  
	/**
	 * 包含
	 */
	public static final String OPT_CONTAIN = "contain";
	
	/**
	 * 连接符
	 */
	public static final String JOIN = "join";
	/**
	 * 左括号
	 */
	public static final String JOIN_LB = "lb";
	/**
	 * 右括号
	 */
	public static final String JOIN_RB = "rb"; 
	/**
	 * 并且
	 */
	public static final String JOIN_AND = "and"; 
	/**
	 * 或者
	 */
	public static final String JOIN_OR = "or"; 
	
	/**
	 * 值
	 */
	public static final String VAL = "val";
	/**
	 * int型值
	 */
	public static final String VAL_INT = "int";
	/**
	 * String型值
	 */
	public static final String VAL_STRING = "string";
	
	/**
	 * 属性
	 */
	public static final String ATTR = "attr";
	
	
	/**
	 * 1-机型 
	 */
	public final static String TYPE_ACTTYPE = "1";
	/**
	 * 2-航空公司
	 */
	public final static String TYPE_AIRLINE = "2";
	/**
	 * 3-进出港
	 */
	public final static String TYPE_INOUT = "3";
	/**
	 * 4-机位
	 */
	public final static String TYPE_APRON = "4";
	/**
	 * 5-航班号
	 */
	public final static String TYPE_FLIGHTNUMBER = "5";
	/**
     * 6-机号（注册号）
     */
    public final static String TYPE_AIRCRAFT = "6";
    
    /**
     * 7-子分公司
     */
    public final static String TYPE_BRANCH = "7";
	
	/**
	 * 系统内机型别名（唯一）
	 */
	public final static String SYS_ACTTYPE_CODE = "acttype_code";
	public final static int SYS_ACTTYPE_ID = 1164;
	
	/**
     * 系统内机号别名（唯一）
     */
    public final static String SYS_AIRCRAFT_NUMBER = "aircraft_number";
    public final static int SYS_AIRCRAFT_ID = 1165;
		
	/**
	 * 系统内机位别名（唯一）
	 */
	public final static String SYS_ACTSTANDKIND_CODE = "actstand_kind"; 
	public final static int SYS_ACTSTANDKIND_ID = 1163;
	
	/**
	 * 系统内航空公司别名（唯一）
	 */
	public final static String SYS_AIRLINE_CODE = "aln_2code";
	public final static int SYS_AIRLINE_ID = 2305;
	
	/**
	 * 系统内进出港标识别名（唯一）
	 */
	public final static String SYS_INOUTPORT_CODE = "flag_in_out";
	public final static int SYS_INOUTPORT_ID = 1162;
	
	/**
     * 系统内航班号别名（唯一）
     */
    public final static String SYS_FLIGHTNUMBER_CODE = "flight_number";
    public final static int SYS_FLIGHTNUMBER_ID = 402;

    
	public  static Map<String,String> VAR_COL_MAP=new HashMap<String,String>();
	static{
		VAR_COL_MAP.put(String.valueOf(SYS_ACTTYPE_ID),String.valueOf(SYS_ACTTYPE_ID) );
		VAR_COL_MAP.put(String.valueOf(SYS_ACTSTANDKIND_ID),String.valueOf(SYS_ACTSTANDKIND_ID) );
		VAR_COL_MAP.put(String.valueOf(SYS_AIRLINE_ID),String.valueOf(SYS_AIRLINE_ID) );
		VAR_COL_MAP.put(String.valueOf(SYS_INOUTPORT_ID),String.valueOf(SYS_INOUTPORT_ID) );
        VAR_COL_MAP.put(String.valueOf(SYS_FLIGHTNUMBER_ID),String.valueOf(SYS_FLIGHTNUMBER_ID) );
	}
}
