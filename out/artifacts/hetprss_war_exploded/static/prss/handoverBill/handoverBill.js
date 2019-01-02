var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

var table = $('#createDetailTable');
var createDetailColumns = [];
var searchData = "";
var searchId = "";

var sign = "";
$(function() {
	// 初始化数据
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();
	// 初始化标识
	sign = $('#sign').val();
	
	
	// 初始化按钮
	$('button[type=button]').each(function(i, e) {
		$(e).click(function() {			
			var f = e.getAttribute("id");
			if("addBill" == f) {
				
				if("in" == sign) {
					fBtn('add', '进港邮货交接单-增加', new Array("100%", "95%"), new Array("保存", "重置", "返回"));					
				} else if("out" == sign){
					fBtn('add', '出港邮货交接单-增加', new Array("100%", "95%"), new Array("保存", "重置", "返回"));										
				}
			} else if("modifyBill" == f) {
				if("" != searchId) {
					if("in" == sign) {	
						fBtn('modify', '进港邮货交接单-修改', new Array("100%", "95%"), new Array("保存", "重置", "返回"));						
					} else if("out" == sign) {
						fBtn('modify', '出港邮货交接单-修改', new Array("100%", "95%"), new Array("保存", "重置", "返回"));												
					}
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
			} else if("delBill" == f) {
				if("" != searchId) {
					delBillAndGoods('del');					
				} else {
					layer.msg("请点击一行！" , {icon : 7, time : 800});
				}
			} 
		});
	});
	
});

//跳转控制
function fBtn(param, title,area, btn) {
	var method;
	layer.open({
		id : 'handoverBill',
		type : 2,
		title : title,
		offset : '5%',
		resize : false,
		fix : false,
		area : area,
		content : [ctx + "/handoverBill/bill/pageJump/" + param +"/" + sign + "?searchId=" + searchId, 'no'],
		btn : btn,
		success : function(layero, index) {
//			var iframeId = document.getElementById('largePieceOfPlumLuggage').getElementsByTagName("iframe")[0].id;
//			$("#"+iframeId).contents().find("body").attr({"style": "min-height: initial;"});
			var frameId=document.getElementById('handoverBill').getElementsByTagName("iframe")[0].id;
			method = $('#'+frameId)[0].contentWindow;
		},
		yes : function(index, layero) {
			
			if('add' == param) {
				addData(method,index);
			} else if('modify' == param) {
				modify(method,index);
			} else if('rep' == param) {
				feedReport(method,index);
			} else if('cdm' == param) {
				aCDMContractorResponsibleBtn(method,index);
			} else if('search' == param) {
				screenAbnormalBtn(method,index);	
			}
			

		},
		btn2 : function(index, layero) {
			method.reset();
		    return false;

		},
		btn3 : function(index, layero) {
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
			url : ctx + "/handoverBill/bill/del/"+sign,
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
// 修改数据
function modify(method,index) {
	if(!checkData(method)) {
		return;
	}
	
	var inn = layer.load(2);
	var data = method.getFormData();
	$.ajax({
		type : 'post',
		url : ctx + "/handoverBill/bill/modify/"+sign,
		data : data,
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
// 保存数据
function addData(method,index) {

	if(!checkData(method)) {
		return;
	}
	var inn = layer.load(2);
	var data = method.getFormData();
	$.ajax({
		type : 'post',
		url : ctx + "/handoverBill/bill/isExists/" + sign,
		data : data,
		success : function(msg) {
			if (msg == "1") { // 已存在
				layer.msg('该航班记录已存在', {icon : 7, time : 1500});
				layer.close(inn);
			} else {
				$.ajax({
					type : 'post',
					url : ctx + "/handoverBill/bill/add/"+sign,
					data : data,
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
	// 检查查理
	if(!method.checkCharline()) {
		return false;
	}
	// 检查司机与日期
	if(!method.checkReceiverDate()) {
		return false;
	}
	// 检查是否存在相同的代码
//	if(!method.checkCollCode()) {
//		return false;
//	}
	
	// 检查数据合法性
	if(!method.checkFormData()) {
		return false;
	}
//	return false;
	return true;
}





function initTable() {

	createDetailColumns = [];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/handoverBill/bill/getTableHeader/main/"+$('#sign').val(),
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
		width : 8 + '%',
		formatter: function (value, row, index) {  
			return index+1;  
        }  
	}, {
		field : "ID",
		title : "ID",
		visible : false
	}, {
		title : "日期",
		align : 'center',
		field : "FLIGHTDATE",
	}, {
		field : "FLIGHTNUMBER",
		align : 'center',
		title : "航班号"
	}, {
		field : "ETA",
		align : 'center',
		title : "进港预落"
	}, {
		field : "AIRCRAFTNUMBER",
		align : 'center',
		title : "机号"
	}, {
		field : "ACTSTANDCODE",
		align : 'center',
		title : "机位"
	}, {
		field : "OPERATORNAME",
		align : 'center',
		title : "查理"
	}, {
		field : "CREATEDATE",
		align : 'center',
		title : "操作时间 "
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
	    onlyInfoPagination: false,
	    undefinedText : '',
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
		url : ctx + "/handoverBill/bill/getGridData/" + $('#sign').val(),
		columns : createDetailColumns,
		onClickRow : function onClickRow(row, tr, field) {
			searchId = row.ID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			searchId = row.ID;
			if("in" == sign) {	
				fBtn('modify', '进港邮货交接单-修改', new Array("100%", "95%"), new Array("保存", "重置", "返回"));						
			} else if("out" == sign) {
				fBtn('modify', '出港邮货交接单-修改', new Array("100%", "95%"), new Array("保存", "重置", "返回"));												
			}
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