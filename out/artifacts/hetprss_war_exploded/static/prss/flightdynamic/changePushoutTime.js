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
		oldValue : $("#oldValue").val() || '',
		newValue : parseDate($("#date").val(),$("#newValue").val()) || ''
	}
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/fdChange/savePushoutTime",
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
			} else if(data.code == "1000"){
				layer.alert(data.msg,{
					title:'警告',
					icon:7
				},function() {
					parent.saveSuccess();
				});
			}else {
				layer.msg('操作失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	});
}
/**
 * checkForm()
 * @returns {Boolean}
 * @description:
 * 1 如果time为空  -->旧值为空时 提示值未改变；旧值不为空 将旧值update为空
 * 2 如果date为空 time不为空 -->提示日期不能为空
 * 3 如果time date 都为空--> 旧值为空时 提示值未改变；旧值不为空 将旧值update为空
 */
function checkForm(){
	var newValue = $("#newValue").val();
	var oldValue = $("#oldValue").val();
	var date=$("#date").val();
	//如果时间不为空，校验合法性
	if(newValue){
		if(date){
			//校验所填新值的时间格式是否合法
			if(!checkInputTime(newValue)){
				layer.msg("时间格式不合法",{icon:7});
				return false;
			}
		}else{
			layer.msg("日期不能为空",{icon:7});
			return false;
		}
		
	}
		if(parseDate(date,newValue)==oldValue){
			layer.msg("值未改变",{icon:7});
			return false;
		}
	return true;
}
function checkInputTime(value){
	var reg = new RegExp("[0-9]{4}");
	if(reg.test(value)){
		if(value.length!=4){
			return false;
		}else{
			var hh = Number(value.substring(0,2));
			var mi = Number(value.substring(2,4));
			if(hh<0||hh>23){
				return false;
			}
			if(mi<0||mi>59){
				return false;
			}
		}
	}else{
		return false;
	}
	return true;
}
function parseDate(date,time){
	var datestr='';
//	日期和时间都有值的时候 才去拼接字符串  否则就是空串
	if(date&&time){
		var year=date.substring(0,4);
		var month=date.substring(4,6);
		var day=date.substring(6,8);
		var hh=time.substring(0,2);
		var mi=time.substring(2,4);
		datestr=year+'-'+month+'-'+day+' '+hh+':'+mi;
	}
	return datestr;
}
