var layer;
var clickRowId = "";
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(function(){
	if(flag=="order"){
		$("#showOrderDiv").show();
		$("#showLogDiv").hide();
		//设置滚动条
		$("#orderPlanUL").css("position","relative");
		new PerfectScrollbar("#orderPlanUL");
		initOrder();
	}else if(flag=="log"){
		$("#showOrderDiv").hide();
		$("#showLogDiv").show();
		//设置滚动条
		$("#showLogDiv").css("position","relative");
		new PerfectScrollbar("#showLogDiv");
		
		var num=$("#num").val();
		initGridData();
		$("#searchBtn").click(function(){
			doSearch();
		});
	}
});

function initOrder(){
	$(".sortable").sortable({
		connectWith: ".sortable",
	});
	$(".list-group-item").click(function(){
		var obj = $(this);
		$("#orderPlanUL").find("li").removeClass("bechoose");
		obj.addClass("bechoose");
	});
}

function getOrderData(){
	var lis = $("#orderPlanUL li");
	var datas = [];
	for(var i=0;i<lis.length;i++){
		var li = $(lis[i]);
		var data = {
			id:li.data("code"),
			sortnum:i+1
		}
		datas.push(data);
	}
	return JSON.stringify(datas);
}

function initGridData(){
	var tableOptions = {
		url: ctx+"/arrange/empplan/showLogList",
		    method: "get",	
		    dataType: "json",
			striped: true,	
		    cache: true,
		    uniqueId:"id",
		    checkboxHeader:false,
		    toolbar:$("#tool-box"),
		    search:false,
		    pagination: true,
		    sidePagination: 'server',
		    pageNumber: 1,
		    sortName:"RTIME",
		    sortOrder:"desc",
		    pageSize: 10,
		    pageList: [5, 10, 20],
		    queryParamsType:'',
		    queryParams: function (params) {
		    	var param = {  
		    			timeStart : $("#timeStart").val(),
		    			timeEnd : $("#timeEnd").val(),
	                    pageNumber: params.pageNumber,    
	                    pageSize: params.pageSize,
	                    sortName:params.sortName,
	                    sortOrder:params.sortOrder,
	                    empName: $("#empName").val()
	                }; 
	                return param;     
		    },
		    columns : [ {
					field : "RM",
					title : "序号",
					width : "5%"
				}, {
					field : "WORKER_NAME",
					title : "员工姓名",
					width : "20%"
				}, {
					field : "OPT_TYPE",
					title : "操作类型",
					width : "20%"
				}, {
					field : "WORK_TIME",
					title : "工作时间",
					width : "40%"
				}, {
					field : "BLOCKUP_REASON",
					title : "停用原因",
					width : "15%"
				} ]
		};
	//tableOptions.height = $("body").innerHeight()-$("#searchBox").outerHeight()-60;
	$("#baseTable").bootstrapTable(tableOptions);
//	new PerfectScrollbar("#showLogDiv").update();
}

function doSearch(){
	initGridData();
	$("#baseTable").bootstrapTable('refresh');
}
	