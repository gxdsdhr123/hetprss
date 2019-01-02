var layer;// 初始化layer模块
$(function() {
	
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	$(".select2").select2({
		placeholder : "请选择",
		width : "100%",
		language : "zh-CN"
	});
	
	//修改页面清洁员工回显
	$("#cleaners").val(eval('['+$("#hidClean").val()+']')).trigger("change");
});
//保存单据
function DoSave(){
	var tag = 0;
	var airline = $("#airline").val();
	var fltStatus = $("#fltStatus").val();
	var flightDate = $("#flightDate").val();
	var flightNum = $("#flightNum").val();
	var AirNum = $("#AirNum").val();
	var atcType = $("#atcType").val();
	var fltTime = $("#fltTime").val();
	var remark = $("#remark").val();
	
	var date = new Date();
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	console.info(year+month+day);
	
	if(airline == "" || airline == null){
		layer.msg('请选择航空公司！', {
			icon : 2
		});
		tag = 1;
	}else if(flightDate == "" || flightDate == null){
		layer.msg('请填写航班日期', {
			icon : 2
		});
		tag = 1;
	}else if((parseInt(year+month+day)-parseInt(flightDate))>1){
		layer.msg('不允许修改或保存历史单据！', {
			icon : 2
		});
		tag = 1;
	}else if(fltStatus == "" || fltStatus == null){
		layer.msg('请选择航班性质', {
			icon : 2
		});
		tag = 1;
	}else if(flightNum == "" || flightNum == null){
		layer.msg('该填写航班号！', {
			icon : 2
		});
		tag = 1;
	}else if(AirNum == "" || AirNum == null){
		layer.msg('该填写飞机号！', {
			icon : 2
		});
		tag = 1;
	}else if(atcType == "" || atcType == null){
		layer.msg('该填写机型！', {
			icon : 2
		});
		tag = 1;
	}
	
	if(tag == 0){
		$("#tableForm").submit();
		return tag;
	}else{
		return tag;
	}
}
