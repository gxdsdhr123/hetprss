var layer;
layui.use([ "layer" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	if($("#hisFlag").val() == 'his'){
		$("#tool-box").hide();
	}
	//刷新
	$("#refreshBtn").click(function(){
		refresh();
	});
	// 手工创建
	$("#createBtn").click(function() {
		taskForm("");
	});
	//执行任务
	$("#nextBtn").click(function(){
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		var jobTaskId = checkedRow.val();
		var orderId = checkedRow.data("orderid");
		var procId = checkedRow.data("procid");
		var jobState = checkedRow.data("state");
		if(!jobTaskId){
			layer.msg("请选择要执行的任务！",{icon:7});
			return false;
		}
		if(jobState=='3'){
			layer.msg("任务已完成，无继续可执行项！",{icon:7});
			return false;
		} else if(jobState!='2'){
			layer.msg("当前任务非运行状态，无法执行！",{icon:7});
			return false;
		}
		if(jobTaskId){
			layer.open({
				type:2,
				title:"代点原因",
				area:["600px","300px"],
				content:[ctx+"/scheduling/jobManage/jobSurrogateForm","no"],
				btn:["确定","取消"],
				yes:function(index,layero){
					var reason = layer.getChildFrame("#reason", index).val();
					var remark = layer.getChildFrame("#remark", index).val();
					if(reason=="其他"&&$.trim(remark)==""){
						layer.msg("请输入代点原因描述！",{icon:7});
						return false;
					}
					var loading = layer.load(2, {
						shade : [ 0.1, '#000' ]
					// 0.1透明度
					});
					$.ajax({
						type : 'post',
						url : ctx + "/workflow/btnEvent/doNext",
						async : false,
						data : {
							reason:reason,
							remark:remark,
							orderId:orderId,
							procId:procId,
							jobTaskId : jobTaskId
						},
						success : function(data) {
							layer.close(loading);
							if(data=="succeed"){
								layer.close(index);
								layer.msg("执行成功",{icon:1,time:600},function(){
									refresh();
								});
							} else {
								layer.msg("执行失败:"+data,{icon:7});
							}
						}
					});
				}
			});
		} else {
			layer.msg("请选择一条任务！",{icon:7});
		}
	});
	//修改
	$("#editBtn").click(function(){
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		var jobTaskId = checkedRow.val();
		if(!jobTaskId){
			layer.msg("请选择要修改的任务！",{icon:7});
			return false;
		}
		var jobState = checkedRow.data("state");//任务状态
		if(jobState=="2"){
			layer.msg("任务执行中，不可以修改！",{icon:7});
			return false;
		} else if(jobState=="3"){
			layer.msg("任务已执行完成，不可以修改！",{icon:7});
			return false;
		}
		taskForm(jobTaskId);
	});
	// 删除
	$("#removeBtn").click(function() {
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		var jobState = checkedRow.data("state");//任务状态
		if(jobState=="2"){
			layer.msg("任务执行中，不可以删除！",{icon:7});
			return false;
		} else if(jobState=="3"){
			layer.msg("任务已执行完成，不可以删除！",{icon:7});
			return false;
		}
		var jobTaskId = checkedRow.val();//任务ID
		var orderId = checkedRow.data("orderid");//流程实例id
		var procId = checkedRow.data("procid");
		var personid = checkedRow.data("personid");
		if(jobTaskId){
			var loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			// 0.1透明度
			});
			$.ajax({
				type : 'post',
				url : ctx + "/scheduling/jobManage/remove",
				async : false,
				data : {
					id : jobTaskId,
					orderId:orderId,
					procId:procId,
					personId:personid
				},
				success : function(data) {
					layer.close(loading);
					if(data=="succeed"){
						layer.msg("删除成功！",{icon:1,time:600},function(){
							refresh();
						});
					} else {
						layer.msg(data,{icon:7});
					}
				},
				error:function(){
					layer.msg("删除失败！",{icon:7});
				}
			});
		} else {
			layer.msg("请选择一条任务！",{icon:7});
		}
	});
	
	//中止任务
	$("#completeBtn").click(function(){
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		if(!checkedRow||checkedRow.length==0){
			layer.msg("请选择要中止的任务！",{icon:7});
			return false;
		}
		var jobState = checkedRow.data("state");//任务状态
		if(jobState!='2'){
			layer.msg("只有【执行中】的任务才可以中止！",{icon:7});
			return false;
		}
		var confirm = layer.confirm('您确定要中止当前任务？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			var jobTaskId = checkedRow.val();
			var orderId = checkedRow.data("orderid");
			var procId = checkedRow.data("procid");
			$.ajax({
				type : 'post',
				url : ctx + "/workflow/btnEvent/doTermination",
				async : false,
				data : {
					procId:procId,
					jobTaskId : jobTaskId,
					orderId:orderId
				},
				success : function(data) {
					if(data=="succeed"){
						layer.msg("任务已中止！",{icon:1,time:600},function(){
							refresh();
						});
					} else {
						layer.msg("中止失败！"+data,{icon:7});
					}
				}
			});
		});
	});
	//转交任务
	$("#transferBtn").click(function(){
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		var jobTaskId = checkedRow.val();
		var jobState = checkedRow.data("state");//任务状态
		if(!jobTaskId){
			layer.msg("请选择要转交的任务！",{icon:7});
			return false;
		}
		if(jobState!='2'){
			layer.msg("任务暂未执行，无法转交！",{icon:7});
			return false;
		}
		transfer(checkedRow);
	});
	$("#taskListGrid tbody tr").click(function(){
		$(this).find("td:eq(0) input[type=radio]").prop("checked",true);
	});
	
	//任务状态
	$("#viewStateBtn").click(function(){
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		var jobTaskId = checkedRow.val();
		if(!jobTaskId){
			layer.msg("请选择一条任务！",{icon:7});
			return false;
		}
		var jobState = checkedRow.data("state");//任务状态
		if(jobState!="2"&&jobState!="3"){
			layer.msg("任务暂未保障！",{icon:7});
			return false;
		}
		var orderId = checkedRow.data("orderid");
		var processId = checkedRow.data("procid");
		layer.open({
			type : 2,
			title : false,
			closeBtn:false,
			btn:["关闭"],
			area : [ '100%', '100%' ],
			content : [ctx + "/workflow/order/display?processId="+processId+"&orderId="+orderId,"yes"]
		});
	});
	//人员释放
	$("#releasePersonBtn").click(function(){
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		var jobTaskId = checkedRow.val();
		var personid = checkedRow.data("personid");
		if(!jobTaskId){
			layer.msg("请选择一条任务！",{icon:7});
			return false;
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		// 0.1透明度
		});
		$.ajax({
			type : 'post',
			url : ctx + "/scheduling/jobManage/releaseOperator",
			async : false,
			data : {
				taskId : jobTaskId,
				personId:personid
			},
			success : function(data) {
				layer.close(loading);
				if(data=="success"){
					layer.msg("人员释放成功！",{icon:1,time:600},function(){
						refresh();
					});
				} else {
					layer.msg("人员释放失败："+data,{icon:7});
				}
			}
		});
	});
	//任务恢复
	$("#recoverBtn").click(function(){
		var checkedRow = $("#taskListGrid tbody tr input[type=radio]:checked");
		var jobTaskId = checkedRow.val();
		if(!jobTaskId){
			layer.msg("请选择一条任务！",{icon:7});
			return false;
		}
		var jobState = checkedRow.data("state");//任务状态
		if(jobState!="6"){
			layer.msg("任务不是【人员释放】状态，不能执行恢复操作！",{icon:7});
			return false;
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		// 0.1透明度
		});
		$.ajax({
			type : 'post',
			url : ctx + "/scheduling/jobManage/recoveryTask",
			async : false,
			data : {
				taskId : jobTaskId
			},
			success : function(data) {
				layer.close(loading);
				if(data=="success"){
					layer.msg("任务恢复成功！",{icon:1,time:600},function(){
						refresh();
					});
				} else {
					layer.msg("任务恢复失败："+data,{icon:7});
				}
			}
		});
	});
});
var taskFormWin = null;
function taskForm(jobTaskId) {
	var schemaId = $("#schemaId").val();
	var inFltId = $("#inFltId").val();// 进港航班ID
	var outFltId = $("#outFltId").val();// 出港航班ID
	taskFormWin = layer.open({
		type : 2,
		title : "编辑任务",
		closeBtn : false,
		area : [ '100%', '98%' ],
		btn : [ "发布", "取消" ],
		content : [
				ctx + "/scheduling/jobManage/form?schemaId=" + schemaId
						+ "&inFltId=" + inFltId + "&outFltId=" + outFltId+"&jobTaskId="+jobTaskId,
				"no" ],
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveGridData();
		}
	});
}
function newTaskCallback(){
	layer.close(taskFormWin);
	layer.msg("发布成功！",{icon:1,time:600},function(){
		refresh();
	});
}
/**
 * 转交任务
 */
function transfer(checkedRow) {
	$.ajax({
		type : 'post',
		url : ctx + "/scheduling/jobManage/setJmSemaState",
		async : false,
		success : function(state) {
			if(state==0){
				var fltId = checkedRow.data("fltid");
				var jobType = checkedRow.data("jobtype");
				var procId = checkedRow.data("procid");
				var startTime = checkedRow.data("starttm");
				var endTime = checkedRow.data("endtm");
				var jobTaskId = checkedRow.val();
				var params = "?fltId="+fltId+"&jobType="+jobType+"&startTime="+startTime+"&endTime="+endTime;
				layer.open({
					type : 2,
					title : "转交任务",
					closeBtn : false,
					area : [ '100%', '98%' ],
					btn : [ "确定", "取消" ],
					success: function(layero, index){
						var selected = layer.getChildFrame('input[name=personRadio][value='+checkedRow.data("personid")+']', index);
						if(selected&&selected.length>0){
							selected.prop("checked",true);
						}
					},
					content : [ ctx + "/scheduling/jobManage/jobTaskUserForm"+params, "yes" ],
					yes : function(index, layero) {
						var person = layer.getChildFrame('input[name=personRadio]:checked',index);
						if (person.val()) {
							if(person.val()==checkedRow.data("personid")){
								layer.msg('任务不能转交给自己！', {
									icon : 2
								});
								return false;
							}
							var loading = null;
							//任务接收人
							var targetActor = person.val();
							$.ajax({
								type : 'post',
								url : ctx + "/workflow/btnEvent/doTransfer",
								async : false,
								beforeSubmit : function() {
									loading = layer.load(2, {
										shade : [ 0.1, '#fff' ]
									// 0.1透明度
									});
								},
								error : function() {
									layer.close(loading);
									layer.msg('转交失败！', {
										icon : 2
									});
									return false;
								},
								data : {
									targetActor:targetActor,
									jobTaskId : jobTaskId,
									procId:procId
								},
								success : function(data) {
									layer.close(loading);
									if(data=="succeed"){
										layer.msg("任务已转交！",{icon:1,time:600},function(){
											refresh();
										});
									} else {
										layer.msg("转交失败"+data,{icon:7});
									}
								}
							});
						} else {
							layer.msg("请选择作业人！", {
								icon : 7
							});
							return false;
						}
					}
				});
			} else {
				layer.msg("系统或其他用户正在进行人员分配，请稍后重试！",{icon:7});
				return false;
			}
		}
	});
}
function refresh(){
	var schemaId = $("#schemaId").val();
	var inFltId = $("#inFltId").val();
	var outFltId = $("#outFltId").val();
	document.location.href = ctx+"/scheduling/jobManage?schemaId="+schemaId+"&inFltId="+inFltId+"&outFltId="+outFltId;
}
function  getNodeView(id){
	var jobKind = $("#jobKind").val();
	iframe = layer.open({
		type : 2,
		title : "任务节点时间",
		area:[$("body").width()*0.5+"px","500px"],
		closeBtn : false,
		content : ctx + "/scheduling/jobManage/showTaskTime?id="+id+"&jobKind="+jobKind,
		btn : [ "返回" ]
	});
}