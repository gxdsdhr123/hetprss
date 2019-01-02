var layer;
var showTree;
var showTreeDef;
var editTree;
var iframe;
$(function(){
	layui.use([ "layer","form", "element" ], function() {
		layer = layui.layer;
		var form = layui.form;
	});
	$("html,body").height("100%");
	$(".ztree").each(function(){
		new PerfectScrollbar(this);
	})
	var tableOptions = {
			url : ctx + "/scheduling/gantt/getResGanttSet", // 请求后台的URL（*）
			method : "get", // 请求方式（*）
			dataType : "json", // 返回结果格式
			striped : true, // 是否显示行间隔色
			pagination : false, // 是否显示分页（*）
			cache : false, // 是否启用缓存
			undefinedText : '', // undefined时显示文本
			//checkboxHeader : false, // 是否显示全选
			toolbar : $("#tool-box"), // 指定工具栏dom
			search : true,
			//searchOnEnterKey : true,
			height : $("body").height(),
			columns : [
				{
					field : 'order',
					title : '序号',
					sortable : false,
					width : 44,
					formatter : function(value, row, index) {
						return index + 1;
					}
				},{
					field : 'name',
					title : '类型',
					sortable : true,
				},{
					field : 'number',
					title : '车号',
					sortable : true,
				},{
					field : 'status',
					title : '设备状态',
					sortable : true,
				},{
					field : 'area',
					title : '机位区域',
					sortable : true,
				},{
					field : 'areaDef',
					title : '默认机位区域',
					sortable : true,
				},{
					field : 'actype',
					title : '机型',
					sortable : true,
				},{
					field : 'stand',
					title : '机位',
					align : 'center',
					width : 100,
					formatter : function(value, row, index) {
						return '<button type="button" data-id="'+row.areaCode+'" data-num="'+row.number+'" style="height:auto;line-height:1.5" class="btn btn-sm btn-link" onclick="showArea(this)">查看</button>';
					}
				},{
					field : 'operate',
					title : '操作',
					align : 'center',
					width : 100,
					formatter : function(value, row, index) {
						var a = '<button type="button" data-id="'+row.areaCode+'" data-actid="'+row.actypeCode+'" data-num="'+row.number+'" style="height:auto;line-height:1.5" class="btn btn-sm btn-link" onclick="editArea(this)">修改</button>';
						if(row.actype){
							a += ' <button type="button" data-id="'+row.areaDefCode+'" data-num="'+row.number+'" style="height:auto;line-height:1.5" class="btn btn-sm btn-link" onclick="editDefArea(this)">修改默认</button>';
						}
						return a;
					}
				}
			]
	}
	$("#settingTable").bootstrapTable(tableOptions);
	$("#areas").change(function(){
		$.fn.zTree.destroy(editTree);
		if($(this).val()!="!"){
			$.ajax({
				type:'post',
				url:ctx+"/scheduling/gantt/getJWById",
				data:{
					id:$(this).val()
				},
				dataType:'json',
				success:function(tree){
					editTree = $.fn.zTree.init($("#editTree"), editSetting, tree);
					editTree.expandAll(true);
					editTree.checkAllNodes(true);
				}
			});
		}
	})
})
var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "pId",
			rootPId : '0'
		}
	},
	view : {
		showLine : false,
		selectedMulti : false
	}
};
var editSetting = {
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : '0'
			}
		},
		view : {
			showLine : false,
			selectedMulti : false
		},
		check : {
			enable : true
		}
	};
function showArea(btn){
	var id = $(btn).data("id");
	if(!id || id=="" || typeof(id)=="undefined"){
		return false;
	}
	var num = $(btn).data("num");
	iframe = layer.open({
		type:1,
		title:"机位",
		area:["600px","350px"],
		content:$("#showArea"),
		success:function(){
			$.fn.zTree.destroy(showTree);
			$.fn.zTree.destroy(showTreeDef);
			$.ajax({
				type:'post',
				url:ctx+"/scheduling/gantt/getAreaByNum",
				data:{
					num:num
				},
				dataType:'json',
				success:function(stree){
					showTree = $.fn.zTree.init($("#showTree"), setting, stree);
					showTree.expandAll(true);
				}
			})
			$.ajax({
				type:'post',
				url:ctx+"/scheduling/gantt/getDefAreaByNum",
				data:{
					num:num
				},
				dataType:'json',
				success:function(stree){
					showTreeDef = $.fn.zTree.init($("#showTreeDef"), setting, stree);
					showTreeDef.expandAll(true);
				}
			})
		}
	});
}
function editArea(btn){
	$('#actypeSel').show();
	$("#areas").val("!");
	$("#actypes").val("!");
	$.fn.zTree.destroy(editTree);
	var id = $(btn).data("id");
	var actid = $(btn).data("actid");
	var num = $(btn).data("num");
	iframe = layer.open({
		type:1,
		title:"修改",
		area:["600px","400px"],
		content:$("#editArea"),
		btn : ["保存","取消"],
		success:function(){
			if(id && num){
				$.ajax({
					type:'post',
					url:ctx+"/scheduling/gantt/getAreaById",
					data:{
						id:id,
						num:num
					},
					dataType:'json',
					success:function(res){
						if(res.jw){
							$("#areas").val(res.jw);
						}else{
							$("#areas").val("!");
						}
						if(res.jx){
							$("#actypes").val(res.jx);
						}else{
							$("#actypes").val("!");
						}
						editTree = $.fn.zTree.init($("#editTree"), editSetting, res.atree);
						editTree.expandAll(true);
						var chosen = res.stands.split(",");
						for(var i=0;i<chosen.length;i++){
							var node = editTree.getNodeByParam("id", chosen[i], null);
							editTree.checkNode(node, true, true);
						}
					}
				})
			}
			if(actid){
				$("#actypes").val(actid);
			}
		},
		yes:function(){
			var data = {};
			data.num = num;
			if($("#areas").val() == "!"){
				layer.msg('请选择保障机位范围',{icon:7,time:1000});
				return false;
			}
			if($("#actypes").val() == "!"){
				layer.msg('请选择保障机型',{icon:7,time:1000});
				return false;
			}
			if($("#areas").val() != "!"){
				data.area = $("#areas").val();
				var nodes = editTree.getCheckedNodes(true);
				var nodeList = [];
				for(var i=0;i<nodes.length;i++){
					if(nodes[i].level == 1){
						nodeList.push(nodes[i].name);
					}
				}
				if(nodeList.length == 0){
					layer.msg('请选择保障机位范围',{icon:7,time:1000});
					return false;
				}
				data.nodeList = nodeList;
			}
			if($("#actypes").val() != "!"){
				data.actype = $("#actypes").val()
			}
			var loading = layer.load(2);
			$.ajax({
				type:'post',
				url:ctx+"/scheduling/gantt/saveResSet",
				data:{
					data:JSON.stringify(data)
				},
				error:function(){
					layer.msg("请求发送失败",{icon:2,time:1000},function(){
						closeLayer();
					});
					layer.close(loading);
				},
				success:function(msg){
					layer.close(loading);
					if(msg == "success"){
						layer.msg("保存成功！",{icon:1,time:1000},function(){
							closeLayer();
						});
					}else{
						layer.msg("保存失败",{icon:2,time:1000});
						console.error(msg);
					}
				}
			});
		}
	});
}
function editDefArea(btn){
	$('#actypeSel').hide();
	$("#areas").val("!");
	$.fn.zTree.destroy(editTree);
	var id = $(btn).data("id");
	var num = $(btn).data("num");
	iframe = layer.open({
		type:1,
		title:"修改默认",
		area:["600px","400px"],
		content:$("#editArea"),
		btn : ["保存","取消"],
		success:function(){
			if(id && num){
				$.ajax({
					type:'post',
					url:ctx+"/scheduling/gantt/getAreaById",
					data:{
						id:id,
						num:num
					},
					dataType:'json',
					success:function(res){
						if(res.jw_def){
							$("#areas").val(res.jw_def);
						}else{
							$("#areas").val("!");
						}
						editTree = $.fn.zTree.init($("#editTree"), editSetting, res.atree);
						editTree.expandAll(true);
						var chosen = res.stands_def.split(",");
						for(var i=0;i<chosen.length;i++){
							var node = editTree.getNodeByParam("id", chosen[i], null);
							editTree.checkNode(node, true, true);
						}
					}
				})
			}
		},
		yes:function(){
			var data = {};
			data.num = num;
			if($("#areas").val() == "!"){
				layer.msg('请选择保障机位范围',{icon:7,time:1000});
				return false;
			}
			if($("#areas").val() != "!"){
				data.area = $("#areas").val();
				var nodes = editTree.getCheckedNodes(true);
				var nodeList = [];
				for(var i=0;i<nodes.length;i++){
					if(nodes[i].level == 1){
						nodeList.push(nodes[i].name);
					}
				}
				if(nodeList.length == 0){
					layer.msg('请选择保障机位范围',{icon:7,time:1000});
					return false;
				}
				data.nodeList = nodeList;
			}
			if($("#actypes").val() != "!"){
				data.actype = $("#actypes").val()
			}
			var loading = layer.load(2);
			$.ajax({
				type:'post',
				url:ctx+"/scheduling/gantt/saveDefResSet",
				data:{
					data:JSON.stringify(data)
				},
				error:function(){
					layer.msg("请求发送失败",{icon:2,time:1000},function(){
						closeLayer();
					});
					layer.close(loading);
				},
				success:function(msg){
					layer.close(loading);
					if(msg == "success"){
						layer.msg("保存成功！",{icon:1,time:1000},function(){
							closeLayer();
						});
					}else{
						layer.msg(msg,{icon:2,time:1000});
						console.error(msg);
					}
				}
			});
		}
	});
}
function closeLayer(){
	layer.close(iframe);
	$("#settingTable").bootstrapTable('refresh');
}