var layer;
layui.use(["layer","form","laypage"],function(){	
	layer = layui.layer;
	var laypage = layui.laypage;
	var form = layui.form;
	laypage.render({
		elem : 'pagination',
		pages :$("#pages").val(),
		count :$("#totalRows").val(),
		curr:$("#pageNum").val(),
		groups : 5,
		skip : false,
		jump:function(obj, first){
			if(!first){
				 var pageNo = obj.curr;
				 doPageQuery(pageNo);
			}
		}
	});
	var divH=$(window).height()-80;
	$('#tableDiv').css("height",divH);
	new PerfectScrollbar('#tableDiv');
});

$(document).ready(function() {
	initFrom();
	//根据起点类型选择下拉框
	$("#editorstype").change(function() {
		var stype=$("#editorstype").val();
		$("#spos").val("");
		if (stype == "1"){
			var spos=$("#editorstypegate").val();
			$("#spos").val(spos);
			$("#editorstypeaprons").css('display','none');
			$("#editorstypegate").css('display','block'); 
		}else if(stype == "2"){
			var spos=$("#editorstypeaprons").val();
			$("#spos").val(spos);
			$("#editorstypegate").css('display','none');
			$("#editorstypeaprons").css('display','block'); 
		}else{
			$("#spos").val("");
			$("#editorstypeaprons").css('display','none');
			$("#editorstypegate").css('display','none'); 
		}
	});
	//根据终点类型选择下拉框
	$("#editoretype").change(function() {
		var etype=$("#editoretype").val();
		$("#epos").val("");
		if (etype == "1"){
			var epos=$("#editoretypegate").val();
			$("#epos").val(epos);
			$("#editoretypeaprons").css('display','none');
			$("#editoretypegate").css('display','block'); 
		}else if(etype == "2"){
			var epos=$("#editoretypeaprons").val();
			$("#epos").val(epos);
			$("#editoretypegate").css('display','none');
			$("#editoretypeaprons").css('display','block'); 
		}else{
			$("#epos").val("");
			$("#editoretypeaprons").css('display','none');
			$("#editoretypegate").css('display','none'); 
		}
	});
	//起点机坪
	$("#editorstypeaprons").change(function() {
		var spos=$("#editorstypeaprons").val();
		$("#spos").val(spos);
	});
	//起点登机口
	$("#editorstypegate").change(function() {
		var spos=$("#editorstypegate").val();
		$("#spos").val(spos);
	});
	//终点机坪
	$("#editoretypeaprons").change(function() {
		var epos=$("#editoretypeaprons").val();
		$("#epos").val(epos);
	});
	//终点登机口
	$("#editoretypegate").change(function() {
		var epos=$("#editoretypegate").val();
		$("#epos").val(epos);
	});
	//新增
	$("#btnAdd").click(function() {
		initFrom();
		var addWin=layer.open({
			type : 1,
			title : "新增",
			area : [ '40%', '60%' ],
			btn : [ "保存", "关闭" ],
			content :$("#editordiv"),
			yes : function() {
				var jobKind=$("#editorjobKind").val();
				var jobKindText=$("#editorjobKind").find("option:selected").text();
				var stype=$("#editorstype").val();
				var etype=$("#editoretype").val();
				var spos=$("#spos").val();
				var epos=$("#epos").val();
				var stime=$("#stime").val();
				var etime=$("#etime").val();
				var res=doCheck();
				if(res==false){
					return false;
				}
				$.ajax({
					type : "POST",
					url : ctx + "/sys/RuningTime/save",
					dataType : "text",
					data : {jobKind,stype,etype,spos,epos,stime,etime},
					error : function() {
						layer.msg('保存失败！', {
							icon : 2
						});
					},
					success : function(data) {
						if (data == "success") {
							layer.msg('保存成功！', {
								icon : 1,
								time : 600
							});
							layer.close(addWin);
							initFrom();
							doPageQuery(1);
						} else {
							layer.msg(''+jobKindText+'-'+spos+'-'+epos+' 正向 反向 记录已存在!', {
								icon : 2
							});
						};
					}
				})
			},
			btn2 : function(){
				initFrom();
			}
		});
		//layer.full(addWin);
	});
	
	//全选
	$("#checkAll").click(function(){
		if($(this).is(':checked')){
			$(this).attr("title","取消全选")
			$(".delcheckbox").prop("checked",true);
		}else{
			$(this).attr("title","全选")
			$(".delcheckbox").prop("checked",false);
		}
	})
	
});
//初始化表单
function initFrom(){
	$("[name='selectEditor']").val("");
	$("[name='inputEditor']").val("");
	$("[name='selectEditor']").removeAttr('disabled');
	$("[name='inputEditor']").removeAttr('disabled');
	$("[name='queryShow']").css('display','none');
	$("#editorstypeaprons").css('display','none');
	$("#editorstypegate").css('display','none'); 
	$("#editoretypeaprons").css('display','none');
	$("#editoretypegate").css('display','none'); 
}
//表单提交
function doSubmit(){
	doPageQuery(1);
}
//分页
function doPageQuery(pageNo){
	$("#pageNum").val(pageNo);
	$("#pageCount").val($("#pageSizeSelect").val());
	$("#searchForm").submit();
	return false;
}
//每页显示行数改变
function doPageSizeSelect(){
	doPageQuery(1);
}
//删除一条
function doOneDelete(id) {
	layer.confirm('确定要删除该条记录吗?', {
		shade : [ 0.3, '#eeeeee' ]
	},function(index){
		if(id==""){
			layer.msg('删除失败！', {
				icon : 2
			});
		};
		$.ajax({
			url : ctx + "/sys/RuningTime/deleteOneData",
			type : "POST",
			dataType :"text",
			data : {id},
			error : function() {
				layer.msg('删除失败！', {
					icon : 2
				});
			},
			success : function(data) {
				if (data == "success") {
					layer.msg('删除成功！', {
						icon : 1,
						time : 600
					});
					layer.close(index);
					doPageQuery(1);
				};
			}
		});
	});
}
//批量删除
function doMultiDelete() {
	if($(".delcheckbox:checked").length==0){
		layer.msg("请选择要删除的记录！", {
			icon : 7,
			time : 1000
		});
		return;
	}
	layer.confirm('确定要删除所选择的记录吗?', {
		shade : [ 0.3, '#eeeeee' ]
	}, function(index) {
		var idsArr = new Array();
		$(".delcheckbox:checked").each(function(k, v) {
			var param = $(v).val();
			idsArr.push(param);
		});
		$.ajax({
			url : ctx + "/sys/RuningTime/deleteMultiData",
			type : "POST",
			dataType :"text",
			data : {'idsArr':idsArr},
			async : true,
			error : function() {
				layer.msg('删除失败！', {
					icon : 2
				});
			},
			success : function(data) {
				if (data == "success") {
					layer.msg('删除成功！', {
						icon : 1,
						time : 600
					});
					layer.close(index);
					doPageQuery(1);
				};
			}
		});
		layer.close(index);
	});
}
//获取详细信息
function doGetOneValue(id){
	initFrom();
	var jobTypeCode=$("#"+id+"_JOB_TYPE_CODE").text();
	var spos=$("#"+id+"_SPOS").text();
	var stype=$("#"+id+"_STYPE").text();
	var epos=$("#"+id+"_EPOS").text();
	var etype=$("#"+id+"_ETYPE").text();
	var ftime=$("#"+id+"_FTIME").text();
	var rtime=$("#"+id+"_RTIME").text();
	var createTime=$("#"+id+"_CREATE_TIME").text();
	var updateTime=$("#"+id+"_UPDATE_TIME").text();
	var operator=$("#"+id+"_OPERATOR").text();
	$("#editorjobKind").val(jobTypeCode);
	$("#editorstype").val(stype);
	$("#spos").val(spos);
	if(stype=="1"){
		$("#editorstypeaprons").css('display','none');
		$("#editorstypegate").css('display','block'); 
		$("#editorstypegate").val(spos);
	}else{
		$("#editorstypeaprons").css('display','block');
		$("#editorstypegate").css('display','none'); 
		$("#editorstypeaprons").val(spos);
	}
	$("#editoretype").val(etype);
	$("#epos").val(epos);
	if(etype=="1"){
		$("#editoretypeaprons").css('display','none');
		$("#editoretypegate").css('display','block'); 
		$("#editoretypegate").val(epos);
	}else{
		$("#editoretypeaprons").css('display','block');
		$("#editoretypegate").css('display','none'); 
		$("#editoretypeaprons").val(epos);
	}
	$("#stime").val(ftime);
	$("#etime").val(rtime);
	$("#createTime").val(createTime);
	$("#updateTime").val(updateTime);
	$("#operator").val(operator);
}

//查看
function doQuery(id){
	doGetOneValue(id);
	$("[name='selectEditor']").attr('disabled',true);
	$("[name='selectEditor']").css('background','#002F63');
	$("[name='inputEditor']").attr('disabled',true);
	$("[name='queryShow']").css('display','block');
	var addWin=layer.open({
		type : 1,
		title : "查看",
		area : [ '40%', '80%' ],
		btn : [ "关闭" ],
		yes : function(){
			layer.close(addWin);
			initFrom();
		},
		content :$("#editordiv")
	});
}
//修改
function doModify(id){
	doGetOneValue(id);
	var addWin=layer.open({
		type : 1,
		title : "修改",
		area : [ '50%', '80%' ],
		btn : [ "修改","关闭" ],
		yes : function() {
			var jobKind=$("#editorjobKind").val();
			var jobKindText=$("#editorjobKind").find("option:selected").text();
			var stype=$("#editorstype").val();
			var etype=$("#editoretype").val();
			var spos=$("#spos").val();
			var epos=$("#epos").val();
			var stime=$("#stime").val();
			var etime=$("#etime").val();
			var res=doCheck();
			if(res==false){
				return false;
			}
			$.ajax({
				type : "POST",
				url : ctx + "/sys/RuningTime/update",
				dataType : "text",
				data : {id,jobKind,stype,etype,spos,epos,stime,etime},
				error : function() {
					layer.msg('保存失败！', {
						icon : 2
					});
				},
				success : function(data) {
					if (data == "success") {
						layer.msg('保存成功！', {
							icon : 1,
							time : 5000
						});
						layer.close(addWin);
						initFrom();
						doPageQuery(1);
					} else {
						layer.msg(''+jobKindText+'-'+spos+'-'+epos+' 正向 反向 记录已存在!', {
							icon : 2
						});
					};
				}
			})
		},
		btn2 : function(){
			initFrom();
		},
		content :$("#editordiv")
	});
}
//输入校验
function doCheck(){
	var re= /^[1-9]+[0-9]*]*$/;
	var jobKind=$("#editorjobKind").val();
	if(jobKind==""){
		layer.msg("作业类型不能为空！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	var stype=$("#editorstype").val();
	if(stype==""){
		layer.msg("起点类型不能为空！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	var etype=$("#editoretype").val();
	if(etype==""){
		layer.msg("终点类型不能为空！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	var spos=$("#spos").val();
	if(spos==""){
		layer.msg("起点不能为空！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	var epos=$("#epos").val();
	if(epos==""){
		layer.msg("终点不能为空！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	var stime=$("#stime").val();
	if(stime==""){
		layer.msg("正向时间不能为空！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	if(!re.test(stime)){
		layer.msg("正向时间必须为正整数！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	var etime=$("#etime").val();
	if(etime==""){
		layer.msg("反向时间不能为空！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	if(!re.test(etime)){
		layer.msg("反向时间必须为正整数！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	
	if(stype+spos==etype+epos){
		layer.msg("起点和终点不能相同！", {
			icon : 5,
			time : 1000
		});
		return false;
	};
	return true;
}