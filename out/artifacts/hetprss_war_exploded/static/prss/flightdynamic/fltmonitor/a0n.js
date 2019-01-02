var createFltmonitor = function (dataObj){
	var nodeTextArray=['起飞','落地','挡轮档','廊桥靠接','开客舱门','下客完成','清洁完成','廊桥撤离','结束'];
	var idArray = [1,2,3,4,14,20,5,6,null];
//	var nodeTimeArray=[];
//	var nodeStatusArray=[];
	var beginX=0;
	var beginY=150;
	var step=140;
	var otherNodewidth=230;
	
	var options = {
			beginX : beginX,
			beginY : beginY,
			step : step,
			monitorWidth:1260,
			monitorHeight:620
	};
	//主流程参数
	options.mainNodes = [];
	$.each(nodeTextArray,function(k,v){
		options.mainNodes.push({
			 id: idArray[k]?'node_in_' + idArray[k]:null,
			 keyId:'mainNode_' + k,
           	 nodeText:v,
           	 //nodeTime:nodeTimeArray[k]?nodeTimeArray[k]:'',
        	 nodeBeginX:beginX+step*k,
        	 nodeBeginY:beginY
        	 //status:nodeStatusArray[k] 
         });
	});
	// 其他节点
	var otherNodeBeginY=beginY+140;
	var otherNodeSpacing=60;
	var topOtherNodeSpacing=230+30;
	var topOtherNodePositionY = -120;
	options.otherNodes = [
	                      // 下边节点
	                      {keyId:'otherNode_0',id:'node_in_9',nodeText:'航线放行',outText:'',nodeBeginX:beginX+4*step,nodeBeginY:otherNodeBeginY},
	                      {keyId:'otherNode_1',id:'node_in_10',nodeText:'清水车完成',outText:'',nodeBeginX:beginX+4*step,nodeBeginY:otherNodeBeginY+otherNodeSpacing},
	                      {keyId:'otherNode_2',id:'node_in_11',nodeText:'污水车完成',outText:'',nodeBeginX:beginX+4*step,nodeBeginY:otherNodeBeginY+otherNodeSpacing*2},
	                      {keyId:'otherNode_3',id:'node_in_12',nodeText:'加油完成' , outText:'' , nodeBeginX:beginX+4*step , nodeBeginY:otherNodeBeginY+otherNodeSpacing*3 },
	                      {keyId:'otherNode_4',id:'node_in_13',nodeText:'配餐完成' , outText:'' , nodeBeginX:beginX+4*step , nodeBeginY:otherNodeBeginY+otherNodeSpacing*4 },
	                      // 上边节点
	                      {keyId:'otherNode_5',id:'node_in_16',nodeText:'货舱开门',outText:'',nodeBeginX:beginX+3*step+20,nodeBeginY:beginY+topOtherNodePositionY,width:200},
	                      {keyId:'otherNode_6',id:'node_in_17',nodeText:'货舱关门',outText:'',nodeBeginX:beginX+5.5*step,nodeBeginY:beginY+topOtherNodePositionY,width:200}
                      ]
	// 画线
	options.lines = [
	                 	//左竖线
	                 	{x1:beginX+step*1.5,y1:beginY+94,x2:beginX+step*1.5,y2:otherNodeBeginY+otherNodeSpacing*4+20},
	                 	// 右竖线
	                 	{x1:beginX+step*8.5,y1:beginY+94,x2:beginX+step*8.5,y2:otherNodeBeginY+otherNodeSpacing*4+20},
	                 	//上侧左侧线
	                 	{x1:beginX+step*2.5,y1:beginY,x2:beginX+step*2.5,y2:beginY+topOtherNodePositionY+20},
	                 	//上侧右侧线
	                 	{x1:beginX+step*8.5,y1:beginY,x2:beginX+step*8.5,y2:beginY+topOtherNodePositionY+20}
	                 ]
	// 箭头线
	options.arrowLine = [
	                     	//左侧带箭头的横线
	                     	{x1:beginX+step*1.5,y1:otherNodeBeginY+20,width:step*2.5-2},
	                     	{x1:beginX+step*1.5,y1:otherNodeBeginY+20+otherNodeSpacing,width:step*2.5-2},
	                     	{x1:beginX+step*1.5,y1:otherNodeBeginY+20+otherNodeSpacing*2,width:step*2.5-2},
	                     	{x1:beginX+step*1.5,y1:otherNodeBeginY+20+otherNodeSpacing*3,width:step*2.5-2},
	                     	{x1:beginX+step*1.5,y1:otherNodeBeginY+20+otherNodeSpacing*4,width:step*2.5-2},
	                     	//右侧带箭头的横线
	                     	{x1:beginX+step*4+235,y1:otherNodeBeginY+20,width:step*2.5+40},
	                     	{x1:beginX+step*4+235,y1:otherNodeBeginY+20+otherNodeSpacing*1,width:step*2.5+40},
	                     	{x1:beginX+step*4+235,y1:otherNodeBeginY+20+otherNodeSpacing*2,width:step*2.5+40},
	                     	{x1:beginX+step*4+235,y1:otherNodeBeginY+20+otherNodeSpacing*3,width:step*2.5+40},
	                     	{x1:beginX+step*4+235,y1:otherNodeBeginY+20+otherNodeSpacing*4,width:step*2.5+40},
	                     	//上侧箭头线
	                     	{x1:beginX+step*2.5,y1:beginY+topOtherNodePositionY+20,width:90},
	                     	{x1:beginX+4.5*step+10,y1:beginY+topOtherNodePositionY+20,width:130},
	                     	{x1:beginX+7*step-10,y1:beginY+topOtherNodePositionY+20,width:220}
	                     ]
	
	// 上折线
	/*options.brokenLine = [];
	var x1 = beginX+2.5*step , y1 = beginY+topOtherNodePositionY+80;
	var points1 = [[x1,beginY],[x1,y1-60],[x1-2.5*step,y1-60],[x1-2.5*step,y1-100],[x1-2*step,y1-100]];
	options.brokenLine.push({points:points1 , status:1});*/
	
	// 时间显示
	options.timeNodes = [
	                     // 主流程
	                     {keyId:'mainNode_1',aTime:'',eTime:'',nodeBeginX:beginX+90,nodeBeginY:beginY-40,width:80,isText:true},
	                     {keyId:'mainNode_2',aTime:'',eTime:'',nodeBeginX:beginX+90+step*1,nodeBeginY:beginY-40,width:80,isText:true},
	                     {keyId:'mainNode_3',aTime:'',eTime:'',nodeBeginX:beginX+90+step*2,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_4',aTime:'',eTime:'',nodeBeginX:beginX+90+step*3,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_5',aTime:'',eTime:'',nodeBeginX:beginX+90+step*4,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_6',aTime:'',eTime:'',nodeBeginX:beginX+90+step*5,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_7',aTime:'',eTime:'',nodeBeginX:beginX+90+step*6,nodeBeginY:beginY-40},
	                     {keyId:'mainNode_8',aTime:'',eTime:'',nodeBeginX:beginX+90+step*7,nodeBeginY:beginY-40},
	                     
	                      // 下边时间
	                      {keyId:'otherNode_0',aTime:'',eTime:'',nodeBeginX:beginX+400,nodeBeginY:otherNodeBeginY-20},
	                      {keyId:'otherNode_1',aTime:'',eTime:'',nodeBeginX:beginX+400,nodeBeginY:otherNodeBeginY+otherNodeSpacing-20},
	                      {keyId:'otherNode_2',aTime:'',eTime:'',nodeBeginX:beginX+400,nodeBeginY:otherNodeBeginY+otherNodeSpacing*2-20},
	                      {keyId:'otherNode_3',aTime:'',eTime:'',nodeBeginX:beginX+400,nodeBeginY:otherNodeBeginY+otherNodeSpacing*3-20},
	                      {keyId:'otherNode_4',aTime:'',eTime:'',nodeBeginX:beginX+400,nodeBeginY:otherNodeBeginY+otherNodeSpacing*4-20},
	                      // 上边时间
	                      {keyId:'otherNode_5',aTime:'',eTime:'',nodeBeginX:beginX+90+step*2,nodeBeginY:beginY+topOtherNodePositionY-20},
	                      {keyId:'otherNode_6',aTime:'',eTime:'',nodeBeginX:beginX+90+step*4,nodeBeginY:beginY+topOtherNodePositionY-20}
                      ]
	
	// 流程节点分组
	options.nodeGroups = [
	                      	{id:'group_23',points:[[beginX+3*step-20,beginY-50],[beginX+3*step-20,beginY+120],[beginX+8*step,beginY+120],[beginX+8*step,beginY-50]]}
	                      	/*{id:'group_31',points:[[beginX+0.5*step-10,beginY+topOtherNodePositionY+50],[beginX+0.5*step-10,beginY+topOtherNodePositionY-45],
	                      	                          [beginX+4*step+20,beginY+topOtherNodePositionY-45],[beginX+4*step+20,beginY+topOtherNodePositionY-15],
	                      	                          [beginX+8*step,beginY+topOtherNodePositionY-15],[beginX+8*step,beginY+topOtherNodePositionY+50]]},
	                      	{id:'group_30',points:[[beginX+0.5*step-10,beginY+topOtherNodePositionY+10],[beginX+0.5*step-10,beginY+topOtherNodePositionY-90],
	                      	                          [beginX+8*step,beginY+topOtherNodePositionY-90],[beginX+8*step,beginY+topOtherNodePositionY - 25],
	                      	                          [beginX+4*step+20,beginY+topOtherNodePositionY-25],[beginX+4*step+20,beginY+topOtherNodePositionY+10]
	                      	                          ]}*/
	                      	
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