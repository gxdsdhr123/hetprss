var layer;
var form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
			layui.use([ "form" ], function() {
				 form = layui.form;
				form.render();
				
			})
		})
window.onload=function(){
	form.render('select');
	form.render();
	
}

function saveForm() {
	
	if(!checkForm()){
		return false;
	};
	var json = {
		fltid:$("#fltid").val() || '',
		diversionPort : $("#diversionPort").val() || '',
		diversionRes : $("#diversionRes").val() || '',
		diversionResDetail : $("#diversionResDetail").val() || '',
		diversionATD : $("#diversionATD").val() || '',
		diversionPortOld: $("#diversionPortOld").val() || '',
		diversionResOld : $("#diversionResOld").val() || '',
		diversionATDOld : $("#diversionATDOld").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/fdChange/saveDiversion",
		data : {
			'string' : JSON.stringify(json)
		},
		error : function() {
			layer.close(loading);
			layer.msg('操作失败！', {
				icon : 2
			});
		},
		success : function(data) {
			layer.close(loading);
			if (data.code == "0000") {
				layer.msg(data.msg, {
					icon : 1,
					time : 600
				}, function() {
					parent.saveSuccess();
				});
			}else if(data.code == "1000"){
				layer.alert(data.msg,{
					title:'警告',
					icon:7
				},function() {
					parent.saveSuccess();
				});
			}else {
				layer.msg(data.msg, {
					icon : 2,
					time : 2600
				});
			}
		}
	});
}

function checkForm(){
	var diversionResDetail = $("#diversionResDetail").val();
	if(diversionResDetail.length>30){
		layer.msg("详情不得超过30个字符",{icon:7});
		return false;
	}
	
	return true;
}
function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,  
//        width:"400px",
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
