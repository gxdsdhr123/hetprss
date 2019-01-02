var carHostUrl = 'http://10.32.128.74:8080/'; 

var layer;
var form;

// 页面初始加载的参数表
var initParam = {
		schemaId : $('#schemaId').val(),
		ioTag : '',
		ynTag:'',
		isPart:0,
		updateSession:0
};

layui.use(['layer','form'],function(){
	layer = layui.layer;
	form = layui.form;
	
	form.on('select(ioTag)',function(data){
		var val = data.elem.value;
		initParam.ioTag = val;
		refresh();
	});
	
	form.on('select(ynTag)',function(data){
		var val = data.elem.value;
		initParam.ynTag = val;
		refresh();
	});
	
	form.on('switch(switch)',function(data){
		userSwitch = 1;
		if(data.elem.checked){
			initParam.isPart = 0
		}else{
			initParam.isPart = 1
		}
		refresh();
	});
	
	// 默认开关判定
	loadDefaultSwitch(refresh);
//	setInterval(loadDefaultSwitch, 10000);
	
	// 重置滚动条
	parent.resetScrollBar({
		handlers : ['click-rail', 'drag-thumb', 'keyboard']
	},1000);
	
})

var showloading = true;
//重新加载分配图
var refresh = function(){
	var layerIndex;
	if(showloading){
		layerIndex = layer.load(2);
	}
	
	$.ajax({
        type: "POST",
        url: ctx + '/taskmonitor/getData',
        data : initParam,
        dataType: "json",
        success: function(dataMap){
            if(dataMap.code == '0'){
            	inFltId = 0;
            	outFltId = 0;
            	closeLayer();
            	clearTimeout(popTimer);
        		popTimer=null;
            	$('#monitor').html('').TaskChart(dataMap.data);
            	// 重置刷新机制
				if(sessionStorage.getItem(AUTO_RELOAD_SESSION)){
					autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
				}
				layer.close(layerIndex);
				
				// 加载加班人员弹窗
				loadPersonPopup(dataMap.data);
				
				parent.iframe_ps.update();
            }else{
            	layer.close(layerIndex);
            	layer.msg(dataMap.msg,{icon:7});
            }
         },
         error : function(){
        	 layer.close(layerIndex);
        	 layer.msg('数据访问失败，请稍后重试！',{icon:7});
         }
    });
	if($("#error").length > 0){
		$("#error").click(function(){
			$("#error .label").remove();
		});
		var user = $("#user").val();
		if(!localStorage["latestErrorTimeOf"+user]){
			localStorage["latestErrorTimeOf"+user] = "1970-01-01 00:00:00";
		}
		var time = localStorage["latestErrorTimeOf"+user];
		$.ajax({
			type:'post',
			url:ctx+"/taskmonitor/getUnreadErrorNum",
			data:{
				time:time
			},
			success:function(num){
				$("#error .label").remove();
				if(num != 0){
					$("#error").append("<span class=\"label label-danger\">"+num+"</span>");
				}
			}
		});
	}
}

//加载加班人员弹窗
function loadPersonPopup(data){
	// 重新加载人员
	var personList = data.unsetPersons;
	$.each(personList,function(i,item){
		if($('#workDiv .person-list li[data-code='+item.personId+']').length == 0){
			var person = $('<li class="list-group-item operator is-show" data-code="'+item.personId+'">'+item.personName+'</li>');
			person.on('click',function(){
				if($(this).hasClass("choosed")){
					$(this).removeClass("choosed");
					$("#workDiv .chooseAll").removeClass("allChoosed");
					$("#workDiv .chooseAll").text("全选");
				}else{
					$(this).addClass("choosed");
					if($("#workDiv li").not(".choosed").length == 0){
						$("#workDiv .chooseAll").text("取消全选");
						$("#workDiv .chooseAll").addClass("allChoosed");
					}
				}
			});
			$('#workDiv .person-list').append(person);
		}else{
			$('#workDiv .person-list li[data-code='+item.personId+']').addClass('is-show');
		}
	});
	// 移除不显示人员
	$('#workDiv .person-list li').each(function(){
		if(!$(this).hasClass('is-show')){
			$(this).remove();
		}else{
			$(this).removeClass('is-show');
		}
	});
	// 重置全选状态
	if(personList && $("#workDiv li").not(".choosed").length == 0){
		$("#workDiv .chooseAll").text("取消全选");
		$("#workDiv .chooseAll").addClass("allChoosed");
	}else{
		$("#workDiv .chooseAll").removeClass("allChoosed");
		$("#workDiv .chooseAll").text("全选");
	}
	
	// 是否展示弹出
	/*var popUnset = data.popUnset;
	if(personList && popUnset == '1' && $('#workDiv').css('display') == 'none'){
		openOverWork();
	}*/
}

var userSwitch;
// 默认开关判定
function loadDefaultSwitch(callback){
	$.ajax({
        type: "POST",
        url: ctx + '/taskmonitor/defSwitch',
        data:{schemaId : $('#schemaId').val()},
        dataType: "json",
        success: function(result){
        	if(result.code == 0 && result.data){
        		var stateArr = result.data.split(',');
        		if(stateArr[0] == 1){
        			setSwitch(stateArr[1]);
        			refresh();
        		}else if(!userSwitch){
        			setSwitch(stateArr[1]);
        		} 
        	}
        	if(callback != undefined){
        		callback();
        	}
         }
    });
}

function setSwitch(state){
	if(state == 1){
		$('#switch').prop('checked','checked');
		initParam.isPart = 0
	}else{
		$('#switch').removeAttr('checked');
		initParam.isPart = 1
	}
	form.render();
}

var inFltId;
var outFltId;
$(function(){
	
	var toolbarHeight = 45;
	var dragItem;
	var dragFrom;
	// 手动分配任务，拖拽选择人员信息
	$('#monitor').on('task.unassigned.mousedown',function(e,thisObj,data,type){
		// 清除
		try{
			dragItem.remove();
			dragItem = null;
		}catch(e){}
		// 清除定时器
		stopAutoReload();
		
		var allow;
		$.ajax({
	        type: "POST",
	        url: ctx + '/scheduling/gantt/setJmSemaState',
	        dataType: "text",
	        async:false,
	        success: function(result){
	        	allow = result;
	         }
	    });
		if(allow != 'success'){
			layer.msg(allow,{icon:7});
			return;
		}
		var oEvent = e || event;
		var itemwidth = $('#monitor').TaskChart('get','itemwidth');
		var itemHeight = $('#monitor').TaskChart('get','itemHeight');
		var $this = $(this);
		dragFrom = $(thisObj);
		dragItem = $(thisObj).clone();
		dragItem = dragItem.css('position','absolute').css('z-index',10000);
		dragItem.css('background','none').css('border','2px dotted #b7ffff').css('pointer-events','none');
		dragItem.css('top',$(document).scrollTop() + oEvent.clientY - toolbarHeight - (itemHeight / 2)).css('left',oEvent.clientX - (itemwidth / 2));
		// 相关数据传递
		dragItem.attr('data-type',type);
		$this.append(dragItem);
		$this.on('mousemove',function(e){
			var dEvent = e || event;
			//console.log('[' + ($(document).scrollTop() + dEvent.clientY - (itemHeight / 2)) + ',' + (dEvent.clientX - (itemwidth / 2)) + ']');
			if(dragItem){
				dragItem.css('top',$(document).scrollTop() + dEvent.clientY - toolbarHeight - (itemHeight / 2)).css('left',dEvent.clientX - (itemwidth / 2));
			}
		}).on('mouseup',function(e){
			var target = $(e.target);
			if(target.parents('.item-container').length != 0){
				target = target.parents('.item-container');
			}
			var targettype = target.attr('role-type');
			var targetActor = target.attr('data-id');
			if( (targettype == 'YP' || targettype == 'NP') && target.hasClass('dragging') ){
//			if( targettype == 'YP' || targettype == 'NP'){
					
				// 判断任务时间与人员时间是否冲突，若冲突需弹出提示
				$.ajax({
			        type: "POST",
			        url: ctx + '/taskmonitor/getIfTimeConflict',
			        data: {schemaId : $('#schemaId').val(),operatorId:targetActor,taskId:data.id,fltid:data.fltid},
			        dataType: "json",
			        async:false,
			        success: function(dataMap){
			        	if(dataMap.code == 0){
			        		runSetTask();
			        		clearDrag();
			        	}else{
			        		layer.confirm(dataMap.msg, {icon: 3, title:'提示'}, function(index){
			        			layer.close(index);
			        			runSetTask();
			        			clearDrag();
		        			},function(index){
		        				layer.close(index);
		        				clearDrag();
		        			});
			        	}
			         }
			    });
				
			}else{
				clearDrag();
			}
			
			
			function runSetTask(resultData){
				var msg = '';
				// 接口调用
				$.ajax({
			        type: "POST",
			        url: ctx + '/scheduling/gantt/allocationTask',
			        data: {targetActor:targetActor,jobTaskId:data.id,procId:data.procId},
			        dataType: "text",
			        async:false,
			        success: function(result){
			        	msg = result;
			         },
			         error:function(){
			        	 msg = '服务器发生异常';
			         }
			    });
				if(msg == 'success'){
					var if_high;
					if(dragFrom.hasClass('high')){
						if_high = true;
					}
					data.jobState=2; // 任务状态为执行
					data.taskStatus = '启';	// 状态变更为启
					data.groupNum = target.prop('data-group-num');	// 小组人员
					data.personId = target.attr('data-id');
					data.personName = target.attr('search-text'); // 人名变更
//					if(target.find('.drag-item').length < 4){
						// 人员新增任务
						/*if(data.inOutFlag == 'A'){
							data.status = 2;
						}else if (data.inOutFlag == 'D'){
							data.status = 1;
						}*/
						var targetDragItem = $('#monitor').TaskChart('createDraggable',data.flightName,data,'YP');
						if(if_high){
							targetDragItem.addClass('high');
						}
						if(targettype == 'YP'){
							/*if(target.find('.drag-item:first').length > 0){
								target.find('.drag-item:first').before(targetDragItem);
							}else{
								target.find('.container-title').before(targetDragItem);
							}*/
							target.find('.task-body').prepend(targetDragItem);
						}else{
							/*if(target.find('.drag-item:first').length > 0){
								target.find('.drag-item:last').after(targetDragItem);
							}else{
								target.find('.container-title').after(targetDragItem);
							}*/
							target.find('.task-body').append(targetDragItem);
						}
						$('#monitor').TaskChart('resizeItem',target);
//					}
					
					// 判断任务冲突
					/*if(resultData){
						var taskIds = resultData.ids.split(',');
						$.each(taskIds,function(k,v){
		            		$('.drag-item[task=' + v +']').addClass('item-status-conflict');
		            	});
					}*/
					// 航班任务变为已分配的人
					dragFrom.html(target.attr('search-text') + (data.taskStatus?'('+data.taskStatus+')':''));
					dragFrom.addClass('item-status-'+data.status);
					/*********************补自定义事件*******************/
					dragFrom.off("mousedown");
					// 自定义click事件
					dragFrom.on('click',function(e){
						$this.trigger('drag.click',[this,data,type]);
					});
					// 自定义onmouseover事件
					dragFrom.on('mouseover',function(e){
						$this.trigger('drag.mouseover',[e,this,data,type]);
					});
					// 自定义onmouseout事件
					dragFrom.on('mouseout',function(e){
						$this.trigger('drag.mouseout',[e,this,data,type]);
					});
					// 禁用右键菜单
					dragFrom.on("contextmenu", function (e) {
			            e.preventDefault();
			            return false;
			        });
					dragFrom.on("mousedown", function (event) {
			            if (event.which == 3) {
			            	// 绑定右键事件
			            	$this.trigger('drag.rclick',[event,this,data,type]);
			            }else{
			            	// 已分配mousedown事件
			            	$this.trigger('task.assigned.mousedown',[this,data,type]);
			            }
			        });
				}else{
					clearDrag();
					layer.msg(msg,{icon:7});
				}
				
			}
			
			function clearDrag(){
				$.ajax({
			        type: "POST",
			        url: ctx + '/scheduling/gantt/recoverJmSemaState',
			        data: {},
			        dataType: "json"
			    });
				// 重置刷新机制
				if(sessionStorage.getItem(AUTO_RELOAD_SESSION)){
					autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
				}
				$this.off('mousemove').off('mouseup');
//				$('#airport').removeClass('airport-dragging');
//				$('#terminal').removeClass('terminal-dragging');
				$('.dragging').removeClass('dragging');
				$('.tuijian-1').removeClass('tuijian-1');
				$('.tuijian-2').removeClass('tuijian-2');
				$('.tuijian-3').removeClass('tuijian-3');
				dragItem.remove();
				dragItem = null;
				dragFrom = null;
			}
			
		});
//		$('#airport').addClass('airport-dragging');
//		$('#terminal').addClass('terminal-dragging');
		
		// 自动检测最合适的人员（取前三），给予提示
		$.ajax({
	        type: "POST",
	        url: ctx + '/taskmonitor/getSelectPersons',
	        data: {schemaId : $('#schemaId').val(),taskId:data.id},
	        dataType: "json",
	        async:false,
	        success: function(resultMap){
	        	if(resultMap.code == 0){
	        		var tuijian = 1;
	        		$.each(resultMap.data,function(i,personId){
	        			// 最优推荐样式
	        			var personContainer = $('#airport .item-container[data-id='+personId+']');
	        			if(personContainer.length > 0 && tuijian <= 3){
	        				personContainer.addClass('tuijian-' + tuijian);
	        				tuijian++;
	        			}
	        			$('#airport .item-container[data-id='+personId+']').addClass('dragging');
	        		});
	        		
	        	}
	         }
	    });
		
	});
	
	
	// 任务转交
	$('#monitor').on('task.assigned.mousedown',function(e,thisObj,data,type){
		// 清除
		try{
			dragItem.remove();
			dragItem = null;
		}catch(e){}
		/*if(type == 'NP' || type == 'YP'){
			return;
		}*/
		// 清除定时器
		stopAutoReload();
		var allow;
		$.ajax({
			type: "POST",
			url: ctx + '/scheduling/gantt/setJmSemaState',
			dataType: "text",
			async:false,
			success: function(result){
				allow = result;
			}
		});
		if(allow != 'success'){
			layer.msg(allow,{icon:7});
			return;
		}
		var oEvent = e || event;
		var itemwidth = $('#monitor').TaskChart('get','itemwidth');
		var itemHeight = $('#monitor').TaskChart('get','itemHeight');
		var $this = $(this);
		
		dragItem = $(thisObj).clone();
		dragItem = dragItem.css('position','absolute').css('z-index',10000);
		dragItem.css('background','none').css('border','2px dotted #b7ffff').css('pointer-events','none');
		dragItem.css('top',$(document).scrollTop() + oEvent.clientY - toolbarHeight - (itemHeight / 2)).css('left',oEvent.clientX - (itemwidth / 2));
		// 相关数据传递
		dragItem.attr('data-type',type);
		$this.append(dragItem);
		$this.on('mousemove',function(e){
			var dEvent = e || event;
			//console.log('[' + ($(document).scrollTop() + dEvent.clientY - (itemHeight / 2)) + ',' + (dEvent.clientX - (itemwidth / 2)) + ']');
			if(dragItem){
				dragItem.css('top',$(document).scrollTop() + dEvent.clientY - toolbarHeight - (itemHeight / 2)).css('left',dEvent.clientX - (itemwidth / 2));
			}
		}).on('mouseup',function(e){
			var target = $(e.target);
			if(target.parents('.item-container').length != 0){
				target = target.parents('.item-container');
			}
			
			// 禁止转交给自己
			if(data.personName == target.attr('search-text')){
				clearDrag()
				return;
			}
			
			var targettype = target.attr('role-type');
			var targetActor = target.attr('data-id');
			if( (targettype == 'YP' || targettype == 'NP') && target.hasClass('dragging') ){
//			if(targettype == 'YP' || targettype == 'NP'){
				
				// 判断任务时间与人员时间是否冲突，若冲突需弹出提示
				$.ajax({
			        type: "POST",
			        url: ctx + '/taskmonitor/getIfTimeConflict',
			        data: {schemaId : $('#schemaId').val(),operatorId:targetActor,taskId:data.id,fltid:data.fltid},
			        dataType: "json",
			        async:false,
			        success: function(dataMap){
			        	if(dataMap.code == 0){
			        		runSetTask();
			        		clearDrag();
			        	}else{
			        		layer.confirm(dataMap.msg, {icon: 3, title:'提示'}, function(index){
			        			layer.close(index);
			        			runSetTask(dataMap.data);
			        			clearDrag();
		        			},function(index){
		        				layer.close(index);
		        				clearDrag();
		        			});
			        	}
			         }
			    });
				
			}else{
				clearDrag();
			}
			
			
			
			function runSetTask(resultData){
				var msg = '';
				// 接口调用
				$.ajax({
					type: "POST",
					url: ctx + '/workflow/btnEvent/doTransferPre',
					data: {targetActor:targetActor,jobTaskId:data.id,procId:data.procId},
					dataType: "text",
					async:false,
					success: function(result){
						msg = result;
					},
			         error:function(){
			        	 msg = '服务器发生异常';
			         }
				});
				if(msg == 'success'||msg=='succeed'){
					
					var dragFrom = $('.yFlight .drag-item[task='+data.id+'],.nFlight .drag-item[task='+data.id+']');
					if(dragFrom.hasClass('item-status-conflict')){
						refresh();
						return;
					}
					
					var if_high;
					// 删除原任务人员
					var oldItem = $('#airport .drag-item[task='+data.id+']');
					if(oldItem.hasClass('high')){
						if_high = true;
					}
					var oldContainer = oldItem.parent();
					oldItem.remove();
					$('#monitor').TaskChart('resizeItem',oldContainer);
//					if(target.find('.drag-item').length < 4){
						// 人员新增任务
						var targetDragItem = $('#monitor').TaskChart('createDraggable',data.flightName,data,'YP');
						if(if_high){
							targetDragItem.addClass('high');
						}
						if(targettype == 'YP'){
							/*if(target.find('.drag-item:first').length > 0){
								target.find('.drag-item:first').before(targetDragItem);
							}else{
								target.find('.container-title').before(targetDragItem);
							}*/
							target.find('.task-body').prepend(targetDragItem);
						}else{
							/*if(target.find('.drag-item:first').length > 0){
								target.find('.drag-item:last').after(targetDragItem);
							}else{
								target.find('.container-title').after(targetDragItem);
							}*/
							target.find('.task-body').append(targetDragItem);
						}
						$('#monitor').TaskChart('resizeItem',target);
//					}
					// 更新data数据
					data.personName = target.attr('search-text');
					data.personId = targetActor;
					data.groupNum = target.prop('data-group-num');	// 小组人员
					// 航班任务变为已分配的人
					dragFrom.html(target.attr('search-text') +  (data.taskStatus?'('+data.taskStatus+')':''));
					/*if(type == 'YF'){
						dragFrom.addClass('item-status-2');
					}else{
						dragFrom.addClass('item-status-1');
					}*/
					
					/*if(dragItem.attr('data-type') == 'YF'){
						data.status = 2;
					}else if (dragItem.attr('data-type') == 'NF'){
						data.status = 1;
					}*/
					
					// 判断任务冲突
					/*if(resultData){
						var taskIds = resultData.ids.split(',');
						$.each(taskIds,function(k,v){
		            		$('.drag-item[task=' + v +']').addClass('item-status-conflict');
		            	});
					}else{
						$('.drag-item[task=' + data.id +']').removeClass('item-status-conflict');
					}*/
				}else{
					if(msg){
						layer.msg(msg,{icon:7});
					}else{
						layer.msg('该任务无法被转交！',{icon:7});
					}
					clearDrag();
				}
				
			}
			
			function clearDrag(){
				
				$.ajax({
			        type: "POST",
			        url: ctx + '/scheduling/gantt/recoverJmSemaState',
			        data: {},
			        dataType: "json"
			    });
				// 重置刷新机制
				if(sessionStorage.getItem(AUTO_RELOAD_SESSION)){
					autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
				}
				$this.off('mousemove').off('mouseup');
//				$('#airport').removeClass('airport-dragging');
//				$('#terminal').removeClass('terminal-dragging');
				$('.dragging').removeClass('dragging');
				$('.tuijian-1').removeClass('tuijian-1');
				$('.tuijian-2').removeClass('tuijian-2');
				$('.tuijian-3').removeClass('tuijian-3');
				dragItem.remove();
				dragItem = null;
				dragFrom = null;
			}
			
			
		});
//		$('#airport').addClass('airport-dragging');
//		$('#terminal').addClass('terminal-dragging');
		
		// 自动检测最合适的人员（取前三），给予提示
		$.ajax({
	        type: "POST",
	        url: ctx + '/taskmonitor/getSelectPersons',
	        data: {schemaId : $('#schemaId').val(),taskId:data.id},
	        dataType: "json",
	        async:false,
	        success: function(resultMap){
	        	if(resultMap.code == 0){
	        		var tuijian = 1;
	        		$.each(resultMap.data,function(i,personId){
	        			// 最优推荐样式
	        			var personContainer = $('#airport .item-container[data-id='+personId+']');
	        			if(personContainer.length > 0 && tuijian <= 3){
	        				personContainer.addClass('tuijian-' + tuijian);
	        				tuijian++;
	        			}
	        			$('#airport .item-container[data-id='+personId+']').addClass('dragging');
	        		});
	        		
	        	}
	         }
	    });
		
		
	});
	

	$('#monitor').on('flight.title.click',function(e,thisObj,data,type){
		// 任务选中
		$('.container-title').removeClass('selected');
		$(thisObj).addClass('selected');
		inFltId = data.inFltid;
		outFltId = data.outFltid;
	});
	
	
	// 任务块右键事件
	$('#monitor').on('drag.rclick',function(e,event,thisObj,data,type){

		var $this = $(this);
    	/*var offsetX = event.clientX;
        if(offsetX > $(window).width() - 160){
       	 	offsetX -= 160;
       	 	if(offsetX <0){offsetX=0}
        }
        
    	 var offsetY = event.clientY - toolbarHeight;
    	 if(offsetY > $(window).height() - 50){
    		 offsetY -= 50;
    		 if(offsetY <20){offsetY=20}
        }*/
    	 
    	 var rightArr = [ {
			name : '高亮显示',
			callback : function(e) {
				$.ajax({
			        type: "POST",
			        url: ctx + '/taskmonitor/highTasks',
			        data: {schemaId : $('#schemaId').val(),taskId:data.id},
			        dataType: "json",
			        success: function(dataMap){
			            if(dataMap.code == '0'){
			            	var taskIds = dataMap.data;
			            	$('.drag-item').removeClass('high');
			            	$.each(taskIds,function(k,v){
			            		$('.drag-item[task=' + v +']').addClass('high');
			            	});
			            }else{
			           	 	layer.msg(dataMap.msg,{icon:7});
			            }
			         }
			    });
			}
		} ];
    	 // 航班任务新增 释放人员 / 任务重置
    	 if($(thisObj).parents('.nFlight,.yFlight').length > 0){
    		 if(data.jobState == 1 || data.jobState == 2){
    			 // 释放人员
    			 rightArr.push({
    				name : '释放人员',
					callback : function(e) {
						$.ajax({
					        type: "POST",
					        url: ctx + '/scheduling/jobManage/releaseOperator',
					        data: {taskId:data.id,personId:data.personId},
					        dataType: "text",
					        success: function(msg){
					            if(msg == 'success'){
					            	/*// 任务块状态变更
									data.status = 6;
									data.jobState = 6;
									var title = "&nbsp;";
									$(thisObj).html(title);
									$(thisObj).removeClass('item-status-1').removeClass('item-status-2').removeClass('item-status-conflict').addClass('item-status-6');
									// 重新定义事件
									$(thisObj).off('click').off('mousedown');
									$(thisObj).on("mousedown", function (event) {
							            if (event.which == 3) {
							            	// 绑定右键事件
							            	$this.trigger('drag.rclick',[event,this,data,type]);
							            }else{
							            	// 未分配mousedown事件
							            	$this.trigger('task.unassigned.mousedown',[this,data,type]);
							            }
							        });
									// 删除人员中对应的任务块
									var personItem = $('#airport .drag-item[task='+data.id+'] , #terminal .drag-item[task='+data.id+']');
									var container = personItem.parents('.item-container');
									personItem.remove();
									$('#monitor').TaskChart('resizeItem',container);*/
					            	refresh();
					            }else{
					            	layer.msg(msg,{icon:7});
					            }
					         }
					    });
					}
    			 });
    		 }else if (data.jobState == 6){
    			 // 任务恢复
    			 rightArr.push({
     				name : '任务恢复',
 					callback : function(e) {
 						$.ajax({
 					        type: "POST",
 					        url: ctx + '/scheduling/jobManage/recoveryTask',
 					        data: {taskId:data.id},
 					        dataType: "text",
 					        success: function(msg){
 					            if(msg == 'success'){
 					            	// 任务块状态变更
 			 						/*data.status = 0;
 			 						data.jobState = 0;
 			 						$(thisObj).removeClass('item-status-6').addClass('item-status-0');*/
 					            	refresh();
 					            }else{
 					            	layer.msg(msg,{icon:7});
 					            }
 					         }
 					    });
 					}
     			 });
    		 }
    		 
    		 
    		 rightArr.push({
 				name : '终止任务',
				callback : function(e) {
					stopTask(data);
				}
 			 });
    		 
    		 rightArr.push({
    			 name : '删除任务',
    			 callback : function(e) {
    				 deleteTask(data);
    			 }
    		 });
    		 
    	 }
    	 
		$('#monitor').TaskChart('popMenu', event, rightArr);
		
	});
	
	
	// 人员姓名右键事件
	$('#monitor').on('person.title.rclick',function(e,event,thisObj,data,type){
		/*var offsetX = event.clientX;
        if(offsetX > $(window).width() - 160){
       	 	offsetX -= 160;
       	 	if(offsetX <0){offsetX=0}
        }
        
    	 var offsetY = event.clientY - toolbarHeight;
    	 if(offsetY > $(window).height() - 50){
    		 offsetY -= 50;
    		 if(offsetY <20){offsetY=20}
        }*/
    	 
    	 var popArr = [ {
			name : '拨打电话',
			callback : function(e) {
				talk(data.personId,0);
			}
		}];
    	 
    	 if($(thisObj).hasClass('is_stop')){
    		 popArr.push({
    				name : '恢复',
    				callback : function(e) {
    					doResumeWork(data.personId +'@' + data.personName );
    				}
    			});
    	 }else{
    		 popArr.push({
 				name : '停用',
 				callback : function(e) {
 					doStopWork(data.personId +'@' + data.personName);
 				}
 			});
    	 }
    	 /*// 回到待命区
    	 popArr.push({
    		 name:'回到待命区',
    		 children:[
    		           {
    		        	   name:'回到8坪',
    		        	   callback:function(){
    		        		   operatorReturn(0,'P8');
    		        	   }
    		           },{
    		        	   name:'回到T2',
    		        	   callback:function(){
    		        		   operatorReturn(1,'T2');
    		        	   }
    		           }
    		 ]
    	 });*/
    	 
		$('#monitor').TaskChart('popMenu', event, popArr);
		
		function operatorReturn(posType,posName){
			$.ajax({
				type: "POST",
				url: ctx + '/taskmonitor/operatorReturn',
				data: {schemaId : $('#schemaId').val(),posType:posType,posName:posName,operator:data.personId},
				dataType: "json",
				success: function(dataMap){
					if(dataMap.code == 0){
						layer.msg('操作成功！',{icon:1});
						refresh();
					}else{
						layer.msg(dataMap.msg,{icon:7});
					}
				}
     		});
		}
	});
	
	// 航班右键事件
	$('#monitor').on('flight.title.rclick',function(e,event,thisObj,data,type){
		// 清除定时器
		stopAutoReload();
		
		// 菜单定位
		/*var offsetX = event.clientX;
        if(offsetX > $(window).width() - 160){
       	 	offsetX -= 160;
       	 	if(offsetX <0){offsetX=0}
        }
        
    	 var offsetY = event.clientY - toolbarHeight;
    	 if(offsetY > $(window).height() - 50){
    		 offsetY -= 50;
    		 if(offsetY <20){offsetY=20}
        }*/
    	 
    	// 进出港类型判断
    	var inout;
    	if(data.id == data.outFltid){
    		inout = 'D';
    	}else{
    		inout = 'A';
    	}
    	// 加载二级菜单项
     	var childsNode = [];
     	
     	$.each(jobTypes,function(i,item){
     		var typeCode = item.typeCode;
     		var typeName = item.typeName;
     		
     		var thisItem = {name:typeName , children:[{
            	name : '暂无可用规则',
            	callback : function(){reset();}
            }]};
     		
     		// 获取三级规则列表
     		$.ajax({
				type: "POST",
				url: ctx + '/taskmonitor/getRules',
				data: {schemaId : $('#schemaId').val(),jobType:typeCode,inOut:inout},
				dataType: "json",
				async : false,
				success: function(dataMap){
					if(dataMap.code == 0){
						// 规则
						if(dataMap.data){
							thisItem.children = [];
							$.each(dataMap.data,function(index , o){
								thisItem.children.push({
									name : o.RULENAME,
									callback : function(){
										// 根据规则生成任务
										createTask(o.RULEID);
										reset();
									}
								});
							});
						}
					}else{
						console.log(dataMap.msg);
					}
				}
     		});
     		
     		childsNode.push(thisItem);
     	});
     	
    	var popArr = [{
 			name : '创建任务',
 			children : childsNode
 		}];
    	if(data.inOutFlag == 'A'){
    		popArr.push({
	 			name : '未做清舱',
	 			callback : function(e) {
	 				saveNoClean(data.inFltid);
				}
    		});
    	}
    	
    	function reset(){
    		// 重置刷新机制
    		if(sessionStorage.getItem(AUTO_RELOAD_SESSION)&&sessionStorage.getItem(AUTO_RELOAD_SESSION)!='null'){
    			autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
    		}
    	}
    	
    	function createTask(ruleId){
    		// 调用作业管理中的添加行方法（应该只有一行），根据规则创建行
    		$.ajax({
    			type : 'post',
    			url : ctx + "/scheduling/jobManage/addRow",
    			async : false,
    			dataType : 'json',
    			data : {
    				inFltId : data.inFltid?data.inFltid:'',
    				outFltId :  data.outFltid?data.outFltid:'',
    				ruleId : ruleId
    			},
    			success : function(data) {
    				var paramData = [];
    				$.each(data,function(i,item){
    					if(item.processId){
    						paramData.push(item);
    					}
    				});
    				// 执行提交
    				$.ajax({
    	    			type : 'post',
    	    			url : ctx + "/scheduling/jobManage/save",
    	    			async : false,
    	    			dataType : 'text',
    	    			data : {taskList:JSON.stringify(paramData)},
    	    			success : function(msg) {
    	    				if(msg == 'succeed'){
    	    					layer.msg("发布成功！",{icon:1,time:600},function(){
    	    						refresh();
    	    					});
    	    				}else {
    	    					layer.alert("发布失败："+msg,{icon:5});
    	    				}
    	    			},
    	    			error : function(){
    	    				layer.alert("发布失败，请联系管理员或稍后重试",{icon:5});
    	    			}
    	    		});
    			},
    			error : function(){
    				layer.alert("发布失败，该航班无法创建这个任务",{icon:5});
    			}
    		});
    	}
		
    	/*航班下屏*/
    	/*popArr.push({
			name : '航班下屏',
			callback : function(e) {
				layer.confirm("执行航班下屏会将该航班已有任务删除，是否继续？",function(index){
					screenLoading = layer.load(2);
					if(data.inFltid){
						doFlightOutScreen(data.inFltid);
					}
					
					if(data.outFltid){
						doFlightOutScreen(data.outFltid);
					}
					
					layer.close(index);
					
				})
			}
		});*/
    	
    	
    	$('#monitor').TaskChart('popMenu', event, popArr);
	});
	
	// 航班详情
	$('#monitor').on('flight.title.mouseover',function(e,event,thisObj,data,type){
		if(popTimer){
			return;
		}
		popTimer = setTimeout(function(){
			if(popLayer){
				return;
			}
			var pop;
			var inOutType = data.inOutFlag;
			var fltid;
			if(inOutType == 1){
				//进港
				pop = $('#flight-in'); 
				pop.width(450).height(90);
				fltid = data.inFltid;
			}else if(inOutType == 2){
				// 出港
				pop = $('#flight-out'); 
				pop.width(450).height(90);
				fltid = data.outFltid;
			}else if(inOutType == 3){
				// 进出港
				pop = $('#flight-inout'); 
				pop.width(500).height(120);
				fltid = data.inFltid;
			}
			
			$.ajax({
				type: "POST",
				url: ctx + '/taskmonitor/getFlightInfo',
				data: {schemaId : $('#schemaId').val(),fltid:fltid,type:inOutType,updateSession:0},
				dataType: "json",
				success: function(dataMap){
					if(dataMap.code == '0'){
						pop.find('span').each(function(){
							var k =$(this).attr('data-id');
							var v = dataMap.data[k];
							setProperty(pop,k,v);
						});
						openLayer(event,pop);
					}else{
						layer.msg(dataMap.msg,{icon:7});
					}
					clearTimeout(popTimer);
					popTimer=null;
				}
				
			});
		},300);
		
	}).on('flight.title.mouseout',function(e,event,thisObj,data,type){
    	closeLayer();
    	clearTimeout(popTimer);
		popTimer=null;
    });
	// 任务详情
	$('#monitor').on('drag.mouseover',function(e,event,thisObj,data,type){
		if(popTimer){
			return;
		}
		popTimer = setTimeout(function(){
			if(popLayer){
				return;
			}
			// 未分配人的只显示名字
			if(!data.personName){
				layer.tips(data.taskName, thisObj);
				return;
			}
			var pop = $('#task-info');
			pop.width(400).height(110);
			
			$.ajax({
				type: "POST",
				url: ctx + '/taskmonitor/getTaskInfo',
				data: {schemaId : $('#schemaId').val(),taskId:data.id,updateSession:0},
				dataType: "json",
				success: function(dataMap){
					if(dataMap.code == '0'){
						pop.find('span').each(function(){
							var k =$(this).attr('data-id');
							var v = dataMap.data[k];
							setProperty(pop,k,v);
						});
						pop.find('#nodes').html('');
						var nodes = dataMap.data.nodes;
						if(nodes){
							$.each(nodes,function(k,v){
								var _row = $('<div class="row">');
								_row.append('<div class="col-sm-6">预计'+v.name+'：<span>'+(v.eVal?v.eVal:'')+'</span></div>');
								_row.append('<div class="col-sm-6">实际'+v.name+'：<span>'+(v.aVal?v.aVal:'')+'</span></div>');
								pop.find('#nodes').append(_row);
							});
						}
						
						openLayer(event,pop);
					}else{
						layer.msg(dataMap.msg,{icon:7});
					}
				}
			});
		},300);
	}).on('drag.mouseout',function(e,event,thisObj,data,type){
		closeLayer();
    	clearTimeout(popTimer);
		popTimer=null;
    });
	
	var setProperty = function(pop,id,value){
		pop.find('span[data-id='+id+']').html(value?value:'');
	}
	
	// 人员左键点击
	$('#monitor').on('person.title.click',function(e,event,thisObj,data,type){
		
		$.ajax({
			type: "POST",
			url: ctx + '/taskmonitor/getWorkingGroupMember',
			data: {schemaId : $('#schemaId').val(),id:data.personId,updateSession:0},
			dataType: "json",
			success: function(dataMap){
				if(dataMap.code == '0'){
					var pop = $('#team-pop').html('').width(100);
					var resultData = dataMap.data;
					if(resultData){
						$.each(resultData,function(i,item){
							pop.append('<p>'+item+'</p>');
						});
					}
					var oEvent = event;
					var offsetX = oEvent.clientX + 10;
				    if(offsetX > $(window).width() - pop.width()){
				   	 	offsetX -= pop.width();
				   	 	if(offsetX <0){offsetX=0}
				    }
					 var offsetY = oEvent.clientY + 10;
					 if(offsetY > $(window).height() - pop.height()){
						 offsetY -= pop.height();
						 if(offsetY <20){offsetY=20}
				    }
					layer.open({
						type: 1,
						shade: 0.00000000000000000000000000000000000001,
						shadeClose:true,
						offset:[offsetY,offsetX],
						title:false,
						closeBtn:false,
						content : pop,
						fixed:false,
						resize:false,
						anim: -1,
						isOutAnim :false,
						cancel: function(index, layero){ 
							popLayer = null;
						}
					});
				}else{
					layer.msg(dataMap.msg,{icon:7});
				}
			}
		});
	});
});

var popTimer;
var popLayer;
/**弹窗*/
var openLayer = function (e , pop){
	closeLayer();
	var oEvent = e || event;
	var offsetX = oEvent.clientX + 10;
    if(offsetX > $(window).width() - pop.outerWidth()){
   	 	offsetX -= (pop.outerWidth() + 20);
   	 	if(offsetX <0){offsetX=0}
    }
	 var offsetY = oEvent.clientY + 10;
	 if(offsetY > $(window).height() - pop.outerHeight()){
		 offsetY -= pop.outerHeight();
		 if(offsetY <20){offsetY=20}
    }
	popLayer = layer.open({
		type: 1,
		shade: false,
		offset:[offsetY,offsetX],
		title:false,
		closeBtn:false,
		content : pop,
		fixed:false,
		resize:false,
		anim: -1,
		isOutAnim :false,
		area:[450],
		cancel: function(index, layero){ 
			popLayer = null;
		}
	});
}

var closeLayer = function (){
	layer.close(popLayer);
//	layer.closeAll();
	popLayer = null;
}


/**
 * 按钮功能
 */
var AUTO_RELOAD_SESSION = null;//自动刷新频率缓存

$(function(){
	AUTO_RELOAD_SESSION = $("#loginName").val()+$("#schemaId").val()+"refresh";
	
	//刷新
	if(!sessionStorage){
		console.log("该浏览器不支持本地缓存");
	} else {
		if(sessionStorage.getItem(AUTO_RELOAD_SESSION)&&sessionStorage.getItem(AUTO_RELOAD_SESSION)!='null'){
			autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
		}else{
			autoReload("0.5");
		}
	}
	$("#refresh").click(function() {
		/*clearInterval(intervalObj);
		$("#refresh").text("刷新");*/
		refresh();
		// 重置刷新机制
		if(sessionStorage.getItem(AUTO_RELOAD_SESSION)&&sessionStorage.getItem(AUTO_RELOAD_SESSION)!='null'){
			autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
		}
		/*if(sessionStorage&&sessionStorage.getItem(AUTO_RELOAD_SESSION)&&sessionStorage.getItem(AUTO_RELOAD_SESSION)!='null'){
			sessionStorage.removeItem(AUTO_RELOAD_SESSION);
		}*/
		
	})
	$("#refresh-c").click(function() {
		layer.open({
			title : '自定义刷新',
			content : '每<input type="number" min="1" class="layui-input layui-sm" style="width:120px;margin:0px 10px;display:inline-block"/>分钟自动刷新',
			yes : function(index, layero) {
				var refreshTime = layero.find("input").val();
				if (refreshTime != null && refreshTime != "") {
					autoReload(refreshTime);
				}
				layer.close(index);
			}
		});
	})
	// 点击空白地方重新激活刷新功能
	$('body').click(function(){
		if(!intervalObj){
			autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
		}
	});
	// 列表
	$("#graph").click(function() {
			var schemaId = $('#schemaId').val();
			var form = $("<form id='graphForm' style='display:none' action='"+ctx + "/scheduling/list/list'></form>")
			form.append($("<input type='text' name='schemaId' value='"+schemaId+"'>"));
			$("body").append(form);
			$("#graphForm").submit();
		});
	// 人员分工
	$("#divisionBtn").click(function() {
		doWorkerDivision();
	});
	// 人员计划
	$("#memberPlan").click(function() {
		memberPlan();
	});
	//任务分配
	$("#autoManual").click(function() {
		iframe = layer.open({
			type : 2,
			title : "任务分配",
			closeBtn : false,
			area : ['400px','200px'],
			content : ctx + "/scheduling/list/autoManual",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var json = iframeWin.sub();
				var loading = layer.load(2, {
					shade : [ 0.1, '#000' ]
				});
				$.ajax({
					type : 'post',
					async : false,
					url : ctx + "/scheduling/list/updateAutoManual",
					data : {
						'autoManual' :JSON.stringify(json)
					},
					error : function() {
						layer.close(loading);
						layer.msg('保存失败！', {
							icon : 2
						});
					},
					success : function(data) {
						layer.close(loading);
						if (data == "success") {
							layer.msg('保存成功！', {
								icon : 1,
								time : 600
							},function(){
								layer.close(iframe);
							});
						} else {
							layer.msg('保存失败！', {
								icon : 2,
								time : 600
							});
						}
					}
				});
				return false;
			}
		});
	});
	// 作业管理
	$("#jobManageBtn").click(function() {
		jobManage();
	});
	// 报文
	$("#message").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		var msg = layer.open({
			type : 1,
			title : false,
			closeBtn : 0,
			area:[$("body").width()-200+"px",$("body").height()-200+"px"],
			content : $("#msgDiv"),
			btn : [ "确认" ],
			success : function(layero, index) {
				setTimeout(function() {
					$("#msgTable").bootstrapTable(messageTableOptions);
				}, 100);
			}
		});
		layer.full(msg);
	});
	// 指令
	$('#order').click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		sended(clickRow);
	});
	// 异常情况查看
	$("#error").click(function() {
		iframe = layer.open({
			type : 2,
			title : "异常情况查看",
			area : [ '100%', '100%' ],
			content : ctx + "/flightDynamic/error"
		});
	});
	
	
	
	// 搜索功能
	$('#search_text').on('keydown',function(event){
		if(event.keyCode == 13){
			var index = $('#monitor').TaskChart('search',$(this).val(),$('#search_type').val());
			if(index == -1){
				layer.msg('没有搜索到 '+$(this).val()+'！',{icon:2});
				return;
			}
			// 重置刷新机制
			if(sessionStorage.getItem(AUTO_RELOAD_SESSION)){
				autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
			}
		}
	})
	
	// 加班弹窗
	$('#work').on('click',function(){
		openOverWork();
	});
	new PerfectScrollbar($("#workDiv ul")[0]);
	$("#workDiv .chooseAll").click(function(){
		if($(this).hasClass("allChoosed")){
			$("#workDiv li").removeClass("choosed");
			$(this).removeClass("allChoosed");
			$(this).text("全选");
		}else{
			$("#workDiv li").addClass("choosed");
			$(this).addClass("allChoosed");
			$(this).text("取消全选");
		}
	});
	initShiftsTable();
	$("#shifts").click(function(){
		$(".clickRow").removeClass("clickRow");
		shiftsRow = null;
		layer.open({
			title:'班制',
			type:1,
			content:$("#shiftsDiv"),
			area:['800px','300px'],
			btn : ["确认","关闭"],
			yes : function(index, layero){
				if(!shiftsRow){
					layer.msg("请选择一条班制",{icon:7});
				}
				$("#overWorkStart").val(shiftsRow.start);
				$("#overWorkEnd").val(shiftsRow.end);
				layer.close(index);
			}
		});
	});
});
var shiftsRow;
function initShiftsTable(){
	$("#shiftsTable").bootstrapTable({
		url : ctx + "/taskmonitor/getShifts", // 请求后台的URL（*）
		method : "get", // 请求方式（*）
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		height : 145,
		columns : [
			{
				field : "name",
				title : "名称"
			},{
				field : "start",
				title : "开始时间"
 			},{
 				field : "end",
 				title : "结束时间"
 			}
		],
		onClickRow : function(row, tr, field) {
			$(".clickRow").removeClass("clickRow");
			shiftsRow = row;
			tr.addClass("clickRow");
		}
	});
}
function openOverWork(){
	layer.open({
		title:'加班',
		type:1,
		content:$('#workDiv'),
		area:['800px','300px'],
		btn : ["确认","关闭"],
		success:function(layero, index){
			var now = new Date();
			var hour = now.getHours();
			var min = now.getMinutes();
			var end = "";
			$("#overWorkStart").val(zeroize(hour,2)+zeroize(min,2));
			if(hour>15){
				end = zeroize((hour+8)%24,2)+zeroize(min,2)+"+";
			}else{
				end = zeroize(hour+8,2)+zeroize(min,2);
			}
			$("#overWorkEnd").val(end);
		},
		yes:function(index, layero){
			var start = $("#overWorkStart").val();
			var end = $("#overWorkEnd").val();
			if($("#workDiv .choosed").length == 0){
				layer.msg("请选择加班人员", {icon: 7}); 
				return false;
			}
			var reg = /^(0\d|1\d|2[0-3])[0-5]\d\+?$/;
			var re = new RegExp(reg);
			if(!start || start=="" || !re.test(start)){
				$("#overWorkStart").parent().addClass("has-error");
				setTimeout(function(){$("#overWorkStart").parent().removeClass("has-error");},2000);
				return false;
			}
			if(!end || end=="" || !re.test(end)){
				$("#overWorkEnd").parent().addClass("has-error");
				setTimeout(function(){$("#overWorkEnd").parent().removeClass("has-error");},2000);
				return false;
			}
			var ids = [];
			$("#workDiv .choosed").each(function(i,ele){
				ids.push($(ele).data("code"));
			})
			var loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			// 0.1透明度
			});
			$.ajax({
				type:'post',
				url:ctx+"/taskmonitor/setOverWorkTime",
				async:false,
				data:{
					schemaId : $('#schemaId').val(),
					'ids':ids.toString(),
					'start':start,
					'end':end
				},
				dataType:'json',
				error:function(){
					layer.close(loading);
					layer.msg('网络错误，保存失败', {icon: 2}); 
				},
				success:function(res){
					layer.close(loading);
					if(res.code == 0){
						layer.msg("设置成功！", {icon: 1});
						layer.close(index);
						refresh();
		        	}else{
		        		layer.msg(res.msg, {icon: 2});
		        	}
				}
			});
		}
	});
}
//补零
var zeroize = function(value,length){
	value = value+"";
	for(var i=0;i<(length-value.length);i++){
		value = "0"+value;
	}
	return value;
}
/**
 * 人员分工
 */
function doWorkerDivision() {
	layer.open({
		type : 2,
		title : false,
		closeBtn : false,
		area : [ '100%', '100%' ],
		btn : [ "返回" ],
		content : [ ctx + "/division/info/list" ]
	});
}
/**
 * 员工计划
 */
function memberPlan() {
	layer.open({
		type : 2,
		title : false,
		closeBtn : false,
		area : [ '100%', '100%' ],
		btn : [ "返回" ],
		content : [ ctx + "/arrange/empplan/list" ]
	});
}
/**
 * 作业管理
 */
function jobManage() {
	var schemaId = $("#schemaId").val();
	if(!inFltId && !outFltId){
		layer.msg("请选择一个航班！",{icon:7});
		return;
	}
	layer.open({
		type : 2,
		title : false,
		closeBtn : false,
		area : [ '100%', '100%' ],
		btn : [ "返回" ],
		content : [ ctx + "/scheduling/jobManage?schemaId=" + schemaId + "&inFltId=" + inFltId + "&outFltId=" + outFltId ]
	});
}
/**
 * 指令
 */
function sended(row) {
	var fltid;
	if (ioFlag == "I") {
		fltid = row.in_fltid
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid
	}
	var num = "1";
	var msg = layer.open({
		type : 2,
		title : '指令消息',
		zIndex : 99999999999,
		maxmin : false,
		shadeClose : false,
		area:[$("body").width()-200+"px",$("body").height()-200+"px"],
		content : ctx + "/message/history/list?num=" + num + "&fltid=" + fltid,
	});
	layer.full(msg);
}

/**
 * 自动刷新
 * baochl 20171117
 * @param interval 刷新频率
 */
var intervalObj = null;
var refreshUnit = 60*1000;//刷新单位是分钟
function autoReload(interval){
	if(interval<0.16){
		layer.msg("最小刷新频率不要小于10秒！",{icon:7});
		return false;
	}
	if(sessionStorage){
		sessionStorage.setItem(AUTO_RELOAD_SESSION,interval);
	}
	var time = Number(interval) * refreshUnit;
	stopAutoReload();
	var btnText;
	if(interval < 1){
		time = Math.round(interval*60) * 1000;
		btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + Math.round(interval*60) + "秒钟";
	}else{
		btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "分钟";
	}
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		refresh();
	}, time);
}
// 停止定时刷新
function stopAutoReload(){
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	intervalObj = null;
}
// 停止员工
function doStopWork(personId){
	$("#stopStart").val("");
	$("#stopEnd").val("");
	
	layer.open({
		type:1,
		title: "停用时段",
		offset: '20px',
		content:$("#planRangeDate"),
		btn:["确认","返回"],
		success:function(layero,index){
			var now = new Date();
			layero.find(".rangeDate").eq(0).val(now.getFullYear()+"-"+zeroize((now.getMonth()+1),2)+"-"+zeroize(now.getDate(),2)+" "+zeroize(now.getHours(),2)+":"+zeroize(now.getMinutes(),2));
			$.ajax({
				type:'post',
				url: ctx + "/arrange/empplan/getClosestShiftEndTime",
				data:{
					workerId : personId.split("@")[0]
				},
				success:function(time){
					layero.find(".rangeDate").eq(1).val(time);
				}
			});
		},
		yes:function(index, layero){
			var times = "";
			var time1 = layero.find(".rangeDate").eq(0).val();
			var time2 = layero.find(".rangeDate").eq(1).val();
			if(time1==""||time2==""){
				layer.msg('请选择要停用开始时间或结束时间', {
					icon : 2
				});
				return false;
			}else{
				times = time1+":00"+","+time2+":00";
				var reason = $("#reason option:selected").text();
				$.ajax({
					type : 'post',
					url : ctx + "/arrange/empplan/saveStopPlan",
					data : {
						times : times,
						reason:reason,
						ids:[personId]
					},
					success : function(msg) {
						if (msg == "check") {
							layer.msg('此人员还有未完成的任务，请处理！', {
								icon : 2
							});
							return false;
						}else if (msg == "success") {
							layer.msg('保存成功！', {
								icon : 1,
								time : 600
							});
							refresh();
							layer.close(index);
						}
					}
				});
			}
		}
	});
}
// 恢复工作
function doResumeWork(personId){
	
	layer.confirm("是否恢复已选择人员工作？",{offset:'100px'},function(index){
		$.ajax({
			type : 'post',
			url : ctx + "/arrange/empplan/resumePlan",
			data : {
				ids:[personId]
			},
			success : function(msg) {
				if (msg == "success") {
					layer.msg('恢复成功！', {
						icon : 1,
						time : 600
					});
					refresh();
				}
			}
		});
		layer.close(index);
	});
}

//删除任务
function deleteTask(data){
	var jobState = data.jobState;//任务状态
	if(jobState=="2"){
		layer.msg("任务执行中，不可以删除！",{icon:7});
		return false;
	} else if(jobState=="3"){
		layer.msg("任务已执行完成，不可以删除！",{icon:7});
		return false;
	}
	var confirm = layer.confirm('您确定要删除当前任务？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var jobTaskId = data.id;//任务ID
		var orderId = data.orderId;//流程实例id
		var procId = data.procId;
		if(jobTaskId){
			var loading = layer.load(2, {
				shade : [ 0.1, '#000' ]
			// 0.1透明度
			});
			$.ajax({
				type : 'post',
				url : ctx + "/scheduling/jobManage/remove",
				async : false,
				data : {
					id : jobTaskId,
					orderId:orderId,
					procId:procId,
					personId:data.personId
				},
				success : function(data) {
					layer.close(loading);
					if(data=="succeed"){
						layer.msg("删除成功！",{icon:1,time:600},function(){
							refresh();
						});
					} else {
						layer.msg(data,{icon:7});
					}
				},
				error:function(){
					layer.msg('任务删除失败！',{icon:7});
				}
			});
		} else {
			layer.msg("请选择一条任务！",{icon:7});
		}
	});
}

//终止任务
function stopTask(data){
	var jobState = data.jobState;//任务状态
	if(jobState!='2'){
		layer.msg("只有【执行中】的任务才可以终止！",{icon:7});
		return false;
	}
	var confirm = layer.confirm('您确定要终止当前任务？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var jobTaskId = data.id;//任务ID
		var orderId = data.orderId;//流程实例id
		var procId = data.procId;
		$.ajax({
			type : 'post',
			url : ctx + "/workflow/btnEvent/doTermination",
			async : false,
			data : {
				procId:procId,
				jobTaskId : jobTaskId,
				orderId:orderId
			},
			success : function(data) {
				if(data=="succeed"){
					layer.msg("任务已中止！",{icon:1,time:600},function(){
						refresh();
					});
				} else {
					layer.msg("中止失败！"+data,{icon:7});
				}
			}
		});
	});
}


/** 保存未做清舱航班 */
function saveNoClean(inFltId){
	$.ajax({
		type : 'post',
		url : ctx + "/taskmonitor/saveNoClean",
		data : {
			inFltId:inFltId,
			type:"QC",
			schemaId:$('#schemaId').val()
		},
		success : function(dataMap) {
			if(dataMap.data == 1){
				layer.msg('标记成功！', {
					icon : 1,
					time : 1600
				});
			}else{
				layer.msg(dataMap.msg, {
					icon : 1,
					time : 1600
				});
			};
		}
	});
}

/** 打开未做清舱航班列表 */
function openNoClean(){
	layer.open({
		type:2,
		title:"未做清舱航班列表",
		content:ctx + '/taskmonitor/openNoClean?schemaId='+$('#schemaId').val(),
		area:['1100px','100%'],
		success: function (index, layero) {
	        $(':focus').blur();
	    },
		btn:['关闭']
	});
}

/**
 * 航班手动下屏
 * @param fltid
 */
function doFlightOutScreen(fltid){
	// 获取是否有未完成的任务
	$.ajax({
		type : 'post',
		url : ctx + "/taskmonitor/getFlightUnfinishedTasks",
		data : {
			fltid:fltid,
			schemaId:$('#schemaId').val()
		},
		success : function(dataMap) {
			if(dataMap.code == '0'){
				var tasks = dataMap.data;
				if(tasks){
					screenTaskNum += tasks.length;
					// 如果有任务未完成则进行删除终止操作
					$.each(tasks,function(i,task){
						if(task.OPERATOR){
							// 有人则终止任务
							$.ajax({
								type : 'post',
								url : ctx + "/workflow/btnEvent/doTermination",
								data : {
									jobTaskId : task.ID,
									orderId:task.ORDER_ID,
									procId:task.PROC_ID
								},
								success : function(data) {
									if(data!="succeed"){
										console.log(dataMap.msg);
									}
									screenRefresh();
								}
							});
						}else{
							// 未分配则删除任务
							$.ajax({
								type : 'post',
								url : ctx + "/scheduling/jobManage/remove",
								data : {
									id : task.ID,
									orderId:task.ORDER_ID,
									procId:task.PROC_ID,
									personId:task.OPERATOR
								},
								success : function(data) {
									if(data!="succeed"){
										console.log(dataMap.msg);
									}
									screenRefresh();
								}
							});
						}
					});
				}else{
					screenTaskNum++;
					// 标记下屏
					$.ajax({
						type : 'post',
						url : ctx + "/taskmonitor/flightOutScreen",
						data : {
							fltid:fltid,
							schemaId:$('#schemaId').val()
						},
						success : function(dataMap) {
							if(dataMap.code != '0'){
								console.log(dataMap.msg);
							}
							screenRefresh();
						}
					});
				}
			}else{
				console.log(dataMap.msg);
			}
		}
	});
}

var screenTaskNum = 0
var screenLoading;

function screenRefresh(){
	screenTaskNum--;
	if(screenTaskNum == 0){
		layer.close(screenLoading);
		refresh();
	}
}


/**图例*/
function showLegend(){
//	layer.msg('图例正在制作中……',{icon:5});
	layer.open({
		type:2,
		title:"图例说明",
		content:ctx + '/taskmonitor/legend?schemaId='+$('#schemaId').val(),
		area:['1100px','600px'],
		btn:['关闭']
	});
}
