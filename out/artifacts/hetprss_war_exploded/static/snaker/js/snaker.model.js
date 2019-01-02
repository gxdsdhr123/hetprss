(function($){
var snakerflow = $.snakerflow;

$.extend(true,snakerflow.config.rect,{
	attr : {
	r : 8,
	fill : '#092955',
	stroke : '#03689A',
	"stroke-width" : 2
}
});

$.extend(true,snakerflow.config.props.props,{
	name : {},
	displayName : {},
	expireTime : {},
	instanceUrl : {},
	instanceNoClass : {},
	releaseAlarmMsg:{},
	surrogateAlarmMsg:{},
	terminationAlarmMsg:{},
	startMsg:{},
	cancelMsg:{},
	removeMsg:{},
	jobKindId:{},
	jobTypeId:{}
});


$.extend(true,snakerflow.config.tools.states,{
			start : {
				showType: 'image',
				type : "start",
				name : {text:'<<start>>'},
				text : {text:'start'},
				img : {src : 'images/48/start_event_empty.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					id : {value : 'start'},
					type : {value : 'start'},
					name: {value:'start'}
				}},
			end : {
				showType: 'image',
				type : 'end',
				name : {text:'<<end>>'},
				text : {text:'end'},
				img : {src : 'images/48/end_event_terminate.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					id : {value : 'end'},
					type : {value : 'end'},
					name: {value:'end'},
					label: {value:'3'}
				}},
			task : {
				/*id:{id:''},*/
				showType: 'text',
				type : 'task',
				name : {text:'<<task>>'},
				text : {text:''},
				img : {src : 'images/48/task_empty.png',width :48, height:48},
				props : {
					nodeId: {value:''},
					name: {value:''},
					label: {value:''},
					jobKind: {value:''},
					jobType: {value:''},
					aftMsg1: {value:''},
					aftMsg2: {value:''},
					notifyMsg: {value:''},
					alarmMsgLv1: {value:''},
					alarmMsgLv2: {value:''},
					
					alarmMsgLv3: {value:''},
					alarmMsgLv4: {value:''},
					alarmMsgLv5: {value:''},
					notifyTm: {value:''},
					alarmTm1: {value:''},
					
					alarmTm2: {value:''},
					alarmTm3: {value:''},
					alarmTm4: {value:''},
					alarmTm5: {value:''},
					icon: {value:''}

				}}
		
});
})(jQuery);