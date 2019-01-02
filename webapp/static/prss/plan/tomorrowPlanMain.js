var leftTableData;
var rightTableData;
var baseTable;
var clickRowId = "";// 当前单选行id，以便工具栏操作
var clickRow;
var searchOption="";
$(function() {
	$("html,body").css("cssText","100% !important");
	var layer;
	baseTable = $("#baseTable");
	layui.use([ 'form', 'layer' ], function() {// 调用layui表单及弹出层组件
		layer = layui.layer;
	});
	$("#baseTable").each(function() {
		$(this).on('load-success.bs.table', function(thisObj) {
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody[0].removeEventListener('ps-y-reach-end', load);
			tableBody[0].addEventListener('ps-y-reach-end', load);
		});
	});
	
	//创建表单数据
	createTable("Left","remote",null);
	createTable("Right","remote",null);
	function createTable(tableType,mode,data){
		// 基本表格选项
		var tableOptions = options(tableType,mode,data);
		tableOptions.height=$(window).height()-35;// 表格适应页面高度
		$("#baseTable"+tableType).bootstrapTable(tableOptions);// 加载基本表格
	}
	
	//为搜索加下拉搜索条件
	$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
	//刷新
	$("#refresh").click(function(){
		var tableOptionsL = options("Left","remote",null);
		tableOptionsL.height = $(window).height()-35;// 表格适应页面高度
		$("#baseTableLeft").bootstrapTable('refresh',tableOptionsL);// 加载基本表格
		var tableOptionsR = options("Right","remote",null);
		tableOptionsR.height = $(window).height()-35;// 表格适应页面高度
		$("#baseTableRight").bootstrapTable('refresh',tableOptionsR);// 加载基本表格
	});
	// 导入
	$('#import').click(function() {
		//已发布次日计划，不可编辑、新增、导入、发布
		if(checkPublish){
			layer.msg('次日计划已发布,请在航班动态新增计划！', {icon : 1});
			return false;
		}
		
		$.ajax({
			type : 'post',
			url : ctx + "/tomorrow/plan/filterImport",
			async : true,
			dataType : 'text',
			success : function(result) {
				if (result == "true") {
					layer.confirm("已存在导入的计划，是否重新导入？",{
				            btn : [ '是', '否' ]//按钮
				        }, function(index) {
				        	layer.close(index);
				        	uploadWin();
				        },function(index){
				        	layer.close(index);
							openFilter();
				        }); 
				} else {
					uploadWin();
				}
			}
		});
	});
	// 新增
	$('#create').click(function() {
		//已发布次日计划，不可编辑、新增、导入、发布
		if(checkPublish){
			layer.msg('次日计划已发布,请在航班动态新增计划！', {icon : 1});
			return false;
		}
		planForm();
	});
	// 删除
	$('#remove').click(function() {
		//已发布次日计划，不可编辑、新增、导入、发布
		if(checkPublish){
			layer.msg('次日计划已发布！不可删除', {icon : 1});
			return false;
		}
		//起场次日计划
		var departPlanData = $("#baseTableLeft").bootstrapTable("getSelections");
		//落场次日计划
		var arrivalPlanData = $("#baseTableRight").bootstrapTable("getSelections");
		
		if(departPlanData.length==0&&arrivalPlanData.length==0){
			layer.msg('没有选择任何数据');
			return;
		}
//		var hasPublished = false;//选择行中是否包含已发布的数据
//		var allCheckedRow = departPlanData.concat(arrivalPlanData);
//		for(var i=0;i<allCheckedRow.length;i++){
//			var rowData = allCheckedRow[i];
//			if(rowData&&rowData.data_state==2){
//				layer.msg("已发布的计划不可以删除！",{icon:7});
//				return false;
//			}
//		}
		layer.confirm("确定删除选中的航班次日计划？", {
			icon : 3,
			offset : '100px'
		}, function(index) {
			var loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			});
			$.ajax({
				type : 'post',
				url : ctx + "/tomorrow/plan/delete",
				data : {
					'departPlanData' : JSON.stringify(departPlanData),
					'arrivalPlanData' : JSON.stringify(arrivalPlanData)
				},
				success : function(data) {
					layer.close(loading);
					if(data=="succeed"){
						layer.msg("删除成功！已发布计划请在航班动态中操作",{icon:1},function(){
							$("#baseTableLeft").bootstrapTable('refresh');
							$("#baseTableRight").bootstrapTable('refresh');
						});
					}else {
						layer.alert("次日计划删除失败！"+data, {icon: 2});
					}
				},error:function(msg){
					layer.close(loading);
					layer.alert("次日计划删除失败！", {icon: 2});
				}
			});
			layer.close(index);
		});	
	});
	// 发布
	$('#publish').click(function() {
//		一次发布全部次日计划，不需要选择发布
//		//进港次日计划
//		var departPlanData = $("#baseTableLeft").bootstrapTable("getSelections");
//		//出港次日计划
//		var arrivalPlanData = $("#baseTableRight").bootstrapTable("getSelections");
//		var msg = "";
//		if(departPlanData.length==0&&arrivalPlanData.length==0){
//			msg = "是否发布全部次日计划？";
//			//进港次日计划
//			departPlanData = $("#baseTableLeft").bootstrapTable("getData");
//			//出港次日计划
//			arrivalPlanData = $("#baseTableRight").bootstrapTable("getData");
//		}else{
//			msg = "是否发布选中的航班次日计划？";
//		}
		//已发布次日计划，不可编辑、新增、导入、发布
		if(checkPublish){
			layer.msg('次日计划已发布，不可再次发布！', {icon : 1});
			return false;
		}
		
		//校验发布数据是否需存在空值
		var checkRs = checkRow();
		if(checkRs){
			return false;
		}
		var msg = "是否发布全部次日计划？";
		//进港次日计划
		var departPlanData = leftTableData;
		//出港次日计划
		var arrivalPlanData = rightTableData;
		
		layer.confirm(msg, {
			icon : 3,
			offset : '100px'
		}, function(index) {
			var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
			$.ajax({
				type : 'post',
				url : ctx + "/tomorrow/plan/publish",
				data : {
					'departPlanData' : JSON.stringify(departPlanData),
					'arrivalPlanData' : JSON.stringify(arrivalPlanData)
				},
				success : function(data) {
					layer.close(loading);
					if(data.result=="succeed"){
						layer.msg("次日计划发布成功！", {icon: 1},function(){
							$("#baseTableLeft").bootstrapTable('refresh');
							$("#baseTableRight").bootstrapTable('refresh');
						});
						checkPublish = true;
					}else if(data.result=="nonPublished"){
						layer.msg("没有任何次日计划发布！", {icon: 1});
						$("#baseTableLeft").bootstrapTable('refresh');
						$("#baseTableRight").bootstrapTable('refresh');
					}else if(data.result=="alreadyPublished"){
						layer.msg("次日计划已经发布到动态！", {icon: 1});
						$("#baseTableLeft").bootstrapTable('refresh');
						$("#baseTableRight").bootstrapTable('refresh');
					}else if(data.result=="err"){
						layer.alert("次日计划发布失败！"+data.msg+"请联系管理员", {icon: 2});
					}
				},error:function(msg){
					layer.close(loading);
					layer.alert("次日计划发布失败！", {icon: 2});
				}
			});
			layer.close(index);
		});	
	});
	// 预测
	$('#forecast').click(function() {
		var iframe = layer.open({
			type : 2,
			title : "次日航班情况分析",
			content : ctx + "/tomorrow/plan/toForecast",
			btn : ['预测配置','关闭'],
			btn1 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.showHideConf();
				return false;
			}
		});
		layer.full(iframe);
	});
	// 全选
	$("#selectAll").click(function() {
		// 如果全选 取消全选
		if ($('#baseTable').bootstrapTable('getSelections').length == $('#baseTable').bootstrapTable('getData').length) {
			$('#baseTable').bootstrapTable('uncheckAll');
			$("#selectAll").text("全选");
		} else {
			$('#baseTable').bootstrapTable('checkAll');
			$("#selectAll").text("取消全选");
		}
	})
	// 打印
	$("#print").click(function() {
		var planType = $("#planType").val();
		$("#exportForm").attr("action",ctx + "/tomorrow/plan/exportExcel?planType="+planType);
		$("#exportForm").submit();
	});
	//回车搜索
	var searchInput =$("#searchTab");    
		searchInput.bind('keydown', function(event) {
		if (event.keyCode == "13") {
			var text = searchInput.val();
			if (text) {
				leftSearchData = [];
				rightSearchData = [];
				text = text.toUpperCase();
				//进港计划搜索
				for (var i = 0; i < leftTableData.length; i++) {
					var rowData = leftTableData[i];
					for ( var key in rowData) {
						if (key.indexOf(searchOption) > -1) {
							if (rowData[key] && rowData[key].toString().indexOf(text) != -1) {
								leftSearchData.push(rowData);
								break;
							}
						}
					}
				}
				//出港计划搜索
				for (var i = 0; i < rightTableData.length; i++) {
					var rowData = rightTableData[i];
					for ( var key in rowData) {
						if (key.indexOf(searchOption) > -1) {
							if (rowData[key] && rowData[key].toString().indexOf(text) != -1) {
								rightSearchData.push(rowData);
								break;
							}
						}
					}
				}
				//进港次日计划 
				$('#baseTableLeft').bootstrapTable('load',leftSearchData);				
				//出港次日计划
				$('#baseTableRight').bootstrapTable('load',rightSearchData);
			} else {
				//进港次日计划 
				$('#baseTableLeft').bootstrapTable('load',leftTableData);	
				//出港次日计划
				$('#baseTableRight').bootstrapTable('load',rightTableData);
			}
		}
	});
})

//单击事件
function onClickRow(row, tr, field) {// 记录单选行id，并赋予天蓝色底纹
	clickRowId = row.ID;
	$(".clickRow").removeClass("clickRow");
	$(tr).addClass("clickRow");
}

/**
 * 双击行，弹出修改页
 */
function onDblClickRowLeft(row,field){
	var dataState = row["data_state"];
	if(dataState==2){
		layer.msg('请在航班动态中对已发布计划进行修改、删除', {icon : 2});
		return false;
	}
	planForm(row.ids,row.flt_date,row.flt_no,row.airline_code_v,row.airline_code,"A");
}

function onDblClickRowRight(row,field){
	var dataState = row["data_state"];
	if(dataState==2){
		layer.msg('请在航班动态中对已发布计划进行修改、删除', {icon : 2});
		return false;
	}
	planForm(row.ids,row.flt_date,row.flt_no,row.airline_code_v,row.airline_code,"D");
}

/**
 * 计划编辑页面
 * @param fltData
 * @param fltNo
 */
function planForm(ids,fltDate,fltNo,airlineCodeV,airlineCode,ioType){
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
		content : ctx + "/tomorrow/plan/toForm?fltDate="+fltDate+"&fltNo="+fltNo+"&isNew="+isNew+"&ioType="+ioType+"&ids="+ids,
		btn : [ "新增行","删除行","保存", "取消" ],
		btn1 : function(index, layero) {
			//新增行
			var iframeWin = window[layero.find('iframe')[0]['name']];
			if(isNew){
				iframeWin.addRow("","");
			}else{
				iframeWin.addRow(fltNo,airlineCodeV,airlineCode);
			}
			
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
						$("#baseTableLeft").bootstrapTable('refresh');
						$("#baseTableRight").bootstrapTable('refresh');
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
 * tableType:进港Left 出港Right
 * mode:client remote
 * data:json数据
 */
function options(tableType,mode,data){
	var planType = $("#planType").val();
	
	// 基本表格选项
	var tableOptions = {
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : true, // 是否显示全选
		search : false, // 是否开启搜索功能
		searchOnEnterKey : false,
		sortable:true,
		sortOrder:'asc',
		editable:true,//开启编辑模式
		responseHandler : function(res) {
			if(tableType == "Left"){
				leftTableData = res;
			}else if(tableType == "Right"){
				rightTableData = res;
			}
			return res;
		},
		queryParams : {
			ioType : tableType == "Left"?"A":"D",
			planType : planType
		},
		onCheck:function(row,$element){
			if(row&&row.data_state==2){
				$element.prop("checked",false);
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
		},
		contextMenu: '#context-menu',
		onContextMenuItem: function(row, $el){
			if($el.data("item") == "msgView"){
				msgShow(row);
			}
		},
		columns : "Left" == tableType?mainColumns(true,false):mainColumns(false,true),
		onClickRow : onClickRow,// 单击选定行
		onDblClickRow : function(row,tr,field){
			var dataState = row["data_state"];
			if(dataState==2){
				layer.msg('请在航班动态中对已发布计划进行修改、删除', {icon : 2});
				return false;
			}
			planForm(row.ids,row.flt_date,row.flt_no,row.airline_code_v,row.airline_code,row.flag);
		},
		onEditableSave : "Left" == tableType?editableLeftSave:editableRightSave// 编辑结束触发
	};
	
	//mode client remote
	if(mode=='client'){
		tableOptions.data = data;
	}else if(mode=='remote'){
		tableOptions.url = ctx + "/tomorrow/plan/init";// 请求后台的URL（*）
		tableOptions.method = "get";// 请求方式（*）
		tableOptions.dataType = "json";// 返回结果格式
	}
	return tableOptions;
}

function mainColumns(depart,attival){
	var columns = [];
		columns.push(column("checkbox",false,"",true,false,null,null,true));
		columns.push(column("order",false,"序号",true,false,function(value, row, index) {return index + 1;},null));
		columns.push(column("flt_no",true,"航班号",true,false,stateFormatter,null));
		columns.push(column("aircraft_number",true,"机号",true,false,nullFormatter,isClassWhite()));
		columns.push(column("act_type",true,"机型",true,false,nullFormatter,isClassWhite()));
		if(depart)
			columns.push(column("routeName",true,"起场",true,false,nullFormatter,isClassWhite()));
		if(attival)
			columns.push(column("routeName",true,"落场",true,false,nullFormatter,isClassWhite()));
		columns.push(column("std",true,"计起",true,false,null,isClassWhite()));
		columns.push(column("sta",true,"计落",true,false,null,isClassWhite()));
		columns.push(column("etd",true,"预起",true,false,null,isClassWhite()));
		columns.push(column("eta",true,"预落",true,false,etaFlagFormatter,isClassWhite()));
		columns.push(column("property_code",true,"性质",true,false,null,isClassWhite()));
		columns.push(column("attr_code",true,"属性",true,false,null,isClassWhite()));
		columns.push(column("airline_code",true,"航空公司",true,false,null,isClassWhite()));
		columns.push(column("flt_date",true,"日期",true,false,nullFormatter,isClassWhite()));
		columns.push(column("share_flt_no",true,"共享航班号",true,false,nullFormatter,isClassWhite()));
	return columns;
}

function refreshTable(){
	$("#baseTableLeft").bootstrapTable('refresh');
	$("#baseTableRight").bootstrapTable('refresh');
}


function fileOnChange(){
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
				var suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
				if (suffix != "txt") {
					msg = '上传列表存在不支持的文件类型！请上传txt类型中航信数据文件';
					check = true;
				}
			}
		});
		if(countFileNum <= 0){
			msg = '您还没有选择要导入的文件！';
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
	error : function(e) {
		resetUpload();
		layer.close(loading);
		layer.msg('导入失败！', {
			icon : 2
		});
	},
	success : function(result) {
//		delete $("#fileInput")[0].files;
//		$("#fileTable").empty();
		resetUpload();
		layer.close(loading);
		if ("success" == result) {
			refreshTable();
			openFilter();
		} else  {
			layer.msg('导入失败！', {
				icon : 2
			});
		}
	}
	});
}

function openFilter(){
	var addIframe = layer.open({
		type : 2,
		title : "请核对下列计划信息",
		content : ctx + "/tomorrow/plan/toFilterPlan",
		btn : ['批量保存','批量删除','取消'],
		yes : function(index, layero) {
			$(layero).find("iframe")[0].contentWindow.saveBatch();
		},
		btn2 : function(index, layero) {
			$(layero).find("iframe")[0].contentWindow.deleteBatch();
			return false;
		},
		btn3 : function(index, layero) {
			layer.close(index);
		}
	});
	layer.full(addIframe);
}

/**
 * 根据记录状态值，改变cell值显示style
 */
function stateFormatter(value, row, index, field){
	var dataState = row.data_state;
	if(dataState&&dataState==2){
		return "<span class='label bg-green'>"+value+"</span>"
	} else {
		return value;
	}
}

/**
 * 根据记录状态值，改变cell值显示style
 */
function etaFlagFormatter(value, row, index, field){
	//0:未修正，1:修正失败，2:修正成功
	var etaCalculateFlag = row.eta_calculate_flag;
	if(etaCalculateFlag==0||etaCalculateFlag==1){
		return "<span style='border:1px solid #F60;'>"+value+"</span>";
	}else{
		return value;
	}
}

/**
 * 分类查询，全部计划、已发布、未发布
 */
function choosePlanType(){
	var planType = $("#planType").val();
	$("#baseTableLeft").bootstrapTable('refresh',{query:{ioType : "A",planType:planType}});
	$("#baseTableRight").bootstrapTable('refresh',{query:{ioType : "D",planType:planType}});
}

function save(isNew,data){
	var succeed = false;
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		url : ctx + "/tomorrow/plan/save",
		async : false,
		data : {
			isNew:isNew,
			newRows:JSON.stringify(data.newRows),
			editRows:JSON.stringify(data.editRows),
			removeRows:JSON.stringify(data.removeRows),
			ioType:data.ioType
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

function msgShow(row) {
	iframe = layer.open({
		type : 2,
		title : "次日计划报文",
		content : ctx + "/tomorrow/plan/showMsg?msgId=" + row.msg_id,
		area : [ '700px', '400px' ],
		btn : ["取消"],
		btn1 : function(index, layero) {
			layer.close(index);
		}
	});
}

function uploadWin(){
	var iframe = layer.open({
		type : 1,
		area: ['450px', '250px'],
		title : "上传中航信计划文件",
		content : $("#upload")
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
	$("#fileInput1").val('');
	$("#fileName1").text('');
	$("#fileSize1").text('');
}

function checkRow(){
	//进港次日计划
	var arrivalPlanData = $("#baseTableLeft").bootstrapTable("getData");
	//出港次日计划
	var departPlanData = $("#baseTableRight").bootstrapTable("getData");
	//起场计划校验
	var tableDepartMsg = "";
	if(departPlanData&&departPlanData.length>0){
		for(var i=0;i<departPlanData.length;i++){
			var row = departPlanData[i];
			var msg = checkColumn(row,'depart');
			if(msg!=''){
				tableDepartMsg = tableDepartMsg +"<tr><td>"+msg+"</td></tr>";
			}
		}
	}
	if(tableDepartMsg!=''){
		tableDepartMsg = "<table>"+tableDepartMsg+ "</table>";
	}
	//落场计划校验
	var tableArrivalMsg = "";
	if(arrivalPlanData&&arrivalPlanData.length>0){
		for(var i=0;i<arrivalPlanData.length;i++){
			var row = arrivalPlanData[i];
			var msg = checkColumn(row,'arrival');
			if(msg!=''){
				tableArrivalMsg = tableArrivalMsg +"<tr><td>"+msg+"</td></tr>";
			}
		}
	}
	if(tableArrivalMsg!=''){
		tableArrivalMsg = "<table>"+tableArrivalMsg+ "</table>";
	}
	
	if(tableDepartMsg!=''||tableArrivalMsg!=''){
		$("#errMsg")[0].innerHTML=tableDepartMsg+tableArrivalMsg;
		var iframe = layer.open({
			type : 1,
			area: ['450px', '260px'],
			title : "发布数据校验失败",
			content : $("#errMsg")
		});
		return true;
	}else{
		return false;
	}
}

function checkColumn(row,type){
	//type: depart arrival
	var apt = '';
	if(type=='depart'){
		apt="离港计划列表,";
	}else if(type=='arrival'){
		apt="进港计划列表,";
	}
	var msg = "";
	//航班号
	if(row.flt_no==''||row.flt_no==undefined||row.flt_no==null){
		msg+="检查航班号为空计划,";
	}else{
		apt+="航班号:"+row.flt_no+"计划,"
	}
	//机型
	if(row.act_type==''||row.act_type==undefined||row.act_type==null){
		msg+="机型为空,";
	}
	//起场、落场
	if(row.routeName==''||row.routeName==undefined||row.routeName==null){
		if(type=='depart'){
			msg+="起场为空,";
		}else if(type=='arrival'){
			msg+="落场为空,";
		}
	}
	//计起
	if(row.std==''||row.std==undefined||row.std==null){
		msg+="计起为空,";
	}
	//计落
	if(row.sta==''||row.sta==undefined||row.sta==null){
		msg+="计落为空,";
	}
	//预起
	if(row.etd==''||row.etd==undefined||row.etd==null){
		msg+="预起为空,";
	}
	//预落
	if(row.eta==''||row.eta==undefined||row.eta==null){
		msg+="预落为空,";
	}
	//性质
	if(row.property_code==''||row.property_code==undefined||row.property_code==null){
		msg+="性质为空,";
	}
	//属性
	if(row.attr_code==''||row.attr_code==undefined||row.attr_code==null){
		msg+="属性为空,";
	}
	//航空公司
	if(row.airline_code==''||row.airline_code==undefined||row.airline_code==null){
		msg+="航空公司为空,";
	}
	//日期
	if(row.flt_date==''||row.flt_date==undefined||row.flt_date==null){
		msg+="日期为空,";
	}
	if(msg!=''){
		msg=apt+msg;
	}
	return msg;
}

function searchOpt(text,obj) {
	searchOption=text;
	var icon=$("#searchOptBtn").find("span").eq(0);
	$("#searchOptBtn").text($(obj).text());
	$("#searchOptBtn").append(icon);
}