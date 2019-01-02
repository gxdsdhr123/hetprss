var mainKindId;
var currKindName;
var currKindCode;
var currTypeName;
var currTypeId;
var mainTypeId;
var iframe;
var baseTable;
var clickRowId;
// 表格列选项默认设置
jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
jQuery.fn.bootstrapTable.columnDefaults.align = "center";

var tableOptions = {
	striped : true, // 是否显示行间隔色
	pagination : false, // 是否显示分页（*）
	cache : false, // 是否启用缓存
	undefinedText : '', // undefined时显示文本
	checkboxHeader : false, // 是否显示全选
	toolbar : $("#tool-box"), // 指定工具栏dom
	search : false, // 是否开启搜索功能
	columns : [ {
		field : 'ID',
		title : 'ID',
		visible : false
	}, {
		field : 'order',
		title : '序号',
		align : "center",
		formatter : function(value, row, index) {
			return index + 1;
		}

	}, {
		field : 'RESTYPE',
		title : '作业类型',
		align : "left"
	}, {
		field : 'TYPENAME',
		align : "left",
		title : '作业名称'
	}, {
		field : 'WORKFLOW',
		align : "left",
		title : '作业流程'
	} ],
	contextMenu : '#context-menu',
	// 左键
	onClickRow : function(row, tr, field) {
		clickRow = row;
		currTypeName = row.TYPENAME;
		currTypeId = row.RESTYPE;
		mainTypeId = row.ID;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
};

function getLeftMenu() {
	$.ajax({
		type : 'post',
		url : ctx + "/logistic/getAllReskind",
		dataType : 'json',
		success : function(result) {
			$(".layui-colla-item").find("div").remove();
			$.each(result, function(index, element) {
				$(".layui-colla-item").append(
						'<div style="cursor:pointer" class="layui-colla-content layui-show" id="' + element.ID + '" onclick="loadTab(\'' + element.ID + '\',\'' + element.RESKIND
								+ '\',\'' + element.KINDNAME + '\')"><p>' + element.KINDNAME + '</p></div>');
			});
		}
	});
}
// 加载右侧表格
function loadTab(kindMainId, reskind, kindname) {

	$("#" + kindMainId).parent().find(".layui-bg-gray").removeClass("layui-bg-gray");
	$("#" + kindMainId).addClass("layui-bg-gray");

	currKindName = kindname;
	mainKindId = kindMainId;
	currKindCode = reskind;

	$.ajax({
		type : 'post',
		url : ctx + "/logistic/getListData",
		data : {
			'kindMainId' : kindMainId
		},
		dataType : 'json',
		success : function(result) {
			$("#baseTable").bootstrapTable("load", result);
		}
	});
}

function formSubmitCallback() {// 子页面提交完成后，父页面关闭弹出层及刷新表格
	layer.close(iframe);
	window.location.href = window.location.href;
}

function freshTab() {// 子页面提交完成后，父页面关闭弹出层及刷新表格
	layer.close(iframe);
	loadTab(mainKindId, currKindCode, currKindName);
}

$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	baseTable = $("#baseTable");
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	getLeftMenu();

	// 新增保障类型
	$("#addType").click(function() {
		iframe = layer.open({
			type : 2,
			title : '新增保障类型',
			closeBtn : false,
			area : [ "500px", "400px" ],
			content : ctx + "/logistic/addType",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
				return false;
			}
		});
	});
	// 修改保障类型
	$("#modifyType").click(function() {
		if (!currKindName) {
			layer.msg('请选择一个保障类型！', {
				icon : 2
			});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title : '修改保障类型',
			closeBtn : false,
			area : [ "500px", "400px" ],
			content : ctx + "/logistic/modifyType?kindName=" + currKindName + "&mainKindId=" + mainKindId,
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
				return false;
			}
		});
	});
	// 删除保障类型
	$("#delType").click(function() {
		if (!currKindName) {
			layer.msg('请选择一个保障类型！', {
				icon : 2
			});
			return false;
		}
		layer.confirm("是否删除选中的保障？", {
			offset : '100px'
		}, function(index) {
			deleteKind(mainKindId);
			layer.close(index);
		});
	});
	// 增加作业
	$("#addWork").click(function() {
		if (!currKindName) {
			layer.msg('请选择一个保障类型！', {
				icon : 2
			});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title : '新增作业',
			closeBtn : false,
			area : [ "500px", "400px" ],
			content : ctx + "/logistic/addWork?kindName=" + currKindName + "&kindcode=" + currKindCode,
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
				return false;
			}
		});
	});

	// 修改作业
	$("#modifyWork").click(function() {
		if (!mainTypeId) {
			layer.msg('请选择一个作业', {
				icon : 2
			});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title : '修改作业',
			closeBtn : false,
			area : [ "500px", "400px" ],
			content : ctx + "/logistic/modifyWork?mainTypeId=" + mainTypeId,
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
				return false;
			}
		});
	});
	// 删除作业
	$("#delWork").click(function() {

		if (!currKindName) {
			layer.msg('请选择一个作业！', {
				icon : 2
			});
			return false;
		}
		if (clickRowId == "") {
			layer.msg('请选择一个作业', {
				icon : 2
			});
			return false;
		}
		layer.confirm("是否删除选中的作业？", {
			offset : '100px'
		}, function(index) {
			deleteType();
			layer.close(index);
		});

		layer.full(iframe);// 展开弹出层直接全屏显示
	});
})

// 删除保障
function deleteKind(mainKindId) {
	$.ajax({
		type : 'post',
		url : ctx + "/logistic/deleteKind",
		data : {
			'mainKindId' : mainKindId
		},
		success : function(result) {
			if (result == "success") {
				layer.msg('删除成功！', {
					icon : 1,
					time : 600
				});
				layer.close(iframe);
				window.location.href = window.location.href;
			} else {
				layer.msg('删除失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	});
}

// 删除作业
function deleteType() {
	$.ajax({
		type : 'post',
		url : ctx + "/logistic/deleteType",
		data : {
			'mainTypeId' : mainTypeId,
			'restype' : currTypeId
		},
		success : function(result) {
			if (result == "success") {
				layer.msg('删除成功！', {
					icon : 1,
					time : 600
				});
				layer.close(iframe);
				freshTab();
			} else {
				layer.msg('删除失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	});
}
