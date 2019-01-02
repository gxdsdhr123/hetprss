$(document).ready(function() {
	initNodeBtnGrid();
});
/**
 * 初始化表格
 */
function initNodeBtnGrid() {
	var gridData = $(
			'#nodeBtnGrid tbody tr:eq(' + parent.selectedRowIndex
					+ ') td:eq(5) a', parent.document).data("params");
	if (!gridData || gridData == "undefined") {
		gridData = [];
	}
	$("#variableGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
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
		field : "name",
		title : '参数名',
		width:"200px",
		editable : {
			type : 'text'
		}
	}, {
		field : "label",
		title : '中文名',
		width:"200px",
		editable : {
			type : 'text'
		}
	}, {
		field : "type",
		title : '类型',
		width:"200px",
		editable : {
			type : 'text'
		}
	}, {
		field : "value",
		title : '参数',
		width:"200px",
		editable : {
			type : 'text'
		}
	}, {
		field : "btn",
		title : '操作',
		width:"100px",
		align : 'center',
		events : operateEvents,
		formatter : function() {
			return '<a href="javascript:void(0)" class="delBtn" >删除</a>';
		}
	} ];
	return columns;
}
/**
 * 注册按钮的点击事件
 */
window.operateEvents = {
	'click .delBtn' : function(e, value, row, index) {
		$('#variableGrid').bootstrapTable('removeByUniqueId', row.id);
	}
};
function insertRow() {
	var index = $('#variableGrid').bootstrapTable('getData').length;
	var columns = getGridColumns();
	var emptyRow = {};
	for (var i = 0; i < columns.length; i++) {
		var column = columns[i];
		emptyRow[column.field] = null;
	}
	emptyRow["id"] = index;
	$('#variableGrid').bootstrapTable('insertRow', {
		index : index,
		row : emptyRow
	});
}
function reData() {
	var data = [];
	var tableDate = $('#variableGrid').bootstrapTable('getData');
	for (var i = 0; i < tableDate.length; i++) {
		var json = {
			name : tableDate[i].name || '',
			label : tableDate[i].label || '',
			type : tableDate[i].type || '',
			value : tableDate[i].value || ''
		}
		data.push(json);
	}
	return JSON.stringify(data);

}