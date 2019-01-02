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
    window.clickRow;
	//工具方法-定义颜色组
    var colorPool = {
    		white:{
    			back:'#FFFFFF',
    			inner:'#FFFFFF',
    			border:'#FFFFFF',
    			color:'#000000'
    		},
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
			zoom:3,//缩放级别
			maxZoom:5,//最大级别
			minZoom:1,//最小级别
			center:0.35,//中心位置
			height:500,
			showEmptyRow:false,
			shadeColor:"rgba(0, 0, 0, 0.2)",
            background:"url("+ctxStatic+"/images/SJgantt-bg.jpg) no-repeat scroll 0% 0% / 100% 100%",
            clashColor:colorPool.yellow,
            url:undefined,
            data:[],
            queryParams:function (params) {//请求参数
                return params;
            },
            responseHandler:function (res) {//预处理
                return res;
            },
            xAxis:{//横轴
            	color:'#FFFFFF',//字体颜色
            	scaleColor:'#B59E80',//标尺颜色
            	alphaSep:0.003,//透明度衰减系数
            	minAlpha:0.3,//最小透明度
            	scaleHeight:6,//标尺高度
            	backgroundColor: 'rgba(4, 19, 50, 0.8)',
            	arrowColor: '#5486FF',//左右箭头颜色
            	fontSize:14,
            	height:50,
            	separation:60,//大刻度宽度
            	paddingBottom:18//数值下边距
            },
            yAxis:{//纵轴
            	color:'#D3E2FF',
            	backgroundColor: 'rgba(40, 108, 183, 0.95)',
            	fontSize:14,
            	width:65,
            	blockColor:'#071037',//装饰块颜色
            	paddingLeft:10//数值左边距
            },
            grid:{//栅格
            	show:true,
            	lineHeight:37,
            	lineWidth:0.1,
            	color:'#9A9A9B'
            },
            yData:{//纵轴数据
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
    //补零
	var zeroize = function(value,length){
		value = value+"";
		for(var i=0;i<(length-value.length);i++){
			value = "0"+value;
		}
		return value;
	}
    var today = new Date();
    var baseDate = today.getFullYear()+zeroize((today.getMonth()+1),2)+zeroize(today.getDate(),2);
	var canvas;//全局canvas对象
	var cxt;//全局canvas上下文
	var settings;//全局设置
	var mouse;//鼠标位置
	window.pool = {};//覆盖物池
	var tempPool = {};//覆盖物缓冲池
	var ypool = {};//Y轴池
	var tempYpool = {};//Y轴缓冲池
	var center;//中心坐标
	var oriYWidth;//记录Y轴单列宽度
	var tempYAxis;//Y轴游标
	var openOverlay;//记录展开悬浮窗的覆盖物
	var contentHeight = 0;//甘特图内容高度
	var basicY = 0;//Y轴基准
	var scrollY = {};//滚动条
	var draggingY = false;//正在拖动滚动条标识
	var draggingCY;//鼠标距离滚动滑块顶端距离
	var dragCy;//被拖动覆盖物与鼠标Y坐标偏移量
	var dragO;//记录被拖动覆盖物
	var dashedLineList = [];//记录未保存块与母块连线坐标
	var allowList = [];//临时保存允许释放的机位
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
            	var loading = $("<div id='loading'><span>正在努力地加载数据中，请稍候……</span></div>");
            	$("body").append(loading);
	            if(settings.yData.data.length == 0){
	            	loadYData();
	            }else{
	            	handleYData(settings.yData.data);
	            }
	            addEvent();
            	drawGantt();
            	setInterval(function(){
            		drawGantt();
            	},40);  
	            return this;
	        },
	        reloadYData: function(){
	        	loadYData();
	        	return this;
	        },
	        refresh: function(i){
	        	if(i != undefined && i != ""){
		        	center = center - time2Coor(i);
	        	}
	        	openOverlay = null;
	        	clickRow = null;
	        	var interval = sessionStorage[$("#loginName").val()+"refreshxlzpGantt"];
	        	$("#refresh").html("<i class='fa fa-spinner' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟");
	        	loadYData();
	        	return this;
	        },
	        load: function(data){
	        	openOverlay = null;
	        	clickRow = null;
	        	if(data){
	        		handleData(data);
	        	}else{
	        		loadData();
	        	}
	        	return this;
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
		var interval = sessionStorage[$("#loginName").val()+"refreshxlzpGantt"];
    	$("#refresh").html("<i class='fa fa-spinner' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟");
		$.ajax({
    		type:'post',
    		url:settings.yData.url,
    		async:true,
    		data:settings.yData.queryParams,
    		dataType:'json',
    		success:function(result){
    			result = settings.yData.responseHandler(result);
    			settings.yData.data = result;
    			handleYData(result);
    		}
    	})
	}
	//Y轴数据预处理
	function handleYData(result){
		tempYpool = {};
		var tempY = settings.xAxis.height;
    	for(var i=0;i<result.length;i++){
    		var f = new field(result[i]);
    		if(ypool[f.id]){
    			f.pre = ypool[f.id].pre;
    		}
    		tempYpool[f.id] = f;
        };
        if(settings.url){
        	loadData();
        }else{
        	handleData(settings.data);
        }
	}
	var field = function(f){
		this.id = f.id;
		this.name = f.name;
		this.pre = "";
		this.x = 0;
		this.y = 0;
		this.w = oriYWidth;
		this.h = settings.grid.lineHeight;
		this.list = [];
	}
    //加载数据
	function loadData(){
		openOverlay = null;
		clickRow = null;
		$.ajax({
    		type:'post',
    		url:settings.url,
    		async:true,
			error:function(){
				$("#refresh").text("刷新失败");
			},
    		dataType:'json',
    		success:function(result){
    			settings.data = settings.responseHandler(result);
    			handleData(settings.data);
    			var interval = sessionStorage[$("#loginName").val()+"refreshxlzpGantt"];
    			var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟";
    			$("#refresh").html(btnText);
    		}
    	})
	}
	//预处理数据
	function handleData(result){
		tempPool = {};
		for(var i in result){
			var o = new overlay(result[i]);
			if(tempYpool[o.stand]){//判断Y轴项是否存在
				if(!tempPool[o.stand]){//判断覆盖物池，该属性是否存在
					tempPool[o.stand] = [];
				}
				if(pool[o.stand]){
					for(var j=0;j<pool[o.stand].length;j++){
						if(pool[o.stand][j].id == o.id){
							o.tempStand = pool[o.stand][j].tempStand;
						}
					}
				}
				
				//定义未保存的临时块
				if(o.stand != o.tempStand){
					var child = {};
					for(var attr in o){
						child[attr] = o[attr];
					}
					child.stand = o.tempStand;
					child.isChild = true;
					child.oriStand = o.stand;
					if(!tempPool[child.stand]){
						tempPool[child.stand] = [];
					}
					tempPool[child.stand].push(child);
					o.hasChild = true;
				}
				tempPool[o.stand].push(o);
			}
		}
		ypool = tempYpool;
		pool = tempPool;
		calcY();
		$("#loading").remove();
	}
	var overlay = function(d){
		this.id = d.id;
		this.stand = d.stand;
		this.tempStand = d.stand;
		this.inFltid = d.id;
		this.inFltNum = d.fltNum||"";
		this.start = d.start;
		this.end = d.end;
		this.status = d.status;
		this.color = d.status == "0"?colorPool.blue:colorPool.green;
		this.clash = false;
		this.x = time2Coor(d.start);
		this.x2 = time2Coor(d.end);
		this.y = -50;
		this.w = this.x2 - this.x;
		this.h = 0.8*settings.grid.lineHeight|0;
		this.text = this.inFltNum;
		this.isChild = false;
		this.hasChild = false;
	}
	
	function calcY(){
		contentHeight = 0;
		var tempY = basicY + settings.xAxis.height;//游标
		//待排
		ypool["DP"].y = tempY;
		ypool["DP"].h = settings.grid.lineHeight;
		if(ypool["DP"].pre == "-"){
			calcExpand("DP");
		}else{
			giveY("DP",ypool["DP"].y);
		}
		tempY += ypool["DP"].h;
		//行李转盘
		for(var i in settings.yData.data){
			var f = settings.yData.data[i].id;
			if(f != "DP" && f != "QX"){
				ypool[f].y = tempY;
				ypool[f].h = settings.grid.lineHeight;
				if(ypool[f].pre == "-"){
					calcExpand(f);
				}else{
					giveY(f,ypool[f].y);
				}
				tempY += ypool[f].h;
			}
		}
		contentHeight = tempY - basicY;
	}
	function calcExpand(f){
		ypool[f].list = [];
		for(var o in pool[f]){
			//计算覆盖物坐标及宽度
			pool[f][o].x = time2Coor(pool[f][o].start);
			pool[f][o].x2 = time2Coor(pool[f][o].end);
			pool[f][o].w = pool[f][o].x2 - pool[f][o].x;
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
		for(var o in pool[f]){
			if(pool[f][o].type == "0"){
				pool[f][o].y = y+0.1*settings.grid.lineHeight;
			}else{
				pool[f][o].y = y;
			}
		}
	}
	function calcX(){
		for(var f in pool){
			for(var o in pool[f]){
				pool[f][o].x = time2Coor(pool[f][o].start);
				pool[f][o].x2 = time2Coor(pool[f][o].end);
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
    function lazyLoad(){
    	var date = coor2Time(canvas.width/2|0).substring(0,10).replace(new RegExp(/-/gm) ,"");
    	if(date != baseDate){
    		baseDate = date;
    		$("#SJgantt").SJgantt('refresh');
    	}
    }
    //事件处理
    var addEvent = function(){
    	//添加移动事件
    	var moveInterval;
    	var moveDelay;
    	var oriCenter = center;
    	//鼠标按下事件
    	canvas.addEventListener('mousedown',function(e){
    		//平移箭头
    		if(mouse.x>settings.yAxis.width){
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
        		//滚动条
        		if(mouse.x>canvas.width-10 && mouse.y>scrollY.y && mouse.y<(scrollY.y+scrollY.h)){
        			draggingY = true;
        			draggingCY = mouse.y - scrollY.y;
        			return false;
        		}
        		//块
        		for(var f in ypool){
        			if(pool[f] && mouse.y>ypool[f].y && mouse.y<ypool[f].y+ypool[f].h){
        				for(var i=pool[f].length-1;i>=0;i--){
            	    		var o = pool[f][i];
            	    		if(!o.lock && !o.hasChild && mouse.x>o.x && mouse.x<o.x2 && mouse.y>o.y && mouse.y<o.y+o.h){
            	    			if(o.status == "1" || o.x < center){
            	    				return false;
            	    			}
            	    			moveDelay = setTimeout(function(){
            	    				$(canvas).css("cursor","move");
            	    				dragO = {};
            	    				if(o.isChild){
            	    					dragO = o;
            	    				}else{
            	    					for (var attr in o) {
                    	    				dragO[attr] = o[attr];
                    	    		    }
            	    				}
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
    		if(dragO){
    			for(var k in ypool){
    				var dragOY = mouse.y;
					if(dragOY>ypool[k].y && dragOY<ypool[k].y+ypool[k].h){
						var yItem = ypool[k];
						if(k!=dragO.stand){
							for(var f in pool){
								for(var i in pool[f]){
									if(pool[f][i].id == dragO.id && !pool[f][i].isChild){
										pool[f][i].tempStand = yItem.id;
									}
								}
							}
							loadData();
							break;
						}
					}
    			}
    			dragO = null;
    		}
    		$(canvas).css("cursor","default");
    		clearInterval(moveInterval);
    		clearTimeout(moveDelay);
    	});
    	//鼠标点击事件
    	canvas.addEventListener('click',function(e){
    		var ifOverlay = false;
			for(var f in ypool){
				if(pool[f] && f != "QX"){
					for(var i=pool[f].length-1;i>=0;i--){
        	    		var o = pool[f][i];
        	    		if(o.type != "1" && mouse.x>o.x && mouse.x<o.x2 && mouse.y>o.y && mouse.y<(o.y+o.h)){
        	    			if(openOverlay == o){
        	    				openOverlay = null;
        	    				clickRow = null;
        	    			}else{
        	    				settings.onclickOverlay(o);
        	    				openOverlay = o;
        		    			openOverlay.page = 1;
        		    			openOverlay.mouseX = mouse.x;
        		    			ifOverly = true;
        		    			clickRow = o;
        	    			}
        	    			return false;
        	    		}
        			}
				}
			}
			if(!ifOverlay){
				openOverlay = null;
				clickRow = null;
			};
    	});
    	//鼠标双击事件
    	canvas.addEventListener('dblclick',function(e){
    		if(mouse.x>settings.yAxis.width+50 && mouse.x<canvas.width-50 && mouse.y>0 && mouse.y<settings.xAxis.height){
    			center = oriCenter;
    		}else if(mouse.x<settings.yAxis.width){
    			for(var f in ypool){
    				if(mouse.x>ypool[f].x && mouse.x<ypool[f].x+oriYWidth && mouse.y>ypool[f].y && mouse.y<(ypool[f].y+ypool[f].h)){
        				if(ypool[f].pre == "-"){
        					ypool[f].pre = "+";
        				}else if(ypool[f].pre == "+"){
        					ypool[f].pre = "-";
        				}
        				calcY();
        				if(contentHeight<canvas.height-settings.xAxis.height){
    						basicY = 0;
    						calcY();
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
    	lazyLoad();
    	cxt.clearRect(0,0,canvas.width,canvas.height);
    	cxt.save();
    	//定义甘特图宽、高、背景、蒙板
    	cxt.fillStyle = settings.shadeColor;
    	cxt.fillRect(0,0,canvas.width,canvas.height);
    	//网格
    	drawGrid();
    	//覆盖物
    	dashedLineList = [];//虚线组
    	for(var f in ypool){
    		//判断机位是否在可视范围内
			if(pool[f] && ypool[f].y<canvas.height && (ypool[f].y+ypool[f].h)>0){
				var hasClash = false;
    			for(var o=0;o<pool[f].length;o++){
    				//计算覆盖物坐标及宽度
    				pool[f][o].x = time2Coor(pool[f][o].start);
    				pool[f][o].x2 = pool[f][o].end==""?canvas.width:time2Coor(pool[f][o].end);
    				pool[f][o].w = pool[f][o].x2 - pool[f][o].x;
    				//判断覆盖物X轴方向是否在可视范围内
        			if(pool[f][o].x<=canvas.width && pool[f][o].x2>=0){
        				//判断时间是否存在错误
        				if(pool[f][o].x < pool[f][o].x2){
        					pool[f][o].clash = false;
        					//判断冲突
        					if(ypool[f].pre != "-"){
        						for(var k=0;k<pool[f].length;k++){
        							if(pool[f][o]!=pool[f][k] && pool[f][o].x<pool[f][k].x2 && pool[f][k].x<pool[f][o].x2){//判断冲突
        								if(pool[f][o].stand.indexOf("DP")==-1 && pool[f][o].stand.indexOf("QX")==-1){
        									pool[f][o].clash = true;
            								pool[f][k].clash = true;
        								}
        								hasClash = true;
        							}
        			    		}
        					}
        					//画覆盖物
        					if( openOverlay != pool[f][o]){
        						drawOverlay(pool[f][o]);
        					}
        				}
        			}
        		}
    			if(hasClash){
					ypool[f].pre = "+";
				}else if(ypool[f].pre != "-"){
					ypool[f].pre = "";
				}
    		}
    	}
    	if(openOverlay){
			cxt.shadowColor = openOverlay.color.back;
			cxt.shadowBlur = 5;
			drawOverlay(openOverlay);
			cxt.shadowBlur = 0;
		}
    	if(dragO){
			drawOverlay(dragO);
			if(mouse.y>canvas.height-50 && basicY>canvas.height-contentHeight){
				basicY -= 20;
				calcY();
			}
			if(mouse.y<settings.xAxis.height+50 && basicY<0){
				basicY += 20;
				calcY();
			}
		}
    	for(var i=0;i<dashedLineList.length;i++){
    		var l = dashedLineList[i];
    		drawDashedLine(l.x1,l.y1,l.x2,l.y2,[2,2],0.5,"#FFFFFF");
    	}
    	//X轴
    	drawXAxis();
    	//当前时间线
    	var baseX = Math.floor(center);
    	drawDashedLine(baseX,settings.xAxis.height,baseX,canvas.height,[2,2],0.5,settings.xAxis.color);
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
    	//悬浮窗
    	if(settings.detail.show){
    		drawDetail();
    	}
    	cxt.restore();
    	//启用动画
    	//requestAnimationFrame(drawGantt);
    }
    //画X轴
    var drawXAxis = function(){
    	//x轴
    	cxt.fillStyle = settings.xAxis.backgroundColor;
    	cxt.fillRect(settings.yAxis.width,0,canvas.width,settings.xAxis.height);
    	//x轴刻度
    	var myDate = new Date();
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
        	cxt.beginPath();
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
        	cxt.closePath();
        	//时间数值
        	cxt.fillStyle = settings.xAxis.color;
        	cxt.textAlign = "center";
        	cxt.font = settings.xAxis.fontSize+"px sans-serif";
        	cxt.fillText(hour+":"+zeroize(min,2),baseX,settings.xAxis.height-settings.xAxis.paddingBottom-13);
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
	var drawOverlay = function(obj){
		var o = {};
	    for ( var attr in obj) {
	        o[attr] = obj[attr];
	    }
	    cxt.globalAlpha=1;
	    if(dragO && obj == dragO){
			o.y = mouse.y-o.h/2;
			cxt.globalAlpha=0.5;
		}
    	if(obj.isChild){
    		cxt.globalAlpha=0.5;
    	}
		cxt.fillStyle = o.color.back;
		cxt.fillRect(o.x,o.y,o.w,o.h);
		cxt.fillStyle = o.color.inner;
		var ix = (0.1*o.w>7?7:0.1*o.w)|0;
		var w2 = (0.2*o.w>40?o.w-ix-40:0.8*o.w-ix)|0;
		cxt.save();
		cxt.fillRect(o.x+ix,o.y+0.15*o.h|0,w2,0.7*o.h|0);
		cxt.fillStyle = o.color.color;
		cxt.font = Math.ceil(0.4*o.h)+"px sans-serif";
		var length = cxt.measureText(o.text).width;
//		if(w2-ix>=length){//默认甘特条太短，但显示机号
			cxt.textAlign = 'left';
			cxt.textBaseline = 'middle';
			if(o.inFlag == "A1" && o.outFlag == "D1"){
				cxt.fillText(o.text,o.x+2*ix,o.y+o.h/2);
			}else{
				cxt.fillText(o.inFltNum,o.x+2*ix,o.y+o.h/2);
				if(o.outFltNum){
					cxt.textAlign = 'right';
					cxt.fillText(o.outFltNum+"("+o.outGate+")",o.x+w2,o.y+o.h/2);
				}
			}
//		}
		cxt.restore();
		cxt.beginPath();
		cxt.strokeStyle = o.color.border;
		cxt.lineWidth = 0.4*ix|0;
		cxt.moveTo(o.x,o.y-2);
		cxt.lineTo(o.x,o.y+o.h);
		cxt.moveTo(o.x+2*ix+w2,o.y+0.2*ix|0);
		cxt.lineTo(o.x+o.w,o.y+0.3*ix|0);
		cxt.moveTo(o.x+2*ix+w2,o.y+0.4*o.h|0);
		cxt.lineTo(o.x+o.w,o.y+0.4*o.h|0);
		cxt.stroke();
		cxt.beginPath();
		cxt.lineWidth = 4;
		cxt.strokeStyle = o.color.border;
		cxt.moveTo(o.x,o.y);
		cxt.lineTo(o.x+(o.w>100?60:o.w/2),o.y)
		cxt.stroke();
		if(o.isChild){
			for(var i=0;i<pool[o.oriStand].length;i++){
				if(pool[o.oriStand][i].id == o.id){
					var ori = pool[o.oriStand][i];
					if(o.y<ori.y){
						var line = {
								x1:o.x+o.w/2,
								y1:o.y+o.h,
								x2:o.x+o.w/2,
								y2:ori.y
						}
						dashedLineList.push(line);
					}else{
						var line = {
								x1:o.x+o.w/2,
								y1:o.y,
								x2:o.x+o.w/2,
								y2:ori.y+ori.h
						}
						dashedLineList.push(line);
					}
				}
			}
		}
		if(o.hasChild){
			for(var i=0;i<pool[o.tempStand].length;i++){
				if(pool[o.tempStand][i].id == o.id){
					var child = pool[o.tempStand][i];
					if(o.y<child.y){
						var line = {
								x1:o.x+o.w/2,
								y1:o.y+o.h,
								x2:o.x+o.w/2,
								y2:child.y
						}
						dashedLineList.push(line);
					}else{
						var line = {
								x1:o.x+o.w/2,
								y1:o.y,
								x2:o.x+o.w/2,
								y2:child.y+child.h
						}
						dashedLineList.push(line);
					}
				}
			}
		}
	}
	//画Y轴
	var drawYAxis = function(){
    	//Y轴文字
    	cxt.fillStyle = settings.yAxis.color;
    	cxt.font = settings.yAxis.fontSize+"px sans-serif";
    	cxt.textAlign = "center";
    	cxt.textBaseline = 'middle';
    	for(var i in ypool){
    		var f = ypool[i];
    		var text = "";
    		cxt.font = settings.yAxis.fontSize*1.5+"px sans-serif";
			cxt.fillText(f.pre,15,(f.y+settings.grid.lineHeight/2));
			cxt.font = settings.yAxis.fontSize+"px sans-serif";
			cxt.fillText(f.name,oriYWidth/2+5,f.y+settings.grid.lineHeight/2);
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
    //画悬浮窗
    function drawDetail(){
    	var currentOverlay;
    	var onOverlay = false;
    	loop:
    	for(var f in ypool){
    		if(ypool[f].y <= mouse.y && ypool[f].y+ypool[f].h > mouse.y && pool[f]){
    			for(var o = pool[f].length-1;o>-1;o--){
    				if(pool[f][o].type == "0" && pool[f][o].x <= mouse.x && pool[f][o].x2 > mouse.x && pool[f][o].y <= mouse.y && pool[f][o].y+pool[f][o].h > mouse.y){
    					currentOverlay = pool[f][o];
    					currentOverlay.page = 1;
    					currentOverlay.mouseX = mouse.x;
    					onOverlay = true;
    					break loop;
    				}
    			}
    		}
    	}
    	if(onOverlay){
    		//定义起点、宽高、内容
	    	var o = currentOverlay;
	    	var color = currentOverlay.color;
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
			//自适应宽度
			/*var dw = 0;
			for(var i=0;i<content.length;i++){
				var width = cxt.measureText(content[i]).width;
				if(width>dw){
					dw = width;
				}
			}
			dw = dw + 80;*/
			dw = 300;
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
				currentOverlay.pre = [{x:dx,y:dy},{x:dx,y:dy+dh},{x:dx+20,y:dy+dh},{x:dx+20,y:dy}];
				//窗台内右箭头
				cxt.beginPath();
				cxt.moveTo(dx+dw-10,dy+dh/2);
				cxt.lineTo(dx+dw-20,dy+dh/2-5);
				cxt.lineTo(dx+dw-20,dy+dh/2+5);
				cxt.fill();
				currentOverlay.next = [{x:dx+dw-20,y:dy},{x:dx+dw-20,y:dy+dh},{x:dx+dw,y:dy+dh-40},{x:dx+dw,y:dy}];
				//窗体内文字
				cxt.fillStyle = settings.detail.color;
				cxt.shadowBlur = 0;
				for(var i=0;i<content.length;i++){
					var t = content[i].split(" ");
					cxt.textAlign = "left";
					cxt.fillText(t[0],dx+40,dy+15+(settings.detail.fontSize+5)*(i+1));
					cxt.textAlign = "center";
					cxt.fillText(t[1],dx+dw/2,dy+15+(settings.detail.fontSize+5)*(i+1));
					cxt.textAlign = "right";
					cxt.fillText(t[2],dx+dw-40,dy+15+(settings.detail.fontSize+5)*(i+1));
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
				currentOverlay.pre = [{x:dx,y:dy},{x:dx,y:dy+dh},{x:dx+20,y:dy+dh},{x:dx+20,y:dy}];
				//窗台内右箭头
				cxt.beginPath();
				cxt.moveTo(dx+dw-10,dy+dh/2);
				cxt.lineTo(dx+dw-20,dy+dh/2-5);
				cxt.lineTo(dx+dw-20,dy+dh/2+5);
				cxt.fill();
				currentOverlay.next = [{x:dx+dw-20,y:dy},{x:dx+dw-20,y:dy+dh},{x:dx+dw,y:dy+dh-40},{x:dx+dw,y:dy}];
				//窗体内文字
				cxt.fillStyle = settings.detail.color;
				cxt.shadowBlur = 0;
				for(var i=0;i<content.length;i++){
					var t = content[i].split(" ");
					cxt.textAlign = "left";
					cxt.fillText(t[0],dx+40,dy+15+(settings.detail.fontSize+5)*(i+1));
					cxt.textAlign = "center";
					cxt.fillText(t[1],dx+dw/2,dy+15+(settings.detail.fontSize+5)*(i+1));
					cxt.textAlign = "right";
					cxt.fillText(t[2],dx+dw-40,dy+15+(settings.detail.fontSize+5)*(i+1));
				}
			}
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
		cxt.save();
		cxt.lineWidth = lineWidth;
		cxt.strokeStyle = color;
		cxt.setLineDash(setLineDash);
		cxt.beginPath();
		cxt.moveTo(startX,startY);
		cxt.lineTo(endX,endY);
		cxt.stroke();
		cxt.setLineDash([]);
		cxt.closePath();
		cxt.restore();
	}
	//计算指定时间加减给定分钟
	var calcTime = function(date,m){
		return new Date(date.getTime()+m*60*1000).pattern("yyyy-MM-dd HH:mm");
	}
	//根据坐标获取时间
	var coor2Time = function(x){
		var myDate = new Date();
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
		var now = new Date();
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