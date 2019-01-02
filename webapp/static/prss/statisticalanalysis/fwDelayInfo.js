var baseTable = $("#dataTable");

$(function(){
	_search();
})

function _search(){
	
	var tableOptions = {
			url: ctx + "/statisticalanalysis/fwDelayInfo/getData", 
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
				params.airline = escape($('#airline').val());
				
				return params;
			},
			height : $(window).height() - 70,
			columns: [
			          [{
			        	  field : "rownum",
			        	  title : "序号",
			        	  align : 'center',
			        	  colspan: 1,
			        	  rowspan: 2,
			        	  formatter : function(value, row, index) {
			      			return index + 1;
			      		}
			          },
			          {
			        	  field : "airlines",
			        	  title : "航空公司",
			        	  align : 'center',
			        	  colspan: 1,
			        	  rowspan: 2
			          },
			          {
			        	  field : "arrangeTypeText",
			        	  title : "类型",
			        	  align : 'center',
			        	  halign : 'center',
			        	  rowspan: 2
			          },
			          {
			        	  field : "yj",
			        	  title : "预计使用",
			        	  align : 'center',
			        	  halign : 'center',
			        	  colspan:7
			          },
			          {
			        	  field : "sj",
			        	  title : "实际使用",
			        	  align : 'center',
			        	  halign : 'center',
			        	  colspan:7
			          }],
			          [{
			        	  field : "eBreakfast",
			        	  title : "早餐",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "eLunch",
			        	  title : "午餐",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "eDinner",
			        	  title : "晚餐",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "eAccommodation",
			        	  title : "住宿",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "eTraffic",
			        	  title : "交通",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "eDrinks",
			        	  title : "饮料",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "eNightSnack",
			        	  title : "夜宵",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "aBreakfast",
			        	  title : "早餐",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "aLunch",
			        	  title : "午餐",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "aDinner",
			        	  title : "晚餐",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "aAccommodation",
			        	  title : "住宿",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "aTraffic",
			        	  title : "交通",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "aDrinks",
			        	  title : "饮料",
			        	  align : 'center',
			        	  halign : 'center',
			          },
			          {
			        	  field : "aNightSnack",
			        	  title : "夜宵",
			        	  align : 'center',
			        	  halign : 'center',
			          }]
			          ]
	};
	baseTable.bootstrapTable('destroy').bootstrapTable(tableOptions);// 加载基本表格
	
}