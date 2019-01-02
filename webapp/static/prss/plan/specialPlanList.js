var clickRowId = "";// 当前选中行
layui.use(["form","layer"]);
$(document).ready(function(){
	//数据表格,默认查未执行状态计划
	var option = tableOption();
	$("#baseTable").bootstrapTable(option);
	
	//新增
	$('#addBtn').click(function() {
		var addIframe = layer.open({
			type : 2,
			title : "公务机/通航计划新增",
			closeBtn : 0,
			content : ctx + "/plan/specialPlan/form?operateType=add",
			btn : ['保存','新增行','删除行','关闭'],
			yes : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.save();
				$("#baseTable").bootstrapTable("refresh");
			},
			btn2 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.addRow();
				return false;
			},
			btn3 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.deleteRow();
				return false;
			},
			btn4 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.delTmpAttachmentFile("allRow");
				return false;
			},
			cancel: function(){
				$(layero).find("iframe")[0].contentWindow.delTmpAttachmentFile("allRow");
				return false;
            }
		});
		layer.full(addIframe);
	});
	
	//回收站
	$('#recycleBtn').click(function() {
		var addIframe = layer.open({
			type : 2,
			title : "公务机/通航计划回收站",
			content : ctx + "/plan/specialPlan/recyclePage",
			btn : ['关闭'],
			end : function(index, layero) {
				$("#baseTable").bootstrapTable("refresh");
			},
			cancel: function(){ 
				$("#baseTable").bootstrapTable("refresh");
            }
		});
		layer.full(addIframe);
	});
	
	//刷新
	$("#refreshBtn").click(function(){
		$("#baseTable").bootstrapTable("refresh");
	});
	
	//删除
	$('#delBtn').click(function() {
		if ($("#baseTable").bootstrapTable("getSelections").length == 0) {
			layer.msg('请选择要移动到回收站的计划', {
				icon : 2
			});
		} else {
			layer.confirm("是否将选中的计划移动到回收站？", {
				offset : '100px'
			}, function(index) {
				var row = $('#baseTable').bootstrapTable('getSelections');
				var ids = "";
				for (var i = 0; i < row.length; i++) {
					if(i!=row.length-1){
						ids += row[i].id+",";
					}else{
						ids += row[i].id;
					}
				}
				var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
				$.ajax({
					type : 'post',
					url : ctx + "/plan/specialPlan/delete",
					data : {
						'ids' : ids
					},
					success : function(data) {
						layer.close(loading);
						if(data=="success"){
							layer.msg('删除成功！', {icon : 1,time : 2000});
							$("#baseTable").bootstrapTable("refresh");
						}else if(data=="fail"){
							layer.alert("删除失败或为已执行历史数据不可删除！", {icon: 2,time : 2000});
						}else{
							layer.alert("删除异常！", {icon: 2,time : 2000});
						}
					}
				});
				layer.close(index);
			});
		}
	});
});

function tableOption(){
	var planStatus = $("#planStatusSel").val();
	var option = {
		url : ctx+"/plan/specialPlan/gridData", 
		method : "get", 
		dataType : "json",
		toolbar : $("#tool-box"),
		cache : false,
		search : false,
		editable: false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		paginationPreText: "上一页",  
		paginationNextText: "下一页",
//		height:$("#baseTables").height(),
		height:$(window).height(),
		contextMenu: '#context-menu',
		onContextMenuItem: function(row, $el){
			if('' == clickRowId){
				layer.msg('请选择一条记录',{icon:0,time:600});
				return;
			}
			if ($el.data("item") == "msgView") {
				msgShow(row);
			}
		},
		onClickRow : function(row, tr, field) {
			clickRowId = row.msgId;
		},
		onDblClickCell : function(field, value, row, td) {
			if(field != "attachmentId"){
				var ids = row.id;
				var addIframe = layer.open({
					type : 2,
					title : "公务机/通航计划修改",
					closeBtn : 0,
					content : ctx + "/plan/specialPlan/form?ids="+ids+"&operateType=update",
					btn : ['保存','关闭'],
					yes : function(index, layero) {
						$(layero).find("iframe")[0].contentWindow.save();
						$("#baseTable").bootstrapTable("refresh");
					},
					btn2 : function(index, layero) {
						$(layero).find("iframe")[0].contentWindow.delTmpAttachmentFile("allRow");
						return false;
					}
				});
				layer.full(addIframe);
			}
		},
		queryParams : function(params) {
			var param = {
				pageNumber: params.offset+1,
		        pageSize: params.limit,
		        planStatus : planStatus
			};
			return param;
		},
		columns : [ {
			checkbox : true, // 显示一个勾选框
			align : 'center'
		}, {
			field : "rowId",
			title : "序号",
			width : '40px',
			align : 'center',
			formatter(value, row, index, field){
				return index+1;
			},
		}, {
			field : "fltDate",
			align : 'center',
			title : "执行日期"
		}, {
			field : "fltNo",
			align : 'center',
			title : "航班号"
		}, {
			field : "aircraftNumber",
			align : 'center',
			title : "机号"
		}, {
			field : "actType",
			align : 'center',
			title : "机型"
		}, {
			field : "departAptName",
			align : 'center',
			title : "起场"
		}, {
			field : "arrivalAptName",
			align : 'center',
			title : "落场"
		}, {
			field : "std",
			align : 'center',
			title : "计起"
		}, {
			field : "sta",
			align : 'center',
			title : "计落"
		}, {
			field : "otherAptName",
			align : 'center',
			title : "联程机场"
		}, {
			field : "otherStd",
			align : 'center',
			title : "联程计起"
		}, {
			field : "otherSta",
			align : 'center',
			title : "联程计落"
		}, {
			field : "propertyName",
			align : 'center',
			title : "性质"
		}, {
			field : "alnName",
			align : 'center',
			title : "航空公司"
		}, {
			field : "attachmentId",
			align : 'center',
			title : "附件",
			formatter : function(value, row, index) {
				if(value==''){
					return '<button type="button" style="height:auto;line-height:1.5" class="btn btn-md btn-link" disabled="disabled">下载</button>';
				}else{
					return '<button type="button" style="height:auto;line-height:1.5" value="'+row.attachmentId+','+row.attachmentName+'" class="btn btn-md btn-link" onclick="downFile(this)">下载</button>';
				}
			}	
		}, {
			field : "operatorDate",
			align : 'center',
			title : "操作日期"
		}, {
			field : "operatorUserName",
			align : 'center',
			title : "操作人"
		}]
	}
	return option;
}

function refreshTable(){
	$("#baseTable").bootstrapTable('refresh');
}

function showMsg(row) {
	iframe = layer.open({
		type : 2,
		title : "公务/通航计划报文",
		content : ctx + "/plan/specialPlan/showMsg?msgId=" + row.msgId,
		area : [ '700px', '400px' ],
		btn : ["取消"],
		btn1 : function(index, layero) {
			layer.close(index);
		}
	});
}

/**
 * 下载附件
 */
function downFile(obj){
	var downFileInfo = obj.value;
	if(downFileInfo!='undefined'&&downFileInfo!=null&&downFileInfo!=''){
		$("#downFileInfo").val(downFileInfo);
		$("#downFileForm").submit();
	}
}

/**
 * 查询列表类型
 */
function changeSel(){
	$("#baseTable").html("");
	var option = tableOption();
	$("#baseTable").bootstrapTable('refreshOptions',option);
}

/**
 * 根据文件ids删除文件fileId1,fileId2...
 */
function delFile(tmpAttachmentIds){
	//判断是否有修改的数据
	if(tmpAttachmentIds==""){
		return false;
	}
	$.ajax({
		type : 'post',
		url : ctx + "/plan/specialPlan/delTmpAttachmentFile",
		async : true,
		dataType : "text",
		data : {
			tmpAttachmentIds : tmpAttachmentIds
		},
		success : function(result) {
		}
	});
}