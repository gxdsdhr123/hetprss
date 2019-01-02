var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	var form = layui.form;
});

$(document).ready(function() {
	if($("#SIGNATORY").val()==""){
		$("#SIGNATORYdiv").css("display", "none");
	}
	$.each($('input:checkbox'),function(){
        if($(this).val()=='1'){
        	$(this).attr("checked", 'true');
        }else if ($(this).val()=='0') {
        	$(this).removeAttr("checked");
		}
    });
	initSelect2("operator","请选择操作人");//操作人
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
function getData() {
	var json = {};
	for(var i=0;i<$("#billForm input").length;i++){
		var o = $("#billForm input").eq(i);
		if (o.attr('type')=="checkbox") {
			if (o.is(':checked')) {
				json[o.attr('name')] = '1';
			}else {
				json[o.attr('name')] = '0';
			}
		}else {
			if (o.attr('name')&&o.attr('name')!="") {
				json[o.attr('name')] = o.val() || '';
			}
		}
	}
	for(var i=0;i<$("#billForm select").length;i++){
		var o = $("#billForm select").eq(i);
		if (o.attr('name')&&o.attr('name')!="") {
			var name =o.attr('name');
			json[name] = $("#billForm select[name='"+name+"'] option:selected").val() || '';
		}
	}
	return json;
}





function addtr() {
	var emptyTr=$("#addtr").find("tr").clone();;
	$("tbody[name='tbody']").append(emptyTr.show());
	
}
function clickRow(obj) {
	$("tbody[name='tbody']").find("tr").removeClass("clickRow");
	var tr=$(obj);
	tr.addClass("clickRow");
}
function minustr() {
	$("tbody[name='tbody']").find("tr").remove(".clickRow");
}


function saveBill() {
	var billData = getData();
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/produce/bill/saveBill",
		data : {
			'bill' :JSON.stringify(billData),
			'isNew' :'new'
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

