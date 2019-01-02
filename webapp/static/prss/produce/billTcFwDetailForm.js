var layer;// 初始化layer模块
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
	if(operateType=="modify"){
		//修改操作不可编辑项
		$("#flightDate").attr("disabled","disabled");
		$("#inFlightNumber").attr("disabled","disabled");
		$("#outFlightNumber").attr("disabled","disabled");
	}else{
		//初始选择日期
		$("#flightDate").val(getFormatDate(0));
		//新增获取服务单号
		getServiceNumber();
	}
	
	//根据航班号和航班日期填充其它表格
	$("#inFlightNumber").blur(function(){
		searchFlightDetail();
	});
	$("#outFlightNumber").blur(function(){
		searchFlightDetail();
	});
});

/**
 * 获取航班信息
 */
function searchFlightDetail(){
	var flightDate = $("#flightDate").val();
	var inFlightNumber = $("#inFlightNumber").val();
	var outFlightNumber = $("#outFlightNumber").val();
	
	if((inFlightNumber==''||inFlightNumber==null)&&(outFlightNumber==''||outFlightNumber==null)){
		layer.msg('请填写进港或出港航班号！', {icon : 1});
		return false;
	}
	
	if(flightDate==''||flightDate==null){
		layer.msg('请选择航班日期！', {icon : 1});
		return false;
	}
	$.ajax({
        type: "POST",
        url: ctx + '/produce/billTcFw/flightDetail',
        data: {
        	"flightDate":flightDate,
        	"inFlightNumber":inFlightNumber,
        	"outFlightNumber":outFlightNumber
        },
        dataType: "json",
        success: function(refltDetailData){
        	//将航班相关详细信息自动填入、选中
        	if(refltDetailData.result=="found"){
        		var fltDetailData = refltDetailData.fltDetailData;
        		//显示信息
        		$("#airlineDescription").text(fltDetailData.airlineDescription);
        		$("#flightDate").val(fltDetailData.flightDate);
            	$("#inFlightNumber").val(fltDetailData.inFlightNumber);
            	$("#outFlightNumber").val(fltDetailData.outFlightNumber);
            	$("#inFlightDate").val(fltDetailData.inFlightDate);
            	$("#outFlightDate").val(fltDetailData.outFlightDate);
            	$("#aircraftNumber").text(fltDetailData.aircraftNumber);
            	$("#acttypeCode").text(fltDetailData.acttypeCode);
            	$("#operatorName").text(fltDetailData.operatorName);
            	//显示信息对应值
            	$("#inFltId").val(fltDetailData.inFltId);
            	$("#outFltId").val(fltDetailData.outFltId);
            	$("#inFlightDate").val(fltDetailData.inFlightDate);
            	$("#outFlightDate").val(fltDetailData.outFlightDate);
        	}else if(refltDetailData.result=="notfound"){
        		$("#airlineDescription").text("");
            	$("#inFlightNumber").val("");
            	$("#outFlightNumber").val("");
            	$("#inFlightDate").val("");
            	$("#outFlightDate").val("");
            	$("#aircraftNumber").text("");
            	$("#acttypeCode").text("");
            	$("#operatorName").text("");
            	$("#inFltId").val("");
            	$("#outFltId").val("");
            	$("#inFlightDate").val("");
            	$("#outFlightDate").val("");
        		layer.msg("没有找到相关的航班信息！", {icon: 1,time:3000});
        	}else if(refltDetailData.result=="allreadyHaveBill"){
        		layer.msg("已存在此航班的特车计费单记录！", {icon: 1,time:3000});
        	}else{
        		layer.alert("获取航班信息出现问题，请联系管理员！", {icon: 1,time:3000});
        	}
        }
    });
}

/**
 * 保存特车计费单信息
 */
function doSave(){
	var flightDate = $("#flightDate").val();
	var inFlightNumber = $("#inFlightNumber").val();
	var outFlightNumber = $("#outFlightNumber").val();
	
	if(flightDate==''||flightDate==null){
		layer.msg('日期不能为空！', {icon : 2});
		return false;
	}
	
	if((inFlightNumber==''||inFlightNumber==null)&&(outFlightNumber==''||outFlightNumber==null)){
		layer.msg('进出港航班号不能同时为空！', {icon : 2});
		return false;
	}
	$("#tableForm").ajaxSubmit({
		beforeSubmit : function() {
			loading = layer.load(2, {
				shade : [ 0.3, '#000' ]// 0.1透明度
			});
		},
		success : function(data) {
			if(data=='success'){
				layer.msg("保存特车计费单记录成功！", {icon : 1,time : 2000},function(){
					layer.close(loading);
					parent.refreshBaseTable();
				});
			}else if(data=='non'){
				layer.close(loading);
				layer.alert("没有保存特车计费单记录！", {icon : 2,time : 2000});
			}else{
				layer.close(loading);
				layer.alert("保存特车计费单记录异常，请联系管理员！", {icon : 2,time : 2000});
			}
		},
		error : function(e) {
			parent.layer.close(loading);
		}
	});
}

/**
 * 获取服务单号
 */
function getServiceNumber(){
	$.ajax({
        type: "GET",
        url: ctx+"/produce/billTcFw/serviceNumber",
        success: function(serviceNumber){
        	$("#serviceNumberShow").text(serviceNumber);
        	$("#serviceNumber").val(serviceNumber);
        }
    });
}

/*
 * 获取日期
 */
function getFormatDate(days){
    var date = new Date();
	date.setDate(date.getDate() + days);
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	return year+""+month+""+day;
}

/**
 * 打印特车计费单
 */
function printExcel(){
	layer.confirm("确定已经先保存了此特车计费单吗？", {btn: ['确定', '取消']}, function (index) {
		//获取选中特车计费单
		var ids = $("#id").val();
		var fltDate = $("#flightDateUnedit").val();
		$("#exportForm").attr("action",ctx + "/produce/billTcFw/printExcel?ids="+ids+"&fltDate="+fltDate);
		$("#exportForm").submit();
		layer.close(index);
	});
}