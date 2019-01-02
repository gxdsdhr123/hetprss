$(function() {
	layui.use([ 'form', 'layer' ], function() {
		layer = layui.layer;
	});
	(function getOptions() {
		$.ajax({
			type : 'post',
			url : ctx + "/plan/longTerm/getOptions",
			async : false,
			dataType : 'json',
			success : function(data) {
				fltProperties = data["fltProperties"];
				airports = data["airports"];
				airlines = data["airlines"];
				terminals = data["terminals"];
			}
		});
	})()
	$("#importTable").bootstrapTable({
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true,
		undefinedText : '',
		uniqueId : 'id',
		checkboxHeader : false,
		toolbar : $("#importToolBox"),
		height : $("#longTermPlanImportTables").height(),
		columns : [ {
			field : "order",
			title : "序号",
			sortable : false,
			editable : false,
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "date",
			title : "日期",
			editable : false
		}, {
			field : "fltNo",
			title : "航班号",
			editable : false
		}, {
			field : "state",
			title : "状态",
			editable : false
		} ],
		onClickRow : onClickRow
	});
});
var layer, clickRowId, iframe, fltProperties, airports, airlines, terminals;
var $table = $('#importTable');
// 刷新
$('#refresh').click(function() {
	$table.bootstrapTable('refresh');
});
// 导入计划
$('#importPlan').click(function() {
	layer.open({
		type : 1,
		title : "导入计划",
		offset : '100px',
		area : [ "400px", "200px" ],
		content : $("#fileList"),
		btn : '确定',
		yes : function(index, layero) {
			var loading = null;
			var impWind = $("#fileList").ajaxSubmit({
				beforeSubmit : function() {
					if (layero.find("#fileInput").val() == "") {
						layer.msg('您还没有选择要导入的文件！', {
							icon : 5
						});
						return false;
					}
					if (layero.find("#timeinput").val() == "") {
						layer.msg('您还没有选择日期！', {
							icon : 5
						});
						return false;
					}
					layero.find("#fileInput").each(function() {
						var filePath = $(this).val();
						var suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
						if (suffix != "xlsx" && suffix != "xls") {
							layer.msg('不支持的文件类型！', {
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
					if ("error" == data) {
						layer.msg('导入失败！', {
							icon : 2
						});
					} else {
						layer.msg('导入成功！', {
							icon : 1,
							time : 600
						}, function() {
							$table.bootstrapTable("load",data);
						});
					}
				}
			});
		}
	})
});

// 删除
$('#remove').click(function() {
	if ($table.bootstrapTable("getSelections").length == 0) {
		layer.msg('请选择要删除的计划', {
			icon : 2
		});
	} else {
		layer.confirm("是否删除选中的航班计划？", {
			offset : '100px'
		}, function(index) {
			var ids = $.map($table.bootstrapTable('getSelections'), function(row) {
				return row.id;
			});
			var loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			// 0.1透明度
			});
			$.ajax({
				type : 'post',
				url : ctx + "/plan/longTerm/removeImportPlan",
				data : {
					"ids" : ids
				},
				error : function() {
					layer.close(loading);
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(msg) {
					layer.close(loading);
					if (msg == "success") {
						layer.msg('删除成功！', {
							icon : 1
						});
						$table.bootstrapTable('remove', {
							field : 'id',
							values : ids
						});
					} else if (msg == "error") {
						layer.msg('删除失败！', {
							icon : 2
						});
					}
				}
			});
			layer.close(index);
		});
	}
});

// 确定同步
$('#synchro').click(function() {
	layer.confirm("请确认是否要同步要客计划！", {
		offset : '100px'
	}, function(index) {
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		// 0.1透明度
		});
		if ($table.bootstrapTable('getData').length == 0) {
			layer.close(loading);
			layer.msg('暂无计划可同步，请先导入计划', {
				icon : 2,
				time : 600
			});
			return false;
		}
		$.ajax({
			type : 'post',
			url : ctx + "/flightDynamic/synchroVip",
			error : function() {
				layer.close(loading);
				layer.msg('转入失败！', {
					icon : 2,
					time : 600
				});
			},
			success : function(msg) {
				layer.close(loading);
				if (msg == "success") {
					layer.msg('转入成功！', {
						icon : 1,
						time : 600
					}, function() {
						parent.formSubmitCallback();// 成功后调用主页面，关闭弹出层并刷新表格
					});
				} else {
					layer.msg('转入失败！', {
						icon : 2,
						time : 600
					});
				}
			}
		});
	});
});

/**
 * 弹出日期控件
 * 
 * @param field
 * @param value
 * @param row
 * @param td
 */
function popupDate() {
	var top = $("#timeinput").offset().top + $("#timeinput").parent().height() + "px";
	var left = $("#timeinput").offset().left + "px";
	layer.open({
		offset : [ top, left ],
		closeBtn : false,
		type : 1,
		title : false,
		shadeClose : true,
		shade : [ 0.1, '#fff' ],
		area : [ "auto", "auto" ],
		success : function(layero, index) {
			WdatePicker({
				startDate : '%y-%M-%d',
				dateFmt : "yyyy-MM-dd",
				eCont : "timerpicker",
				onpicked : function(dp) {
					var newValue = dp.cal.getDateStr();
					$("#timeinput").val(newValue);
					layer.close(index);
				}
			})
		},
		content : "<div id='" + "timerpicker'></div>"
	});
}

/**
 * 表单提交回调
 */
function formSubmitCallback() {
	layer.close(iframe);
	$table.bootstrapTable("refresh");
}
