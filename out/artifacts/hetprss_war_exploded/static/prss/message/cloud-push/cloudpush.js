var CloudPush = function(){
	var LONG_POLLING_TIME = 20000;
	var HEARTBEAT_TIME = 20000;
	
	var heartbeat_timer = 0;
	var health_timeout = 20000;
	var health_timeout_chk = 30000;
	var last_health = -1;
	
	var ws;

	var error = false;
	var btnConnection = undefined;

	var isWebsocket = function() {
		if (!window.WebSocket) {
			return false;
		} else {
			return true;
		}
	};
	
	var jsonpCallback = function(data) // 回调函数
	{
		var b = isWebsocket();
		var code = data.COD;
		if (code == 2000) {
			if (b) {
				go(data.ANE, data.DT, data.VC);
			} else {
				showLongPolling();
				setTimeout("CloudPush.delayclick()", LONG_POLLING_TIME);
			}
			showMessage(data,b);
		} else {
			alert("login error COD=" + code);
		}
	};
	//用户自定义函数，显示消息
	function showError() {
		console.info("服务器没有响应.");
		CloudPush.initConfig(0, 0, initCP())
	};

	function showOpen() {
		//console.info("showOpen");
	};
	
	function reConnet() {
		if(error==true){
			ws.close();
			initCP();
		}
	};

	function showClose() {
		ws.close();
		console.info("showClose");
	};

	function showLongPolling() {
		console.info("showLongPolling");
	};
	//解析数据 json格式
	function showMessage(data, isFirst) {
		if (isFirst) {
			$.post(ctx +'/message/common/getOfflineCount', function(data) {
				var num = $(".messageWarn").text();
				if(num==null || num =='') num ="0";
				var count = parseInt(num) + parseInt(data);
				if(count>0){
					$(".messageWarn").text(count);
					$(".messageWarn").css("display","block");
				}
			}).error(function(result){
				console.info(result)
			});
			return;
		}
		var msglist = data.msgs;
		if (msglist == undefined) {
			//根据是否语音播放状态  进行语音播放
			var msg2 = eval("(" + data + ")");
			createMessage(msg2);
		} else {
			for(var i=0;i<msglist.length;i++){
				var msg = eval(msglist[i]);
				var msgId = msg.MsgId + '';
				createMessage(msg);
			}
		}
	};
	
	var createMessage =function(msg){
		var cntt = msg.CNTT;
		if(msg.ok == 1){
			return;
		}
		var cntt2=JSON.parse(cntt); 
		var ifpopup = cntt2.IFPOPUP;
		if(ifpopup==1){
			//询问框
			createPopUp(cntt2);
		} 

		var num = $(".messageWarn").text();
		if(num==null || num =='') num ="0";
		var count = parseInt(num) + 1;
		$(".messageWarn").text(count);
		$(".messageWarn").css("display","block");
		
		if(_index==null || _index==0){
			$(".layui-fixbar").css("display","block");
			$(".layui-fixbar").find("li[lay-type='bar1']").css("display","block");
		} else {
			var frameId=document.getElementById('messageSend').getElementsByTagName("iframe")[0].id;
		    $('#'+frameId)[0].contentWindow.createUnreadMessage(cntt2);
		    $('#'+frameId)[0].contentWindow.initUnReadNum();
		}
		removemsgs(cntt2.ID,cntt2.MTOID);
		var ifsound = cntt2.IFSOUND;
		if(ifsound=="1"){
			var soundtxt = cntt2.SOUNDTXT;
			play(soundtxt,0);
		}
		
	}
	function removemsgs(mid,mtoid){
		$.post(ctx +'/message/common/removemsgs?mid=' + mid + '&mtoid=' + mtoid , function(data) {
			
		}).error(function(result){
			console.info(result)
		});
	}
	var keepalive = function (  ){
		var time = new Date();
		if( last_health != -1 && ( time.getTime() - last_health > health_timeout_chk ) ){
			//此时即可以认为连接断开，可是设置重连或者关闭
			showError();
		} else if(error==true){
			reConnet();
			if( ws.bufferedAmount == 0 ){
				ws.send( 'B' );
			}
		}
	}
	
	var go = function(url, uid, regcode) {
		ws = new WebSocket(url + "/" + uid + "/" + regcode);
		clearInterval( heartbeat_timer );
		ws.onopen = function() {
			showOpen();
			error = false;
			heartbeat_timer = setInterval( function(){ reConnet() }, health_timeout );
		}
		ws.onclose = function() {
			clearInterval( heartbeat_timer );
			showClose();
		}
		ws.onmessage = function(e) {
			showMessage(e.data, false);
			var msg = eval("(" + e.data + ")");
			ws.send( 'msgid:' + msg.MsgId );
		}
		ws.onerror = function(evt) { 
			console.log('error:'+evt); 
			error = true;
			heartbeat_timer = setInterval( function(){ reConnet() }, health_timeout );
		};
	};
	var delayclick = function() {
		//重新连接
//		console.info("Websocket RECONNECT")
	};
	return{
		initConfig : function(pollingtime, heartbeattime, btnConn) {
			if (pollingtime > 0) {
				LONG_POLLING_TIME = pollingtime;
			}

			if (heartbeattime > 0) {
				HEARTBEAT_TIME = heartbeattime;
			}
			
			btnConnection = btnConn;
		},
		jsonpCallback : function(data) // 回调函数
		{
			return jsonpCallback(data);
		},
		delayclick : function() {
			return delayclick();
		},
		connect : function(server, appid, uid, callback) {
			$.ajax({
				type : "get",
				async : true,
				url : server + "/rest/anc/ajaxlogin" + "/" + appid + "/" + uid
						+ "/jsonp/3",	//3:ostype=web
				cache : false,
				timeout : 20000,
				dataType : "jsonp",
//				jsonpCallback : "CloudPush.jsonpCallback",
				success : function(result) {
//					console.info("注册CP成功");
					CloudPush.jsonpCallback(result);
				},
				error : function(data) {
					var status = data.status;
					if(status!=200){
						console.info("注册CP失败");
//						alert("推送系统注册失败,您将不能接受消息,请联系管理员");
					}
				}
			});
		}
	}
}();