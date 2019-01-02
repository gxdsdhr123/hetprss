var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});


function picOrAudio(searchId, sign) {
	var url = ctx + '/oscarrSecurity/report/pageJump/'+searchId + "/" + sign;
	layer.open({
		type:2,
		area: ['450px','450px'],
		skin: 'layui-layer-nobg',
		shadeClose: true,
		title:false,
		content: url
	});

}
