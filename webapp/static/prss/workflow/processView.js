$(document).ready(function() {
	$.ajax({
		type : 'post',
		url : ctx + "/workflow/order/orderJson",
		data : {
			processId : $("#processId").val(),
			orderId : $("#orderId").val()
		},
		async : false,
		globle : false,
		dataType : "json",
		error : function() {
			alert('数据处理错误！');
			return false;
		},
		success : function(data) {
			display(data.process, data.state);
		}
	});
});

function display(process, state) {
	$('#snakerflow').snakerflow($.extend(true, {
		basePath : ctxStatic + "/snaker/",
		ctxPath : ctx,
		orderId : $("#orderId").val(),
		restore : eval("(" + process + ")"),
		editable : false,
		path:{
			attr:{
				path:{path:"M10 10L100 100",stroke:"#006DC0",fill:"none","stroke-width":3},
				arrow:{path:"M10 10L10 10",stroke:"#006DC0",fill:"#006DC0","stroke-width":3,radius:4}
			}
		}
	}, eval("(" + state + ")")));
}