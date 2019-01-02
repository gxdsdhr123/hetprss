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
    //设置功能
    $(".set").click(function() {

        iframe = layer.open({
            type : 2,
            title : '价格配置',
            area:['800px','500px'],
            btn: ['关闭'],
            closeBtn : false,
            content : ctx + "/jwcbjc/statistics/CBPricePage",
            btn1 : function(index, layero) {
                saveSuccess();
                return false;
            }
        });
    });

	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/jwcbjc/statistics/dataList",
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
			{field: "FLIGHT_DATE", title: "航班日期",align:'left',halign:'center',editable:false},
			{field: "FLIGHT_NUMBER", title: "航班号",align:'left',halign:'center',editable:false},
			{field: "AIRCRAFT_NUMBER", title: "机号",align:'left',halign:'center',editable:false},
			{field: "ACTTYPE_CODE", title: "机型",align:'left',halign:'center',editable:false},
            {field: "ROUTE_NAME", title: "航线",align:'left',halign:'center',editable:false},
			{field: "ROUTE_TYPE", title: "航线性质",align:'left',halign:'center',editable:false},
            {field: "LEG_NAME", title: "航段",align:'left',halign:'center',editable:false},
            {field: "LEG_TYPE", title: "航段性质",align:'left',halign:'center',editable:false},
            {field: "AIRPORT_NAME", title: "机场",align:'left',halign:'center',editable:false},
			{field: "START_TM", title: "开始时间",align:'left',halign:'center',editable:false},
			{field: "END_TM", title: "结束时间",align:'left',halign:'center',editable:false},
            {field: "DEP_ARR_TM", title: "起降时间",align:'left',halign:'center',editable:false},
            {field: "IN_OUT_FLAG", title: "进出属性",align:'left',halign:'center',editable:false},
            {field: "PROPERTY_CODE", title: "任务性质",align:'left',halign:'center',editable:false},
            {field: "FLT_TYPE", title: "航班状态",align:'left',halign:'center',editable:false},
            {field: "FEE_NAME", title: "费用名称",align:'left',halign:'center',editable:false},
            {field: "NUMBERS", title: "数量",align:'left',halign:'center',editable:false},
            {field: "SINGAL_FEE", title: "单价",align:'left',halign:'center',editable:false},
            {field: "ALL_FEE", title: "金额",align:'left',halign:'center',editable:false},
			{field: "OPERATOR", title: "操作者",align:'left',halign:'center',editable:false}
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






