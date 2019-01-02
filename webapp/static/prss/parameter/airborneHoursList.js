var layer;// 初始化layer模块
var baseTable;// 基础表格
var setWin = null;
//var form;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
//		form = layui.form;
	})
	initSelect2("actType","请选择机型，支持多选");
	initSelect2("airport","请选择机场，支持多选");

	//查询功能
	$(".search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query : {
				pageNumber : 1,
				pageSize : 10
			}
		});
	})
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/parameter/airborne/dataList", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:true,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return getParams(params);
	    },
//		onDblClickRow: function (row) {
//			var text=row.TEL_DATA;
//            layer.open({
//            	  type: 1, 
//            	  title: '原始报文内容',
//            	  content:text ,
//            	  area: ['500px','500px']
//            	});
//        },
	    columns: [
	        {field : "checkbox",checkbox:true,title : "",sortable : false,editable : false},
	        {field: "NUM", title: "序号",editable:false,align:'left',halign:'center'},
	        {field: "ID", title: "ID",editable:false,align:'left',halign:'center',visible:false},
			{field: "DESCRIPTION_CN", title: "起落机场",editable:false,align:'left',halign:'center'},
			{field: "ACTTYPE_CODE", title: "机型",editable:false,align:'left',halign:'center'},
			{field: "STANDARD_FLT_TIME", title: "标准航程时间",editable:false,align:'left',halign:'center'},
			{field: "DRIFT_VALUE", title: "浮动值±",editable:false,align:'left',halign:'center'},
			{field: "BEGIN_FLT_DATE", title: "开始日期",editable:false,align:'left',halign:'center'},
			{field: "END_FLT_DATE", title: "结束日期",editable:false,align:'left',halign:'center'},
			{field: "CALC_FLT_TIME", title: "计算航程时间",editable:false,align:'left',halign:'center'},
			{field: "OPER_DATE", title: "操作时间",editable:false,align:'left',halign:'center'},
			{field: "NAME", title: "操作人员",editable:false,align:'left',halign:'center'},
			{field: "OPER_TYPE", title: "操作类型",editable:false,align:'left',halign:'center'},
			{title : "操作",field: "ID",editable : false,
	   	    	  formatter : function(value, row, index){
	   	    		  return "<i class='fa fa-play' onclick='compute("+value+")'></i>" +"&nbsp;&nbsp;&nbsp;"+
	   	    		"<i class='fa fa-pencil' onclick='editRow("+value+")'></i>" +"&nbsp;&nbsp;&nbsp;"+
 	    		  		"<i class='fa fa-trash-o' onclick='deleteRow("+value+")'></i>";
	   	    		  
	   	    		}
			
	   	     }
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	// 新增
	$("#addBtn").click(function() {
		setWin = layer.open({
			type : 2,
			title : "新增",
			closeBtn : false,
			area : ['800px','500px'],
			content : ctx + "/parameter/airborne/newRecord?",
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
//				layero.find('iframe').contents().find('iframe')[0].contentWindow.prepareMembers();
				iframeWin.saveForm();
				return false;
			}
		})
	});
	
	
	//批量删除
	$("#deleteBtn").click(function() {
		var rows = $("#baseTable").bootstrapTable('getSelections');  
	    if (rows.length== 0) {  
	    	layer.msg("请选择要删除的记录",{icon:7});
	        return;  
	    }  
	    var ids = '';
	    for (var i = 0; i < rows.length; i++) {  
	        ids += rows[i]['ID'] + ",";  
	    }  
	    ids = ids.substring(0, ids.length - 1);
	    deleteRow(ids);
	});
	//批量计算
	$("#computeBtn").click(function() {
		var rows = $("#baseTable").bootstrapTable('getSelections');  
	    if (rows.length== 0) {  
	        layer.msg("请选择要计算的记录",{icon:7});
	        return;  
	    }  
	    var ids = '';
	    for (var i = 0; i < rows.length; i++) {  
	        ids += rows[i]['ID'] + ",";  
	    }  
	    ids = ids.substring(0, ids.length - 1);
	    compute(ids);
	});
	

});


function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,
        language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
    }); 
}

function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}
function getParams(params){
	var airport = $("select[name='airport']").val();
	if(airport!=null&&airport!="null"){
		airport = airport.join(",");
	}else{
		airport="";
	}
	var actType = $("select[name='actType']").val();
	if(actType!=null&&actType!="null"){
		actType = actType.join(",");
	}else{
		actType="";
	}
    return {
    	airport:encodeURIComponent(airport),
    	actType:encodeURIComponent(actType),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}
function saveSuccess(){
	layer.close(setWin);
	$("#baseTable").bootstrapTable('refresh');
}

function removeRow(o){
	alert(o);
}
function deleteRow(id){
	
	var confirm = layer.confirm('您确定要删除选中记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/parameter/airborne/deleteRow",
			data : {
				'id' : id
			},
			error : function() {
				layer.close(loading);
				layer.msg('操作失败！', {
					icon : 2
				});
			},
			success : function(data) {
				layer.close(loading);
				if (data.code == "0000") {
					layer.msg(data.msg, {
						icon : 1,
						time : 600
					}, function() {
						$("#baseTable").bootstrapTable('refresh');
					});
				} else {
					layer.msg('操作失败！', {
						icon : 2,
						time : 600
					});
				}
			}
		});
	});
}


function editRow(id){
	setWin = layer.open({
		type : 2,
		title : "修改",
		closeBtn : false,
		area : ['800px','500px'],
		content : ctx + "/parameter/airborne/editRow?id="+id,
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveForm();
			return false;
		}
	});
}

function compute(id){
	
	
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/parameter/airborne/compute",
			data : {
				'id' : id
			},
			error : function() {
				layer.close(loading);
				layer.msg('操作失败！', {
					icon : 2
				});
			},
			success : function(data) {
				layer.close(loading);
				if (data.code == "0000") {
					layer.msg(data.msg, {
						icon : 1,
						time : 600
					}, function() {
						$("#baseTable").bootstrapTable('refresh');
					});
				} else {
					layer.msg('操作失败！', {
						icon : 2,
						time : 600
					});
				}
			}
		});
	
}



