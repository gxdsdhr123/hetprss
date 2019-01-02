package com.neusoft.framework.modules.maintain.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class ColumnVO  implements Serializable{
	
	private static final long serialVersionUID = -4723245023204523257L;
	private String colId;              //维护工具列id                   
	private String maintainId;         //维护工具id                    
	private String colDesc;            //维护工具列描述                   
	private String colNameEn;          //维护工具列英文名                  
	private String colNameCn;          //维护工具列中文名
	private String colUpdateExpress;          //update 表达式
	private String colExpress;         //维护工具列表达式                  
	private String colQueryExpress;    //查询表达式                     
	private String colShowType;        //维护工具列展现类型，文本框、下拉框、日期、地市等  
	private String colDataType;        //维护工具列数据类型，数字、ip、email、url等
	private String defaultValue;       //维护工具列默认值                  
	private String ifCond;             //维护工具列是否作为页面查询条件           
	private String ifNull;             //维护工具列是否为空                 
	private String ifOrder;            //是否排序，1：升序，2：降序，0不排序       
	private String ifPk;               //是否为主键                     
	private String pkSequence;         //主键所使用的序列     
	private String colOrder;//排序
	private String ifUpdate;//是否需要Update
	private String colStyle;//列的样式
	private String insertDefautValue;//初始化值
	private String ifUnique;//列值是否唯一
	private String ifInsert;//新增时是否用到
	private String fileTypeId;//文件类型，上传文件时用到 gaojd 20150615
	private String colHeaderStyle;//表头样式 gaojd 20150730 add
	private String ifModify;
	private String colValueMaxLength;//字段最大长度
	private String colValueRegValidate;	// 正则表达式值
	
	public String getColValueMaxLength() {
		return colValueMaxLength;
	}
	public void setColValueMaxLength(String colValueMaxLength) {
		this.colValueMaxLength = colValueMaxLength;
	}
	private List<JSONObject> chooseDataList;//选择值列表
	
	public List<JSONObject> getChooseDataList() {
		return chooseDataList;
	}
	public void setChooseDataList(List<JSONObject> chooseDataList) {
		this.chooseDataList = chooseDataList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIfModify() {
		return ifModify;
	}
	public void setIfModify(String ifModify) {
		this.ifModify = ifModify;
	}
	public String getColHeaderStyle() {
		return colHeaderStyle;
	}
	public void setColHeaderStyle(String colHeaderStyle) {
		this.colHeaderStyle = colHeaderStyle;
	}
	public String getFileTypeId() {
		return fileTypeId;
	}
	public void setFileTypeId(String fileTypeId) {
		this.fileTypeId = fileTypeId;
	}
	public String getIfInsert() {
		return ifInsert;
	}
	public void setIfInsert(String ifInsert) {
		this.ifInsert = ifInsert;
	}
	public String getInsertDefautValue() {
		return insertDefautValue;
	}
	public void setInsertDefautValue(String insertDefautValue) {
		this.insertDefautValue = insertDefautValue;
	}
	public String getColStyle() {
		return colStyle;
	}
	public void setColStyle(String colStyle) {
		this.colStyle = colStyle;
	}
	public String getIfUpdate() {
		return ifUpdate;
	}
	public void setIfUpdate(String ifUpdate) {
		this.ifUpdate = ifUpdate;
	}
	public String getColOrder() {
		return colOrder;
	}
	public void setColOrder(String colOrder) {
		this.colOrder = colOrder;
	}
	public String getColUpdateExpress() {
		return colUpdateExpress;
	}
	public void setColUpdateExpress(String colUpdateExpress) {
		this.colUpdateExpress = colUpdateExpress;
	}
	public String getColId() {
		return colId;
	}
	public void setColId(String colId) {
		this.colId = colId;
	}
	public String getMaintainId() {
		return maintainId;
	}
	public void setMaintainId(String maintainId) {
		this.maintainId = maintainId;
	}
	public String getColDesc() {
		return colDesc;
	}
	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}
	public String getColNameEn() {
		return colNameEn;
	}
	public void setColNameEn(String colNameEn) {
		this.colNameEn = colNameEn;
	}
	public String getColNameCn() {
		return colNameCn;
	}
	public void setColNameCn(String colNameCn) {
		this.colNameCn = colNameCn;
	}
	public String getColExpress() {
		return colExpress;
	}
	public void setColExpress(String colExpress) {
		this.colExpress = colExpress;
	}
	public String getColQueryExpress() {
		return colQueryExpress;
	}
	public void setColQueryExpress(String colQueryExpress) {
		this.colQueryExpress = colQueryExpress;
	}
	public String getColShowType() {
		return colShowType;
	}
	public void setColShowType(String colShowType) {
		this.colShowType = colShowType;
	}
	public String getColDataType() {
		return colDataType;
	}
	public void setColDataType(String colDataType) {
		this.colDataType = colDataType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getIfCond() {
		return ifCond;
	}
	public void setIfCond(String ifCond) {
		this.ifCond = ifCond;
	}
	public String getIfNull() {
		return ifNull;
	}
	public void setIfNull(String ifNull) {
		this.ifNull = ifNull;
	}
	public String getIfOrder() {
		return ifOrder;
	}
	public void setIfOrder(String ifOrder) {
		this.ifOrder = ifOrder;
	}
	public String getIfPk() {
		return ifPk;
	}
	public void setIfPk(String ifPk) {
		this.ifPk = ifPk;
	}
	public String getPkSequence() {
		return pkSequence;
	}
	public void setPkSequence(String pkSequence) {
		this.pkSequence = pkSequence;
	}
	/**
	 * Get the counters
	 * @return String ifUnique.
	 */
	public String getIfUnique() {
		return ifUnique;
	}
	/**
	 * Set the packet counters
	 * @param ifUnique The ifUnique to set.
	 */
	public void setIfUnique(String ifUnique) {
		this.ifUnique = ifUnique;
	}
	public String getColValueRegValidate() {
		return colValueRegValidate;
	}
	public void setColValueRegValidate(String colValueRegValidate) {
		this.colValueRegValidate = colValueRegValidate;
	}
	
}
