var page = 1;
var limit = 100;
var tableData;
var baseTable;

var SHOW_TYPE_CHART = 1;
var SHOW_TYPE_STYLE = 2;
var watch; // 每分钟刷新数据，存储定时器

$(function() {
	
	$("html,body").css("cssText","100% !important");
	var layer;
	var clickRowId = "";// 当前单选行id，以便工具栏操作
	baseTable = $("#baseTable");
	layui.use([ 'form', 'layer' ], function() {// 调用layui表单及弹出层组件
		layer = layui.layer;
	});
	
	$("#baseTable").each(function() {
		$(this).on('load-success.bs.table', function(thisObj) {
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody[0].removeEventListener('ps-y-reach-end', load);
			tableBody[0].addEventListener('ps-y-reach-end', load);
		});
	});
	
	// 基本表格选项
	var tableOptions = {
		url : ctx + "/flightdynamic/fltdynmonitor/init", // 请求后台的URL（*）
		method : "get", // 请求方式（*）
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : false, // 是否显示全选
		// toolbar : $("#tool-box"), // 指定工具栏dom
		search : false, // 是否开启搜索功能
		editable : false,//开启编辑模式
		searchOnEnterKey : true,
		// silent : true, //刷新事件必须设置
		responseHandler : function(res) {
			tableData = res;
			return res.slice(0, limit);
		},
		columns : columns(true),
		onDblClickCell : onDblClickCell,// 双击选定行
		onClickRow : onClickRow,// 单击选定行
		onClickCell : function(field, value, row, $element) {
			
		},
		onLoadSuccess : function(data) {
			watch = setInterval(function() {
				refreshTable();
			}, 60000);
		}
	};
	
	function onClickRow(row, tr) {
		// 记录单选行id，并赋予天蓝色底纹
		clickRowId = row.ID;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
	
	tableOptions.height = $("body").height() ;// 表格适应页面高度
	$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
	//双击事件
	function onDblClickCell(field, value, row, td) {
		
	}

	// 全选
	$("#selectAll").click(function() {
		// 如果全选 取消全选
		if ($('#baseTable').bootstrapTable('getSelections').length == $('#baseTable').bootstrapTable('getData').length) {
			$('#baseTable').bootstrapTable('uncheckAll');
			$("#selectAll").text("全选");
		} else {
			$('#baseTable').bootstrapTable('checkAll');
			$("#selectAll").text("取消全选");
		}
	})
})

function load() {
	var start = page * limit;
	if (start < tableData.length) {
		var end = 0;
		if (tableData.length > (page + 1) * limit) {
			end = (page + 1) * limit;
		} else {
			end = tableData.length;
		}
		baseTable.bootstrapTable('append', tableData.slice(start, end));
		var pos = baseTable.bootstrapTable('getScrollPosition');
		baseTable.bootstrapTable('scrollTo', pos + 100);
		page++;
	}
}

function refreshTable(){
	clearInterval(watch);
	$("#baseTable").bootstrapTable('refresh');
}


function columns(visible){
	return  [ // 列设置
		{
			field : "checkbox",
			checkbox : true,
			sortable : false,
			title : ""
		}, {
			field : "order", // 数据属性名
			title : "序号", // 表格列头
			sortable : false, // 是否开启排序
			editable : false, // 是否可编辑
			formatter : function(value, row, index) { // 自定义格式
				return index + 1;
			}
		}, {
			field : "FLT_NO",
			title : "航班号",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		},{
			field : "COL1",
			title : "机位",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		},{
			field : "GATE",
			title : "登机口",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		},{
			field : "ETA",
			title : "预达时间",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		},{
			field : "ATA",
			title : "实落时间",
			formatter : function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		},{
			field : "COL2",
			title : "值机开始",
			formatter :function(value, row, index) {
				var condition = "0";
				value = value ? value : '';
				if(row.COL2_STATUS=="1"&&value=="") {
					if(row.COL2_ST<=row.COL2_ETS) {
						condition = "1";
					} else {
						condition = "2";
					}
				} else if(row.COL2_STATUS=="1"&&value!="") {
					condition = "3";
				} else if(row.COL2_STATUS!="1"&&value!="") {
					value = "";
				}
				return getReturnValue("COL2" + row.ID, condition, value, SHOW_TYPE_CHART);
			},
			class : isClassWhite()
		},{
			field : "STA",
			title : "值机结束",
			formatter : function(value, row, index) {
				var condition = "0";
				value = value ? value : '';
				if(row.COL2_STATUS=="1"&&value!="") {
					if(value<=row.COL2_ETE) {
						condition = "3";
					} else {
						condition = "4";
					}
				} else if(row.COL2_STATUS!="1"&&value!="") {
					value = "";
				}
				return getReturnValue("STA" + row.ID, condition, value, SHOW_TYPE_CHART);
			},
			class : isClassWhite()
		},{
			field : "COL3",
			title : "值机/过检/登机",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "COL4",
			title : "预计登机",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "COL5",
			title : "实际登机开始",
			formatter :function(value, row, index) {
				var condition = "0";
				value = value ? value : '';
				if(row.COL4_STATUS=="1"&&value=="") {
					if(row.COL4_ST<=row.COL4) {
						condition = "1";
					} else {
						condition = "2";
					}
				} else if(row.COL4_STATUS=="1"&&value!="") {
					condition = "3";
				} else if(row.COL4_STATUS!="1"&&value!="") {
					value = "";
				}
				return getReturnValue("COL5" + row.ID, condition, value, SHOW_TYPE_CHART);
			},
			class : isClassWhite()
		}, {
			field : "COL6",
			title : "登机结束",
			formatter : function(value, row, index) {
				var condition = "0";
				value = value ? value : '';
				if(row.COL4_STATUS=="1"&&value!="") {
					if(value<=row.COL4_ETE) {
						condition = "3";
					} else {
						condition = "4";
					}
				} else if(row.COL4_STATUS!="1"&&value!="") {
					value = "";
				}
				return getReturnValue("COL6" + row.ID, condition, value, SHOW_TYPE_CHART);
			},
			class : isClassWhite()
		}, {
			field : "COL7",
			title : "关舱门",
			formatter : function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "COL8",
			title : "关货舱门",
			formatter : function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "COL9",
			title : "加油完毕",
			formatter : function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "EOBT",
			title : "预计撤轮挡",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "DOBT",
			title : "目标撤轮挡",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "AOBT",
			title : "实际撤轮挡时间",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "TSAT",
			title : "目标同意开车时间",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "QYC",
			title : "牵引车",
			formatter :function(value, row, index) {
				var condition = "0";
				value = value ? value : '';
				if(row.QYC_STATUS=="1"&&value=="") {
					if(row.QYC_ST<=row.QYC_ETS) {
						condition = "1";
					} else {
						condition = "2";
					}
				} else if(row.QYC_STATUS=="1"&&value!="") {
					condition = "3";
				} else if(row.QYC_STATUS!="1"&&value!="") {
					value = "";
				}
				return getReturnValue("QYC" + row.ID, condition, value, SHOW_TYPE_CHART);
			},
			class : isClassWhite()
		}, {
			field : "JW",
			title : "机务",
			formatter :function(value, row, index) {
				var condition = "0";
				value = value ? value : '';
				if(row.JW_STATUS=="1"&&value=="") {
					if(row.JW_ST<=row.JW_ETS) {
						condition = "1";
					} else {
						condition = "2";
					}
				} else if(row.JW_STATUS=="1"&&value!="") {
					condition = "3";
				} else if(row.JW_STATUS!="1"&&value!="") {
					value = "";
				}
				return getReturnValue("JW" + row.ID, condition, value, SHOW_TYPE_CHART);
			},
			class : isClassWhite()
		}, {
			field : "TC",
			title : "推出",
			formatter :function(value, row, index) {
				var condition = "0";
				value = value ? value : '';
				if(row.JW_STATUS=="1"&&row.QYC_STATUS=="1"&&row.QYC!=""&&row.JW!="") {
					if(value<=row.TC_ETS) {
						condition = "3";
					} else {
						condition = "4";
					}
				} else if((row.JW_STATUS!="1"||row.QYC_STATUS!="1")&&row.QYC!=""&&row.JW!=""&&value!="") {
					value = "";
				}
				return getReturnValue("TC" + row.ID, condition, value, SHOW_TYPE_CHART);
			},
			class : isClassWhite()
		}, {
			field : "ETD",
			title : "预计起飞",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "CTOT",
			title : "计算起飞",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "COL10",
			title : "预计放行",
			formatter : function(value, row, index) {
				return (value ? value : '')
			},
			class : isClassWhite()
		}, {
			field : "ATD",
			title : "实际起飞",
			class : isClassWhite()
		}, {
			field : "COL11",
			title : "放行延误",
            formatter : function(value, row, index) {
            	
            	var condition = row.COL10;
            	value = value ? value : '';
            	
            	// 将延误时间转化为时间格式
            	var hours = condition.substring(0, 2);
            	var min = condition.substring(2, 4);
            	var day = new Date();
            	day.setHours(hours);
            	day.setMinutes(min);
            	var newDay = new Date(day.getTime() + value*60*1000);
            	hours = newDay.getHours();
            	min = newDay.getMinutes();
				if (hours >= 0 && hours <= 9) {
					hours = "0" + hours;
				}
				if (min >= 0 && min <= 9) {
					min = "0" + min;
				}
            	
            	condition = "" + hours + min; // 计算延误具体时间
            	
				return ((value > "0") ? getReturnValue("COL11" + row.ID, value, condition, SHOW_TYPE_STYLE) : '')
			},
			/*cellStyle : function(value, row, index){    
	            if (value > "0"){    
	                return {css:{"background-color":"red"}}    
	            }   
			},*/
			class : isClassWhite()
		} ];
}

function isClassWhite() {
	var cla = "color-white";
	return cla;
}

// id 替换元素
// condition 替换值（旧值）
// value 替换值（新值）
// type 替换表格内容为图形或文字
function getReturnValue(id, condition, value, type) {
	
	// 如果没有值设置为空格，占div的空间
	if(value==""){
		value = "&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	
	var tag = "<div id='" + id 
		+ "' onmouseover='mouseOver(\"#" + id + "\", \"" + value + "\"" 
		+ ");' onmouseout='mouseOut(\"#" + id + "\", \"" + condition + "\", " + type + ");'>" 
		+ setShowIcon(condition, type) + "</div>";
	
	if(type==SHOW_TYPE_STYLE) {
		tag = "<div style='background-color:red;' id='" + id 
		+ "' onmouseover='mouseOver(\"#" + id + "\", \"" + value + "\"" 
		+ ");' onmouseout='mouseOut(\"#" + id + "\", \"" + condition + "\", " + type + ");'>" 
		+ setShowIcon(condition, type) + "</div>";
	}
	return tag;
}


var t; //延迟方法传参
function mouseOver(id, value) {
	$(id).html(value);
	t = setInterval(function(){	
	}, 100);
}

function mouseOut(id, condition, type) {
	clearInterval(t);
	$(id).html(setShowIcon(condition, type));
}

function setShowIcon(condition, type) {
	
	var content; // 替换内容，页面初始化加载
	if(type==SHOW_TYPE_CHART) { // 类型为替换图片
		switch(condition) {
		case "1":
			content = "<div class='circle-progress-red'></div>";
			break;
		case "2":
			content = "<div class='circle-progress-green'></div>";
			break;
		case "3":
			content = "<div class='circle-red'></div>";
			break;
		case "4":
			content = "<div class='circle-green'></div>";
			break;
		default:
			content = "&nbsp;&nbsp;&nbsp;&nbsp;";
		}
	} else if(type==SHOW_TYPE_STYLE) { // 类型为替换文字
		content = condition;
	}
	
	return content;
}