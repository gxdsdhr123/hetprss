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
		inSpecialCargo : $("#inSpecialCargo").val() || '',
		inZxRemark : $("#inZxRemark").val() || '',
		inSpecialCargoOld : $("#inSpecialCargoOld").val() || '',
		inZxRemarkOld : $("#inZxRemarkOld").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/scheduling/change/saveZXInInfo",
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
			layer.close(loading);
			if (data.code == "0000") {
				layer.msg(data.msg, {
					icon : 1,
					time : 600
				}, function() {
					parent.saveSuccess();
				});
			}else if(data.code == "1000"){
				layer.alert(data.msg,{
					title:'警告',
					icon:7
				},function() {
					parent.saveSuccess();
				});
			}else {
				layer.msg(data.msg, {
					icon : 2,
					time : 2600
				});
			}
		}
	});
}

function checkForm(){
	var inZxRemark = $("#inZxRemark").val();
	var inSpecialCargo = $("#inSpecialCargo").val();
//	var delayReasonInner = $("#delayReasonInner").val();
	if(inZxRemark.length>255){
		layer.msg("备注不得超过255个字符",{icon:7});
		return false;
	}
	if(inSpecialCargo.length>255){
		layer.msg("特货信息不得超过255个字符",{icon:7});
		return false;
	}
	
	return true;
}
