window.clickRowId = "";
window.clickRow;
var layer;// 初始化layer模块
var iframe;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	var d = new Date(new Date().getTime()-48*60*60*1000);
	var s = d.getFullYear()+"-"+addzero(d.getMonth() + 1)+"-"+addzero(d.getDate());
	$("#hisdate").val(s);
	getOptions();
	var ganttOptions = {
		url:ctx+"/schedulingHis/gantt/ganttData",
		queryParams:{
            hisDate:$("#hisdate").val()+" 08:00:00"
        },
		showEmptyRow:true,
		height:$("body").height()-$("#tool-box").height()+5,
		yData:{
			url:ctx+"/schedulingHis/gantt/ganttYData",
			queryParams:{
                hisDate:$("#hisdate").val()+" 08:00:00"
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
				url:ctx + "/schedulingHis/gantt/getGanttDetail",
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
	$("#SJgantt").SJgantt(ganttOptions);
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
			area : [ '900px', '500px' ],
			content : ctx + "/flightDynamic/error"
		});
	});
	// 作业管理
	$("#jobManageBtn").click(function() {
		jobManage();
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
					content : ctx + "/fltmonitor/his?inFltid="+inFltid+"&outFltid="+outFltid,// type用来区分页面来源，控制功能权限
					//btn : [ "返回" ],
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
function dateFilter(){
	WdatePicker({
		maxDate:'%y-%M-{%d-2}',
		dateFmt : 'yyyy-MM-dd',
		onpicking : function(dp) {
			$("#hisdate").val(dp.cal.getNewDateStr());
			var options = {
					queryParams:{
			            hisDate:$("#hisdate").val()+" 08:00:00"
			        },
			        yData:{
			        	queryParams:{
				            hisDate:$("#hisdate").val()+" 08:00:00"
				        }
			        }
			}
			$("#SJgantt").SJgantt('refresh',options);
		}
	})
}
function addzero(v) {
	if (v < 10)
		return '0' + v;
	return v.toString();
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