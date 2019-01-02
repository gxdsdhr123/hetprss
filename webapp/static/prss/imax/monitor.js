var layer;
var form;
layui.use(['layer','form'],function(){
	layer = layui.layer;
	form = layui.form;
	
	form.on('select', function(data) {
		initData();
	});
})


var barHeight = 450;

function initData(){
	var param = {
			inOut : $('#inOut').val()
		}
	
	$.ajax({
        type: "POST",
        url: ctxI + '/getMonitorData',
        data: param,
        dataType: "json",
        success: function(dataMap){
            if(dataMap.code == '0'){
            	var data = dataMap.data;
            	// 条状图
            	if(data.barList){
            		barList(data.barList);
            	}
            	// 表格
            	if(data.tableList){
            		tableList(data.tableList);
            	}
            	// 标准块
        		bzObj(data.bzObj);
            	
            }else{
           	 	layer.alert(dataMap.msg);
            }
         }
    });
}
	
function barList(data){
	
	// 各机型保障标准情况
	var optionBar = {
			legend: {
				data:['标准保障时间','实际保障时间'],
				bottom: 7,
				orient:'vertical',
				left:'0'
			},
			tooltip: {
				trigger: 'axis',
				axisPointer: {
					type: 'shadow'
				}
			},
			grid: {
				left: '3%',
				right: '3%',
				bottom: 120,
				containLabel: true
			},
			xAxis: {
				type: 'value',
				boundaryGap: [0, '20%']
			},
			yAxis: {
				type: 'category',
				data: ['60座以下','61-150座','151-250座','251-500座','500座以上']
			},
			series: [
			         {
			        	 type: 'bar',
			        	 name:'标准保障时间',
			        	 data: [40,55,65,75,120],
			        	 barWidth:'25%'
			         },
			         {
			        	 type: 'bar',
			        	 name:'实际保障时间',
			        	 data: data,
			        	 barWidth:'25%'
			         }
			         ]
	};
	
	echartsItems['bar'].resize({height:barHeight});
	echartsItems['bar'].setOption(optionBar);
	
	$('#barTable').html('');
	$('#barTable').append('<td>&nbsp;</td>');
	$.each(data , function (i,item){
		$('#barTable').append('<td>'+item+'</td>');
	});
}
	
function tableList(data){
	$('#bzTable tbody').html('');
	if(data)
		$.each(data,function(i,item){
			var tr = $('<tr>');
			
			tr.append('<td>' + (item.KINDNAME ? item.KINDNAME : '0') + '</td>');
			tr.append('<td>' + (item.OK_TIMES ? item.OK_TIMES : '0') + '</td>');
			tr.append('<td>' + (item.NOOK_TIMES ? item.NOOK_TIMES	 : '0') + '</td>');
			
			$('#bzTable tbody').append(tr);
		});
}

function bzObj(data){
	$('.node-item').removeClass('late').find('span[data-id]').html('');
	if(data)
		$.each(data,function(k,v){
			var aTime = $('span[data-id='+k+']').html(v);
			var eTime = parseInt(aTime.nextAll('span').text());
			if(v > eTime){
				aTime.parents('.node-item').addClass('late');
			}
		});
}

$(function(){
	setInterval(initData, 60000);
	initData();
});
