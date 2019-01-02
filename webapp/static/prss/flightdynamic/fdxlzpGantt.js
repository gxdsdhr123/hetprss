window.clickRowId = "";
var layer;// 初始化layer模块
var iframe;
var form;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		form=layui.form;
	});
	var ganttOptions = {
		url:ctx+"/flightdynamic/gantt/fdxlzpGanttData",
		showEmptyRow:true,
		height:$("body").height()-$("#tool-box").height(),
		yData:{
			url:ctx+"/flightdynamic/gantt/fdxlzpGanttYData",
			responseHandler:function (res) {
				res.unshift({"id":"DP","name":"待排"});
                return res;
            }
		}
	}
	$("#SJgantt").SJgantt(ganttOptions);
	// 刷新
	if(!sessionStorage || !sessionStorage[$("#loginName").val()+"refreshxlzpGantt"] || sessionStorage[$("#loginName").val()+"refreshxlzpGantt"] == "null" || sessionStorage[$("#loginName").val()+"refreshxlzpGantt"]=="undefined" || typeof(sessionStorage[$("#loginName").val()+"refreshxlzpGantt"]) == "undefined"){
		autoReload("10");
	}else{
		autoReload(sessionStorage[$("#loginName").val()+"refreshxlzpGantt"]);
	}
	$("#refresh").click(function() {
		$("#SJgantt").SJgantt('refresh');
	})
	$("#refresh-c").click(function() {
		layer.open({
			title : '自定义刷新',
			content : '每<input type="number" min="1" class="layui-input layui-sm" style="width:120px;margin:0px 10px;display:inline-block"/>秒钟自动刷新',
			yes : function(index, layero) {
				var refreshTime = layero.find("input").val();
				if (refreshTime != null && refreshTime != "") {
					autoReload(refreshTime);
				}
				layer.close(index);
			}
		});
	})
	//保存机位
	$("#save").click(function(){
		var loading = layer.load(1);
		var param = [];
		for(var f in pool){
			for(var o in pool[f]){
				if(pool[f][o].tempStand != pool[f][o].stand){
					var j = {
							fltid:pool[f][o].id,
							oldVal:pool[f][o].stand=="DP"?"":pool[f][o].stand,
							newVal:pool[f][o].tempStand=="DP"?"":pool[f][o].tempStand
					}
					param.push(j);
				}
			}
		}
		$.ajax({
			type:'post',
			url: ctx + "/flightdynamic/gantt/saveCarousel",
			data:{
				param:JSON.stringify(param)
			},
			success:function(msg){
				if(msg == "success"){
					layer.msg("保存行李转盘成功",{icon:1});
					$("#SJgantt").SJgantt('refresh');
				}else{
					layer.msg("保存行李转盘失败",{icon:2});
				}
				layer.close(loading);
			}
		});
	})
	//取消调整
	$("#cancel").click(function(){
		layer.confirm('确定取消调整机位吗？', {icon: 3, title:'提示'}, function(index){
			for(var f in pool){
				for(var o in pool[f]){
					if(pool[f][o].tempStand != pool[f][o].stand){
						pool[f][o].tempStand = pool[f][o].stand;
						pool[f][o].hasChild = false;
					}
					if(pool[f][o].isChild){
						pool[f].splice(o,1);
					}
				}
			}
			layer.close(index);
		});
	})
});
var intervalObj = null;
var refreshUnit = 1000;//刷新单位是秒
function autoReload(interval){
	if(interval<10){
		layer.msg("最小刷新频率不要小于10秒！",{icon:7});
		return false;
	}
	sessionStorage.setItem($("#loginName").val()+"refreshxlzpGantt",interval);
	var time = Number(interval) * refreshUnit;
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟";
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		$("#SJgantt").SJgantt('refresh');
	}, time);
}