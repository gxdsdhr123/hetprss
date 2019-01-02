var serverName = document.location.hostname;
// var serverName = "localhost";
var port = 11111;

function getVideo(fType, code) {

    $.ajax({
        type : 'post',
        url : ctx + "/video/common/getServerInfo",
        data : {

            fType : fType,
            code : code
        },
        async : true,
        success : function(result) {

            var param = JSON.parse(result);
            sendMessage(dealMessage(param.serverName, param.serverPort, param.serverUser, param.serverPassword, param.avPath));
        },
        error:function(msg){

        }
    });
}

//处理发送内容
//参数userName 视频用户名
//参数ip IP地址
//参数password 密码
//参数avPath 视频路径
var dealMessage = function (ip, port, userName, password, avPath) {

    var sentMsg = "VIDEO@@";
    var msg = "";

    msg += userName + "@@";
    msg += password + "@@";
    msg += ip + "@@";
    msg += port + "@@";
    msg += avPath;

    var base = new Base64();
    sentMsg += base.encode(msg);

    return sentMsg;
}

//发送websocket消息
function sendMessage(msg) {

    if ("WebSocket" in window) {
        var ws = new WebSocket("ws://" + serverName + ":" + port);

        ws.onopen = function () {

            console.log("open");
            ws.send(msg);
        };

        ws.onmessage = function (evt) {

            console.log(evt.data)
        };

        ws.onclose = function (evt) {

            console.log("WebSocketClosed!");
        };

        ws.onerror = function (evt) {

            console.log("WebSocketError!");

            var html = "您还没有安装视频服务，请点击 <a class='talk-a' href='" + ctxStatic + "/prss/message/exe/PrssPluginSetup.msi' onclick='closeTalk()'>下载</a> 安装视频服务，如已下载请启动视频服务！";
            layui.use(["layer"], function () {
                layer.msg(html, {
                    time: 99999999999,
                    area: [$("body").width() + "px", "50px"],
                    offset: ["200px", "0"]
                });
            });
        };
    } else {
        layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
    }
}

function closeTalk(){
    layer.closeAll();
}

function Base64() {

    // private property
    var _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // public method for encoding
    this.encode = function (input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = _utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
                _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    }

    // public method for decoding
    this.decode = function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }

    // private method for UTF-8 encoding
    var _utf8_encode = function (string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }
        return utftext;
    }

    // private method for UTF-8 decoding
    var _utf8_decode = function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while ( i < utftext.length ) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}