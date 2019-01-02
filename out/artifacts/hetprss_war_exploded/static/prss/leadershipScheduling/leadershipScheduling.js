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
var table = $('#createDetailTable');
var cellDetailInfo = $('#cellDetailInfo');
var officeId = "";
var seqNum = "";
var clickId = "";
var time = "";
var searchTime = "";
var tableData = "";
var ev;
$(function() {
	
	// 默认选中当前时间
	var daystr = getOperaDate('flight');
	$('#searchTime').val(daystr);
	searchTime = daystr;
	// 初始化数据
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();

	// 上一周
	$('#prevWeekBtn').click(function() {
		// 清除选中单元格
		time = "";
		officeId = "";
		searchTime = $('#searchTime').val();
		var datStr = "";
		datStr = dealTime(-7, searchTime, 'btn');
		searchTime = datStr;
		$('#searchTime').val(datStr);
		changeTemplate("");
	});
	// 下一周
	$('#nextWeekBtn').click(function() {
		// 清除选中单元格
		time = "";
		officeId = "";
		searchTime = $('#searchTime').val();
		var datStr = "";
		datStr = dealTime(7, searchTime, 'btn');
		searchTime = datStr;
		$('#searchTime').val(datStr);
		changeTemplate("");
	});
	// 查询
	$('#searchBtn').click(function() {
		searchTime = $('#searchTime').val();
		changeTemplate("");
	});
	// 领导值班计划
	$('#leadDutyBtn').click(function() {
		if ("" != officeId && "" != time) {
			leadDutyBtn();
		} else {
			layer.msg("请选则其中一天" , {icon : 7, time : 600})
			return;
		}
	});
	// 删除计划
	$('#delPlanBtn').click(function() {
		if ("" != officeId && "" != time) {
			delPlanBtn();
		} else {
			layer.msg("请选则其中一天" , {icon : 7, time : 600});
			return;
		}
	});
	// 打印计划
	$('#printBtn').click(function() {
		printBtn();
	});
	// 增加部门
	$('#addDepartmentBtn').click(function() {
		seqNum = "";
		officeId = "";
		addDepartmentBtn("增加部门计划", 'addDepartment', "add");
	});
	// 修改部门排序
	$('#modifyDepartmentBtn').click(function() {
		if ("" != officeId && "" != seqNum) {
			addDepartmentBtn("修改部门计划排序", 'toModifyDepartment?officeId=' + officeId + "&seqNum=" + seqNum, "modify");
		} else {
			layer.msg("请选中一行" , {icon : 7, time : 600});
			return;
		}
	});
	// 删除部门
	$('#delDepartmentBtn').click(function() {
		if ("" != clickId) {
			officeId = clickId.split(',')[1];
			seqNum = clickId.split(',')[0];
			delDepartmentBtn(officeId);
		} else {
			layer.msg("请选中一行" , {icon : 7, time : 600});
			return;
		}

	});

	var divH = $(window).height();
	$("#createDetailTableDiv").css("height", divH - 40);
	new PerfectScrollbar("#createDetailTableDiv");

});

// 领导值班计划
function leadDutyBtn() {
	if ("" != officeId) {
		layer.open({
			id : 'leaderDutyIframe',
			type : 2,
			title : '领导值班计划',
			offset : '12%',
			resize : false,
			fix : false,
			area : [ "70%", "80%" ],
			content : [ctx + "/leaderPlan/leader/leaderPlan?officeId=" + officeId + "&time=" + time, 'no'],
			btn : [ "保存", "取消" ],
			success : function(layero, index) {
				var iframeId = document.getElementById('leaderDutyIframe').getElementsByTagName("iframe")[0].id;
				$("#"+iframeId).contents().find("body").attr({"style": "min-height: initial;"});
			},
			yes : function(index, layero) {
				layer.close(index);
				changeTemplate("all");
			},
			btn2: function(index) {
				time = "";
				layer.close(index);
				changeTemplate("all");
			}
		});
	} else {
		layer.msg("请选中其中一天" , {icon : 7, time : 600})
		return;
	}
}
// 删除计划
function delPlanBtn() {
	var index = layer.confirm('是否删除当前部门选中计划？', {
		btn : [ '确定', '取消' ], // 按钮
		icon : 3
	}, function() {

		$.ajax({
			type : 'post',
			url : ctx + "/leaderPlan/leader/delDepartment/staffPdate",
			data : {
				officeId : officeId,
				pdate : time
			},
			success : function(msg) {
				if (msg == "success") {
					layer.msg('操作成功！', {
						icon : 1,
						time : 600
					});
					officeId = "";
					seqNum = "";
					layer.close(index);
					changeTemplate("all");
				} else {
					layer.msg("操作失败" , {icon : 2, time : 600});
					return;
				}
			}
		});

	}, function() {
		layer.close(index);
	});

}
// 打印计划
function printBtn() {
	var data = tableData;
	$('input[name=searchTime]').val($('#searchTime').val());
	var columns = $.map(table.bootstrapTable('getOptions').columns[0],
			function(col) {
				if (col.field != "ID" && col.field != "OFFICEID") {
					return {
						"field" : col.field,
						"title" : col.title
					};
				} else {
					return null;
				}

			});
	$("#printTitle").text(JSON.stringify(columns));
	$("#printData").text(JSON.stringify(data));
	$("#printForm").submit();
}
// 增加或修改部门计划
function addDepartmentBtn(title, url, flag) {
	layer.open({
		id : 'addDepartmentIframe',
		type : 2,
		title : title,
		offset : '18%',
		resize : false,
		area : [ "45%", "55%" ],
		content : [ctx + "/leaderPlan/leader/" + url, 'no'],
		btn : [ "保存", "取消" ],
		success : function(layero, index) {
			var iframeId = document.getElementById('addDepartmentIframe').getElementsByTagName("iframe")[0].id;
			$("#"+iframeId).contents().find("body").attr({"style": "min-height: initial;"});
		},
		yes : function(index, layero) {
			var inn = layer.load(2);
			if (null == officeId || "" == officeId) {
				layer.close(inn);
				layer.msg("请选择部门" , {icon : 7, time : 600});
				return;
			}
			
			if ("" != seqNum) {
				if (isNaN(seqNum)) {
					layer.close(inn);
					layer.msg("请输入正确的排序数字" , {icon : 7, time : 600});
					return;
				}
			}
			$('div[class=mark_c]').attr({'style': 'display: block;'});
			saveOrUpdateDept(officeId, seqNum, index, flag, inn);
		
		},
		btn2: function() {
//			table.bootstrapTable('refreshOptions',{pageNumber : 1});
		}
	});
}

// 删除部门计划
function delDepartmentBtn(officeId) {
	// 选定当前一个部门
	var index = layer.confirm('是否删除当前部门所有计划？', {
		btn : [ '确定', '取消' ], // 按钮
		icon : 3
	}, function() {

		$.ajax({
			type : 'post',
			url : ctx + "/leaderPlan/leader/delDepartment/dept",
			data : {
				officeId : officeId
			},
			success : function(msg) {
				if (msg == "success") {
					layer.msg('操作成功！', {
						icon : 1,
						time : 600
					});
					officeId = "";
					seqNum = "";
					layer.close(index);
					changeTemplate("all");
				} else {
					layer.msg("操作失败" , {icon : 2, time : 600});
					return;
				}
			}
		});

	}, function() {
		layer.close(index);
	});
}
// 保存或修改部门计划
function saveOrUpdateDept(officeId, seqNum, index, flag, inn) {
	$.ajax({
		type : 'post',
		url : ctx + "/leaderPlan/leader/saveOrUpdate",
		data : {
			officeId : officeId,
			seqNum : seqNum,
			flag : flag
		},
		success : function(msg) {
			layer.close(inn);
			if (msg == "s1") {
				layer.msg('保存成功！', {
					icon : 1,
					time : 600
				});
				closeFlg = true;
				layer.close(index);
				changeTemplate("all");
			} else if (msg == 'f1') {
				layer.msg("该部门已添加，请重新选择！", {icon : 2, time : 600});
				return;
			} else if (msg == 's2') {
				layer.msg('修改成功！', {
					icon : 1,
					time : 600
				});
				closeFlg = true;
				layer.close(index);
				changeTemplate("all");
			} else if (msg == 'f2') {
				layer.msg("修改失败， 请重试！" , {icon : 2, time : 600});
				return;
			} else {
				layer.msg("操作失败" , {icon : 2, time : 600});
				return;
			}
			
			
		}
	});
}

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
	var createRow1 = [ {
		title : "序号",
		field : "SEQNUM",
		valign: "middle",
		width : 5 + '%'
	}, {
		field : "ID",
		title : "ID",
		visible : false
	}, {
		field : "OFFICEID",
		title : "OFFICEID",
		visible : false
	}, {
		title : "部门",
		align : 'left',
		field : "OFFICENAME",
		width : 15 + '%',
		valign: "middle"
	}, {
		field : "MON",
		align : 'left',
		title : "周一 - " + (dealTime(1, searchTime, 'cell')),
		width : 12 + '%',
		formatter: function (value, row, index) {  
			var str = String(value);
			 
			if(null != value) {
				
				return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
			} else {
				return '';
			}
        }  
	}, {
		field : "TUE",
		align : 'left',
		title : "周二 - " + (dealTime(2, searchTime, 'cell')),
		width : 12 + '%',
		formatter: function (value, row, index) {  
			var str = String(value);
			 
			if(null != value) {
				return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
			} else {
				return '';
			}
        }  
	}, {
		field : "WED",
		align : 'left',
		title : "周三 - " + (dealTime(3, searchTime, 'cell')),
		width : 12 + '%',
		formatter: function (value, row, index) {  
			var str = String(value);
			 
			if(null != value) {
				return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
			} else {
				return '';
			}
        }  
	}, {
		field : "THU",
		align : 'left',
		title : "周四 - " + (dealTime(4, searchTime, 'cell')),
		width : 12 + '%',
		formatter: function (value, row, index) {  
			var str = String(value);
			 
			if(null != value) {
				return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
			} else {
				return '';
			}
        }  
	}, {
		field : "FRI",
		align : 'left',
		title : "周五 - " + (dealTime(5, searchTime, 'cell')),
		width : 12 + '%',
		formatter: function (value, row, index) {  
			var str = String(value);
			 
			if(null != value) {
				return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
			} else {
				return '';
			}
        }  
	}, {
		field : "SAT",
		align : 'left',
		title : "周六 - " + (dealTime(6, searchTime, 'cell')),
		width : 12 + '%',
		formatter: function (value, row, index) {  
			var str = String(value);
			 
			if(null != value) {
				return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
			} else {
				return '';
			}
        }  
	}, {
		field : "SUN",
		align : 'left',
		title : "周日 - " + (dealTime(0, searchTime, 'cell')),
		width : 12 + '%',
		formatter: function (value, row, index) {  
			var str = String(value);
			 
			if(null != value) {
				return str.replace(/:/g,'</br>&nbsp;&nbsp;&nbsp;&nbsp;').replace(/,/g, '</br>').replace(/\+/g, '&nbsp;&nbsp;');
			} else {
				return '';
			}
        }  
	} ];

	createDetailColumns.push(createRow1);
	createDetailColumns.push(createRow2);
	initTableDate();
}

function initTableDate() {
	// 针对快速连续点击样式变形问题
	$('div[class=mark_c]').attr({'style': 'display: block;'});
	
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
		toolbar : $("#tool-box"),
		search : true, 
		searchOnEnterKey : true,
		
		url : ctx + "/leaderPlan/leader/getGridData/leader",
		columns : createDetailColumns,
		onClickRow : function onClickRow(row, tr, field) {
			// 默认选中序号， 部门名称为一行
			if ("SEQNUM" == field || "OFFICENAME" == field) {
				clickId = row.SEQNUM + "," + row.OFFICEID;
				officeId = row.OFFICEID;
				seqNum = row.SEQNUM;
				time = "";
				$(".clickRow").removeClass("clickRow");
				$(tr).addClass("clickRow");
				cellDetailInfo.empty();
			}
			
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			if ("SEQNUM" == field || "OFFICENAME" == field) {
				officeId = row.OFFICEID;
				seqNum = row.SEQNUM;
				addDepartmentBtn("修改部门计划排序", 'toModifyDepartment?officeId=' + row.OFFICEID + "&seqNum=" + row.SEQNUM, "modify");
			} else {
				clickId = "";
				time = dealTime(dateWeekStr.indexOf(field), searchTime, "cell");
				officeId = row.OFFICEID;
				leadDutyBtn();
			}
		},
		onClickCell : function onClickCell(field, value, row, td, tr) {
			if ("SEQNUM" != field && "OFFICENAME" != field) {
				time = dealTime(dateWeekStr.indexOf(field), searchTime, 'cell');
				officeId = row.OFFICEID;
				$(".clickRow").removeClass("clickRow");
				$(td).addClass("clickRow");
				// 鼠标点击 当前表格数据展示
				cellDetailInfo.empty();
				if(null != value) {					
					var str = "";
					var strs = String(value).split(",");
					var html = "";
					for(var i = 0; i < strs.length; i++) {
						str = strs[i].replace(/:/g, ' ');
						html += "<input type='text' class='layui-input time_c' value='" + str + "' style='width: 100%' />";
					}
//					$(td).hover(function(ev) {
//						cellDetailInfo.empty();
						cellDetailInfo.append(html);
						// 跟随鼠标移动
						$(td).mousemove(function(e) { //传递事件对象e
							getMousePos(e);
						});
					
					
				}
			}
		},
		onLoadSuccess : function(data) {
			time = "";
			cellDetailInfo.empty();
			tableData = data;
			
			setTimeout(function() {
				$('div[class=mark_c]').attr({'style': 'display: none;'});
			}, 600);
		}
	};
	createDetailOption.height = $(window).height() * 0.93;
	table.bootstrapTable(createDetailOption);
}
// 获取鼠标位置
function getMousePos(e) {

	var mouseX = e.clientX+document.body.scrollLeft;//鼠标x位置
	var mouseY = e.clientY+document.body.scrollTop;//鼠标y位置
	cellDetailInfo.css("margin-left",mouseX);
	cellDetailInfo.css("margin-top",mouseY);
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
	// 刷新表头时间不会变
//	table.bootstrapTable('refresh');
	initTable();
}
