(function ($) {
	//工具方法声明
    window.SJgantt = {};
    //工具方法-捕获鼠标坐标
    window.SJgantt.captureMouse = function(ele){
    	var mouse = {x:0,y:0};
    	ele.addEventListener('mousemove',function(e){
    		var x,y;
	    	if(e.pageX||e.pageY){
	    		x = e.pageX;
	    		y = e.pageY;
	    	}else{
	    		x = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
	    		y = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
	    	}
	    	x -= this.offsetLeft;
	    	y -= this.offsetTop;
	    	mouse.x = x;
	    	mouse.y = y+$(canvas).parent().scrollTop();
    	},false);
    	return mouse;
    }
	//工具方法-定义颜色组
    var colorPool = {
			gray:{
				back:'#5F7BB4',
				inner:'#3D5685',
				border:'#68738B',
				color:'#D3E2FF'
			},
			green:{
				back:'#117087',
				inner:'#0E5F73',
				border:'#258299',
				color:'#D3E2FF'
			},
			blue:{
				back:'#3057B9',
				inner:'#213E85',
				border:'#5486FF',
				color:'#D3E2FF'
			},
			red:{
				back:'#963545',
				inner:'#622A3C',
				border:'#963545',
				color:'#D3E2FF'
			},
			yellow:{
				back:'#CFC415',
				inner:'#FFEE33',
				border:'#F8FF56',
				color:'#696A6E'
			}
	}
	//默认设置
	var defaultOptions = {
			zoom:3,
			maxZoom:5,
			minZoom:1,
			center:0.35,
			height:500,
			showEmptyRow:false,
			shadeColor:"rgba(0, 0, 0, 0.2)",
            background:"url("+ctxStatic+"/images/SJgantt-bg.jpg) no-repeat scroll 0% 0% / 100% 100%",
            clashColor:colorPool.yellow,
            url:undefined,
            data:[],
            queryParams:function (params) {
                return params;
            },
            responseHandler:function (res) {
                return res;
            },
            xAxis:{
            	color:'#FFFFFF',
            	scaleColor:'#B59E80',
            	alphaSep:0.003,
            	minAlpha:0.3,
            	scaleHeight:6,
            	backgroundColor: 'rgba(4, 19, 50, 0.8)',
            	arrowColor: '#5486FF',
            	fontSize:14,
            	height:50,
            	separation:60,
            	paddingBottom:18
            },
            yAxis:{
            	color:'#D3E2FF',
            	backgroundColor: 'rgba(40, 108, 183, 0.95)',
            	fontSize:13,
            	width:65,
            	blockColor:'#071037',
            	paddingLeft:10
            },
            grid:{
            	show:true,
            	lineHeight:37,
            	lineWidth:0.1,
            	color:'#9A9A9B'
            },
            yData:{
            	data:[],
            	startLevel:"1",
            	url:undefined,
                queryParams:function (params) {
                    return params;
                },
                responseHandler:function (res) {
                    return res;
                }
            },
            detail:{
            	show:true,
                fontSize:14,
                color:'#C4CFE9',
                shadeColor:'rgba(0, 0, 0, 0.7)'
            },
            onclickOverlay:function(o){}
	}
	//定义缩放级别与时间间隔（分钟）
	var zoomTime = {
			1:5,
			2:10,
			3:20,
			4:30,
			5:60,
			6:120,
			7:180,
			8:240,
			9:480,
			10:960,
			11:1920,
			12:3840,
			13:7680,
			14:15360,
			15:30720
	}
	var canvas;//全局canvas对象
	var cxt;//全局canvas上下文
	var settings;//全局设置
	var mouse;
	var yDataResult = [];
	window.pool = [];//覆盖物池
	var ypool = {};//Y轴池
	var center;
	var oriYWidth;//记录Y轴单列宽度
	var tempYAxis;//Y轴游标
	var openOverlay;//记录展开悬浮窗的覆盖物
	var contentHeight = 0;//内容高度
	var basicY = 0;//Y轴基准
	var scrollY = {};//滚动滑块
	var draggingY = false;//正在拖动滚动条标识
	var draggingCY;//鼠标距离滚动滑块顶端距离
	var dragCy;//被拖动覆盖物与鼠标Y坐标偏移量
	var dragO;//记录被拖动覆盖物
	var dragingPeople = false;//正在拖动人标识
	var dragY;//记录被拖动人
	var dragYcY;//被拖动人与鼠标Y坐标偏移量
	//定义方法
	var methods = {
	        init: function (options) {
	        	//创建一些默认值，拓展任何被提供的选项
	            settings = $.extend(true,defaultOptions,options);
	            oriYWidth = settings.yAxis.width;
	            this.css("width","100%");
	            this.css("height",settings.height);
	            this.css("background",settings.background);
	            canvas = this[0];
            	canvas.width = this.width();
            	canvas.height = settings.height;
            	cxt = canvas.getContext("2d");
            	mouse = SJgantt.captureMouse(canvas);
            	center = settings.center * canvas.width;
            	//若没有直接使用data添加Y轴数据则使用url获取
	            if(settings.yData.data.length == 0){
	            	loadYData();
	            }else{
	            	handleYData(settings.yData.data);
	            }
	            if(settings.data.length == 0){
	            	loadData();
	            }else{
	            	handleData(settings.data);
	            }
	            addEvent();
            	drawGantt();
	            return this;
	        },
	        reloadYData: function(){
	        	loadYData();
	        	return this;
	        },
	        load: function(data){
	        	if(data){
	        		handleData(data);
	        	}else{
	        		loadData();
	        	}
	        	return this;
	    	},
	    	refresh:function(){
	    		openOverlay = null;
	    		if(settings.yData.data.length == 0){
	            	loadYData();
	            }else{
	            	handleYData(settings.yData.data);
	            }
	            if(settings.data.length == 0){
	            	loadData();
	            }else{
	            	handleData(settings.data);
	            }
	    	},
	    	resetHeight:function(h){
	    		settings.height = h;
	    		this.css("height",settings.height);
		    	canvas.height = settings.height;
	    		return this;
	    	},
	    	refreshOptions: function(options){
	    		openOverlay = null;
	    		settings = $.extend(true,defaultOptions,options);
	    		loadYData();
	    		loadData();
	        	return this;
	    	}
	    };
	//甘特图构造
	$.fn.SJgantt = function (method) {
        // 方法调用
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            console.log('方法' + method + '在SJgantt中不存在');
        }
    };
    //定义刷新
    window.requestAnimationFrame = window.requestAnimationFrame ||
	    window.webkitRequestAnimationFrame ||
	    window.mozRequestAnimationFrame ||
	    function(callback) {
	        window.setTimeout(callback, 1000 / 60);
	    };
    //加载Y轴数据
	function loadYData(){
		$.ajax({
    		type:'post',
    		url:settings.yData.url,
    		async:false,
    		data:settings.yData.queryParams,
    		dataType:'json',
    		success:function(result){
    			yDataResult = settings.yData.responseHandler(result);
    			handleYData(yDataResult);
    		}
    	})
	}
	//Y轴数据预处理
	function handleYData(result){
		ypool = {};
		isBusy = false;
    	for(var i=0;i<result.length;i++){
    		var f = new field(result[i]);
    		if(!window.showAll){
    			if(f.type == '1'){
    				ypool[f.id] = f;
    			}
    		}else{
    			ypool[f.id] = f;
    		}
        };
        settings.yAxis.width = 2*oriYWidth;
	}
	var field = function(f){
		this.id = f.id;
		this.pid = f.pid;
		this.name = f.name.replace("客梯车","");
		this.level = f.level;
		this.type = f.carType;
		this.x = (f.level-1)*oriYWidth;
		this.y = -50;
		this.list = [];
		this.h = settings.grid.lineHeight;
		this.allow = false;
		this.expand = false;
		this.moved = f.moved;
	}
    //加载数据
	function loadData(){
		$.ajax({
    		type:'post',
    		url:settings.url,
    		async:false,
    		data:settings.queryParams,
			error:function(){
				$("#refresh").text("刷新失败");
			},
    		dataType:'json',
    		success:function(result){
    			if(result == "error"){
    				layer.msg("加载失败",{icon:2,time:1000});
    				result = [];
    			}
    			pool = [];
    			result = settings.responseHandler(result);
    			handleData(result);
    		}
    	})
	}
	//预处理数据
	function handleData(result){
		pool = {};
		for(var j in result){
			var o = new overlay(result[j]);
			if(!pool[o.field]){
				pool[o.field] = [];
			}
			pool[o.field].push(o);
		}
		calcY();
	}
	var overlay = function(d){
		this.field = d.actKtcNo == ""?d.ktcNo:d.actKtcNo;
		this.inFltId = d.inFltId;
		this.outFltId = d.outFltId;
		this.inFltnum = (d.inFltNum || "--");
		this.outFltnum = (d.outFltNum || "--");
		this.actType = (d.actType || "--");
		this.actType2 = d.actType2;
		this.actNum = (d.actNum || "--");
		this.kTaskId = d.kTaskId;
		this.cTaskId = d.cTaskId;
		this.startTm = d.startTm;
		this.endTm = d.endTm;
		this.stand = d.stand;
		this.areaId = d.areaId;
		this.ktcNo = d.ktcNo;
		this.actKtcNo = d.actKtcNo;
		this.ifSameArea = d.ifSameArea;
		this.ifError = d.ifError;
		this.x = time2Coor(d.startTm);
		this.x2 = time2Coor(d.endTm);
		this.y = -50;
		this.w = this.x2 - this.x;
		this.h = 0.8*settings.grid.lineHeight|0;
		this.icon = d.actKtcNo == ""?"预":"实";
		this.color = this.ifError == "0"?colorPool.red:this.icon=="实"?colorPool.green:colorPool.blue;
		this.ocolor = this.ifError == "0"?colorPool.red:this.icon=="实"?colorPool.green:colorPool.blue;
		this.text = (this.inFltnum == this.outFltnum?this.inFltnum:this.inFltnum+"/"+this.outFltnum)+" "+this.stand+" "+this.actNum+" "+this.actType;
		this.page = 1;
		this.detail = [[this.text]];
		this.clash = false;
	}
	function calcY(){
		contentHeight = 0;
		var tempY = basicY + settings.xAxis.height;
		for(var f in ypool){
			if(ypool[f].level == "1"){
				ypool[f].y = tempY;
				ypool["DP"+f].y = tempY;
				ypool["DP"+f].h = settings.grid.lineHeight;
				if(ypool["DP"+f].expand){
					calcExpand("DP"+f);
				}else{
					giveY("DP"+f,ypool["DP"+f].y);
				}
				tempY += ypool["DP"+f].h; 
				for(var s in ypool){
					if(ypool[s].pid == ypool[f].id && ypool[s].id.indexOf("DP")==-1){
						ypool[s].y = tempY;
						ypool[s].h = settings.grid.lineHeight;
						if(ypool[s].expand){
							calcExpand(s);
						}else{
							giveY(s,ypool[s].y);
						}
						tempY += ypool[s].h;
					}
				}
				if(tempY != ypool[f].y){
					ypool[f].h = tempY - ypool[f].y;
				}else{
					tempY += ypool[f].h;
				}
			}
		}
		contentHeight = tempY - basicY;
	}
	function calcExpand(f){
		ypool[f].list = [];
		for(var o in pool[f]){
			if(ypool[f].list.length==0){
				ypool[f].list.push([[pool[f][o].x,pool[f][o].x2]]);
				pool[f][o].y = ypool[f].y;
			}else{
				var rowClash = true;
				loop:
				for(var j=0;j<ypool[f].list.length;j++){
					var colClash = false;
					for(var k=0;k<ypool[f].list[j].length;k++){
						if(pool[f][o].x<ypool[f].list[j][k][1] && ypool[f].list[j][k][0]<pool[f][o].x2){
							colClash = true;
							break;
						}
					}
					if(!colClash){
						ypool[f].list[j].push([pool[f][o].x,pool[f][o].x2]);
						pool[f][o].y = ypool[f].y + j*settings.grid.lineHeight;
						rowClash = false;
						break;
					}
				}
				if(rowClash){
					ypool[f].list.push([[pool[f][o].x,pool[f][o].x2]]);
					ypool[f].h += settings.grid.lineHeight;
					pool[f][o].y = ypool[f].y+(ypool[f].list.length-1)*settings.grid.lineHeight;
				}
			}
		}
	}
	function giveY(f,y){
		for(o in pool[f]){
			pool[f][o].y = y;
		}
	}
	function calcX(){
		for(var f in pool){
			for(var o in pool[f]){
				pool[f][o].x = time2Coor(pool[f][o].startTm);
				pool[f][o].x2 = time2Coor(pool[f][o].endTm);
				pool[f][o].w = pool[f][o].x2 - pool[f][o].x;
			}
		}
	}
	//日期格式处理
    Date.prototype.pattern=function(fmt) {         
		var o = {         
		"M+" : this.getMonth()+1, //月份         
		"d+" : this.getDate(), //日         
		"h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
		"H+" : this.getHours(), //小时         
		"m+" : this.getMinutes(), //分         
		"s+" : this.getSeconds(), //秒         
		"q+" : Math.floor((this.getMonth()+3)/3), //季度         
		"S" : this.getMilliseconds() //毫秒         
		};         
		var week = {         
		"0" : "/u65e5",         
		"1" : "/u4e00",         
		"2" : "/u4e8c",         
		"3" : "/u4e09",         
		"4" : "/u56db",         
		"5" : "/u4e94",         
		"6" : "/u516d"        
		};         
		if(/(y+)/.test(fmt)){         
			fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
		}         
		if(/(E+)/.test(fmt)){         
			fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);         
		}         
		for(var k in o){         
			if(new RegExp("("+ k +")").test(fmt)){         
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
			}         
		}         
		return fmt;         
	}
    //事件处理
    var addEvent = function(){
    	//添加移动事件
    	var moveInterval;
    	var moveDelay;
    	var oriCenter = center;
    	var typeIndex = 0;
    	//鼠标按下事件
    	canvas.addEventListener('mousedown',function(e){
    		$("#carMenu").hide();
    		if(mouse.x<settings.yAxis.width){
    			openOverlay = null;
    			if(mouse.y>settings.xAxis.height){
    				if(e.button==2 || e.button==3){
        				$("#reset").data("id","");
        				for(var y in ypool){
                			if(ypool[y].level=="2" && ypool[y].moved == "1"){
                    			if(mouse.x>ypool[y].x && mouse.x<ypool[y].x+oriYWidth && mouse.y>ypool[y].y && mouse.y<ypool[y].y+ypool[y].h){
                    				$("#carMenu").css("cssText","top:"+(mouse.y+34)+"px;left:"+mouse.x+"px;");
                    				$("#carMenu").show();
                    				$("#reset").data("id",ypool[y].id);
                    			}
                			}
                		}
        			}else{
        				for(var y in ypool){
                			if(ypool[y].level=="2" && ypool[y].id.indexOf("DP")==-1){
                    			if(mouse.x>ypool[y].x && mouse.x<ypool[y].x+oriYWidth && mouse.y>ypool[y].y && mouse.y<ypool[y].y+ypool[y].h){
                    				dragingPeople = true;
                    				dragY = ypool[y];
                    				dragYcY = mouse.y - ypool[y].y;
                    			}
                			}
                		}
        			}
    			}
    		}else{
    			if(mouse.x>settings.yAxis.width && mouse.x-settings.yAxis.width<50 && mouse.y>0 && mouse.y<settings.xAxis.height){
        			moveInterval = setInterval(function(){
        				center += 10;
        				for(var f in pool){
        					for(var o in pool[f]){
            					pool[f][o].x += 10;
            					pool[f][o].x2 += 10;
            				}
        				}
        			},10)
        			return false;
        		}
        		if(mouse.x>canvas.width-50 && mouse.y>0 && mouse.y<settings.xAxis.height){
        			moveInterval = setInterval(function(){
        				center -= 10;
        				for(var f in pool){
        					for(var o in pool[f]){
            					pool[f][o].x -= 10;
            					pool[f][o].x2 -= 10;
            				}
        				}
        			},10)
        			return false;
        		}
        		if(mouse.x>canvas.width-10 && mouse.y>scrollY.y && mouse.y<(scrollY.y+scrollY.h)){
        			draggingY = true;
        			draggingCY = mouse.y - scrollY.y;
        			return false;
        		}
        		for(var f in ypool){
        			if(pool[f] && mouse.y>ypool[f].y && mouse.y<ypool[f].y+ypool[f].h){
        				for(var i=pool[f].length-1;i>=0;i--){
            	    		var o = pool[f][i];
            	    		if(mouse.x>o.x && mouse.x<o.x2 && mouse.y>o.y && mouse.y<o.y+o.h){
            	    			if(o.icon == "实" || o.ifError == "0"){
            	    				return false;
            	    			}
            	    			moveDelay = setTimeout(function(){
            	    				$(canvas).css("cursor","move");
                	    			dragO = o;
                	    			$.ajax({
                	    				type:'post',
                	    				url:ctx+"/scheduling/gantt/manualSelectCars",
                	    				delay:200,
                	    				data:{
                	    					actType : o.actType2,
                	    					stand:o.stand
                	    				},
                	    				dataType:'json',
                	    				success:function(list){
                	    					for(var k in ypool){
                	    	    				ypool[k].allow = false;
                	    	    			}
                	    					$.each(list,function(i,item){
                	    						if(ypool[item]){
                	    							ypool[item].allow = true;
                	    						}
                	    					})
                	    					ypool["DP"+o.areaId].allow = true;
                	    				}
                	    			});
            	    			},200);
            	    			break;
            	    		}
                		}
        			}
        		}
    		}
    	});
    	//鼠标抬起事件
    	canvas.addEventListener('mouseup',function(e){
    		draggingY = false;
    		var over = true;
    		if(dragO){
    			for(var k in ypool){
    				if(ypool[k].allow){
    					var dragOY = mouse.y;
    					if(dragOY>ypool[k].y && dragOY<ypool[k].y+ypool[k].h){
    						over = false;
    						var yItem = ypool[k];
    						if(k!=dragO.field){
    							if(k.indexOf("DP")==-1){
    								var tempO = dragO;
    								layer.confirm('确认分配任务吗？', {icon: 3, title:'提示'}, function(index){
    									var loading = layer.load(2);
										$.ajax({
    										type:'post',
    										url:ctx+"/scheduling/gantt/changeCar",
    										data:{
    											kTaskId:tempO.kTaskId,
    											cTaskId:tempO.cTaskId,
    											car:k
    										},
    										error:function(){
        										layer.close(loading);
        										layer.msg('请求错误，请检查网络！')
        									},
    										success:function(msg){
    											layer.close(loading);
    											if(msg=="success"){
    												layer.msg("任务已分配！",{icon:1,time:1000});
    												$("#SJgantt").SJgantt('refresh');
    											} else {
    												layer.msg("任务分配失败",{icon:2,time:1000});
        											console.log(msg);
    											}
    										}
    									});
									  	layer.close(index);
									});
    							}else{
    								var tempO = dragO;
    								layer.confirm('确认释放任务吗？', {icon: 3, title:'提示'}, function(index){
    									var loading = layer.load(2);
    									var tasks = "'"+tempO.kTaskId+"','"+tempO.cTaskId+"'";
										$.ajax({
    										type:'post',
    										url:ctx+"/scheduling/gantt/releaseTasks",
    										data:{
    											tasks:tasks
    										},
    										error:function(){
        										layer.close(loading);
        										layer.msg('请求错误，请检查网络！')
        									},
    										success:function(msg){
    											layer.close(loading);
    											if(msg=="success"){
    												layer.msg("任务已释放！",{icon:1,time:1000});
    												$("#SJgantt").SJgantt('refresh');
    											} else {
    												layer.msg("任务释放失败",{icon:2,time:1000});
        											console.log(msg);
    											}
    										}
    									});
									  	layer.close(index);
									});
    							}
    						}
    						dragO = null;
    						break;
    					}
    				}
    			}
    			if(over){
    				dragO = null;
    			}
    		}
    		if(dragingPeople){
    			var dragYY = Math.floor(mouse.y-dragYcY+dragY.h/2);
    			for(var k in ypool){
    				if(ypool[k].level == "1"){
    					if(dragYY>ypool[k].y && dragYY<ypool[k].y+ypool[k].h){
    						if(k!=dragY.pid){
    							layer.confirm("更换车辆区域时，是否释放已分配任务？",{btn:["是","否","取消"]},
    							function(index){
    								var loading = layer.load(2);
    								var tasks = "";
    								for(var o in pool[dragY.id]){
    									tasks += ",'"+pool[dragY.id][o].kTaskId+"','"+pool[dragY.id][o].cTaskId+"'";
    								}
    								tasks = tasks.substring(1);
    								$.ajax({
    									type:'post',
    									url : ctx + "/scheduling/gantt/updateResCarArea",
    									data:{
    										area:k,
    										num:dragY.id,
    										tasks:tasks
    									},
    									error:function(){
    										layer.close(loading);
    										layer.msg('请求错误，请检查网络！')
    									},
    									success:function(msg){
    										layer.close(loading);
    										if(msg == "success"){
    											layer.msg("变更成功！",{icon:1,time:1000});
    											$("#SJgantt").SJgantt('refresh');
    										}else{
    											layer.msg("变更失败",{icon:2,time:1000});
    											console.log(msg);
    										}
    									}
    								});
    								layer.close(index);
    							},function(index){
    								var loading = layer.load(2);
    								$.ajax({
    									type:'post',
    									url : ctx + "/scheduling/gantt/updateResCarArea",
    									data:{
    										area:k,
    										num:dragY.id
    									},
    									error:function(){
    										layer.close(loading);
    										layer.msg('请求错误，请检查网络！')
    									},
    									success:function(msg){
    										layer.close(loading);
    										if(msg == "success"){
    											layer.msg("变更成功！",{icon:1,time:1000});
    											$("#SJgantt").SJgantt('refresh');
    										}else{
    											layer.msg("变更失败",{icon:2,time:1000});
    											console.log(msg);
    										}
    									}
    								});
    								layer.close(index);
    							});
    						}
    						break;
    					}
    				}
    			}
    			dragingPeople = false;
    		}
			$(canvas).css("cursor","default");
    		clearInterval(moveInterval);
    		clearTimeout(moveDelay);
    	});
    	//鼠标点击事件
    	canvas.addEventListener('click',function(e){
    		if(mouse.x>settings.yAxis.width){
    			if(settings.detail.show){
    				if(openOverlay){
        				cxt.beginPath();
            			cxt.moveTo(openOverlay.pre[0].x,openOverlay.pre[0].y);
            			cxt.lineTo(openOverlay.pre[1].x,openOverlay.pre[1].y);
            			cxt.lineTo(openOverlay.pre[2].x,openOverlay.pre[2].y);
            			cxt.lineTo(openOverlay.pre[3].x,openOverlay.pre[3].y);
            			if(cxt.isPointInPath(mouse.x, mouse.y)){
            				openOverlay.page -= 1;
            				if(openOverlay.page<1){
            					openOverlay.page = openOverlay.detail.length;
            				}
            				return false;
                        }
            			cxt.beginPath();
            			cxt.moveTo(openOverlay.next[0].x,openOverlay.next[0].y);
            			cxt.lineTo(openOverlay.next[1].x,openOverlay.next[1].y);
            			cxt.lineTo(openOverlay.next[2].x,openOverlay.next[2].y);
            			cxt.lineTo(openOverlay.next[3].x,openOverlay.next[3].y);
            			if(cxt.isPointInPath(mouse.x, mouse.y)){
            				openOverlay.page += 1;
            				if(openOverlay.page>openOverlay.detail.length){
            					openOverlay.page = 1;
            				}
            				return false;
                        }
        			}
    			}
    			var ifOverly = false;
    			window.clickRowId = "";
    			window.clickRow = null;
    			for(var f in ypool){
    				if(pool[f] && mouse.y>ypool[f].y && mouse.y<ypool[f].y+ypool[f].h){
    					for(var i=pool[f].length-1;i>=0;i--){
    	    	    		var o = pool[f][i];
    	    	    		if(mouse.x>o.x && mouse.x<o.x2 && mouse.y>o.y && mouse.y<o.y+o.h){
    	    	    			settings.onclickOverlay(o);
    	    	    			if(openOverlay == o){
    	    	    				openOverlay = null;
    	    	    			}else{
    	    	    				openOverlay = o;
    	    		    			openOverlay.page = 1;
    	    		    			openOverlay.mouseX = mouse.x;
    	    		    			ifOverly = true;
    	    		    			window.clickRowId = o.fltid;
    	    		    			window.clickRow = o;
    	    	    			}
    	    	    			return false;
    	    	    		}
    	    			}
    				}
    			}
				if(!ifOverly){
					openOverlay = null;
				};
    		}
    	});
    	//鼠标双击事件
    	canvas.addEventListener('dblclick',function(e){
    		if(mouse.x>settings.yAxis.width+50 && mouse.x<canvas.width-50 && mouse.y>0 && mouse.y<settings.xAxis.height){
    			center = oriCenter;
    		}else if(mouse.x<settings.yAxis.width){
    			for(var f in ypool){
    				if(ypool[f].level == "2"){
            			if(mouse.x>ypool[f].x && mouse.x<ypool[f].x+oriYWidth && mouse.y>ypool[f].y && mouse.y<(ypool[f].y+ypool[f].h)){
            				if(ypool[f].expand){
            					ypool[f].expand = false;
            				}else{
            					ypool[f].expand = true;
            				}
            				calcY();
            				if(contentHeight<canvas.height-settings.xAxis.height){
        						basicY = 0;
        						calcY();
        					}
            			}
        			}
        		}
    		}
    	});
    	//滚动滚轮调整缩放级别
    	var wheelAble = true;
    	var keydown = false;
    	canvas.addEventListener('DOMMouseScroll',function(e){
    		if(!keydown){
    			if(wheelAble && contentHeight>canvas.height-settings.xAxis.height){
        			if(e.detail<0){
        				if(basicY < 0){
        					basicY += 100;
        				}else{
        					basicY = 0;
        				}
            			calcY();
            		}else{
            			if(basicY>canvas.height-contentHeight-settings.xAxis.height){
            				basicY -= 100;
            			}else{
            				basicY = canvas.height-contentHeight-settings.xAxis.height;
            			}
            			calcY();
            		}
            		setTimeout(function(){
            			wheelAble = true;
            		},100)
        		}
    		}
    	});
    	canvas.addEventListener('mousewheel',function(e){
    		if(!keydown){
    			if(wheelAble && contentHeight>canvas.height-settings.xAxis.height){
        			if(e.wheelDelta>0){
        				if(basicY < 0){
        					basicY += 100;
        				}else{
        					basicY = 0;
        				}
            			calcY();
            		}else{
            			if(basicY>canvas.height-contentHeight-settings.xAxis.height){
            				basicY -= 100;
            			}else{
            				basicY = canvas.height-contentHeight-settings.xAxis.height;
            			}
            			calcY();
            		}
            		setTimeout(function(){
            			wheelAble = true;
            		},100)
        		}
    		}
    	});
    	window.parent.document.onkeydown = function(e){
    		keydown = true;
    		if(e.keyCode == 192){
    			canvas.addEventListener('DOMMouseScroll',changeZoomFF);
    			canvas.addEventListener('mousewheel',changeZoom);
    		}
    	}
    	window.parent.document.onkeyup = function(e){
    		keydown = false;
    		if(e.keyCode == 192){
    			canvas.removeEventListener('DOMMouseScroll',changeZoomFF);
    			canvas.removeEventListener('mousewheel',changeZoom);
    		}
    	}
    	document.onkeydown = function(e){
    		keydown = true;
    		if(e.keyCode == 192){
    			canvas.addEventListener('DOMMouseScroll',changeZoomFF);
    			canvas.addEventListener('mousewheel',changeZoom);
    		}
    	}
    	document.onkeyup = function(e){
    		keydown = false;
    		if(e.keyCode == 192){
    			canvas.removeEventListener('DOMMouseScroll',changeZoomFF);
    			canvas.removeEventListener('mousewheel',changeZoom);
    		}
    	}
    	//firefox浏览器兼容性设置
    	var changeZoomFF = function(e){
    		if(wheelAble){
    			if(e.detail<0){
        			if(settings.zoom>settings.minZoom){
        				var oldX = mouse.x;
        				var time = coor2Time(oldX);
        				settings.zoom--;
        				wheelAble = false;
        				var newX = time2Coor(time);
        				center += oldX - newX;
        				calcX();
        			}
        		}else{
        			if(settings.zoom<settings.maxZoom){
        				var oldX = mouse.x;
        				var time = coor2Time(oldX);
        				settings.zoom++;
        				wheelAble = false;
        				var newX = time2Coor(time);
    					center += oldX - newX;
    					calcX();
        			}
        		}
        		setTimeout(function(){
        			wheelAble = true;
        		},100)
    		}
    		e.preventDefault();
    	};
    	//除firefox外浏览器设置
    	var changeZoom = function(e){
    		if(wheelAble){
    			if(e.wheelDelta>0){
        			if(settings.zoom>settings.minZoom){
        				var oldX = mouse.x;
        				var time = coor2Time(oldX);
        				settings.zoom--;
        				wheelAble = false;
        				setTimeout(function(){
        					var newX = time2Coor(time);
        					center += oldX - newX;
        				},50)
        			}
        		}else{
        			if(settings.zoom<settings.maxZoom){
        				var oldX = mouse.x;
        				var time = coor2Time(oldX);
        				settings.zoom++;
        				wheelAble = false;
        				setTimeout(function(){
        					var newX = time2Coor(time);
        					center += oldX - newX;
        				},50)
        			}
        		}
    			setTimeout(function(){
        			wheelAble = true;
        		},100)
    		}
    		e.preventDefault();
    	};
    }
	//绘制甘特图主方法
    var drawGantt = function(){
    	cxt.clearRect(0,0,canvas.width,canvas.height);
    	cxt.save();
    	//定义甘特图宽、高、背景、蒙板
    	cxt.fillStyle = settings.shadeColor;
    	cxt.fillRect(0,0,canvas.width,canvas.height);
    	//网格
    	drawGrid();
    	//覆盖物
    	//drawOverlays();
    	for(var f in pool){
    		if(ypool[f]){
    			if(ypool[f].y<canvas.height && (ypool[f].y+ypool[f].h)>0){
        			for(var o in pool[f]){
        				pool[f][o].x = time2Coor(pool[f][o].startTm)|0;
        				pool[f][o].x2 = time2Coor(pool[f][o].endTm)|0;
        				pool[f][o].w = pool[f][o].x2 - pool[f][o].x;
            			if(pool[f][o].x<canvas.width && pool[f][o].x2>0 && pool[f][o].y<canvas.height && (pool[f][o].y+pool[f][o].h)>0 && openOverlay != pool[f][o]){
            				if(pool[f][o].x <= pool[f][o].x2){
            					var color = ypool[f].expand?pool[f][o].ocolor:pool[f][o].color;
            					if(pool[f][o].field.indexOf("DP")==-1){
            						for(var k in pool[f]){
            							if(pool[f][o]!=pool[f][k] && pool[f][o].x<pool[f][k].x2-1 && pool[f][k].x<pool[f][o].x2-1){
            								pool[f][o].clash = true;
            								pool[f][o].color = colorPool.yellow;
            								pool[f][k].clash = true;
            								pool[f][k].color = colorPool.yellow;
            							}
            			    		}
            					}
            					if(dragO != pool[f][o]){
            						if(pool[f][o].ifSameArea == "0"){
            							drawOverlay(pool[f][o].x,pool[f][o].y,pool[f][o].w,pool[f][o].h,color,pool[f][o].text,pool[f][o].icon,0.5);
            						}else{
            							drawOverlay(pool[f][o].x,pool[f][o].y,pool[f][o].w,pool[f][o].h,color,pool[f][o].text,pool[f][o].icon,1);
            						}
            					}else{
            						drawOverlay(pool[f][o].x,pool[f][o].y,pool[f][o].w,pool[f][o].h,color,pool[f][o].text,pool[f][o].icon,0.3);
            					}
            				}
            			}
            		}
        		}
    		}
    	}
    	if(openOverlay){
    		var color = ypool[openOverlay.field].expand?openOverlay.ocolor:openOverlay.color;
			cxt.shadowColor = color.back;
			cxt.shadowBlur = 5;
			drawOverlay(openOverlay.x,openOverlay.y,openOverlay.w,openOverlay.h*1.1,color,openOverlay.text,openOverlay.icon,1);
			cxt.shadowBlur = 0;
		}
    	if(dragO){
			drawOverlay(dragO.x,mouse.y-settings.grid.lineHeight/2,dragO.w,dragO.h,dragO.color,dragO.text,dragO.icon,1);
			if(mouse.y>canvas.height-50 && basicY>canvas.height-contentHeight){
				basicY -= 20;
				calcY();
			}
			if(mouse.y<settings.xAxis.height+50 && basicY<0){
				basicY += 20;
				calcY();
			}
		}
    	//X轴
    	drawXAxis();
    	//当前时间线
    	/*var baseX = Math.floor(center);
    	drawDashedLine(baseX,settings.xAxis.height,baseX,canvas.height,[2,2],0.5,settings.xAxis.color);*/
    	//y轴
    	tempYAxis = settings.xAxis.height;
    	//Y轴背景
    	cxt.fillStyle = settings.yAxis.backgroundColor;
    	cxt.fillRect(0,settings.xAxis.height,settings.yAxis.width,canvas.height-settings.xAxis.height);
    	drawYAxis();
    	//Y轴左上样式块
    	cxt.fillStyle = settings.yAxis.backgroundColor;
    	cxt.fillRect(0,0,settings.yAxis.width,settings.xAxis.height);
    	cxt.fillStyle = settings.yAxis.blockColor;
    	cxt.beginPath();
    	cxt.moveTo(5,5);
    	cxt.lineTo(5,25);
    	cxt.lineTo(25,5);
    	cxt.closePath();
    	cxt.fill();
    	if(contentHeight > canvas.height - settings.xAxis.height){
    		drawScrollY();
    	}
    	//跟随鼠标虚线
    	if(mouse.x>settings.yAxis.width){
    		if(contentHeight>canvas.height-settings.xAxis.height){
    			if(mouse.x<canvas.width-10){
    				drawDashedLine(mouse.x,settings.xAxis.height,mouse.x,canvas.height,[2,2],0.5,'#FFFFFF');
            		cxt.fillStyle = "rgba(0,0,0,0.5)";
            		cxt.fillRect(mouse.x+15,mouse.y+15,94,20);
            		cxt.fillStyle = "#ccc";
            		cxt.font = "10px sans-serif";
                	cxt.fillText(coor2Time(mouse.x),mouse.x+62,mouse.y+30);
    			}
    		}else{
    			drawDashedLine(mouse.x,settings.xAxis.height,mouse.x,canvas.height,[2,2],0.5,'#FFFFFF');
        		cxt.fillStyle = "rgba(0,0,0,0.5)";
        		cxt.fillRect(mouse.x+15,mouse.y+15,94,20);
        		cxt.fillStyle = "#ccc";
        		cxt.font = "10px sans-serif";
            	cxt.fillText(coor2Time(mouse.x),mouse.x+62,mouse.y+30);
    		}
    	}
    	if(dragingPeople){
			cxt.fillStyle = '#071037';
			cxt.globalCompositeOperation = "source-over";
	    	cxt.fillRect(dragY.x,mouse.y-dragYcY,oriYWidth,dragY.h);
	    	var item = dragY;
	    	var start = item.level;
			var fontWidth = cxt.measureText(item.name).width;
			cxt.fillStyle = '#FFFFFF';
			cxt.font = settings.yAxis.fontSize+"px sans-serif";
			fontWidth = cxt.measureText(item.name).width;
			cxt.fillText(item.name,(start-0.5)*oriYWidth,item.h/2+mouse.y-dragYcY+(settings.yAxis.fontSize/2));
			if(mouse.y>canvas.height-50 && basicY>canvas.height-contentHeight){
				basicY -= 20;
				calcY();
			}
			if(mouse.y<settings.xAxis.height+50 && basicY<0){
				basicY += 20;
				calcY();
			}
		}
    	//悬浮窗
    	if(settings.detail.show){
    		drawDetail();
    	}
    	cxt.restore();
    	//启用动画
    	requestAnimationFrame(drawGantt);
    }
    //画X轴
    var drawXAxis = function(){
    	//x轴
    	cxt.fillStyle = settings.xAxis.backgroundColor;
    	cxt.fillRect(settings.yAxis.width,0,canvas.width,settings.xAxis.height);
    	//x轴刻度
    	var myDate = new Date(settings.queryParams.start);
    	var hour = myDate.getHours();
    	var min = myDate.getMinutes();
    	var sec = myDate.getSeconds();
    	var baseX = Math.floor(center);
    	var offset = Math.floor((60-sec)/60*settings.xAxis.separation/5);
    	var alpha = 1;
    	//当前时间右侧刻度
    	var tempX = baseX+offset;
    	var tempMin = min;
    	while(tempX<canvas.width-50){
    		if(tempX<settings.yAxis.width+50){
    			tempX += settings.xAxis.separation/5;
            	tempMin += 1;
            	continue;
    		}
    		cxt.beginPath();
    		cxt.strokeStyle = settings.xAxis.scaleColor;
    		var tempY;
    		if(tempMin%5==4){
    			cxt.lineWidth = 3;
    			tempY = settings.xAxis.scaleHeight;
    		}else{
    			cxt.lineWidth = 0.5;
    			tempY = settings.xAxis.scaleHeight*2/3;
    		}
    		if(alpha>settings.xAxis.minAlpha){
    			alpha = alpha - (settings.zoom - 1)*settings.xAxis.alphaSep;
    		}
    		cxt.globalAlpha = alpha; 
    		cxt.moveTo(tempX,settings.xAxis.height);
        	cxt.lineTo(tempX,settings.xAxis.height-tempY);
        	cxt.stroke();
        	cxt.closePath();
        	if(tempMin%5==4 && (tempX-baseX)>=settings.xAxis.separation*2/3){
        		var time = coor2Time(tempX).split(" ")[1];
        		var tempX2 = Math.ceil(baseX+(4-min%5)*settings.xAxis.separation/5+offset+2*settings.xAxis.separation)+settings.xAxis.separation;
        		if(settings.zoom>=5 && tempX>=tempX2){
        			time = time.split(":")[0];
        		}
    			cxt.fillStyle = settings.xAxis.color;
    	    	cxt.textAlign = "center";
    	    	cxt.font = (settings.xAxis.fontSize-1)+"px sans-serif";
    	    	cxt.textBaseline = 'bottom';
    	    	cxt.fillText(time,tempX,settings.xAxis.height-settings.xAxis.paddingBottom);
        	}
        	tempX += settings.xAxis.separation/5;
        	tempMin += 1;
    	}
    	//当前时间左侧刻度
    	tempX = baseX-(settings.xAxis.separation/5-offset);
    	tempMin = min;
    	alpha = 1;
    	while(tempX>settings.yAxis.width+50){
    		if(tempX>canvas.width-50){
    			tempX -= settings.xAxis.separation/5;
            	tempMin -= 1;
            	continue;
    		}
    		cxt.beginPath();
    		cxt.strokeStyle = settings.xAxis.scaleColor;
    		var tempY;
    		if((60-tempMin)%5==0){
    			cxt.lineWidth = 3;
    			tempY = settings.xAxis.scaleHeight;
    		}else{
    			cxt.lineWidth = 0.5;
    			tempY = settings.xAxis.scaleHeight*2/3;
    		}
    		if(alpha>settings.xAxis.minAlpha){
    			alpha = alpha - (settings.zoom - 1)*settings.xAxis.alphaSep;
    		}
    		cxt.globalAlpha = alpha; 
    		cxt.moveTo(tempX,settings.xAxis.height);
        	cxt.lineTo(tempX,settings.xAxis.height-tempY);
        	cxt.stroke();
        	cxt.closePath();
        	if((60-tempMin)%5==0 && (baseX-tempX)>=settings.xAxis.separation*2/3){
        		var time = coor2Time(tempX).split(" ")[1];
        		var tempX2 = Math.floor(baseX-(settings.xAxis.separation-((4-min%5)*settings.xAxis.separation/5+offset)+2*settings.xAxis.separation)) - settings.xAxis.separation;
        		if(settings.zoom>=5 && tempX<=tempX2){
        			time = time.split(":")[0];
        		}
    			cxt.fillStyle = settings.xAxis.color;
    	    	cxt.textAlign = "center";
    	    	cxt.font = (settings.xAxis.fontSize-1)+"px sans-serif";
    	    	cxt.textBaseline = 'bottom';
    	    	cxt.fillText(time,tempX,settings.xAxis.height-settings.xAxis.paddingBottom);
        	}
        	tempX -= settings.xAxis.separation/5;
        	tempMin -= 1;
    	}
    	cxt.globalAlpha = 1;  
    	if(baseX > settings.yAxis.width+50 && baseX < canvas.width-50){
    		//当前时间飞机图标
        	/*cxt.beginPath();
        	cxt.fillStyle = settings.xAxis.color;
        	cxt.moveTo(baseX-6,settings.xAxis.height-22);
        	cxt.lineTo(baseX+6,settings.xAxis.height-22);
        	cxt.lineTo(baseX,settings.xAxis.height-14);
        	cxt.fill();
        	cxt.closePath();*/
        	/*cxt.beginPath();
        	cxt.fillStyle = settings.xAxis.color;
        	var fltBaseY = settings.xAxis.height-8;
        	cxt.moveTo(baseX-6,fltBaseY-10);
        	cxt.lineTo(baseX+12,fltBaseY-16);
        	cxt.lineTo(baseX+13,fltBaseY-17);
        	cxt.lineTo(baseX+12,fltBaseY-19);
        	cxt.lineTo(baseX+8,fltBaseY-20);
        	cxt.lineTo(baseX+7,fltBaseY-20);
        	cxt.lineTo(baseX+6,fltBaseY-20);
        	cxt.lineTo(baseX,fltBaseY-18);
        	cxt.lineTo(baseX-7,fltBaseY-22);
        	cxt.lineTo(baseX-9,fltBaseY-21);
        	cxt.lineTo(baseX-5,fltBaseY-15);
        	cxt.lineTo(baseX-8,fltBaseY-14);
        	cxt.lineTo(baseX-11,fltBaseY-17);
        	cxt.lineTo(baseX-13,fltBaseY-16);
        	cxt.lineTo(baseX-9,fltBaseY-11);
        	cxt.lineTo(baseX-7,fltBaseY-10);
        	cxt.fill();
        	cxt.closePath();*/
        	//时间数值
        	cxt.fillStyle = settings.xAxis.color;
        	cxt.textAlign = "center";
        	cxt.font = settings.xAxis.fontSize+"px sans-serif";
        	cxt.fillText(hour+":"+zeroize(min,2),baseX,settings.xAxis.height-settings.xAxis.paddingBottom);
    	}
    	//左右移动箭头
    	cxt.beginPath();
    	cxt.strokeStyle = settings.xAxis.arrowColor;
    	cxt.lineWidth = 3;
    	cxt.moveTo(settings.yAxis.width+25,settings.xAxis.height/2-5);
    	cxt.lineTo(settings.yAxis.width+18,settings.xAxis.height/2);
    	cxt.lineTo(settings.yAxis.width+25,settings.xAxis.height/2+5);
    	/*cxt.moveTo(settings.yAxis.width+32,settings.xAxis.height/2-3);
    	cxt.lineTo(settings.yAxis.width+25,settings.xAxis.height/2);
    	cxt.lineTo(settings.yAxis.width+32,settings.xAxis.height/2+3);*/
    	cxt.moveTo(canvas.width-25,settings.xAxis.height/2-5);
    	cxt.lineTo(canvas.width-18,settings.xAxis.height/2);
    	cxt.lineTo(canvas.width-25,settings.xAxis.height/2+5);
    	/*cxt.moveTo(canvas.width-32,settings.xAxis.height/2-3);
    	cxt.lineTo(canvas.width-25,settings.xAxis.height/2);
    	cxt.lineTo(canvas.width-32,settings.xAxis.height/2+3);*/
    	cxt.stroke();
    	cxt.closePath();
    }
    //绘制网格
    var drawGrid = function(){
    	if(settings.grid.show){
    		cxt.strokeStyle = settings.grid.color;
    		cxt.setLineDash([]);
    		cxt.lineWidth = settings.grid.lineWidth;
    		for(var i=0;i<(contentHeight-settings.xAxis.height)/settings.grid.lineHeight+2;i++){
        		cxt.moveTo(0,basicY+settings.xAxis.height+(i*settings.grid.lineHeight));
        		cxt.lineTo(canvas.width,basicY+settings.xAxis.height+(i*settings.grid.lineHeight));
        	}
    		cxt.stroke();
    	}
    }
    //画覆盖物
	var drawOverlay = function(x,y,w,h,color,text,icon,alpha){
		cxt.globalAlpha = alpha;
		y = y+Math.ceil((settings.grid.lineHeight-h)/2);
		cxt.fillStyle = color.back;
		cxt.fillRect(x,y,w,h);
		cxt.fillStyle = color.inner;
		var ix = (0.1*w>7?7:0.1*w)|0;
		var w2 = (0.2*w>40?w-40:0.8*w)|0;
		cxt.fillRect(x,y+0.15*h|0,w2,0.7*h|0);
		
		if(w2>=0.7*h){
			cxt.fillStyle = color.back;
			cxt.fillRect(x,y+0.15*h,0.7*h,0.7*h);
			cxt.fillStyle = color.color;
			cxt.font = "11px sans-serif";
			cxt.textAlign = 'center';
			cxt.textBaseline = 'middle';
			cxt.fillText((icon || ""),x+0.35*h,y+h/2);
		}
		
		cxt.font = "12px sans-serif";
		var length = cxt.measureText(text).width;
		if(w2>length+0.7*h+4){
			cxt.textAlign = 'left';
			cxt.textBaseline = 'middle';
			cxt.fillText(text,x+0.7*h+2,y+h/2);
		}
		cxt.beginPath();
		cxt.strokeStyle = color.border;
		cxt.lineWidth = 2;
		cxt.moveTo(x,y-2);
		cxt.lineTo(x,y+h);
		cxt.moveTo(x+ix+w2,y+0.2*ix|0);
		cxt.lineTo(x+w,y+0.2*ix|0);
		cxt.moveTo(x+ix+w2,y+0.4*h|0);
		cxt.lineTo(x+w,y+0.4*h|0);
		cxt.stroke();
		cxt.beginPath();
		cxt.lineWidth = 4;
		cxt.moveTo(x,y);
		cxt.lineTo(x+(w>100?60:w/2),y)
		cxt.stroke();
	}
	//画Y轴
	var drawYAxis = function(type,code){
    	//Y轴文字
		cxt.save();
		cxt.fillStyle = settings.yAxis.color;
    	cxt.font = settings.yAxis.fontSize+"px sans-serif";
    	cxt.textAlign = "center";
    	cxt.textBaseline = 'middle';
    	for(var f in ypool){
    		var item = ypool[f];
    		var start = item.level;
    		if(item.moved == "1"){
    			cxt.fillStyle = colorPool.green.border;
				cxt.fillRect((start-1)*oriYWidth,item.y,oriYWidth,item.h);
				cxt.fillStyle = settings.yAxis.color;
    		}
    		if(dragO && mouse.y>item.y && mouse.y < item.y+item.h && item.level == 2){
				cxt.fillStyle = "#071037";
				cxt.fillRect(0,item.y,settings.yAxis.width,item.h);
				cxt.fillStyle = settings.yAxis.color;
			}
			var fontWidth = cxt.measureText(item.name).width;
			if(fontWidth > oriYWidth){
				cxt.font = Math.floor(settings.grid.lineHeight/3)+"px sans-serif";
				fontWidth = cxt.measureText(item.name.substring(0,Math.floor(item.name.length/2))).width;
				if(fontWidth > oriYWidth){
					cxt.font = Math.floor(settings.grid.lineHeight/4)+"px sans-serif";
    				cxt.fillText(item.name.substring(0,Math.floor(item.name.length/3)),(start-0.5)*oriYWidth,item.y+settings.grid.lineHeight/2-Math.floor(settings.grid.lineHeight/4)-4);
    				cxt.fillText(item.name.substring(Math.floor(item.name.length/3),2*Math.floor(item.name.length/3)),(start-0.5)*oriYWidth,item.y+settings.grid.lineHeight/2);
    				cxt.fillText(item.name.substring(2*Math.floor(item.name.length/3)),(start-0.5)*oriYWidth,item.y+settings.grid.lineHeight/2+Math.floor(settings.grid.lineHeight/4)+4);
				}else{
					cxt.fillText(item.name.substring(0,Math.floor(item.name.length/2)),(start-0.5)*oriYWidth,item.y+settings.grid.lineHeight/2-Math.floor(settings.grid.lineHeight/6)-2);
					cxt.fillText(item.name.substring(Math.floor(item.name.length/2)),(start-0.5)*oriYWidth,item.y+settings.grid.lineHeight/2+Math.floor(settings.grid.lineHeight/6)+2);
				}
			}else{
				cxt.font = settings.yAxis.fontSize+"px sans-serif";
				fontWidth = cxt.measureText(item.name).width;
				cxt.fillText(item.name,(start-0.5)*oriYWidth,item.y+settings.grid.lineHeight/2);
			}
			if(!isBusy || (isBusy && item.level == "1")){
				cxt.beginPath();
    			cxt.fillStyle = settings.yAxis.color;
    			cxt.setLineDash([]);
    	    	cxt.lineWidth = 0.1;
    	    	cxt.moveTo(item.x,item.y);
    	    	cxt.lineTo(settings.yAxis.width,item.y);
    	    	cxt.moveTo(item.x,item.y+item.h);
    	    	cxt.lineTo(settings.yAxis.width,item.y+item.h);
    	    	cxt.stroke();
			}
			if(dragO && item.level=="2"){
				if(item.allow){
					cxt.fillStyle = "rgba(126, 213, 61, 0.2)";
					cxt.globalCompositeOperation = "destination-over";
					cxt.fillRect(0,item.y,canvas.width,item.h);
					cxt.globalCompositeOperation = "source-over";
					cxt.fillStyle = settings.yAxis.color;
				}else{
					cxt.fillStyle = "rgba(248, 35, 9, 0.2)";
					cxt.globalCompositeOperation = "destination-over";
					cxt.fillRect(0,item.y,canvas.width,item.h);
					cxt.globalCompositeOperation = "source-over";
					cxt.fillStyle = settings.yAxis.color;
				}
			}
    	}
    	cxt.restore();
    }
    //画悬浮窗
    function drawDetail(){
    	if(openOverlay){
    		//定义起点、宽高、内容
	    	var o = openOverlay;
	    	var color = ypool[openOverlay.field].expand?openOverlay.ocolor:openOverlay.color;
	    	var ox = o.mouseX;
			var oy = o.y;
			var content = o.detail[o.page-1];
			var dh = settings.detail.fontSize*content.length+5*(content.length-1)+40;
			//定义样式
			cxt.strokeStyle = color.back;
			cxt.shadowColor = color.back;
			cxt.shadowBlur = 5;
			cxt.lineWidth = 1;
			cxt.setLineDash([1,1]);
			cxt.textAlign = "left";
			cxt.textBaseline = 'bottom';
			cxt.font = settings.detail.fontSize+"px sans-serif";
			var dw = 0;
			for(var i=0;i<content.length;i++){
				var width = cxt.measureText(content[i]).width;
				if(width>dw){
					dw = width;
				}
			}
			dw = dw + 80;
			if(ox+dw+82<canvas.width){
				//连接线
				cxt.beginPath();
				cxt.moveTo(ox,oy);
				if(oy-15-dh/2 < settings.xAxis.height){
					oy += settings.xAxis.height-(oy-15-dh/2)+10; 
				}
				if(oy-15+dh/2 > canvas.height){
					oy -= oy-25+dh/2-canvas.height;
				}
				cxt.lineTo(ox+40,oy-15);
				cxt.lineTo(ox+70,oy-15);
				cxt.lineTo(ox+70,oy-30);
				cxt.lineTo(ox+74,oy-36);
				cxt.lineTo(ox+74,oy-15-dh/2);
				cxt.moveTo(ox+70,oy-15);
				cxt.lineTo(ox+70,oy);
				cxt.lineTo(ox+74,oy+6);
				cxt.lineTo(ox+74,oy-15+dh/2);
				cxt.moveTo(ox+74,oy-15+dh/2);
				cxt.closePath();
				cxt.stroke();
				//折点圆圈
				cxt.setLineDash([]);
				cxt.beginPath();
				cxt.arc(ox+40,oy-15,2,0,Math.PI*2,true);
				cxt.stroke();
				cxt.beginPath();
				cxt.lineWidth = 2;
				cxt.arc(ox+65,oy-15,5,0,Math.PI*2,true);
				cxt.stroke();
				cxt.beginPath();
				cxt.lineWidth = 2;
				cxt.arc(ox+65,oy-15,2,0,Math.PI*2,true);
				cxt.fill();
				//悬浮窗左侧梯形块
				cxt.beginPath();
				cxt.fillStyle = color.back;
				cxt.moveTo(ox+74,oy-30)
				cxt.lineTo(ox+78,oy-36);
				cxt.lineTo(ox+78,oy+6);
				cxt.lineTo(ox+74,oy);
				cxt.fill();
				//定义窗体样式 及起点
				cxt.lineWidth = 0.5;
				var dx = ox+82;
				var dy = oy-15-dh/2;
				//窗体边框
				cxt.beginPath();
				cxt.moveTo(dx+4,dy);
				cxt.bezierCurveTo(dx, dy, dx, dy+5, dx, dy+10);
				cxt.lineTo(dx,dy+dh);
				cxt.lineTo(dx+dw-20,dy+dh);
				cxt.lineTo(dx+dw,dy+dh*2/3);
				cxt.lineTo(dx+dw,dy);
				cxt.lineTo(dx+4,dy);
				cxt.stroke();
				//窗体填充背景
				cxt.fillStyle = settings.detail.shadeColor;
				cxt.fill();
				//窗体左下护角
				cxt.fillStyle = color.back;
				cxt.beginPath();
				cxt.moveTo(dx,dy+dh);
				cxt.lineTo(dx,dy+dh-25);
				cxt.quadraticCurveTo(dx+4, dy+dh-25, dx+4, dy+dh-21);
				cxt.lineTo(dx+4,dy+dh-8);
				cxt.lineTo(dx+8,dy+dh-4);
				cxt.lineTo(dx+21,dy+dh-4);
				cxt.quadraticCurveTo(dx+25, dy+dh-4, dx+25, dy+dh);
				cxt.lineTo(dx,dy+dh);
				cxt.fill();
				//窗体右上护角
				cxt.beginPath();
				cxt.moveTo(dx+dw,dy);
				cxt.lineTo(dx+dw,dy+25);
				cxt.quadraticCurveTo(dx+dw-4, dy+25, dx+dw-4, dy+21);
				cxt.lineTo(dx+dw-4,dy+8);
				cxt.lineTo(dx+dw-8,dy+4);
				cxt.lineTo(dx+dw-21,dy+4);
				cxt.quadraticCurveTo(dx+dw-25, dy+4, dx+dw-25, dy);
				cxt.lineTo(dx+dw,dy);
				cxt.fill();
				//窗体内左右虚线
				cxt.beginPath();
				cxt.setLineDash([1,1]);
				cxt.moveTo(dx+20,dy+20);
				cxt.lineTo(dx+20,dy+dh-20);
				cxt.moveTo(dx+dw-20,dy+20);
				cxt.lineTo(dx+dw-20,dy+dh-20);
				cxt.stroke();
				//窗体内左箭头
				cxt.beginPath();
				cxt.moveTo(dx+10,dy+dh/2);
				cxt.lineTo(dx+20,dy+dh/2-5);
				cxt.lineTo(dx+20,dy+dh/2+5);
				cxt.fill();
				openOverlay.pre = [{x:dx,y:dy},{x:dx,y:dy+dh},{x:dx+20,y:dy+dh},{x:dx+20,y:dy}];
				//窗台内右箭头
				cxt.beginPath();
				cxt.moveTo(dx+dw-10,dy+dh/2);
				cxt.lineTo(dx+dw-20,dy+dh/2-5);
				cxt.lineTo(dx+dw-20,dy+dh/2+5);
				cxt.fill();
				openOverlay.next = [{x:dx+dw-20,y:dy},{x:dx+dw-20,y:dy+dh},{x:dx+dw,y:dy+dh-40},{x:dx+dw,y:dy}];
				//窗体内文字
				cxt.fillStyle = settings.detail.color;
				cxt.shadowBlur = 0;
				for(var i=0;i<content.length;i++){
					cxt.fillText(content[i],dx+40,dy+15+(settings.detail.fontSize+5)*(i+1));
				}
			}else if(ox-dw-82>0){
				//连接线
				cxt.beginPath();
				cxt.moveTo(ox,oy);
				if(oy-15-dh/2 < settings.xAxis.height){
					oy += settings.xAxis.height-(oy-15-dh/2)+10; 
				}
				if(oy-15+dh/2 > canvas.height){
					oy -= oy-25+dh/2-canvas.height;
				}
				cxt.lineTo(ox-40,oy-15);
				cxt.lineTo(ox-70,oy-15);
				cxt.lineTo(ox-70,oy-30);
				cxt.lineTo(ox-74,oy-36);
				cxt.lineTo(ox-74,oy-15-dh/2);
				cxt.moveTo(ox-70,oy-15);
				cxt.lineTo(ox-70,oy);
				cxt.lineTo(ox-74,oy+6);
				cxt.lineTo(ox-74,oy-15+dh/2);
				cxt.moveTo(ox-74,oy-15+dh/2);
				cxt.closePath();
				cxt.stroke();
				//折点圆圈
				cxt.setLineDash([]);
				cxt.beginPath();
				cxt.arc(ox-40,oy-15,2,0,Math.PI*2,true);
				cxt.stroke();
				cxt.beginPath();
				cxt.lineWidth = 2;
				cxt.arc(ox-65,oy-15,5,0,Math.PI*2,true);
				cxt.stroke();
				cxt.beginPath();
				cxt.lineWidth = 2;
				cxt.arc(ox-65,oy-15,2,0,Math.PI*2,true);
				cxt.fill();
				//悬浮窗左侧梯形块
				cxt.beginPath();
				cxt.fillStyle = color.back;
				cxt.moveTo(ox-74,oy-30)
				cxt.lineTo(ox-78,oy-36);
				cxt.lineTo(ox-78,oy+6);
				cxt.lineTo(ox-74,oy);
				cxt.fill();
				//定义窗体样式 及起点
				cxt.lineWidth = 0.5;
				var dx = ox-82-dw;
				var dy = oy-15-dh/2;
				//窗体边框
				cxt.beginPath();
				cxt.moveTo(dx+dw-4,dy);
				cxt.bezierCurveTo(dx+dw, dy, dx+dw, dy+5, dx+dw, dy+10);
				cxt.lineTo(dx+dw,dy+dh);
				cxt.lineTo(dx+20,dy+dh);
				cxt.lineTo(dx,dy+dh*2/3);
				cxt.lineTo(dx,dy);
				cxt.lineTo(dx+dw-4,dy);
				cxt.stroke();
				//窗体填充背景
				cxt.fillStyle = settings.detail.shadeColor;
				cxt.fill();
				//窗体左上护角
				cxt.fillStyle = color.back;
				cxt.beginPath();
				cxt.moveTo(dx,dy);
				cxt.lineTo(dx,dy+25);
				cxt.quadraticCurveTo(dx+4, dy+25, dx+4, dy+21);
				cxt.lineTo(dx+4,dy+8);
				cxt.lineTo(dx+8,dy+4);
				cxt.lineTo(dx+21,dy+4);
				cxt.quadraticCurveTo(dx+25, dy+4, dx+25, dy);
				cxt.lineTo(dx,dy);
				cxt.fill();
				//窗体右下护角
				cxt.beginPath();
				cxt.moveTo(dx+dw,dy+dh);
				cxt.lineTo(dx+dw,dy+dh-25);
				cxt.quadraticCurveTo(dx+dw-4, dy+dh-25, dx+dw-4, dy+dh-21);
				cxt.lineTo(dx+dw-4,dy+dh-8);
				cxt.lineTo(dx+dw-8,dy+dh-4);
				cxt.lineTo(dx+dw-21,dy+dh-4);
				cxt.quadraticCurveTo(dx+dw-25, dy+dh-4, dx+dw-25, dy+dh);
				cxt.lineTo(dx+dw,dy+dh);
				cxt.fill();
				//窗体内左右虚线
				cxt.beginPath();
				cxt.setLineDash([1,1]);
				cxt.moveTo(dx+20,dy+20);
				cxt.lineTo(dx+20,dy+dh-20);
				cxt.moveTo(dx+dw-20,dy+20);
				cxt.lineTo(dx+dw-20,dy+dh-20);
				cxt.stroke();
				//窗体内左箭头
				cxt.beginPath();
				cxt.moveTo(dx+10,dy+dh/2);
				cxt.lineTo(dx+20,dy+dh/2-5);
				cxt.lineTo(dx+20,dy+dh/2+5);
				cxt.fill();
				openOverlay.pre = [{x:dx,y:dy},{x:dx,y:dy+dh},{x:dx+20,y:dy+dh},{x:dx+20,y:dy}];
				//窗台内右箭头
				cxt.beginPath();
				cxt.moveTo(dx+dw-10,dy+dh/2);
				cxt.lineTo(dx+dw-20,dy+dh/2-5);
				cxt.lineTo(dx+dw-20,dy+dh/2+5);
				cxt.fill();
				openOverlay.next = [{x:dx+dw-20,y:dy},{x:dx+dw-20,y:dy+dh},{x:dx+dw,y:dy+dh-40},{x:dx+dw,y:dy}];
				//窗体内文字
				cxt.fillStyle = settings.detail.color;
				cxt.shadowBlur = 0;
				for(var i=0;i<content.length;i++){
					cxt.fillText(content[i],dx+40,dy+15+(settings.detail.fontSize+5)*(i+1));
				}
			}
    	}	
    }
    var drawScrollY = function(){
    	cxt.setLineDash([]);
    	cxt.fillStyle = '#162659';
    	cxt.fillRect(canvas.width-10,settings.xAxis.height,10,canvas.height-settings.xAxis.height);
    	cxt.fillStyle = '#24356B';
    	cxt.strokeRect(canvas.width-10,settings.xAxis.height,10,canvas.height-settings.xAxis.height);
    	cxt.fillStyle = '#3B77FF';
    	scrollY.x = canvas.width-10;
    	scrollY.y = -basicY/contentHeight*(canvas.height-settings.xAxis.height)+settings.xAxis.height;
    	scrollY.h = (canvas.height-settings.xAxis.height)*(canvas.height-settings.xAxis.height)/contentHeight;
    	if(draggingY){
    		scrollY.y = mouse.y-draggingCY;
    		if(scrollY.y<settings.xAxis.height){
    			scrollY.y = settings.xAxis.height;
    		}else if(scrollY.y+scrollY.h>canvas.height){
    			scrollY.y = canvas.height-scrollY.h;
    		}
    		basicY = -(scrollY.y-settings.xAxis.height)/(canvas.height-settings.xAxis.height)*contentHeight;
    		calcY();
    	}
    	cxt.fillRect(scrollY.x,scrollY.y,10,scrollY.h);
    }
    //统计y轴展开数目
    var getOpenNum = function(){
    	var count = 0;
    	for(var f in ypool){
    		if(ypool[f].open){
    			count++;
    		}
    	}
    	return count;
    }
    //将子级是否显示标识设为false
    var setChildShowFalse = function(id){
    	for(var f in ypool){
    		if(ypool[f].pid == id){
    			ypool[f].show = false;
    		}
    	}
    }
    //重置展开位置记录list
    var clearYPoolList = function(){
    	for(var i in ypool){
    		ypool[i].list = [];
    		ypool[i].h = settings.grid.lineHeight;
    	}
    }
    //画虚线
	var drawDashedLine = function(startX,startY,endX,endY,setLineDash,lineWidth,color){
		cxt.lineWidth = lineWidth;
		cxt.strokeStyle = color;
		cxt.setLineDash(setLineDash);
		cxt.beginPath();
		cxt.moveTo(startX,startY);
		cxt.lineTo(endX,endY);
		cxt.stroke();
		cxt.closePath();
	}
	//补零
	var zeroize = function(value,length){
		value = value+"";
		for(var i=0;i<(length-value.length);i++){
			value = "0"+value;
		}
		return value;
	}
	//计算指定时间加减给定分钟
	var calcTime = function(date,m){
		return new Date(date.getTime()+m*60*1000).pattern("yyyy-MM-dd HH:mm");
	}
	//根据坐标获取时间
	var coor2Time = function(x){
		var myDate = new Date(settings.queryParams.start);
    	var hour = myDate.getHours();
    	var min = myDate.getMinutes();
    	var sec = myDate.getSeconds();
    	var baseX = Math.floor(center);
    	var offset = Math.floor((60-sec)/60*settings.xAxis.separation/5);
    	var tempMin,t;
    	if(x<=baseX){
    		var beforeX = Math.floor(baseX-(settings.xAxis.separation-((4-min%5)*settings.xAxis.separation/5+offset)+2*settings.xAxis.separation));
    		if(x>=beforeX){
    			if((baseX-x)<=(settings.xAxis.separation/5-offset)){
    				t = 0;
    			}else{
    				t = Math.ceil((baseX-x-(settings.xAxis.separation/5-offset))*5/settings.xAxis.separation);
    			}
    		}else if((beforeX-settings.xAxis.separation)<=x){
    			tempMin = ((60*hour+min)-(10+min%5)-5)%zoomTime[settings.zoom]+5;
    			t = (10+min%5) + Math.ceil((beforeX-x)*tempMin/settings.xAxis.separation);
    		}else{
    			tempMin = ((60*hour+min)-(10+min%5)-5)%zoomTime[settings.zoom]+5;
    			t = (tempMin+10+min%5) + Math.ceil((beforeX-settings.xAxis.separation-x)*zoomTime[settings.zoom]/settings.xAxis.separation);
    		}
    		return calcTime(myDate,-t);
    	}else{
    		var afterX = Math.ceil(baseX+(4-min%5)*settings.xAxis.separation/5+offset+2*settings.xAxis.separation);
    		if(x<=afterX){
    			if((x-baseX)<offset){
    				t = 0;
    			}else{
    				t = Math.floor((x-baseX-offset)*5/settings.xAxis.separation)+1;
    			}
    		}else if(afterX+settings.xAxis.separation>=x){
    			if(settings.zoom==1){
    				tempMin = 5;
    			}else{
    				tempMin = zoomTime[settings.zoom]-((60*hour)+((Math.floor(min/5)+1)*5)+10)%zoomTime[settings.zoom];
    			}
    			t = (15-min%5) + Math.floor((x-afterX)*tempMin/settings.xAxis.separation);
    		}else{
    			if(settings.zoom==1){
    				tempMin = 5;
    			}else{
    				tempMin = zoomTime[settings.zoom]-((60*hour)+((Math.floor(min/5)+1)*5)+10)%zoomTime[settings.zoom];
    			}
    			t = (15-min%5+tempMin) + Math.floor((x-afterX-settings.xAxis.separation)*zoomTime[settings.zoom]/settings.xAxis.separation);
    		}
    		return calcTime(myDate,t);
    	}
	}
	//根据时间获取坐标
	var time2Coor = function(dateStr){
		if(!dateStr){
			return false;
		}
		dateStr=dateStr.replace(new RegExp(/-/gm) ,"/");
		var now = new Date(settings.queryParams.start);
		var hour = now.getHours();
    	var min = now.getMinutes();
    	var sec = now.getSeconds();
    	var t = Math.round((new Date(dateStr).getTime() - (now.getTime()-sec*1000))/60000);
    	var baseX = Math.floor(center);
		var offset = Math.floor((60-sec)/60*settings.xAxis.separation/5);
		var tempMin;
		if(t>=0){
			var afterX = Math.ceil(baseX+(4-min%5)*settings.xAxis.separation/5+offset+2*settings.xAxis.separation);
			if(settings.zoom==1){
				tempMin = 5;
			}else{
				tempMin = zoomTime[settings.zoom]-((60*hour)+((Math.floor(min/5)+1)*5)+10)%zoomTime[settings.zoom];
			}
			if(t<=(15-min%5)){
				return baseX-settings.xAxis.separation/5+offset+t*settings.xAxis.separation/5;
			}else if(t<=(15-min%5+tempMin)){
				return afterX+(t-(15-min%5))*settings.xAxis.separation/tempMin;
			}else{
				return afterX+settings.xAxis.separation+(t-(15-min%5+tempMin))*settings.xAxis.separation/zoomTime[settings.zoom];
			}
		}else{
			var beforeX = Math.floor(baseX-(settings.xAxis.separation-((4-min%5)*settings.xAxis.separation/5+offset)+2*settings.xAxis.separation));
			tempMin = ((60*hour+min)-(10+min%5)-5)%zoomTime[settings.zoom]+5;
			if(-t <= 10+min%5){
				return baseX-settings.xAxis.separation/5+offset+t*settings.xAxis.separation/5;
			}else if(-t <= 10+min%5+tempMin){
				return beforeX-(-t-10-min%5)*settings.xAxis.separation/tempMin;
			}else{
				return beforeX-settings.xAxis.separation-(-t-10-min%5-tempMin)*settings.xAxis.separation/zoomTime[settings.zoom];
			}
		}
	}
})(jQuery);