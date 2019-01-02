$(function(){
	var layer;
	var clickRowId = "";

	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	//设置滚动条--begin
	var tpl=$("#condition");
	tpl.css("position","relative");
	new PerfectScrollbar(tpl[0]);
	
	var mtext = $("#mtext");
	mtext.css("position", "relative");
	new PerfectScrollbar(mtext[0]);
	//设置滚动条--end
});


//向导
$('#guide').click(function() {
	var ruleId = $("#ruleId").val();
	var condition = $("#condition").text();
	var rule_colids = $("#colids").val();
	var expression = $("#expression").val();
	var createIframe = parent.layer.open({
		type : 2,
		title : '条件',
		maxmin : false,
		shadeClose : false,
		area: ["640px","400px"],
		content: ctx+"/param/common/guide?schema=RULE&ruleId=" + ruleId 
		+ "&colids=" + rule_colids 
		+ "&condition="+encodeURIComponent(encodeURIComponent(condition)) 
		+ "&expression=" + encodeURIComponent(encodeURIComponent(expression)),
		btn:["确认","复制规则"],
		yes:function(index, layero){
			var mtext = $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).text();
			var expression = $(layero).find("iframe")[0].contentWindow.getDrlRule();
			var rule_colids = $(layero).find("iframe")[0].contentWindow.getvarcols();
			$("#condition").text(mtext);
			$("#colids").val(rule_colids);
			$("#expression").val(expression);
			$.ajax({
				type : 'post',
				url : ctx + "/rule/taskAllotRule/createDrl",
				data : {
					expression : expression
				},
				async : false,
				dataType : 'json',
				success : function(data) {
					if(data.code == 0){
						var drl = data.result;
						$("#condition").val(mtext);
						$("#colids").val(rule_colids);
						$("#expression").val(expression);
						$("#drools").val(drl);
//						layer.close(index);
					}else{
						layer.msg(data.msg,{icon: 2,time: 1000});
					}
				}
			});
			layero.find(".layui-layer-close").click();
		},
		btn2:function(index, layero){
			$(layero).find("iframe")[0].contentWindow.openCopy();
		  return false;
		}
	});
//	layer.full(createIframe);
});
	
	
function submitCheck() {
	var eventId = $("select[name=event]").val();
	var airline_code = $("select[name=airline]").val();
	var condition = $("#condition").val();
	var colids = $("input[name=colids]").val();
	var mtemplid = $("input[name=mtemplid]").val();
	var mtext = $("div[id=mtext]").text();
	var varcols = $("#varcols").val();
	var sendType = $("input[name=sendType]:checked").val();
	
	var fu = new ChkUtil();
	var res = '';

	res = fu.checkFormInput(eventId, true,'',10);
	if (res.length > 0) {
		var msg = '[触发类型]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		$("select[name=event]").focus();
		return false;
	}
	res = fu.checkFormInput(airline_code, true,'',10);
	if (res.length > 0) {
		var msg = '[航空公司]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		$("select[name=airline]").focus();
		return false;
	}
//	res = fu.checkFormInput(condition, true,'',500);
//	if (res.length > 0) {
//		var msg = '[触发条件]合法性检查错误,' + res;
//		layer.msg(msg, {icon: 2,time: 1000});
//		$("#condition").focus();
//		return false;
// 	}		 
	res = fu.checkFormInput(mtemplid, true,'String',100);
	if (res.length > 0) {
		var msg = '[模板名称]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		$("input[name=mtemplid]").focus();
		return false;
	}
	res = fu.checkFormInput(mtext, true,'String','');
	if (res.length > 0) {
		var msg = '[消息正文]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		mtext = $("div[id=mtext]").focus();
		return false;
	}
	res = fu.checkFormInput(sendType, true,'String','');
	if (res.length > 0) {
		var msg = '[发送方式]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		$("input[name=sendType]").focus();
		return false;
 	}	
	return true;
}
var save = function (){

	var id = $("input[name=id]").val();
	var flag = $("input[name=flag]").val();
	var eventId = $("select[name=event]").val();
	var airline_code = $("select[name=airline]").val();
	var condition = $("#condition").val();
	var colids = $("input[name=colids]").val();
	var mtemplid = $("input[name=mtemplid]").val();
	var mtext = $("div[id=mtext]").text();
	var old = $("#mtext").html();
	old = old.replace(/\<br\>/ig, '\n');
	var oldobj = $("<div>"+old+"</div>");
	mtext = oldobj.text();
//	var varcols = $("#varcols").val();
    var varcols = getvarcols();
	var ruleId = $("#ruleId").val();
	var sendType = $("input[name=sendType]:checked").val();
	var expression = $("#expression").val();
	var drools = $("#drools").val();
	var check = submitCheck();
	if(check){
	  	$.ajax({
			type:'post',
			data:{
				'id': id,
				'flag': flag,
				'eventId' : eventId,
				'condition' : condition,
				'expression' : expression,
				'airline_code' : airline_code,
				'colids' : colids,
				'drools' : drools,
				'mtemplid' : mtemplid,
				'mtext' : mtext,
				'ruleId' : ruleId,
				'sendType' : sendType,
				'varcols' : varcols
			},
			async:false,
			url:ctx+"/telegraph/auto/save",
			dataType:"text",
			success:function(result){
//			    layer.closeAll('iframe');
				layer.msg(result, {icon: 1,time: 1000});
				return true;
			},
			error:function(msg){
				var result = "操作失败";
				layer.msg(result, {icon: 1,time: 1000});
				return false;
			}
		});
	}
	return check;
}

//接收人名称页面
function reciverList(){	
	var flagsr=$('#flagsr').val();
	var mfromtype = $('#mfromtype').val();
	var mtotype = $('#mtotype').val();
	if (flagsr == "0") {
		if (mfromtype == "") {
			return;
		} else if (mfromtype == "3") {
			$("#mfromername").val("sys");
			return
		}
	}
	if (flagsr == "1") {
		if (mtotype == "") {
			return;
		} else if (mtotype == "3") {
			$("#mtoername").val("sys");
			return
		} else {
			var mfromtype = mtotype;
		}
	}
	
	layui.use(['form','element']);
	var createIframe = layer.open({
		  type: 2, 
		  title:'选择人员',
		  maxmin:true,
		  shadeClose: true,
		  area: ["490px","410px"],
		  content: ctx + "/message/templ/srList?mfromtype="+mfromtype
		});
	
	}
//向导赋值至消息正文
function getAllValue() {
	var val=$("#condition1").val();
	var a=$("#condition").val();
	var b=a+" ["+val+"] ";
	$("#condition").val(b);	 
//	layer.closeAll('iframe');
//	 layero.find(".layui-layer-close1").click();
}
//模板名称赋值
function setValue(no,name,mtext,varcols){
	$('#mtemplname').val(name);
	$('#mtemplid').val(no);
	$("#mtext").text('');
	$("#mtext").append(mtext);
	$('#varcols').val(varcols);
}


//模板信息页面弹出
function showTempl(){	
	layui.use(['form','element']);
	var createIframe = layer.open({
	  type: 2, 
	  title:'模板',
	  maxmin:false,
	  shadeClose: false,
	  area: ["490px","410px"],
	  content: ctx + "/telegraph/auto/autoTempl?eventId=" + $("#event").val() + "&airline_code=" + $("#airline").val()
	});
}

//参数获取值页面
function paramList(){		
	layui.use(['form','element']);
	var createIframe = layer.open({
	  type: 2, 
	  title:'选择参数变量',
	  maxmin:false,
	  shadeClose: true,
	  area: ["480px","400px"],
	  content: ctx + "/telegraph/templ/getVariable"
	});
}
