window.clickRowId = "";
var layer;// 初始化layer模块
var iframe;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	getOptions();
	var ganttOptions = {
		url:ctx+"/flightDynamic/ganttData",
		showEmptyRow:false,
		height:$("body").height()-$("#tool-box").height(),
		yData:{
			url:ctx+"/flightDynamic/ganttYData",
			responseHandler:function (res) {
				res.unshift({"level":"1","name":"取消","pid":"0","id":"QU"});
				res.unshift({"level":"1","name":"待排","pid":"0","id":"dp"});
				res.unshift({"level":"2","name":" ","pid":"dp","id":"DP"});
                return res;
            }
		},
		onclickOverlay:function(o){
			$.ajax({
				type:'post',
				url:ctx + "/flightDynamic/getGanttDetail",
				async:false,
				data:{
					id:o.id,
					inFltid:o.infltid,
					outFltid:o.outfltid
				},
				dataType:'json',
				success:function(detail){
					o.detail = detail;
				}
			});
		}
	}
	$("#SJgantt").SJgantt(ganttOptions);
	// 刷新
	if(!sessionStorage || !sessionStorage[$("#loginName").val()+"refreshGantt"] || sessionStorage[$("#loginName").val()+"refreshGantt"]=="undefined" || typeof(sessionStorage[$("#loginName").val()+"refreshGantt"]) == "undefined"){
		autoReload("0.5");
	}else{
		autoReload(sessionStorage[$("#loginName").val()+"refreshGantt"]);
	}
	$("#refresh").click(function() {
		clearInterval(intervalObj);
		$("#refresh").text("刷新");
		$("#SJgantt").SJgantt('load');
		if(sessionStoragesessionStorage[$("#loginName").val()+"refreshGantt"]){
			sessionStorage.setItem($("#loginName").val()+"refreshGantt",null);
		}
	})
	$("#refresh-c").click(function() {
		layer.open({
			title : '自定义刷新',
			content : '每<input type="number" min="1" class="layui-input layui-sm" style="width:120px;margin:0px 10px;display:inline-block"/>分钟自动刷新',
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
				if ($("#filterForm .flightScope").val()) {
					filter.flightScope = $("#filterForm .flightScope").val();
				}
				;
				if ($("#filterForm .apron").val()) {
					filter.apron = $("#filterForm .apron").val();
				}
				;
				if ($("#filterForm .terminal").val()) {
					filter.terminal = $("#filterForm .terminal").val();
				}
				;
				if ($("#filterForm .airline").val()) {
					filter.airline = $("#filterForm .airline").val();
				}
				;
				if ($("#filterForm .alnFlag").val()) {
					filter.alnFlag = $("#filterForm .alnFlag").val();
				}
				;
				if ($("#filterForm .acttypeSizes").val()) {
					filter.acttypeSizes = $("#filterForm .acttypeSizes").val();
				}
				;
				if ($("#filterForm .departAirport").val()) {
					filter.departAirport = $("#filterForm .departAirport").val();
				}
				;
				if ($("#filterForm .arriveAirport").val()) {
					filter.arriveAirport = $("#filterForm .arriveAirport").val();
				}
				;
				if ($("#filterForm .GAFlag").val()) {
					filter.GAFlag = $("#filterForm .GAFlag").val();
				}
				;
				if ($("#filterForm .identifying").val()) {
					filter.identifying = $("#filterForm .identifying").val();
				}
				;
				if ($("#filterForm .fltPropertys").val()) {
					filter.fltPropertys = $("#filterForm .fltPropertys").val();
				}
				;
				if ($("#filterForm .aircraft").val()) {
					filter.aircraft = $("#filterForm .aircraft").val();
				}
				;
				if ($("#filterForm .actStatus").val()) {
					filter.actStatus = $("#filterForm .actStatus").val();
				}
				;
				ganttOptions.queryParams = {
					param : JSON.stringify(filter)
				}
				$("#SJgantt").SJgantt('refreshOptions', ganttOptions);
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
				$("#filterForm").find("select").val('').removeAttr('selected');
				$("#filterForm .select2-selection__choice").remove();
				$("#filterForm .select2-search__field").attr("placeholder", "请选择");
				$("#filterForm .select2-search__field").css("width", "100%");

				$("#filterForm .select2-selection--single .select2-selection__rendered").empty();
				$("#filterForm .select2-selection--single .select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
				filter = {};
				layer.close(index);
			}
		});
		layer.full(filterIframe);
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
			type:'post',
			url:ctx+"/flightDynamic/ifGbakExist",
			data:{
				fltid:clickRowId
			},
			success:function(res){
				if(res=="exist"){
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
				}else{
					layer.msg('未匹配到航程信息', {
						icon : 2
					});
				}
			}
		});
	});
	// 新增
	$('#create').click(function() {
		iframe = layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			content : ctx + "/flightDynamic/form?type=create",
			btn : [ "保存", "重置", "返回" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
				return false;
			},
			btn2 : function(index, layero) {
				layer.getChildFrame("#createForm", index)[0].reset();
				var refBtn = layer.getChildFrame("#refreshDetailTable", index);
				refBtn.click();
				return false;
			}
		});
		layer.full(iframe);// 展开弹出层直接全屏显示
	});

	// 编辑
	$('#modify').click(function() {
		if (clickRowId == "") {
			layer.msg('请选择一个计划', {
				icon : 2
			});
			return false;
		}
		$.ajax({
			type:'post',
			url:ctx+"/flightDynamic/ifGbakExist",
			data:{
				fltid:clickRowId
			},
			success:function(res){
				if(res=="exist"){
					iframe = layer.open({
						type : 2,
						title : false,
						closeBtn : false,
						content : ctx + "/flightDynamic/getFdById?type=edit&id=" + clickRowId,
						btn : [ "保存", "重置", "返回" ],
						yes : function(index, layero) {
							var iframeWin = window[layero.find('iframe')[0]['name']];
							iframeWin.delFiles();// 编辑页面中删除依据正式记录了要删除的文件的id，并未执行删除，在这里与提交一起执行
							iframeWin.doSubmit();
							return false;
						},
						btn2 : function(index, layero) {
							layer.getChildFrame("#createForm", index)[0].reset();
							var refBtn = layer.getChildFrame("#refreshDetailTable", index);
							refBtn.click();
							return false;
						}
					});
					layer.full(iframe);
				}else{
					layer.msg('未匹配到航程信息', {
						icon : 2
					});
				}
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
	// 要客计划导入
	$('#importvip').click(function() {
		iframe = layer.open({
			type : 2,
			title : "要客计划导入",
			closeBtn : false,
			content : ctx + "/flightDynamic/openImportvip",
			btn : [ "确认" ],
			yes : function() {
				layer.close(iframe);
			}
		});
		layer.full(iframe);
	});
	// 指令
	$('#order').click(function() {
		if (clickRowId == "") {
			layer.msg('请选择一个计划', {
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

	// 回收站
	$("#recycle").click(function() {
		if (clickRowId == "") {
			layer.msg('请选择一个计划', {
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
		var msg = layer.open({
			type : 1,
			title : false,
			closeBtn : 0,
			content : $("#msgDiv"),
			btn : [ "确认" ],
			success : function(layero, index) {
				setTimeout(function() {
					$("#msgTable").bootstrapTable({
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
						height : $("body").height() - 56,
						detailFormatter : function(index, row) {
							return "<p style='margin-left:20px;'>" + row.original + "</p>";
						},
						columns : [ {
							field : 'order',
							title : '序号',
							formatter : function(value, row, index) {
								return index + 1;
							}
						}, {
							field : 'flightDate',
							title : '航班日期'
						}, {
							field : 'messageType',
							title : '报文类型'
						}, {
							field : 'flightNumber',
							title : '航班号'
						}, {
							field : 'departAirport',
							title : '起场'
						}, {
							field : 'arriveAirport',
							title : '落场'
						}, {
							field : 'messageTime',
							title : '报文时间'
						}, {
							field : 'receiveTime',
							title : '接收时间'
						}, {
							field : 'original',
							title : '报文原文',
							visible : false
						} ]
					});
				}, 100)
			}
		});
		layer.full(msg);
	});
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
		var inFltid = clickRow.infltid ? clickRow.infltid : '';
		var outFltid = clickRow.outfltid ? clickRow.outfltid : '';
		iframe = layer.open({
		type : 2,
		title : false,
		closeBtn : 1,
		content : ctx + "/fltmonitor?inFltid=" + inFltid + "&outFltid=" + outFltid,// type用来区分页面来源，控制功能权限
		// btn : [ "返回" ],
		});
		layer.full(iframe);// 展开弹出层直接全屏显示

	});
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
var refreshUnit = 60*1000;//刷新单位是分钟
function autoReload(interval){
	if(interval<0.5){
		layer.msg("最小刷新频率不要小于30秒！",{icon:7});
		return false;
	}
	sessionStorage.setItem($("#loginName").val()+"refreshGantt",interval);
	var time = Number(interval) * refreshUnit;
	if (intervalObj) {
		clearInterval(intervalObj);
	}
	var btnText = "<i class='fa fa-clock-o' style='display:inline-block;width:15px'>&nbsp;</i>每" + interval + "分钟";
	$("#refresh").html(btnText);
	intervalObj = setInterval(function() {
		$("#SJgantt").SJgantt('load');
	}, time);
}