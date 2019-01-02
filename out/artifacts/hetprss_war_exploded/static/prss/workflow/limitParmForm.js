layui.use(["form","element"],function(){
	var form = layui.form;
	form.on('select(jobKind)', function(data) {
		$.ajax({
			type : 'post',
			url : ctx + "/workflow/node/getTypeByKind",
			data : {
				'jobKind':data.value
			},
			dataType : "json",
			success:function(result){
				$("#jobType").empty();
				$("#jobType").append("<option value=''>请选择</option>");
				for(var i=0;i<result.length;i++){
					$("#jobType").append("<option value='"+result[i]["RESTYPE"]+"'>"+result[i]["TYPENAME"]+"</option>");
				}
				$("#process").empty();
				$("#process").append("<option value=''>请选择</option>");
				$("#node").empty();
				$("#node").append("<option value=''>请选择</option>");
				form.render('select');
			}
		});
	});
	form.on('select(jobType)', function(data) {
		$.ajax({
			type : 'post',
			url : ctx + "/workflow/process/getProcessData",
			data : {
				'jobType':data.value,
				jobKind:$("#jobKind").val()
			},
			dataType : "json",
			success:function(result){
				$("#process").empty();
				$("#process").append("<option value=''>请选择</option>");
				for(var i=0;i<result.length;i++){
					$("#process").append("<option value='"+result[i]["id"]+"'>"+result[i]["displayName"]+"</option>");
				}
				$("#node").empty();
				$("#node").append("<option value=''>请选择</option>");
				form.render('select');
			}
		});
	});
	form.on('select(process)', function(data) {
		$.ajax({
			type : 'post',
			url : ctx + "/workflow/process/getProcessNodes",
			data : {
				id:data.value,
			},
			dataType : "json",
			success:function(result){
				$("#node").empty();
				$("#node").append("<option value=''>请选择</option>");
				for(var i=0;i<result.length;i++){
					$("#node").append("<option value='"+result[i]["id"]+"'>"+result[i]["displayName"]+"</option>");
				}
				form.render('select');
			}
		});
	});
});
function getFormData(){
	return JSON.stringify({
		processId : $("#process").val(),
		processName:$("#process option:selected").text(),
		nodeId : $("#node").val(),
		nodeName:$("#node option:selected").text()
	});
}