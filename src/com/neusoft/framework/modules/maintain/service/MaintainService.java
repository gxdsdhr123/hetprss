/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月28日 下午6:54:08
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.framework.modules.maintain.service;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.framework.modules.maintain.dao.MaintainDAO;
import com.neusoft.framework.modules.maintain.dao.UpdateUtil;
import com.neusoft.framework.modules.maintain.entity.ColumnVO;
import com.neusoft.framework.modules.maintain.entity.ConditionVO;
import com.neusoft.framework.modules.maintain.entity.TableVO;
import com.neusoft.framework.modules.maintain.util.SqlCreator;
import com.neusoft.framework.modules.sys.utils.UserUtils;
import com.neusoft.prss.cacheSyn.service.CacheSynService;

@Service
public class MaintainService extends BaseService {
	@Autowired
	private MaintainDAO maintainDAO;
	@Autowired
	private CacheSynService cacheSynService;
	/**
	 * Discription:初始化
	 * 
	 * @param paramMap
	 * @return
	 * @return:返回值意义
	 * @author:gaojingdan
	 * @update:2017年8月28日 gaojingdan [变更描述]
	 */
	public TableVO initTable(Map<String, String> paramMap) {
		TableVO tableVO = maintainDAO.initTable(paramMap);
		// 处理条件的默认值和数据集
		List<ConditionVO> condList = tableVO.getConditionList();
		if (condList != null && !condList.isEmpty()) {
			for (ConditionVO conditionVO : condList) {
				// 默认值处理
				String condDefault = StringUtils.nvl(conditionVO.getCondDefault());
				if (!"".equals(condDefault)) {
					String temp = maintainDAO.getDataString(SqlCreator.replaceParam(condDefault, paramMap));
					conditionVO.setCondDefault(temp);
				}else{
					conditionVO.setCondDefault("");
				}
				// 数据集处理
				String condString = StringUtils.nvl(conditionVO.getCondSQL(), "");
				if (!"".equals(condString)) {
					List<JSONObject> data = maintainDAO.getDataList(SqlCreator.replaceParam(condString, paramMap));
					conditionVO.setDataList(data);
				}
			}
		}
		// 处理列的默认值和下拉列表值
		List<ColumnVO> colList = tableVO.getColumnList();
		if (colList != null && !colList.isEmpty()) {
			for (ColumnVO colVO : colList) {
				// 默认值处理
				String insertDefault = colVO.getInsertDefautValue();
				if (insertDefault != null && !"".equals(insertDefault)) {
					try {
						String data = maintainDAO.getDataString(SqlCreator.replaceParam(insertDefault, paramMap));
						colVO.setInsertDefautValue(data);
					} catch (Exception e) {

					}
				}
				// 下拉值选择
				String colExpress = colVO.getColExpress();
				if (colExpress != null && !"".equals(colExpress)) {
					try {
						List<JSONObject> data = maintainDAO.getDataList(SqlCreator.replaceParam(colExpress, paramMap));
						colVO.setChooseDataList(data);
					} catch (Exception e) {

					}
				}
				//必填项处理
				String ifNull=colVO.getIfNull();
				String dataType=colVO.getColDataType();
				//最大长度处理,格式为：maxlength:11
				if(StringUtils.nvl(dataType).indexOf("maxlength")>=0){
					String regex="\\S*(maxlength:\\d+)\\S*";
					String temp=dataType.replaceAll(regex, "$1");
					String[] tempArray=temp.split(":");
					dataType=dataType.replaceAll("maxlength:\\d+", tempArray[0]);
					if(tempArray.length>1){
						colVO.setColValueMaxLength(tempArray[1]);
						colVO.setColDataType(dataType);
					}
				}
				//正则表达式验证处理,格式为：regValidate:正则式
				if(StringUtils.nvl(dataType).indexOf("regValidate")>=0){
					String regex="\\S*(regValidate:)\\S*";
					String temp=dataType.replaceAll(regex, "$1");
					String value = dataType.replace(temp, "");
					dataType = "regValidate";
					if(!StringUtils.isEmpty(value)){
						colVO.setColValueRegValidate(value);
						colVO.setColDataType(dataType);
					}
				}
				if(ifNull!=null&&"n".equalsIgnoreCase(ifNull)){
					if(StringUtils.nvl(dataType).indexOf("required")<0){
						colVO.setColDataType("required|"+dataType);
					}
				}
				
				
			}
		}
		return tableVO;
	}

	/**
	 * 
	 * Discription:传入SQL，返回查询结果
	 * 
	 * @param paramMap
	 * @return
	 * @return:返回值意义
	 * @author:gaojingdan
	 * @update:2017年8月29日 gaojingdan [变更描述]
	 */
	public List<JSONObject> getDataList(String sql) {
		return maintainDAO.getDataList(sql);
	}

	/**
	 * 
	 * Discription:传入SQL，返回查询结果
	 * 
	 * @param paramMap
	 * @return
	 * @return:返回值意义
	 * @author:gaojingdan
	 * @update:2017年8月29日 gaojingdan [变更描述]
	 */
	public List<JSONObject> getDataList(String sql, Map<String, String> paramMap) {
		String start = paramMap.get("start");
		String end = paramMap.get("end");
		if (!(start == null || end == null || "".equals(start) || "".equals(end) || "0".equals(end))) {
			sql += " WHERE ROW_NUM>=" + start + " AND ROW_NUM<=" + end;
		}
		return maintainDAO.getDataList(sql);
	}

	/**
	 * 
	 * Discription:查询总数
	 * 
	 * @param sql
	 * @return
	 * @return:返回值意义
	 * @author:gaojingdan
	 * @update:2017年8月29日 gaojingdan [变更描述]
	 */
	public String getTotalCount(String sql) {
		String totalSql = "SELECT COUNT(1) FROM ( " + sql + " )";
		String total = this.getDataString(totalSql);
		return total;
	}

	/**
	 * 
	 * Discription:传入SQL，返回字符串
	 * 
	 * @param sql
	 * @return
	 * @return:返回值意义
	 * @author:gaojingdan
	 * @update:2017年8月29日 gaojingdan [变更描述]
	 */
	public String getDataString(String sql) {
		return maintainDAO.getDataString(sql);
	}
	
	/**
	 * Discription:传入SQL，返回字符串
	 * @param sql
	 * @return
	 * @return:返回值意义
	 * @author:gaojingdan
	 * @update:2017年8月29日 gaojingdan [变更描述]
	 */
	public Map<String,String> getDataMap(String sql) {
		return maintainDAO.getDataMap(sql);
	}

	/**
	 * Discription:传入SQL，执行UPDATE
	 * 
	 * @param sql
	 * @return
	 * @return:返回值意义
	 * @author:gaojingdan
	 * @update:2017年8月31日 gaojingdan [变更描述]
	 */
	public void doUpdate(String sql) {
		maintainDAO.doUpdate(sql);
	}
	/**
	 *Discription:传入SQL，执行INSERT
	 *@param sql
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月31日 gaojingdan [变更描述]
	 */
	public String doInsert(String sql,Map<String,String> paramMap){
		return UpdateUtil.doInsert(sql, paramMap);
	}
	/**
	 * 
	 *Discription:验证数据唯一性
	 *@param tableVO
	 *@param valueList
	 *@param whereClause
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月25日 gaojingdan [变更描述]
	 */
	public String doUniqueValidate(TableVO tableVO,List<JSONArray> valueList,String whereClause) {
        String result = "true";
        String checkSql = " SELECT COUNT(1) FROM " + tableVO.getDataTabName() + " WHERE ";
        Map<String,ColumnVO> uniqueColumnMap = new LinkedHashMap<String,ColumnVO>();//存取需要唯一验证的列
        List<ColumnVO> columnList = tableVO.getColumnList();
        int colOrder = 0;
        for (ColumnVO column : columnList) {
            //上传附件列、主键以及含有默认值的不考虑
            if ("file".equalsIgnoreCase(column.getColShowType()) || "1".equals(column.getIfPk())
                    || !"".equals(StringUtils.nvl(column.getDefaultValue()))) {
                continue;
            }
            //取If_Unique=1的列
            if ("1".equals(column.getIfUnique())) {
                uniqueColumnMap.put(String.valueOf(colOrder), column);
            }
            colOrder++;
        }
        try {
            //首先判断新增记录是否有重复值
            if (valueList.get(0) != null && valueList.get(0).size() > 1) {
                for (int m = 0; m < valueList.size(); m++) {
                    if (!uniqueColumnMap.containsKey(String.valueOf(m))) {
                        continue;
                    }
                    JSONArray sArray = valueList.get(m);
                    ColumnVO col = uniqueColumnMap.get(String.valueOf(m));
                    for (int i = 0; i < sArray.size(); i++) {
                        if (sArray.getString(i) == null) {
                            result = "列：【" + col.getColNameCn() + "】具有数据唯一性，不能为空!";
                            return result;
                        }
                        for (int j = i + 1; j < sArray.size(); j++) {
                            if (sArray.getString(j) == null) {
                                result = "列：【" + col.getColNameCn() + "】具有数据唯一性，不能为空!";
                                return result;
                            }
                            if (sArray.get(j).equals(sArray.get(i))) {
                                result = "第" + (i + 1) + "条记录跟第" + (j + 1) + "调记录的【" + col.getColNameCn()
                                        + "】值重复，请重新填写或者选择！";
                                return result;
                            }
                        }
                    }
                }
            }
            //比较是否与数据库中数据重复
            String checkSqlCopy = checkSql;
            for (int m = 0; m < valueList.size(); m++) {
                if (!uniqueColumnMap.containsKey(String.valueOf(m))) {
                    continue;
                }
                JSONArray sArray = valueList.get(m);
                ColumnVO col = uniqueColumnMap.get(String.valueOf(m));
                for (int i = 0; i < sArray.size(); i++) {
                	checkSql = checkSqlCopy;
                    checkSql += col.getColNameEn() + "= ?";
                    if (whereClause != null && !"".equals(whereClause)) {
                        checkSql += " AND " + whereClause;
                    }
                    String tp = URLDecoder.decode(sArray.getString(i), "UTF-8");
                    String checkResutl = this.queryCheckSql(checkSql, tp);
                    if (Integer.parseInt(checkResutl) > 0) {
                        result = "您所填入的【" + col.getColNameCn() + "】的值，数据库中已经存在，请重新填写或者选择！";
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            result = "验证数据唯一性出错，错误原因:" + e.getMessage();
            logger.error("MaintainService--doUniqueValidate:" + e.getMessage());
        }
        return result;
    }
	/**
	 * 
	 *Discription:根据表名获取缓存配置并放入生成缓存队列
	 *@param sourceTable
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2018年1月19日 gaojingdan [变更描述]
	 */
	public void doUpdateCache(String sourceTable){
		//String cacheKeyName="cache_conf_signal:0";
		if(sourceTable!=null&&!"".equals(sourceTable)){
			List<JSONObject> cacheList=maintainDAO.getCacheConfByTableName(sourceTable);
			for(JSONObject jsonObject:cacheList){
				try{
					String cacheName=jsonObject.getString("cacheName");
					if(cacheName!=null&&!"".equals(cacheName)){
						JSONObject cacheConf = cacheSynService.getCacheConfByName(cacheName);
						if(cacheConf!=null&&!cacheConf.isEmpty()){
							cacheSynService.doGenerateCache(cacheConf);
						}
						//JodisUtils.listAdd(cacheKeyName, cacheName);
					}
				}catch(Exception e){
					logger.error("MaintainService--doUpdateCache:" + e.getMessage());
				}
			}
		}
	}
	private String queryCheckSql(String checkSql,String value) {
        String checkResut = "0";
        try {
             checkResut = UpdateUtil.doQueryString(checkSql, value);
        } catch (Exception e) {
        	logger.error("MaintainService--queryCheckSql:" + e.getMessage());
        } 
        return checkResut;
    }
	public static void main(String[] args){
		String test="re|maxlength|phone";
		String regex="maxlength:\\d+";
		System.out.println(test.replaceAll(regex, "ssss"));
	}

	public void doInsertLog(TableVO tableVO, Map<String, String> oldMap, Map<String, String> newMap,String flag) {
		Map<String, String> map = new HashMap<String,String>();
		map.put("MAINTAIN_ID", tableVO.getMaintainId());
		map.put("TABLE_NAME", tableVO.getDataTabName());
		map.put("CREATE_USER",  UserUtils.getUser().getId());
		map.put("OPER_TYPE", flag);
		
		List<ColumnVO> columnList = tableVO.getColumnList();

		String OLD_VALUE = "";
		String NEW_VALUE = "";
		for (int i = 0; i < columnList.size(); i++) {
			String colName = columnList.get(i).getColNameEn();
			String newValue = "";
			if(newMap != null) {
				newValue = newMap.get(colName)==null?"": ( "null".equals(String.valueOf(newMap.get(colName)))?"":String.valueOf(newMap.get(colName)));
			}
			String oldValue = "";
			if(oldMap != null) {
				oldValue = oldMap.get(colName)==null?"": ( "null".equals(String.valueOf(oldMap.get(colName)))?"":String.valueOf(oldMap.get(colName)));
			}
			if(!oldValue.equals(newValue) && oldMap != null) {
				OLD_VALUE+=colName+":'"+oldValue.toString()+"',";
			}
			if(!oldValue.equals(newValue) && newMap != null) {
				NEW_VALUE+=colName+":'"+newValue+"',";
			}
		}
		map.put("OLD_VALUE", OLD_VALUE);
		map.put("NEW_VALUE", NEW_VALUE);
		
		maintainDAO.doInsertLog(map);
		
	}

	/**
	 * 
	 *Discription:判断主键是否修改，修改替换更新后sql
	 *@param sourceTable
	 *@return:返回值意义
	 *@author:yunwq
	 *@update:2018年9月21日 gaojingdan [变更描述]
	 */
	public String getNewSelectSql(TableVO tableVO, Map<String, String> paramMap, String selectSql) {
		String where = tableVO.getUpdateWhere().split("=")[0];
		String codeStr = paramMap.get("CODE");
		String whereStr = paramMap.get(where);
		
		if(null != codeStr && null != whereStr){
			if(!codeStr.equals(whereStr)) {
				String oleValue = paramMap.get("CODE");
				String newValue = paramMap.get(where);
				selectSql = selectSql.replace("'"+oleValue+"'", "'"+newValue+"'");
			}
		}
		
		return selectSql;
	}
}
