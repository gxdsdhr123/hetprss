layui.use([ "form", "layer" ]);
$(document).ready(function() {
	initGrid();
});

function initGrid() {
	$("#dataGrid").bootstrapTable({
//		url : ctx+"/plan/specialPlan/gridData",
		url : ctx+"/plan/specialPlan/gridData",
		method : "get", 
		dataType : "json",
		toolbar : $("#toolBox"),
		search : false,
		editable:false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		paginationPreText: "上一页",  
		paginationNextText: "下一页",
//		height:$(window).height(),
		queryParams : function(params) {
			var param = {
				offset : params.offset,
				limit : params.limit,
				acceptState : "0" //公务通航计划接收状态 0未接收 1已接收
			};
			return param;
		},
		columns : [ {
			checkbox : true, // 显示一个勾选框
			align : 'center'
		}, {
			field : "rowId",
			title : "序号",
			width : '40px',
			align : 'center',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "fltDate",
			title : "执行日期",
			width : "60px"
		}, {
			field : "fltNo",
			title : "航班号",
			width : "80px"
		}, {
			field : "aircraftNumber",
			title : "机号",
			width : "80px"
		}, {
			field : "actType",
			title : "机型",
			width : "80px"
		}, {
			field : "departApt",
			title : "起场",
			width : "80px"
		}, {
			field : "arrivalApt",
			title : "落场",
			width : "80px"
		}, {
			field : "std",
			title : "计起",
			width : "80px"
		}, {
			field : "sta",
			title : "计落",
			width : "80px"
		}, {
			field : "propertyCode",
			title : "性质",
			width : "80px"
		}, {
			field : "alnCode",
			title : "航空公司",
			width : "80px"
		} ]
	});
}

/**
 * 刷新公务机/通航未接收状态计划数据
 */
function query() {
	$("#dataGrid").bootstrapTable("refresh");
}

/**
 * 预测公务通航计划
 */
function forecastFun() {
	//TODO
}

/**
 * 接收公务通航计划
 */
function acceptFun(){
	var data = $("#dataGrid").bootstrapTable("getSelections");
	if(data.length==0){
		layer.msg('没有选择任何数据');
		return;
	}
	
	layer.confirm("是否接收选中的航班计划？", {
		icon : 3,
		offset : '100px'
	}, function(index) {
		var acceptDataIdStrs = "";
		for(var i=0;i<data.length;i++){
			var rowData = data[i];
			acceptDataIdStrs += rowData.id+","
		}
		acceptDataIdStrs = acceptDataIdStrs.substring(0,acceptDataIdStrs.length-1);
		var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
		$.ajax({
			type : 'post',
			url : ctx + "/plan/specialPlan/accept",
			data : {
				'acceptDataIdStrs' : acceptDataIdStrs
			},
			success : function(data) {
				layer.close(loading);
				if(data=="succeed"){
					layer.msg("公务机通航计划已接收！", {icon: 1});
					$("#dataGrid").bootstrapTable('refresh');
				}else{
					layer.alert("公务机通航计划接收失败！"+data, {icon: 2});
				}
			},
			error:function(msg){
				layer.close(loading);
				layer.alert("公务机通航计划接收失败！", {icon: 2});
			}
		});
		layer.close(index);
	});
}