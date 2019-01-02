package com.neusoft.prss.grid.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 列分组entity
 * @author baochl
 *
 */
public class GridColumnGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//分组id
	private String name;//分组名称
	private List<GridColumn> columnList;//列集合
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<GridColumn> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<GridColumn> columnList) {
		this.columnList = columnList;
	}
}
