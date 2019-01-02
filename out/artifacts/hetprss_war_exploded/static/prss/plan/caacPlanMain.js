var page = 1;
var limit = 10000;
var tableData;
var baseTable;
$(function() {
	$("html,body").css("cssText","100% !important");
	var layer;
	var clickRowId = "";// 当前单选行id，以便工具栏操作
	baseTable = $("#baseTable");
	layui.use([ 'form', 'layer' ], function() {// 调用layui表单及弹出层组件
		layer = layui.layer;
	});
	$("#baseTable").each(function() {
		$(this).on('load-success.bs.table', function(thisObj) {
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody[0].removeEventListener('ps-y-reach-end', load);
			tableBody[0].addEventListener('ps-y-reach-end', load);
		});
	});
	// 基本表格选项
	var tableOptions = {
		url : ctx + "/caac/plan/init", // 请求后台的URL（*）
		method : "get", // 请求方式（*）
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : true, // 是否显示全选
		sortName : 'flt_no',
		sortOrder : "asc",
		sortStable : true,
		toolbar : $("#tool-box"), // 指定工具栏dom
		search : false, // 是否开启搜索功能
		editable:true,//开启编辑模式
		searchOnEnterKey : true,
		responseHandler : function(res) {
			tableData = res;
			return res.slice(0, limit);
		},
		customSearch : function(text) {
			var resultData = [];
			if (text) {
				var searchDate =text.trim().toUpperCase();
				if (searchDate) {
					for (var i = 0; i < tableData.length; i++) {
						var rowData = tableData[i];
						for ( var key in rowData) {
							if (rowData[key]&&rowData[key].toString().indexOf(searchDate) >-1) {
								resultData.push(rowData);
								break;
							}
						}
					}
				}else if (searchDate=="") {
					resultData=tableData;
				}
				this.data=resultData;
			}
		},
		columns : columns(true),
		onDblClickCell : onDblClickCell,// 班期
		onClickRow : onClickRow,// 单击选定行
		onEditableSave : editableSave,// 编辑结束触发
		onClickCell : function(field, value, row, $element) {
			if(field == 'PLAN_DATE_BEGIN' || field == 'PLAN_DATE_END'){
				popupDate(field, value, row, $element,'yyyyMMdd');
			} 
		}
	};
	
	function onClickRow(row, tr, field) {// 记录单选行id，并赋予天蓝色底纹
		clickRowId = row.ID;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
	
	tableOptions.height = $("body").height() ;// 表格适应页面高度
	$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
	//双击事件
	function onDblClickCell(field, value, row, td) {
		if (field == "FLT_WEEK" ) {// 班期
			createFltWeek(field, value, row, td);
		} 
	}

	// 导入
	$('#import').click(function() {
		$("#fileInput").click();
	});
	// 新增
	$('#create').click(function() {
		var addIframe = layer.open({
			type : 2,
			title : "新增",
			content : ctx + "/caac/plan/toAdd",
			btn : ['保存','新增行','删除行','关闭'],
			yes : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.save();
			},
			btn2 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.addRow();
				return false;
			},
			btn3 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.deleteRow();
				return false;
			}
		});
		layer.full(addIframe);
	});
	// 删除
	$('#remove').click(function() {
		if ($("#baseTable").bootstrapTable("getSelections").length == 0) {
			layer.msg('请选择要删除的计划', {
				icon : 2
			});
		} else {
			layer.confirm("是否删除选中的航班计划？", {
				offset : '100px'
			}, function(index) {
				var ids = $.map($('#baseTable').bootstrapTable('getSelections'), function(row) {
					return row.ID;
				});
				deletePlan(ids);
				layer.close(index);
			});
		}
	});

	// 全选
	$("#selectAll").click(function() {
		// 如果全选 取消全选
		if ($('#baseTable').bootstrapTable('getSelections').length == $('#baseTable').bootstrapTable('getData').length) {
			$('#baseTable').bootstrapTable('uncheckAll');
			$("#selectAll").text("全选");
		} else {
			$('#baseTable').bootstrapTable('checkAll');
			$("#selectAll").text("取消全选");
		}
	})
	// 打印
	$("#print").click(function() {
		var columns = $.map($('#baseTable').bootstrapTable('getOptions').columns[0], function(col) {
			if (col.field != "order" && col.field != "checkbox") {
				return {
				"field" : col.field,
				"title" : encodeURI(col.title)
				};
			} else {
				return null;
			}
		});
		$("#printTitle").text(JSON.stringify(columns));
		$("#printForm").submit();// 空表单提交
	});
})

// 删除计划
function deletePlan(ids) {
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/caac/plan/delete",
		async : true,
		data : {
			'ids' : ids.join(",")
		},
		dataType : 'text',
		success : function(msg) {
			layer.close(loading);
			if (msg == "true") {
				layer.msg('删除成功！', {
					icon : 1,
					time : 600
				});
				$('#baseTable').bootstrapTable('remove', {
					field : 'ID',
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

function load() {
	var start = page * limit;
	if (start < tableData.length) {
		var end = 0;
		if (tableData.length > (page + 1) * limit) {
			end = (page + 1) * limit;
		} else {
			end = tableData.length;
		}
		baseTable.bootstrapTable('append', tableData.slice(start, end));
		var pos = baseTable.bootstrapTable('getScrollPosition');
		baseTable.bootstrapTable('scrollTo', pos + 100);
		page++;
	}
}

/**
 * 自定义select2输入过滤
 * 
 * @param params
 * @param data
 * @returns
 */
function select2Matcher(params, data) {
	var term = $.trim(params.term);
	if (term === '') {
		return data;
	}
	var text = "";
	for ( var key in data) {
		var val = data[key];
		if (typeof val == "string") {
			text += val.toLocaleLowerCase();
		}
	}
	if (text.indexOf(term.toLocaleLowerCase()) > -1) {
		var resultData = $.extend({}, data, true);
		return resultData;
	}
	return null;
}

function refreshTable(){
	$("#baseTable").bootstrapTable('refresh');
}


function fileOnChange(){
	var loading = null;
	var obj = $("#fileInput")[0];
	var impWind = $("#fileList").ajaxSubmit({
	beforeSubmit : function() {
		if (obj.files.length <= 0) {
			layer.msg('您还没有选择要导入的文件！', {
				icon : 5
			});
			return false;
		}
		$("#fileInput").each(function() {
			var filePath = $(this).val();
			var suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
			if (suffix != "xls" && suffix != "xlsx") {
				layer.msg('不支持的文件类型！', {
					icon : 7
				});
				return false;
			}
			var fileSize = $(this)[0].files[0].size;
			if (fileSize > 5242880) {
				layer.msg('文件过大！', {
					icon : 7
				});
				return false;
			}
		});
		loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		// 0.1透明度
		});
	},
	error : function(e) {
		layer.close(loading);
		layer.msg('导入失败！', {
			icon : 2
		});
	},
	success : function(data) {
		delete $("#fileInput")[0].files;
		$("#fileTable").empty();
		layer.close(loading);
		if ("success" == data) {
			layer.msg('呼和相关计划导入成功！', {
			icon : 1,
			time : 600
			}, function() {
				refreshTable();
			});
		} else  {
			layer.msg('导入失败！', {
				icon : 2
			});
		}
	}
	});
}