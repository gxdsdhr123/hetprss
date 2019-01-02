var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

$(document).ready(function() {
	if (!$("#editBtn").length > 0 && !$("#scheEditBtn").length > 0){ 
		$("input").attr("readonly","readonly");
	} 
});

function savePassenger() {
	if (!$("#editBtn").length > 0 && !$("#scheEditBtn").length > 0){ 
		parent.saveSuccess();
	}
	var json = {};
	for(var i=0;i<$("#passengerForm input").length;i++){
		var o = $("#passengerForm input").eq(i);
		json[o.attr('name')] = o.val() || '';
	}
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/flightDynamic/updateNop",
		data : {
			'passenger' :JSON.stringify(json)
		},
		error : function() {
			layer.close(loading);
			layer.msg('保存失败！', {
				icon : 2
			});
		},
		success : function(data) {
			layer.close(loading);
			if (data == "success") {
				layer.msg('保存成功！', {
					icon : 1,
					time : 600
				},function(){
					parent.saveSuccess();
				});
			} else {
				layer.msg('保存失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	});
}