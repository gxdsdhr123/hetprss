var layer;
var clickDate = "";
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(function(){
	clickDate = "";
	initPlanDate(currDate);
	/*// 默认选择今天
	$('#planCal').fullCalendar( 'select', $.fullCalendar.moment(totalDate.format('YYYY-MM-DD')) )*/
	//上一月
	$("#prevMonBtn").click(function() {
		clickDate = "";
		$('#planCal').fullCalendar('prev');
		var prevD = $('#planCal').fullCalendar('getDate');
		$("#planDate").val(prevD.format('YYYY-MM'));
		initPlanDate($("#planDate").val()+"-01");
		$("#exportMonth").val(prevD.format('YYYY-MM-DD'));
		
	});
	//下一月
	$("#nextMonBtn").click(function() {
		clickDate = "";
		$('#planCal').fullCalendar('next');
		var prevD = $('#planCal').fullCalendar('getDate');
		$("#planDate").val(prevD.format('YYYY-MM'));
		initPlanDate($("#planDate").val()+"-01");
		$("#exportMonth").val(prevD.format('YYYY-MM-DD'));
	});
	//删除
	$("#delBtn").click(function() {
		if(clickDate==""){
			layer.msg('请选择要删除的计划,并且只能删除当日和以后日期的工作计划！', {
				icon : 2
			});
			return false;
		}else if(clickDate<currDate){
			layer.msg('只能删除当日和以后日期的工作计划！', {
				icon : 2
			});
			return false;
		}else {
			layer.confirm("是否删除选中的排班计划？",{offset:'100px'},function(index){
				doDelPlan();
				layer.close(index);
			});
		}
	});
	//批量删除
	$("#batchDelBtn").click(function() {
		clickDate = "";
		$(".planRangeDate").val("");
		var obj = $(this);
		layer.open({
			type:1,
			title: "批量删除计划",
			offset: '100px',
			content:$("#planRangeDate"),
			btn:["确认"],
			success:function(layero,index){
			},
			yes:function(index, layero){
				var times = "";
				var time1 = layero.find(".planRangeDate").eq(0).val();
				var time2 = layero.find(".planRangeDate").eq(1).val();
				if(time1==""||time2==""){
					layer.msg('请选择要删除的计划开始时间或结束时间', {
						icon : 2
					});
					return false;
				}else{
					times = time1+","+time2;
					doDelBatchPlan(times);
				}
				layer.close(index);
			}
		});
	});
	//打印
	$("#printBtn").click(function(){
		$("#printForm").submit();
	});
	//工作计划
	$('#planBtn').click(function() {
		if(clickDate==""){
			layer.msg('请选择日期', {
				icon : 2
			});
		}else{
			doPlanPage(clickDate);
		}
	});
	//人员停用日志
	$("#showLogBtn").click(function(){
		doShowLog();
	});
	//生成非固定班制计划
	$("#unfixedBtn").click(function(){
		doUnfixed();
	});
	//批量生成
	$("#batchAddBtn").click(function(){
		doBatchAdd();
	});
});

function initPlanDate(date){
	if(date==undefined){
		date = currDate;
	}
	selPlanDate(date);
	renderCalendar(date);
	// 默认当前月份
	var totalDate =$('#planCal').fullCalendar('getDate');
	$("#planDate").val(totalDate.format('YYYY-MM'));
}
var eventsObj;

function selPlanDate(date){
	$.ajax({
		type:'post',
		url: ctx+"/arrange/empplan/getEmpPlan",
		async:false,
		data:{
			selDate:date
		},
		dataType:"json",
		success:function(result){
			eventsObj = result;
		}
	});
}
function doCheckMonth(){
	clickDate = "";
	var selDate=$("#planDate").val()+"-01";
	selPlanDate(selDate);
	renderCalendar(selDate);
	$("#exportMonth").val(selDate);
	
}

function renderCalendar(date) {
	debugger;
	$('#planCal').fullCalendar('destroy');
	$('#planCal').fullCalendar({
		dayNamesShort : ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
		header: false,
		defaultDate: date,
		lang: 'zh-cn',
		buttonIcons: false,
		weekNumbers: false,
		weekMode:'liquid',
		height : 650,
		eventLimit:true,
		eventLimitClick:function( cellInfo, jsEvent ){
			setTimeout(function(){
				new PerfectScrollbar($('.fc-more-popover')[0]);
			});
			return 'popover';
		},
		dayClick: function(date, allDay, jsEvent, view) {
			var selDate = date.format('YYYY-MM-DD');
			$('#planCal').fullCalendar('select', selDate,selDate,allDay);
			clickDate = selDate;
		},
		events: eventsObj,
		eventRender: function(event, element, view){
			element.on('click',function(){
				var selDate = event.start.format('YYYY-MM-DD');
				$('#planCal').fullCalendar('select', selDate);
				clickDate = selDate;
			});
	        element.bind('dblclick', function() {
	        	var selDate = event.start.format('YYYY-MM-DD');
	        	doPlanPage(selDate);
	        });
	    }
	});
}

function doDelPlan(){
	doDelBatchPlan(clickDate+","+clickDate);
}

function doDelBatchPlan(dateRange){
	$.ajax({
		type:'post',
		url: ctx+"/arrange/empplan/getDelBatchEmpPlan",
		async:false,
		data : {
			dateStart : dateRange.split(",")[0],
			dateEnd : dateRange.split(",")[1]
		},
		dataType:"text",
		success:function(result){
			if(result=="success"){
				layer.msg('删除成功！', {
					icon : 1
				});
			}else{
				layer.msg('删除失败！', {
					icon : 2
				});
			}
			var dateTp = clickDate;
			if(clickDate==""){
//				dateTp=currDate;
				dateTp=dateRange.split(",")[0];
			}
			initPlanDate(dateTp);
			clickDate = "";
		}
	});
}

function doPlanPage(dt){
	planIframe = layer.open({
		type: 2, 
		title:"人员计划",
		area : ['90%','90%'],
		content: ctx+"/arrange/empplan/showPlanPage?selDate="+dt+"&currDate="+currDate,
		cancel: function(index, layero){
			clickDate = "";
			$("#planDate").val("");
			initPlanDate(currDate);
		}

	});
	//layer.full(planIframe);
	clickDate = "";
}


function doShowLog(){
	var logIFrame = layer.open({
		type : 2,
		offset: '20px',
		area:["80%","94%"],
		title : '人员停用日志',
		content : ctx + "/arrange/empplan/showLogPage"
	});
}

function doUnfixed(){
	var logIFrame = layer.open({
		type : 2,
		//offset: '20px',
		area:["100%","100%"],
		title : '生成非固定班制计划',
		content : ctx + "/arrange/empplan/showUnfixedPage"
	});
}

function doBatchAdd(){
	$("#timeStart").val("");
	$("#timeEnd").val("");
	$("#timeStart2").val("");
	$("#timeEnd2").val("");
	layer.open({
		type:1,
		title: "批量生成",
		offset: '50px',
		area:["50%","60%"],
		content:$("#contentDiv"),
		btn:["保存","关闭"],
		yes:function(index, layero){
			var stime1 = $("#timeStart").val();
			var etime1 = $("#timeEnd").val();
			var stime2 = $("#timeStart2").val();
			var etime2 = $("#timeEnd2").val();
			if(stime1==""||etime1==""||stime2==""||etime2==""){
				layer.msg('请选择计划来源时间或生成计划时间！', {
					icon : 2
				});
				return false;
			}
			$.ajax({
				type:'post',
				url: ctx+"/arrange/empplan/getIfExistPlan",
				async:false,
				data : {
					stime : stime2,
					etime : etime2
				},
				dataType:"text",
				success:function(result){
					if(result=="success"){
						saveBatchAddPlan();
					}else{
						layer.confirm(result,saveBatchAddPlan);
					}
				}
			});
			function saveBatchAddPlan(){
				$.ajax({
					type:'post',
					url: ctx+"/arrange/empplan/saveBatchAddPlan",
					async:false,
					data : {
						stime1 : stime1,
						etime1 : etime1,
						stime2 : stime2,
						etime2 : etime2
					},
					dataType:"text",
					success:function(result){
						if(result=="success"){
							layer.msg('生成成功！', {
								icon : 1
							});
							initPlanDate(stime2);
						}else{
							layer.msg('生成失败！', {
								icon : 2
							});
						}
					}
				});
				layer.close(index);
			}
		}
	});
}