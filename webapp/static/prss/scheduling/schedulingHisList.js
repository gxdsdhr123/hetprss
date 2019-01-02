var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
var clickRow;
var filter;// 用于筛选
var iframe;
var ioFlag;
var sortName;
var sortOrder;
var searchOption="";
var flagBGS=0;
var filter = {};
var globleForm;
var tableOptions = {};
$(document).ready(function() {
	document.oncontextmenu = function(e) {
		return false;
	}
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		var form = layui.form;
		globleForm=form;
		/*form.on('switch(BGS)', function(data) {
			flagBGS = data.elem.checked ? 1 : 0;
			tableOptions.queryParams.flagBGS = flagBGS;
			baseTable.bootstrapTable('refreshOptions',tableOptions);
			$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
			
		});*/
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
	})
	function addzero(v) {
		if (v < 10){
			return '0' + v;
		}
		return v.toString();
	}
	var d = new Date(new Date().getTime()-2*24*60*60*1000);
	var s = d.getFullYear().toString() + addzero(d.getMonth() + 1)+ addzero(d.getDate());
	$("#hisdate").val(s);
	$("#baseTables").css("height","100%");
	$("#baseTable").each(function(){
		$(this).on('load-success.bs.table',function(thisObj){
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody[0].removeEventListener('ps-y-reach-end',load);
			tableBody[0].addEventListener('ps-y-reach-end',load);
		});
	});
	
	baseTable = $("#baseTable");
	tableOptions = {
			url : ctx + "/schedulingHisList/getDynamic", // 请求后台的URL（*）
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
				schema : $("#schemaId").val(),
				suffix : "_HIS",
				hisDate : $("#hisdate").val(),
				flagBGS : flagBGS,
				param : JSON.stringify(filter)
			},
			responseHandler : function(res) {
				tableData = res;
				return res.slice(0, limit);
			},
			onLoadSuccess:function(){
//				var tableBody = $("#baseTables").find('.fixed-table-body');
//				tableBody[0].removeEventListener('ps-y-reach-end',load);
//				tableBody[0].addEventListener('ps-y-reach-end',load);
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
				} else if ($el.data("item") == "fltDetail") {
				} else if ($el.data("item") == "passenger") {
					passenger(row);
				} else if ($el.data("item") == "baggage") {
					baggage(row);
				} else if ($el.data("item") == "timeDynamic") {
					timeDynamic(row);
				} else if ($el.data("item") == "resourseState") {
					resourseState(row);
				} else if ($el.data("item") == "massageChange") {
					massageChange(row);
				} else if ($el.data("item") == "vipInfo") {
					vipInfo(row);
				} else if($el.data("item") == "exception"){
					openException(row);
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
			}
		};
	tableOptions.height = $("body").height();// 表格适应页面高度

	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));

	// 作业管理
	$("#jobManageBtn").click(function() {
		jobManage();
	});

	// 异常情况查看
	$("#error").click(function() {
		iframe = layer.open({
			type : 2,
			title : "异常情况查看",
			area : [ '100%', '100%' ],
			content : ctx + "/flightDynamic/error?hisDate="+$("#hisdate").val()+"&hisFlag=his"
		});
	});
	
	// 保障
	$("#ensure").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条动态', {
				icon : 2
			});
			return false;
		}
		$('body').on('layui.layer.open',function(){
			$('.layui-layer-close').removeClass('layui-layer-close2').addClass('layui-layer-close1');
		});
		var inFltid = clickRow.in_fltid ? clickRow.in_fltid : '';
		var outFltid = clickRow.out_fltid ? clickRow.out_fltid : '';
		iframe = layer.open({
		type : 2,
		title : false,
		closeBtn : 1,
		content : ctx + "/fltmonitor/his?inFltid=" + inFltid + "&outFltid=" + outFltid// type用来区分页面来源，控制功能权限
		});

		layer.full(iframe);// 展开弹出层直接全屏显示
	});

	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})

	getOptions();

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
			getMessageMenu(ioFlag);
		}
	})
	//刷新
	$("#refresh").click(function() {
		clearSelectedRow();
		baseTable.bootstrapTable('refresh');
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
				
				var actKindsArr=new Array();
				$('input[name="actkinds"]:checked').each(function(){
					actKindsArr.push($(this).val());
				});
				var kindsStr=actKindsArr.join(',');
				if(kindsStr.length>0){
					filter.actkinds=kindsStr;
				}
				
				
				// 始发/中转
				var stayFlagStr = ($("#filterForm .stayFlag").val())?$("#filterForm .stayFlag").val().join(','):"";
				if(stayFlagStr.length > 0){
					filter.stayFlag=stayFlagStr;
				}
				/*if ($("#filterForm .stayFlag").val()==''||$("#filterForm .stayFlag").val()) {
					filter.stayFlag = $("#filterForm .stayFlag").val()=='!'?'':$("#filterForm .stayFlag").val();
				}*/
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
				var GAFlagStr = ($("#GAFlag").val())?$("#GAFlag").val().join(','):"";
				if(GAFlagStr.length > 0){
					filter.GAFlag=GAFlagStr;
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
				// 登机口
				if ($("input[name='gatevalue']").val()==''||$("input[name='gatevalue']").val()) {
					filter.gate = $("input[name='gatevalue']").val();
				}
				// 航班状态
				if ($("#filterForm .actStatus").val()==''||$("#filterForm .actStatus").val()) {
					filter.actStatus = $("#filterForm .actStatus").val()=='!'?'':$("#filterForm .actStatus").val();
				}
				
				// 分子公司
				if ($("#filterForm .branch").val()==''||$("#filterForm .branch").val()) {
					filter.branch = $("#filterForm .branch").val()=='!'?'':$("#filterForm .branch").val();
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
				var iceFltStr = ($("#filterForm .iceFlt").val())?$("#filterForm .iceFlt").val().join(','):"";
				if(iceFltStr.length > 0){
					filter.iceFlt=iceFltStr;
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
				//状态
				var actStatusStr = ($("#filterForm .actStatus").val())?$("#filterForm .actStatus").val().join(','):"";
				if(actStatusStr.length > 0){
					filter.acfStatus=actStatusStr;
				}
				/*if ($("#filterForm .iceFlt").val()==''||$("#filterForm .iceFlt").val()) {
					filter.iceFlt = $("#filterForm .iceFlt").val()=='!'?'':$("#filterForm .iceFlt").val();
				}*/
				// 保障状态
				if ($("#filterForm .guarantee").val()==''||$("#filterForm .guarantee").val()) {
					filter.guarantee = $("#filterForm .guarantee").val()=='!'?'':$("#filterForm .guarantee").val();
				}
				
				if (!vaildFilterForm()) {
					return false;
				}
				tableOptions.queryParams = {
						schema : $("#schemaId").val(),
						suffix : "_HIS",
						hisDate : $("#hisdate").val(),
						flagBGS : flagBGS,
						param : JSON.stringify(filter)
				}
				$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
				$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
				
				layer.close(index);
			},
			btn2 : function(index) {// 重置表单及筛选条件
				$("#filterForm").find("select").val('').removeAttr('selected');
				$("#filterForm .select2-selection__choice").remove();
				$("#filterForm .select2-search__field").attr("placeholder", "请选择");
				$("#filterForm .select2-search__field").css("width", "100%");
				$("#filterForm").find("input").val('');
				$("#filterForm").find("input").attr("checked",false);
				globleForm.render('checkbox');
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
	
	$(".mutilselect2").select2({
		placeholder : "请选择",
		width : "100%",
		language : "zh-CN"
    });

	// 报文
	$("#message").click(function() {
		if (!clickRowId) {
			layer.msg('请选择一条动态', {
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
			schema : $("#schemaId").val(),
			suffix : "_HIS",
			hisDate : $("#hisdate").val(),
			flagBGS : flagBGS,
			param : JSON.stringify(filter)
	}
	$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
	$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
	
}

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
		content : [ ctx + "/scheduling/jobManage?schemaId=" + schemaId + "&inFltId=" + inFltId + "&outFltId=" + outFltId + "&hisFlag=his" ]
	});
}

// 表格列选项默认设置
jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
jQuery.fn.bootstrapTable.columnDefaults.align = "center";
var limit = 100;
var page = 1;
var tableData;
getRoleFilter();

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
		content : ctx + "/flightDynamic/passenger?inFltId=" + clickRow.in_fltid + "&outFltId=" + clickRow.out_fltid+ "&his='his'",
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
		content : ctx + "/flightDynamic/baggage?infltid=" + row.in_fltid+"&outfltid=" + row.out_fltid+ "&his='his'",
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
	content : ctx + "/flightDynamic/timeDynamic?id=" + id+ "&his='his'"
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
	content : ctx + "/flightDynamic/resourseState?id=" + id+ "&his='his'"
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
	content : ctx + "/flightDynamic/massageChange?id=" + id+ "&his='his'"
	});
}

//右键要客详情
function vipInfo(row) {
	var hisFlag="hisFlag";
	iframe = layer.open({
		type : 2,
		title : "要客详情",
		content : ctx + "/flightDynamic/vipInfo?inFltId=" + clickRow.in_fltid + "&outFltId=" + clickRow.out_fltid + "&inFltNo=" + clickRow.in_fltno + "&outFltNo=" + clickRow.out_fltno+ "&hisFlag=" + hisFlag,
		area : [ '700px', '400px' ],
		btn : ["返回" ],
		
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

// 指令
function sended(row) {
	var fltid;
	if (ioFlag == "I") {
		fltid = row.in_fltid
	}
	if (ioFlag == "O") {
		fltid = row.out_fltid
	}
	var num = "64";
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
						if (row.inStatus == "4") {
							className = "col-state-1";
							title = "前站已起飞";
						} else if (row.inStatus == "5") {
							className = "col-state-2";
							title = "本场已落地";
						} else if (row.inStatus == "3") {
							className = "col-state-3";
							title = "延误";
						} else if (row.inStatus == "1" || row.inStatus == "2") {
							className = "col-state-5";
							title = row.inStatus == "1"?"取消":"预出";
						} else if (row.inStatus == "6" || row.inStatus == "7") {
							className = "col-state-6";
							title = row.inStatus == "6"?"返航":"备降";
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
						var title = "前站未起飞";
						if(row.inStatus == "5"){
							className = "col-state-4";
							title = "本场预出";
						}
						if (row.outStatus == "4") {
							className = "col-state-1";
							title = "本场已起飞";
						} else if (row.outStatus == "5") {
							className = "col-state-2";
							title = "后站已落地";
						} else if (row.outStatus == "3") {
							className = "col-state-3";
							title = "延误";
						} else if (row.outStatus == "1" || row.outStatus == "2") {
							className = "col-state-5";
							title = row.outStatus == "1"?"取消":"预出";
						} else if (row.outStatus == "6" || row.outStatus == "7") {
							className = "col-state-6";
							title = row.outStatus == "6"?"返航":"备降";
						}
						return '<span class=\'' + className + '\' title=\''+title+'\'>' + value + '</span>';
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
				delete column[i].editable;
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
}
/**
 * 单航班异常查看
 * @param row
 */
function openException(row){
	var hisFlag="hisFlag";
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
		content : ctx + "/flightDynamic/error?fltid="+fltid+ "&hisFlag=" + hisFlag,
	});
}

//历史日期选择
function dateFilter() {
	WdatePicker({
	maxDate:'%y-%M-{%d-2}',
	dateFmt : 'yyyyMMdd',
	onpicking : function(dp) {
		tableOptions.queryParams = {
				schema : $("#schemaId").val(),
				suffix : "_HIS",
				hisDate : dp.cal.getNewDateStr(),
				flagBGS : flagBGS,
				param : JSON.stringify(filter)
		}
		$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
		$(".pull-right.search").addClass("input-group margin").width('15%').prepend($("#ssConf").clone().removeAttr("style"));
	}
	})
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