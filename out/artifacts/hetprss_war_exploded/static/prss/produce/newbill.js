layui.use([ "layer", "form", "element" ], function() {
	var form = layui.form;
});

$(function() {
	var layero = parent.layer.getChildFrame().context;
    var iframe = $(layero).find("iframe")[0];
    var height = iframe.clientHeight;
    $("#frame").height(height-70);
	layui.use([ "form" ], function() {
		var form = layui.form;
		form.on('select(jobType)', function(data) {
			getSelect(data.value);
			$("#frame").attr('src', '');
		});
		form.on('select(jobCode)', function(data) {
			$("#frame").attr('src', ctx + "/produce/bill/togo?jobCode=" + data.value);
		});
	});
})

function getSelect(type){
	layui.use([ "form" ], function() {
		var form = layui.form;
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/produce/bill/getSelect",
			data : {
				'type' : type
			},
			dataType : "json",
			success : function(result) {
				$("#jobCode").empty();
				$("#jobCode").append("<option value=''>请选择一个单据</option>");
				for (var i = 0; i < result.length; i++) {
					$("#jobCode").append("<option value='" + result[i]["TYPE_CODE"] + "'>"	+ result[i]["TYPE_NAME"] + "</option>");
				}
				form.render(null, 'jobCode');
			}
		});
	})
}

function addBill() {
	if(frame.window.isEmpty()){
		var billData = frame.window.getData();
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/produce/bill/addBill",
			data : {
				'bill' :JSON.stringify(billData),
				'code' :$("#jobCode").val()
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
}