layui.use(["form","layer"]);
$(document).ready(function(){
	initGrid();
});
function initGrid() {
	$("#dataGrid").bootstrapTable({
		url : ctx+"/common/operLog/gridData", // 请求后台的URL（*）
		method : "get", // 请求方式（*）
		dataType : "json",
		toolbar : $("#toolBox"),
		search : false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		height:$(window).height(),
		queryParams : function(params) {
			var param = {
				offset : params.offset,
				limit : params.limit,
				startDate:$("#startDate").val(),
				endDate:$("#endDate").val(),
				colName:$("#colName").val(),
				keyword:$.trim($("#keyword").val())
			};
			return param;
		},
		onClickCell:function(field, value, row, $element){
			if(field=="remark"){
				var content = "";
				if(value){
					content = value;
				}
				layer.open({
					type:1,
					title:"备注",
					area:["600px","400px"],
					content:"<div>"+content+"</div>"
				});
			}
		},
		columns : [{
			field : "rowNum",
			title : "序号"
		} , {
			field : "module",
			title : "目录名称"
		} , {
			field : "funcName",
			title : "模块名称"
		}  , {
			field : "operType",
			title : "操作类型"
		} , {
			field : "propertyName",
			title : "字段名称"
		} , {
			field : "originalValue",
			title : "字段原值"
		} , {
			field : "newValue",
			title : "字段修改后值"
		} , {
			field : "fltNo",
			title : "航班号"
		}, {
			field : "fltDate",
			title : "航班日期"
		} , {
			field : "ioType",
			title : "进出港类型"
		}, {
			field : "operUser",
			title : "操作人"
		}, {
			field : "loginName",
			title : "操作人账号"
		} , {
			field : "officeName",
			title : "操作人部门"
		} , {
			field : "operTm",
			title : "操作时间"
		}, {
			field : "remark",
			title : "备注"
		} ]
	});
}
function query(){
	$("#dataGrid").bootstrapTable("refresh");
}