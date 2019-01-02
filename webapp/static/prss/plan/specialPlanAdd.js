layui.use([ "form", "layer" ]);
var rowIndex = 0;
var index = parent.layer.getFrameIndex(window.name);
var uploadRowIndex = "";//上传行的下标
var oldFileId = "";//已经保存的文件id,重新上传,需要删除旧的文件
//var delTmpAttachmentFileType="allRow";//记录取消操作删除类型allRow所有行、delRow选择的行
$(document).ready(function() {
	var tableOptions = {
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : false, // 是否显示全选
		toolbar : $("#toolBox"),
		height:$(window).height(),
		search : false,
		editable:true,
		pagination : false,
		onClickCell : function(field, value, row, $element) {
			if(field == "fltDate"){
				popupDate(field, value, row, $element);
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
				$.post(ctx + "/plan/specialPlan/getShareFltNo", {
					fltNo : value
				}, function(shareFltNo) {
					updateCell(dataIndex,shareFltNo,"shareFltNo",tr);
				})
			}else if(field=="alnCode"){
				var val = value.toUpperCase() + row["fltNo"].substring(3, row["fltNo"].length);
				updateCell(dataIndex,val,"fltNo",tr);
			}else if ("aircraftNumber" == field) {
				// 机号机型联动
				$.post(ctx + "/plan/specialPlan/getActType", {
					aftNum : value
				}, function(act) {
					if(act!='undefined'&&act!=null&&act!=''){
						updateCell(dataIndex,act.actype_code,"actType",tr);
						var actAirlineCode = '';
						if(act.airline_code!=undefined)
							actAirlineCode = act.airline_code;
						layer.msg("机号对应航空公司("+actAirlineCode+")与航班号对应航空公司不一致！",{time:3000,icon:0})
					}else{
						layer.msg("没有匹配到填写机号相关信息！",{time:3000,icon:0})
					}
				});
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
							if(nextField!="attachmentId"&&nextField!="attachmentName"){
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
		columns : detailColumns
	};
	if(dataRows&&dataRows!=''){
		tableOptions.data = $.parseJSON(dataRows);// 更新数据
		$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
	}else{
		$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
		addRow();
	}
});

var detailColumns = [{
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
	field : "fltDate",
	title : "执行日期",
	width : "60px",
	formatter : function(value, row, index) {
		return "<a href='javascript:void(0)' id='fltDate" + index + "'>" + (value ? value : '请选择') + "</a>";
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
},{
	field : "attachmentName",
	title : "原有附件文件名",
	editable : false,
	visible : false
},{
	field : "attachmentId",
	title : "附件文件id",
	editable : false,
	visible : false
},{
	field : "tmpAttachmentName",
	title : "附件文件名",
	editable : false
},{
	field : "tmpAttachmentId",
	title : "临时附件文件id",
	editable : false,
	visible : false
},{
	field : "file",
	title : "附件",
	formatter : function(value, row, index) {
		return '<button type="button" value="'+index+'" style="height:auto;line-height:1.5" class="btn btn-md btn-link" onclick="uploadWin(this)">上传附件</button>';
	}
}]

function updateCell(rowIndex,value,field,tr){
	var dataList = $("#baseTable").bootstrapTable("getData");
	dataList[rowIndex][field] = value;
	var columns = $("#baseTable").bootstrapTable('getOptions').columns[0];
	var index = 0;
	for(var i=0;i<columns.length;i++){
		if(columns[i].field == field){
			index = i;
			break;
		}
	}
	tr.find("td").eq(index).find(".editable").editable('setValue',value,true);
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
 * 新增一行公务通航计划记录
 */
function addRow() {
	var currDataArr = $("#baseTable").bootstrapTable("getData");
	if(currDataArr.length>0){
		rowIndex += currDataArr.length;
	}
	$("#baseTable").bootstrapTable("insertRow", {
		index : rowIndex++,
		row :{"order" : rowIndex++,"fltDate":getCurrFormatDate(),"fltNo":"","shareFltNo":"","departApt":"","arrivalApt":"","std":"","sta":""
			,"otherApt":"","otherStd":"","otherSta":"","aircraftNumber":"","actType":"","propertyCode":"","alnCode":"","attachmentName":""}
	});
}

function deleteRow(){
    var ids = $.map($("#baseTable").bootstrapTable('getSelections'), function (row) {
        return row.order;
    });
    if (ids.length < 1 ) {
        layer.msg("请选择行!",{time:600,icon:0});
        return;
    }
    //删除删除行对应的附件
    delTmpAttachmentFile("delRow");
    //去除行数据
    $("#baseTable").bootstrapTable('remove', {
        field: 'order',
        values: ids
    });
}

/**
 * 保存公务通航计划
 */
function save(){
	var dataArr = $("#baseTable").bootstrapTable("getData");
	if(dataArr.length<1){
		layer.msg("请新增行!",{time:2500,icon:2});
		return;
	}
	var columns = detailColumns;
	var newRows = [];//新增的行
	for(var i=0;i<dataArr.length;i++){
		var data = $.extend(true,{},dataArr[i]);
		for(var j=0;j<columns.length;j++){
			var column = columns[j];
			var field = column.field;
			var title = column.title;
			if(field!='checkbox'&&field!='order'&&field!='shareFltNo'&&field!='aircraftNumber'
				&&field!='attachmentName'&&field!='attachmentId'&&field!='otherApt'&&field!='otherStd'
					&&field!='otherSta'&&field!='file'&&field!='tmpAttachmentName'&&field!='tmpAttachmentId'&&!data[field]){
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
	//判断是否有修改的数据
	if(newRows.length==0){
		layer.msg("没有需要保存的内容！",{icon:7});
		return false;
	}
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/plan/specialPlan/save",
		async : true,
		dataType : "text",
		data : {
			operateType : operateType,
			saveData : JSON.stringify(newRows)
		},
		success : function(result) {
			layer.close(loading);
			if (result == 'success') {
				parent.layer.close(index);
				parent.refreshTable();
				layer.msg("公务机通航计划保存成功！",{time:2000,icon:1});
			}else if(result == 'fail'){
				layer.alert("公务机通航计划保存失败！",{time:2000,icon:2});
			}else{
				layer.alert("公务机通航计划保存异常！",{time:2000,icon:2});
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

function getCurrFormatDate() {
	var date = new Date();
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	var currentdate = year+""+month+""+day;
    return currentdate;
}

function uploadWin(obj){
	uploadRowIndex = obj.value;
	var iframe = layer.open({
		type : 1,
		area: ['450px', '230px'],
		title : "上传公务机通航计划附件",
		content : $("#upload")
	});
}

/**
 * 上传附件
 */
function fileOnChange(){
	//保存上次上传的文件id，用于删除旧上传文件
	var dataList = $("#baseTable").bootstrapTable("getData");
	var tmpAttachmentId = dataList[uploadRowIndex]["tmpAttachmentId"];
	$("#oldFileId").val(tmpAttachmentId);
	
	var loading = null;
	var fileArr = $("input[name='file']");
	var impWind = $("#fileList").ajaxSubmit({
	beforeSubmit : function() {
		var check = false;
		var msg = '';
		var countFileNum = 0;
		fileArr.each(function() {
			var filePath = $(this).val();
			if(filePath!=''){
				countFileNum+=1;
			}
		});
		if(countFileNum <= 0){
			msg = '您还没有选择要上传的文件！';
			check = true;
		}
		if(check){
			layer.msg(msg, {icon : 7});
			return false;
		}else{
			layer.close(layer.index);
			loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			// 0.1透明度
			});
			return true;
		}
	},
	success : function(res) {
		if(res!=null){
			if(res.result == "succeed"){
				layer.msg('上传成功！', {icon : 1});
				var rows = {
					index : uploadRowIndex,
					field : "tmpAttachmentId",
					value : res.info
				}
				$('#baseTable').bootstrapTable("updateCell",rows);
				var rows = {
					index : uploadRowIndex,
					field : "tmpAttachmentName",
					value : res.fileName
				}
				$('#baseTable').bootstrapTable("updateCell",rows);
				uploadRowIndex = "";
				$("#oldFileId").val("");
			}else if(res.result == "fail"){
				layer.msg('上传失败！', {icon : 2});
			}else if(res.result == "error"){
				layer.msg('上传异常！', {icon : 2});
			}
		}
		resetUpload();
		layer.close(loading);
	}
	});
}

function uploadOnChange(fileObj,showName,showSize){
	var name = fileObj.files[0].name;
	var size = fileObj.files[0].size;
	$("#"+showName).text(name);
	$("#"+showSize).text((size/1014).toFixed(1)+"KB");
	
}

function resetUpload(){
	$("#fileInput").val('');
	$("#fileName").text('');
	$("#fileSize").text('');
}

/**
 * 删除取消、删除行、关闭操作列表中上传的文件
 */
function delTmpAttachmentFile(operateType){
	var dataArr;
	var tmpAttachmentIds = "";
	if(operateType=="delRow"){
		dataArr = $("#baseTable").bootstrapTable('getSelections');
	}else{
		dataArr = $("#baseTable").bootstrapTable("getData");
	}
	for(var i=0;i<dataArr.length;i++){
		var data = $.extend(true,{},dataArr[i]);
		var tmpAttachmentId = data["tmpAttachmentId"];
		if(tmpAttachmentId!="undefined"&&tmpAttachmentId!=null&&tmpAttachmentId!=""){
			if(i!=dataArr.length-1){
				tmpAttachmentIds += tmpAttachmentId+",";
			}else{
				tmpAttachmentIds += tmpAttachmentId;
			}
		}
	}
	if(operateType=="delRow"){
		//调用删除上传文件
		parent.delFile(tmpAttachmentIds);
	}else{
		//调用删除上传文件
		parent.delFile(tmpAttachmentIds);
		parent.layer.close(index);
	}
}