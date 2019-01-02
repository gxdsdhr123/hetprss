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
var searchId = "";
var state = "";
var searchData = "";
var print = false;

$(function() {
	
	// 初始化数据
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();
	
	// 默认选中当前时间
	var date = new Date();
	var daystr = "";
	daystr = "" + date.getFullYear() + (date.getMonth() + 1) + date.getDate();

	
	// 初始化按钮
	$('button[type=button]').each(function(i, e) {
		$(e).click(function() {			
			var f = e.getAttribute("id");
			if("addabnormalFlightBtn" == f) {
				fBtn('add', '新增', new Array("80%", "95%"), new Array("发送", "返回"));
			} else if("showAbnormalFlightBtn" == f) {
				if("" != searchId) {
					fBtn('show', '查看', new Array("80%", "95%"), new Array("打印","返回"));
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
			} else if("delAbnormalFlightBtn" == f) {
				if("" != searchId) {
					delAbnormalFlightBtn('del');					
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
			} else if("surveyFeedBackReportBtn" == f) {
				if("" != searchId) {
					surveyFeedBackReportBtn();
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
			} else if("CDMContractorResponsibleBtn" == f) {
				if("" != searchId) {
					fBtn('cdm', 'CDM判责', new Array("80%", "95%"), new Array("发送", "返回"));					
				} else {
					layer.msg("请点击一行！", {icon : 7, time : 800});
				}
			} else if("screenAbnormalBtn" == f) {
				fBtn('search', '筛选', new Array("50%", "65%"), new Array("确定", "返回"));	
			} else if("printSelectedFlightBtn" == f) {
				printSelectedFlightBtn();
			}
		});
	});
		
});
// 跳转控制
function fBtn(param, title,area, btn) {
	layer.open({
		id : 'showAbnormalFlightIframe',
		type : 2,
		title : title,
		offset : '5%',
		resize : false,
		fix : false,
		area : area,
		content : [ctx + "/abnormal/abnormalFlightManagement/abnormalFlight/"+param+"?searchId="+searchId, 'no'],
		btn : btn,
		success : function(layero, index) {
			var iframeId = document.getElementById('showAbnormalFlightIframe').getElementsByTagName("iframe")[0].id;
			$("#"+iframeId).contents().find("body").attr({"style": "min-height: initial;"});
		},
		yes : function(index, layero) {
			var inn = layer.load(2);
			var frameId=document.getElementById('showAbnormalFlightIframe').getElementsByTagName("iframe")[0].id;
			var method = $('#'+frameId)[0].contentWindow;
			if('add' == param) {
				addAbnormalFlightBtn(method,index, inn);
			} else if('show' == param) {
				layer.close(inn);
				method.printword();
			} else if('rep' == param) {
				feedReport(method,index, inn);
			} else if('cdm' == param) {
				aCDMContractorResponsibleBtn(method,index, inn);
			} else if('search' == param) {
				screenAbnormalBtn(method,index, inn);	
			}
		}
	});
}
// 打印选中
function printSelectedFlightBtn() {
	if(!print) {		
		print = true;
		$('input[type=checkbox]').attr({'style':'display: block;'});
	} else {
		var data = table.bootstrapTable('getSelections');
		var columns = $.map(table.bootstrapTable('getOptions').columns[0],
				function(col) {
					if (col.field != "ID" && col.field != "checkbox") {
						return {
							"field" : col.field,
							"title" : col.title
						};
					} else {
						return null;
					}
				});
		if(data.length == 0) {
			print = false;
			$('input[type=checkbox]').attr({'style':'display: none;'});
//			layer.msg("请选择打印数据", {icon : 7, time : 800});
//			return;
		} else {			
			$("#printTitle").text(JSON.stringify(columns));
			$("#printData").text(JSON.stringify(data));
			$("#printForm").submit();
			print = false;
			$('input[type=checkbox]').attr({'style':'display: none;'});
		}
	}
}
// 筛选
function screenAbnormalBtn(method,index, inn) {
	var data = method.getFormData();
	searchData = data;
	layer.close(inn);
	layer.close(index);
	changeTemplate();
}
// CDM判责
function aCDMContractorResponsibleBtn(method,index, inn) {
	var data = method.getCDMInfo();
	$.ajax({
		type : 'post',
		url : ctx + "/abnormal/abnormalFlightManagement/aCDMContractorResponsible",
		data : data,
		success : function(msg) {
			if (msg == "success") {
				layer.msg('发送成功！', {
					icon : 1,
					time : 600
				});
				searchId = "";
				layer.close(inn);
				layer.close(index);
				changeTemplate();
			} else {
				layer.msg("发送失败！", {icon : 2, time : 600});
				return;
			}
		}
	});

}
// 反馈调查报告
function surveyFeedBackReportBtn() {
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/abnormal/abnormalFlightManagement/deptIsExist",
		data : {searchId: searchId},
		dataType : "json",
		success : function(result) {
			if("1" == result) {
				if(state == 1){
					fBtn('rep', '调查反馈', new Array("80%", "95%"), null);
				}else{
					fBtn('rep', '调查反馈', new Array("80%", "95%"), new Array("发送", "返回"));
				}
			} else {
				layer.msg("该选项中不存在您所在的部门", {icon : 7});
			}
		}
	});
	
}
// 
function feedReport(method,index, inn) {
	var data = method.getFeedBackInfo();
	$.ajax({
		type : 'post',
		url : ctx + "/abnormal/abnormalFlightManagement/feedBackAirRep",
		data : data,
		success : function(msg) {
			if (msg == "success") {
				layer.msg('发送成功！', {
					icon : 1,
					time : 600
				});
				searchId = "";
				layer.close(inn);
				layer.close(index);
				changeTemplate();
			} else {
				layer.msg("发送失败！", {icon : 2, time : 600});
				return;
			}
		}
	});
}
// 删除不正常航班信息
function delAbnormalFlightBtn(param) {
	var index = layer.confirm('是否删除当前不正常航班计划？', {
		btn : [ '确定', '取消' ], // 按钮
		icon : 3
	}, function() {
		$.ajax({
			type : 'post',
			url : ctx + "/abnormal/abnormalFlightManagement/delAbnormalFlight",
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
					layer.msg("操作失败", {icon : 2, time : 600});
					return;
				}
			}
		});

	}, function() {
		layer.close(index);
	});
}

// 增加不正常航班
function addAbnormalFlightBtn(method,index, inn) {
	 layer.close(inn);
	 if(undefined == method.getRadioChecked()) {
		 method.alertInfo("请选择消息来源");
		 return;
	 } else if('' != method.getRadioChecked()) {
		 method.clearInfoSourceInfo();
	 } else {
		 if(!method.setSourceInfo()) {
			 return;
		 }
	 }
	 if(!method.getFormCheckNull()) {
		 method.alertInfo("请输入航班号");
		 return;
	 }
	 if(!method.getSendDeptValue()) {
		 method.alertInfo("请选择发送部门");
		 return;
	 }
	 var data = method.getFormData();
	 var index1 = layer.load(2);
	$.ajax({
		type : 'post',
		url : ctx + "/abnormal/abnormalFlightManagement/addAbnormalFlightData",
		data : data,
		success : function(msg) {
			if("success" == msg) {
				layer.msg("发送成功！", {icon : 1, time : 600});
				layer.close(index1);
				layer.close(index);
				changeTemplate();						
			} else {
				layer.msg("发送失败！", {icon : 1, time : 600});
				layer.close(index1);
			}
		}
	});
		
}


function initTable() {

	createDetailColumns = [];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/abnormal/abnormalFlightManagement/getTableHeader",
		data : null,
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
		field: "checkbox",
		checkbox:true,
		sortable:false,
		editable:false
	}, {
		title : "序号",
		field : "NO",
		formatter: function (value, row, index) {  
			return index+1;  
        }  
	},  {
		field : "ID",
		title : "ID",
		visible : false
	}, {
		title : "航班日期",
		align : 'left',
		field : "FLIGHTDATE",
		valign: "middle"
	}, {
		field : "FLIGHTNUMBER",
		align : 'left',
		title : "航班号"
	}, {
		field : "AIRCRAFTNUMBER",
		align : 'left',
		title : "机号 "
	}, {
		field : "REMARK",
		align : 'left',
		title : "总控备注"
	}, {
		field : "OPERDATE",
		align : 'left',
		title : "发送调查时间"
	}, {
		field : "DEPTNAME",
		align : 'left',
		title : "涉及部门 " 
	}, {
		field : "DEPTSTA",
		align : 'left',
		title : "保障部门反馈状态",
		formatter: function (value, row, index) {  
			var str = String(value);
			var st = str.split(",");
			var restr = 0;
			for(var i = 0; i < st.length; i ++) {
				if("1" == st[i]) {
					restr += 1;
				}
			}
			if("0" == restr) {
				return "未调查";
			} else if(restr == st.length) {
				return "已反馈";
			} else {
				return "调查中";
			}
        }  
	}, {
		field : "CMDSTA",
		align : 'left',
		title : "CMD判责状态",
		formatter: function (value, row, index) {  
			var str = String(value);
			if("0" == str) {
				return "调查中";
			} else {
				return "已完成";
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
		checkboxHeader : true,
	    dataType : "json",
	    sidePagination : 'server',
	    queryParamsType : '',
	    undefinedText : '',
	    onlyInfoPagination : false,
		queryParams : function(params) {
			var param = {
				searchData : searchData,
                pageNumber : params.pageNumber,    
                pageSize : params.pageSize
			};
			return param;
		},
		pagination: true,//是否开启分页（*）
        pageNumber:1,//初始化加载第一页，默认第一页
        pageSize: 10,//每页的记录行数（*）
        pageList: [10,15,20],
        
		toolbar : "#tool-box",
//		search : true, 
//		searchOnEnterKey : true,
		
		url : ctx + "/abnormal/abnormalFlightManagement/getGridData",
		columns : createDetailColumns,
		onClickRow : function onClickRow(row, tr, field) {
			if(!print) {		
				$(tr).removeClass('selected').siblings().removeClass('selected');
//				if($('input[type=checkbox]',tr).prop('checked') == true) {
//					$('input[type=checkbox]',tr).prop('checked', false);
//				} else {					
//					$('input[type=checkbox]',tr).prop('checked', true);
//				}
			}
			searchId = row.ID;
			state = row.DEPTSTA;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			searchId = row.ID;
			fBtn('show', '查看', new Array("80%", "95%"), new Array("打印","返回"));
		},
		onClickCell : function onClickCell(field, value, row, td, tr) {
			
		},
		onLoadSuccess : function(data) {
			time = "";
			searchId = "";
			$('input[type=checkbox]').attr({'style':'display: none;'});
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