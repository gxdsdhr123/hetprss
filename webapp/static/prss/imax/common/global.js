$(document).ready(function() {
	if($(window).width() < 1600){
		$('body').css('font-size','14px');
		$('h4').css('font-size','16px');
	}
});
// echart load style
var loadOpt = {
	text : '正在加载',
	color : '#ffffff',
	textColor : '#ffffff',
	maskColor : 'rgba(255, 255, 255, 0.2)',
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