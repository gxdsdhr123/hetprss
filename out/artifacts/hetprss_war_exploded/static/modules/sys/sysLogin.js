var layer = null;
layui.use(["layer"],function(){
	layer = layui.layer;
});
$(document).ready(function() {
	//$("#username").focus();
	$("#loginForm").validate({
		rules: {
			validateCode: {remote: path+"/servlet/validateCodeServlet"}
		},
		messages: {
			username: {required: "请填写用户名."},
			password: {required: "请填写密码."},
			validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
		},
		errorPlacement: function(error, element) {
			var pos = 2;
			if(element.attr("name")=="validateCode"){
				pos = 1;
			}
			layer.tips(error.html(), element,{tipsMore: true,tips:[pos,"#f39c12"]});
			return false;
		}
	});
	filterTalk();
	//匿名登录
	$("#anonymousBtn").click(function(){
		$("#username").attr("type","password");
		$("#username").val("anonymous");
		$("#password").val("1234");
		$("#loginForm").submit();
	});
});

function filterTalk(){
	if ("WebSocket" in window) {
		// 打开一个 web socket
		var url = "ws://" + serverIp + ":" + port;
		var ws = new WebSocket(url);
		ws.onerror = function() {
			var html = "您还没有安装通讯服务，请点击 <a class='talk-a' href='" + ctxStatic + "/prss/message/exe/ImaPhoneServiceInstall.msi' onclick='closeTalk()'>下载</a> 安装通讯服务，如已下载请启动通讯服务！";
			layui.use([ "layer" ], function() {
				layer.msg(html, {
					time : 99999999999,
					area : [ $("body").width() + "px", "50px" ],
					offset : 't'
				});
			});
		};
	} else {
		layer.alert("您的浏览器不支持通信！", {
			icon : 0,
			time : 1000
		});
	}
}

function closeTalk(){
	layer.closeAll();
}
// 如果在框架或在对话框中，则弹出提示并跳转到首页
if(self.frameElement && self.frameElement.tagName == "IFRAME"){
	alert('未登录或登录超时。请重新登录，谢谢！');
	top.location = "${ctx}";
}