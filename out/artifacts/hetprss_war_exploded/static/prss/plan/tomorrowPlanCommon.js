
var updateUrl = ctx + "/tomorrow/plan/update";

/*
 * 创建列
 */
function column(field,sortable,title,visible,editable,formatter,classStr,checkbox){
	var column = {};
	column.field = field;
	column.sortable = sortable;
	column.title = title;
	column.visible = visible;
	if(editable)
		column.editable = editable;
	if(formatter)
		column.formatter = formatter;
	if(classStr)
		column.class = classStr;
	column.checkbox = checkbox;
	return column;
}
/*
 * 构建列
 */
function columns(depart,attival){
	var columns = [];
		columns.push(column("checkbox",false,"",true,false,null,null,true));
	columns.push(column("order",false,"序号",true,false,function(value, row, index) {return index + 1;},null));
	columns.push(column("flt_no",false,"航班号",true,editableText1(updateUrl,'航班号不能为空!'),nullFormatter,isClassWhite()));
	columns.push(column("share_flt_no",false,"共享航班号",true,editableText(updateUrl,''),nullFormatter,isClassWhite()));
	columns.push(column("aircraft_number",false,"机号",true,editableSelect1('',"AIRCRAFT_NUMBER"),nullFormatter,isClassWhite()));
	columns.push(column("act_type",false,"机型",true,editableSelect('',"ACT_TYPE"),nullFormatter,isClassWhite()));
	if(depart)
	columns.push(column("depart_apt",false,"起场",true,editableText(updateUrl,''),nullFormatter,isClassWhite()));
	if(attival)
	columns.push(column("arrival_apt",false,"落场",true,editableText(updateUrl,''),nullFormatter,isClassWhite()));
	columns.push(column("std",false,"计起",true,editableText(updateUrl,''),function(value, row, index) {return (value ? value : '');},isClassWhite()));
	columns.push(column("sta",false,"计落",true,editableText(updateUrl,''),function(value, row, index) {return (value ? value : '');},isClassWhite()));
	columns.push(column("etd",false,"预起",true,editableText(updateUrl,''),function(value, row, index) {return (value ? value : '');},isClassWhite()));
	columns.push(column("eta",false,"预落",true,editableText(updateUrl,''),function(value, row, index) {return (value ? value : '');},isClassWhite()));
	columns.push(column("property_code",false,"性质",true,editableSelect('',"PROPERTY_CODE"),null,isClassWhite()));
	columns.push(column("attr_code",false,"属性",true,editableSelect('',"ATTR_CODE"),null,isClassWhite()));
	columns.push(column("airline_code",false,"航空公司",true,editableSelect('',"AIRLINE_CODE"),null,isClassWhite()));
	columns.push(column("flt_date",false,"日期",true,false,function(value, row, index) {
		return "<a href='javascript:void(0)' id='fltTime_" + index + "' class='color-white'>"
		+ (value ? value : getNextFormatDate()) + "</a>";
	},isClassWhite()));
	return columns;
}

/*
 * select2下拉框
 */
function editableSelect(pk,source){
	return {
		type: 'select2',
//		onblur:'ignore',
		source: data[source]
	}
}

function editableSelect1(pk,source){
	return {
		type : "select2",
		source : data[source],
		display : function(value){
			$(this).html(value.toUpperCase());
		}
	}
}

/*
 * 可编辑文本框
 */
function editableText(url,msg){
	return {
		type : 'text',
		validate : function(value) {
			if ($.trim(value) == '' && msg !='') {
				return msg;
			} 
		}
	}
}

function editableText1(msg){
	return {
		type:'text',
		display : function(value){
			$(this).html(value.toUpperCase());
		},
		validate : function(value) {
			if ($.trim(value) == '' && msg !='') {
				return msg;
			} 
		}
	}
}

/*
 * 获取今日日期
 */
function getNowFormatDate() {
	var date = new Date();
	date.setDate(date.getDate());
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	return year+""+month+""+day;
} 
/*
 * 获取明日日期
 */
function getNextFormatDate() {
	var date = new Date();
	date.setDate(date.getDate() + 1);
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	return year+""+month+""+day;
} 

/*
 * 弹出日期控件
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
			}
		}
	});
}

/*
 * 修改保存
 */
function editableSave(field, row, oldValue, $el) {
	if((field == "STD" || field == "STA" || field == "ETA" || field == "ETD") && !filterText(row[field])){
		layer.msg('校验失败，请重新编辑！', {
			icon : 0,
			time : 600
		});
	} else if(row[field] != oldValue){
		$.ajax({
			type : 'post',
			url : ctx + "/tomorrow/plan/update",
			data : {
				'id' : row.ID,
				'name' : field,
				'value' : row[field]
			},
			success : function(res) {
				if (res == "overTime") {// 与加载时数据的时间戳对比，有变化说明已被改动，需要重新加载数据
					layer.msg('数据已失效，即将刷新数据', {
						icon : 2
					});
					setTimeout(function() {
						refreshTable();
					}, 1000);
				} else if (res == "error") {
					layer.msg('保存失败！', {
						icon : 2
					});
				}
			}
		});
	}
}

/**
 * 起场表格编辑提交
 */
function editableLeftSave(field, row, oldValue, $el){
	if(checkPublished(field, row, oldValue, $el,'Left')){
		modifyLinkValue(field, row, $el,'Left');
		updateFun(field, row, oldValue);
	}
}

/**
 * 落场表格编辑提交
 */
function editableRightSave(field, row, oldValue, $el){
	if(checkPublished(field, row, oldValue, $el,'Right')){
		modifyLinkValue(field, row, $el,'Right');
		updateFun(field, row, oldValue);
	}
}

/**
 * 校验是否修改已发布数据
 */
function checkPublished(field, row, oldValue, $el,tableType){
	var checkRs = true;
	//发布状态 0、新增未发布，1、修改未发布，2、已发布
	var dataState = row["data_state"];
	if(dataState==2){
		var dataIndex = $el.parent().parent().index();
		$("#baseTable"+tableType).bootstrapTable("updateCell",{
			index:dataIndex,
			field:field,
			value:oldValue
		});
		layer.msg('请在航班动态中对已发布计划进行修改、删除', {icon : 2});
		checkRs = false;
	}
	return checkRs;
}

/**
 * 修改记录联动值，航班号、机场三字码
 */
function modifyLinkValue(field, row, $el,tableType){
	var dataIndex = $el.parent().parent().index();
	var value = row[field];
	if(field=="flt_no"){
		$("#baseTable"+tableType).bootstrapTable("updateCell",{
			index:dataIndex,
			field:"airline_code_v",
			value:value.substring(0, 3).toUpperCase()
		});
		$("#baseTable"+tableType).bootstrapTable("updateCell",{
			index:dataIndex,
			field:"flt_no",
			value:value.toUpperCase()
		});
	}
	
	if(field=="airline_code_v"){
		$("#baseTable"+tableType).bootstrapTable("updateCell",{
			index:dataIndex,
			field:"flt_no",
			value:value.toUpperCase()+row["flt_no"].substring(3, row["flt_no"].length)
		});
	}
}

/*
 * 白色
 */
function isClassWhite() {
	var cla = "color-white";
	return cla;
}

function nullFormatter(value, row, index) {
	return (value ? value : '');
}

/**
 * 校验sta std字符串格式,"小时分钟+"
 */
function checkStaStdFormat(s){
	var patrn = /^(20|21|22|23|[0-1]\d)[0-5]\d\+?$/;
	if (!patrn.exec(s))
		return false
	return true;
}

function updateFun(field, row, oldValue) {
	if(row[field] == oldValue)
		return;
	$.ajax({
		type : 'post',
		url : ctx + "/tomorrow/plan/update",
		data : {
			'name' : field,
			'rowJsonValue' : JSON.stringify(row)
		},
		success : function(data) {
			if(data!="succeed"){
				layer.alert("次日计划修改失败！"+data, {icon: 2});
				$("#baseTableLeft").bootstrapTable('refresh');
				$("#baseTableRight").bootstrapTable('refresh');
			}
		},error:function(msg){
			layer.alert("次日计划修改失败！", {icon: 2});
			$("#baseTableLeft").bootstrapTable('refresh');
			$("#baseTableRight").bootstrapTable('refresh');
		}
	});
}
