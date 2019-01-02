package com.neusoft.framework.modules.maintain.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.maintain.entity.ColumnVO;
import com.neusoft.framework.modules.maintain.entity.TableVO;
import com.neusoft.framework.modules.maintain.service.MaintainService;

public class SqlCreator {
	@Autowired
	private static MaintainService maintainService;
	/**
	 * 创建查询SQL
	 * @param TableVO tableVO
	 * @param Map<String,String> paramMap
	 * @return String
	 * @author gaojd 20140225
	 */
	public static String getSelectSql(TableVO tableVO,Map<String,String> paramMap){
		String sql=" SELECT ";		
		String whereExp=" WHERE ";
		String selectExp="";
		String orderByExp=" ORDER BY ";
		if(tableVO.getQueryTabWhere()!=null&&!"".equals(tableVO.getQueryTabWhere())){
			whereExp+=tableVO.getQueryTabWhere();
		}else{
			whereExp+=" 1=1 ";
		}
		List<ColumnVO> columnList=tableVO.getColumnList();
		if(columnList!=null){
			for(ColumnVO column:columnList){
				if(!"0".equals(StringUtils.nvl(column.getColShowType()))){
					if("password".equalsIgnoreCase(column.getColShowType())){
						selectExp+="'******' AS "+column.getColNameEn().toUpperCase()+",";
					}else{
						selectExp+=column.getColQueryExpress().toUpperCase()+" AS "+column.getColNameEn().toUpperCase()+",";
					}
				}
				if(!"".equals(StringUtils.nvl(column.getIfOrder()))){
					String orderTemp=column.getColQueryExpress();
					if(orderTemp.indexOf("~~")>=0){
						orderTemp=orderTemp.split("~~")[0].replaceAll("\\|\\|'", "");
					}
					orderByExp+=orderTemp+" "+StringUtils.nvl(column.getIfOrder())+",";
				}
			}
			/**如果排序为空，默认取第一列*/
			if(" ORDER BY ".equals(orderByExp)){
				orderByExp+=columnList.get(0).getColQueryExpress()+" DESC	,";
			}
		}
		sql+=selectExp+" ROW_NUMBER() OVER("+orderByExp.substring(0, orderByExp.length()-1)+") AS ROW_NUM ";
		sql+=" FROM "+tableVO.getQueryTabName();
		sql+=whereExp;
		sql+=" ORDER BY ROW_NUM ASC ";
		return "SELECT * FROM ("+replaceParam(sql,paramMap)+")";
	}
	/**
	 * 获取主键值
	 * @param String tableName
	 * @param String pkExpress
	 * @return String
	 * @author gaojd 20140225
	 */
	public static String getMaxId(String tableName,String pkExpress){
		String sql=" SELECT NVL(MAX("+pkExpress+"),0)+1 AS NO FROM "+tableName;
		/*List<JSONObject> dataList=maintainService.getDataList(sql);
		String value=getStringFromList(dataList);*/
		String value=maintainService.getDataString(sql);
		return value;
	}
	/**
	 * 创建DELETE SQL
	 * @param TableVO tableVO
	 * @param Map<String,String> paramMap
	 * @return String
	 * @author gaojd 20140225
	 */
	public static String getDeleteSql(TableVO tableVO,Map<String,String> paramMap){
		String sql=" DELETE FROM "+tableVO.getDataTabName()+" WHERE 1=1";
		if(tableVO.getUpdateWhere()!=null&&!"".equals(tableVO.getUpdateWhere())){
			sql+=" AND "+tableVO.getUpdateWhere();
		}
		return replaceParam(sql,paramMap);
	}
	/**
	 * 创建UPDATE SQL
	 * @param TableVO tableVO
	 * @param Map<String,String> paramMap
	 * @return String
	 * @author gaojd 20140225
	 */
	public static String getUpdateSql(TableVO tableVO,Map<String,String> paramMap){
		String sql=" UPDATE  "+tableVO.getDataTabName()+" SET ";
		List<ColumnVO> columnList=tableVO.getColumnList();
		String whereExpree=" WHERE 1=1 ";
		if(columnList!=null){
			for (ColumnVO column : columnList) {
				if (!"1".equals(StringUtils.nvl(column.getIfPk()))
						&& "1".equals(StringUtils.nvl(column
								.getIfUpdate()))&&!"0".equals(column.getIfModify())) {
					if (!"".equals(StringUtils.nvl(
							column.getDefaultValue(), ""))) {
						sql += column.getColNameEn() + "=("
								+ column.getDefaultValue() + "),";
					} else {
						sql += column.getColNameEn()
								+ "='"
								+ replaceStr(StringUtils.nvl(paramMap
										.get(column.getColNameEn()))) + "',";
					}
				}
			}
		}
		if(tableVO.getUpdateWhere()!=null&&!"".equals(tableVO.getUpdateWhere())){
			whereExpree+=" AND "+tableVO.getUpdateWhere();
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=whereExpree;
		return replaceParam(sql,paramMap);
	}
	/**
	 * 创建UPDATE  SELECT SQL
	 * @param TableVO tableVO
	 * @param Map<String,String> paramMap
	 * @return String
	 * @author gaojd 20140225
	 */
	public static String getUpdateSelectSql(TableVO tableVO,Map<String,String> paramMap){
		String sql=" SELECT   ";
		List<ColumnVO> columnList=tableVO.getColumnList();
		String whereExpree=" WHERE 1=1 ";
		if(columnList!=null){
			for(ColumnVO column:columnList){
				sql+=column.getColUpdateExpress()+",";
			}
		}
		if(tableVO.getUpdateWhere()!=null&&!"".equals(tableVO.getUpdateWhere())){
			whereExpree+=" AND "+tableVO.getUpdateWhere();
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=" FROM "+tableVO.getDataTabName()+" ";
		sql+=whereExpree;
		return replaceParam(sql,paramMap);
	}
	
	public static String getInsertSql(TableVO tableVO,Map<String,String> paramMap){
		return getInsertSql(tableVO,false,paramMap);
	}
	
	public static String getImportInsertSql(TableVO tableVO,Map<String,String> paramMap){
		return getInsertSql(tableVO,true,paramMap);
	}
	/**
	 * 
	 *<p>Discription:INSERT SQL</p>
	 *@param tableVO
	 *@return
	 *@return [返回值含义]
	 *@author <a href="mailto: gao.jd@neusoft.com">gaojd</a>
	 *@update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getInsertSql(TableVO tableVO,boolean ifImport,Map<String,String> paramMap){
		String sql=" INSERT INTO   "+tableVO.getDataTabName()+" ( ";
		List<ColumnVO> columnList=tableVO.getColumnList();
		String express="";
		String values="  ";
		if(columnList!=null){
			for(ColumnVO column:columnList){
				if(column.getIfInsert()!=null&&"0".equals(column.getIfInsert())){
					continue;
				}
				if(ifImport&&"file".equalsIgnoreCase(column.getColShowType())){
					continue;
				}
				express+=column.getColNameEn()+",";
				if("1".equals(StringUtils.nvl(column.getIfPk()))){
					if(!"".equals(StringUtils.nvl(column.getPkSequence()))){
						values+=column.getPkSequence()+".NEXTVAL,";
					}else{
						values+="(SELECT NVL(MAX(TO_NUMBER("+column.getColNameEn()+")),0)+1 FROM "+tableVO.getDataTabName()+") ,";
					}
				}else{
					if(!"".equals(StringUtils.nvl(column.getDefaultValue(),""))){
						values+="("+column.getDefaultValue()+"),";
					}else{
						values+="${"+column.getColNameEn()+"},";
					}
				}
			}
		}
		sql+=express.substring(0,express.length()-1)+" )";
		sql+=" VALUES (";
		sql+=values.substring(0,values.length()-1);
		sql+=" )";
		return replaceParam(sql,paramMap);
	}
	
	private static String replaceStr(String str){
		return str.replaceAll("'", "''");
	}
	
	public static String getStringFromList(List<JSONObject> dataList){
		String value="";
		if(dataList!=null&&!dataList.isEmpty()){
			JSONObject json=dataList.get(0);
			Set<String> keys=json.keySet();//取第一个key
			Iterator<String> iter=keys.iterator();
			if(iter.hasNext()){
				value=json.getString(iter.next());
			}
			
		}
		return value;
	}
	public static String replaceParam(String str, Map<String, String> paramMap) {
		int booleanInt = 0;
		String name;
		String value;
		Set<String> set = paramMap.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			for(int i=0;i<1;i++){
				try {
					String key=iter.next();
					name = "$" +key + "$";
					value = (paramMap.get(key) == null || paramMap.get(key).equals("")) ? "" : paramMap.get(key);
					while(str.indexOf(name)>0)
					{
						str = str.substring(0, str.indexOf(name))
								+ (value.equalsIgnoreCase("all") ? "%" : value)
								+ str.substring(str.indexOf(name) + name.length(),
										str.length());
					}
					booleanInt = str.indexOf(name);
				} catch (StringIndexOutOfBoundsException e) {
					booleanInt = -1;
				} catch (NullPointerException e) {
					System.out.println(e.toString());
					return str;
				}
				if (booleanInt >= 0)
					i--;
			}
		}
		return str;
	}
}
