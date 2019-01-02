var layer;
var form;
$(function(){
	layui.use(['layer','form'],function(){
		layer = layui.layer;
		form = layui.form();
		
		form.on('select', function(data) {
			initData();
		});
		
	})
	initData();
	monitorChart();
});

var pieHeight = 300;
var lineHeight = 450; 

function initData(){
	$.ajax({
        type: "POST",
        url: ctxI + '/getResource2Data',
        data: {depId: $('#adType').val()},
        dataType: "json",
        success: function(dataMap){
            if(dataMap.code == '0'){
            	var data = dataMap.data;
            	//人员保障图
            	if(data.monitorChart){
            		monitorChart(data.monitorChart);
            	}
            	//人员保障文字
            	if(data.monitorText){
            		monitorText(data.monitorText);
            	}
            	// 人员占用图
            	if(data.occupyChart){
            		occupyChart(data.occupyChart);
            	}
            	// 人员占用列表
            	if(data.occupyTable){
            		occupyTable(data.occupyTable);
            	}
            	
            }else{
           	 	layer.alert(dataMap.msg);
            }
         }
    });
	
}

//人员保障图
function monitorChart(data){
	var optionLine = {
			title: {
		        text: '人员保障情况',
		        left: 'center'
		    },
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['预计人数','实际人数'],
		        bottom: 10,
		        orient:'vertical',
		        left:'0'
		    },
		    grid: {
		    	top:'12%',
		        left: '9%',
		        right: '0',
		        bottom: 80,
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : ['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
			        boundaryGap: [0, '20%']
		        }
		    ],
		    series : [
		        {
		            name:'预计人数',
		            type:'line',
		            data:data.yj
		        },
		        {
		            name:'实际人数',
		            type:'line',
		            data:data.sj
		        }
		    ]
		};
	
	echartsItems['line'].resize({height:lineHeight});
	echartsItems['line'].setOption(optionLine);
	
	$('#chartTable').html('');
	var tr_yj = $('<tr>'); 
	$.each(data.yj , function (i,item){
		tr_yj.append('<td>'+item+'</td>');
	});
	$('#chartTable').append(tr_yj);
	var tr_sj = $('<tr>'); 
	$.each(data.sj , function (i,item){
		tr_sj.append('<td>'+item+'</td>');
	});
	$('#chartTable').append(tr_sj);
}

//人员保障文字
function monitorText(data){
	$.each(data,function(k,v){
		k = k.toLowerCase();
		if(k == 'gf_hour' || k == 'jq_hour'){
			if(v){
				v = v + ':00-' + (parseInt(v)+1) + ':00';
			}
		}
		$('#monitorText .' + k).text(v);
	});
}

// 人员占用图
function occupyChart(data){
	
	$.each(data,function(k,v){
		$('.pie-text .' + k.toLowerCase()).text(v);
	});
	
	
	var pieOption = {
		title: {
	        text: $('#adType').val(),
	        left: 'center',
	        top:'bottom'
	    },
	    series: [
	        {
	            type:'pie',
	            radius: ['40%', '60%'],
	            hoverAnimation:false,
	            avoidLabelOverlap: false,
	            label: {
	                normal: {
	                    show: true,
	                    position: 'center',
	                    formatter : '{d}%'
	                }
	            },
	            labelLine: {
	                normal: {
	                    show: false
	                }
	            },
	            data:[
	                    {value:data.ZY_NUM},
	                  	{value:data.KX_NUM,label:{normal: {show: false}}}
	                  ]
	        }
	    ]
	};
	// 高度设定
	echartsItems['pie'].resize({height:pieHeight});
	echartsItems['pie'].setOption(pieOption);
}

//人员占用列表
function occupyTable(data){
	
	$('#occupyTable tbody').html('');
	$.each(data,function(i,item){
		var tr = $('<tr>');
		
		tr.append('<td>' + (item.CAR_NUMBER ? item.CAR_NUMBER : '&nbsp;' )+ '</td>');
		tr.append('<td>' + (item.STATUS ? item.STATUS : '&nbsp;') + '</td>');
		tr.append('<td>' + (item.BZ_NUM ? item.BZ_NUM : '0') + '</td>');
		tr.append('<td>' + (item.XS_NUM	 ? item.XS_NUM	 : '0') + '</td>');
		tr.append('<td>' + (item.BX_TM ? item.BX_TM : '&nbsp;') + '</td>');
		
		$('#occupyTable tbody').append(tr);
	});
	
}
