var layer;
var form;
layui.use([ 'laypage', 'form', "layer" ], function() {
	var totalCount = Number($("#totalCount").val());
	var pageSize = Number($("#pageSize").val());

	var laypage = layui.laypage;
	form = layui.form;
	layer = layui.layer;
	laypage.render({
		elem : 'pagination',
		pages : Math.ceil(totalCount + pageSize - 1) / pageSize,
		curr : $("#pageNo").val(),
		count : totalCount,
		skip : true,
		jump : function(obj, first) {
			if (!first) {
				var pageNo = obj.curr;
				$("#pageNo").val(pageNo);
				query();
			}
		}
	});
	
	form.on('select(orderState)', function (data) {
		query();
    });
});


function query() {
	$("#searchForm").submit();
}

function view(processId,orderId){
	layer.open({
		type : 2,
		title : false,
		closeBtn:false,
		btn:["关闭"],
		area : [ '100%', '100%' ],
		content : [ctx + "/workflow/order/display?processId="+processId+"&orderId="+orderId,"yes"]
	});
}

function removeOrder(orderId){
	layer.confirm('您确定要删除当前实例？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		$.ajax({
			type : 'post',
			url : ctx + "/workflow/order/removeOrder",
			async : false,
			data : {
				orderId : orderId
			},
			success : function(data) {
				if(data=="succeed"){
					layer.msg("删除成功",{icon:1,time:600},function(){
						document.location.href = ctx+"/workflow/order";
					});
				} else {
					layer.msg("删除失败",{icon:7});
				}
			}
		});
	});
}