$(function(){
	var layer;
	layui.use([ "layer", "form"], function() {
		layer = layui.layer;
	});
	
});


function child(date,time,airport,airline){
	$.ajax({
	    type: "POST",
	    url: ctx + '/plan/planFilter/getChartData',
	    data: {
	    	"date":date,
	    	"time":time,
	    	"airport":airport,
	    	"airline":airline
	    },
	    dataType: "json",
	    success: function(dataMap){
	        	var data = dataMap.data;
	        	initChart(data.planChartData,data.planChartX);
	        	
	        
	     }
	});
	
}
function initChart(data,x){
	var optionBar = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['总价次','进港架次','出港架次'],
		        bottom: 20
		    },
		    grid: {
		    	top:'3%',
		        left: '3%',
		        right: '4%',
		        bottom: 50,
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
//		            data : ['20180428','20180429','20180430','20180431']
		            data : x.StringHeader
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'总价次',
		            type:'line',
		            data :data.allCntArr,
		        	barWidth:'50%'
		        },
		        {
		            name:'进港架次',
		            type:'line',
		            data :data.outCntrr,
		        	barWidth:'50%'
		        },
		        {
		            name:'出港架次',
		            type:'line',
		            data :data.inCntArr,
		        	barWidth:'50%'
		        }
		    ]
		};
	
	echartsItems['planChart'].resize({height:356});
	echartsItems['planChart'].setOption(optionBar);
}



