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
		url: ctx + "/kcqj/statistics/dataList", 
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
	    columns: [
	        {field: "NUM", title: "序号",align:'left',halign:'center',editable:false},
			{field: "FLIGHT_DATE", title: "日期",align:'left',halign:'center',editable:false},
			{field: "FLIGHT_NUMBER", title: "航班号",align:'left',halign:'center',editable:false},
			{field: "AIRLINE_SHORTNAME", title: "航空公司",align:'left',halign:'center',editable:false},
			{field: "AIRCRAFT_NUMBER", title: "机号",align:'left',halign:'center',editable:false},
			{field: "ACTTYPE_CODE", title: "机型",align:'left',halign:'center',editable:false},
			{field: "PROPERTY_CODE", title: "航班性质",align:'left',halign:'center',editable:false},
			{field: "MONEY", title: "金额",align:'left',halign:'center',editable:false},
			{field: "PER_NUM", title: "人数",align:'left',halign:'center',editable:false},
			{field: "PER_MONEY", title: "个人所得",align:'left',halign:'center',editable:false},
			{field: "OPERATORS", title: "姓名",align:'left',halign:'center',editable:false}
	    ]
	};

	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});
function searchOption(){
	var dateStart=$("#dateStart").val();
	var dateEnd=$("#dateEnd").val();
	if(dateStart==""||dateEnd==""){
		return false;
	}else{
		return true;
	}
}

//获取搜索数据
function getParams(params){
    return {
    	dateStart :encodeURIComponent($("#dateStart").val()),
    	dateEnd: encodeURIComponent($("#dateEnd").val()),
    	searchText:encodeURIComponent($("#searchText").val()),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}






