var layer;
var level = "1";
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
		
window.onload = function(){
	$("#airport").val($("#airportValue").val());
	$("#actType").val($("#actTypeValue").val());
	form.render(); //更新全部
	form.render('select'); 
}
function saveForm() {
	
	if(!checkForm()){
		return false;
	};
	var json = {
		id:$("#id").val() || '',
		airport : $("#airport").val() || '',
		actType : $("#actType").val() || '',
		standardFlightTime : $("#standardFlightTime").val() || '',
		driftValue : $("#driftValue").val() || '',
		beginFlightDate : $("#beginFlightDate").val() || '',
		endFlightDate : $("#endFlightDate").val() || '',
		calcFltTime: $("#calcFltTime").val() || ''
	}

	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/parameter/airborne/save",
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
			} else {
				layer.msg('操作失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	});
}

function checkForm(){
	var airport = $("#airport").val();
	if(airport==""){
		layer.msg("起飞机场不能为空",{icon:7});
		return false;
	}
	var actType = $("#actType").val();
	if(actType==""){
		layer.msg("机型不能为空",{icon:7});
		return false;
	}
	var standardFlightTime = $("#standardFlightTime").val();
	if(standardFlightTime==""){
		layer.msg("标准航程时间不能为空",{icon:7});
		return false;
		
	}else{
		if(!isPositiveInteger(standardFlightTime)){
			layer.msg("标准航程时间应为正整数或0",{icon:7});
			return false;
		}
	}
	
	
	var driftValue = $("#driftValue").val();
	if(driftValue!=""){
		if(!isPositiveInteger(driftValue)){
			layer.msg("浮动值应为正整数或0",{icon:7});
			return false;
		}
	}
	var calcFltTime = $("#calcFltTime").val();
	if(calcFltTime!=""){
		if(!isPositiveInteger(calcFltTime)){
			layer.msg("航程时间为正整数或0",{icon:7});
			return false;
		}
	}
	var beginFlightDate = $("#beginFlightDate").val();
	if(beginFlightDate==""){
		layer.msg("开始日期不能为空",{icon:7});
		return false;
	}
	var endFlightDate = $("#endFlightDate").val();
	if(endFlightDate==""){
		layer.msg("结束日期不能为空",{icon:7});
		return false;
	}
	return true;
	
	
}

function isPositiveInteger(time){//是否为正整数
	var re=/^[0]|[1-9]\d*$/;
    return re.test(time);
} 
