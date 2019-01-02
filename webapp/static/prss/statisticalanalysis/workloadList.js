var layer;// 初始化layer模块
var baseTable;// 基础表格
var iframe;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	initSelect();
	
	//查询功能
	$(".search").click(function() {
		if(searchOption()){
			var a = $('#baseTable').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 10
				}
			});
		}else{
			layer.msg("日期不能为空",{icon: 2});
		}	
	});
	$("#searchText").keydown(function(e){
		if (e.keyCode == 13) {
			if(searchOption()){
				var a = $('#baseTable').bootstrapTable('refresh', {
					query : {
						pageNumber: 1,    
		                pageSize: 10
					}
				});
			}else{
				layer.msg("日期不能为空",{icon: 2});
			}
	    }
		
	});
	//打印功能
	$(".print").click(function() {
		$("#listForm").submit();
	});

	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/workload/statistics/dataList", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    searchOnEnterKey : false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10,15,20,50,100],
	    queryParamsType:'',
	    queryParams: function (params) {
	    	return getParams(params);
	    },
	    onLoadSuccess: function (data) {
	    	 mergeCells(data.rows, "OPERATOR_NAME", 1, $('#baseTable'),
	    			 ["FLIGHT_DATE","KINDNAME","BZ_TIME_COUNT","BZ_TIME_TOTAL"],"TYPENAME",["BZ_TIME_NUM"]);//行合并
	    },
	    columns: [
	        {field: "FLIGHT_DATE", title: "日期",align:'left',halign:'center',editable:false},
			{field: "KINDNAME", title: "岗位",align:'left',halign:'center',editable:false},
			{field: "OPERATOR_NAME", title: "姓名",align:'left',halign:'center',editable:false},
			{field: "TYPENAME", title: "作业类型",align:'left',halign:'center',editable:false},
			{field: "BZ_TIME_NUM", title: "保障次数",align:'left',halign:'center',editable:false},
			{field: "BZ_TIME", title: "保障时长",align:'left',halign:'center',editable:false},
			{field: "FLIGHT_NUMBER", title: "航班详情",align:'left',halign:'center',editable:false},
			{field: "IN_OUT_FLAG", title: "航班性质",align:'left',halign:'center',editable:false},
			{field: "ACTSTAND_KIND", title: "机位",align:'left',halign:'center',editable:false},
			{field: "BZ_TIME_COUNT", title: "保障总次数",align:'left',halign:'center',editable:false},
			{field: "BZ_TIME_TOTAL", title: "保障总时长",align:'left',halign:'center',editable:false}
	    ]
	};

	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});

/**
* 合并行
* @param data 原始数据（在服务端完成排序）
* @param fieldName 合并属性名称数组
* @param colspan 列数
* @param target 目标表格对象
*/
function mergeCells(data, fieldName, colspan, target,fieldNameArr,secondFieldName,secondFieldNameArr) {
	if (data.length > 0) {
		var numArr = [];
		var value = data[0][fieldName];
		var flightDateValue = data[0]["FLIGHT_DATE"];
		var num = 0;
		for (var i = 0; i < data.length; i++) {
			if (value != data[i][fieldName]) {
				numArr.push(num);
				value = data[i][fieldName];
				flightDateValue = data[i]["FLIGHT_DATE"];
				num = 1;
				continue;
			} else if (flightDateValue != data[i]["FLIGHT_DATE"]) {
				numArr.push(num);
				value = data[i][fieldName];
				flightDateValue = data[i]["FLIGHT_DATE"];
				num = 1;
				continue;
			}
			num++;
		}
		numArr.push(num);
		var merIndex = 0;
		for (var i = 0; i < numArr.length; i++) {
			$(target).bootstrapTable('mergeCells', { index: merIndex, field: fieldName, colspan: colspan, rowspan: numArr[i] });
			for (var v in fieldNameArr) {
				if(!(data[merIndex]['FLAG']==1 && (fieldNameArr[v]=='BZ_TIME_COUNT' || fieldNameArr[v]=='BZ_TIME_TOTAL')))
					$(target).bootstrapTable('mergeCells', { index: merIndex, field: fieldNameArr[v], colspan: colspan, rowspan: numArr[i] });
			}
			
			var secondNumArr = [];
			var secondValue = data[merIndex][secondFieldName];
			var secondNum = 0;
			for (var j = merIndex; j < merIndex+numArr[i]; j++) {
				if (secondValue != data[j][secondFieldName]) {
					secondNumArr.push(secondNum);
					secondValue = data[j][secondFieldName];
					secondNum = 1;
					continue;
				}
				secondNum++;
			}
			secondNumArr.push(secondNum);
			var secondMerIndex = merIndex;
			for (var k = 0; k < secondNumArr.length; k++) {
				$(target).bootstrapTable('mergeCells', { index: secondMerIndex, field: secondFieldName, colspan: colspan, rowspan: secondNumArr[k]});
				for (var v in secondFieldNameArr) {
					$(target).bootstrapTable('mergeCells', { index: secondMerIndex, field: secondFieldNameArr[v], colspan: colspan, rowspan: secondNumArr[k] });
				}
				secondMerIndex += secondNumArr[k];
			}
			merIndex += numArr[i];
		}
	}
}


/**
 * 合并列
 * @param data 原始数据（在服务端完成排序）
 * @param fieldName 合并属性数组
 * @param target 目标表格对象
 */
function mergeColspan(data, fieldNameArr, target) {
	if (data.length == 0) {
		alert("不能传入空数据");
		return;
	}
	if (fieldNameArr.length == 0) {
		alert("请传入属性值");
		return;
	}
	var num = -1;
	var index = 0;
	for (var i = 0; i < data.length; i++) {
		num++;
		for (var v in fieldNameArr) {
			index = 1;
			if (data[i][fieldNameArr[v]] != data[i][fieldNameArr[0]]) {
				index = 0;
				break;
			}
		}
		console.info(index)
		if (index == 0) {
			continue;
		}
		$(target).bootstrapTable('mergeCells', { index: num, field: fieldNameArr[0], colspan: fieldNameArr.length, rowspan: 1 });
	}
}
function searchOption(){
	var dateStart=$("#dateStart").val();
	var dateEnd=$("#dateEnd").val();
	if(dateStart==""||dateEnd==""){
		return false;
	}else{
		return true;
	}
}

//获取搜索数据
function getParams(params){
    return {
    	dateStart :encodeURIComponent($("#dateStart").val()),
    	dateEnd: encodeURIComponent($("#dateEnd").val()),
    	searchText:encodeURIComponent($("#searchText").val()),
    	name:encodeURIComponent($("#name").val()),
    	ifJobKind:encodeURIComponent($("#ifJobKind").val()),
    	jobKind:encodeURIComponent($("#jobKind").val()),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}

function initSelect() {
	$('#alnCode').select2({
//		placeholder : "请选择",
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
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}


function changeJobKind(obj){
	var jobKindName = $(obj).find("option:selected").text();
	$("input[name=jobKindName]").val(jobKindName);
}

