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
		url: ctx + "/abnormalClick/reskindClickData", 
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
			{field: "kindName", title: "保障类型",editable:false},
			{field: "clickBefore", title: "提前点击",editable:false},
			{field: "clickAfter", title: "滞后点击",editable:false},
			{field: "safeguardTime", title: "保障时长异常",editable:false},
			{field: "nodeUnnormal", title: "节点采集异常",editable:false},
			{field: "noSafeGuard", title: "任务未保障",editable:false},
			{field: "clickUnnormal", title: "不正常点击任务数",editable:false},
			{field: "jobAllnum", title: "任务总数",editable:false},
			{field: "clickUnnormalRatio", title: "不正常点击率",editable:false},
			{field: "ids", title: "详情",editable:false,sortable:false,
				formatter:function(value,row,index){
					return '<button style="width:70px;height:35px" class="layui-btn layui-btn-xs layui-btn-radius bg-blue" onClick="doDetail(\''+value+'\')">详情</button>';
				}
			}
	    ]
	};
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
		$("#param").val(JSON.stringify(filter));
		$("#exportForm").attr("action",ctx + "/abnormalClick/exportReskindClickExcel");
		$("#exportForm").submit();
	});
	
	//详情滚动
	$('.detail').each(function(){
		new PerfectScrollbar(this);
	});
});

/**
 * 违规点击详情
 */
function doDetail(ids){
	console.info(ids);
	layer.open({
		type : 1,
		title : "详情",
		content : $("#detail"),
		area:['1000px','600px'],
		success : function(layero, index){
			var tableOptions = {
				url: ctx + "/abnormalClick/reskindClickDetail",
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
			    pageSize: 20,
			    pageList: [20,50,100],
			    queryParamsType:'',
			    queryParams: function (params) {
			    	filter.ids = ids;
			    	filter.pageNumber = (params.offset / params.limit) + 1;
					filter.pageSize =  params.limit;
			        return {
			        	param : JSON.stringify(filter)
			        }
			    },
			    columns:[
			 			{field: "SEQ",title:"序号",sortable:false,editable:false,
							formatter:function(value,row,index){
								return index+1;
							}
						},
						{field: "flightDate", title: "航班日期",editable:false},
						{field: "kindName", title: "保障类型",editable:false},
						{field: "flightNumber", title: "航班号",editable:false},
						{field: "taskName", title: "任务名称",editable:false},
						{field: "userName", title: "作业人",editable:false},
						{field: "details", title: "违规类型",editable:false}
		           ]
			    
			};
			$("#detailTable").bootstrapTable('destroy').bootstrapTable(tableOptions);	
		}
	})
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
