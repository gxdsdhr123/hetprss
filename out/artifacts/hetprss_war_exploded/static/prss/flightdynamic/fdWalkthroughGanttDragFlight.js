var layer;
var autoCompleteId;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	//人工确认添加滚动条
	$('.taskContainer').each(function(){
		new PerfectScrollbar(this);
	});
	
	//没有选择飞机打开弹窗，保存按钮禁用
	if($("#saveActstand").val()==""){
		$("#saveBut").attr("disabled","disabled");
	}
	
	//默认加载人工确认内容
	loadTable();
	//日期格式处理
    Date.prototype.pattern=function(fmt) {         
		var o = {         
		"M+" : this.getMonth()+1, //月份         
		"d+" : this.getDate(), //日         
		"h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
		"H+" : this.getHours(), //小时         
		"m+" : this.getMinutes(), //分         
		"s+" : this.getSeconds(), //秒         
		"q+" : Math.floor((this.getMonth()+3)/3), //季度         
		"S" : this.getMilliseconds() //毫秒         
		};         
		var week = {         
		"0" : "/u65e5",         
		"1" : "/u4e00",         
		"2" : "/u4e8c",         
		"3" : "/u4e09",         
		"4" : "/u56db",         
		"5" : "/u4e94",         
		"6" : "/u516d"        
		};         
		if(/(y+)/.test(fmt)){         
			fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
		}         
		if(/(E+)/.test(fmt)){         
			fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);         
		}         
		for(var k in o){         
			if(new RegExp("("+ k +")").test(fmt)){         
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
			}         
		}         
		return fmt;         
	}
	//保存按钮
	$("#saveBut").on('click',function(){
		var tempActstand = $("#tempActstand").val();
		if(tempActstand == ""){
			layer.msg("拖至机位不能为空!",{icon:2});
			return false;
		}
		var now = new Date();
		var sVal = $("#sBeginTime").val();
		var eVal = $("#sEndTime").val();
		var sStr = sVal.substring(0,4)+"/"+sVal.substring(4,6)+"/"+sVal.substring(6);
		if(sVal && new Date(sStr).getTime()<now.getTime()){
			layer.msg("“预计开始时间”不可小于当前时间！",{icon:2});
			return false;
		}
		var sBeginTime = sVal||now.pattern("yyyy-MM-dd HH:mm");
		var sEndTime = eVal||now.pattern("yyyy-MM-dd HH:mm");
		if((sVal == "" && eVal != "") || (sVal != "" && eVal == "")){
			layer.msg("“预计开始时间”和“预计结束时间”请同时选择或同时不选择！",{icon:2});
			return false;
		}
		var id = $("#mainId").val();
		var fltId = $("#fltId").val();
		var fltNo = $("#fltNo").val();
		var airCode = $("#airCode").val();
		var saveActstand = $("#saveActstand").val();
		var start = Date.parse(new Date($("#start").val()));
		var end = Date.parse(new Date($("#end").val()));
		var date = new Date();
		if(sVal == "" && (date<start || date>end)){
			layer.msg("该飞机不在本场，无法立即执行拖飞机操作！",{icon:2});
			return false;
		}
		if(sVal == "" && eVal == ""){
			autoCompleteId = id;
		}
		$.ajax({
			type:'post',
			async:false,
			url: ctx + "/flightdynamic/gantt/saveDragFlight",
			data:{
				id:id,
				fltId:fltId,
				fltNo:fltNo,
				airCode:airCode,
				saveActstand:saveActstand,
				tempActstand:tempActstand,
				sBeginTime:sBeginTime,
				sEndTime:sEndTime
			},
			success:function(msg){
				if(msg == "success"){
					layer.msg("拖飞机任务保存成功",{icon:1});
					loadTable();
				}else{
					layer.msg("拖飞机任务保存失败！",{icon:2});
				}
			}
		});
	})
})

//加载人工确认数据
function loadTable(){
	$.ajax({
		type:'post',
		url: ctx + "/flightdynamic/gantt/getTaskTable",
		dataType:"json",
		success:function(data){
			var div = "";
			$.each(data,function(k,v){
				if(autoCompleteId && v.DAYS_ID == autoCompleteId){
					doSave(v.ID,v.DAYS_ID,v.FLT_ID,v.TO_STAND,v.E_START_TM,v.E_END_TM);
					autoCompleteId == null;
				}else{
					div +="<div class='taskDiv'>"+
					"<div class='taskLine'>"+
						"任务"+(k+1)+"："+v.FLT_NO+"由"+v.FROM_STAND+"号拖至"+v.TO_STAND+"号机位，" +
						"时间"+v.E_START_TM.substring(10,16)+"-"+v.E_END_TM.substring(10,16)+""+
					"</div>"+
					"<button onClick='doSave("+v.ID+","+v.DAYS_ID+","+v.FLT_ID+","+v.TO_STAND+",\""+v.E_START_TM+"\",\""+v.E_END_TM+"\")' class='taskbut' type='button'>"+
					"	确认"+
					"</button>"+
					"<button onClick='doCencel("+v.ID+")' class='taskbut' type='button'>"+
					" 	删除"+
					"</button>"+
				  "</div>";
				}
			})
			$(".taskContainer").html("").append(div);
		}
	});
}

//人工确认按钮
function doSave(id,daysId,fltId,stand,sTm,eTm){
	$.ajax({
		type:'post',
		url: ctx + "/flightdynamic/gantt/updateTaskState",
		data:{
			id:id,
			daysId:daysId,
			fltId:fltId,
			stand:stand,
			sTm:sTm,
			eTm:eTm
		},
		success:function(data){
			if(data=="success"){
				layer.msg("确认成功",{icon:1});
				loadTable();
				$("#refresh",parent.document).click();
			}else{
				layer.msg("拖飞机任务保存失败！",{icon:2});
			}
		}
	});
}
//人工删除按钮 
function doCencel(id){
	$.ajax({
		type:'post',
		url: ctx + "/flightdynamic/gantt/deleteDragFlight",
		data:{
			id:id
		},
		success:function(data){
			if(data=="success"){
				layer.msg("删除成功",{icon:1});
				loadTable();
			}else{
				layer.msg("删除失败！",{icon:2});
			}
		}
	});
}