var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var filter = {};
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
	//默认日期
	$("#startDate").val(getFormatDate(-2));
	$("#endDate").val(getFormatDate(-1));
	
	//获取表格标题、数据,创建列表
	createTable();
	
	//查询按钮
	$("#search").click(function() {
		createTable();
	});
	
	//下载
	$("#down").click(function(){
		// 航班日期开始时间
		filter.startDate = $("#startDate").val();
		// 航班日期结束时间
		filter.endDate = $("#endDate").val();
		$("#param").val(JSON.stringify(filter));
		$("#exportForm").attr("action",ctx + "/saFltNum/exportExcel");
		$("#exportForm").submit();
	});
});

/**
 * 获取表格标题、数据，生成数据报表
 */
function createTable(){
	// 航班日期开始时间
	var startDate = $("#startDate").val();
	filter.startDate = startDate;
	// 航班日期结束时间
	var endDate = $("#endDate").val();
	filter.endDate = endDate;
	$.ajax({
		type : 'post',
		url : ctx + "/saFltNum/getData",
		async : false,
		data : {
			param : JSON.stringify(filter)
		},
		success : function(rsData) {
			if(rsData.columns.length==0){
				layer.msg(startDate+"-"+endDate+"无航班架次统计结果！",{icon:7,time:3000});
			}
			var tableOptions = getTableOption(rsData.rows, rsData.columns);
			if(baseTable!=null){
				baseTable.bootstrapTable("refreshOptions",tableOptions);// 加载基本表格
				baseTable.bootstrapTable("resetView");
			}else{
				baseTable = $("#baseTable");
				baseTable.bootstrapTable(tableOptions);// 加载基本表格
			}
		}
	});
}

function getTableOption(rows,columns){
	var tableOptions = {
		data : rows,
	    align:"center",
		striped: true,
	    cache: true,
	    sortable: false,
	    undefinedText:'',
	    checkboxHeader:false,
	    search:false,
	    pagination: false,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 20,
	    pageList: [20,50,100],
	    paginationPreText: "上一页",  
		paginationNextText: "下一页",
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		columns: columns
	};
	tableOptions.height = $(window).height()-100;// 表格适应页面高度
	return tableOptions;
}

/*
 * 获取日期
 */
function getFormatDate(days){
	var date = new Date();
	date.setDate(date.getDate() + days);
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	return year+""+month+""+day;
}
