$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	if ($('#mainKindId').val()) {
		$('input[name="RESKIND"]').attr("readonly", "readonly");
	}
	// 表单提交方法被调用后触发此验证
	$('#createForm').validate({
		ignore : "",
		rules : {
			RESKIND : "required",// 必填项
			DEPNAME : "required",
			KINDNAME : "required",
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.parent());
		},
		submitHandler : function(form) {
			if ((!$('#mainKindId').val() && vaild()) || $('#mainKindId').val()) {
				var loading = null;
				$("#createForm").ajaxSubmit({
					async : false,
					beforeSubmit : function() {
						loading = layer.load(2, {
							shade : [ 0.1, '#000' ]
						// 0.1透明度
						});
					},
					error : function(XmlHttpRequest, textStatus, errorThrown) {
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
								parent.formSubmitCallback();// 成功后调用主页面，关闭弹出层并刷新表格
							});
						} else if (data == "error") {
							layer.msg('保存失败！', {
								icon : 2,
								time : 600
							});
						} else if (data == "overTime") {// 修改时间验证，若有变化说明数据已被改动，需要重新加载数据
							layer.msg('数据已失效，请重新加载数据', {
								icon : 2,
								time : 600
							}, function() {
								parent.formSubmitCallback();
							});
						}
					}
				});
				return false;
			}
		}
	});
})

function vaild() {
	var flag = false;
	$.ajax({
		type : 'post',
		url : ctx + "/logistic/vaildOnlyReskind",
		async : false,
		data : {
			'reskind' : $('input[name="RESKIND"]').val()
		},
		success : function(data) {
			if (data == "Y") {
				flag = true;
			} else if (data == "N") {
				layer.msg('已存在此保障类型，请重新输入！', {
					icon : 2,
					time : 3000
				});
			} else if (data == "E") {
				layer.msg('请输入作业类型！', {
					icon : 2,
					time : 2000
				});
			}
		}
	});
	return flag;
}

function doSubmit() {
	$('#createForm').submit();
}