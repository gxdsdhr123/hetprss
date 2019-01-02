var layer;
$(document).ready(function() {
	$.ajax({
		type : 'POST',
		async : false,
		url : ctx + "/workflow/node/getBtnEvent",
		dataType : "json",
		success:function(result){
			btnEvent = result;
		}
	});
	$("#addBtnBtn").click(function() {
		insertRow();
	});
	initNodeBtnGrid();
	layui.use([ "layer", "form", "element" ], function() {
		var form = layui.form;
		layer = layui.layer;
		form.on('select(jobKind)', function(data) {
			$.ajax({
				type : 'post',
				url : ctx + "/workflow/node/getTypeByKind",
				data : {
					'jobKind':data.value
				},
				dataType : "json",
				success:function(result){
					$("#jobType").empty();
					$("#jobType").append("<option value=''>请选择</option>");
					for(var i=0;i<result.length;i++){
						$("#jobType").append("<option value='"+result[i]["RESTYPE"]+"'>"+result[i]["TYPENAME"]+"</option>");
					}
					form.render('select');
				}
			});
		});
	});
});
$.fn.editable.defaults.mode = 'inline';
/**
 * 初始化表格
 */
function initNodeBtnGrid() {
	$("#nodeBtnGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "rowId",
		url : ctx + "/workflow/node/getNodeBtnData",
		method : "get",
		pagination : false,
		showRefresh : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getGridColumns(),
		queryParams : function() {
			var param = {
				id : $("#id").val()
			};
			return param;
		}
	});
}
/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getGridColumns() {
	var columns = [ {
		field : "rowId",
		title : "序号",
		width : '40px',
		align : 'center',
		formatter : function(value, row, index) {
			row["rowId"] = index;
			return index + 1;
		}
	},{
		field : "showOrder",
		title : '排序',
		width:"200px",
		editable : {
			type : 'text'
		}
	}, {
		field : "name",
		title : '名称',
		width:"200px",
		editable : {
			type : 'text'
		}
	}, {
		field : "label",
		title : '中文名',
		width:"200px",
		editable : {
			type : 'text'
		}
	}, {
		field : "eventId",
		title : '事件',
		width:"200px",
		editable:{
			showbuttons: false,
			type:"select",
			title:"事件",
			source:btnEvent,
		}
	}, {
		field : "variable",
		title : '拓展参数',
		align : 'center',
		width:"100px",
		formatter : function(value,row,index) {
			return '<a href="javascript:void(0)" class="variableBtn btn-link" data-params='+(value?value:'""')+' onclick="setVariable(this)">设置</a>';
		}
	}, {
		field : "limitParm",
		title : '限制参数',
		align : 'center',
		width:"100px",
		formatter : function(value,row,index) {
			return '<a href="javascript:void(0)" class="limitBtn" data-params='+(value?value:'""')+' onclick="setLimitParm(this)">设置</a>';
		}
	}, {
		field : "btn",
		title : '操作',
		align : 'center',
		width:"100px",
		events : operateEvents,
		formatter : function() {
			return '<a href="javascript:void(0)" class="delBtn btn-link" >删除</a>';
		}
	} ];
	return columns;
}

function insertRow() {
	var index = $('#nodeBtnGrid').bootstrapTable('getData').length;
	var emptyRow = {};
	emptyRow["rowId"] = index;
	emptyRow["showOrder"] = "";
	emptyRow["name"] = "";
	emptyRow["label"] = "";
	$('#nodeBtnGrid').bootstrapTable('insertRow', {
		index : index,
		row : emptyRow
	});
}

/**
 * 注册按钮的点击事件
 */
window.operateEvents = {
	'click .delBtn' : function(e, value, row, index) {
		$('#nodeBtnGrid').bootstrapTable('removeByUniqueId', row.rowId);
	}
};

function saveNodeGrid() {
	var name = $("#name").val();
	var label = $("#label").val();
	var jobKind = $("#jobKind").val();
	var jobType = $("#jobType").val();
	var icon = $("#icon").val();
	if(!name||!$.trim(name)){
		layer.msg("请输入节点名称！",{icon:7});
		return false;
	}
	if(!label||!$.trim(label)){
		layer.msg("请输入节点中文名称！",{icon:7});
		return false;
	}
	if(!jobKind||!$.trim(jobKind)){
		layer.msg("请选择保障类型！",{icon:7});
		return false;
	}
	if(!jobType||!$.trim(jobType)){
		layer.msg("请选择作业类型！",{icon:7});
		return false;
	}
	if(!icon||!$.trim(icon)){
		layer.msg("请设置节点简称！",{icon:7});
		return false;
	}
	
	var reg = /^[0-9\+\-]*$/;
	var notifyTm = $("#notifyTm").val();
	if(notifyTm&&!reg.test(notifyTm)){
		layer.tips("只能输入正整数或负数,如:+5或-5","#notifyTm",{
			tips: [2, '#05163b']
		});
		return false;
	}
	var alarmValid = true;
	for(var i=1;i<=5;i++){
		var id = "#alarmTm"+i;
		var alarmTmVal = $(id).val();
		if(alarmTmVal&&!reg.test(alarmTmVal)){
			layer.tips("只能输入正整数或负数,如:+5或-5",id,{
				tips: [2, '#05163b']
			});
			alarmValid = false;
			break;
		}
	}
	if(!alarmValid){
		return false;
	}
	var btns = $('#nodeBtnGrid').bootstrapTable('getData');
	for(var i=0;i<btns.length;i++){
		var btnParmas = $('#nodeBtnGrid tbody tr:eq('+i+') td:eq(5) a').data("params");
		if(!btnParmas||btnParmas==null||btnParmas==undefined||btnParmas.length==0){
			btns[i]["variable"] = "";
		}else{
			btns[i]["variable"] = btnParmas;
		}
		//限制参数
		var limitParm = $('#nodeBtnGrid tbody tr:eq('+i+') td:eq(6) a').data("params");
		if(!limitParm||limitParm==null||limitParm==undefined||limitParm.length==0){
			btns[i]["limitParm"] = "";
		}else{
			var limitParmData = [];
			for(var j=0;j<limitParm.length;j++){
				var data = limitParm[j];
				limitParmData.push({
					processId:data.processId,
					nodeId:data.nodeId
				});
			}
			btns[i]["limitParm"] = limitParmData;
		}
	}
	$("#btns").val(JSON.stringify(btns));

	$('#nodeForm').ajaxSubmit({
		async:false,
		beforeSubmit:function(){
			loading = layer.load(2, {
				 shade: [0.1,'#000'] //0.1透明度
			});
		},
		error:function(){
			layer.close(loading);
			layer.msg('保存失败！', {
				icon : 2
			});
		},
		success:function(data){
			layer.close(loading);
			if(data=="success"){
				layer.msg('保存成功！', {
					icon : 1,
					time : 600
				});
				parent.saveSuccess();
			}else{
				layer.msg('保存失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	})
}

var selectedRowIndex = -1;
function setVariable(btnVariable){
	selectedRowIndex = $(btnVariable).parent().parent().index();
	var variableWin = layer.open({
		closeBtn : false,
		type : 2,
		title : "拓展参数",
		area : [ '90%', '80%' ],
		btn : [ "添加行", "确定", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.insertRow();
		},
		btn2 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var gridData = $('#nodeBtnGrid').bootstrapTable('getData');
			var reData = iframeWin.reData();
			if(reData&&reData.length>0){
				gridData[selectedRowIndex]["variable"] = reData;
				$(btnVariable).parent().html('<a href="javascript:void(0)" class="variableBtn btn-link" data-params='+reData+' onclick="setVariable(this)">设置</a>');
			} else {
				gridData[selectedRowIndex]["variable"] = "";
				$(btnVariable).parent().html('<a href="javascript:void(0)" class="variableBtn btn-link" onclick="setVariable(this)">设置</a>');
			}
			layer.close(variableWin);
			return false;
		},
		content : [ ctx + "/workflow/node/variable?time="+(Date.parse(new Date()))]
	});
}
/**
 * 设置限制参数
 * @param value
 */
function setLimitParm(curr){
	var currRowIndex = $(curr).parent().parent().index();
	layer.open({
		closeBtn : false,
		type : 2,
		title : "限制参数",
		area : [ '800px', '400px' ],
		btn : [ "新增参数", "确定", "取消" ],
		btn1 : function(index, layero) {
			addLimitParm(index, layero);
		},
		btn2 : function(index, layero) {
			var win = window[layero.find('iframe')[0]['name']];
			var limitData = win.getLimitParmData();
			$(curr).parent().html('<a href="javascript:void(0)" class="limitBtn" data-params='+limitData+' onclick="setLimitParm(this)">设置</a>');
			var gridData = $('#nodeBtnGrid').bootstrapTable('getData');
			if(limitData&&limitData.length>0){
				gridData[currRowIndex]["limitParm"] = limitData;
			} else {
				gridData[currRowIndex]["limitParm"] = "";
			}
		},
		content : [ ctx + "/workflow/node/limitParm?time="+(Date.parse(new Date()))],
		success: function(layero, index){
			var win = window[layero.find('iframe')[0]['name']];
			var gridData = $(curr).data("params");
			win.initGrid(gridData?gridData:[]);
		}
	});
}
function addLimitParm(pIndex,pLayero){
	layer.open({
		closeBtn : false,
		type : 2,
		title : "限制参数",
		area : [ '800px', '400px' ],
		btn : [ "确定", "取消" ],
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var limitDataStr = iframeWin.getFormData();
			var limitData = JSON.parse(limitDataStr);
			if(!limitData.processId){
				layer.msg("请选择流程模板！",{icon:7});
				return false;
			}
			if(!limitData.nodeId){
				layer.msg("请选择流程节点！",{icon:7});
				return false;
			}
			var parentWin = window[pLayero.find('iframe')[0]['name']];
			parentWin.insertRow(limitData);
			layer.close(index);
		},
		content : [ ctx + "/workflow/node/limitParmForm"]
	});
}