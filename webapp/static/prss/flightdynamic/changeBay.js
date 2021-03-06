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
		in_fltid:$("#in_fltid").val() || '',
		out_fltid : $("#out_fltid").val() || '',
		newValue : $("#newValue").val() || '',
		oldValue : $("#oldValue").val() || '',
		acttype_code : $("#acttype_code").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/fdChange/saveBay",
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
			}else if(data.code == "2000"){
				layer.alert(data.msg,{
					title:'',
					icon:2,
					closeBtn: 0
				});
			}else {
				layer.msg('操作失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	});
}

function checkForm(){
	var newValue = $("#newValue").val();
	var oldValue = $("#oldValue").val();
	if(newValue==""){
		layer.msg("新值不能为空",{icon:7});
		return false;
	}
	if(newValue==oldValue){
		layer.msg("值未改变",{icon:7});
		return false;
	}
	
	return true;
}
