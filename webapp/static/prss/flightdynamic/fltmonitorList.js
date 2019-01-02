var layer;// 初始化layer模块
var inFltid,outFltid,isY;
var fltType;
var hisFlag;

var vipFlagMap = {};

$(function() {
	// 历史标识位
	hisFlag=$("#hisFlag").val()?$("#hisFlag").val():'';
	
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		
		setInterval(init, 300000);
		init();
		
	})
	
	// 取得要客详情对照表
	$.ajax({
		type: "POST",
		url: ctx + "/fltmonitor/getVipFlagMap",
		dataType: "json",
		success: function(data){
			$.each(data,function(i,item){
				vipFlagMap[item.id] = item.text;
			});
		}
	});
});

var zrenderObj;

function init(){
	
	inFltid = $("#inFltid").val();
	outFltid = $("#outFltid").val();
	isY = $("#isY").val();
	
	var fltType;
	if(!outFltid){
		if(isY == 'Y'){
			fltType = "a0y";
		}else{
			fltType = "a0n";
		}
	}else if(!inFltid){
		if(isY == 'Y'){
			fltType = "d0y";
		}else{
			fltType = "d0n";
		}
	}else{
		if(isY == 'Y'){
			fltType = "a1y";
		}else{
			fltType = "a1n";
		}
	}
	console.log(fltType);
	$.ajax({
		  url: ctxStatic + "/prss/flightdynamic/fltmonitor/"+fltType+".js",
		  dataType: "script",
		  async:false
		});
	
	$.ajax({
         type: "POST",
         url: ctx + "/fltmonitor/getData",
         data: {inFltid:inFltid,outFltid:outFltid,isY:isY,hisFlag:hisFlag},
         dataType: "json",
         async:false,
         success: function(dataMap){
             if(dataMap.code == '0'){
            	 if(zrenderObj){
            			zrender.dispose();
            		}
            	 zrenderObj = createFltmonitor(dataMap.data);
            	 new PerfectScrollbar('#fltmonitor');
             }else{
            	 layer.alert(dataMap.desc);
             }
          }
     });
	
	// 事件添加
	$('#monitor').ProgressChart('getZrenderObject').on('click',function(event){
		layer.closeAll();
		var node = event.target;
		if(node && node.id && typeof node.id == 'string'){
			if(node.id.indexOf('node_') != -1){
				// 流程节点
				var nodeIdArr = node.id.split('_');
				var nodeId = nodeIdArr[2];
				
				$.ajax({
			         type: "POST",
			         url: ctx + "/fltmonitor/getNodeData",
			         data: {inFltid:inFltid,outFltid:outFltid,nodeId:nodeId,hisFlag:hisFlag},
			         dataType: "json",
			         success: function(dataMap){
			             if(dataMap.code == '0'){
			            	 var tableOptions = {
			 						cols:[
			 						      {
			 						    	  field:'time',
			 						    	  title:'时间'
			 						      },
			 						      {
			 						    	  field:'event',
			 						    	  title:'事件'
			 						      }
			 					    ],
			 						data:[]
			 				};
			            	 $.each(dataMap.data,function(k,v){
			            		 var time = v.eventTime;
			            		 var event = v.typeComment;
			            		 if(v.personCode){
			            			 event += '，<a href="javascript:void(0)" onclick="showPersonInfo(\''+v.taskId+'\',event)">'+v.personName+'</a><a href="javascript:void(0)" onclick="call(\''+v.personCode+'\',\''+v.personName+'\',event)"><img src="'+ctxStatic+'/images/icon-phone.png" style="width:15px;margin-left:7px"></img></a>' ;
			            		 }
			            		 if(v.typeComment == '起飞报' || v.typeComment == '落地报' ){
			            			 event = '<a href="javascript:void(0)" onclick="showTip(\''+ v.personName +'\',this)">' + v.typeComment + '</a>';
			            		 }
			            		 tableOptions.data.push({time:time,event:event});
			            	 });
			            	 createDataTable($('#layer_table'),tableOptions);
			             }else{
			            	 layer.alert(dataMap.desc);
			             }
			             
			             openLayer(event,node.style.text?node.style.text:' ',1);
			          }
			     });
				
			}else if(node.id.indexOf('group_') != -1){
				// 流程分组
				var nodeIdArr = node.id.split('_');
				var nodeId = nodeIdArr[1];
				
				$.ajax({
			         type: "POST",
			         url: ctx + "/fltmonitor/getNodeData",
			         data: {inFltid:inFltid,outFltid:outFltid,nodeId:nodeId,hisFlag:hisFlag},
			         dataType: "json",
			         success: function(dataMap){
			             if(dataMap.code == '0'){
			            	 var tableOptions = {
			 						cols:[
			 						      {
			 						    	  field:'time',
			 						    	  title:'时间'
			 						      },
			 						      {
			 						    	  field:'event',
			 						    	  title:'事件'
			 						      }
			 					    ],
			 						data:[]
			 				};
			            	 $.each(dataMap.data,function(k,v){
			            		 var time = v.eventTime;
			            		 var event = v.typeComment;
			            		 if(v.personCode){
			            			 event += '，<a href="javascript:void(0)" onclick="showPersonInfo(\''+v.taskId+'\',event)">'+v.personName+'</a>' ;
			            		 }
//			            		 event += '，<a href="javascript:void(0)" onclick="showPersonInfo(\'24781\',event)">造的人</a>' ;
			            		 tableOptions.data.push({time:time,event:event});
			            	 });
			            	 var $tableDiv = $('<div id="base_table" style="overflow:auto;position: relative;"></div>');
			            	 $('#layer_table').html('').append($tableDiv);
			            	 createDataTable($tableDiv,tableOptions);
			            	 var $infoDiv = $('<div id="base_info"></div>');
			            	 $('#layer_table').append($infoDiv);
			            	 createInformation($infoDiv,nodeId);
			             }else{
			            	 layer.alert(dataMap.desc);
			             }
			             
			             openLayer(event,node.style.text?node.style.text:' ',2);
			          }
			     });
			}
		}
		
	});
}


var openLayer = function (event ,title, openType){
	var offsetX = event.event.clientX;
    if(offsetX > $(window).width() - $('#layer_table').width()){
   	 	offsetX -= $('#layer_table').width();
   	 	if(offsetX <0){offsetX=0}
    }
	 var offsetY = event.event.clientY;
	 if(offsetY > $(window).height() - $('#layer_table').height()){
		 offsetY -= $('#layer_table').height();
		 if(offsetY <20){offsetY=20}
    }
    
	 
	 var area=[];
	 if(openType == 1){
		 $('#layer_table').width(450).height(300);
	 }else if(openType == 2){
		 $('#layer_table').width(450).height(400);
		 $('#base_table').height(200);
	 }
	 
    layer.open({
			type: 1,
			shade: false,
			offset:[offsetY,offsetX],
			title:title,
			content : $('#layer_table'),
			area:area,
			fixed:false,
			resize:false,
			success:function(){
				if(openType == 2){
					new PerfectScrollbar('#base_table');
				 }
				new PerfectScrollbar('#layer_table');
			},
			cancel: function(index, layero){ 
				$('#layer_table').html('');
			}
		});
}

var showTip = function (msg,thisObj){
	layer.tips(msg,thisObj);
}
var call = function (userId,userName){
	$.ajax({
		type:'post',
		url:ctx+"/fltmonitor/getPhoneByUserId",
		data:{
			userId:userId
		},
		success:function(phone){
			if(!phone || phone == ""){
				layer.msg("人员未设置通信号码",{icon:7});
			}else if(phone =="error"){
				layer.msg("呼叫失败",{icon:2});
			}else {
				if ("WebSocket" in window){
					// 打开一个 web socket
					var url = "ws://127.0.0.1:7777";
					var ws = new WebSocket(url);
					ws.onopen = function(){
						var request = "MAKECALL:REQUEST:AUDIO:" + phone;
						ws.send(request);
						console.log("对"+phone+"发起呼叫");
					};
					ws.onmessage = function (evt) { 
						console.log(evt);
						layer.open({
							type:2,
							title:'通讯',
							area: ['300px', '370px'],
							content:ctx + '/message/common/totalk?phones='+phone+'&names='+encodeURIComponent(encodeURIComponent(userName))
						})
						ws.close();
					};
					ws.onclose = function(){ };
				} else {
					layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
				}
			}
		}
	});
}
// 创建数据表格
var createDataTable = function (tableDiv,tableData) {
	
	var $table = $('<table class="layui-table">');
	var $thead = $('<thead>');
	var $tbody = $('<tbody>');
	
	var $th_line = $('<tr>');
	$th_line.append('<th width="80">序号</th>');
	if(tableData && tableData.cols){
		$.each(tableData.cols,function(k,v){
			$th_line.append('<th>'+v.title+'</th>');
		});
	}
	$thead.append($th_line);
	
	if(tableData && tableData.data){
		if(tableData.data.length==0){
			$tbody.append('<tr><td colspan='+(tableData.cols.length+1)+'>没有查询到结果</td></tr>');
		}else{
			$.each(tableData.data,function(k,v){
				var $td_line = $('<tr>'); 
				$td_line.append('<td>' + (k+1) + '</td>');
				$.each(tableData.cols,function(k2,v2){
					$td_line.append('<td>'+(v[v2.field]?v[v2.field]:'&nbsp;')+'</td>');
				});
				$tbody.append($td_line);
			});
		}
	}
	$table.append($thead).append($tbody);
	tableDiv.html('').append($table);
	return tableDiv;
}

// 创建详细信息
var createInformation = function (infoDiv,nodeId){
	if(nodeId == '30'){
		
		// 行李数据
		var xl = $('<div class="group_info"><h4 class="group_info_title">行李数据</h4></div>'); 
		var data = {
				//出港行李重量
				outWeight:'',
				//	出港行李件数
				outLuggageNum:'',
				//	出港集装器/散斗数量
				outOtherNum:'',
				//	进港行李重量
				inWeight:'',
				//	进港行李件数
				inLuggageNum:'',
				//	进港集装器/散斗数量
				inOtherNum:''
		};
		$.ajax({
			type: "POST",
			url: ctx + "/fltmonitor/aaa",
			//data: {inFltid:inFltid,outFltid:outFltid,isY:isY},
			dataType: "json",
			async:false,
			success: function(dataMap){
				if(dataMap.code == '0'){
					data = jQuery.extend(data, dataMap.data);
				}else{
					console.log(dataMap.desc);
				}
			}
		});
		
		var data_xl = $('<div class="row"></div>');
		data_xl.append('<div class="col-sm-12">出港行李重量：'+data.outWeight+'</div>');
		data_xl.append('<div class="col-sm-12">出港行李件数：'+data.outLuggageNum+'</div>');
		data_xl.append('<div class="col-sm-12">出港集装器/散斗数量：'+data.outOtherNum+'</div>');
		data_xl.append('<div class="col-sm-12">&nbsp;</div>');
		data_xl.append('<div class="col-sm-12">进港行李重量：'+data.inWeight+'</div>');
		data_xl.append('<div class="col-sm-12">进港行李件数：'+data.inLuggageNum+'</div>');
		data_xl.append('<div class="col-sm-12">进港集装器/散斗数量：'+data.inOtherNum+'</div>');
		xl.append(data_xl);
		infoDiv.append(xl);
	}else if(nodeId == '31'){
		
		// 货邮数据
		var hy = $('<div class="group_info"><h4 class="group_info_title">货邮数据</h4></div>'); 
		var data = {
				//出港货邮重量
				outCargoNum:'',
				//	出港集装器/散斗数量
				otherNum:'',
				//	进港货邮重量
				inCargoNum:'',
				//	进港集装器/散斗数量
				inNum:''
		};
		$.ajax({
			type: "POST",
			url: ctx + "/fltmonitor/aaa",
			//data: {inFltid:inFltid,outFltid:outFltid,isY:isY},
			dataType: "json",
			async:false,
			success: function(dataMap){
				if(dataMap.code == '0'){
					data = jQuery.extend(data, dataMap.data);
				}else{
					console.log(dataMap.desc);
				}
			}
		});
		var data_hy = $('<div class="row"></div>');
		data_hy.append('<div class="col-sm-12">出港货邮重量：'+data.outCargoNum+'</div>');
		data_hy.append('<div class="col-sm-12">出港集装器/散斗数量：'+data.otherNum+'</div>');
		data_hy.append('<div class="col-sm-12">&nbsp;</div>');
		data_hy.append('<div class="col-sm-12">进港货邮重量：'+data.inCargoNum+'</div>');
		data_hy.append('<div class="col-sm-12">进港集装器/散斗数量：'+data.inNum+'</div>');
		hy.append(data_hy);
		infoDiv.append(hy);
		
	}else if(nodeId == '23'){
		// 旅客数据
		if (inFltid){
			// 进港旅客
			var jg = $('<div class="group_info"><h4 class="group_info_title">进港旅客信息</h4></div>'); 
			var data = {
					//旅客人数
					passengerNum:'',
					// 要客详情
					vipFlagText:''
					
			};
			$.ajax({
				type: "POST",
				url: ctx + "/fltmonitor/getPassengerInfo",
				data: {fltid:inFltid,inout:'in',hisFlag:hisFlag},
				dataType: "json",
				async:false,
				success: function(dataMap){
					if(dataMap.code == '0'){
						data = jQuery.extend(data, dataMap.data);
					}else{
						console.log(dataMap.desc);
					}
				}
			});

			var data_jg = $('<div class="row"></div>');
			data_jg.append('<div class="col-sm-12">旅客人数：'+data.passengerNum+'</div>');
			data_jg.append('<div class="col-sm-12">要客详情：'+data.vipFlagText+' <a href="javascript:void(0)" onclick="showVipText(\'I\',event)">查看</a></div>');
			jg.append(data_jg);
			infoDiv.append(jg);
		}
		if(outFltid){
			// 出港旅客
			var cg = $('<div class="group_info"><h4 class="group_info_title">出港旅客信息</h4></div>'); 
			var data = {
					//旅客人数
					passengerNum:'',
					//订座人数
					bookNum:'',
					//已值机人数
					operatedNum:'',
					//网上值机人数
					onlineOperateNum:'',
					//自助值机
					selfhelpOperateNum:'',
					//柜台值机
					counterOperateNum:'',
					//值机关闭人数
					unoperateNum:'',
					//已过检人数
					seizedNum:'',
					//已登机人数
					boardingNum:'',
					//结束登机人数
					unboardingNum:'',
					// 要客详情
					vipFlagText:''
			};
			$.ajax({
				type: "POST",
				url: ctx + "/fltmonitor/getPassengerInfo",
				data: {fltid:outFltid,inout:'out',hisFlag:hisFlag},
				dataType: "json",
				async:false,
				success: function(dataMap){
					if(dataMap.code == '0'){
						data = jQuery.extend(data, dataMap.data);
					}else{
						console.log(dataMap.desc);
					}
				}
			});
			var data_cg = $('<div class="row"></div>');
			data_cg.append('<div class="col-sm-6">旅客人数：'+data.passengerNum+'</div>');
			data_cg.append('<div class="col-sm-6">订座人数：'+data.bookNum+'</div>');
			data_cg.append('<div class="col-sm-6">已值机人数：'+data.operatedNum+'</div>');
			data_cg.append('<div class="col-sm-6">网上值机人数：'+data.onlineOperateNum+'</div>');
			data_cg.append('<div class="col-sm-6">自助值机：'+data.selfhelpOperateNum+'</div>');
			data_cg.append('<div class="col-sm-6">柜台值机：'+data.counterOperateNum+'</div>');
			data_cg.append('<div class="col-sm-6">值机关闭人数：'+data.unoperateNum+'</div>');
			data_cg.append('<div class="col-sm-6">已过检人数：'+data.seizedNum+'</div>');
			data_cg.append('<div class="col-sm-6">已登机人数：'+data.boardingNum+'</div>');
			data_cg.append('<div class="col-sm-6">结束登机人数：'+data.unboardingNum+'</div>');
			data_cg.append('<div class="col-sm-12">要客详情：'+data.vipFlagText+' <a href="javascript:void(0)" onclick="showVipText(\'O\',event)">查看</a></div>');
			cg.append(data_cg);
			infoDiv.append(cg);
		}
	}
	
} 

/**
 * 展示人员事件详情
 */
var showPersonInfo = function(taskId,event){
	// 清空内容
	$('#taskInfo').html('');
	$.ajax({
		type: "POST",
		url: ctx + "/fltmonitor/getPersonEvent",
		data: {taskId:taskId,hisFlag:hisFlag},
		dataType: "json",
		success: function(dataMap){
			if(dataMap.code == '0'){
				var data = dataMap.data;
				// 流程记录
				var flowDiv = $('<div class="group_info"><h4 class="group_info_title">流程记录</h4></div>'); 
				var flowData = data.flow;
				if(flowData){
					var $row = $('<div class="row"></div>');
					$.each(flowData,function(i,item){
						if(item.time){
							$row.append('<div class="col-sm-12">'+item.time + ' '+ item.eventDes +'</div>');
						}
					});
					flowDiv.append($row);
				}
				$('#taskInfo').append(flowDiv);
				// 提醒消息
				var msgDiv = $('<div class="group_info"><h4 class="group_info_title">提醒消息</h4></div>'); 
				var msgData = data.message;
				if(msgData){
					var $row = $('<div class="row"></div>');
					$.each(msgData,function(i,item){
						if(item.time){
							$row.append('<div class="col-sm-12">'+item.time + ' '+ item.eventDes +'</div>');
						}
					});
					msgDiv.append($row);
				}
				$('#taskInfo').append(msgDiv);
				// 不正常事件
				var abnDiv = $('<div class="group_info"><h4 class="group_info_title">不正常事件</h4></div>'); 
				var abnData = data.abnormal;
				if(abnData){
					var $row = $('<div class="row"></div>');
					$.each(abnData,function(i,item){
						if(item.time){
							$row.append('<div class="col-sm-12">'+item.time + ' '+ item.eventDes +'</div>');
						}
					});
					abnDiv.append($row);
				}
				$('#taskInfo').append(abnDiv);
				
				// 弹窗
				var offsetX = event.clientX;
			    if(offsetX > $(window).width() - $('#taskInfo').width()){
			   	 	offsetX -= $('#taskInfo').width();
			   	 	if(offsetX <0){offsetX=0}
			    }
				 var offsetY = event.clientY;
				 if(offsetY > $(window).height() - $('#taskInfo').height()){
					 offsetY -= $('#taskInfo').height();
					 if(offsetY <20){offsetY=20}
			    }
			    
			    layer.open({
						type: 1,
						shade: false,
						offset:[offsetY,offsetX],
						title:event.target.innerText,
						content : $('#taskInfo'),
						fixed:false,
						resize:false,
						success:function(){
							new PerfectScrollbar('#taskInfo');
						},
						cancel: function(index, layero){ 
							$('#taskInfo').html('');
						}
					});
			}else{
				console.log(dataMap.desc);
			}
		}
	});
}

/**
 * 要客详情弹窗
 */
var showVipText = function (inout_flag,event){
	if($('#fdVipInfoGrid').length > 0){
		return;
	}
	// 弹窗
	var offsetX = event.clientX;
    if(offsetX > $(window).width() - 500){
   	 	offsetX -= $('#taskInfo').width();
   	 	if(offsetX <0){offsetX=0}
    }
	 var offsetY = event.clientY;
	 if(offsetY > $(window).height() - 400){
		 offsetY -= $('#taskInfo').height();
		 if(offsetY <20){offsetY=20}
    }
	layer.open({
		type: 1,
		shade: false,
		offset:[offsetY,offsetX],
		area:['500px','400px'],
		title:'要客详情',
		content : '<div style="padding:0 10px;"><table id="fdVipInfoGrid"></table></div>',
		fixed:false,
		resize:false,
		success:function(){
			/**
			 * 表格列与数据映射
			 * 
			 * @returns {Array}
			 */
			function getVipInfoGridColumns() {
				var columns = [ {
					field : "id",
					title : "序号",
					width : '40px',
					align : 'center',
					valign : "middle",
					formatter : function(value, row, index) {
						row["id"] = index;
						return index + 1;
					}
				}, {
					field : "flightNumber",
					title : '航班号',
					width : '100px',
					align : 'center',
					valign : "middle"
				}, {
					field : "vipFlag",
					title : '要客标识',
					width : '100px',
					align : 'center',
					valign : "middle",
					formatter : function(value, row, index) {
						return vipFlagMap[value];
					}
				}, {
					field : "vipInfo",
					title : '要客详情',
					valign : "middle",
					formatter : function(value, row, index) {
						return value?value:'';
					}
				} ];
				return columns;
			}
			/**
			 * 初始化表格
			 */
			$("#fdVipInfoGrid").bootstrapTable({
				uniqueId : "id",
				url : ctx + "/flightDynamic/getVipDate",
				method : "get",
				pagination : false,
				showRefresh : false,
				search : false,
				clickToSelect : false,
				undefinedText : "",
				height : 350,
				columns : getVipInfoGridColumns(),
				queryParams : function() {
					var param = {
							inFltid : inout_flag=='I'?inFltid:'',
							outFltid : inout_flag=='O'?outFltid:'',
							hisFlag:hisFlag
					};
					return param;
				}
			});

			
		}
	});
}