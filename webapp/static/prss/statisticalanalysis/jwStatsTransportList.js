var layer;// 初始化layer模块
var baseTable;// 基础表格
var iframe;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	initSelect();
	
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
	$("#searchText").keydown(function(e){
		if (e.keyCode == 13) {
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
	    }
		
	});
	//打印功能
	$(".print").click(function() {
		$("#listForm").submit();
	});

	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/jwys/statistics/dataList", 
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
			{field: "DESCRIPTION_CN", title: "航空公司",align:'left',halign:'center',editable:false},
			{field: "IN_OUT_FLAG", title: "进出港",align:'left',halign:'center',editable:false},
			{field: "FLIGHT_DATE", title: "航班日期",align:'left',halign:'center',editable:false},
			{field: "ATA_D", title: "起降时间",align:'left',halign:'center',editable:false},
			{field: "DEPART_APT3CODE", title: "起飞机场",align:'left',halign:'center',editable:false},
			{field: "ARRIVAL_APT3CODE", title: "目的机场",align:'left',halign:'center',editable:false},
			{field: "FLIGHT_NUMBER2", title: "航班号",align:'left',halign:'center',editable:false},
			{field: "AIRCRAFT_NUMBER", title: "飞机号",align:'left',halign:'center',editable:false},
			{field: "ACTTYPE_CODE", title: "机型",align:'left',halign:'center',editable:false},
			{field: "PROPERTY_CN", title: "飞行任务",align:'left',halign:'center',editable:false},
			{field: "FW_TYPE", title: "是否放行",align:'left',halign:'center',editable:false}
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
    	alnCode:encodeURIComponent($("#alnCode").val()),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}

function initSelect() {
	$('#alnCode').select2({
//		placeholder : "请选择",
		width : "100%",
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




