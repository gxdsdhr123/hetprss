var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	$("select").select2();
	initDataGrid();
	/* 增加 */
	$("#addBtn").click(function() {
		form("");
	});

	/* 修改 */
	$("#editBtn").click(function() {
		var id = getSelected();
		if (!id) {
			layer.msg("请选择要修改的行！", {
				icon : 7
			});
			return false;
		}
		form(id);
	});
	/* 删除 */
	$("#remove").click(function() {
		var id = getSelected();
		if (!id) {
			layer.msg("请选择要删除的行！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中模板？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				type : 'POST',
				url : ctx + "/workflow/process/processRemove",
				data : {
					id : id
				},
				success : function(data) {
					layer.close(confirm);
					if(data=="successed"){
						layer.msg("删除成功！", {
							icon : 1
						});
						$("#dataGrid").bootstrapTable("refresh");
					} else {
						layer.msg("删除失败！"+data, {
							icon : 7
						});
					}
				}
			});
		}, function() {

		});
	});
})
/**
 * 初始化表格
 */
function initDataGrid() {
	$("#dataGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "id",
		url : ctx + "/workflow/process/getProcessData",
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
	var columns = [ {
		field : "id",
		title : '序号',
		width : '50px',
		align : 'center',
		formatter : function(value, row, index) {
			return "<input type='radio' name='idRadio' value='" + value + "'>&nbsp;&nbsp;"+(index + 1);
		}
	}, {
		field : "name",
		title : '名称',
	}, {
		field : "displayName",
		title : '流程显示名称',
	}, {
		field : "jobKind",
		title : '保障类型',
	}, {
		field : "jobType",
		title : '作业类型',
	} 
	/*,{
		field : "state",
		title : '状态',
		formatter : function(value, row, index) {
			if (value == 1) {
				return "启用"
			} else {
				return "禁用"
			}
		}
	}, {
		field : "version",
		title : '版本号',
	} */
	];
	return columns;
}
var formWin = null;
function form(id){
	formWin = layer.open({
		type : 2,
		title : false,
		closeBtn:false,
		area : [ '100%', '100%' ],
		btn : [ "保存", "取消" ],
		content : [ctx + "/workflow/process/processForm?id="+id,"yes"],
		btn1 : function(index, layero) {
			var confirm = layer.confirm('您确定要保存？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				layer.close(confirm);
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveDeploy();
			}, function() {

			});
		}
	});
}

function closeFormWin(){
	if(formWin){
		if(formWin){
			layer.close(formWin);
			$("#dataGrid").bootstrapTable("refresh");
		} 
	}
}

function getSelected(){
	return $("input[name='idRadio']:checked").val();
}

function changKind(){
	queryJobType($("#jobKind").val());
	query();
}
function query(){
	$.ajax({
		type : 'POST',
		url : ctx + "/workflow/process/getProcessData",
		data :{
			jobType : $("#jobType").val(),
			jobKind:$("#jobKind").val()
		},
		dataType:"json",
		async : false,
		success : function(data) {
			$("#dataGrid").bootstrapTable("load", data);
		}
	}); 
}

function queryJobType(data){
	$("#jobType").empty();
	$.ajax({
		type : 'POST',
		url : ctx+"/workflow/process/processProfile",
		data :{
			resKind:data
		},
		dataType:"json",
		async : false,
		success : function(data) {
			$("#jobType").append("<option value=''>"+"请选择作业类型"+"</option>");
			for(var i=0;i<data.length;i++){
				$("#jobType").append("<option value='"+data[i].RESTYPE+"'>"+data[i].TYPENAME+"</option>");
			} 
		}
	}); 
}