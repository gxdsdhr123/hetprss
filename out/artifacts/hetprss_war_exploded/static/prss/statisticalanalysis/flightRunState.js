$(function(){
	//页面初始化显示图表
	searchImage();
	
	$("#searchBut").on('click',function(){
		var beginTime = $("#beginTime").val();
		if("" == beginTime || null == beginTime){
			alert("请输入查询时间！");
			return;
		}
		location.href= PATH +"/flightRunState/searchPage?beginTime="+beginTime;
	});
});
function searchImage(){
	var beginTime = $("#beginTime").val();
	$.ajax({
        type: "POST",
        url: PATH + '/flightRunState/SearchImage',
        data: {
        	"beginTime":beginTime
        },
        dataType: "json",
        success: function(data){
        	 showImage(data);
        }
	})
}

//显示折线图-运行架次时刻分布图
function showImage(data){
	var option = {
	    title : {
	        text: '运行架次时刻分布图',
	        subtext: ''
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['进港','出港','总计']
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]
	        }
	    ],
	    yAxis : [
	        {
	        	name : '架次',
	            type : 'value',
	            axisLabel : {
	                formatter: '{value}'
	            }
	        }
	    ],
	    series : [
	        {
	            name:'进港',
	            type:'line',
	            data:data.jg
	        },
	        {
	            name:'出港',
	            type:'line',
	            data:data.cg
	        },
	        {
	            name:'总计',
	            type:'line',
	            data:data.zj
	        }
	    ]
	};
	echartsItems['runBar'].resize({height:300});
	echartsItems['runBar'].setOption(option);
}