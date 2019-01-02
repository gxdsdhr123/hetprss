var layer;// 初始化layer模块
var baseTableLeft;// 基础表格
var baseTableRight;
var iframe;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	initSelect2("airline","请选择航空公司，支持多选");//航空公司
	initSelect2("airport","请选择机场，支持多选");//报文类型
	
	
	//查询功能
	$(".search").click(function() {
		if(searchOption()){
			
			var option= $('#baseTableRight').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 10
					
				}
			});
			var a = $('#baseTableLeft').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 10
					
				}
			});
			
			
		}else{
			layer.msg("日期和时间不能为空",{icon: 2});
			
		}
		
	});
	
	$("#btnChart").click(function(){
		if(!searchOption()){
			layer.msg("日期和时间不能为空",{icon: 2});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title : false,
			area:[$("body").width()+"px",$("body").height()*0.65+"px"],
			btn: ['关闭'],
			closeBtn : true,
			content : ctx + "/plan/planFilter/getChartPage",
			success: function(layer, index){
			    var iframe = window['layui-layer-iframe'+index];
			    //调用子页面的全局函数
			    var date=$("#planDateStart").val()+$("#planDateEnd").val();
		    	var time=$("#planTimeStart").val()+$("#planTimeEnd").val();
		    	var airport = $("select[name='airport']").val();
		    	if(airport!=null&&airport!="null"){
		    		airport = airport.join(",");
		    	}else{
		    		airport="";
		    	}
		    	var airline = $("select[name='airline']").val();
		    	if(airline!=null&&airline!="null"){
		    		airline = airline.join(",");
		    	}else{
		    		airline="";
		    	}
			    iframe.child(date,time,airport,airline);
			}
		});
		
	})
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTableLeft = $("#baseTableLeft");
	baseTableRight= $("#baseTableRight");
	var leftTableOptions = {
		url: ctx + "/plan/planFilter/dataListLeft", 
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
	    	return getParams(params);
	    },
	    columns: [
	        {field: "NUM", title: "序号",align:'left',halign:'center',editable:false},
			{field: "FLT_NO", title: "进港航班",align:'left',halign:'center',editable:false},
			{field: "SHARE_FLT_NO", title: "共享航班号",align:'left',halign:'center',editable:false},
			{field: "TYPE_NAME_CN", title: "机型",align:'left',halign:'center',editable:false},
			{field: "AIRPORT_NAME", title: "起场",align:'left',halign:'center',editable:false},
			{field: "STD", title: "计起",align:'left',halign:'center',editable:false},
			{field: "STA", title: "计落",align:'left',halign:'center',editable:false},
			{field: "ATTR_CODE", title: "属性",align:'left',halign:'center',editable:false},
			{field: "AIRLINE_NAME", title: "航空公司",align:'left',halign:'center',editable:false},
			{field: "SHOW_TIME", title: "日期",align:'left',halign:'center',editable:false}
	    ]
	};
	var rightTableOptions = {
			url: ctx + "/plan/planFilter/dataListRight", 
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
		    	return getParams(params);
		    },
		    columns: [
				{field: "NUM", title: "序号",align:'left',halign:'center',editable:false},
				{field: "FLT_NO", title: "出港航班",align:'left',halign:'center',editable:false},
				{field: "SHARE_FLT_NO", title: "共享航班号",align:'left',halign:'center',editable:false},
				{field: "TYPE_NAME_CN", title: "机型",align:'left',halign:'center',editable:false},
				{field: "AIRPORT_NAME", title: "落场",align:'left',halign:'center',editable:false},
				{field: "STD", title: "计起",align:'left',halign:'center',editable:false},
				{field: "STA", title: "计落",align:'left',halign:'center',editable:false},
				{field: "ATTR_CODE", title: "属性",align:'left',halign:'center',editable:false},
				{field: "AIRLINE_NAME", title: "航空公司",align:'left',halign:'center',editable:false},
				{field: "SHOW_TIME", title: "日期",align:'left',halign:'center',editable:false}
		    ]
		};
	leftTableOptions.height = $("#baseTables").height();// 表格适应页面高度
	rightTableOptions.height = $("#baseTables").height();
	baseTableLeft.bootstrapTable(leftTableOptions);// 加载基本表格
	baseTableRight.bootstrapTable(rightTableOptions);
});
function searchOption(){
	var dateStart=$("#planDateStart").val();
	var dateEnd=$("#planDateEnd").val();
	var timeStart=$("#planTimeStart").val();
	var timeEnd=$("#planTimeEnd").val();
	if(dateStart==""||dateEnd==""||timeStart==""||timeEnd==""){
		return false;
	}else{
		return true;
	}
}

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
	var airline = $("select[name='airline']").val();
	if(airline!=null&&airline!="null"){
		airline = airline.join(",");
	}else{
		airline="";
	}
    return {
    	date :encodeURIComponent($("#planDateStart").val()+$("#planDateEnd").val()),
    	time :encodeURIComponent($("#planTimeStart").val())+encodeURIComponent($("#planTimeEnd").val()),
    	airline:encodeURIComponent(airline),
    	airport:encodeURIComponent(airport),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}






