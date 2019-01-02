/*全屏*/
function fullScreen(el) {  
	if(!el){
		el=document.documentElement;
	}
    var rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen,  
        wscript;  
   
    if(typeof rfs != "undefined" && rfs) {  
        rfs.call(el);  
        return;  
    }  
   
    if(typeof window.ActiveXObject != "undefined") {  
        wscript = new ActiveXObject("WScript.Shell");  
        if(wscript) {  
            wscript.SendKeys("{F11}");  
        }  
    }  
}  

 /*退出全屏*/
function exitFullScreen() {
    var el= document,  
        cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.mozCancelFullScreen || el.exitFullScreen,  
        wscript;  
   
    if (typeof cfs != "undefined" && cfs) {  
      cfs.call(el);  
      return;  
    }  
   
    if (typeof window.ActiveXObject != "undefined") {  
        wscript = new ActiveXObject("WScript.Shell");  
        if (wscript != null) {  
            wscript.SendKeys("{F11}");  
        }  
  }  
}  

//bootstrapTable 重定义大小
function resizeBootstrapTable(){
		var options = {};
		options.height = $("body").height() - $('#totalBox').height();
	try {
		$("#baseTable").bootstrapTable('resetView', options);
	} catch (e) {
	}
	try {
		$("#ambulatoryShiftsGrid").bootstrapTable('resetView', options);
	} catch (e) {
	}
}
// 甘特图 重定义大小
function resizeSJgantt(){
	try {
		/*var options = {};
		options.height = $("body").height() - $(".box-header").height();
		alert(options.height);*/
		var height = $("body").height() - $(".box-header").height();
		//alert(height);
		$("#SJgantt").SJgantt('resetHeight', height);
	} catch (e) {
	}
}
$(function(){
	$(window).on('resize',function(){
		// bootstrapTable 重定义大小
		resizeBootstrapTable();
		// 甘特图 重定义大小
		resizeSJgantt();
	})
	
	$("#baseTable").on('refresh-options.bs.table', function(options){
		// bootstrapTable 重定义大小
		resizeBootstrapTable();
	});
	
});