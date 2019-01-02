var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var filter = {};
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	//默认日期
	$("#startDate").val(getFormatDate(-3));
	$("#endDate").val(getFormatDate(-1));
	
	//表格
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/abnormalClick/userClickData", 
	    method: "get",
	    dataType: "json",
	    align:"center",
		striped: true,
	    cache: true,
	    sortable: false,
	    undefinedText:'',
	    checkboxHeader:false,
	    search:false,
	    pagination: true,
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
			// 作业类型
			filter.reskind = $("#reskind").val();
			// 姓名
			filter.searchValue = encodeURI($("#searchValue").val());
			filter.pageNumber = (params.offset / params.limit) + 1;
			filter.pageSize =  params.limit;
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
			{field: "SEQ",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "startDate", title: "开始日期",editable:false},
			{field: "endDate", title: "结束如期",editable:false},
			{field: "groupName", title: "班组",editable:false},
			{field: "userName", title: "姓名",editable:false},
			{field: "allTask", title: "完成任务数",editable:false},
			{field: "vgTask", title: "违规任务数",editable:false},
			{field: "clickNormalRatio", title: "正常点击率",editable:false}
	    ]
	};
//	tableOptions.height = $("#baseTables").height()+300;// 表格适应页面高度
	tableOptions.height = $(window).height()-100;// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	//查询按钮
	$("#search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query :  function (params) {
				// 航班日期开始时间
				filter.startDate = $("#startDate").val();
				// 航班日期结束时间
				filter.endDate = $("#endDate").val();
				// 作业类型
				filter.reskind = $("#reskind").val();
				// 姓名
				filter.searchValue = encodeURI($("#searchValue").val());
				filter.pageNumber = (params.offset / params.limit) + 1;
				filter.pageSize =  params.limit;
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
		// 作业类型
		filter.reskind = $("#reskind").val();
		// 姓名
		filter.searchValue = encodeURI($("#searchValue").val());
		$("#param").val(JSON.stringify(filter));
		$("#exportForm").attr("action",ctx + "/abnormalClick/exportUserClickExcel");
		$("#exportForm").submit();
	});
	
	//回车搜索
	var searchInput =$("#searchValue");    
		searchInput.bind('keydown', function(event) {
		if (event.keyCode == "13") {
			var text = searchInput.val();
			if (text) {
				// 航班日期开始时间
				filter.startDate = $("#startDate").val();
				// 航班日期结束时间
				filter.endDate = $("#endDate").val();
				// 作业类型
				filter.reskind = $("#reskind").val();
				// 姓名
				filter.searchValue = encodeURI($("#searchValue").val());
				tableOptions.queryParams.param =JSON.stringify(filter);
				$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
			}else{
				$("#baseTable").bootstrapTable('refresh');
			}
		}
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
