$(function(){
	var layer;
	layui.use([ "layer", "form"], function() {
		layer = layui.layer;
	});
	
	//当beginTime没有值时才自动加载统计表格，避免一直循环加载
	if($("#beginTime").val()==""||$("#beginTime").val()=="null"){
		var yesterdayDate=getYesterdayFormatDate();
		$("#beginTime").val(yesterdayDate);
		showTables();
	}
	//页面初始化加载航班运行情况图
	showImage();
	
	$("#searchBut").on('click',function(){
		showTables();
	});
});
function showTables(){
	var beginTime = $("#beginTime").val();
	if("" == beginTime || null == beginTime){
		layer.msg("请选择查询时间！" , {icon : 7, time : 1000});
		return;
	}
	location.href= PATH +"/qcDailyGuaranteeRecord/searchPage?beginTime="+beginTime;
}

function showImage(){
	var beginTime = $("#beginTime").val();
	if("" != beginTime && null != beginTime){
		$.ajax({
	        type: "POST",
	        url: PATH + '/qcDailyGuaranteeRecord/showImage',
	        data: {
	        	"beginTime":beginTime
	        },
	        dataType: "json",
	        success: function(dataMap){
	            if(dataMap.code == '0'){
	            	var data = dataMap.data;
	            	// 航班运行情况（图）
	            	if(data.flightNums){
	            		flightNums(data.flightNums);
	            	}
	            }else{
	           	 	layer.alert(dataMap.msg);
	            }
	         }
	    });
	}
}

// 航班运行情况（图）
function flightNums(data){
	var optionBar = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['南航系','BGS外航系'],
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
		            data : ['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'南航系',
		            type:'bar',
		            stack: 'same',
		            data:data.nh,
		        	barWidth:'50%'
		        },
		        {
		            name:'BGS外航系',
		            type:'bar',
		            stack: 'same',
		            data:data.bgs,
		            barWidth:'50%'
		        }
		    ]
		};
	
	echartsItems['runBar'].resize({height:356});
	echartsItems['runBar'].setOption(optionBar);
}

function getYesterdayFormatDate() {
    var date = new Date();
	date.setDate(date.getDate() - 1);
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	return year+""+month+""+day;
}
