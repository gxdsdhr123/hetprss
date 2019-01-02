var layer;
var element;
var form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
	element = layui.element;
	/**
	 * 设置流程属性按钮
	 */
	$("#addFlow").click(function() {
		var oldJobKind = $("#jobKindId").val();
		var oldJobType = $("#jobTypeId").val();
		layer.open({
			type : 1,
			title : "设置流程属性",
			area : [ '700px','380px' ],
			btn : [ "确定", "取消" ],
			yes:function(index, layero){
				if(valid()){
					layer.close(index);
					var jobKind = $("#jobKindId").val();
					var jobType = $("#jobTypeId").val();
					if(oldJobType!=jobType){
						processNode(jobType);
						renderDesigner();
					} else if(oldJobType==jobType&&oldJobKind!=jobKind){
						processNode(jobType);
						renderDesigner();
					}
				}
			},
			btn2:function(index,layero){
				
			},
			content : $("#flowInput")
		});
	});	
	/**
	 * 根据保障类型联动作业类型
	 */
	form.on('select(jobKindId)', function (data) {
		$("#jobTypeId").empty();
		if(data.value){
			queryJobType(data.value);
		}
	});
});

$(document).ready(function() {
	//如果为修改获取原有节点
	if($("#id").val()){
		processNode($("#jobTypeId").val());
	}
	$('#snakerflow').snakerflow({
		basePath : ctxStatic+"/snaker/",
		ctxPath : ctx,
		restore : model,
		formPath : "forms/",
		path:{
			attr:{
				path:{path:"M10 10L100 100",stroke:"#006DC0",fill:"none","stroke-width":3},
				arrow:{path:"M10 10L10 10",stroke:"#006DC0",fill:"#006DC0","stroke-width":3,radius:4}
			}
		},
		tools : {
			save : {
				onclick : function(data) {
					$.ajax({
						type : 'POST',
						url : ctx+"/workflow/process/deployXml",
						data : {
							model:data,
							id:$("#id").val()
						},
						async: false,
						globle:false,
						error : function() {
							layer.msg("保存失败!", {
								icon : 7
							});
							return false;
						},
						success : function(data) {
							if (data == true) {
								layer.msg("保存成功！", {
									icon : 1
								});
								parent.closeFormWin();
								return true;
							} else {
								layer.msg("数据处理错了!！", {
									icon : 7
								});
								return false;
							}
						}
					});
				}
			}
		}
	});
});

/**
 * 根据保障类型获取作业类型
 * @param data
 */
function queryJobType(data){
	$.ajax({
		type : 'POST',
		url : ctx+"/workflow/process/processProfile",
		data :{
			resKind:data
		},
		dataType:"json",
		async : false,
		success : function(data) {
			$("#jobTypeId").append("<option value=''>"+"请选择一项"+"</option>");
			for(var i=0;i<data.length;i++){
				$("#jobTypeId").append("<option value='"+data[i].RESTYPE+"'>"+data[i].TYPENAME+"</option>");
			} 
			form.render('select');
		}
	}); 
}
/**
 * 根据作业类型获取流程节点
 * @param data
 */
function processNode(data){
	$("#node").empty();
	$.ajax({
		type : 'POST',
		url : ctx+"/workflow/process/processNodeById",
		data :{
			jobType:data
		},
		dataType:"json",
		async:false,
		success : function(data) {
			for(var i=0;i<data.length;i++){
				var node = data[i];
				var nodeDiv = $("<div class='node state' nodeId='"+data[i].id+"' type='task'><i class='fa fa-plane'>&nbsp;</i>"+node.label+"("+node.name+")</div>");
				for(var propName in node){
					nodeDiv.attr(propName,node[propName]);
				}
				$("#node").append(nodeDiv);
			}
		}
	}); 
}
/**
 * 重新渲染定义
 */
function renderDesigner(){
	$("#save").unbind("click");
	$('#snakerflow').snakerflow();
}
/**
 * 保存流程定义
 */
function saveDeploy(){
	if(valid()){
		$("#save").trigger("click");
	} else {
		return false;
	}
}
/**
 * 流程图中节点双击查看节点信息
 * @param nodeId
 */
function showNodeInfo(nodeId){
	layer.open({
		type : 2,
		title : "节点详情",
		area : [ '900px', '400px' ],
		content : [ ctx + "/workflow/node/nodeForm?id="+nodeId]
	});
}

function valid(){
	if($("#name").val()==""){
		layer.msg('请输入流程名', {icon: 6}); 
		return false;
	}
	if(!/^[A-Za-z0-9]+$/.test($("#name").val())){ 
		layer.msg('流程名需要数字或者字母', {icon: 6});
		 return false; 
	}
	if($("#displayName").val()==""){
		layer.msg('请输入流程中文名', {icon: 6});
		return false;
	}
	if($("#jobKindId").val()==""){
		layer.msg('请选择一个保障类型', {icon: 6});
		return false;
	}
	if($("#jobTypeId").val()==""){
		layer.msg('请选择一个作业类型', {icon: 6});
		return false;
	}
	return true;
}