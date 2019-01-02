var layer;
layui.use(['layer','element'],function(){
	layer = layui.layer;
})

/*预定义高度*/
var rateHeight = 500; 
var delayHeight = 400;

function initData(){
	$.ajax({
        type: "POST",
        url: ctxI + '/getRun2Data',
        data: {},
        dataType: "json",
        success: function(dataMap){
            if(dataMap.code == '0'){
            	var data = dataMap.data;
            	// 航班运行情况占比左侧文字
            	if(data.runText){
            		runText(data.runText);
            	}
            	// 航班运行情况占比表格
            	if(data.runTable){
            		runTable(data.runTable);
            	}
            	// 航空公司占比图
            	if(data.runChart){
            		runChart(data.runChart);
            	}
            	// 航班延误图表
            	if(data.ywChart){
            		ywChart(data.ywChart);
            	}
            	// 航班延误文字
            	if(data.ywText){
            		ywText(data.ywText);
            	}
            	
            }else{
           	 	layer.alert(dataMap.msg);
            }
         }
    });
}

//航班运行情况占比左侧文字
function runText(data){
	$.each(data,function(k,v){
		$('#runText .' + k.toLowerCase()).text(v);
	});
}

// 航班运行情况占比图
function runChart(data){
	var legendArr = [];
	var dataArr = [];
	$('#chartTable').html('');
	$.each(data,function(i,item){
		var name = item.ICAO_CODE;
		var value = item.COUNTNUM;
		
		legendArr.push(name);
		dataArr.push({name:name,value:value});
		// 右侧
		var tr = $('<tr>');
		tr.append('<td>'+name+'</td>');
		tr.append('<td>'+value+'</td>');
		$('#chartTable').append(tr);
		
	});
	
	var optionRate = {
			title: {
		        text: '机型比例',
		        left: 'center'
		    },
			tooltip : {
		        trigger: 'item',
		        formatter: "{b} : {c} ({d}%)"
		    },
			legend: {
		        data:legendArr,
		        bottom: 0,
		        left: 'center'
		    },
		    series: [
		        {
		            type:'pie',
		            radius: ['25%', '50%'],
		            hoverAnimation:false,
		            data:dataArr
		        }
		    ]
		};
	
	echartsItems['rate'].resize({height:rateHeight});
	echartsItems['rate'].setOption(optionRate);
}

// 航空公司占比
function runTable(data){
	var runTable = $('#rateTable table')
	var line1 = $('<tr><td>航空公司</td></tr>');
	var line2 = $('<tr><td>架次</td></tr>');
	var line3 = $('<tr><td>占比</td></tr>');
	
	$.each(data,function(i,item){
		line1.append('<td>'+item.ALN_3CODE+'</td>');
		line2.append('<td>'+item.JCNUM+'</td>');
		line3.append('<td>'+(parseFloat(item.ZHB) * 100).toFixed(1) + '%'+'</td>');
		
	});
	
	runTable.find('tbody').html('');
	runTable.find('tbody').append(line1);
	runTable.find('tbody').append(line2);
	runTable.find('tbody').append(line3);
}

// 航班延误情况
function ywChart(data){
	
	var titleArr = [];
	var valArr = [];
	$.each(data,function(k,v){
		titleArr.push(k);
		valArr.push(v);
	});
	
	
	var optionDelay = {
		    
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    grid: {
		        left: '3%',
		        right: '3%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		        type: 'value',
		        boundaryGap: [0, '20%']
		    },
		    yAxis: {
		        type: 'category',
		        data: titleArr
		    },
		    series: [
		        {
		            type: 'bar',
		            data: valArr,
		            barWidth:'50%'
		        }
		    ]
		    
		};
		

	echartsItems['delay'].resize({height:delayHeight});
	echartsItems['delay'].setOption(optionDelay);
}

// 航班延误文字
function ywText(data){
	$.each(data,function(k,v){
		$('#ywText .' + k.toLowerCase()).text(v);
	});
}
$(function(){
	setInterval(initData, 60000);
	initData();
	
});