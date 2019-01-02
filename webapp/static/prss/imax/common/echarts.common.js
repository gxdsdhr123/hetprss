var echartsItems = {};
$(function(){
	// 统一皮肤及公共设置
	$('.echarts-container').each(function(i,item){
		echartsItems[this.id] = echarts.init(this, "dark-prss");
	});
	
});