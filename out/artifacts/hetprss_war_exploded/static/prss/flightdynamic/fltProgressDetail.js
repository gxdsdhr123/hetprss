var layer;
layui.use(["layer"]);
function viewState(processId,orderId){
	parent.layer.open({
		type : 2,
		title : false,
		closeBtn:false,
		btn:["关闭"],
		area : [ '100%', '100%' ],
		content : [ctx + "/workflow/order/display?processId="+processId+"&orderId="+orderId,"yes"]
	});
}