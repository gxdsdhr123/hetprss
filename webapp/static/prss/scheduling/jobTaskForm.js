var layer;
layui.use([ "layer" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	var jobTaskId = $("#jobTaskId").val();
	if(jobTaskId){
		$("#toolbar").hide();
		$("input[name=inOutFlag]").attr("disabled","disabled");
		$("input[name=jobType]").attr("disabled","disabled");
	}
	initTaskGrid();
	// 新增行
	$("#addRow").click(function() {
		addRow();
	});
	// 删除行
	$("#removeRow").click(function() {
		var selections = $("#taskGrid").bootstrapTable('getSelections');
		if (!selections || selections.length <= 0) {
			layer.msg("请选择要删除的行！", {
				icon : 7
			});
			return false;
		}
		for (var i = 0; i < selections.length; i++) {
			var rowData = selections[i];
			$("#taskGrid").bootstrapTable("removeByUniqueId", rowData.rowId);
		}
	});
});
/**
 * 初始化表格
 */
function initTaskGrid() {
	$("#taskGrid").bootstrapTable({
		toolbar : "#toolbar",
		toolbarAlign : "right",
		uniqueId : "rowId",
		idField:"id",
		url : ctx + "/scheduling/jobManage/gridData",
		method : "get",
		queryParams : function() {
			var param = {
				jobTaskId : $("#jobTaskId").val(),
			};
			return param;
		},
		toolbarAlign : "left",
		pagination : false,
		clickToSelect : false,
		undefinedText : "",
		columns : getGridColumns(),
		onEditableSave : function(field, row, oldValue, $el) {
			if(field=="processId" && row.processId){
				if(processSource){
					for(var i=0;i<processSource.length;i++){
						if(processSource[i].value==row.processId){
							row.processName = processSource[i].text;
							row.name = processSource[i].text;
							break;
						}
					}
				}
			}
		},
		onLoadSuccess:function(data){
			$('#taskGrid').bootstrapTable('hideColumn', 'id');
			var jobTaskId = $("#jobTaskId").val();
			if (jobTaskId && data && data.length > 0) {
				$('#taskGrid').bootstrapTable('hideColumn', 'checkbox');
				for (var i = 0; i < data.length; i++) {
					var rowData = data[i];
					if (rowData && rowData.jobType) {
						$("input[name=jobType][value="+ rowData.jobTypeId + "]").prop("checked", true);
					}
				}
			}
		},
		onClickCell : function(field, value, row, $element) {
			// 作业人
			if (field == "person") {
				if (!$.trim(row.startTime)) {
					layer.msg("请设置开始时间！", {
						icon : 7
					});
					return false;
				}
				if (!$.trim(row.endTime)) {
					layer.msg("请设置结束时间！", {
						icon : 7
					});
					return false;
				}
				selectPerson(field, value, row, $element);
			} else if(field == "startTime"||field == "endTime"){
				//baochl_20180402 手动任务允许修改时间
				if(row.autoFlag&&row.autoFlag==0){
					layer.msg("自动任务不可以修改任务时间！",{icon:7});
					return false;
				}
				popupDate(field, value, row, $element);
			}
		}
	});
}
function getGridColumns() {
	var columns = [
			{
				field : "checkbox",
				checkbox : true
			},
			{
				field : "id"
			},
			{
				field : "rowId",
				title : "序号",
				width : '40px',
				align : 'center',
				formatter : function(value, row, index) {
					row["rowId"] = index;
					return index + 1;
				}
			},
			{
				field : "jobTypeId",
				title : "作业类型",
				editable : {
					onblur : "ignore",
					disabled : true,
					type : "select",
					source : jobTypeSource
				}
			},
			{
				field : "person",
				title : '作业人员',
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0)' id='person_" + index
							+ "' data-value='"
							+ (row.personId ? row.personId : '') + "'>"
							+ (value ? value : '请选择') + "</a>";
				}
			}, {
				field : "fltNo",
				title : '航班号',
				editable : false
			}, {
				field : "inOutFlag",
				title : '进出港',
				editable : {
					disabled : true,
					type : "select",
					source : [ {
						text : "进港",
						value : "A0"
					}, {
						text : "进出进",
						value : "A1"
					}, {
						text : "出港",
						value : "D0"
					} , {
						text : "进出出",
						value : "D1"
					} ]
				}
			}, {
				field : "processId",
				title : '作业流程',
				editable : {
					onblur : "ignore",
					type : "select2",
					source : processSource
				}
			}, {
				field : "startTime",
				title : '开始时间',
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0)' id='startTime_" + index + "'>"
							+ (value ? value : '请选择') + "</a>";
				}
			}, {
				field : "endTime",
				title : '结束时间',
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0)' id='endTime_" + index + "'>"
							+ (value ? value : '请选择') + "</a>";
				}
			} ];
	//保障任务扩展属性列
	$.ajax({
		type : 'post',
		url : ctx + "/scheduling/jobManage/getPlusColumns",
		async : false,
		dataType : 'json',
		data : {},
		success : function(data) {
			if(data){
				for(var i=0;i<data.length;i++){
					var column = data[i];
					if(column.disType=="input"){
						columns.push({
							field : "jmplus_"+column.attrCode,
							title : column.disName,
							editable : {
								type : "text"
							}
						});
					} else if(column.disType=="select"){
						columns.push({
							field : "jmplus_"+column.attrCode,
							title : column.disName,
							editable : {
								onblur : "ignore",
								type : "select2",
								source :column.source
							}
						});
					}
				}
			}
		}
	});
	return columns;
}
/**
 * 新增行
 */
function addRow() {
	var inFltId = "";
	var outFltId = "";
	// 进出港类型
	$("input[name=inOutFlag]:checked").map(function(index, elem) {
		if ($(this).val() == 1) {
			inFltId = $("#inFltId").val();
		} else if ($(this).val() == 2) {
			outFltId = $("#outFltId").val();
		}
	});
	// 作业类型
	var jobTypes = $("input[name=jobType]:checked").map(function(index, elem) {
		return $(this).val();
	}).get().join(",");

	if (inFltId == "" && outFltId == "") {
		layer.msg("请选择进出港类型！", {
			icon : 7
		});
		return false;
	}
	if (jobTypes == "") {
		layer.msg("请选择作业类型！", {
			icon : 7
		});
		return false;
	}
	$.ajax({
		type : 'post',
		url : ctx + "/scheduling/jobManage/addRow",
		async : false,
		dataType : 'json',
		data : {
			inFltId : inFltId,
			outFltId : outFltId,
			jobTypes : jobTypes
		},
		success : function(data) {
			var gridData = $("#taskGrid").bootstrapTable("getData");
			var rowIndex = gridData.length;
			if (data && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					var rowData = data[i];
					rowIndex++;
					rowData["rowId"] = rowIndex;
					$("#taskGrid").bootstrapTable("insertRow", {
						index : rowIndex,
						row : rowData
					});
				}
			}
		}
	});
}
/**
 * 选择作业人
 */
function selectPerson(field, value, row, $element) {
	$.ajax({
		type : 'post',
		url : ctx + "/scheduling/jobManage/setJmSemaState",
		async : false,
		success : function(state) {
			if(state==0){
				var fltId = row.fltid;
				var jobType = row.jobTypeId;
				var startTime = row.startTime;
				var endTime = row.endTime;
				var params = "?fltId="+fltId+"&jobType="+jobType+"&startTime="+startTime+"&endTime="+endTime;
				layer.open({
					type : 2,
					title : "选择作业人",
					closeBtn : false,
					area : [ '100%', '98%' ],
					btn : [ "确定", "取消" ],
					success: function(layero, index){
						var selected = layer.getChildFrame('input[name=personRadio][value='+row.personId+']', index);
						if(selected&&selected.length>0){
							selected.prop("checked",true);
						}
					},
					content : [ ctx + "/scheduling/jobManage/jobTaskUserForm"+params, "yes" ],
					yes : function(index, layero) {
						var person = layer.getChildFrame('input[name=personRadio]:checked',index);
						if (person.val()) {
							$element.find("a").text(person.data("name"));
							$element.find("a").data("value", person.val());
							row["personId"] = person.val();
							row["person"] = person.data("name");
							layer.close(index);
						} else {
							layer.msg("请选择作业人！", {
								icon : 7
							});
							return false;
						}
					}
				});
			} else {
				layer.msg("系统或其他用户正在进行人员分配，请稍后重试！",{icon:7});
				return false;
			}
		}
	});
}

function saveGridData() {
	var result = true;
	var gridData = $("#taskGrid").bootstrapTable("getData");
	if (gridData.length <= 0) {
		layer.msg("没有可发布的任务！",{icon:7});
		result = false;
		return false;
	}
	for(var i=0;i<gridData.length;i++){
		var rowData = gridData[i];
		if(!rowData.processId){
			layer.msg("第"+[i+1]+"行请选择作业流程！",{icon:7});
			result = false;
			break;
		}
		if(!rowData.startTime){
			layer.msg("第"+[i+1]+"行请设置开始时间！",{icon:7});
			result = false;
			break;
		}
		if(!rowData.endTime){
			layer.msg("第"+[i+1]+"行请设置结束时间！",{icon:7});
			result = false;
			break;
		}
		var startTime = new Date(Date.parse(rowData.startTime));
		var endTime = new Date(Date.parse(rowData.endTime));
		if(startTime>endTime){
			layer.msg("第"+[i+1]+"行【结束时间】必须大于【开始时间】！",{icon:7});
			result = false;
			break;
		}
	}
	if(result){
		$("#taskList").val(JSON.stringify(gridData));
		var loading = null;
		$("#inputForm").ajaxSubmit({
			async : false,
			beforeSubmit : function() {
				loading = parent.layer.load(2, {
					shade : [ 0.1, '#000' ]
				// 0.1透明度
				});
			},
			error : function() {
				parent.layer.close(loading);
				layer.msg('保存失败！', {
					icon : 2
				});
				return false;
			},
			success : function(data) {
				parent.layer.close(loading);
				if(data=="succeed"){
					parent.newTaskCallback();
				}  else {
					layer.alert("发布失败："+data,{icon:5});
				}
			}
		});
	} else {
		return false;
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
		dateFmt:"yyyy-MM-dd HH:mm:ss",
		onpicked : function(dp) {
			var newValue = dp.cal.getDateStr();
			if (newValue) {
				row[field] = newValue;
			}
		}
	});
}