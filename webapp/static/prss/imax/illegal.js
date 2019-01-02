var layer;
var form;
layui.use(['layer','form'],function(){
	layer = layui.layer;
	form = layui.form;
	
	form.on('select', function(data) {
		initData();
	});
	
})

var lineHeight = 350; 
var barHeight = 350;

function initData(){
	var param = {
			date : $('#date').val(),
			targetDate:$('#targetDate').val(),
			officeId : $('#office').val()
		}
	
	$.ajax({
        type: "POST",
        url: ctxI + '/getIllegalData',
        data: param,
        dataType: "json",
        success: function(dataMap){
            if(dataMap.code == '0'){
            	var data = dataMap.data;
            	// 折线图
            	if(data.lineChart){
            		lineChart(data.lineChart);
            	}
            	// 折线图下方文字
            	if(data.lineText){
            		lineText(data.lineText)
            	}
            	// 柱状图
            	if(data.barChart){
            		barChart(data.barChart);
            	}
            	// 人员列表
        		personList(data.personList);
            	
            	// 车辆列表
            	if(data.carList){
            		carList(data.carList);
            	}
            	
            }else{
           	 	layer.alert(dataMap.msg);
            }
         }
    });
}

function lineChart(data){
	
	// 岗位人员
	var optionLine = {
			tooltip : {
				trigger: 'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend: {
				data:['人员违规次数','车辆违规次数'],
				bottom: 10,
				orient:'vertical',
				left:'0'
			},
			grid: {
				top:'3%',
				left: '9%',
				right: '0',
				bottom: 70,
				containLabel: true
			},
			xAxis : [
			         {
			        	 type : 'category',
			        	 data : data.month
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
			                            	name:'人员违规次数',
			                            	type:'line',
			                            	/*label:{
		                normal:{
		                    show:true,
		                    position: 'top'
		                }
		            },*/
			                            	data:data.person
			                            },
			                            {
			                            	name:'车辆违规次数',
			                            	type:'line',
			                            	/*label:{
		                normal:{
		                    show:true,
		                    position: 'bottom'
		                }
		            },*/
			                            	data:data.car
			                            }
			                            ]
	};
	
	echartsItems['line'].resize({height:lineHeight});
	echartsItems['line'].setOption(optionLine);
	
	
	$('#lineTable').html('');
	var tr_person = $('<tr>'); 
	$.each(data.person , function (i,item){
		tr_person.append('<td>'+item+'</td>');
	});
	$('#lineTable').append(tr_person);
	var tr_car = $('<tr>'); 
	$.each(data.car , function (i,item){
		tr_car.append('<td>'+item+'</td>');
	});
	$('#lineTable').append(tr_car);
}

function lineText(data){
	$('span[data-id]').html('');
	$.each(data,function(k,v){
		if(k == 'hb' || k == 'tb'){
			if(v.indexOf('-') >= 0){
				v = v.substring(1);
				$('span[data-name='+k+']').html('下降');
			}else{
				$('span[data-name='+k+']').html('增长');
			}
			$('span[data-id='+k+']').html(v + '%');
		}else{
			$('span[data-id='+k+']').html(v);
		}
	});
}
	
function barChart(data){
	
	// 部门违规操作情况
	
	var optionBar = {
			legend: {
				data:['对比日期','目标日期'],
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
				bottom: 110,
				top:0,
				containLabel: true
			},
			xAxis: {
				type: 'value',
				boundaryGap: [0, '20%']
			},
			yAxis: {
				type: 'category',
				data: ['总体','车辆','人员']
			},
			series: [
			         {
			        	 type: 'bar',
			        	 name:'对比日期',
			        	 data: [
			        	        {
			        	        	category:'总体',
			        	        	value:data.targetResult.total
			        	        },
			        	        {
			        	        	category:'车辆',
			        	        	value:data.targetResult.car
			        	        },
			        	        {
			        	        	category:'人员',
			        	        	value:data.targetResult.person
			        	        }
			        	        ],
			        	        barWidth:'25%'
			         },
			         {
			        	 type: 'bar',
			        	 name:'目标日期',
			        	 data: [
			        	        {
			        	        	category:'总体',
			        	        	value:data.dateResult.total
			        	        },
			        	        {
			        	        	category:'车辆',
			        	        	value:data.dateResult.car
			        	        },
			        	        {
			        	        	category:'人员',
			        	        	value:data.dateResult.person
			        	        }
			        	        ],
			        	        barWidth:'25%'
			         }
			         ]
	};
	
	echartsItems['bar'].resize({height:barHeight});
	echartsItems['bar'].setOption(optionBar);
	
	$('#barTable td[data-id=db_person]').html(data.targetResult.person);
	$('#barTable td[data-id=db_car]').html(data.targetResult.car);
	$('#barTable td[data-id=db_total]').html(data.targetResult.total);
	$('#barTable td[data-id=mb_person]').html(data.dateResult.person);
	$('#barTable td[data-id=mb_car]').html(data.dateResult.car);
	$('#barTable td[data-id=mb_total]').html(data.dateResult.total);
}	

function personList(data){
	$('#personList tbody').html('');
	if(data)
		$.each(data,function(i,item){
			var tr = $('<tr>');
			
			tr.append('<td>' + (i+1)+ '</td>');
			tr.append('<td>' + (item.OPERATOR ? item.OPERATOR : '&nbsp;' )+ '</td>');
			tr.append('<td>' + (item.WD_TIME ? item.WD_TIME : '0') + '</td>');
			tr.append('<td>' + (item.CS_TIME ? item.CS_TIME : '0') + '</td>');
			tr.append('<td>' + (item.HJ_TIME	 ? item.HJ_TIME	 : '0') + '</td>');
			
			$('#personList tbody').append(tr);
		});
}

$(function(){
	setInterval(initData, 60000);
	initData();
});