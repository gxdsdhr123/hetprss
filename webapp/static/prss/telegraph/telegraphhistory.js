var tableOptions;
$(function() {
	var layer;
	var clickRowId = "";

	layui.use([ 'form', 'layer' ], function() {
		layer = layui.layer;
	});


	initSelect2("airplane","请选择航空公司，支持多选");//航空公司
	initSelect2("telegraphType","请选择报文类型，支持多选");//报文类型
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {
			toggle : 'dblclick'
	};
	tableOptions = {
			url : ctx + "/telegraph/history/historyList", // 请求后台的URL（*）
			method : "get", // 请求方式（*）
			dataType : "json",
			striped : true, // 是否显示行间隔色
			cache : false,
			undefinedText:'',
			uniqueId : "id",
			checkboxHeader : false,
			toolbar : $("#tool-box"),
			search : false,
			pagination : true,
			sidePagination : 'server',
			pageNumber : 1,
			sortName : ($("input[name=isHis]").val()==2?'ACCEPT_TIME':'SEND_TIME'),
			sortOrder : "desc",
			pageSize : 10,
			pageList : [ 5, 10, 20 ],
			queryParamsType : '',
			queryParams : function(params) {
				var telegraphType = $("select[name='telegraphType']").val();
				if(telegraphType!=null)
					telegraphType = telegraphType.join(",");
				var airplane = $("select[name='airplane']").val();
				if(airplane!=null)
					airplane = airplane.join(",");
				var param = {
						mflightdate : $("input[name='mflightdate']").val(),
						flightnumber : $("input[name='flightnumber']").val(),
						beginTime : $("input[name='beginTime']").val(),
						endTime : $("input[name='endTime']").val(),
						isHis : $("input[name='isHis']").val(),
						unioncode : $("select[name='unioncode']").val(),
						sendorrecieve : $("select[name='sendorrecieve']").val(),
						airplane : airplane,
						status : $("select[name='status']").val(),
						isfavoriter : $("select[name='isfavoriter']").val(),
						fltId : $("#fltId").val(),
						telegraphType : telegraphType,
						pageNumber : params.pageNumber,
						pageSize : params.pageSize,
						sortName : params.sortName,
						sortOrder : params.sortOrder
				};

				return param;
			},
			columns : getColumn(),
			onDblClickRow : onDblClickRow
	};
	function getColumn(){
		var column = [];
		var isHis = $("input[name=isHis]").val();
		column.push({field : "R",title : "序号",sortable : false,editable : false});
		column.push({field : "",title : "<input type='checkbox' name='all'>",sortable : false,editable : false,checkbox:true});
		/*
		 * ,formatter:function(value,row,index){ return index+1; }
		 */
		if(isHis==2){//收件
			column.push({field : "SEND_ADDRESS",title : "发报地址",editable : false,
				formatter:function(value,row,index){
					if(row.SEND_ADDRESS==null || row.SEND_ADDRESS == ''){
						return  '未知';
					} else {
						return row.SEND_ADDRESS;
					}
				}
			});
			column.push({field : "FLIGHT_NUMBER",title : "航班号",editable : false,
				formatter:function(value,row,index){
					var className = '';
					if(row.ISREAD==0)
						className = 'col-state-1';
					return '<span class=\''+className+'\' title=\''+row.FLIGHT_NUMBER+'\'>' + row.FLIGHT_NUMBER + '</span>';
				}
			});
			column.push({field : "TG_CODE",title : "报文类型",editable : false});
			column.push({field : "ACCEPT_TIME",title : "收发时间",editable : false});
			column.push({field : "PRIORITY",title : "优先级",editable : false,
				formatter:function(value,row,index){
					if(row.PRIORITY==null || row.PRIORITY == ''){
						return  '未知';
					} else {
						return row.PRIORITY;
					}
				}
			});
			column.push({field : "ISREAD",title : "状态",editable : false,
				formatter:function(value,row,index){
					var status = '';
					if(row.ISREAD == 1){
						status = '已读';
					} else if(row.ISREAD == 0){
						status = '未读';
					} 
					return status;
				}
			});
			column.push({field : "ISPRINT",title : "是否打印",editable : false,
				formatter:function(value,row,index){
					var print = '';
					if(row.ISPRINT == 1){
						print = '是';
					} else if(row.ISPRINT == 0){
						print = '否';
					} 
					return print;
				}
			});
		} else if(isHis==3){
			column.push({field : "FLIGHT_NUMBER",title : "航班号",editable : false});
			column.push({field : "ADDRESS",title : "目的地址",editable : false});
			column.push({field : "TG_CODE",title : "报文类型",editable : false});
			column.push({field : "SEND_TIME",title : "报文发送时间",editable : false});
			column.push({field : "PRIORITY",title : "优先级",editable : false});
			column.push({field : "NAME",title : "操作者",editable : false});
		} else if(isHis ==1  || isHis ==4){
			column.push({field : "TEXT",visible : false,title : "TEXT"});
			column.push({field : "TYPE",title : "已收/已发",editable : false});
			column.push({field : "FLIGHT_DATE",title : "航班日期",editable : false});
			column.push({field : "FLIGHT_NUMBER",title : "航班号",editable : false,
				formatter:function(value,row,index){
					var className = '';
					if(row.ISREAD==0)
						className = 'col-state-1';
					return '<span class=\''+className+'\' title=\''+row.FLIGHT_NUMBER+'\'>' + row.FLIGHT_NUMBER + '</span>';
				}
			});
			column.push({field : "TG_CODE",title : "报文类型",editable : false});
			column.push({field : "ACCEPT_TIME",title : "收发时间",editable : false,
				formatter:function(value,row,index){
					var time = '';
					if(row.TYPE=='已收')
						time = row.ACCEPT_TIME;
					else if(row.TYPE=='已发')
						time = row.SEND_TIME;
					return time;
				}	
			});
			column.push({field : "PRIORITY",title : "优先级",editable : false,
				formatter:function(value,row,index){
					if(row.PRIORITY==null || row.PRIORITY == ''){
						return  '未知';
					} else {
						return row.PRIORITY;
					}
				}
			});
			column.push({field : "ISREAD",title : "状态",editable : false,
				formatter:function(value,row,index){
					var status = '';
					if(row.ISREAD == 1){
						status = '已读';
					} else {
						status = '未读';
					} 
					return status;
				}
			});
			column.push({field : "ISPRINT",title : "是否打印",editable : false,
				formatter:function(value,row,index){
					var print = '';
					if(row.ISPRINT == 1){
						print = '是';
					} else {
						print = '否';
					}
					return print;
				}
			});
		}
		column.push({field: "mj", title: "操作",editable:false,
			formatter:function(value,row,index){
				var isFavorite = row.FAVORITE;
				var html = '';
				if(isHis ==4 && row.SENDORRECIEVE == 1){
					html += "<i class='fa fa-fw fa-plane' title='报文归档' onclick='isPlane("+row.ID+")'></i>&nbsp;&nbsp;"
				}
				if(isHis ==1 || isHis == 2){
//					if((isHis ==1 || isHis == 4) && (row.FLTID == null || row.FLTID =='')){
					html += "<i class='fa fa-fw fa-plane' title='报文归档' onclick='isPlane("+row.ID+")'></i>&nbsp;&nbsp;"
				}
				if(isHis == 2 && row.ISPRINT == 0){
					html += "<i class='fa fa-fw fa-print' title='报文打印' onclick='printDate("+row.ID+")'></i>&nbsp;&nbsp;"
				}
				if(isHis ==2 || isHis == 3){
					html += "<i class='fa fa-fw fa-reply' title='报文转发' onclick='send("+row.ID+")'></i>&nbsp;&nbsp;"
				}
				if(isFavorite==0){
					html += "<i class='glyphicon glyphicon-heart-empty' title='收藏' onclick='dofavorite(1,"+row.ID+","+row.SENDORRECIEVE+")'></i>";
				} else {
					html += "<i class='glyphicon glyphicon-heart'title='取消收藏'  onclick='dofavorite(2,"+row.ID+","+row.SENDORRECIEVE+")'></i>";
				}
				return html;
			}
		});
		return column;
	}
	function onDblClickRow(row, tr, field) {
		if(row.ISREAD==0){
			$.ajax({
				type : 'post',
				url : ctx + "/telegraph/history/isread",
				data : {
					type : row.SENDORRECIEVE,
					id : row.ID,
					isHis : $("#isHis").val()
				},
				async : true,
				dataType : 'json',
				success : function(data) {
				},error : function(msg){
					layer.msg("操作失败",{time:1000,icon:2});
				}
			});
		}
		searchHisDetail(row)

	}
//	tableOptions.height = $("#baseTables").height();
	$("#baseTable").bootstrapTable(tableOptions);

	// 查询功能
	$(".search").click(function() {
		refresh();
	});
	
	// 打印功能
	$(".print").click(function() {
		var ids = $.map($('#baseTable').bootstrapTable('getSelections'), function(row) {
			return row.ID;
		});
		if(ids == null || ids == ''){
			layer.msg("请选择导出数据",{time:1000,icon:0});
			return ;
		}
		printDate(ids.join(','))
	});
	
	// 导出功能
	$(".export").click(function() {
		var ids = $.map($('#baseTable').bootstrapTable('getSelections'), function(row) {
			return row.ID;
		});
		if(ids == null || ids == ''){
			layer.msg("请选择导出数据",{time:1000,icon:0});
			return ;
		}
			
		$("input[name=id]").val(ids);
		$("#exportForm").submit();
	})

	var mtext = $("#mtext");
	mtext.css("position", "relative");
	new PerfectScrollbar(mtext[0]);
	
	$(".send").click(function(){
		send('');
	});
	
	$("input[name=all]").click(function(){
		if(this.checked){
			$("input:checkbox").prop("checked","checked");
			$("tbody tr").addClass("selected");
		} else {
			 $("input:checkbox").removeAttr("checked");
			 $("tbody tr").removeClass("selected");
		}
	})

});

function send(id){
	var open = layer.open({
		type : 2,
		title : '发送报文',
		resize : false,
		maxmin : false,
		shadeClose : true,
		area : [ "590px", "400px" ],
		btn : [ "发送","返回" ],
		content : ctx +'/telegraph/history/tosend?id='+id+'&isHis='+$("#isHis").val(),
		yes:function(index,layero){
			var result = $(layero).find("iframe")[0].contentWindow.send();
		}
	});
	layer.full(open);
}
function isPlane(id){
	var open = layer.open({
		type : 2,
		title : '报文归档',
		resize : false,
		maxmin : false,
		shadeClose : true,
		area : [ "590px", "400px" ],
		content : ctx +'/telegraph/history/toIsPlane?id='+id+'&isHis='+$("input[name='isHis']").val(),
		success: function(layero, index){
		}
	});
	layer.full(open);
}
function printDate(ids){
	var telegraphType = $("select[name='telegraphType']").val();
	if(telegraphType!=null)
		telegraphType = telegraphType.join(",");
	var airplane = $("select[name='airplane']").val();
	if(airplane!=null)
		airplane = airplane.join(",");
	$.ajax({
		type : 'post',
		url : ctx + "/telegraph/history/printData",
		data : {
			mflightdate : $("input[name='mflightdate']").val(),
			flightnumber : $("input[name='flightnumber']").val(),
			beginTime : $("input[name='beginTime']").val(),
			endTime : $("input[name='endTime']").val(),
			isHis : $("input[name='isHis']").val(),
			unioncode : $("select[name='unioncode']").val(),
			sendorrecieve : $("select[name='sendorrecieve']").val(),
			airplane : airplane,
			status : $("select[name='status']").val(),
			isfavoriter : $("select[name='isfavoriter']").val(),
			telegraphType : telegraphType,
			isHis : $("input[name=isHis]").val(),
			ids : ids
		},
		async : true,
		dataType : 'json',
		success : function(data) {
			refresh();
		},error : function(msg){
			layer.msg("操作失败",{time:1000,icon:2});
		}
	});
}

function refresh(){
	tableOptions.queryParams.pageNumber = 1;
	$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
}
function dofavorite(type,id,sendorrecieve){
	$.ajax({
		type : 'post',
		url : ctx + "/telegraph/history/dofavorite",
		data : {
			type : type,
			sendorrecieve : sendorrecieve,
			id : id
		},
		async : true,
		dataType : 'json',
		success : function(data) {
			refresh();
		},error : function(msg){
			layer.msg("操作失败",{time:1000,icon:2});
		}
	});
}
/**
 * 初始化select下拉
 * @param selectId
 * @param tip
 */
function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,  
        width:"400px",
        language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
    }); 
}

function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}

// 查看按钮,查看详情
function searchHisDetail(obj) {
	var open = layer.open({
	type : 1,
	title : '详情',
	resize : false,
	maxmin : false,
	shadeClose : true,
	area:[$("body").width()+"px",$("body").height()+"px"],
	content : $('#detail'),
	success: function(layero, index){
		$("input[name='fd']").val(obj.FLIGHT_DATE);
		$("input[name='fn']").val(obj.FLIGHT_NUMBER);
		$("input[name='tt']").val(obj.TG_CODE);
		$("input[name='st']").val(obj.SEND_TIME);
		$("input[name='at']").val(obj.ACCEPT_TIME);
		$("#mtext").text(obj.TEXT);
	}
	});
	refresh();
	layer.full(open);
}