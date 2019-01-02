/**
 * 初始化表格
 */
function initGrid(gridData) {
	$("#dataGrid").bootstrapTable({
		striped : true,
		idField : "id",
		uniqueId : "id",
		data : gridData,
		method : "get",
		pagination : false,
		showRefresh : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getGridColumns(),
	});
}
/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getGridColumns() {
	var columns = [ {
		field : "rowId",
		title : "序号",
		width : '40px',
		align : 'center',
		formatter : function(value, row, index) {
			row["id"] = index;
			return index + 1;
		}
	}, {
		field : "processName",
		title : '模板名称',
	}, {
		field : "nodeName",
		title : '节点名称'
	}, {
		field : "btn",
		title : '操作',
		width : "100px",
		align : 'center',
		formatter : function(value, row, index) {
			return '<a href="javascript:void(0)" onclick="removeRow(\''+index+'\')">删除</a>';
		}
	} ];
	return columns;
}

function insertRow(rowData) {
	var index = $('#dataGrid').bootstrapTable('getData').length;
	rowData["id"] = index;
	$('#dataGrid').bootstrapTable('insertRow', {
		index : index,
		row : rowData
	});
}

function removeRow(id){
	$('#dataGrid').bootstrapTable('removeByUniqueId',id);
}

function getLimitParmData(){
	var gridData = $('#dataGrid').bootstrapTable('getData');
	return JSON.stringify(gridData);
}