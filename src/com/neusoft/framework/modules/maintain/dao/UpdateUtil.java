/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月6日 上午11:09:31
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.framework.modules.maintain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neusoft.framework.common.utils.SpringContextHolder;

public class UpdateUtil {
	private static Logger logger = LoggerFactory.getLogger(UpdateUtil.class);
	private static String REGEX="\\$\\{(\\w+)\\}";//表达式正则
	
	public static String doInsert(String sql,Map<String,String> paramMap){
		SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)SpringContextHolder.getBean("sqlSessionFactory");
		String res="1";
		Connection conn=null;
		PreparedStatement pst=null;
		try{
			conn=sqlSessionFactory.getConfiguration().getEnvironment().getDataSource().getConnection();
			pst=conn.prepareStatement(sql.replaceAll(REGEX, "?"));
			List<String> paramList=parseParam(sql);
			for(int i=0;i<paramList.size();i++){
				pst.setString(i+1, paramMap.get(paramList.get(i)));
			}
			pst.executeUpdate();
		}catch(Exception e){
			logger.error("UpdateUtil-doInsert1 error:"+e.getMessage());
			if(e.getMessage().contains("ORA-12899")){
				res = "保存失败，字段太长！";
			}else if(e.getMessage().contains("ORA-00001")){
				res = "保存失败，存值不唯一！";
			}else{
				res = e.getMessage();
			}
		}finally{
			try {
				if(pst!=null)
					pst.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				logger.error("UpdateUtil-doInsert2 error:"+e.getMessage());
			}
		}
		return res;
	}
	
	public static String doQueryString(String sql,String value){
		SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)SpringContextHolder.getBean("sqlSessionFactory");
		String res="";
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			conn=sqlSessionFactory.getConfiguration().getEnvironment().getDataSource().getConnection();
			pst=conn.prepareStatement(sql);
			if(value!=null){
				pst.setString(1, value);
			}
			rs=pst.executeQuery();
			if(rs.next()){
				res=rs.getString(1);
			}
		}catch(Exception e){
			logger.error("UpdateUtil-doQueryString error:"+e.getMessage());
		}finally{
			try {
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				logger.error("UpdateUtil-doQueryString error:"+e.getMessage());
			}
		}
		return res;
	}
	public static List<String> parseParam(String sql){
		List<String> paramList=new ArrayList<String>();
		Pattern pattern=Pattern.compile(REGEX);
		Matcher m = pattern.matcher(sql);
		while(m.find()){
			String groupStr=m.group(0);
			paramList.add(groupStr.replaceAll(REGEX, "$1"));
		}
		return paramList;
	}
}
