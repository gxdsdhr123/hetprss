var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});

var searchTime = "";
var searchData = "";
var clickRow;

$(function() {
	// 初始化下拉选
//	initSelect();
	
	// 初始化时间
	if("" == $('#flightDate').val()) {		
		$('#flightDate').val(getOperaDate());
	}
	// 新增一行
	$('#addRow').click(function() {
		addRow();
		
	});
	// 删除一行
	$('#delRow').click(function() {
		if(null == clickRow || '' == clickRow) {
			layer.msg("请点击一行" , {icon : 7, time : 600});
		} else {
			delRow();
		}
	});
	// 初始化下拉默认选中
//	var operator = $('input[name=operator]').val();
//	$("#charlineId").val(operator).trigger('change');
	// 初始化点击事件
	initClickRow();
	
	
	// 光标离开触发
	$('#flightNumber').blur(function() { 
		getLargePieceData();
	});
	// 回车触发
    $('#flightNumber').on('keypress',function(event){ 
        if(event.keyCode == 13) {  
        	getLargePieceData();
        }  
    });
    // 修改界面
    if("" != $('#paramModify').val() && null != $('#paramModify').val()) {
    	$('#flightNumber').prop('disabled', true);
    	$('#flightDate').prop('disabled', true);
    }
    
});

// 删除一行
function delRow() {
	if($('#tbody tr').length == 1) {
		$('input[name=seq]').val(1);
		$('input[name=bagNumber]').val('');
		$('input[name=dest]').val('');
	} else {		
		$(clickRow).remove();
		clickRow = '';
		descSeq();
	}
}
// 每一行初始化单击事件
function initClickRow() {
	$('#tbody tr').each(function(i, e) {
		$(e).click(function() {
			$(e).addClass("clickRow").siblings().removeClass("clickRow");
			clickRow = e;
		});
	});
}


//增加一行
function addRow() {
	var tbody = $('#tbody');
	var newNode = $('.cloneHtml').clone()[0];
	$('input[name=bagNumber]', newNode).val('');
	$('input[name=dest]', newNode).val('');
	tbody.append(newNode);
	descSeq();
	// 清除选中样式
	$('#tbody tr').each(function(i, e) {
		$(e).removeClass("clickRow");
	});
	// 初始行点击事件
	initClickRow();
}
// 触发重新排序
function descSeq() {
	var index = 1;
	// 序号重新排序
	$('input[name=seq]').each(function(i, e) {
		$(e).val(index++);
		
	});
}

//改变时间触发加载数据
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
	getLargePieceData();
}

// 获取输入数据
function getLargePieceData() {
	var flightNumber = $('#flightNumber').val();
	if("" == flightNumber.trim() || null == flightNumber) {
		clearAnbormalForm();
		return;
	}
	var flightDate = $('#flightDate').val();
	if("" == flightDate.trim() || null == flightDate) {
		clearAnbormalForm();
		return;
	}
	if(searchData == flightNumber) {
		return;
	}
	var inn = layer.load(2);
	$.ajax({
		type : 'post',
		url : ctx + "/largePieceOfPlum/luggage/getAirFligthInfo",
		data : {
			flightNumber : flightNumber,
			flightDate : flightDate
		},
		success : function(msg) {
			searchData = flightNumber;
			searchTime = flightDate;
			var list = eval(msg);
//			removeDest();
			if(list.length != 0) {
				dealAbnormalFlightData(list[0]);
			} else {
				clearAnbormalForm();
			}
			layer.close(inn);
		}
	});
}

//清空赋值
function clearAnbormalForm() {
	// 清空表单
	$('.content-c').each(function(i, e) {
		$(e).val('');
	});
	// 清空选中
//	initSelect();
	// 清除增加内容区
	$('#luggageAdd').empty();
}
// 清除增加表格
function removeDest() {
	$('#tbody tr').each(function(i, e) {
		if(i != 0) {
			$(e).remove();
		} else {
			$('input[name=bagNumber]').val('');
			$('input[name=dest]').val('');
		}
	});
}
//表单赋值
function dealAbnormalFlightData(data) {
	 for(var o in data){  
		$('input[id=' + o + "]").val(data[o]);
		if("ACTSTANDCODE" == o) {
			$('input[name=actstandCode]').val(data[o]);
		}
	 }  
}
// 重置
function reset() {
	removeDest();
	initSelect();
	if("modify" != $('#paramModify').val()) {
		$('#reset').click();
		$('#flightDate').val(getOperaDate());
		$('#flightNumber').val('');
		clearAnbormalForm();
	}
}


// 获取表单数据
function getFormData() {
	return $("#largePieceOfPlum").serialize();
}

// 弹出消息
function alertInfo(msg) {
	layer.msg(msg , {icon : 7, time : 1000});
}

// 检查时间
function checkFlightDate() {
	if(null == $('#flightDate').val()) {
		alertInfo("请选择日期");
		return false;
	}
	return true;
}
// 检查航班数据
function checkAirFlightData() {
	if(null == $('#FLTID').val() || '' == $('#FLTID').val()) {
		alertInfo("请填写出与航班日期匹配的航班号");
		return false;
	}
	return true;
}
// 检查查理
function checkCharline() {
	if(null == $('#charlineId').val() || '' == $('#charlineId').val()) {
		alertInfo("请选择操作人");
		return false;
	}
	return true;
}
// 检查行李号与目的地
function checkBagAndDest() {
	var bagNumber = [];
	var dest = [];
	var bag = $('.bagNumber');
	for(var i = 0; i < bag.length; i++) {
		if(null != $(bag[i]).val() && "" != $(bag[i]).val().trim()) {
			bagNumber.push($(bag[i]).val());			
		}
	}
	var de = $('.dest');
	for(var i = 0; i < de.length; i++) {
		if(null != $(de[i]).val() && "" != $(de[i]).val().trim()) {
			dest.push($(de[i]).val());
		}
	}
	

	if(bagNumber.length != dest.length) {
		alertInfo("请填写对应的行李号与目的站");
		return false;
	}
	return true;
}






//select 改变事件
//$('#charlineId').on('change', function(){
//	$('input[name=operatorName]').val($('#charlineId').find("option:selected").text());
//	$('input[name=operator]').val($('#charlineId').find("option:selected").val());
//});

//初始化下拉选
function initSelect() {
//		$('select[id='+ id +']').select2({
	$('#charlineId').select2({
		placeholder : "请选择",
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
//	获取日期， 如果日期为空则默认当前时间
function getOperaDate() {
	var date = new Date();
	var year = date.getFullYear();
	var mon = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+1) : (date.getMonth()+1);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
//	var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
//	var min = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
//	var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
//	daystr = '' +  year + "-" +  mon + "-" + day + " " + hour + ":" + min + ":" + second;
	daystr = '' +  year  +  mon  + day;
	
	return daystr;
}
window.onload = function() {
	// 取消遮罩
	setTimeout(function() {
		$('div[class=mark_c]').attr({
			'style' : 'display: none;'
		});
	}, 600);

}

$(window).load(function(){
	var pototStr=$("#photoCode").val();
	var photoArr=pototStr.split(",");
	for(i= 0,len=photoArr.length; i< len; i++) {
		if(photoArr[i]!=''){
		   $("#SIGNIMGdiv").append('<div style="float:left;margin-right:5px"><img  src="'+ctx+'/largePieceOfPlum/luggage/outputPicture?id='+photoArr[i]+'" height=90 width=160/></div>');
		}
	}
})