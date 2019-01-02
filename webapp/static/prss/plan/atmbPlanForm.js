layui.use(["form","layer"]);
$(document).ready(function(){
	initGrid();
	//添加机号
	$("#aircraftBtn").click(function(){
		doAddMaintain(5,"parent");
	});
	//添加机型
	$("#actTypeBtn").click(function(){
		doAddMaintain(2,"parent");
	});
	//添加机场
	$("#airportBtn").click(function(){
		doAddMaintain(1,"parent");
	});
	//添加航空公司
	$("#alnBtn").click(function(){
		doAddMaintain(3,"parent");
	});
});
function initGrid() {
	$("#dataGrid").bootstrapTable({
		url : ctx+"/plan/atmbPlan/formGridData", // 请求后台的URL（*）
		queryParams : function(params) {
			var param = {
				fltDate:$("#fltDate").val(),
				fltNo:$("#fltNo").val(),
				isNew:$("#isNew").val(),
				ioType:$("#ioType").val(),
				ids:$("#ids").val()
			};
			return param;
		},
		uniqueId:"id",
		onLoadSuccess:function(data){
			var count = data.length;
			//默认增加2个空行
			if(count==0){
				for(var i=0;i<2;i++){
					addRow();
				}
			}
		},
		onClickCell : function(field, value, row, $element) {
			//航班日期
			if (field == "fltDate") {
				popupDate(field, value, row, $element);
			}
		},
		onEditableSave : function(field, row, oldValue, $el) {
			var rowIndex = $el.parent().parent().index();
			var value = row[field];
			var tr = $el.parents("tr");
			if (!isNewRow(row.id)) {
				editRows[row.id] = $.extend(true,{},row);
			}
			// 航班号联动航空公司、共享航班号
			if ("fltNo" == field && value && value.length >= 3) {
				//航空公司
				var val = value.substring(0, 3).toUpperCase();
				updateCell(rowIndex,val,"airlineCode",tr);
				//共享航班号
				$.post(ctx + "/plan/atmbPlan/getShareFltNo", {
					fltNo : value
				}, function(shareFltNo) {
					updateCell(rowIndex,shareFltNo,"shareFltNo",tr);
				})
			} else 
			//航空公司联动航班号
			if(field=="airlineCode"){
				var val = value.toUpperCase() + row["fltNo"].substring(3, row["fltNo"].length);
				updateCell(rowIndex,val,"fltNo",tr);
			} else 
			// 机号机型联动
			if ("aircraftNumber" == field) {
				$.post(ctx + "/plan/atmbPlan/getActType", {
					aftNum : value
				}, function(act) {
					updateCell(rowIndex,act,"actType",tr);
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
				if ($el.parent().next() && $el.parent().next().is("td")) {
					var edit = $el.parent().next().find('.editable');
					if(edit.text() == "" || edit.text() == null){
						edit.editable('show');
					}
				}
			}
        },
		method : "get", // 请求方式（*）
		dataType : "json",
		search : false,
		pagination : false,
		columns : getColumns()
	});
}
function updateCell(rowIndex,value,field,tr){
	var dataList = $("#dataGrid").bootstrapTable("getData");
	dataList[rowIndex][field] = value;
	var columns = getColumns();
	var index = 0;
	for(var i=0;i<columns.length;i++){
		if(columns[i].field == field){
			index = i;
			break;
		}
	}
	tr.find("td").eq(index).find(".editable").editable('setValue',value,true);
}
/**
 * 表头信息
 * @returns {Array}
 */
function getColumns() {
	var columns = [
			{
				field : "checkbox",
				checkbox : true,
				sortable : false,
				editable : false
			},
			{
				field : "rowNum",
				title : "序号",
				width : '40px',
				align : 'center',
				valign : 'middle',
				formatter : function(value, row, index) {
					return index + 1;
				}
			},
			{
				field : "fltNo",
				title : "航班号",
				width : "60px",
				editable :{
					type:"text",
					display : function(value){
						$(this).html(value.toUpperCase());
					}
				}
			},
			{
				field : "shareFltNo",
				title : "共享航班号",
				width : "60px",
				editable :{
					type:"text",
					display : function(value){
						$(this).html(value.toUpperCase());
					}
				}
			},
			{
				field : "aircraftNumber",
				title : "机号",
				width : "60px",
				editable :{
					type:"text",
					display : function(value){
						$(this).html(value.toUpperCase());
					}
				}
			},
			{
				field : "actType",
				title : "机型",
				width : "60px",
				editable : {
					type : "select2",
					//onblur:'ignore',
					source : actTypes
				}
			},
			{
				field : "departApt",
				title : "起场",
				width : "60px",
				editable : {
					//onblur:'ignore',
					type : "select2",
					source : airports,
					select2:{
					    matcher: aptMatcher
					}
				}
			},
			{
				field : "arrivalApt",
				title : "落场",
				width : "60px",
				editable : {
					//onblur:'ignore',
					type : "select2",
					source : airports,
					select2:{
					    matcher: aptMatcher
					}
				}
			},
			{
				field : "std",
				title : "计起",
				width : "60px",
				editable : {
					type:"text",
					width:"200px",
					placeholder:"0000~2359"
				}
			},
			{
				field : "sta",
				title : "计落",
				width : "60px",
				editable : {
					type:"text",
					placeholder:"0000~2359"
				}
			},
			{
				field : "propertyCode",
				title : "性质",
				width : "60px",
				editable : {
					type : "select2",
					//onblur:'ignore',
					source : propertys
				}
			},
			{
				field : "airlineCode",
				title : "航空公司",
				width : "60px",
				editable : {
					type : "select2",
					//onblur:'ignore',
					source : airlines,
					select2:{
					    matcher: alnLineMatcher
					}
				}
			},
			{
				field : "fltDate",
				title : "日期",
				width : "60px",
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0)' id='fltDate" + index + "'>" + (value ? value : '请选择') + "</a>";
				}
			} ];
	return columns;
}

function alnLineMatcher(params, data) {
	var search = data.text+data.id+data.airline_code;
	if ($.trim(params.term) === '') {
      return data;
    }
    if (search.toUpperCase().indexOf(params.term.toUpperCase()) > -1) {
      var modifiedData = $.extend({}, data, true);
      return modifiedData;
    }
    return null;
}

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
/**
 * 新增空行
 */
function addRow() {
	$("#dataGrid").bootstrapTable("append",{
		id:"new_"+(new Date().getTime()),
		fltNo : "",
		shareFltNo : "",
		aircraftNumber : "",
		actType : "",
		departApt : "",
		arrivalApt : "",
		propertyCode : "",
		std : "",
		sta : "",
		airlineCode : "",
		fltDate : getFltDate()
	});
}
function removeRow(){
	var selections = $("#dataGrid").bootstrapTable("getSelections");
	if(selections&&selections.length>0){
		for(var i=0;i<selections.length;i++){
			var row = selections[i];
			if(row&&row.id){
				if (!isNewRow(row.id)) {
					removeRows[row.id] = $("#dataGrid").bootstrapTable("getRowByUniqueId",row.id);
				}
				$("#dataGrid").bootstrapTable("removeByUniqueId",row.id);
			}
		}
	}
}
/**
 * 弹出日期控件
 * @param field
 * @param value
 * @param row
 * @param td
 */
function popupDate(field, value, row, $element){
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
		dateFmt:"yyyyMMdd",
		onpicked : function(dp) {
			var newValue = dp.cal.getDateStr();
			if (newValue) {
				row[field] = newValue;
			}
		}
	});
}
/**
 * 获取表格数据
 */
var editRows = {};//修改的行
var removeRows = {};//删除的行
function getGridData(){
	var dataList = $("#dataGrid").bootstrapTable("getData");
	var columns = getColumns();
	var newRows = [];//新增的行
	for(var i=0;i<dataList.length;i++){
		var data = $.extend(true,{},dataList[i]);
		var isEmpty = true;
		for(var key in data){
			if (key != 'id' && data[key]) {
				isEmpty = false;
				break;
			}
		}
		if(!isEmpty){
			for(var j=0;j<columns.length;j++){
				var column = columns[j];
				var field = column.field;
				var title = column.title;
				if(field!='checkbox'&&field!='rowNum'&&field!='shareFltNo'&&field!='aircraftNumber'&&!data[field]){
					layer.msg("第"+(i+1)+"行"+title+"不能为空！",{icon:7});
					return false;
				}
				//如果计落小于计起，或者预落小于预起，或者预起小于计起，均无法保存
				/**
				 * 三种情况下校验不通过
				 * 1 前>后 && 都没有+号
				 * 2 前>后 && 都有+号
				 * 3 前<后 && 前有+ && 后无+
				 */
				if(field=='std'){
					if(data['sta']<data['std']){
						if((data['std'].indexOf("+")!=-1&&data['sta'].indexOf("+")!=-1)||
							(data['std'].indexOf("+")==-1&&data['sta'].indexOf("+")==-1)){
							layer.msg("计落小于计起，无法保存",{icon:7});
							 return false;
						} 
					}
					if(data['sta']>data['std']&&data['std'].indexOf("+")!=-1&&data['sta'].indexOf("+")==-1){
						 layer.msg("计落小于计起，无法保存",{icon:7});
						 return false;
					}
				}
				if(field=='sta'||field=='std'){
					var val = data[field];
					
					if(validFltTime(val)){
						var fltDate = data["fltDate"];
						var fltTime = formatFltTime(fltDate,val);
						data[field] = fltTime;
					}else{
						layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
						return false;
					}
					
					
				}
				
				 
			}
			//新增的行
			if(data.id&&isNewRow(data.id)){
				delete data["id"];
				newRows.push(data);
			} else if(data.id&&editRows[data.id]){//修改的行
				var editedRow = editRows[data.id];
				var fltDate = editedRow.fltDate;
				editedRow.std = formatFltTime(fltDate,editedRow.std);//转换std格式
				editedRow.sta = formatFltTime(fltDate,editedRow.sta);//转换sta格式
			}
		}
	}
	//判断是否有修改的数据
	if(newRows.length==0&&$.isEmptyObject(editRows)&&$.isEmptyObject(removeRows)){
		layer.msg("没有需要保存的内容！",{icon:7});
		return false;
	}
	var result = {
			newRows:newRows,
			editRows:editRows,
			removeRows:removeRows
	}
	return result;
}
/**
 * 判断是否为新增行
 * @param id
 * @returns
 */
function isNewRow(id){
	var reg = new RegExp("^new_");
	return reg.test(id);
}
/**
 * 校验计起、计落
 * @param str
 * @returns {Boolean}
 */
function validFltTime(str){
	var reg = new RegExp("[0-9]{1,}[+]?");
	if(reg.test(str)){
		if(str.indexOf("\+")==-1&&str.length!=4){
			return false;
		} else if(str.indexOf("\+")>0&&str.length!=5){
			return false;
		} else {
			var hh = Number(str.substring(0,2));
			var mi = Number(str.substring(2,4));
			if(hh<0||hh>23){
				return false;
			}
			if(mi<0||mi>59){
				return false;
			}
		}
	} else {
		return false;
	}
	return true;
}
/**
 * 转换航班时间为 yyyy-mm-dd hh24:mi格式
 * @param fltDate
 * @param fltTime
 */
function formatFltTime(fltDate,fltTime){
	var date = new Date(fltDate.substring(0,4)+"/"+fltDate.substring(4,6)+"/"+fltDate.substring(6,8));
	if(fltTime.indexOf("\+")>0){
		date.setDate(date.getDate() + 1);
	}
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (parseInt(date.getMonth()) + 1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	fltDate = year+"-"+month+"-"+day;
	return fltDate+" "+fltTime.substring(0,2)+":"+fltTime.substring(2,4);
}
/**
 * 获取次日日期
 */
function getFltDate(){
	var date = new Date();
	date.setDate(date.getDate() + 1);
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	return year+""+month+""+day;
}