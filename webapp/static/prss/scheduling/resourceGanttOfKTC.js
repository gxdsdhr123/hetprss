window.clickRowId = "";
window.clickRow;
window.showAll = false;//只展现机动客梯车或展现所有客梯车
var layer;// 初始化layer模块
var iframe;
var loading;
$(function(){
	document.body.onselectstart=$("#carMenu")[0].oncontextmenu=function(){ return false;} ;
	var ganttOptions = {
		url:ctx+"/scheduling/gantt/getResGanttData",
		queryParams:{
			start:$("#dateB").val(),
			end:$("#dateE").val()
		},
		showEmptyRow:true,
		height:$("body").height()-$("#tool-box").height()-5,
		yData:{
			url:ctx+"/scheduling/gantt/getResGanttYData"
		}
	}
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		var form = layui.form;
		form.on('switch(switch)', function(data) {
			window.showAll = data.elem.checked ? true : false;
			ganttOptions.queryParams.start = $("#dateB").val();
			ganttOptions.queryParams.end = $("#dateE").val();
			$("#SJgantt").SJgantt('refreshOptions',ganttOptions);
		});
		loading = layer.load(2);
		$("#SJgantt").SJgantt(ganttOptions);
		layer.close(loading);
	})
	//刷新
	$("#refresh").click(function() {
		loading = layer.load(2);
		ganttOptions.queryParams.start = $("#dateB").val();
		ganttOptions.queryParams.end = $("#dateE").val();
		$("#SJgantt").SJgantt('refreshOptions',ganttOptions);
		layer.close(loading);
	})
	//设置
	$("#setting").click(function(){
		layer.open({
			type:2,
			title:"设置",
			area:["100%","100%"],
			content:ctx+"/scheduling/gantt/listResourceGanttSet"
		});
	})
	//车辆保障区域
	$("#area").click(function(){
		layer.open({
			type:2,
			title:"车辆保障区域",
			area:["100%","100%"],
			content:ctx+"/scheduling/gantt/listResourceGanttArea"
		});
	})
	//释放
	$("#truncate").click(function(){
		loading = layer.load(2);
		$.ajax({
			type:'post',
			url:ctx+"/scheduling/gantt/truncateTasks",
			data:{
				start:$("#dateB").val(),
				end:$("#dateE").val()
			},
			error:function(){
				layer.close(loading);
				layer.msg("请求错误，请检查网络",{icon:7,time:1000});
			},
			success:function(msg){
				if(msg == "success"){
					layer.msg("释放成功！",{icon:1,time:1000});
					$("#SJgantt").SJgantt('refresh');
				}else{
					layer.msg("释放失败",{icon:2,time:1000});
					console.log(msg);
				}
				layer.close(loading);
			}
		});
	});
	//预排
	$("#walkthrough").click(function(){
		loading = layer.load(2);
		$.ajax({
			type:'post',
			url:ctx+"/scheduling/gantt/walkthrough",
			data:{
				start:$("#dateB").val(),
				end:$("#dateE").val()
			},
			error:function(){
				layer.close(loading);
				layer.msg("请求错误，请检查网络",{icon:7,time:1000});
			},
			success:function(msg){
				if(msg == "success"){
					layer.msg("预排成功！",{icon:1,time:1000});
					$("#SJgantt").SJgantt('refresh');
				}else{
					layer.msg("预排失败",{icon:2,time:1000});
					console.log(msg);
				}
				layer.close(loading);
			}
		});
	})
	//人员分配
	$("#allocation").click(function(){
		loading = layer.load(2);
		$.ajax({
			type:'post',
			url:ctx+"/scheduling/gantt/resGanttAllocation",
			data:{
				start:$("#dateB").val(),
				end:$("#dateE").val()
			},
			error:function(){
				layer.close(loading);
				layer.msg("请求错误，请检查网络",{icon:7,time:1000});
			},
			success:function(msg){
				layer.close(loading);
				if(msg == "success"){
					layer.msg("人员分配成功！",{icon:1,time:1000});
				}else{
					layer.msg("人员分配失败",{icon:2,time:1000});
					console.log(msg);
				}
			}
		});
	})
})
function reset(obj){
	var id = obj.data("id");
	if(window.pool[id] && window.pool[id].length > 0){
		layer.confirm("更换车辆区域时，是否释放已分配任务？",{btn:["是","否","取消"]},
		function(index){
			resetWithTask(id);
			layer.close(index);
		},function(index){
			resetWithoutTask(id);
			layer.close(index);
		});
	}else{
		resetWithoutTask(id);
	}
	$("#carMenu").hide();
}
function resetWithoutTask(id){
	var loading = layer.load(2);
	$.ajax({
		type:'post',
		url:ctx+"/scheduling/gantt/resetWithoutTask",
		data:{
			id:id
		},
		error:function(){
			layer.close(loading);
			layer.msg('请求错误，请检查网络！')
		},
		success:function(msg){
			layer.close(loading);
			if(msg == "success"){
				layer.msg("恢复成功！",{icon:1,time:1000});
				$("#SJgantt").SJgantt('refresh');
			}else{
				layer.msg("恢复失败",{icon:2,time:1000});
				console.log(msg);
			}
		}
	});
}
function resetWithTask(id){
	var tasks = "";
	for(var o in window.pool[id]){
		tasks += ",'"+window.pool[id][o].kTaskId+"','"+window.pool[id][o].cTaskId+"'";
	}
	tasks = tasks.substring(1);
	var loading = layer.load(2);
	$.ajax({
		type:'post',
		url:ctx+"/scheduling/gantt/resetWithTask",
		data:{
			id:id,
			tasks:tasks
		},
		error:function(){
			layer.close(loading);
			layer.msg('请求错误，请检查网络！')
		},
		success:function(msg){
			layer.close(loading);
			if(msg == "success"){
				layer.msg("恢复成功！",{icon:1,time:1000});
				$("#SJgantt").SJgantt('refresh');
			}else{
				layer.msg("恢复失败",{icon:2,time:1000});
				console.log(msg);
			}
		}
	});
}