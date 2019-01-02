$(function(){
	layui.use("form", function() {
		var form = layui.form;
		form.on('select(rFilter)', function(data) {
			$('#mtoername').val("");
			if(data.value=="8"){
				$("#receiverName").text('保障作业类型');
			} else {
				$(".texter").removeAttr("hidden");
				$("#receiverName").text('接收者名称');
			}
		});
		//是否转发
		form.on('checkbox(transFilter)', function(data) {
			if(data.elem.checked==true){
				$("#transtemplid").val('');
				$("#transtemplname").val('');
				$("#transtemplname").attr("disabled","disabled");
				$("#transtemplname").attr("disabled",false);
			} else {
				$("#transtemplname").attr("disabled",true);
			}
		});
		
		//是否触发
		form.on('checkbox(procFilter)', function(data) {
			if(data.elem.checked==false){
				$("#procdefparamto").val('');
				$("#procdefparamto").attr("disabled",true);
				$("#proceclsto").attr("disabled",true);
			} else {
				$("#procdefparamto").attr("disabled",false);
				$("#proceclsto").attr("disabled",false);
			}
			$("#proceclsto").val("all");
			form.render("select","filter");
		});
	})
	
	//设置滚动条--begin
		var scroll = $("#condition");
		scroll.css("position", "relative");
		new PerfectScrollbar(scroll[0]);

		var createFormReciver = $("#createFormReciver");
		createFormReciver.css("position", "relative");
		new PerfectScrollbar(createFormReciver[0]);
		//设置滚动条--end
});
//发送接收人名称获取方法	
function senderList(){	
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
//	if(mtotype=="8"){
//		parent.layer.msg("接收者名称可为空");
//		return false;
//	}
	layui.use(['form','element']);
	var createIframe = parent.layer.open({
		type: 2, 
		title:'添加名称',
		maxmin:false,
		resize : false,
		shadeClose: true,
		area: ["400px","410px"],
		content: ctx + "/message/templ/srList?mfromtype="+mfromtype,
		success:function(layero,index){

		}
	});
}


//模板信息获取
function mMessage(){
	layui.use(['form','element']);
	var createIframe = parent.layer.open({
		type: 2, 
		title:'模板',
		maxmin:false,
		resize : false,
		shadeClose: true,
		area: ["400px","410px"],
		content: ctx + "/message/templ/mMessage?mftype=1"
	});
}
//变量信息获取
function varListnew(){		
	layui.use(['form','element']);
	var createIframe = layer.open({
		type: 2, 
		title:'选择参数变量',
		maxmin:false,
		resize : false,
		shadeClose: true,
		area: ["400px","290px"],
		content: ctx + "/message/templ/getVariable"
	});
}
//发送接受名称赋值
function aa(val,val1){
	var flagsr=$('#flagsr').val();
	 if(flagsr=="0"){
		$('#mfromer').val(val);
		$('#mfromername').val(val1);
	}
	else if(flagsr=="1"){
		$('#mtoer').val(val);
		$('#mtoername').val(val1);
	}
}
//接受条件赋值
function cc(val,val1){
	$('#condtionname').val(val);
	$('#condtion').val(val1);
}

//模板名称赋值
function setParam(mtitle,no,mtext,varcols,mftype){
	$('#transtemplname').val(mtitle);
	$('#transtemplid').val(no);
}

//向导
function guide(){
	var ruleId = $("#ruleId").val();
	var condition = $("#condition").text();
	var rule_colids = $("#colids").val();
	var expression = $("#drlStr").val();
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
}

