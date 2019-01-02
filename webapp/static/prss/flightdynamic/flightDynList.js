var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
var clickRow;
var clickRowIndex;
var filter = {};// 用于筛选
var iframe;
var ioFlag;
var switches = 0;
var flagBGS = 0;
var sortName;
var sortOrder;
var searchOption="";
var AUTO_RELOAD_SESSION = null;// 自动刷新频率缓存
var globleForm;
var overAllIds = new Array();  //全局数组
$(function() {
	document.oncontextmenu = function(e) {
		return false;
	}// 兼容火狐、谷歌浏览器
	$("#baseTable").bind("contextmenu", function(){
	    return false;
	})
	$("#baseTables").css("cssText","height:calc(100% - "+$("#totalBox").outerHeight(true)+"px)")
	AUTO_RELOAD_SESSION = $("#loginName").val() + "1" + "refresh";

	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		var form = layui.form;
		globleForm=form;
		form.on('switch(switch)', function(data) {
			switches = data.elem.checked ? 1 : 0;
			tableOptions.queryParams.switches = switches;
			baseTable.bootstrapTable('refresh');
			$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
			
		});
		form.on('switch(BGS)', function(data) {
			flagBGS = data.elem.checked ? 1 : 0;
			tableOptions.queryParams.flagBGS = flagBGS;
			baseTable.bootstrapTable('refresh');
			$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
			
		});
		listenFilterCheckbox();
	})
	$("#baseTable").each(function() {
		$(this).on('load-success.bs.table', function(thisObj) {
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody[0].removeEventListener('ps-y-reach-end', load);
			tableBody[0].addEventListener('ps-y-reach-end', load);
		});
	});
	baseTable = $("#baseTable");
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格

	getOptions();
	
	$(".mutilselect2").select2({
		placeholder : "请选择",
		width : "100%",
		language : "zh-CN"
    });
	
	//为搜索加下拉搜索条件
	$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
	//为右键菜单绑定事件
	appendEventForMenu();
	// 单击
	$("#baseTable").mousedown(function(e) {
		// 右键为3
		if (3 == e.which) {
			if (!clickRowId) {
				layer.alert("请先选中一条进港或出港！", {
				icon : 0,
				time : 4000
				});
			}
			var th = $("#baseTable").find(".separator").eq(0).next();
			if (th.length > 0) {
				var borderLeft = th.offset().left;
				if (e.pageX - borderLeft > 0) {
					ioFlag = "O";
				} else {
					ioFlag = "I";
				}
			} else {
				ioFlag = "I";
			}
			getMessageMenu(ioFlag);
		}
		if (1 == e.which) {//左键
			var th = $("#baseTable").find(".separator").eq(0).next();
			if (th) {
				var borderLeft = th.offset().left;
				if (e.pageX - borderLeft > 0) {
					ioFlag = "O";
				} else {
					ioFlag = "I";
				}
			} else {
				ioFlag = "I";
			}
		}
	});
	// 刷新
	if (!sessionStorage) {
		console.log("该浏览器不支持本地缓存");
	} else {
		if (sessionStorage.getItem(AUTO_RELOAD_SESSION) && sessionStorage.getItem(AUTO_RELOAD_SESSION) != 'null') {
			autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
		} else {
			autoReload("10");
		}
	}
	$("#refresh").click(function() {
		clearSelectedRow();
		clearCheckRow();
		refreshFunc();
		//clearInterval(intervalObj);
		//$("#refresh").text("刷新");
		/*$(this).find(".fa-clock-o").addClass("fa-spinner").removeClass("fa-clock-o");
		baseTable.bootstrapTable('refresh');*/
		/*if (sessionStorage && sessionStorage.getItem(AUTO_RELOAD_SESSION) && sessionStorage.getItem(AUTO_RELOAD_SESSION) != 'null') {
			sessionStorage.removeItem(AUTO_RELOAD_SESSION);
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
		var filterIframe = layer.open({
		type : 1,
		title : "筛选",
		area : ["100%","100%"],
		content : $("#filterDiv"),
		btn : [ "确定", "重置", "取消" ],
		yes : function(index) {
			clearSelectedRow();
			clearCheckRow();
			// 航空公司
			if ($("input[name='airlinevalue']").val()==''||$("input[name='airlinevalue']").val()) {
				filter.airline = $("input[name='airlinevalue']").val();
			}
			// 机坪区域
			if ($("#filterForm .apron").val()==''||$("#filterForm .apron").val()) {
				filter.apron = $("#filterForm .apron").val()=='!'?'':$("#filterForm .apron").val();
			}
			// 廊桥/远机位
//			if ($("#filterForm .GAFlag").val()==''||$("#filterForm .GAFlag").val()) {
//				filter.GAFlag = $("#filterForm .GAFlag").val()=='!'?'':$("#filterForm .GAFlag").val();
//			}
			var GAFlagStr = ($("#GAFlag").val())?$("#GAFlag").val().join(','):"";
			if(GAFlagStr.length > 0){
				filter.GAFlag=GAFlagStr;
			}
			//filter.GAFlag=$("#GAFlag").val();
			// 起场
			if ($("input[name='departAirportvalue']").val()==''||$("input[name='departAirportvalue']").val()) {
				filter.departAirport = $("input[name='departAirportvalue']").val();
			}
			// 落场
			if ($("input[name='arriveAirportvalue']").val()==''||$("input[name='arriveAirportvalue']").val()) {
				filter.arriveAirport = $("input[name='arriveAirportvalue']").val();
			}
			// 国内/国际
			/*if ($("#filterForm .alntype").val()==''||$("#filterForm .alntype").val()) {
				filter.alntype = $("#filterForm .alntype").val()=='!'?'':$("#filterForm .alntype").val();
			}*/
			var alntypeStr = ($("#filterForm .alntype").val())?$("#filterForm .alntype").val().join(','):"";
			if(alntypeStr.length > 0){
				filter.alntype=alntypeStr;
			}
			// 性质
			/*if ($("#filterForm .fltPropertys").val()==''||$("#filterForm .fltPropertys").val()) {
				filter.fltPropertys = $("#filterForm .fltPropertys").val()=='!'?'':$("#filterForm .fltPropertys").val();
			}*/
			var fltPropertysStr = ($("#filterForm .fltPropertys").val())?$("#filterForm .fltPropertys").val().join(','):"";
			if(fltPropertysStr.length > 0){
				filter.fltPropertys=fltPropertysStr;
			}
			// 飞机类型
//			if ($("input[name='actkinds']").val()==''||$("input[name='actkinds']").val()) {
//				filter.actkinds = $("input[name='actkinds']").val();
//			}
			var actKindsArr=new Array();
			$('input[name="actkinds"]:checked').each(function(){
				actKindsArr.push($(this).val());
			});
			var kindsStr=actKindsArr.join(',');
			if(kindsStr.length>0){
				filter.actkinds=kindsStr;
			}
			// 除冰航班
			/*if ($("#filterForm .iceFlt").val()==''||$("#filterForm .iceFlt").val()) {
				filter.iceFlt = $("#filterForm .iceFlt").val()=='!'?'':$("#filterForm .iceFlt").val();
			}*/
			var iceFltStr = ($("#filterForm .iceFlt").val())?$("#filterForm .iceFlt").val().join(','):"";
			if(iceFltStr.length > 0){
				filter.iceFlt=iceFltStr;
			}
			// 状态
			/*if ($("#filterForm .actStatus").val()==''||$("#filterForm .actStatus").val()) {
				filter.actStatus = $("#filterForm .actStatus").val()=='!'?'':$("#filterForm .actStatus").val();
			}*/
			var actStatusStr = ($("#filterForm .actStatus").val())?$("#filterForm .actStatus").val().join(','):"";
			if(actStatusStr.length > 0){
				filter.actStatus=actStatusStr;
			}
			// 延误
//			if ($("#filterForm .delay").val()==''||$("#filterForm .delay").val()) {
//				filter.delay = $("#filterForm .delay").val();
//			}
			//filter.delay= $("#delay").val();
			var delayStr = ($("#delay").val())?$("#delay").val().join(','):"";
			if(delayStr.length > 0){
				filter.delay=delayStr;
			}
			// 延误原因
//			if ($("#filterForm .delyReson").val()==''||$("#filterForm .delyReson").val()) {
//				filter.delyReson = $("#filterForm .delyReson").val()=='!'?'':$("#filterForm .delyReson").val();
//			}
			//filter.delyReson=$("#delyReson").val();
			var delyResonStr = ($("#delyReson").val())?$("#delyReson").val().join(','):"";
			if(delyResonStr.length > 0){
				filter.delyReson=delyResonStr;
			}
			// 机号
//			if ($("input[name='aircraftNovalue']").val()==''||$("input[name='aircraftNovalue']").val()) {
//				filter.aircraftNo = $("input[name='aircraftNovalue']").val();
//			}
			filter.aircraftNo=$("input[name='aircraftNo']").val();
			// 机型
			if ($("input[name='actTypevalue']").val()==''||$("input[name='actTypevalue']").val()) {
				filter.actType = $("input[name='actTypevalue']").val();
			}
			//机位
			var bayStr = ($("#bay").val())?$("#bay").val().join(','):"";
			if(bayStr.length > 0){
				filter.bay=bayStr;
			}
			//filter.bay=$("#bay").val();
			// 登机口
			if ($("input[name='gatevalue']").val()==''||$("input[name='gatevalue']").val()) {
				filter.gate = $("input[name='gatevalue']").val();
			}
			//时间类型
				filter.timetype = $("#timetype").val();
			// 开始时间 如果没有填写 默认从0点开始
			if(filter.timetype!=""){
				if ($("#beginTime").val()=='') {
					filter.beginTime = "0000";
				}else{
					filter.beginTime = $("#beginTime").val();
				}
				// 结束时间 如果没有填写 默认23:59结束
				if ($("#endTime").val()=='') {
					filter.endTime = "2359";
				}else{
					filter.endTime = $("#endTime").val();
				}
			}
			// 始发/航后
			var stayFlagStr = ($("#filterForm .stayFlag").val())?$("#filterForm .stayFlag").val().join(','):"";
			if(stayFlagStr.length > 0){
				filter.stayFlag=stayFlagStr;
			}
			//前后小时数
			if ($("#beforeHour").val()=='') {
				filter.beforeHour = "";
			}else{
				filter.beforeHour = $("#beforeHour").val();
			}
			
			if ($("#afterHour").val()=='') {
				filter.afterHour = "";
			}else{
				filter.afterHour = $("#afterHour").val();
			}
			
			//日期
			if ($("#searchFlightDate").val()=='') {
				filter.searchFlightDate = "";
			}else{
				filter.searchFlightDate = $("#searchFlightDate").val();
			}
			if (!vaildFilterForm()) {
				return false;
			}
//			console.log(JSON.stringify(filter))
			tableOptions.queryParams.param =JSON.stringify(filter);
			$("#baseTable").bootstrapTable('refreshOptions', tableOptions);	
			//为搜索加下拉搜索条件
			$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
			filter = {};
			layer.close(index);
		
		},
		btn2 : function(index) {// 重置表单及筛选条件
			$("#filterForm").find("select").val('').removeAttr('selected');
			$("#filterForm .select2-selection__choice").remove();
			$("#filterForm .select2-search__field").attr("placeholder", "请选择");
			$("#filterForm .select2-search__field").css("width", "100%");
			$("#filterForm").find("input").attr("checked",false);
			globleForm.render('checkbox');
			$("#filterForm").find("input").not('input[type=checkbox]').val('');
			$("#filterForm .select2-selection--single .select2-selection__rendered").empty();
			$("#filterForm .select2-selection--single .select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
			filter = {};
			return false;
		},
		btn3 : function(index) {// 关闭前重置表单及筛选条件
			layer.close(index);
		}
		});
		//layer.full(filterIframe);
	})
	// 设置
	$("#setting").click(function() {
		var set = layer.open({
			type : 2,
			title : false,
			closeBtn:false,
			area:["100%","100%"],
			content : ctx + "/flightDynamic/settingList?schema=1",
			btn : [ "确定", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var data = iframeWin.getHeadData();
				$.ajax({
					type : 'post',
					url : ctx + "/flightDynamic/saveHeadInfo",
					data : {
						data : data,
						schema : "1"
					},
					success : function(msg) {
						if (msg == "success") {
							tableOptions.columns = getBaseColumns();
							baseTable.bootstrapTable('refreshOptions', tableOptions);
							baseTable.bootstrapTable('refresh');
							$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
						}
					}
				});
				layer.close(index);
			}
		})
	})
	// 查看
//	$("#show").click(function() {
//		if (!clickRowId) {
//			layer.msg('请选择一条动态', {
//				icon : 2
//			});
//			return false;
//		}
//		iframe = layer.open({
//			type : 2,
//			title : false,
//			closeBtn : false,
//			content : ctx + "/flightDynamic/listDetailInfo?infltid="+clickRow.in_fltid+"&outfltid="+clickRow.out_fltid,
//			btn : [ "返回" ],
//			success : function(layero, index) {
//			}
//		});
//		layer.full(iframe);// 展开弹出层直接全屏显示
//	});
	// 保障
	$("#ensure").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条动态', {
				icon : 2
			});
			return false;
		}
		// 修改关闭按钮样式
		$('body').on('layui.layer.open', function() {
			$('.layui-layer-close').removeClass('layui-layer-close2').addClass('layui-layer-close1');
		});
		var inFltid = clickRow.in_fltid ? clickRow.in_fltid : '';
		var outFltid = clickRow.out_fltid ? clickRow.out_fltid : '';
		iframe = layer.open({
		type : 2,
		title : false,
		closeBtn : 1,
		content : ctx + "/fltmonitor?inFltid=" + inFltid + "&outFltid=" + outFltid,// type用来区分页面来源，控制功能权限
		// btn : [ "返回" ],
		});
		layer.full(iframe);// 展开弹出层直接全屏显示

	});
	// 打印
	$("#print").click(function() {
		var data = tableData;
		var columns = $.map(baseTable.bootstrapTable('getOptions').columns[0], function(col) {
			if (col.field != "order" && col.field != "checkbox") {
				return {
				"field" : col.field,
				"title" : col.title
				};
			} else {
				return null;
			}
		});
		$("#printTitle").text(JSON.stringify(columns));
		$("#printData").text(JSON.stringify(data));
		$("#printForm").submit();
	});
	//离港导出
	$("#printPassenger").click(function() {
		$("#printPassengerForm").submit();
	});
	//值机柜台默认分配
	$("#counterDefultAllot").click(function(){
		iframe = layer.open({
			type : 2,
			title : "值机柜台默认分配",
			closeBtn : false,
			area:["100%","100%"],
			content : ctx + "/flightDynamic/counterDefultAllotList?dim=D&island=A",
			btn : ['保存','返回'],
			yes : function(index, layero) {
				//保存
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
			}
		});
	});
	//新增
	$('#create').click(function() {
		iframe = layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			area:["100%","100%"],
			content : ctx + "/flightDynamic/form?dataSource=1",
			btn : ['保存','新增行','删除行','返回'],
			yes : function(index, layero) {
				//保存
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
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
			}
		});
	});
	// 编辑
//	$('#modify').click(function() {
//		if (!clickRowId) {
//			layer.msg('请选择一条动态', {
//				icon : 2
//			});
//			return false;
//		}
//		iframe = layer.open({
//			type : 2,
//			title : false,
//			closeBtn : false,
//			content : ctx + "/flightDynamic/editForm?inFltId=" + clickRow.in_fltid + "&outFltId=" + clickRow.out_fltid,
//			btn : [ "保存", "返回" ],
//			yes : function(index, layero) {
//				var iframeWin = window[layero.find('iframe')[0]['name']];
//				iframeWin.doSubmit();
//				return false;
//			}
//			});
//			layer.full(iframe);
//	});

	// 要客计划导入
	$('#importvip').click(function() {
		iframe = layer.open({
			type : 2,
			title : "要客计划导入管理",
			area : [ '900px', '400px' ],
			content : ctx + "/flightDynamic/openImportvip",
			btn : [ "返回" ]
		});
	});
	// 指令
	$('#order').click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条动态', {
				icon : 2
			});
			return false;
		}
		sended(clickRow);
	});

	// 回收站
//	$("#recycle").click(function() {
//		if (!clickRowId) {
//			layer.msg('请选择一条动态', {
//				icon : 2
//			});
//			return false;
//		}
//		layer.open({
//		type : 1,
//		title : '回收站',
//		content : $("#recycleBox"),
//		btn : "确定",
//		yes : function(index, layero) {
//			var type = layero.find("input[type=radio]:checked").data("type");
//			$.ajax({
//			type : 'post',
//			url : ctx + "/flightDynamic/putRecycle",
//			data : {
//			gbak : clickRowId,
//			inFltid : clickRow.in_fltid,
//			outFltid : clickRow.out_fltid,
//			type : type
//			},
//			success : function(msg) {
//				if (msg == "success") {
//					baseTable.bootstrapTable('refresh');
//				}
//			}
//			});
//			layer.close(index);
//		}
//		});
//	})

	// 报文
	$("#message").click(function() {
		var msg = layer.open({
		type : 2,
		title : "报文",
		//closeBtn : 0,
		area : [ $("body").width() - 200 + "px", $("body").height() - 200 + "px" ],
//		content : $("#msgDiv"),
		content : ctx + '/flightDynamic/telegraph?isHis=0',
		//btn : [ "确认" ],
		success : function(layero, index) {
//			setTimeout(function() {
//				$("#msgTable").bootstrapTable(messageTableOptions);
//			}, 100);
		}
		});
		layer.full(msg);
	});
	
	//延误原因录入
	$("#delay").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条动态', {
				icon : 2
			});
			return false;
		}
		if (!clickRow.out_fltid) {
			layer.msg('请选择出港动态', {
				icon : 2
			});
			return false;
		}
		var id = clickRow.out_fltid;
		iframe = layer.open({
			type : 2,
			title : "延误信息管理",
			area : [ '700px', '400px' ],
			btn : [ "保存", "返回" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveDelay();
			},
			content : [ ctx + "/flightDynamic/delay?fltid=" + id, "no" ]
		})
	});
	
	// 异常情况查看
	$("#error").click(function() {
		iframe = layer.open({
		type : 2,
		title : "异常情况查看",
		area : [ '100%', '100%' ],
		content : ctx + "/flightDynamic/error"
		});
	});
	
	// 部门值班日志查看
	$("#dutyLog").click(function() {
		iframe = layer.open({
			type : 2,
			title : "部门值班日志查看",
			area : [ '1000px', '500px' ],
			content : ctx + "/sys/maintain?tabId=32"
		});
	});
	//删除功能
	$("#delFlt").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条航班！', {
				icon : 2
			});
			return false;
		}
		var inFltId = "";//进港航班id
		var outFltId = "";//出港航班id
		if (clickRow.in_fltid) {
			inFltId = clickRow.in_fltid
		}
		if (clickRow.out_fltid) {
			outFltId = clickRow.out_fltid
		}
		var confirm = layer.confirm('您确定要删除选中航班？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				type : 'post',
				url : ctx + "/flightDynamic/delFlt",
				data : {
					inFltId:inFltId,
					outFltId:outFltId
				},
				dataType:"json",
				success : function(res) {
					if (res&&res.status=="1") {
						layer.msg('删除航班成功', {
							icon : 1
						});
						$("#baseTable").bootstrapTable('refresh');
					} else {
						layer.msg('删除航班失败'+res.error, {
							icon : 2
						});
					}
				}
			});
		});
	});
	// 机号拆分
	$("#fdRel").click(function() {		
		var row = $('#baseTable').bootstrapTable('getSelections');
		var fltIds="";
		for (var i = 0; i < row.length; i++) {
			if (row[i].in_fltid!=""&&row[i].in_fltid!=null) {
				fltIds+=row[i].in_fltid+",";
			}
			if (row[i].out_fltid!=""&&row[i].out_fltid!=null) {
				fltIds+=row[i].out_fltid+",";
			}
		}
		iframe = layer.open({
			type : 2,
			title : false,
			closeBtn:false,
			area : [ '100%', '100%' ],
			content : ctx + "/fdRel/list?fltIds="+fltIds,
			btn : [ "保存", "关闭" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveRel();
			}
		});	
	});
	
	//配载初始航班txt导出
	$('#initialFlt').click(function() {
		var outFltIds="";
		for (var i = 0; i < tableData.length; i++) {
			if (tableData[i].out_fltid!=""&&tableData[i].out_fltid!=null) {
				outFltIds+=tableData[i].out_fltid+",";
			}
		}
		if (outFltIds=='') {
			layer.msg('没有出港航班任务', {
				icon : 2
			});
			return false;
		}else{
			outFltIds = outFltIds.substr(0,outFltIds.length-1);
		}
		$("#exportForm").attr("action",ctx + "/flightDynamic/exportInitialInfo?exportType=initialFlt&outFltIds="+outFltIds);
		$("#exportForm").submit();
	});
	
	//配载初始登机口txt导出
	$('#initialGate').click(function() {
		var outFltIds="";
		for (var i = 0; i < tableData.length; i++) {
			if (tableData[i].out_fltid!=""&&tableData[i].out_fltid!=null) {
				outFltIds+=tableData[i].out_fltid+",";
			}
		}
		if (outFltIds=='') {
			layer.msg('没有出港航班任务', {
				icon : 2
			});
			return false;
		}else{
			outFltIds = outFltIds.substr(0,outFltIds.length-1);
		}
		$("#exportForm").attr("action",ctx + "/flightDynamic/exportInitialInfo?exportType=initialGate&outFltIds="+outFltIds);
		$("#exportForm").submit();
	});

	// 黄卡
//	$("#yellowCard").click(function() {
//		if (clickRowId == "") {
//			layer.msg('请选择一个动态', {
//				icon : 2
//			});
//			return false;
//		}
//		iframe = layer.open({
//		type : 2,
//		title : "黄卡",
//		area : [ '100%', '100%' ],
//		content : ctx + "/flightDynamic/yellowCard?id=" + clickRowId,
//		btn : [ "取消" ]
//		});
//	});

//	var messageTableOptions = {
//	url : ctx + "/flightDynamic/getMessage", // 请求后台的URL（*）
//	method : "get", // 请求方式（*）
//	dataType : "json", // 返回结果格式
//	striped : true, // 是否显示行间隔色
//	pagination : false, // 是否显示分页（*）
//	cache : true, // 是否启用缓存
//	undefinedText : '', // undefined时显示文本
//	search : true, // 是否开启搜索功能
//	showRefresh : true,
//	detailView : true,
//	searchOnEnterKey : true,
//	queryParams : function(params) {
//		return {
//			id : clickRowId
//		}
//	},
//	height : $("body").height() - 56,
//	detailFormatter : function(index, row) {
//		return "<p style='margin-left:20px;'>" + row.XMLDATA + "</p>";
//	},
//	columns : [ {
//	field : 'order',
//	title : '序号',
//	formatter : function(value, row, index) {
//		return index + 1;
//	}
//	}, {
//	field : 'FLIGHT_DATE',
//	title : '航班日期'
//	}, {
//	field : 'TG_NAME',
//	title : '报文类型'
//	}, {
//	field : 'FLIGHT_NUMBER',
//	title : '航班号'
//	}, {
//	field : 'DEPART',
//	title : '起场'
//	}, {
//	field : 'ARRIVAL',
//	title : '落场'
//	}, {
//	field : 'messageTime',
//	visible : false,
//	title : '报文时间'
//	}, {
//	field : 'ACCEPT_TIME',
//	title : '接收时间'
//	}, {
//	field : 'XMLDATA',
//	title : '报文原文',
//	visible : false
//	} ]
//	};
})
// 表格列选项默认设置
jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
jQuery.fn.bootstrapTable.columnDefaults.align = "center";
var limit = 100;
var page = 1;
var tableData;
var tableOptions = {
		url : ctx + "/flightDynamic/getDynamic", // 请求后台的URL（*）
		method : "get", // 请求方式（*）
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : false, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : false, // 是否显示全选
		toolbar : $("#tool-box"), // 指定工具栏dom
		search : true, // 是否开启搜索功能
		searchOnEnterKey : true,
		columns : getBaseColumns(),
		contextMenu : $("#loginName").val()=="anonymous"?"":"#context-menu",//匿名账号屏蔽右键
		queryParams : {
			switches : switches,
			flagBGS : flagBGS,
			schema : "1",
			time : "1",//默认显示今日航班动态列表
			suffix : ""
		},
		responseHandler : function(res) {
			tableData = res;
			$("#total").text(res.length);
			return res.slice(0, limit);
		},
		onLoadSuccess:function(){
			$("#refresh").find(".fa-spinner").addClass("fa-clock-o").removeClass("fa-spinner");
		},
		customSearch : function(text) {
//			clearSelectedRow();
			if (text) {
				text=text.toUpperCase();
				var searchData = [];
				if (searchOption != "") {
					for (var i = 0; i < tableData.length; i++) {
						var rowData = tableData[i];
						for ( var key in rowData) {
							if (key.toUpperCase().indexOf(searchOption) > -1) {
								if (rowData[key] && rowData[key].toString().indexOf(text) != -1) {
									searchData.push(rowData);
									break;
								}
							}
						}
					}
				} else {
					for (var i = 0; i < tableData.length; i++) {
						var rowData = tableData[i];
						for ( var key in rowData) {
							if (rowData[key] && rowData[key].toString().indexOf(text) != -1) {
								searchData.push(rowData);
								break;
							}
						}
					}
				}
				this.data = searchData;
				if(this.options.data){
					for(var i=0;i<searchData.length;i++) {
						//判断元素是否存在于this.options.data中，如果不存在则插入到new_arr的最后
					　　	if($.inArray(searchData[i],this.options.data)==-1) {
					　　　　	this.options.data.push(searchData[i]);
					　　	}
					}
				}
				$("#total").text(searchData.length);
			} else {
				this.data = tableData;
				$("#total").text(tableData.length);
			}
			if(clickRowIndex){
				$("#baseTable tr[data-index='"+clickRowIndex+"']").addClass("clickRow");
			}
		},
		customSort : function(fieldName, fieldOrder) {
			sortName = fieldName;
			sortOrder = fieldOrder;
			this.data.sort(function(a, b) {
				var order = sortOrder === 'desc' ? -1 : 1
				var val1 = a[sortName];
				var val2 = b[sortName];
				if (val1 === undefined || val1 === null) {
					val1 = '';
				}
				if (val2 === undefined || val2 === null) {
					val2 = '';
				}
				if ($.isNumeric(val1) && $.isNumeric(val2)) {
					// Convert numerical values form string to float.
					val1 = parseFloat(val1);
					val2 = parseFloat(val2);
					if (val1 < val2) {
						return order * -1;
					}
					return order;
				}
		
				if (val1 === val2) {
					return 0;
				}
				// If value is not a string, convert to string
				if (typeof val1 !== 'string') {
					val1 = val1.toString();
				}
				if (typeof val2 !== 'string') {
					val2 = val2.toString();
				}
		
				if (val1.localeCompare(val2, "zh") === -1) {
					return order * -1;
				}
		
				return order;
			});
			this.data = this.data.slice(0, limit);
		},
		// 右键菜单
		onContextMenuItem : function(row, $el) {
			if ($el.data("item") == "instructionView") {
				sended(row);
				// 右键离港信息
			} else if ($el.data("item") == "departPsg") {
				departPsg(row);
			} else if ($el.data("item") == "sendMessage") {
				sendMessage(row);
			} else if ($el.data("item") == "action2") {
			} else if ($el.data("item") == "fltDetail") {
			} else if ($el.data("item") == "passenger") {
				passenger(row);
			} else if ($el.data("item") == "baggage") {
				baggage(row);
			} else if ($el.data("item") == "timeDynamic") {
				timeDynamic(row);
			} else if ($el.data("item") == "guaranteeData") {
				guaranteeData(row);
			} else if ($el.data("item") == "resourseState") {
				resourseState(row);
			} else if ($el.data("item") == "massageChange") {
				massageChange(row);
			} else if ($el.data("item") == "cancle") {
				cancelFlt(row);
			} else if ($el.data("item") == "delFlt") {
				delFlt(row);
			} else if ($el.data("item") == "vipInfo") {
				vipInfo(row);
			} else if($el.data("item") == "fltDataInput") {
				fltDataInput(row);
			} else if($el.data("item") == "exception"){
				openException(row);
			} else if($el.data("item") == "delay"){
				delayInfo(row);
			} else if ($el.data("item") == "video") {
            } else if($el.data("item") == "seat"){
                video(row, $el.data("item"));
            } else if($el.data("item") == "gate"){
                video(row, $el.data("item"));
            } else if($el.data("item") == "carousel"){
                video(row, $el.data("item"));
            } else if($el.data("item") == "counter"){
                video(row, $el.data("item"));
            } else if($el.data("item") == "runway"){
                video(row, $el.data("item"));
            } else {
				openMessage(row, $el.data("item"));
			}
		
		},
		
		onCheck:function(row,$element){
	        index = $element.data('index');
	        if ($.inArray(index,overAllIds)<0) {
	        	overAllIds.push(index);
	        }
	     },
	    onUncheck : function(row, $element) {
			index = $element.data('index');
			if ($.inArray(index, overAllIds) >= 0) {
				overAllIds.remove(index);
			}
	    },
		onClickRow : function(row, tr, field) {
			if (field) {
				clearSelectedRow();
				// row = baseTable.bootstrapTable("getData")[tr.data("index")];
				var th = $("#baseTable").find(".separator").eq(0);
				var ex = $("th[data-field=" + field + "]").offset().left;
				if (th.length > 0) {
					var borderLeft = th.offset().left + th.width();
					if (ex >= borderLeft) {
						if (row.out_fltid) {
							clickRowId = row.out_fltid;
							ioFlag = "O";
						} else {
							return false;
						}
					} else {
						if (row.in_fltid) {
							clickRowId = row.in_fltid;
							ioFlag = "I";
						} else {
							return false;
						}
					}
				} else {
					clickRowId = row.out_fltid;
				}
				if (clickRowId) {
					clickRow = row;
					clickRowIndex=$(tr).attr("data-index");
					$(tr).addClass("clickRow");
				}
			}
		},
		onDblClickRow : function(row,tr,field){
			//baochl_20180717 匿名账号禁止操作
			if($("#loginName").val()=="anonymous"){
				return false;
			}
//			双击修改航班动态的全部字段，除了设计好的双击修改机号等功能  双击其他字段均可进入此页面
			if(field!="checkbox" && field!="order" && field!="in_aircraft_number" &&  field!="out_aircraft_number" 
				&& field!="in_acttype_code"  && field!="out_acttype_code" && field!="in_property_code" && field!="out_property_code"
				&& field!="in_flt_attr_code" && field!="out_flt_attr_code" && field!="in_flight_date" && field!="out_flight_date"
				&& field!="in_actstand_code" && field!="out_actstand_code" && field!="in_delay_reason" && field!="in_delay_reason_detail"
				&& field!="out_delay_reason" && field!="out_delay_reason_detail" && field!="out_release_reason" && field!="out_release_reason_detail"
				&& field!="depart_sort"	&& field!="out_diversion_port" && field!="in_diversion_port" &&field!="out_diversion_res"
				&& field!="in_diversion_res" && field!="out_diversion_atd"	&& field!="in_diversion_atd" && field!="admin_pushout_tm"
				&& field!="in_bag_crsl" && field!="out_gate" && field!="in_ground_hand_over_tm" && field!="out_ground_hand_over_tm"
				&& field!="out_boarding_status"	&& field!="out_checkin_counter" && field!="out_checkin_status"
				&& field!="out_brd_btm" && field!="out_brd_etm" && field!="out_brd_alarm_tm"){
				var editBtn = $("#editBtn");
				//有修改权限
				if(editBtn&&editBtn.length>0){
					iframe = layer.open({
						type : 2,
						title : false,
						closeBtn : false,
						area:["100%","100%"],
						content : ctx + "/flightDynamic/form?inFltId="+row.in_fltid+"&outFltId="+row.out_fltid,
						btn : ['保存','新增航班','删除行','返回'],
						yes : function(index, layero) {
							//保存
							var iframeWin = window[layero.find('iframe')[0]['name']];
							iframeWin.save();
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
						}
					});
				} 
				/*
				 * 客户讨论后决定没有权限的人不需要弹出此页面
				 */
//				else {//查看 
//					iframe = layer.open({
//						type : 2,
//						title : false,
//						closeBtn : false,
//						content : ctx + "/flightDynamic/form?inFltId="+row.in_fltid+"&outFltId="+row.out_fltid+"&ifShow=y",
//						btn : ['返回']
//					});
//				}				
				layer.full(iframe);
			}
			//修改机号
			if(field=="in_aircraft_number" || field=="out_aircraft_number"){
				var editAptNoBtn = $("#editAptNoBtn");
				//有修改权限
				if(editAptNoBtn&&editAptNoBtn.length>0){
					iframe = layer.open({
						type : 2,
						title : "机号变更",
						closeBtn : false,
						area : ['500px','220px'],
						content : [ctx+"/fdChange/changeActNumber?in_aircraft_number="+row.in_aircraft_number+
						"&out_aircraft_number="+row.out_aircraft_number+"&in_fltid="+row.in_fltid+"&out_fltid="+
						row.out_fltid, 'no'],
						btn : [ "保存", "取消" ],
						btn1 : function(index, layero) {
							var iframeWin = window[layero.find('iframe')[0]['name']];
							iframeWin.saveForm();
							return false;
						}
					});
				}
			}
			//机号为空的时候才能修改机型
			if(field=="in_acttype_code" || field=="out_acttype_code"){
				var editActTypeBtn = $("#editActTypeBtn");
				//有修改权限
				if(editActTypeBtn&&editActTypeBtn.length>0){
					var isOpen=false;
					var titleText='';
					var btnText='';
					var refresh='';
					var actNumber='';
					if(row.in_aircraft_number==""&&row.out_aircraft_number==""){
						isOpen=true;
						titleText="机型变更";
						btnText="保存";
						refresh="N";
						//在机号至少有一个不为空的时候 刷新机型
					}else if(row.in_aircraft_number!=""||row.out_aircraft_number!=""){
						isOpen=true;
						titleText="刷新机型";
						btnText="刷新";
						refresh="Y";
						actNumber=row.in_aircraft_number==''?row.out_aircraft_number:row.in_aircraft_number;
					}
					if(isOpen){
						iframe = layer.open({
							type : 2,
							title : titleText,
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeActType?in_acttype_code="+row.in_acttype_code+
							"&out_acttype_code="+row.out_acttype_code+"&in_actstand_code="+row.in_actstand_code+
							"&out_actstand_code="+row.out_actstand_code+"&in_fltid="+row.in_fltid+"&out_fltid="+row.out_fltid+
							"&refresh="+refresh+"&actNumber="+actNumber,
							btn : [ btnText, "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}
			}
			
			if(field=="in_property_code"||field=="out_property_code" ){
				var editPropertyBtn = $("#editPropertyBtn");
				//有修改权限
				if(editPropertyBtn&&editPropertyBtn.length>0){
					var fltid;
					if(field=="in_property_code"){
						fltid=row.in_fltid;
					}else{
						fltid=row.out_fltid;
					}
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "性质变更",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeProperty?fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			
			if(field=="in_flt_attr_code"||field=="out_flt_attr_code" ){
				var editAttrCodeBtn = $("#editAttrCodeBtn");
				//有修改权限
				if(editAttrCodeBtn&&editAttrCodeBtn.length>0){
					var fltid;
					if(field=="in_flt_attr_code"){
						fltid=row.in_fltid;
					}else{
						fltid=row.out_fltid;
					}
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "属性变更",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeAttrCode?fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}
			}
			
			//修改航班日期
			if(field=="in_flight_date"||field=="out_flight_date" ){
				var editFltDateBtn = $("#editFltDateBtn");
				//有修改权限
				if(editFltDateBtn&&editFltDateBtn.length>0){
					var flightDate;
					var fltid;
					if(field=="in_flight_date"){
						flightDate=row.in_flight_date;
						fltid=row.in_fltid;
						
					}else{
						flightDate=row.out_flight_date;
						fltid=row.out_fltid;
					}
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "航班日期变更",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeFltDate?flightDate="+flightDate+"&fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}
			}
			//修改延误原因
			if(field=="out_delay_reason"||field=="out_delay_reason_detail" ||field=="in_delay_reason"
				||field=="in_delay_reason_detail" ||field=="out_release_reason" ||field=="out_release_reason_detail"){
				var editDelayBtn = $("#editDelayBtn");
				//有修改权限
				if(editDelayBtn&&editDelayBtn.length>0){
					var fltid;
					if(field=="in_delay_reason_detail" || field=="in_delay_reason"){
						fltid=row.in_fltid;
					}else{
						fltid=row.out_fltid;
					}
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "延误原因变更",
							closeBtn : false,
							area : ['800px','550px'],
							content : ctx+"/fdChange/changeDelayReason?fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			//修改出港排序
			if(field=="depart_sort"){
				var editSortBtn = $("#editSortBtn");
				//有修改权限
				if(editSortBtn&&editSortBtn.length>0){
					var fltid=row.out_fltid;
					var oldValue=row.depart_sort;
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "出港排序变更",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeDepartSort?fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			//修改备降场信息
			if(field=="out_diversion_port"||field=="in_diversion_port" ||field=="out_diversion_res"
				||field=="in_diversion_res" ||field=="out_diversion_atd" ||field=="in_diversion_atd"){
				var editDiversionBtn = $("#editDiversionBtn");
				//有修改权限
				if(editDiversionBtn&&editDiversionBtn.length>0){
					var fltid;
					if(field=="out_diversion_port" || field=="out_diversion_atd"|| field=="out_diversion_res"){
						fltid=row.out_fltid;
					}else{
						fltid=row.in_fltid;
					}
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "备降外场变更",
							closeBtn : false,
							area : ['800px','550px'],
							content : ctx+"/fdChange/changeDiversion?fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			//修改许可推出时间
			if(field=="admin_pushout_tm"){
				var editPushoutBtn = $("#editPushoutBtn");
				//有修改权限
				if(editPushoutBtn&&editPushoutBtn.length>0){
					var fltid=row.out_fltid;
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "许可推出时间",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changePushoutTime?fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			
			//修改登机口/行李转盘
			if(field=="out_gate" || field=="in_bag_crsl"){
				var editScope=null;
//				var editPushoutBtn = $("#editPushoutBtn");
//				//有修改权限
//				if(editPushoutBtn&&editPushoutBtn.length>0){
				var fltid="";
				var title="";
				var url="";
				if(field=="out_gate"){
					fltid=row.out_fltid;
					title="登机口变更";
					url=ctx+"/fdChange/changeOutGate?fltid="+fltid;
					editScope=$("#editGateBtn");
				}
				if(field=="in_bag_crsl"){
					fltid=row.in_fltid;
					title="行李转盘变更";
					url=ctx+"/fdChange/changeBagCrsl?fltid="+fltid;
					editScope=$("#editBagCrslBtn");
				}
				if(editScope&&editScope.length>0){	
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : title,
							closeBtn : false,
							area : ['500px','300px'],
							content : url,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			//修改地面移交时间
			if(field=="in_ground_hand_over_tm"||field=="out_ground_hand_over_tm" ){
				var editHDTimeBtn = $("#editHDTimeBtn");
				//有修改权限
				if(editHDTimeBtn&&editHDTimeBtn.length>0){
					var ground_hand_over_tm;
					var fltid;
					if(field=="in_ground_hand_over_tm"){
						ground_hand_over_tm=row.in_ground_hand_over_tm;
						fltid=row.in_fltid;
						
					}else{
						ground_hand_over_tm=row.out_ground_hand_over_tm;
						fltid=row.out_fltid;
					}
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "地面移交时间",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeGroundHOTime?oldValue="+ground_hand_over_tm+"&fltid="+fltid,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}
			}
			//修改值机柜台
			if(field == "out_checkin_counter"){
				var editCounter = $("#editCounter");
				//有修改权限
				if(editCounter&&editCounter.length>0){
					if(row.out_fltid && row.out_fltid!=""){
						iframe = layer.open({
							type : 2,
							title : "值机柜台变更",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeCounter?fltid="+row.out_fltid+"&oldval="+row.out_checkin_counter,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}
			}
			//修改值机状态
			if(field == "out_checkin_status"){
				var editCheckinStatus = $("#editCheckinStatus");
				//有修改权限
				if(editCheckinStatus&&editCheckinStatus.length>0){
					if(row.out_fltid && row.out_fltid!=""){
						iframe = layer.open({
							type : 2,
							title : "值机状态变更",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeCheckinStatus?fltid="+row.out_fltid+"&oldval="+row.out_ckeckin_status,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}
			}
			//修改登机状态
			if(field == "out_boarding_status"){
				var editBrdBtn = $("#editBrdBtn");
				//有修改权限
				if(editBrdBtn&&editBrdBtn.length>0){
					if(row.out_fltid && row.out_fltid!=""){
						iframe = layer.open({
							type : 2,
							title : "登机状态变更",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeBoardingStatus?fltid="+row.out_fltid+"&oldval="+row.out_boarding_status,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}
			}	
			
			
			//修改登机开始时间
			if(field=="out_brd_btm"){
				var editBrdBtmBtn = $("#editBrdBtmBtn");
				//有修改权限
				if(editBrdBtmBtn&&editBrdBtmBtn.length>0){
					var fltid=row.out_fltid;
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "登机开始时间",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeBrdTime?fltid="+fltid+"&field=" + field,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			
			
			//修改登机结束时间
			if(field=="out_brd_etm"){
				var editBrdEtmBtn = $("#editBrdEtmBtn");
				//有修改权限
				if(editBrdEtmBtn&&editBrdEtmBtn.length>0){
					var fltid=row.out_fltid;
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "登机结束时间",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeBrdTime?fltid="+fltid+"&field=" + field,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			
			console.info(field)
			//修改通知登机口时间
			if(field=="out_brd_alarm_tm"){
				var editTZDJKTmBtn = $("#editTZDJKTmBtn");
				//有修改权限
				if(editTZDJKTmBtn&&editTZDJKTmBtn.length>0){
					var fltid=row.out_fltid;
					if(fltid!=''){
						iframe = layer.open({
							type : 2,
							title : "通知登机口时间",
							closeBtn : false,
							area : ['500px','300px'],
							content : ctx+"/fdChange/changeTZDJKTime?fltid="+fltid+"&field=" + field,
							btn : [ "保存", "取消" ],
							btn1 : function(index, layero) {
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.saveForm();
								return false;
							}
						});
					}
				}	
			}
			
		}
};
/**
 * 保存
 * @returns
 */
function saveEdit(data){
	var succeed = false;
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		url : ctx + "/flightDynamic/saveEdit",
		async : false,
		data : {
			'fltNo':data.fltNo,
			newRows:JSON.stringify(data.newRows),
			editRows:JSON.stringify(data.editRows),
			removeRows:JSON.stringify(data.removeRows)
		},
		success : function(result) {
			layer.close(loading);
			if(result=="succeed"){
				succeed = true;
			} else {
				layer.alert("保存失败！"+result, {icon: 2});
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			layer.close(loading)
			var result = "保存失败！";
			layer.alert(result, {icon: 2});
		}
	});
	return succeed;
}
function load() {
	var start = page * limit;
	if (start < tableData.length) {
		var end = 0;
		if (tableData.length > (page + 1) * limit) {
			end = (page + 1) * limit;
		} else {
			end = tableData.length;
		}
		baseTable.bootstrapTable('append', tableData.slice(start, end));
		var pos = baseTable.bootstrapTable('getScrollPosition');
		baseTable.bootstrapTable('scrollTo', pos + 100);
		page=page+1;
	}
}

//右键旅客信息
function passenger(row) {
	iframe = layer.open({
		type : 2,
		title : "旅客信息",
		content : ctx + "/flightDynamic/passenger?inFltId=" + clickRow.in_fltid + "&outFltId=" + clickRow.out_fltid,
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.savePassenger();
		}
	});
	layer.full(iframe);
}

// 右键货邮行
function baggage(row) {
	iframe = layer.open({
	type : 2,
	title : "货邮行信息",
	area : [ '700px', '400px' ],
	content : ctx + "/flightDynamic/baggage?infltid=" + row.in_fltid+"&outfltid=" + row.out_fltid,
	});
	layer.full(iframe);
}

// 右键时间动态
function timeDynamic(row) {
	if (ioFlag == "I") {
		var id = row.in_fltid;
	}
	if (ioFlag == "O") {
		var id = row.out_fltid;
	}
	iframe = layer.open({
	type : 2,
	title : "时间动态",
	area : [ '100%', '100%' ],
	content : ctx + "/flightDynamic/timeDynamic?id=" + id
	});
}


//右键离港信息录入
function departPsg(row) {
	var inFltId=row.in_fltid;
	var outFltId=row.out_fltid;
//	var inFltNum=row.in_fltno;
//	var outFltNum=row.out_fltno;
//	var inRoute3code=row.in_route_3code;
//	var outRoute3code=row.out_route_3code;
	var isOverStation=false;
	//如果进出港都有，并且航班号一样  才是过站航班
	if(row.in_fltno!=""&&row.out_fltno!=""&&row.in_fltno==row.out_fltno){
		isOverStation=true;
	}
	iframe = layer.open({
		type : 2,
		title : "离港信息录入",
		area : [ '100%', '100%' ],
		content : ctx + "/departPsgInfo/form?inFltId=" + inFltId+"&outFltId="+outFltId
					+"&isOverStation="+isOverStation+"&ioFlag="+ioFlag,
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.save();
			return false;
		}
	});
}

// 右键保障资料
// function guaranteeData(row) {
// if (ioFlag == "I") {
// var id = row.in_fltid;
// }
// if (ioFlag == "O") {
// var id = row.out_fltid;
// }
// iframe = layer.open({
// type : 2,
// title : "保障资料",
// area : [ '700px', '400px' ],
// content : ctx + "/flightDynamic/guaranteeData?id=" + id
// });
// }

// 右键资源状态
function resourseState(row) {
	if (ioFlag == "I") {
		var id = row.in_fltid;
	}
	if (ioFlag == "O") {
		var id = row.out_fltid;
	}
	iframe = layer.open({
	type : 2,
	title : "资源状态",
	area : [ '700px', '400px' ],
	content : ctx + "/flightDynamic/resourseState?id=" + id
	});
}

// 右键消息变更轨迹
function massageChange(row) {
	if (ioFlag == "I") {
		var id = row.in_fltid;
	}
	if (ioFlag == "O") {
		var id = row.out_fltid;
	}
	iframe = layer.open({
	type : 2,
	title : "变更轨迹",
	area : [ '700px', '400px' ],
	content : ctx + "/flightDynamic/massageChange?id=" + id + "&ioFlag=" + ioFlag
	});
}

//右键要客详情
function vipInfo(row) {
	iframe = layer.open({
		type : 2,
		title : "要客详情",
		content : ctx + "/flightDynamic/vipInfo?inFltId=" + clickRow.in_fltid + "&outFltId=" + clickRow.out_fltid + "&inFltNo=" + clickRow.in_fltno + "&outFltNo=" + clickRow.out_fltno,
		area : [ '700px', '400px' ],
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveVipInfo();
		}
	});
}

//右键延误录入
function delayInfo(row) {
	if (!clickRowId) {
		layer.msg('请选择一条动态', {
			icon : 2
		});
		return false;
	}
	if (!clickRow.out_fltid) {
		layer.msg('请选择出港动态', {
			icon : 2
		});
		return false;
	}
	var id = clickRow.out_fltid;
	iframe = layer.open({
		type : 2,
		title : "延误信息管理",
		area : [ '700px', '400px' ],
		btn : [ "保存", "返回" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveDelay();
		},
		content : [ ctx + "/flightDynamic/delay?fltid=" + id, "no" ]
	})
}

// 右键发送报文
function sendMessage(row) {
	var fltno, fltid;
	if (ioFlag == "I") {
		fltno = row.in_fltno;
		fltid = row.in_fltid;
	} else if (ioFlag == "O") {
		fltno = row.out_fltno;
		fltid = row.out_fltid;
	}

	if (!fltid) {
		layer.msg('请选择一条动态', {
			icon : 2
		});
		return false;
	}
	iframe = layer.open({
	type : 2,
	title : '报文发送',
	closeBtn : false,
	content : ctx + "/telegraph/auto/sendMessageList?fltno=" + fltno + "&fltid=" + fltid,
	btn : [ "发送", "取消" ],
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
	layer.full(iframe);
}

// 取消航班
function cancelFlt() {
	if (!clickRowId) {
		layer.msg('请选择一个动态', {
			icon : 2
		});
		return false;
	}
	$("#recycleBox input").attr("checked",false);
	var row = {};
	for(var f in clickRow){
		row[f] = clickRow[f];
	}
	layer.open({
		type : 1,
		title : '航班取消',
		area : ["400px","200px"],
		offset: '100px',
		content : $("#recycleBox"),
		btn : ['确定','重置','取消'],
		yes : function(index, layero) {
			if(layero.find("input[name='optionsRadios']:checked").length == 0){
				layer.msg("请选择要取消的航班！",{icon:7});
			}else{
				var cancleType = layero.find("input[name='optionsRadios']:checked").val();
				var type = layero.find("input[name='cancleFilterRadio']:checked").length == 0?"0":layero.find("input[name='cancleFilterRadio']:checked").val();
				if(cancleType == "1"){
					if(!row.in_fltid || row.in_fltid==""){
						layer.msg("无进港航班！",{icon:7});
						return false;
					}
				} else if(cancleType == "2"){
					if(!row.out_fltid || row.out_fltid==""){
						layer.msg("无出港航班！",{icon:7});
						return false;
					}
				}
				var inFltId = row.in_fltid?row.in_fltid:"";
				var outFltId = row.out_fltid?row.out_fltid:"";
				$.ajax({
					type: 'post',
					url: ctx+"/flightDynamic/cancleFlt",
					data: {
						inFltId : inFltId,
						outFltId : outFltId,
						cancleType:cancleType,
						type : type
					},
					success : function(res) {
						if (res == "success") {
							layer.msg('取消航班成功', {icon : 1});
							$("#refresh").click();
						} else {
							layer.msg('取消航班失败', {icon : 2});
						}
					}
				});
				if(row.in_fltid && row.out_fltid && cancleType != "3"){
					var fltNum = row.in_aircraft_number||row.out_aircraft_number;
					iframe = layer.open({
						type : 2,
						title : false,
						closeBtn:false,
						area : [ '100%', '100%' ],
						content : ctx + "/fdRel/list?fltIds=&fltNum="+fltNum,
						btn : [ "保存", "关闭" ],
						btn1 : function(index, layero) {
							var iframeWin = window[layero.find('iframe')[0]['name']];
							iframeWin.saveRel();
						}
					});	
				}
				layer.close(index);
			}
		},
		btn2: function(index, layero){
			$("#recycleBox input").attr("checked",false);
			return false;
		},
		btn3: function(index, layero){
			
		}
	});
}

// 指令
function sended(row) {
	var fltid;
	if (ioFlag == "I") {
		fltid = row.in_fltid
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid
	}
	// 动态指令
	var num = "4";
	var msg = layer.open({
	type : 2,
	title : '指令消息',
	zIndex : 99999999999,
	maxmin : false,
	shadeClose : false,
	area : [ $("body").width() - 200 + "px", $("body").height() - 200 + "px" ],
	content : ctx + "/message/history/list?num=" + num + "&fltid=" + fltid,
	});
	layer.full(msg);
}

// 调用消息模块相关
function openMessage(row, tid) {
	var fltid = "";
	var flightNumber = "";
	var flightDate = "";
	var in_fltid = "";
	var out_fltid = "";
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
	parent.openMessage(fltid, flightNumber, flightDate, in_fltid, out_fltid, tid, ioFlag);
}

// 获取基本表格列头
function getBaseColumns() {
	var columns = [];
	$.ajax({
	type : 'post',
	url : ctx + "/flightDynamic/getDefaultColumns",
	async : false,
	data : {
		'schema' : "1"
	},
	dataType : 'json',
	success : function(column) {
		var order = [{
			field : "checkbox",
			checkbox: true,
			sortable : false,
			editable : false,// 显示复选框
            formatter: function (value,row,index) { 
                if($.inArray(index,overAllIds)!=-1){// 判断数组里有没有这个 id 避免刷新时清空复选状态
                  console.log("index-"+index); 
                	return {
                        checked : true               // 存在则选中
                    }
                }
            }  
		},
		{
		field : 'order',
		title : '序号',
		sortable : false,
		editable : false,
		width : 44,
		formatter : function(value, row, index) {
			return index + 1;
		}
		} ];
		for ( var i in column) {
			if (column[i].field == "in_fltno"||column[i].field == "in_flight_number2"||column[i].field == "in_eta") {
				column[i].formatter = function(value, row, index) {
					if (!value) {
						return '';
					}
					var className = "col-state-0";
					var title = "前站未起飞";
					if (row.inStatus == "2") {
						className = "col-state-5";
						title = "预出";
					} else if (row.inStatus == "4") {
						className = "col-state-1";
						title = "前站已起飞";
					} else if (row.inStatus == "5") {
						className = "col-state-2";
						if(row.outStatus=="4" || row.outStatus=="5" || row.outStatus=="3" || row.outStatus=="7"){
							className = "col-state-4";
						}
						title = "本场已落地";
					} else if (row.inStatus == "3") {
						className = "col-state-3";
						title = "延误";
					} else if (row.inStatus == "6") {
						className = "col-state-6 col-border-3";
						title = "返航";
					} else if (row.inStatus == "7") {
						className = "col-state-4 col-border-3";
						title = "备降";
					} else if (row.inStatus == "1") {
						className = "col-state-5";
						title = "取消";
					} 
					return '<span class=\'' + className + '\' title=\''+title+'\'>' + value + '</span>';
				}
			}
			if (column[i].field == "out_fltno"||column[i].field == "out_flight_number2"||column[i].field == "out_atd") {
				column[i].formatter = function(value, row, index) {
					if (!value) {
						return '';
					}
					var className = "col-state-0";
					var title = "前站未落地";
					if(row.inStatus == "5" || row.outStatus == "2"){
						className = "col-state-2";
						title = "本场预出";
					}
					if (row.outStatus == "4") {
						className = "col-state-4";
						title = "本场已起飞";
					} else if (row.outStatus == "5") {
						className = "col-state-4";
						title = "后站已落地";
					} else if (row.outStatus == "3") {
						className = "col-state-3";
						title = "延误";
					} else if (row.outStatus == "6") {
						className = "col-state-6 col-border-3";
						title = "返航";
					} else if (row.outStatus == "7") {
						className = "col-state-4 col-border-3";
						title = "备降";
					} else if (row.outStatus == "1") {
						className = "col-state-5";
						title = "取消";
					}
					return '<span class=\'' + className + '\' title=\''+title+'\'>' + value + '</span>';
				}
			}
			if (column[i].field == "in_atd") {
				column[i].formatter = function(value, row, index) {
					if (!value) {
						return '';
					}
					if(row.yudaTm && row.yudaTm != ""){
						var className = "col-state-8";
						return '<span class=\'' + className + '\'>' + value + '</span>';
					}else{
						return value;
					}
				}
			}
			if (column[i].field == "out_delay_name") {
				column[i].formatter = function(value, row, index) {
					if (!value) {
						return '';
					}
					var className = "col-state-0";
					return '<span class=\'' + className + '\'>' + value + '</span>';
				}
			}
			if(column[i].field){
				if(column[i].field.indexOf("actstand_code_chg")!=-1 || column[i].field.indexOf("aircraft_number_chg")!=-1 || column[i].field.indexOf("gate_chg")!=-1){
					column[i].formatter = function(value, row, index) {
						if (!value) {
							return '';
						}else if(value.indexOf("->")!=-1){
							var className = "col-background-yellow";
							return '<span class=\'' + className + '\'>' + value + '</span>';
						}else{
							return value;
						}
					}
				}
			}
			if(column[i].editable == "true"){
				column[i].formatter = function(value, row, index){
					if(!value){
						return ' ';
					}else{
						return value;
					}
				};
				column[i].editable = {
			        	type:'text',
			        	onblur:'cancel',
			        	url:ctx+"/flightDynamic/columnUpdate",
			        	params:function(params){
			        		var data = baseTable.bootstrapTable('getData'),
		                    index = $(this).parents('tr').data('index');
			        		params.fltid = params.name.indexOf("out_")==-1?data[index].in_fltid:data[index].out_fltid;
			        		params.col = params.name;
			        		return params;
			        	},
			        	success:function(msg){
			        		if(msg=="success"){
			        			layer.msg('修改成功', {
									icon : 1
								});
			        		}else{
			        			layer.msg('修改失败', {
									icon : 2
								});
			        		}
			        	},
			        	error:function(){
			        		layer.msg('修改失败', {
								icon : 2
							});
			        	}
				}
			}
			if (column[i].field == "out_depart_getoff_flag") {
				column[i].formatter = function(value, row, index) {
					if (!value || value == '0') {
						return '';
					}
					return '<img src="'+ctxStatic+'/images/human-male.png" style="height: 34px;"></img>';
				}
			}
			if(column[i].editable && column[i].editable!="true" && column[i].editable!="true"){
				column[i].editable = false;
			}
		}
		columns = order.concat(column);
	}
	});
	return columns;
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
/**
 * 表单提交回调
 */
function formSubmitCallback() {// 子页面提交完成后，父页面关闭弹出层及刷新表格
	layer.close(iframe);
	$("#baseTable").bootstrapTable("refresh");
}

function getMessageMenu(ioFlag) {
	$.ajax({
		type : 'post',
		url : ctx + "/message/common/getTemplateList",
		dataType : "json",
		async:false,
		error : function() {
			layer.alert("获取模板失败！", {
				icon : 0,
				time : 1000
			});
		},
		data :{
			"ioFlag" : encodeURIComponent(ioFlag)
		},
		success : function(data) {
			if (data.result) {
				var html = '';
				var templateList = data.templateList;
				$("#context-menu li.divider:last").nextAll().remove();
				$(templateList).each(function(index, element) {
					$("#context-menu").append('<li  data-item="' + element.ID + '"><a>' + element.MTITLE + '</a></li>');
				});
				appendEventForMenu();
			}
		}
	});
}

/**
 * 自动刷新 baochl 20171117
 * 
 * @param interval
 *            刷新频率
 */
var intervalObj = null;
var refreshUnit = 1000;// 刷新单位是秒
function autoReload(interval) {
	if (interval < 10) {
		layer.msg("最小刷新频率不要小于10秒！", {
			icon : 7
		});
		return false;
	}
	if (sessionStorage) {
		sessionStorage.setItem(AUTO_RELOAD_SESSION, interval);
	}
	var time = Number(interval) * refreshUnit;
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟";
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		// 刷新前滚动条
		var beforeScroll = $(".fixed-table-body").scrollTop();
		$.ajax({
		type : 'post',
		url : ctx + "/flightDynamic/getDynamic",
		dataType : "json",
		beforeSend : function() {
			$("#refresh").html("<i class='fa fa-spinner' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟");
		},
		error : function() {
			$("#refresh").text("刷新失败");
		},
		data : tableOptions.queryParams,
		success : function(result) {
//			clearSelectedRow();
			if (result) {
				if (sortName) {
					result.sort(function(a, b) {
						var order = sortOrder === 'desc' ? -1 : 1
						var val1 = a[sortName];
						var val2 = b[sortName];
						if (val1 === undefined || val1 === null) {
							val1 = '';
						}
						if (val2 === undefined || val2 === null) {
							val2 = '';
						}
						if ($.isNumeric(val1) && $.isNumeric(val2)) {
							// Convert numerical values form string to
							// float.
							val1 = parseFloat(val1);
							val2 = parseFloat(val2);
							if (val1 < val2) {
								return order * -1;
							}
							return order;
						}

						if (val1 === val2) {
							return 0;
						}
						// If value is not a string, convert to string
						if (typeof val1 !== 'string') {
							val1 = val1.toString();
						}
						if (typeof val2 !== 'string') {
							val2 = val2.toString();
						}

						if (val1.localeCompare(val2, "zh") === -1) {
							return order * -1;
						}

						return order;
					});
				}
				tableData = result;
				$("#total").text(result.length);
				if (result.length > (limit * page)) {
					baseTable.bootstrapTable("load", result.slice(0, (limit * page)));
				} else {
					baseTable.bootstrapTable("load", result);
				}
				if (beforeScroll) {
					baseTable.bootstrapTable("scrollTo", beforeScroll);
				}
			}
			$("#refresh").html(btnText);
			if(clickRowIndex){
				$("#baseTable tr[data-index='"+clickRowIndex+"']").addClass("clickRow");
			}
		}
		});
	}, time);
}
function refreshFunc() {
	//刷新前滚动条
	var beforeScroll = $(".fixed-table-body").scrollTop();
	$.ajax({
		type : 'post',
//		url : ctx + "/scheduling/list/getDynamic",
		url : ctx + "/flightDynamic/getDynamic",
		dataType:"json",
		beforeSend:function(){
			$("#refresh").find(".fa-clock-o").addClass("fa-spinner").removeClass("fa-clock-o");
		},
		error:function(){
			$("#refresh").text("刷新失败");
		},
		data :tableOptions.queryParams,
		success : function(result) {
			clearSelectedRow();
			clearCheckRow();
			if(result){
				if (sortName) {
					result.sort(function(a, b) {
						var order = sortOrder === 'desc' ? -1 : 1
						var val1 = a[sortName];
						var val2 = b[sortName];
						if (val1 === undefined || val1 === null) {
							val1 = '';
						}
						if (val2 === undefined || val2 === null) {
							val2 = '';
						}
						if ($.isNumeric(val1) && $.isNumeric(val2)) {
							// Convert numerical values form string to
							// float.
							val1 = parseFloat(val1);
							val2 = parseFloat(val2);
							if (val1 < val2) {
								return order * -1;
							}
							return order;
						}

						if (val1 === val2) {
							return 0;
						}
						// If value is not a string, convert to string
						if (typeof val1 !== 'string') {
							val1 = val1.toString();
						}
						if (typeof val2 !== 'string') {
							val2 = val2.toString();
						}

						if (val1.localeCompare(val2, "zh") === -1) {
							return order * -1;
						}

						return order;
					});
				}
				tableData = result;
				if(result.length>(limit*page)){
					baseTable.bootstrapTable("load",result.slice(0,(limit*page))); 
				} else {
					baseTable.bootstrapTable("load",result); 
				}
				if(beforeScroll){
					baseTable.bootstrapTable("scrollTo",beforeScroll); 
				}
			}
			$("#refresh").find(".fa-spinner").addClass("fa-clock-o").removeClass("fa-spinner");
		}
	});
}
/**
 * baochl 20171125 清除选中行
 */
function clearSelectedRow() {
	clickRowId = "";
	clickRow = null;
	clickRowIndex="";
	$(".clickRow").removeClass("clickRow");
}
//清空保存复选框index的数组
function clearCheckRow(){
	overAllIds=[];
}

function saveSuccess() {
	layer.close(iframe);
	// 刷新前滚动条
	var beforeScroll = $(".fixed-table-body").scrollTop();
	$.ajax({
	type : 'post',
	url : ctx + "/flightDynamic/getDynamic",
	dataType : "json",
	error : function() {
		$("#refresh").text("刷新失败");
	},
	data : tableOptions.queryParams,
	success : function(result) {
		clearSelectedRow();
		clearCheckRow();
		if (result) {
			if (sortName) {
				result.sort(function(a, b) {
					var order = sortOrder === 'desc' ? -1 : 1
					var val1 = a[sortName];
					var val2 = b[sortName];
					if (val1 === undefined || val1 === null) {
						val1 = '';
					}
					if (val2 === undefined || val2 === null) {
						val2 = '';
					}
					if ($.isNumeric(val1) && $.isNumeric(val2)) {
						// Convert numerical values form string to
						// float.
						val1 = parseFloat(val1);
						val2 = parseFloat(val2);
						if (val1 < val2) {
							return order * -1;
						}
						return order;
					}

					if (val1 === val2) {
						return 0;
					}
					// If value is not a string, convert to string
					if (typeof val1 !== 'string') {
						val1 = val1.toString();
					}
					if (typeof val2 !== 'string') {
						val2 = val2.toString();
					}

					if (val1.localeCompare(val2, "zh") === -1) {
						return order * -1;
					}

					return order;
				});
			}
			tableData = result;
			$("#total").text(result.length);
			if (result.length > (limit * page)) {
				baseTable.bootstrapTable("load", result.slice(0, (limit * page)));
			} else {
				baseTable.bootstrapTable("load", result);
			}
			if (beforeScroll) {
				baseTable.bootstrapTable("scrollTo", beforeScroll);
			}
		}
	}
	});
}
// 筛选弹出复选框
function openCheck(type) {
	layer.open({
	type : 2,
	title : '复选',
	content : ctx + "/flightDynamic/openCheck?type=" + type + "&selectedId=" + $("input[name='" + type + "value']").val() + "&selectedText=" + $("input[name='" + type + "']").val(),
	btn : [ "确定", "清空已选", "取消" ],
	area : [ '800px', '450px' ],
	yes : function(index, layero) {
		var iframeWin = window[layero.find('iframe')[0]['name']];
		var data = iframeWin.getChooseData();
		$("input[name='" + type + "']").val(data.chooseTitle);
		$("input[name='" + type + "value']").val(data.chooseValue);
		layer.close(index);
	},
	btn2 : function(index, layero) {// 重置表单及筛选条件
		var iframeWin = window[layero.find('iframe')[0]['name']];
		iframeWin.clearSelect();
		return false;
	},
	btn3 : function(index) {// 关闭前重置表单及筛选条件
		layer.close(index);
	}
	})
}

// 校验FilterForm
function vaildFilterForm() {
	if (($("#beginTime").val().length != 4 &&$("#beginTime").val()!="") || ($("#endTime").val().length != 4&&$("#endTime").val()!="") || $("#beginTime").val().charAt(0) > 2 || $("#beginTime").val().charAt(2) > 5 
	|| $("#endTime").val().charAt(0) > 2 || $("#endTime").val().charAt(2) > 5) {
		layer.msg('请输入正确的时间格式！', {
			icon : 2
		});
		return false;
	}
	return true
}

function searchOpt(text,obj) {
	searchOption=text;
	var icon=$("button[name='searchOptBtn']").find("span").eq(0);
	$("button[name='searchOptBtn']").text($(obj).text());
	$("button[name='searchOptBtn']").append(icon);
}
/**
 * 外航数据录入（入位，离位，机号，关舱门）
 * @param row
 */
function fltDataInput(row){
	var fltid = "";
	if (ioFlag == "I") {
		fltid = row.in_fltid;
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid;
	}
	iframe = layer.open({
		type:2,
		title:"外航数据录入",
		content : [ctx + "/flightDynamic/fltDataInput?fltid="+fltid+"&ioType="+ioFlag,"no"],
		area:["500px","400px"],
		btn:["保存","取消"],
		resize:false,
		yes:function(index,layero){
			if(ioFlag == "I"){//进港
				// 报文ETA
				var eta = layer.getChildFrame('#eta', index).val();
				// 入位时间
				var standTm = layer.getChildFrame('#standTm', index).val();
				if(eta){
					if(eta.length!=4){
						layer.msg("报文ETA输入有误！",{icon:7});
						return false;
					}
					var hh = eta.substring(0,2);
					var mi = eta.substring(2);
					if(Number(hh)>23){
						layer.msg("报文ETA小时不能大于23点！",{icon:7});
						return false;
					}
					if(Number(mi)>59){
						layer.msg("报文ETA分钟不能大于59分！",{icon:7});
						return false;
					}
				}
				if(standTm){
					if(standTm.length!=4){
						layer.msg("入位时间输入有误！",{icon:7});
						return false;
					}
					var hh = standTm.substring(0,2);
					var mi = standTm.substring(2);
					if(Number(hh)>23){
						layer.msg("入位时间小时不能大于23点！",{icon:7});
						return false;
					}
					if(Number(mi)>59){
						layer.msg("入位时间分钟不能大于59分！",{icon:7});
						return false;
					}
				}
				var iframeWin = window[layero.find('iframe')[0]['name']];
				if(!iframeWin.validActNumber()){
					return false;
				}
				var inputForm = layer.getChildFrame('#inputForm', index);
				inputForm.ajaxSubmit({
					async:false,
					error:function(){
						layer.msg('保存失败！', {
							icon : 2
						});
					},
					success:function(result){
						if("succeed"==result){
							layer.msg('保存成功！', {
								icon : 1,
								time:1
							},function(){
								saveSuccess();
							});
						}
					}
				});
			} else if(ioFlag == "O"){//出港
				// 离位时间
				var relsStandTm = layer.getChildFrame('#relsStandTm', index).val();
				// 客舱门关闭
				var htchCloTm = layer.getChildFrame('#htchCloTm', index).val();
				if(relsStandTm){
					if(relsStandTm.length!=4){
						layer.msg("离位时间输入有误！",{icon:7});
						return false;
					}
					var hh = relsStandTm.substring(0,2);
					var mi = relsStandTm.substring(2);
					if(Number(hh)>23){
						layer.msg("离位时间小时不能大于23点！",{icon:7});
						return false;
					}
					if(Number(mi)>59){
						layer.msg("离位时间分钟不能大于59分！",{icon:7});
						return false;
					}
				}
				if(htchCloTm){
					if(htchCloTm.length!=4){
						layer.msg("客舱门关闭输入有误！",{icon:7});
						return false;
					}
					var hh = htchCloTm.substring(0,2);
					var mi = htchCloTm.substring(2);
					if(Number(hh)>23){
						layer.msg("客舱门关闭小时不能大于23点！",{icon:7});
						return false;
					}
					if(Number(mi)>59){
						layer.msg("客舱门关闭分钟不能大于59分！",{icon:7});
						return false;
					}
				}
				var iframeWin = window[layero.find('iframe')[0]['name']];
				if(!iframeWin.validActNumber()){
					return false;
				}
				var inputForm = layer.getChildFrame('#inputForm', index);
				inputForm.ajaxSubmit({
					async:false,
					error:function(){
						layer.msg('保存失败！', {
							icon : 2
						});
					},
					success:function(result){
						if("succeed"==result){
							layer.msg('保存成功！', {
								icon : 1,
								time:1
							},function(){
								saveSuccess();
							});
						}
					}
				});
			}
		}
	});
}
/**
 * 单航班异常查看
 * @param row
 */
function openException(row){
	var fltid = "";
	if (ioFlag == "I") {
		fltid = row.in_fltid;
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid;
	}
	layer.open({
		type : 2,
		title : "异常情况查看",
		area : [ '100%', '100%' ],
		content : ctx + "/flightDynamic/error?fltid="+fltid
	});
}
function listenFilterCheckbox(){
	filter.runways="";
	globleForm.on('checkbox(runways)', function(data){
		if (data.elem.checked==true) {
			if (!filter.runways) {
				filter.runways="";
			}
			filter.runways += data.value + ",";
		}else {
			filter.runways=filter.runways.replace(data.value + ",","");
		}
	});        
	filter.terminals="";
	globleForm.on('checkbox(terminals)', function(data){
		if (data.elem.checked==true) {
			if (!filter.terminals) {
				filter.terminals="";
			}
			filter.terminals += data.value + ",";
		}
		else {
			filter.terminals=filter.terminals.replace(data.value + ",","");
		}
	});        
	filter.alnGroup="";
	globleForm.on('checkbox(alnGroup)', function(data){
		if (data.elem.checked==true) {
			if (!filter.alnGroup) {
				filter.alnGroup="";
			}
			filter.alnGroup += data.value + ",";
		}else {
			filter.alnGroup=filter.alnGroup.replace(data.value + ",","");
		}
	});        
	filter.actkinds="";
	globleForm.on('checkbox(actkinds)', function(data){
		if (data.elem.checked==true) {
			if (!filter.actkinds) {
				filter.actkinds="";
			}
			filter.actkinds += data.value + ",";
		}else {
			filter.actkinds=filter.actkinds.replace(data.value + ",","");
		}
	});        
	filter.vipFlags="";
	globleForm.on('checkbox(vipFlags)', function(data){
		if (data.elem.checked==true) {
			if (!filter.vipFlags) {
				filter.vipFlags="";
			}
			filter.vipFlags += data.value + ",";
		}else {
			filter.vipFlags=filter.vipFlags.replace(data.value + ",","");
		}
	});     
	
}
function changeFltInfo(obj){
	var time = obj.options[obj.selectedIndex].value;
	tableOptions.queryParams.time =time;
	//刷新前滚动条
//	var beforeScroll = $(".fixed-table-body").scrollTop();
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/flightDynamic/getDynamic",
		dataType:"json",
		data:tableOptions.queryParams,
//		data :{
//			'time':time,
//			'schema':1
//		},
		success : function(result) {
			layer.close(loading);
			clearSelectedRow();
			clearCheckRow();
			if(result){
				tableData = result;
				if(result.length>(limit*page)){
					baseTable.bootstrapTable("load",result.slice(0,(limit*page))); 
				} else {
					baseTable.bootstrapTable("load",result); 
				}
//				if(beforeScroll){
//					baseTable.bootstrapTable("scrollTo",beforeScroll); 
//				}
			}
			$("#refresh").find(".fa-spinner").addClass("fa-clock-o").removeClass("fa-spinner");
		}
	});

}

function appendEventForMenu(){
	$("#context-menu li").hover(function() {
		$(this).siblings().find("ul").hide();
		var wholeWidth = $("#baseTables").width();
		var left = $(this).offset().left;
		var right = wholeWidth - left - 180;
		if (wholeWidth - left < 540) {
			$(this).children("ul").css({
			'position' : "absolute",
			'left' : -160,
			'top' : 0
			});
		} else {
			$(this).children("ul").css({
			'position' : "absolute",
			'left' : 158,
			'top' : 0
			});
		}
		$(this).children("ul").show();
	});
}
//给数组添加属性
Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};

Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

// 视频
function video(row, item){

    var type = '';
    var code = '';
    if(item=='runway') {

        type = '1';
        if(undefined==row.out_runway_code||'undefined'==row.out_runway_code) {

            code = row.in_runway_code;
        } else {

            code = row.out_runway_code;
        }
    } else if (item=='seat') {

        type = '2';
        if(undefined==row.out_actstand_code||'undefined'==row.out_actstand_code) {

            code = row.in_actstand_code;
        } else {

            code = row.out_actstand_code;
        }
    } else if (item=='gate') {

        type = '5';
        if(undefined==row.out_gate||'undefined'==row.out_gate) {

            code = row.in_gate;
        } else {

            code = row.out_gate;
        }
    } else if (item=='counter') {

        type = '9';
        if(undefined==row.out_checkin_counter||'undefined'==row.out_checkin_counter) {

            code = row.in_checkin_counter;
        } else {

            code = row.out_checkin_counter;
        }
    } else if (item=='carousel') {

        type = '11';
        if(undefined==row.out_bag_crsl||'undefined'==row.out_bag_crsl) {

            code = row.in_bag_crsl;
        } else {

            code = row.out_bag_crsl;
        }
    }

    if(undefined==code||'undefined'==code) {

        layer.alert('没有配置该列！');
    } else {

        getVideo(type, code);
    }
}