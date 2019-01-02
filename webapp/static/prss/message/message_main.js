	var element;
	var pageNum =1;
	var senddefId = '';
	$(function() {
		layui.use('layer');
		layui.use('element', function(){
			element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
		});
		
		var layero = parent.layer.getChildFrame().context;
	    var iframe = $(layero).find("iframe")[0];
	    var height = iframe.clientHeight-44;
	    if(height  < 506)
			height = 506;
	    $("#main").height(height);
		show(1);
		//初始化模板
		initTemplate('');
		//初始化未读消息、已发消息
 		setInterval(refresh,5000);// 每隔5秒执行一次页面刷新
 		$("#ifreply").on("click",function(){
 			if($(this).prop("checked")){
 				$("#defreply").removeAttr("disabled"); 
 			} else {
 				$("#defreply").attr("disabled","disabled");
 			}
 		});
		$(".send").on("click",function(){
			var messtype = $("li[class='layui-this']").attr("lay-id");
			if(messtype == '11'){
				if($(".template-select").length<=0){
					layer.alert("请选择模板！",{icon:0,time: 1000});
				} else {
					var flag = $("#flag").val();
					if("add" == flag){
						var toListStr = [];
						var tTSelect = $(".templto-div-select");
						if(tTSelect.length<=0){
							layer.alert("请选择接收人！",{icon:0,time: 1000});
						} else {
							var tT = $(".templto");
							if(tT.length>tTSelect.length){
								$(tTSelect).each(function(index,e){
									var obj = $(e).find("span")[0];
									var id = $(obj).attr("to-er");
									toListStr.push(id);
								});
							}
							var template = $(".template-select");
							var mid = $(template).attr("mid");	
							$("#tid").val(mid);
							$("#toListStr").val(toListStr.join(","));
							var fiotype = $(template).data("fiotype");
							if(fiotype =='A' ) {//进港
								$("#fiotype").val(fiotype);
							} else if(fiotype == 'D' ) {//出港
								$("#fiotype").val(fiotype);
							} else {//无关联
								$("#fiotype").val(fiotype);
							}
							dosubmit();
						}
					}
				}
			} else {
				if(senddefId==''){
					layer.alert("请选择模板！",{icon:0,time: 1000});
				} else {
					$("#tid").val(senddefId);
					var toListStr = [];
					var tTSelect = $(".templto-div-select");
					if(tTSelect.length<=0){
						layer.alert("请选择接收人！",{icon:0,time: 1000});
					} else {
						var tT = $(".templto");
						if(tT.length>tTSelect.length){
							$(tTSelect).each(function(index,e){
								var obj = $(e).find("span")[0];
								var id = $(obj).attr("to-er");
								toListStr.push(id);
							});
						}
						var template = $(".template-select");
						var mid = $(template).attr("mid");	
						$("#toListStr").val(toListStr.join(","));
						dosubmit();
					}
				}
			}
		});
		
		$(".messagefile2").on("click",function(){
			if($(".template-select").length<=0){
				layer.alert("请选择模板！",{icon:0,time: 1000});
			} else {
				$("#fileInput").click();
			}
		});
		
		$(".messagefile").on("click",function(){
			layer.open({
				type:1,
				  shade: 0,
				  shadeClose: true,
				title: "上传附件",
				offset: '100px',
				area:["600px","300px"],
				content:$("#messFile"),
				btn:["附件上传","确认"],
				yes:function(index, layero){
					$("#fileInput").click();
				},
				btn2:function(index, layero){
					layer.close();
				}
			})
		});
		refresh();
		$(".query_mess").keydown(function(event) {//给输入框绑定按键事件
	        if(event.keyCode == "13") {//判断如果按下的是回车键则执行下面的代码
	        	var text = $(this).val();
	        	initTemplate(text);
	        	initSendInfo();
	        	$("#mtext").val("");
	        	$("#template_to").empty();
	        }
		});
		//设置滚动条--begin
		$.each($(".layui-tab-item"),function(k,v){
			$(v).css("position","relative");
			new PerfectScrollbar(v);
		})
		var tpl=$(".templateList");
		tpl.css("position","relative");
		new PerfectScrollbar(tpl[0]);
		
		var mtext = $(".mtext");
		mtext.css("position", "relative");
		new PerfectScrollbar(mtext[0]);
		
		var mtext = $("#template_to");
		mtext.css("position", "relative");
		new PerfectScrollbar(mtext[0]);
		//设置滚动条--end
		//设置div滚动加载数据 --begin
		$(".sendmessage").scroll(function(){
			var nScrollTop=$(this)[0].scrollTop;//滚动条距顶部的高度
			var nDivHight=$(this).height();//可见区域的高度
			var nScrollHight= $(this)[0].scrollHeight;//为整个DIV的高度（包括屏幕外的高度）
			if(nScrollTop + nDivHight >= nScrollHight){
				console.log("滚动条到底部了");
				pageNum = pageNum +1;
				initSendMessage(pageNum);
			}
		});
		$(".EVENT_list").scroll(function(){
			var nScrollTop=$(this)[0].scrollTop;
			var nDivHight=$(this).height();
			var nScrollHight= $(this)[0].scrollHeight;
			if(nScrollTop + nDivHight >= nScrollHight){
				pageNum = pageNum +1;
				initUnReadMessage('EVENT');
			}
		});
		$(".SUBS_list").scroll(function(){
			var nScrollTop=$(this)[0].scrollTop;
			var nDivHight=$(this).height();
			var nScrollHight= $(this)[0].scrollHeight;
			if(nScrollTop + nDivHight >= nScrollHight){
				pageNum = pageNum +1;
				initUnReadMessage('SUBS');
			}
		});
		$(".WARN_list").scroll(function(){
			var nScrollTop=$(this)[0].scrollTop;
			var nDivHight=$(this).height();
			var nScrollHight= $(this)[0].scrollHeight;
			if(nScrollTop + nDivHight >= nScrollHight){
				pageNum = pageNum +1;
				initUnReadMessage('WARN');
			}
		});
		//设置div滚动加载数据 --end
	});
	
	function queryMess(obj){
		 if(event.keyCode==13){  
//		     console.info('click enter');  
		 }  
	}
	function dosubmit(){
			var loading = null;
			var filenum = $("#fileTable").find("tr").length;
			$("#filenum").val(filenum);
			var impWind = $("#fileList").ajaxSubmit({
				beforeSubmit : function() {
// 					$("#messFile").find("#fileInput").each(function(){
// 						var filePath = $(this).val();
// 						var suffix = filePath.substring(filePath.lastIndexOf(".")+1);
// 						if(suffix!="xlsx"&&suffix!="xls"){
// 							layer.msg('不支持的文件类型！', {
// 								icon : 7
// 							});
// 							return false;
// 						}
// 					});
					loading = layer.load(2, {
						shade : [ 0.1, '#000' ]
					// 0.1透明度
					});
				},
				error : function(e) {
					initUnReadNum();
					layer.close(loading);
					delete $("#fileInput")[0].files;
					layer.alert("发送消息失败！",{icon:0,time: 1000});
				},
				success : function(data) {
					initUnReadNum();
		        	$("#mtext").val("");
		        	var messtype = $("li[class='layui-this']").attr("lay-id");
		    		if(messtype=='11'){
		    			$("#template_to").empty();
		    		}
		        	$(".template-btn").each(function(index,e){
		    			$(e).removeClass("template-select");
		    		});
					delete $("#fileInput")[0].files;
					$("#fileTable").empty();
					layer.close(loading);
					layer.alert(data,{icon:0,time: 1000}, function() {
					});
					var defaultSubs = $("#defaultSubs").val();
					if(defaultSubs == 'true'){
						$("li[lay-id='SUBS']").click();
					}
				}
			});
	}
	function refresh(){
		var messtype = $("li[class='layui-this']").attr("lay-id");
		if(messtype=='11'){
		} else if(messtype == 'FEEDBACK'){
			initSendMessage(pageNum);
		} else {
			initUnReadMessage('');
		}
		initUnReadNum();
//		refreshTemplTo(0);
	}
	function refreshLoad(){
		pageNum =1;
		refresh();
	}
	function initUnReadNum (){
		$.getJSON(ctx+"/message/common/unReadListNum", function(data) {
			if(data.result){
				var obj = data.unReadNumList;
				if(obj.length>0){
					$.each(obj, function (n, e) {
						var num = e.NUM;
						if(num>0){
							$("li[lay-id='"+e.MESSTYPE+"']").html(e.TYPENAME +'(<b style="color: red;"><span>'+
									e.NUM+'</span></b>)');
						} else {
							$("li[lay-id='"+e.MESSTYPE+"']").html(e.TYPENAME);
						}
					});
				} else {
					$("li[lay-id=WARN]").html('待办预警');
					$("li[lay-id=EVENT]").html('待办事件');
					$("li[lay-id=SUBS]").html('待办订阅');
					$("li[lay-id=FEEDBACK]").html('待反馈');
				}
			} 
		});
	}
	function initUnReadMessage(messtype){
		if(messtype=='')
			messtype = $("li[class='layui-this']").attr("lay-id");
		if(messtype !=null && messtype != ''){
			$.getJSON(ctx+"/message/common/unReadList",{messtype : messtype,pageNum : pageNum}, function(data) {
				if(data.result){
					var unReadList = data.unReadList;
					if(unReadList.length>0){
						if(pageNum ==1)
							$("."+messtype+"_list").empty();
						$(unReadList).each(function(index , e){
							createUnreadMessage(e,index,messtype);
						});
					} else {
						if(pageNum == 1){
							$("."+messtype+"_list").empty();
							if(messtype=='WARN'){
								$("li[lay-id=WARN]").html('待办预警');
							} else if(messtype=='EVENT'){
								$("li[lay-id=EVENT]").html('待办事件');
							} else if(messtype=='SUBS'){
								$("li[lay-id=SUBS]").html('待办订阅');
							}
						}
					}
				} 
			});
		} 
	}
	
	function createUnreadMessage(e,index,messtype){
		if(messtype==null || messtype =='')
			messtype = $("li[class='layui-this']").attr("lay-id");
		var ifreply = e.IFREPLY;
		var $html = $('<div class="unread-message"></div>');
		var timestamp = (new Date()).valueOf() + index;
		var favorite = e.FAVORITE;
		var html = '<input type="hidden" id="'+timestamp+'" data-id="'+e.ID+'" data-mtoid="'+e.MTOID+'"/>'+
			'<div class="mess-color">'+
			'<span id="unmtitle">'+(e.MTITLE==null?"":e.MTITLE)+' </span>'+
			'<span id="unFlightNumber">'+(e.FLIGHTNUMBER==null?"":e.FLIGHTNUMBER)+'</span> <span id="time">'
			+e.SENDTIME+'</span> <span id="sender">'+e.SENDERCN+'</span>'+
			'<i class="favorite glyphicon '+(e.FAVORITE==1?"glyphicon-heart":"glyphicon-heart-empty")+
			'" onclick="favorite(this);" data-id="'+e.ID+'" data-type="1">&nbsp;</i>'+
			'</div>'+
			'<div class="div_mtext" ><span class="un_mtext">'+(e.MTEXT==null?"":e.MTEXT)+'</span>'+
			'</div>'+
			'<div class="text-right">';
		var iftrans = e.IFTRANS;
		if(e.ISREPLED == null || e.ISREPLED == 0){
			if(ifreply == 1){
//				if(e.REPLYTEXT!=null && e.REPLYTEXT != '')
					html += '	<input type="text" disabled class="input-width" id="reply_'+timestamp+'" value="'+(e.REPLYTEXT==null?'':e.REPLYTEXT)+'"/>';
				html += '	<button class="layui-btn layui-btn-mini layui-btn-normal reply" data-id="'+timestamp+'" onclick="reply(this);">回复</button>';
				
			} else {
				html += '	<button class="layui-btn layui-btn-mini layui-btn-normal reply" data-id="'+timestamp
				+'" onclick="isreceive(this);">接收</button>';
			}	
		}
		if(iftrans ==1){
			html += '	<button class="layui-btn layui-btn-mini layui-btn-normal trans" data-id="'+timestamp
			+'" data-transtmplid="'+e.TRANSTEMPL+'" data-fltid="'+e.RFID+'" data-flightnumber="'+e.FLIGHTNUMBER+
			'" data-mtoid="'+e.MTOID+'" data-fiotype="'+e.FIOTYPE+'" onclick="trans(this)">转发</button>';
		}
		var fileslen = e.FILESLEN;
		if(fileslen>0){
			html += '	<button class="layui-btn layui-btn-mini layui-btn-normal downfile" data-id="'+timestamp
			+'" onclick="downfile(this)">附件下载</button>';
		}
		var tempName = e.TEMPNAME;
		if(tempName =='telegraphSendAuto'){
			html += '	<button class="layui-btn layui-btn-mini layui-btn-normal downfile" data-id="'+timestamp
			+'" onclick="sendTelegraph('+e.RFID+')">发送报文</button>';
		}
		html += '</div>'+'<legend class="legend"></legend>';
		$html.append(html);	
		if(index != null){
			$("."+messtype+"_list").append($html);
		} else {
			$("."+messtype+"_list").prepend($html);
		}
	}
	function sendTelegraph(fltid){
		var layero = parent.layer.getChildFrame().context;
	    var iframe = $(layero).find("iframe")[0];
	    var height = iframe.clientHeight;
		parent.layer.open({
			type : 2,
			title : '报文发送',
			closeBtn : false,
			content : ctx + "/telegraph/auto/sendMessageList?fltid=" + fltid,
			btn : [ "发送", "取消" ],
			shadeClose : false,
			area:[window.parent.$("body").width()+"px",height + "px"],
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
			}
		});
	}
	function update(param,type){
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		// 0.1透明度
		});
		$.post(ctx+"/message/common/"+type,param, function(msg) {
			if(msg == "false"){
				layer.alert("操作失败，请联系管理员！",{icon:0,time: 1000});
			} else {
				pageNum =1;
//				initUnReadNum();
//				if(param.type=='ignore'){
//					initSendMessage(1);
//				} else {
//					initUnReadMessage('');
//				}
				refresh();
			}
			layer.close(loading);
		},"text").success(function() {
			
		})
	    .error(function() { 
	    	layer.alert("操作失败！",{icon:0,time: 1000});
			layer.close(loading);
	    });
	}
	function favorite(obj){
		var id = $(obj).data("id");
		var type = $(obj).data("type");
		var isFavorite = $(obj).attr('class').indexOf('glyphicon-heart-empty')>-1?1:0;
		var param = {mid : id,isFavorite : isFavorite, type: type};
		update(param,"favorite");
	}
	function ignoreAll(){
		var mids = [];
		$(".ignore").each(function(index,e){
			var mid = $(e).data("id");
			if(mid!=null && mid != '')
				mids.push(mid);
		});
		$(".ignore").parent().parent().remove();
		var param = {mids:mids.join(","),type:'ignore'};
		update(param,"update");
	}
	function ignore(obj){
		var mid = $(obj).data("id");
		var param = {mids:mid,type:'ignore'};
		$(obj).parent().parent().remove();
		update(param,"update");
	}
	function trans(obj){
		var $id = $(obj).data("id");
		var id = $("#"+$id).data("id");
		var transtmplid = $(obj).data("transtmplid");
		var mtoid = $(obj).data("mtoid");
		var fiotype = $(obj).data("fiotype");
		var fltid = $(obj).data("fltid");
		var flightNumber = $(obj).data("flightnumber");
		$("#fltid").val(fltid);
		$("#flightNumber").val(flightNumber);
		element.tabChange('demo', '11');
		$("#transsubId").val(mtoid)
		$(".template-btn").each(function(index,e){
			var $mid = $(e).attr("mid");
			if($mid == transtmplid){
				$(e).addClass("template-select");
				var mtext = $(obj).parent().parent().find("span[class='un_mtext']").html();
				initTemplTo(2,$mid,mtext,fiotype);
				$("#mtext").val(mtext);
				$("#oldmid").val(id);
			} else {
				$(e).removeClass("template-select");
			}
		});
		$(".template-btn").attr("disabled","disabled");
	}

	function isreceive(obj){
		var $id = $(obj).data("id");
		var id = $("#"+$id).data("id");
		var replytext = $("#reply_"+$id).val();
		var mtoid = $("#"+$id).data("mtoid");
		var param = {mid:id,type:'isreceive',mtoid : mtoid};
		update(param,"update");
	}
	
	function reply(obj){
		var $id = $(obj).data("id");
		var id = $("#"+$id).data("id");
		var replytext = $("#reply_"+$id).val();
		var mtoid = $("#"+$id).data("mtoid");
		var param = {mid:id,replytext:replytext,type:'reply',mtoid : mtoid};
		update(param,"update");
	}
	
	function downfile(obj){
		var $id = $(obj).data("id");
		var id = $("#"+$id).data("id");
		layer.open({
			type:1,
			title: "附件下载",
			offset: '100px',
			area:["400px","300px"],
			content:$("#messLoadFile"),
			yes : function() {
//				$("#fileInput2").click();
			},
			success:function(){
				//获取附件列表信息
				$("#fileLoadTable").empty();
				$.ajax({
					type : 'post',
					url : ctx + "/message/history/getFileIds",
					data : {
						'id' : id
					},
					dataType : 'json',
					success : function(fileIds) {
						for (var i = 0; i < fileIds.length; i++) {
							var tr = $([ '<tr>',
									'<td>' + fileIds[i]["NAME"] + '</td>', '</tr>' ]
									.join(''));
							var option = $([
									'<td>',
									'<button type="button" data-id="'
											+ fileIds[i]["ID"]
											+ '" data-name="'
											+ fileIds[i]["NAME"]
											+ '" class="layui-btn layui-btn-mini layui-btn-normal file-download" onclick="downloadFile(this)">下载</button>',
									'</td>' ].join(''));
							tr.append(option);
							$("#fileLoadTable").append(tr);
						}
					}
				});
			}
		})
	}
	
	// 附件下载
	function downloadFile(btn) {
		var id = $(btn).data("id");
		var fileName = $(btn).data("name");
		var url = ctx + "/message/common/downloadFile"
		var $form = $('<form action="' + url
				+ '" method="post" style="display:none"></form>');
		var hidden = '<input type="hidden" name="id" value="' + id + '"/>'
				+ '<input type="hidden" name="fileName" value="' + fileName + '"/>';
		$form.append(hidden);
		$('body').append($form);
		$form.submit();
	}
	function initSendMessage(pageNum){
		$.getJSON(ctx+"/message/common/sendMessageList",{pageNum : pageNum}, function(data) {
			if(data.result){
				var sendMessage = data.sendMessageList;
				if(sendMessage.length>0){
					if(pageNum ==1 ){
						$(".sendmessage").empty();
						var ignoreAll = '<div class="mess-div"><div class="text-right"><button class="layui-btn layui-btn-mini layui-btn-normal ignore" '
							+'onclick="ignoreAll()">全部忽略</button></div></div>';
						$(".sendmessage").append(ignoreAll);
					}
					$(sendMessage).each(function(index , e){
						var html = '<div class="mess-div">'+
							'<div onclick="showTemplTo('+e.ID+');" style="cursor: pointer;" class="mess-color">'+
							'<span id="mtitle">'+(e.MTITLE==null?"":e.MTITLE)+' </span>'+ 	
							'<i class="favorite glyphicon '+(e.FAVORITE==1?"glyphicon-heart":"glyphicon-heart-empty")+
							'" onclick="favorite(this);" data-id="'+e.ID+'" data-type="2">&nbsp;</i>'+
							'<span id="sendFlightNumber">'+(e.FLIGHTNUMBER==null?"":e.FLIGHTNUMBER)+'</span> <span id="time">'
							+(e.SENDTIME==null?"":e.SENDTIME)+'</span> <span id="sender">'+(e.SENDERCN==null?"":e.SENDERCN)+'</span>'+
							'</div>'+
							'<div style="cursor: pointer;" class="div_mtext" onclick="showTemplTo('+e.ID+');">' + 
							'<span id="send_mtext">'+(e.MTEXT==null?"":e.MTEXT)+'</span>'+
							'</div>'+
							'<div class="text-right">'+
							'	<button class="layui-btn layui-btn-mini layui-btn-normal ignore" data-id="'+e.ID+'" onclick="ignore(this)">忽略</button>'+
							'</div>'+
							'<legend class="legend"></legend>'+
							'</div>';
						$(".sendmessage").append(html);
					});
					initUnreadMessClick();
				} else {
					if(pageNum == 1){
						$(".sendmessage").empty();
						$("li[lay-id='FEEDBACK']").html("待反馈");
					}
				}
			} 
		});
	}
	function initUnreadMessClick(){
		$(".mess-div").on("click",function(){
			$(".mess-div").removeClass("mess-div-select");
			$(this).addClass("mess-div-select");
		});
	}
	function initTemplate(text){
		$(".templateList").children().remove();
		var tid = $("#tid").val();
		var param = {text : encodeURIComponent(text)};
		$.getJSON(ctx+"/message/common/getTemplateList",param, function(data) {
			if(data.result){
				var html='';
				var templateList = data.templateList;
				$(templateList).each(function(index,element){
					if(index%2==0){
						html += '<div>';
					}
					html += '<button type="button" class="template-btn btn template-templ ';
					if(tid=='' || tid == null){
						if(element.SENDDEF == 1){
							senddefId = element.ID;
							initAutoTemplTo(senddefId);
						}
					} else {
						if(tid == element.ID){
							html += 'template-select';
						}
					}
					html += '" data-type="tabChange" title="'
						+(element.MTITLE==null?"":element.MTITLE)+'" ' + ' mid="'+element.ID+'" ' + ' data-fiotype="' + element.FIOTYPE + '" ' +
						' onclick="initTemplTo(2,'+element.ID+',\'\',\''+element.FIOTYPE+'\')">'+(element.MTITLE==null?"":element.MTITLE)+'</button>';
					if(index%2==1){
						html += '</div>';
					}
				});
				$(".templateList").append(html);
				initTemplClick();
				if(tid != null && tid != ''){
					initTemplTo(2,tid,'',$("#fiotype").val());
				}
			} else {
				layer.alert("获取模板失败！",{icon:0,time: 1000});
			}
		}).success(function(){
		}).fail(function(){
			layer.alert("获取模板失败！",{icon:0,time: 1000});
		});
	}
	
	function initAutoTemplTo(tid){
		var obj = $("#template_to");
		obj.children().remove();
		var fltid = $("#fltid").val();
		initSendMessTo(fltid,'',tid,'',obj);
	}
	function initTemplClick(){
		$(".template-btn").on("click", function() {
			$(".template-btn").removeClass("template-select");
			$(this).addClass("template-select");
		});
	}
	function initClick() {
		$(".templto2").on("click", function() {
			$(this).parent().toggleClass('templto-div-select', 'templto-div');
			var checkbox = $(this).parent().find(":checkbox");
			checkbox.prop('checked', !checkbox.prop('checked'));
		});
		$(".templto :checkbox").on("click", function() {
			$(this).parent().toggleClass('templto-div-select', 'templto-div');
		});
	}
	function initToReplyClick() {
		$(".to_reply").on("click", function() {
			var lay_id = $(".layui-this").attr("lay-id");
			if(lay_id == 'FEEDBACK'){
				var obj = $(this);
				var mtoid = obj.data("mtoid");
				var mid = obj.data("mid");
				var param = {mid : mid,mtoid : mtoid,type:'toreply'};
				$.post(ctx+"/message/common/update",param, function(msg) {
					if(msg=='true'){
						if($(".to_reply").length<=0){
							refresh();
						} else {
							if($(".to_reply").length<=1){
								$("#template_to").empty();
							} else {
								showTemplTo(mid);
							}
						}
					}
				},"text").success(function() {
					refresh();
				})
			    .error(function() { 
			    	layer.alert("操作失败！",{icon:0,time: 1000});
			    });
			} else {
				layer.alert("请选择已发消息",{icon:0,time: 1000});
			}
		});
	}
	function refreshTemplTo(){
		if($(".toreply-div-select").length<=0){
			$("#template_to").empty();
		}
	}
	function initTemplTo(type, tid,$mtext,fiotype) {
		var obj = $("#template_to");
		obj.children().remove();
		if (type == 1) {
			var param = {tid : tid,type:'toreply'};
			$.getJSON(ctx+"/message/common/getMessageInfo",param, function(data) {
				if(data.result){
					obj.children().remove();
					var toList = data.TOLIST;
					if(toList){
						$(toList).each(function(index,e){
							var isreplyed = e.ISREPLED
							var reply = e.ISREPLED==0?"未回复":e.REPLYTEXT ;
							var css = e.ISREPLED==0?"to_reply2 toreply-div toreply-div-select":"toreply-div";
							var html = '';
								html += '<div class="'+css+'" data-mtoid="'+e.ID+'" data-mid="'+e.MID+'">';
								//if(e.MTOTYPE!=8)
									html += '<i class="layui-icon" onclick="talk(\''+ e.MTOER+'\',\''+e.MTOTYPE+'\')">&#xe63a;</i>';
								var checkbox = '<input type="checkbox" '+(e.ISREPLED==0?'':'checked disabled')+' /> ';
								html += '<span class="to_reply" data-mtoid="'+e.ID+'" data-mid="'+e.MID+'">'+checkbox+'<span value="">' +(e.MTOTYPE==8?'作业人['+e.NAME+']':e.NAME) + 
									'</span>[<span class="reply-txt">' + reply + '</span>]';
								if(e.ISREPLED==1)
									html += '[' + e.REPLYTIME + ']'
								html +='</span></div>';
							obj.append(html);
						})
					}
					initToReplyClick();
				} else {
					layer.alert("获取接收人失败，请联系管理员！",{icon:0,time: 1000});
				}
			});
		} else if (type == 2) {
			$(".mess-div").removeClass("mess-div-select");
				var fltid = $("#fltid").val();
				initSendMessTo(fltid,fiotype,tid,$mtext,obj);
				layui.use('element', function(){
					element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
					element.tabChange('demo', '11');
				});
			show(2);
		}
	}
	function initSendMessTo(fltid,fiotype,tid,$mtext,obj){
		var flightNumber = $("#flightNumber").val();
		var flightDate = $("#flightDate").val();
		fltid = $("#fltid").val();
		oth_fltid = $("#oth_fltid").val();
		var param = {tid : tid,flightDate : flightDate,flightNumber : flightNumber,fltid : fltid,oth_fltid : oth_fltid,fiotype : fiotype};
		$.getJSON(ctx+"/message/common/getTemplateInfo",param, function(data) {
			if(data.result){
				obj.children().remove();
				var template = data.TEMPLATE;
				if(template!=null){
					var mtext = template.MTEXT;//标题
					var ifreply = template.IFREPLY;//是否回复
					if(ifreply==1){
						$("#ifreply").prop("checked","checked");
					}
					if($mtext==''){
						$("#mtext").val(mtext);
					} else {
						$("#mtext").val($mtext);
					}
	
					var templateToList = data.TEMPLATETOLIST;
					if(templateToList.length>0){
						$(templateToList).each(function(index,e){
							var checkbox = '<input type="checkbox" checked/> ';
		 					var htm = '<div class="templto templto-div templto-div-select" data-id="'+e.ID+'">'+checkbox+'<span to-type="'+ e.MTOTYPE + '" ' +
		 					' class="templto2" to-er="' + e.MTOER + '" to-id="' + e.ID + '" t-id="' + e.TID +'" >' + (e.MTOTYPE==8?'作业人['+e.MTOERNAME+']':e.NAME) + '</span>';
		 					if(e.MTOTYPE!=8){
//		 						<i class="fa fa-search">&nbsp;</i>
		 						htm += '<i class="layui-icon" onclick="talk(\''+ e.MTOER+'\',\''+e.MTOTYPE+'\')">&#xe63a;</i>';
//		 						htm += '<i class="layui-icon" onclick="stop(\''+ e.MTOER+'\',\''+e.MTOTYPE+'\')">&#xe63a;</i>';
		 					}
		 					htm += '</div> ';
		 					obj.append(htm);
		 				});
					} else {
	//					var temp = '<div style="color : blue;padding:10px;"><span>无接收人</span></div> ';
	// 					obj.append(temp);
					}
					$(".template-btn").each(function(index,e){
						var $mid = $(e).attr("mid");
						if($mid == tid){
							$(e).addClass("template-select");
						} else {
							$(e).removeClass("template-select");
						}
					});
					initClick();
					initSendInfo();
				} 
			} else {
				layer.alert("获取接收人失败，请联系管理员！",{icon:0,time: 1000});
			}
		}).success(function(){
		}).fail(function(){
		});
	}
	function show(flag){
		pageNum =1;
		if(flag==1){//隐藏
 			$("#to-left").css('display','none'); 
			$("#templ-right").slideDown("slow");
 			$("#templ-right").removeClass();
			templRightAddClass(12);
 			setTimeout('refresh();',50);
 			setTimeout('templateSelect();',50);
			$("#show").show();
		} else if(flag==2) {
			$("#to-left").slideDown("slow");
			$("#templ-right").slideDown("slow");
			$("#templ-right").removeClass();
			templRightAddClass(8);
			$("#show").hide();
		} else if(flag ==3){
			var right_class = $("#templ-right").attr("class");
 			$("#to-left").css('display','none'); 
			$("#templ-right").removeClass();
			templRightAddClass(12);
			$("#show").show();
		} else if(flag ==4){
			$("#to-left").slideDown("slow");
			$("#templ-right").slideDown("slow");
			$("#templ-right").removeClass();
			templRightAddClass(8);
			$("#show").hide();
			initAutoTemplTo(senddefId);
		}
	}
	function templRightAddClass(row){
		$("#templ-right").addClass("col-md-" + row);
		$("#templ-right").addClass("col-xs-" + row);
		$("#templ-right").addClass("col-sm-" + row);
		$("#templ-right").addClass("height");
	}
	function templateSelect (){
		var messtype = $("li[class='layui-this']").attr("lay-id");
		if(messtype=='11'){
			select();
		} 
	}
	function select(){
		$(".template-btn").removeAttr("disabled","disabled");
		$(".template-btn").each(function(index,e){
			var $mid = $(e).attr("mid");
			if($mid == senddefId){
				$(e).addClass("template-select");
				initTemplTo(2,$mid,'','');
			} else {
				$(e).removeClass("template-select");
			}
		});
	}
	function showTemplTo(tid){
		initTemplTo(1,tid,'','');
		show(2);
	}
	function fileOnChange(){
		var obj = $("#fileInput")[0];
		for(var i=0;i<obj.files.length;i++){
			var file = obj.files[i];
			var tr = $(['<tr id="upload-'+ i +'">'
			            ,'<td>'+ file.name +'</td>'
			            ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
			            ,'<td>'
			              ,'<button class="layui-btn layui-btn-mini layui-btn-danger file-delete">删除</button>'
			            ,'</td>'
			          ,'</tr>'].join(''));
	      
	      //删除
	      tr.find('.file-delete').on('click', function(){
	    	var delVal = $("#delFile").val()+file.name;
	        $("#delFile").val(delVal);
	        tr.remove();
	      });
	      
	      $("#fileTable").append(tr);
		}
	}


	function initSendInfo(){
		$("#tid").val("");
		$("#toListStr").val("");
		$("#ifreply").removeAttr("checked");
		$("#fileTable").empty();
	}
