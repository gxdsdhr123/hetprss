var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
var clickRowId = "";
$(document).ready(function() {
	
	initFdVipImportGrid();
	
	$("#importBtn").click(function() {
		var iframe = layer.open({
			type : 1,
			title : "导入计划",
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
						var sp = 0;
						layero.find("#fileInput").each(function() {
							var filePath = $(this).val();
							var fileSize = $(this)[0].files[0].size;
							var suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
							if (suffix != "xls") {
								layer.msg('不支持的文件类型！', {
									icon : 7
								});
								sp = 1;
								return;
							}
							if (fileSize > 5242880) {
								layer.msg('文件过大！', {
									icon : 7
								});
								sp = 1;
								return;
							}
						});
						if(sp == 1){
							return false;
						}
						loading = layer.load(2, {
							shade : [ 0.1, '#000' ]
						// 0.1透明度
						});
					},
					error : function(e) {
						layer.close(loading);
						layer.msg('导入失败！', {
							icon : 2,
							time : false,
							btn : ["确认"],
							yes : function(index,layero){
								layer.close(iframe);
								layer.close(index);
								$("#fdVipImportGrid").bootstrapTable('refresh');
							}
						});
					},
					success : function(data) {
						delete $("#fileInput")[0].files;
						layer.close(loading);
						if ("success" == data) {
							layer.msg('导入成功！', {
								icon : 1,
								time : false,
								btn : ["确认"],
								yes : function(index,layero){
									layer.close(iframe);
									layer.close(index);
									$("#fdVipImportGrid").bootstrapTable('refresh');
								}
							});
						} else {
							layer.msg(data, {
								icon : 2,
								time : false,
								btn : ["确认"],
								yes : function(index,layero){
									layer.close(iframe);
									layer.close(index);
									$("#fdVipImportGrid").bootstrapTable('refresh');
								}
							});
						}
					}
				});
			}
		})
	});
	
	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的行！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中内容？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				type : 'post',
				url : ctx + "/flightDynamic/delVipInfoById",
				data : {
					'id':clickRowId
				},
				dataType : "text",
				error : function() {
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(data) {
					if(data=="success"){
						layer.msg('删除成功！', {
							icon : 1,
							time : 600
						}, function() {
							clickRowId = "";
							$("#fdVipImportGrid").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#fdVipImportGrid").bootstrapTable('refresh');
						});
					}
					
				}
			});
		});
	});
	
	$("#emptiedBtn").click(function() {
		var confirm = layer.confirm('您确定要清空要客信息？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			var dataItme = $('#fdVipImportGrid').bootstrapTable('getData');
			var loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			});
			$.ajax({
				async : false,
				type : "POST",
				url : ctx + "/flightDynamic/emptiedVipInfoById",
				data : {
					dataItme : JSON.stringify(dataItme)
				},
				error : function() {
					layer.close(loading);
					layer.msg('清空失败！', {
						icon : 2
					});
				},
				success : function(data) {
					layer.close(loading);
					if (data == "success") {
						layer.msg('清空成功！', {
							icon : 1,
							time : 600
						},function(){
							$("#fdVipImportGrid").bootstrapTable('refresh');
						});
					} else {
						layer.msg('清空失败！', {
							icon : 2,
							time : 600
						});
					}
				}
			});
		});
	});
	
	$("#hisBtn").click(function() {
		var iframe = parent.layer.open({
			type : 2,
			title : "要客计划历史管理",
			area : [ '900px', '400px' ],
			content : ctx + "/flightDynamic/openVipHis",
			btn : [ "返回" ]
		});
	});
	
});
/**
 * 初始化表格
 */
function initFdVipImportGrid() {
	$("#fdVipImportGrid").bootstrapTable({
		uniqueId : "id",
		url : ctx + "/flightDynamic/getVipImportDate",
		method : "get",
		toolbar : "#toolbar",
		pagination : false,
		showRefresh : false,
		search : true,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getVipImportGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.vipid;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		}
	});
}

/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getVipImportGridColumns() {
	var columns = [ {
		field : "id",
		title : "序号",
		width : '40px',
		align : 'center',
		formatter : function(value, row, index) {
			row["id"] = index;
			return index + 1;
		}
	}, {
		field : "vipid",
		title : "序号",
		visible : false
	}, {
		field : "flightDate",
		title : "日期",
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "flightNumber",
		title : '航班号',
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "vipFlag",
		title : '标识',
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "vipInfo",
		title : '要客详情'
	}, {
		field : "status",
		title : '状态',
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "update_user",
		title : '上传人',
		width : '140px',
		align : 'center',
		sortable : true
	}, {
		field : "update_time",
		title : '时间',
		width : '150px',
		align : 'center',
		sortable : true
	} ];
	return columns;
}
