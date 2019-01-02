/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月9日 上午11:11:48
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.util;

import java.util.*;
import java.text.*;

public class FUtil
{
    public static void main(final String[] args) {
    }
    
    public static int formatI(final Object str) {
        try {
            return Integer.parseInt(String.valueOf(str));
        }
        catch (Exception ex) {
            return 0;
        }
    }
    
    public static long formatL(final Object str) {
        try {
            return Long.parseLong(String.valueOf(str));
        }
        catch (Exception ex) {
            return 0L;
        }
    }
    
    public static float formatF(final Object str) {
        try {
            return Float.parseFloat(String.valueOf(str));
        }
        catch (Exception ex) {
            return 0.0f;
        }
    }
    
    public static String formatS(final Object str) {
        if (str == null) {
            return "";
        }
        return String.valueOf(str);
    }
    
    public static String html(final Object txt) {
        String str = formatS(txt);
        if (str.length() <= 0) {
            return "";
        }
        str = replaceStr(str, "&", "&amp;");
        str = replaceStr(str, "<", "&lt;");
        str = replaceStr(str, ">", "&gt;");
        str = replaceStr(str, "\"", "&quot;");
        str = replaceStr(str, "'", "&apos;");
        return str;
    }
    
    public static String replaceStr(final String source, final String src, final String des) {
        String res = "";
        int pe = 0;
        for (int ps = source.indexOf(src); ps >= 0; ps = source.indexOf(src, pe)) {
            res = String.valueOf(res) + source.substring(pe, ps) + des;
            pe = ps + src.length();
        }
        res = String.valueOf(res) + source.substring(pe);
        return res;
    }
    
    public static String formatS(final Date date, final String f) {
        try {
            final SimpleDateFormat df = new SimpleDateFormat(f, Locale.getDefault());
            return df.format(date);
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public static Date formatD(final String d, final String f) {
        try {
            final SimpleDateFormat df = new SimpleDateFormat(f, Locale.getDefault());
            return df.parse(d);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Vector outputCalendarMonth(final int year, int month) {
        if (month <= 0) {
            month = 1;
        }
        final Vector ls = new Vector();
        final Calendar calen = Calendar.getInstance();
        calen.set(year, month - 1, 1);
        for (int dayofWeek = calen.get(7), i = 1; i < dayofWeek; ++i) {
            ls.add("none");
        }
        for (int i = 0; i < 31; ++i) {
            final String dstr = formatS(calen.getTime(), "yyyy-MM-dd");
            ls.add(dstr);
            calen.add(5, 1);
            final int m = calen.get(2);
            if (m >= month) {
                break;
            }
        }
        final int l = ls.size() % 7;
        if (l > 0) {
            for (int j = 0; j < 7 - l; ++j) {
                ls.add("none");
            }
        }
        return ls;
    }
    
    public static String getPrevMonth(final String datestr) {
        final Date date = formatD(datestr, "yyyy-MM-dd");
        date.setMonth(date.getMonth() - 1);
        return formatS(date, "yyyy-MM-dd");
    }
    
    public static String getNextMonth(final String datestr) {
        final Date date = formatD(datestr, "yyyy-MM-dd");
        date.setMonth(date.getMonth() + 1);
        return formatS(date, "yyyy-MM-dd");
    }
    
    public static Vector outputCalendarWeek(final String datestr) {
        final Vector ls = new Vector();
        final Date date = formatD(datestr, "yyyy-MM-dd");
        final Calendar calen = Calendar.getInstance();
        calen.setTime(date);
        final int dayofWeek = calen.get(7);
        if (dayofWeek == 1) {
            date.setDate(date.getDate() - 6);
        }
        else {
            date.setDate(date.getDate() - (dayofWeek - 2));
        }
        for (int i = 0; i < 7; ++i) {
            final String dstr = formatS(date, "yyyy-MM-dd");
            ls.add(dstr);
            date.setDate(date.getDate() + 1);
        }
        return ls;
    }
    
    public static String getPrevWeek(final String datestr) {
        final Date date = formatD(datestr, "yyyy-MM-dd");
        date.setDate(date.getDate() - 7);
        return formatS(date, "yyyy-MM-dd");
    }
    
    public static String getNextWeek(final String datestr) {
        final Date date = formatD(datestr, "yyyy-MM-dd");
        date.setDate(date.getDate() + 7);
        return formatS(date, "yyyy-MM-dd");
    }
    
    public static int[] splitTimeStrs(final String str, final boolean addDay) {
        final int[] hm = new int[3];
        if (str.length() == 4) {
            final String h = str.substring(0, 2);
            final String m = str.substring(2, 4);
            hm[1] = formatI(h);
            hm[2] = formatI(m);
            if (hm[1] < 5 && addDay) {
                hm[0] = 1;
            }
        }
        else if (str.length() == 6) {
            final String d = str.substring(0, 2);
            final String h2 = str.substring(2, 4);
            final String i = str.substring(4, 6);
            hm[0] = formatI(d);
            hm[1] = formatI(h2);
            hm[2] = formatI(i);
        }
        return hm;
    }
    
    public static String combinTimeStrs(final int[] dhm, final boolean hasDay) {
        String str = "";
        if (dhm.length == 1) {
            final String sM = "00" + String.valueOf(dhm[0]);
            str = "00" + sM.substring(sM.length() - 2);
            if (hasDay) {
                str = "00" + str;
            }
        }
        else if (dhm.length == 2) {
            final String sH = "00" + String.valueOf(dhm[0]);
            final String sM2 = "00" + String.valueOf(dhm[1]);
            str = String.valueOf(sH.substring(sH.length() - 2)) + sM2.substring(sM2.length() - 2);
            if (hasDay) {
                str = "00" + str;
            }
        }
        else if (dhm.length == 3) {
            final String sH = "00" + String.valueOf(dhm[1]);
            final String sM2 = "00" + String.valueOf(dhm[2]);
            str = String.valueOf(sH.substring(sH.length() - 2)) + sM2.substring(sM2.length() - 2);
            if (hasDay) {
                final String sD = "00" + String.valueOf(dhm[0]);
                str = String.valueOf(sD.substring(sD.length() - 2)) + str;
            }
        }
        return str;
    }
    
    public static int accountMinute(final int[] dhm) {
        int m = 0;
        if (dhm.length == 1) {
            m = dhm[0];
        }
        else if (dhm.length == 2) {
            m = dhm[0] * 60 + dhm[1];
        }
        else if (dhm.length == 3) {
            m = dhm[0] * 24 * 60 + dhm[1] * 60 + dhm[2];
        }
        return m;
    }
    
    public static int[] minuteToDHM(final int mins) {
        final int[] dhm = new int[3];
        if (mins >= 0) {
            dhm[0] = mins / 1440;
            final int m = mins % 1440;
            dhm[1] = m / 60;
            dhm[2] = m % 60;
        }
        else {
            final int h = (Math.abs(mins) + 1439) / 1440;
            dhm[0] = h * -1;
            final int i = h * 24 * 60 - Math.abs(mins);
            dhm[1] = i / 60;
            dhm[2] = i % 60;
        }
        return dhm;
    }
    
    public static int[] addTimeStrs(final String startT, final String delayT) {
        int[] dhm = new int[3];
        final int[] st = splitTimeStrs(startT, true);
        final int[] dt = splitTimeStrs(delayT, false);
        final int ms = accountMinute(st) + accountMinute(dt);
        dhm = minuteToDHM(ms);
        return dhm;
    }
    
    public static int[] subTimeStrs(final String endT, final String delayT) {
        int[] dhm = new int[3];
        final int[] st = splitTimeStrs(endT, true);
        final int[] dt = splitTimeStrs(delayT, false);
        final int ms = accountMinute(st) - accountMinute(dt);
        dhm = minuteToDHM(ms);
        return dhm;
    }
    
    public static String calcuRealflightDate(final String baseDateStr, final String hmStr) {
        String str = baseDateStr;
        final int m1 = accountMinute(splitTimeStrs(hmStr, false));
        if (m1 < 300) {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d1 = null;
            final Calendar cal = new GregorianCalendar();
            try {
                d1 = df.parse(baseDateStr);
            }
            catch (ParseException ex) {}
            cal.setTime(d1);
            cal.add(5, 1);
            str = df.format(cal.getTime());
        }
        return str;
    }
    
    public static String calcuFlightDate(final String baseRealDateStr, final String hmStr) {
        String str = baseRealDateStr;
        final int m1 = accountMinute(splitTimeStrs(hmStr, false));
        if (m1 < 300) {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d1 = null;
            final Calendar cal = new GregorianCalendar();
            try {
                d1 = df.parse(baseRealDateStr);
            }
            catch (ParseException ex) {}
            cal.setTime(d1);
            cal.add(5, -1);
            str = df.format(cal.getTime());
        }
        return str;
    }
    
    public static String calcuRealTime(final String baseDateStr, final String hmStr) {
        String str = baseDateStr;
        final int m1 = accountMinute(splitTimeStrs(hmStr, false));
        if (m1 < 300) {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d1 = null;
            final Calendar cal = new GregorianCalendar();
            try {
                d1 = df.parse(baseDateStr);
            }
            catch (ParseException ex) {}
            cal.setTime(d1);
            cal.add(5, 1);
            str = df.format(cal.getTime());
        }
        final String hm = combinTimeStrs(minuteToDHM(m1), false);
        if (hm.length() == 4) {
            str = String.valueOf(str) + " " + hm.substring(0, 2) + ":" + hm.substring(2);
        }
        return str;
    }
    
    public static String getNowFlightDate() {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar cal = new GregorianCalendar();
        if (cal.get(11) < 5) {
            cal.add(5, -1);
        }
        return df.format(cal.getTime());
    }
    
    public static String getNextFlightDate(final String fStr) {
        final SimpleDateFormat df = new SimpleDateFormat(fStr, Locale.getDefault());
        final Calendar cal = new GregorianCalendar();
        if (cal.get(11) > 5) {
            cal.add(5, 1);
        }
        return df.format(cal.getTime());
    }
    
    public static String getNextFlightDate() {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar cal = new GregorianCalendar();
        if (cal.get(11) > 5) {
            cal.add(5, 1);
        }
        return df.format(cal.getTime());
    }
    
    public static String getNextDate(final String datebase) {
        try {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            final Calendar cal = new GregorianCalendar();
            cal.setTime(df.parse(datebase));
            cal.add(5, 1);
            return df.format(cal.getTime());
        }
        catch (ParseException ex) {
            return "";
        }
    }
    
    public static String getLastDate(final String datebase) {
        try {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            final Calendar cal = new GregorianCalendar();
            cal.setTime(df.parse(datebase));
            cal.add(5, -1);
            return df.format(cal.getTime());
        }
        catch (ParseException ex) {
            return "";
        }
    }
    
    public static String getYesterdayStr() {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar cal = new GregorianCalendar();
        cal.add(5, -1);
        return df.format(cal.getTime());
    }
    
    public static String getLastweekTodayStr(final String datebase) {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar cal = new GregorianCalendar();
        try {
            cal.setTime(df.parse(datebase));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(5, -7);
        return df.format(cal.getTime());
    }
    
    public static String getLastweekTodayStr() {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar cal = new GregorianCalendar();
        cal.add(5, -7);
        return df.format(cal.getTime());
    }
    
    public static int checkTimeRange(final int[] timeRang1, final int[] timeRang2) {
        final int min1 = Math.min(timeRang1[0], timeRang1[1]);
        final int max1 = Math.max(timeRang1[0], timeRang1[1]);
        timeRang1[0] = min1;
        timeRang1[1] = max1;
        final int min2 = Math.min(timeRang2[0], timeRang2[1]);
        final int max2 = Math.max(timeRang2[0], timeRang2[1]);
        timeRang2[0] = min2;
        timeRang2[1] = max2;
        if (timeRang2[0] > timeRang1[1] || timeRang2[1] < timeRang1[0]) {
            return 0;
        }
        if (timeRang2[0] >= timeRang1[0] && timeRang2[1] <= timeRang1[1]) {
            return 1;
        }
        if (timeRang2[0] < timeRang1[0] && timeRang2[1] >= timeRang1[0] && timeRang2[1] <= timeRang1[1]) {
            return 2;
        }
        if (timeRang2[0] >= timeRang1[0] && timeRang2[0] <= timeRang1[1] && timeRang2[1] > timeRang1[1]) {
            return 3;
        }
        if (timeRang2[0] < timeRang1[0] && timeRang2[1] > timeRang1[1]) {
            return 4;
        }
        return 9;
    }
    
    public Vector parseVars(final String txt) {
        final Vector vars = new Vector();
        int pe = -1;
        int ps = txt.indexOf("@", pe + 1);
        while (ps > 0) {
            pe = txt.indexOf("@", ps + 1);
            if (pe > ps) {
                final String var = txt.substring(ps + 1, pe);
                if (!vars.contains(var)) {
                    vars.add(var);
                }
                ps = txt.indexOf("@", pe + 1);
            }
            else {
                ps = -1;
            }
        }
        return vars;
    }
}
