var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

var searchTime = "";
var searchData = "";
var clickRow;
var table = $('#createDetailTable');
var createDetailColumns = [];
var selectRow = null;


$(function() {
	
	// 初始化表格
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();
	initScroll();
	// 初始化操作员下拉选
	initSelect();
	// 初始化时间
	if("" == $('#flightDate').val()) {		
		$('#flightDate').val(getOperaDate());
	}
	// 新增一行
	$('#addRow').click(function() {
//		addEmptyRow();
//		initScroll();
		var index = $(".firstTr:last").find("td:first").text();
		var row ="<tr class='firstTr'>                                "+
		"		<td rowspan=3 width=7%>"+(parseInt(index)+1)+"</td>      "+
		"		<td rowspan=3 width=7%><input type='text' class='deviceNo' name='deviceNo' /></td>      "+
		"		<td rowspan=3 width=7%></td>      "+
		"		<td rowspan=3 width=7%></td>      "+
		"		<td rowspan=3 width=7%></td>      "+
		"		<td width=7% height=40px><input type='checkbox' class='WG'/>外观</td>    "+
		"		<td width=7% height=40px><input type='checkbox' class='HB'/>护板</td>    "+
		"		<td width=7% height=40px><input type='checkbox' class='SJ'/>升降</td>    "+
		"		<td rowspan=3 width=7%></td>      "+
		"		<td rowspan=3 width=7%></td>      "+
		"		<td rowspan=3 width=7%></td>      "+
		"		<td rowspan=3 width=7%>" +
		"			<textarea class='remark' name='remark' style='width:90px;background-color:#002f63;height:120px;border:1px solid #006dc0'></textarea>" +
		"		</td>      "+
		"	</tr>                               "+
		"	<tr>                                "+
		"		<td height=40px><input type='checkbox' class='DG'/>灯光</td>             "+
		"		<td height=40px><input type='checkbox' class='LT'/>轮胎</td>             "+
		"		<td height=40px><input type='checkbox' class='WS'/>卫生</td>             "+
		"	</tr>                               "+
		"	<tr>                                "+
		"		<td height=40px><input type='checkbox' class='ZD'/>制动</td>             "+
		"		<td height=40px><input type='checkbox' class='ZJ'/>支脚</td>             "+
		"		<td height=40px><input type='checkbox' class='MHQ'/>灭火器</td>             "+
		"	</tr>                               "+
		"	<tr>"+
		"		<td colspan=4 height=40px></td>"+
		"		<td>操作员：</td>"+
		"		<td class='selectTd'>" +
		"			<select name='operator' class='select2 form-control operList' data-type='operList' >"+
		"				<option value=''></option>"+
//		"				<c:forEach items='${operList}' var='office'>"+
//		"					<option value='${office.ID}' <c:if test='${office.ID == car.OPERATOR}'>checked</c:if>>${office.NAME}</option>"+
//		"				</c:forEach>"+
		"			</select>" +
		"		</td>"+
		"		<td>取车地：</td>"+
		"		<td><input type='text' class='QCD' name='QCD' value='' style='width:80px'/></td>"+
		"		<td>送车地：</td>"+
		"		<td><input type='text' class='SCD' name='SCD' value='' style='width:80px'/></td>"+
		"		<td>报送时间：</td>"+
		"		<td></td>"+
		"	</tr>";
		$("tbody").append(row);
		$.ajax({
			type : 'post',
			url : ctx + "/passengerCar/operationRecord/getOperList",
			success : function(msg) {
				$.each(msg,function(k,v){
					$(".operList:last").append("<option value="+v.ID+">"+v.NAME+"</option>");
				})
			}
		});
		$(".operList").select2({
			placeholder : "请选择操作员",
			width : "100%",
			language : "zh-CN"
		});
	});
	// 删除一行
	$('#delRow').click(function() {
//		if(null == selectRow) {
//			layer.msg("请点击序号进行删除" , {icon : 7, time : 600});
//		} else {
//			delRow();
//		}
		$("tbody").find("tr:last").remove();
		$("tbody").find("tr:last").remove();
		$("tbody").find("tr:last").remove();
		$("tbody").find("tr:last").remove();
	});
	
	// 光标离开触发
	$('#flightNumber').blur(function() { 
		getAirFlightInfo();
	});
	// 回车触发
    $('#flightNumber').on('keypress',function(event){ 
        if(event.keyCode == 13) {  
        	getAirFlightInfo();
        }  
    });

    // 修改界面
    if("" != $('#paramModify').val() && null != $('#paramModify').val()) {
    	// 禁止操作分
    	$('.modify-display').each(function(i, e) {
    		$(e).prop('disabled', true);
    	});
    	// form默认选择
    	var operator = $('#operator').val();
    	$("#operList").val(operator).trigger('change');
    }
});

//改变时间触发加载数据
function changeSelectTime() {
	var flightNumber = $('#flightNumber').val();
	if("" == flightNumber.trim() || null == flightNumber) {
		return false;
	}
	var flightDate = $('#flightDate').val();
	if("" == flightDate.trim() || null == flightDate) {
		clearAirFlightData();
		searchTime = "";
		return false;
	}
	if(searchTime == flightDate) {
		return false;
	} else {
		searchData = "";		
	}
	getAirFlightInfo();
}
//获取输入数据
function getAirFlightInfo() {
	var flightNumber = $('#flightNumber').val();
	if("" == flightNumber.trim() || null == flightNumber) {
		clearAirFlightData();
		return;
	}
	var flightDate = $('#flightDate').val();
	if("" == flightDate.trim() || null == flightDate) {
		clearAirFlightData();
		return;
	}
	if(searchData == flightNumber) {
		return;
	}
	var inn = layer.load(2);
	$.ajax({
		type : 'post',
		url : ctx + "/passengerCar/operationRecord/getAirFligthInfo",
		data : {
			flightNumber : flightNumber,
			flightDate : flightDate
		},
		success : function(msg) {
			searchData = flightNumber;
			searchTime = flightDate;
			var list = eval(msg);
			if(list.length != 0) {
				dealAirFlightData(list[0]);
			} else {
				clearAirFlightData();
			}
			layer.close(inn);
		}
	});
}
// 赋值
function dealAirFlightData(data) {
	 for(var o in data){  
		$('input[id=' + o + "]").val(data[o]);
	 }  
}
// 清空
function clearAirFlightData() {
	$('.content-c').each(function(i, e) {
		$(e).val('');
	});
}
// 添加一行
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
		data = dealRowToHold(columns, 'add', tableData, tbody);
		// 增加一行空数据
		data.push(addEmptyData(row, columns, data.length));
	}
	// 增加行与数据
	table.bootstrapTable('append', row);
	table.bootstrapTable('load', data);
}
// 填充空数据
function addEmptyData(row, columns, index) {
	for (var j = 0; j < columns.length; j++) {
		row[columns[j].field] = null;
		if("SEQ" == columns[j].field) {
			row[columns[j].field]= index;
		}
	}
	return row;
}

// 获取保持数据
function dealRowToHold(columns, param, tableData, tbody) {
	
	
	var index = 0;
	var data = [];
	$('tr', tbody).each(function(i, e) {
		var row1 = {};
		var td = $('td', e);
		var flag = true;
		
		if(param == 'del') {
			if(null != selectRow) {				
				if(i == selectRow) {
					flag = false;
				}
			}
		} else {
			flag = true;
		}
		if(flag) {
			for(var j = 0; j < td.length; j++) {
				var field = columns[j].field;
				
				// 保持选中状态
				if($('span', td[j]).length != 0) {
					if(field != 'DEVICE_NO' && field != 'qcd' && field != 'scd') {							
						row1[field] = '<span class="glyphicon glyphicon-ok" data-value="'+columns[j].field+'"></span>';
					} else { // 针对点击车辆编号却不选择新增值空问题
						if(field == 'qcd') {
							row1[field] = $('input', td[j]).val();
						} else if(field == 'scd') {
							row1[field] = $('input', td[j]).val();
						} else {							
							row1[field] = tableData[i][field];
						}
					}
				} else {
					if(field == 'SEQ'  ) { // 处理SEQ
						
						row1[field] = index++;
					} else {
						row1[field] = tableData[i][field];
					}
				}
			}
			data.push(row1)
		}
	});
	return data;
}
// 删除一行
function delRow() {
	var tbody = $('tbody', table);
//	table.bootstrapTable('removeByUniqueId', selectRow);
	// 删完新增
	var tableData = table.bootstrapTable("getData");
	var length = tableData.length;
	var data = [];
	var row = {};
	var columns = createDetailColumns[0];
	if (length == 1) { // 全部删完处理新增一条
		
		data.push(addEmptyData(row, columns, 0));
		table.bootstrapTable('append', row);
		table.bootstrapTable('load', data);
	} else { // 未保存的数据删除保持选中状态
		data = dealRowToHold(columns, 'del', tableData, tbody);
		table.bootstrapTable('load', data);
	}

	selectRow = null;
}






function initTable() {

	createDetailColumns = [];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/passengerCar/operationRecord/getTableHeader",
		data : {pageSign: "mainAdd"},
		dataType : "json",
		success : function(result) {
			
			getDetailColumns(result);
		}
	});
}

function getDetailColumns(heaerPart) {
	var carList = eval(heaerPart.carList);
	
	var headerList = heaerPart.headerList;
	var createRow1 = [{
		title : "序号",
		field : "SEQ",
	    width: '6' + '%',
		formatter: function (value, row, index) {  
			return index+1;  
        }  
	}, {
		title : '车辆编号<font class=required>*</font>',
		field : 'DEVICE_NO',
//		align : 'left',
	    width: 10 + '%',
	    class : 'select2',
		editable: {
//			disabled : true,
			type : "select2",
			title : "车辆编号",
			onblur : "ignore",
			source : carList,
			select2:{
				width : "100%",
				language : "zh-CN"
				
			}
        },
        formatter: function (value, row, index) {  
        	var str = String(value);
        	
			return value;
        } 
	
	}
	, {
		title : "取车地",
		field : "qcd",
		align : 'center',
		width : 10 + '%',
		editable : {
			type : 'text',
			title : '取车地',
			 validate: function (v) {
                 if (v.length > 27) return '输入内容过长';

             }
		},
		formatter: function (value, row, index) {  
			var s = String(value);
			if(s.length > 27) {
				return '';
			} else {
				return s;
			}
        } 
	}, {
		title : "停车地",
		field : "scd",
		align : 'center',
		width : 10 + '%',
		editable : {
			type : 'text',
			title : '停车地',
			 validate: function (v) {
                 if (v.length > 27) return '输入内容过长';

             }
		},
		formatter: function (value, row, index) {  
			var s = String(value);
			if(s.length > 27) {
				return '';
			} else {
				return s;
			}
        } 
	}
	];

	if(headerList.length != 0) {
		var colsArr = headerList;
		for(var i in headerList) {
			createRow1.push({
				'field' : colsArr[i].ITEM_CODE,
//				'field' : colsArr[i].ID,
				'title' : colsArr[i].ITEM_NAME,
				'align' : 'center'
			});
		}
	}
	
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
		url : ctx + "/passengerCar/operationRecord/getGridData",
		columns : createDetailColumns,
		onEditableSave : function(field, row, oldValue, $el) {
		},
		onClickRow : function onClickRow(row, tr, field) {
			searchId = row.ID;
			if("SEQ" == field || "DEVICE_NO" == field || "scd" == field || "qcd" == field) {
				selectRow = row.SEQ;
				$(".clickRow").removeClass("clickRow");
				$(tr).addClass("clickRow");
			}
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			searchId = row.ID;
		},
		onClickCell : function onClickCell(field, value, row, td, tr) {
			$(".clickRow").removeClass("clickRow");
			if("SEQ" != field && "DEVICE_NO" != field && "scd" != field && "qcd" != field) {	
				// 验证需选择车辆编号方可操作
				if(null == row.DEVICE_NO || '' ==  row.DEVICE_NO) {
					layer.msg("请先选择车辆编号" , {icon : 7, time : 600});
					return;
				}
				if($('span', td).length == 0) {
					$(td).append('<span class="glyphicon glyphicon-ok" data-value="'+field+'"></span>');					
				} else {
					$(td).empty();
				}
			}
		},
		onLoadSuccess : function(data) {
			if(null == data || '' == data) { // 添加空行
				addEmptyRow();
			} else { // 加载数据
				dealData(data);
			}			
			
			setTimeout(function() {
				$('div[class=mark_c]').attr({'style': 'display: none;'});
			}, 600);
		}
	};
//	createDetailOption.height = $(window).height() * 0.50;
	table.bootstrapTable(createDetailOption);
}
function dealData(data){
	
	var tbody = $('tbody', table);
	var index = "";
	var columns = createDetailColumns[0];
	var tableData = [];
	var row = {};
	 // 空数据为重置
	if(null == data) {
		tableData.push(addEmptyData(row, columns, 1));
		table.bootstrapTable('load', tableData);
		return;
	}
	
	$('tr', tbody).each(function(i, e) {
		index = i;
		var row1 = {};
		var td = $('td', e);
		var value1 = data[i];
		for(var j = 0; j < td.length; j++) {
			var field = columns[j].field;
			var f = '';
			var value = '';
			if('SEQ' != field && 'DEVICE_NO' != field) {
				f = field.toUpperCase();
				value = value1[f];
			} else {
				value = value1[field];
			}
			
			// 保持选中状态
			if(null != value) {
				if('SEQ' != field && 'DEVICE_NO' != field) {		
					if('qcd' == field || 'scd' == field) {
						row1[field] = value;
					} else {
						if(value == '1') {							
							row1[field] = '<span class="glyphicon glyphicon-ok" data-value="'+field+'"></span>';
						}
					}
				} else if(field == 'SEQ') {
					row1[field] = value;
				} else if(field == 'DEVICE_NO') {
					row1[field] = value;
				} 
			} else {
				row1[field] = null;
			}
		}
		tableData.push(row1)
	});
	table.bootstrapTable('load', tableData);
		
}
// 


//弹出消息
function alertInfo(msg) {
	layer.msg(msg , {icon : 7, time : 1000});
}


//检查时间
function checkFlightDate() {
	if(null == $('#flightDate').val()) {
		alertInfo("请选择日期");
		return false;
	}
	return true;
}
//检查航班数据
function checkAirFlightData() {
	if(null == $('#FLTID').val() || '' == $('#FLTID').val()) {
		alertInfo("请提供相应日期对应的出港航班号");
		return false;
	}
	return true;
}

// 检查表单并获取表单数据
function checkAndGetForm() {
	var a = 0;
	$('.deviceNo').each(function(){
		var deviceNo = $(this).val();
		if(null == deviceNo || '' == deviceNo.trim() ) {
			alertInfo("请输入车号");
			a = 1;
		}
	})
	$('.operatorName').each(function(){
		var operatorName = $(this).val();
		if(null == operatorName || '' == operatorName.trim()) {
			alertInfo("请填写操作员");
			a = 1
		} 
	})
	if(a == 1){
		return false;
	}
	var result = "";
	$(".firstTr").each(function(){
		result += $(this).find(".billId").val()+","+$(this).find(".deviceNo").val()+","+$(this).next().next().next().find(".operList").val()+","+
		$(this).find(".remark").val()+","+($(this).find(".WG").is(':checked')==true?1:0)+","+($(this).find(".HB").is(':checked')==true?1:0)+","+($(this).find(".SJ").is(':checked')==true?1:0)+","+($(this).next().find(".DG").is(':checked')==true?1:0)+","+
		($(this).next().find(".LT").is(':checked')==true?1:0)+","+($(this).next().find(".WS").is(':checked')==true?1:0)+","+($(this).next().next().find(".ZD").is(':checked')==true?1:0)+","+($(this).next().next().find(".ZJ").is(':checked')==true?1:0)+","+
		($(this).next().next().find(".MHQ").is(':checked')==true?1:0)+","+$(this).next().next().next().find(".QCD").val()+","+$(this).next().next().next().find(".SCD").val()+","+$("#flightNumber").val()+","+$("#flightDate").val()+","+$("#AIRCRAFTNUMBER").val()+","+
		$("#post").val()+","+$("#scheduler").val()+","+$("#ETA").val()+","+$("#ETD").val()+","+$("#ACTSTANDCODE").val()+","+$("#ACTTYPECODE").val()+"|";
	})
	return result;
}

// 获取表格数据
function getData() {
	
	var data = [];
	var columns = createDetailColumns[0];
	var tableData = table.bootstrapTable("getData");
	var tbody = $('tbody', table);
	var deviceNos = [];
	// 行
	$('tr', tbody).each(function(i, e) {
		
		var check = [];
		var td = $('td', e);
		var str = "";
		var deviceNo = "";
		var row = {};
		var scd = '';
		var qcd = '';
		// 列
		for(var j = 0; j < td.length; j++) {
			var field = columns[j].field;
			// 保持选中状态
			if($('span', td[j]).length != 0) {
				
				if(field == 'DEVICE_NO' || field == 'qcd' || field == 'scd') {		// 针对点击车辆编号却不选择保存不上问题
					
					if(field == 'qcd') {
						qcd = $('input', td[j]).val();
						if(qcd.length > 27) {
							qcd = '';
						}
					} else if(field == 'scd') {
						scd = $('input', td[j]).val();	
						if(scd.length > 27) {
							scd = '';
						}
					} else {						
						deviceNo = tableData[i][field];
					}
					
				} else { 
					if("" == str) {
						str += field;
					} else {
						str += ","+field;
					}
				}
			} else {
				if(field == 'DEVICE_NO') {	// 处理车辆编号	
					deviceNo = tableData[i][field];
					deviceNos.push(deviceNo);
				} else if(field == 'scd') {
					scd = tableData[i]['scd'];
				} else if(field == 'qcd') {
					qcd = tableData[i]['qcd'];					
				}
			}
		}
		row['checkStatus'] = str;
		row['deviceNo'] = deviceNo;
		row['scd'] = scd;
		row['qcd'] = qcd;
		data.push(row);
	});
	
	
	$('#data').val(JSON.stringify(data));
	var deviceNoStr = deviceNos.join(",")+","; 
	var de = "";
	var returnDe = "";
	for(var i=0;i<deviceNos.length;i++) {
		for(var j=0;j<deviceNos.length;j++) {
			if(j == i) {
				continue;
			}
			de = deviceNos[i];
			if(deviceNos[i] == deviceNos[j]) {
				returnDe = de;				
				break;
			}
		}
		
	}

	return returnDe;

	
}


// 重置
function reset() {
	if("modify" != $('#paramModify').val()) {
		clearAirFlightData();
		$('#flightNumber').val('');		
		$('#flightDate').val(getOperaDate());
		$("#operList").val('').trigger('change');
	} 
	dealData(null);
}

//初始化下拉选
function initSelect() {
	$('.operList').select2({
		placeholder : "请选择操作员",
		width : "100%",
		language : "zh-CN"
	});
}
// 监听操作员改变
$('#operList').on('change', function(){
	$('input[name=operatorName]').val($('#operList').find("option:selected").text());
});


// 强制改变滚动条样式
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

//	获取日期， 如果日期为空则默认当前时间
function getOperaDate() {
	var date = new Date();
	var year = date.getFullYear();
	var mon = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+1) : (date.getMonth()+1);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	daystr = '' +  year  +  mon  + day;
	
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
function doOpen(remarkId,type){
	layer.open({
		type:2,
		area: ['4px50','450px'],
		skin: 'layui-layer-nobg',
		shadeClose: true,
		title:false,
		content:ctx + '/passengerCar/operationRecord/openExceptionWin?remarkId='+remarkId+'&type='+type
	});
}