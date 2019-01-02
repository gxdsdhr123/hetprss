$(function(){
	layui.use('form');
})
var index = parent.layer.getFrameIndex(window.name);


function submitCheck() {

	var tcode = $("input[name=tcode]").val();
	var tname = $("input[name=tname]").val();
	var disc = $("input[name=disc]").val();
	
	var fu = new ChkUtil();
	var res = '';
	res = fu.checkFormInput(tcode, true,'',10);
	if (res.length > 0) {
		var msg = '[类型代码]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("tcode").focus();
		return false;
	}
	res = fu.checkFormInput(tname, true,'',50);
	if (res.length > 0) {
		var msg = '[类型名称]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("tname").focus();
		return false;
	}
	
	res = fu.checkFormInput(disc, true,'',100);
	if (res.length > 0) {
		var msg = '[描述]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("disc").focus();
		return false;
	}
	return true;
}
var save = function (){
	var id = $("input[name=id]").val();
	var flag = $("input[name=flag]").val();
	var tcode = $("input[name=tcode]").val();
	var tname = $("input[name=tname]").val();
	var ifreply = $("input[name=ifreply]").prop("checked");
	var ifflight = $("input[name=ifflight]").prop("checked");
	var disc = $("input[name=disc]").val();
	var check = submitCheck();
	if(check){
	  	$.ajax({
			type:'post',
			data:{
				id: id, 
				flag: flag,
				tcode : tcode,
				tname : tname,
				ifreply : ifreply==true?1:0,
				ifflight : ifflight==true?1:0,
				disc : disc
			},
			url:ctx+"/message/type/save",
			dataType:"text",
			success:function(result){
				if(result == "操作成功"){
					parent.layer.close(index);
					parent.layer.msg(result, {icon: 1,time: 1000});
					parent.refresh();
				} else {
					layer.msg(result, {icon: 2,time: 1000});
				}
			},
			error:function(msg){
				var result = "操作失败";
				layer.msg(result, {icon: 2,time: 1000});
			}
		});
	}
}
