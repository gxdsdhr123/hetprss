var baseTable = $("#dataTable");

$(function(){
	_search();
})

function _search(){
	jQuery.fn.bootstrapTable.columnDefaults.align = 'center';
	jQuery.fn.bootstrapTable.columnDefaults.halign = 'center';
	var io = $('#inOut').val();
	var tableOptions = {
			url: ctx + "/inOutBillingStatistics/getData", 
			method: "get",
			dataType: "json",
			striped: true,
			cache: true,
			undefinedText:'',
			checkboxHeader:false,
			pagination: true,
			sidePagination: 'server',
			pageNumber: 1,
			pageSize: 10,
			pageList: [10, 15, 20],
			queryParamsType:'',
			queryParams: function (params) {
				params.startDate = $('#startDate').val();
				params.endDate = $('#endDate').val();
				params.inOut = $('#inOut').val();
				
				return params;
			},
			height : $(window).height() - 70,
			columns: [
				{
					field : "ORDER",
		        	title : "",
		        	formatter : function(value, row, index) {
		      			return index + 1;
		      		}	  
				},{
					field : "DATA_ID",
		        	title : "数据主键"
				},{
					field : "STAT_DAY",
		        	title : "运行日"
				},{
					field : "FLIGHT_NUMBER",
		        	title : "航班号"
				},{
					field : "IN_OUT_FLAG",
		        	title : "进出"
				},{
					field : "AIRCRAFT_NUMBER",
		        	title : "机号"
				},{
					field : "ACTTYPE_CODE",
		        	title : "机型"
				},{
					field : "PROPERTY_CODE",
		        	title : "性质"
				},{
					field : "FLT_ATTR_CODE",
		        	title : "航线性质"
				},{
					field : "ALN_2CODE",
		        	title : "二字"
				},{
					field : "STD",
		        	title : "计起",
		        	visible : io=="D"?true:false
				},{
					field : "ETD",
		        	title : "预起",
		        	visible : io=="D"?true:false
				},{
					field : "ATD",
		        	title : "实起",
		        	visible : io=="D"?true:false
				},{
					field : "STA",
		        	title : "计落",
		        	visible : io=="A"?true:false
				},{
					field : "ETA",
		        	title : "预落",
		        	visible : io=="A"?true:false
				},{
					field : "ATA",
		        	title : "实落",
		        	visible : io=="A"?true:false
				},{
					field : "ACTSTAND_CODE",
		        	title : "机位"
				},{
					field : "DEPART_APT3CODE",
		        	title : "起场"
				},{
					field : "ARRIVAL_APT3CODE",
		        	title : "落场"
				},{
					field : "DELAY_CODE",
		        	title : "延误代码"
				},{
					field : "GATE",
		        	title : "登机口"
				},{
					field : "RUNWAY",
		        	title : "跑道"
				},{
					field : "LIGHT",
		        	title : "灯光"
				},{
					field : "GUIDE",
		        	title : "引导"
				},{
					field : "STATE",
		        	title : "状态"
				},{
					field : "SORT",
		        	title : "分拣口"
				},{
					field : "STOP_OVER1",
		        	title : "经停1"
				},{
					field : "STOP_OVER2",
		        	title : "经停2"
				},{
					field : "STOP_OVER3",
		        	title : "经停3"
				}
			]
	};
	baseTable.bootstrapTable('destroy').bootstrapTable(tableOptions);// 加载基本表格
	
}