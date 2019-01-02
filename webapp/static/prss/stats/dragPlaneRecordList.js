var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var win = null;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	new PerfectScrollbar('#container');
	
	//初始选择日期
//	$("#startDate").val(getFormatDate(0));
	
	//下载
	$("#down").click(function(){
		var fltNo = $("#fltNo").val();
		var aircraftNumber = $("#aircraftNumber").val();
		var startDate = $("#startDate").val();
		var userName = encodeURI($("#userName").val());
		$("#exportForm").attr("action",ctx + "/stats/dragPlaneRecord/exportExcel?fltNo="+fltNo+"&aircraftNumber="+aircraftNumber+"&startDate="+startDate+"&userName="+userName);
		$("#exportForm").submit();
	});
	
	//查询按钮
	$("#search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query :  function (params) {
				return {
					fltNo : $("#fltNo").val(),
					aircraftNumber : $("#aircraftNumber").val(),
					startDate : $("#startDate").val(),
					userName : encodeURI($("#userName").val()),
					pageNumber: (params.offset / params.limit) + 1,
					pageSize: params.limit
				}
			}
        });
	});
	
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/stats/dragPlaneRecord/getData", 
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
	        return {
	        	fltNo : $("#fltNo").val(),
				aircraftNumber : $("#aircraftNumber").val(),
				startDate : $("#startDate").val(),
				userName : encodeURI($("#userName").val()),
				pageNumber: (params.offset / params.limit) + 1,
				pageSize: params.limit
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
			{field: "fltNo", title: "航班号",editable:false},
			{field: "aircraftNumber", title: "机号",editable:false},
			{field: "fromStand", title: "原机位",editable:false},
			{field: "toStand", title: "拖至机位",editable:false},
			{field: "eStartTm", title: "预计开始时间",editable:false},
			{field: "eEndTm", title: "预计结束时间",editable:false},
			{field: "sStartTm", title: "实际开始时间",editable:false},
			{field: "sEndTm", title: "实际结束时间",editable:false},
			{field: "createTm", title: "创建任务时间",editable:false},
			{field: "createUserName", title: "新增操作人",editable:false},
			{field: "stateTmConfirm", title: "确认时间",editable:false},
			{field: "stateTmDel", title: "删除时间",editable:false},
			{field: "operUserName", title: "确认/删除操作人",editable:false}
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
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

function setNullValue(obj){
	obj.value = "";
}
