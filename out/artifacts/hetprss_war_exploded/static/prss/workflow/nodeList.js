var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	$("select").select2();
	initNodeGrid();

	// 新增
	$("#addBtn").click(function() {
		openEditWin(null, "add");
	});
	// 修改
	$("#editBtn").click(function() {
		var id = getSelected();
		if (!id) {
			layer.msg("请选择要修改的行！", {
				icon : 7
			});
			return false;
		}
		openEditWin(id, "edit");
	});
	// 删除
	$("#delBtn").click(function() {
		var id = getSelected();
		if (!id) {
			layer.msg("请选择要删除的行！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中节点？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				type : 'post',
				url : ctx + "/workflow/node/delNode",
				data : {
					'id' : id,
					jobKind:$("input[name='idRadio']:checked").data("jobkind"),
					jobType:$("input[name='idRadio']:checked").data("jobtype")
				},
				dataType : "text",
				error : function() {
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(data) {
					if (data == "success") {
						layer.msg('删除成功！', {
							icon : 1,
							time : 600
						}, function() {
							$("#nodeGrid").bootstrapTable('refresh');
						});
					} else {
						layer.msg('删除失败！'+data, {
							icon : 2
						}, function() {
							//$("#nodeGrid").bootstrapTable('refresh');
						});
					}

				}
			});
		});
	});
})
/**
 * 初始化表格
 */
function initNodeGrid() {
	$("#nodeGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "id",
		url : ctx + "/workflow/node/getNodeData",
		queryParams : function() {
			var param = {
				jobType : $("#jobType").val(),
				jobKind : $("#jobKind").val()
			};
			return param;
		},
		method : "get",
		pagination : false,
		showRefresh : false,
		search : true,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getGridColumns(),
		onClickRow : function onClickRow(row, tr, field) {
			tr.find("input[name='idRadio']:eq(0)").prop("checked", true);
		}
	});
}
/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getGridColumns() {
	var columns = [
			{
				field : "id",
				title : "序号",
				width : '50px',
				align : 'center',
				formatter : function(value, row, index) {
					return "<input type='radio' data-jobkind='"+row.jobKindId+"' data-jobtype='"+row.jobTypeId+"' name='idRadio' value='" + value
							+ "'>&nbsp;&nbsp;" + (index + 1);
				}
			}, {
				field : "jobKind",
				title : '保障类型',
				sortable : true
			}, {
				field : "jobType",
				title : '作业类型',
				sortable : true
			}, {
				field : "name",
				title : '名称',
				sortable : true
			}, {
				field : "label",
				title : '中文名',
				sortable : true
			} ];
	return columns;
}
// 编辑窗口
var editWin = null;
function openEditWin(id, type) {
	editWin = layer.open({
		closeBtn : false,
		type : 2,
		title : false,
		area : [ '100%', '100%' ],
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveNodeGrid();
			return false;
		},
		content : [ ctx + "/workflow/node/nodeForm?id=" + (id ? id : "") ]
	});
}

function saveSuccess() {
	layer.close(editWin);
	$("#nodeGrid").bootstrapTable('refresh');
}
function getSelected() {
	return $("input[name='idRadio']:checked").val();
}
var loading = null;
function changKind() {
	loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	queryJobType($("#jobKind").val());
	query();
}
function query() {
	if (loading) {
		layer.close(loading);
	}
	$.ajax({
		type : 'POST',
		url : ctx + "/workflow/node/getNodeData",
		data : {
			jobType : $("#jobType").val(),
			jobKind : $("#jobKind").val()
		},
		dataType : "json",
		async : false,
		success : function(data) {
			$("#nodeGrid").bootstrapTable("load", data);
		}
	});
}

function queryJobType(data) {
	$("#jobType").empty();
	$.ajax({
		type : 'POST',
		url : ctx + "/workflow/node/getTypeByKind",
		data : {
			jobKind : data
		},
		dataType : "json",
		async : false,
		success : function(data) {
			$("#jobType").append(
					"<option value=''>" + "请选择作业类型" + "</option>");
			for (var i = 0; i < data.length; i++) {
				$("#jobType").append(
						"<option value='" + data[i].RESTYPE + "'>"
								+ data[i].TYPENAME + "</option>");
			}
		}
	});
}