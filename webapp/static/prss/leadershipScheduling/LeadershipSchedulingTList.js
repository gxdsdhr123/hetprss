var layer, form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
	form.on('select(templateSel)', function(data) {
		var id = data.value;
		changeTemplate(id);
	});
});

var createDetailColumns = [];
var dateWeekStr = [ "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" ];
var weekColumns = [];
var table = $('#createDetailTable');
var officeId = "";
var seqNum = "";
var clickId = "";
var time = "";
var searchTime = "";
var tableData = "";
$(function() {
	// 默认选中当前时间
	var daystr = getOperaDate('flight');
	$('#searchTime').val(daystr);
	searchTime = daystr;
	
	// 初始化数据
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();

	var divH = $(window).height();
	$("#createDetailTableDiv").css("height", divH - 40);
	new PerfectScrollbar("#createDetailTableDiv");

});


function initTable() {
	clickId = "";
	clickFeild = "";
	ifModify = false;
	oldLen = null, len = null;
	createDetailColumns = [];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/leaderPlan/leader/getTableHeader",
		data : null,
		dataType : "json",
		success : function(result) {
			getDetailColumns(result);
		}
	});
}

function getDetailColumns(heaerPart) {
	var createRow2 = [];
	var createRow1 = [];

	timeToWeek(searchTime);
	createRow1.push({
		'title': "序号",
		'field' : "SEQNUM",
		'valign' : "middle",
		'width' : 5 + '%'
	}, {
		'title' : "部门",
		'field' : "OFFICENAME",
		'width' : 20 + '%',
		'valign' : "middle",
		'align' : 'left',
	});
	for(var i = 0; i < weekColumns.length; i++) {
		var title = "";
		if(weekColumns[i] == 'MON') {
			title = "周一  - " + (dealTime(1, searchTime, 'cell'));
		}
		if(weekColumns[i] == 'TUE') {
			title = "周二 - " + (dealTime(2, searchTime, 'cell'));
		}
		if(weekColumns[i] == 'WED') {
			title = "周三  - " + (dealTime(3, searchTime, 'cell'));
		}
		if(weekColumns[i] == 'THU') {
			title = "周四  - " + (dealTime(4, searchTime, 'cell'));
		}
		if(weekColumns[i] == 'FRI') {
			title = "周五  - " + (dealTime(5, searchTime, 'cell'));
		}
		if(weekColumns[i] == 'SAT') {
			title = "周六  - " + (dealTime(6, searchTime, 'cell'));
		}
		if(weekColumns[i] == 'SUN') {
			title = "周日  - " + (dealTime(0, searchTime, 'cell'));
		}
		
		createRow1.push({
			'field' : weekColumns[i],
			'title' : title,
			'align' : 'left',
			'formatter' : function (value, row, index) {  
				var str = String(value);
				 
				if(null != value) {
					return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
				} else {
					return '';
				}
	        }  
		});
	}

	createDetailColumns.push(createRow1);
	createDetailColumns.push(createRow2);
	initTableDate();
}

function initTableDate() {
	var createDetailOption = {
		method : 'get',
		striped : true,
		undefinedText : '',
		queryParams : function(params) {
			var param = {
				searchTime : searchTime
			};
			return param;
		},
		url : ctx + "/leaderPlan/leader/getGridData/TPlan",
		columns : createDetailColumns,
		onLoadSuccess : function(data) {
			tableData = data;
		}
	};
	createDetailOption.height = $(window).height() * 0.94;
	table.bootstrapTable(createDetailOption);
}
// 处理时间
function dealTime(dayNum, dat, flag) {
	if (dayNum == "0") {
		dayNum = 7;
	}
	var uom = new Date(), dateStr = '', fday = '';
	fday = dat.substring(6, 8);
	uom.setYear(dat.substring(0, 4));
	uom.setMonth(parseInt(dat.substring(4, 6)) - 1);
	uom.setDate(fday);
	// 上一周下一周
	if ('btn' == flag) {
		uom.setDate(parseInt(fday) + dayNum);
	}
	// 单元格的日期
	if ('cell' == flag) {
		uom.setDate(uom.getDate() - (uom.getDay() - dayNum));
	}
	var mon = (uom.getMonth() + 1) + '';
	if (mon.length != 2) {
		mon = '0' + mon;
	}
	var day = uom.getDate() + '';
	if (day.length != 2) {
		day = '0' + day;
	}
	dateStr = '' + uom.getFullYear() + mon + day;
	return dateStr;
}
//获取日期， 如果日期为空则默认当前时间
function getOperaDate(param) {
	var date = new Date();
	var year = date.getFullYear();
	var mon = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+1) : (date.getMonth()+1);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	var min = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
//	daystr = '' +  year + "-" +  mon + "-" + day + " " + hour + ":" + min + ":" + second;
	if('flight' == param) {		
		daystr = '' +  year  +  mon  + day;
	}
	if('create' == param) {
		daystr = '' +  year + "-" +  mon + "-" + day + " " + hour + ":" + min + ":" + second;
	}
	
	return daystr;
}
function changeTemplate(id) {
	table.bootstrapTable('destroy');
	initTable();
}

function timeToWeek(time) {
	var uom = new Date(), dateStr = '', fday = '', beforeIndex = '', afterIndex = '';
	fday = time.substring(6, 8);
	uom.setYear(time.substring(0, 4));
	uom.setMonth(parseInt(time.substring(4, 6)) - 1);
	uom.setDate(fday)
	
	var index = parseInt(uom.getDay());
		beforeIndex = index - 1;
		afterIndex = index + 1;
	if(index == 0) {
		beforeIndex = 6;
	}
	if(index == 6 ) {
		afterIndex = 0;
	}
	
	weekColumns.push(dateWeekStr[beforeIndex]);
	weekColumns.push(dateWeekStr[index]);
	weekColumns.push(dateWeekStr[afterIndex]);
}

