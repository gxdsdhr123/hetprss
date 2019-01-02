var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

var table = $('#createDetailTable');
var createDetailColumns = [];
var searchData = "";
var searchId = "";
var startDate = "";
var endDate = "";
var showId = "";
$(function() {
	// 初始化数据
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();
	
	// 初始化按钮
	$('button[type=button]').each(function(i, e) {
		$(e).click(function() {			
			var f = e.getAttribute("id");
			if("searchBtn" == f) { // 查询			
				searchId = "";
				startDate = $('#startDate').val();
				endDate = $('#endDate').val();
				searchData = $('#searchData').val();
				changeTemplate();
			} else if("showDetail" == f) {
				if("" != searchId) {
					fBtn('show', '查看', new Array("100%", "99%"), new Array("返回"));						
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
			} else if("printWorld" == f) { // 打印
				if("" != searchId) {
					printWorld('print');					
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
			} else if("downloadAtta" == f) {
				if("" != searchId) {
					downloadAtta('download');					
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
				
			}
		});
	});
	
});

//跳转控制
function fBtn(param, title,area, btn) {
	var method;
	layer.open({
//		id : 'handoverBill',
		type : 2,
		title : title,
		offset : '2%',
		resize : false,
		fix : false,
		area : area,
		content : [ctx + "/oscarrSecurity/report/pageJump/" + searchId + "/flt", 'no'],
		btn : btn,
		yes : function(index, layero) {
			layer.close(index);
		}
		
	});
}
// 下载
function downloadAtta(download) {
	$('#searchId').val(searchId);
	$('#showId').val(showId);
	$('#param').val(download);
	$('#print').submit();
}
// 打印world
function printWorld(print) {
	$('#searchId').val(searchId);
	$('#param').val(print);
	$('#print').submit();
}





function initTable() {

	createDetailColumns = [];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/oscarrSecurity/report/getTableHeader",
		data : null,
		dataType : "json",
		success : function(result) {
			getDetailColumns(result);
		}
	});
}

function getDetailColumns(heaerPart) {
	var createRow1 = [ 
	{
		title : "序号",
		width : 5 + '%',
		formatter: function (value, row, index) {  
			return index+1;  
        }  
	}, {
		field : "FLTID",
		title : "FLTID",
		visible : false
	}, {
		field : "SHOWID",
		title : "SHOWID",
		visible : false
	}, {
		title : "航班日期",
		align : 'center',
		field : "FLIGHTDATE",
	}, {
		field : "INFLIGHTNUMBER",
		align : 'center',
		title : "进港航班号"
	}, {
		field : "OUTFLIGHTNUMBER",
		align : 'center',
		title : "出港航班号"
	}, {
		field : "AIRCRAFTNUMBER",
		align : 'center',
		title : "机号"
	}, {
		field : "INOSCARNAME",
		align : 'center',
		title : "进港OSCAR"
	}, {
		field : "OUTOSCARNAME",
		align : 'center',
		title : "出港OSCAR"
	}];

	createDetailColumns.push(createRow1);
	initTableDate();
}

function initTableDate() {
	// 针对快速连续点击样式变形问题
	$('div[class=mark_c]').attr({'style': 'display: block;'});
	var createDetailOption = {
		method : 'get',
		striped : true,
		checkboxHeader : true,
	    dataType: "json",
	    checkboxHeader:false,
	    sidePagination: 'server',
	    queryParamsType:'',
	    undefinedText: '',
	    onlyInfoPagination: false,
		queryParams : function(params) {
			var param = {
				pageNumber : params.pageNumber,    
                pageSize : params.pageSize,
                startDate : startDate,
                endDate : endDate,
                searchData : searchData
			};
			return param;
		},
		pagination: true,//是否开启分页（*）
        pageNumber:1,//初始化加载第一页，默认第一页
        pageSize: 10,//每页的记录行数（*）
        pageList: [10,15],
		toolbar : $("#tool-box"),
		url : ctx + "/oscarrSecurity/report/getGridData",
		columns : createDetailColumns,
		onClickRow : function onClickRow(row, tr, field) {
			searchId = row.FLTID;
			showId = row.SHOWID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			searchId = row.FLTID;
			fBtn('show', '查看', new Array("100%", "99%"), new Array("返回"));
		},
		onClickCell : function onClickCell(field, value, row, td, tr) {
			
		},
		onLoadSuccess : function(data) {
			time = "";
			$('input[type=checkbox]').attr({'style':'display: none;'});
			
			setTimeout(function() {
				$('div[class=mark_c]').attr({'style': 'display: none;'});
			}, 600);
		}
	};
//	createDetailOption.height = $(window).height() * 0.93;
	table.bootstrapTable(createDetailOption);
}


function changeTemplate() {
//	table.bootstrapTable('destroy');
	// 刷新表头时间不会变
	table.bootstrapTable('refresh');
	initTable();
}

window.onload = function() {
	// 取消遮罩
	setTimeout(function() {
		$('div[class=mark_c]').attr({
			'style' : 'display: none;'
		});
	}, 600);

}