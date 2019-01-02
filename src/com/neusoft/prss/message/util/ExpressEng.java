/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月9日 上午11:06:43
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.util;

import java.util.regex.*;
import java.util.*;

public class ExpressEng
{
    public static final String PATT_VAR_CN = "\\[(([[\\w]|[^\\p{ASCII}]]+))\\]";
    public static final String PATT_LOGIC1 = "\\s((\u7b49\u4e8e|\u4e0d\u7b49\u4e8e|\u5927\u4e8e|\u5927\u4e8e\u7b49\u4e8e|\u5c0f\u4e8e|\u5c0f\u4e8e\u7b49\u4e8e|\u5e76\u4e14|\u6216\u8005))\\s";
    public static final String PATT_LOGIC2 = "\\s((\u5339\u914d|\u524d\u5339\u914d|\u5305\u542b|\u540e\u5339\u914d))\\s+('(([[^']|[^\\p{ASCII}]]*))')";
    Hashtable logicNams;
    
    public static void main(final String[] args) {
//        ([航空公司三字码]等于'CCA')或者([航空公司三字码]等于'CSZ')或者([航空公司三字码]等于'CCD')或者([航空公司三字码]等于'CNM')
//        new ExpressEng().test("[\u673a\u53f7] \u4e0d\u7b49\u4e8e[\u62a5\u6587\u673a\u53f7]");
        ExpressEng eng = new ExpressEng();
        String str = "([航空公司三字码]  等于 'CCA') 或者 ([航空公司三字码] 等于 'CSZ') 或者 ([航空公司三字码] 等于 'CCD') 或者 ([航空公司三字码] 等于 'CNM')";
//        eng.test(str);
        Map<String,String> varnames = new HashMap<String,String>();
        varnames.put("航空公司三字码", "CCA");
        Hashtable tempData = new Hashtable<String,String>();
        tempData.put("CCA", "CCA");

        Hashtable a = new Hashtable();
        a.putAll(tempData);
        a.putAll(varnames);
        varnames.putAll(a);
        String result = eng.compilerExpress2(str, varnames,tempData);
    }
    
    public ExpressEng() {
        (this.logicNams = new Hashtable()).put("\u7b49\u4e8e", "=");
        this.logicNams.put("\u4e0d\u7b49\u4e8e", "!=");
        this.logicNams.put("\u5927\u4e8e", ">");
        this.logicNams.put("\u5927\u4e8e\u7b49\u4e8e", ">=");
        this.logicNams.put("\u5c0f\u4e8e", "<");
        this.logicNams.put("\u5c0f\u4e8e\u7b49\u4e8e", "<=");
        this.logicNams.put("\u5e76\u4e14", "&&");
        this.logicNams.put("\u6216\u8005", "||");
    }
    
    private String formatS(final Object str) {
        if (str == null) {
            return "";
        }
        return String.valueOf(str);
    }
    
    public void test(final String condExpress) {
        if (condExpress == null || condExpress.length() <= 0) {
            return;
        }
        final String strRes = "";
        int pL = 0;
        final Pattern reg = Pattern.compile("\\[(([[\\w]|[^\\p{ASCII}]]+))\\]");
        final Matcher m = reg.matcher(new String(condExpress));
        while (m.find()) {
            final String repl = m.group(1);
            final int ps = m.start(0);
            final int pe = pL = m.end(0);
        }
    }
    
    public String compilerExpress(final String condExpress, final Map varnames) {
        if (condExpress == null || condExpress.length() <= 0) {
            return "";
        }
        String strRes = "";
        int pL = 0;
        final Pattern reg = Pattern.compile("\\[(([[\\w]|[^\\p{ASCII}]]+))\\]");
        final Matcher m = reg.matcher(new String(condExpress));
        while (m.find()) {
            final String repl = m.group(1);
            final int ps = m.start(0);
            final int pe = m.end(0);
            final String newStr = this.formatS(varnames.get(repl));
            strRes = String.valueOf(strRes) + condExpress.substring(pL, ps) + newStr;
            pL = pe;
        }
        String sourStr;
        strRes = (sourStr = String.valueOf(strRes) + condExpress.substring(pL));
        strRes = "";
        pL = 0;
        final Pattern reg2 = Pattern.compile("\\s((\u7b49\u4e8e|\u4e0d\u7b49\u4e8e|\u5927\u4e8e|\u5927\u4e8e\u7b49\u4e8e|\u5c0f\u4e8e|\u5c0f\u4e8e\u7b49\u4e8e|\u5e76\u4e14|\u6216\u8005))\\s");
        final Matcher m2 = reg2.matcher(new String(sourStr));
        while (m2.find()) {
            final String repl2 = m2.group(1);
            final int ps2 = m2.start(0);
            final int pe2 = m2.end(0);
            final String newStr2 = " " + this.formatS(this.logicNams.get(repl2)) + " ";
            strRes = String.valueOf(strRes) + sourStr.substring(pL, ps2) + newStr2;
            pL = pe2;
        }
        strRes = (sourStr = String.valueOf(strRes) + sourStr.substring(pL));
        strRes = "";
        pL = 0;
        final Pattern reg3 = Pattern.compile("\\s((\u5339\u914d|\u524d\u5339\u914d|\u5305\u542b|\u540e\u5339\u914d))\\s+('(([[^']|[^\\p{ASCII}]]*))')");
        final Matcher m3 = reg3.matcher(new String(sourStr));
        while (m3.find()) {
            final String lgic = m3.group(1);
            final String valu = m3.group(4);
            final int ps3 = m3.start(0);
            final int pe3 = m3.end(0);
            String newStr3 = "";
            if (lgic.equals("\u5339\u914d")) {
                newStr3 = " like '" + valu + "' ";
            }
            else if (lgic.equals("\u524d\u5339\u914d")) {
                newStr3 = " like '" + valu + "%' ";
            }
            else if (lgic.equals("\u5305\u542b")) {
                newStr3 = " like '%" + valu + "%' ";
            }
            else if (lgic.equals("\u540e\u5339\u914d")) {
                newStr3 = " like '%" + valu + "' ";
            }
            strRes = String.valueOf(strRes) + sourStr.substring(pL, ps3) + newStr3;
            pL = pe3;
        }
        strRes = (sourStr = String.valueOf(strRes) + sourStr.substring(pL));
        return sourStr;
    }
    
    public String compilerExpress2(final String condExpress, final Map varnames, final Hashtable data) {
        if (condExpress == null || condExpress.length() <= 0) {
            return "";
        }
        String strRes = "";
        int pL = 0;
        final Pattern reg = Pattern.compile("\\[(([[\\w]|[^\\p{ASCII}]]+))\\]");
        final Matcher m = reg.matcher(new String(condExpress));
        while (m.find()) {
            final String repl = m.group(1);
            final int ps = m.start(0);
            final int pe = m.end(0);
            final String var = this.formatS(varnames.get(repl));
            final String varV = (String) data.get(var);
            final String newStr = "'" + varV + "'";
            strRes = String.valueOf(strRes) + condExpress.substring(pL, ps) + newStr;
            pL = pe;
        }
        String sourStr;
        strRes = (sourStr = String.valueOf(strRes) + condExpress.substring(pL));
        strRes = "";
        pL = 0;
        final Pattern reg2 = Pattern.compile("\\s((\u7b49\u4e8e|\u4e0d\u7b49\u4e8e|\u5927\u4e8e|\u5927\u4e8e\u7b49\u4e8e|\u5c0f\u4e8e|\u5c0f\u4e8e\u7b49\u4e8e|\u5e76\u4e14|\u6216\u8005))\\s");
        final Matcher m2 = reg2.matcher(new String(sourStr));
        while (m2.find()) {
            final String repl2 = m2.group(1);
            final int ps2 = m2.start(0);
            final int pe2 = m2.end(0);
            final String newStr = " " + this.formatS(this.logicNams.get(repl2)) + " ";
            strRes = String.valueOf(strRes) + sourStr.substring(pL, ps2) + newStr;
            pL = pe2;
        }
        strRes = (sourStr = String.valueOf(strRes) + sourStr.substring(pL));
        strRes = "";
        pL = 0;
        final Pattern reg3 = Pattern.compile("\\s((\u5339\u914d|\u524d\u5339\u914d|\u5305\u542b|\u540e\u5339\u914d))\\s+('(([[^']|[^\\p{ASCII}]]*))')");
        final Matcher m3 = reg3.matcher(new String(sourStr));
        while (m3.find()) {
            final String lgic = m3.group(1);
            final String valu = m3.group(4);
            final int ps3 = m3.start(0);
            final int pe3 = m3.end(0);
            String newStr2 = "";
            if (lgic.equals("\u5339\u914d")) {
                newStr2 = " like '" + valu + "' ";
            }
            else if (lgic.equals("\u524d\u5339\u914d")) {
                newStr2 = " like '" + valu + "%' ";
            }
            else if (lgic.equals("\u5305\u542b")) {
                newStr2 = " like '%" + valu + "%' ";
            }
            else if (lgic.equals("\u540e\u5339\u914d")) {
                newStr2 = " like '%" + valu + "' ";
            }
            strRes = String.valueOf(strRes) + sourStr.substring(pL, ps3) + newStr2;
            pL = pe3;
        }
        strRes = (sourStr = String.valueOf(strRes) + sourStr.substring(pL));
        return sourStr;
    }
    
    public String replaceExpressValue(final String condExpress, final Map varnames, final Map data) {
        if (condExpress == null || condExpress.length() <= 0) {
            return "";
        }
        final String chg = ",TM_DEPARTCHG,TM_ARRIVALCHG,ZY_AIRFCHG,ZY_FLIGHTSTACHG,ZY_REASONCHG,ZY_DISCHARCHG,ZY_POSICHG,ZY_GATECHG,ZY_BRGCHG,ZY_BAGCHG,ZY_BELTCHG,";
        String strRes = "";
        int pL = 0;
        final Pattern reg = Pattern.compile("\\[(([[\\w]|[^\\p{ASCII}]]+))\\]");
        final Matcher m = reg.matcher(new String(condExpress));
        while (m.find()) {
            final String repl = m.group(1);
            final int ps = m.start(0);
            final int pe = m.end(0);
            final String varNameCn = repl;
            String varEn = "";
            if (varnames.containsKey(varNameCn)) {
                varEn = this.formatS(varnames.get(repl)).toLowerCase();
            }
            String value = this.formatS(data.get(varEn));
            if (chg.indexOf(String.valueOf(varEn.toUpperCase()) + ",") >= 0) {
                value = FUtil.replaceStr(value, "-", "变更为");
            }
            strRes = String.valueOf(strRes) + condExpress.substring(pL, ps) + value;
            pL = pe;
        }
        final String sourStr;
        strRes = (sourStr = String.valueOf(strRes) + condExpress.substring(pL));
        return sourStr;
    }
}
