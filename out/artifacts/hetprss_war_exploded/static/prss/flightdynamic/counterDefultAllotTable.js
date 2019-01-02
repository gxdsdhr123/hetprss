var layer;
var form;
var iframe;
$(function(){
	layui.use(['layer','element',"form"],function(){
		layer = layui.layer;
		form = layui.form;
	});
	//新增
	$("#add").click(function(){
		openEditPage("D","A");
	});
	//删除
	$("#delete").click(function(){
		var ids = "'"+$("#baseTable").bootstrapTable("getAllSelections").map(function(row,i){
			return row.id;
		}).join('\',\'')+"'";
		var loading = layer.load(2);
		$.ajax({
			type:'post',
			url:ctx+"/flightDynamic/deleteCounterAllotById",
			data:{
				ids:ids
			},
			success:function(msg){
				layer.close(loading);
				if("success"==msg){
					layer.msg("删除成功",{icon:1});
					$("#baseTable").bootstrapTable("refresh");
				}else{
					layer.msg("删除失败",{icon:2});
					console.error(msg);
				}
			}
		});
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();
});
//子页面提交完成后，父页面关闭弹出层及刷新表格
function formSubmitCallback() {
	layer.close(iframe);
	$("#baseTable").bootstrapTable("refresh");
}
//初始化表格
function initTable(){
	var tableOptions = {
			url : ctx + "/flightDynamic/getCounterAllotTable", // 请求后台的URL（*）
			method : "get", // 请求方式（*）
			dataType : "json", // 返回结果格式
			striped : true, // 是否显示行间隔色
			pagination : false, // 是否显示分页（*）
			cache : false, // 是否启用缓存
			undefinedText : '', // undefined时显示文本
			toolbar : $(".tool-bar"),
			search : true,
			showRefresh : true,
			height:$(".container-fluid").height(),
			columns : getColumns(),
			onDblClickRow : function(row,tr,field){
				openEditPage(row.dim,row.island);
			}
		}
	$("#baseTable").bootstrapTable(tableOptions);
}
//配置列
function getColumns(){
	var columns = [{
			field : "checkbox",
			checkbox: true
		},{
			field : 'order',
			title : '序号',
			sortable : false,
			editable : false,
			width : 44,
			formatter : function(value, row, index) {
				return index + 1;
			}
		},{
			title:'国际/国内/混装',
			field:'dim'
		},{
			title:'值机岛',
			field:'island'
		},{
			title:'航班号',
			field:'fltNum'
		},{
			title:'值机柜台1',
			field:'counter1'
		},{
			title:'值机柜台2',
			field:'counter2'
		},{
			title:'值机柜台3',
			field:'counter3'
		},{
			title:'已选航空公司',
			field:'aln2codes'
		}
	]
	return columns;
}
//双击后打开编辑页面
function openEditPage(dim,island){
	iframe = layer.open({
		type : 2,
		title : "值机柜台默认分配",
		closeBtn : false,
		area:["100%","100%"],
		content : ctx + "/flightDynamic/counterDefultAllotList?dim="+dim+"&island="+island,
		btn : ['保存','返回'],
		yes : function(index, layero) {
			//保存
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.save(formSubmitCallback);
		}
	});
}