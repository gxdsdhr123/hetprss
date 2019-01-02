var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId;// 当前选中行，以便单选后操作
var page = 1;
var limit = 100;
var tableData;
var clickRow;
var sortName;
var sortOrder;
var searchOption="";
var ioFlag;
var filter = {};
var globleForm;
var searchData = [];
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		var form = layui.form;
		globleForm=form;
		listenFilterCheckbox();
	})
	var d = new Date();
	    function addzero(v) {
		if (v < 10)
			return '0' + v;
		return v.toString();
	}
	    
	var s = d.getFullYear().toString() + addzero(d.getMonth() + 1)+ addzero(d.getDate()-2);
	$("#hisdate").val(s);
	$("#baseTables").css("cssText","height:calc(100% - "+$("#totalBox").outerHeight(true)+"px)");
	baseTable = $("#baseTable");
	tableOptions.height = $(window).height()-10;// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	getOptions();
	
	$("#totalBox li").css("float","left");
	$("#refresh").click(function() {
		baseTable.bootstrapTable('refresh');
	})
	// 加载前一百条 滑轮滚动加载
//	$("#baseTable").each(function() {
//		$(this).on('load-success.bs.table', function(thisObj) {
//			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
//			tableBody[0].removeEventListener('ps-y-reach-end', load);
//			tableBody[0].addEventListener('ps-y-reach-end', load);
//		});
//	});
	
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
	
	//隐藏筛选中fltdate航班日期
	$("#filt_fltdate").hide();
	//筛选
	$("#filter").click(function() {
		var filterIframe = layer.open({
			type : 1,
			title : "筛选",
			content : $("#filterDiv"),
			btn : [ "确定", "重置", "取消" ],
			yes : function(index) {
				clearSelectedRow();
				searchData = [];
				// 日期范围
				filter.dateB=$("#dateB").val();
				filter.dateE=$("#dateE").val();
				// 时间
				filter.beginTime = $("#beginTime").val();
				filter.endTime = $("#endTime").val();
				// 航空公司x
				var airline = $("#airlinevalue").val();
				if(airline!=""){
					airline = "'"+airline.replace(/,/g,"','")+"'"
				}
				
				filter.airline=airline;
				// 机坪
				var apron=$("#aprons").val();
				filter.apron = apron=='!'?'':apron;
				// 廊桥/远机位
				var GAFlag=$("#GAFlag").val();
				filter.GAFlag = GAFlag=='!'?'':GAFlag;
				// 起场x
				var departAirportvalue = $("#departAirportvalue").val();
				if(departAirportvalue!=""){
					departAirportvalue = "'"+departAirportvalue.replace(/,/g,"','")+"'"
				}
				filter.departAirport = departAirportvalue;
				// 落场x
				var arriveAirportvalue = $("#arriveAirportvalue").val();
				if(arriveAirportvalue!=""){
					arriveAirportvalue = "'"+arriveAirportvalue.replace(/,/g,"','")+"'"
				}
				filter.arriveAirport = arriveAirportvalue;
				// 航班性质
				var alntype=$("#alntype").val();
				filter.alntype = alntype=='!'?'':alntype;
				//性质
				var fltPropertys=$("#fltPropertys").val();
				filter.fltPropertys = fltPropertys=='!'?'':fltPropertys;
				// 除冰航班
				var iceFlt=$("#iceFlt").val();
				filter.iceFlt = iceFlt=='!'?'':iceFlt;
				// 状态
				var actStatus=$("#actStatus").val();
				filter.actStatus = actStatus=='!'?'':actStatus;
				// 延误
				var delayType=$("#delayType").val();
				filter.delayType = delayType=='!'?'':delayType;
				// 延误原因
				var delyReson=$("#delyReson").val();
				filter.delyReson = delyReson=='!'?'':delyReson;
				// 机号
				var aircraftNo=$("#aircraftNo").val();
				filter.aircraftNo = aircraftNo;
				// 机型x
				var actTypevalue = $("#actTypevalue").val();
				if(actTypevalue!=""){
					actTypevalue = "'"+actTypevalue.replace(/,/g,"','")+"'"
				}
				filter.actTypevalue = actTypevalue;
				// 登机口x
				var gatevalue = $("#gatevalue").val();
				if(gatevalue!=""){
					gatevalue = "'"+gatevalue.replace(/,/g,"','")+"'"
				}
				filter.gatevalue = gatevalue;
				
				filter.searchKey = "";
				filter.searchValue = "";
				if (!vaildFilterForm()) {
					return false;
				}
				tableOptions.queryParams = function(params){
					sortName= params.sort;
				    sortOrder= params.order;
					return {
						pageNumber: (params.offset / params.limit) + 1,
					    pageSize: params.limit,
						schema : "3",
						param : JSON.stringify(filter),
						suffix:"_HIS",
						sortName: params.sort,      //排序列名  
					    sortOrder: params.order //排位命令（desc，asc）
			        }
				}
				$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
				layer.close(index);
			},
			btn2 : function(index) {// 重置表单及筛选条件
				$("#filterForm").find("select").val('').removeAttr('selected');
				$("#filterForm .select2-selection__choice").remove();
				$("#filterForm .select2-search__field").attr("placeholder", "请选择");
				$("#filterForm .select2-search__field").css("width", "100%");
				$("#filterForm").find("input").attr("checked",false);
				$("#filterForm").find("input").not('input[type=checkbox]').val('');
				globleForm.render('checkbox');
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
	// 设置
	$("#setting").click(function() {
		var set = layer.open({
		type : 2,
		title : false,
		closeBtn:false,
		content : ctx + "/flightDynamic/settingList?schema=3",
		btn : [ "确定", "取消" ],
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var data = iframeWin.getHeadData();
			$.ajax({
			type : 'post',
			url : ctx + "/flightDynamic/saveHeadInfo",
			data : {
			data : data,
			schema : "3"
			},
			success : function(msg) {
				if (msg == "success") {
					tableOptions.columns = getBaseColumns();
					baseTable.bootstrapTable('refreshOptions', tableOptions);
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
		if (clickRowId == "") {
			layer.msg('请选择一个计划', {
				icon : 2
			});
			return false;
		}
		$.ajax({
		type : 'post',
		url : ctx + "/fdHistorical/ifGbakExist",
		data : {
		fltid : clickRowId,
		suffix : "_his"
		},
		success : function(res) {
			if (res == "exist") {
				iframe = layer.open({
				type : 2,
				title : false,
				closeBtn : false,
				content : ctx + "/fdHistorical/getFdById?type=show&id=" + clickRowId + "&suffix=" + "_his",// type用来区分页面来源，控制功能权限
				btn : [ "返回" ],
				success : function(layero, index) {
				}
				});

				layer.full(iframe);// 展开弹出层直接全屏显示
			} else {
				layer.msg('未匹配到航程信息', {
					icon : 2
				});
			}
		}
		});
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
		$("#printData").text(JSON.stringify(filter));
		//历史动态 schema = 3
		$("#schema").text("3");
		//排序列名
		$("#sortName").text(sortName);
		//排位命令（desc，asc）
	    $("#sortOrder").text(sortOrder);
		
		$("#printForm").submit();
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
			content : [ ctx + "/flightDynamic/delay?fltid=" + id + "&hisFlag=his", "no" ]
		})
	});
	
	//回车搜索
	var searchInput =$("#searchTab");    
		searchInput.bind('keydown', function(event) {
		if (event.keyCode == "13") {
			var text = searchInput.val();
			filter.dateB = $("#dateB").val();
			filter.dateE = $("#dateE").val();
			if(filter.dateB == null || filter.dateB ==''){
				layer.msg('请选择数据日期开始时间', {
					icon : 2
				});
				return;
			}
			if(filter.dateE == null || filter.dateE ==''){
				layer.msg('请选择数据日期结束时间', {
					icon : 2
				});
				return;
			}
			if (text) {
				clearSelectedRow();
				searchData = [];
				if (text) {
					text = text.toUpperCase();
					filter.searchKey = searchOption;
					filter.searchValue = text;
					tableOptions.queryParams = function(params) {
						return {
						pageNumber: (params.offset / params.limit) + 1,
						pageSize: params.limit,
						schema : "3",
						param : JSON.stringify(filter),
						suffix : "_HIS"
						}
					}
					$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
				} else {
					this.data = tableData;
					$("#total").text(tableData.length);
					searchData = [];
				}
			}else{
				$("#baseTable").bootstrapTable('refresh');
			}
		}
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
	// 报文
	$("#message").click(function() {
		var msg = layer.open({
			type : 2,
			title : "报文",
			//closeBtn : 0,
			area : [ $("body").width() - 200 + "px", $("body").height() - 200 + "px" ],
//			content : $("#msgDiv"),
			content : ctx + '/flightDynamic/telegraph?isHis=1',
			//btn : [ "确认" ],
			success : function(layero, index) {
//				setTimeout(function() {
//					$("#msgTable").bootstrapTable(messageTableOptions);
//				}, 100);
			}
			});
			layer.full(msg);
	});
	var messageTableOptions = {
	url : ctx + "/fdHistorical/getMessage", // 请求后台的URL（*）
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
})
// 表格列选项默认设置
jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
jQuery.fn.bootstrapTable.columnDefaults.align = "center";
var tableOptions = {
url : ctx + "/fdHistorical/getDynamic", // 请求后台的URL（*）
method : "get", // 请求方式（*）
dataType : "json", // 返回结果格式
striped : true, // 是否显示行间隔色
pagination : true, // 是否显示分页（*）
pageSize: 100,  
//可供选择的每页的行数（*）    
pageList: [20, 30, 50, 100],
sidePagination: "server",
sortable: true,                     //是否启用排序
sortOrder: "desc",                   //排序方式
pageNumber: 1,
paginationPreText: '上一页',//指定分页条中上一页按钮的图标或文字,这里是<  
paginationNextText: '下一页',//指定分页条中下一页按钮的图标或文字,这里是>  
cache : false, // 是否启用缓存
undefinedText : '', // undefined时显示文本
checkboxHeader : false, // 是否显示全选
toolbar : $("#tool-box"), // 指定工具栏dom
search : false, // 是否开启搜索功能
searchOnEnterKey : false,
columns : getBaseColumns(),
contextMenu : '#context-menu',
queryParams : function(params) {
	sortName= params.sort;
    sortOrder= params.order;
	var temp = {
	pageNumber: (params.offset / params.limit) + 1,
    pageSize: params.limit,
    param : JSON.stringify(filter),
	schema : "3",
	suffix : "_HIS",
	hisDate : $("#hisdate").val(),
	sortName: params.sort,      //排序列名  
    sortOrder: params.order     //排位命令（desc，asc）
	};
	return temp;
},
//右键菜单
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
	}  else if($el.data("item") == "fltDataInput") {
		fltDataInput(row);
	}  else if($el.data("item") == "exception"){
		openException(row);
	} else if($el.data("item") == "delay"){
		delayInfo(row);
	}else {
		openMessage(row, $el.data("item"));
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
				} else {
					return false;
				}
			} else {
				if (row.in_fltid) {
					clickRowId = row.in_fltid;
				} else {
					return false;
				}
			}
			
		} else {
			clickRowId = row.out_fltid;
		}
		if (clickRowId) {
			clickRow = row;
			$(tr).addClass("clickRow");
		}
	}
},
onDblClickRow : function(row,tr,field){
	if(field=="in_fltno" || field=="out_fltno" || field=="share_flt_no" || field=="in_depart_apt3code" || field=="out_arrival_apt3code" || field=="acttype_code"){
		//查看
		var inFltId = typeof(row.in_fltid)=="undefined"?"":row.in_fltid;
		var outFltId = typeof(row.out_fltid)=="undefined"?"":row.out_fltid;
		iframe = layer.open({
			type : 2,
			title : "历史动态航班信息",
			closeBtn : false,
			content : ctx + "/fdHistorical/toHisform?inFltId="+inFltId+"&outFltId="+outFltId,
			btn : ['返回']
		});
		layer.full(iframe);
	}
}
};

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
		content : [ ctx + "/flightDynamic/delay?fltid=" + id + "&hisFlag=his", "no" ]
	})
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

function getBaseColumns() {
	var columns = [];
	$.ajax({
	type : 'post',
	url : ctx + "/fdHistorical/getDefaultColumns",
	async : false,
	data : {
		'schema' : "3"
	},
	dataType : 'json',
	success : function(column) {
		var order = [ {
		field : 'order',
		title : '序号',
		sortable : false,
		editable : false,
		formatter : function(value, row, index) {
			return index + 1;
		}
		}, {
		field : "checkbox",
		checkbox : true,
		sortable : false,
		editable : false
		} ];
		//column= sortByKey(column,'title');
		columns = order.concat(column);
	}
	});
	return columns;
}

function sortByKey(array, key) {
    return array.sort(function(a, b) {
        var x = a[key].substring(0,1); 
        var y = b[key].substring(0,1);
        return ((x > y) ? -1 : ((x < y) ? 1 : 0));
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
	url : ctx + '/fdHistorical/getOptions',
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
//// 滑轮滚动加载
//function load() {
//	var start = page * limit;
//	if (start < tableData.length) {
//		var end = 0;
//		if (tableData.length > (page + 1) * limit) {
//			end = (page + 1) * limit;
//		} else {
//			end = tableData.length;
//		}
//		baseTable.bootstrapTable('append', tableData.slice(start, end));
//		var pos = baseTable.bootstrapTable('getScrollPosition');
//		baseTable.bootstrapTable('scrollTo', pos + 100);
//		page++;
//	}
//}


//指令
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
function clearSelectedRow() {
	clickRowId = "";
	clickRow = null;
	$(".clickRow").removeClass("clickRow");
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
	layer.open({
		type:2,
		title:"外航数据录入",
		content : [ctx + "/fdHistorical/fltDataInput?fltid="+fltid+"&ioType="+ioFlag,"no"],
		area:["500px","400px"],
		btn:["返回"],
		resize:false
	});
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

// 右键货邮行
function baggage(row) {
	iframe = layer.open({
	type : 2,
	title : "货邮行信息",
	area : [ '700px', '400px' ],
	content : ctx + "/flightDynamic/baggage?infltid=" + row.in_fltid+"&outfltid=" + row.out_fltid+ "&his='his'",
	});
	layer.full(iframe);
}


//右键时间动态
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


//右键资源状态
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

//右键消息变更轨迹
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

function saveSuccess() {
	layer.close(iframe);
}

function searchOpt(text,obj) {
	searchOption=text;
	var icon=$("#searchOptBtn").find("span").eq(0);
	$("#searchOptBtn").text($(obj).text());
	$("#searchOptBtn").append(icon);
}

