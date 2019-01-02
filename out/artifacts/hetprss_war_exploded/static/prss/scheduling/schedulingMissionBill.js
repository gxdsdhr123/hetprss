var layer;// 初始化layer模块
var baseTable;// 基础表格
var rowIndex = 0;
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	
	//加载数据表
	initGrid();
});

function initGrid() {
	$("#baseTable").bootstrapTable({
		url : ctx+"/scheduling/list/getFltMailInfo",
		method : "get", 
		dataType : "json",
		toolbar : $("#tool-box"),
		search : false,
		editable:true,
		pagination : false,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		paginationPreText: "上一页",  
		paginationNextText: "下一页",
		height:$(window).height()-10,
		queryParams : function(params) {
			var param = {
				'inFltId' : $("#inFltId").val(),
				'outFltId' : $("#outFltId").val(),
				'inFltNum' : $("#inFltNum").val(),
				'inFltNum2' : $("#inFltNum2").val(),
				'outFltNum' : $("#outFltNum").val(),
				'outFltNum2' : $("#outFltNum2").val(),
				'inFltDate' : $("#inFlightDate").val(),
				'outFltDate' : $("#outFlightDate").val()
			};
			return param;
		},
		columns : [ {
			field : "order",
			title : "序号",
			width : '40px',
			align : 'center',
			formatter(value, row, index, field){
				return index+1;
			},
		}, {
			field : "id",
			title : "id",
			visible : false
		},{
			field : "aptType",
			title : "本站/过站",
			width : "80px",
			editable : {
				type : "select2",
				source : [{id:"0",text:"本站"},{id:"1",text:"过站"}]
			}
		}, {
			field : "mailPackageType",
			title : "货邮行类型",
			width : "80px",
			editable : {
				type : "select2",
				source : [{id:"C",text:"C"},{id:"BY",text:"BY"},{id:"M",text:"M"},{id:"X",text:"X"},{id:"E",text:"E"}]
			}
		}, {
			field : "arrivalApt",
			title : "到达站",
			width : "80px",
			editable : {
				type:'text'
			}
		},{
			field : "packageWeight",
			title : '行李重量',
			editable:false,
			visible:false
		},{
			field : "mailWeight",
			title : '货邮重量',
			editable:false,
			visible:false
		},{
			field : "weight",
			title : "重量",
			width : "80px",
			editable : {
				type:'text'
			},
			formatter : function(value, row, index) {
				var val = "";
				//行李重量
				var packageWeight = row["packageWeight"];
				//货邮重量
				var mailWeight = row["mailWeight"];
				if(packageWeight!=null && packageWeight !="" && packageWeight !="0"){
					val = packageWeight;
				}else{
					val = mailWeight;
				}
				return val;
			}
		},{
			field : "packageNum",
			title : "件数",
			width : "80px",
			editable : {
				type:'text'
			}
		},{
			field : "mailCabin",
			title : "舱位",
			width : "80px",
			editable : {
				type:'text'
			}
		},{
			field : "operate",
			title : "操作",
			formatter : function(value, row, index) {
				return '<button type="button" value="'+row.id+";"+row.order+'" style="height:auto;line-height:1.5" class="btn btn-md btn-link" onclick="deleteFun(this)">删除</button>';
			}
		}]
	});
}

/**
 * 增加行
 */
function addRow() {
	var currDataArr = $("#baseTable").bootstrapTable("getData");
	if(currDataArr.length>0){
		rowIndex += currDataArr.length;
	}
	$("#baseTable").bootstrapTable("insertRow", {
		index : rowIndex++,
		row :{"order" : rowIndex,"aptType":"","mailPackageType":"","arrivalApt":"","packageWeight":"","mailWeight":"","weight":"","packageNum":"","mailCabin":""}
	});
}

/**
 * 删除货航邮、行李信息
 */
function deleteFun(obj){
	var val = obj.value;
	var tmpArr = val.split(";");
	var id = tmpArr[0];
	var index = tmpArr[1];
	if(id!=null&&id!=""&&id!='undefined'){
		//从列表移除数据行
	    $("#baseTable").bootstrapTable('remove', {
	        field: 'order',
	        values: [parseInt(index)]
	    });
		var loading = layer.load(2, {shade : [ 0.3, '#000' ]});
		$.ajax({
	        type: "POST",
	        url: ctx + '/scheduling/list/delFltMailInfo',
	        data: {
	        	"id":id
	        },
	        success: function(data){
	        	layer.close(loading);
	        	if(data=="success"){
	        		layer.msg("删除成功！", {icon: 1,time:2000});
	        		//从列表移除数据行
	        	    $("#baseTable").bootstrapTable('remove', {
	        	        field: 'order',
	        	        values: [parseInt(index)]
	        	    });
	        		clickRowId = null;
	        	}else if(data=="err"){
	        		layer.alert("删除异常！", {icon: 2,time:2000});
	        	}else if(data=="non"){
					layer.alert("没有数据被删除！", {icon: 2,time:2000});
				}
	        }
	    });
	}else{
		//从列表移除数据行
	    $("#baseTable").bootstrapTable('remove', {
	        field: 'order',
	        values: [parseInt(index)]
	    });
	}
}

/**
 * 新增、更新货航邮、行李信息
 */
function saveFun() {
	var allData = $("#baseTable").bootstrapTable("getData");
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/scheduling/list/saveFltMailInfo",
		data : {
			'data' : JSON.stringify(allData),
			'inFltId' : $("#inFltId").val(),
			'outFltId' : $("#outFltId").val(),
			'inFltNum' : $("#inFltNum").val(),
			'inFltNum2' : $("#inFltNum2").val(),
			'outFltNum' : $("#outFltNum").val(),
			'outFltNum2' : $("#outFltNum2").val(),
			'inFltDate' : $("#inFlightDate").val(),
			'outFltDate' : $("#outFlightDate").val(),
			'supervisionUser' : $("#supervisionUser").val()
		},
		success : function(data) {
			layer.close(loading);
			if(data=="success"){
				layer.msg("货航邮、行李信息保存、更新成功！", {icon: 1,time:2000});
				$("#baseTable").bootstrapTable("refresh");
			}else if(data=="err"){
				layer.alert("货航邮、行李信息保存、更新异常！", {icon: 2,time:2000});
			}
		}
	});
}

/**
 * 导入解析货航邮、行李信息
 */
function importFun(importData) {
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx+"/scheduling/list/importFltMailInfo",
		data : {
			'inFltId' : $("#inFltId").val(),
			'outFltId' : $("#outFltId").val(),
			'inFltNum' : $("#inFltNum").val(),
			'inFltNum2' : $("#inFltNum2").val(),
			'outFltNum' : $("#outFltNum").val(),
			'outFltNum2' : $("#outFltNum2").val(),
			'inFltDate' : $("#inFlightDate").val(),
			'outFltDate' : $("#outFlightDate").val(),
			'fltMail' : importData.fltMail,
			'fltPackage' : importData.fltPackage,
			'supervisionUser' : $("#supervisionUser").val()
		},
		success : function(data) {
			layer.close(loading);
			if(data.result=="success"){
				layer.msg("货航邮、行李信息导入解析成功！", {icon: 1,time:2000});
				$("#baseTable").bootstrapTable("refresh");
			}else if(data.result=="fail"){
				layer.alert(data.failInfo, {icon: 2,time:20000},function(){
					$("#baseTable").bootstrapTable("refresh");
				});
			}else if(data.result=="err"){
				layer.alert("货航邮、行李信息导入解析异常！", {icon: 2,time:2000});
			}else if(data.result=="non"){
				layer.alert("无进港航班信息或没有货航邮、行李信息需要导入解析！", {icon: 2,time:4000});
			}
		}
	});
}

/**
 * 打印任务指派单
 */
function print(){
	$("#exportForm").attr("action",ctx + "/scheduling/list/exportFltMailInfo");
	$("#exportForm").submit();
}
