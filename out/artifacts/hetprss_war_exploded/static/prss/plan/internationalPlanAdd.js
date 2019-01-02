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
		height:$(window).height(),
		searchOnEnterKey : true,
		onClickCell : function(field, value, row, td) {
			if (field == "fltWeek" ) {// 班期
				createFltWeek(field, value, row, td);
			}
			if (field == "fltDates" ) {// 固定执飞日期
				createFltDates(field, value, row, td);
			}
		},
		onEditableSave : function(field, row, oldValue, $el) {
			var dataIndex = $el.parent().parent().index();
			var value = row[field];
			var tr = $el.parents("tr");
			if(field=="fltNo"){
				//航班号、航空公司
				var val = value.substring(0, 3).toUpperCase();
				updateCell(dataIndex,val,"alnCode",tr);
				updateCell(dataIndex,value.toUpperCase(),"fltNo",tr);
				//共享航班号
				$.post(ctx + "/plan/interPlan/getShareFltNo", {
					fltNo : value
				}, function(shareFltNo) {
					updateCell(dataIndex,shareFltNo,"shareFltNo",tr);
				})
			}else if(field=="alnCode"){
				var val = value.toUpperCase() + row["fltNo"].substring(3, row["fltNo"].length);
				updateCell(dataIndex,val,"fltNo",tr);
			}else if ("aircraftNumber" == field) {
				// 机号机型联动
				$.post(ctx + "/plan/interPlan/getActType", {
					aftNum : value
				}, function(act) {
					if(act!='undefined'&&act!=null&&act!=''){
						updateCell(dataIndex,act.actype_code,"actType",tr);
						if(act.airline_code != row.alnCode){
							var actAirlineCode = '';
							if(act.airline_code!=undefined)
								actAirlineCode = act.airline_code;
							layer.msg("机号对应航空公司("+actAirlineCode+")与航班号对应航空公司不一致！",{time:3000,icon:0})
						}
					}else{
						layer.msg("没有匹配到填写机号相关信息！",{time:3000,icon:0})
					}
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
			if(field == "aircraftNumber"){
				if ($el.parent().next().next() && $el.parent().next().next().is("td")) {
					var edit = $el.parent().next().next().find('.editable');
					if(edit.text() == "" || edit.text() == null){
						edit.editable('show');
					}
				}
			}else{
				show($el.parent());
				function show(ele){
					if (ele.next() && ele.next().is("td")) {
						var edit = ele.next().find('.editable');
						var nextField = edit[0].attributes["data-name"].value;
							if(nextField!="fltWeek"&&nextField!="fltDays"&&nextField!="fltDates"){
							if(edit.text() == "" || edit.text() == null){
								edit.editable('show');
							}else{
								show(edit);
							}
						}else{
							show(ele.next());
						}
					}
				}
			}
        },
		columns : createDetailColumns
	};
	if(dataRows&&dataRows!=''){
		tableOptions.data = $.parseJSON(dataRows);// 拷贝或更新数据
		$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
	}else{
		$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
		addRow();
	}
})

var rowIndex =0;
function addRow(){
	var currDataArr = $("#baseTable").bootstrapTable("getData");
	if(currDataArr.length>0){
		rowIndex += currDataArr.length;
	}
	$("#baseTable").bootstrapTable("insertRow", {
		index : rowIndex++,
		row :{"order" : rowIndex++,"fltNo":"","shareFltNo":"","departApt":"","arrivalApt":"","std":"","sta":""
			,"otherApt":"","otherStd":"","otherSta":"","aircraftNumber":"","actType":"","propertyCode":"","alnCode":""
			,"fltPeriodTime":"","fltWeek":"","fltDays":"","fltDates":""}
	});
}

var createDetailColumns = [{
	field : "checkbox",
	checkbox : true,
	sortable : false,
	title : "",
	visible : true,
	editable : false
},  {
	field : "order",
	title : "序号",
	sortable : false,
	editable : false,
	formatter : function(value, row, index) {
		return index + 1;
	}
}, {
	field : "fltNo",
	title : "航班号",
	width : '80px',
	editable : {
		type:'text',
		display : function(value){
			$(this).html(value.toUpperCase());
		}
	}
},{
	field : "shareFltNo",
	title : "共享航班号",
	width : '80px',
	editable : {
		type:'text'
	}
}, {
	field : "departApt",
	title : "起场",
	width : "60px",
	editable : {
		type : "select2",
		onblur : "ignore",
		source : airportCodeSource,
		select2 : {
			matcher : aptMatcher
		}
	}
},{
	field : "arrivalApt",
	title : "落场",
	width : "60px",
	editable : {
		type : "select2",
		onblur : "ignore",
		source : airportCodeSource,
		select2 : {
			matcher : aptMatcher
		}
	}
},{
	field : "std",
	title : "计起",
	editable : {
		type:'text'
	}
},{
	field : "sta",
	title : "计落",
	editable : {
		type:'text'
	}
},{
	field : "otherApt",
	title : "联程机场",
	width : "60px",
	editable : {
		type : "select2",
		onblur : "ignore",
		source : airportCodeSource,
		select2 : {
			matcher : aptMatcher
		}
	}
},{
	field : "otherStd",
	title : "联程计起",
	editable : {
		type:'text'
	}
},{
	field : "otherSta",
	title : "联程计落",
	editable : {
		type:'text'
	}
},{
	field : "aircraftNumber",
	title : "机号",
	width : '60px',
	editable : {
		type:'text',
		display : function(value){
			$(this).html(value.toUpperCase());
		}
	}
},{
	field : "actType",
	title : "机型",
	width : '80px',
	editable : {
		type : "select2",
		onblur : "ignore",
		source : actTypeSource
	}
}, {
	field : "propertyCode",
	title : "性质",
	width : "80px",
	editable : {
		type : "select2",
		onblur : "ignore",
		source : propertyCodeSource,
	}
},{
	field : "alnCode",
	title : "航空公司",
	editable : {
		type : "select2",
		onblur : "ignore",
		source : airlinesCodeSource
	}
}, {
	field : "fltPeriodTime",
	title : "执飞时间段",
	editable : {
		type:'text'
	}
}, {
	field : "fltWeek",
	title : "班期",
	editable : false
},{
	field : "fltDays",
	title : "每隔天数",
	editable : {
		type:'text'
	}
},{
	field : "fltDates",
	title : "固定执飞日期",
	editable : {
		type:'text'
	}
}]

function aptMatcher(params, data) {
	var search = data.text+data.id+data.icao_code;
	if ($.trim(params.term) === '') {
      return data;
    }
    if (search.toUpperCase().indexOf(params.term.toUpperCase()) > -1) {
      var modifiedData = $.extend({}, data, true);
      return modifiedData;
    }
    return null;
}

function updateCell(dataIndex,value,field,tr){
	var dataList = $("#baseTable").bootstrapTable("getData");
	dataList[dataIndex][field] = value;
	var columns = createDetailColumns;
	var index = 0;
	for(var i=0;i<columns.length;i++){
		if(columns[i].field == field){
			index = i;
			break;
		}
	}
	tr.find("td").eq(index).find(".editable").editable('setValue',value,true);
}

/*
 * 删除选中行
 */
function deleteRow(){
	var ids = $.map($("#baseTable").bootstrapTable('getSelections'), function (row) {
        return row.order;
    });
    if (ids.length < 1 ) {
        layer.msg("请选择行!",{time:600,icon:0});
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
	var columns = createDetailColumns;
	var newRows = [];//新增的行
	for(var i=0;i<dataList.length;i++){
		var data = $.extend(true,{},dataList[i]);
		for(var j=0;j<columns.length;j++){
			var column = columns[j];
			var field = column.field;
			var title = column.title;
			if(field!='checkbox'&&field!='order'&&field!='shareFltNo'&&field!='aircraftNumber'
			&&field!='fltPeriodTime'&&field!='fltWeek'&&field!='fltDays'&&field!='fltDates'
			&&field!='otherApt'&&field!='otherStd'&&field!='otherSta'&&!data[field]){
				layer.msg("第"+(i+1)+"行"+title+"不能为空！",{icon:7});
				return false;
			}
			var val = data[field];
			if(field=='sta'||field=='std'||(field=='otherStd'&&val!='')||(field=='otherSta'&&val!='')){
				if(!checkStaStdFormat(val)){
					layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
					return false;
				}
			}
		}
		//新增的行
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
		url : ctx + "/plan/interPlan/save",
		async : true,
		data : {
			operateType : operateType,
			planDetail : JSON.stringify(newRows)
		},
		dataType : "text",
		success : function(result) {
			layer.close(loading);
			if (result == 'success') {
				parent.layer.close(index);
				parent.$("#planGrid").bootstrapTable('refresh');
				layer.msg("国际航班计划保存成功！",{time:2000,icon:1});
			}else if(result == 'fail'){
				layer.alert("国际航班计划保存失败！",{time:2000,icon:2});
			}else{
				layer.alert("国际航班计划保存异常！",{time:2000,icon:2});
			}
		}
	});
}
/**
 * 校验sta std字符串格式
 */
function checkStaStdFormat(s){
	var patrn = /^(20|21|22|23|[0-1]\d)[0-5]\d\+?$/;
	if (!patrn.exec(s))
		return false;
	return true;
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
			var times="";
			if(layero.find(".layui-form-checked").length<=0){
				layer.msg("请选择班期!",{icon:0,time:600});
				return;
			}
			
			layero.find(".layui-unselect").each(function(index,item) {
				var cls = $(item).attr("class");
				times += cls =="layui-unselect layui-form-checkbox layui-form-checked"?(index+1):".";
			})
			var oldValue = row.fltWeek;
			row.fltWeek = times;
			var currRowIndex = td.parent().data("index");
			$("#baseTable").bootstrapTable("updateRow", {
				index : currRowIndex,
				row : row
			});
			layer.close(index);
		}
	});
}

function createFltDates(field, value, row, td){
	$("#flightDatesVal").val(value);
	layer.open({
		type : 1,
		title : "固定执飞日期",
		content : $("#flightDates"),
		area: ['410px', '350px'],
		scrollbar:false,
		resize:false,
		btn : [ "确定" ],
		yes : function(index, layero) {
			var flightDatesVal=$("#flightDatesVal").val();
			var oldValue = row.fltDates;
			row.fltDates = flightDatesVal;
			var currRowIndex = td.parent().data("index");
			$("#baseTable").bootstrapTable("updateRow", {
				index : currRowIndex,
				row : row
			});
			layer.close(index);
		}
	});
}