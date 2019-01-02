var layer;
var form;
var level = "1";
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
	if($("#type").val() == 0){
		$("#frame").attr('src', ctx + '/aptitude/aptitudeLimits/editLevel0?id='+$("#id").val());
	}else if($("#type").val() == 1){
		$("#frame").attr('src', ctx + '/aptitude/aptitudeLimits/editLevel1?id='+$("#id").val()+'&type='+$("#areType").val());
	};
	form.on('select(areType)', function(data) {
		$("#frame").attr('src', ctx + '/aptitude/aptitudeLimits/getAreaInfo?id=' + data.value);
	});
});

function saveForm() {
	var name = $("#name").val();
	if(name.length>25){
		layer.msg("区域名称过长，不要超过25个字。",{icon:7});
		return false;
	}
	
	var json = {
		id : $("#id").val() || '',
		type : $("#type").val() || '',
		name : $("#name").val() || '',
		areType : $("#areType").val() || '',
		info : $("#info").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/aptitude/aptitudeLimits/saveEdit",
		data : {
			'string' : JSON.stringify(json)
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
				}, function() {
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