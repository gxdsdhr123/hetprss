var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var clickRowCode = "";//航班id
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	
	//查询功能
	$(".search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query : {
				fltDate : $("#fltDate").val(),
	        	fltNum : $("#fltNum").val(),
	        	operator : $("#operator").val(),
				pageNumber: 1,    
                pageSize: 10
			}
		});
	})
	
	//查看功能
	$(".sub").click(function() {
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要查看的数据', {
				icon : 2
			});
		}else{
		layer.open({
			type : 2,
			title: "进港卸机记录单",
			title : false,
			area:['1000px', '80%'],
			closeBtn : true,
			content : ctx + "/produce/inUnloaderBill/openSub?fltid=" + clickRowCode,
			btn: ['返回']
		});
	}
});
	
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/produce/inUnloaderBill/data", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:true,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
	        	fltDate : $("#fltDate").val(),
	        	fltNum : $("#fltNum").val(),
	        	operator : $("#operator").val(),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize
	        }
	    },
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.ID;
			clickRowCode = row.FLTID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickRow : function onDblClickRow(field, value, row, td) {
			clickRowId = row.ID;
			layer.open({
				type : 2,
				title: "进港卸机记录单",
				title : false,
				area:['1000px', '80%'],
				closeBtn : true,
				content : ctx + "/produce/inUnloaderBill/openSub?fltid=" + clickRowCode,
				btn: ['返回']
			});
		},
	    columns: [
			{field: "SEQ",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "FLIGHT_DATE", title: "航班日期",editable:false,align:'center',halign:'center'},
			{field: "FLIGHT_NUMBER", title: "航班号",editable:false,align:'center',halign:'center'},
			{field: "OPERATOR", title: "监装员",editable:false,align:'center',halign:'center'},
			{field: "CREATE_DATE", title: "操作时间",editable:false,align:'center',halign:'center'}
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});