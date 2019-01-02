var layer;
var tempData;
var _sitaType;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(function() {
	$("#beginTime").blur(function() {
		countTime();
	});
//	var defaultDate = new Date();
//	$("#beginTime").val(
//			defaultDate.getFullYear() + "-" + (defaultDate.getMonth() + 1) + "-" + defaultDate.getDate() + " " + defaultDate.getHours() + ":" + defaultDate.getMinutes() + ":"
//					+ defaultDate.getSeconds());
	$("#recycleTime").blur(function() {
		countTime();
	});
	$("#mtext").blur(function() {
		var old = $("#mtext").html();
		old = old.replace(/\<br\>/ig, '\n');
		var oldobj = $("<div>"+old+"</div>");
		var mtext = oldobj.text();
		$("#msgtext").val(mtext);
	});
	// 下拉选中事件 自动带出类型和正文
	$("#mtemplid").change(function() {
		var tempId = $(this).find("option:checked").val();
		if(tempId != null && tempId != ''){
			// 获取并绑定邮箱
			changeTempl(tempId);
		} else {
			$("#tempType").val('');
			$("#mtext").text('');
			$("#airline_code").text('');
			$("#priority").val('');
		}
	});
	function rules(){
		return {
			recycleTime : {
				digits : true,
				min : 1
			},
			address : {
				required : true
			},
			mtemplid : {
				required : true
			}
		}
	}
	// 表单提交方法被调用后触发此验证
	$('#createForm').validate({
		rules : rules(),
		errorPlacement : function(error, element) {
			error.appendTo(element.parent());
		},
		submitHandler : function(form) {
			var loading = null;
			$("#createForm").ajaxSubmit({
				async : false,
				beforeSubmit : function() {
					loading = layer.load(2, {
						shade : [ 0.1, '#000' ]
					// 0.1透明度
					});
				},
				error : function(XmlHttpRequest, textStatus, errorThrown) {
					layer.close(loading);
					layer.msg('保存失败！', {
						icon : 2
					});
				},
				success : function(data) {
					layer.close(loading);
					if (data == "success" || data.indexOf("成功") > -1) {
						layer.msg('操作成功！', {
							icon : 1,
							time : 600
						}, function() {
							parent.formSubmitCallback();// 成功后调用主页面，关闭弹出层并刷新表格
						});
					} else if (data == "error" || data.indexOf("失败") > -1) {
						layer.msg('操作失败！', {
							icon : 2,
							time : 600
						});
					} else if (data == "overTime") {// 修改时间验证，若有变化说明数据已被改动，需要重新加载数据
						layer.msg('数据已失效，请重新加载数据', {
							icon : 2,
							time : 600
						}, function() {
							parent.formSubmitCallback();
						});
					}
				}
			});
			return false;
		}
	});

	//设置滚动条--begin
	var mtext = $("#mtext");
	mtext.css("position", "relative");
	new PerfectScrollbar(mtext[0]);
	//设置滚动条--end
	countTime();
})

function changeTempl(tempId){
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$.ajax({
		type : 'post',
		url : ctx + "/telegraph/templ/getAddressByTempId",
		dataType : 'json',
		async : false,
		data : {
			'id' : tempId,
			'fltid' : $("#fltid").val()
		},
		success : function(result) {
			var templ = result.templ;
			$("#tempType").val(templ.TG_CODE);
//			var mtext = $("#mtext").text();
			
//			if(mtext==null || mtext ==''){
//				$("#mtext").text('');
//				$("#mtext").append(templ.TG_TEXT);
//				$("#mtext").blur();
				$("#mtext").text(templ.TG_TEXT)
//			}
			$("#airline_code").val(templ.ALN_2CODE);
			$("#priority").val(templ.PRIORITY);
			var sitaType = templ.TG_SITE_TYPE;
			if(sitaType =='1'){//sita
				$(".addressName").text('SITA地址');
			} else if(sitaType =='2'){//邮件
				$(".addressName").text('邮件地址');
			}
			var data = result.address;
			var sender = result.sender;
			if (data.length > 0) {
				for ( var item in data) {
					_sitaType = data[item].TG_SITE_TYPE;
					$("#address").val(data[item].ADDRESS);
				}
			} else {
				$("#address").val("");
			}
			layer.close(loading);
		},
		error : function(msg){
			console.info(JSON.stringify(msg));
		}
	});
}
// 保存
function formSubmit() {
	
	var beginTime = $("#beginTime").val();
	if(beginTime == '' || beginTime == null){
		layer.msg("开始时间不能为空",{icon:0,time:1000});
		return ;
	}
	var endTime = $("#endTime").val();
	if(endTime == '' || endTime == null){
		layer.msg("结束时间不能为空",{icon:0,time:1000});
		return ;
	}
	var varcols = getvarcols();
	var mtext = $("#mtext").val();
	console.info(mtext);
	$("#teleText").val(mtext);
	$("#varcols").val(varcols);
	$("#submitType").val("save");
	$('#createForm').attr("action",ctx + '/telegraph/auto/saveTime');
	$('#createForm').submit();

}
// 终止
function terminal() {
	var manualId = $("input[name='manualId']").val();
	$.ajax({
		type : 'post',
		url : ctx + "/telegraph/auto/stop?&manualId="+manualId,
		dataType : 'text',
		success : function(data) {
			
			if (data.indexOf("成功") > -1) {
				layer.msg('操作成功！', {
					icon : 1,
					time : 600
				}, function() {
					parent.formSubmitCallback();// 成功后调用主页面，关闭弹出层并刷新表格
					$(".text-center").find("strong").html("自动发送设置【已停止】");
				});
			} else if (data.indexOf("失败") > -1) {
				layer.msg('操作失败！', {
					icon : 2,
					time : 600
				});
			} 
		}
	})

}
function doSend() {
	$("#submitType").val("send");
	$('#createForm').attr("action",ctx + '/telegraph/auto/send');
	$('#createForm').submit();
}

function clear() {
	$("input").val("");
	$("select").val("");
}
function countTime() {
	// 获取当前时间
	var date = new Date();
	var now = date.getTime();
	// 设置截止时间
	var beginTime = new Date($("#beginTime").val());
	var sendTime = beginTime.getTime();
	var leftvalue = getLeftTime(sendTime, now)
	if("no" == leftvalue)
		return false;
	if ("noRecycle" == leftvalue) {
		layer.alert("请输入正确的循环时间", {
			icon : 0,
			time : 3000
		});
		return false;
	}
	$("#leftTime").val(leftvalue);
	// 递归每秒调用countTime方法，显示动态时间效果
	setTimeout(countTime, 1000);
}
function getLeftTime(sendTime, now) {
	// 时间差
	if(isNaN(sendTime))
		return "no";
	var leftTime = sendTime - now;
	// 定义变量 d,h,m,s保存倒计时的时间
	var d, h, m, s;
	if (leftTime >= 0) {
		d = Math.floor(leftTime / 1000 / 60 / 60 / 24);
		h = Math.floor(leftTime / 1000 / 60 / 60 % 24);
		m = Math.floor(leftTime / 1000 / 60 % 60);
		s = Math.floor(leftTime / 1000 % 60);
		// 将倒计时赋值到div中
		var leftvalue = d + "天" + h + "时" + m + "分" + s + "秒";
		return leftvalue;
	} else {
		if ($("#recycleTime").val() && $("#recycleTime").val() != "" && !isNaN($("#recycleTime").val())) {
			sendTime = sendTime + $("#recycleTime").val() * 60 * 1000;
			return getLeftTime(sendTime, now);
		} else {
			return "noRecycle";
		}
	}

}
function openParmsForm() {
	var iframe = layer.open({
		type : 2,
		title : '选择参数变量',
		maxmin : false,
		resize : false,
		shadeClose : true,
		area : [ "480px", "400px" ],
		content : ctx + "/telegraph/templ/getVariable"
	});
}
