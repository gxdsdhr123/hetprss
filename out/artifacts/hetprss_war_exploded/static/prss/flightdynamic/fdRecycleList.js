var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
var filter;// 用于筛选
var iframe;
$(function() {
	filter = {};
	var fltProperties, airports, airlines;
	layui.use([ 'form', 'layer' ], function() {
		layer = layui.layer;
	});
	baseTable = $("#baseTable");
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	getOptions();
	$("#context-menu li").hover(function() {
		$("#context-menu").find("ul").hide();
		$(this).find("ul").show();
	})
	// 刷新
	$("#refresh").click(function() {
		$('#baseTable').bootstrapTable('refresh');
	})

	// 查看
	$('#show').click(
			function() {
				if (clickRowId == "") {
					layer.msg('请选择一个计划', {
						icon : 2
					});
					return false;
				}
				iframe = layer.open({
					type : 2,
					title : false,
					closeBtn : false,
					content : ctx + "/flightDynamic/getFdById?type=show&id="
							+ clickRowId,// type用来区分页面来源，控制功能权限
					btn : [ "返回" ],
					success : function(layero, index) {
					}
				});

				layer.full(iframe);// 展开弹出层直接全屏显示});
			})
	// 全选
	$("#selectAll")
			.click(
					function() {
						// 如果全选 取消全选
						if ($('#baseTable').bootstrapTable('getSelections').length == $(
								'#baseTable').bootstrapTable('getData').length) {
							$('#baseTable').bootstrapTable('uncheckAll');
							$("#selectAll").text("全选");
						} else {
							$('#baseTable').bootstrapTable('checkAll');
							$("#selectAll").text("取消全选");
						}
					})

	// 删除
	$('#remove').click(
			function() {
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.confirm("是否删除选中的航班计划？", {
						offset : '100px'
					}, function(index) {
						var ids = $.map($('#baseTable').bootstrapTable(
								'getSelections'), function(row) {
							return row.id;
						});
						deleteRecycle(ids);
						layer.close(index);
					});
				});
			})

	// 还原 recoveryRecycle
	$("#recovery").click(
			function() {
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.confirm("是否还原选中的航班计划？", {
						offset : '100px'
					}, function(index) {
						var ids = $.map($('#baseTable').bootstrapTable(
								'getSelections'), function(row) {
							return row.id;
						});
						recoveryRecycle(ids);
						layer.close(index);
					});
				});
			})

	// 清空回收站
	$('#clear').click(function() {
		layui.use('layer', function() {
			var layer = layui.layer;
			layer.confirm("请确认是否要清空回收站！", {
				offset : '100px'
			}, function(index) {
				clearRecycle();
				layer.close(index);
			});
		});
	})
})

// 表格列选项默认设置
jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
jQuery.fn.bootstrapTable.columnDefaults.align = "center";
var tableOptions = {
	url : ctx + "/fdRecycle/getRecycleList", // 请求后台的URL（*）
	method : "get", // 请求方式（*）
	dataType : "json", // 返回结果格式
	striped : true, // 是否显示行间隔色
	pagination : false, // 是否显示分页（*）
	cache : true, // 是否启用缓存
	undefinedText : '', // undefined时显示文本
	checkboxHeader : false, // 是否显示全选
	toolbar : $("#tool-box"), // 指定工具栏dom
	search : true, // 是否开启搜索功能
	columns : getBaseColumns(),
	contextMenu : '#context-menu',
	queryParams : function(params) {
		var temp = {
			schema : "1"
		}
		return temp;
	},
	onContextMenuItem : function(row, $el) {
		if ($el.data("item") == "edit") {
			alert("Edit: " + row.inFltno);
		} else if ($el.data("item") == "delete") {
			alert("Delete: " + row);
		} else if ($el.data("item") == "action1") {
			alert("Action1: " + row);
		} else if ($el.data("item") == "action2") {
			alert("Action2: " + row);
		}
	},
	onContextMenuRow : function() {
		$("#context-menu ul").hide();
	},
	onClickRow : function(row, tr, field) {
		clickRowId = row.id;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
};
// 获取基本表格列头
function getBaseColumns() {
	var columns = [];
	$.ajax({
		type : 'post',
		url : ctx + "/flightDynamic/getDefaultColumns",
		async : false,
		data : {
			'schema' : "1"
		},
		dataType : 'json',
		success : function(column) {
			var order = [ {
				field : 'order',
				title : '序号',
				sortable : false,
				editable : false,
				formatter : function(value, row, index) {
					return index + 1;
				}
			}, {
				field : "checkbox",
				checkbox : true,
				sortable : false,
				editable : false
			} ];
			columns = order.concat(column);
		}
	});
	return columns;
}
// 获取下拉选项
function getOptions() {
	$(".select2").each(function() {
		var type = $(this).data("type");
		if (type == "terminal" || type == "apron") {
			$(this).select2({
				placeholder : '请选择'
			});
		} else {
			$(this).select2(select2Options(type));
		}
	});
}
// select2设置
function select2Options(type) {
	var temp = {
		ajax : {
			url : ctx + '/flightDynamic/getOptions',
			type : 'post',
			dataType : 'json',
			delay : 250,
			data : function(params) {
				params.limit = 30;
				return {
					q : params.term,
					page : params.page,
					limit : params.limit,
					type : type
				};
			},
			processResults : function(data, params) {
				params.page = params.page || 1;
				return {
					results : data.item,
					pagination : {
						more : (params.page * params.limit) < data.count
					}
				};
			},
			cache : true
		},
		placeholder : '请选择',
		escapeMarkup : function(markup) {
			return markup;
		},
		language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection
	}
	return temp;
}
// select2返回值处理
function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}

function deleteRecycle(ids) {
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/flightDynamic/fdRecycle/delFdRecycle",
		async : true,
		data : {
			'ids' : ids
		},
		dataType : 'text',
		success : function(msg) {
			layer.close(loading);
			if (msg == "success") {
				layer.msg('删除成功！', {
					icon : 1,
					time : 600
				});
				$('#baseTable').bootstrapTable('remove', {
					field : 'id',
					values : ids
				});
			} else {
				layer.close(loading);
				layer.msg('删除失败！', {
					icon : 2
				});
			}
		}
	});
}
function customMatcher(params, data) {
	if ($.trim(params.term) === '') {
		return data;
	}
	for ( var i in data) {
		var search = JSON.stringify(data[i]).toLowerCase();
		var term = params.term.toLowerCase();
		if (i != "id" && i != "disabled" && i != "selected" && i != "element") {
			if (search.indexOf(term) > -1) {
				var modifiedData = $.extend({}, data, true);
				modifiedData.text += ' (' + search + ')';
				return modifiedData;
			}
		}
	}

	return null;
}
function clearRecycle() {
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/plan/recycleBin/clearFdRecycle",
		async : true,
		dataType : 'text',
		success : function(msg) {
			layer.close(loading);
			if (msg == "success") {
				layer.msg('删除成功！', {
					icon : 1,
					time : 600
				});
				$('#baseTable').bootstrapTable('removeAll');
			} else {
				layer.close(loading);
				layer.msg('删除失败！', {
					icon : 2
				});
			}
		}
	});
}
function recoveryRecycle(ids) {
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/fdRecycle/recoveryFdRecycle",
		async : true,
		data : {
			'ids' : ids
		},
		dataType : 'text',
		success : function(msg) {
			layer.close(loading);
			if (msg == "success") {
				layer.msg('还原成功！', {
					icon : 1,
					time : 600
				});
				$('#baseTable').bootstrapTable('remove', {
					field : 'id',
					values : ids
				});
			} else {
				layer.close(loading);
				layer.msg('还原失败！', {
					icon : 2
				});
			}
		}
	});
}