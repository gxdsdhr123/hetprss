var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	
	initFdVipGrid();
	
	$("#addVipInfoBtn").click(function() {
		insertRow();
	});
	
	if(!$("#editBtn").length > 0 && !$("#scheEditBtn").length > 0){
		$("#toolbar").hide();
	}
	
});
/**
 * 初始化表格
 */
function initFdVipGrid() {
	var vipurl="/flightDynamic/getVipDate";
	if ($("#hisFlag").val()!="") {
		vipurl="/fdHistorical/getVipDate";
	}
	
	$("#fdVipInfoGrid").bootstrapTable({
		uniqueId : "id",
		url : ctx + vipurl,
		method : "get",
		toolbar : "#toolbar",
		pagination : false,
		showRefresh : false,
		search : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getVipInfoGridColumns(),
		queryParams : function() {
			var param = {
					inFltid : $("#inFltid").val(),
					outFltid : $("#outFltid").val(),
			};
			return param;
		},
		onEditableSave : function(field, row, oldValue, $el) {
			if ("ioFlag" == field) {
				var rowIndex = $el.parent().parent().index();
				$("#fdVipInfoGrid").bootstrapTable("updateCell", {
					index : rowIndex,
					field : "flightNumber",
					value : row.ioFlag=='A'?$("#inFltNo").val():$("#outFltNo").val()
				});
			}
		}
	});
}

/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getVipInfoGridColumns() {
	var columns = [];
	if ($("#editBtn").length > 0 || $("#scheEditBtn").length > 0){ 
		columns = [ {
			field : "id",
			title : "序号",
			width : '40px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				row["id"] = index;
				return index + 1;
			}
		}, {
			field : "ioFlag",
			title : "进出港",
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : {
				type : "select",
				onblur : "ignore",
				source : ioFlag
			}	
		}, {
			field : "flightNumber",
			title : '航班号',
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : false
		}, {
			field : "vipFlag",
			title : '要客标识',
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : {
				type : "select",
				onblur : "ignore",
				source : vipFlag
			}
		}, {
			field : "vipInfo",
			title : '要客详情',
			valign : "middle",
			formatter : function(value, row, index) {
				return value?value:'';
			},
			editable : {
				type : 'text'
			}
		}, {
			field : "btn",
			title : '操作',
			width : '50px',
			align : 'center',
			valign : "middle",
			events : operateEvents,
			formatter : function() {
				return '<button type="button" class="delBtn" >删除</button>';
			}
		} ];
	} else {
		columns = [ {
			field : "id",
			title : "序号",
			width : '40px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				row["id"] = index;
				return index + 1;
			}
		}, {
			field : "ioFlag",
			title : "进出港",
			width : '100px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				return ioFlag[value];
			}
		}, {
			field : "flightNumber",
			title : '航班号',
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : false
		}, {
			field : "vipFlag",
			title : '要客标识',
			width : '100px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				return value?vipFlag[value]:'';
			}
		}, {
			field : "vipInfo",
			title : '要客详情',
			valign : "middle",
			formatter : function(value, row, index) {
				return value?value:'';
			}
		}, {
			field : "vipId",
			visible : false
		} ];
	}
	return columns;
}
/**
 * 注册按钮的点击事件
 */
window.operateEvents = {
	'click .delBtn' : function(e, value, row, index) {
		$('#fdVipInfoGrid').bootstrapTable('removeByUniqueId', row.id);
	}
};
function insertRow() {
	var index = $('#fdVipInfoGrid').bootstrapTable('getData').length;
	var columns = getVipInfoGridColumns();
	var emptyRow = {};
	for (var i = 0; i < columns.length; i++) {
		var column = columns[i];
		emptyRow[column.field] = null;
	}
	emptyRow["id"] = index;
	$('#fdVipInfoGrid').bootstrapTable('insertRow', {
		index : index,
		row : emptyRow
	});
}
function saveVipInfo() {
	if ($("#editBtn").length > 0 || $("#scheEditBtn").length > 0){
		var dataItme = $('#fdVipInfoGrid').bootstrapTable('getData');
		var result = [];
		for (var i = 0; i < dataItme.length; i++) {
			var vipInfo = dataItme[i];
			var isEmptyRow = true;
			for ( var field in vipInfo) {
				if (field !== "id" && vipInfo[field]) {
					isEmptyRow = false;
					break;
				}
			}
			if (!isEmptyRow) {
				result.push(vipInfo);
			}
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			async : false,
			type : "POST",
			url : ctx + "/flightDynamic/saveVipInfo",
			data : {
				inFltId : $("#inFltid").val(),
				outFltId : $("#outFltid").val(),
				vipResult : JSON.stringify(result)
			},
			error : function() {
				layer.close(loading);
				layer.msg('保存失败！', {
					icon : 2
				});
			},
			success : function(data) {
				layer.close(loading);
				if (data == "success") {
					layer.msg('保存成功！', {
						icon : 1,
						time : 600
					},function(){
						parent.saveSuccess();
					});
				} else {
					layer.msg('保存失败！', {
						icon : 2,
						time : 600
					});
				}
			}
		});
	} else {
		parent.saveSuccess();
	}
}