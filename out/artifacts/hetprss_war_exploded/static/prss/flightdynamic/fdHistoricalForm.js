var baseTable;
var index = parent.layer.getFrameIndex(window.name);
$(function() {
	var layer;
	baseTable = $("#baseTable");
	layui.use([ 'form', 'layer' ], function() {// 调用layui表单及弹出层组件
		layer = layui.layer;
	});
	// 基本表格选项
	var tableOptions = {
		url : ctx+"/fdHistorical/getHisFltInfo",
		method : "get", 
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : false, // 是否显示全选
		search : false, // 是否开启搜索功能
		editable:false,//开启编辑模式
		height : $("#baseTables").height(),// 表格适应页面高度
		columns : createDetailColumns,
		queryParams : {
			inFltId : $("#inFltId").val(),
			outFltId : $("#outFltId").val()
		}
	};
	$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
});

// 定义表格列头
var createDetailColumns = [ {
	field : "order",
	title : "序号",
	sortable : false,
	editable : false,
	formatter : function(value, row, index) {
		return index + 1;
	}
}, {
	field : "inFltNo",
	title : "进港航班号"
}, {
	field : "outFltNo",
	title : "出港航班号"
}, {
	field : "departApt",
	title : "起场"
}, {
	field : "arrivalApt",
	title : "落场"
}, {
	field : "aircraftNo",
	title : "机号"
}, {
	field : "actType",
	title : "机型"
},  {
	field : "std",
	title : "计起"
},  {
	field : "etd",
	title : "预起"
},  {
	field : "atd",
	title : "实起"
},  {
	field : "sta",
	title : "计落"
},  {
	field : "eta",
	title : "预落"
},  {
	field : "ata",
	title : "实落"
},  {
	field : "shareFltNo",
	title : "共享航班"
},  {
	field : "aln3code",
	title : "航空公司"
}, {
	field : "status",
	title : "状态"
}, {
	field : "delayReason",
	title : "延误原因"
}, {
	field : "releaseReason",
	title : "放行原因"
}, {
	field : "remark",
	title : "备注"
}];
