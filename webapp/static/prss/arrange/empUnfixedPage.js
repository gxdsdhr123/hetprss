var layer,form;
var clickRowId="";
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
	form.on('select(groupSel)', function(data){
        var id = data.value;
        getEmpInfo(id);
    });  
});

$(function(){
	initGridData();
	//增加
	$("#addBtn").click(function(){
		doAddPlan();
	});
	//修改
	$("#modifyBtn").click(function(){
		doModifyPlan();
	});
	//删除
	$("#deleteBtn").click(function(){
		doDeletePlan();
	});
	//生成计划
	$("#makePlanBtn").click(function(){
		makePlanBtn();
	});
	//关闭
	$("#closeBtn").click(function(){
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	});
	//作业组
	$("#teamBtn").click(function(){
		doTeamFunc();
	});
	//选择人
	$("#workerBtn").click(function(){
		doWorkerFunc();
	});
	//非固定班次类型
	$("#unfixedBtn").click(function(){
		doUnfixed();
	});
	
	initFunc2();
	initFunc3();
	initFunc4();
	$(".sortable").sortable({
		connectWith: ".sortable",
		stop: function( event, ui ) {
			setTimeout(function(){
				ui.item.removeClass("bechoose");
			},10)
			haveEmpInfo();	
		}
	});
	//作业组搜素
	$("#teamSearch").keyup(function(event) {
		if (event.keyCode == 13) {
			applyAllTeam();
		}
	});
	//选人搜素
	$("#personSearch").keyup(function(event) {
		if (event.keyCode == 13) {
			var groupId = $("#groupInfo").val();
			getEmpInfo(groupId);
		}
	});
	//非固定班次类型搜素
	$("#unfixedSearch").keyup(function(event) {
		if (event.keyCode == 13) {
			applyUnfixed();
		}
	});
});

function initFunc2(){
	$("#leftDiv2 .hr").draggable({
		connectToSortable: ".chooseTeamFiled",
		helper: "clone",
		revert: "invalid",
		stop: function( event, ui ) {
			ui.helper.dblclick(function(){
				ui.helper.remove();
			});
		}
	});
	$("#pushleft").click(function(){
		var bechoose = $("#rightDiv2 .bechoose");
		bechoose.removeClass("bechoose");
		$("#leftDiv2 .list-group").append(bechoose);
	});
	$("#pushright").click(function(){
		var bechoose = $("#leftDiv2 .bechoose");
		bechoose.removeClass("bechoose");
		$("#rightDiv2 .list-group").append(bechoose);
	});
}
function initFunc3(){
	$("#leftDiv3 .hr").draggable({
		connectToSortable: ".chooseWorkerFiled",
		helper: "clone",
		revert: "invalid",
		stop: function( event, ui ) {
			ui.helper.dblclick(function(){
				ui.helper.remove();
			});
		}
	});
	$("#pushleft2").click(function(){
		var bechoose = $("#rightDiv3 .bechoose");
		bechoose.removeClass("bechoose");
		$("#leftDiv3 .list-group").append(bechoose);
		haveEmpInfo();
	});
	$("#pushright2").click(function(){
		var bechoose = $("#leftDiv3 .bechoose");
		bechoose.removeClass("bechoose");
		$("#rightDiv3 .list-group").append(bechoose);
		haveEmpInfo();
	});
}

function initFunc4(){
	$("#leftDiv4 .hr").draggable({
		connectToSortable: ".chooseUnfixedFiled",
		helper: "clone",
		revert: "invalid",
		stop: function( event, ui ) {
			ui.helper.dblclick(function(){
				ui.helper.remove();
			});
		}
	});
	$("#pushleft3").click(function(){
		var bechoose = $("#rightDiv4 .bechoose");
		bechoose.removeClass("bechoose");
		$("#leftDiv4 .list-group").append(bechoose);
	});
	$("#pushright3").click(function(){
		var bechoose = $("#leftDiv4 .bechoose");
		bechoose.removeClass("bechoose");
		$("#rightDiv4 .list-group").append(bechoose);
	});
}

function init(){
	$("li").unbind("click");
	$("li").click(function(){
		var obj = $(this);
		if(obj.hasClass("bechoose")){
			obj.removeClass("bechoose");
		}else{
			obj.addClass("bechoose");
		}
	});
}

function initGridData(){
	var tableOptions = {
		url: ctx+"/arrange/empplan/showUnfixedList",
		    method: "get",	
		    dataType: "json",
			striped: true,	
		    cache: true,
		    uniqueId:"ID",
		    checkboxHeader:false,
		    toolbar:$("#tool-box"),
		    search:false,
		    pagination: true,
		    sidePagination: 'server',
		    pageNumber: 1,
		    pageSize: 5,
		    pageList: [5, 10, 20],
		    queryParamsType:'',
		    queryParams: function (params) {
		    	var param = {  
                    pageNumber: params.pageNumber,    
                    pageSize: params.pageSize
                }; 
	            return param;     
		    },
		    onClickRow:function onClickRow(row,tr,field){
				clickRowId = row.ID;
				$(".clickRow").removeClass("clickRow");
				$(tr).addClass("clickRow");
			},
		    columns : [ {
					field : "RM",
					title : "序号",
					width : "6%"
				}, {
					field : "NAME",
					title : "循环人员",
					width : "47%"
				}, {
					field : "SHIFTS",
					title : "循环班制",
					width : "47%"
				}]
		};
	tableOptions.height = $("body").innerHeight()-$("#searchBox").outerHeight()-100;
	$("#baseTable").bootstrapTable(tableOptions);
}


function doAddPlan(){
	$("#teamInfoUL").empty();
	$("#shiftsInfoUL").empty();
	unfixedAdd=layer.open({
		type:1,
		title: "生成计划",
		offset: '5px',
		area:["90%","90%"],
		anim:-1,
		content:$("#contentDiv"),
		btn:["确定","关闭"],
		yes:function(index, layero){
			if(!layero.find(".layui-layer-btn0").hasClass("layui-btn-disabled")){
				loading = parent.layer.load(2, {
					shade : [ 0.1, '#000' ]
				// 0.1透明度
				});
				layero.find(".layui-layer-btn0").addClass("layui-btn-disabled");
				var ids1=[],ids2=[];
				var lis1 = $("#leftDiv li");
				var lis2 = $("#rightDiv li");
				if(lis1.length==0||lis2.length==0){
					layer.msg('人员或班制不能为空！', {
						icon : 2
					});
					parent.layer.close(loading);
					layero.find(".layui-layer-btn0").removeClass("layui-btn-disabled");
					return false;
				}
				for(var i=0;i<lis1.length;i++){
					var li = $(lis1[i]);
					ids1.push(li.data("code"));
				}
				for(var i=0;i<lis2.length;i++){
					var li = $(lis2[i]);
					ids2.push(li.data("code"));
				}
				$.unique(ids1);
				$.unique(ids2);
				$.ajax({
					type : 'post',
					url : ctx + "/arrange/empplan/saveUnfiexPlan",
					data : {
						ids1 : ids1.join(","),
						ids2 : ids2.join(",")
					},
					error:function(){
						parent.layer.close(loading);
						layero.find(".layui-layer-btn0").removeClass("layui-btn-disabled");
					},
					success : function(result) {
						parent.layer.close(loading);
						if(result=="success"){
							layer.msg('保存成功！', {
								icon : 1
							});
							initGridData();
							$("#baseTable").bootstrapTable('refresh');
						}else{
							layer.msg('保存失败！', {
								icon : 2
							});
							layero.find(".layui-layer-btn0").removeClass("layui-btn-disabled");
						}
					}
				});
				layer.close(index);
			}
		}
	});
}

function doDeletePlan(){
	if(clickRowId==""){
		layer.msg('请选择要删除的计划', {
			icon : 2
		});
		return false;
	}
	$.ajax({
		type : 'post',
		url : ctx + "/arrange/empplan/deleteUnfixedPlan",
		data : {
			id : clickRowId
		},
		success : function(result) {
			if(result=="success"){
				layer.msg('删除成功！', {
					icon : 1
				});
				initGridData();
				$("#baseTable").bootstrapTable('refresh');
			}else{
				layer.msg('删除失败！', {
					icon : 2
				});
			}
		}
	});
}


function makePlanBtn(){
	var time1 = $("#timeStart").val();
	var time2 = $("#timeEnd").val();
	if(time1==""||time2==""){
		layer.msg('生成计划的开始时间或结束时间不能为空！', {
			icon : 2
		});
		return false;
	}
	var times=time1+","+time2;
	
	var index = layer.load(2);
	
	$.ajax({
		type : 'post',
		url : ctx + "/arrange/empplan/saveMakePlan",
		data : {
			times : times
		},
		success : function(result) {
			if(result=="success"){
				layer.close(index);
				layer.msg('生成计划成功！', {
					icon : 1
				});
				var index = parent.layer.getFrameIndex(window.name);
				parent.initPlanDate();
				parent.layer.close(index);
			}else{
				layer.msg('生成计划失败！', {
					icon : 2
				});
			}
		}
	});
}

function doTeamFunc(){
	//渲染到dom
	applyAllTeam();
	//打开窗口
	teamAdd=layer.open({
		type:1,
		title: "选择作业组",
		offset: '5px',
		area:["90%","90%"],
		content:$("#teamDiv"),
		btn:["保存","关闭"],
		success:function(){
			applySelectedTeam();
		},
		yes:function(index, layero){
			var lis = $("#rightDiv2 li");
			$("#teamInfoUL").find(".teamli").remove();
			for(var i=0;i<lis.length;i++){
				var li = $(lis[i]);
				var id = li.data("code");
				var name = li.text();
				$("#teamInfoUL").append("<li class='list-group-item teamli' data-code='"+id+"'>" + name +"</li>");
			}
			init();
			layer.close(index);
		}
	});
}

/**
 * 渲染全部作业组窗口数据
 * @param result
 */
function applyAllTeam(){
	$("#allTeamUL").empty();
	var teamName = $("#teamSearch").val();
	var progressBar = null;
	$.ajax({
		type:'post',
		url: ctx+"/arrange/empplan/getTeamList",
		async:false,
		data :{
			teamName:teamName
		},
		dataType:"json",
		beforeSend : function() {
			progressBar = parent.layer.load(2, {
				shade : [ 0.1, '#000' ]
			// 0.1透明度
			});
		},
		complete : function() {
			parent.layer.close(progressBar);
		},
		success:function(result){
			for(var i in result){
				var lis = $("#teamInfoUL li");
				var chk=true;
				lis.each(function(){
					 var cd = $(this).data("code");
					 if(cd==result[i].ID){
						 chk=false;
					 }
				});
				if(chk){
					$("#allTeamUL").append("<li class='list-group-item hr' data-code='"+result[i].ID+"'>" + result[i].NAME +"</li>");
				}
			}
			init();
		}
	});
}

function applySelectedTeam(){
	$("#selTeamUL").empty();
	var lis = $("#teamInfoUL").find(".teamli");
	lis.each(function(){
		var cd = $(this).data("code");
		var nm = $(this).html();
		$("#selTeamUL").append("<li class='list-group-item hr' data-code='"+cd+"'>" + nm +"</li>");
	});
	init();
}

function doWorkerFunc(){
	$("#selWorkerUL").empty();
	var progressBar = parent.layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	setGroupOpt();//作业作下拉
	getAllEmp();//全部人
	layer.open({
		type:1,
		title: "选择人员",
		offset: '5px',
		area:["90%","90%"],
		content:$("#workerDiv"),
		btn:["保存","关闭"],
		success:function(layero,index){
			applySelectedWorker();
			parent.layer.close(progressBar);
		},
		yes:function(index, layero){
			var lis = $("#rightDiv3 li");
			$("#teamInfoUL").find(".worker").remove();
			for(var i=0;i<lis.length;i++){
				var li = $(lis[i]);
				var id = li.data("code");
				var name = li.text();
				$("#teamInfoUL").append("<li class='list-group-item worker' data-code='"+id+"'>" + name +"</li>");
			}
			init();
			layer.close(index);
		}
	});
}
//分组下拉
function setGroupOpt(){
	$.ajax({
		type:'post',
		url: ctx+"/arrange/empplan/getGroupInfo",
		async:false,
		data :null,
		dataType:"json",
		success:function(result){
			$(".optadd").remove();
			for(var i in result){
				$("#groupInfo").append("<option class='optadd' value='"+result[i].ID+"'>" + result[i].NAME +"</option>");
			}
			form.render('select');
		}
	});
}

function applySelectedWorker(){
	var lis = $("#teamInfoUL").find(".worker");
	lis.each(function(){
		var cd = $(this).data("code");
		var nm = $(this).html();
		$("#selWorkerUL").append("<li class='list-group-item hr worker' data-code='"+cd+"'>" + nm +"</li>");
	});
	init();
}
function haveEmpInfo(){
	var lis = $("#selWorkerUL li");
	var temp = "";
	lis.each(function(){
		 if(temp.indexOf($(this).data("code"))!=-1)
		 $(this).remove();
		 temp += $(this).data("code")+",";
	});
}

function getAllEmp(){
	$("#allWorkerUL").empty();
	var personName = $("#personSearch").val();
	$.ajax({
		type:'post',
		url: ctx+"/arrange/empplan/getAllEmpInfo",
		async:false,
		data : {
			personName:personName
		},
		dataType:"json",
		success:function(result){
			for(var i in result){
				var lis = $("#selWorkerUL li");
				var chk=true;
				lis.each(function(){
					 var cd = $(this).data("code");
					 if(cd==result[i].ID){
						 chk=false;
					 }
				});
				if(chk){
					$("#allWorkerUL").append("<li class='list-group-item hr' data-code='"+result[i].ID+"'>" + result[i].NAME +"</li>");
				}
			}
		}
	});
}

function getEmpInfo(id){
	if(id=='all'){
		getAllEmp();
	}else{
		var personName = $("#personSearch").val();
		$.ajax({
			type:'post',
			url: ctx+"/arrange/empplan/getEmpInfoById",
			async:false,
			data : {
				groupId : id,
				personName:personName
			},
			dataType:"json",
			success:function(result){
				$("#allWorkerUL").empty();
				for(var i in result){
					var lis = $("#selWorkerUL li");
					var chk=true;
					lis.each(function(){
						 var cd = $(this).data("code");
						 if(cd==result[i].ID){
							 chk=false;
						 }
					});
					if(chk){
						$("#allWorkerUL").append("<li class='list-group-item hr' data-code='"+result[i].ID+"'>" + result[i].NAME +"</li>");
					}
				}
			}
		});
	}
	init();
}

function doUnfixed(){
	var progressBar = parent.layer.load(2, {
		shade : [ 0.1, '#000' ]
	// 0.1透明度
	});
	$("#selUnfixedUL").empty();
	applyUnfixed();
	layer.open({
		type:1,
		title: "选择班制",
		offset: '5px',
		area:["90%","90%"],
		content:$("#unfixedDiv"),
		btn:["保存","关闭"],
		success:function(layero,index){
			var lis = $("#shiftsInfoUL li");
			lis.each(function(){
				 var cd = $(this).data("code");
				 var nm = $(this).html();
				 $("#selUnfixedUL").append("<li class='list-group-item hr' data-code='"+cd+"'>" + nm +"</li>");
			});
			init();
			parent.layer.close(progressBar);
		},
		yes:function(index, layero){
			var lis = $("#rightDiv4 li");
			$("#shiftsInfoUL li").remove();
			for(var i=0;i<lis.length;i++){
				var li = $(lis[i]);
				var id = li.data("code");
				var name = li.text();
				$("#shiftsInfoUL").append("<li class='list-group-item' data-code='"+id+"'>" + name +"</li>");
			}
			init();
			layer.close(index);
		}
	});
}
function applyUnfixed(){
	$("#allUnfixedUL").empty();
	var unfixedName = $("#unfixedSearch").val();
	$.ajax({
		type:'post',
		url: ctx+"/arrange/empplan/getUnfixedDim",
		async:false,
		data :{
			unfixedName:unfixedName
		},
		dataType:"json",
		success:function(result){
			var lis = $("#shiftsInfoUL li");
			for(var i in result){
				var chk=true;
				lis.each(function(){
					 var cd = $(this).data("code");
					 if(cd==result[i].ID){
						 chk=false;
					 }
				});
				if(chk){
					$("#allUnfixedUL").append("<li class='list-group-item hr' data-code='"+result[i].ID+"'>" + result[i].NAME +"</li>");
				}
			}
			init();
		}
	});
}

function doModifyPlan(){
	$("#teamInfoUL").empty();
	$("#shiftsInfoUL").empty();
	if(clickRowId==""){
		layer.msg('请选择要修改的计划', {
			icon : 2,
			time:800
		});
		return false;
	}
	unfixedModify=layer.open({
		type:1,
		title: "生成计划",
		offset: '5px',
		area:["90%","90%"],
		anim:-1,
		content:$("#contentDiv"),
		btn:["确定","关闭"],
		success:function(layero,index){
			$.ajax({
				type:'post',
				url: ctx+"/arrange/empplan/getUnfixedHaveList",
				async:false,
				data :{
					id:clickRowId
				},
				dataType:"json",
				success:function(result){
					var arr1 = result.workerHaveList;
					var arr2 = result.shiftsHaveList;
					for(var i in arr1){
						var teamId = arr1[i].TEAM_ID;
						if(teamId==null||teamId==""){
							$("#teamInfoUL").append("<li class='list-group-item worker' data-code='"+arr1[i].ID+"'>" + arr1[i].NAME +"</li>");
						}else{
							$("#teamInfoUL").append("<li class='list-group-item teamli' data-code='"+arr1[i].ID+"'>" + arr1[i].NAME +"</li>");
						}
					}
					for(var j in arr2){
						$("#shiftsInfoUL").append("<li class='list-group-item' data-code='"+arr2[j].ID+"'>" + arr2[j].NAME +"</li>");
					}
				}
			});
		},
		yes:function(index, layero){
			var ids1=[],ids2=[];
			var lis1 = $("#leftDiv li");
			var lis2 = $("#rightDiv li");
			if(lis1.length==0||lis2.length==0){
				layer.msg('人员或班制不能为空！', {
					icon : 2
				});
				return false;
			}
			for(var i=0;i<lis1.length;i++){
				var li = $(lis1[i]);
				ids1.push(li.data("code"));
			}
			for(var i=0;i<lis2.length;i++){
				var li = $(lis2[i]);
				ids2.push(li.data("code"));
			}
			$.unique(ids1);
			$.unique(ids2);
			$.ajax({
				type : 'post',
				url : ctx + "/arrange/empplan/saveUnfiexPlan",
				data : {
					ids1 : ids1.join(","),
					ids2 : ids2.join(","),
					planId :clickRowId,
					flag : 'modify'
				},
				success : function(result) {
					if(result=="success"){
						layer.msg('保存成功！', {
							icon : 1
						});
						initGridData();
						$("#baseTable").bootstrapTable('refresh');
					}else{
						layer.msg('保存失败！', {
							icon : 2
						});
					}
				}
			});
			layer.close(index);
		}
	});
}