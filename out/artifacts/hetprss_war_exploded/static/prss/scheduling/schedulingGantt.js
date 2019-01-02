window.clickRowId = "";
window.clickRow;
window.types = $("#types").val().split(",");
window.wthMode = false;
var layer;// 初始化layer模块
var iframe;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		var form = layui.form;
		form.on('switch(switch)', function(data) {
			if(data.elem.name == "xxx"){
				if(data.elem.checked){
					ganttOptions.yData.queryParams.switches = 2;
					$("#walkthroughTools").css("visibility","visible");
					window.wthMode = true;
				}else{
					window.wthMode = false;
					ganttOptions.yData.queryParams.switches = $("input[name=zzz]").next().hasClass("layui-form-onswitch")?1:0;
					$("#walkthroughTools").css("visibility","hidden");
				}
			}else{
				ganttOptions.yData.queryParams.switches = data.elem.checked ? 1 : 0;
			}
			$("#SJgantt").SJgantt('refreshOptions',ganttOptions);
		});
	})
	document.body.onselectstart=$("#stopMenu")[0].oncontextmenu=function(){ return false;} ;
	getOptions();
	var defaultType = types.length==0?"":types[0];
	var ganttOptions = {
		url:ctx+"/scheduling/gantt/ganttData",
		showEmptyRow:false,
		height:$("body").height()-$("#tool-box").height(),
		yData:{
			url:ctx+"/scheduling/gantt/ganttYData",
			queryParams:{
				type:defaultType,
				switches:0
			},
			responseHandler:function (res) {
				res.unshift({"level":"1","name":"取消","pid":"0","id":"QX"});
				res.unshift({"level":"1","name":"待排","pid":"0","id":"DP"});
                return res;
            }
		},
		onclickOverlay:function(o){
			$.ajax({
				type:'post',
				url:ctx + "/scheduling/gantt/getGanttDetail",
				data:{
					id:o.id,
					schemaId:$("#schemaId").val()
				},
				dataType:'json',
				success:function(detail){
					o.detail = detail;
				}
			});
		}
	}
	if($("#schemaId").val() == "19"){
		ganttOptions.zoom = 1;
		ganttOptions.xAxis = {
				separation : 280
		};
	}
	$("#SJgantt").SJgantt(ganttOptions);
	//停用
	$("#stop").click(function(){
		var text = $(this).text();
		if(text=="停用"){
			doStopWork();
		} else {//恢复
			resumeWorker();
		}
		$("#stopMenu").hide();
	});
	//拨打电话
	$("#call").click(function(){
		var userId = $("#call").data("id");
		var userName = $("#call").data("name");
		$.ajax({
			type:'post',
			url:ctx+"/fltmonitor/getPhoneByUserId",
			data:{
				userId:userId
			},
			success:function(phone){
				if(!phone || phone == ""){
					layer.msg("人员未设置通信号码",{icon:7});
				}else if(phone =="error"){
					layer.msg("呼叫失败",{icon:2});
				}else {
					if ("WebSocket" in window){
						// 打开一个 web socket
						var url = "ws://127.0.0.1:7777";
						var ws = new WebSocket(url);
						ws.onopen = function(){
							var request = "MAKECALL:REQUEST:AUDIO:" + phone;
							ws.send(request);
							console.log("对"+phone+"发起呼叫");
						};
						ws.onmessage = function (evt) { 
							console.log(evt);
							layer.open({
								type:2,
								title:'通讯',
								area: ['300px', '370px'],
								content:ctx + '/message/common/totalk?phones='+phone+'&names='+encodeURIComponent(encodeURIComponent(userName))
							})
							ws.close();
						};
						ws.onclose = function(){ };
					} else {
						layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
					}
				}
			}
		});
	});
	//预分配
	$("#walkthrough").click(function(){
		var start = $("#walkthroughStart").val();
		var end = $("#walkthroughEnd").val();
		var reskind = $("#reskind").val();
		if(!start || start == ""){
			layer.msg("请输入开始时间",{icon:7},function(){
				$("#walkthroughStart").focus();
			});
			return false;
		}
		if(!end || end == ""){
			layer.msg("请输入结束时间",{icon:7},function(){
				$("#walkthroughEnd").focus();
			});
			return false;
		}
		var loading = layer.load(1);
		$.ajax({
			type:'post',
			url:ctx+"/scheduling/gantt/walkthroughTask",
			data:{
				start:start,
				end:end,
				reskind:reskind
			},
			success:function(data){
				layer.close(loading);
				if(data=="success"){
					layer.msg("任务已分配！",{icon:1,time:600},function(){
						$("#SJgantt").SJgantt('refresh');
					});
				}else if(data=="fail"){
					layer.msg("任务分配失败！",{icon:2});
				} else {
					layer.msg(data,{icon:7});
				}
			}
		});
	});
	//发布预排
	$("#saveWalkthrough").click(function(){
		var reskind = $("#reskind").val();
		layer.confirm('确认发布预排任务吗？', {icon: 3, title:'提示'}, function(index){
			var loading = layer.load(1);
			$.ajax({
				type : 'post',
				url : ctx + "/scheduling/gantt/saveWalkthrough",
				async : false,
				data : {
					reskind : reskind
				},
				success : function(data) {
					layer.close(loading);
					if(data=="success"){
						layer.msg("任务已启动！",{icon:1,time:600},function(){
							$("#SJgantt").SJgantt('refresh');
						});
					} else {
						layer.msg("发布失败"+data,{icon:7});
					}
				}
			});
		  	layer.close(index);
		});
	});
	//刷新
	if(!sessionStorage || !sessionStorage[$("#loginName").val()+$("#schemaId").val()+"refreshGantt"] || sessionStorage[$("#loginName").val()+$("#schemaId").val()+"refreshGantt"]=="undefined" || typeof(sessionStorage[$("#loginName").val()+$("#schemaId").val()+"refreshGantt"]) == "undefined"){
		autoReload("10");
	}else{
		autoReload(sessionStorage[$("#loginName").val()+$("#schemaId").val()+"refreshGantt"]);
	}
	$("#refresh").click(function() {
		//clearInterval(intervalObj);
		//$("#refresh").text("刷新");
		$("#SJgantt").SJgantt('refresh');
		/*if(sessionStorage[$("#loginName").val()+$("#schemaId").val()+"refreshGantt"]){
			sessionStorage.setItem($("#loginName").val()+$("#schemaId").val()+"refreshGantt",null);
		}*/
	})
	$("#refresh-c").click(function() {
		layer.open({
			title : '自定义刷新',
			content : '每<input type="number" min="1" class="layui-input layui-sm" style="width:120px;margin:0px 10px;display:inline-block"/>秒钟自动刷新',
			yes : function(index, layero) {
				var refreshTime = layero.find("input").val();
				if (refreshTime != null && refreshTime != "") {
					autoReload(refreshTime);
				}
				layer.close(index);
			}
		});
	})
	// 筛选
	$("#filter").click(function() {
		var filter = {};
		var filterIframe = layer.open({
			type : 1,
			title : "筛选",
			content : $("#filterDiv"),
			btn : [ "确定", "重置", "取消" ],
			success : function(layero) {
				layero.find(".layui-layer-content").css("cssText", "overflow:auto !important");
			},
			yes : function(index) {
				if ($("#filterForm .flightScope").val()){filter.flightScope = $("#filterForm .flightScope").val();};
				if ($("#filterForm .apron").val() && $("#filterForm .apron").val() != ''){filter.apron = $("#filterForm .apron").val();};
				if ($("#filterForm .terminal").val() && $("#filterForm .terminal").val() != ''){filter.terminal = $("#filterForm .terminal").val();};
				if ($("#filterForm .airline").val()){filter.airline = $("#filterForm .airline").val();};
				if ($("#filterForm .alnFlag").val()){filter.alnFlag = $("#filterForm .alnFlag").val();};
				if ($("#filterForm .acttypeSizes").val()){filter.acttypeSizes = $("#filterForm .acttypeSizes").val();};
				if ($("#filterForm .departAirport").val()){filter.departAirport = $("#filterForm .departAirport").val();};
				if ($("#filterForm .arriveAirport").val()){filter.arriveAirport = $("#filterForm .arriveAirport").val();};
				if ($("#filterForm .GAFlag").val()){filter.GAFlag = $("#filterForm .GAFlag").val();};
				if ($("#filterForm .identifying").val()){filter.identifying = $("#filterForm .identifying").val();};
				if ($("#filterForm .fltPropertys").val()){filter.fltPropertys = $("#filterForm .fltPropertys").val();};
				if ($("#filterForm .aircraft").val()){filter.aircraft = $("#filterForm .aircraft").val();};
				if ($("#filterForm .actStatus").val()){filter.actStatus = $("#filterForm .actStatus").val();};
				tableOptions.queryParams = {
					switches : switches,
					schema : "3",
					param : JSON.stringify(filter),
					suffix : ""
				}
				$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
				layer.close(index);
			},
			btn2 : function(index) {// 重置表单及筛选条件
				$("#filterForm").find("select").val('').removeAttr('selected');
				$("#filterForm .select2-selection__choice").remove();
				$("#filterForm .select2-search__field").attr("placeholder", "请选择");
				$("#filterForm .select2-search__field").css("width", "100%");

				$("#filterForm .select2-selection--single .select2-selection__rendered").empty();
				$("#filterForm .select2-selection--single .select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
				filter = {};
				return false;
			},
			btn3 : function(index) {// 关闭前重置表单及筛选条件
				layer.close(index);
			}
		});
		layer.full(filterIframe);
	})
	// 查看
	$("#show").click(function() {
		if (clickRowId == "") {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			content : ctx + "/flightDynamic/getFdById?type=show&id=" + clickRowId,// type用来区分页面来源，控制功能权限
			btn : [ "返回" ],
			success : function(layero, index) {
			}
		});

		layer.full(iframe);// 展开弹出层直接全屏显示
	});
	// 作业管理
	$("#jobManageBtn").click(function() {
		jobManage();
	});
	/**
	 * 作业管理
	 */
	function jobManage() {
		var schemaId = $("#schemaId").val();
		var inFltId = "";// 进港航班ID
		var outFltId = "";// 出港航班ID
		if (!clickRow) {
			layer.msg("请选择一条任务！", {
				icon : 7
			});
			return false;
		}
		if (clickRow.in_fltid) {
			inFltId = clickRow.in_fltid;
		}
		if (clickRow.out_fltid) {
			outFltId = clickRow.out_fltid;
		}
		layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			area : [ '100%', '100%' ],
			btn : [ "返回" ],
			content : [ ctx + "/scheduling/jobManage?schemaId=" + schemaId + "&inFltId=" + inFltId + "&outFltId=" + outFltId ]
		});
	}
	// 人员分工
	$("#divisionBtn").click(function() {
		doWorkerDivision();
	});
	// 人员计划
	$("#memberPlan").click(function() {
		memberPlan();
	});
	/**
	 * 人员分工
	 */
	function doWorkerDivision() {
		layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			area : [ '100%', '100%' ],
			btn : [ "返回" ],
			content : [ ctx + "/division/info/list" ]
		});
	}
	/**
	 * 员工计划
	 */
	function memberPlan() {
		layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			area : [ '100%', '100%' ],
			btn : [ "返回" ],
			content : [ ctx + "/arrange/empplan/list" ]
		});
	}
	//任务分配
	$("#autoManual").click(function() {
		iframe = layer.open({
			type : 2,
			title : "任务分配",
			closeBtn : false,
			area : ['400px','200px'],
			content : ctx + "/scheduling/list/autoManual",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var json = iframeWin.sub();
				var loading = layer.load(2, {
					shade : [ 0.1, '#000' ]
				});
				$.ajax({
					type : 'post',
					async : false,
					url : ctx + "/scheduling/list/updateAutoManual",
					data : {
						'autoManual' :JSON.stringify(json)
					},
					error : function() {
						layer.close(loading);
						layer.msg('保存失败！', {
							icon : 2
						});
					},
					success : function(data) {
						layer.close(loading);
						if (data == "success") {
							layer.msg('保存成功！', {
								icon : 1,
								time : 600
							},function(){
								layer.close(iframe);
							});
						} else {
							layer.msg('保存失败！', {
								icon : 2,
								time : 600
							});
						}
					}
				});
				return false;
			}
		});
	});
	// 报文
	$("#message").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		var msg = layer.open({
			type : 1,
			title : false,
			closeBtn : 0,
			area:[$("body").width()-200+"px",$("body").height()-200+"px"],
			content : $("#msgDiv"),
			btn : [ "确认" ],
			success : function(layero, index) {
				setTimeout(function() {
					$("#msgTable").bootstrapTable(messageTableOptions);
				}, 100);
			}
		});
		layer.full(msg);
	});
	var messageTableOptions = {
			url : ctx + "/flightDynamic/getMessage", // 请求后台的URL（*）
			method : "get", // 请求方式（*）
			dataType : "json", // 返回结果格式
			striped : true, // 是否显示行间隔色
			pagination : false, // 是否显示分页（*）
			cache : true, // 是否启用缓存
			undefinedText : '', // undefined时显示文本
			search : true, // 是否开启搜索功能
			showRefresh : true,
			detailView : true,
			searchOnEnterKey : true,
		    queryParams: function (params) {
		        return {
	                id:clickRowId
		        }
		    },
			height : $("body").height() - 56,
			detailFormatter : function(index, row) {
				return "<p style='margin-left:20px;'>" + row.XMLDATA + "</p>";
			},
			columns : [ {
				field : 'order',
				title : '序号',
				formatter : function(value, row, index) {
					return index + 1;
				}
			}, {
				field : 'FLIGHT_DATE',
				title : '航班日期'
			}, {
				field : 'TG_NAME',
				title : '报文类型'
			}, {
				field : 'FLIGHT_NUMBER',
				title : '航班号'
			}, {
				field : 'DEPART',
				title : '起场'
			}, {
				field : 'ARRIVAL',
				title : '落场'
			}, {
				field : 'messageTime',visible:false,
				title : '报文时间'
			}, {
				field : 'ACCEPT_TIME',
				title : '接收时间'
			}, {
				field : 'XMLDATA',
				title : '报文原文',
				visible : false
			} ]
		};
	// 异常情况查看
	$("#error").click(function() {
		iframe = layer.open({
			type : 2,
			title : "异常情况查看",
			area : [ '100%', '100%' ],
			content : ctx + "/flightDynamic/error"
		});
	});
	// 保障
	$("#ensure").click(
			function() {
				if (!clickRowId) {
					layer.msg('请选择一条任务', {
						icon : 2
					});
					return false;
				}
				var fltid, acttype, aircraft;
				if (ioFlag == "I") {
					fltid = clickRow.in_fltid ? clickRow.in_fltid : "";
					acttype = clickRow.in_acttype_code ? clickRow.in_acttype_code : "";
					aircraft = clickRow.in_aircraft_number ? clickRow.in_aircraft_number : "";

				} else if (ioFlag == "O") {
					fltid = clickRow.out_fltid ? clickRow.out_fltid : "";
					acttype = clickRow.out_acttype_code ? clickRow.out_acttype_code : "";
					aircraft = clickRow.out_aircraft_number ? clickRow.out_aircraft_number : "";
				}
				iframe = layer.open({
					type : 2,
					title : '航班保障图',
					closeBtn : false,
					content : ctx + "/fltmonitor?fltid=" + fltid ,// type用来区分页面来源，控制功能权限
					btn : [ "返回" ],
					success : function(layero, index) {
					}
				});

				layer.full(iframe);// 展开弹出层直接全屏显示
			});
	// 指令
	$('#order').click(function() {
		if (clickRowId == "") {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		sended(clickRowId);
	});
	// 指令
	function sended(fltid) {
		var num = "1";
		parent.parent.layer.open({
			type : 2,
			title : '指令消息',
			zIndex : 99999999999,
			maxmin : false,
			shadeClose : false,
			area : [ "1200px", "500px" ],
			content : ctx + "/message/history/list?num=" + num + "&fltid=" + fltid,
		});
	}
	// 获取下拉选项
	function getOptions() {
		$(".select2").each(function() {
			var type = $(this).data("type");
			if (type == "terminal" || type == "apron") {
				$(this).select2({
					placeholder : '请选择'
				});
			} else {
				$(this).select2(select2Options(type));
			}
		});
	}
	// select2设置
	function select2Options(type) {
		var temp = {
			allowClear : true,
			ajax : {
				url : ctx + '/flightDynamic/getOptions',
				type : 'post',
				dataType : 'json',
				delay : 250,
				data : function(params) {
					params.limit = 30;
					return {
						q : params.term,
						page : params.page,
						limit : params.limit,
						type : type
					};
				},
				processResults : function(data, params) {
					params.page = params.page || 1;
					return {
						results : data.item,
						pagination : {
							more : (params.page * params.limit) < data.count
						}
					};
				},
				cache : true
			},
			placeholder : '请选择',
			escapeMarkup : function(markup) {
				return markup;
			},
			language : "zh-CN",
			templateResult : formatRepo,
			templateSelection : formatRepoSelection
		}
		return temp;
	}
	// select2返回值处理
	function formatRepo(repo) {
		return repo.text;
	}
	// select2显示选项处理
	function formatRepoSelection(repo) {
		return repo.text;
	}
})
var intervalObj = null;
var refreshUnit = 1000;//刷新单位是分钟
function autoReload(interval){
	if(interval<10){
		layer.msg("最小刷新频率不要小于10秒！",{icon:7});
		return false;
	}
	sessionStorage.setItem($("#loginName").val()+$("#schemaId").val()+"refreshGantt",interval);
	var time = Number(interval) * refreshUnit;
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟";
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		$("#SJgantt").SJgantt('refresh');
	}, time);
}
function doStopWork(){
	$("#stopStart").val("");
	$("#stopEnd").val("");
	/*var len = $("#dataGrid").bootstrapTable("getSelections").length;
	if(len==0){
		layer.msg('请选择要停止工作的员工！', {
			icon : 2
		});
		return false;
	}*/
	
	var chkFlag=true;
	/*var selections = $('#dataGrid').bootstrapTable('getSelections');
	$.map(selections, function (row) {
		var login = row.loginName;
		if(row.blockupTime!=null){
			var tp = login.split("@")[1];
			layer.msg(tp+'员工是停止状态,无法再次停止工作！', {
				icon : 2
			});
			chkFlag=false;
		}
	});*/
	
	if(chkFlag){
		var ids = [$("#stop").data("id")];
		layer.open({
			type:1,
			title: "停用时段",
			offset: '20px',
			content:$("#planRangeDate"),
			btn:["确认","返回"],
			success:function(layero,index){
			},
			yes:function(index, layero){
				var times = "";
				var time1 = layero.find(".rangeDate").eq(0).val();
				var time2 = layero.find(".rangeDate").eq(1).val();
				if(time1==""||time2==""){
					layer.msg('请选择要停用开始时间或结束时间', {
						icon : 2
					});
					return false;
				}else{
					times = time1+":00"+","+time2+":00";
					var reason = $("#planRangeDate select option:selected").text();
					$.ajax({
						type : 'post',
						url : ctx + "/arrange/empplan/saveStopPlan",
						data : {
							times : times,
							reason:reason,
							ids:ids
						},
						success : function(msg) {
							if (msg == "check") {
								layer.msg('此人员还有未完成的任务，请处理！', {
									icon : 2
								});
								return false;
							}else if (msg == "success") {
								layer.msg('保存成功！', {
									icon : 1,
									time : 600
								});
								layer.close(index);
								$("#SJgantt").SJgantt('refresh');
							}
						}
					});
				}
			}
		});
	}
}
/**
 * 恢复人员停用
 */
function resumeWorker(){
	var ids = [$("#stop").data("id")];
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		url : ctx + "/arrange/empplan/resumePlan",
		data : {
			ids:ids
		},
		success : function(msg) {
			if (msg == "success") {
				$("#SJgantt").SJgantt('refresh');
			}
			layer.close(loading);
		}
	});
}