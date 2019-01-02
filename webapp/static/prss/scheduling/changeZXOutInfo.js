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
		outBaggageReal : $("#outBaggageReal").val() || '',
		outLargeBaggage : $("#outLargeBaggage").val() || '',
		outZxRemark : $("#outZxRemark").val() || '',
		outBaggageRealOld : $("#outBaggageRealOld").val() || '',
		outLargeBaggageOld : $("#outLargeBaggageOld").val() || '',
		outZxRemarkOld : $("#outZxRemarkOld").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/scheduling/change/saveZXOutInfo",
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
	var outZxRemark = $("#outZxRemark").val();
	var outBaggageReal = $("#outBaggageReal").val();
	var outLargeBaggage = $("#outLargeBaggage").val();
	if(outZxRemark.length>255){
		layer.msg("备注不得超过255个字符",{icon:7});
		return false;
	}
	if(outBaggageReal.length>255){
		layer.msg("实装件数不得超过255个字符",{icon:7});
		return false;
	}
	if(outLargeBaggage.length>255){
		layer.msg("大件行李不得超过255个字符",{icon:7});
		return false;
	}
	
	if(outBaggageReal&&!checkNumber(outBaggageReal)){
		layer.msg("值应为正整数或零",{icon:7});
		return false;
	}
	
	if(outLargeBaggage&&!checkNumber(outLargeBaggage)){
		layer.msg("值应为正整数或零",{icon:7});
		return false;
	}
	
	return true;
}
function checkNumber(number){
	var reg =/^([0-9]\d*|0)$/;
	return reg.test(number);
}
