var layer,form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
    form.on('select(groupSel)', function(data){
        var id = data.value;
        getEmpInfo(id);
    });  
});

var left_scrollbar;
var right_scrollbar;

$(function(){
	if(flag=="create"){
		$("#contentDiv").show();
		initCreate();
	}else if(flag=="modify"){
		$("#contentDiv").hide();
		document.getElementById("stime2").disabled="";
		document.getElementById("etime2").disabled="";
		document.getElementById("bzBtn2").disabled="";
		document.getElementById("stime3").disabled="";
		document.getElementById("etime3").disabled="";
		document.getElementById("bzBtn3").disabled="";
	}
	$(".btn2").click(function(){
		var id1=$("#sid1").val();
		var id2=$("#sid2").val();
		var id3=$("#sid3").val();
		var id = $(this).attr("id");
		siframe = layer.open({
			type: 2, 
			title:"选择班制",
			offset: '10px',
			area:["450px","350px"],
			content: ctx+"/arrange/empplan/showShifts?type="+id+"&id1="+id1+"&id2="+id2+"&id3="+id3,
			end: function(index, layero){
				
			}
		});
	});
	$("#empInfoUL").css("position","relative");
	left_scrollbar = new PerfectScrollbar("#empInfoUL");
	right_scrollbar = new PerfectScrollbar("#selectEmpInfoUL");
});

function initCreate(){
	$("#groupInfo option").eq(0).attr("selected",true);
	$("#leftDiv .hr").draggable({
      connectToSortable: ".choosedField",
      helper: "clone",
      revert: "invalid",
      stop: function( event, ui ) {
		ui.helper.dblclick(function(){
			ui.helper.remove();
		});
	  }
	});
	$(".sortable").sortable({
		connectWith: ".sortable",
		stop: function( event, ui ) {
			setTimeout(function(){
				ui.item.removeClass("bechoose");
			},10);
			haveEmpInfo();
		}
	});

	$("#pushleft").click(function(){
		var bechoose = $("#rightDiv .bechoose");
		bechoose.removeClass("bechoose");
		$("#leftDiv .list-group").append(bechoose);
		haveEmpInfo();
		left_scrollbar.update();
		right_scrollbar.update();
	});
	$("#pushright").click(function(){
		var bechoose = $("#leftDiv .bechoose");
		bechoose.removeClass("bechoose");
		bechoose.show();
		$("#rightDiv .list-group").append(bechoose);
		haveEmpInfo();
		left_scrollbar.update();
		right_scrollbar.update();
	});
	$("#pushAllright").click(function(){
		var bechoose = $("#leftDiv .list-group-item");
		bechoose.removeClass("bechoose");
		bechoose.show();
		$("#rightDiv .list-group").append(bechoose);
		haveEmpInfo();
		left_scrollbar.update();
		right_scrollbar.update();
	});
	
	getAllEmp();
	
}

function getAllEmp(){
	$.ajax({
		type:'post',
		url: ctx+"/arrange/empplan/getAllEmpInfo",
		async:false,
		data : null,
		dataType:"json",
		success:function(result){
			$("#empInfoUL").empty();
			for(var i in result){
				$("#empInfoUL").append("<li class='list-group-item hr' data-code='"+result[i].ID+"'>" + result[i].NAME +"</li>");
			}
		}
	});
	initClk();
}

function haveEmpInfo(){
	var lis = $("#rightDiv li");
	var temp = "";
	lis.each(function(){
		 if(temp.indexOf($(this).data("code"))!=-1)
		 $(this).remove();
		 temp += $(this).data("code")+",";
	});
}

function getEmpInfo(id){
	if(id=='all'){
		getAllEmp();
	}else{
		$.ajax({
			type:'post',
			url: ctx+"/arrange/empplan/getEmpInfoById",
			async:false,
			data : {
				groupId : id
			},
			dataType:"json",
			success:function(result){
				$("#empInfoUL").empty();
				for(var i in result){
					$("#empInfoUL").append("<li class='list-group-item hr' data-code='"+result[i].ID+"'>" + result[i].NAME +"</li>");
				}
			}
		});
		initClk();
	}
}

function initClk(){
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

function checkDate(){
	var s1 = $("#busyInterval").val();
	var s2 = $("#idleInterval").val();
	if(s1==""){
		$("#busyInterval").val("0");
	}
	if(s2==""){
		$("#idleInterval").val("0");
	}
	if(flag=="create"){
		var lis = $("#rightDiv li");
		if(lis.length==0){
			layer.msg('请选择人员', {
				icon : 2
			});
			return false;
		}
	}
	s1 = $("#busyInterval").val();
	s2 = $("#idleInterval").val();
	if(!s1.match(/^\-?\d*$/)){
		layer.msg('工作间隔只能填写数字，可填写负数', {
			icon : 2
		});
		return false;
	}
	
	if(!s2.match(/^\-?\d*$/)){
		layer.msg('工作间隔只能填写数字，可填写负数', {
			icon : 2
		});
		return false;
	}
	
	var st1=$("#stime1").val();
	var et1 = $("#etime1").val();
	if(st1==""||et1==""){
		layer.msg('工作时段一不能为空！', {
			icon : 2
		});
		return false;
	}
	return true;
}

function getDatas(){
	var lis = $("#rightDiv li");
	var st1=$("#stime1").val();
	var et1 = $("#etime1").val();
	var st2=$("#stime2").val();
	var et2 = $("#etime2").val();
	var st3=$("#stime3").val();
	var et3 = $("#etime3").val();
	var s1 = $("#busyInterval").val();
	var s2 = $("#idleInterval").val();
	var stp1 = dateTp+" "+st1.substring(0,2)+":"+st1.substring(2,4)+":00";
	var etp1 = "",stp2 = "",etp2 = "",stp3 = "",etp3 = "";
	if(et1.indexOf("+")>-1){
		etp1 = dateAddDays(dateTp,'1')+" "+et1.substring(0,2)+":"+et1.substring(2,4)+":00";
	}else{
		etp1 = dateTp+" "+et1.substring(0,2)+":"+et1.substring(2,4)+":00";
	}
	if(st2!=""){
		stp2 = dateTp+" "+st2.substring(0,2)+":"+st2.substring(2,4)+":00";
	}
	if(et2!=""){
		if(et2.indexOf("+")>-1){
			etp2 = dateAddDays(dateTp,'1')+" "+et2.substring(0,2)+":"+et2.substring(2,4)+":00";
		}else{
			etp2 = dateTp+" "+et2.substring(0,2)+":"+et2.substring(2,4)+":00";
		}
	}
	if(st3!=""){
		stp3 = dateTp+" "+st3.substring(0,2)+":"+st3.substring(2,4)+":00";
	}
	if(et3!=""){
		if(et3.indexOf("+")>-1){
			etp3 = dateAddDays(dateTp,'1')+" "+et3.substring(0,2)+":"+et3.substring(2,4)+":00";
		}else{
			etp3 = dateTp+" "+et3.substring(0,2)+":"+et3.substring(2,4)+":00";
		}
	}
	var haveIds = [];
	for(var i=0;i<lis.length;i++){
		var li = $(lis[i]);
		haveIds.push(li.data("code"));
	}
	$.unique(haveIds);
	var datas = [];
	for(var i=0;i<haveIds.length;i++){
		var name = $("#rightDiv li[data-code="+haveIds[i]+"]").html();
		var data = {
			workerId:haveIds[i],
			pdate:dateTp,
			busyInterval:s1,
			idleInterval:s2,
			sortnum:i+1,
			shiftsType:"0",
			shiftsId:$("#sid1").val(),
			stime1:stp1,
			etime1:etp1,
			stime1Label:st1,
			etime1Label:et1,
			stime2:stp2,
			etime2:etp2,
			stime2Label:st2,
			etime2Label:et2,
			stime3:stp3,
			etime3:etp3,
			stime3Label:st3,
			etime3Label:et3,
			loginName:name
		}
		datas.push(data);
	}
	return JSON.stringify(datas);
}

function getModifyDatas(){
	var datas = [];
	var st1=$("#stime1").val();
	var et1 = $("#etime1").val();
	var st2=$("#stime2").val();
	var et2 = $("#etime2").val();
	var st3=$("#stime3").val();
	var et3 = $("#etime3").val();
	var s1 = $("#busyInterval").val();
	var s2 = $("#idleInterval").val();
	var stp1 = dateTp+" "+st1.substring(0,2)+":"+st1.substring(2,4)+":00";
	var etp1 = "",stp2 = "",etp2 = "",stp3 = "",etp3 = "";
	if(et1.indexOf("+")>-1){
		etp1 = dateAddDays(dateTp,'1')+" "+et1.substring(0,2)+":"+et1.substring(2,4)+":00";
	}else{
		etp1 = dateTp+" "+et1.substring(0,2)+":"+et1.substring(2,4)+":00";
	}
	if(st2!=""){
		stp2 = dateTp+" "+st2.substring(0,2)+":"+st2.substring(2,4)+":00";
	}
	if(et2!=""){
		if(et2.indexOf("+")>-1){
			etp2 = dateAddDays(dateTp,'1')+" "+et2.substring(0,2)+":"+et2.substring(2,4)+":00";
		}else{
			etp2 = dateTp+" "+et2.substring(0,2)+":"+et2.substring(2,4)+":00";
		}
	}
	if(st3!=""){
		stp3 = dateTp+" "+st3.substring(0,2)+":"+st3.substring(2,4)+":00";
	}
	if(et3!=""){
		if(et3.indexOf("+")>-1){
			etp3 = dateAddDays(dateTp,'1')+" "+et3.substring(0,2)+":"+et3.substring(2,4)+":00";
		}else{
			etp3 = dateTp+" "+et3.substring(0,2)+":"+et3.substring(2,4)+":00";
		}
	}
	var data = {
		busyInterval:s1,
		idleInterval:s2,
		shiftsId:$("#sid1").val(),
		stime1:stp1,
		etime1:etp1,
		stime1Label:st1,
		etime1Label:et1,
		stime2:stp2,
		etime2:etp2,
		stime2Label:st2,
		etime2Label:et2,
		stime3:stp3,
		etime3:etp3,
		stime3Label:st3,
		etime3Label:et3
	}
	datas.push(data);
	return JSON.stringify(datas);
}

function dateAddDays(dataStr,dayCount) {
    var strdate=dataStr;
    var isdate = new Date(strdate.replace(/-/g,"/"));
    isdate = new Date((isdate/1000+(86400*dayCount))*1000);
    var year = isdate.getFullYear();  
    var month = isdate.getMonth() + 1;  
    var day = isdate.getDate();  
    if (month < 10) {  
        month = '0' + month;  
    }  
    if (day < 10) {  
        day = '0' + day;  
    }  
    var pdate = year + '-' + month + '-' + day;  
    return pdate;
}
//人员模糊搜索
function search(e){
	var value = e.target.value.trim();
	$("#empInfoUL li").each(function(){
		if($(this).text().indexOf(value)==-1){
			$(this).hide();
		}else{
			$(this).show();
		}
	})
}
