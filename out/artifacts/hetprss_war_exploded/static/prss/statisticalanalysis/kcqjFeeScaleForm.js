var layer;
var form;
var type;
//var editScope;//是否有权限进行新增和修改操作
$(document).ready(function() {
	type=$("#type").val();
	layui.use([ "layer", "form", "element" ], function() {
		layer = layui.layer;
		form=layui.form;
	});
	
	initSelect();
	if(type=='edit'){
		//清空机型下拉
//		$("#actType").empty();
		//设置可选的机型列表
		$.each(actArr, function (n, value) {
			$("#actType").append('<option value="'+value.id+'" >'+value.text+'</option>');
		});
		$("#actType").trigger('change');
		//回显已选机型
		$("#actType").val(arrList).trigger("change"); 
	}
	$('#alnCode').change(function(){
		var currntSelect=$(this).children('option:selected').val();//当前selected的值
		var id=$("#id").val();
		//清空actType的select2
		$("#actType").empty();
		if(currntSelect){
			// 动态获取机型下拉列表
			$.ajax({
				type : 'post',
				async : false,
				url : ctx + "/kcqj/feeScale/getActType",
				data:{alnCode:currntSelect,
					 id:id},
				error : function() {
					layer.close(loading);
					parent.layer.close(loading);
					layer.msg('获取机型下拉项失败！', {
						icon : 2
					});
				},
				success : function(data) {
					$.each(data, function (n, value) {
						$("#actType").append('<option value="'+value.id+'" >'+value.text+'</option>');
					 });
					$("#actType").trigger('change');
				}
			});
		}		
	});

});
function save(){
	var formData=$("#kcqjForm").serializeObject();
	var actType=formData.actType;
	if(actType&&!Array.isArray(actType)){
		actType=new Array();
		actType[0]=formData.actType;//如果用户只选择一个机型，那么serializeObject（）方法会当成字符串处理  后台会报错
		formData.actType=actType;//所以不管机型选择多少个  都要转成数组传过去
	}
	if(checkFormData(formData)){
		formData=JSON.stringify(formData);
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/kcqj/feeScale/save",
			data:{formData:formData,
				  type:type},
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
	}else{
		layer.msg("费用应为数字", {
			icon : 2
		});
	}
}
function checkFormData(data){
	if(data.alnCode==''){
		layer.msg("航空公司不能为空", {
		icon : 2
		});
		return false;
	}
	if(!data.actType){
		layer.msg("机型不能为空", {
		icon : 2
		});
		return false;
	}
	if(data.hqCharges!=''&&!checkNumber(data.hqCharges)){
		return false;
	}else if(data.hhCharges!=''&&!checkNumber(data.hhCharges)){
		return false;
	}else if(data.gzCharges!=''&&!checkNumber(data.gzCharges)){
		return false;
	}else{
		return true;
	}
}

function checkNumber(number){
//	var reg =/^([0-9]\d*|0)$/;
//	return reg.test(number);
	return !isNaN(number);
}
//初始化下拉选
function initSelect() {
	$('#alnCode').select2({
		placeholder : "请选择",
		width : "100%",
		language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
	});
	$('#actType').select2({
		placeholder : "多选，请选择",
		width : "100%",
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

$.fn.serializeObject = function() {
    var o = {};  
    var a = this.serializeArray();  
    $.each(a, function() {  
        if (o[this.name]) {  
            if (!o[this.name].push) {  
                o[this.name] = [ o[this.name] ];  
            }  
            o[this.name].push(this.value || '');  
        } else {  
            o[this.name] = this.value || '';  
        }  
    });  
    return o;  
}