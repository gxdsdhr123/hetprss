var layer;// 初始化layer模块
var form;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
});

/**
 * 保存配置
 */
function doSave(){
	$("#tableForm").ajaxSubmit({
		async : false,
		beforeSubmit : function() {
			loading = parent.layer.load(2, {
				shade : [ 0.3, '#000' ]// 0.1透明度
			});
		},
		success : function(data) {
			parent.layer.close(loading);
			if(data=='success'){
				layer.msg("保存成功！", {icon : 1,time : 2000},function(){
					parent.refreshBaseTable();
				});
			}else{
				layer.alert("保存失败！", {icon : 2,time : 2000});
			}
		}
	});
}

/**
 * 获取作业类型
 */
function getRestype(){
	$("#restype").empty();
	$("#processId").empty();
	$("#nodeId").empty();
	$.ajax({
		type : 'POST',
		url : ctx+"/rule/gisConfig/getRestype",
		data :{
			reskind : $("#reskind").val()
		},
		dataType:"json",
		async : false,
		success : function(data) {
			$("#processId").append("<option value=''>"+"请选择流程"+"</option>");
			$("#nodeId").append("<option value=''>"+"请选择节点"+"</option>");
			$("#restype").append("<option value=''>"+"请选择作业类型"+"</option>");
			for(var i=0;i<data.length;i++){
				$("#restype").append("<option value='"+data[i].id+"'>"+data[i].text+"</option>");
			} 
		}
	}); 
}

/**
 * 获取流程
 */
function getProcess(){
	$("#processId").empty();
	$("#nodeId").empty();
	$.ajax({
		type : 'POST',
		url : ctx+"/rule/gisConfig/getProcess",
		data :{
			reskind : $("#reskind").val(),
			restype : $("#restype").val()
		},
		dataType:"json",
		async : false,
		success : function(data) {
			$("#processId").append("<option value=''>"+"请选择流程"+"</option>");
			$("#nodeId").append("<option value=''>"+"请选择节点"+"</option>");
			for(var i=0;i<data.length;i++){
				$("#processId").append("<option value='"+data[i].id+"'>"+data[i].text+"</option>");
			} 
		}
	}); 
}

/**
 * 获取节点
 */
function getNode(){
	$("#nodeId").empty();
	$.ajax({
		type : 'POST',
		url : ctx+"/rule/gisConfig/getNode",
		data :{
			processId : $("#processId").val(),
			reskind : $("#reskind").val(),
			restype : $("#restype").val()
		},
		dataType:"json",
		async : false,
		success : function(data) {
			$("#nodeId").append("<option value=''>"+"请选择节点"+"</option>");
			for(var i=0;i<data.length;i++){
				$("#nodeId").append("<option value='"+data[i].id+"'>"+data[i].text+"</option>");
			} 
		}
	}); 
}

/**
 * 获取任务位置
 */
function getTargetArea(){
	$("#targetArea").empty();
	$.ajax({
		type : 'POST',
		url : ctx+"/rule/gisConfig/getTargetArea",
		data :{
			targetType : $("#targetType").val()
		},
		dataType:"json",
		async : false,
		success : function(data) {
			$("#targetArea").append("<option value=''>"+"请选择任务位置"+"</option>");
			for(var i=0;i<data.length;i++){
				$("#targetArea").append("<option value='"+data[i].id+"'>"+data[i].text+"</option>");
			} 
		}
	}); 
}

/**
 * 电子围栏复选框
 */
function openCheck() {
	//选中的电子围栏id字符串  id,id1,id2...
	var areaCodesVal = $("#areaCodesVal").val();
	layer.open({
		type : 2,
		title : '电子围栏复选',
		content : ctx + "/rule/gisConfig/getGisRailInfo?areaCodesVal="+areaCodesVal,
		btn : [ "确定", "清空已选", "取消" ],
		area:['550px','380px'],
		scrollbar:false,
		move:false,
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var data = iframeWin.getChooseData();
			$("#areaName").val(data.chooseTitle);
			$("#areaCodesVal").val(data.chooseValue);
			layer.close(index);
		},
		btn2 : function(index, layero) {// 重置表单及筛选条件
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.clearSelect();
			return false;
		},
		btn3 : function(index) {// 关闭前重置表单及筛选条件
			layer.close(index);
		}
	})
}