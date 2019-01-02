var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

$(function() {
	// 初始化下拉选
	initSelect();
});

function initSelect() {
	$('.departType').each(function(i, e) {
		var id = $(e).attr("id");
		var placeholder = "";
		var width = "100%";
		if("sendDept" == id) {
			placeholder = "请选择涉及岗位";
			width = "100%";
		} else if("airlineCompany" == id) {
			placeholder = "航空公司复选";
		} else if("airGroup" == id) {
			placeholder = "航空公司分组复选";			
		}
		$('#' + id).select2({
			placeholder : placeholder,
			width : width,
			language : "zh-CN"
		});
	});
}

// 获取表单数据
function getFormData() {
	return $("#abnormalForm").serialize();
}

window.onload = function() {
	// 取消遮罩
	setTimeout(function() {
		$('div[class=mark_c]').attr({
			'style' : 'display: none;'
		});
	}, 600);

}