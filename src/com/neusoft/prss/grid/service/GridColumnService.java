/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.grid.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.grid.entity.GridColumn;
import com.neusoft.prss.grid.entity.GridColumnGroup;

public interface GridColumnService {
    /**
     * 
     * Discription:根据用户及模块id获取表列头.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public List<GridColumn> getColumnList(String userId,String schemaId);

    /**
     * 获取默认列
     *Discription:方法功能中文描述.
     *@param schemaId
     *@return
     *@return:返回值意义
     *@author:yuzd
     *@update:2017年12月28日yuzd [变更描述]
     */
    public String getDefColumnList(String schemaId);

    /**
     * 
     * Discription:根据用户及模块id获取表列头.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public String getColumns(String userId,String schemaId);

    /**
     * 
     * Discription:获取用户已选列.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public List<GridColumn> getSelectedList(String userId,String schemaId);

    /**
     * 
     * Discription:获取用户可选列.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public List<GridColumn> getSelectableList(String userId,String schemaId);

    /**
     * 
     * Discription:更新列信息.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public String updateColInfo(Map<String,String> params);

    /**
     * 
     * Discription:保存列头信息.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public String saveHeadInfo(List<Map<String,String>> params,String schema,String userId);

    /**
     * 
     * Discription:获取所有航班.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public JSONArray getAirlines(Map<String,Object> param);

    /**
     * 
     * Discription:获取航班总数.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public int getAirlineTotNum(Map<String,Object> param);

    /**
     * 
     * Discription:获取机型总数.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public int getActTypeTotNum(Map<String,Object> param);

    /**
     * 
     * Discription:获取机型.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public JSONArray getActTypes(Map<String,Object> param);

    /**
     * 
     * Discription:获取机场总数.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public int getAirportTotNum(Map<String,Object> param);

    /**
     * 
     * Discription:获取机场.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public JSONArray getAirports(Map<String,Object> param);

    /**
     * 
     * Discription:获取航班动态数据.
     * 
     * @param userId
     * @param schemaId
     * @return
     * @return:
     * @author:yu-zd
     * @update:2017年10月20日 yu-zd [变更描述]
     */
    public JSONArray getDynamic(String switches,String flagBGS,String reskind,String schema,String suffix,String userId,String time,
            Map<String,Object>... params);

    public JSONArray getDefColInfo(String schema);

    public JSONArray getColInfo(String userId,String schema);

    /**
     * 从oracle中获取维表
     *Discription:方法功能中文描述.
     *@param table
     *@param idcol
     *@param textcol
     *@return
     *@return:返回值意义
     *@author:yu-zd
     *@update:2017年12月7日 yu-zd [变更描述]
     */
    public JSONArray getDimFromOracle(String table,String idcol,String textcol);

    /**
     * 获取货邮行信息
     *Discription:方法功能中文描述.
     *@param infltid
     *@param outfltid
     *@return
     *@return:返回值意义
     *@author:yu-zd
     *@update:2017年12月12日 yu-zd [变更描述]
     */
    public JSONArray getCargoData(String fltid);

	public Map<String, String> getTableByCol(String col);

	public String columnUpdate(Map<String, String> param);

	public JSONArray getGuarantee();
    /**
     * 根据schemaId 获取所有的列
     * @param schemaId
     * @return
     */
    public List<GridColumnGroup> getColumnBySchema(String schemaId);

	public JSONArray getExportData(String userId);
}
