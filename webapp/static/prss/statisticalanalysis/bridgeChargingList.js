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
		url: ctx + "/bridge/statistics/dataList", 
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
			{field: "ID", title: "数据主键",align:'left',halign:'center',editable:false,formatter:function(value, row, index){
				return row.START_DAY + row.FLIGHT_NUMBER + row.IN_OUT_FLAG.substring(0,1);
			}},
			{field: "START_DAY", title: "运行日",align:'left',halign:'center',editable:false},
			{field: "FLIGHT_NUMBER", title: "航班号",align:'left',halign:'center',editable:false},
			{field: "IO_TYPE", title: "进出",align:'left',halign:'center',editable:false},
			{field: "BRIDGE_NAME", title: "桥位",align:'left',halign:'center',editable:false},
			{field: "PUT_TIME", title: "廊桥靠接时间",align:'left',halign:'center',editable:false},
			{field: "LEAVE_TIME", title: "廊桥撤离时间",align:'left',halign:'center',editable:false}
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




