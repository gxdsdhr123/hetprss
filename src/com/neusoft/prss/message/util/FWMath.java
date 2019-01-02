/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月9日 下午1:58:16
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.util;

import java.math.*;

public class FWMath
{
    public static double add(final double dat1, final double dat2) {
        try {
            final BigDecimal bDat1 = new BigDecimal(dat1);
            final BigDecimal bDat2 = new BigDecimal(dat2);
            BigDecimal bValu = new BigDecimal(0.0);
            bValu = bDat1.add(bDat2);
            return bValu.doubleValue();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static double sub(final double dat1, final double dat2) {
        try {
            final BigDecimal bDat1 = new BigDecimal(dat1);
            final BigDecimal bDat2 = new BigDecimal(dat2);
            BigDecimal bValu = new BigDecimal(0.0);
            bValu = bDat1.subtract(bDat2);
            return bValu.doubleValue();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static double mult(final double dat1, final double dat2) {
        try {
            final BigDecimal bDat1 = new BigDecimal(dat1);
            final BigDecimal bDat2 = new BigDecimal(dat2);
            BigDecimal bValu = new BigDecimal(0.0);
            bValu = bDat1.multiply(bDat2);
            return bValu.doubleValue();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static double div(final double dat1, final double dat2) {
        try {
            final BigDecimal bDat1 = new BigDecimal(dat1);
            final BigDecimal bDat2 = new BigDecimal(dat2);
            BigDecimal bValu = new BigDecimal(0.0);
            bValu = bDat1.divide(bDat2, 8, 5);
            return bValu.doubleValue();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static double div(final double dat1, final double dat2, final int afterDot) {
        try {
            final BigDecimal bDat1 = new BigDecimal(dat1);
            final BigDecimal bDat2 = new BigDecimal(dat2);
            BigDecimal bValu = new BigDecimal(0.0);
            bValu = bDat1.divide(bDat2, afterDot, 5);
            return bValu.doubleValue();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static double EFFECT(final double nominal_rate, final int npery) {
        try {
            double dat1 = add(1.0, div(nominal_rate, npery));
            dat1 = Math.pow(dat1, npery);
            dat1 = sub(dat1, 1.0);
            return dat1;
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static double PV(final double rate, final double nper, final double pmt, final double fv, final int type) {
        try {
            double pv = 0.0;
            if (rate == 0.0) {
                pv = sub(0.0, fv);
                pv = sub(pv, mult(pmt, nper));
            }
            else {
                pv = sub(0.0, fv);
                double dat1 = add(1.0, rate);
                dat1 = Math.pow(dat1, nper);
                dat1 = sub(dat1, 1.0);
                dat1 = div(dat1, rate);
                dat1 = mult(dat1, pmt);
                dat1 = mult(dat1, add(1.0, mult(rate, type)));
                pv = sub(pv, dat1);
                dat1 = add(1.0, rate);
                dat1 = Math.pow(dat1, nper);
                pv = div(pv, dat1);
            }
            return pv;
        }
        catch (Exception ex) {
            return 0.0;
        }
    }
    
    public static double DB(final double cost, final double salvage, final int life, int period, int month) {
        int maxP = life;
        month = Math.max(1, month);
        month = Math.min(12, month);
        period = Math.max(1, period);
        if (month < 12) {
            period = Math.min(life + 1, period);
            maxP = life + 1;
        }
        else {
            period = Math.min(life, period);
        }
        double dat1 = 0.0;
        double rate = Math.pow(div(salvage, cost), div(1.0, life));
        rate = div(sub(1.0, rate), 1.0, 3);
        if (period == 1) {
            dat1 = mult(cost, rate);
            dat1 = mult(dat1, month);
            dat1 = div(dat1, 12.0);
        }
        else if (period == maxP) {
            double dat2 = 0.0;
            for (int i = 1; i < period; ++i) {
                dat2 = add(dat2, DB(cost, salvage, life, i, month));
            }
            dat1 = mult(sub(cost, dat2), rate);
            dat1 = mult(dat1, sub(12.0, month));
            dat1 = div(dat1, 12.0);
        }
        else {
            double dat2 = 0.0;
            for (int i = 1; i < period; ++i) {
                dat2 = add(dat2, DB(cost, salvage, life, i, month));
            }
            dat1 = mult(sub(cost, dat2), rate);
        }
        return dat1;
    }
    
    public static double DDB(final double cost, final double salvage, final int life, int period, final double factor) {
        period = Math.max(1, period);
        period = Math.min(life, period);
        double dat1 = 0.0;
        double dat2 = 0.0;
        for (int i = 1; i < period; ++i) {
            dat2 = add(dat2, DDB(cost, salvage, life, i, factor));
        }
        dat1 = sub(sub(cost, salvage), dat2);
        dat1 = mult(dat1, div(factor, life));
        return dat1;
    }
    
    public static double VDB(final double cost, final double salvage, final int life, int start_period, int end_period, final double factor, final boolean no_switch) {
        start_period = Math.max(0, start_period);
        end_period = Math.min(life, end_period);
        double dat2 = 0.0;
        for (int i = start_period; i < end_period; ++i) {
            dat2 = add(dat2, DDB(cost, salvage, life, i + 1, factor));
        }
        return dat2;
    }
    
    public static double FV(final double rate, final int nper, final double pmt, final double pv, final int type) {
        double dat1 = pv;
        if (type == 1) {
            for (int i = 1; i <= nper; ++i) {
                dat1 = mult(add(dat1, pmt), add(1.0, rate));
            }
        }
        else {
            for (int i = 1; i <= nper; ++i) {
                dat1 = add(mult(dat1, add(1.0, rate)), pmt);
            }
        }
        return dat1;
    }
    
    public static double FVSCHEDULE(final double principal, final double[] schedule) {
        double dat1 = principal;
        for (int i = 0; i < schedule.length; ++i) {
            dat1 = mult(dat1, add(1.0, schedule[i]));
        }
        return dat1;
    }
    
    public static double CUMIPMT(final double rate, final int nper, final double pv, final int start_period, final int end_period, final int type) {
        final double dat1 = 0.0;
        final double dat2 = 0.0;
        return dat1;
    }
    
    public static double NPER(final double rate, final double pmt, final double pv, final double fv, final double type) {
        return 0.0;
    }
    
    public static double NOMINAL(final double effect_rate, final int npery) {
        double dat1 = add(effect_rate, 1.0);
        dat1 = Math.pow(dat1, div(1.0, npery));
        dat1 = sub(dat1, 1.0);
        dat1 = mult(dat1, npery);
        dat1 = div(dat1, 1.0, 6);
        return dat1;
    }
    
    public static double NPV(final double rate, final double[] values) {
        final int n = values.length;
        double dat1 = 0.0;
        for (int i = 0; i < n; ++i) {
            dat1 = add(dat1, div(values[i], Math.pow(add(1.0, rate), i + 1)));
        }
        dat1 = div(dat1, 1.0, 6);
        return dat1;
    }
    
    public static double MIRR(final double[] values, final double finance_rate, final double reinvest_rate) {
        int pn = 0;
        int nn = 0;
        final int n = values.length;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] >= 0.0) {
                ++pn;
            }
        }
        final double rrate = Math.pow(add(1.0, reinvest_rate), n);
        final double frate = add(1.0, finance_rate);
        final double[] pvalues = new double[pn];
        final double[] nvalues = new double[values.length - pn];
        pn = 0;
        nn = 0;
        for (int j = 0; j < values.length; ++j) {
            if (values[j] >= 0.0) {
                pvalues[pn] = values[j];
                ++pn;
            }
            else {
                nvalues[nn] = values[j];
                ++nn;
            }
        }
        double dat1 = -NPV(reinvest_rate, pvalues);
        dat1 = mult(dat1, rrate);
        double dat2 = NPV(finance_rate, nvalues);
        dat2 = mult(dat2, frate);
        dat1 = div(dat1, dat2, 4);
        dat2 = div(1.0, n - 1, 4);
        dat1 = Math.pow(dat1, dat2);
        dat1 = sub(dat1, 1.0);
        return dat1;
    }
    
    public static double SLN(final double cost, final double salvage, final double life) {
        double dat1 = 0.0;
        dat1 = div(sub(cost, salvage), life);
        return dat1;
    }
    
    public static double SYD(final double cost, final double salvage, final double life, final double per) {
        double dat1 = 0.0;
        dat1 = mult(sub(cost, salvage), add(sub(life, per), 1.0));
        dat1 = mult(dat1, 2.0);
        dat1 = div(dat1, life);
        dat1 = div(dat1, add(life, 1.0));
        return dat1;
    }
    
    public static void main(final String[] args) {
        final double[] d = { -120.0, 39.0, 30.0, 21.0, 37.0, 46.0 };
    }
}
