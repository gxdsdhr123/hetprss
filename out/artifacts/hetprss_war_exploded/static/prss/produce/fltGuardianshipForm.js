var layer;// 初始化layer模块
var form;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		form=layui.form;
		form.on('select(flightType)', function(data){
			searchFlightDetail();
		});
		
		form.on('select(operator)', function(data){
			$("#operatorName").val(data.elem[data.elem.selectedIndex].text);
		});
		
		//根据operateType添加控件是否可以编辑
		if(operateType=="modify"){
			//日期、航班号、飞机号、机位
			$("#flightNumber").attr("disabled","disabled");
			$("#flightDate").attr("disabled","disabled");
			$("#aircraftNumber").attr("disabled","disabled");
			$("#actstandCode").attr("disabled","disabled");
			$("#inOutFlag").attr("disabled","disabled");
			form.render();
		}else{
			$("#aircraftNumber").attr("disabled","disabled");
			$("#actstandCode").attr("disabled","disabled");
			form.render();
		}
	});
	
	//根据航班号和航班日期填充其它表格
	$("#flightNumber").blur(function(){
		searchFlightDetail();
	});
	$("#flightDate").blur(function(){
		searchFlightDetail();
	});
});

function searchFlightDetail(){
	var flightNumber = $("#flightNumber").val();
	var flightDate = $("#flightDate").val();
	var inOutFlag = $("#inOutFlag").val();
	if(flightNumber==''||flightDate==''||inOutFlag==''||flightNumber==null||flightDate==null||inOutFlag==null){
		return false;
	}
	$.ajax({
        type: "POST",
        url: ctx + '/produce/fltGuardianship/flightDetail',
        data: {
        	"flightNumber":flightNumber,
        	"flightDate":flightDate,
        	"inOutFlag":inOutFlag
        },
        dataType: "json",
        success: function(reFltDetailData){
        	//将航班相关详细信息自动填入、选中
        	if(reFltDetailData.result=="found"){
        		var fltDetailData = reFltDetailData.fltDetailData;
        		$("#aircraftNumber").val(fltDetailData.aircraftNumber);
        		$("#aircraftNumberReal").val(fltDetailData.aircraftNumber);
            	$("#actstandCode").val(fltDetailData.actstandCode);
            	$("#actstandCodeReal").val(fltDetailData.actstandCode);
            	$("#fltId").val(fltDetailData.fltId);
            	$("#aln2code").val(fltDetailData.aln2code);
            	$("#acttypeCode").val(fltDetailData.acttypeCode);
            	$("#aln3code").val(fltDetailData.aln3code);
            	$("#inOutFlagReal").val(fltDetailData.inOutFlag);
            	form.render();
        	}else{
        		$("#aircraftNumber").val("");
        		$("#aircraftNumberReal").val("");
        		$("#actstandCode").val("");
        		$("#actstandCodeReal").val("");
        		$("#fltId").val("");
        		$("#aln2code").val("");
        		$("#acttypeCode").val("");
        		$("#aln3code").val("");
        		$("#inOutFlagReal").val("");
        		form.render();
        		layer.alert("没有找到相关的航班信息！", {icon: 2,time:3000});
        	}
        }
    });
}

function doSave(){
	var flightNumber = $("#flightNumber").val();
	var flightDate = $("#flightDate").val();
	var inOutFlag = $("#inOutFlag").val();
	if(flightNumber==''||flightNumber==null){
		layer.msg('航班号不能为空！', {icon : 2});
		return false;
	}
	if(flightDate==''||flightDate==null){
		layer.msg('日期不能为空！', {icon : 2});
		return false;
	}
	if(inOutFlag==''||inOutFlag==null){
		layer.msg('未选择航班类型！', {icon : 2});
		return false;
	}
	
	//地面服务部
	var dmfwNum = $("#dmfwNum").val();
	if(dmfwNum!=''&&dmfwNum!=null&&(!checkNumber(dmfwNum))){
		layer.msg('地面服务部,请输入数字！', {icon : 2});
		return false;
	}
	//机务
	var jwNum = $("#jwNum").val();
	if(jwNum!=''&&jwNum!=null&&(!checkNumber(jwNum))){
		layer.msg('机务,请输入数字！', {icon : 2});
		return false;
	}
	//清洁
	var qjNum = $("#qjNum").val();
	if(qjNum!=''&&qjNum!=null&&(!checkNumber(qjNum))){
		layer.msg('清洁,请输入数字！', {icon : 2});
		return false;
	}
	//航务保障部
	var hwbzNum = $("#hwbzNum").val();
	if(hwbzNum!=''&&hwbzNum!=null&&(!checkNumber(hwbzNum))){
		layer.msg('航务保障部,请输入数字！', {icon : 2});
		return false;
	}
	//货运
	var hyNum = $("#hyNum").val();
	if(hyNum!=''&&hyNum!=null&&(!checkNumber(hyNum))){
		layer.msg('货运,请输入数字！', {icon : 2});
		return false;
	}
	//机组
	var jzNum = $("#jzNum").val();
	if(jzNum!=''&&jzNum!=null&&(!checkNumber(jzNum))){
		layer.msg('机组,请输入数字！', {icon : 2});
		return false;
	}
	
	$("#tableForm").ajaxSubmit({
		async : false,
		beforeSubmit : function() {
			loading = parent.layer.load(2, {
				shade : [ 0.3, '#000' ]// 0.1透明度
			});
		},
		success : function(data) {
			parent.layer.close(loading);
			if(data=='succeed'){
				layer.msg("保存成功！", {icon : 1,time : 2000},function(){
					parent.refreshBaseTable();
				});
			}else{
				layer.alert("保存失败！", {icon : 2,time : 2000});
			}
		}
	});
}

function checkNumber(number){
	var reg =/^[1-9]\d*$/;
	return reg.test(number);
}
