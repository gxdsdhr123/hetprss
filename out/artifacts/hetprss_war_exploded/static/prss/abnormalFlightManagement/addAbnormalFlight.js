var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

var table = $('#createDetailTable');
var createDetailColumns = [];
var searchData = "";
var searchTime = "";
var daystr = "";
var loading;

$(function() {
	var sign = $('#sign').val();
//	// 默认选中当前时间
//	var daystr = getOperaDate('flight');
//	if("" == $('#flightDate').val() || null == $('#flightDate').val()) {
//		$('#flightDate').val($(".flightDate").val());
//	}
	
	// 初始化下拉选
	initSelect();
	
	// 查看 - 所有输入框禁止操作
	if(sign == 'show') {
		$('input').prop('disabled', 'true');
		$('textarea').prop('disabled', 'true');
		$('select').prop('disabled', 'true');
	}
	// 初始化默认选中
	if("" != $('#officeids').val()) {
		var officeid = $('#officeids').val().split(",");
		// 初始化多选框
		$("#sendDept").val(officeid).trigger('change');
		// 初始化单选框
		var radioChecked = $('#infoSourceChecked').val();
		if(radioChecked == "航空公司" || radioChecked == "机场" || radioChecked == "现场") {
			$("input[type=radio][name=infoSource][value='" + radioChecked + "']").prop("checked", true);	
			$('#infoSourceInfo').val('');
			$('#infoSourceInfo').prop('disabled', 'true');
			$('#infoSourceInfo').siblings().attr('readonly', 'true');
		} else {
			$("input[type=radio][name=infoSource][value='']").prop("checked", true);
			$('#infoSourceInfo').val(radioChecked);
		}
	}
//	// 初始化索搜时间
//	if($('#flightDate').val() != '') {		
//		$('#flightDate').val(daystr);
//	}
	// 初始化运控当天日期
	if(sign == 'add') {
		$('#operDate').val(getOperaDate('create'));
	}
	// 反馈报告
	if(sign == 'rep') {
		$('input').prop('disabled', 'true');
		$('select').prop('disabled', 'true');
		if('' == $('input[name=operDate1]').val()) {
			$('input[name=operDate1]').val(getOperaDate('create'));
		}
		$('#sendDeptContent').attr({"style":"display:none;"});
		$('#cmdContent').prop('disabled', 'true');
		$('#remark').prop('disabled', 'true');
		if($("#feedBackContent").val() != ''){
			$('#feedBackContent').prop('disabled', 'true');
		}
	}
	// CDM判责
	if(sign == 'cdm') {
		$('input').prop('disabled', 'true');
		$('select').prop('disabled', 'true');
		if('' == $('input[name=operDate2]').val()) {
			$('input[name=operDate2]').val(getOperaDate('create'));
		}
		$('#sendDeptContent').attr({"style":"display:none;"});
		$('textarea[name=deptFeedBackContent]').prop('disabled', 'true');
		$('#remark').prop('disabled', 'true');

	}
	
	$('#searchBut').on('click',function(){
		loading = layer.load(2);
		getAbnormalFlightData();
	})
	
//	// 光标离开触发
//	$('#flightNumber').blur(function() {
//		getAbnormalFlightData();
//	});
//	// 回车触发
//    $('#flightNumber').on('keypress',function(event){ 
//        if(event.keyCode == 13) {  
//        	getAbnormalFlightData();
//        }  
//    });
    
    $('#flightDate').bind('input propertychange', function() {  
        $('#result').html($(this).val().length + ' characters');  
    });       

});
// 改变时间触发加载数据
function changeSelectTime() {
	var flightNumber = $('#flightNumber').val();
	if("" == flightNumber.trim() || null == flightNumber) {
		return false;
	}
	var flightDate = $('#flightDate').val();
	if("" == flightDate.trim() || null == flightDate) {
		clearAnbormalForm();
		searchTime = "";
		return false;
	}
	if(searchTime == flightDate) {
		return false;
	} else {
		searchData = "";		
	}
	getAbnormalFlightData();
}
// 获取航班数据
function getAbnormalFlightData() {
	
	var flightNumber = $('#flightNumber').val();
	if("" == flightNumber.trim() || null == flightNumber) {
		clearAnbormalForm();
		layer.msg('请输入航班号！', {icon : 7, time : 1000});
		layer.close(loading);
		return;
	}
	var flightDate = $('#flightDate').val();
	if("" == flightDate.trim() || null == flightDate) {
		clearAnbormalForm();
		layer.msg('请输入航班日期！', {icon : 7, time : 1000});
		layer.close(loading);
		return;
	}

	$.ajax({
		type : 'post',
		url : ctx + "/abnormal/abnormalFlightManagement/addAbnormalFlightInfo",
		//async:false,
		data : {
			flightNumber : flightNumber,
			flightDate : flightDate
		},
		error:function(){
			layer.msg('请求错误！', {icon : 7, time : 1000});
			layer.close(loading);
		},
		success : function(msg) {
			searchData = flightNumber;
			searchTime = flightDate;
			var list = eval(msg);
			if(list.length != 0) {
				dealAbnormalFlightData(list[0])
			} else {
				layer.msg('该航班不存在不正常信息！', {icon : 7, time : 1000});
				$("#flightNumber").val('');
				clearAnbormalForm();
			}
			layer.close(loading);
		}
	});
}


// 表单赋值
function dealAbnormalFlightData(data) {
	console.log(data);
	 for(var o in data){  
		$('input[id=' + o + "]").val(data[o]);
		if(o == "AIRCRAFT_NUMBER") {
			$('#aircraftNumber').val(data[o]);
			
		}
	 }  
}
// 清空赋值
function clearAnbormalForm() {
	$('.content-c').each(function(i, e) {
		$(e).val('');
	});
}
// 初始化下拉选
function initSelect() {
	$('#sendDept').select2({
		placeholder : "请选择部门",
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
// 获取该表单数据
function getFormData() {
	return $("#abnormalForm").serialize();
}
// 获取表单radio选中内容
function getRadioChecked() {
	return $('input:radio:checked').val()
}
// 弹出消息
function alertInfo(msg) {
	layer.msg(msg, {icon : 7, time : 800});
}
// 清空消息来源选框
function clearInfoSourceInfo() {
	$('#infoSourceInfo').val('');
}
// 其他类型的消息来源赋值
function setSourceInfo() {
	if("" == $('#infoSourceInfo').val() || null == $('#infoSourceInfo').val()) {
		alertInfo("请填写其他消息内容");
		return false;
	}
	$('#infoSource').val($('#infoSourceInfo').val());
	return true;
}
// 获取数据是否为空
function getFormCheckNull() {
	if(null == $('#flightNumber').val() || "" == $('#flightNumber').val()) {
		return false;
	}
	return true;
}
// 	获取日期， 如果日期为空则默认当前时间
function getOperaDate(param) {
	var date = new Date();
	var year = date.getFullYear();
	var mon = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+1) : (date.getMonth()+1);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	var min = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
//	daystr = '' +  year + "-" +  mon + "-" + day + " " + hour + ":" + min + ":" + second;
	if('flight' == param) {		
		daystr = '' +  year  +  mon  + day;
	}
	if('create' == param) {
		daystr = '' +  year + "-" +  mon + "-" + day + " " + hour + ":" + min;
	}
	
	return daystr;
}
// 获取下拉选选中
function getSendDeptValue() {
	if(null == $('#sendDept').val()) {
		return false;
	}
	return true;
}
// 获取反馈信息
function getFeedBackInfo() {
	var data = {
		"feedBackId" : $('#feedBackIdKey').val(),
		"feedBackOper" : $('#feedBackOper').val(),
		"feedBackDate" : $('#feedBackDate').val(),
		"feedBackContent" : $('#feedBackContent').val()
	};
	return data;
}
// 获取CDM判责内容
function getCDMInfo() {
	var data = {
			"feedBackId" : $('#feedBackId').val(),
			"cdmId" : $('#cdmId').val(),
			"cdmDate" : $('#cdmDate').val(),
			"cmdContent" : $('#cmdContent').val()
		};
		return data;
}
//查看页面打印Word
function printword(){
	$('input').removeAttr("disabled");
	$('textarea').removeAttr("disabled");
	$('select').removeAttr("disabled");
	
	$("#abnormalForm").submit();
	
	
	$('input').prop('disabled', 'true');
	$('textarea').prop('disabled', 'true');
	$('select').prop('disabled', 'true');
}