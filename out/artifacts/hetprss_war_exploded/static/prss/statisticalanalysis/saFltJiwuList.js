var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var filter = {};
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/saFltJiwu/getData", 
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
	    	// 航空公司
			filter.airlines = $("input[name='airlinevalue']").val();
			// 航班日期开始时间
			filter.startDate = $("#startDate").val();
			// 航班日期结束时间
			filter.endDate = $("#endDate").val();
			// 进出港标识
			filter.flag = $("#flag").val();
			// 搜索
			filter.searchValue = encodeURI($("#searchValue").val().toUpperCase());
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
			{field: "airlineShortName", title: "运输公司",editable:false},
			{field: "inOutFlag", title: "进出属性",editable:false},
			{field: "flightDate", title: "航班日期",editable:false},
			{field: "flightNumber2", title: "航班号",editable:false},
			{field: "acttypeCode", title: "机型",editable:false},
			{field: "aircraftNumber", title: "机号",editable:false},
			{field: "atdOrAta", title: "起降时间",editable:false},
			{field: "departApt3code", title: "起飞机场",editable:false},
			{field: "arrivalApt3code", title: "降落机场",editable:false},
			{field: "fltAttrCode", title: "航段性质",editable:false},
			{field: "propertyShortName", title: "任务性质",editable:false},
			{field: "fxFlag", title: "是否放行",editable:false},
			{field: "specialService", title: "非例行",editable:false}
	    ]
	};
	tableOptions.height = $("#baseTable").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	//下载
	$("#down").click(function(){
		// 航空公司
		filter.airlines = $("input[name='airlinevalue']").val();
		// 航班日期开始时间
		filter.startDate = $("#startDate").val();
		// 航班日期结束时间
		filter.endDate = $("#endDate").val();
		// 进出港标识
		filter.flag = $("#flag").val();
		// 搜索
		filter.searchValue = encodeURI($("#searchValue").val().toUpperCase());
		$("#param").val(JSON.stringify(filter));
		$("#exportForm").attr("action",ctx + "/saFltJiwu/exportExcel");
		$("#exportForm").submit();
	});
	
	// 筛选
	$("#filter").click(function() {		
		var filterIframe = layer.open({
		type : 1,
		title : "筛选",
		area : ["520px","400px"],
		content : $("#filterDiv"),
		btn : [ "确定", "重置", "取消" ],
		yes : function(index) {
			// 航空公司
			filter.airlines = $("input[name='airlinevalue']").val();
			// 航班日期开始时间
			filter.startDate = $("#startDate").val();
			// 航班日期结束时间
			filter.endDate = $("#endDate").val();
			// 进出港标识
			filter.flag = $("#flag").val();
			// 搜索
			filter.searchValue = encodeURI($("#searchValue").val().toUpperCase());
			tableOptions.queryParams.param =JSON.stringify(filter);
			$("#baseTable").bootstrapTable('refreshOptions', tableOptions);	
			layer.close(index);
		},
		btn2 : function(index) {// 重置表单及筛选条件
			$("#filterForm").find("select").val('').removeAttr('selected');
			$("#filterForm").find("input").not('input[type=checkbox]').val('');
			filter = {};
			return false;
		},
		btn3 : function(index) {// 关闭前重置表单及筛选条件
			layer.close(index);
		}
		});
	});
	
	//回车搜索
	var searchInput =$("#searchValue");    
		searchInput.bind('keydown', function(event) {
		if (event.keyCode == "13") {
			var text = searchInput.val();
			if (text) {
				// 航空公司
				filter.airlines = $("input[name='airlinevalue']").val();
				// 航班日期开始时间
				filter.startDate = $("#startDate").val();
				// 航班日期结束时间
				filter.endDate = $("#endDate").val();
				// 进出港标识
				filter.flag = $("#flag").val();
				// 搜索
				filter.searchValue = encodeURI($("#searchValue").val().toUpperCase());
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

function setNullValue(obj){
	obj.value = "";
}

/**
 * 筛选弹出复选框
 */
function openCheck(type) {
	layer.open({
	type : 2,
	title : '复选',
	content : ctx + "/saFltJiwu/openCheck?type=" + type + "&selectedId=" + $("input[name='" + type + "value']").val() + "&selectedText=" + $("input[name='" + type + "']").val(),
	btn : [ "确定", "清空已选", "取消" ],
	area : [ '800px', '450px' ],
	yes : function(index, layero) {
		var iframeWin = window[layero.find('iframe')[0]['name']];
		var data = iframeWin.getChooseData();
		$("input[name='" + type + "']").val(data.chooseTitle);
		$("input[name='" + type + "value']").val(data.chooseValue);
		layer.close(index);
	},
	btn2 : function(index, layero) {// 重置表单及筛选条件
		var iframeWin = window[layero.find('iframe')[0]['name']];
		iframeWin.clearSelect();
		return false;
	},
	btn3 : function(index) {// 关闭前重置表单及筛选条件
		layer.close(index);
	}
	});
}
