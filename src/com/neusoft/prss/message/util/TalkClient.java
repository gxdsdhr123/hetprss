package com.neusoft.prss.message.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TalkClient {
	
	private InetAddress serverIp;
	private int serverPort;
	
	private static Logger logger = LoggerFactory.getLogger(TalkClient.class);
	
	public TalkClient(InetAddress serverIp,int serverPort){
		this.serverIp=serverIp;
		this.serverPort=serverPort;
	}
	

    public static void main(String[] args) throws Exception {
            TalkClient client;
            try {
                String mobile = "7003";
                boolean isPortUser = NetUtil.isLoclePortUsing("127.0.0.1",6000);
                client = new TalkClient(InetAddress.getByName("127.0.0.1"),6000);
//                client.run(mobile);
                Thread.sleep(1000);
//                client.stop(mobile);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                logger.info("通讯失败：" + e.getMessage());
            }
//      DatagramSocket  server = new DatagramSocket();
//      String sendStr = "MAKECALL:REQUEST:AUDIO:7003";
//        byte[] sendBuf;
//        sendBuf = sendStr.getBytes();
//        DatagramPacket sendPacket 
//            = new DatagramPacket(sendBuf , sendBuf.length , InetAddress.getByName("127.0.0.1") , 6000 );
//        server.send(sendPacket);
//        server.close();
    }
    
	public boolean run(String msg){
	    DatagramSocket server = null;
	    boolean result = false;
		try {
	        server = new DatagramSocket();
	        String sendStr = "MAKECALL:REQUEST:AUDIO:" + msg;
	        byte[] sendBuf;
	        sendBuf = sendStr.getBytes();
	        DatagramPacket sendPacket 
	            = new DatagramPacket(sendBuf , sendBuf.length , serverIp , serverPort );
	        server.send(sendPacket);
	        result = true;
		} catch (UnknownHostException e) {
            e.printStackTrace();
			logger.info("通讯失败：" + e.getMessage());
		} catch (IOException e) {
		    e.printStackTrace();
            logger.info("通讯失败：" + e.getMessage());
		} finally {
		    if(server != null)
		        server.close();
		}
		return result;
	}
	
	
	public boolean broadcast(String msg){
        DatagramSocket server = null;
        boolean result = false;
        try {
            server = new DatagramSocket();
            String sendStr = "BROADCAST:REQUEST:" + msg;
            byte[] sendBuf;
            sendBuf = sendStr.getBytes();
            DatagramPacket sendPacket 
                = new DatagramPacket(sendBuf , sendBuf.length , serverIp , serverPort );
            server.send(sendPacket);
            result = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.info("通讯失败：" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("通讯失败：" + e.getMessage());
        } finally {
            if(server != null)
                server.close();
        }
        return result;
    }
	
	public boolean stop(String msg){
        DatagramSocket server = null;
        boolean result = false;
        try {
            server = new DatagramSocket();
            String sendStr = "HANGUP:REQUEST:" + msg;
            byte[] sendBuf;
            sendBuf = sendStr.getBytes();
            DatagramPacket sendPacket 
                = new DatagramPacket(sendBuf , sendBuf.length , serverIp , serverPort );
            server.send(sendPacket);
           result = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.info("通讯失败：" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("通讯失败：" + e.getMessage());
        } finally {
            if(server != null)
                server.close();
        }
        return result;
    }
}
