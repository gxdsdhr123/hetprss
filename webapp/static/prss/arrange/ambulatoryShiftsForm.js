var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
function saveAFT() {
	var json = {};
	for(var i=0;i<$("#aftForm input").length;i++){
		var o = $("#aftForm input").eq(i);
		json[o.attr('name')] = o.val() || '';
	}
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/arrange/ambulatoryShifts/save",
		data : {
			'aft' :JSON.stringify(json)
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

var selectedRow = -1;
function setFln(btn){
	selectedRow = $(btn).attr("name").substr(6,1);
	var sdata = $("input[name=bindFlt"+selectedRow+"]").val();
	var flnWin = layer.open({
		closeBtn : false,
		type : 2,
		title : "绑定航班",
		area : [ '700px', '400px' ],
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.prepareMembers();
			layer.close(flnWin);
			return false;
		},
		content : [ ctx + "/arrange/ambulatoryShifts/fln?day="+selectedRow+"&sFltNo="+sdata]
	})
}
