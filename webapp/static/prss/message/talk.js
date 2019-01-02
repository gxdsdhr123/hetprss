
function talk(mtoer,mtotype){
	$.post(ctx + '/message/common/talk',{mtoer : mtoer ,mtotype : mtotype}, function(data) {
		if ("WebSocket" in window)
		{
			// 打开一个 web socket
			var url = "ws://" + data.host + ":" + data.port;
			var ws = new WebSocket(url);
			ws.onopen = function()
			{
				var request = "MAKECALL:REQUEST:AUDIO:" + data.phones;
//				var request = "BROADCAST:REQUEST:" + phones;
				ws.send(request);
			};
			ws.onmessage = function (evt) 
			{ 
				var received_msg = evt.data;
				layer.open({
					type:2,
					title:'通讯',
					area: ['300px', '370px'],
					content:ctx + '/message/common/totalk?phones='+data.phones+'&names='+encodeURIComponent(encodeURIComponent(data.names))
				})
				ws.close();
			};
			ws.onclose = function()
			{ 
			};
		}  else {
			layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
		}
	}).success(function() {

	}).error(function() {

	});

}

function stop(obj,phone){
		if ("WebSocket" in window)
		{
			// 打开一个 web socket
			var url = "ws://" + $("#ip").val() + ":" + $("#port").val();
			console.info(url)
			var ws = new WebSocket(url);
			ws.onopen = function()
			{
				var request = "HANGUP:REQUEST:" + phone;
				ws.send(request);
			};
			ws.onmessage = function (evt) 
			{ 
				var received_msg = evt.data;
				$(obj).remove();
			};
			ws.onclose = function()
			{ 
				var html = "通讯服务没有启动，请启动通讯服务！";
				layer.alert(html,{icon:0});
			};
		}  else {
			layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
		}

}