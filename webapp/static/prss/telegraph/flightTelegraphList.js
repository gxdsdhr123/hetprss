var layer;// 初始化layer模块
var baseTable;// 基础表格
//var form;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
//		form = layui.form;
	})
//	$('#telType').select2({
//		placeholder : "请选择部门",
//		width : "100%",
//		language : "zh-CN"
//	});
	
//	document.onkeydown = function(e){
//	    var ev = document.all ? window.event : e;
//	    if(ev.keyCode==13) {
//	    	var ruleName = encodeURIComponent($("#ruleName").val());
//	    	var options = $('#baseTable').bootstrapTable('refresh', {
//				query : {
//					pageNumber: 1,    
//	                pageSize: 10
//					
//				}
//			});
//		    return false;
//	     }
//	}

	//查询功能
	$(".search").click(function() {
		if(searchOption()){
			var options = $('#baseTable').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 10
					
				}
			});
		}else{
			layer.msg("接收时间必须同时填写或同时为空",{icon: 2});
			
		}
		
	})
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/telegraph/flight/dataList", 
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
	        return {
	        	flightNumber :encodeURIComponent($("#flightNumber").val()),
	        	telType :encodeURIComponent($("#telType").val()),
	        	acceptDate :encodeURIComponent($("#acceptDate").val()),
	        	acceptTime :encodeURIComponent($("#acceptTimeStart").val())+encodeURIComponent($("#acceptTimeEnd").val()),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize
	        }
	    },
		onDblClickRow: function (row) {
			var text=row.TEL_DATA;
            layer.open({
            	  type: 1, 
            	  title: '原始报文内容',
            	  content:text ,
            	  area: ['500px','500px']
            	});
        },
	    columns: [
			{field: "NUM", title: "序号",editable:false,align:'left',halign:'center'},
			{field: "ACCEPT_DATE", title: "接收日期",editable:false,align:'left',halign:'center'},
			{field: "ACCEPT_TIME", title: "接收时间",editable:false,align:'left',halign:'center'},
			{field: "FLIGHT_NUMBER", title: "航班号",editable:false,align:'left',halign:'center'},
			{field: "TEL_TYPE", title: "报文类型",editable:false,align:'left',halign:'center'},
			{field: "TEL_DATA", title: "原始报文内容",editable:false,align:'left',halign:'center'}

	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});
function searchOption(){
	var acceptTimeStart=$("#acceptTimeStart").val();
	var acceptTimeEnd=$("#acceptTimeEnd").val();
	if((acceptTimeStart!=""&&acceptTimeEnd!="")||(acceptTimeStart==""&&acceptTimeEnd=="")){
		return true;
	}else{
		return false;
	}
}




