var layer;
layui.use(['layer','element'],function(){
	layer = layui.layer;
})

/*预定义高度*/
var carHeight = 120; 
var runHeight = 365;
var illegalHeight = 390;

function initData(){
	$.ajax({
        type: "POST",
        url: ctxI + '/getIndexData',
        data: {},
        dataType: "json",
        success: function(dataMap){
            if(dataMap.code == '0'){
            	var data = dataMap.data;
            	$('#nowTime').html(data.nowTime);
            	// 安全运行时间
            	if(data.safeTime){
            		safeTime(data.safeTime);
            	}
            	// 值班领导
            	if(data.leader){
            		leader(data.leader);
            	}
            	// 车辆资源占用
            	if(data.carNums){
            		carNums(data.carNums);
            	}
            	// 人员占用
            	if(data.personNums){
            		personNums(data.personNums);
            	}
            	// 航班运行情况（图）
            	if(data.flightNums){
            		flightNums(data.flightNums);
            	}
            	// 航班运行情况
            	if(data.flightMap){
            		flightMap(data.flightMap);
            	}
            	// 航班占比情况
            	if(data.flightRate){
            		flightRate(data.flightRate);
            	}
            	// 航班保障进度
            	if(data.monitorNums){
            		monitorNums(data.monitorNums);
            	}
            	// 部门违规情况
            	//if(data.departmentIllegal){
            		departmentIllegal(data.departmentIllegal);
            	//}
            	// 现场违规情况
            	//if(data.personIllegal){
            		personIllegal(data.personIllegal);
            	//}
            	
            }else{
           	 	layer.alert(dataMap.msg);
            }
         }
    });
}

// 安全生产时间
function safeTime(data){
	$('#safeTime').html('安全运行'+data.DAY+'天'+data.HOUR+'小时'+data.MI+'分');
}

// 值班领导
function leader(data){
	$('#leader').html(data);
}

// 车辆资源占用
function carNums(data){
	var option = {
		    series: [
		        {
		            type:'pie',
		            radius: ['50%', '70%'],
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
		            data:[]
		        }
		    ]
		};
	
	$.each(data,function(k,v){
		// 高度设定
		echartsItems[k].resize({height:carHeight});
		
		option.series[0].data = [];
		option.series[0].data.push({value:v.use});
		option.series[0].data.push({value:v.unuse,label:{normal: {show: false}}});
		echartsItems[k].setOption(option);
		
		
		$('#' + k).next().find('span.use').html(v.use);
		$('#' + k).next().find('span.unuse').html(v.unuse);
	});
}

// 人员占用
function personNums(data){
	var KeysObj = {
			po_djkfw : '4a886a6b7aa34fc3bfc76a66f743154d',		// 登机口服务
			po_jzjx : '4263e6ec15f3437bbf0b55f4d307609d',		// 监装监卸
			po_tc : '45744eb689d842749e8327d8e2be5ae2',		    // 特车
			po_jw : '9562e7c64a4a4acabd8046a6bcbe390a',		    // 机务
			po_ylgs : '354fd58d1f424f2991949c82d9e6e363',		// 油料公司
			po_pcgs : 'fe21f12110174bc7aff729b4c66c6e2e',	    // 配餐公司
			po_qjgs : 'de92dc7d609349449f29f0a8cb633c70'	    // 清洁公司
	}
	
	$.each(KeysObj , function(k,v){
		var _this = $('#' + k);
		var thisData = data[v];
		if(thisData){
			_this.find('.zg').text(thisData['zg']);	// 在岗
			_this.find('.kx').text(thisData['kx']);	// 空闲
			_this.find('.oc').text(thisData['oc'] + '%');	// 占用
			// 进度条
			_this.find('.progress-bar').css('width',thisData['oc'] + '%').attr('aria-valuenow',thisData['oc']);
		}
	});
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
		        data:['进港','出港'],
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
		            name:'进港',
		            type:'bar',
		            stack: '内航',
		            data:data.jg,
		        	barWidth:'50%'
		        },
		        {
		            name:'出港',
		            type:'bar',
		            stack: '内航',
		            data:data.cg,
		            barWidth:'50%'
		        }
		    ]
		};
	
	echartsItems['runBar'].resize({height:runHeight});
	echartsItems['runBar'].setOption(optionBar);
	
}

// 航班运行情况
function flightMap(data){
	$.each(data,function(k,v){
		k = k.toLowerCase();
		if(k == 'gfsd1' || k == 'gfsd2'){
			v = v + ':00-' + (parseInt(v)+1) + ':00';
		}
		$('#flight_' + k).text(v);
	});
}

// 航班占比情况
function flightRate(data){
	var option = {
		    series: [
		        {
		            type:'pie',
		            radius: ['50%', '70%'],
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
		            data:[]
		        }
		    ]
		};
	$.each(data,function(i,item){
		$.each(item,function(k,v){
			k = k.toLowerCase();
			if(echartsItems[k]){
				echartsItems[k].resize({height:carHeight});
				option.series[0].data = [];
				option.series[0].data.push({value:v});
				option.series[0].data.push({value:(1-v),label:{normal: {show: false}}});
				echartsItems[k].setOption(option);
			}
			v = parseInt(parseFloat(v) * 100) + '%';
			$('#' + k + '_' + i).text(v);
		});
	});
}

// 航班保障进度
function monitorNums(data){
	$('#monitor .count1').text(data.COUNT1);
	$('#monitor .count2').text(data.COUNT2);
	$('#monitor .count3').text(data.COUNT3);
	// 进度条
	var rate = parseInt(parseFloat(data.COUNT4) * 100);
	$('#monitor').find('.progress-bar').css('width',rate + '%').attr('aria-valuenow',rate);
}

// 部门违规操作情况
function departmentIllegal(data){
	var colArr = ['清洁公司','油料公司','配餐公司','机务公司','航空安保部','地面服务部'];
	var dataArr = [0,0,0,0,0,0];
	
	$.each(data,function(i,item){
		var name = item.OFFICE;
		if(name ==  '总运行控制中心'){
			name = '总控';
		}
		
		var i = colArr.indexOf(name);
		if(i >= 0){
			dataArr[i] = item.WGNUM;
		}
	});
	
	
	var optionIllegal = {
		    
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
		        boundaryGap: [0, '20%'],
		        minInterval: 1
		    },
		    yAxis: {
		        type: 'category',
		        data: colArr
		    },
		    series: [
		        
		        {
		            type: 'bar',
		            data: dataArr,
		            barWidth:'50%'
		        }
		    ]
		};

	echartsItems['illegal'].resize({height:illegalHeight});
	echartsItems['illegal'].setOption(optionIllegal);
}


var limarquee;

// 现场违规情况
function personIllegal(data){
	
	if(limarquee){
		limarquee.liMarquee('destroy');
	}
	
	$('#illegal-table').html('');
	
	$.each(data,function(i,item){
		var tr = $('<tr>');
		tr.append('<td width="20%">'+(item['OFFICE'] == '总运行控制中心'?'总控':item['OFFICE'])+'</td>');
		tr.append('<td width="20%">'+item['OPERATOR']+'</td>');
		tr.append('<td width="20%">'+item['FLIGHT_NUMBER']+'</td>');
		tr.append('<td width="40%">'+item['CS_TIME']+'</td>');
		$('#illegal-table').append(tr);
	});
	
	if(!limarquee){
		limarquee = $('.illegal-body').liMarquee({
			direction: 'up',
			runshort: false
		});
	}else{
		limarquee.liMarquee('update');
	}
}

$(function(){
	
	setInterval(initData, 60000);
	initData();
	
	
});
