$(function() {
	var layer;
	var clickRowId = "";

	layui.use([ 'form', 'layer' ], function() {
		layer = layui.layer;
	});

	// 已发未发
	$('#overSend').click(function() {
		var num = "1";
		parent.layer.open({
		type : 2,
		title : '已发',
		maxmin : false,
		resize : false,
		shadeClose : false,
		area : [ "850px", "450px" ],
		content : ctx + "/message/history/test",
		btn : [ "返回" ]

		});

	});

	var num = $("#num").val();
	var fltid = $("#fltid").val();
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {
		toggle : 'dblclick'
	};
	var tableOptions = {
	url : ctx + "/message/history/historyList?num=" + num + "&fltid=" + fltid, // 请求后台的URL（*）
	method : "get", // 请求方式（*）
	dataType : "json",
	striped : true, // 是否显示行间隔色
	cache : false,
	// undefinedText:'',
	uniqueId : "id",
	checkboxHeader : false,
	toolbar : $("#tool-box"),
	search : false,
	pagination : true,
	sidePagination : 'server',
	pageNumber : 1,
	sortName : "SENDTIME",
	sortOrder : "desc",
	pageSize : 10,
	pageList : [ 5, 10, 20 ],
	queryParamsType : '',
	queryParams : function(params) {
		var param = {
		mflightdate : $("input[name='mflightdate']").val(),
		flightnumber : $("input[name='flightnumber']").val(),
		mtitle : encodeURIComponent($("input[name='mtitle']").val()),
		mtext : encodeURIComponent($("input[name='mtext']").val()),
		pageNumber : params.pageNumber,
		pageSize : params.pageSize,
		sortName : params.sortName,
		sortOrder : params.sortOrder
		};

		return param;
	},
	columns : [ {
	field : "R",
	title : "序号",
	sortable : false,
	editable : false
	/*
	 * ,formatter:function(value,row,index){ return index+1; }
	 */
	},
	// {field: "checkbox",checkbox:true,sortable:false,editable:false},
	{
	field : "ID",
	visible : false,
	title : "ID"
	}, {
	field : "TYPE",
	title : "已收/已发",
	editable : false
	}, {
	field : "MFLIGHTDATE",
	title : "航班日期",
	editable : false
	}, {
	field : "FLIGHTNUMBER",
	title : "航班号",
	editable : false
	}, {
	field : "MTITLE",
	title : "标题",
	editable : false
	}, {
	field : "MTEXT",
	title : "正文",
	editable : false
	}, {
	field : "SENDERCN",
	title : "发送人",
	editable : false
	}, {
	field : "SENDTIME",
	title : "时间",
	editable : false
	}, {
	field : "MJ",
	title : "操作",
	sortable : false,
	editable : false,
	formatter : function(value, row, index) {
		var isFavorite = row.ISFAVORITE;
		var mmType = row.MMTYPE;
		var tempName = row.TEMPNAME;
		
		var html = "<i class='fa fa-download'  title='下载附件' onclick='download(" + row.ID + ")'>" +
		"</i>&nbsp;&nbsp;&nbsp;<i class='fa fa-search'  title='详情查看' onclick='searchHisDetail(" + row.ID + ")'>" +
		"</i>&nbsp;&nbsp;&nbsp;<i class='fa fa-eye' title='流转图查看' onclick='messageFlow(" + row.ID + ")'></i>"
		if(mmType == 'send' || mmType == 'receive'){
			if(isFavorite==1){
				html +=	"&nbsp;&nbsp;&nbsp;<i class='glyphicon glyphicon-heart' title='取消收藏' onclick='unfavorite(" + row.ID + "," + row.FAVORITETYPE + "," + isFavorite + ")'></i>"
			} else {
				html +=	"&nbsp;&nbsp;&nbsp;<i class='glyphicon glyphicon-heart-empty' title='收藏' onclick='unfavorite(" + row.ID + "," + row.FAVORITETYPE + "," + isFavorite + ")'></i>"
			}
		} else if(mmType == 'favorite'){
			html +=	"&nbsp;&nbsp;&nbsp;<i class='glyphicon glyphicon-heart' title='取消收藏' onclick='unfavorite(" + row.ID + "," + row.FAVORITETYPE + "," + isFavorite + ")'></i>"
		} else {
			
		}
		if(tempName =='telegraphSendAuto'){
			html +=	"&nbsp;&nbsp;&nbsp;<i class='fa fa-send' title='发送报文' onclick='sendTelegraph(" + row.ID + ")'>&nbsp;</i>";
		}
		return html;
	}
	} ],
	onDblClickRow : onDblClickRow
	};

	function onDblClickRow(row, tr, field) {
		var id = row.ID;
		searchHisDetail(id)

	}
	$("#baseTable").bootstrapTable(tableOptions);

	// 查询功能
	$(".search").click(function() {
		tableOptions.queryParams.pageNumber = 1;
		$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
	})
	// 打印
	$("#print").click(function() {
		var mflightdate = $("input[name='mflightdate']").val();
		var flightnumber = $("input[name='flightnumber']").val();
		var mtitle = $("input[name='mtitle']").val();
		var mtext = $("input[name='mtext']").val();
		var num = $("#num").val();

		var columns = $.map($('#baseTable').bootstrapTable('getOptions').columns[0], function(col) {
			if (col.field != "id" && col.field != "checkbox" && col.field != "mj") {
				return {
				"field" : col.field,
				"title" : col.title
				};
			} else {
				return null;
			}
		});
		$("#printTitle").text(JSON.stringify(columns));
		$("#printmflightdate").text(mflightdate);
		$("#printflightnumber").text(flightnumber);
		$("#printmtitle").text(mtitle);
		$("#printmtext").text(mtext);
		$("#printnum").text(num);
		$("#printForm").submit();
	});

});

function sendTelegraph(fltid){
	console.info(fltid);
	layer.open({
		type : 2,
		title : '报文发送',
		closeBtn : false,
		content : ctx + "/telegraph/auto/sendMessageList?fltid=" + fltid,
		btn : [ "发送", "取消" ],
		shadeClose : false,
		area:[window.parent.$("body").width()+"px","550px"],
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
// 查看按钮,查看指令详情
function searchHisDetail(id) {
	var num = $("#num").val();
	layer.open({
	type : 2,
	title : '指令详情',
	resize : false,
	maxmin : false,
	// shadeClose : false,
	area : [ "590px", "400px" ],
	content : ctx + "/message/history/searchHisDetail?id=" + id + "&num=" + num

	});

}
// 消息流转按钮 消息流转页面
function messageFlow(id) {
	var num = $("#num").val();
	var messageFlow = layer.open({
	type : 2,
	title : '消息流转',
	maxmin : false,
	resize : false,
	shadeClose : true,
	area : [ "500px", "400px" ],
	content : ctx + "/message/history/messageFlow?id=" + id + "&num=" + num

	});
	layer.full(messageFlow);
}
// 打开数据对应的附件列表
function download(i) {

	layer.open({
	type : 1,
	title : "附件列表",
	offset : '100px',
	area : [ "600px", "300px" ],
	content : $("#fileList"),
	yes : function() {
		$("#fileInput").click();
	},
	success : function() {
		// 获取附件列表信息
		var num = $("#num").val();
		$.ajax({
		type : 'post',
		url : ctx + "/message/history/getFileIds",
		data : {
			'id' : i,
			'num' : num
		},
		dataType : 'json',
		success : function(fileIds) {
			$("#fileTable").find("tr").remove();
			for (var i = 0; i < fileIds.length; i++) {

				var tr = $([ '<tr>', '<td>' + fileIds[i]["NAME"] + '</td>', '</tr>' ].join(''));
				var option = $([ '<td>', '<button type="button" data-id="' + fileIds[i]["ID"] + '" data-name="' + fileIds[i]["NAME"] + '" class="layui-btn layui-btn-primary file-download" onclick="downloadFile(this)">下载</button>', '</td>' ].join(''));
				tr.append(option);
				$("#fileTable").append(tr);
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
	var $form = $('<form action="' + url + '" method="post" style="display:none"></form>');
	var hidden = '<input type="hidden" name="id" value="' + id + '"/>' + '<input type="hidden" name="fileName" value="' + fileName + '"/>';
	$form.append(hidden);
	$('body').append($form);
	$form.submit();
}

function unfavorite(id,type,isFavorite){
	isFavorite = isFavorite==1?0:1;
	$.post(ctx+"/message/common/favorite",{mid : id,type : type ,isFavorite : isFavorite}, function(msg) {
		if(msg == "false"){
			layer.alert("操作失败，请联系管理员！",{icon:0,time: 1000});
		} else {
			$("#baseTable").bootstrapTable('refresh');
		}
	});
}
