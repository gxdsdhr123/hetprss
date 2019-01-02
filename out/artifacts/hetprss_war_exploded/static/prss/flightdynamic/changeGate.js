var layer;
var form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
var row;
$(document).ready(function() {
	layui.use([ "form" ], function() {
		 form = layui.form;
		form.render();
		
	});
	$("html,body").css("cssText","height:100% !important");
	row = parent.clickRow
});
function saveForm() {
	var fltid = $("#fltid").val();
	var counterStatus = $("#counterStatus").val();
	var oldval = $("#oldval").val();
	$.ajax({
		type:'post',
		url:ctx+"/fdChange/saveGate",
		data:{
			fltid:row.outFltid,
			newVal:$("#gates").val(),
			oldval:row.outGate
		},
		success:function(res){
			if("success"==res){
				layer.msg("保存成功",{icon:1,time:1000},function(){
					parent.formSubmitCallback();
				});
			}else{
				layer.msg("保存失败",{icon:2,time:1000},function(){
					parent.formSubmitCallback();
				});
				console.log(res);
			}
		}
	});
}