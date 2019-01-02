$(document).ready(function(){
	$(".layui-breadcrumb a").click(function(){
		$(this).parent().find("a").removeClass("active");
		$(this).addClass("active");
	});
})
function getQueryString(name, url) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	if (!url || url == "") {
		url = window.location.search;
	} else {
		url = url.substring(url.indexOf("?"));
	}
	r = url.substr(1).match(reg)
	if (r != null)
		return unescape(r[2]);
	return null;
}

function redirect(href,target) {
	if(target&&target=="parent"){
		window.parent.location.href = href;
	} else {
		document.location.href = href;
	}
}

//获取字典标签
function getDictLabel(data, value, defaultValue){
	for (var i=0; i<data.length; i++){
		var row = data[i];
		if (row.value == value){
			return row.label;
		}
	}
	return defaultValue;
}

//打开一个窗体
function windowOpen(url, name, width, height){
	var top=parseInt((window.screen.height-height)/2,10),left=parseInt((window.screen.width-width)/2,10),
		options="location=no,menubar=no,toolbar=no,dependent=yes,minimizable=no,modal=yes,alwaysRaised=yes,"+
		"resizable=yes,scrollbars=yes,"+"width="+width+",height="+height+",top="+top+",left="+left;
	window.open(url ,name , options);
}

//echart load style
var loadOpt = {
	text : '正在加载',
	color : '#ffffff',
	textColor : '#ffffff',
	maskColor : 'rgba(255, 255, 255, 0.2)',
	zlevel : 0
};
var loadOptDark = {
		text : '正在加载',
		textColor : '#ffffff',
		maskColor : 'rgba(0, 0, 0, 0.1)',
		zlevel : 0
	};
/**
 * echart 图形无数据时使用
 */
var echartNoDataLoad = {
	/**
	 * @param chart
	 *            图形实例
	 */
	show : function(chart) {
		if (chart && chart.getDom()) {
			try{
				var chartDom = $(chart.getDom());
				var noDataDiv = $("<div class='noDataDiv'></div>");
				noDataDiv.css({
					"padding-top" : chart.getHeight() / 2 - 12
				});
				var noDataText = $("<span class='noDataText'>无可显示数据</span>");
				noDataDiv.append(noDataText);
				chart.dispose();
				chartDom.empty();
				chartDom.append(noDataDiv);
			} catch(e){
				
			}
		}
	}
};

function ellipsis(str,length){
	length = length?length:10;
	if(str && str.length>length){
		str = str.substring(0,length)+"...";
	}
	return str;
}
//中文长度
String.prototype.gblen = function() {    
    var len = 0;    
    for (var i=0; i<this.length; i++) {    
        if (this.charCodeAt(i)>127 || this.charCodeAt(i)==94) {    
             len += 2;    
         } else {    
             len ++;    
         }    
     }    
    return len;    
}

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
function layTableScroll(){
	 $(".layui-table-main").each(function(){
		 var tableMain =  $(this);
		 tableMain.css('position' , 'relative');
		 var s = new PerfectScrollbar(this);
	 });
}