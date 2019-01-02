var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	
	var d = new Date(new Date().getTime()-1*24*60*60*1000);
    function addzero(v) {
    	if (v < 10){
    		return '0' + v;
    	}
    	return v.toString();
    }
	var s = d.getFullYear().toString() + addzero(d.getMonth() + 1)+ addzero(d.getDate());
	$("#hisdate").val(s);
	
	initFdVipImportGrid();
	
});

var tableOptions = {
		uniqueId : "id",
		url : ctx + "/flightDynamic/getVipHisDate",
		method : "get",
		toolbar : "#toolbar",
		pagination : false,
		showRefresh : false,
		search : true,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getVipImportGridColumns(),
		queryParams : function() {
			var param = {
					hisDate : $("#hisdate").val()
			};
			return param;
		}
	};

/**
 * 初始化表格
 */
function initFdVipImportGrid() {
	$("#fdVipImportGrid").bootstrapTable(tableOptions);
}

/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getVipImportGridColumns() {
	var columns = [ {
		field : "id",
		title : "序号",
		width : '40px',
		align : 'center',
		formatter : function(value, row, index) {
			row["id"] = index;
			return index + 1;
		}
	}, {
		field : "vipid",
		title : "序号",
		visible : false
	}, {
		field : "flightDate",
		title : "日期",
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "flightNumber",
		title : '航班号',
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "vipFlag",
		title : '标识',
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "vipInfo",
		title : '要客详情'
	}, {
		field : "status",
		title : '状态',
		width : '100px',
		align : 'center',
		sortable : true
	}, {
		field : "update_user",
		title : '上传人',
		width : '140px',
		align : 'center',
		sortable : true
	}, {
		field : "update_time",
		title : '时间',
		width : '150px',
		align : 'center',
		sortable : true
	} ];
	return columns;
}

//历史日期选择
function dateFilter() {
	WdatePicker({
	maxDate:'%y-%M-{%d-1}',
	dateFmt : 'yyyyMMdd',
	onpicking : function(dp) {
		tableOptions.queryParams = {
			hisDate : dp.cal.getNewDateStr()
		}
		$("#fdVipImportGrid").bootstrapTable('refreshOptions', tableOptions);
	}
	})
}
