layui.use(["form","layer"]);
$(document).ready(function() {
	initArrivalGrid();
	initDepartGrid();
});
/** 
 * 初始化进港表格
 */
function initArrivalGrid() {
	$("#arrivalGrid").bootstrapTable({
		url : ctx + "/plan/travelskyPlan/getData",	
		queryParams : function(params) {
			var param = {
				offset : params.offset,
				limit : params.limit,
				start:$("#startDate").val(),
				end:$("#endDate").val(),
				ioType:"A"
			};
			return param;
		},
		method : "get",
		search : false,
		maintainSelected:true,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		dataType : "json",
		height : $(window).height(),
		onDblClickRow:function(row,tr,field){
			planForm(row.FLT_DATE,row.FLT_NO);
		},
		columns : [{
			field : "checkbox",
			checkbox : true,
			sortable : false,
			editable : false
		},    {
			field : "rowId",
			title : "序号",
			width : '40px',
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "FLT_NO",
			title : '进港航班',
			align : 'center'
		}, {
			field : "SHARE_FLT_NO",
			title : '共享航班号',
			align : 'center'
		}, {
			field : "DEPART_APT",
			title : '起场',
			align : 'center'
		}, {
			field : "STD",
			title : '计起',
			align : 'center'
		}, {
			field : "STA",
			align : 'center',
			title : '计落'
		}, {
			field : "ACT_TYPE",
			align : 'center',
			title : '机型'
		}, {
			field : "FLT_DATE",
			align : 'center',
			title : '日期'
		}]
	});
	
}
// 初始化出港表格
function initDepartGrid() {
	$("#departureGrid").bootstrapTable({
		url : ctx + "/plan/travelskyPlan/getData",
		queryParams : function(params) {
			var param = {
				offset : params.offset,
				limit : params.limit,
				start:$("#startDate").val(),
				end:$("#endDate").val(),
				ioType:"D"
			};
			return param;
		},
		method : "get",	
		search : false,
		maintainSelected:true,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		dataType : "json",
		height : $(window).height(),
		onDblClickRow:function(row,tr,field){
			planForm(row.FLT_DATE,row.FLT_NO);
		},
		columns : [ {
			field : "checkbox",
			checkbox : true,
			sortable : false,
			title : "",
			visible : true,
			editable : false
		}, {
			field : "rowId",
			title : "序号",
			width : '40px',
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "FLT_NO",
			title : '出港航班',
			align : 'center'
		}, {
			field : "SHARE_FLT_NO",
			title : '共享航班号',
			align : 'center'
		}, {
			field : "ARRIVAL_APT",
			title : '落场',
			align : 'center'
		}, {
			field : "STD",
			title : '计起',
			align : 'center'
		}, {
			field : "STA",
			align : 'center',
			title : '计落'
		}, {
			field : "ACT_TYPE",
			align : 'center',
			title : '机型'
		}, {
			field : "FLT_DATE",
			align : 'center',
			title : '日期'
		}]
	});
}
// 日期选择
function dateFilter() {
	WdatePicker({
//	maxDate:'%y-%M-%d',
	dateFmt : 'yyyyMMdd'
	})
}
//全选
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


//导入计划
function importFile(){
	$("#fileTable").show();
	layer.open({
	type : 1,
	title : "导入计划",
	area : [ "800px", "400px" ],
	content : $("#fileList"),
	btn : [ "选择文件", "导入" ],
	yes : function(index, layero) {
		$("#fileInput").click();
	},
	btn2 : function(index, layero) {
		var loading = null;
		var impWind = $("#fileList").ajaxSubmit({
		data: {
			type: 'A'
        },
		beforeSubmit : function() {
			if (layero.find("input:first").val() == "") {
				layer.msg('您还没有选择要导入的文件！', {
					icon : 5
				});
				return false;
			}
			layero.find("#fileInput").each(function() {
				var filePath = $(this).val();
				var suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
				if (suffix != "txt" && suffix != "txt") {
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
			if ("success" == data) {
				layer.msg('导入成功！', {
				icon : 1,
				time : 600
				}, function() {
					$('#arrivalGrid').bootstrapTable('refresh');
					$('#departureGrid').bootstrapTable('refresh');
				});
			} else  {
				layer.msg('导入模板不正确！', {
					icon : 2
				});
			}
		}
		});
	}
	})
	
}
function fileOnChange() {
//	$("#fileTable").html("");
	var obj = $("#fileInput")[0];
	for (var i = 0; i < obj.files.length; i++) {
		var file = obj.files[i];
		var tr = $([ '<tr id="upload-' + i + '">', '<td>' + file.name + '</td>', '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>', '</tr>' ].join(''));
		$("#fileTable").append(tr);

	}
}
function removeTr(obj) {
	var ele = $(obj);
	var delVal = ele.parent().parent().find("td").eq(0).text();
	var fileInput = $("#fileInput")[0];
	for (var i = 0; i < fileInput.files.length; i++) {
		var file = fileInput.files[i];
		if (file.name == delVal) {
			allFiles.files.splice(i, 1);
		}
	}
	$("#fileInput")[0] = allFiles;
	ele.parent().parent().remove();
}
//修改
/**
 * 计划编辑页面
 * @param fltData
 * @param fltNo
 */
function planForm(fltDate,fltNo){
	title = "计划修改";
	isNew = false;
	layer.open({
		type : 2,
		title : title,
		area : [ "100%", "100%" ],
		content : [ ctx + "/plan/travelskyPlan/form?fltDate="+fltDate+"&fltNo="+fltNo+"&isNew="+isNew, "no" ],
		btn : ['保存','新增行','删除行','关闭'],
		btn1 : function(index, layero) {
			//保存
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var data = iframeWin.getGridData();
			if(data){
				var success = edit(isNew,data);
				if(success){
					layer.msg("保存成功！", {icon: 1},function(){
						layer.close(index);
						$("#arrivalGrid").bootstrapTable("refresh");
						$("#departureGrid").bootstrapTable("refresh");
					});
				} else {
					return false;
				}
			} else {
				return false;
			}
		},
		btn2 : function(index, layero) {
			//新增行
			$(layero).find("iframe")[0].contentWindow.addRow();
			return false;
		},
		btn3 : function(index, layero) {			
			//删除行
			$(layero).find("iframe")[0].contentWindow.deleteRow();
			return false;
		}
	});
}
/**
 * 保存
 * @returns
 */
function edit(isNew,data){
	var succeed = false;
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		url : ctx + "/plan/travelskyPlan/edit",
		async : false,
		data : {
			isNew:isNew,
			newRows:JSON.stringify(data.newRows),
			editRows:JSON.stringify(data.editRows),
			removeRows:JSON.stringify(data.removeRows)
		},
		success : function(result) {
			layer.close(loading);
			if(result=="success"){
				succeed = true;
			} else {
				layer.alert("保存失败！"+result, {icon: 2});
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			layer.close(loading)
			var result = "保存失败！";
			layer.alert(result, {icon: 2});
		}
	});
	return succeed;
}
//新增
function arrvialAdd(){
	var addIframe = layer.open({
		type : 2,
		title : "中航信计划新增",
		content : ctx + "/plan/travelskyPlan/form",
		btn : ['保存','新增行','删除行','关闭'],
		yes : function(index, layero) {
			$(layero).find("iframe")[0].contentWindow.save();
			$("#arrivalGrid").bootstrapTable("refresh");
			$("#departureGrid").bootstrapTable("refresh");
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
}
//删除
$('#remove').click(function() {
	if ($("#arrivalGrid").bootstrapTable("getSelections").length == 0&&$("#departureGrid").bootstrapTable("getSelections").length == 0) {
		layer.msg('请选择要删除的计划', {
			icon : 2
		});
	} else {
		layer.confirm("是否删除选中的计划？", {
			offset : '100px'
		}, function(index) {
			var ids;
			ids = $.map($('#arrivalGrid').bootstrapTable('getSelections'), function(row) {
				return row.ID;
			});
			var _ids ;
			_ids = $.map($('#departureGrid').bootstrapTable('getSelections'), function(row) {
				return row.ID;
			});
			deletePlan(ids,_ids);
			layer.close(index);
		});
	}
});
//删除计划
function deletePlan(ids,_ids) {
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/plan/travelskyPlan/delete",
		async : true,
		data : {
			'ids' : ids.join(","),
			'_ids':_ids.join(",")
		},
		dataType : 'text',
		success : function(msg) {
			layer.close(loading);
			if (msg == "true") {
				layer.msg('删除成功！', {
					icon : 1,
					time : 600
				});
				$('#arrivalGrid').bootstrapTable('remove', {
					field : 'ID',
					values : ids
				});
				$('#departureGrid').bootstrapTable('remove', {
					field : 'ID',
					values : _ids
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
$("#print").click(function() {
	$("#printForm").submit();// 空表单提交
});
$("#search").click(function() {
	var start = $("#startDate").val();
	var end = $("#endDate").val();
	// 查询条件存入打印form隐藏域
	$("#sDate").val(start);
	$("#eDate").val(end);
	$("#arrivalGrid").bootstrapTable("refresh");
	$("#departureGrid").bootstrapTable("refresh");
});