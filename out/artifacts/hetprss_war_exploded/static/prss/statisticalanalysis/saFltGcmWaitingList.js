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
	
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/saFltGcmWaiting/getData", 
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
			{field: "id",title:"id",sortable:false,editable:false,visible:false,
				formatter:function(value,row,index){
					return row.id;
				}
			},
			{field: "flightDate", title: "日期",editable:false},
			{field: "flightNumber", title: "航班号",editable:false},
			{field: "routeName", title: "航段",editable:false},
			{field: "std", title: "计划离港时间",editable:false},
			{field: "difufuwuGcmTm", title: "关舱门时间",editable:false},
			{field: "atd", title: "实际起飞时间",editable:false},
			{field: "waitingTm", title: "关舱门候机坪等待时间(分钟)",editable:false},
			{field: "waitingReason", title: "等待原因",editable:false},
			{field: "remark", title: "备注",editable:false}
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
		$("#param").val(JSON.stringify(filter));
		$("#exportForm").attr("action",ctx + "/saFltGcmWaiting/exportExcel");
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
