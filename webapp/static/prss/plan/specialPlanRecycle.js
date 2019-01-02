var clickRowId = "";// 当前选中行
layui.use(["form","layer"]);
$(document).ready(function(){
	initGrid('2');
	
	//刷新
	$("#refreshBtn").click(function(){
		$("#baseTable").bootstrapTable("refresh");
	});
	
	//彻底删除
	$('#removeBtn').click(function() {
		if ($("#baseTable").bootstrapTable("getSelections").length == 0) {
			layer.msg('请选择要删除的计划', {
				icon : 2
			});
		} else {
			layer.confirm("是否将选中的计划彻底删除？", {
				offset : '100px'
			}, function(index) {
				var row = $('#baseTable').bootstrapTable('getSelections');
				var ids = "";
				var attachmentIds = "";
				for (var i = 0; i < row.length; i++) {
					if(i!=row.length-1){
						ids += row[i].id+",";
						attachmentIds += row[i].attachmentId+",";
					}else{
						ids += row[i].id;
						attachmentIds += row[i].attachmentId;
					}
				}
				var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
				$.ajax({
					type : 'post',
					url : ctx + "/plan/specialPlan/operate",
					data : {
						'ids' : ids,
						'attachmentIds': attachmentIds,
						'operateType' : 'remove'
					},
					success : function(data) {
						layer.close(loading);
						if(data=="success"){
							layer.msg('删除成功！', {icon : 1,time : 2000});
							$("#baseTable").bootstrapTable("refresh");
						}else if(data=="fail"){
							layer.alert("删除失败！", {icon: 2,time : 2000});
						}else{
							layer.alert("删除异常！", {icon: 2,time : 2000});
						}
					}
				});
				layer.close(index);
			});
		}
	});
	
	//恢复
	$('#recoveryBtn').click(function() {
		if ($("#baseTable").bootstrapTable("getSelections").length == 0) {
			layer.msg('请选择要恢复的计划', {
				icon : 2
			});
		} else {
			layer.confirm("是否将选中的计划恢复？", {
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
					url : ctx + "/plan/specialPlan/operate",
					data : {
						'ids' : ids,
						'operateType' : 'recovery'
					},
					success : function(data) {
						layer.close(loading);
						if(data=="success"){
							layer.msg('恢复成功！', {icon : 1,time : 2000});
							$("#baseTable").bootstrapTable("refresh");
						}else if(data=="fail"){
							layer.alert("恢复失败！", {icon: 2,time : 2000});
						}else{
							layer.alert("恢复异常！", {icon: 2,time : 2000});
						}
					}
				});
				layer.close(index);
			});
		}
	});
});

function initGrid(planStatus) {
	$("#baseTable").bootstrapTable({
		url : ctx+"/plan/specialPlan/gridData", 
		method : "get", 
		dataType : "json",
		toolbar : $("#tool-box"),
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
			title : "附件文件id",
			editable : false,
			visible : false
		},{
			field : "file",
			align : 'center',
			title : "附件",
			formatter : function(value, row, index) {
				var attachmentId = row.attachmentId;
				if(attachmentId==''){
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
	});
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
