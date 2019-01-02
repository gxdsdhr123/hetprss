var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var filter = {};
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	//默认日期
	$("#startDate").val(getFormatDate(-2));
	$("#endDate").val(getFormatDate(-1));
	
	//列表
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/saFltNormal/getData", 
	    method: "get",
	    dataType: "json",
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
	    queryParams: function (params) {
			// 航班日期开始时间
			filter.startDate = $("#startDate").val();
			// 航班日期结束时间
			filter.endDate = $("#endDate").val();
			// 进出港标识
			filter.inOutFlag = $("#inOutFlag").val();
	        return {
				param : JSON.stringify(filter)
	        }
	    },
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
	    columns: [
	        [{title: "", valign: "middle",align:"center",colspan: 1,rowspan: 1},
	        {title: "正常率统计", valign: "middle",align:"center",colspan: 4,rowspan: 1},
	        {title: "不正常原因", valign: "middle",align:"center",colspan: 12,rowspan: 1},
	        {title: "延误时间", valign: "middle",align:"center",colspan: 6,rowspan: 1}
	        ],
			[{field: "airlineShortName", title: "公司名称",editable:false},
			{field: "planFltNum", title: "计划",editable:false},
			{field: "normalFltNum", title: "正常",editable:false},
			{field: "unnormalFltNum", title: "不正常",editable:false},
			{field: "normalFltRatio", title: "正常率",editable:false},
			{field: "unnormalFltNum1", title: "天气",editable:false},
			{field: "unnormalFltNum2", title: "航空公司",editable:false},
			{field: "unnormalFltNum3", title: "流量",editable:false},
			{field: "unnormalFltNum4", title: "军事活动",editable:false},
			{field: "unnormalFltNum5", title: "空管",editable:false},
			{field: "unnormalFltNum6", title: "机场",editable:false},
			{field: "unnormalFltNum7", title: "联检",editable:false},
			{field: "unnormalFltNum8", title: "油料",editable:false},
			{field: "unnormalFltNum9", title: "离港系统",editable:false},
			{field: "unnormalFltNum10", title: "旅客",editable:false},
			{field: "unnormalFltNum11", title: "公共安全",editable:false},
			{field: "unnormalFltNum12", title: "航班时刻安排",editable:false},
			{field: "waitingFltNum1", title: "无时间",editable:false},
			{field: "waitingFltNum2", title: "30分钟内",editable:false},
			{field: "waitingFltNum3", title: "31-60分钟",editable:false},
			{field: "waitingFltNum4", title: "61-120分钟",editable:false},
			{field: "waitingFltNum5", title: "121-240分钟",editable:false},
			{field: "waitingFltNum6", title: "241分钟 以上",editable:false}]
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	//查询按钮
	$("#search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query :  function (params) {
				// 航班日期开始时间
				filter.startDate = $("#startDate").val();
				// 航班日期结束时间
				filter.endDate = $("#endDate").val();
				// 进出港标识
				filter.inOutFlag = $("#inOutFlag").val();
		        return {
					param : JSON.stringify(filter)
		        }
			}
        });
	});
	
	//下载
	$("#down").click(function(){
		// 航班日期开始时间
		filter.startDate = $("#startDate").val();
		// 航班日期结束时间
		filter.endDate = $("#endDate").val();
		// 进出港标识
		filter.inOutFlag = $("#inOutFlag").val();
		$("#param").val(JSON.stringify(filter));
		$("#exportForm").attr("action",ctx + "/saFltNormal/exportExcel");
		$("#exportForm").submit();
	});
});

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
