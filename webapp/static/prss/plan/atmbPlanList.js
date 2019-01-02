var inFltGridData;
var outFltGridData;
var searchOption="";
layui.use(["form","layer"]);
$(document).ready(function(){
//	inFltGrid();
//	outFltGrid();
	//初始化表格
	var inTableOptions = getOption("in","remote",null);
	inTableOptions.height=$(window).height()-35;// 表格适应页面高度
	$("#inFltGrid").bootstrapTable(inTableOptions);
	var outTableOptions = getOption("out","remote",null);
	outTableOptions.height=$(window).height()-35;// 表格适应页面高度
	$("#outFltGrid").bootstrapTable(outTableOptions);
	//刷新
	$("#refresh").click(function(){
		refreshRemoteTable();
//		$("#inFltGrid").bootstrapTable("refresh");
//		$("#outFltGrid").bootstrapTable("refresh");
	});
	//导入
	$("#importBtn").click(function(){
		importData();
	});
	//导入excel计划
	$("#importExcelBtn").click(function(){
		$("#fileInput").click();
	});
	//新增
	$("#addBtn").click(function(){
		planForm();
	});
	//发布
	$("#publishBtn").click(function(){
		var inFltData = $("#inFltGrid").bootstrapTable("getData");
		var outFltData = $("#outFltGrid").bootstrapTable("getData");
		if(inFltData.length==0&&outFltData.length==0){
			layer.msg("暂无可发布航班计划！",{icon:7});
			return false;
		} else {
			publish();
		}
	});
	//打印
	$("#printBtn").click(function(){
		$("#hiddenFrame").attr("src",ctx+"/plan/atmbPlan/export");
	});
	//删除
	$("#delBtn").click(function(){
		remove();
	});
	//回车搜索
	var searchInput =$("#searchTab");    
		searchInput.bind('keydown', function(event) {
		if (event.keyCode == "13") {
			var text = searchInput.val();
			if (text) {
				inSearchData = [];
				outSearchData = [];
				text = text.toUpperCase();
				//进港计划搜索
				for (var i = 0; i < inFltGridData.length; i++) {
					var rowData = inFltGridData[i];
					for ( var key in rowData) {
						if (key.indexOf(searchOption) > -1) {
							if (rowData[key] && rowData[key].toString().indexOf(text) != -1) {
								inSearchData.push(rowData);
								break;
							}
						}
					}
				}
				//出港计划搜索
				for (var i = 0; i < outFltGridData.length; i++) {
					var rowData = outFltGridData[i];
					for ( var key in rowData) {
						if (key.indexOf(searchOption) > -1) {
							if (rowData[key] && rowData[key].toString().indexOf(text) != -1) {
								outSearchData.push(rowData);
								break;
							}
						}
					}
				}
				//进港次日计划 
				$('#inFltGrid').bootstrapTable('load',inSearchData);
				//出港次日计划
				$('#outFltGrid').bootstrapTable('load',outSearchData);
			} else {
				//进港次日计划 
				$('#inFltGrid').bootstrapTable('load',inFltGridData);
				//出港次日计划
				$('#outFltGrid').bootstrapTable('load',outFltGridData);
			}
		}
	});
});

/**
 * tableType:进港in 出港out
 * mode:client remote
 * data:json数据
 */
function getOption(tableType,mode,data){
	var ioType = tableType == "in"?"A":"D";
	var tableOptions = {
		queryParams : {
			ioType : ioType,
		},
		sortable:true,
		sortOrder:'asc',
		search : false,
		columns : getColumns(tableType),
		responseHandler : function(res) {
			if(tableType == "in"){
				inFltGridData = res;
			}else if(tableType == "out"){
				outFltGridData = res;
			}
			return res;
		},
		onDblClickRow:function(row,tr,field){
			planForm(row.ids,row.fltDate,row.fltNo,ioType);
		},
		contextMenu: '#context-menu',
		onContextMenuItem: function(row, $el){
			if($el.data("item") == "msgView"){
				msgShow(row);
			}
		},
		customSort : function(fieldName, fieldOrder) {
			sortName = fieldName;
			sortOrder = fieldOrder;
			this.data.sort(function(a, b) {
				var order = sortOrder === 'desc' ? -1 : 1
				var val1 = a[sortName];
				var val2 = b[sortName];
				if (val1 === undefined || val1 === null) {
					val1 = '';
				}
				if (val2 === undefined || val2 === null) {
					val2 = '';
				}
				if ($.isNumeric(val1) && $.isNumeric(val2)) {
					// Convert numerical values form string to float.
					val1 = parseFloat(val1);
					val2 = parseFloat(val2);
					if (val1 < val2) {
						return order * -1;
					}
					return order;
				}
				if (val1 === val2) {
					return 0;
				}
				// If value is not a string, convert to string
				if (typeof val1 !== 'string') {
					val1 = val1.toString();
				}
				if (typeof val2 !== 'string') {
					val2 = val2.toString();
				}
				if (val1.localeCompare(val2, "zh") === -1) {
					return order * -1;
				}
				return order;
			});
		}
	}
	//mode client remote
	if(mode=='client'){
		tableOptions.data = data;
	}else if(mode=='remote'){
		tableOptions.url = ctx+"/plan/atmbPlan/gridData";// 请求后台的URL（*）
		tableOptions.method = "get";// 请求方式（*）
		tableOptions.dataType = "json";// 返回结果格式
	}
	return tableOptions;
	
}

/**
 * tableType:进港in 出港out
 */
function getColumns(tableType){
	var columns = [{
		field : "checkbox",
		checkbox : true,
		sortable : false,
		editable : false
	},{
		field : "rowId",
		title : "序号",
		width : '40px',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return index + 1;
		}
	}  , {
		field : "fltNo",
		title : tableType=="in"?"进港航班":"出港航班",
		align:"center",
		sortable : true,
		formatter(value, row, index, field){
			var dataState = row.dataState;
			if(dataState&&dataState==2){
				return "<span class='label bg-green'>"+value+"</span>"
			} else {
				return value;
			}
		}
	} , {
		field : "aircraftNumber",
		title : "机号",
		sortable : true
	}  , {
		field : "actType",
		title : "机型",
		sortable : true
	} , {
		field : "std",
		title : "计起",
		sortable : true
	} , {
		field : "sta",
		title : "计落",
		sortable : true
	} , {
		field : "propertyCode",
		title : "性质",
		sortable : true
	}, {
		field : "attrCode",
		title : "属性",
		sortable : true
	} , {
		field : "airlineCode",
		title : "航空公司",
		sortable : true
	}, {
		field : "fltDate",
		title : "日期",
		sortable : true
	}];
	
	if(tableType=="in"){
		columns.push({field : "airports",title : "起场",sortable : true});
	}else{
		columns.push({field : "airports",title : "落场",sortable : true});
	}
	return columns;
}

/**
 * 进港航班列表
 */
//function inFltGrid() {
//	$("#inFltGrid").bootstrapTable({
//		url : ctx+"/plan/atmbPlan/gridData", // 请求后台的URL（*）
//		queryParams : function(params) {
//			var param = {
//				ioType:"A"
//			};
//			return param;
//		},
//		method : "get", // 请求方式（*）
//		dataType : "json",
//		sortable:true,
//		sortOrder:'asc',
//		search : false,
//		height:$(window).height()-35,
//		columns : [{
//			field : "checkbox",
//			checkbox : true,
//			sortable : false,
//			editable : false
//		},{
//			field : "rowId",
//			title : "序号",
//			width : '40px',
//			align : 'center',
//			valign : 'middle',
//			formatter : function(value, row, index) {
//				return index + 1;
//			}
//		}  , {
//			field : "fltNo",
//			title : "进港航班",
//			align:"center",
//			sortable : true,
//			formatter(value, row, index, field){
//				var dataState = row.dataState;
//				if(dataState&&dataState==2){
//					return "<span class='label bg-green'>"+value+"</span>"
//				} else {
//					return value;
//				}
//			}
//		} , {
//			field : "aircraftNumber",
//			title : "机号",
//			sortable : true
//		}  , {
//			field : "actType",
//			title : "机型",
//			sortable : true
//		} , {
//			field : "airports",
//			title : "起场",
//			sortable : true
//		} , {
//			field : "std",
//			title : "计起",
//			sortable : true
//		} , {
//			field : "sta",
//			title : "计落",
//			sortable : true
//		} , {
//			field : "propertyCode",
//			title : "性质",
//			sortable : true
//		}, {
//			field : "attrCode",
//			title : "属性",
//			sortable : true
//		} , {
//			field : "airlineCode",
//			title : "航空公司",
//			sortable : true
//		}, {
//			field : "fltDate",
//			title : "日期",
//			sortable : true
//		}],
//		onDblClickRow:function(row,tr,field){
//			planForm(row.fltDate,row.fltNo);
//		},
//		contextMenu: '#context-menu',
//		onContextMenuItem: function(row, $el){
//			if($el.data("item") == "msgView"){
//				msgShow(row);
//			}
//		},
//		customSort : function(fieldName, fieldOrder) {
//			sortName = fieldName;
//			sortOrder = fieldOrder;
//			this.data.sort(function(a, b) {
//				var order = sortOrder === 'desc' ? -1 : 1
//				var val1 = a[sortName];
//				var val2 = b[sortName];
//				if (val1 === undefined || val1 === null) {
//					val1 = '';
//				}
//				if (val2 === undefined || val2 === null) {
//					val2 = '';
//				}
//				if ($.isNumeric(val1) && $.isNumeric(val2)) {
//					// Convert numerical values form string to float.
//					val1 = parseFloat(val1);
//					val2 = parseFloat(val2);
//					if (val1 < val2) {
//						return order * -1;
//					}
//					return order;
//				}
//		
//				if (val1 === val2) {
//					return 0;
//				}
//				// If value is not a string, convert to string
//				if (typeof val1 !== 'string') {
//					val1 = val1.toString();
//				}
//				if (typeof val2 !== 'string') {
//					val2 = val2.toString();
//				}
//		
//				if (val1.localeCompare(val2, "zh") === -1) {
//					return order * -1;
//				}
//		
//				return order;
//			});
//		}
//	});
//}
/**
 * 出港航班列表
 */
//function outFltGrid() {
//	$("#outFltGrid").bootstrapTable({
//		url : ctx+"/plan/atmbPlan/gridData", // 请求后台的URL（*）
//		queryParams : function(params) {
//			var param = {
//				ioType:"D"
//			};
//			return param;
//		},
//		method : "get", // 请求方式（*）
//		dataType : "json",
//		sortable:true,
//		sortOrder:'asc',
//		search : false,
//		pagination : false,
//		height:$(window).height()-35,
//		columns : [{
//			field : "checkbox",
//			checkbox : true,
//			sortable : false,
//			editable : false
//		},{
//			field : "rowId",
//			title : "序号",
//			width : '40px',
//			align : 'center',
//			valign : 'middle',
//			formatter : function(value, row, index) {
//				return index + 1;
//			}
//		} , {
//			field : "fltNo",
//			title : "出港航班",
//			align:"center",
//			sortable : true,
//			formatter(value, row, index, field){
//				var dataState = row.dataState;
//				if(dataState&&dataState==2){
//					return "<span class='label bg-green'>"+value+"</span>"
//				} else {
//					return value;
//				}
//			}
//		} , {
//			field : "aircraftNumber",
//			title : "机号",
//			sortable : true
//		}  , {
//			field : "actType",
//			title : "机型",
//			sortable : true
//		} , {
//			field : "airports",
//			title : "落场",
//			sortable : true
//		} , {
//			field : "std",
//			title : "计起",
//			sortable : true
//		} , {
//			field : "sta",
//			title : "计落",
//			sortable : true
//		} , {
//			field : "propertyCode",
//			title : "性质",
//			sortable : true
//		}, {
//			field : "attrCode",
//			title : "属性",
//			sortable : true
//		} , {
//			field : "airlineCode",
//			title : "航空公司",
//			sortable : true
//		}, {
//			field : "fltDate",
//			title : "日期",
//			sortable : true
//		}],
//		onDblClickRow:function(row,tr,field){
//			planForm(row.fltDate,row.fltNo);
//		},
//		contextMenu: '#context-menu',
//		onContextMenuItem: function(row, $el){
//			if($el.data("item") == "msgView"){
//				msgShow(row);
//			}
//		},
//		customSort : function(fieldName, fieldOrder) {
//			sortName = fieldName;
//			sortOrder = fieldOrder;
//			this.data.sort(function(a, b) {
//				var order = sortOrder === 'desc' ? -1 : 1
//				var val1 = a[sortName];
//				var val2 = b[sortName];
//				if (val1 === undefined || val1 === null) {
//					val1 = '';
//				}
//				if (val2 === undefined || val2 === null) {
//					val2 = '';
//				}
//				if ($.isNumeric(val1) && $.isNumeric(val2)) {
//					// Convert numerical values form string to float.
//					val1 = parseFloat(val1);
//					val2 = parseFloat(val2);
//					if (val1 < val2) {
//						return order * -1;
//					}
//					return order;
//				}
//		
//				if (val1 === val2) {
//					return 0;
//				}
//				// If value is not a string, convert to string
//				if (typeof val1 !== 'string') {
//					val1 = val1.toString();
//				}
//				if (typeof val2 !== 'string') {
//					val2 = val2.toString();
//				}
//		
//				if (val1.localeCompare(val2, "zh") === -1) {
//					return order * -1;
//				}
//		
//				return order;
//			});
//		}
//	});
//}
/**
 * 导入计划
 */
function importData(){
	layer.confirm('确定清空已有计划，导入'+getTomorrowDate()+"计划？", {
		icon : 3,
		title : '提示'
	}, function(index) {
		layer.close(index);
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			url : ctx + "/plan/atmbPlan/importData",
			async : false,
			data : {
			},
			success : function(result) {
				layer.close(loading)
				if (result == 0) {
					layer.msg("暂无"+getTomorrowDate()+"航班计划！",{icon:7,time:2000});
				} else {
					layer.msg("计划导入成功！",{icon:1,time:2000},function(){
//						$("#inFltGrid").bootstrapTable("refresh");
//						$("#outFltGrid").bootstrapTable("refresh");
						refreshRemoteTable();
					});
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				layer.close(loading)
				var result = "导入失败，计划导入出错！";
				layer.alert(result, {icon: 2});
			}
		});
	});
}
/**
 * 获取当前日期次日
 * @returns {String}
 */
function getTomorrowDate(){
	var date = new Date();
	date.setDate(date.getDate() + 1);
	var year = date.getFullYear();
	var mon = date.getMonth()+1;
	mon = (Array(2).join(0) + mon).slice(-2);
	var day = date.getDate();
	day = (Array(2).join(0) + day).slice(-2);
	return year + "-" + mon + "-" + day;
}
/**
 * 计划编辑页面
 * @param fltData
 * @param fltNo
 */
function planForm(ids,fltDate,fltNo,ioType){
	var title = "计划新增";
	var isNew = true;
	if(fltNo){
		title = "计划修改";
		isNew = false;
	}
	layer.open({
		type : 2,
		title : title,
		area : [ "100%", "100%" ],
		content : [ ctx + "/plan/atmbPlan/form?fltDate="+fltDate+"&fltNo="+fltNo+"&isNew="+isNew+"&ioType="+ioType+"&ids="+ids, "no" ],
		btn : [ "新增行","删除行","保存", "取消" ],
		btn1 : function(index, layero) {
			//新增行
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.addRow();
			return false;
		},
		btn2 : function(index, layero) {
			//删除行
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.removeRow();
			return false;
		},
		btn3 : function(index, layero) {
			//保存
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var data = iframeWin.getGridData();
			if(data){
				var succeed = save(isNew,data);
				if(succeed){
					layer.msg("保存成功！", {icon: 1},function(){
						layer.close(index);
//						$("#inFltGrid").bootstrapTable("refresh");
//						$("#outFltGrid").bootstrapTable("refresh");
						refreshRemoteTable();
					});
				} else {
					return false;
				}
			} else {
				return false;
			}
		},
		btn4 : function(index, layero) {
			//取消
		}
	});
}
/**
 * 保存
 * @returns
 */
function save(isNew,data){
	var succeed = false;
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		url : ctx + "/plan/atmbPlan/save",
		async : false,
		data : {
			isNew:isNew,
			newRows:JSON.stringify(data.newRows),
			editRows:JSON.stringify(data.editRows),
			removeRows:JSON.stringify(data.removeRows)
		},
		success : function(result) {
			layer.close(loading);
			if(result=="succeed"){
				succeed = true;
			} else {
				layer.alert("保存失败！"+result, {icon: 2});
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			layer.close(loading)
			var result = "保存失败！";
			layer.alert(result, {icon: 2});
		}
	});
	return succeed;
}
/**
 * 发布
 */
function publish(){
	layer.confirm("确定发布航班计划？", {
		icon : 3,
		title : '提示'
	}, function(index) {
		layer.close(index);
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			url : ctx + "/plan/atmbPlan/publish",
			async : false,
			success : function(result) {
				layer.close(loading);
				if(result=="succeed"){
					layer.msg("发布成功！", {icon: 1},function(){
//						$("#inFltGrid").bootstrapTable("refresh");
//						$("#outFltGrid").bootstrapTable("refresh");
						refreshRemoteTable();
					});
				} else if(result==0){
					layer.msg("暂无未发布航班计划！",{icon:7});
				}else {
					layer.alert("发布失败！"+result, {icon: 2});
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				layer.close(loading)
				var result = "发布失败!";
				layer.alert(result, {icon: 2});
			}
		});
	});
}
/**
 * 删除
 * @returns
 */
function remove(){
	var inFltRows = $("#inFltGrid").bootstrapTable("getSelections");
	var outFltRows = $("#outFltGrid").bootstrapTable("getSelections");
	var rows = inFltRows.concat(outFltRows);
	if(rows&&rows.length>0){
		layer.confirm('确定要删除航班计划？', {
			icon : 3,
			title : '提示'
		}, function(index) {
			layer.close(index);
			var loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			});
			$.ajax({
				type : 'post',
				url : ctx + "/plan/atmbPlan/remove",
				async : false,
				data : {
					rows:JSON.stringify(rows)
				},
				success : function(result) {
					layer.close(loading);
					if(result=="succeed"){
						layer.msg("删除成功！",{icon:1},function(){
//							$("#inFltGrid").bootstrapTable("refresh");
//							$("#outFltGrid").bootstrapTable("refresh");
							refreshRemoteTable();
						});
					} else {
						layer.alert("删除失败！"+result, {icon: 2});
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					layer.close(loading)
					var result = "删除失败！";
					layer.alert(result, {icon: 2});
				}
			});
		});
	} else {
		layer.msg("请选择要删除的行！",{icon:7});
		return false;
	}
}

function fileOnChange(){
	var loading = null;
	var obj = $("#fileInput")[0];
	$("#fileForm").ajaxSubmit({
	beforeSubmit : function() {
		var checkSubmit = true;
		if (obj.files.length <= 0) {
			layer.msg('您还没有选择要导入的文件！', {
				icon : 5
			});
			checkSubmit = false;
		}
		$("#fileInput").each(function() {
			var filePath = $(this).val();
			var fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.lastIndexOf("."));
			if(servicePublishDate!=fileName){
				layer.msg("请确认导入的空管计划excel是否是"+servicePublishDate+"次日计划！文件名格式:yyyymmdd.xls(xlsx)", {icon : 7,time : 4000});
				checkSubmit = false;
			}
			var suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
			if (suffix != "xls"&&suffix != "xlsx") {
				layer.msg('不支持的文件类型！请上传“.xls .xlsx”格式文件', {icon : 7});
				checkSubmit = false;
			}
			var fileSize = $(this)[0].files[0].size;
			if (fileSize > 5242880) {
				layer.msg('文件过大！', {icon : 7});
				checkSubmit = false;
			}
		});
		if(checkSubmit){
			loading = layer.load(2, {shade : [ 0.1, '#000' ]});
		}
		return checkSubmit;
	},
	error : function(e) {
		layer.close(loading);
		layer.msg('导入失败！', {
			icon : 2
		});
	},
	success : function(rsJsonArr) {
		delete $("#fileInput")[0].files;
		layer.close(loading);
		if(rsJsonArr != null||rsJsonArr!=""){
			var importRs = rsJsonArr[0].rs;
			var data = rsJsonArr[1];
			if(importRs=="err"){
				showExcelImportMsg("空管次日计划excel校验失败信息","errMsg",data);
			}else{
				layer.msg('空管次日计划导入完成！', {icon : 1,time : 2000}, function() {
//					$("#inFltGrid").bootstrapTable("refresh");
//					$("#outFltGrid").bootstrapTable("refresh");
					refreshRemoteTable();
					if(data.length>0){
						showExcelImportMsg("空管次日计划excel校验信息","infoMsg",data);
					}
				});
			}
		}
	}
	});
}

function showExcelImportMsg(title,msgKey,data){
	var table = "<table>";
	for(var i=0;i<data.length;i++){
		table = table +"<tr><td><span style='font-size:16px;'>"+data[i][msgKey]+"</span></td></tr>";
	}
	table = table + "</table>";
	$("#errMsg")[0].innerHTML=table;
	var iframe = layer.open({
		type : 1,
		area: ['600px', '410px'],
		title : title,
		content : $("#errMsg")
	});
//	layer.full(iframe);
}

function msgShow(row) {
	iframe = layer.open({
		type : 2,
		title : "空管次日计划报文",
		content : ctx + "/plan/atmbPlan/showMsg?msgId=" + row.msgId,
		area : [ '700px', '400px' ],
		btn : ["取消"],
		btn1 : function(index, layero) {
			layer.close(index);
		}
	});
}

function searchOpt(text,obj) {
	searchOption=text;
	var icon=$("#searchOptBtn").find("span").eq(0);
	$("#searchOptBtn").text($(obj).text());
	$("#searchOptBtn").append(icon);
}

function refreshRemoteTable(){
	var inTableOptions = getOption("in","remote",null);
	inTableOptions.height=$(window).height()-35;// 表格适应页面高度
	$("#inFltGrid").bootstrapTable('refresh',inTableOptions);
	var outTableOptions = getOption("out","remote",null);
	outTableOptions.height=$(window).height()-35;// 表格适应页面高度
	$("#outFltGrid").bootstrapTable('refresh',outTableOptions);
}