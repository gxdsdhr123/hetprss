window.clickRowId = "";
var layer;// 初始化layer模块
var iframe;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	var ganttOptions = {
		url:ctx+"/flightdynamic/gantt/fdGateGanttData",
		showEmptyRow:true,
		height:$("body").height()-$("#tool-box").height(),
		yData:{
			url:ctx+"/flightdynamic/gantt/fdGateGanttYData",
		}
	}
	$("#SJgantt").SJgantt(ganttOptions);
	// 刷新
	if(!sessionStorage || !sessionStorage[$("#loginName").val()+"refreshGantt"] || sessionStorage[$("#loginName").val()+"refreshGantt"] == "null" || sessionStorage[$("#loginName").val()+"refreshGantt"]=="undefined" || typeof(sessionStorage[$("#loginName").val()+"refreshGantt"]) == "undefined"){
		autoReload("0.5");
	}else{
		autoReload(sessionStorage[$("#loginName").val()+"refreshGantt"]);
	}
	$("#refresh").click(function() {
		clearInterval(intervalObj);
		$("#SJgantt").SJgantt('refresh');
	})
	$("#refresh-c").click(function() {
		layer.open({
			title : '自定义刷新',
			content : '每<input type="number" min="1" class="layui-input layui-sm" style="width:120px;margin:0px 10px;display:inline-block"/>分钟自动刷新',
			yes : function(index, layero) {
				var refreshTime = layero.find("input").val();
				if (refreshTime != null && refreshTime != "") {
					autoReload(refreshTime);
				}
				layer.close(index);
			}
		});
	})
	//保存登机口
	$("#save").click(function(){
		var loading = layer.load(1);
		$.ajax({
			type:'post',
			url: ctx + "/flightdynamic/gantt/saveGate",
			success:function(msg){
				if(msg == "success"){
					layer.msg("保存登机口成功",{icon:1});
					$("#SJgantt").SJgantt('refresh');
				}else{
					layer.msg("保存登机口失败",{icon:2});
				}
				layer.close(loading);
			}
		});
	})
	//取消调整
	$("#cancel").click(function(){
		layer.confirm('确定取消调整登机口吗？', {icon: 3, title:'提示'}, function(index){
			var loading = layer.load(1);
			$.ajax({
				type:'post',
				url: ctx + "/flightdynamic/gantt/cancelGate",
				success:function(msg){
					if(msg == "success"){
						layer.msg("取消登机口调整成功",{icon:1});
						$("#SJgantt").SJgantt('refresh');
					}else{
						layer.msg("取消登机口调整失败",{icon:2});
					}
					layer.close(loading);
				}
			});
		});
	})
	//配置
	$("#setting").click(function(){
		layer.open({
			type:1,
			title:"起止时间配置",
			area:["500px","200px"],
			content:$("#settingDiv"),
			btn:["保存","取消"],
			yes:function(index,layero){
				var loading = layer.load(1);
				$.ajax({
					type:'post',
					url: ctx + "/flightdynamic/gantt/saveGateSettings",
					data:{
						startAttr:$(".startAttr").val(),
						startRel:$(".startRel").val(),
						startMinute:$(".startMinute").val(),
						endAttr:$(".endAttr").val(),
						endRel:$(".endRel").val(),
						endMinute:$(".endMinute").val()
					},
					success:function(msg){
						if(msg == "success"){
							layer.msg("起止时间配置调整成功",{icon:1});
							$("#SJgantt").SJgantt('refresh');
						}else{
							layer.msg("起止时间配置调整失败",{icon:2});
						}
						layer.close(loading);
					}
				});
				layer.close(index);
			}
		});
	});
	initSettings();
});
var intervalObj = null;
var refreshUnit = 60*1000;//刷新单位是分钟
function autoReload(interval){
	if(interval<0.5){
		layer.msg("最小刷新频率不要小于30秒！",{icon:7});
		return false;
	}
	sessionStorage.setItem($("#loginName").val()+"refreshGantt",interval);
	var time = Number(interval) * refreshUnit;
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "分钟";
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		$("#SJgantt").SJgantt('refresh');
	}, time);
}
function dateFilter() {
	WdatePicker({
		maxDate:'%y-%M-%d',
		dateFmt : 'yyyyMMdd',
		onpicking : function(dp) {	

		}
	});
}
function initSettings(){
	$.ajax({
		type:'post',
		url: ctx + "/flightdynamic/gantt/getGateSettings",
		dataType:'json',
		success:function(res){
			$(".startAttr").val(res.startAttr);
			$(".startRel").val(res.startRel);
			$(".startMinute").val(res.startMinute);
			$(".endAttr").val(res.endAttr);
			$(".endRel").val(res.endRel);
			$(".endMinute").val(res.endMinute);
		}
	});
}