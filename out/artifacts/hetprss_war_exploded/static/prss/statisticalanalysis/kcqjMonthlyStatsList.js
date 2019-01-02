var layer;// 初始化layer模块
var baseTable;// 基础表格
var iframe;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	
	
	//查询功能
	$(".search").click(function() {
		
		if(searchOption()){
			tableOptions.columns=getColumns();
			$("#baseTable").bootstrapTable('destroy');
			baseTable.bootstrapTable(tableOptions);
			var a = $('#baseTable').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 10
				}
			});	
		}else{
			layer.msg("日期不能为空",{icon: 2});
		}
	});
	//打印功能
	$(".print").click(function() {
		$("#listForm").submit();
	})
	
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/kcqj/statistics/monthlyList", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    searchOnEnterKey : true,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10,15,20,50,100],
	    queryParamsType:'',
	    queryParams: function (params) {
	    	return getParams(params);
	    },
	    columns:getColumns()
	};

	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});
function searchOption(){
	var year=$("#year").val();
	var month=$("#month").val();
	if(year==""||month==""){
		return false;
	}else{
		return true;
	}
}

//获取搜索数据
function getParams(params){
    return {
    	year :encodeURIComponent($("#year").val()),
    	month: encodeURIComponent($("#month").val()),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}
function getColumns(){

		var columns = [];
		$.ajax({
		type : 'post',
		url : ctx + "/kcqj/statistics/getColumns",
		async : false,
		data : {
			'year' :$("#year").val(),
		    'month':$("#month").val()
		},
		dataType : 'json',
		success : function(column) {
			var order = {field: "NUM", title: "序号",align:'center',halign:'center',editable:false};
			var name = {field: "NAME", title: "姓名",align:'center',halign:'center',editable:false};
			var count = {field: "workQuantity", title: "合计",align:'center',halign:'center',editable:false};
			columns.push(order);columns.push(name);columns.push(count);
			$.each(column,function(i,value){
				var every={field:value.field,title:value.title,align:'center',halign:'center',editable:false};
				columns.push(every);
			});	
		}
		});
		return columns;
}






