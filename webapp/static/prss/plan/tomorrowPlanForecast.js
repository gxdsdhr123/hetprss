var layer;
layui.use(['layer'],function(){
	layer = layui.layer;
})
$(function(){
	initGrid();
	initChart();
});

function initChart(){
	$.ajax({
		type : 'post',
		url : ctx + "/tomorrow/plan/forecast",
		async : true,
		dataType : "text",
		success : function(chatData) {
			if (chatData != null) {
				var jsJsonChatData = eval("("+chatData+")");
				$("#showMsg").text(jsJsonChatData.showMsg);
				createChat(jsJsonChatData);
			}else{
				layer.alert("次日计划预测失败！", {icon: 2});
			}
		},
		error:function(msg){
			layer.alert("次日计划预测失败！", {icon: 2});
		}
	});
}

function createChat(chatData){
	//出港航班架次时刻预测图
	var chartDepartOption = createChatOption("出港航班架次时刻预测图","架次",chatData.chartDepart);
	echartsItems['chartDepart'].clear();
	echartsItems['chartDepart'].resize({height:356,width:700});
	echartsItems['chartDepart'].setOption(chartDepartOption);
	
	//机场全天各时段旅客人数
	var chartPassengerOption = createChatOption("机场全天各时段旅客人数","人数",chatData.chartPassenger);
	echartsItems['chartPassenger'].clear();
	echartsItems['chartPassenger'].resize({height:356,width:700});
	echartsItems['chartPassenger'].setOption(chartPassengerOption);
	
	//值机柜台建议开放个数时段分布
	var chartChekcInOption = createChatOption("值机柜台建议开放个数时段分布","个数",chatData.chartChekcIn);
	echartsItems['chartChekcIn'].clear();
	echartsItems['chartChekcIn'].resize({height:356,width:700});
	echartsItems['chartChekcIn'].setOption(chartChekcInOption);
	
	//安检通道建议开放个数时段分布
	var chartSecurityOption = createChatOption("安检通道建议开放个数时段分布","个数",chatData.chartSecurity);
	echartsItems['chartSecurity'].clear();
	echartsItems['chartSecurity'].resize({height:356,width:700});
	echartsItems['chartSecurity'].setOption(chartSecurityOption);
}

function createChatOption(titleText,yAxisStack,seriesData){
	var option = {
			title:{
			     text: titleText,
			     left:'center'
			},
		    legend: {                                   // 图例配置
		        padding: 5,                             // 图例内边距，单位px，默认上下左右内边距为5
		        itemGap: 10                            // Legend各个item之间的间隔，横向布局时为水平间隔，纵向布局时为纵向间隔
		    },
		    tooltip: {                                  // 气泡提示配置
		        trigger: 'item',                        // 触发类型，默认数据触发，可选为：'axis'
		    },
		    xAxis: [                                    // 直角坐标系中横轴数组
		        {
		            name: '小时',
		        	type: 'category',                   // 坐标轴类型，横轴默认为类目轴，数值轴则参考yAxis说明
		        	stack: '小时',
		        	data: ['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23']
		        }
		    ],
		    yAxis: [                                    // 直角坐标系中纵轴数组
		        {
		        	name: '数量',
		        	type: 'value',                      // 坐标轴类型，纵轴默认为数值轴，类目轴则参考xAxis说明
		            minInterval : 1,
		            boundaryGap: [0, 0.1],            // 坐标轴两端空白策略，数组内数值代表百分比
		            stack: yAxisStack,
		            splitNumber: 4                      // 数值轴用，分割段数，默认为5
		        }
		    ],
		    series: [
		        {
		            name: '数据',                        // 系列名称
		            type: 'bar',                       // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
		            itemStyle:{
                        normal:{
                            color:'#2828FF'
                        }
                    },
		            data: seriesData
		        }
		    ]
		};
	return option;
}

function initGrid() {
	$("#dataGrid").bootstrapTable({
		data : confData,
		dataType: "json",
		toolbar : $("#tool-box"),
		editable:true,
		height:$("#dataGrid").height(),
		columns : [{
			field : "rowId",
			title : "序号",
			width : '40px',
			align : 'center',
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "PLF",
			title : "客座率",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return '客座率不能为空!';
	                }
	            }
			}
		},{
			field : "CHECKIN_THRESHOLD",
			title : "值机柜台伐值",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return '值机柜台伐值不能为空!';
	                }
	            }
			}
		},{
			field : "SECURITY_THRESHOLD",
			title : "安检通道伐值",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return '安检通道伐值不能为空!';
	                }
	            }
			}
		},{
			field : "M30",
			title : "M30",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return 'M30系数不能为空!';
	                }
	            }
			}
		},{
			field : "M60",
			title : "M60",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return 'M60系数不能为空!';
	                }
	            }
			}
		},{
			field : "M90",
			title : "M90",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return 'M90系数不能为空!';
	                }
	            }
			}
		},{
			field : "M120",
			title : "M120",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return 'M120系数不能为空!';
	                }
	            }
			}
		},{
			field : "M150",
			title : "M150",
			width : "80px",
			editable : {
				type:'text',
				validate: function (value) {
	                if ($.trim(value) == '') {
	                    return 'M150系数不能为空!';
	                }
	            }
			}
		}]
	});
}

/**
 * 保存次日计划预测配置
 */
function saveFun(){
	var dataArr = $("#dataGrid").bootstrapTable("getData");
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/tomorrow/plan/forecastConfSave",
		async : true,
		dataType : "text",
		data : {
			saveData : JSON.stringify(dataArr)
		},
		success : function(result) {
			layer.close(loading);
			if (result == 'succeed') {
				showHideConf();
				initChart();
			}else{
				layer.alert("次日计划预测配置保存失败！"+result, {icon: 2});
			}
		},
		error:function(msg){
			layer.close(loading);
			layer.alert("次日计划预测配置保存失败！", {icon: 2});
		}
	});
}

function showHideConf(){
	if($('#conf').is(':hidden')){
		$('#conf').show();
	}else{
		$('#conf').hide();
	} 
}	
