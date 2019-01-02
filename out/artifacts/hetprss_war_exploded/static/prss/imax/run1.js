var layer;
layui.use(['layer','element'],function(){
	layer = layui.layer;
})

/*预定义高度*/
var chartHeight = 300; 


function initData(){
	$.ajax({
        type: "POST",
        url: ctxI + '/getRun1Data',
        data: {},
        dataType: "json",
        success: function(dataMap){
            if(dataMap.code == '0'){
            	var data = dataMap.data;
            	// 航班运行情况（图）
            	if(data.flightChart){
            		flightChart(data.flightChart);
            	}
            	// 文字
            	if(data.flightText){
            		flightText(data.flightText);
            	}
            	// 表格
            	if(data.flightTable){
            		flightTable(data.flightTable);
            	}
            	
            }else{
           	 	layer.alert(dataMap.msg);
            }
         }
    });
}


// 航班运行情况（图）
function flightChart(data){
	
	$.each(data , function(k,v){
		
		var option = {
			    tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    legend: {
			        data:['正常航班','延误航班'],
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
			            type : 'value',
			            boundaryGap: [0, '20%']
			        }
			    ],
			    series : [
			        {
			            name:'正常航班',
			            type:'bar',
			            stack: '航班',
			            data:v.zc,
			        	barWidth:'50%'
			        },
			        {
			            name:'延误航班',
			            type:'bar',
			            stack: '航班',
			            data:v.yw,
			            barWidth:'50%'
			        }
			    ]
			};
		
		echartsItems[k].resize({height:chartHeight});
		echartsItems[k].setOption(option);
		
	});
	
}
// 文字
function flightText(data){
	$.each(data,function(k,v){
		$('#' + k + ' .jg').text(v.JG_NUM);
		$('#' + k + ' .cg').text(v.CG_NUM);
		$('#' + k + ' .jcg').text(v.JCG_NUM);
		$('#' + k + ' .yjw').text(v.YJW_NUM);
		$('#' + k + ' .hbzc').text(parseInt(parseFloat(v.ZC_ZB) * 100) + '%');
		$('#' + k + ' .fxzc').text(parseInt(parseFloat(v.FX_ZB) * 100) + '%');
	});
}
// 表格
function flightTable(data){
	$('#flight_table tbody').html('');
	$.each(data,function(i,item){
		var tr = $('<tr>');
		var in_out;
		if(item.IN_OUT_FLAG == 'A'){
			in_out = '进港';
		}else if(item.IN_OUT_FLAG == 'D'){
			in_out = '出港';
		}else{
			return;
		}
		tr.append('<td>' + in_out + '</td>');
		tr.append('<td>' + (item.FLIGHT_NUM ? item.FLIGHT_NUM : '0' )+ '</td>');
		tr.append('<td>' + (item.BZ_FLIGHT_NUM ? item.BZ_FLIGHT_NUM : '0') + '</td>');
		tr.append('<td>' + (item.QX_NUM ? item.QX_NUM : '0') + '</td>');
		tr.append('<td>' + (item.BJ_NUM ? item.BJ_NUM : '0') + '</td>');
		tr.append('<td>' + (item.YW_NUM ? item.YW_NUM : '0') + '</td>');
		tr.append('<td>' + (item.YW_NUM1 ? item.YW_NUM1 : '&nbsp;') + '</td>');
		tr.append('<td>' + (item.YW_NUM12 ? item.YW_NUM12 : '&nbsp;') + '</td>');
		tr.append('<td>' + (item.YW_NUM2 ? item.YW_NUM2 : '&nbsp;') + '</td>');
		tr.append('<td>' + (item.FX_ZB ? parseInt(parseFloat(item.FX_ZB) * 100) + '%' : '&nbsp;') + '</td>');
		tr.append('<td>' + (item.ZC_ZB ? parseInt(parseFloat(item.ZC_ZB) * 100) + '%' : '&nbsp;') + '</td>');
		
		$('#flight_table tbody').append(tr);
	});
}


$(function(){
	setInterval(initData, 60000);
	initData();
});