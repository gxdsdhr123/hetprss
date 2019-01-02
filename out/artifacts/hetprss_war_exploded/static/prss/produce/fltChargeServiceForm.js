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
		
		form.on('checkbox(addCleanCheckBox)', function(data){
			if(data.elem.checked){
				$("#addCleanReal").val("0");
			}else{
				$("#addCleanReal").val("1");
			}
		}); 
		
		form.on('checkbox(sewerageCheckBox)', function(data){
			if(data.elem.checked){
				$("#sewerageReal").val("0");
			}else{
				$("#sewerageReal").val("1");
			}
		});
		
		//根据operateType添加控件是否可以编辑
		if(operateType=="modify"){
			//日期、航班号、起场、机号、机型、进港实落、清洁人员
			$("#flightNumber").attr("disabled","disabled");
			$("#flightDate").attr("disabled","disabled");
			$("#aircraftNumber").attr("disabled","disabled");
			$("#acttypeCode").attr("disabled","disabled");
			$("#inOutFlag").attr("disabled","disabled");
			form.render();
		}else{
			$("#aircraftNumber").attr("disabled","disabled");
			$("#acttypeCode").attr("disabled","disabled");
			$("#addCleanReal").val("1");
			$("#sewerageReal").val("1");
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
        url: ctx + '/produce/fltChargeService/flightDetail',
        data: {
        	"flightNumber":flightNumber,
        	"flightDate":flightDate,
        	"inOutFlag":inOutFlag
        },
        dataType: "json",
        success: function(reFltDetailData){
        	console.info(reFltDetailData);
        	//将航班相关详细信息自动填入、选中
        	if(reFltDetailData.result=="found"){
        		var fltDetailData = reFltDetailData.fltDetailData;
        		$("#aircraftNumber").val(fltDetailData.aircraftNumber);
        		$("#aircraftNumberReal").val(fltDetailData.aircraftNumber);
            	$("#fltId").val(fltDetailData.fltId);
            	$("#aln2code").val(fltDetailData.aln2code);
            	$("#acttypeCode").val(fltDetailData.acttypeCode);
            	$("#acttypeCodeReal").val(fltDetailData.acttypeCode);
            	$("#aln3code").val(fltDetailData.aln3code);
            	$("#inOutFlagReal").val(fltDetailData.inOutFlag);
            	form.render();
        	}else{
        		$("#aircraftNumber").val("");
        		$("#acttypeCodeReal").val("");
        		$("#aircraftNumberReal").val("");
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
