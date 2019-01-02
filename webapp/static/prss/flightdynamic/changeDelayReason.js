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
		delayReason : $("#delayReason").val() || '',
		releaseReason : $("#releaseReason").val() || '',
		delayReasonOld : $("#delayReasonOld").val() || '',
		releaseReasonOld : $("#releaseReasonOld").val() || '',
		delayReasonDetail : $("#delayReasonDetail").val() || '',
		releaseReasonDetail : $("#releaseReasonDetail").val() || '',
		delayReasonDetailOld : $("#delayReasonDetailOld").val() || '',
		releaseReasonDetailOld : $("#releaseReasonDetailOld").val() || '',
		delayReasonInner : $("#delayReasonInner").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/fdChange/saveDelayReason",
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
	var delayReasonDetail = $("#delayReasonDetail").val();
	var releaseReasonDetail = $("#releaseReasonDetail").val();
	var delayReasonInner = $("#delayReasonInner").val();
	if(delayReasonDetail.length>30){
		layer.msg("详情不得超过30个字符",{icon:7});
		return false;
	}
	
	return true;
}
