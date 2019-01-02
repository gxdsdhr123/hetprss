var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
function addBill(){
	var billData = {};
	$("input,textarea").each(function(i,ele){
		billData[this.name] = $(this).val();
	});
	console.log(billData)
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/produce/monitor/update",
		data : billData,
		error : function() {
			layer.close(loading);
			layer.msg('保存失败！', {
				icon : 2,
				time : 1000
			});
		},
		success : function(dataMap) {
			layer.close(loading);
			if (dataMap.code == 0) {
				parent.saveSuccess();
			} else {
				layer.msg('保存失败，'+dataMap.msg, {
					icon : 2,
					time : 600
				});
			}
		}
	});
}