var layer;
var form;
$(document).ready(function() {
	layui.use([ "layer", "form", "element" ], function() {
		layer = layui.layer;
		form = layui.form;
		form.render(); //更新全部
		form.render('select'); 

	})
});
window.onload = function(){
	$("#ACTSTAND_CODE1").val($("#code1").val());
	$("#AIRCRAFT_TYPE1").val($("#type1").val());
	$("#ACTSTAND_CODE2").val($("#code2").val());
	$("#AIRCRAFT_TYPE2").val($("#type2").val());
	form.render(); //更新全部
	form.render('select'); 
}



function saveForm() {
	var time = $("#TIME_MIN").val();
	if(!isPositiveInteger(time)){
		layer.msg("互斥时长应为0或正整数",{icon:7});
		return false;
	}
	if(!required()){
		layer.msg("存在未填写的项目",{icon:7});
		return false;
	}
	
	var data=getFormData();
	
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/stand/parkingMutex/save",
		data : {
			'formData' : JSON.stringify(data)
		},
		error : function() {
			layer.close(loading);
			layer.msg('保存失败！', {
				icon : 2
			});
		},
		success : function(result) {
			layer.close(loading);
			if(result.code=="0000"){
				layer.msg(result.msg,
						{icon:1},
						function() {
							parent.saveSuccess();
						});
			}else{
				layer.msg(result.msg,{icon:2});
			}
		}
	});
}


function isPositiveInteger(time){//是否为正整数
	var re=/^[0]|[1-9]\d*$/;
    return re.test(time);
} 

function required(){
	if($("#TIME_MIN").val()==""|| $("#ACTSTAND_CODE1").val()==""||
			$("#ACTSTAND_CODE2").val()==""||$("#AIRCRAFT_TYPE1").val()==""||
			$("#AIRCRAFT_TYPE2").val()==""){
		return false;
	}else{
		return true;
	}
}

function getFormData(){
	
	var json = {
			ID:$("#ID").val() || '',
			TIME_MIN : $("#TIME_MIN").val() || '',
			ACTSTAND_CODE1 : $("#ACTSTAND_CODE1").val() || '',
			ACTSTAND_CODE2 : $("#ACTSTAND_CODE2").val() || '',
			AIRCRAFT_TYPE1 : $("#AIRCRAFT_TYPE1").val() || '',
			AIRCRAFT_TYPE2 : $("#AIRCRAFT_TYPE2").val() || ''
		}
	
	return json;
}


