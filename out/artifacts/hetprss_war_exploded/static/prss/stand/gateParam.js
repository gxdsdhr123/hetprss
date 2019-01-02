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
	//登机口搜素
	$("#unfixedSearch").keyup(function(event) {
		if (event.keyCode == 13) {
			applyUnfixed();
		}
	});
	//设置滚动条--begin
	var scroll = $("#mtext");
	scroll.css("position", "relative");
	new PerfectScrollbar(scroll[0]);
	//设置滚动条--end
});


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

function applyUnfixed(){
	var unfixedName = $("#unfixedSearch").val();
	$("#allUnfixedUL li").each(function(index,item){
		var code = $(item).data("code")+"";
		if(code.indexOf(unfixedName)<0){
			$(item).hide();
		} else {
			$(item).show();
		}
	});
}