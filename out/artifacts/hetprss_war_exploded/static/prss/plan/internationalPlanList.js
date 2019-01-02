var clickRowId = "";// 当前选中行，以便单选后操作
layui.use(["form","layer"]);
$(document).ready(function(){
	 document.oncontextmenu = function(e) {
		 return false;
	 }
	initPlanGrid();
	//新增
	$('#addBtn').click(function() {
		var addIframe = layer.open({
			type : 2,
			title : "国际航班计划新增",
			content : ctx + "/plan/interPlan/form?operateType=add",
			btn : ['保存','新增行','删除行','关闭'],
			yes : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.save();
				$("#planGrid").bootstrapTable("refresh");
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
	
	//刷新
	$("#refresh").click(function(){
		$("#planGrid").bootstrapTable("refresh");
	});
	
	//复制
	$('#copyBtn').click(function() {
		var row = $('#planGrid').bootstrapTable('getSelections');
		if (row.length == 0) {
			layer.msg('请选择要复制的计划', {
				icon : 2
			});
		} else {
			var ids = "";
			for (var i = 0; i < row.length; i++) {
				if(i!=row.length-1){
					ids += row[i].id+",";
				}else{
					ids += row[i].id;
				}
			}
			var addIframe = layer.open({
				type : 2,
				title : "国际航班计划新增",
				content : ctx + "/plan/interPlan/form?ids="+ids+"&operateType=copy",
				btn : ['保存','新增行','删除行','关闭'],
				yes : function(index, layero) {
					$(layero).find("iframe")[0].contentWindow.save();
					$("#planGrid").bootstrapTable("refresh");
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
	});
	
	//删除
	$('#delBtn').click(function() {
		if ($("#planGrid").bootstrapTable("getSelections").length == 0) {
			layer.msg('请选择要删除的计划', {
				icon : 2
			});
		} else {
			layer.confirm("是否删除选中的计划？", {
				offset : '100px'
			}, function(index) {
				var row = $('#planGrid').bootstrapTable('getSelections');
				var ids = "";
				for (var i = 0; i < row.length; i++) {
					if(i!=row.length-1){
						ids += row[i].id+",";
					}else{
						ids += row[i].id;
					}
				}
				deletePlan(ids);
				layer.close(index);
			});
		}
	});
	
	//打印
	$("#printBtn").click(function() {
		var columns = $.map($('#planGrid').bootstrapTable('getOptions').columns[0], function(col) {
			if (col.field != "rowId" && col.field != "checkbox") {
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
});
/**
 * 国际航班列表
 */
function initPlanGrid() {
	$("#planGrid").bootstrapTable({
		url : ctx+"/plan/interPlan/getGridData", // 请求后台的URL（*）
		method : "get", // 请求方式（*）
		dataType : "json",
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : true, // 是否显示全选
		toolbar : $("#tool-box"), // 指定工具栏dom
		contextMenu : '#context-menu',//右键效果
		search : false, // 是否开启搜索功能
		editable:true,//开启编辑模式
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		paginationPreText: "上一页",  
		paginationNextText: "下一页",
		height:$(window).height(),
//		height:$("#baseTables").height(),
		onDblClickCell : onDblClickCell,
		queryParams : function(params) {
			var param = {
				pageNumber: params.offset+1,
	            pageSize: params.limit
			};
			return param;
		},
		columns : [{
			field : "checkbox",
			checkbox : true,
			sortable : false,
			editable : false
		},{
			field : "rowId",
			title : "序号",
			width : '40px',
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index) {
				return index + 1;
			}
		},{
			field : "fltNo",
			align : 'center',
			title : "航班号"
		}, {
			field : "departAptName",
			align : 'center',
			title : "起场"
		}, {
			field : "arrivalAptName",
			align : 'center',
			title : "落场"
		}, {
			field : "std",
			align : 'center',
			title : "计起"
		}, {
			field : "sta",
			align : 'center',
			title : "计落"
		}, {
			field : "otherAptName",
			align : 'center',
			title : "联程机场"
		}, {
			field : "otherStd",
			align : 'center',
			title : "联程计起"
		}, {
			field : "otherSta",
			align : 'center',
			title : "联程计落"
		}, {
			field : "actType",
			align : 'center',
			title : "机型"
		}, {
			field : "propertyName",
			align : 'center',
			title : "性质"
		} , {
			field : "fltPeriodTime",
			align : 'center',
			title : "执飞时间段"
		} , {
			field : "fltWeek",
			align : 'center',
			title : "班期"
		}, {
			field : "fltDays",
			align : 'center',
			title : "每隔天数"
		} , {
			field : "fltDates",
			align : 'center',
			title : "固定执飞日期"
		}, {
			field : "alnName",
			align : 'center',
			title : "航空公司"
		}, {
			field : "operatorDate",
			align : 'center',
			title : "操作日期",
		}, {
			field : "operatorUserName",
			align : 'center',
			title : "操作人"
		}]
		// 右键菜单
		,onContextMenuItem : function(row, $el) {
			if('' == clickRowId){
				layer.msg('请选择一条记录',{icon:0,time:600});
				return;
			}
			if ($el.data("item") == "msgView") {
				msgShow(row);
			} 
		}
		,onClickRow : function(row, tr, field) {
			clickRowId = row.msgId;
		}
	});
}

//双击事件
function onDblClickCell(field, value, row, td) {
	var ids = row.id;
	var addIframe = layer.open({
		type : 2,
		title : "国际航班计划修改",
		content : ctx + "/plan/interPlan/form?ids="+ids+"&operateType=update",
		btn : ['保存','关闭'],
		yes : function(index, layero) {
			$(layero).find("iframe")[0].contentWindow.save();
			$("#planGrid").bootstrapTable("refresh");
		}
	});
	layer.full(addIframe);
}

function msgShow(row) {
	iframe = layer.open({
		type : 2,
		title : "国际航班计划报文",
		content : ctx + "/plan/interPlan/showMsg?msgId=" + clickRowId ,
		area : [ '700px', '400px' ],
		btn : ["取消"],
		btn1 : function(index, layero) {
			layer.close(index);
		}
	});
}

function deletePlan(ids) {
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/plan/interPlan/delete",
		async : true,
		data : {
			'ids' : ids
		},
		dataType : 'text',
		success : function(msg) {
			layer.close(loading);
			if (msg == "true") {
				layer.msg('删除成功！', {
					icon : 1,
					time : 2000
				});
				$("#planGrid").bootstrapTable("refresh");
			} else {
				layer.close(loading);
				layer.msg('删除失败！', {
					icon : 2
				});
			}
		}
	});
}