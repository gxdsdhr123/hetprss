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
		})
function saveForm() {
	var fltid = $("#fltid").val();
	var boardingStatus = $("#boardingStatus").val();
	var oldval = $("#oldval").val();
	$.ajax({
		type:'post',
		url:ctx+"/fdChange/saveBoardingStatus",
		data:{
			fltid:fltid,
			boardingStatus:boardingStatus,
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