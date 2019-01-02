var intervalObj;
var layForm;
var loading;
layui.use(["layer","form"],function(){
	loadData();
	setHeight();
	layForm = layui.form;
	if(intervalObj){
		clearInterval(intervalObj);
	}
	intervalObj = setInterval(function() {
		loadData(true);
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
	layForm.on('select(ioType)', function (data) {
		loadData();
    });
	$(window).resize(function(event){
		var srcElement = event.srcElement||event.target;
		//防止拖拽触发window.resize
		if(srcElement&&!srcElement.tagName){
			setHeight();
		}
	});
});

function setHeight(){
	var winHeight = $(window).height()-120-$("#tool-box").height();
	$(".box-body").height(winHeight);
	$(".task-list").slimScroll({
		height:winHeight,
		alwaysVisible: true,
		color:"#eee"
	});
}

function loadData(autoLoad){
	if(!autoLoad){
		layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
	}
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
	//进出港类型
	var ioType = $("#ioType").val();
	//未分配
	$("#flight4").load(ctx+"/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:ioType,
		taskState:0,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		viewDetail("flight4");
	});
	//已排
	$("#flight2").load(ctx+"/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:ioType,
		taskState:2,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		viewDetail("flight2");
	});
	//完成
	$("#flight3").load(ctx+"/progress/getTaskItmes",{
		jobType:$("#jobType").val(),
		ioType:ioType,
		taskState:3,
		fltNo:fltNo,
		airlineGroup:airlineGroup,
		alarmLevel:alarmLevel,
		updateSession:0
	},function(){
		layer.closeAll();
		viewDetail("flight3");
	});
}
/**
 * 查看详情
 */
var tempFltId = null;
function viewDetail(divId){
	$('#'+divId+' .task-item').mousemove(function(e) {
		var currentDiv = $(this);
		var parentDiv = $(this).parent();
		var fltid = "";
		var infltid = currentDiv.data("infltid");
		if(infltid){
			fltid = infltid;
		}
		var outfltid = currentDiv.data("outfltid");
		if(outfltid){
			if(fltid){
				fltid+=",";
			} 
			fltid+=outfltid;
		}
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
					url : ctx + "/progress/getTasks",
					data : {
						jobType:$("#jobType").val(),
						fltid : fltid,
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
		content:[ctx+"/progress/fltRemarkForm?fltid="+$(itemDom).data("fltid")],
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