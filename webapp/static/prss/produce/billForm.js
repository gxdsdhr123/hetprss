var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
//	if($("#SIGNATORY").val()==""){
//		$("#SIGNATORYdiv").css("display", "none");
//	}
	var parentIframe = parent.document.getElementById("frame");
	var height = $(parentIframe).height();
	$("#billForm").height(height);
	
	
	//设置滚动条--begin
	var billForm = $("#billForm");
	billForm.css("position", "relative");
	new PerfectScrollbar(billForm[0]);
	//设置滚动条--end
	initSelect2("ACT_TYPE","请选择机型");//机型
	initSelect2("OPERATOR","请选择操作人");//操作人
	$(".layui-unselect").hide();
})

/**
 * 初始化select下拉
 * @param selectId
 * @param tip
 */
function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,  
        width:"190px",
        language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
    }); 
}

function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}

function saveBill() {
	var fltNum = $("#FLIGHT_NUMBER").val();
	var itemDate = $("#ITEM_DATE").val();
	var name = $("#ITEM_NAME").val();
	var num = $("#NUM").val();
	if(!fltNum||!$.trim(fltNum)){
		layer.msg("请输入航班号",{icon:7});
		return false;
	}
	if(!itemDate||!$.trim(itemDate)){
		layer.msg("请输入日期",{icon:7});
		return false;
	}
	if(!name||!$.trim(name)){
		layer.msg("请输入项目",{icon:7});
		return false;
	}
	if(!num||!$.trim(num)){
		layer.msg("请输入数量",{icon:7});
		return false;
	}
	var json = {};
	for(var i=0;i<$("#billForm input").length;i++){
		var o = $("#billForm input").eq(i);
		json[o.attr('name')] = o.val() || '';
	}
	for(var i=0;i<$("#billForm select").length;i++){
		var o = $("#billForm select").eq(i);
		json[o.attr('name')] = o.val() || '';
	}
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/produce/bill/save",
		data : {
			'bill' :JSON.stringify(json)
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

function isEmpty() {
	var fltNum = $("#FLIGHT_NUMBER").val();
	var itemDate = $("#ITEM_DATE").val();
	var name = $("#ITEM_NAME").val();
	var num = $("#NUM").val();
	if(!fltNum||!$.trim(fltNum)){
		layer.msg("请输入航班号",{icon:7});
		return false;
	}
	if(!itemDate||!$.trim(itemDate)){
		layer.msg("请输入日期",{icon:7});
		return false;
	}
	if(!name||!$.trim(name)){
		layer.msg("请输入项目",{icon:7});
		return false;
	}
	if(!num||!$.trim(num)){
		layer.msg("请输入数量",{icon:7});
		return false;
	}
	return true;
}

function getData() {
	var json = {};
	for(var i=0;i<$("#billForm input").length;i++){
		var o = $("#billForm input").eq(i);
		json[o.attr('name')] = o.val() || '';
	}
	return json;
}