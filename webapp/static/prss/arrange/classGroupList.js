var groupid;
var iframe;
var baseTable;
var clickRow;
// 表格列选项默认设置
jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
jQuery.fn.bootstrapTable.columnDefaults.align = "center";

var tableOptions = {
	url : ctx + "/arrange/classGroup/getListData", // 请求后台的URL（*）
	method : "get", // 请求方式（*）
	dataType : "json", // 返回结果格式
	striped : true, // 是否显示行间隔色
	pagination : false, // 是否显示分页（*）
	cache : false, // 是否启用缓存
	undefinedText : '', // undefined时显示文本
	checkboxHeader : false, // 是否显示全选
	toolbar : $("#tool-box"), // 指定工具栏dom
	search : true, // 是否开启搜索功能
	columns : [ {
		field : 'ID',
		title : 'ID',
		visible : false,
		searchable:false
	}, {
		field : 'order',
		title : '序号',
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		field : 'NAME',
		sortable:true,
		title : '班组名称'
	}, {
		field : 'PM',
		sortable:true,
		title : '领班'
	}, {
		field : 'SM',
		sortable:true,
		title : '副领班'
	}, {
		field : 'CREATOR',
		sortable:true,
		title : '创建人'
	}, {
		field : 'CREATETIME',
		sortable:true,
		title : '创建时间'
	} ],
	contextMenu : '#context-menu',
	onClickRow : function(row, tr, field) {
		clickRow = row;
		groupid = row.ID;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
};

function formSubmitCallback() {// 子页面提交完成后，父页面关闭弹出层及刷新表格
	layer.close(iframe);
	window.location.href = window.location.href;
}

function freshTab() {// 子页面提交完成后，父页面关闭弹出层及刷新表格
	layer.close(iframe);
	baseTable.bootstrapTable('refresh');
}

$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	baseTable = $("#baseTable");
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
	// 新增
	$("#add").click(function() {
		iframe = layer.open({
			type : 2,
			title : '新增班组',
			closeBtn : false,
			content : ctx + "/arrange/classGroup/add",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
				return false;
			}
		});
		layer.full(iframe);
	});
	// 修改
	$("#modify").click(function() {
		if (!groupid) {
			layer.msg('请选择一个班组！', {
				icon : 2
			});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title : '修改班组',
			closeBtn : false,
			content : ctx + "/arrange/classGroup/modify?groupid=" + groupid,
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
				return false;
			}
		});
		layer.full(iframe);
	});
	// 删除
	$("#del").click(function() {
		if (!groupid) {
			layer.msg('请选择一个班组！', {
				icon : 2
			});
			return false;
		}
		layer.confirm("是否删除选中的班组？", {
			offset : '100px'
		}, function(index) {
			deleteGroup(groupid);
			layer.close(index);
		});
	});
	
})

// 删除
function deleteGroup(groupid) {
	$.ajax({
		type : 'post',
		url : ctx + "/arrange/classGroup/deleteGroup",
		data : {
			'groupid' : groupid
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
