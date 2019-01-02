var layer, form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
	form.on('select(templateSel)', function(data) {
		var id = data.value;
		changeTemplate();
	});
});
var table = $('#createDetailTable');
var createDetailColumns = [];
var searchData = "";
var searchId = "";

var sign = "";
$(function() {
	// 初始化数据
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
//	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;

	initTable();
	// 初始化标识
	sign = $('#sign').val();
	
	
	// 初始化按钮
	$('button[type=button]').each(function(i, e) {
		$(e).click(function() {			
			var f = e.getAttribute("id");
			if("addBill" == f) {
				fBtn('add', '勤务单-增加', new Array("100%", "95%"), new Array("保存", "重置", "返回"));
			} else if("modifyBill" == f) {
				if("" != searchId) {
					fBtn('modify', '记录单详情', new Array("100%", "95%"), new Array("保存", "重置", "返回"));						
				} else {
					layer.msg("请点击一行进行修改！" , {icon : 7, time : 600});
				}
			} else if("delBill" == f) {
				if("" != searchId) {
					delBillAndGoods('del');					
				} else {
					layer.msg("请点击一行进行删除！" , {icon : 7, time : 600});
				}
			} 
		});
	});
	
});

//跳转控制
function fBtn(param, title,area, btn) {
	var method;
	layer.open({
		id : 'largePieceOfPlumLuggage',
		type : 2,
		title : title,
		offset : '5%',
		resize : false,
		fix : false,
		area : area,
		content : [ctx + "/passengerCar/operationRecord/pageJump/" + param + "?searchId=" + searchId, 'no'],
		btn : btn,
		success : function(layero, index) {
			var frameId=document.getElementById('largePieceOfPlumLuggage').getElementsByTagName("iframe")[0].id;
			method = $('#'+frameId)[0].contentWindow;
		},
		yes : function(index, layero) {
			
			if('add' == param || 'modify' == param) {
				dealData(method,index, param);
			}

		},
		btn2 : function(index, layero) {
			method.reset();
		    return false;

		},
		btn3 : function(index, layero) {
			searchId = "";
			changeTemplate();
		}
	});
}
// 删除数据
function delBillAndGoods(param) {
	var index = layer.confirm('是否删除当前选中数据？', {
		btn : [ '确定', '取消' ], // 按钮
		icon : 3
	}, function() {
		$.ajax({
			type : 'post',
			url : ctx + "/passengerCar/operationRecord/del",
			data : {
				searchId : searchId
			},
			success : function(msg) {
				if (msg == "success") {
					layer.msg('操作成功！', {
						icon : 1,
						time : 600
					});
					searchId = "";
					changeTemplate();
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
function dealData(method,index, param) {
	if(!checkData(method)) {
		return;
	}
	// 表单中表格数据
	var deviceNo = method.getData();
	if("" != deviceNo) {
		layer.msg("存在相同的车辆编号（" + deviceNo + "）！" , {icon : 7, time : 1500});

	} else {
		if('add' == param) {
			addData(method,index);
		} else if('modify' == param) {
			modify(method,index);
		}
	}
}
// 修改数据
function modify(method,index) {

	var passengerCarOperRecord = method.checkAndGetForm();
	// 获取航班数据
	if(!passengerCarOperRecord) {
		return;
	}
	var inn = layer.load(2);
	$.ajax({
		type : 'post',
		url : ctx + "/passengerCar/operationRecord/modify",
		data : {
			"data":passengerCarOperRecord
			},
		success : function(msg) {
			if (msg == "success") {
				layer.msg('保存成功！', {
					icon : 1,
					time : 600
				});
				searchId = "";
				layer.close(inn);
				layer.close(index);
				changeTemplate();
			} else {
				layer.msg("保存失败！" , {icon : 2, time : 600});
				return;
			}
		}
	});
		
}
// 保存数据
function addData(method,index) {
	
	var passengerCarOperRecord = method.checkAndGetForm();
	// 获取航班数据
	if(!passengerCarOperRecord) {
		return;
	}
	var inn = layer.load(2);
	
	$.ajax({
		type : 'post',
		url : ctx + "/passengerCar/operationRecord/isExists",
		data : passengerCarOperRecord,
		success : function(msg) {
			if (msg == "1") { // 已存在
				layer.msg('该航班记录已存在', {icon : 7, time : 1500});
				layer.close(inn);
			} else {
				$.ajax({
					type : 'post',
					url : ctx + "/passengerCar/operationRecord/add",
					data : passengerCarOperRecord,
					success : function(msg) {
						if (msg == "success") {
							layer.msg('保存成功！', {
								icon : 1,
								time : 600
							});
							searchId = "";
							layer.close(inn);
							layer.close(index);
							changeTemplate();
						} else {
							layer.msg("保存失败！" , {icon : 2, time : 600});
							layer.close(inn);
							return;
						}
					}
				});
			}
		}
	});

	

	
}
// 
// 检查数据
function checkData(method) {
	// 时间非空
	if(!method.checkFlightDate()) {
		return false;
	}
	// 航班数据非空
	if(!method.checkAirFlightData()) {
		return false;
	}
	
	return true;
}

function initTable() {

	createDetailColumns = [];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/passengerCar/operationRecord/getTableHeader",
		data : {pageSign: "main"},
		dataType : "json",
		success : function(result) {
			getDetailColumns(result);
		}
	});
}

function getDetailColumns(heaerPart) {
	var createRow2 = [];
	var createRow1 = [ 
	{
		title : "序号",
		width: 10 + '%',
		formatter: function (value, row, index) {  
			return index+1;  
        }  
	},  {
		field : "ID",
		title : "ID",
		visible : false
	}, {
		title : "日期",
		field : "FLIGHTDATE",
	}, {
		field : "FLIGHTNUMBER",
		title : "航班号"
	},{
		field : "AIRCRAFTNUMBER",
		title : "机号"
	}, {
		field : "OPERATORNAME",
		title : "客梯车司机"
	}];

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
		checkboxHeader : true,
	    dataType: "json",
	    checkboxHeader:false,
	    sidePagination: 'server',
	    queryParamsType:'',
	    undefinedText : '',
	    onlyInfoPagination: false,
		queryParams : function(params) {
			var param = {
				searchText : params.searchText,
				pageSign : 'main',
                pageNumber : params.pageNumber,    
                pageSize : params.pageSize
			};
			return param;
		},
		pagination: true,//是否开启分页（*）
        pageNumber:1,//初始化加载第一页，默认第一页
        pageSize: 10,//每页的记录行数（*）
        pageList: [10,15],
		toolbar : $("#tool-box"),
		search : true, 
		searchOnEnterKey : true,
		
		url : ctx + "/passengerCar/operationRecord/getGridData",
		columns : createDetailColumns,
		onClickRow : function onClickRow(row, tr, field) {
			searchId = row.ID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			searchId = row.ID;
			fBtn('modify', '记录单详情', new Array("100%", "95%"), new Array("保存", "重置", "返回"));
		},
		onClickCell : function onClickCell(field, value, row, td, tr) {
			
		},
		onLoadSuccess : function(data) {
			setTimeout(function() {
				$('div[class=mark_c]').attr({'style': 'display: none;'});
			}, 600);
		}
	};
//	createDetailOption.height = $(window).height() * 0.93;
	table.bootstrapTable(createDetailOption);
}


function changeTemplate() {
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