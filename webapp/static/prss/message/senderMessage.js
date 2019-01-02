var form ;
$(function(){
	layui.use("form", function() {
		form = layui.form;
		form.on('select(sFilter)', function(data) {
			$('#mfromername').val("");
			if (data.value == "9") {
				$('#mfromername').val("sys");
			}
		});
		//是否触发
		form.on('checkbox(procFilter)', function(data) {
			if(data.elem.checked==false){
				$("input[name=procdefparamfrom]").val('');
				$("input[name=procdefparamfrom]").attr("disabled",true);
				$("select[name=proceclsfrom]").attr("disabled",true);
			} else {
				$("input[name=procdefparamfrom]").attr("disabled",false);
				$("select[name=proceclsfrom]").attr("disabled",false);
				$("#addProc").show();
			}
			$("select[name=proceclsfrom]").val("all");
			form.render("select","filter");
		});
	})
	
	$("#addProc").on("click",function(){
		var proc1 = '<div class="layui-inline cloneProc1">'+
					'	<label class="layui-form-label">处理类</label>'+
					'	<div class="layui-input-inline ">'+
					'		<select name="proceclsfrom" class="layui-disabled">';
			proc1+= $("select[name=proceclsfrom]").first().html()+
					'		</select>'+
			 		'	</div>'+
			 		'</div>';
		$(".formDiv").append($(proc1));
		$(".formDiv").append($($(".cloneProc2").first().prop("outerHTML")));
		form.render("select","filter");
		if($(".cloneProc1").length>1){
			$("#removeProc").show();
		}
	});
	
	$("#removeProc").on("click",function(){
		$(".cloneProc1").last().remove();
		$(".cloneProc2").last().remove();
		if($(".cloneProc1").length==1){
			$("#removeProc").hide();
		}
	});
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
			$("#mfromername").val("sys").attr("disabled",true);
			return
		}
	}
	if (flagsr == "1") {
		if (mtotype == "") {
			return;

		} else if (mtotype == "9") {
			$("#mtoername").val("sys").attr("disabled",true);
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

