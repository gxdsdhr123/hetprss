$(function(){
	keepGantt();
});
var ypool = {};
var pool = {};
var oriYWidth;//记录Y轴单列宽度
var basicY = 0;
var contentHeight = 0;
var dashedLineList = [];//记录未保存块与母块连线坐标
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
		},
		purple:{
			back:'#8500DD',
			inner:'#5A0686',
			border:'#AF00FF',
			color:'#D3E2FF'
		}
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
//默认设置
var settings = {
		zoom:4,//缩放级别
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
        	width:100,
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
oriYWidth = settings.yAxis.width;
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
var morning = new Date();
morning.setHours(6,0,0,0);
var center = 0;
var centerOffset = time2Coor(new Date().pattern("yyyy-MM-dd HH:mm"))*1 - time2Coor(morning.pattern("yyyy-MM-dd HH:mm"))*1;
center = 170+centerOffset;
var canvas = document.createElement("canvas");  
canvas.width = 3400; 
var cxt = canvas.getContext("2d");
var field = function(f){
	this.id = f.id;
	this.name = f.name;
	this.type = f.type==null?'':f.type;
	this.x = 0;
	this.y = 0;
	this.w = oriYWidth;
	this.h = settings.grid.lineHeight;
	this.list = [];
}
var overlay = function(d){
	this.id = d.id;
	this.pId = d.pId;
	this.type = d.type;
	this.fltDate = d.fltDate;
	this.inIssue = d.inIssueStand;
	this.outIssue = d.outIssueStand;
	this.stand = d.saveStand;
	this.tempStand = d.tempStand;
	this.inFltid = d.inFltid;
	this.outFltid = d.outFltid;
	this.inFltNum = d.inFltNum||"";
	this.outFltNum = d.outFltNum||"";
	this.actNum = d.actNum;
	this.aftType = d.actType;
	this.minHTime = d.minHTime;
	this.depart = d.depart;
	this.arrival = d.arrival;
	this.outGate = d.gate; 
	this.inFlag = d.inFlag;
	this.outFlag = d.outFlag;
	this.start = d.start==""?((d.end.substring(11,13)*1>=6?d.end.substring(0,10):(calcTime(new Date(d.end.replace(new RegExp(/-/gm) ,"/")),-60*24).substring(0,11)))+" 06:00:00"):d.start;
	this.rStart = d.rStart;
	this.sStart = d.sStart;
	this.end = d.end;
	this.rEnd = d.rEnd;
	this.sEnd = d.sEnd;
	this.lock = d.lock=='1'?true:false;
	this.needIssue = d.needIssue=='0'?false:true;
	this.status = d.status;
	this.color = d.status == "0"?colorPool.gray:d.status == "1"?colorPool.red:d.status == "2"?colorPool.purple:d.status == "3"?colorPool.green:colorPool.white;
	this.clash = false;
	this.x = time2Coor(this.start);
	this.x2 = this.end == ""?canvas.width:time2Coor(d.end);
	this.y = -50;
	this.w = this.x2 - this.x;
	this.h = 0.8*settings.grid.lineHeight|0;
	this.detail = [[(d.inFltNum||"--")+" "+(d.actType||"--")+" "+(d.outFltNum||"--"),(d.startDetail==""?"--":d.startDetail)+" "+(d.actNum||"--")+" "+(d.endDetail==""?"--":d.endDetail),(d.depart||"--")+"  "+(d.arrival||"--")]];
	this.page = 1;
	this.text = "";
	this.isChild = false;
	this.hasChild = false;
}
function getDate(){
	$.ajax({
		type:'post',
		url:ctx+"/flightdynamic/gantt/keepStandGanttYData",
		dataType:'json',
		success:function(ydata){
			settings.yData.data = ydata;
			for(var i=0;i<ydata.length;i++){
	    		var f = new field(ydata[i]);
	    		ypool[f.id] = f;
	        };
	        $.ajax({
	    		type:'post',
	    		url:ctx+"/flightdynamic/gantt/keepStandGanttData",
	    		dataType:'json',
	    		success:function(data){
	    			pool = {};
	    			for(var i in data){
	    				var o = new overlay(data[i]);
	    				if(ypool[o.stand]){//判断Y轴项是否存在
	    					if(!pool[o.stand]){//判断覆盖物池，该属性是否存在
	    						pool[o.stand] = [];
	    					}
	    					//合并航班号
	    					var text = "";
	    					if(!o.outFltNum){
	    						text = o.inFltNum+"/--";
	    					}else if(!o.inFltNum){
	    						text = "--/"+o.outFltNum;
	    					}else {
	    						if(o.outFltNum == o.inFltNum){
	    							text = o.inFltNum;
	    						}else{
	    							var inNum = o.inFltNum.split('');
	    							var outNum = o.outFltNum.split('');
	    							var index = 0;
	    							for(var i=0;i<inNum.length;i++){
	    								if(inNum[i] != outNum[i]){
	    									index = i;
	    									break;
	    								}
	    							}
	    							text = o.inFltNum+"/"+o.outFltNum.substring(index,o.outFltNum.length);
	    						}
	    					}
	    					if(o.outGate){
	    						text += " ("+o.outGate+")";
	    					}
	    					o.text = text;
	    					//定义未保存的临时块
	    					if(o.stand != o.tempStand){
	    						var child = {};
	    						for(var attr in o){
	    							child[attr] = o[attr];
	    						}
	    						child.stand = o.tempStand;
	    						child.isChild = true;
	    						child.oriStand = o.stand;
	    						if(!pool[child.stand]){
	    							pool[child.stand] = [];
	    						}
	    						pool[child.stand].push(child);
	    						o.hasChild = true;
	    					}
	    					if(o.type == "2"){
	    						pool[o.stand].splice(0,0,o);
	    					}else{
	    						pool[o.stand].push(o);
	    					}
	    				}
	    			}
	    			calcY();
	    		}
	    	});
		}
	});
}
function keepGantt(){
	$.ajax({
		type:'post',
		url:ctx+"/home/common/hasDynamicRole",
		success:function(res){
			if(res && res=="1"){
				setInterval(function(){
					getDate();
				},1000*60*30);
			}
		}
	});
}
function calcY(){
	contentHeight = 0;
	var tempY = basicY + settings.xAxis.height;//游标
	//待排
	ypool["DP"].y = tempY;
	ypool["DP"].h = settings.grid.lineHeight;
	calcExpand("DP");
	tempY += ypool["DP"].h;
	//取消
	ypool["QX"].y = tempY;
	ypool["QX"].h = settings.grid.lineHeight;
	calcExpand("QX");
	tempY += ypool["QX"].h;
	//机位
	for(var i in settings.yData.data){
		var f = settings.yData.data[i].id;
		if(f != "DP" && f != "QX"){
			ypool[f].y = tempY;
			ypool[f].h = settings.grid.lineHeight;
			calcExpand(f);
			tempY += ypool[f].h;
		}
	}
	contentHeight = tempY - basicY;
	canvas.height = contentHeight;
	$(canvas).css("height",canvas.height);
	$(canvas).css("width",canvas.width);
	drawGantt();
}
function calcExpand(f){
	ypool[f].list = [];
	for(var o in pool[f]){
		//计算覆盖物坐标及宽度
		pool[f][o].x = time2Coor(pool[f][o].start);
		pool[f][o].x2 = pool[f][o].end==""?canvas.width:time2Coor(pool[f][o].end);
		if(f != "QX" && pool[f][o].type == "0"){
			pool[f][o].reset = false;
			//如果进港航班没有实落，预落时间推到当前时间
			if((pool[f][o].status == "0" || pool[f][o].status == "1") && (!pool[f][o].rStart || pool[f][o].rStart == "")){
				if(pool[f][o].x < center){
					pool[f][o].x = center;
					pool[f][o].reset = true;
				}
			}
			//如果出港航班没有实起，起飞时间推至当前时间
			if(pool[f][o].status == "2" && (!pool[f][o].rEnd || pool[f][o].rEnd == "")){
				if(pool[f][o].x2 < center){
					pool[f][o].x2 = center;
					pool[f][o].reset = true;
				}
			}
			//保证占用时间至少为5分钟
			if(pool[f][o].x2 - pool[f][o].x < settings.xAxis.separation*pool[f][o].minHTime/zoomTime[settings.zoom]){
				pool[f][o].x2 = pool[f][o].x+settings.xAxis.separation*pool[f][o].minHTime/zoomTime[settings.zoom];
				pool[f][o].reset = true;
			}
		}else if(f == "QX"){
			if(pool[f][o].end == ""){
				pool[f][o].x2 = time2Coor(calcTime(new Date(pool[f][o].start),120));
			}
			if(pool[f][o].start == ""){
				pool[f][o].x = time2Coor(calcTime(new Date(pool[f][o].end),-120));
			}
		}
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
//绘制甘特图主方法
function drawGantt(){
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
			for(var o=0;o<pool[f].length;o++){
				//计算覆盖物坐标及宽度
				pool[f][o].x = time2Coor(pool[f][o].start);
				pool[f][o].x2 = pool[f][o].end==""?canvas.width:time2Coor(pool[f][o].end);
				if(f != "QX" && pool[f][o].type == "0"){
					pool[f][o].reset = false;
    				//如果进港航班没有实落，预落时间推到当前时间
    				if((pool[f][o].status == "0" || pool[f][o].status == "1") && (!pool[f][o].rStart || pool[f][o].rStart == "")){
    					if(pool[f][o].x < center){
    						pool[f][o].x = center;
    					}
    				}
    				//如果出港航班没有实起，起飞时间推至当前时间
    				if(pool[f][o].status == "2" && (!pool[f][o].rEnd || pool[f][o].rEnd == "")){
    					if(pool[f][o].x2 < center){
    						pool[f][o].x2 = center;
    					}
    				}
    				//保证占用时间至少为5分钟
    				var tempEnd = time2Coor(calcTime(new Date(coor2Time(pool[f][o].x)),pool[f][o].minHTime));
    				if(pool[f][o].x2 < tempEnd){
    					pool[f][o].x2 = tempEnd;
    					pool[f][o].reset = true;
    				}
				}else if(f == "QX"){
					if(pool[f][o].end == ""){
						pool[f][o].x2 = time2Coor(calcTime(new Date(pool[f][o].start),120));
					}
					if(pool[f][o].start == ""){
						pool[f][o].x = time2Coor(calcTime(new Date(pool[f][o].end),-120));
					}
				}
				pool[f][o].w = pool[f][o].x2 - pool[f][o].x;
				//判断覆盖物X轴方向是否在可视范围内
    			if(pool[f][o].x<=canvas.width && pool[f][o].x2>=0){
    				//判断时间是否存在错误
    				if(pool[f][o].x < pool[f][o].x2){
    					pool[f][o].clash = false;
    					//判断冲突
    					for(var k=0;k<pool[f].length;k++){
							if(pool[f][o]!=pool[f][k] && pool[f][o].x<pool[f][k].x2 && pool[f][k].x<pool[f][o].x2){//判断冲突
								if(pool[f][o].stand.indexOf("DP")==-1 && pool[f][o].stand.indexOf("QX")==-1){
									pool[f][o].clash = true;
    								pool[f][k].clash = true;
								}
							}
			    		}
    					//画覆盖物
    					drawOverlay(pool[f][o]);
    				}
    			}
    		}
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
	cxt.restore();
	//打印
	autoGenerateGantt();
}
function autoGenerateGantt(){
	var host="127.0.0.1";
	var port="7777";
	var imgStr = canvas.toDataURL().replace("data:image/png;base64,","");
	if ("WebSocket" in window){
		// 打开一个 web socket
		var url = "ws://" + host + ":" + port;
		var ws = new WebSocket(url);
		ws.onopen = function(){
			var request = "GANTT@@"+imgStr;
			ws.send(request);
		};
		ws.onmessage = function (evt) { 
			ws.close();
		};
		ws.onclose = function(){ };
	}else {
		layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
	}
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
    if(o.type == "0"){//正常航班
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
		if(o.lock){
			cxt.lineWidth = 2;
			//cxt.strokeStyle = "#DA4B25";
			var g1 = cxt.createLinearGradient(o.x-1,o.y, o.x2, o.y);
			g1.addColorStop(0, '#30204B'); 
			g1.addColorStop(0.5, '#D18056'); 
			g1.addColorStop(1, '#8DB290'); 
			cxt.strokeStyle = g1;
			cxt.strokeRect(o.x-1,o.y,o.w+1,o.h);
		}
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
		cxt.strokeStyle = o.clash?colorPool.yellow.border:(o.reset?"#db7c12":o.color.border);
		cxt.moveTo(o.x,o.y);
		cxt.lineTo(o.x+(o.w>100?60:o.w/2),o.y)
		cxt.stroke();
		if(o.reset || o.clash){
			cxt.lineWidth = 2;
			cxt.strokeRect(o.x+1,o.y+1,o.w-2,o.h-2);
		}
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
    }else if(o.type == "1"){//机位停用
    	cxt.fillStyle = "#207144";
		cxt.fillRect(o.x,ypool[o.stand].y,o.w,settings.grid.lineHeight);
		var start = new Date(o.start.replace(new RegExp(/-/gm) ,"/")).pattern("HHmm");
		var end = new Date(o.end.replace(new RegExp(/-/gm) ,"/")).pattern("HHmm");
		var text = "停用（"+start+"-"+end+")";
		var length = cxt.measureText(text).width;
		if(o.w>=length){
			cxt.textAlign = 'center';
			cxt.textBaseline = 'middle';
			cxt.fillStyle = "#FFFFFF";
			cxt.font = Math.ceil(0.4*o.h)+"px sans-serif";
			cxt.fillText(text,o.x+o.w/2,o.y+o.h/2);
		}
    }else if(o.type == "2"){//驻场
    	cxt.fillStyle = "#474749";
    	if(obj.isChild){
    		cxt.globalAlpha=0.5;
    	}
    	if(o.end == ""){
    		o.w = canvas.width - o.x;
    	}
		cxt.fillRect(o.x,o.y,o.w,settings.grid.lineHeight);
		var text = o.actNum+"(停场)";
		var length = cxt.measureText(text).width;
		if(o.w>=length+5){
			cxt.textAlign = 'left';
			cxt.textBaseline = 'middle';
			cxt.fillStyle = "#FFFFFF";
			cxt.font = Math.ceil(0.4*o.h)+"px sans-serif";
			if(o.x<oriYWidth){
				o.x = oriYWidth;
			}
			cxt.fillText(text,o.x+5,o.y+o.h/2);
		}
		if(o.isChild){
			for(var i=0;i<pool[o.oriStand].length;i++){
				if(pool[o.oriStand][i].id == o.id){
					var ori = pool[o.oriStand][i];
					if(o.y<ori.y){
						var line = {
								x1:o.x+(o.x2-o.x)/2,
								y1:o.y+o.h,
								x2:o.x+(o.x2-o.x)/2,
								y2:ori.y
						}
						dashedLineList.push(line);
					}else{
						var line = {
								x1:o.x+(o.x2-o.x)/2,
								y1:o.y,
								x2:o.x+(o.x2-o.x)/2,
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
								x1:o.x+(o.x2-o.x)/2,
								y1:o.y+o.h,
								x2:o.x+(o.x2-o.x)/2,
								y2:child.y
						}
						dashedLineList.push(line);
					}else{
						var line = {
								x1:o.x+(o.x2-o.x)/2,
								y1:o.y,
								x2:o.x+(o.x2-o.x)/2,
								y2:child.y+child.h
						}
						dashedLineList.push(line);
					}
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
		if(i == "DP" || i == "QX"){
			cxt.font = settings.yAxis.fontSize+"px sans-serif";
			cxt.fillText(f.name,oriYWidth/2-10,(f.y+settings.grid.lineHeight/2+2));
		}else{
			cxt.font = settings.yAxis.fontSize+"px sans-serif";
			cxt.fillText(f.name,oriYWidth/2-10,(f.y+settings.grid.lineHeight/2+2));
			cxt.fillText(f.type,oriYWidth-10,(f.y+settings.grid.lineHeight/2+2));
		}
		
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
//补零
var zeroize = function(value,length){
	value = value+"";
	for(var i=0;i<(length-value.length);i++){
		value = "0"+value;
	}
	return value;
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
