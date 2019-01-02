/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月9日 下午1:57:29
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.util;

import java.util.regex.*;
import java.text.*;

public class FormulaComplie
{
    public static final String PATT_FIELD_SPLIT = "(\\s)|(=)|(\\>)|(\\<)|(\\&)|(\\|)|(\\s[L|l][I|i][K|k][E|e]\\s)";
    public static final String PATT_FIELD = "([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)";
    public static final String PATT_NUMERIC = "([\\-]?[\\.0-9Ee]+)";
    public static final String PATT_STRING = "[\\']([[^\\']|[^\\p{ASCII}]]*)[\\']";
    public static final String PATT_FUNCTION = "([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)\\s*(\\((([^\\(\\)]*))\\))";
    public static final String PATT_OPT_BRACKET = "[^\\w](\\(([^\\(\\)]*)\\))";
    public static final String PATT_OPT_MultDivi = "(\\*)|(/)|(\\%)";
    public static final String PATT_EXP_MultDivi = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\*)|(/)|(\\%))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    public static final String PATT_OPT_AddSub = "(\\+)|(\\-)";
    public static final String PATT_EXP_AddSub = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\+)|(\\-))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    public static final String PATT_OPT_AddStr = "(\\+)|(\\-)";
    public static final String PATT_EXP_AddStr = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((\\+)|(\\-))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])";
    public static final String PATT_OPT_Less = "(\\<=)|(\\<)";
    public static final String PATT_EXP_Less = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\<=)|(\\<))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    public static final String PATT_OPT_More = "(\\>=)|(\\>)";
    public static final String PATT_EXP_More = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\>=)|(\\>))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    public static final String PATT_OPT_Even = "(=)";
    public static final String PATT_EXP_Even = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    public static final String PATT_OPT_UnEven = "(\\!=)";
    public static final String PATT_EXP_UnEven = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\!=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    public static final String PATT_EXP_EvenString = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])";
    public static final String PATT_EXP_UnEvenString = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((\\!=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])";
    public static final String PATT_OPT_Like = "(\\s[Ll][Ii][Kk][Ee]\\s)";
    public static final String PATT_EXP_LikeString = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((\\s[Ll][Ii][Kk][Ee]\\s))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])";
    public static final String PATT_OPT_AND = "(\\&\\&)";
    public static final String PATT_EXP_LogicAnd = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\&\\&))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    public static final String PATT_OPT_OR = "(\\|\\|)";
    public static final String PATT_EXP_LogicOr = "(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\|\\|))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))";
    protected FWMath fMath;
    private int _$12220;
    private Pattern _$12221;
    private Pattern _$12222;
    private Pattern _$12223;
    private Pattern _$12224;
    private Pattern _$12225;
    private Pattern _$12226;
    private Pattern _$12227;
    private Pattern _$12228;
    private Pattern _$12229;
    private Pattern _$12230;
    private Pattern _$12231;
    private Pattern _$12232;
    private Pattern _$12233;
    private Pattern _$12234;
    
    public static void main(final String[] args) {
        final FormulaComplie f = new FormulaComplie();
        String str = "('CCA'  = 'CCA') || ('CCA' = 'CSZ') || ('CCA' = 'CCD') || ('CCA' = 'CNM')";
        try {
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected void testCheck(final String str) {
        final String strExp = " " + str;
        final Pattern p = Pattern.compile("([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)");
        final Matcher m = p.matcher(strExp);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); ++i) {
                System.out.println(i + ": " + m.group(i) + " :" + m.start(i) + ":" + m.end(i));
            }
        }
    }
    
    public FormulaComplie() {
        this.fMath = new FWMath();
        this._$12220 = 0;
        this._$12221 = Pattern.compile("([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)\\s*(\\((([^\\(\\)]*))\\))");
        this._$12222 = Pattern.compile("[^\\w](\\(([^\\(\\)]*)\\))");
        this._$12223 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\*)|(/)|(\\%))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
        this._$12224 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\+)|(\\-))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
        this._$12225 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((\\+)|(\\-))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])");
        this._$12226 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\<=)|(\\<))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
        this._$12227 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\>=)|(\\>))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
        this._$12228 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
        this._$12229 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\!=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
        this._$12230 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])");
        this._$12231 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((\\!=))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])");
        this._$12232 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])\\s*((\\s[Ll][Ii][Kk][Ee]\\s))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|[\\']([[^\\']|[^\\p{ASCII}]]*)[\\'])");
        this._$12233 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\&\\&))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
        this._$12234 = Pattern.compile("(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))\\s*((\\|\\|))\\s*(([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)|([\\-]?[\\.0-9Ee]+))");
    }
    
    public String loadVarValue(final String varName) {
        return "0";
    }
    
    public String loadFunctionValue(final String funName, final String paraStr) {
        return "0";
    }
    
    public String execute(final String expressStr) {
        return this.execute(expressStr, false);
    }
    
    public String execute(final String expressStr, final boolean singlevar) {
        String strObj = expressStr;
        try {
            this._$12220 = 0;
            strObj = this._$12243(expressStr);
            if (singlevar) {
                strObj = this.complieSingleVar(strObj);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return strObj;
    }
    
    private String _$12243(final String expressStr) throws Exception {
        String strObj;
        try {
            strObj = this.complieFunction(expressStr);
            strObj = this.complieBracket(strObj);
            strObj = this.complieMultDivi(strObj);
            strObj = this.complieAddSub(strObj);
            strObj = this.complieAddString(strObj);
            strObj = this.complieLessThan(strObj);
            strObj = this.complieMoreThan(strObj);
            strObj = this.complieEven(strObj);
            strObj = this.complieUnEven(strObj);
            strObj = this.complieEvenString(strObj);
//            strObj = this.complieUnEvenString(strObj);
//            strObj = this.complieLikeString(strObj);
//            strObj = this.complieLogicAnd(strObj);
//            strObj = this.complieLogicOr(strObj);
            if (!strObj.trim().equals(expressStr.trim()) && this._$12220 <= 100) {
                ++this._$12220;
                this._$12243(strObj);
            }
        }
        catch (Exception ex) {
            throw ex;
        }
        return strObj.trim();
    }
    
    protected String complieSingleVar(final String expressStr) {
        String strExp = " " + expressStr;
        final Pattern p = Pattern.compile("([[@][#][$][a-zA-Z][^\\p{ASCII}]]+[[\\w][^\\p{ASCII}]]*)");
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String exp1 = m.group(0);
            final String varName = m.group(1);
            final String value = this.loadVarValue(varName);
            strExp = strExp.substring(1, m.start(0)) + value + strExp.substring(m.end(0));
            strExp = this.complieFunction(strExp);
        }
        else {
            strExp = expressStr;
        }
        return strExp;
    }
    
    protected String complieFunction(final String expressStr) {
        String strExp = " " + expressStr;
        final Pattern p = this._$12221;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String exp1 = m.group(0);
            final String funName = m.group(1);
            final String funPara = m.group(3);
            final String value = this.loadFunctionValue(funName, funPara);
            strExp = strExp.substring(1, m.start(0)) + value + strExp.substring(m.end(0));
            strExp = this.complieFunction(strExp);
        }
        else {
            strExp = expressStr;
        }
        return strExp;
    }
    
    protected String complieBracket(final String expressStr) throws Exception {
        String strExp = " " + expressStr;
        final Pattern p = this._$12222;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String exp = m.group(1);
            final String exp2 = m.group(2);
            final String valu1 = this._$12243(exp2);
            strExp = strExp.substring(1, m.start(1)) + valu1 + strExp.substring(m.end(1));
            strExp = this.complieBracket(strExp);
        }
        else {
            strExp = expressStr;
        }
        return strExp;
    }
    
    protected String complieMultDivi(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12223;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(8);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                double dl = 0.0;
                double dr = 0.0;
                final String opt = m.group(4);
                if (m.group(2) != null) {
                    dl = this.formatD(this.loadVarValue(m.group(2)));
                }
                else {
                    dl = this.formatD(m.group(3));
                }
                if (m.group(9) != null) {
                    dr = this.formatD(this.loadVarValue(m.group(9)));
                }
                else {
                    dr = this.formatD(m.group(10));
                }
                String value = "0";
                if (opt.equals("*")) {
                    final FWMath fMath = this.fMath;
                    value = this.formatS(FWMath.mult(dl, dr));
                }
                if (opt.equals("/")) {
                    if (dr != 0.0) {
                        final FWMath fMath2 = this.fMath;
                        value = this.formatS(FWMath.div(dl, dr));
                    }
                    else {
                        value = "NaN";
                    }
                }
                if (opt.equals("%")) {
                    if (dr != 0.0) {
                        value = this.formatS(dl % dr);
                    }
                    else {
                        value = "NaN";
                    }
                }
                strExp = strExp.substring(0, m.start(0)) + value + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieAddSub(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12224;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(7);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                double dl = 0.0;
                double dr = 0.0;
                final String opt = m.group(4);
                if (m.group(2) != null) {
                    dl = this.formatD(this.loadVarValue(m.group(2)));
                }
                else {
                    dl = this.formatD(m.group(3));
                }
                if (m.group(8) != null) {
                    dr = this.formatD(this.loadVarValue(m.group(8)));
                }
                else {
                    dr = this.formatD(m.group(9));
                }
                String value = "0";
                if (opt.equals("+")) {
                    final FWMath fMath = this.fMath;
                    value = this.formatS(FWMath.add(dl, dr));
                }
                if (opt.equals("-")) {
                    final FWMath fMath2 = this.fMath;
                    value = this.formatS(FWMath.sub(dl, dr));
                }
                strExp = strExp.substring(0, m.start(0)) + value + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieAddString(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12225;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(7);
            String value = "";
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                if (m.group(2) != null) {
                    value += this.loadVarValue(m.group(2));
                }
                else {
                    value += m.group(3);
                }
                if (m.group(8) != null) {
                    value += this.loadVarValue(m.group(8));
                }
                else {
                    value += m.group(9);
                }
                strExp = strExp.substring(0, m.start(0)) + "'" + value + "'" + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieLessThan(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12226;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(7);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                double dl = 0.0;
                double dr = 0.0;
                final String opt = m.group(4);
                if (m.group(2) != null) {
                    dl = this.formatD(this.loadVarValue(m.group(2)));
                }
                else {
                    dl = this.formatD(m.group(3));
                }
                if (m.group(8) != null) {
                    dr = this.formatD(this.loadVarValue(m.group(8)));
                }
                else {
                    dr = this.formatD(m.group(9));
                }
                String value = "0";
                if (opt.equals("<=")) {
                    value = String.valueOf(dl <= dr);
                }
                if (opt.equals("<")) {
                    value = String.valueOf(dl < dr);
                }
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieMoreThan(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12227;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(7);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                double dl = 0.0;
                double dr = 0.0;
                final String opt = m.group(4);
                if (m.group(2) != null) {
                    dl = this.formatD(this.loadVarValue(m.group(2)));
                }
                else {
                    dl = this.formatD(m.group(3));
                }
                if (m.group(8) != null) {
                    dr = this.formatD(this.loadVarValue(m.group(8)));
                }
                else {
                    dr = this.formatD(m.group(9));
                }
                String value = "0";
                if (opt.equals(">=")) {
                    value = String.valueOf(dl >= dr);
                }
                if (opt.equals(">")) {
                    value = String.valueOf(dl > dr);
                }
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieEven(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12228;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(6);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                double dl = 0.0;
                double dr = 0.0;
                if (m.group(2) != null) {
                    dl = this.formatD(this.loadVarValue(varL));
                }
                else {
                    dl = this.formatD(m.group(3));
                }
                if (m.group(7) != null) {
                    dl = this.formatD(this.loadVarValue(varR));
                }
                else {
                    dr = this.formatD(m.group(8));
                }
                final String exp1 = m.group(0);
                final String value = String.valueOf(dl == dr);
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieUnEven(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12229;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(6);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                double dl = 0.0;
                double dr = 0.0;
                if (m.group(2) != null) {
                    dl = this.formatD(this.loadVarValue(varL));
                }
                else {
                    dl = this.formatD(m.group(3));
                }
                if (m.group(7) != null) {
                    dl = this.formatD(this.loadVarValue(varR));
                }
                else {
                    dr = this.formatD(m.group(8));
                }
                final String exp1 = m.group(0);
                final String value = String.valueOf(dl != dr);
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieEvenString(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12230;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(6);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                String dl = "";
                String dr = "";
                if (m.group(2) != null) {
                    dl = this.loadVarValue(varL);
                }
                else {
                    dl = m.group(3);
                }
                if (m.group(7) != null) {
                    dl = this.loadVarValue(varR);
                }
                else {
                    dr = m.group(8);
                }
                final String value = String.valueOf(dl.equals(dr));
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieUnEvenString(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12231;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(6);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                String dl = "";
                String dr = "";
                if (m.group(2) != null) {
                    dl = this.loadVarValue(varL);
                }
                else {
                    dl = m.group(3);
                }
                if (m.group(7) != null) {
                    dl = this.loadVarValue(varR);
                }
                else {
                    dr = m.group(8);
                }
                final String value = String.valueOf(!dl.equals(dr));
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieLikeString(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12232;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(6);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                String dl = "";
                String dr = "";
                if (m.group(2) != null) {
                    dl = this.loadVarValue(varL);
                }
                else {
                    dl = m.group(3);
                }
                if (m.group(7) != null) {
                    dl = this.loadVarValue(varR);
                }
                else {
                    dr = m.group(8);
                }
                if (dr.startsWith("%")) {
                    dr = dr.substring(1);
                }
                if (dr.endsWith("%")) {
                    dr = dr.substring(0, dr.length() - 1);
                }
                final String value = String.valueOf(dl.indexOf(dr) >= 0);
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieLogicAnd(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12233;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(6);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                boolean dl = false;
                boolean dr = false;
                if (m.group(2) != null) {
                    dl = m.group(2).trim().toLowerCase().equals("true");
                }
                else {
                    dl = (this.formatD(m.group(3)) >= 1.0);
                }
                if (m.group(7) != null) {
                    dr = m.group(7).trim().toLowerCase().equals("true");
                }
                else {
                    dr = (this.formatD(m.group(8)) >= 1.0);
                }
                final String value = String.valueOf(dl && dr);
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected String complieLogicOr(final String expressStr) throws Exception {
        String strExp = expressStr;
        final Pattern p = this._$12234;
        final Matcher m = p.matcher(strExp);
        if (m.find()) {
            final String varL = m.group(1);
            final String varR = m.group(6);
            if (varL != null && varR != null && varL.length() > 0 && varR.length() > 0) {
                boolean dl = false;
                boolean dr = false;
                if (m.group(2) != null) {
                    dl = m.group(2).trim().toLowerCase().equals("true");
                }
                else {
                    dl = (this.formatD(m.group(3)) >= 1.0);
                }
                if (m.group(7) != null) {
                    dr = m.group(7).trim().toLowerCase().equals("true");
                }
                else {
                    dr = (this.formatD(m.group(8)) >= 1.0);
                }
                final String value = String.valueOf(dl || dr);
                strExp = strExp.substring(0, m.start(0)) + " " + value + " " + strExp.substring(m.end(0));
                strExp = this._$12243(strExp);
            }
        }
        return strExp;
    }
    
    protected double formatD(final String str) {
        try {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException ex) {
            return 0.0;
        }
    }
    
    protected String formatS(final double dat) {
        try {
            final DecimalFormat df = new DecimalFormat("#0.000000000000");
            String str = df.format(dat);
            if (str.endsWith("000000")) {
                str = str.substring(0, str.length() - 6);
            }
            while (str.endsWith("000")) {
                if (str.endsWith(".0")) {
                    break;
                }
                str = str.substring(0, str.length() - 3);
            }
            while (str.endsWith("0") && !str.endsWith(".0")) {
                str = str.substring(0, str.length() - 1);
            }
            return str;
        }
        catch (NumberFormatException ex) {
            return String.valueOf(dat);
        }
    }
}
