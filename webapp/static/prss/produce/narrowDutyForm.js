layui.use([ "layer", "form", "element" ], function() {
	var form = layui.form;
});

$(document).ready(function() {
	
	loadTable();
	
	// 新增行
	$('#addRow').on('click',function(e){
		addEmptyRow(1);
	});
	
	$('#flightNumber').on('blur',function(e){
		getFlightInfo();
		getTimeInfo();
	});
	$('#flightDate').on('change',function(e){
		getFlightInfo();
		getTimeInfo();
	});
	$(':radio[name=inout]').on('click',function(e){
		getFlightInfo();
		getTimeInfo();
	})
	
	$('.select2').each(function(){
		$(this).select2({
			placeholder : '请选择'
		});
	});
	
	
	$('#operator').on('change',function(){
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/produce/narrowDuty/getMembers",
			data : {id : $(this).val()},
			success : function(dataMap) {
				if (dataMap.code == 0) {
					var oldVal = $('input[name=members]').val();
					$('input[name=members]').val(dataMap.data);
				} 
			}
		});
	});
})

// 获取航班信息
function getFlightInfo(){
	var flightNumber = $('#flightNumber').val();
	var inout = $(':radio[name=inout]:checked').val();
	var flightDate = $('#flightDate').val();
	
	if(!flightNumber || !inout || !flightDate){
		clearFlightInfo();
		return;
	}
	$.ajax({
		type : 'post',
		url : ctx + "/produce/narrowDuty/getFlightInfo",
		data : {
			flightNumber : flightNumber,
			inout : inout,
			flightDate : flightDate
		},
		success : function(data) {
			if(data.code == 0){
				var fltinfo = data.data;
				if(fltinfo){
					$('#aircraftNumber').val(fltinfo.aircraftNumber?fltinfo.aircraftNumber:'');
					$('#acttypeCode').val(fltinfo.acttypeCode?fltinfo.acttypeCode:'');
					$('#actstandCode').val(fltinfo.actstandCode?fltinfo.actstandCode:'');
					$('#eta').val(fltinfo.eta?fltinfo.eta:'');
					$('#ata').val(fltinfo.ata?fltinfo.ata:'');
					$('#departApt4code').val(fltinfo.departApt4code?fltinfo.departApt4code:'');
					$('#etd').val(fltinfo.etd?fltinfo.etd:'');
					$('#atd').val(fltinfo.atd?fltinfo.atd:'');
					$('#arrivalApt4code').val(fltinfo.arrivalApt4code?fltinfo.arrivalApt4code:'');
				}else{
					clearFlightInfo();
				}
			}else{
				console.log('flightInfo error : ' + data.desc);
				clearFlightInfo();
			}
		},
		error : function(e) {
			console.log('flightInfo error : ' + e);
			clearFlightInfo();
		}
	});
}

// 清空航班信息
function clearFlightInfo(){
	$('#aircraftNumber').val('');
	$('#acttypeCode').val('');
	$('#actstandCode').val('');
	$('#eta').val('');
	$('#ata').val('');
	$('#departApt4code').val('');
	$('#etd').val('');
	$('#atd').val('');
	$('#arrivalApt4code').val('');
}

// 获取各种时间
function getTimeInfo(){
	var flightNumber = $('#flightNumber').val();
	var inout = $(':radio[name=inout]:checked').val();
	var flightDate = $('#flightDate').val();
	
	if(!flightNumber || !inout || !flightDate){
		clearTimeInfo();
		return;
	}
	$.ajax({
		type : 'post',
		url : ctx + "/produce/narrowDuty/getTimeInfo",
		data : {
			flightNumber : flightNumber,
			inout : inout,
			flightDate : flightDate
		},
		success : function(data) {
			if(data.code == 0){
				var timeinfo = data.data;
				if(timeinfo){
					$('#lqdkTime').val(timeinfo.dockingTime?timeinfo.dockingTime:'');
					$('#hcmkqTime').val(timeinfo.doorOpenTime?timeinfo.doorOpenTime:'');
					$('#zcwcTime').val(timeinfo.finishLodingTime?timeinfo.finishLodingTime:'');
					$('#xcwcTime').val(timeinfo.finishOffTime?timeinfo.finishOffTime:'');
					$('#zhhcmgbTime').val(timeinfo.lastCloseTime?timeinfo.lastCloseTime:'');
					$('#zczsqdTime').val(timeinfo.orderGetTime?timeinfo.orderGetTime:'');
					$('#scmgbTime').val(timeinfo.doorClosedTime?timeinfo.doorClosedTime:'');
					$('#kszcTime').val(timeinfo.startLodingTime?timeinfo.startLodingTime:'');
				}else{
					clearTimeInfo();
				}
			}else{
				console.log('flightInfo error : ' + data.desc);
				clearTimeInfo();
			}
		},
		error : function(e) {
			console.log('flightInfo error : ' + e);
			clearTimeInfo();
		}
	});
}

// 清空各种时间
function clearTimeInfo(){
	$('#lqdkTime').val('');
	$('#hcmkqTime').val('');
	$('#zcwcTime').val('');
	$('#xcwcTime').val('');
	$('#zhhcmgbTime').val('');
	$('#zczsqdTime').val('');
	$('#scmgbTime').val('');
	$('#kszcTime').val('');
}


function addBill() {
	
	// 表单的json格式数据
	var billData = $('#newBill').serializeObject();
	// 操作人姓名
	billData.operatorName = $('#operator option:selected').text();
	// 获取车辆信息
	var goodsObj = {};
	$('#createDetailTable tbody tr').each(function(){
		var deviceNo = $(this).find('td a[data-name="deviceNo"]').text();
		if(deviceNo){
			var deviceArr = [];
			$(this).find(':checkbox').each(function(){
				var obj = {};
				obj.jcxmId = $(this).attr('data-field');
				obj.jcxmName = $(this).attr('data-name');
				obj.id = $(this).attr('data-id');
				obj.jcxmVal = $(this).is(':checked')?'0':'1';
				
				deviceArr.push(obj);
			});
			
			goodsObj[deviceNo] = deviceArr;
		}
	});
	
	billData.billZpqwbgGoodsStr = JSON.stringify(goodsObj);
	
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/produce/narrowDuty/saveBill",
		data : billData,
		error : function() {
			layer.close(loading);
			layer.msg('保存失败！', {
				icon : 2
			});
		},
		success : function(dataMap) {
			layer.close(loading);
			if (dataMap.code == 0) {
				layer.msg('保存成功！', {
					icon : 1,
					time : 1000
				},function(){
					parent.saveSuccess();
				});
			} else {
				layer.msg('保存失败，'+dataMap.msg, {
					icon : 2,
					time : 1000
				});
			}
		}
	});
}

var editable = true;
var disabled = false;

// 车辆信息表格表头
var createDetailColumns = [ {
	field : "order",
	title : "序号",
	sortable : false,
	editable : false,
	formatter : function(value, row, index) {
		return index + 1;
	}
}, {
	field : "id",
	title : "ID",
	visible : false
}, {
	field : "deviceNo",
	title : "车辆编号",
	editable : {
		disabled : disabled,
		title : "车辆编号"
	},
}, {
	field : "wg",
	title : "外观",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'wg'){
					dataId = item.id;
					break;
				}
			};
		}
		
		if(value == 0){
			return '<input type="checkbox" data-field="wg" data-id="'+dataId+'" data-name="外观"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="wg" data-id="'+dataId+'" data-name="外观"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "lt",
	title : "轮胎",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'lt'){
					dataId = item.id;
					break;
				}
			};
		}
		
		if(value == 0){
			return '<input type="checkbox" data-field="lt" data-id="'+dataId+'" data-name="轮胎"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="lt" data-id="'+dataId+'" data-name="轮胎"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "dg",
	title : "灯光",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'dg'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="dg" data-id="'+dataId+'" data-name="灯光"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="dg" data-id="'+dataId+'" data-name="灯光"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "zd",
	title : "制动",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'zd'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="zd" data-id="'+dataId+'" data-name="制动"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="zd" data-id="'+dataId+'" data-name="制动"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "yyxt",
	title : "液压系统",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'yyxt'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="yyxt" data-id="'+dataId+'" data-name="液压系统"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="yyxt" data-id="'+dataId+'" data-name="液压系统"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "zx",
	title : "转向",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'zx'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="zx" data-id="'+dataId+'" data-name="转向"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="zx" data-id="'+dataId+'" data-name="转向"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "lb",
	title : "喇叭",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'lb'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="lb" data-id="'+dataId+'" data-name="喇叭"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="lb" data-id="'+dataId+'" data-name="喇叭"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "yb",
	title : "仪表",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'yb'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="yb" data-id="'+dataId+'" data-name="仪表"  onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="yb" data-id="'+dataId+'" data-name="仪表"  onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "ry",
	title : "燃油",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'ry'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="ry" data-id="'+dataId+'" data-name="燃油"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="ry" data-id="'+dataId+'" data-name="燃油"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "lqy",
	title : "冷却液",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'lqy'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="lqy" data-id="'+dataId+'" data-name="冷却液"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="lqy" data-id="'+dataId+'" data-name="冷却液"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "jsd",
	title : "警示灯",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'jsd'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="jsd" data-id="'+dataId+'" data-name="警示灯"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox" data-field="jsd" data-id="'+dataId+'" data-name="警示灯"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "mhq",
	title : "灭火器",
	formatter : function(value, row, index) {
		var dataId = '';
		if(tmpGridData && row.deviceNo){
			var rowArr = tmpGridData[row.deviceNo];
			for(var i = 0 ; i < rowArr.length; i++){
				var item = rowArr[i];
				if(item.jcxmId == 'mhq'){
					dataId = item.id;
					break;
				}
			};
		}
		if(value == 0){
			return '<input type="checkbox" data-field="mhq" data-id="'+dataId+'" data-name="灭火器"   onclick="changeCarVal(this,\''+row.id+'\')" checked />';
		}else{
			return '<input type="checkbox"  data-field="mhq" data-id="'+dataId+'" data-name="灭火器"   onclick="changeCarVal(this,\''+row.id+'\')"  />';
		}
	}
}, {
	field : "operate",
	title : "操作",
	sortable : false,
	editable : false,
	visible : editable,
	formatter : function(value, row, index) {
		return "<i class='fa fa-remove' onclick='removeRow(this)'></i>";
	}
}];

var createDetailOption = {
		method : 'get',
		dataType : 'json',
		striped : false, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true,
		sortable : false,
		toolbar : "#toolbar",
		uniqueId : "id",
		undefinedText : '', // undefined时显示文本
		columns : createDetailColumns,
		data:[],
		/*onLoadSuccess : function(data) {
			addEmptyRow(1);// 加载成功后添加一个空行
		},*/
		onEditableSave : function(field, row, oldValue, $el) {
			
		}
	};

function loadTable(){
	
	if(tmpGridData){
		var data = [];
		$.each(tmpGridData,function(k,v){
			
			var item = {};
			item.id = k;
			item.deviceNo =	 k; 
			
			$.each(v , function(i,o){
				item[o.jcxmId] = o.jcxmVal;
			});
			data.push(item);
		});
		createDetailOption.data = data;
	}
	
	$("#createDetailTable").bootstrapTable(createDetailOption);
}


//添加空行，并将固定信息带入
function addEmptyRow(num) {
	var table = $("#createDetailTable");
	for (var i = 0; i < num; i++) {
		var length = table.bootstrapTable("getData").length;
		var row = {};
		for (var j = 0; j < createDetailColumns.length; j++) {
			if(createDetailColumns[j].field == 'deviceNo'){
				row[createDetailColumns[j].field] = null;
			}else{
				row[createDetailColumns[j].field] = 1;
			}
		}
		row.id = (new Date()).valueOf();
		/*if (length == 0) {
			for (var j = 0; j < createDetailColumns.length; j++) {
				row[createDetailColumns[j].field] = null;
			}
		} else {
			var lastRow = table.bootstrapTable("getData")[length - 1];
			for (var j = 0; j < createDetailColumns.length; j++) {
				var field = createDetailColumns[j].field;
				if (field == "aircraftNumber" || field == "actType" || field == "airline") {
					row[createDetailColumns[j].field] = lastRow[field];
				} else {
					row[createDetailColumns[j].field] = null;
				}

			}
		}*/
		table.bootstrapTable('append', row);
	}
}

//删除一行
function removeRow(i) {
	if ($("#createDetailTable").bootstrapTable('getData').length > 1) {
		var uniqueid = $(i).parents("tr").data("uniqueid");
		$("#createDetailTable").bootstrapTable('removeByUniqueId', uniqueid);
	}
}

function changeCarVal(checkbox,rowID){
	var rowData = $("#createDetailTable").bootstrapTable('getRowByUniqueId', rowID);
	var data_key = $(checkbox).attr('data-field');
	var data_val = rowData[data_key] == 1 ? 0 : 1;
	rowData[data_key] = data_val;
}