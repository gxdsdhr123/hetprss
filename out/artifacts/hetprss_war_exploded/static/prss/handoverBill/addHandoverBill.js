var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

var table = $('#createDetailTable');

var searchTime = "";
var searchData = "";
var clickRow;
var sign = "";
var selectRow = null;
$(function() {
	// 初始标识
	sign = $('#sign').val();
	// 初始化表格
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();
	
	// 初始化下拉选
	initSelect();
	// 初始化时间
	if("" == $('#flightDate').val()) {		
		$('#flightDate').val(getOperaDate('flight'));
	}
	if("" == $('#createDate').val()) {
		$('#createDate').val(getOperaDate('create'));
	}
	// 强制初始化下拉选滚轮样式
	initScroll();

	
	// 光标离开触发
	$('#flightNumber').blur(function() { 
		getLargePieceData();
	});
	// 回车触发
    $('#flightNumber').on('keypress',function(event){ 
        if(event.keyCode == 13) {  
        	getLargePieceData();
        }  
    });
    // 修改界面
    if("" != $('#paramModify').val() && null != $('#paramModify').val()) {
    	$('#flightNumber').prop('disabled', true);
    	$('#flightDate').prop('disabled', true);
    	// 选择下拉内容
    	var operator = $('input[name=operator]').val();
		$("#operList").val(operator).trigger('change');
		
//		$("#operList").prop('disabled', true);
		$("#createDate").prop('disabled', true);
    }
    
});

// 删除一行
function removeRow(i) {
	if ($("#createDetailTable").bootstrapTable('getData').length > 1) {
		var uniqueid = $(i).parents("tr").data("uniqueid");
		$("#createDetailTable").bootstrapTable('removeByUniqueId', uniqueid);
	}
}


//添加一行
function addEmptyRow() {
	var length = table.bootstrapTable("getData").length;
	var data = [];
	var row = {};
	var columns = createDetailColumns[0];
	if (length == 0) {
		data.push(addEmptyData(row, columns, 0));
	} else {
		var tableData = table.bootstrapTable("getData");
		var tbody = $('tbody', table);
		data = dealRowToHold(columns, 'add', tableData, tbody, null);
		// 增加一行空数据
		data.push(addEmptyData(row, columns, data.length));
	}
	// 增加行与数据
	table.bootstrapTable('append', row);
	table.bootstrapTable('load', data);
	// 重新初始化下拉选
	initScroll();
}
//填充空数据
function addEmptyData(row, columns, index) {
	for (var j = 0; j < columns.length; j++) {
		row[columns[j].field] = null;
		if("SEQ" == columns[j].field) {
			row[columns[j].field]= index;
		}
	}
	return row;
}

//获取保持数据
function dealRowToHold(columns, param, tableData, tbody, selected) {
	
	var index = 0;
	var data = [];
	$('tr', tbody).each(function(i, e) {		
		var row1 = {};
		var td = $('td', e);
		var flag = true;
		
			for(var j = 0; j < td.length; j++) {
				var field = columns[j].field;
				// 保持状态
				if(field == "OPA") {
					if(tableData[i][field] == null) {							
						row1[field] = '<span></span>';
					} else { 
						row1[field] = tableData[i][field];
					}
				} else {
					if(field == 'SEQ') { // 处理SEQ
						row1[field] = i;
					} else {
						if($(td[j]).find('input').length != 0) {							
							row1[field] = $(td[j]).find('input').val();
						} else {							
							row1[field] = tableData[i][field];
						}
					}
				}
			}
			data.push(row1)
	});
	return data;
}
//改变时间触发加载数据
function changeSelectTime() {
	var flightNumber = $('#flightNumber').val();
	if("" == flightNumber.trim() || null == flightNumber) {
		return false;
	}
	var flightDate = $('#flightDate').val();
	if("" == flightDate.trim() || null == flightDate) {
		clearAnbormalForm();
		searchTime = "";
		return false;
	}
	if(searchTime == flightDate) {
		return false;
	} else {
		searchData = "";		
	}
	getLargePieceData();
}

// 获取输入数据
function getLargePieceData() {
	var flightNumber = $('#flightNumber').val();
	if("" == flightNumber.trim() || null == flightNumber) {
		clearAnbormalForm();
		return;
	}
	var flightDate = $('#flightDate').val();
	if("" == flightDate.trim() || null == flightDate) {
		clearAnbormalForm();
		return;
	}
	if(searchData == flightNumber) {
		return;
	}
	var inn = layer.load(2);
	$.ajax({
		type : 'post',
		url : ctx + "/handoverBill/bill/getAirFligthInfo",
		data : {
			flightNumber : flightNumber,
			flightDate : flightDate,
			sign : $('#sign').val()
		},
		success : function(msg) {
			searchData = flightNumber;
			searchTime = flightDate;
			var list = eval(msg);
			if(list.length != 0) {
				dealAbnormalFlightData(list[0]);
			} else {
				clearAnbormalForm();
			}
			layer.close(inn);
		}
	});
}

//清空赋值
function clearAnbormalForm() {
	// 清空表单
	$('.content-c').each(function(i, e) {
		$(e).val('');
	});
}
//表单赋值
function dealAbnormalFlightData(data) {
	 for(var o in data){  
		$('input[id=' + o + "]").val(data[o]);
		if("ACTSTANDCODE" == o) {
			$('input[name=actstandCode]').val(data[o]);
		}
	 }  
}


function initTable() {

	createDetailColumns = [];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/handoverBill/bill/getTableHeader/mainAdd/"+sign,
		data : {pageSign: "mainAdd"},
		dataType : "json",
		success : function(result) {
			
			getDetailColumns(result);
		}
	});
}

function getDetailColumns(heaerPart) {
	var selectList = eval(heaerPart.selectList);
	var headerList = eval(heaerPart.headerList);
	var receiverList = eval(heaerPart.receiverList);
	
	var createRow1 = [];
	var createRow1 = [{
		title : "序号",
		field : "SEQ",
		width : 5 + '%',
		valign: "middle",
		formatter: function (value, row, index) {  
			return index+1;  
        }  
	}];
	if(headerList.length != 0) {
		var colsArr = headerList;
		for(var i in colsArr) {
			var field = colsArr[i].CODE;
			var title = colsArr[i].TITLE;
			if(field == 'INC_GOODS') {
				createRow1.push({
					'field' : field,
					'title' : title,
					'align' : 'center',
					valign: "middle",
					editable : {
//						disabled : true,
						type : "select",
						title : title,
//						onblur : "ignore",
						source : selectList
			        }
				});
			} else if("SIGNATORY" == field){	// 交接人			
				createRow1.push({
					'field' : field,
					'title' : title,
					'align' : 'center',
					valign: "middle",
                    formatter: function(value,row,index){
                    	if(null != value) {    
                    		return '<img src="${ctx }/passengerCar/operationRecord/outputPicture?id=' + value + '" height=40px width=80%/>'
                    	}
                    }
				});
			} 
//			else if(field.indexOf("DATE") > 0) {
//									
//					createRow1.push({
//						'field' : field,
//						'title' : title,
//						'align' : 'center',
//						'valign' : "middle",
//						'formatter' : function(value, row, index) {
//							if('RECEIVERDATE' == field) {
//								return "<a href='javascript:void(0)' id='receiverdate_" + index + "'>" + (value ? value : '请选择') + "</a>";	
//							} 
//						}
//					});
//				
//			}
			else if('RECEIVERDATE' == field) {
				createRow1.push({
					'field' : field,
					'title' : title,
					'align' : 'center',
					'valign' : "middle",
					'formatter' : function(value, row, index) {
						if('RECEIVERDATE' == field) {
							return "<a href='javascript:void(0)' id='receiverdate_" + index + "'>" + (value ? value : '请选择') + "</a>";	
						} 
					}
				});
			} else if('SIGNATORYDATE' == field) {
				createRow1.push({
					'field' : field,
					'title' : title,
					'align' : 'center',
					'valign' : "middle",
					'formatter' : function(value, row, index) {
						if('RECEIVERDATE' == field) {
							return "<a href='javascript:void(0)' id='signdate_" + index + "'>" + (value ? value : '请选择') + "</a>";	
						} 
					}
				});
			}
			
			else if("RECEIVER" == field) {
				createRow1.push({
					'field' : field,
					'title' : title,
					'align' : 'center',
				    'class' : 'select2',
				    valign: "middle",
					editable: {
	//					disabled : true,
						type : "select2",
						title : title,
						onblur : "ignore",
						source : receiverList,
						select2:{
							width : "100%",
							language : "zh-CN"
							
						}
			        }
				});
			}
			
			else {
				createRow1.push({
					'field' : field,
					'title' : title,
					'align' : 'center',
					valign: "middle",
					editable : true
				});
			}
		}
	}
	createRow1.push({
		'field' : 'OPA',
		'title' : '操作',
		'align' : 'center',
		'width' : 5 + '%',
		valign: "middle",
		'formatter' : function(value, row, index) {
			if(null == value) {				
				return "<i class='fa fa-plus-circle' onclick='addEmptyRow();' style='cursor: pointer;'></i>";
			} else {				
				return "<i class='fa fa-minus-circle' onclick='removeRow(this)' style='cursor: pointer;'></i>";
			}
		}
	});
	
	
	createDetailColumns.push(createRow1);
	initTableDate();
}

function initTableDate() {
	
	// 针对快速连续点击样式变形问题
	$('div[class=mark_c]').attr({'style': 'display: block;'});
	var createDetailOption = {
		method : 'get',
		striped : true,
	    dataType: "json",
	    uniqueId : "SEQ",
		undefinedText : '', // undefined时显示文本
		queryParams : function(params) {
			var param = {
				pageSign : 'mainAdd',
				searchId : $('#id').val()
			};
			return param;
		},
		url : ctx + "/handoverBill/bill/getGridData/"+$('#sign').val(),
		columns : createDetailColumns,
		onEditableSave : function(field, row, oldValue, $el) {
		},
		onClickRow : function onClickRow(row, tr, field) {
			searchId = row.ID;
			selectRow = row.SEQ;
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			searchId = row.ID;
		},
		onClickCell : function onClickCell(field, value, row, $element) {
			$(".clickRow").removeClass("clickRow");
			selectRow = row.SEQ;
			if('RECEIVERDATE' == field || 'SIGNATORYDATE' == field) {
				popupDate(field, value, row, $element);
			}
		},
		onLoadSuccess : function(data) {
			
			addEmptyRow();
			
			setTimeout(function() {
				$('div[class=mark_c]').attr({'style': 'display: none;'});
			}, 600);
		}
	};
	createDetailOption.height = $(window).height() * 0.78;
	table.bootstrapTable(createDetailOption);

}


// 重置
function reset() {
	var data = [];
	var row = {};
	var columns = createDetailColumns[0];
	data.push(addEmptyData(row, columns, 0));
	table.bootstrapTable('load', data);
	if('modify' != $('#paramModify').val()) {
		clearAnbormalForm();
		$('#flightNumber').val('');		
		$('#flightDate').val(getOperaDate('flight'));
		$("#operList").val('').trigger('change');
	}
}
//未触发框架保存  提交数据
function checkBootData() {
	var data = [];
	var row = {};
	var columns = createDetailColumns[0];
	var tableData = table.bootstrapTable("getData");
	var tbody = $('tbody', table);
	data = dealRowToHold(columns, 'add', tableData, tbody, null);
	table.bootstrapTable('load', data);
}

// 获取表单数据
function getFormData() {
	checkBootData();
	var tableData = table.bootstrapTable("getData");
	$('#data').val(JSON.stringify(tableData));
	return $("#handoverBill").serialize();
}

// 弹出消息
function alertInfo(msg) {
	layer.msg(msg, {icon : 7, time : 1500});
}

// 检查时间
function checkFlightDate() {
	
	if(null == $('#flightDate').val() || "" == $('#flightDate').val()) {
		alertInfo("请选择日期");
		return false;
	}
	if(null == $('#createDate').val() || "" == $('#createDate').val()) {
		alertInfo("请选择操作时间");
		return false;
	}
	return true;
}
// 检查航班数据
function checkAirFlightData() {
	if(null == $('#FLTID').val() || '' == $('#FLTID').val()) {
		if("in" == sign) {			
			alertInfo("请填写航班号");
		} else if("out" == sign){			
			alertInfo("请填写航班号");
		}
		return false;
	}
	return true;
}
// 检查查理
function checkCharline() {
	if(null == $('#operList').val() || '' == $('#operList').val()) {
		alertInfo("请选择查理");
		return false;
	}
	return true;
}
// 选择司机，要填写日期
function checkReceiverDate() {
	var tableData = table.bootstrapTable("getData");
	for(var o in tableData) {
		if(null != tableData[o].RECEIVER && null == tableData[o].RECEIVERDATE){			
			if("in" == $('#sign').val()) {
				alertInfo("请输入货运司机接收时间");
			} else if("out" == $('#sign').val()) {
				alertInfo("请选择运输司机接收时间");				
			}
			return false;
		}
		if(null == tableData[o].RECEIVER && null != tableData[o].RECEIVERDATE){		
			if("in" == $('#sign').val()) {
				alertInfo("请选择货运司机");
			} else if("out" == $('#sign').val()) {
				alertInfo("请选择运输司机");				
			}
			return false;
		}
	}
	return true;
}
// 检查是否存在相同的代码
function checkCollCode() {
	var data = [];
	var tableData = table.bootstrapTable("getData");
	for(var o in tableData) {
		var value = tableData[o].COLL_CODE;
		
		if($.inArray(value, data) != -1) {
			if("in" == $('#sign').val()) {
				alertInfo("存在相同的集装器/散装斗代码");
			} else if("out" == $('#sign').val()) {
				alertInfo("存在相同的非机动设备号");				
			}
			return false;
		}
		data.push(value);
		
	}
	return true;
}



// 检查数据是否合法
function checkFormData() {
	checkBootData();
	var columns = createDetailColumns[0];
	var tableData = table.bootstrapTable("getData");
	var tbody = $('tbody', table);
	var index = 0;
	for(var o in tableData) {
		var field = tableData[o];
		var collCode = field['COLL_CODE'];
		index++;
		if(null == collCode && null == field['INC_GOODS']) {
			continue;
		}
		for(var f in field) {
//			if(null != collCode) {
				var value = field[f];
				// 代码长度限制
				if('COLL_CODE' == f) {
					if(null != value) {
						if(collCode.length >= 90) {
							if("in" == sign) {
								alertInfo("序号为"+index+"的 集装器/散斗代码内容过长");							
							} else if("out" == sign) {
								alertInfo("序号为"+index+"非 设备机动号内容过长");														
							}
							return false;
						}
					}
				}
				// 内含不为空
				if('INC_GOODS' == f) { 
					if(null == value) {
						if("in" == sign) {
							if(null == collCode) {
								alertInfo("请选择序号为  " + index + " 的 内含");							
							} else {								
								alertInfo("请选择 集装器/散斗代码为  " + collCode + " 的 内含");							
							}
						} else if("out" == sign) {
							if(null == collCode) {								
								alertInfo("请选择 序号为 为  " + index + " 的 类型");														
							} else {
								alertInfo("请选择 非设备机动号 为  " + collCode + " 的 类型");														
							}
						}
						return false;
					}
				}
				// 备注限制长度
				if('REMARK' == f) {
					if(null != value) {
						if(value.length >= 90) {
							if("in" == sign) {
								if(null == collCode) {									
									alertInfo("序号为  " + index + "的 备注内容过长");							
								} else {
									alertInfo("集装器/散斗代码为  " + collCode + "的 备注内容过长");							
								}
							} else if("out" == sign) {
								if(null == collCode) {
									alertInfo("序号 为  " + index + "的 备注内容过长");																							
								} else {
									alertInfo("非设备机动号 为  " + collCode + "的 备注内容过长");																							
								}
							}
							return false;
						}
					}
				}
				// 重量
				if('WEIGHT' == f) {
					if(null != value) {
						var reg = /^[0-9]*$/;
						if("out" == sign) {
							if(value.length >= 13 || !reg.test(value)) {									
								if(null == collCode) {
									alertInfo("序号 为 " + index + "的 重量不符合规则");														
								} else {									
									alertInfo("非设备机动号 为 " + collCode + "的 重量不符合规则");														
								}
								return false;
							}
						}
					}
				}
//			}
		}
	}
	return true;
}

	


//监听操作员改变
$('#operList').on('change', function(){
	$('input[name=operatorName]').val($('#operList').find("option:selected").text());
	$('input[name=operator]').val($('#operList').find("option:selected").val());
});

//强制改变滚动条样式
function initScroll() {
	var tbody = $('tbody', table);
	$('tr', tbody).each(function(i, e) {
		$('.select2', e).on("click",function() {
			$('.select2-results__options').each(function(){
				$(this).css('position' , 'relative');
				var s = new PerfectScrollbar(this);
				scrollArr.push(s);
				if(this.id)
					scrollObjs[this.id] = s;
			});
		});
	});
}
//初始化下拉选
function initSelect() {
	$('#operList').select2({
		placeholder : "请选择查理",
		width : "100%",
		language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
	});
}

function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}

/**
 * 弹出日期控件
 * @param field
 * @param value
 * @param row
 * @param td
 */
function popupDate(field, value, row, $element){
	var aElem = $element.find("a:eq(0)");
	WdatePicker({
		startDate:value,
		errDealMode:1,
		el:aElem.attr("id"),
		enableKeyboard:false,
		qsEnabled:false,
		isShowClear:false,
		isShowOthers:false,
		autoUpdateOnChanged:false,
		dateFmt:"yyyy-MM-dd HH:mm:ss",
		onpicked : function(dp) {
			var newValue = dp.cal.getDateStr();
			if (newValue) {
				row[field] = newValue;
			}
		}
	});
}
//	获取日期， 如果日期为空则默认当前时间
function getOperaDate(param) {
	var date = new Date();
	var year = date.getFullYear();
	var mon = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+1) : (date.getMonth()+1);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	var min = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
	if('flight' == param) {		
		daystr = '' +  year  +  mon  + day;
	}
	if('create' == param) {
		daystr = '' +  year + "-" +  mon + "-" + day + " " + hour + ":" + min + ":" + second;
	}
	
	return daystr;
}
window.onload = function() {
	// 取消遮罩
	setTimeout(function() {
		$('div[class=mark_c]').attr({
			'style' : 'display: none;'
		});
	}, 600);

}