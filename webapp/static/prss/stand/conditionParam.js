$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	$(".sortable").sortable({
		connectWith: ".sortable",
		stop: function( event, ui ) {
			setTimeout(function(){
				ui.item.removeClass("bechoose");
			},10)
		}
	});
	initFunc4();
	init();
	//机位搜素
	$("#unfixedSearch").keyup(function(event) {
		if (event.keyCode == 13) {
			applyUnfixed();
		}
	});
});
var page = 1;

function applyUnfixed(){
	var unfixedName = $("#unfixedSearch").val();
	
	$("#allUnfixedUL").empty();
	$.each(itemList,function(index,item){
		var colCnName = item.COL_CNNAME;
		if(unfixedName ==null || unfixedName ==''){
			if(index<200){
				$("#allUnfixedUL").append("<li class='list-group-item hr' data-code='"+item.ID +"'>"+item.COL_CNNAME +"</li>")
			}
		} else {
			if(colCnName.indexOf(unfixedName)>-1){
				$("#allUnfixedUL").append("<li class='list-group-item hr' data-code='"+item.ID +"'>"+item.COL_CNNAME +"</li>")
			} 
		}
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

function initFunc4(){
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


function getSubmitData() {
	//表达式
	var items=[];
	$.each($("#selUnfixedUL li"),function(k,v){
		items.push($(v).data("code"));
	})
	return JSON.stringify(items);
}
