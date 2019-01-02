$(function(){
	var layer;
	var clickRowId = "";
	
	$("body").css("position","relative");
	$("body").each(function(){
		scrollbar = new PerfectScrollbar(this);
	});

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
	var expression = $("#drlStr").val();
	var createIframe = layer.open({
		type : 2,
		title : '条件',
		maxmin : false,
		resize : false,
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
			$("#drlStr").val(expression);
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
						$("#drlStr").val(expression);
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
	var hbevent = $("select[name=hbevent]").val();
	var schtime = $("input[name=schtime]").val();
	var condition = $("textarea[name=condition]").val();
	var flightnumber = $("input[name=flightnumber]").val();
	var hbiotype = $("select[name=hbiotype]").val();
	var mtemplid = $("input[name=mtemplid]").val();
	var mtemplname = $("input[name=mtemplname]").val();
	var disable = $("select[name=disable]").val();
	var mtext = $("div[id=mtext]").text();
	var totype = $("#mfromtype").val();
	var torangenames = $("input[name=torangenames]").val();
	var ifsms = $("input[name=ifsms]").prop("checked")== true?"1":"0";
	var mftype = $("#mftype").val();
	var sendDate = $("input[name=sendDate]").val();
	
	var fu = new ChkUtil();
	var res = '';
	if(schtime==""){
		res = fu.checkFormInput(hbevent, true,'String','');
		if (res.length > 0) {
			var msg = '[触发类型]合法性检查错误,' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}

//		res = fu.checkFormInput(condition, true,'String','');
//		if (res.length > 0) {
//			var msg = '[触发条件]合法性检查错误,' + res;
//			layer.msg(msg, {icon: 2,time: 1000});
//			return false;
//	 	}	

//		res = fu.checkFormInput(flightnumber, true,'String',10);
//		if (res.length > 0) {
//			var msg = '[航班号]合法性检查错误,' + res;
//			layer.msg(msg, {icon: 2,time: 1000});
//			document.all("input[name=flightnumber]").focus();
//			return false;
//		}
	}

	if(hbevent==''){
		var reg = /^(([0,1]\d{1})|([2][0-3]))([0-5])\d$/;
		if(reg.test(schtime)==false){
			var msg = '[发送时间]合法性检查错误     (格式1310),' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}
	} 
			
	if(sendDate!=null && sendDate!=''){
		var reg = /^(\d{8})$/;
		if(reg.test(sendDate)==false){
			var msg = '[发送日期]合法性检查错误     (格式20180101),' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}
	}
	if($("#mtemplname").lenght >0){
		res = fu.checkFormInput(mtemplid, true,'String','');
		if (res.length > 0) {
			var msg = '[模板名称]合法性检查错误,' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}
	}
	if(mtemplname!=null && mtemplname!=''){
		res = fu.checkFormInput(mtext, true,'String','');
		if (res.length > 0) {
			var msg = '[消息正文]合法性检查错误,' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}
	} else {
		res = fu.checkFormInput(mtext, true,'String','');
		if (res.length > 0) {
			var msg = '[消息正文]合法性检查错误,' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}
		res = fu.checkFormInput(totype, true,'int',1);
		if (res.length > 0) {
			var msg = '[接收人类型]合法性检查错误';
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}
		res = fu.checkFormInput(torangenames, true,'String','');
		if (res.length > 0) {
			var msg = '[接收人]合法性检查错误,' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
		}
	}
	res = fu.checkFormInput(ifsms, false,'int',2);
	if (res.length > 0) {
		var msg = '[短信]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		return false;
	}

	return true;
}
var save = function (){
	var id = $("input[name=id]").val();
	var hbevent = $("select[name=hbevent]").val();
	
	var schtime = $("input[name=schtime]").val();
	var condition = $("#condition").val();
	var flightnumber = $("input[name=flightnumber]").val();
	var hbiotype = $("select[name=hbiotype]").val();
	
	var mtemplid = $("input[name=mtemplid]").val();
	var mtemplname = $("input[name=mtemplname]").val();
	if(mtemplname==null || mtemplname=='')
		mtemplid ='';
	var disable = $("select[name=disable]").val();
	var mtext = $("div[id=mtext]").text();
	var old = $("#mtext").html();
	old = old.replace(/\<br\>/ig, '\n');
	var oldobj = $("<div>"+old+"</div>");
	mtext = oldobj.text();
	var colids = $("#colids").val();
	var totype = $("#mfromtype").val();
	var torange = $("#torange").val();
	var torangenames = $("input[name=torangenames]").val();
	if(totype=="8" ||totype =='-1'){
		var torange="";
		var torangenames="";
	}
	var ifsms = $("input[name=ifsms]").prop("checked")== true?"1":"0";
	if(totype!="0"){
		var ifsms="0";
	}
	var crtime = $("input[name=crtime]").val();
	var cruseren = $("input[name=cruseren]").val();
	var cruserid = $("#cruserid").val();
	var ruleid=$("#ruleId").val();
//	var varcols=$("#varcols").val();
    var varcols = getvarcols();
	var drlStr = $("#drlStr").val();
	var drools = $("#drools").val();
	var jobId = $("#jobId").val();
	var ifpopup = $("input[name=ifpopup]").prop("checked");
	var sendDate = $("input[name=sendDate]").val();
	var check = submitCheck();
	if(check==true){
	  	$.ajax({
			type:'post',
			data:{
				'id': id,
				'hbevent': hbevent,
				'schtime' : schtime,
				'condition' : condition,
				'flightnumber' : flightnumber,
				'hbiotype' : hbiotype,
				'mtemplid' : mtemplid,
				'disable' : disable,
				'mtext' : mtext,
				'totype' : totype,
				'torange':torange,
				'torangenames' : torangenames,
				'ifsms' : ifsms,
				'cruseren' : cruseren,
	            'ruleid' : ruleid,
	            'colids' : colids,
				'crtime' : crtime,
				'varcols' : varcols,
				'drlStr' :drlStr,
				'drools' : drools,
				'jobId' : jobId,
				'cruserid' : cruserid,
				'ifpopup' : ifpopup==true?1:0,
				'sendDate':sendDate
			},
			async:false,
			url:ctx+"/message/subscribe/save",
			dataType:"text",
			success:function(result){
				if(result == "操作成功"){
					 layer.closeAll('iframe');
						layer.msg(result, {icon: 1,time: 1000});
				} else {
					layer.msg(result, {icon: 1,time: 1000});
				}
		   
			},
			error:function(msg){
				var result = "操作失败";
				layer.msg(result, {icon: 1,time: 1000});
			}
		});
	}
	return check;
}

//参数获取值页面
function paramList(){		
	layui.use(['form','element']);
	var createIframe = layer.open({
		  type: 2, 
		  title:'选择参数变量',
		  maxmin:false,
		  resize : false,
		  shadeClose: true,
		  area: ["480px","400px"],
		  content: ctx + "/message/templ/getVariable"
		});
	}
//接收人名称页面
function reciverList(){	
	var flagsr=$('#flagsr').val();
	var mfromtype = $('#mfromtype').val();
	var mtotype = $('#mtotype').val();
	if (flagsr == "0") {
		if (mfromtype == "") {
			return;
		} else if (mfromtype == "9") {
			$("#mfromername").val("sys");
			return
		}
	}
	if (flagsr == "1") {
		if (mtotype == "") {
			return;
		} else if (mtotype == "9") {
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
		  maxmin:false,
		  resize : false,
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
//接受范围名称赋值
function aa(val,val1){
	console.info(val1)
	$("#torange").val(val);
	$("#torangenames").val(val1);
}
//模板名称赋值
function setParam(mtitle,no,mtext,varcols,mftype){
	   $('#mtemplname').val(mtitle);
	   $('#mtemplid').val(no);
	   $("#mtext").text('');
	   $("#mtext").append(mtext);
	   $("#varcols").val(varcols);
	   $("#mftype").val(mftype);
}

//向导运算获取值方法
function getValue (val) {
	var a=$("#condition1").val();
	var b=a+" "+val;
	$("#condition1").val(b);	 
	}
//模板信息页面弹出
function mMessage(){	
	layui.use(['form','element']);
	var createIframe = layer.open({
	  type: 2, 
	  title:'模板',
	  maxmin:false,
	  resize : false,
//	  shade:0,
	  shadeClose: false,
	  area: ["490px","410px"],
	  content: ctx + "/message/templ/mMessage"
	});

}
