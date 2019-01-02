$(function(){
	layui.use("form", function() {
		var form = layui.form;
		//监听指定开关
		  form.on('switch(switchTest)', function(data){
			  form.render('select'); //这个很重要
		  });
		  
		form.on('select(sFilter)', function(data) {
			$('#tg_address').val('');
			$('#tg_site_name').val('');
			$('#tg_site_id').val('');
		});

		//是否触发
		form.on('checkbox(procFilter)', function(data) {
			if(data.elem.checked==false){
				$("#procdefparamfrom").val('');
				$("#procdefparamfrom").attr("disabled",true);
				$("#proceclsfrom").attr("disabled",true);
			} else {
				$("#procdefparamfrom").attr("disabled",false);
				$("#proceclsfrom").attr("disabled",false);
			}
			$("#proceclsfrom").val("all");
			form.render("select","filter");
		});
	})
})

//发送接收人名称获取方法	
function senderList(){	
	var sitaType = $("#sitaType").val();
	var ids = $("#ids").val()
	layui.use(['form','element']);
	layer.open({
		type: 2, 
		title:'添加名称',
		maxmin:false,
//		shade:0,
		area: ["490px","290px"],
		content: ctx + "/telegraph/templ/srList?sitaType=" + sitaType + "&ids=" + ids,
		success:function(layero,index){

		}
	});
}
//发送接受名称赋值
function setValue(row){
	$('#tg_address').val(row.TG_ADDRESS);
	$('#tg_site_name').val(row.NAME);
	$('#tg_site_id').val(row.NO);
}

function changeProc(){
	var value = $(this).val();
}