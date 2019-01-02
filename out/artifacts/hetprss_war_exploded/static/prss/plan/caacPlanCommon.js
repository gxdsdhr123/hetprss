
var updateUrl = ctx + "/caac/plan/update";
function columns(visible){
	return  [ // 列设置
		{
			field : "checkbox",
			checkbox : true,
			sortable : false,
			align : "center",
			title : "",
			editable : false
		}, {
			field : "order", // 数据属性名
			title : "序号", // 表格列头
			align : "center",
			sortable : false, // 是否开启排序
			editable : false, // 是否可编辑
			formatter : function(value, row, index) { // 自定义格式
				return index + 1;
			}
		}, {
			field : "FLT_NO",
			title : "航班号",
			align : "center",
			sortable : true, 
			display : function(value){
				$(this).html(value.toUpperCase());
			},
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable: editableText(updateUrl,'航班号不能为空!'),
			class : isClassWhite()
		},{
			field : "ACT_TYPE",
			title : "机型",
			align : "center",
			sortable : true, 
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : editableSelect('',data.ACT_TYPE),
			class : isClassWhite()
		},{
			field : "FLT_WEEK",
			title : "班期",
			align : "center",
			sortable : true, 
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : false,
			class : isClassWhite()
		},{
			field : "DEPART_APT",
			title : "起场",
			align : "center",
			sortable : true, 
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable: {
				type: 'select2',
				onblur:'ignore',
				source: data.APT,
				select2:{
				    matcher: matcher
				}
			},
			class : isClassWhite()
		},{
			field : "STD",
			title : "计起时间",
			align : "center",
			sortable : true, 
			formatter : function(value, row, index) {
				return (value ? value : '');
			},
			editable : {
				type:'text',
				placeholder:"0000~2359",
				validate: function (value) {
	                if(!filterText(value)){
						return '计起时间格式不正确!格式(24小时制)：小时分钟+';
					}
	            }
			},
			class : isClassWhite()
		},{
			field : "ARRIVAL_APT",
			title : "落场",
			align : "center",
			sortable : true, 
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable: {
				type: 'select2',
				source: data.APT,
				onblur:'ignore',
				select2:{
				    matcher: matcher
				}
			},
			class : isClassWhite()
		},{
			field : "STA",
			title : "计落时间",
			align : "center",
			sortable : true, 
			formatter : function(value, row, index) {
				return (value ? value : '');
			},editable : {
				type:'text',
				placeholder:"0000~2359",
				validate: function (value) {
	                if(!filterText(value)){
						return '计落时间格式不正确!格式(24小时制)：小时分钟+';
					}
	            }
			},
			class : isClassWhite()
		},{
			field : "SHARE_FLT_NO",
			title : "共享航班号",
			sortable : true, 
			align : "center",
			display : function(value){
				$(this).html(value.toUpperCase());
			},
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : editableText(updateUrl,''),
			class : isClassWhite()
		}, {
			field : "ATTR_CODE",
			title : "属性",
			align : "center",
			sortable : true, 
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable: editableSelect('',data.ATTR_CODE),
			class : isClassWhite()
		}, 
		{
			field : "ALN_CODE",
			title : "航空公司",
			visible : visible,
			align : "center",
			formatter :function(value, row, index) {
				return (value ? value : '-')
			},
			editable: {
				type: 'select2',
				source: data.AIRLINE_CODE,
				select2:{
				    matcher: matcher
				}
			},
			class : isClassWhite()
		}, 
		{
			field : "PLAN_DATE_BEGIN",
			title : "起始日期",
			align : "center",
			sortable : true, 
			formatter : function(value, row, index) {
				return "<a href='javascript:void(0)' id='beginTime_" + index + "' class='color-white'>"
				+ (value ? value : getNowFormatDate()) + "</a>";
			},
			class : isClassWhite()
		}, {
			field : "PLAN_DATE_END",
			title : "终止日期",
			align : "center",
			sortable : true, 
			formatter : function(value, row, index) {
				return "<a href='javascript:void(0)' id='endTime_" + index + "' class='color-white'>"
				+ (value ? value : getNowFormatDate()) + "</a>";
			},
			class : isClassWhite()
		}, {
			field : "REMARK",
			title : "备注",
			align : "center",
			editable : true
		} ];
}

function matcher(params, data) {
	var search = '';
	for(var str in data){
		if('element' != str && 'selected' != str && 'disabled' != str){
			search += data[str];
		}
	}
	if ($.trim(params.term) === '') {
      return data;
    }
    if (search.toUpperCase().indexOf(params.term.toUpperCase()) > -1) {
      var modifiedData = $.extend({}, data, true);
      return modifiedData;
    }
    return null;
}
function filterText(text){
	var rep =new RegExp("^(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))[0-5]{1}[0-9]{1}[+]?$");
	return rep.test(text);
}
function editableSelect(pk,source){
	return {
		type: 'select2',
		source: source
	}
}

function editableText(url,msg){
	return {
		type : 'text',
		display : function(value){
			value = value + "";
			$(this).html(value.toUpperCase());
		},
		validate : function(value) {
			if ($.trim(value) == '' && msg !='') {
				return msg;
			} 
		}
	}
}


function getNowFormatDate() {
   	var date = new Date();
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	var currentdate = year+""+month+""+day;
    return currentdate;
} 

/**
 * 弹出日期控件
 * @param field
 * @param value
 * @param row
 * @param td
 */
function popupDate(field, value, row, $element,formatter){
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
		dateFmt : formatter,
		onpicked : function(dp) {
			var newValue = dp.cal.getDateStr();
			if (newValue) {
				row[field] = newValue;
				editableSave(field, row, value, $element);
			}
		}
	});
}


function editableSave(field, row, oldValue, td) {
//	var dataIndex = td.parent().parent().index();
	var rowIndex = td.parent().data("index");
	var value = row[field];
	//航班号三字码大写，航空公司联动
	if(field=="FLT_NO"){
		row["FLT_NO"] = value.toUpperCase();
		row['ALN_CODE'] = value.substring(0,2).toUpperCase();
		$("#baseTable").bootstrapTable("updateRow", {
			index : rowIndex,
			row : row
		});
		//共享航班号
		$.post(ctx + "/caac/plan/getShareFltNo", {
			fltNo : value
		}, function(shareFltNo) {
			row['SHARE_FLT_NO'] = shareFltNo;
			$("#baseTable").bootstrapTable("updateRow", {
				index : rowIndex,
				row : row
			});
		})
	}
	if(field=="ALN_CODE"){
		row['FLT_NO'] = value.toUpperCase()+row["FLT_NO"].substring(2, row["FLT_NO"].length)
		$("#baseTable").bootstrapTable("updateRow", {
			index : rowIndex,
			row : row
		})
	}
	
	if(row[field] == oldValue)
		return;
	var loading = null;
	loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/caac/plan/update",
		data : {
			'id' : row.ID,
			'name' : field,
			'value' : row[field]
		},
		success : function(res) {
			if (res == "success") {// 与加载时数据的时间戳对比，有变化说明已被改动，需要重新加载数据
				layer.close(loading);
				var rowIndex = td.parent().data("index");
				$("#baseTable").bootstrapTable("updateRow", {
					index : rowIndex,
					row : row
				})
//				refreshTable();
			} else {
				layer.close(loading);
				layer.msg(res, {
					icon : 2,
					time : 1000
				});
				var rowIndex = td.parent().data("index");
				row[field] = oldValue;
				$("#baseTable").bootstrapTable("updateRow", {
					index : rowIndex,
					row : row
				})
			}
		}
	});
}

function isClassWhite() {
	var cla = "color-white";
	return cla;
}

function createFltWeek(field, value, row, td){
	layer.open({
		type : 1,
		title : "班期",
		offset : '100px',
		move : false,
		content : $("#flightWeek"),
		btn : [ "确定" ],
		success : function(layero, index) {
			// 按照内容初始化班期
			layero.find(".layui-unselect").removeClass("layui-form-checked");
			if (value) {
				for (var i = 0; i < value.length; i++) {
					var val = value[i];
					if (!isNaN(val) && val != ".") {
						layero.find(".layui-unselect").eq(val-1).addClass("layui-form-checked");
					}
				}
			}
		},
		yes : function(index, layero) {
			// 生成班期填入表格，并保存
			var disabledDays = [];
			var times="";
			if(layero.find(".layui-form-checked").length<=0){
				layer.msg("请选择班期!",{icon:0,time:600});
				return;
			}
			
			layero.find(".layui-unselect").each(function(index,item) {
				var cls = $(item).attr("class");
				times += cls =="layui-unselect layui-form-checkbox layui-form-checked"?(index+1):".";
			})
			var oldValue = row.FLT_WEEK;
			row.FLT_WEEK = times;
			if(type != "add"){
				editableSave(field, row, oldValue, td);
			} else {
				var rowIndex = td.parent().data("index");
				$("#baseTable").bootstrapTable("updateRow", {
					index : rowIndex,
					row : row
				})
			}
			layer.close(index);
		}
	});
} 