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
		
		//初始化新增和修改页面
		if(type=="edit"){
			//关于航班的一切属性都不能改
			$("#flightNumber").attr('readonly','readonly');
			$("#flightNumber").removeAttr("onblur");
			$("#flightDate").attr('readonly','readonly');
			$("#flightDate").removeAttr("onblur");
			$("#flightDate").removeAttr("onclick");
			$("#inOutFlag").attr('disabled',true);
			form.render();
		}else if(type=="add"){
			
		}
	});

});
function getFltInfo(){
	var flightNumber=$("#flightNumber").val();
	var flightDate=$("#flightDate").val();
	var inOutFlag=$("#inOutFlag").val();
	if(flightNumber!=""&&flightDate!=""){
		emptyInput();
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/produce/deicing/getFltInfo",
			data : {
				flightNumber:flightNumber,
				flightDate:flightDate,
				inOutFlag:inOutFlag
			},
			success : function(data) {
				if (data.code == "0001") {
					var fltinfo=data.data;
					$("#aln2code").val(fltinfo.ALN_2CODE);
					$("#aln3code").val(fltinfo.ALN_3CODE);
					$("#inOutFlag").val(fltinfo.IN_OUT_FLAG.substring(0,1));
					$("#inOutFlagReal").val(fltinfo.IN_OUT_FLAG);
					$("#arrivalTime").val(fltinfo.ETD);
					$("#departTime").val(fltinfo.ETA);
					$("#aircraftNumber").val(fltinfo.AIRCRAFT_NUMBER);
					$("#station").val(fltinfo.STATION);
					$("#fltid").val(fltinfo.FLTID);
					$("#acttypeCode").val(fltinfo.ACTTYPE_CODE);
					$("#airlineShortName").val(fltinfo.AIRLINE_SHORTNAME);
					$("#ata").val(fltinfo.ATA);
					$("#propertyName").val(fltinfo.PROPERTY_NAME);
					$("#routeName").val(fltinfo.ROUTE_NAME);
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
	$("#aln2code").val("");
	$("#aln3code").val("");
	$("#inOutFlag").val("");
	$("#arrivalTime").val("");
	$("#departTime").val("");
	$("#aircraftNumber").val("");
	$("#station").val("");
	$("#fltid").val("");
	$("#acttypeCode").val("");
	$("#airlineShortName").val("");
	$("#ata").val("");
	$("#propertyName").val("");
	$("#routeName").val("");
	
}

function checkNumber(number){
	var reg =/^([0-9]\d*|0)$/;
	return reg.test(number);
}