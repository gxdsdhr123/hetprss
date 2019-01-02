(function ($) {
	
	var global = {
	};
	
	var $this;
	// 横向间隔
	var itemspace = 130;
	// 方块宽度
	var itemwidth = 80; 
	// 方块宽度
	var personItemwidth = 90; 
	// 方块高度
	var itemHeight = 32;
	// 最多人员任务
	var max_person_item = 7;
	// 最多航班任务
	var max_flight_item = 3;
	// 航班区域title高度
	var flightTitleHeight = 100;
	// 航班区域高度
	var flightHeight = 223;
	// 人员区域高度
	var personHeight = 390;
	// 滚动条高度
	var scrollbarHeight = 15;
	// 机坪/航站楼名字宽度
	var areaTextWidth = 25;
	// 人员区域类别宽度
	var flightTypeWidth = 50;
	
	// 记录滚动条位置
	var scrollRate = {
		topScrollRate : 0,
		middleScrollRate : 0,
//		middle2ScrollRate : 0,
		bottomScrollRate : 0
	}
	
	/**
	 * 绘制DOM
	 */
	var draw = function (options) {
		// 画板大小
		$this.height(flightHeight*2+personHeight+scrollbarHeight*3);
		// 航班
		
		
		// 大飞机
		var yFlightTitle = $('<div class="area-title">').width(flightTypeWidth).height(flightHeight).css({'top':scrollbarHeight+'px',left:0});
		var yFlightContent = $('<div class="area-content">').width($this.width() - flightTypeWidth).height(flightHeight).css({'top':scrollbarHeight+'px',left:flightTypeWidth+'px'});
		var yFlightDiv = $('<div class="yFlight" id="yFlight"></div>');
		yFlightTitle.html('<h4>大飞机</h4>')
		
		// 小飞机
		var nFlightTitle = $('<div class="area-title">').width(flightTypeWidth).height(flightHeight).css({'top':flightHeight+scrollbarHeight*2+'px',left:0});
		var nFlightContent = $('<div class="area-content">').width($this.width() - flightTypeWidth).height(flightHeight).css({'top':flightHeight+scrollbarHeight*2+'px',left:flightTypeWidth+'px'});
		var nFlightDiv = $('<div class="yFlight" id="nFlight"></div>');
		nFlightTitle.html('<h4>小飞机</h4>')
		
		var num1 = 0;
		var num2 = 0;
		var title_num1 = 0;
		var title_num2 = 0;
		if (options.terminal){
			$.each(options.terminal,function(i,item){
				if(item.name == '大飞机'){
					// 大飞机
					var terminal_item = $('<div class="terminal-item"></div>');
					var flights = item.flights;
					if(flights){
						$.each(flights,function(k,v){
							// 出港单个人员容器
							var container = createContainer(v , 'YF');
							terminal_item.append(container);
							num1++;
						})
						// 样式
						terminal_item.width(itemspace * flights.length).height(flightHeight);
					}
					yFlightDiv.append(terminal_item);
					title_num1++;
					
				}else if(item.name == '小飞机'){
					// 小飞机
					var terminal_item = $('<div class="terminal-item"></div>');
					var flights = item.flights;
					if(flights){
						$.each(flights,function(k,v){
							// 出港单个人员容器
							var container = createContainer(v , 'YF');
							terminal_item.append(container);
							num2++;
						})
						// 样式
						terminal_item.width(itemspace * flights.length).height(flightHeight);
					}
					nFlightDiv.append(terminal_item);
					title_num2++;
				}
				
			});
		}
		/*yFlightDiv.width(itemspace * num1 + areaTextWidth * title_num1).height(flightHeight).css('top',scrollbarHeight);
		if(yFlightDiv.width() < $this.width()){
			yFlightDiv.width($this.width());
		}
		$this.append(yFlightDiv);
		nFlightDiv.width(itemspace * num2 + areaTextWidth * title_num2).height(flightHeight).css('top',flightHeight+scrollbarHeight*2+'px');
		if(nFlightDiv.width() < $this.width()){
			nFlightDiv.width($this.width());
		}
		$this.append(nFlightDiv);*/
		
		yFlightDiv.width(itemspace * num1).height(flightHeight);
		if(yFlightDiv.width() < yFlightContent.width()){
			yFlightDiv.width(yFlightContent.width());
		}
		yFlightContent.append(yFlightDiv);
		$this.append(yFlightTitle,yFlightContent);
		
		
		nFlightDiv.width(itemspace * num2).height(flightHeight);
		if(nFlightDiv.width() < nFlightContent.width()){
			nFlightDiv.width(nFlightContent.width());
		}
		nFlightContent.append(nFlightDiv);
		$this.append(nFlightTitle,nFlightContent);
		
		// 人员
		var airportDiv = $('<div class="airport" id="airport"></div>');
		num1 = 0;
		title_num1 = 0;
		if(options.airport){
			$.each(options.airport,function(k,v){
//				if(v.name != '待排'){
				// 显示在上的人员
				var persons = v.persons;
				if(persons){
					// 机坪容器
					var airport_item = $('<div class="airport-item"></div>');
					var title = $('<div class="airport-title">' + v.name + '</div>').width(areaTextWidth - 5);
					airport_item.append(title);
					var persons = v.persons;
					if(persons){
						$.each(persons,function(i,p){
							// 进港单个人员容器
							var container = createContainer(p , 'YP');
							airport_item.append(container);
							num1++;
						})
					}
					// 样式
					airport_item.width(itemspace * persons.length + (v.name?areaTextWidth:0)).height(personHeight);
					//airport_item.find('.airport-title').width(itemspace * persons.length + areaTextWidth);
					airportDiv.append(airport_item);
					title_num1++;
				}
				
			});
			
		}
		
		airportDiv.width(itemspace * num1 + areaTextWidth * (title_num1-1)).height(personHeight).css('top',scrollbarHeight*3+flightHeight*2+'px');
		if(airportDiv.width() < $this.width()){
			airportDiv.width($this.width());
		}
		$this.append(airportDiv);
		
		// 画滚动条
		var topScroll = $('<div id="topScroll" class="sBar"><span></span></div>');
		topScroll.height(scrollbarHeight).css('top',0);
		$this.append(topScroll);
		var middleScroll = $('<div id="middleScroll" class="sBar"><span></span></div>');
		middleScroll.height(scrollbarHeight).css('top',scrollbarHeight+flightHeight);
		$this.append(middleScroll);
		/*var middle2Scroll = $('<div id="middle2Scroll" class="sBar"><span></span></div>');
		middle2Scroll.height(scrollbarHeight).css('top',scrollbarHeight*2+flightHeight+personHeight);
		$this.append(middle2Scroll);*/
		var bottomScroll = $('<div id="bottomScroll" class="sBar"><span></span></div>');
		bottomScroll.height(scrollbarHeight).css('top',scrollbarHeight*2+flightHeight*2);
		$this.append(bottomScroll);
		
		// 滑块样式
		$('.sBar').find('span').height(scrollbarHeight - 4).css('top',2);
		
		// 新增航班任务滚动条
		$('.task-body,.title-group').each(function(){
			new PerfectScrollbar(this);
		});
		
	}

	/**
	 * 创建航班/任务容器
	 * 
	 * @param data
	 * @param type 类型：YF 上方航班 , YP 上方人员, NF 下方航班, NP 下方人员
	 */
	var createContainer = function (data , type){
		var container;
		if(type == 'YF' || type == 'NP'){
			container = $('<div class="item-container top-container" role-type="'+type+'" data-id="'+(data.personId?data.personId:'')+'"   search-text="'+(data.name?data.name:data.personName)+'" actstand-code="'+(data.seat?data.seat:'')+'" ></div>');
		}else if (type == 'YP' || type == 'NF'){
			container = $('<div class="item-container bottom-container" role-type="'+type+'" data-id="'+(data.personId?data.personId:'')+'"    search-text="'+(data.name?data.name:data.personName)+'" actstand-code="'+(data.seat?data.seat:'')+'" ></div>');
		}
		if(type == 'YF' || type == 'NF'){
			var title = $('<div class="container-title"></div>').width(itemspace-10).height(flightTitleHeight-10);	// 标题
			title.append('<h4 class="in-number">' + (data.inVipFlag?data.inVipFlag+' ':'') + (data.inFltNum?data.inFltNum:'———') + '</h4>');	//进港航班号
			title.append('<h4 class="out-number">' + (data.outVipFlag?data.outVipFlag+' ':'') + (data.outFltNum?data.outFltNum:'———') + '</h4>');	//出港航班号 
			title.append('<span class="title-time">' + (data.inTime?data.inTime:'----')+' | '+(data.outTime?data.outTime:'----') + '</span>');	// 进/出港显示时间
			if(data.inSeat && data.outSeat && data.inSeat != data.outSeat){
				title.append( '<br/><span class="in-seat">'+(data.inSeat?data.inSeat:'--') + '</span> | <span class="out-seat">'+(data.outSeat?data.outSeat:'--') + '</span>');	// 机位
			}else{
				var seat = '--';
				if(data.inSeat){
					seat = data.inSeat;
				}else if(data.outSeat){
					seat = data.outSeat;
				}
				title.append( '<br/><span class="in-seat out-seat">' + seat + '</span>');	// 机位
			}
			
			// 自定义click事件
			title.on('click',function(e){
				$this.trigger('flight.title.click',[this,data,type]);
			});
			// 禁用右键菜单
			title.off("mousedown").on("contextmenu", function (e) {
	            e.preventDefault();
	            return false;
	        });
			// 绑定右键事件
			title.off("mousedown").on("mousedown", function (event) {
	            if (event.which == 3) {
	            	$this.trigger('flight.title.rclick',[event,this,data,type]);
	            }
	        });
			// 自定义onmouseover事件
			title.on('mouseover',function(e){
				$this.trigger('flight.title.mouseover',[e,this,data,type]);
			});
			// 自定义onmouseout事件
			title.on('mouseout',function(e){
				$this.trigger('flight.title.mouseout',[e,this,data,type]);
			});
			// 判断是否有下划线
			/*if(data.ifLine == 1){
				title.addClass('title-underline');
			}*/
			
			// 是否显示为红色
			if(data.ifRed == 1){
				title.find('.in-number').addClass('title-red');
			}else if(data.ifRed == 2){
				title.find('.out-number').addClass('title-red');
			}else if(data.ifRed == 3){
				title.find('.in-number').addClass('title-red');
				title.find('.out-number').addClass('title-red');
			}
		
			// 判断是否绿色背景
			if(data.ifGreen == 1){
				title.addClass('title-gray');
			}else if(data.ifGreen == 2){
				title.addClass('title-yellow');
			}else if(data.ifGreen == 3){
				title.addClass('title-green');
			}else if(data.ifGreen == 4){
				title.addClass('title-blue');
			}
			
			// 远近机位
			if(data.inActKind == 'N'){
				title.find('.in-seat').addClass('font-red');
			}
			if(data.outActKind == 'N'){
				title.find('.out-seat').addClass('font-red');
			}
			
			title.css('cursor','pointer');
			title.css('margin-top', -(flightTitleHeight - 10) + 'px');
			// 判断航班实际保障人数小于标准作业人员数
			if(data.ifCarOk == 1){
				container.addClass('to-more-pax');
				title.css('margin-top', -(flightTitleHeight - 8) + 'px');
			}
			container.append(title);
			container.width(itemspace - 30);
		}else if(type == 'NP' || type == 'YP'){
			container.prop('data-group-num',data.groupNum);
			var title = $('<div class="container-title"></div>').width(personItemwidth);	// 标题
			if(data.ifPlus == 1){
				title.append('+ ');	// 加班人员
			}
			if(data.personName){
				title.append('<span class="'+(data.ifUnderLine==1?'under-line':'')+'">'+ data.personName + '</span>('+(data.finishNum?data.finishNum:'0')+')');	// 人员姓名
			}
			if(data.ifStar == 1){
				title.append(' *');	// 快下班的人
			}
			title.append('<br/><span class="car-id">'+ (data.areaName? data.areaName :'&nbsp;') + '</span>');	// 机位
			
			var group = $('<div class="title-group"></div>').height(personHeight-max_person_item*itemHeight-60);
			$.each(data.teamName.split(','),function(i,name){
				group.append(name + '<br/>');
			});
			
			// 人员状态
			if(data.status){
				title.addClass('title-status-' + data.status);
			}else{
				title.addClass('title-status-0');
			}
			// 是否被停用
			if(data.ifStop == 1){
				title.addClass('is_stop');
			}
			// 自定义click事件
			title.on('click',function(e){
				$this.trigger('person.title.click',[e,this,data,type]);
			});
			// 禁用右键菜单
			title.off("mousedown").on("contextmenu", function (e) {
	            e.preventDefault();
	            return false;
	        });
			// 绑定右键事件
			title.off("mousedown").on("mousedown", function (event) {
	            if (event.which == 3) {
	            	$this.trigger('person.title.rclick',[event,this,data,type]);
	            }
	        });
			container.append(title);
			container.append(group);
			container.width(itemspace - 10);
		}
		//任务新加一层，用于超出可滚动
		var flightTaskBody = $('<div class="task-body"></div>').height(flightHeight - (flightTitleHeight + 20));
		var tasks = data.task;	//任务
		if(tasks){
			// 添加任务块
			$.each(tasks,function(i,t){
				if(type == 'YF' || type == 'NF'){
					flightTaskBody.append(createDraggable(t.personName,t,type));
//					container.append(createDraggable(t.personName,t,type));
				}else if(type == 'NP'){
					flightTaskBody.append(createDraggable(t.flightName,t,type));
				}else if(type == 'YP'){
					flightTaskBody.prepend(createDraggable(t.flightName,t,type));
				}
			});
		}
		// 任务新加一层，用于超出可滚动
		if(type=='NP'){
			flightTaskBody.height(personHeight - 30);
			container.append(flightTaskBody);
		}else if(type=='YP'){
			flightTaskBody.height(personHeight - 30);
			container.prepend(flightTaskBody);
		}else{
			container.append(flightTaskBody);
		}
		 return resizeItem(container,type);
	}
	
	// 创建可拖拽的小方块
	var createDraggable = function (title,data,type) {
		if(!title){
			if(data.taskDescribe){
				title = data.taskDescribe;
			}else{
				title = '&nbsp;';
			}
		}else{
			/*if(type == 'YF' || type == 'NF'){
				var groupNum =  '('+(data.groupNum?data.groupNum:'0')+')';
				title += groupNum;
			}else{*/
				var taskStatus = data.taskStatus?'('+data.taskStatus+')':'';
				title += taskStatus
//			}
		}
		var content = $('<div class="drag-item" task="'+data.id+'">'+title+'</div>').width(itemwidth);
		if(type == 'YP' || type == 'NP'){
			content.width(personItemwidth);
		}
		if(data.ifOverWorkTime){
			content.addClass('person-status-1');
		}
		/*if(data.ifConflict){
			content.addClass('item-status-conflict');
		}*/
		
		// 自定义mousedown事件，为实现拖拽选择人员信息
		if((data.jobState == 0 || data.jobState == 4 || data.jobState == 6)){
			content.addClass('item-status-' + data.jobState);
			// 自定义onmouseover事件
			content.on('mouseover',function(e){
				$this.trigger('drag.mouseover',[e,this,data,type]);
			});
			// 自定义onmouseout事件
			content.on('mouseout',function(e){
				$this.trigger('drag.mouseout',[e,this,data,type]);
			});
			
			// 禁用右键菜单
			content.on("contextmenu", function (e) {
	            e.preventDefault();
	            return false;
	        });
			content.on("mousedown", function (event) {
	            if (event.which == 3) {
	            	// 绑定右键事件
	            	$this.trigger('drag.rclick',[event,this,data,type]);
	            }else{
	            	// 未分配mousedown事件
	            	$this.trigger('task.unassigned.mousedown',[this,data,type]);
	            }
	        });
		}else{
			content.addClass('item-status-' + data.status);
			// 自定义click事件
			content.on('click',function(e){
				$this.trigger('drag.click',[this,data,type]);
			});
			// 自定义onmouseover事件
			content.on('mouseover',function(e){
				$this.trigger('drag.mouseover',[e,this,data,type]);
			});
			// 自定义onmouseout事件
			content.on('mouseout',function(e){
				$this.trigger('drag.mouseout',[e,this,data,type]);
			});
			
			// 禁用右键菜单
			content.on("contextmenu", function (e) {
	            e.preventDefault();
	            return false;
	        });
			content.on("mousedown", function (event) {
	            if (event.which == 3) {
	            	// 绑定右键事件
	            	$this.trigger('drag.rclick',[event,this,data,type]);
	            }else{
	            	// 已分配mousedown事件
	            	$this.trigger('task.assigned.mousedown',[this,data,type]);
	            }
	        });
		}
		return content;
	}
	
	// 设置任务容器起始位置
	var resizeItem = function (container , type){
		if(type == 'YP'){
			var item_length = container.find('.drag-item').length;
			if(item_length > max_person_item){
				item_length = max_person_item;
			}
			var top =  (max_person_item - item_length) * itemHeight;
			var height = personHeight - top;
			container.css('padding-top',top + 'px').height(height);
			container.find('.task-body').height(item_length*itemHeight+6);
		}else	if(type == 'NF'){
			container.css('margin-top',flightTitleHeight + 'px').height(flightHeight - (flightTitleHeight + 10));
		}else if (type == 'NP') {
			container.height(personHeight);
			container.find('.task-body').height(max_person_item*itemHeight+6);
		}else if (type == 'YF') {
			container.css('margin-top',flightTitleHeight + 'px').height(flightHeight - (flightTitleHeight + 10));
		}
		return container
	}
	
	/**
	 * 加载滚动条事件
	 */
	var scrollEvent = function () {
		setScroll('topScroll',['yFlight']);
		setScroll('middleScroll',['nFlight']);
//		setScroll('middle2Scroll',['terminal']);
		setScroll('bottomScroll',['airport']);
		
		// 初始位置设置
		scrollTo('topScroll', scrollRate.topScrollRate);
		scrollTo('middleScroll', scrollRate.middleScrollRate);
//		scrollTo('middle2Scroll', scrollRate.middle2ScrollRate);
		scrollTo('bottomScroll', scrollRate.bottomScrollRate);
	}
	
	// 设置滚动条事件
	var setScroll = function (scrollBarId,scrollContainerIds){
		var scrollBar = $('#' + scrollBarId);
		var barWidth = 0;
		var containers = [];
		$.each(scrollContainerIds,function(k,v){
			containers.push($('#'+ v )[0]);
			barWidth = Math.max(barWidth , $('#'+ v ).width());
		});
		
		var oSbar = scrollBar[0];
		var oSbtn = oSbar.children[0];
		
		var oMain = $this[0];
		var oCont = containers;

		//拖拽
		oSbtn.onmousedown = function(e) {
			var oEvent = e || event;

			var disX = oEvent.clientX - oSbtn.offsetLeft;

			function fnMove(e) {
				var oEvent = e || event;
				var l = oEvent.clientX - disX;
				
				setDirection({
					left : l
				});
			}

			function fnUp() {
				this.onmousemove = null;
				this.onmouseup = null;

				if (oSbar.releaseCapture)
					oSbar.releaseCapture();
			}

			if (oSbar.setCapture) {
				oSbar.onmousemove = fnMove;
				oSbar.onmouseup = fnUp;
				oSbar.setCapture();
			} else {
				document.onmousemove = fnMove;
				document.onmouseup = fnUp;
			}

			return false;
		};

		//判断滚动距离
		function setDirection(json) {
			if (json.left <= 0) {
				json.left = 0;
			} else if (json.left > oSbar.offsetWidth
					- oSbtn.offsetWidth) {
				json.left = oSbar.offsetWidth - oSbtn.offsetWidth;
			}
			oSbtn.style.left = json.left + 'px';

			var scale = json.left
					/ (oSbar.offsetWidth - oSbtn.offsetWidth);
			scrollRate[scrollBarId + 'Rate'] = scale;
			$.each(oCont , function(k,v){
				/*var spaceNum = 0;
				if(-(v.offsetWidth - oMain.offsetWidth) * scale % itemspace == 0){
					spaceNum = parseInt((v.offsetWidth - oMain.offsetWidth) * scale / itemspace);
				}else{
					spaceNum = parseInt((v.offsetWidth - oMain.offsetWidth) * scale / itemspace) + 1;
				}*/
				v.style.left = - (v.offsetWidth - oMain.offsetWidth) * scale + 'px';
			});
		}

		//加鼠标滚轮
		$.each(oCont , function(k,v){
			addMouseWheel(v, function(d) {
				var mouseX = 0;
	
				if (d) {
					mouseX = oSbtn.offsetLeft - 20;
				} else {
					mouseX = oSbtn.offsetLeft + 20;
				}
	
				setDirection({
					left : mouseX
				});
			});
		});
		//算滑块大小
		var oBtnW = oMain.offsetWidth / barWidth
				* oSbar.offsetWidth;

		if (oBtnW < 50)
			oBtnW = 50;

		oSbtn.style.width = oBtnW + 'px';
		
	}
	
	//加鼠标滚轮
	var addMouseWheel = function(obj, fn) {
		obj.onmousewheel = fnWheel;
		if (obj.addEventListener)
			obj.addEventListener('DOMMouseScroll', fnWheel, false);

		function fnWheel(e) {
			var oEvent = e || event;
			var d = oEvent.wheelDelta ? oEvent.wheelDelta > 0
					: oEvent.detail < 0;

			fn(d);

			if (oEvent.preventDefault)
				oEvent.preventDefault();

			return false;
		}
	}
	
	/**
	 * 滚动至
	 * @param scrollId 滚动条ID
	 * @param leftRate 滚动百分比
	 */
	var scrollTo = function (scrollId , leftRate){
		var containerMap = {
				'topScroll':['yFlight'],
				'middleScroll':['nFlight'],
//				'middle2Scroll':['terminal'],
				'bottomScroll':['airport'],
			};
		var oSbar = $('#'+scrollId)[0];
		var oSbtn = oSbar.children[0];
		
		var left = (oSbar.offsetWidth - oSbtn.offsetWidth) * leftRate;
//		console.log(scrollId +','+left + ',' + oSbar.offsetWidth+',' + oSbtn.offsetWidth +',' + leftRate);   
		if (left <= 0) {
			left = 0;
		} else if (left > (oSbar.offsetWidth - oSbtn.offsetWidth)) {
			left = oSbar.offsetWidth - oSbtn.offsetWidth;
		}
		oSbtn.style.left = left + 'px';
		scrollRate[scrollId + 'Rate'] = leftRate;
		$.each(containerMap[scrollId] , function(k,v){
			v = $('#'+v)[0];
			v.style.left = - (v.offsetWidth - $this[0].offsetWidth) * leftRate + 'px';
		});
		
	}
	
	/**
	 * 滚动至（搜索专用）
	 * @param scrollId 滚动条ID
	 * @param leftRate 滚动百分比
	 */
	var searchScrollTo = function (scrollId , leftRate){
		var containerMap = {
				'topScroll':['yFlight'],
				'middleScroll':['nFlight'],
//				'middle2Scroll':['terminal'],
				'bottomScroll':['airport'],
			};
		
		var oSbar = $('#'+scrollId)[0];
		var oSbtn = oSbar.children[0];
		
		var left = oSbar.offsetWidth * leftRate;
		
		if (left <= 0) {
			left = 0;
		} else if (left > oSbar.offsetWidth
				- oSbtn.offsetWidth) {
			left = oSbar.offsetWidth - oSbtn.offsetWidth;
		}
		oSbtn.style.left = left + 'px';

		var scale = left
				/ (oSbar.offsetWidth - oSbtn.offsetWidth);
		scrollRate[scrollId + 'Rate'] = scale;
		$.each(containerMap[scrollId] , function(k,v){
			v = $('#'+v)[0];
			v.style.left = - (v.offsetWidth - $this[0].offsetWidth) * scale + 'px';
		});
		
	}
	
	// 右键菜单宽度
	var popMenuWidth = 160;
	// 右键菜单高度
	var popMenuHeight = 29;
	// 右键菜单纵向空白
	var popMenuYspace = 12;
	// 左右弹出方向（默认右侧）
	var popMenuPosition = 'r';
	// 菜单层级（默认一级）
	var popMenuLevel = 1;
	// 菜单最多展示行数
	var popMenuMaxLine = 10;
	
	var contextMenu;
	
	var createPopMenu = function(menuList , isChildren , event){
		// 计算参数
		if(!isChildren){
			popMenuLevel = 1;
			// 获取菜单层数
			popMenuLevel = getChildNum(menuList,popMenuLevel);
			// 计算往左侧弹出还是右侧弹出
			 if(event.clientX - $this.offset().left > $(window).width() - $this.offset().left - popMenuWidth * popMenuLevel){
				 popMenuPosition = 'l';
	        }else{
	        	popMenuPosition = 'r';
	        }
		}
		
		// 菜单容器
		var menuContent;
		if(isChildren){
			// 创建子菜单
			menuContent = $('<ul class="dropdown-menu" style="position: absolute;z-index: 1100;">');
		}else{
			if(!contextMenu || $this.find('#context-menu').length == 0){
				// 创建一级菜单
				contextMenu = $('<ul class="dropdown-menu" style="position: absolute;z-index: 1100;">');
				$this.append(contextMenu);
				$this.on('click',function(){
					contextMenu.hide();
				});
			}
			contextMenu.html('');
			
			// 菜单定位
			var offsetX = event.clientX - $this.offset().left;
	        if(offsetX > $(window).width() - $this.offset().left -  popMenuWidth){
	       	 	offsetX -= popMenuWidth;
	       	 	if(offsetX <$this.offset().left){offsetX=$this.offset().left}
	        }
	        
	    	 var offsetY = event.clientY - $this.offset().top;
	    	 if(offsetY > $(window).height()  - $this.offset().top - (menuList.length * popMenuHeight + popMenuYspace)){
	    		 offsetY -= (menuList.length * popMenuHeight + popMenuYspace);
	    		 if(offsetY <$this.offset().top){offsetY=$this.offset().top}
	        }
	    	contextMenu.css({'top': offsetY + $(document).scrollTop() , 'left': offsetX});
	    	 
	    	menuContent = contextMenu;
		}
		// 循环菜单内容
		$.each(menuList , function(k,v){
			if(v.children){
				// 如果含有子菜单则继续创建子菜单项
				var menuItem = $('<li class="dropdown"><a>'+v.name+'<b class="fa fa-chevron-right"></b></a></li>');
				var childNode = createPopMenu(v.children, true).css({'display':'none'});
				menuItem.append(childNode);
				menuItem.on('mouseover',function(e){
					if(popMenuPosition == 'l'){
						childNode.css({'left' : -popMenuWidth});
					}else{
						childNode.css({'left' : popMenuWidth});
					}
					var nodeHeight = Math.min(v.children.length,popMenuMaxLine) * popMenuHeight + popMenuYspace;
					// 判断是否需要滚动条
					if(v.children.length > popMenuMaxLine){
						childNode.height(nodeHeight);
						childNode.css({'overflow-y':'auto'});
					}
					// 判断是否向上弹出
			    	if($(this).offset().top + 20 - $(document).scrollTop() + nodeHeight > $(window).height()){
			    		 childNode.css({'top' : - Math.min(v.children.length,popMenuMaxLine) * popMenuHeight});
			        }else{
			        	childNode.css({'top' : 0});
			        }
					childNode.show();
					$(this).siblings().find(".dropdown-menu").hide();
				});
				menuContent.append(menuItem);
			}else{
				// 如果不含子菜单直接绑定事件
				var menuItem = $('<li><a>'+v.name+'</a></li>');
				menuItem.on('click',function(e){
					v.callback(e);
					$('#context-menu').hide();
				});
				menuContent.append(menuItem);
			}
		});
		return menuContent; 
	}
	
	// 计算数组维度
	var getChildNum = function(arr , deep){
		// 当前层级
		var curDeep = deep;
        for (var i=0;i<arr.length;i++){  
            if(arr[i].children){  
                curDeep = Math.max(getChildNum(arr[i].children,deep+1),curDeep);  
            }  
        }  
        return curDeep;
	}
	
	//定义方法
	var methods = {
			// 初始化
	        init: function (obj) {
	        	$this = this;
	        	var options = jQuery.extend(obj, global);
	        	draw(options);
	        	scrollEvent();
				return this;
	        },
	        // 获取内部变量
	        get : function (name){
	        	var value =  eval( name);
	        	if(value == name){
	        		console.log('未找到变量' + name);
	        		return null;
	        	}else{
	        		return value;
	        	}
	        },
	        // 创建小滑块
	        createDraggable:function (title,data,type) {
	        	return createDraggable(title,data,type)
	        },
	       // 重新布局container
	        resizeItem : function (container){
	        	var type = container.attr('role-type');
	        	return resizeItem(container , type);
	        },
	        /**
	         * 弹出菜单
	         * menuList: 菜单项 [{name:'菜单一', callback : func}, ……]
	         */
	        popMenu : function (event, menuList){
	        	var pop = createPopMenu(menuList, false , event).attr('id','context-menu');
	        	// 添加滚动条美化
	        	$(pop).find(".dropdown-menu").each(function(i){
	        		if(i!=0){
	        			new PerfectScrollbar(this);
	        		}
	        	})
	        	pop.show();
	        },
	        /*refresh : function (obj){
	        	var options = jQuery.extend(obj, global);
	        	return this;
	        }*/
	        search : function (text , type){
	        	var scrollId , scrollLeft;
	        	if(type == 'P'){
	        		// 搜素人名
	        		var item;
	        		item = $('#airport .airport-item .item-container[search-text$='+text+']');
	        		if(item.length == 0){
	        			item = $('#bottomAirport .airport-item .item-container[search-text$='+text+']');
	        		}
	        		if(item.length == 0){
	        			return -1;
	        		}
	        		// 改变样式
	        		$('.search-result').removeClass('search-result');
	        		item.addClass('search-result');
	        		// 计算滚动距离
	        		var index = -1;
	        		if(item.parents('#airport').length>0){
	        			// 确定滚动条
		        		scrollId = 'middleScroll';
		        		index = 1;
	        			var marginLeft = item[0].offsetLeft;
	        			var marginLeftParent = item.parent()[0].offsetLeft;
	        			scrollLeft = (marginLeft)/$('#airport').width();
	        		}else if(item.parents('#bottomAirport').length>0){
	        			// 确定滚动条
		        		scrollId = 'bottomScroll';
		        		index = 1;
	        			var marginLeft = item[0].offsetLeft;
	        			var marginLeftParent = item.parent()[0].offsetLeft;
	        			scrollLeft = (marginLeft)/$('#bottomAirport').width();
	        		}
	        		searchScrollTo(scrollId, scrollLeft);
	        		return index;
	        	}else if (type == 'F'){
	        		// 搜索航班号
	        		var item;
	        		item = $('#yFlight .item-container[search-text*='+text+']');
	        		if(item.length == 0){
	        			item = $('#nFlight .item-container[search-text*='+text+']');
	        		}
	        		if(item.length == 0){
	        			return -1;
	        		}
	        		// 改变样式
	        		$('.search-result').removeClass('search-result');
	        		item.addClass('search-result');
	        		// 确定滚动条、计算滚动距离
	        		var index = -1;
	        		if(item.parents('#yFlight').length>0){
	        			scrollId = 'topScroll';
	        			index = 1;
	        			var marginLeft = item[0].offsetLeft;
	        			scrollLeft = marginLeft/$('#yFlight').width();
	        		}else if(item.parents('#nFlight').length>0){
	        			scrollId = 'middleScroll';
	        			index = 1;
	        			var marginLeft = item[0].offsetLeft;
	        			var marginLeftParent = item.parent()[0].offsetLeft;
	        			scrollLeft = (marginLeft+marginLeftParent)/$('#nFlight').width();
	        		}
	        		searchScrollTo(scrollId, scrollLeft);
	        		return index;
	        	}else if (type == 'A'){
	        		// 搜索机位
	        		var item;
	        		item = $('#yFlight .item-container[actstand-code*='+text+']');
	        		if(item.length == 0){
	        			item = $('#nFlight .item-container[actstand-code*='+text+']');
	        		}
	        		if(item.length == 0){
	        			return -1;
	        		}
	        		// 改变样式
	        		$('.search-result').removeClass('search-result');
	        		item.addClass('search-result');
	        		// 确定滚动条、计算滚动距离
	        		var index = -1;
	        		if(item.parents('#yFlight').length>0){
	        			scrollId = 'topScroll';
	        			index = 1;
	        			var marginLeft = item[0].offsetLeft;
	        			scrollLeft = marginLeft/$('#yFlight').width();
	        		}else if(item.parents('#nFlight').length>0){
	        			scrollId = 'middleScroll';
	        			index = 1;
	        			var marginLeft = item[0].offsetLeft;
	        			var marginLeftParent = item.parent()[0].offsetLeft;
	        			scrollLeft = (marginLeft+marginLeftParent)/$('#nFlight').width();
	        		}
	        		searchScrollTo(scrollId, scrollLeft);
	        		return index;
	        	}
	        	
	        }
	};
	
	$.fn.TaskChart = function(method){
		// 方法调用
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            console.log('方法' + method + '在ProgressChart中不存在');
        }
	} 
})(jQuery);