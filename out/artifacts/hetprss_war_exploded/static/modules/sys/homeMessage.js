var _index;
layui.use('util', function() {
	var util = layui.util;
	util.fixbar({
		bar1 : true,
		css : {
			right : 0,
			bottom : 20,
			'z-index' : '9999999999999',
			display : 'none'
		},
		bgcolor : '#393D49',
		showHeight : 200,
		click : function(type) {
			if (type === 'bar1') {
				openMess();
			} else if (type === 'bar2') {
				layer.msg('两个bar都可以设定是否开启')
			}
		}
	});
});

function initCP() {
	CloudPush.connect(server, appkey, uid);
	return false;
}

var _button = '<div id="toolbar">';
_button += '<button id="refresh" onclick="refresh();" class="btn btn-link" type="button">刷新</button>';
// _button += '<button id="send" onclick="alert(9);" class="btn btn-link"
// type="button">发消息</button>';
_button += '<button onclick="showMessage(1)" class="btn btn-link" type="button">已发消息</button>';
_button += '<button onclick="showMessage(0)" class="btn btn-link" type="button">已收消息</button>';
_button += '<button onclick="showMessage(3)" class="btn btn-link" type="button">已收藏消息</button>';
_button += '<button id="sche" onclick="sche()" class="btn btn-link" type="button">订阅</button>';
// _button += '<a href="'+ctx+'/message/common/subs">测试订阅定时</a>';
_button += '</div>';
// _button += '<a href="/WEB-INF/views/prss/message/talk.jsp">测试</a>';

// 展示消息
function showMessage(num) {
	var title = '';
	if (num == 0) {
		title = '已收消息(48小时以内消息)';
	} else if (num == 1) {
		title = '已发消息(48小时以内消息)';
	} else if (num == 3) {
		title = '收藏消息';
	}
	var layero = parent.layer.getChildFrame().context;
    var iframe = $(layero).find("iframe")[0];
    var height = iframe.clientHeight;
    if(height  < 550)
		height = 550;
	var msg = parent.layer.open({
		type : 2,
		title : title,
		zIndex : 99999999999,
		maxmin : false,
		shadeClose : false,
		area : [ $("body").width() + "px", height +"px" ],
		offset : 'rb',
		content : ctx + "/message/history/list?num=" + num

	});
}

// 订阅消息
function sche() {
	var layero = parent.layer.getChildFrame().context;
    var iframe = $(layero).find("iframe")[0];
    var height = iframe.clientHeight;
    if(height  < 550)
		height = 550;
	parent.layer.open({
		type : 2,
		title : "订阅",
		maxmin : false,
		zIndex : 99999999999,
		content : ctx + "/message/subscribe/list",
		area : [ $("body").width() + "px", height + "px" ],
		offset : 'rb',
		btn2 : function(index, layero) {
			layer.getChildFrame("#createForm1", index)[0].reset();
			var refBtn = layer.getChildFrame("#refreshDetailTable", index);
			refBtn.click();
			return false;
		}
	});
}
function openMess() {

	var fltid = '';
	var flightNumber = '';
	var flightDate = '';
	var in_fltid = '';
	var out_fltid = '';
	var tid = '';
	var ioFlag = '';
	var mainFrame = $("#mainFrame")[0];

	// 判断是否是航班动态页面
	// var src = mainFrame.src;
	// src = src.substring(src.length-18);
	if(mainFrame != null){
		var table = mainFrame.contentWindow.document.getElementById("baseTable");
		// if(src == 'flightDynamic/list'){
		if (table != null) {
			var row = mainFrame.contentWindow.clickRow;
			var ioFlag = mainFrame.contentWindow.ioFlag;
			if (row != null) {
				if (row.flight_date) {
					flightDate = row.flight_date;
				}
				if (row.in_fltid) {
					in_fltid = row.in_fltid;
				}
				if (row.out_fltid) {
					out_fltid = row.out_fltid;
				}
				if (ioFlag == "I") {
					fltid = in_fltid;
					flightNumber = row.in_fltno;
				}
				if (ioFlag == "O") {
					fltid = out_fltid;
					flightNumber = row.out_fltno;
				}
			}
		}
	}
	openMessage(fltid, flightNumber, flightDate, in_fltid, out_fltid, tid,
			ioFlag);
}
function openMessage(fltid, flightNumber, flightDate, in_fltid, out_fltid, tid,
		ioFlag) {
	layer.closeAll(); // 关闭加载层
	$(".messageWarn").css("display", "none");
	$(".messageWarn").text('');
	$(".layui-fixbar").find("li[lay-type='bar1']").css("display", "none");
	
	var layero = parent.layer.getChildFrame().context;
    var iframe = $(layero).find("iframe")[0];
    var height = iframe.clientHeight;
	var width = $("body").width();
	if(width * 0.5 < 700)
		width = 700;
	else 
		width = width * 0.5;
	if(height  < 550)
		height = 550;
	layer.ready(function() {
		_index = layer.open({
			id : "messageSend",
			type : 2,
			anim : 2,
			resize : false,
			zIndex : 9999999,
			tips : [ 1, '#red' ],
			title : _button,
			shade : 0,
			skin : 'demo-class',
			// maxmin:false,
			area : [ width + "px", height +"px" ],
			offset : 'rb',
			content : ctx + "/message/common/send?flag=add&fltid=" + fltid
					+ "&flightNumber=" + flightNumber + "&flightDate="
					+ flightDate + "&in_fltid=" + in_fltid + "&out_fltid="
					+ out_fltid + "&tid=" + tid + "&fiotype=" + ioFlag,
			move : "messageSend",
			success : function(layero, index) {
				layer.setTop(layero);
			},
			cancel : function(index, layero) {
				// if(confirm('确定要关闭么')){ //只有当点击confirm框的确定时，该层才会关闭
				layer.close(index);
				$(".messageWarn").css("display", "none");
				$(".messageWarn").text('');
				// }
				_index = 0;
				return false;
			}
		});
	});
}

function refresh() {
	var frameId = document.getElementById('messageSend').getElementsByTagName(
			"iframe")[0].id;
	$('#' + frameId)[0].contentWindow.refreshLoad();
}
function createPopUp(cntt2) {
	//判断消息来源（asup报文提醒消息）
	if(cntt2.MSGSOURCE&&cntt2.MSGSOURCE=="asup"){
		asupMessagePopUp(cntt2);
	} else {//正常消息
		var timestamp = (new Date()).valueOf();
		var show = '';
		show += '<input type="hidden" id="' + timestamp + '" data-id="' + cntt2.ID
				+ '" data-mtoid="' + cntt2.MTOID + '"/>';
		show += '<div class="layui-tab-content">';
		show += '<span id="time">' + (cntt2.SENDTIME == null ? "" : cntt2.SENDTIME)
				+ '</span>';
		show += ' <span id="sender">'
				+ (cntt2.SENDERCN == null ? "" : cntt2.SENDERCN) + '</span>';
		show += '</div>';
		show += '<div class="layui-tab-content mtext">';
		show += '<span class="un_mtext">'
				+ (cntt2.MTEXT == null ? "" : cntt2.MTEXT) + '</span>';
		show += '</div>';
		var btn;
		var iftrans = cntt2.IFTRANS;
		if (iftrans == 1) {
			btn = [ '接收', '忽略', '转发' ];
		} else {
			btn = [ '接收', '忽略' ];
		}
		var tempName = cntt2.TEMPNAME;
		if (tempName == 'telegraphSendAuto') {
			btn.push('发送');
		}
		layer.open({
			zIndex : 9999999999999,
			shadeClose : false,
			shade : 0,
			type : 1,
			title : (cntt2.MTITLE == null ? "消息" : cntt2.MTITLE) + ' '
					+ (cntt2.FLIGHTNUMBER == null ? "" : cntt2.FLIGHTNUMBER),
			area : ["450px","auto"],
			btn : btn,
			content : show,
			yes : function(index, layero) {
				receive(cntt2, index);
			},
			btn2 : function(index, layero) {
				layer.close(index);
			},
			btn3 : function(index, layero) {
				if (tempName == 'telegraphSendAuto') {
					sendTelegraph(cntt2.FLTID);
				} else {
					trans(cntt2, index);
				}
			},
			btn4 : function(index, layero) {
				sendTelegraph(cntt2.FLTID);
			}
		});
		var mtext = $(".mtext");
		mtext.css("position", "relative");
		new PerfectScrollbar(mtext[0]);
	}
}
function sendTelegraph(fltid) {
	layer.open({
		type : 2,
		title : '报文发送',
		closeBtn : false,
		content : ctx + "/telegraph/auto/sendMessageList?fltid=" + fltid,
		btn : [ "发送", "取消" ],
		area : [ $("body").width() + "px", "550px" ],
		offset : 'rb',
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.doSend();
			return false;
		},
		success : function(layero, index) {
			var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; // 得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			var inputFltid = body.find('#fltid');
			if (ioFlag == "I") {
				inputFltid.val(row.in_fltid);
			}
			if (ioFlag == "O") {
				inputFltid.val(row.out_fltid);
			}
		}
	});
}
function receive(obj, index) {
	var id = obj.MID;
	var mtoid = obj.MTOID;
	var param = {
		mid : id,
		type : 'isreceive',
		mtoid : mtoid
	};
	$.post(ctx + "/message/common/update", param, function(msg) {
		if (msg == "false") {
		} else {
			layer.close(index);
		}
	}, "text").success(function() {

	}).error(function() {
		layer.alert("操作失败！", {
			icon : 0,
			time : 1000
		});
	});

}
function trans(obj, index) {
	var id = obj.MID;
	var transtmplid = obj.TRANSTEMPL;
	var mtoid = obj.MTOID;
	var param = {
		id : id,
		mtoid : mtoid,
		transtmplid : transtmplid
	};
	$.post(ctx + "/message/common/popTrans", param, function(msg) {
		if (msg == "false") {
			// layer.alert("操作失败，请联系管理员！",{icon:0,zIndex : 99999999999999,time:
			// 1000});
		} else {
			layer.close(index);
		}
	}, "text").success(function() {

	}).error(function() {
		layer.alert("操作失败！", {
			icon : 0,
			time : 1000
		});
	});
}

// 消息语音播放功能
function play(s, i) {
	read(s, i);
}
function read(s, i) {
	if (i == s.length) {
		i = 0;
		return false;
	}
	var Media = new Audio();
	var text = s.charAt(i);
	text = getText(text);
	if (text == null || text == '') {
		i++;
		if (i < s.length)
			read();
	} else {
		Media.src = ctxStatic + "/prss/message/voices/" + text + ".wav"
		Media.load();
		Media.addEventListener("ended", function() {
			i++;
			if (i < s.length)
				read(s, i);
		});
		Media.play();
	}
}
function getText(text) {
	if (text == "新") {
		text = "zxin"
	} else if (text == "落") {
		text = "zluo"
	} else if (text == "起") {
		text = "zqi"
	} else if (text == "降") {
		text = "zjiang";
	} else if (text == "返") {
		text = "zfan";
	} else if (text == "取") {
		text = "zqu";
	} else if (text == "变") {
		text = "zbian";
	} else if (text == "号") {
		text = "zhao";
	} else if (text == "由") {
		text = "zyou";
	} else if (text == "保") {
		text = "zbao";
	} else if (text == "位") {
		text = "zwei";
	}
	return text;
}
/**
 * ASUP报文消息弹窗
 * @param content
 */
function asupMessagePopUp(content){
	var eventRecord = content.EVENT_RECORD;
	var fltNo = content.FLIGHT_NUMBER;
	var mText = content.MTEXT;
	//新增航班
	if(eventRecord.TYPE==1){
		layer.open({
			zIndex : 9999999999999,
			shadeClose : false,
			shade : 0,
			type : 1,
			title : "新增航班",
			offset: 'rb',
			area : [ "300px", "200px" ],
			btn : ["查看报文", "新增航班","忽略"],
			content : "<div class='layui-tab-content mtext' style='overflow: auto;height: 60px;'>"+mText+"<span class='un_mtext'></span></div>",
			yes : function(index, layero) {//查看报文原文
				asupMessageHandler(1,eventRecord,index);
			},
			btn2 : function(index, layero) {//新增航班
				asupMessageHandler(2,eventRecord,index);
				return false;
			},
			btn3 : function(index, layero) {//忽略
				layer.close(index);
			}
		});
	} else if(eventRecord.TYPE==2){//变更事件
		layer.open({
			zIndex : 9999999999999,
			shadeClose : false,
			shade : 0,
			type : 1,
			offset: 'rb',
			title : (content.MTITLE?content.MTITLE: "消息提醒"),
			area : [ "300px", "200px" ],
			btn : ["查看报文", "更新","忽略"],
			content : "<div class='layui-tab-content mtext' style='overflow: auto;height: 100px;'>"+mText+"<span class='un_mtext'></span></div>",
			yes : function(index, layero) {//查看报文原文
				asupMessageHandler(1,eventRecord,index);
				return false;
			},
			btn2 : function(index, layero) {//更新
				asupMessageHandler(3,eventRecord,index);
				return false;
			},
			btn3 : function(index, layero) {//忽略
				layer.close(index);
			}
		});
	}
	var mtext = $(".mtext");
	mtext.css("position", "relative");
	new PerfectScrollbar(mtext[0]);
}
/**
 * ASUP报文消息按钮回调
 * @param type
 * @param handlerType
 * @param eventRecord
 */
var addFltWin = null;
var eventSource = {};//记录新增航班点击时的事件id
function asupMessageHandler(handlerType,eventRecord,popUpIndex){
	if(handlerType==1){//查看报文
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ],
			zIndex : 99999999999999
		});
		jQuery.ajax({
			url : ctx + "/asup/messageHandler/source",
			data : {
				eventRecord : JSON.stringify(eventRecord)
			},
			async : false,
			error:function(){
				layer.close(loading);
				layer.alert("请求失败！",{icon:2});
			},
			success : function(data) {
				layer.close(loading);
				if(data){
					layer.open({
						zIndex : 99999999999999,
						shadeClose : false,
						shade : 0,
						type : 1,
						title : "查看报文",
						area : [ "500px", "400px" ],
						content : "<div class='layui-tab-content mtext'>"+data+"</div>",
					});
				} else {
					layer.msg("未能找到相关报文！",{icon:7});
				}
			}
		});
	} else if(handlerType==2){//新增航班
		addFltWin = layer.open({
			type : 2,
			title : "新增航班",
			zIndex : 99999999999999,
			area:["95%","90%"],
			content : ctx + "/flightDynamic/form?dataSource=2&sourceId="+eventRecord.SOURCE_ID,
			btn : ['保存','新增行','删除行','关闭'],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var gridData = iframeWin.getGridData();
				var result = 0;
				for(var i=0;i<gridData.length;i++){
					var row = gridData[i];
					var departApt = row.DEPARTAPT;
					var arriveApt = row.ARRIVALAPT;
					var fltNo = row.FLTNO;
					var fltDate = row.FLTDATE;
					if(departApt==currentAirport||arriveApt==currentAirport){
						var count = validFltExist({
							departApt:departApt,
							arriveApt:arriveApt,
							fltNo:fltNo,
							fltDate:fltDate
						});
						result+=Number(count);
					}
				}
				if(result==0){//保存
					iframeWin.save();
				} else {
					layer.msg("航班已存在！",{icon:7,zIndex : 99999999999999});
					return false;
				}
			},
			btn2 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.addRow();
				return false;
			},
			btn3 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.removeRow();
				return false;
			},
			success: function(layero, index){
				eventSource[eventRecord.SOURCE_ID] = eventRecord.ID;
				layer.close(popUpIndex);
			}
		});
	} else if(handlerType==3){//更新
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ],
			zIndex : 99999999999999
		});
		jQuery.ajax({
			url : ctx + "/asup/messageHandler/update",
			data : {
				eventRecord : JSON.stringify(eventRecord)
			},
			async : false,
			error:function(){
				layer.close(loading);
				layer.alert("请求失败！",{icon:2});
			},
			success : function(result) {
				layer.close(loading);
				if(result=="succeed"){
					layer.close(popUpIndex);
					layer.msg("航班"+eventRecord.FLIGHT_NUMBER+"信息更新成功！",{icon:1});
				} else if(result&&result!="succeed"){
					layer.alert(result,{icon:2});
				} else {
					layer.alert("更新失败！",{icon:2});
				}
			}
		});
	}
}
/**
 * 新增航班回调
 * @param data
 */
function addFltCallback(data,sourceId){
	if(data){
		var obj = JSON.parse(data);
		if(obj.status == 1){
			layer.close(addFltWin);
			delete eventSource[sourceId];
			layer.msg("新增航班成功！",{icon:1});
			/*var loading = layer.load(2, {
				shade : [ 0.1, '#000' ],
				zIndex : 99999999999999
			});
			var fltId = "";
			if(obj.inFltId){
				fltId = obj.inFltId;
			} else if(obj.outFltId){
				fltId = obj.outFltId;
			}
			jQuery.ajax({
				url : ctx + "/asup/messageHandler/updateEventStatus",
				data : {
					fltId : fltId,
					eventRecordId:eventSource[sourceId]
				},
				async : false,
				error:function(){
					layer.close(loading);
					layer.alert("请求失败！",{icon:2});
				},
				success : function(result) {
					layer.close(addFltWin);
					layer.close(loading);
					if(result=="succeed"){
						delete eventSource[sourceId];
						layer.msg("新增航班成功！",{icon:1});
					} else {
						layer.alert("新增航班成功，事件状态处理失败！"+result,{icon:7});
					}
				}
			});*/
		} else {
			layer.alert("保存失败"+result.error,{icon:2});
		}
	}
}
/**
 * 新增航班校验航班是否存在
 */
function validFltExist(param){
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ],
		zIndex : 99999999999999
	});
	var result = "";
	jQuery.ajax({
		url : ctx + "/flightDynamic/isFltExist",
		data :param,
		async : false,
		error:function(){
			layer.close(loading);
			layer.alert("请求失败！",{icon:2});
		},
		success : function(data) {
			layer.close(loading);
			result = data;
		}
	});
	return result;
}