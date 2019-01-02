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
			zoom:1,
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
            	fontSize:14,
            	width:50,
            	blockColor:'#071037',
            	paddingLeft:10
            },
            grid:{
            	show:true,
            	lineHeight:40,
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
	var pool = [];//覆盖物池
	var ypool = {};//Y轴池
	var center;
	var oriYWidth;//记录Y轴单列宽度
	var tempYAxis;//Y轴游标
	var openOverlay;//记录展开悬浮窗的覆盖物
	var contentHeight = 0;
	var basicY = 0;
	var scrollY = {};
	var draggingY = false;
	var draggingCY;
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
    			result = settings.yData.responseHandler(result);
    			handleYData(result);
    		}
    	})
	}
	//Y轴数据预处理
	function handleYData(result){
		ypool = {};
		var tempY = settings.xAxis.height;
    	for(var i=0;i<result.length;i++){
    		var f = new field(result[i]);
        	ypool[f.id] = f;
        };
	}
	var field = function(f){
		this.id = f.id;
		this.pre = "";
		this.name = f.name;
		this.x = 0;
		this.y = 0;
		this.list = [];
		this.h = settings.grid.lineHeight;
		this.expand = true;
	}
	var earlestTime = new Date();
    //加载数据
	function loadData(){
		$.ajax({
    		type:'post',
    		url:settings.url,
    		async:false,
    		data:settings.queryParams,
    		dataType:'json',
    		success:function(result){
    			pool = [];
    			/*if(init && result.length!=0){
    				center = 2*center-time2Coor(result[0].estart); 
    				init = false;
    			}*/
    			if(result.length>0){
    				earlestTime = result[0].estart;
    			}
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
			if(!pool[o.id]){
				pool[o.id] = [];
			}
			pool[o.id].push(o);
		}
		var done = 0;
		for(var o in result){
			if(result[o].end){
				done++;
			}
		}
		$(".flow").css("width",(70*done/result.length|0)+"%");
		if(0==done){
			$(".rate").hide();
		}else{
			$(".rate").show();
			$(".rate").text((100*done/result.length|0)+"%");
		}
		$(".rate").css("left",(35*done/result.length|0)-4+"%");
		$(".plane").css("left",(70*done/result.length|0)-4+"%");
		calcY();
	}
	var overlay = function(d){
		this.id = d.id;
		this.start = d.start;
		this.end = d.end;
		this.estart = d.estart;
		this.eend = d.eend;
		this.err = d.err;
		this.x = d.start?time2Coor(d.start):null;
		this.x2 = d.end?time2Coor(d.end):null;
		this.ex = d.estart?time2Coor(d.estart):null;
		this.ex2 = d.eend?time2Coor(d.eend):null;
		this.minx = min(this.x,this.ex);
		this.maxx = max(this.x2,this.ex2);
		this.y = 0;
		this.h = settings.grid.lineHeight;
		this.color = d.end?colorPool.green:colorPool.blue;
        this.validate();
	}
	overlay.prototype = {
		validate:function(){
			for(var o in pool[this.id]){
				if(this.minx<pool[this.id][o].maxx && pool[this.id][o].minx<this.maxx){
					if(ypool[this.id].expand){
						ypool[this.id].pre = "-";
					}else{
						ypool[this.id].pre = "+";
					}
				}
    		}
		}
	}
	function min(a,b){
		if(!a){
			return b;
		}
		if(!b){
			return a;
		}
		return a<b?a:b;
	}
	function max(a,b){
		if(!a){
			return max(b,center);
		}
		if(!b){
			return a;
		}
		return a>b?a:b;
	}
	function calcY(){
		contentHeight = 0;
		var tempY = basicY + settings.xAxis.height;
		for(var f in ypool){
			ypool[f].y = tempY;
			ypool[f].h = settings.grid.lineHeight;
			if(ypool[f].expand){
				if(ypool[f].pre == "+"){ypool[f].pre = "-"};
				ypool[f].list = [];
				for(var i in pool[f]){
					var o = pool[f][i];
					if(ypool[f].list.length==0){
						ypool[f].list.push([[o.minx,o.maxx]]);
						o.y = ypool[f].y;
					}else{
						var rowClash = true;
						loop:
						for(var j=0;j<ypool[f].list.length;j++){
							var colClash = false;
							for(var k=0;k<ypool[f].list[j].length;k++){
								if(o.minx<ypool[f].list[j][k][1] && ypool[f].list[j][k][0]<o.maxx){
									colClash = true;
									break;
								}
							}
							if(!colClash){
								ypool[f].list[j].push([o.minx,o.maxx]);
								o.y = ypool[f].y + j*settings.grid.lineHeight;
								rowClash = false;
								break;
							}
						}
						if(rowClash){
							ypool[f].list.push([[o.minx,o.maxx]]);
							ypool[f].h += settings.grid.lineHeight;
							o.y = ypool[f].y+(ypool[f].list.length-1)*settings.grid.lineHeight;
						}
					}
				}
			}else{
				if(ypool[f].pre == "-"){ypool[f].pre = "+"};
				for(var o in pool[f]){
					pool[f][o].y = tempY;
				}
			}
			tempY += ypool[f].h;
		}
		contentHeight = tempY - basicY + 100;
	}
	function calcX(){
		for(var f in pool){
			for(var o in pool[f]){
				pool[f][o].x = pool[f][o].start?time2Coor(pool[f][o].start):null;
				pool[f][o].x2 = pool[f][o].end?time2Coor(pool[f][o].end):null;
				pool[f][o].ex = pool[f][o].estart?time2Coor(pool[f][o].estart):null;
				pool[f][o].ex2 = pool[f][o].eend?time2Coor(pool[f][o].eend):null;
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
    	var oriCenter = center;
    	//鼠标按下事件
    	canvas.addEventListener('mousedown',function(e){
    		if(mouse.x<settings.yAxis.width){
    			openOverlay = null;
    			for(var f in ypool){
    				if(mouse.x>ypool[f].x && mouse.x<ypool[f].x+oriYWidth && mouse.y>ypool[f].y && mouse.y<ypool[f].y+ypool[f].h){
        				if(ypool[f].expand){
        					ypool[f].expand = false;
        					calcY();
        					if(contentHeight<canvas.height-settings.xAxis.height){
        						basicY = 0;
        						calcY();
        					}
        				}else{
        					ypool[f].expand = true;
    						calcY();
        				}
        				break;
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
            					pool[f][o].ex += 10;
            					pool[f][o].ex2 += 10;
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
            					pool[f][o].ex -= 10;
            					pool[f][o].ex2 -= 10;
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
    		}
    	});
    	//鼠标抬起事件
    	canvas.addEventListener('mouseup',function(e){
    		draggingY = false;
    		clearInterval(moveInterval);
    	});
    	//鼠标双击事件
    	canvas.addEventListener('dblclick',function(e){
    		if(mouse.x>settings.yAxis.width+50 && mouse.x<canvas.width-50 && mouse.y>0 && mouse.y<settings.xAxis.height){
    			center = oriCenter;
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
    		var keyCode = e.which?e.which:w.keyCode;
    		if(keyCode == 192){
    			canvas.addEventListener('DOMMouseScroll',changeZoomFF);
    			canvas.addEventListener('mousewheel',changeZoom);
    		}
    	};
    	document.onkeyup = function(e){
    		keydown = false;
    		var keyCode = e.which?e.which:w.keyCode;
    		if(keyCode == 192){
    			canvas.removeEventListener('DOMMouseScroll',changeZoomFF);
    			canvas.removeEventListener('mousewheel',changeZoom);
    		}
    	};
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
    		if(ypool[f].y<canvas.height && (ypool[f].y+ypool[f].h)>0){
    			for(var o in pool[f]){
    				pool[f][o].x = pool[f][o].start?time2Coor(pool[f][o].start):null;
    				pool[f][o].x2 = pool[f][o].end?time2Coor(pool[f][o].end):null;
    				pool[f][o].ex = pool[f][o].estart?time2Coor(pool[f][o].estart):null;
    				pool[f][o].ex2 = pool[f][o].eend?time2Coor(pool[f][o].eend):null;
    				pool[f][o].minx = min(pool[f][o].x,pool[f][o].ex);
    				pool[f][o].maxx = max(pool[f][o].x2,pool[f][o].ex2);
        			if(pool[f][o].minx<canvas.width && pool[f][o].maxx>0 && pool[f][o].y<canvas.height && (pool[f][o].y+pool[f][o].h)>0){
        				drawOverlay(pool[f][o]);
        			}
        		}
    		}
    	}
    	//X轴
    	drawXAxis();
    	//当前时间线
    	//var baseX = Math.floor(center);
    	//drawDashedLine(baseX,settings.xAxis.height,baseX,canvas.height,[2,2],0.5,settings.xAxis.color);
    	//y轴
    	tempYAxis = settings.xAxis.height;
    	//Y轴背景
    	cxt.fillStyle = settings.yAxis.backgroundColor;
    	cxt.fillRect(0,settings.xAxis.height,settings.yAxis.width,canvas.height-settings.xAxis.height);
    	drawYAxis("level",settings.yData.startLevel);
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
                	cxt.fillText(coor2Time(mouse.x),mouse.x+62,mouse.y+25);
    			}
    		}else{
    			drawDashedLine(mouse.x,settings.xAxis.height,mouse.x,canvas.height,[2,2],0.5,'#FFFFFF');
        		cxt.fillStyle = "rgba(0,0,0,0.5)";
        		cxt.fillRect(mouse.x+15,mouse.y+15,94,20);
        		cxt.fillStyle = "#ccc";
        		cxt.font = "10px sans-serif";
            	cxt.fillText(coor2Time(mouse.x),mouse.x+62,mouse.y+25);
    		}
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
    	var myDate = new Date(earlestTime);
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
        	if(tempMin%5==4/* && (tempX-baseX)>=settings.xAxis.separation*2/3*/){
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
        	if((60-tempMin)%5==0 /*&& (baseX-tempX)>=settings.xAxis.separation*2/3*/){
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
        	/*cxt.fillStyle = settings.xAxis.color;
        	cxt.textAlign = "center";
        	cxt.font = settings.xAxis.fontSize+"px sans-serif";
        	cxt.fillText(hour+":"+zeroize(min,2),baseX,settings.xAxis.height-settings.xAxis.paddingBottom);*/
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
	var drawOverlay = function(o){
		var h = settings.grid.lineHeight;
		cxt.strokeStyle = o.color.back;
		cxt.fillStyle = o.color.inner;
		if((o.x && o.x>o.ex) || (!o.x && o.ex<center)){
			cxt.fillStyle = colorPool.red.inner;
		}
		cxt.font = 0.35*h+"px sans-serif";
		cxt.fillRect(o.ex,o.y+0.8*h,o.ex2-o.ex,0.1*h);
		cxt.fillStyle = o.color.inner;
		cxt.lineWidth = 0.2*h;
		var eTime = minusTime(o.eend,o.estart);
		var aTime = 0;
		if(o.x){
			if(o.x>=o.ex2){
				var end = center;
				if(o.x2){
					end = o.x2;
				}
				cxt.strokeStyle = colorPool.red.back;
				cxt.fillStyle = colorPool.red.inner;
				cxt.strokeRect(o.x+0.1*h,o.y+0.2*h,end-o.x-0.2*h,0.5*h);
				cxt.fillRect(o.x+0.1*h,o.y+0.2*h,end-o.x-0.2*h,0.5*h);
				var aTime = minusTime(o.end,o.start);
				if(cxt.measureText(aTime+"/"+eTime).width<=end-o.x-0.4*h){
					cxt.fillStyle = o.color.color;
					cxt.fillText(aTime+"/"+eTime,o.x+0.2*h,o.y+0.6*h);
				}
			}else{
				if(o.x2){
					if(o.x2<=o.ex2){
						cxt.strokeRect(o.x+0.1*h,o.y+0.2*h,o.x2-o.x-0.2*h,0.5*h);
						cxt.fillRect(o.x+0.1*h,o.y+0.2*h,o.x2-o.x-0.2*h,0.5*h);
						var aTime = minusTime(o.end,o.start);
						if(cxt.measureText(aTime+"/"+eTime).width<=o.x2-o.x-0.4*h){
							cxt.fillStyle = o.color.color;
							cxt.fillText(aTime+"/"+eTime,o.x+0.2*h,o.y+0.6*h);
						}
					}else{
						cxt.strokeRect(o.x+0.1*h,o.y+0.2*h,o.ex2-o.x-0.2*h,0.5*h);
						cxt.fillRect(o.x+0.1*h,o.y+0.2*h,o.ex2-o.x-0.2*h,0.5*h);
						var aTime = minusTime(o.end,o.start);
						if(cxt.measureText(aTime+"/"+eTime).width<=o.ex2-o.x-0.4*h){
							cxt.fillStyle = o.color.color;
							cxt.fillText(aTime+"/"+eTime,o.x+0.2*h,o.y+0.6*h);
						}
						
						cxt.strokeStyle = colorPool.red.back;
						cxt.fillStyle = colorPool.red.inner;
						cxt.strokeRect(o.ex2+0.1*h,o.y+0.2*h,o.x2-o.ex2-0.2*h,0.5*h);
						cxt.fillRect(o.ex2+0.1*h,o.y+0.2*h,o.x2-o.ex2-0.2*h,0.5*h);
						if(cxt.measureText(minusTime(o.end,o.eend)).width<=o.x2-o.ex2-0.4*h){
							cxt.fillStyle = o.color.color;
							cxt.fillText("+"+minusTime(o.end,o.eend),o.ex2+0.2*h,o.y+0.6*h);
						}
					}
				}else{
					if(o.ex2>center){
						cxt.strokeRect(o.x+0.1*h,o.y+0.2*h,center-o.x-0.2*h,0.5*h);
						cxt.fillRect(o.x+0.1*h,o.y+0.2*h,center-o.x-0.2*h,0.5*h);
						var aTime = minusTime(o.end,o.start);
						if(cxt.measureText(aTime+"/"+eTime).width<=center-o.x-0.4*h){
							cxt.fillStyle = o.color.color;
							cxt.fillText(aTime+"/"+eTime,o.x+0.2*h,o.y+0.6*h);
						}
					}else{
						cxt.strokeRect(o.x+0.1*h,o.y+0.2*h,o.ex2-o.x-0.2*h,0.5*h);
						cxt.fillRect(o.x+0.1*h,o.y+0.2*h,o.ex2-o.x-0.2*h,0.5*h);
						var aTime = minusTime(o.end,o.start);
						if(cxt.measureText(aTime+"/"+eTime).width<=o.ex2-o.x-0.4*h){
							cxt.fillStyle = o.color.color;
							cxt.fillText(aTime+"/"+eTime,o.x+0.2*h,o.y+0.6*h);
						}
						
						cxt.strokeStyle = colorPool.red.back;
						cxt.fillStyle = colorPool.red.inner;
						cxt.strokeRect(o.ex2+0.1*h,o.y+0.2*h,center-o.ex2-0.2*h,0.5*h);
						cxt.fillRect(o.ex2+0.1*h,o.y+0.2*h,center-o.ex2-0.2*h,0.5*h);
						if(cxt.measureText(minusTime(null,o.eend)).width<=center-o.ex2-0.4*h){
							cxt.fillStyle = o.color.color;
							cxt.fillText("+"+minusTime(null,o.eend),o.ex2+0.2*h,o.y+0.6*h);
						}
					}
				}
			}
		}else{
			cxt.fillStyle = o.color.color;
			cxt.fillText(aTime+"/"+eTime,o.ex+0.3*h,o.y+0.7*h);
		}
		if(o.err != 0){
			cxt.fillStyle = colorPool.yellow.back;
			cxt.beginPath();
			var startX = o.ex-0.5*h;
			if(o.x && o.x < o.ex){
				startX = o.x-0.5*h;
			}
			cxt.save();
			cxt.moveTo(startX,o.y+0.25*h);
			cxt.lineTo(startX-0.25*h,o.y+0.65*h);
			cxt.lineTo(startX+0.25*h,o.y+0.65*h);
			cxt.fill();
			cxt.globalCompositeOperation = "destination-out";
			cxt.fillText("!",startX-2,o.y+0.6*h);
			cxt.restore();
		}
		/*y = y+Math.ceil((settings.grid.lineHeight-h)/2);
		cxt.fillStyle = color.back;
		cxt.fillRect(x,y,w,h);
		cxt.fillStyle = color.inner;
		var ix = (0.1*w>7?7:0.1*w)|0;
		var w2 = (0.2*w>40?w-ix-40:0.8*w-ix)|0;
		cxt.fillRect(x+ix,y+0.15*h|0,w2,0.7*h|0);
		cxt.fillStyle = color.color;
		cxt.font = Math.ceil(0.4*h)+"px sans-serif";
		var length = cxt.measureText(text).width;
		if(w2-ix>=length){
			cxt.textAlign = 'left';
			cxt.textBaseline = 'middle';
			cxt.fillText(text,x+2*ix,y+h/2);
		}
		cxt.beginPath();
		cxt.strokeStyle = color.border;
		cxt.lineWidth = 0.4*ix|0;
		cxt.moveTo(x,y-2);
		cxt.lineTo(x,y+h);
		cxt.moveTo(x+2*ix+w2,y+0.2*ix|0);
		cxt.lineTo(x+w,y+0.3*ix|0);
		cxt.moveTo(x+2*ix+w2,y+0.4*h|0);
		cxt.lineTo(x+w,y+0.4*h|0);
		cxt.stroke();
		cxt.beginPath();
		cxt.lineWidth = 4;
		cxt.moveTo(x,y);
		cxt.lineTo(x+(w>100?60:w/2),y)
		cxt.stroke();*/
	}
	//画Y轴
	var drawYAxis = function(type,code){
    	//Y轴文字
    	cxt.fillStyle = settings.yAxis.color;
    	cxt.font = settings.yAxis.fontSize+"px sans-serif";
    	cxt.textAlign = "center";
    	cxt.textBaseline = 'middle';
    	for(var f in ypool){
    		var f = ypool[f];
    		var width = cxt.measureText(f.pre+" "+f.name).width;
    		if(width>settings.yAxis.width-4){
    			settings.yAxis.width = width+4;
    		}
    		cxt.fillText(f.pre+" "+f.name,f.x+settings.yAxis.width/2,(f.y+settings.grid.lineHeight/2));
    		cxt.beginPath();
			cxt.fillStyle = settings.yAxis.color;
			cxt.setLineDash([]);
	    	cxt.lineWidth = 0.1;
	    	cxt.moveTo(f.x,f.y);
	    	cxt.lineTo(settings.yAxis.width,f.y);
	    	cxt.moveTo(f.x,f.y+f.h);
	    	cxt.lineTo(settings.yAxis.width,f.y+f.h);
	    	cxt.stroke();
    	}
    }
    var drawScrollY = function(){
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
	//计算时间差
	var minusTime = function(date1,date2){
		if(!date1){
			return Math.floor((new Date().getTime()-new Date(date2).getTime())/60000);
		}else{
			return Math.floor((new Date(date1).getTime()-new Date(date2).getTime())/60000);
		}
	}
	//根据坐标获取时间
	var coor2Time = function(x){
		var myDate = new Date(earlestTime);
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
		dateStr=dateStr.replace(new RegExp(/-/gm) ,"/");
		var now = new Date(earlestTime);
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