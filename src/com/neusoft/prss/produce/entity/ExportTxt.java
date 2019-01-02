package com.neusoft.prss.produce.entity;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neusoft.framework.common.utils.StringUtils;

public class ExportTxt {

	private static Logger log = LoggerFactory.getLogger(ExportTxt.class);
			
	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public ExportTxt write(OutputStream os) throws IOException{
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportTxt write(HttpServletResponse response) throws IOException{
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param fileName 输出文件名
	 */
	public ExportTxt writeFile(String name) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}
	
	/**
	 * 清理临时文件
	 */
	public ExportTxt dispose(){
		return this;
	}
	public void setBridgeChargingData(List<Map<String,String>> dataList,HttpServletResponse response){

		BufferedOutputStream buff = null;  
		ServletOutputStream outSTr = null; 
		try {
			StringBuffer write = new StringBuffer();         
			String enter = "\r\n";                 
			outSTr = response.getOutputStream();  // 建立         
			buff = new BufferedOutputStream(outSTr);  
			//把内容写入文件  
			for (int i=0;i<dataList.size();i++){
				Map<String,String> data = dataList.get(i);
				String startDay = data.get("START_DAY");
				if(StringUtils.isBlank(startDay))
					startDay = "        ";
				String flightNumber = data.get("FLIGHT_NUMBER2");
				String inOutFlag = data.get("IN_OUT_FLAG");
				if(StringUtils.isBlank(inOutFlag)) {
					inOutFlag = " ";
				} else {
					inOutFlag = inOutFlag.substring(0, 1);
				}
				String id = "";
				if(StringUtils.isBlank(flightNumber))
					flightNumber = "      ";
				else if(flightNumber.length() ==5)
					flightNumber = flightNumber + " ";
				else if(flightNumber.length() ==4)
					flightNumber = flightNumber + "  ";
				else if(flightNumber.length() ==3)
					flightNumber = flightNumber + "   ";
				else if(flightNumber.length() ==2)
					flightNumber = flightNumber + "    ";
				else if(flightNumber.length() ==1)
					flightNumber = flightNumber + "     ";
				id = startDay + flightNumber + inOutFlag;
				write.append( id+ "     ");
				write.append(startDay + " 000000");
				write.append(flightNumber + "    ");
				write.append(inOutFlag + "N-");
				String bridgeId = data.get("BRIDGE_ID");
				if(StringUtils.isBlank(bridgeId)) {
					bridgeId = "00";
				} else if(bridgeId.length()==1) {
					bridgeId = "0" + bridgeId;
				}
				write.append( bridgeId + "  ");
				String putTime = data.get("PUT_TIME");
				String put = "";
				if(StringUtils.isNotBlank(putTime))
					put = putTime.substring(0, 8) + " " + putTime.substring(8);
				else 
					put = "               ";
				write.append( put);
				String leaveTime = data.get("LEAVE_TIME");
				String leave = "";
				if(StringUtils.isNotBlank(leaveTime))
					leave = leaveTime.substring(0, 8) + " " + leaveTime.substring(8);
				else 
					leave = "               ";
				write.append(leave);
				write.append("                                                  ");
				write.append(enter);
			}
			buff.write(write.toString().getBytes("UTF-8"));         
			buff.flush();         
			buff.close();        

		} catch (Exception e) {
			e.printStackTrace();
			log.info("廊桥计费统计导出失败：{}",e.getMessage());
		} finally {         
            try {         
                buff.close();         
                outSTr.close();         
            } catch (Exception e) {         
            	log.info("廊桥计费统计导出失败：{}",e.getMessage());
           }         
       }  
	}
	
}
