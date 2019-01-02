package com.neusoft.framework.modules.maintain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.utils.StringUtils;

public class TableVO implements Serializable {
	
	private static final long serialVersionUID = 8056584251101767565L;
	
	private String maintainId;       //维护工具id      
	private String maintainName;     //维护工具名称      
	private String createUser;       //维护页面创建人     
	private String createTime;       //维护页面创建时间    
	private String maintainContent;  //维护页面详细描述    
	private String dataTabName;      //维护对应的维表名    
	private String queryTabName;     //查询所使用的表     
	private String queryTabWhere;    //查询所用的where条件
	private String updateWhere;//UPDATE OR DELETE时所使用where条件
	private String operateButton;//操作按钮，五位，每一位如果为1表示显示该按钮，顺序依次为查询、新增、删除、导入、导出
	private String gridButton;//表格中按钮，三位，每一位如果为1表示显示该按钮，顺序依次为查看、修改、删除
	private String addButton;//新增页面按钮，三位，每一位如果为1表示显示该按钮，顺序依次为保存、继续添加、返回
	private int pageNumber;//每页显示行数
	private String pageNumberList;//每页显示行数选择
	private List<ColumnVO> columnList;//所有列集合
	private List<ConditionVO> conditionList;
	private List<FunctionVO> functionList;
	private Map<String ,List<FunctionVO>> functionMap;//自定义函数Map
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getPageNumberList() {
		return pageNumberList;
	}
	public void setPageNumberList(String pageNumberList) {
		this.pageNumberList = pageNumberList;
	}
	public String getAddButton() {
		return addButton;
	}
	public void setAddButton(String addButton) {
		this.addButton = addButton;
	}
	
	public String getOperateButton() {
		return operateButton;
	}
	public void setOperateButton(String operateButton) {
		this.operateButton = operateButton;
	}
	public String getGridButton() {
		return gridButton;
	}
	public void setGridButton(String gridButton) {
		this.gridButton = gridButton;
	}
	public String getUpdateWhere() {
		return updateWhere;
	}
	public void setUpdateWhere(String updateWhere) {
		this.updateWhere = updateWhere;
	}
	public List<ConditionVO> getConditionList() {
		return conditionList;
	}
	public void setConditionList(List<ConditionVO> conditionList) {
		this.conditionList = conditionList;
	}
	public String getMaintainId() {
		return maintainId;
	}
	public void setMaintainId(String maintainId) {
		this.maintainId = maintainId;
	}
	public String getMaintainName() {
		return maintainName;
	}
	public void setMaintainName(String maintainName) {
		this.maintainName = maintainName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMaintainContent() {
		return maintainContent;
	}
	public void setMaintainContent(String maintainContent) {
		this.maintainContent = maintainContent;
	}
	public String getDataTabName() {
		return dataTabName;
	}
	public void setDataTabName(String dataTabName) {
		this.dataTabName = dataTabName;
	}
	public String getQueryTabName() {
		return queryTabName;
	}
	public void setQueryTabName(String queryTabName) {
		this.queryTabName = queryTabName;
	}
	public String getQueryTabWhere() {
		return queryTabWhere;
	}
	public void setQueryTabWhere(String queryTabWhere) {
		this.queryTabWhere = queryTabWhere;
	}
	public List<ColumnVO> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<ColumnVO> columnList) {
		this.columnList = columnList;
	}
	
	public Map<String, List<FunctionVO>> getFunctionMap() {
		for (FunctionVO vo:functionList) {
            if (!functionMap.containsKey(vo.getFunctionPosition())) {
                functionMap.put(vo.getFunctionPosition(), new ArrayList<FunctionVO>());
            }
            functionMap.get(vo.getFunctionPosition()).add(vo);
        }
		return functionMap;
	}
	
	public Map<String,List<ConditionVO>> getConditionMap(){
		Map<String,List<ConditionVO>> conditionMap=new HashMap<String,List<ConditionVO>>();//条件Map
		if(conditionList!=null&&conditionList.size()!=0){
			for(ConditionVO vo:conditionList){
				String row=Integer.toString(vo.getCondRow());
				if(!conditionMap.containsKey(row)){
					List<ConditionVO> temp=new ArrayList<ConditionVO>();
					for(int i=0;i<10;i++){
						temp.add(null);
					}
					conditionMap.put(row, temp);
				}
				conditionMap.get(row).set(vo.getCondCol()-1, vo);
			}
		}
		return conditionMap;
	}

	public ConditionVO getConditionVOByCode(String condCode){
		ConditionVO conditionVO=null;
		for(ConditionVO vo:conditionList){
			if(vo.getCondCode().equals(condCode)){
				conditionVO=vo;
				break;
			}
		}
		return conditionVO;
	}
	public ColumnVO getColumnByColumnNameEN(String enName){
		ColumnVO columnVO=null;
		for(ColumnVO vo:columnList){
			if(vo.getColNameEn().equals(enName)){
				columnVO=vo;
				break;
			}
		}
		return columnVO;
	}
	public List<ColumnVO> getSelectTypeColumnList(){
		 List<ColumnVO> selectTypeColumnList=new ArrayList<ColumnVO>();
		for(ColumnVO vo:columnList){
			if(vo.getColExpress()!=null&&!"".equals(vo.getColExpress())){
				selectTypeColumnList.add(vo);
			}
		}
		return selectTypeColumnList;
	}
	public List<FunctionVO> getFunctionList() {
		return functionList;
	}
	public void setFunctionList(List<FunctionVO> functionList) {
		this.functionList = functionList;
	}
	/***
	 * 
	 *Discription:将每页显示行数字符串转换为list.
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年8月29日 gaojingdan [变更描述]
	 */
	public String[] getPageArray(){
		String[] pageArray=StringUtils.split(pageNumberList, ',');
		return pageArray;
	}
}
