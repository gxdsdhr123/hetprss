var gufl =  $(".gufl").val();
debugger
$("#douxuefeng").val(gufl);


var layer;
layui.use([ "layer", "form" ], function() {
	layer = layui.layer;
});
var receiverData = "";
$(function(){
	initSelect();
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	
	$.ajax({
		type : 'post',
		async : false,
		url:ctx+"/arrival/produce/initTable",
		success : function(result) {
			debugger
			receiverData = eval(result);
		}
	});
	
	
	var editable = true;
	var disabled = false;
	createDetailColumns = [
	                       
	{
		field : "order",
		title : "序号",
		sortable : false,
		editable : false,
		formatter : function(value, row, index) {
			return index + 1;
		}
	},{
		field: "ID",
		title: "ID",
		editable:false,
		visible:false
		
	},{
		field : "COLLCODE",
		title : "集装箱/散斗代码<font class=required>*</font>",
		editable:{
			title:"集装箱/散斗代码"
			
		}
		
	},{
		field : "INCGOODS",
		title : "内含<font class=required>*</font>",
		editable:{
			title:"内含",
			/*disabled : disabled,*/
			type:"checklist",
			source: [{ value: "头等/公务", text: "头等/公务" }, { value: "普通", text: "普通" }, {value:"转港",text:"转港"},{value:"混装",text:"混装"}]
			
		}
		
	},{
		field : "RENARK",
		title : "备注<font class=required>*</font>",
		editable:{
			title:"备注"
		}
		
	},{
		field : "OPERATORDATE",
		title : "操作时间<font class=required>*</font>",
		formatter:function(value, row, index){
			return "<a href='javascript:void(0)' id='receiverdate123_" + index + "'>" + (value ? value : '请选择') + "</a>";
		}
		
	},{
		field : "RECEIVER",
		title : "行李司机<font class=required>*</font>",
		'class':"select2",
		editable:{
			title:"行李司机",
			type : "select2",
			onblur : "ignore",
			source : receiverData,
			select2:{
				width : "50%",
				language : "zh-CN"
				
			}
		}
		
	},{
		field : "RECEIVETIME",
		title : "接受时间<font class=required>*</font>",
		formatter:function(value, row, index){
			return "<a href='javascript:void(0)' id='receiverdate456_" + index + "'>" + (value ? value : '请选择') + "</a>";
		}
		
	},{
		field : "operate",
		title : "操作",
		sortable : false,
		editable : false,
		visible : editable,
		formatter : function(value, row, index) {
			return "<i class='fa fa-remove' onclick='removeRow(this)'></i>";
		}
	}];
	var createDetailOption = {
			method : 'get',
			dataType : 'json',
			striped : true, // 是否显示行间隔色
			pagination : false, // 是否显示分页（*）
			cache : true,
			sortable : false,
			uniqueId : "ID",
			undefinedText : '', // undefined时显示文本
			columns : createDetailColumns,
			onClickRow:onClickRow,
			onClickCell:onClickCellDou,
			onEditableSave : function(field, row, oldValue, $el) {}
	
	};
	createDetailOption.data = window.detailList;
	$("#createDetailTable").bootstrapTable(createDetailOption);
	function onClickCellDou(field, value, row, $element){
		if('OPERATORDATE' == field || 'RECEIVETIME' == field){
			popupDate(field, value, row, $element);
		}
	};
	function onClickRow(row,tr,field){
		console.log(row);
	}
	
	
	function addEmptyRow(table, num){
			
			for(var i = 0; i < num; i++){
				var length = table.bootstrapTable("getData").length;
				var row = {};
				if(length == 0){
					for(var j = 0; j < createDetailColumns.length; j++){
						row[createDetailColumns[j].field] = null;
					}
				}else{
					var lastRow = table.bootstrapTable("getData")[length - 1];
					for(var j = 0; j < createDetailColumns.length; j++){
						var field = createDetailColumns[j].field;
						row[createDetailColumns[j].field] = null;
					}
				}
				table.bootstrapTable('append', row);
			}
		}
	
	$("#nextRow").click(function(){	
		addEmptyRow($("#createDetailTable"),1);
	});
	
	//给航班号text blur事件
	$("#douxf").blur(function(){
		if($(".flightDate").val()=="" || $(".flightDate").val()==null){
			layer.msg("请填写日期", {icon: 0,time: 2000});
			return false;
		}
		
		$.ajax({
			url:ctx+"/arrival/produce/findInfo",	//*
			type : "POST",
			data :{"flightDate": $(".flightDate").val(),"flightNumber":$(".flightNumber").val()},
			success:function(result){
				if(result!=null){
					$(".aircraftNumber").val(result.AIRCRAFT_NUMBER);
					$(".actstandCode").val(result.ACTSTAND_CODE);
					$(".ata").val(result.ATA);
				}else{
					layer.msg("请你重新操作", {icon: 2,time: 2000});
				}
			}
			
		});
		
		
	});
	
	$("#refreshDetailTable").click(function() {
		$(".select2-selection__rendered").empty();
		$(".select2-selection__rendered").text("请选择");
		
		/*$("input.layui-input").val("");*/
		$("select").find("option[text='请选择']").attr("selected", true);
		$("#createDetailTable").bootstrapTable("removeAll");
		addEmptyRow($("#createDetailTable"),1);
	
	});
	
});

//删除一行
function removeRow(i) {
//	var uniqueid = $(i).parents("tr").data("uniqueid");
//	$("#createDetailTable").bootstrapTable('removeByUniqueId', uniqueid);
	$(i).parents("tr").remove();
}

function preserve(){
	var index = layer.load(2);
	var dataI = $('#createDetailTable').bootstrapTable('getData');
	for(i=0;i<dataI.length;i++){
		var dou = dataI[i];
		var COLLCODEI = dou.COLLCODE;
		var INCGOODSI = dou.INCGOODS;
		var RENARKI = dou.RENARK;
		var OPERATORDATEI = dou.OPERATORDATE;
		var RECEIVERI = dou.RECEIVER;
		var RECEIVETIMEI = dou.RECEIVETIME;
		if(COLLCODEI=="" || INCGOODSI=="" || RENARKI=="" || OPERATORDATEI=="" || RECEIVERI=="" || RECEIVETIMEI==""){
			layer.msg("必须输入项不能为空", {icon: 0,time: 1000},function(){
				layer.close(index);
			});
			return false;
		}
		if(COLLCODEI==null || INCGOODSI==null || RENARKI==null || OPERATORDATEI==null || RECEIVERI==null || RECEIVETIMEI==null){
			layer.msg("必须输入项不能为空", {icon: 0,time: 1000},function(){
				layer.close(index);
			});
			return false;
		}
	}
	
	var data =  JSON.stringify(dataI);
	var	id =  $(".mainId").val();
	var flightDateI =  $(".flightDate").val();
	var flightNumberI =  $(".flightNumber").val();
	var aircraftNumberI = $(".aircraftNumber").val();
	var actstandCodeI =  $(".actstandCode").val();
	var ataI = $(".ata").val();
	var chaliI = $(".chali").val();
	
	if(flightDateI=="" || flightNumberI=="" || chaliI=="" ){
		layer.msg("必须输入项不能为空", {icon: 0,time: 1000},function(){
			layer.close(index);
		});
		return false;
	}
	if(flightDateI==null || flightNumberI==null || chaliI==null ){
		layer.msg("必须输入项不能为空", {icon: 0,time: 1000},function(){
			layer.close(index);
		});
		return false;
	}
	
	
	
	$.ajax({
		url:ctx+"/arrival/produce/delgoodsB",
		type:"POST",
		data:{"id":id},
		success:function(result){
			if(result!=0){
				$.ajax({
					url:ctx+"/arrival/produce/update",
					type : "POST",
					data :{data: data,"id":id,"flightDate":flightDateI,"flightNumber":flightNumberI,"aircraftNumber":aircraftNumberI,"actstandCode":actstandCodeI,"ata":ataI,"chali":chaliI},
					success:function(result){
						if(result!=0){
							layer.msg("成功", {icon: 1,time: 1000},function(){
								layer.close(index);
								window.parent.location.reload();
							});
						}else if(result==0){
							layer.msg("必须输入项不能为空", {icon: 0,time: 2000},function(){
								layer.close(index);
							});
							return false;
						}
					},
					error:function(){
						layer.msg("请你重新输入", {icon: 2,time: 2000});
						return false;
					}
					
				});
			}
		}
	});
	
	
	
}



function initSelect() {
	$('#douxuefeng').select2({
		placeholder : "请选择",
		width : "100%",
		language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
	});
}


function formatRepo(repo) {
	return repo.text;
}

function formatRepoSelection(repo) {
	return repo.text;
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
		dateFmt:"HHmmss",
		onpicked : function(dp) {
			var newValue = dp.cal.getDateStr();
			if (newValue) {
				row[field] = newValue;
			}
		}
	});
}







