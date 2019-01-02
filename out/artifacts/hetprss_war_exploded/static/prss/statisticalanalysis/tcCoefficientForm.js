var layer;
var form;
//var type;
//var editScope;//是否有权限进行新增和修改操作
$(document).ready(function() {
//	type=$("#type").val();
	layui.use([ "layer", "form", "element" ], function() {
		layer = layui.layer;
		form=layui.form;
		//是否有修改权限
//		var editPassengerT = $("#editPassengerT");
//		if(editPassengerT&&editPassengerT.length>0){
//			editScope=true;
//		}else{
//			//没有修改权限 整个表单只读
//			$('#tableForm').find('input,textarea').attr("readonly","readonly");
//			editScope=false;
//		}
		//新增页面隐藏掉创建人 创建时间等
//		if("add"==$("#type").val()){
//			$("#hiddenDiv").hide();
//		}
		//监听提交
		form.on('submit(save)', function(data){
			var data= JSON.stringify($('#coefficientForm').serializeArray());
			$.ajax({
				type : 'post',
				async : false,
				url : ctx + "/tc/statistics/saveTcCoefficient",
				data:{formData:data},
				beforeSend:function() {
					loading = parent.layer.load(2, {
						shade : [ 0.3, '#000' ]// 0.1透明度
					});
				},
				error : function() {
					layer.close(loading);
					parent.layer.close(loading);
					layer.msg('保存失败！', {
						icon : 2
					});
				},
				success : function(data) {
					layer.close(loading);
					parent.layer.close(loading);
					if(data.code=="0000"){
						layer.msg(data.msg, {
							icon : 1,
							time : 1500
						}, function() {
								try{
									parent.saveSuccess();
								}catch(ex){
									
								}
						});
					}else{
						layer.msg(data.msg, {
							icon : 2
						});
					}
				}
			});
//			}else{
//				layer.msg("当前用户没有操作权限", {
//					icon : 4
//				});
//			}
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		form.verify({
			  numberByCus: function(value,item){
				  if(isNaN(value)){
					  return '值非法！';
				  }
			  }
		}); 
	});

});
function save(){
	$("#submitBtn").trigger("click");
}

//function checkNumber(number){
//	var reg =/^([0-9]\d*|0)$/;
//	return reg.test(number);
//}
//$.fn.serializeObject = function() {  
//    var o = {};  
//    var a = this.serializeArray();  
//    $.each(a, function() {  
//        if (o[this.name]) {  
//            if (!o[this.name].push) {  
//                o[this.name] = [ o[this.name] ];  
//            }  
//            o[this.name].push(this.value || '');  
//        } else {  
//            o[this.name] = this.value || '';  
//        }  
//    });  
//    return o;  
//}