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
			$("#counters").select2({
				data:data.results,
				allowClear:true,
				multiple: true,
			});
		})
function saveForm() {
	var fltid = $("#fltid").val();
	var counter = $("#counters").val();
	var oldval = $("#oldval").val();
	var counterStr = "";
	for(var i=0;i<counter.length;i++){
		counterStr += counter[i]+",";
	}
	if(""!= counterStr){
		counterStr = counterStr.substring(0,counterStr.length-1);
	}
	$.ajax({
		type:'post',
		url:ctx+"/fdChange/saveCounter",
		data:{
			fltid:fltid,
			counter:counterStr,
			oldval:oldval
		},
		success:function(res){
			if("success"==res){
				layer.msg("保存成功",{icon:1,time:1000},function(){
					parent.formSubmitCallback();
				});
			}else{
				layer.msg("保存失败",{icon:2,time:1000});
				console.log(res);
			}
		}
	});
}