$(function(){
	var layer;
	var clickRowId = "";

	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/jwhbjc/statistics/dataList",
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    searchOnEnterKey : true,
//	    search: true,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 500,
//	    pageList: [10,15,20,50,100],
	    queryParamsType:'',
	    queryParams: function (params) {
	    	return {
	        	dateStart :encodeURIComponent($("#dateStart").val()),
	        	dateEnd: encodeURIComponent($("#dateEnd").val()),  
	        	searchText:encodeURIComponent($("#searchText").val()),
	        	pageNumber: params.pageNumber,
	        	pageSize: params.pageSize
	        	
	        }
	    },
	    columns: [
	    	{field: "ID",title:"序号",salign:'left',halign:'center',editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "AIRLINE_SHORTNAME", title: "航空公司",align:'left',halign:'center',editable:false},
			{field: "ACTTYPE_CODE", title: "机型",align:'left',halign:'center',editable:false},
			{field: "FX_NUM", title: "放行",align:'left',halign:'center',editable:false},
			{field: "HQHH_NUM", title: "航前/航后",align:'left',halign:'center',editable:false},
            {field: "QW_NUM", title: "勤务",align:'left',halign:'center',editable:false},
			{field: "TOTAL_NUM", title: "架次合计",align:'left',halign:'center',editable:false},
            {field: "REMARK", title: "备注",align:'left',halign:'center',editable:false}
	
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});

//筛选
$(".search").click(function() {
	if(searchOption()){
		var a = $('#baseTable').bootstrapTable('refresh', {
			query : {
				pageNumber: 1,    
                pageSize: 500
			}
		});
	}else{
		layer.msg("日期不能为空",{icon: 2});
	}	
});
//导出
$(".print").click(function() {
	$("#listForm").submit();
});
//搜索
$("#searchText").keydown(function(e){
	if (e.keyCode == 13) {
		if(searchOption()){
			var a = $('#baseTable').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 500
				}
			});
		}else{
			layer.msg("日期不能为空",{icon: 2});
		}
    }
	
});

function searchOption(){
	var dateStart=$("#dateStart").val();
	var dateEnd=$("#dateEnd").val();
	if(dateStart==""||dateEnd==""){
		return false;
	}else{
		return true;
	}
};
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}