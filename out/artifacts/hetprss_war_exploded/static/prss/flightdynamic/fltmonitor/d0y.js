var createFltmonitor = function (dataObj){
	var nodeTextArray=['开始','客梯车靠接','清洁完成','登机开始','登机结束','关客舱门','客梯车撤离','撤轮档','起飞'];
	var idArray = [null,4,5,21,22,15,6,7,8];
	var beginX=0;
	var beginY=230;
	var step=110;
	
	var options = {
			beginX : beginX,
			beginY : beginY,
			step : step,
			monitorWidth:1260,
			monitorHeight:700
	};
	//主流程参数
	options.mainNodes = [];
	$.each(nodeTextArray,function(k,v){
		options.mainNodes.push({
			 id: idArray[k]?'node_out_' + idArray[k]:null,
			 keyId:'mainNode_' + k,
           	 nodeText:v,
           	 //nodeTime:nodeTimeArray[k]?nodeTimeArray[k]:'',
        	 nodeBeginX:beginX+step*k,
        	 nodeBeginY:beginY,
        	 //status:nodeStatusArray[k] 
        	 width:90
         });
	});
	// 其他节点
	var otherNodeBeginY=beginY+140;
	var otherNodeSpacing=60;
	var topOtherNodePositionY = -120;
	options.otherNodes = [
	                      // 下边节点
	                      {keyId:'otherNode_0',id:'node_out_9',nodeText:'航线放行',outText:'',nodeBeginX:beginX+4*step,nodeBeginY:otherNodeBeginY},
	                      {keyId:'otherNode_1',id:'node_out_10',nodeText:'清水车完成',outText:'',nodeBeginX:beginX+4*step,nodeBeginY:otherNodeBeginY+otherNodeSpacing},
	                      {keyId:'otherNode_2',id:'node_out_11',nodeText:'污水车完成',outText:'',nodeBeginX:beginX+4*step,nodeBeginY:otherNodeBeginY+otherNodeSpacing*2},
	                      {keyId:'otherNode_3',id:'node_out_12',nodeText:'加油完成' , nodeBeginX:beginX+4*step , nodeBeginY:otherNodeBeginY+otherNodeSpacing*3 },
	                      {keyId:'otherNode_4',id:'node_out_13',nodeText:'配餐完成' , nodeBeginX:beginX+4*step , nodeBeginY:otherNodeBeginY+otherNodeSpacing*4 },
	                      // 上边节点
	                      {keyId:'otherNode_5',id:'node_out_16',nodeText:'货舱开门',outText:'',nodeBeginX:beginX+2.5*step+20,nodeBeginY:beginY+topOtherNodePositionY,width:200},
	                      {keyId:'otherNode_6',id:'node_out_17',nodeText:'货舱关门',outText:'',nodeBeginX:beginX+5.5*step,nodeBeginY:beginY+topOtherNodePositionY,width:200},
	                      {keyId:'otherNode_7',id:'node_out_19',nodeText:'出港摆渡车完成',outText:'',nodeBeginX:beginX+4*step,nodeBeginY:beginY+topOtherNodePositionY-80,width:200}
                      ]
	// 画线
	options.lines = [
	                 	//左竖线
	                 	{x1:beginX+step*0.5,y1:beginY+94,x2:beginX+step*0.5,y2:otherNodeBeginY+otherNodeSpacing*4+20},
	                 	// 右竖线
	                 	{x1:beginX+step*8.5,y1:beginY+94,x2:beginX+step*8.5,y2:otherNodeBeginY+otherNodeSpacing*4+20},
	                 	//上侧左侧线
	                 	{x1:beginX+step*1.5,y1:beginY,x2:beginX+step*1.5,y2:beginY+topOtherNodePositionY-60},
	                 	//上侧右侧线
	                 	{x1:beginX+step*8.5,y1:beginY,x2:beginX+step*8.5,y2:beginY+topOtherNodePositionY-60}
	                 ]
	// 箭头线
	options.arrowLine = [
	                     	//下左侧带箭头的横线
	                     	{x1:beginX+step*0.5,y1:otherNodeBeginY+20,width:step*3.5-2},
	                     	{x1:beginX+step*0.5,y1:otherNodeBeginY+20+otherNodeSpacing,width:step*3.5-2},
	                     	{x1:beginX+step*0.5,y1:otherNodeBeginY+20+otherNodeSpacing*2,width:step*3.5-2},
	                     	{x1:beginX+step*0.5,y1:otherNodeBeginY+20+otherNodeSpacing*3,width:step*3.5-2},
	                     	{x1:beginX+step*0.5,y1:otherNodeBeginY+20+otherNodeSpacing*4,width:step*3.5-2},
	                     	//下右侧带箭头的横线
	                     	{x1:beginX+step*4+231,y1:otherNodeBeginY+20,width:step*2+40},
	                     	{x1:beginX+step*4+231,y1:otherNodeBeginY+20+otherNodeSpacing*1,width:step*2+40},
	                     	{x1:beginX+step*4+231,y1:otherNodeBeginY+20+otherNodeSpacing*2,width:step*2+40},
	                     	{x1:beginX+step*4+231,y1:otherNodeBeginY+20+otherNodeSpacing*3,width:step*2+40},
	                     	{x1:beginX+step*4+231,y1:otherNodeBeginY+20+otherNodeSpacing*4,width:step*2+40},
	                     	//上侧箭头线
	                     	{x1:beginX+step*1.5,y1:beginY+topOtherNodePositionY+20,width:130},
	                     	{x1:beginX+4.5*step,y1:beginY+topOtherNodePositionY+20,width:110},
	                     	{x1:beginX+7.5*step-20,y1:beginY+topOtherNodePositionY+20,width:130},
	                     	{x1:beginX+step*1.5,y1:beginY+topOtherNodePositionY-60,width:270},
	                     	{x1:beginX+step*6-20,y1:beginY+topOtherNodePositionY-60,width:295}
	                     	
	                     ]
	
	
	// 时间显示
	options.timeNodes = [
	                     // 主流程
	                     {keyId:'mainNode_1',aTime:'',eTime:'',nodeBeginX:beginX+70,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_2',aTime:'',eTime:'',nodeBeginX:beginX+70+step*1,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_3',aTime:'',eTime:'',nodeBeginX:beginX+70+step*2,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_4',aTime:'',eTime:'',nodeBeginX:beginX+70+step*3,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_5',aTime:'',eTime:'',nodeBeginX:beginX+70+step*4,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_6',aTime:'',eTime:'',nodeBeginX:beginX+70+step*5,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_7',aTime:'',eTime:'',nodeBeginX:beginX+70+step*6,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_8',aTime:'',eTime:'',nodeBeginX:beginX+70+step*7,nodeBeginY:beginY-40,width:80,isText:true},
	                     
	                      // 下边时间
	                      {keyId:'otherNode_0',aTime:'',eTime:'',nodeBeginX:beginX+step*2.5,nodeBeginY:otherNodeBeginY-20},
	                      {keyId:'otherNode_1',aTime:'',eTime:'',nodeBeginX:beginX+step*2.5,nodeBeginY:otherNodeBeginY+otherNodeSpacing-20},
	                      {keyId:'otherNode_2',aTime:'',eTime:'',nodeBeginX:beginX+step*2.5,nodeBeginY:otherNodeBeginY+otherNodeSpacing*2-20},
	                      {keyId:'otherNode_3',aTime:'',eTime:'',nodeBeginX:beginX+step*2.5,nodeBeginY:otherNodeBeginY+otherNodeSpacing*3-20},
	                      {keyId:'otherNode_4',aTime:'',eTime:'',nodeBeginX:beginX+step*2.5,nodeBeginY:otherNodeBeginY+otherNodeSpacing*4-20},
	                      
	                   // 上边时间
	                      {keyId:'otherNode_5',aTime:'',eTime:'',nodeBeginX:beginX+90+step*1,nodeBeginY:beginY+topOtherNodePositionY-20},
	                      {keyId:'otherNode_6',aTime:'',eTime:'',nodeBeginX:beginX+90+step*4,nodeBeginY:beginY+topOtherNodePositionY-20},
	                      {keyId:'otherNode_7',aTime:'',eTime:'',nodeBeginX:beginX+90+step*2,nodeBeginY:beginY+topOtherNodePositionY-100}
                      ]
	
	
	// 流程节点分组
	options.nodeGroups = [
	                      	{id:'group_23',points:[[beginX+1*step-20,beginY-50],[beginX+1*step-20,beginY+120],[beginX+9*step+10,beginY+120],[beginX+9*step+10,beginY-50]]}
	                      	
	                  ]
	
	// =========================== 数据填充 ==============================
	// 时间显示回填
	var timeDatas = dataObj.timeDatas;
	var mainDatas = dataObj.mainDatas;
	var otherDatas = dataObj.otherDatas;
	
	if(timeDatas)
		$.each(options.timeNodes,function(k,v){
			if(v.keyId){
				var d = timeDatas[v.keyId];
				if(d){
					v.nodeText = d.nodeText;
					v.aTime = d.aTime;
					v.eTime = d.eTime;
					v.status = d.status;
				}
			}
		});
	
	if(mainDatas)
		$.each(options.mainNodes,function(k,v){
			if(v.keyId){
				var d = mainDatas[v.keyId];
				if(d){
					v.nodeTime = d.nodeTime;
					v.status = d.status;
				}
			}
		});
	
	if(otherDatas)
		$.each(options.otherNodes,function(k,v){
			if(v.keyId){
				var d = otherDatas[v.keyId];
				if(d){
					v.outText = d.nodeTime;
					if(d.status)
						v.status = d.status;
				}
			}
		});
	
	return $('#monitor').ProgressChart(options);
	
	
};