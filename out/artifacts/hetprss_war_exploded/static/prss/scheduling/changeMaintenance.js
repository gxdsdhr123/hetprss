var layer;
var form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
			layui.use([ "form" ], function() {
				 form = layui.form;
				form.render();
				
			})
})
window.onload=function(){
	form.render('select');
	form.render();
	
}	

function saveForm() {
	if(!checkForm()){
		return false;
	};
	
	var json = {
		fltid:$("#fltid").val() || '',
		field:$("#field").val() || '',
		attrId:$("#attrId").val() || '',
		value : $("#value").val() || ''
	}
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/scheduling/change/saveMaintenance",
		data : {
			'string' : JSON.stringify(json)
		},
		error : function() {
			layer.close(loading);
			layer.msg('操作失败！', {
				icon : 2
			});
		},
		success : function(data) {
			if (data.code == "0000") {
				layer.msg(data.msg, {
					icon : 1,
					time : 600
				}, function() {
					parent.saveSuccess();
				});
			}else {
				layer.msg(data.msg, {
					icon : 2,
					time : 600
				});
			}
		}
	});
}
/**
 * checkForm()
 * @returns {Boolean}
 * @description:
 * 字符限制在100字内
 */
function checkForm(){
	var value = $("#value").val();
	if(!checkInputLength(value)){
		layer.msg("字符限制在100字内",{icon:7});
		return false;
	}
		
	return true;
}
function checkInputLength(value){
	if(value.length>100){
		return false;
	}
	return true;
}
