var layer;
var form;
layui.use(["layer","form"],function(){	
	layer = layui.layer;
	form = layui.form;
	form.verify({
		maxlength:function(value,item){
			var maxLength=$(item).attr("max_length");
			var name = $(item).attr("alt");
			if(value.gblen()>maxLength){
				if(maxLength%2==0){
					return name+"长度最长不能超过"+maxLength+"个字符或"+maxLength/2+"个汉字";
				}else{
					return name+"长度最长不能超过"+maxLength+"个字符或"+(maxLength-1)/2+"个汉字";
				}
			}
		},
		regValidate:function(value,item){
			var regValidate=$(item).attr("reg-validate");
			var name = $(item).attr("alt");
			if(!value.match(regValidate)){
				return name+"的格式错误，请输入正确的格式";
			}
		}
	});
	var loading = null;
	form.on('submit(add)', function(data){
		if(vertifySelect2()){
			$("#addForm").ajaxSubmit({
				async : false,
				beforeSubmit : function() {
					loading = parent.layer.load(2, {
						shade : [ 0.3, '#000' ]// 0.1透明度
					});
				},
				error : function() {
					layer.close(loading);
					layer.msg('保存失败！', {
						icon : 2
					});
				},
				success : function(data) {
					parent.layer.close(loading);
					var msg="保存成功！";
					if(data!="1"){
						if(data=="0"){
							msg="保存失败！";
						}else{
							msg=data;
						}
					}
					
					layer.msg(msg, {
						icon : 7,
						time : 1500
					}, function() {
						if(data=="1"){
							layer.close(parent.addWin);
							try{
								parent.doQuery(1);
							}catch(ex){
								
							}
						}
					});
				}
			});
		}
		
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
	//隐藏layui生成部分
	$.each($("select[selectType='select2']"),function(k,v){
		$(v).next().hide();
	});
});
$(document).ready(function() {
	var divH=$(window).height()-20;
	$('#formDiv').css("height",divH);
	new PerfectScrollbar($("#formDiv")[0]);
	//输入框值转换大写
	$("#addForm input:text").each(function(){
		var input = $(this);
		var transform = input.css("text-transform");
		if(transform&&transform.toUpperCase()=="UPPERCASE"){
			input.keyup(function(){
				this.value=this.value.toUpperCase();
			});
		}
	});
})

function plus(colNameEn,obj){
	var parent = $(obj).parent();
	var col = $("#" + colNameEn).clone();
	col.val('');
	parent.append('&nbsp;');
	parent.append(col);
}
function minus(colNameEn,obj){
	var rows = $("input[name=" + colNameEn+"]");
	if(rows.length>1){
		console.info($(rows[rows.length-1]))
		$(rows[rows.length-1]).remove();
	}
}
function doSubmit(){
	$("#submitBtn").trigger("click");
}