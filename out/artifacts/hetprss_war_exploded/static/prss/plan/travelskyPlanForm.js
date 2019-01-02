var page = 1;
var limit = 100;
var tableData;
var baseTable;
var disabled = false;
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
		url : ctx+"/plan/travelskyPlan/formGridData", // 请求后台的URL（*）
		uniqueId:"id",
		queryParams : function(params) {
			var param = {
				fltDate:$("#fltDate").val(),
				fltNo:$("#fltNo").val(),
				isNew:$("#isNew").val()
			};
			return param;
		},
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : true, // 是否显示全选
		toolbar : $("#tool-box"), // 指定工具栏dom
		search : false, // 是否开启搜索功能
		editable:true,//开启编辑模式
		searchOnEnterKey : true,
		onClickCell : function(field, value, row, $element) {
			if(field == "fltDate"){
				popupDate(field, value, row, $element);
			}
		},
		onEditableSave : function(field, row, oldValue, td) {
			if (!isNewRow(row.id)) {
				editRows[row.id] = $.extend(true,{},row);
			}
			var rowIndex = td.parent().data("index");
			var value = row[field];
			//航班号三字码大写，航空公司联动
			if(field=="fltNo"){
				//共享航班号
				$.post(ctx + "/plan/travelskyPlan/getShareFltNo", {
					fltNo : value
				}, function(shareFltNo) {
					row['shareFltNo'] = shareFltNo;
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
							show(edit);
						}
					}else{
						show(ele.next());
					}
				}
			}
        },
		onLoadSuccess:function(data){
			var count = data.length;
			//默认增加2个空行
			if(count==0){
				for(var i=0;i<2;i++){
					addRow();
				}
			}
		},
		columns : createDetailColumns
	};
	

	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
	// 编辑事件
	//addRow();
})
var createDetailColumns = [ {
	field : "checkbox",
	checkbox : true,
	sortable : false,
	title : "",
	visible : true,
	editable : false
}, {
	field : "order",
	title : "序号",
	sortable : false,
	editable : false,
	formatter : function(value, row, index) {
		return index + 1;
	}
}, {
	field : "id",
	title : "ID",
	visible : false
}, {
	field : "fltNo",
	title : "航班号",
	editable : {
		type:'text',
		display : function(value){
			$(this).html(value.toUpperCase());
		},
		validate: function (value) {  
            if ($.trim(value) == '') {
                return '航班号不能为空!';  
            }  
        }
	}
},{
	field : "fltDate",
	title : "执行日期",
	width : "60px",
	formatter : function(value, row, index) {
		return "<a href='javascript:void(0)' id='fltDate" + index + "'>" + (value ? value : '请选择') + "</a>";
	}
}, {
	field : "std",
	title : "计起",
	editable : {
		type:'text',
		placeholder:"0000~2359",
		validate: function (value) {
            if ($.trim(value) == '') {
                return '计起不能为空!';
            }
            if(!checkStaStdFormat(value)){
				return '计起时间格式不正确!格式(24小时制)：小时分钟+';
			}
        }
	}
},  {
	field : "sta",
	title : "计落",
	editable : {
		type:'text',
		placeholder:"0000~2359",
		validate: function (value) {
            if ($.trim(value) == '') {
                return '计落不能为空!';
            }
            if(!checkStaStdFormat(value)){
				return '计落时间格式不正确!格式(24小时制)：小时分钟+';
			}
        }
	}
}, {
	field : "departApt",
	title : "起场",
	editable : {
		type : "select2",
		onblur : "ignore",
		source : airportCodeSource,
		select2 : {
			placeholder : "请选择起场",
			width : "80px",
			matcher: aptMatcher
		},
		validate: function (value) {
            if ($.trim(value) == '') {
                return '起场不能为空!';  
            }  
        }
	}
},  {
	field : "arrivalApt",
	title : "落场",
	editable : {
		type : "select2",
		onblur : "ignore",
		source : airportCodeSource,
		select2 : {
			placeholder : "请选择落场",
			width : "80px",
			matcher: aptMatcher
		},
		validate: function (value) {
            if ($.trim(value) == '') {
                return '落场不能为空!';  
            }  
        }
	}
}, {
	field : "actType",
	title : "机型",
	editable : {
		type : "select2",
		source : actTypeSource,
		select2 : {
			placeholder : "请选择机型",
			width : "80px"
		},
		validate: function (value) {
            if ($.trim(value) == '') {
                return '机型不能为空!';  
            }
        }
	}
}, {
	field : "shareFltNo",
	title : "共享航班号",
	editable : {
		type:'text',
		display : function(value){
			value = value + "";
			$(this).html(value.toUpperCase());
		},
	}
}];

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

function addRow() {
	$("#baseTable").bootstrapTable("append",{
		id:"new_"+(new Date().getTime()),
		fltNo : "",
		fltDate : "",
		std : "",
		sta : "",
		departApt : "",
		arrivalApt : "",
		actType : "",
		shareFltNo : ""
	});
}
function deleteRow(){
	var selections = $("#baseTable").bootstrapTable("getSelections");
	if(selections&&selections.length>0){
		for(var i=0;i<selections.length;i++){
			var row = selections[i];
			if(row&&row.id){
				if (!isNewRow(row.id)) {
					removeRows[row.id] = $("#baseTable").bootstrapTable("getRowByUniqueId",row.id);
				}
				$("#baseTable").bootstrapTable("removeByUniqueId",row.id);
			}
		}
	}
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
//	var data = $("#baseTable").bootstrapTable("getData");
//	if (data.length < 1 ) {
//        layer.msg("请新增行!",{time:600,icon:0});
//        return;
//    }
//	var flag = true;
//	$.each(data,function(index,item){
//		var fltNo = item.fltNo;
//		var fltWeek = item.FLT_WEEK;
//		if(fltNo == null || fltNo ==''){
//			layer.msg("航班号不能为空!",{time:1000,icon:0});
//			flag = false;
//			return;
//		}
//		item.sta = formatFltTime(item.fltDate,item.sta);
//		item.std = formatFltTime(item.fltDate,item.std);
//	})
//	if(!flag)
//		return;
	
	var dataArr = $("#baseTable").bootstrapTable("getData");
	if(dataArr.length<1){
		layer.msg("请新增行!",{time:2500,icon:2});
		return;
	}
	var columns = [{field:"fltNo",title:"航班号"},{field:"fltDate",title:"执行日期"},{field:"std",title:"计起"},{field:"sta",title:"计落"},{field:"departApt",title:"起场"},{field:"arrivalApt",title:"落场"},{field:"actType",title:"机型"},{field:"shareFltNo",title:"共享航班号"}];
	var newRows = [];//新增的行
	for(var i=0;i<dataArr.length;i++){
		var data = $.extend(true,{},dataArr[i]);
		for(var j=0;j<columns.length;j++){
			var column = columns[j];
			var field = column.field;
			var title = column.title;
			if(field!='shareFltNo'&&!data[field]){
				layer.msg("第"+(i+1)+"行"+title+"不能为空！",{icon:7});
				return false;
			}
			if(field=='sta'||field=='std'){
				var val = data[field];
				if(!checkStaStdFormat(val)){
					layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
					return false;
				}
			}
		}
		data.sta = formatFltTime(data.fltDate,data.sta);
		data.std = formatFltTime(data.fltDate,data.std);
		data.shareFltNo = (data.shareFltNo+"").toUpperCase();
		//新增的行
		newRows.push(data);
	}
	//判断是否有修改的数据
	if(newRows.length==0){
		layer.msg("没有需要保存的内容！",{icon:7});
		return false;
	}
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/plan/travelskyPlan/save",
		async : true,
		data : {
			planDetail : JSON.stringify(newRows)
		},
		dataType : "text",
		success : function(result) {
			layer.close(loading);
			if (result == 'success') {
				parent.layer.close(index);
				//parent.refreshTable("#arrivalGrid");
				//parent.refreshTable("#departureGrid");
			} else {
				layer.alert("中航信计划保存失败！"+result, {icon: 2});
			}
		},
		error:function(msg){
			layer.close(loading);
			layer.alert("中航信计划保存失败！"+result, {icon: 2});
		}
	});
}
/**
 * 校验sta std字符串格式
 */
function checkStaStdFormat(s){
	var patrn = /^(20|21|22|23|[0-1]\d)[0-5]\d\+?$/;
	if (!patrn.exec(s))
		return false
	return true;
}
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
function getNowFormatDate() {
	var date = new Date();
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	var currentdate = year+""+month+""+day;
    return currentdate;
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
 * 获取表格数据
 */
var editRows = {};//修改的行
var removeRows = {};//删除的行
function getGridData(){
	var dataList = $("#baseTable").bootstrapTable("getData");
	var columns = createDetailColumns;
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
				if(field!='checkbox'&&field!='rowNum'&&field!='shareFltNo'&&field!='id'&&field!='order'&&!data[field]){
					layer.msg("第"+(i+1)+"行"+title+"不能为空！",{icon:7});
					return false;
				}
				if(field=='sta'||field=='std'){
					var val = data[field];
					if(checkStaStdFormat(val)){
						var fltDate = data["fltDate"];
						var fltTime = formatFltTime(fltDate,val);
						data[field] = fltTime;
					} else {
						layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
						return false;
					}
				}
			}
			//新增的行
			if(data.id&&isNewRow(data.id)){
				delete data["id"];
				data.sta = formatFltTime(data.fltDate,data.sta);
				data.std = formatFltTime(data.fltDate,data.std);
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