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
	// 基本表格选项
	var tableOptions = {
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : false, // 是否显示全选
		toolbar : $("#tool-box"), // 指定工具栏dom
		search : false, // 是否开启搜索功能
		editable:true,//开启编辑模式
		searchOnEnterKey : true,
		responseHandler : function(res) {
			tableData = res;
			return res.slice(0, limit);
		},
		customSearch : function(text) {
			var resultData = [];
			if (text) {
				var searchDate =text.trim().toUpperCase();
				if (searchDate) {
					for (var i = 0; i < tableData.length; i++) {
						var rowData = tableData[i];
						for ( var key in rowData) {
							if (rowData[key]&&rowData[key].toString().indexOf(searchDate) >-1) {
								resultData.push(rowData);
								break;
							}
						}
					}
				}else if (searchDate=="") {
					resultData=tableData;
				}
				this.data=resultData;
			}
		},
		columns : columns(false),
		onDblClickCell : onDblClickCell,// 班期 $("#modify").click();
		onEditableSave : function(field, row, oldValue, td) {
			var rowIndex = td.parent().data("index");
			var value = row[field];
			if(field=="FLT_NO"){
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
		},
		onEditableShown: function(field, row, $el, editable) {
			setTimeout(function(){
				$el.next().find(".select2-hidden-accessible").select2('open');
			},100);
        },
		onEditableHidden: function(field, row, $el, reason) {
			$el.next().find(".select2-hidden-accessible").select2('close');
			show($el.parent());
			function show(ele){
				if (ele.next() && ele.next().is("td")) {
					var edit = ele.next().find('.editable');
					if(edit.length > 0){
						if(edit.text() == "" || edit.text() == null){
							edit.editable('show');
						}else{
							show(edit.parent);
						}
					}else{
						show(ele.next());
					}
				}
			}
        },
		onClickCell : function(field, value, row, $element) {
			if(field == 'PLAN_DATE_BEGIN' || field == 'PLAN_DATE_END'){
				popupDate(field, value, row, $element,'yyyyMMdd');
			} 
		}
	};
	
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
	// 编辑事件
	var wdateOpt = {};
	function onDblClickCell(field, value, row, td) {
		if (field == "FLT_WEEK" ) {// 班期
			createFltWeek(field, value, row, td);
		}
	}
	addRow();
	addRow();//gaojd 20180604 add
})
var rowIndex =0;
function addRow(){
	$("#baseTable").bootstrapTable("insertRow", {
		index : rowIndex++,
		row :{"order" : rowIndex++,"FLT_NO":"","ACT_TYPE":"","FLT_WEEK":"","DEPART_APT":"","STD":"","ARRIVAL_APT":""
			,"STA":"","SHARE_FLT_NO":"","ATTR_CODE":"","PLAN_DATE_BEGIN":getNowFormatDate(),"PLAN_DATE_END":getNowFormatDate(),"REMARK":""}
	});
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

function save(){
	var dataList = $("#baseTable").bootstrapTable("getData");
	if (dataList.length < 1 ) {
        layer.msg("请新增行！",{time:600,icon:0});
        return;
    }
	var columns = [{field : "checkbox",title : ""}, {field : "order",	title : "序号"}, {field : "FLT_NO",title : "航班号"},{field : "ACT_TYPE",title : "机型"},{field : "FLT_WEEK",title : "班期"},{field : "DEPART_APT",title : "起场"},{field : "STD",title : "计起时间"},{field : "ARRIVAL_APT",title : "落场"},{field : "STA",title : "计落时间"},{field : "SHARE_FLT_NO",title : "共享航班号"}, {field : "ATTR_CODE",title : "属性"}, {field : "PLAN_DATE_BEGIN",title : "起始日期"}, {field : "PLAN_DATE_END",title : "终止日期"},{field : "REMARK",title : "备注"} ];
	var newRows = [];//新增的行
	for(var i=0;i<dataList.length;i++){
		var data = $.extend(true,{},dataList[i]);
			for(var j=0;j<columns.length;j++){
				var column = columns[j];
				var field = column.field;
				var title = column.title;
				if(field!='checkbox'&&field!='order'&&field!='SHARE_FLT_NO'&&!data[field]){
					layer.msg("第"+(i+1)+"行"+title+"不能为空！",{icon:7});
					return false;
				}
				if(field=='STA'||field=='STD'){
					var val = data[field];
					if(!filterText(val)){
						layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
						return false;
					}
				}
			}
			//新增的行
			data.SHARE_FLT_NO = (data.SHARE_FLT_NO+"").toUpperCase();
			newRows.push(data);
	}
	//判断是否有保存的数据
	if(newRows.length==0){
		layer.msg("没有需要保存的内容！",{icon:7});
		return false;
	}
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/caac/plan/save",
		async : true,
		data : {
			data : JSON.stringify(newRows)
		},
		dataType : "text",
		success : function(result) {
			layer.close(loading);
			if (result == 'success') {
				parent.layer.close(index);
				parent.refreshTable();
			} else {
				layer.msg(result,{time:1500,icon:2});
			}
		},
		error:function(msg){
			layer.close(loading);
			var result = "操作失败";
			layer.msg(result, {icon: 1,time: 1000});
		}
	});
}

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
			}
		}
	});
}
