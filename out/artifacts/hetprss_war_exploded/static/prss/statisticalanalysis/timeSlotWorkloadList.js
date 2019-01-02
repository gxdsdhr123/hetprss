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
		url: ctx + "/timeslotworkload/statistics/dataList",
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
	    	return {
	        	dateStart :encodeURIComponent($("#dateStart").val()),
	        	dateEnd: encodeURIComponent($("#dateEnd").val()),  
	        	name:encodeURIComponent($("#name").val()),
	        	jobType : $("#jobType").val(),
	        	pageNumber: params.pageNumber,
	        	pageSize: params.pageSize
	        	
	        }
	    },
	    onLoadSuccess: function (data) {
	    	 mergeCells(data.rows, "TIMESLOT","NAME","ID", 1, $('#baseTable'));//行合并
	    },
	    columns: [
	    	{field: "ID",title:"序号",align:'left',halign:'center',editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "TIMESLOT", title: "日期",align:'left',halign:'center',editable:false},
			{field: "NAME", title: "姓名",align:'left',halign:'center',editable:false},
			{field: "TYPE_NAME", title: "作业类型",align:'left',halign:'center',editable:false},
			{field: "KIND_COUNT", title: "保障次数",align:'left',halign:'center',editable:false},
            {field: "KIND_TIME", title: "保障时长",align:'left',halign:'center',editable:false},
			{field: "TYPE_COUNT", title: "保障总次数",align:'left',halign:'center',editable:false},
            {field: "TYPE_TIME", title: "保障总时长",align:'left',halign:'center',editable:false}
	
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});

//查询功能
$(".search").click(function() {
	refresh();
})
function refresh(){

	$('#baseTable').bootstrapTable('refresh', {
		query : {
			dateStart :encodeURIComponent($("#dateStart").val()),
        	dateEnd: encodeURIComponent($("#dateEnd").val()), 
        	name:encodeURIComponent($("#name").val()),
        	jobType : $("#jobType").val()			
		}
	});
}
//打印功能
$(".print").click(function() {
//	var jobKindName = $("#jobKind").text();
//	console.info(jobKindName);
//	$("input[name=jobKindName]").val(jobKindName);
	$("#listForm").submit();
});
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}
/**
* 合并行
* @param data 原始数据（在服务端完成排序）
* @param fieldName 合并属性名称数组
* @param colspan 列数
* @param target 目标表格对象
*/
function mergeCells(data, fieldName1,fieldName2,fieldName3, colspan, target) {
	
	if (data.length > 0) {
		var value = "总计";
		var num = 0;
		var index = 0;
		for (var i = 0; i < data.length; i++) {
			if (value == data[i][fieldName1]) {
				num++;
			} 
			index++;
		}
		$(target).bootstrapTable('mergeCells', { index: index-num, field: fieldName1, colspan: colspan, rowspan: num });
		$(target).bootstrapTable('mergeCells', { index: index-num, field: fieldName2, colspan: colspan, rowspan: num });
		$(target).bootstrapTable('mergeCells', { index: index-num, field: fieldName3, colspan: colspan, rowspan: num });
	}
}