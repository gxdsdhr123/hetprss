var intervalObj;
var layer;
var layForm;
layui.use(["layer","form"],function(){
	layForm = layui.form;
});
$(document).ready(function(){
	setHeight();
	loadData();
	if(intervalObj){
		clearInterval(intervalObj);
	}
	intervalObj = setInterval(function() {
		loadData();
	}, 1000*30);
	$("#fltNo").keyup(function(e){
		if(e.keyCode==13){
			loadData();
		}
	});
	//航空公司分组
	$("#airlineGroupText").click(function(){
		layer.open({
			type:1,
			title:"航空公司",
			closeBtn:false,
			shadeClose:true,
			btn:["确定"],
			area:["460px","150px"],
			offset:[$(this).offset().top+20,$(this).offset().left-5],
			content:$("#airlineItems"),
			yes:function(index,layero){
				if ($("input[name=airlineGroup]:checked").length == 0
						|| $("input[name=airlineGroup]:checked").length == $("input[name=airlineGroup]").length) {
					$("#airlineGroupText").text("全部");
				} else {
					var airlineGroup = $("input[name=airlineGroup]:checked").map(function(){
						return $(this).attr("title");
					}).get().join("、");
					$("#airlineGroupText").text(airlineGroup);
				}
				loadData();
				layer.close(index);
			}
		});
	});
	$("#alarmLevelText").click(function(){
		layer.open({
			type:1,
			title:"预警级别",
			closeBtn:false,
			shadeClose:true,
			btn:["确定"],
			area:["420px","150px"],
			offset:[$(this).offset().top+20,$(this).offset().left-5],
			content:$("#alarmItems"),
			yes:function(index,layero){
				if ($("input[name=alarmLevel]:checked").length == 0
						|| $("input[name=alarmLevel]:checked").length == $("input[name=alarmLevel]").length) {
					$("#alarmLevelText").text("全部");
				} else {
					var airlineGroup = $("input[name=alarmLevel]:checked").map(function(){
						return $(this).attr("title");
					}).get().join("、");
					$("#alarmLevelText").text(airlineGroup);
				}
				loadData();
				layer.close(index);
			}
		});
	});
	$(window).resize(function(event){
		var srcElement = event.srcElement||event.target;
		//防止拖拽触发window.resize
		if(srcElement&&!srcElement.tagName){
			setHeight();
		}
	});
	resizeTaskList();
});

function setHeight(){
	var winHeight = $(window).height()-120-$("#tool-box").height();
	$(".box-body").height(winHeight);
	$(".task-list").slimScroll({
		height:winHeight/2-10,
		alwaysVisible: true,
		color:"#eee"
	});
}
/**
 * 拖拽边框调整大小
 */
function resizeTaskList(){
	//未分配
	$("#inFlight4").parent().resizable({
		handles:"s",
		containment:"parent",
		resize:function(event,ui){
			var winHeight = $(window).height()-120-$("#tool-box").height();
			var outFltHeight = winHeight-ui.size.height;
			$("#inFlight4").slimScroll({
				height:ui.size.height
			});
			$("#outFlight4").slimScroll({
				height:outFltHeight
			});
		}
	});
	//已排
	$("#inFlight2").parent().resizable({
		handles:"s",
		containment:"parent",
		resize:function(event,ui){
			var winHeight = $(window).height()-120-$("#tool-box").height();
			var outFltHeight = winHeight-ui.size.height;
			$("#inFlight2").slimScroll({
				height:ui.size.height
			});
			$("#outFlight2").slimScroll({
				height:outFltHeight
			});
		}
	});
	//完成
	$("#inFlight3").parent().resizable({
		handles:"s",
		containment:"parent",
		resize:function(event,ui){
			var winHeight = $(window).height()-120-$("#tool-box").height();
			var outFltHeight = winHeight-ui.size.height;
			$("#inFlight3").slimScroll({
				height:ui.size.height
			});
			$("#outFlight3").slimScroll({
				height:outFltHeight
			});
		}
	});
}

function loadData(){
	//航班号
	var fltNo = $.trim($("#fltNo").val());
	//航空公司分组
	var airlineGroup = $("input[name=airlineGroup]:checked").map(function(){
		return $(this).val();
	}).get().join(",");
	//预警级别
	var alarmLevel = $("input[name=alarmLevel]:checked").map(function(){
		return $(this).val();
	}).get().join(",");
	//未分配-进港
	$("#inFlight4").load(ctx+"/scheduling/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:"A",
		taskState:0,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		$("#total4 span:eq(0)").text("进港:"+$("#inFlight4 .task-item").length+"条");
		viewDetail("inFlight4");
	});
	// 未分配-出港
	$("#outFlight4").load(ctx+"/scheduling/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:"D",
		taskState:0,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		$("#total4 span:eq(1)").text(" / 出港:"+$("#outFlight4 .task-item").length+"条");
		viewDetail("outFlight4");
	});
	//已排-进港
	$("#inFlight2").load(ctx+"/scheduling/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:"A",
		taskState:2,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		$("#total2 span:eq(0)").text("进港:"+$("#inFlight2 .task-item").length+"条");
		viewDetail("inFlight2");
	});
	//已排-出港
	$("#outFlight2").load(ctx+"/scheduling/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:"D",
		taskState:2,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		$("#total2 span:eq(1)").text(" / 出港:"+$("#outFlight2 .task-item").length+"条");
		viewDetail("outFlight2");
	});
	//完成-进港
	$("#inFlight3").load(ctx+"/scheduling/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:"A",
		taskState:3,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		$("#total3 span:eq(0)").text("进港:"+$("#inFlight3 .task-item").length+"条");
		viewDetail("inFlight3");
	});
	//完成-出港
	$("#outFlight3").load(ctx+"/scheduling/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:"D",
		taskState:3,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		$("#total3 span:eq(1)").text(" / 出港:"+$("#outFlight3 .task-item").length+"条");
		viewDetail("outFlight3");
	});
}
/**
 * 查看详情
 * @param fltid
 */
var tempFltId = null;
function viewDetail(divId){
	$('#'+divId+' .task-item').mousemove(function(e) {
		var currentDiv = $(this);
		var parentDiv = $(this).parent();
		var fltid = currentDiv.data("fltid");
		if(fltid){
			var winHeight = $(window).height()-150-$("#tool-box").height();
			var top = e.pageY;
			var left = e.pageX+20;
			var state = currentDiv.data("taskstate");
			if(state==3){
				left = currentDiv.offset().left-$('#detailDiv').width();
			}
			if(tempFltId!=fltid){
				tempFltId = fltid;
				$.ajax({
					type : 'post',
					url : ctx + "/scheduling/progress/getTasks",
					data : {
						fltid : fltid,
						jobType : $("#jobType").val(),
						updateSession:0
					},
					dataType : "html",
					success : function(html) {
						
						$('#detailDiv').html(html);
						var detailHeight = $('#detailDiv').height();
						if((top+detailHeight)>winHeight){
							top-=detailHeight;
						}
						$('#detailDiv').show(500).css({
							"top" : (top<0?parentDiv.offset().top:top),
							"left" :left
						});
					}
				});
			} else if(tempFltId&&fltid==tempFltId){
				var detailHeight = $('#detailDiv').height();
				if((top+detailHeight)>winHeight){
					top-=detailHeight;
				}
				$('#detailDiv').show(500).css({
					"top" : (top<0?parentDiv.offset().top:top),
					"left" :left
				});
			} else {
				layer.msg("没有查询到信息");
			}
		}
	});
	$('#'+divId+' .task-item').mouseleave(function() {
		$('#detailDiv').hide();
	});
}
/**
 * 航班备注
 * @param itemDom
 * @returns
 */
function openFltRemark(itemDom){
	layer.open({
		type:2,
		area:["600px","400px"],
		title:"航班备注",
		btn:["保存","取消"],
		content:[ctx+"/scheduling/progress/fltRemarkForm?fltid="+$(itemDom).data("fltid")],
		yes:function(index,layero){
			var form = layer.getChildFrame("#inputForm", index);
			var loading = null;
			form.ajaxSubmit({
				async : false,
				beforeSubmit : function() {
					loading = layer.load(2, {
						shade : [ 0.1, '#000' ]
					// 0.1透明度
					});
				},
				error : function() {
					layer.close(loading);
					layer.msg('保存失败！', {
						icon : 2
					});
					return false;
				},
				success : function(data) {
					layer.close(loading);
					if(data=="succeed"){
						layer.msg('保存成功！', {
							icon : 1,
							time:2
						},function(){
							layer.close(index);
						});
					}  else {
						layer.alert("保存失败："+data,{icon:5});
					}
				}
			});
		}
	});
}