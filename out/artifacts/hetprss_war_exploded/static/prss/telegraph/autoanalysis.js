layui.use("layer");
$(function(){
	var height = 400 ;
	$(".textarea").height(height);
	$(".analysis").height(height);
})

function analysis(){
	var mtext = $(".textarea").val();
	if(mtext==null || mtext == ''){
		layer.msg("解析报文不能为空",{icon:0,time:1000});
		return false;
	}
	$.ajax({
		type : 'post',
		url : ctx + "/telegraph/analysis/doAnalysis",
		data : {
			mtext : mtext
		},
		async : true,
		dataType : 'json',
		success : function(data) {
			if(data.errNum==0){
				setValue(data);
				if(data.errMsg != null && data.errMsg != '')
					layer.msg(data.errMsg,{time:1000,icon:2});
			} else {
				layer.msg(data.errMsg,{time:1000,icon:2});
			}
			
		},error : function(msg){
			layer.msg("解析出错",{time:1000,icon:2});
		}
	});
}

function setValue(data){
	$("#fligthNumber").text(data.fltNumber);
	$("#jl").text(data.sta);
	$("#jq").text(data.std);
	$("#eta").text(data.aodbETA);
	$("#jw").text(data.aodbActstand);
	$("#jx").text(data.aodbActtype);
	$("#bweta").text(data.eta);
	$("#bwjh").text(data.aircraft);
	$("#bwjx").text(data.acttype);
	$("#peg").text(data.px);
	$("#delayInfo").text(data.delayCode);
	$("#delayTime").text(data.delayTime);
	$("#si").text(data.si);
	if(data.TelegraphETA == "1"){
		$("#eta").css("color","red");
		$("#bweta").css("color","red");
	}
	if(data.TelegraphAircraf == "1"){
		$("#bwjx").css("color","red");
		$("#jx").css("color","red");
	}
}
function reset(){
	$("#fligthNumber").text('');
	$("#jl").text('');
	$("#jq").text('');
	$("#eta").text('');
	$("#jw").text('');
	$("#jx").text('');
	$("#bweta").text('');
	$("#bwjh").text('');
	$("#bwjx").text('');
	$("#peg").text('');
	$("#delayInfo").text('');
	$("#delayTime").text('');
	$("#si").text('');
	$(".textarea").text('');
	$(".textarea").text('');
}
//判断obj是否为json对象  
function isJson(obj){  
    var isjson = typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;   
    return isjson;  
} 

function getHsonLength(json){
    var jsonLength=0;
    for (var i in json) {
        jsonLength++;
    }
    return jsonLength;
}
function iter(data,flag){
	var html='';
	$.each(data,function(key){
		if(!(key =='FLTID' || key == 'result')){
			var value = data[key];
			if(!isJson(value)){
				html += "<strong>"+key+"：</strong>" + data[key];
				if(flag==1){
					html +="</br>"
				} else {
					html +="&nbsp;"
				}
			} else {
				if(flag==1){
					html += iter(value,2)+"</br>";
				} else {
					html += "<strong>"+key+"：</strong>" + iter(value,2)+"</br>";
				}
			}
		}
	})
	return html;
}

function back(){
	layer.closeAll();
}