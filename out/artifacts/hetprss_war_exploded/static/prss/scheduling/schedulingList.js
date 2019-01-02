var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
var clickRow;
var filter;// 用于筛选
var iframe;
var ioFlag;
var switches = 0;
var flagBGS=0;
var sortName;
var sortOrder;
var searchOption="";
var filter = {};
var selectFilter={};//存放查询过滤条件的历史筛选条件对象
var time_type="";
var globleForm;
var AUTO_RELOAD_SESSION = null;//自动刷新频率缓存
var schemaId = $('#schemaId').val();
$(document).ready(function() {
	AUTO_RELOAD_SESSION = $("#loginName").val()+$("#schemaId").val()+"refresh";
	document.oncontextmenu = function(e) {
		return false;
	}
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		var form = layui.form;
		globleForm=form;
		form.on('checkbox(checkBoxes)', function(data){
			  if($(data.elem).hasClass("zt")){
				  $(".checkBox").not($(data.elem)).attr('checked',false);
				  form.render();
			  }
			  $(".checkBoxes .layui-form-checkbox").each(function(i,ele){
				  if($(ele).hasClass("layui-form-checked")){
					  filter[$(ele).prev().attr("name")] = 'Y';
				  }else{
					  filter[$(ele).prev().attr("name")] = '';
				  }
			  });
			  roleFilter();
		});     
		listenFilterCheckbox();
	});
	$("#baseTable").each(function(){
		$(this).on('load-success.bs.table',function(thisObj){
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody[0].removeEventListener('ps-y-reach-end',load);
			tableBody[0].addEventListener('ps-y-reach-end',load);
		});
	});
	
	baseTable = $("#baseTable");
	tableOptions.height = $("body").height();// 表格适应页面高度

	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));

	// 作业管理
	$("#jobManageBtn").click(function() {
		jobManage();
	});

	// 人员分工
	$("#divisionBtn").click(function() {
		doWorkerDivision();
	});
	// 人员计划
	$("#memberPlan").click(function() {
		memberPlan();
	});
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
	// 异常情况查看
	$("#error").click(function() {
		iframe = layer.open({
			type : 2,
			title : "异常情况查看",
			area : [ '100%', '100%' ],
			content : ctx + "/flightDynamic/error"
		});
	});

    if($("#error").length > 0){
        $("#error").click(function(){
            $("#error .label").remove();
        });
        var user = $("#user").val();
        if(!localStorage["latestErrorTimeOf"+user]){
            localStorage["latestErrorTimeOf"+user] = "1970-01-01 00:00:00";
        }
        var time = localStorage["latestErrorTimeOf"+user];
        $.ajax({
            type:'post',
            url:ctx+"/taskmonitor/getUnreadErrorNum",
            data:{
                time:time
            },
            success:function(num){
                $("#error .label").remove();
                if(num != 0){
                    $("#error").append("<span class=\"label label-danger\">"+num+"</span>");
                }
            }
        });
    };

	// 旅客人数录入
	$("#nop").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一个动态', {
				icon : 2
			});
			return false;
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/flightDynamic/getAttrCode",
			data : {
				'id' : clickRowId
			},
			success : function(result) {
				layer.close(loading);
				iframe = layer.open({
					type : 2,
					title : "旅客人数录入",
					area : [ '500px', '250px' ],
					content : ctx + "/flightDynamic/nop?id=" + clickRowId + "&ioFlag=" + ioFlag + "&type=" + result,
					btn : [ "保存", "取消" ],
					btn1 : function(index, layero) {
						var iframeWin = window[layero.find('iframe')[0]['name']];
						var loading = layer.load(2, {
							shade : [ 0.1, '#000' ]
						});
						$.ajax({
							type : 'post',
							async : false,
							url : ctx + "/flightDynamic/updateNop",
							data : {
								'id' : clickRowId,
								'dPaxNum' : iframeWin.$("#dPaxNum").val(),
								'iPaxNum' : iframeWin.$("#iPaxNum").val(),
								'paxNum' : iframeWin.$("#paxNum").val()
							},
							success : function(result) {
								layer.close(loading);
								if (result == "success") {
									layer.msg('保存成功！', {
										icon : 1,
										time : 600
									}, function() {
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
					}
				});
			}
		});
	});
	// 保障
	$("#ensure").click(
			function() {
				if (!clickRowId) {
					layer.msg('请选择一条动态', {
						icon : 2
					});
					return false;
				}
				/*var fltid, acttype, aircraft;
				if (ioFlag == "I") {
					fltid = clickRow.in_fltid ? clickRow.in_fltid : "";
					acttype = clickRow.in_acttype_code ? clickRow.in_acttype_code : "";
					aircraft = clickRow.in_aircraft_number ? clickRow.in_aircraft_number : "";

				} else if (ioFlag == "O") {
					fltid = clickRow.out_fltid ? clickRow.out_fltid : "";
					acttype = clickRow.out_acttype_code ? clickRow.out_acttype_code : "";
					aircraft = clickRow.out_aircraft_number ? clickRow.out_aircraft_number : "";
				}*/
				// 修改关闭按钮样式
				$('body').on('layui.layer.open',function(){
					$('.layui-layer-close').removeClass('layui-layer-close2').addClass('layui-layer-close1');
				});
				var inFltid = clickRow.in_fltid?clickRow.in_fltid:'';
				var outFltid = clickRow.out_fltid?clickRow.out_fltid:'';
				iframe = layer.open({
					type : 2,
					title : false,
					closeBtn : 1,
					content : ctx + "/fltmonitor?inFltid="+inFltid+"&outFltid="+outFltid,// type用来区分页面来源，控制功能权限
					//btn : [ "返回" ],
				});
				layer.full(iframe);// 展开弹出层直接全屏显示
			});

	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})

	getOptions();
	
	$(".mutilselect2").select2({
		placeholder : "请选择",
		width : "100%",
		language : "zh-CN"
    });
	
	/*
	 * $("input[placeholder='搜索']").keydown(function(event) { if (event.keyCode == 13) { // 绑定回车 searchdata($("input[placeholder='搜索']").val()); } });
	 */

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
	})
	// 右键获取菜单
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
		if (1 == e.which) {
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
	})
	//刷新
	if(!sessionStorage){
		console.log("该浏览器不支持本地缓存");
	} else {
		if(sessionStorage.getItem(AUTO_RELOAD_SESSION)&&sessionStorage.getItem(AUTO_RELOAD_SESSION)!='null'){
			autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
		}else{
			autoReload("10");
		}
	}
	$("#refresh").click(function() {
		clearSelectedRow();
		refreshFunc();
		//clearInterval(intervalObj);
		//$("#refresh").text("刷新");
		//$(this).find(".fa-clock-o").addClass("fa-spinner").removeClass("fa-clock-o");
		//baseTable.bootstrapTable('refresh');
		/*if(sessionStorage&&sessionStorage.getItem(AUTO_RELOAD_SESSION)&&sessionStorage.getItem(AUTO_RELOAD_SESSION)!='null'){
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
			content : $("#filterDiv"),
			btn : [ "确定", "重置", "取消" ],
			yes : function(index) {
				clearSelectedRow();
				// 航班范围
				if ($("#filterForm .flightScope").val()==''||$("#filterForm .flightScope").val()) {
					filter.flightScope = $("#filterForm .flightScope").val()=='!'?'':$("#filterForm .flightScope").val();
				}
				// 机坪
				if ($("input[name='apronvalue']").val()==''||$("input[name='apronvalue']").val()) {
					filter.apron = $("input[name='apronvalue']").val();
				}
				//机位
				var bayStr = ($("#bay").val())?$("#bay").val().join(','):"";
				if(bayStr.length > 0){
					filter.bay=bayStr;
				}
				// 廊桥/远机位
				//filter.GAFlag=$("#GAFlag").val();
				var GAFlagStr = ($("#GAFlag").val())?$("#GAFlag").val().join(','):"";
				if(GAFlagStr.length > 0){
					filter.GAFlag=GAFlagStr;
				}
				
				var actKindsArr=new Array();
				$('input[name="actkinds"]:checked').each(function(){
					actKindsArr.push($(this).val());
				});
				var kindsStr=actKindsArr.join(',');
				if(kindsStr.length>0){
					filter.actkinds=kindsStr;
				}
				
				// 始发/中转
				/*if ($("#filterForm .stayFlag").val()==''||$("#filterForm .stayFlag").val()) {
					filter.stayFlag = $("#filterForm .stayFlag").val()=='!'?'':$("#filterForm .stayFlag").val();
				}*/
				var stayFlagStr = ($("#filterForm .stayFlag").val())?$("#filterForm .stayFlag").val().join(','):"";
				if(stayFlagStr.length > 0){
					filter.stayFlag=stayFlagStr;
				}
				// 航空公司
				if ($("input[name='airlinevalue']").val()==''||$("input[name='airlinevalue']").val()) {
					filter.airline = $("input[name='airlinevalue']").val();
				}
				// 航线性质 D I M
				/*if ($("#filterForm .alntype").val()==''||$("#filterForm .alntype").val()) {
					filter.alnFlag = $("#filterForm .alntype").val()=='!'?'':$("#filterForm .alntype").val();
				}*/
				var alntypeStr = ($("#filterForm .alntype").val())?$("#filterForm .alntype").val().join(','):"";
				if(alntypeStr.length > 0){
					filter.alntype=alntypeStr;
				}
				// 宽窄体
				if ($("#filterForm .acttypeSizes").val()==''||$("#filterForm .acttypeSizes").val()) {
					filter.acttypeSizes = $("#filterForm .acttypeSizes").val()=='!'?'':$("#filterForm .acttypeSizes").val();
				}
				// 起场
				if ($("input[name='departAirportvalue']").val()==''||$("input[name='departAirportvalue']").val()) {
					filter.departAirport = $("input[name='departAirportvalue']").val();
				}
				// 落场
				if ($("input[name='arriveAirportvalue']").val()==''||$("input[name='arriveAirportvalue']").val()) {
					filter.arriveAirport = $("input[name='arriveAirportvalue']").val();
				}
				// 廊桥/远机位
				if ($("#filterForm .GAFlag").val()==''||$("#filterForm .GAFlag").val()) {
					filter.GAFlag = $("#filterForm .GAFlag").val()=='!'?'':$("#filterForm .GAFlag").val();
				}
				
				// 登机口
				if ($("input[name='gatevalue']").val()==''||$("input[name='gatevalue']").val()) {
					filter.gate = $("input[name='gatevalue']").val();
				}
				/*
				 * if ($("#filterForm .identifying").val()) { filter.identifying = $("#filterForm .identifying").val(); }
				 */
				// 航班性质
				/*if ($("#filterForm .fltPropertys").val()==''||$("#filterForm .fltPropertys").val()) {
					filter.fltPropertys = $("#filterForm .fltPropertys").val()=='!'?'':$("#filterForm .fltPropertys").val();
				}*/
				var fltPropertysStr = ($("#filterForm .fltPropertys").val())?$("#filterForm .fltPropertys").val().join(','):"";
				if(fltPropertysStr.length > 0){
					filter.fltPropertys=fltPropertysStr;
				}
				// 机型
				if ($("input[name='actTypevalue']").val()==''||$("input[name='actTypevalue']").val()) {
					filter.actType = $("input[name='actTypevalue']").val();
				}
				// 航班状态
				/*if ($("#filterForm .actStatus").val()==''||$("#filterForm .actStatus").val()) {
					filter.actStatus = $("#filterForm .actStatus").val()=='!'?'':$("#filterForm .actStatus").val();
				}*/
				var actStatusStr = ($("#filterForm .actStatus").val())?$("#filterForm .actStatus").val().join(','):"";
				if(actStatusStr.length > 0){
					filter.acfStatus=actStatusStr;
				}
				// 延误
				var delayStr = ($("#delay").val())?$("#delay").val().join(','):"";
				if(delayStr.length > 0){
					filter.delay=delayStr;
				}
				// 延误原因
				var delyResonStr = ($("#delyReson").val())?$("#delyReson").val().join(','):"";
				if(delyResonStr.length > 0){
					filter.delyReson=delyResonStr;
				}
				// 机号
				filter.aircraftNo=$("input[name='aircraftNo']").val();
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
				
				// 分子公司
				if ($("#filterForm .branch").val()==''||$("#filterForm .branch").val()) {
					filter.branch = $("#filterForm .branch").val()=='!'?'':$("#filterForm .branch").val();
				}
				// 航班日期
				if ($("#fltdate").val()==''||$("#fltdate").val()) {
					filter.fltdate = $("#fltdate").val();
				}
				// 时间类型
				if ($("#filterForm .timetype").val()==''||$("#filterForm .timetype").val()) {
					filter.timetype = $("#filterForm .timetype").val()=='!'?'':$("#filterForm .timetype").val();
				}
				// 开始时间
				if ($("#beginTime").val()==''||$("#beginTime").val()) {
					filter.beginTime = $("#beginTime").val();
				}
				// 结束时间
				if ($("#endTime").val()==''||$("#endTime").val()) {
					filter.endTime = $("#endTime").val();
				}
				
				// 除冰坪
				if ($("#filterForm .dprks").val()==''||$("#filterForm .dprks").val()) {
					filter.dprks = $("#filterForm .dprks").val()=='!'?'':$("#filterForm .dprks").val();
				}
				// 除冰位
				if ($("#filterForm .dcnds").val()==''||$("#filterForm .dcnds").val()) {
					filter.dcnds = $("#filterForm .dcnds").val()=='!'?'':$("#filterForm .dcnds").val();
				}
				// 出港方向
				if ($("#filterForm .direction").val()==''||$("#filterForm .direction").val()) {
					filter.direction = $("#filterForm .direction").val()=='!'?'':$("#filterForm .direction").val();
				}
				// 出港点
				if ($("#filterForm .outDot").val()==''||$("#filterForm .outDot").val()) {
					filter.outDot = $("#filterForm .outDot").val()=='!'?'':$("#filterForm .outDot").val();
				}
				// 慢车除冰
				if ($("#filterForm .slowDeice").val()==''||$("#filterForm .slowDeice").val()) {
					filter.direction = $("#filterForm .slowDeice").val()=='!'?'':$("#filterForm .slowDeice").val();
				}
				// 除冰航班
				/*if ($("#filterForm .iceFlt").val()==''||$("#filterForm .iceFlt").val()) {
					filter.iceFlt = $("#filterForm .iceFlt").val()=='!'?'':$("#filterForm .iceFlt").val();
				}*/
				var iceFltStr = ($("#filterForm .iceFlt").val())?$("#filterForm .iceFlt").val().join(','):"";
				if(iceFltStr.length > 0){
					filter.iceFlt=iceFltStr;
				}
				// 保障状态
				if ($("input[name='guarantee']").val()==''||$("input[name='guarantee']").val()) {
					filter.guarantee = $("input[name='guarantee']").val()=='!'?'':$("input[name='guarantee']").val();
				}
				//后加装卸类型筛选
				if(schemaId=="11"){
					//海航系
					if($('#HHXcheck').is(':checked')) {
						if ($("input[name='HHXairline']").val()==''||$("input[name='HHXairline']").val()) {
							filter.airline2Code = (filter.airline2Code==undefined?"":filter.airline2Code+",")+$("input[name='HHXairline']").val();
						}
						if ($("input[name='HHXalntypevalue']").val()==''||$("input[name='HHXalntypevalue']").val()) {
							filter.alntype = (filter.alntype==undefined?"":filter.alntype+",")+$("input[name='HHXalntypevalue']").val();
						}
					}
					if($('#GHXcheck').is(':checked')) {
						if ($("input[name='GHXairline']").val()==''||$("input[name='GHXairline']").val()) {
							filter.airline2Code = (filter.airline2Code==undefined?"":filter.airline2Code+",")+$("input[name='GHXairline']").val();
						}
						if ($("input[name='GHXalntypevalue']").val()==''||$("input[name='GHXalntypevalue']").val()) {
							filter.alntype = (filter.alntype==undefined?"":filter.alntype+",")+$("input[name='GHXalntypevalue']").val();
						}
					}
					if($('#GJcheck').is(':checked')) {
						if ($("input[name='GJairline']").val()==''||$("input[name='GJairline']").val()) {
							filter.airline2Code = (filter.airline2Code==undefined?"":filter.airline2Code+",")+$("input[name='GJairline']").val();
						}
						if ($("input[name='GJalntypevalue']").val()==''||$("input[name='GJalntypevalue']").val()) {
							filter.alntype = (filter.alntype==undefined?"":filter.alntype+",")+$("input[name='GJalntypevalue']").val();
						}
					}
					if($('#QTcheck').is(':checked')) {
						if ($("input[name='QTairline']").val()==''||$("input[name='QTairline']").val()) {
							filter.airline2Code = (filter.airline2Code==undefined?"":filter.airline2Code+",")+$("input[name='QTairline']").val();
						}
						if ($("input[name='QTalntypevalue']").val()==''||$("input[name='QTalntypevalue']").val()) {
							filter.alntype = (filter.alntype==undefined?"":filter.alntype+",")+$("input[name='QTalntypevalue']").val();
						}
					}
				}
				filter.time_type=time_type;
				if (!vaildFilterForm()) {
					return false;
				}
				tableOptions.queryParams = {
				switches : switches,
				flagBGS : flagBGS,
				schema : $("#schemaId").val(),
				param : JSON.stringify(filter)
				}
				$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
				$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
				
				layer.close(index);
				selectFilter = cloneObj(filter);
				filter = {};
			},
			btn2 : function(index) {// 重置表单及筛选条件
				//$("#filterForm").find("select").val('').removeAttr('selected');
				$(":selected").removeAttr('selected');
				$("#filterForm .select2-selection__choice").remove();
				$("#filterForm .select2-search__field").attr("placeholder", "请选择");
				$("#filterForm .select2-search__field").css("width", "100%");
				$("#filterForm").find("input").attr("checked",false);
				globleForm.render('checkbox');
				//清空搜索条件的时候  将装卸调度的配置过滤掉
				$("#filterForm").find("input").not('input[type=checkbox ]').not('.ignoreClear').val('');
				$("#filterForm .select2-selection--single .select2-selection__rendered").empty();
				$("#filterForm .select2-selection--single .select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
				filter = {};
				getRoleFilter();
				return false;
			},
			btn3 : function(index) {// 关闭前重置表单及筛选条件
				layer.close(index);
			}
		});
		layer.full(filterIframe);
	})
	// 设置
	$("#setting").click(function() {
		var set = layer.open({
			type : 2,
			title : "设置",
			content : ctx + "/flightDynamic/settingList?schema=" + $("#schemaId").val(),
			btn : [ "确定", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var data = iframeWin.getHeadData();
				$.ajax({
					type : 'post',
					url : ctx + "/flightDynamic/saveHeadInfo",
					data : {
						data : data,
						schema : $("#schemaId").val()
					},
					success : function(msg) {
						if (msg == "success") {
							tableOptions.columns = getBaseColumns();
							baseTable.bootstrapTable('refreshOptions', tableOptions);
							$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
						}
					}
				});
				layer.close(index);
			}
		})
		layer.full(set);
	})
	// 查看
	$("#show").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			content : ctx + "/scheduling/getFdById?type=show&id=" + clickRowId,// type用来区分页面来源，控制功能权限
			btn : [ "返回" ],
			success : function(layero, index) {
			}
		});

		layer.full(iframe);// 展开弹出层直接全屏显示
	});
	// 打印
	$("#print").click(function() {
		var data = tableData;
		if(!data){
			data=[];
		}
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
		$.each(tableOptions.queryParams,function(k,v){
			var hidden = $("#printForm input[name="+k+"]");
			if(hidden.length == 0){
				hidden = $('<input type="hidden" name="'+k+'" >');
			}
			hidden.val(v);
			$("#printForm").append(hidden);
		});
		if($("#schemaId").val() == '3'){
			$('#hasSheet2').val('yes');
		}
		$("#printForm").submit();
	});

	// 要客计划导入
	$('#importvip').click(function() {
		iframe = layer.open({
			type : 2,
			title : "要客计划导入",
			closeBtn : false,
			content : ctx + "/flightDynamic/openImportvip"
		});
		layer.full(iframe);
	});
	// 指令
	$('#order').click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		sended(clickRow);
	});

	// 回收站
	$("#recycle").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		layer.open({
			type : 1,
			title : '回收站',
			content : $("#recycleBox"),
			btn : "确定",
			yes : function(index, layero) {
				var type = layero.find("input[type=radio]:checked").data("type");
				$.ajax({
					type : 'post',
					url : ctx + "/flightDynamic/putRecycle",
					data : {
						gbak : clickRowId,
						inFltid : clickRow.in_fltid,
						outFltid : clickRow.out_fltid,
						type : type
					},
					success : function(msg) {
						if (msg == "success") {
							baseTable.bootstrapTable('refresh');
						}
					}
				});
				layer.close(index);
			}
		});
	})

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
	
	//任务指派单
	if(schemaId!='11'){
		//监装调度列表有任务指派单
		$("#mission").attr("style","display:none");
	}
	$('#mission').click(function() {
		if (!clickRow) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		var missionIframe = layer.open({
			type : 2,
			title : "任务指派单打印",
			area:['670px','500px'],
			content : ctx + "/scheduling/list/missionBillForm?inFltId="+clickRow.in_fltid+"&outFltId="+clickRow.out_fltid,
			btn : ['导入货邮行','打印','新增行','保存','取消'],
			btn1 : function(index, layero) {
				var billWin = $(layero).find("iframe")[0].contentWindow;
				var importWin = layer.open({
					type : 1,
					area:['670px','410px'],
					title : "导入货邮行",
					content : $("#import"),
					closeBtn : 0,
					btn : ['保存','取消'],
					yes : function(index, layero) {
						var importData = {};
						importData.fltMail = $("#fltMail").val();
						importData.fltPackage = $("#fltPackage").val();
						if(importData.fltMail==null||importData.fltMail==''||importData.fltPackage==null||importData.fltPackage==''){
							layer.msg('货邮行、行李信息需要同时导入！', {
								icon : 1
							});
							return false;
						}else{
							//导入货邮行信息
							billWin.importFun(importData);
							//关闭货邮行弹窗
							layer.close(importWin);
							//重置货邮行输入框内容
							$("#fltMail").val("");
							$("#fltPackage").val("");
						}
					},
					end : function(index, layero) {
						$("#fltMail").val("");
						$("#fltPackage").val("");
					}
				});
				layer.full(importWin);
				return false;
			},
			btn2 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.print();
				return false;
			},
			btn3 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.addRow();
				return false;
			},
			btn4 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.saveFun();
				return false;
			}
		});
	});
	
	//服务项目确认书
	if(schemaId!='13'){
		//旅客服务调度列表有任务指派单
		$("#serviceConfirm").attr("style","display:none");
	}
	$('#serviceConfirm').click(function() {
		if (!clickRow) {
			layer.msg('请选择一条任务', {
				icon : 2
			});
			return false;
		}
		//打印服务项目确认书
		$("#exportForm").attr("action",ctx + "/scheduling/list/exportServiceConfirm?inFltId="+clickRow.in_fltid+"&outFltId="+clickRow.out_fltid);
		$("#exportForm").submit();
	});
	
});

function getRoleFilter() {
	$(".checkBox").each(function(i,ele){
		  if($(ele).is(':checked')){
			  filter[ele.name] = 'Y';
		  }else{
			  filter[ele.name] = '';
		  }
	  });
}

function roleFilter() {
	clearSelectedRow();
	//getRoleFilter();
	tableOptions.queryParams = {
	switches : switches,
	flagBGS : flagBGS,
	schema : $("#schemaId").val(),
	param : JSON.stringify(filter)
	}
	$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
	$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
	
}

//深复制对象方法    
var cloneObj = function (obj) {  
    var newObj = {};  
    if (obj instanceof Array) {  
        newObj = [];  
    }  
    for (var key in obj) {  
        var val = obj[key];  
        newObj[key] = typeof val === 'object' ? cloneObj(val): val;  
    }  
    return newObj;  
}; 

/**
 * 作业管理
 */
function jobManage() {
	var schemaId = $("#schemaId").val();
	var inFltId = "";// 进港航班ID
	var outFltId = "";// 出港航班ID
	if (!clickRow) {
		layer.msg("请选择一行记录！", {
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

// 表格列选项默认设置
jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
jQuery.fn.bootstrapTable.columnDefaults.align = "center";
var limit = 100;
var page = 1;
var tableData;
getRoleFilter();
var tableOptions = {
	url : ctx + "/scheduling/list/getDynamic", // 请求后台的URL（*）
	method : "get", // 请求方式（*）
	dataType : "json", // 返回结果格式
	striped : true, // 是否显示行间隔色
	pagination : false, // 是否显示分页（*）
	cache : false, // 是否启用缓存
	undefinedText : '', // undefined时显示文本
	checkboxHeader : false, // 是否显示全选
	toolbar : $("#tool-box"), // 指定工具栏dom
	search : true,
	searchOnEnterKey : true,
	columns : getBaseColumns(),
	contextMenu : '#context-menu',
	queryParams : {
		switches : switches,
		flagBGS:flagBGS,
		schema : $("#schemaId").val(),
		suffix : "",
		param : JSON.stringify(filter)
	},
	responseHandler : function(res) {
		tableData = res;
		if($("#schemaId").val()==6){
			var haveETA = false;
			for(var i=0;i<res.length;i++){
				if(res[i].in_eta){
					haveETA = true;
				}
			}
			if(!haveETA){
				$("#baseTable").bootstrapTable('hideColumn','in_eta');
			}else{
				$("#baseTable").bootstrapTable('showColumn','in_eta');
			}
		}
		return res.slice(0, limit);
	},
	onLoadSuccess:function(){
//		var tableBody = $("#baseTables").find('.fixed-table-body');
//		tableBody[0].removeEventListener('ps-y-reach-end',load);
//		tableBody[0].addEventListener('ps-y-reach-end',load);
		$("#refresh").find(".fa-spinner").addClass("fa-clock-o").removeClass("fa-spinner");
	},
	customSearch : function(text) {
		clearSelectedRow();
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
		} else {
			this.data = tableData;
		}
	},
	customSort : function(fieldName,fieldOrder){
		sortName = fieldName;
		sortOrder = fieldOrder;
		this.data.sort(function(a,b){
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
                if (val1<val2) {
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
            
            if (val1.localeCompare(val2,"zh") === -1) {
                return order * -1;
            }
            
            return order;
		});
		this.data = this.data.slice(0,limit);
	},
	// 右键菜单
	onContextMenuItem : function(row, $el) {
		if ($el.data("item") == "instructionView") {
			sended(row);
			// 右键发送报文
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
		} else if ($el.data("item") == "ssgDetail") {
		} else if($el.data("item") == "exception"){
			openException(row);
		} else if($el.data("item") == "dGateInput"){
			dGateInput(row);
		} else if($el.data("item") == "delayInfo"){
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
	onContextMenuRow : function() {
		$("#context-menu ul").hide();
	},
	onClickRow : function(row, tr, field) {
		if (field) {
			clearSelectedRow();
			//row = baseTable.bootstrapTable("getData")[tr.data("index")];
			var th = $("#baseTable").find(".separator").eq(0);
			var ex = $("th[data-field=" + field + "]").offset().left;
			if (th.length > 0) {
				var borderLeft = th.offset().left+th.width();
				if (ex >= borderLeft) {
					if(row.out_fltid){
						clickRowId = row.out_fltid;
					}else{
						return false;
					}
				} else {
					if(row.in_fltid){
						clickRowId = row.in_fltid;
					}else{
						return false;
					}
				}
			} else {
				clickRowId = row.out_fltid;
			}
			if(clickRowId){
				clickRow = row;
				$(tr).addClass("clickRow");
			}
		}
	},
	onDblClickRow : function(row,tr,field){
		//baochl_20180717 匿名账号禁止操作
		if($("#loginName").val()=="anonymous"){
			return false;
		}
		console.log(field);
		//进港信息
		if(field=="in_special_cargo"||field=="in_zx_remark"){
			var fltid=row.in_fltid;
			if(fltid!=''){
				iframe = layer.open({
					type : 2,
					title : "进港装卸变更",
					closeBtn : false,
					area : ['500px','300px'],
					content : ctx+"/scheduling/change/changeZXInInfo?fltid="+fltid,
					btn : [ "保存", "取消" ],
					btn1 : function(index, layero) {
						var iframeWin = window[layero.find('iframe')[0]['name']];
						iframeWin.saveForm();
						return false;
					}
				});
			}
				
		}
		//出港信息
		if(field=="out_baggage_real"||field=="out_zx_remark"||field=="out_large_baggage"){
			var fltid=row.out_fltid;
			if(fltid!=''){
				iframe = layer.open({
					type : 2,
					title : "出港装卸变更",
					closeBtn : false,
					area : ['500px','300px'],
					content : ctx+"/scheduling/change/changeZXOutInfo?fltid="+fltid,
					btn : [ "保存", "取消" ],
					btn1 : function(index, layero) {
						var iframeWin = window[layero.find('iframe')[0]['name']];
						iframeWin.saveForm();
						return false;
					}
				});
			}
				
		}
		//修改机务备注
		if(field=="in_maintenance_remark" || field=="out_maintenance_remark"){
			var maintenanceBtn = $("#editMaintenanceBtn");
			console.info(maintenanceBtn&&maintenanceBtn.length>0)
			//有修改权限
			if(maintenanceBtn&&maintenanceBtn.length>0){
				var inFltid=row.in_fltid;
				var outFltid = row.out_fltid;
				if(fltid!=''){
					iframe = layer.open({
						type : 2,
						title : "机务备注",
						closeBtn : false,
						area : ['500px','300px'],
						content : ctx+"/scheduling/change/changeMaintenance?inFltid="+inFltid+"&outFltid=" + outFltid+"&field=maintenance_remark",
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
		page++;
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

//右键货邮行
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
		content : ctx + "/flightDynamic/massageChange?id=" + id
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

// 右键发送报文
function sendMessage(row) {
	iframe = layer.open({
		type : 2,
		title : '报文发送',
		closeBtn : false,
		content : ctx + "/message/common/sendMessageList",
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

/*//取消航班
function cancelFlt(row) {
	var fltid;
	if (ioFlag == "I") {
		fltid = row.in_fltid
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid
	}
	$.ajax({
		type:'post',
		url:ctx+"/flightDynamic/isByHand",
		data:{
			fltId:clickRowId
		},
		success:function(res){
			if(res=="true"){
				var confirm = layer.confirm('您确定要取消选中航班？', {
					btn : [ '确定', '取消' ]
				// 按钮
				}, function() {
					$.ajax({
						type:'post',
						url:ctx+"/flightDynamic/cancleFlt",
						data:{
							fltId:clickRowId
						},
						success:function(res){
							if(res=="success"){
								layer.msg('取消航班成功', {
									icon : 1
								});
							}else{
								layer.msg('取消航班失败', {
									icon : 2
								});
							}
						}
					});
				});
			}else{
				layer.msg('仅支持修改手工动态', {
					icon : 2
				});
			}
		}
	});
}

//删除航班
function delFlt(row) {
	var fltid;
	if (ioFlag == "I") {
		fltid = row.in_fltid
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid
	}
	$.ajax({
		type:'post',
		url:ctx+"/flightDynamic/isByHand",
		data:{
			fltId:clickRowId
		},
		success:function(res){
			if(res=="true"){
				var confirm = layer.confirm('您确定要删除选中航班？', {
					btn : [ '确定', '取消' ]
				// 按钮
				}, function() {
					$.ajax({
						type:'post',
						url:ctx+"/flightDynamic/delFlt",
						data:{
							fltId:clickRowId
						},
						success:function(res){
							if(res=="success"){
								layer.msg('删除航班成功', {
									icon : 1
								});
							}else{
								layer.msg('删除航班失败', {
									icon : 2
								});
							}
						}
					});
				});
			}else{
				layer.msg('仅支持修改手工动态', {
					icon : 2
				});
			}
		}
	});
}*/

// 指令
function sended(row) {
	var fltid;
	if (ioFlag == "I") {
		fltid = row.in_fltid
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid
	}
	var num = "4";
	var msg = layer.open({
		type : 2,
		title : '指令消息',
		zIndex : 99999999999,
		maxmin : false,
		shadeClose : false,
		area:[$("body").width()-200+"px",$("body").height()-200+"px"],
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
	if (row.aircraft_number) {
		flightNumber = row.aircraft_number;
	}
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
	}
	if (ioFlag == "O") {
		fltid = out_fltid;
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
			'schema' : $("#schemaId").val()
		},
		dataType : 'json',
		success : function(column) {
			var order = [ {
				field : 'order',
				title : '序号',
				sortable : false,
				editable : false,
				width : 44,
				formatter : function(value, row, index) {
					return index + 1;
				}
			} ];
			for(var i in column){
				if (column[i].field == "in_fltno"||column[i].field == "in_flight_number2") {
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
				if (column[i].field == "out_fltno"||column[i].field == "out_flight_number2") {
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
				if (column[i].field == "in_aircraft_number") {
					column[i].formatter = function(value, row, index) {
						if (!value) {
							return '';
						}
						var className = "";
						if (row.in_flt_air_status == "1") {
							className = "background-color:yellow;color:black;";
						}
						return '<span style=\'' + className + '\' >' + value + '</span>';
					}
				}
				if (column[i].field == "out_aircraft_number") {
					column[i].formatter = function(value, row, index) {
						if (!value) {
							return '';
						}
						var className = "";
						if (row.out_flt_air_status == "1") {
							className = "background-color:yellow;color:black;";
						}
						return '<span style=\'' + className + '\' >' + value + '</span>';
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
				if(column[i].field.indexOf("actstand_code_chg")!=-1 || column[i].field.indexOf("aircraft_number_chg")!=-1 || column[i].field.indexOf("gate_chg")!=-1){
					column[i].formatter = function(value, row, index) {
						if (!value) {
							return '';
						}else if(value.indexOf("->")!=-1){
							var className = "col-background-yellow";
							return '<span class=\'' + className + '\'>' + value + '</span>';
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
			url : ctx + '/scheduling/list/getOptions',
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
	var param = {
		"ioFlag" : encodeURIComponent(ioFlag)
	};
	$.getJSON(ctx + "/message/common/getTemplateList", param, function(data) {
		if (data.result) {
			var html = '';
			var templateList = data.templateList;
			$("#context-menu li.divider:last").nextAll().remove();
			$(templateList).each(function(index, element) {
				$("#context-menu").append('<li  data-item="' + element.ID + '"><a>' + element.MTITLE + '</a></li>');
			});
		} else {
			layer.alert("获取模板失败或登录超时", {
				icon : 0,
				time : 1000
			});
		}
	}).success(function() {
	}).fail(function() {
		layer.alert("获取模板失败！", {
			icon : 0,
			time : 1000
		});
	});
}
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
/**
 * 自动刷新
 * baochl 20171117
 * @param interval 刷新频率
 */
var intervalObj = null;
var refreshUnit = 1000;//刷新单位是分钟
function autoReload(interval){
	if(interval<10){
		layer.msg("最小刷新频率不要小于10秒！",{icon:7});
		return false;
	}
	if(sessionStorage){
		sessionStorage.setItem(AUTO_RELOAD_SESSION,interval);
	}
	var time = Number(interval) * refreshUnit;
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟";
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		//刷新前滚动条
		var beforeScroll = $(".fixed-table-body").scrollTop();
		$.ajax({
			type : 'post',
			url : ctx + "/scheduling/list/getDynamic",
			dataType:"json",
			beforeSend:function(){
				$("#refresh").html("<i class='fa fa-spinner' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "秒钟");
			},
			error:function(){
				$("#refresh").text("刷新失败");
			},
			data :tableOptions.queryParams,
			success : function(result) {
				clearSelectedRow();
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
				$("#refresh").html(btnText);
			}
		});

        if($("#error").length > 0){
            $("#error").click(function(){
                $("#error .label").remove();
            });
            var user = $("#user").val();
            if(!localStorage["latestErrorTimeOf"+user]){
                localStorage["latestErrorTimeOf"+user] = "1970-01-01 00:00:00";
            }
            var time = localStorage["latestErrorTimeOf"+user];
            $.ajax({
                type:'post',
                url:ctx+"/taskmonitor/getUnreadErrorNum",
                data:{
                    time:time
                },
                success:function(num){
                    $("#error .label").remove();
                    if(num != 0){
                        $("#error").append("<span class=\"label label-danger\">"+num+"</span>");
                    }
                }
            });
        };
	}, time);
}
function refreshFunc() {
	//刷新前滚动条
	var beforeScroll = $(".fixed-table-body").scrollTop();
	$.ajax({
		type : 'post',
		url : ctx + "/scheduling/list/getDynamic",
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

    if($("#error").length > 0){
        $("#error").click(function(){
            $("#error .label").remove();
        });
        var user = $("#user").val();
        if(!localStorage["latestErrorTimeOf"+user]){
            localStorage["latestErrorTimeOf"+user] = "1970-01-01 00:00:00";
        }
        var time = localStorage["latestErrorTimeOf"+user];
        $.ajax({
            type:'post',
            url:ctx+"/taskmonitor/getUnreadErrorNum",
            data:{
                time:time
            },
            success:function(num){
                $("#error .label").remove();
                if(num != 0){
                    $("#error").append("<span class=\"label label-danger\">"+num+"</span>");
                }
            }
        });
    };

    if(sessionStorage.getItem(AUTO_RELOAD_SESSION)&&sessionStorage.getItem(AUTO_RELOAD_SESSION)!='null'){
        autoReload(sessionStorage.getItem(AUTO_RELOAD_SESSION));
    }
}
/**
 * baochl 20171125 清除选中行
 */
function clearSelectedRow(){
	clickRowId = "";
	clickRow = null;
	$(".clickRow").removeClass("clickRow");
}

function saveSuccess(){
	layer.close(iframe);
	// 刷新前滚动条
	var beforeScroll = $(".fixed-table-body").scrollTop();
	$.ajax({
	type : 'post',
	url : ctx + "/scheduling/list/getDynamic",
	dataType : "json",
	error : function() {
		$("#refresh").text("刷新失败");
	},
	data : tableOptions.queryParams,
	success : function(result) {
		clearSelectedRow();
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

//校验FilterForm
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
//筛选弹出复选框
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
		//如果是装卸调度的筛选设置  要保存
		if(schemaId==11&&(type=="GJairline")||(type=="HHXairline")||(type=="GHXairline")||(type=="QTairline")||
				(type=="GJalntype")||(type=="HHXalntype")||(type=="GHXalntype")||(type=="QTalntype")){
			var value=data.chooseValue;
			var fieldName="";//更新哪个字段
			var valueType="";//更新哪一行
			if(type.indexOf("airline")!=-1){
				fieldName="AIRLINE_CODE";
				valueType=type.replace("airline","");
				
			}else if(type.indexOf("alntype")!=-1){
				fieldName="ATTR_TYPE";
				valueType=type.replace("alntype","");
			}
			$.ajax({
				type : 'post',
				async : false,
				url : ctx + "/scheduling/list/printFilter",
				data : {
					'type' :valueType,
					'fieldName':fieldName,
					'value':value
				},
				error : function() {
					layer.close(loading);
					layer.msg('保存失败！', {
						icon : 2
					});
				},
				success : function(data) {
					if(data.code=="0000"){
						layer.msg(data.msg, {
							icon : 1
						});
					}else{
						layer.msg(data.msg, {
							icon : 2
						});
					}
				}
			});
			
		}
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

function searchOpt(text,obj) {
	searchOption=text;
	var icon=$("button[name='searchOptBtn']").find("span").eq(0);
	$("button[name='searchOptBtn']").text($(obj).text());
	$("button[name='searchOptBtn']").append(icon);
	clearSelectedRow();
	refreshFunc();
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

/**
 * 登机口录入
 * @param row
 */
function dGateInput(row){
	var fltid = row.out_fltid;
	if(!fltid){
		layer.msg('请选择一个出港动态', {icon : 2});
		return false;
	}
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/scheduling/list/isMix",
		data : {
			'fltId' :fltid
		},
		error : function() {
			layer.close(loading);
			layer.msg('录入失败！', {
				icon : 2
			});
		},
		success : function(data) {
			layer.close(loading);
			if (data == "yes") {
				iframe = layer.open({
					type : 2,
					area : ['500px','200px'],
					title : "登机口录入",
					content : ctx + "/scheduling/list/dGateInput?fltid="+fltid,
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
							url : ctx + "/scheduling/list/saveDGate",
							data : {
								'json' :JSON.stringify(json)
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
										saveSuccess();
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
			} else {
				layer.msg('不是混合航班！', {
					icon : 2
				});
				return false;
			}
		}
	});
	return false;
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
//	var timeVal = obj.options[obj.selectedIndex].value;
//	var timeCond = {time_type:timeVal};
//	tableOptions.queryParams.param = JSON.stringify(timeCond);
	time_type=obj.options[obj.selectedIndex].value;
	selectFilter.time_type=time_type;
	console.log(JSON.stringify(selectFilter));
	tableOptions.queryParams.param=JSON.stringify(selectFilter);
	//刷新前滚动条
//	var beforeScroll = $(".fixed-table-body").scrollTop();
	var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
	$.ajax({
		type : 'post',
		url : ctx + "/scheduling/list/getDynamic",
		dataType:"json",
		data:tableOptions.queryParams,
		success : function(result) {
			layer.close(loading);
			clearSelectedRow();
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

// 旅客服务——航延信息录入
function delayInfo(row){
	var fltid = "";
	if (ioFlag == "I") {
		fltid = row.in_fltid;
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid;
	}
	layer.open({
		type : 2,
		title : false,
		closeBtn : false,
		area : [ '100%', '550px' ],
		content : ctx + "/bill/fwDelay/edit?fltid=" + fltid,
		btn : [ "保存","重置","返回" ],
		yes:function(index,layero){
			$(layero).find('iframe')[0].contentWindow.saveDelayInfo(index);
			return false;
		},
		btn2:function(index,layero){
			$(layero).find('iframe')[0].contentWindow.resetDelay();
			return false;
		}
	});
}

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