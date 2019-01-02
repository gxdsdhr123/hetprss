window.clickRowId = "";
var layer;// 初始化layer模块
var iframe;
var form;
var HIDE_NONEXECUTION = 0;//隐藏未执行航班0：否，1：是
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		form=layui.form;
	});
	initOptions();
	//禁用鼠标右键
	$(document).contextmenu(function (e) {
	    e.preventDefault();
	});
	var ganttOptions = {
		url:ctx+"/flightdynamic/gantt/fdWalkthroughGanttData",
		showEmptyRow:true,
		height:$("body").height()-$("#tool-box").height(),
		yData:{
			url:ctx+"/flightdynamic/gantt/fdWalkthroughGanttYData",
		}
		/*onclickOverlay:function(o){
			$.ajax({
				type:'post',
				url:ctx + "/flightDynamic/getGanttDetail",
				async:false,
				data:{
					id:o.id,
					inFltid:o.infltid,
					outFltid:o.outfltid
				},
				dataType:'json',
				success:function(detail){
					o.detail = detail;
				}
			});
		}*/
	}
	$("#SJgantt").SJgantt(ganttOptions);
	// 刷新
	if(!sessionStorage || !sessionStorage[$("#loginName").val()+"refreshGantt"] || sessionStorage[$("#loginName").val()+"refreshGantt"] == "null" || sessionStorage[$("#loginName").val()+"refreshGantt"]=="undefined" || typeof(sessionStorage[$("#loginName").val()+"refreshGantt"]) == "undefined"){
		autoReload("10");
	}else{
		autoReload(sessionStorage[$("#loginName").val()+"refreshGantt"]);
	}
	$("#refresh").click(function() {
		//clearInterval(intervalObj);
		$("#SJgantt").SJgantt('refresh');
	})
	$("#refresh-c").click(function() {
		layer.open({
			title : '自定义刷新',
			content : '每<input type="number" min="1" class="layui-input layui-sm" style="width:120px;margin:0px 10px;display:inline-block"/>秒钟自动刷新',
			yes : function(index, layero) {
				var refreshTime = layero.find("input").val();
				if (refreshTime != null && refreshTime != "") {
					autoReload(refreshTime);
				}
				layer.close(index);
			}
		});
	})
	//锁定航班
	$("#lock").click(function(){
		if(!clickRow){
			layer.msg('请先选择航班',{icon:7});
			return false;
		}
		if(clickRow.type=="0" && clickRow.status != "3" && clickRow.lock == false){
			var loading = layer.load(1);
			$.ajax({
				type:'post',
				url: ctx + "/flightdynamic/gantt/lockFlight",
				data:{
					id:clickRow.id
				},
				success:function(msg){
					layer.close(loading);
					if(msg == "success"){
						layer.msg("锁定成功",{icon:1});
						clickRow.lock = true;
					}else{
						layer.msg("锁定失败",{icon:2});
					}
				}
			});
		}
	});
	//解锁航班
	$("#unlock").click(function(){
		if(!clickRow){
			layer.msg('请先选择航班',{icon:7});
			return false;
		}
		if(clickRow.lock){
			var loading = layer.load(1);
			$.ajax({
				type:'post',
				url: ctx + "/flightdynamic/gantt/unlockFlight",
				data:{
					id:clickRow.id
				},
				success:function(msg){
					layer.close(loading);
					if(msg == "success"){
						layer.msg("解锁成功",{icon:1});
						clickRow.lock = false;
					}else{
						layer.msg("解锁失败",{icon:2});
					}
				}
			});
		}
	});
	//增加驻场
	$("#stay").click(function(){
		layer.open({
			type: '1',
			title : "增加驻场",
			area : ['400px','200px'],
			content : $("#stayDiv"),
			btn : ['保存','取消'],
			yes:function(index,layero){
				var actNum = $("#stayActnum").val();
				var stand = $("#stayStand").val();
				if(!actNum || actNum == ""){
					layer.msg("请输入机号",{icon:7});
					return false;
				}
				if(!stand || stand == ""){
					layer.msg("请输入机位",{icon:7});
					return false;
				}
				var loading = layer.load(1);
				$.ajax({
					type:'post',
					url: ctx + "/flightdynamic/gantt/addStay",
					data:{
						actNum:actNum,
						stand:stand
					},
					success:function(msg){
						layer.close(loading);
						if(msg == "success"){
							layer.msg("增加驻场成功",{icon:1});
							$("#SJgantt").SJgantt('refresh');
						}else{
							layer.msg("增加驻场失败",{icon:2});
						}
					}
				});
				layer.close(index);
			}
		});
	});
	//删除驻场
	$("#delstay").click(function(){
		if(!clickRow || clickRow.type != "2"){
			layer.msg('请先选择驻场航班',{icon:7});
			return false;
		}
		layer.confirm("确定删除该驻场航班吗？",function(index){
			var loading = layer.load(1);
				$.ajax({
					type:'post',
					url: ctx + "/flightdynamic/gantt/delStay",
					data:{
						id:clickRow.id,
						fltid:clickRow.inFltid
					},
					success:function(msg){
						layer.close(loading);
						if(msg == "success"){
							layer.msg("删除驻场成功",{icon:1});
							$("#SJgantt").SJgantt('clearChoose');
							$("#SJgantt").SJgantt('refresh');
						}else{
							layer.msg("删除驻场失败",{icon:2});
						}
					}
				});
			layer.close(index);
		});
	});
	//停用机位
	$("#stop").click(function(){
		stopStandForm("");
	});
	//显示隐藏机位
	$("#toggle").click(function(){
		layer.open({
			type: '1',
			title : "显示机位",
			area : ['400px','200px'],
			content : $("#visibleDiv"),
			btn : ['显示','取消'],
			success:function(layero,index){
				layero.find("select").empty();
				layero.find("select").append($("<option>加载中...</option>"))
				$.ajax({
					type:'post',
					url:ctx+"/flightdynamic/gantt/getHidedStand",
					dataType:'json',
					success:function(data){
						layero.find("select").empty();
						for(var i=0;i<data.length;i++){
							var option = $("<option value="+data[i].id+">"+data[i].text+"</option>");
							layero.find("select").append(option);
						}
					}
				});
			},
			yes:function(index,layero){
				var stand = layero.find("select").val();
				if(!stand || stand == ""){
					layer.msg("请选择机位",{icon:7});
					return false;
				}
				var loading = layer.load(1);
				$.ajax({
					type:'post',
					url: ctx + "/flightdynamic/gantt/showStand",
					data:{
						stand:stand
					},
					success:function(msg){
						if(msg == "success"){
							$("#SJgantt").SJgantt('refresh');
						}
						layer.close(loading);
					}
				});
				layer.close(index);
			}
		});
	})
	//保存机位
	$("#save").click(function(){
		var loading = layer.load(1);
		$.ajax({
			type:'post',
			url: ctx + "/flightdynamic/gantt/saveStand",
			success:function(msg){
				if(msg == "success"){
					layer.msg("保存机位成功",{icon:1});
					$("#SJgantt").SJgantt('refresh');
				}else{
					layer.msg("保存机位失败",{icon:2});
				}
				layer.close(loading);
			}
		});
	})
	
	//取消调整
	$("#cancel").click(function(){
		layer.confirm('确定取消调整机位吗？', {icon: 3, title:'提示'}, function(index){
			var loading = layer.load(1);
			$.ajax({
				type:'post',
				url: ctx + "/flightdynamic/gantt/cancelStand",
				success:function(msg){
					if(msg == "success"){
						layer.msg("取消机位调整成功",{icon:1});
						$("#SJgantt").SJgantt('refresh');
					}else{
						layer.msg("取消机位调整失败",{icon:2});
					}
					layer.close(loading);
				}
			});
		});
	})
	
	//拖飞机
	$("#drag").click(function(){
		if(clickRow.status == "3"){
			return false;
		}
		var id = clickRow == null?'':clickRow.id;
		var inFltid = clickRow == null?'':clickRow.inFltid;
		var outFltid = clickRow == null?'':clickRow.outFltid;
		var inFltNum = clickRow == null?'':clickRow.inFltNum;
		var outFltNum = clickRow == null?'':clickRow.outFltNum;
		var actNum = clickRow == null?'':clickRow.actNum;
		var stand = clickRow == null?'':clickRow.stand;
		var type = clickRow == null?'':clickRow.type;
		var start = clickRow == null?'':clickRow.start;
		var end = clickRow == null?'':clickRow.end;
		layer.open({
			type: 2,
			title : "拖飞机",
			area : ['700px','400px'],
			scrollbar: false,
			content : ctx + "/flightdynamic/gantt/dragFlightPage?inFltid="+inFltid+
			"&outFltid="+outFltid+"&inFltNum="+inFltNum+"&outFltNum="+outFltNum+
			"&actNum="+actNum+"&stand="+stand+"&type="+type+"&id="+id+"&start="+start+"&end="+end,
			end: function () {
				$("#SJgantt").SJgantt('refresh');
            }
		});
	})
	
	//今日图形，次日图形下拉框
	$("#quickDate").on('change',function(i){
		var value = $(this).val();
		if(value == "today"){
			$("#SJgantt").SJgantt(ganttOptions);
		}else{
			var curDate = new Date();
			var nextDate = new Date(curDate.getTime() + 24*60*60*1000); //次日
			var str = nextDate.pattern("yyyy-MM-dd hh:mm:ss")
			$("#SJgantt").SJgantt('refresh',str);
		}
	})
	
	//机位调整
	$("#takeStand").on('click',function(i){
		layer.open({
			type: '1',
			title : "机位调整",
			area : ['400px','240px'],
			content : $("#takeStandDiv"),
			btn : ['预排'],
			success:function(layero,index){
				layero.find("input").val("");
			},
			yes:function(index,layero){
				var beginTm = layero.find("#beginTime").val();
				var endTm = layero.find("#endTime").val();
				if(beginTm == ""){
					layer.msg("请选择开始时间！",{icon:7});
					return false;
				}
				if(endTm == ""){
					layer.msg("请选择结束时间！",{icon:7});
					return false;
				}
				var loading = layer.load(1);
				$.ajax({
					type:'post',
					async:false,
					url: ctx + "/flightdynamic/gantt/takeStand",
					data:{
						beginTm:beginTm,
						endTm:endTm
					},
					success:function(msg){
						if(msg == "success"){
							$("#SJgantt").SJgantt('refresh');
						}
						layer.close(loading);
					}
				});
				layer.close(index);
			}
		});
	})
	//隐藏未执行航班
	$("#hideNonexecution").click(function(){
		if(HIDE_NONEXECUTION==0){//隐藏
			HIDE_NONEXECUTION = 1;
			$(this).text("显示未执行航班");
		} else {//显示
			HIDE_NONEXECUTION = 0;
			$(this).text("隐藏未执行航班");
		}
	});
	//搜索
	$('#search').on('keypress',function(event){ 
        if(event.keyCode == 13) {  
        	$("#SJgantt").SJgantt('search',$('#search').val());
        }  
    });
});
function initOptions(){
	$.ajax({
		type:'post',
		url:ctx+"/flightdynamic/gantt/getDim?idCol=acreg_code&textCol=acreg_code&tableName=dim_acreg",
		dataType:'json',
		success:function(res){
			$("#stayActnum").select2({
				data:res.results
			});
		}
	});
	$.ajax({
		type:'post',
		url:ctx+"/flightdynamic/gantt/getDim?idCol=bay_code&textCol=bay_code&tableName=dim_bay",
		dataType:'json',
		success:function(res){
			$("#stayStand").select2({
				data:res.results
			});
		}
	});
}
var intervalObj = null;
var refreshUnit = 1000;//刷新单位是秒
function autoReload(interval){
	if(interval<10){
		layer.msg("最小刷新频率不要小于10秒！",{icon:7});
		return false;
	}
	sessionStorage.setItem($("#loginName").val()+"refreshGantt",interval);
	var time = Number(interval) * refreshUnit;
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟";
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		$("#SJgantt").SJgantt('refresh');
	}, time);
}
function dateFilter() {
	WdatePicker({
		maxDate:'%y-%M-%d',
		dateFmt : 'yyyy-MM-dd',
		onpicking : function(dp) {	
			$("#SJgantt").SJgantt('refresh',dp.cal.getNewDateStr()+" 00:00:00");
		}
	});
}

function doRelease(e){
	var id = $("#stop").data("id");
	$.ajax({
		type:'post',
		url: ctx + "/flightdynamic/gantt/cancelStop",
		data : {
			id : id
		},
		success:function(msg){
			if(msg == "success"){
				layer.msg("恢复机位成功",{icon:1});
				$("#SJgantt").SJgantt('refresh');
			}else{
				layer.msg("恢复机位失败",{icon:2});
			}
			layer.close(loading);
		}
	});
	$("#stopMenu").hide();
}
var iframe;
function changeStand(e){
	iframe = layer.open({
		type : 2,
		title : "更改机位",
		closeBtn : false,
		area : ['500px','300px'],
		content : ctx+"/fdChange/listChangeStand?id="+clickRow.id,
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveForm();
			return false;
		}
	});
	$("#rightMenu").hide();
}
function changeGate(e){
	iframe = layer.open({
		type : 2,
		title : "更改登机口",
		closeBtn : false,
		area : ['500px','300px'],
		content : ctx+"/fdChange/listChangeGate?id="+clickRow.outFltid,
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveForm();
			return false;
		}
	});
	$("#rightMenu").hide();
}
function formSubmitCallback() {// 子页面提交完成后，父页面关闭弹出层及刷新表格
	layer.close(iframe);
	$("#SJgantt").SJgantt('refresh');
}

/**
 * 停用机位
 * @param id
 */
function stopStandForm(id){
	var stopInfo = {
			stand:"",
			start:"",
			end:"",
			remark:""
	};
	if(id){
		var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
		$.ajax({
			type:'post',
			url: ctx + "/flightdynamic/gantt/getStopStand",
			async:false,
			data:{
				id:id
			},
			dataType:"json",
			success:function(data){
				stopInfo = data;
				layer.close(loading);
			}
		});
	}
	$("#stopStand").val(stopInfo.stand);
	$("#stopStartTime").val(stopInfo.start);
	$("#stopEndTime").val(stopInfo.end);
	$("#stopRemark").val(stopInfo.remark);
	layer.open({
		type: '1',
		title : "停用机位",
		area : ['500px','400px'],
		content : $("#stopDiv"),
		btn : ['保存','取消'],
		yes:function(index,layero){
			var stand = $("#stopStand").val();
			var start = $("#stopStartTime").val();
			var end = $("#stopEndTime").val();
			var remark = $("#stopRemark").text();
			if(!stand || stand == ""){
				layer.msg("请输入机位",{icon:7});
				return false;
			}
			if(!start || start == ""){
				layer.msg("请输入停用开始时间",{icon:7});
				return false;
			}
			if(!end || end == ""){
				layer.msg("请输入停用结束时间",{icon:7});
				return false;
			}
			var loading = layer.load(1);
			$.ajax({
				type:'post',
				url: ctx + "/flightdynamic/gantt/addStop",
				data:{
					stand:stand,
					start:start,
					end:end,
					remark:remark,
					id:id
				},
				success:function(msg){
					layer.close(loading);
					if(msg == "success"){
						layer.msg("停用机位成功",{icon:1});
						$("#SJgantt").SJgantt('refresh');
					}else{
						layer.msg("停用机位失败",{icon:2});
					}
				}
			});
			layer.close(index);
		}
	});
}
/**
 * 修改停车航班机号
 * @param id id
 * @param actNum 机号
 * @param stand 机位
 */
function chgStayAircraftNo(id,actNum,inFltid){
	console.log(id+","+actNum+","+inFltid);
	if(inFltid==""){
		layer.msg("航班ID不存在，无法修改",{icon:7});
	}else{
		$("#stayActnumOld").val(actNum);
		$("#stayActnumOld").attr("readonly","readonly");
		
		layer.open({
			type: '1',
			title : "修改停场机号",
			area : ['500px','250px'],
			content : $("#editStayDiv"),
			btn : ['保存','取消'],
			yes:function(index,layero){
				var stayActnumNew = $("#stayActnumNew").val();
				if(!stayActnumNew || stayActnumNew == ""){
					layer.msg("请输入机号",{icon:7});
					return false;
				}
				if(stayActnumNew == actNum ){
					layer.msg("值未改变",{icon:7});
					return false;
				}
				var loading = layer.load(1);
				$.ajax({
					type:'post',
					url: ctx + "/flightdynamic/gantt/editStay",
					data:{
						stayActnumOld:actNum,
						stayActnumNew:stayActnumNew,
						id:id,
						fltid:inFltid
					},
					success:function(msg){
						layer.close(loading);
						if(msg == "success"){
							layer.msg("停场机号修改成功",{icon:1});
							$("#SJgantt").SJgantt('refresh');
						}else if(msg=="notexist"){
							layer.msg("机号不存在",{icon:2});
						}else{
							layer.msg("停场机号修改失败",{icon:1});
						}
					}
				});
				layer.close(index);
			}
		});

	}
}
/**
 * 双击修改航班动态
 * @param fltId
 */
var editWin = null;
function openFltEdit(fltId,status){
	if(fltId){
		var loading = layer.load(1);
		$.ajax({
			type:'post',
			url:ctx + "/flightdynamic/gantt/getInOutFlight",
			async:false,
			data:{
				fltId:fltId
			},
			dataType:'json',
			error:function(){
				layer.close(loading);
				layer.msg("获取航班信息失败",{icon:7});
			},
			success:function(data){
				layer.close(loading);
				if(data&&data.length>0){
					var inOutFltId = data[0];
					var inFltId = inOutFltId.inFltId?inOutFltId.inFltId:"";
					var outFltId = inOutFltId.outFltId?inOutFltId.outFltId:"";
					if(status == "3"){
						editWin = layer.open({
							type : 2,
							title : false,
							closeBtn : false,
							area:["100%","100%"],
							content : ctx + "/flightDynamic/form?ifShow=false&inFltId="+inFltId+"&outFltId="+outFltId+"&his=true",
							btn : ['返回']
						});
					}else{
						editWin = layer.open({
							type : 2,
							title : false,
							closeBtn : false,
							area:["100%","100%"],
							content : ctx + "/flightDynamic/form?ifShow=false&inFltId="+inFltId+"&outFltId="+outFltId,
							btn : ['保存','新增航班','删除行','返回'],
							yes : function(index, layero) {
								//保存
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.save();
							},
							btn2 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.addRow();
								return false;
							},
							btn3 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.removeRow();
								return false;
							}
						});
					}
				} else {
					layer.msg("未找到该航班",{icon:7});
					return false;
				}
			}
		});
	} else {
		layer.msg("没有选择航班",{icon:7});
		return false;
	}
}
/**
 * 修改保存成功回调
 */
function saveSuccess(){
	if(editWin){
		layer.close(editWin);
		$("#SJgantt").SJgantt('refresh');
	}
}
function search(obj){
	$("#SJgantt").SJgantt('search',$(obj).val());
}
//右键发送消息
function openMessage(obj){
	var id = $(obj).data("id");
	var ioFlag = $(obj).data("io");
	var flightDate = clickRow.fltDate;
	var in_fltid = clickRow.inFltid || "";
	var out_fltid = clickRow.outFltid || "";
	var fltid = ioFlag == "I"?in_fltid:out_fltid;
	var flightNumber = ioFlag == "I"?clickRow.inFltNum:clickRow.outFltNum;
	parent.openMessage(fltid, flightNumber, flightDate, in_fltid, out_fltid, id, ioFlag);
	$("#rightMenu").hide();
}