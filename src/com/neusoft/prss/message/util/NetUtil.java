/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月31日 下午7:42:39
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.util;

import java.io.IOException;  
import java.net.InetAddress;  
import java.net.Socket;  
import java.net.UnknownHostException;  
  
public class NetUtil {  
      
    public static boolean isLoclePortUsing(String host,int port){  
        boolean flag = true;  
        try {  
            flag = isPortUsing(host, port);  
        } catch (Exception e) {  
        }  
        return flag;  
    }  
    public static boolean isPortUsing(String host,int port) throws UnknownHostException{  
        boolean flag = false;  
        InetAddress theAddress = InetAddress.getByName(host);  
        try {  
            Socket socket = new Socket(theAddress,port);  
            flag = true;  
        } catch (IOException e) {  
              
        }  
        return flag;  
    }  
}  
