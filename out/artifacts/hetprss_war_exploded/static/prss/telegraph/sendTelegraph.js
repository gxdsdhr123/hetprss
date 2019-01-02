var layer;
var tempData;
var _sitaType;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(function() {
	//设置滚动条--begin
	var mtext = $("#mtext");
	mtext.css("position", "relative");
	new PerfectScrollbar(mtext[0]);
	//设置滚动条--end
})

function doSend() {
	$("#submitType").val("send");
	$('#createForm').attr("action",ctx + '/telegraph/auto/send');
	$('#createForm').submit();
}

function send(){
	if(sendCheck()){
		var flightNumber = $("#flightNumber").val();
		var flightDate = $("#flightDate").val();
		var fiotype = $("#fiotype").val();
		var telegraphType = $("#telegraphType").val();
		var mtext = $("#mtext").val();
		var sita = $("#sita").val();
		var sendAddress = $("#sendAddress").val();
		var priority = $("#priority").val();
		$.ajax({
			type : 'post',
			url : ctx + "/telegraph/history/sendManual",
			data : {
				flightNumber : flightNumber,
				flightDate : flightDate,
				fiotype : fiotype,
				telegraphType : telegraphType,
				mtext : mtext,
				sita : sita,
				sendAddress : sendAddress,
				priority : priority
			},
			async : true,
			dataType : 'json',
			success : function(data) {
				console.info(data);
				var index = parent.layer.getFrameIndex(window.name);
		        parent.layer.close(index); //再执行关闭 
			},error : function(msg){
				layer.msg("操作失败",{time:1000,icon:2});
			}
		});
	}
	return false;
}

function sendCheck(){
	var flightNumber = $("#flightNumber").val();
	var flightDate = $("#flightDate").val();
	var fiotype = $("#fiotype").val();
	var telegraphType = $("#telegraphType").val();
	var mtext = $("#mtext").val();
	var sita = $("#sita").val();
	if(flightNumber == null || flightNumber == ''){
		layer.msg('请输入航班号', {icon: 0,time: 1000});
		return false;
	}
	if(flightDate == null || flightDate == ''){
		layer.msg('请选择航班日期', {icon: 0,time: 1000});
		return false;
	}
	if(fiotype == null || fiotype == ''){
		layer.msg('请选择进出港类型', {icon: 0,time: 1000});
		return false;
	}
	if(telegraphType == null || telegraphType == ''){
		layer.msg('请选择报文类型', {icon: 0,time: 1000});
		return false;
	}
	if(mtext == null || mtext == ''){
		layer.msg('请输入报文内容', {icon: 0,time: 1000});
		return false;
	}
	if(sita == null || sita == ''){
		layer.msg('请选择或者输入报文地址', {icon: 0,time: 1000});
		return false;
	}
	return true;
}

//发送接收人名称获取方法	
function senderList(){	
	var sitaType = '1';
	var ids = '';
	layui.use(['form','element']);
	layer.open({
		type: 2, 
		title:'添加名称',
		maxmin:false,
//		shade:0,
		area: ["700px","420px"],
		content: ctx + "/telegraph/templ/srList?sitaType=" + sitaType + "&ids=" + ids,
		success:function(layero,index){

		}
	});
}
//发送接受名称赋值
function setValue(row){
	var sita = row.TG_ADDRESS;
	sita = sita.replace(/,/g,';');
	$('#sita').val(sita);
}
