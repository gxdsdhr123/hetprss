var layer;// 初始化layer模块
var baseTable;// 基础表格
var holdDown = 0;
var scrollbar;
var column;
var type;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	type=$("#type").val();
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/statisticalanalysis/ATMZBOW/dataList", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	    	return {pageNumber: params.pageNumber,
	    		   pageSize: params.pageSize,
	    		   date:$("#date").val(),
	    		   type:$("#type").val()}
	    },  
	    columns: getColumns(type)
	};

	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	$(".search").click(function() {
		baseTable.bootstrapTable('refresh',tableOptions);
	})
	
	//打印功能
	$(".print").click(function() {
		$("#printExcel").submit();
	})
});

function getColumns(type){
	if(type=="ATMZBOW"){
		column=[
		        {field: "FLTID", title: "FLTID",align:'left',halign:'left',editable:false,visible:false},
		   		{field: "FLIGHT_NUMBER", title: "CallSign",align:'left',halign:'left',editable:false},
		   		{field: "STD", title: "ScheduleDate",align:'left',halign:'left',editable:false},
		   		{field: "DepAP", title: "DepAP",align:'left',halign:'left',editable:false},
		   		{field: "ArrAP", title: "ArrAP",align:'left',halign:'left',editable:false},
		   		{field: "ASRT", title: "ASRT",align:'left',halign:'left',editable:false},
		   		{field: "ADMIN_PUSHOUT_TM", title: "ASAT",align:'left',halign:'left',editable:false},
		   		{field: "FRIT", title: "FRIT",align:'left',halign:'left',editable:false},
		   		{field: "ATOT", title: "ATOT",align:'left',halign:'left',editable:false},
		   		{field: "ALDT", title: "ALDT",align:'left',halign:'left',editable:false},
		   		{field: "ApDepDelayReason", title: "ApDepDelayReason",align:'left',halign:'left',editable:false},
		   		{field: "InitDepDelayReason", title: "InitDepDelayReason",align:'left',halign:'left',editable:false},
		   		{field: "FlightDelayReason", title: "FlightDelayReason",align:'left',halign:'left',editable:false}
		   		
		    ];
	}else{
		column=[
		        {field: "FLTID", title: "FLTID",align:'left',halign:'left',editable:false,visible:false},
		   		{field: "FLIGHT_NUMBER", title: "CallSign",align:'left',halign:'left',editable:false},
		   		{field: "STD", title: "ScheduleDate",align:'left',halign:'left',editable:false},
		   		{field: "DepAP", title: "DepAP",align:'left',halign:'left',editable:false},
		   		{field: "ArrAP", title: "ArrAP",align:'left',halign:'left',editable:false},
		   		{field: "AOBT", title: "AOBT",align:'left',halign:'left',editable:false},
		   		{field: "AIBT", title: "AIBT",align:'left',halign:'left',editable:false},
		   		{field: "ApDepDelayReason", title: "ApDepDelayReason",align:'left',halign:'left',editable:false},
		   		{field: "InitDepDelayReason", title: "InitDepDelayReason",align:'left',halign:'left',editable:false},
		   		{field: "FlightDelayReason", title: "FlightDelayReason",align:'left',halign:'left',editable:false},
		   		{field: "IntialFlight", title: "IntialFlight",align:'left',halign:'left',editable:false}
		   		
		    ];
	}
	return column;
}














