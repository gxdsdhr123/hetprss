package com.neusoft.prss.grid.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 航班动态及调度动态表头设置entity
 * 
 * @author baochl
 *
 */
public class GridColumn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String colName;// 列英文名称
	private String colDesc;// 描述
	private String title;// 中文名称，显示在页面表头
	private String field;// 别名，对应Grid column field
	private boolean sortable;// 是否可排序
	private String formate;
	private String editable;
	private String width;
	private boolean visible;// 是否隐藏列
	@JSONField(name="class")
	private String className;
	@JsonIgnore
	private String cnname;//中文名称
	@JsonIgnore
	private String customCnname;//自定义显示名称
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public GridColumn(){
		this.visible = true;
		this.sortable = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	@JsonIgnore
	public String getColDesc() {
		return colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String getFormate() {
		return formate;
	}

	public void setFormate(String formate) {
		this.formate = formate;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getCustomCnname() {
		return customCnname;
	}

	public void setCustomCnname(String customCnname) {
		this.customCnname = customCnname;
	}

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}
}
