var layer;
var form;
var type;
$(document).ready(function() {
	type=$("#type").val();
	layui.use([ "layer", "form", "element" ], function() {
		layer = layui.layer;
		form=layui.form;
		form.on('select(valueChange)', function(data){
			 getFltInfo();
		});
		form.on('submit(save)', function(data){
				$("#tableForm").ajaxSubmit({
					async : false,
					beforeSubmit : function() {
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
			
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		form.verify({			
			  fltTime: function(value, item){ //value：表单的值、item：表单的DOM对象
				    if(value!=''&&(!checkFltTime(value))){
					      return '时间格式错误';
					    }
				}
		}); 
		//初始化新增和修改页面
		if(type=="edit"){
			//关于航班的一切属性都不能改
			$("#flightNumber").attr('readonly','readonly');
			$("#flightNumber").removeAttr("onblur");
			$("#flightDate").attr('readonly','readonly');
			$("#flightDate").removeAttr("onblur");
			$("#flightDate").removeAttr("onclick");
			$("#inOutFlag").attr('disabled',true);
			$("#fltAttr").attr('disabled',true);
			$("#fltType").attr('disabled',true);
			form.render();
		}else if(type=="add"){
			
		}
	});

});
function getFltInfo(){
	var flightDate=$("#flightDate").val();
	var flightNumber=$("#flightNumber").val();
	var inOutFlag=$("#inOutFlag").val();
	if(flightNumber!=""&&flightDate!=""){
		emptyInput();
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/produce/inPick/getFltInfo",
			data : {
				flightNumber:flightNumber,
				flightDate:flightDate,
				inOutFlag:inOutFlag
			},
			success : function(data) {
				if (data.code == "0001") {
					var fltinfo=data.data;
					$("#aln3code").val(fltinfo.ALN_3CODE);
					$("#inOutFlag").val(fltinfo.IN_OUT_FLAG.substring(0,1));
					$("#actstandCode").val(fltinfo.ACTSTAND_CODE);
					$("#sta").val(fltinfo.STA.substring(fltinfo.STA.length-5));
					$("#eta").val(fltinfo.ETA.substring(fltinfo.ETA.length-5));
					$("#aircraftNumber").val(fltinfo.AIRCRAFT_NUMBER);
					$("#fltid").val(fltinfo.FLTID);
					$("#acttypeCode").val(fltinfo.ACTTYPE_CODE);
					$("#route").val(fltinfo.ROUTE);
					form.render();
				} else {
					layer.msg(data.msg, {
						icon : 2,
						time : 3000
					});
				}
			}
		});
	}
}
function save(){
	$("#submitBtn").trigger("click");
}
//清空自动带出的值
function emptyInput(){
	$("#aln3code").val("");
	$("#inOutFlag").val("");
	$("#actstandCode").val("");
	$("#sta").val("");
	$("#eta").val("");
	$("#aircraftNumber").val("");
	$("#fltid").val("");
	$("#acttypeCode").val("");
	$("#route").val("");
}

function checkFltTime(str){
	var reg = /^\d{4}$/;
	if(reg.test(str)){
			var hh = Number(str.substring(0,2));
			var mi = Number(str.substring(2,4));
			if(hh<0||hh>23){
				return false;
			}
			if(mi<0||mi>59){
				return false;
			}
	} else {
		return false;
	}
	return true;
}

/**
 * 下载附件
 */
function downFile(obj){
    var downFileInfo = obj.value;
    if(downFileInfo!='undefined'&&downFileInfo!=null&&downFileInfo!=''){
        $("#downFileInfo").val(downFileInfo);
        $("#downFileForm").submit();
    }
}