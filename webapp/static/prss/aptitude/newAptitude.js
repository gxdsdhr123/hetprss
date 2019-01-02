var layer;
var level = "1";
var form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;

	form.on('radio(jw)', function(data) {
		if (level == 0) {
			$("#frame").attr('src', ctx + '/aptitude/aptitudeLimits/aptitudeJW');
		}
		if (level == 1) {
			getSelect(0, $("#id").val());
			$("#frame").attr('src', '');
		}
	});
	form.on('radio(jx)', function(data) {
		if (level == 0) {
			$("#frame").attr('src', ctx + '/aptitude/aptitudeLimits/aptitudeJX');
		}
		if (level == 1) {
			getSelect(1, $("#id").val());
			$("#frame").attr('src', '');
		}
	});
	form.on('radio(hs)', function(data) {
		if (level == 0) {
			$("#frame").attr('src', ctx + '/aptitude/aptitudeLimits/aptitudeHS');
		}
		if (level == 1) {
			getSelect(2, $("#id").val());
			$("#frame").attr('src', '');
		}
	});
	form.on('select(type)', function(data) {
		if (data.value == 0) {
			level = data.value;
			$("#areTypeDiv").attr('style', 'display:none');
			$("#frame").attr('src', '');
			$('input[type="radio"][name="att"]').attr("checked",false);
			form.render('radio');
		} else if (data.value == 1) {
			level = data.value;
			$("#areTypeDiv").attr('style', 'display:inline');
			$("#frame").attr('src', '');
			$('input[type="radio"][name="att"]').attr("checked",false);
			form.render('radio');
		}
	});
	form.on('select(areType)', function(data) {
		$("#frame").attr('src', ctx + '/aptitude/aptitudeLimits/getAreaInfo?id=' + data.value);
	});
});

function getSelect(type, id) {
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/aptitude/aptitudeLimits/getSelect",
		data : {
			'type' : type,
			'id' : id
		},
		dataType : "json",
		success : function(result) {
			$("#areType").empty();
			$("#areType").append("<option value=''>请选择一个资质区域</option>");
			for (var i = 0; i < result.length; i++) {
				$("#areType").append("<option value='" + result[i]["ID"] + "'>"	+ result[i]["AREA_NAME"] + "</option>");
			}
			form.render("select");
		}
	});
}

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
		att : $('input[type="radio"][name="att"]:checked').val() || '',
		areType : $("#areType").val() || '',
		info : $("#info").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/aptitude/aptitudeLimits/save",
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
