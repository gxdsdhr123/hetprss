(function ($) {
	$.ajax({
		  url: ctxStatic + "/jquery/plugins/progressChart/zrender.min.js",
		  dataType: "script",
		  async:false
		});
	
	var zr;
	var startX = 0;
	var startY = 0;
	var nodeTextArray=[];
	var nodeTimeLengthArray=[];
	var nodeStatusArray=[];
	var nodeTimeArray=[];
	// 参数列表
	var options = {
			/**主流程节点    [{nodeText:'string',nodeTime:'string',nodeBeginX:num,nodeBeginY:num,status:num,width:num,height:num,arrowWidth:num}]*/
			mainNodes:[],
			/**其他流程节点  [{nodeText:'string',outText:'string',nodeBeginX:num,nodeBeginY:num,status:num,width:num,height:num}]*/
			otherNodes:[],
			/**时间节点 [{aTime:'string',eTime:'string',nodeBeginX:num,nodeBeginY:num,status:num,width:num,height:num,nodeText:'string',isText:(boolean)}]*/
			timeNodes:[],
			/**画线 [{x1:num,y1:num,x2:num,y2:num,status:num}]*/
			lines:[],
			/**箭头线 [{x1:num,y1:num,width:num,status:num}]*/
			arrowLine:[],
			/**顶部折线 [{points:[[x1,y1],[x2,y2]...],status:num}]*/
			brokenLine:[],
			/**流程节点分组*/
			nodeGroups:[],
			beginX:0,
			beginY:0,
			step:0,
			monitorWidth:0,
			monitorHeight:0,
			alignCenter:1
	};
	
	var initParam = function(){
		nodeTextArray=[];
		nodeTimeLengthArray=[];
		nodeStatusArray=[];
		nodeTimeArray=[];
	}
	
	
	// 状态颜色
	var getColorByStatus = function(status){
		var colorArray=[];
		switch(status){
			case 1:
				colorArray=['#213e85','#3057b9'];
				break;
			case 2:
				colorArray=['#6F2A3F','#A92F42'];
				break;
			case 3:
				colorArray=['#868c99','#cccccc'];
				break;
			case 4:
				colorArray=['#111111','#333333'];
				break;
			default:
				colorArray=['#213e85','#3057b9'];
				break;
	   } 	
	   return colorArray;
	}
	
	//创建流程节点分组
	var createNodeGroup = function(points,id){
		var nodeGroup = new zrender.Polygon({
			id : id,
		    shape: {
		    	points:points
		    },
		    style: {
		        fill: '#000000',
		        opacity:0
		    }
		});
		nodeGroup.on('mouseover',function(params){
			this.attr({
				style: {
					opacity:0.3
			    }
			});
		}).on('mouseout',function(params){
			this.attr({
				style: {
					opacity:0
			    }
			});
		});
		zr.add(nodeGroup);
		return nodeGroup;
	}
	
	//创建主流程节点
	var createNode = function(nodeText,nodeBeginX,nodeBeginY,width,height,arrowWidth,status,id){
		var colorArray=getColorByStatus(status);
		width=width?width:100;
		height=height?height:70;
		arrowWidth=arrowWidth?arrowWidth:20;
		var node = new zrender.Polygon({
			id : id,
			shape: {
				points:[[nodeBeginX,nodeBeginY+height],[nodeBeginX+width,nodeBeginY+height],
				        [nodeBeginX+width+arrowWidth,nodeBeginY+height/2],[nodeBeginX+width,nodeBeginY],
				        [nodeBeginX,nodeBeginY],[nodeBeginX+arrowWidth,nodeBeginY+height/2]]
			},
			style: {
				fill: colorArray[0],
				stroke: colorArray[1],
				lineWidth:5,
				opacity:0.9,
				text:nodeText,
				fontStyle:'Microsoft YaHei',
				textFill:'#ffffff'
			}
		});
		zr.add(node);
		return node;
	}
	
	//画其他流程节点
	var createOtherNode = function(nodeText,outText,nodeBeginX,nodeBeginY,width,height,status,id){
		width=width?width:230;
		height=height?height:40;
		var colorArray=getColorByStatus(status);
		var outer=new zrender.Rect({
			shape:{
				r:[3],
				x:nodeBeginX,
				y:nodeBeginY,
				width:width,
				height:height
			},
			style: {
		        fill: colorArray[1],
		        opacity:0.9,
		        text:outText,
		        textFill:'#ffffff',
		        textPosition:[Math.min(width*0.8,width - 35),15]
		    }
		});
		zr.add(outer);
		var inner=new zrender.Rect({
			id : id,
			shape:{
				r:[3],
				x:nodeBeginX+5,
				y:nodeBeginY+5,
				width:width*0.7,
				height:height-10
			},
			style: {
				text:nodeText,
				fontStyle:'Microsoft YaHei',
		        textFill:'#ffffff',
		        fill: colorArray[0],
		        opacity:0.9,
		    }
		});
		zr.add(inner);
	}
	
	//画时间展示节点
	var createTimeNodes = function(aTime,eTime,nodeBeginX,nodeBeginY,width,height,status,nodeText,isText){
		width=width?width:50;
		height=height?height:35;
		isText=isText?isText:false;
		var colorArray=getColorByStatus(status);
		var param = {
				shape:{
					r:[5],
					x:nodeBeginX,
					y:nodeBeginY,
					width:width,
					height:height
				},
				style: {
					fill: colorArray[0],
					opacity:0.9,
					textFill:'#ffffff',
					textPosition:[5,5]
				}
			};
		if(isText){
			var text = nodeText?nodeText:' ';
			param.style.textPosition = 'inside';
			param.style.text = text;
		}else{
			var text = aTime?aTime:' ';
			text += '\n';
			for(var i = 0; i < (width/5.7) ; i++)
				text += '\t';
			text += eTime?eTime:' ';
			param.style.text = text;
		}
		zr.add(new zrender.Rect(param));
		if(!isText){
			zr.add(new zrender.Line({
				shape:{
					x1:nodeBeginX+1,
					y1:nodeBeginY+height-1,
					x2:nodeBeginX+width-1,
					y2:nodeBeginY+1
				},
				style: {
					stroke: colorArray[1]
				}
			}));
		}
	}
	//画线
	var createLine = function(x1,y1,x2,y2,status){
		var colorArray=getColorByStatus(status);
		var line=new zrender.Line({
			shape:{
				x1:x1,
				y1:y1,
				x2:x2,
				y2:y2
			},
			style: {
		        stroke: colorArray[1],
		        lineWidth:2
		    }
		});
		zr.add(line);
		return line;
	}
	//画带箭头的线
	var createArrowLine = function(x1,y1,width,status){
		var colorArray=getColorByStatus(status);
		var param = {
				shape:{
					points:[[x1,y1],[x1+width,y1],[x1+width-5,y1-5]]
				},
				style: {
			        stroke: colorArray[1],
			        lineWidth:2,
			        textFill:'#ffffff',
			        textPosition:[width/2 - 2,-10]
			    }
			};
		/*if(eTime || aTime){
			var text = aTime?aTime:' ';
			text += '\n\n';
			text += eTime?eTime:' ';
			param.style.text = text;
		}*/
		var line=new zrender.Polyline(param);
		zr.add(line);
		return line;
	}
	//画侧折线
	var createBrokenLine = function(points,status){
		var colorArray=getColorByStatus(status);
		var line=new zrender.Polyline({
			shape:{
				points:points
			},
			style: {
		        stroke: colorArray[1],
		        lineWidth:2
		    }
		});
		zr.add(line);
		return line;
	}
	
	//画时间轴
	var createTimeLine = function(beginX , beginY , nodeLength){
		var x=beginX;
		var y=beginY+85;
		var width=nodeLength*options.step-20;
		var height=8;
		var timeLine=new zrender.Rect({
			shape:{
				r:[4],
				x:x,
				y:y,
				width:width,
				height:height
			},
			style: {
		        fill: '#213e85',
		        opacity:0.9,
		    }
		});
		return timeLine;
	}
	//画时间轴
	var createNowTimeLine = function(beginX,beginY,nowNodeIndex){
		var x=beginX;
		var y=beginY+82;
		var width=10;
		var height=14;
		var timeLine=new zrender.Rect({
			shape:{
				r:[8],
				x:x,
				y:y,
				width:width,
				height:height
			},
			style: {
		        fill: '#3057b9'
		    }
		});
		return timeLine;
	}
	//向时间轴上添加文字
	var addTextToTimeLine = function(){
		var width=50;
		var height=12;
		$.each(nodeTimeArray,function(k,v){
			var textRect=new zrender.Rect({
				shape:{
					x:startX + options.beginX+30+k*options.step,
					y:startY + options.beginY+83,
					width:width,
					height:height
				},
				style: {
			        fill: '#3057b9',
			        opacity:0.9,
			        text:v,
			        textFill:'#ffffff'
			    }
			});
			zr.add(textRect);	
		})
	}
	
	function draw(){
		// 流程节点分组
		var nodeGroups = options.nodeGroups;
		if(nodeGroups)
			$.each(nodeGroups,function(i,item){
				$.each(item.points,function(i,v){
					v[0] += startX;
					v[1] += startY;
				});
				createNodeGroup(item.points,item.id);
			});
    	//主流程节点
    	var mainNodes = options.mainNodes;
    	if(mainNodes)
    		$.each(mainNodes,function(i,item){
    			createNode(item.nodeText,startX + item.nodeBeginX,startY + item.nodeBeginY,item.width,item.height,item.arrowWidth,item.status,item.id);
    		});
    	//画时间轴
    	var timeLine=createTimeLine(startX + options.beginX, startY + options.beginY,mainNodes.length);
    	zr.add(timeLine);
    	// 取时间
    	for(var i = 0 ; i < mainNodes.length; i ++){
    		if(mainNodes[i].nodeTime){
    			nodeTimeArray.push(mainNodes[i].nodeTime);
    		}else{
    			break;
    		}
    	}
    	//当前时间轴
    	var nowTimeLine=createNowTimeLine(startX + options.beginX,startY + options.beginY,nodeTimeArray.length);
    	var finalWidth=nodeTimeArray.length*options.step-20;
    	//设置动画
    	nowTimeLine.animate('shape', false)
        					 .when(1500, {width: finalWidth})
       						 .start()
       						 .done(function(){
       						 	//动画完成时需要执行的动作
       						 	addTextToTimeLine(zr);
       						 });
    	zr.add(nowTimeLine);
    	
		//其他流程节点
		var otherNodes = options.otherNodes;
		if(otherNodes)
			$.each(otherNodes,function(i,item){
				createOtherNode(item.nodeText,item.outText,	startX + item.nodeBeginX,startY + item.nodeBeginY,item.width,item.height,item.status,item.id);
    		});
		//画线
		var lines = options.lines;
		if(lines)
			$.each(lines,function(i,item){
				createLine(startX + item.x1,startY + item.y1,startX + item.x2,startY + item.y2,item.status);
    		});
		//箭头线
		var arrowLine = options.arrowLine;
		if(arrowLine)
			$.each(arrowLine,function(i,item){
				createArrowLine(startX + item.x1,startY + item.y1,item.width,item.status);
    		});
		// 折线
		var brokenLine = options.brokenLine;
		if(brokenLine)
			$.each(brokenLine,function(i,item){
				$.each(item.points,function(i,v){
					v[0] += startX;
					v[1] += startY;
				});
				createBrokenLine(item.points,item.status);
			});
		//时间显示节点
		var timeNodes = options.timeNodes;
		if(timeNodes)
			$.each(timeNodes,function(i,item){
				createTimeNodes(item.aTime,item.eTime,	startX + item.nodeBeginX,startY + item.nodeBeginY,item.width,item.height,item.status,item.nodeText,item.isText);
    		});
	}
	
	//定义方法
	var methods = {
			// 初始化
	        init: function (obj) {
	        	options = jQuery.extend(options, obj);
	        	initParam();
	        	
	        	// 初始位置计算
	        	if(options.alignCenter == 1){
	        		startX = ($(window).width() - options.monitorWidth)/2;
	        		startX = startX <0 ? 0 : startX;
	        		startY = ($(window).height() - options.monitorHeight)/2;
	        		startY = startY <0 ? 0 : startY;
	        	}
        		this.width(options.monitorWidth + startX);
	        	this.height(options.monitorHeight +  startY);
	        	// 创建画布
	        	zr = zrender.init(this[0]);
	        	draw();
	        	
				return this;
	        },
	        getZrenderObject : function(){
	        	return zr;
	        },
	        getOptions:function(){
	        	return options;
	        }
	};
	$.fn.ProgressChart = function(method){
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