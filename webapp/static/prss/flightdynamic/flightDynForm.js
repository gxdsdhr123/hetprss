var layer;
var form;
var oldRows;
var cancleFlights = [];
layui.use([ 'form', 'layer' ], function() {// 调用layui表单及弹出层组件
	layer = layui.layer;
	form=layui.form;
	//进港性质
	form.on('select(aProperty)', function(data){
		var gridData = $("#formGrid").bootstrapTable("getData");
		if(gridData&&gridData.length>0){
			for(var i=0;i<gridData.length;i++){
				var row = gridData[i];
				var ioType = row.IOTYPE;
				if(row.ARRIVALAPT&&currentAirport==row.ARRIVALAPT&&ioType&&ioType.indexOf("A")>-1){
					row["PROPERTY"] = data.value;
					if (!isNewRow(row.ID)) {
						editRows[row.ID] = $.extend(true,{},row);
					}
					break;
				}
			}
		}
	});
	//出港性质
	form.on('select(dProperty)', function(data){
		var gridData = $("#formGrid").bootstrapTable("getData");
		if(gridData&&gridData.length>0){
			for(var i=0;i<gridData.length;i++){
				var row = gridData[i];
				var ioType = row.IOTYPE;
				if(row.DEPARTAPT&&currentAirport==row.DEPARTAPT&&ioType&&ioType.indexOf("D")>-1){
					row["PROPERTY"] = data.value;
					if (!isNewRow(row.ID)) {
						editRows[row.ID] = $.extend(true,{},row);
					}
					break;
				}
			}
		}
	});
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
	//进港航班
	$("#aFltNo").blur(function(){
		var aFltNo = $(this).val();
		if(aFltNo&&aFltNo.length>=3){
			var gridData = $("#formGrid").bootstrapTable("getData");
			if(gridData&&gridData.length>1){
				$("#formGrid").bootstrapTable("updateCell", {
					index : 0,
					field : "FLTNO",
					value : aFltNo
				});
				$.post(ctx + "/flightDynamic/getAlnCode", {
					fltNo : aFltNo
				}, function(alnCode) {
					if(alnCode){
						$("#formGrid").bootstrapTable("updateCell", {
							index : 0,
							field : "ALN",
							value : alnCode
						});
					} else {
						layer.tips('没有匹配航空公司', '#aFltNo',{
							tips:[2,"#2867BC"]
						});
					}
				})
			}
		
		}
	});
	//出港航班
	$("#dFltNo").blur(function(){
		var dFltNo = $(this).val();
		if(aFltNo&&dFltNo.length>=3){
			var gridData = $("#formGrid").bootstrapTable("getData");
			if(gridData&&gridData.length>1){
				$("#formGrid").bootstrapTable("updateCell", {
					index : 1,
					field : "FLTNO",
					value : dFltNo
				});
				$.post(ctx + "/flightDynamic/getAlnCode", {
					fltNo : dFltNo
				}, function(alnCode) {
					if(alnCode){
						$("#formGrid").bootstrapTable("updateCell", {
							index : 1,
							field : "ALN",
							value : alnCode
						});
					} else {
						layer.tips('没有匹配航空公司', '#dFltNo',{
							tips:[2,"#2867BC"]
						});
					}
				})
			}
		
		}
	});
	initGrid();
});

// 当前输入框
var nowCol;

/**
 * 初始化表格
 */
function initGrid(){
	var dataSource = $("#dataSource").val();
	var url = ctx + "/flightDynamic/formGridData";
	if(dataSource&&dataSource==2){//基于报文消息新增
		url = ctx + "/asup/messageHandler/newFltData?sourceId="+$("#sourceId").val();
	}
	$("#formGrid").bootstrapTable({
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : false, // 是否显示全选
		search : false, // 是否开启搜索功能
		editable:true,//开启编辑模式
		searchOnEnterKey : true,
		uniqueId:"UNIQUEID",
		url : url,
		queryParams : function(params) {
			var param = {
				inFltId:$("#inFltId").val(),
				outFltId:$("#outFltId").val(),
				isHis:$("#isHis").val()
			};
			return param;
		},
		responseHandler : function(res){
			for(var i=0;i<res.length;i++){
				if(res[i].STATUS == "1"){
					cancleFlights.push(res[i].FLTID);
				}
			}
			return res;
		},
		onClickCell : function(field, value, row, $element) {
			if(field == "FLTDATE"){
				popupDate(field, value, row, $element,"FLTDATE");
			}else if(field == "BRDBTM"){
				popupDate(field, value, row, $element,"BRDBTM");
			}else if(field == "BRDETM"){
				popupDate(field, value, row, $element,"BRDETM");
			}
		},
		onLoadSuccess:function(data){
			var count = data.length;
			if(count==0){
				for(var i=0;i<2;i++){
					addRow();
				}
			} else {
				var aFltNo = $("#aFltNo").val();
				var aProperty = $("#aProperty").val();
				var dFltNo = $("#dFltNo").val();
				var dProperty = $("#dProperty").val();
				var actstandCode = $("#actstandCode").val();
				for(var i=0;i<count;i++){
					var row = data[i];
					var departApt = row.DEPARTAPT;
					var arrivalApt = row.ARRIVALAPT;
					var ioType = row.IOTYPE;//进出港标识
					//设置进港航班号、进港航班性质
					if(currentAirport==arrivalApt&&$.trim(aFltNo)==""){
						$("#aFltNo").val(row.FLTNO);
						$("#aProperty").val(row.PROPERTY);
						//$("#aFltNo").attr("disabled","disabled");
						if($.trim(actstandCode)==""&&row.ACTSTANDCODE){
							$("#actstandCode").val(row.ACTSTANDCODE);
						}
					}
					//设置出港航班号、出港航班性质
					if(currentAirport==departApt&&$.trim(dFltNo)==""){
						$("#dFltNo").val(row.FLTNO);
						$("#dProperty").val(row.PROPERTY);
						//$("#dFltNo").attr("disabled","disabled");
						if($.trim(actstandCode)==""&&row.ACTSTANDCODE){
							$("#actstandCode").val(row.ACTSTANDCODE);
						}
					}
					disabledColumn();
				}
				if($("#isNew").val()=="false"){
					$("#actstandCode").attr("disabled","disabled");
				}
				form.render(null, 'baseForm');
			}
		},
		onEditableSave : function(field, row, oldValue, $el) {
			var rowIndx = $el.parent().parent().index();
			var value = row[field];
			var tr = $el.parents("tr");
			if (!isNewRow(row.ID)) {
				editRows[row.ID] = $.extend(true,{},row);
			}
			var value = row[field];
			//STD值保存后自动写入ETD中
			if(field=="STD"){
				updateCell(rowIndx,value,"ETD",tr);
				//计算预落
				calEta(row,value,rowIndx,tr);
			}else if(field=="ETD"){
				//计算预落
				calEta(row,value,rowIndx,tr);
			}else if(field=="ATD"){
				//计算预落
				calEta(row,value,rowIndx,tr);
			}else if ("FLTNO" == field && value && value.length >= 3) {
				// 航班号联动航空公司,航班号三字码大写
				updateCell(rowIndx,value.toUpperCase(),"FLTNO",tr);
				//如果起场是包头  说明是出港航班
				if(row["DEPARTAPT"]==currentAirport){
					$("#dFltNo").val(value.toUpperCase());
				}
				if(row["ARRIVALAPT"]==currentAirport){//如果落场是包头  说明是进港航班
					$("#aFltNo").val(value.toUpperCase());
				}
				$.ajax({
					type : 'POST',
					url : ctx + "/flightDynamic/getAlnCode",
					data : {
						fltNo : value
					},
					async:false,
					success : function(alnCode) {
						if(alnCode){
							updateCell(rowIndx,alnCode,"ALN",tr);
						} else {
							layer.msg("航班号没有匹配的航空公司！",{icon:7,time:1000});
						}
					}
				});
				// 航班号联动共享航班
				$.post(ctx + "/flightDynamic/getShareFltNo", {
					fltNo : value
				}, function(shareFltNo) {
					updateCell(rowIndx,shareFltNo,"SHAREFLTNO",tr);
				})
			} else if(field=="DEPARTAPT"){//将起落场在包头航班号自动带入到进出港航班中
				//如果起场是包头  说明是出港航班
				if(row["DEPARTAPT"]==currentAirport){
					$("#dFltNo").val(row["FLTNO"]);
				}
			} else if(field=="ARRIVALAPT"){
				//如果起场是包头  说明是出港航班
				if(row["ARRIVALAPT"]==currentAirport){
					$("#aFltNo").val(row["FLTNO"]);
				}
			} else if ("AIRCRAFTNO" == field && row.AIRCRAFTNO!=oldValue) {
				value = value?value.toUpperCase():value;
				updateCell(rowIndx,value,field,tr);
				//匹配机型
				$.ajax({
					type : 'POST',
					url : ctx + "/flightDynamic/getActType",
					data : {
						aftNum : value
					},
					async:false,
					error : function() {
						layer.msg('机号校验请求失败，请重试！', {
							icon : 2
						});
					},
					success : function(act) {
						if(act){
							updateCell(rowIndx,act,"ACTTYPE",tr);
						} else {
							layer.msg('没有匹配机型！', {
								icon : 7
							});
						}
					}
				});
				//匹配航空公司
				$.ajax({
					type : 'POST',
					url : ctx + "/flightDynamic/getAlnCode",
					data : {
						aircraftNo : value
					},
					async:false,
					success : function(alnCode) {
						var fltNo = row.FLTNO;
						var alnVal = row.ALN;
						var msg = "";
						if(fltNo&&fltNo.substring(0,3)!=alnCode){
							msg ="航班号";
						}
						if(alnVal&&alnCode!=alnVal){
							msg +="航空公司";
						}
						if(msg){
							layer.msg("机号与"+msg+"不匹配",{icon:7});
						}
					}
				});
			} else if(field=="ALN"&&value){
				var fltNo = row.FLTNO;
				if(fltNo&&fltNo.substring(0,3)!=value){
					layer.msg("航空公司与航班号不匹配",{icon:7});
				}
			}
//			else if(field=="STATUS"&&value){
//				if(value=="1"){//选择了取消
//					if(row.DEPARTAPT == currentAirport || row.ARRIVALAPT == currentAirport){
//						statusSource=null;
//						updateCell(rowIndx,'0',field,tr);
//						layer.msg("包含本场，不可取消",{icon:7});
//					}
//					
//				}
//			}
			disabledColumn();
		},
		onEditableShown: function(field, row, $el, editable) {
			nowCol = field + "_" + row.ID;
			setTimeout(function(){
				$el.next().find(".select2-hidden-accessible").select2('open');
			},100);
        },
		onEditableHidden: function(field, row, $el, reason) {
			if(nowCol != field + "_" + row.ID){
				return;
			}
			$el.next().find(".select2-hidden-accessible").select2('close');
			if(field == "AIRCRAFTNO"){
				if ($el.parent().next().next() && $el.parent().next().next().is("td")) {
					var edit = $el.parent().next().next().find('.editable');
					if(edit.text() == "" || edit.text() == null){
						edit.editable('show');
					}
				}
			}else{//下一个为空的项弹出
				show($el);
				function show(ele){
					if (ele.parent().next() && ele.parent().next().is("td")&&field!="ATD"&&field!="ATA"&&field!="SHAREFLTNO"&&field!="ALN"&&field!="STATUS"&&field!="DELAYREASON"&&field!="RELEASEREASON"&&field!="REMARK") {
						var edit = ele.parent().next().find('.editable');
						var nextField = edit[0].attributes["data-name"].value;
						if(nextField!="ATD"&&nextField!="ATA"&&nextField!="SHAREFLTNO"&&nextField!="ALN"&&nextField!="STATUS"&&nextField!="DELAYREASON"&&nextField!="RELEASEREASON"&&nextField!="REMARK"){
							if(edit.text() == "" || edit.text() == null){
								edit.editable('show');
							}else{
								show(edit);
							}
						}
					}
				}
			}
        },
		columns : getColumns()
	});
}

function updateCell(rowIndex,value,field,tr){
	var dataList = $("#formGrid").bootstrapTable("getData");
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
function getColumns(){
	var columns = [ {
		field : "checkbox",
		checkbox : true,
		sortable : false,
		title : "",
		visible : true,
		editable : false
	},{
		field : "order",
		title : "序号",
		sortable : false,
		editable : false,
//		width:40,
		formatter : function(value, row, index) {
			return index + 1;
		}
	},{
		field : "FLTNO",
		title : "航班号",
		required:true,
//		width:100,
		editable :{
			type:"text",
			display : function(value){
				$(this).html(value.toUpperCase());
			}
		}
	}, {
		field : "AIRCRAFTNO",
		title : "机号",
//		width:100,
		editable :{
			type:"text",
			display : function(value){
				$(this).html(value.toUpperCase());
			}
		}
	}, {
		field : "ACTTYPE",
		title : "机型",
//		width:100,
		required:true,
		editable : {
			type : "select2",
			source : actTypeSource,
			onblur:'ignore',
			select2 : {
//				placeholder : "请选择机型",
//				width : "80px"
			}
		}
	}, {
		field : "DEPARTAPT",
		title : "起场",
		required:true,
//		width:100,
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airportCodeSource,
			select2 : {
//				placeholder : "请选择起场",
//				width : "80px",
				matcher: aptMatcher
			}
		}
	}, {
		field : "ARRIVALAPT",
		title : "落场",
//		width:100,
		required:true,
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airportCodeSource,
			select2 : {
//				placeholder : "请选择起场",
//				width : "80px",
				matcher: aptMatcher
			}
		}
	},  {
		field : "STD",
		title : "计起",
		required:true,
		validType:1,
//		width:"120px",
		editable : {
			type:'text'
//			,placeholder:"0000~2359"
		}
	},  {
		field : "STA",
		title : "计落",
		required:true,
		validType:1,
//		width:"120px",
		editable : {
			type:'text'
//			,placeholder:"0000~2359"
		}
	},  {
		field : "ETD",
		title : "预起",
		required:true,
		validType:1,
//		width:"120px",
		editable : {
			type:'text'
//			,placeholder:"0000~2359"
		}
	},  {
		field : "ETA",
		title : "预落",
		required:true,
//		width:"120px",
		validType:1,
		editable : {
			type:'text'
//			,placeholder:"0000~2359"
		}
	},  {
		field : "ATD",
		title : "实起",
//		width:"120px",
		validType:1,
		editable : {
			type:'text'
//			,placeholder:"0000~2359"
		}
	},  {
		field : "ATA",
		title : "实落",
		validType:1,
//		width:"120px",
		editable : {
			type:'text'
//			,placeholder:"0000~2359"
		}
	},{
		field : "SHAREFLTNO",
		title : "共享航班",
		editable :{
			type:"text",
			display : function(value){
				$(this).html(value.toUpperCase());
			}
		}
	}, {
		field : "ALN",
		title : "航空公司",
//		width:100,
		editable : {
			type : "select2",
			source : airlinesCodeSource,
			select2 : {
//				placeholder : "请选择航空公司",
//				width : "80px"
			}
		}
	}, {
		field : "STATUS",
		title : "状态",
		width:80,
		editable : {
			type : "select2",
			onblur : "ignore",
			source : statusSource,
			select2 : {
				//placeholder : "请选择状态",
//				width : "80px"
			}
		}
	}, {
		field : "DELAYREASON",
		title : "延误原因",
		width:100,
		editable : {
			type : "select2",
			source : alnDelaySource,
			select2 : {
				//allowClear: true
				//placeholder : "请选择延误原因",
//				width : "80px"
				//placeholderOption: "first"
				
		}
	}
	}, {
		field : "RELEASEREASON",
		title : "放行原因",
//		width:100,
		editable : {
			disabled : false,
			type : "select2",
			source : releaseDelaySource,
			select2 : {
				//placeholder : "请选择放行原因",
//				width : "80px"
			}
		}
	}, {
		field : "REMARK",
		title : "二级原因",
		editable :{
			type:"text"
		},
		width:100,
	}, {
		field : "FLTDATE",
		title : "航班日期",
		required:true,
//		width:60,
		formatter : function(value, row, index) {
			return "<a href='javascript:void(0)' id='fltDate" + index + "'>"
					+ (value ? value : '请选择') + "</a>";
		}
	}/*, {
		field : "BRDBTM",
		title : "登机开始时间",
		formatter : function(value, row, index) {
			return "<a href='javascript:void(0)' id='brdbtm" + index + "'>"
					+ (value ? value : '请选择') + "</a>";
		}
	}, {
		field : "BRDETM",
		title : "登机结束时间",
		formatter : function(value, row, index) {
			return "<a href='javascript:void(0)' id='brdetm" + index + "'>"
					+ (value ? value : '请选择') + "</a>";
		}
	}*/];
	return columns;
}
/**
 * 修改时禁用指定列
 */
function disabledColumn(){
	if($("#isNew").val()=="false"){
		var gridData = $("#formGrid").bootstrapTable("getData");
		//修改时禁止修改本场航班
		for(var i=0;i<gridData.length;i++){
			var row = gridData[i];
			var departApt = row.DEPARTAPT;
			var arrivalApt = row.ARRIVALAPT;
			var ioType = row.IOTYPE;
			if(departApt==currentAirport&&ioType&&ioType.indexOf("D")>-1){
				$("#formGrid tr:eq("+(i+1)+") td").each(function(){
					var editableTag = $(this).find("a.editable");
					if(editableTag&&editableTag.length>0){
						var dataName = editableTag.data("name");
						if(dataName&&(dataName=="DEPARTAPT")){
							editableTag.addClass("editable-disabled"); 
							editableTag.unbind("click"); 
						}
					}
				});
				$("#formGrid tr:eq("+(i+1)+") input:checkbox").attr("disabled","disabled");
			}
			if(arrivalApt==currentAirport&&ioType&&ioType.indexOf("A")>-1){
				$("#formGrid tr:eq("+(i+1)+") td").each(function(){
					var editableTag = $(this).find("a.editable");
					if(editableTag&&editableTag.length>0){
						var dataName = editableTag.data("name");
						if(dataName&&(dataName=="ARRIVALAPT")){
							editableTag.addClass("editable-disabled"); 
							editableTag.unbind("click"); 
						}
					}
				});
				$("#formGrid tr:eq("+(i+1)+") input:checkbox").attr("disabled","disabled");
			}
		}
	}
}
//添加行
function addRow(){
	$("#formGrid").bootstrapTable("append",{
		UNIQUEID:(new Date().getTime()),
		ID:"new_"+(new Date().getTime()),
		FLTNO : "",
		AIRCRAFTNO : "",
		ACTTYPE : "",
		DEPARTAPT : "",
		ARRIVALAPT : "",
		STD : "",
		STA : "",
		ETD : "",
		ETA : "",
		ATD : "",
		ATA : "",
		SHAREFLTNO : "",
		ALN : "",
		STATUS : "",
		DELAYREASON : "",
		RELEASEREASON : "",
		REMARK : "",
		FLTDATE:getNowFormatDate()
	});
	disabledColumn();
}
//删除一行
function removeRow(){
	var selections = $("#formGrid").bootstrapTable("getSelections");
	if(selections.length==0){
		layer.msg("请选择要删除的航班！",{icon:7});
	}
	if(selections&&selections.length>0){
		for(var i=0;i<selections.length;i++){
			var row = selections[i];
			if(row&&row.ID){
				if (!isNewRow(row.ID)) {
					removeRows[row.ID] = $("#formGrid").bootstrapTable("getRowByUniqueId",row.UNIQUEID);
				}
				$("#formGrid").bootstrapTable("removeByUniqueId",row.UNIQUEID);
			}
		}
	}
	disabledColumn();
}
// 获取当前日期
function getNowFormatDate() {
	var date = new Date();
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	var currentdate = year+""+month+""+day;
    return currentdate;
}
// 保存
var editRows = {};//修改的行
var removeRows = {};//删除的行
function save(){
	//解决表格处于编辑状态下直接点击保存按钮是数据
	$("#formGrid tbody tr td form").each(function(){
		$(this).trigger("submit");
	});
	var dataList = $("#formGrid").bootstrapTable("getData");
	if (dataList.length < 1 ) {
        layer.msg("没有要保存的航班！",{icon:7});
        return;
    }
	/***表格上面共有信息***/
	var inFltNo = $("#aFltNo").val();//进港航班号
	var outFltNo = $("#dFltNo").val();//出港航班号
	var aProperty = $("#aProperty").val();//进港性质
	var dProperty = $("#dProperty").val();//出港性质
	var actstandCode = $("#actstandCode").val();//机位
	if($.trim(inFltNo)==""&&$.trim(outFltNo)==""){
		layer.msg("请输入进港航班号或出港航班号！",{icon:7});
		return false;
	}
	if($.trim(inFltNo)&&!aProperty){
		layer.msg("请选择进港性质！",{icon:7});
		return false;
	} else if(!$.trim(inFltNo)&&aProperty){
		layer.msg("进港性质没有对应的航班！",{icon:7});
		return false;
	}
	if($.trim(outFltNo)&&!dProperty){
		layer.msg("请选择出港性质！",{icon:7});
		return false;
	} else if(!$.trim(outFltNo)&&dProperty){
		layer.msg("出港性质没有对应的航班！",{icon:7});
		return false;
	}
	/***表格上面共有信息 end***/
	
	/**表格数据部分**/
	var columns = getColumns();
	var newRows = [];//新增的行
	var ifContainApt = false;//校验表格中数据录入是否包含本场
	var inAircraftNO = "";//进港机号
	var outAircraftNO = "";//出港机号
	var emptyRowCount = 0;//空行数
	for(var i=0;i<dataList.length;i++){
		var data = $.extend(true,{},dataList[i]);
		if(isEmptyRow(columns,data)){
			emptyRowCount++;
			continue;
		}
		for(var j=0;j<columns.length;j++){
			var column = columns[j];
			var field = column.field;
			var title = column.title;
			var required = column.required;
			var validType = column.validType;
			var val = data[field];
			if(required&&(!val||$.trim(val)=="")){//校验必填
				layer.msg("第"+(i+1)+"行"+title+"不能为空！",{icon:7});
				return false;
			}
			if($.trim(val)!=""&&validType&&validType==1){//校验预起、预落等时间格式
				if(!validFltTime(val)){
					layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
					return false;
				}
//				if(validFltTime(val)){
//					var fltDate = data["FLTDATE"];
//					var fltTime = formatFltTime(fltDate,val);
//					data[field] = fltTime;
//				} else {
//					layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
//					return false;
//				}
			}
			//校验航班号
			if(field=="FLTNO"){
				if(val!=inFltNo&&val!=outFltNo){
					layer.msg("第"+(i+1)+"行与进港或出港航班号不匹配！",{icon:7});
					return false;
				}
			}
			if(field=="STATUS"){
				if(data["DEPARTAPT"] == currentAirport || data["ARRIVALAPT"] == currentAirport){
					if(val=="1"&&$.inArray(data.FLTID,cancleFlights)==-1){//选择了取消,并且不是已经取消航班
						layer.msg("第"+(i+1)+"行"+title+"不可设为取消！",{icon:7});
						return false;
					}
				}
			}
		}
		
		//计落小于计起,校验失败
		var std = data["STD"];//计起
		var sta = data["STA"];//计落
		if(std.indexOf("+")!=-1&&sta.indexOf("+")!=-1){
			//计起、计落都有"+"号
			var stdStr = std.replace(/\+/g, "");
			var staStr = sta.replace(/\+/g, "");
			if(Number(stdStr)>Number(staStr)){
				layer.msg("第"+(i+1)+"行,计落小于计起,无法保存",{icon:7});
				return false;
			}
		}else if(std.indexOf("+")==-1&&sta.indexOf("+")==-1){
			//计起、计落都没"+"号
			if(Number(std)>Number(sta)){
				layer.msg("第"+(i+1)+"行,计落小于计起,无法保存",{icon:7});
				return false;
			}
		}else if(std.indexOf("+")!=-1&&sta.indexOf("+")==-1){
			//计起有"+"号、计落没有"+"号
			layer.msg("第"+(i+1)+"行,计落小于计起,无法保存",{icon:7});
			return false;
		}
		//预落小于预起，或者预起小于计起,校验失败
		var etd = data["ETD"];//预起
		var eta = data["ETA"];//预落
		if(etd.indexOf("+")!=-1&&eta.indexOf("+")!=-1){
			//预起、预落都有"+"号
			var etdStr = etd.replace(/\+/g, "");
			var etaStr = eta.replace(/\+/g, "");
			if(Number(etdStr)>Number(etaStr)){
				layer.msg("第"+(i+1)+"行,预落小于预起,无法保存",{icon:7});
				return false;
			}
		}else if(etd.indexOf("+")==-1&&eta.indexOf("+")==-1){
			//预起、预落都没"+"号
			if(Number(etd)>Number(eta)){
				layer.msg("第"+(i+1)+"行,预落小于预起,无法保存",{icon:7});
				return false;
			}
		}else if(etd.indexOf("+")!=-1&&eta.indexOf("+")==-1){
			//预起有"+"号、预落没有"+"号
			layer.msg("第"+(i+1)+"行,预落小于预起,无法保存",{icon:7});
			return false;
		}
			
		//预起小于计起,校验失败
		if(etd.indexOf("+")!=-1&&std.indexOf("+")!=-1){
			//预起、计起都有"+"号
			var etdStr = etd.replace(/\+/g, "");
			var stdStr = std.replace(/\+/g, "");
			if(Number(stdStr)>Number(etdStr)){
				layer.msg("第"+(i+1)+"行,预起小于计起,无法保存",{icon:7});
				return false;
			}
		}else if(etd.indexOf("+")==-1&&std.indexOf("+")==-1){
			//预起、计起都没"+"号
			if(Number(std)>Number(etd)){
				layer.msg("第"+(i+1)+"行,预起小于计起,无法保存",{icon:7});
				return false;
			}
		}else if(std.indexOf("+")!=-1&&etd.indexOf("+")==-1){
			//计起有"+"号、预起没有"+"号
			layer.msg("第"+(i+1)+"行,预起小于计起,无法保存",{icon:7});
			return false;
		}
		
		//实落小于实起,校验失败
		var atd = data["ATD"];//实起
		var ata = data["ATA"];//实落
		if(atd!=''&&atd!=null&&atd!='undefined'&&ata!=''&&ata!=null&&ata!='undefined'){
			if(atd.indexOf("+")!=-1&&ata.indexOf("+")!=-1){
				//实起、实落都有"+"号
				var atdStr = atd.replace(/\+/g, "");
				var ataStr = ata.replace(/\+/g, "");
				if(Number(atdStr)>Number(ataStr)){
					layer.msg("第"+(i+1)+"行,实落小于实起，无法保存！",{icon:7});
					return false;
				}
			}else if(atd.indexOf("+")==-1&&ata.indexOf("+")==-1){
				//实起、实落都没"+"号
				if(Number(atd)>Number(ata)){
					layer.msg("第"+(i+1)+"行,实落小于实起，无法保存！",{icon:7});
					return false;
				}
			}else if(atd.indexOf("+")!=-1&&ata.indexOf("+")==-1){
				//实起有"+"号、实落没有"+"号
				layer.msg("第"+(i+1)+"行,实落小于实起，无法保存！",{icon:7});
				return false;
			}
		}
		
		//判断录入数据中是否包含本场
		if(!ifContainApt&&(currentAirport==data.ARRIVALAPT||currentAirport==data.DEPARTAPT)){
			ifContainApt = true;
		}
		if(inFltNo&&outFltNo){
			if(currentAirport==data.ARRIVALAPT){//进港机号
				inAircraftNO = data.AIRCRAFTNO;
			}
			if(currentAirport==data.DEPARTAPT){//出港机号
				outAircraftNO = data.AIRCRAFTNO;
			}
		}
		//新增的行
		if(data.ID&&isNewRow(data.ID)){
			delete data["ID"];
			var fltDate = data.FLTDATE;
			data.STD = formatFltTime(fltDate,data.STD);//转换std格式
			data.STA = formatFltTime(fltDate,data.STA);//转换sta格式
			data.ETA = formatFltTime(fltDate,data.ETA);//转换ETA格式
			data.ETD = formatFltTime(fltDate,data.ETD);//转换ETD格式
			if(data.ATA){
				data.ATA = formatFltTime(fltDate,data.ATA);//转换ATA格式
			}
			if(data.ATD){
				data.ATD = formatFltTime(fltDate,data.ATD);//转换ATD
			}
			if(data.STATUS=="-1"){
				data.STATUS='';
			}
			newRows.push(data);
		} else if(data.ID&&editRows[data.ID]){//修改的行
			var editedRow = editRows[data.ID];
			var fltDate = editedRow.FLTDATE;
			editedRow.STD = formatFltTime(fltDate,editedRow.STD);//转换std格式
			editedRow.STA = formatFltTime(fltDate,editedRow.STA);//转换sta格式
			editedRow.ETA = formatFltTime(fltDate,editedRow.ETA);//转换ETA格式
			editedRow.ETD = formatFltTime(fltDate,editedRow.ETD);//转换ETD格式
			if(editedRow.ATA){
				editedRow.ATA = formatFltTime(fltDate,editedRow.ATA);//转换ATA格式
			}
			if(editedRow.ATD){
				editedRow.ATD = formatFltTime(fltDate,editedRow.ATD);//转换ATD
			}
			if(editedRow.STATUS=="-1"){
				editedRow.STATUS='';
			}
			if(data.DEPARTAPT==currentAirport && data.STATUS != "1" && $.inArray(data.FLTID,cancleFlights)>-1){
				editedRow.restore = "1";
			}else{
				editedRow.restore = "0";
			}
		}
	}
	if(emptyRowCount==dataList.length){
		layer.msg("请录入航班信息！",{icon:7});
		return false;
	}
	if(!ifContainApt){
		layer.msg("请录入与本机场有关的航班！",{icon:7});
		return false;
	}
	if(inAircraftNO!=outAircraftNO){
		layer.msg("进、出港机号不同，无法保存！",{icon:7});
		return false;
	}
	var result = {
			fltInfo:{
				inFltNo:inFltNo,
				outFltNo:outFltNo,
				aProperty:aProperty,
				dProperty:dProperty,
				actstandCode:actstandCode,
				inFltId:$("#inFltId").val(),
				outFltId:$("#outFltId").val()
			},
			newRows:newRows,
			editRows:editRows,
			removeRows:removeRows,
			dataList:dataList,
			dataSource:$("#dataSource").val()
	}
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/flightDynamic/save",
		async : false,
		data : {
			data : JSON.stringify(result),
			isNew:$("#isNew").val()
		},
		dataType : "json",
		success : function(result) {
			layer.close(loading)
			if (result.status == 1) {
				var dataSource = $("#dataSource").val();
				if(dataSource&&dataSource==2){//基于报文消息新增
					var sourceId = $("#sourceId").val();
					parent.addFltCallback(JSON.stringify(result),sourceId);
				} else {
					layer.msg("保存成功！",{time:1000,icon:1},function(){
						parent.saveSuccess();
					});
				}
			} else {
				layer.alert("保存失败"+result.error,{icon:2});
			}
		},
		error:function(msg){
			var result = "保存失败";
			layer.msg(result, {icon: 1,time: 1000});
			layer.close(loading)
		}
	});
}
/**
 * 判断是否为空行
 */
function isEmptyRow(columns,row){
	var isEmpty = true;
	//跳过空行
	for(var j=0;j<columns.length;j++){
		var column = columns[j];
		var field = column.field;
		if(field!="checkbox"&&field!="order"&&field!="FLTDATE"){
			var val = row[field];
			if(val&&$.trim(val)!=""){
				isEmpty = false;
				break;
			}
		}
	}
	return isEmpty;
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
	if(fltTime.length <6){
		var date = new Date(fltDate.substring(0,4)+"/"+fltDate.substring(4,6)+"/"+fltDate.substring(6,8));
		if(fltTime.indexOf("\+")>0){
			date.setDate(date.getDate() + 1);
		}
		var year = date.getFullYear();
		var month = (Array(2).join(0) + (parseInt(date.getMonth()) + 1)).slice(-2);
		var day = (Array(2).join(0) + date.getDate()).slice(-2);
		fltDate = year+"-"+month+"-"+day;
		return fltDate+" "+fltTime.substring(0,2)+":"+fltTime.substring(2,4);
	}else{
		return fltTime;
	}
}
function popupDate(field, value, row, $element,whichDate){
	
	if("FLTDATE"==whichDate){
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
					if (!isNewRow(row.ID)) {
						editRows[row.ID] = $.extend(true,{},row);
					}
				}
			}
		});
	}else if("BRDBTM" == whichDate){
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
			dateFmt:"yyyy-MM-dd HH:mm",
			onpicked : function(dp) {
				var newValue = dp.cal.getDateStr();
				if (newValue) {
					row[field] = newValue;
					if (!isNewRow(row.ID)) {
						editRows[row.ID] = $.extend(true,{},row);
					}
				}
			}
		});
	}else if("BRDETM" == whichDate){
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
			dateFmt:"yyyy-MM-dd HH:mm",
			onpicked : function(dp) {
				var newValue = dp.cal.getDateStr();
				if (newValue) {
					row[field] = newValue;
					if (!isNewRow(row.ID)) {
						editRows[row.ID] = $.extend(true,{},row);
					}
				}
			}
		});
	}
	
}
//判断新增行
function isNewRow(id){
	var reg = new RegExp("^new_");
	return reg.test(id);
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
 * 获取表格数据
 * @returns
 */
function getGridData(){
	return $("#formGrid").bootstrapTable("getData");
}

/**
 * 计算预落
 */
function calEta(row,value,rowIndx,tr){
	//根据预起获取计算的预落
	var departApt = row["DEPARTAPT"];
	var arrivalApt = row["ARRIVALAPT"];
	var etd = value;
	//如果有实起,根据实起计算预落
	var atd = row["ATD"];
	if(atd!=''&&atd!='undefined'&&atd!=null){
		etd = atd;
	}
	
	var actType = row["ACTTYPE"];
	var fltDate = row["FLTDATE"];
	var fltNo = row["FLTNO"];
	
	var aFltNo = $("#aFltNo").val();//进港航班号
	var dFltNo = $("#dFltNo").val();//出港航班号
	var aptCode = "";
	if((aFltNo!=''&&aFltNo!='undefined'&&aFltNo!=null)||(dFltNo!=''&&dFltNo!='undefined'&&dFltNo!=null)){
		//双击更新
		if(fltNo==aFltNo){
			//进港航班
			aptCode = departApt;
		}else if(fltNo==dFltNo){
			//出港航班
			aptCode = arrivalApt;
		}
	}else{
		//点击新增
		if(departApt==currentAirport&&arrivalApt!=currentAirport){
			aptCode = arrivalApt;
		}else if(departApt!=currentAirport&&arrivalApt==currentAirport){
			aptCode = departApt;
		}
	}
	if(etd!=''&&aptCode!=''&&fltDate!=''){
		$.ajax({
			type : 'POST',
			url : ctx + "/flightDynamic/getEta",
			data : {
				etd : etd,
				actType : actType,
				aptCode : aptCode,
				fltDate : fltDate,
			},
			async:false,
			success : function(eta) {
				if(eta!='undefined'&&eta!=null&&eta!=''){
					//修正成功
					updateCell(rowIndx,eta,"ETA",tr);
				}else{
					//修正失败
					layer.msg("eta计算无结果！",{time:3000,icon:0})
				}
			}
		});
	}
}