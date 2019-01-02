var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	
	if($("#hisFlag").val() == 'his'){
		$("#toolbar").hide();
	}
	
	var opt = {
			uniqueId : "id",
			url : ctx + "/flightDynamic/getDelayDate",
			method : "get",
			toolbar : "#toolbar",
			pagination : false,
			showRefresh : false,
			search : false,
			clickToSelect : false,
			searchOnEnterKey : true,
			undefinedText : "",
			height : $(window).height(),
			columns : getDelayGridColumns(),
			queryParams : function() {
				var param = {
						fltid : $("#fltid").val(),
						hisFlag:$("#hisFlag").val(),
				};
				return param;
			},
			onEditableSave : function(field, row, oldValue, $el) {
				if ("delay_code" == field) {
					var rowIndex = $el.parent().parent().index();
					$("#delayGrid").bootstrapTable("updateCell", {
						index : rowIndex,
						field : "delay_reason",
						value : tmReason[row.delay_code]
					});
				}
			}
		};
	
	$("#delayGrid").bootstrapTable(opt);
	
	$("#addDelayBtn").click(function() {
		insertRow();
	});
	
	$("#addTelegramBtn").click(function() {
		iframe = parent.layer.open({
			type : 2,
			title : "延误代码",
			content : ctx + "/sys/maintain/addInit?tabId=8",
			area : [ '700px', '400px' ],
			btn : [ "重置", "保存", "关闭" ],
			btn1 : function(index, layero) {
				var addForm = layer.getChildFrame('#addForm', index);
				addForm[0].reset();
			},
			btn2 : function(index, layero) {
				var addForm = layer.getChildFrame("#addForm", index);
				var iframeWin = $(layero).find("iframe")[0].contentWindow;
				iframeWin.doSubmit();
				$.ajax({
					async : false,
					type : "POST",
					url : ctx + "/flightDynamic/getDelayCode",
					data : {
						airline : $("#airline").val()
					},
					error : function() {
					},
					success : function(data) {
						tmCode = JSON.parse(data).tmCode;
						tmReason = JSON.parse(data).tmReason;
						opt.columns[1].editable.source = tmCode;
						$("#delayGrid").bootstrapTable('refreshOptions', opt);
						layero.find(".layui-layer-close").click();
					}
				});
				return false;
			}
		});
		layer.full(iframe);
	});
	
});

/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getDelayGridColumns() {
	var columns = [];
	if($("#hisFlag").val() == 'his'){
		columns = [ {
			field : "id",
			title : "序号",
			width : '40px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				row["id"] = index;
				return index + 1;
			}
		}, {
			field : "delay_code",
			title : "报文延误代码",
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : false
		}, {
			field : "delay_reason",
			title : '报文延误原因',
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : false
		}, {
			field : "delay_tm",
			title : '延误时间',
			width : '100px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				return value?value:'';
			},
			editable : false
		}, {
			field : "delay_type",
			title : '延误类型',
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : {
				disabled : "disabled",
				type : "select",
				onblur : "ignore",
				source : dimType
			}
		}, {
			field : "remark",
			title : '备注',
			valign : "middle",
			formatter : function(value, row, index) {
				return value?value:'';
			},
			editable : false
		}, {
			field : "delayId",
			visible : false
		} ];
	} else {
		columns = [ {
			field : "id",
			title : "序号",
			width : '40px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				row["id"] = index;
				return index + 1;
			}
		}, {
			field : "delay_code",
			title : "报文延误代码",
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : {
				type : "select",
				onblur : "ignore",
				source : tmCode
			}	
		}, {
			field : "delay_reason",
			title : '报文延误原因',
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : false
		}, {
			field : "delay_tm",
			title : '延误时间',
			width : '100px',
			align : 'center',
			valign : "middle",
			formatter : function(value, row, index) {
				return value?value:'';
			},
			editable : {
				type : 'text',
				validate : function(value) {
					value = $.trim(value);
					if (value != "") {
						var regu = /^[0-9][0-9][0-5][0-9]$/;
						var re = new RegExp(regu);
						if (!re.test(value)) {
							return '输入格式错误';	
						}
					}
					return "";
				}
			}
		}, {
			field : "delay_type",
			title : '延误类型',
			width : '100px',
			align : 'center',
			valign : "middle",
			editable : {
				type : "select",
				onblur : "ignore",
				source : dimType
			}
		}, {
			field : "remark",
			title : '备注',
			valign : "middle",
			formatter : function(value, row, index) {
				return value?value:'';
			},
			editable : {
				type : 'text'
			}
		}, {
			field : "btn",
			title : '操作',
			width : '50px',
			align : 'center',
			valign : "middle",
			events : operateEvents,
			formatter : function() {
				return '<button type="button" class="delBtn" >删除</button>';
			}
		}, {
			field : "delayId",
			visible : false
		} ];
	}
	return columns;
}
/**
 * 注册按钮的点击事件
 */
window.operateEvents = {
	'click .delBtn' : function(e, value, row, index) {
		$('#delayGrid').bootstrapTable('removeByUniqueId', row.id);
	}
};
function insertRow() {
	var index = $('#delayGrid').bootstrapTable('getData').length;
	var columns = getDelayGridColumns();
	var emptyRow = {};
	for (var i = 0; i < columns.length; i++) {
		var column = columns[i];
		emptyRow[column.field] = null;
	}
	emptyRow["id"] = index;
	$('#delayGrid').bootstrapTable('insertRow', {
		index : index,
		row : emptyRow
	});
}
function saveDelay() {
	if($("#hisFlag").val() == 'his'){
		parent.saveSuccess();
	} else {
		var dataItme = $('#delayGrid').bootstrapTable('getData');
		var result = [];
		for (var i = 0; i < dataItme.length; i++) {
			var delayInfo = dataItme[i];
			var isEmptyRow = true;
			for ( var field in delayInfo) {
				if (field !== "id" && delayInfo[field]) {
					isEmptyRow = false;
					break;
				}
			}
			if (!isEmptyRow) {
				result.push(delayInfo);
			}
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			async : false,
			type : "POST",
			url : ctx + "/flightDynamic/saveDelay",
			data : {
				fltid : $("#fltid").val(),
				delayResult : JSON.stringify(result)
			},
			error : function() {
				layer.close(loading);
				layer.msg('保存失败！', {
					icon : 2
				});
			},
			success : function(data) {
				layer.close(loading);
				if (data == "success") {
					layer.msg('保存成功！', {
						icon : 1,
						time : 600
					},function(){
						parent.saveSuccess();
					});
				} else {
					layer.msg('保存失败！', {
						icon : 2,
						time : 600
					});
				}
			}
		});
	}
}