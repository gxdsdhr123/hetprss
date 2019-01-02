/**
 *application name:message-send-service
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月18日 下午5:00:58
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.neusoft.framework.common.config.Global;

public class MsgsUtil {

    private final static Logger logger = LoggerFactory.getLogger(MsgsUtil.class);
    public static String remove(String query) throws Exception
    {
        String resultStr="";
        try {
            String url = Global.getConfig("cp.removemsg.url");
            String connectTimeout = Global.getConfig("rest.connectTimeout");
            String readTimeout = Global.getConfig("rest.readTimeout");
            URL restURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");   
//            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(Integer.parseInt(connectTimeout));
            conn.setReadTimeout(Integer.parseInt(readTimeout));
            conn.setDoOutput(true);
            conn.setAllowUserInteraction(false);
            PrintStream ps = new PrintStream(conn.getOutputStream());
            ps.print(query);
            ps.close();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while(null != (line=bReader.readLine()))
            {
                resultStr +=line;
            }
            bReader.close();
        } catch (Exception e) {
            logger.error("从CloudPush系统删除离线消息 {}", e.getMessage());
        }
        return resultStr;

    }

    
    public static String add(String query) throws Exception
    {
        String resultStr="";
        try {
            String url = Global.getConfig("cp.add.url");
            String connectTimeout = Global.getConfig("rest.connectTimeout");
            String readTimeout = Global.getConfig("rest.readTimeout");
            URL restURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");   
//            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(Integer.parseInt(connectTimeout));
            conn.setReadTimeout(Integer.parseInt(readTimeout));
            conn.setDoOutput(true);
            conn.setAllowUserInteraction(false);
            PrintStream ps = new PrintStream(conn.getOutputStream());
            ps.print(query);
            ps.close();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while(null != (line=bReader.readLine()))
            {
                resultStr +=line;
            }
            bReader.close();
        } catch (Exception e) {
            logger.error("从CloudPush系统删除离线消息 {}", e.getMessage());
        }
        return resultStr;

    }
    public static void main(String []args) {
        try {

//            {"AID":"cloudpush的appid", "UID":"和用户名匹配的设备id／或者用户名", "OS":"1"}
            String str = "{\"AID\":\""+Global.getConfig("cp.appId")+"\", \"UID\":\"M_1c1fb1fb02fb40b8b4f06a6c2a8f021d\", \"OS\":\"1\"}";
//            String str = "{\"DT\":\"P_4bb1b0961ed54067941deaa15e0adcf0\",\"CL\":\""+Global.getConfig("cp.cl")+"\",\"AID\":\""+Global.getConfig("cp.appId")+"\"}";
            String resultString = MsgsUtil.add(str);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
