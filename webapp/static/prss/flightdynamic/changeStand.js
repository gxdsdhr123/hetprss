var layer;
var form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	layui.use([ "form" ], function() {
		 form = layui.form;
		form.render();
		
	});
	$("html,body").css("cssText","height:100% !important");
	$("#oriStand").val(parent.clickRow.stand);
});
function giveValue(obj){
	var stand = $(obj).text();
	$("#nowStand").val(stand=="清空"?"":stand);
}
function saveForm() {
	var pool = parent.window.pool;
	var tempO = parent.clickRow;
	if(""==$("#nowStand").val()){
		layer.confirm('确定置空机位吗？', {icon: 3, title:'提示'}, function(index){
			var loadIcon = layer.load(1);
			$.ajax({
				type:'post',
				url:ctx+"/flightdynamic/gantt/immediateRemoveStand",
				data:{
					id:tempO.id
				},
				success:function(data){
					if(data=="success"){
						layer.msg("机位已置空！",{icon:1,time:600},function(){
							parent.formSubmitCallback();
						});
					} else {
						layer.msg("置空失败",{icon:7},function(){
							parent.formSubmitCallback();
						});
					}
					layer.close(loadIcon);
				}
			});
			layer.close(index);
		});
	}else{
		var fc = false;
		var k = $("#nowStand").val();
		for ( var t in pool[k]) {//判断停用机位和驻场不能拖上来
			if(pool[k][t].type == '1' || pool[k][t].type == '2'){
				if(tempO.x<pool[k][t].x2 && pool[k][t].x<tempO.x2){
					fc = true;
					layer.msg("分配失败,存在停用机位或驻场航班！",{icon:7});
					break;
				}
			}
		}
		if(tempO.type == "2"){
			if(new Date(tempO.start) < new Date()){
				fc = true;
				layer.msg("驻场已开始，如需调整请使用拖飞机功能",{icon:7},function(){
					parent.formSubmitCallback();
				});
			}else{
				for(var p in pool[k]){
					if(tempO.end){
						if(tempO.x < pool[k][p].x2 && tempO.x2 > pool[k][p].x){
							fc = true;
							layer.msg("与已分配航班冲突，请调整后再分配。",{icon:7},function(){
								parent.formSubmitCallback();
							});
							break;
						}
					}else{
						if(tempO.x < pool[k][p].x2){
							fc = true;
							layer.msg("与已分配航班冲突，请调整后再分配。",{icon:7},function(){
								parent.formSubmitCallback();
							});
							break;
						}
					}
				}
			}
		}
		if(tempO.isChild && k == tempO.oriStand){
			fc = false;
		}
		if(!fc){
			if(tempO.type == "2"){
				layer.confirm('确定分配机位吗？', {icon: 3, title:'提示'}, function(index){
					var loadIcon = layer.load(1);
					$.ajax({
						type:'post',
						url:ctx+"/flightdynamic/gantt/saveInFieldStand",
						data:{
							id : tempO.id,
							stand: k,
							oldStand: tempO.stand,
							fltid: tempO.inFltid
						},
						success:function(data){
							if(data=="success"){
								layer.msg("机位已分配！",{icon:1,time:600},function(){
									parent.formSubmitCallback();
								});
							} else if (data=="error"){
								layer.msg("分配失败",{icon:7},function(){
									parent.formSubmitCallback();
								});
							} else {
								//机位分配校验失败
								layer.msg(data,{icon:7},function(){
									parent.formSubmitCallback();
								});
							}
							layer.close(loadIcon);
						}
					});
				  	layer.close(index);
				});
			}else{
				var loadIcon = layer.load(1);
				$.ajax({
					type:'post',
					url:ctx+"/flightdynamic/gantt/immediateSetStand",
					data:{
						id : tempO.id,
						stand: k
					},
					success:function(data){
						if(data=="success"){
							layer.msg("机位已分配！",{icon:1,time:600},function(){
								parent.formSubmitCallback();
							});
						} else if (data=="error"){
							layer.msg("分配失败",{icon:7},function(){
								parent.formSubmitCallback();
							});
						} else {
							//机位分配校验失败
							layer.msg(data,{icon:7},function(){
								parent.formSubmitCallback();
							});
						}
						layer.close(loadIcon);
					}
				});
			}
		}
	}
}