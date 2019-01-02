var page = 1;
var limit = 100;
var tableData;
var baseTable;
var index = parent.layer.getFrameIndex(window.name);
$(function() {
	var layer;
	var clickRowId = "";// 当前单选行id，以便工具栏操作
	var fltProperties, airports, airlines, terminals;// 定义下拉选项
	baseTable = $("#baseTable");
	layui.use([ 'form', 'layer' ], function() {// 调用layui表单及弹出层组件
		layer = layui.layer;
	});

	createTable("Left",true);
	createTable("Right",false);
	function createTable(tableType,visible){
		// 基本表格选项
		var tableOptions = options(tableType,visible);
		tableOptions.height = $("#baseTables"+ tableType).height();// 表格适应页面高度
		$("#baseTable"+tableType).bootstrapTable(tableOptions);// 加载基本表格
		
	}
	function options(tableType,visible){
		return {
			data : result[tableType],
			uniqueId : "id",
			striped : true, // 是否显示行间隔色
			dataType : "json", // 返回结果格式
			pagination : false, // 是否显示分页（*）
			search : false,
			checkboxHeader : true, // 是否显示全选
			columns : columns(visible),
			onEditableSave : saveFilter,
			onClickCell : function(field, value, row, $element) {
			}
		}
	}
	function saveFilter(field, row, oldValue, $el) {
		var rowIndex = $el.parent().data("index");
//		var dataIndex = $element.parent().parent().index();
		var value = row[field];
		if((field == "std" || field == "sta") && !filterText1(row[field])){
			layer.msg('校验失败，请重新编辑！', {
				icon : 0,
				time : 600
			});
			row[field] = oldValue;
			$("#baseTable" + (row.flag == 'A'?'Left':'Right')).bootstrapTable("updateRow", {
				index : rowIndex,
				row : row
			})
		}
		
		if(field=="flt_no"){
			row['aln_code'] = value.substring(0, 3).toUpperCase();
			row['flt_no'] = value.toUpperCase();
			$("#baseTable" + (row.flag == 'A'?'Left':'Right')).bootstrapTable("updateRow", {
				index : rowIndex,
				row : row
			})
		}
		
		if(field=="aln_code"){
			row['flt_no'] = value.toUpperCase()+row["flt_no"].substring(3, row["flt_no"].length)
			$("#baseTable" + (row.flag == 'A'?'Left':'Right')).bootstrapTable("updateRow", {
				index : rowIndex,
				row : row
			})
		}
	}
})
/*
 * 列信息
 */
function columns(visible){
	return  [ // 列设置
		{
			field : "checkbox",
			checkbox : true,
			sortable : false,
			title : "",
			editable : false
		}, {
			field : "order", // 数据属性名
			title : "序号", // 表格列头
			sortable : false, // 是否开启排序
			editable : false, // 是否可编辑
			formatter : function(value, row, index) { // 自定义格式
				return index + 1;
			}
		}, {
			field : "flt_no",
			title : "航班号",
			width : "90px",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable:{
				type : 'text',
				validate: function (value) {  
	                if ($.trim(value) == '') {
	                    return '航班号不能为空!';  
	                }  
	            }
			},
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : function(value, row, index) {
				return isClassWhite();
			}
		},{
			field : "aircraft_number",
			title : "机号",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : false,
//			editable : editableText(''),
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
			field : "act_type",
			title : "机型",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : false,
//			editable : editableSelect("ACT_TYPE"),
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
			field : "depart_apt",
			title : "起场",
			width : "90px",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : {
				type: 'select2',
				onblur : "ignore",
				source: data.APT,
				select2:{
				    matcher: matcher
				}
			},
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
			field : "arrival_apt",
			title : "落场",
			width : "90px",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : {
				type: 'select2',
				onblur : "ignore",
				source: data.APT,
				select2:{
				    matcher: matcher
				}
			},
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
			field : "std",
			title : "计起时间",
			width : "150px",
			formatter : function(value, row, index) {
				return (value ? value : '');
			},
			editable : editableText(''),
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
			field : "sta",
			title : "计落时间",
			width : "150px",
			formatter : function(value, row, index) {
				return (value ? value : '');
			},
			editable : editableText(''),
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
//			field : "property_code",
			field : "property_shortname",
			title : "性质",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : false,
//			editable: editableSelect('PROPERTY_CODE'),
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
//			field : "attr_code",
			field : "attr_cncode",
			title : "属性",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable : false,
//			editable: editableSelect("ATTR_CODE"),
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
			field : "aln_code",
			title : "航空公司",
			width : "100px",
			formatter :function(value, row, index) {
				return (value ? value : '')
			},
			editable: editableSelect("AIRLINE_CODE"),
			cellStyle : function cellStyle(value, row, index, field) {
				var ifRed = row[field + '_red'];
				if(ifRed){
					return {
						css: {"background":"-moz-linear-gradient(right, rgba(255,0,0,0.9), rgba(255,0,0,1))"}
					};
				} else {
					return {
					};
				}
			} ,
			class : isClassWhite()
		},{
			field : "remark",
			title : "备注",
			formatter : function(value, row, index) {
				return (value ? '<a title="'+value+'">'+value+'</a>' : '');
			},
			class : isClassWhite()
		},{
			field : "operate",
			title : "操作",
			align : "center",
			formatter : function(value, row, index) {
				return "<i class='fa fa-save' onclick='save("+row.id+",\""+row.flag+"\")'></i>";
			}
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

/*
 * 保存
 */
function save(uuid,flag){
	var dataModel=$('#baseTable' + ('A' == flag?"Left":"Right")).bootstrapTable('getRowByUniqueId', uuid);
	var data = [];
	data.push(dataModel);
	saveData(data)
}
/*
 * select2下拉框
 */
function editableSelect(source){
	return {
		type: 'select2',
		onblur : "ignore",
		source: data[source]
	}
}

/*
 * 可编辑文本框
 */
function editableText(msg){
	return {
		type : 'text',
//		mode: "popup",//编辑框的模式：支持popup和inline两种模式
		validate : function(value) {
			if ($.trim(value) == '' && msg !='') {
				return msg;
			} 
		}
	}
}

/*
 * 时间格式校验
 */
function filterText(text){
	var rep =new RegExp("^(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))[0-5]{1}[0-9]{1}[+]?$");
	return rep.test(text);
}

function filterText1(text){
	var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d$/;
	var regExp = new RegExp(reg);
	return regExp.test(text);
}

/*
 * 删除选中行
 */
function deleteRow(){
	var ids = $.map($("#baseTable").bootstrapTable('getSelections'), function (row) {
        return row.order;
    });
    if (ids.length < 1 ) {
        layer.msg("请选择行！",{time:600,icon:0});
        return;
    }
    $("#baseTable").bootstrapTable('remove', {
        field: 'order',
        values: ids
    });
}

// 根据判断修改按钮是否加载判断用户的编辑权限，决定可编辑字段颜色
function isClassBlue() {
	var cla = "color-blue";
	if ($("#modify").length == 0) {
		cla = undefined;
	}
	return cla;
}

/*
 * 批量删除
 */
function deleteBatch(){
	var dataLeft = $.map($("#baseTableLeft").bootstrapTable('getSelections'), function (row) {
        return row;
    });
	var dataRight = $.map($("#baseTableRight").bootstrapTable('getSelections'), function (row) {
        return row;
    });
	if (dataLeft.length < 1 && dataRight.length < 1) {
        layer.msg("请选择删除数据！",{time:600,icon:0});
        return;
    }
	var data = [];
	$.each(dataLeft,function(index,item){
		data.push(item);
	})
	$.each(dataRight,function(index,item){
		data.push(item);
	})
	deleteData(data);
}

function deleteData(data){
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/tomorrow/plan/deleteBatch",
		async : true,
		data : {
			data : JSON.stringify(data)
		},
		dataType : "text",
		success : function(result) {
			if (result == 'true') {
				refreshTable();
				var ids = $.map(data, function (row) {
			        return row.id;
			    });
				removeSelection(ids);
			} else {
				layer.msg("操作失败",{time:1000,icon:2});
			}
			layer.close(loading);
		},
		error:function(msg){
			var result = "操作失败3";
			layer.msg(result, {icon: 1,time: 1000});
			layer.close(loading);
		}
	});
}

/*
 * 批量保存
 */
function saveBatch(){

	var dataLeft = $.map($("#baseTableLeft").bootstrapTable('getSelections'), function (row) {
        return row;
    });
	var dataRight = $.map($("#baseTableRight").bootstrapTable('getSelections'), function (row) {
        return row;
    });
	if (dataLeft.length < 1 && dataRight.length < 1) {
        layer.msg("请选择保存数据！",{time:600,icon:0});
        return;
    }
	
	var data=[];
	$.each(dataLeft,function(index,item){
		data.push(item);
	})
	$.each(dataRight,function(index,item){
		data.push(item);
	})
	saveData(data);
}

function saveData(data){
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/tomorrow/plan/saveBatch",
		async : true,
		data : {
			data : JSON.stringify(data)
		},
		dataType : "text",
		success : function(result) {
			if (result == 'true') {
				parent.refreshTable();
				var ids = $.map(data, function (row) {
			        return row.id;
			    });
				removeSelection(ids);
			} else {
				layer.msg("操作失败",{time:1000,icon:2});
			}
			layer.close(loading);
		},
		error:function(msg){
			var result = "操作失败2";
			layer.msg(result, {icon: 1,time: 1000});
			layer.close(loading);
		}
	});
}
/*
 * 表格删除选中
 */
function removeSelection(ids){
    $("#baseTableLeft").bootstrapTable('remove', {
        field: 'id',
        values: ids
    });
    $("#baseTableRight").bootstrapTable('remove', {
        field: 'id',
        values: ids
    });
}
/*
 * 白色
 */
function isClassWhite() {
	var cla = "color-white";
	return cla;
}

function refreshTable(){
	$("#baseTableLeft").bootstrapTable('refresh');
	$("#baseTableRight").bootstrapTable('refresh');
}
/*
 * 红色
 */
function isClassRed() {
	var cla = "color-red";
	return cla;
}