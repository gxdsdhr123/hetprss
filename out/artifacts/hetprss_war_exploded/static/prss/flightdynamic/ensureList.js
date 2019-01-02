$(window).on('resize',function(){
	resizeThis();
}).on('load',function(){
	resizeThis();
	tabEvent();
});
function resizeThis(){
	$('#myTabContent').height($(window).height() - $('#myTabs').outerHeight());
}

function tabEvent(){
	/********加载事件********/
	$('#fltmonitor-tab').on('shown.bs.tab', function (e) {
		$('#fltmonitor iframe')[0].contentWindow.init();
	});
}