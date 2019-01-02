package com.neusoft.prss.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.multipart.MultipartFile;

public class MD5Util {
	/** 
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合 
     */ 
    protected static char  hexDigits[]   = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',  
            'd', 'e', 'f'                       };  
    protected static MessageDigest messagedigest = null;  
    static {  
        try {  
            messagedigest = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
    }  
  /**
   * 
   *<p>Discription:获取文件MD5值</p>
   *@param file
   *@return
   *@throws IOException
   *@return            
   *@exception        
   *@author             yandj 2014-11-5
   *
   */
    public static String getFileMD5String(File file) throws IOException {  
        InputStream fis;  
        fis = new FileInputStream(file);  
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        while ((numRead = fis.read(buffer)) > 0) {  
            messagedigest.update(buffer, 0, numRead);  
        }  
        fis.close();  
        return bufferToHex(messagedigest.digest());  
    }  
    
    public static String getFileMD5String(MultipartFile file) throws IOException {  
    	InputStream fis;  
        fis = (FileInputStream)file.getInputStream();
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        while ((numRead = fis.read(buffer)) > 0) {  
            messagedigest.update(buffer, 0, numRead);  
        }  
        fis.close();  
        return bufferToHex(messagedigest.digest());
    }
  
    private static String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);  
    }  
  
    private static String bufferToHex(byte bytes[], int m, int n) {  
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    }  
  
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换   
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同   
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换   
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }
    
    /**
     * 
     *<p>Discription:读取md5文件内容</p>
     *@param f
     *@return
     *@throws Exception
     *@return            
     *@exception        
     *@author             yandj 2014-11-5
     *
     */
    
	public static String readFile(File f) throws Exception{
		String s;
		BufferedReader br = new BufferedReader(new FileReader(f));
		while((s = br.readLine())!=null){
			 break;
		}
		br.close();
		return s;
	}
  
//    public static void main(String[] args) throws IOException {  
//        File file = new File("E:/orderData/CMBDSDWAL01002A2014111001002.075");  
//        String md5 = getFileMD5String(file);  
//        System.out.println("md5:" + md5);  
//        
//        file = new File("E:/orderData/CMBDSDWAL01001A2014111001001.075");  
//        md5 = getFileMD5String(file);  
//        System.out.println("md5:" + md5);  
//    	File file = new File("../monitor_config");
//    	File f[] = file.listFiles();
//    }  
  


}
