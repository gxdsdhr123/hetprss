var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
var clickRowId = "";
var set = null;
$(document).ready(function() {
	
	initGrid();
	
	$("#addBtn").click(function() {
		set = layer.open({
			type : 2,
			title : "新增设备",
			area : [ '550px', '600px' ],
			content : ctx + "/mobile/mobileManage/toEditForm",
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveVersion();
				return false;
			}
		})
	});
	
	$("#editBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要编辑的设备！", {
				icon : 7
			});
			return false;
		}
		set = layer.open({
			type : 2,
			title : "编辑设备信息",
			area : [ '550px', '600px' ],
			content : ctx + "/mobile/mobileManage/toEditForm?id="+clickRowId,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveVersion();
				return false;
			}
		})
	});

	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的设备！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中的设备？', {
			btn : [ '确定', '取消' ]
		}, function() {
			$.ajax({
				async : false,
				type : 'post',
				url : ctx + '/mobile/mobileManage/delPDA',
				data : {
					'id':clickRowId
				},
				error : function() {
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(result) {
					if(result=="success"){
						layer.msg('删除成功！', {
							icon : 1,
							time : 600
						}, function() {
							clickRowId = "";
							$("#mobileManageTable").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#mobileManageTable").bootstrapTable('refresh');
						})
					}
				}
			});
		});
	});
	
});
/**
 * 初始化表格
 */
function initGrid() {
	$("#mobileManageTable").bootstrapTable({
		uniqueId : "id",
		url : ctx + "/mobile/mobileManage/getMobileManageData",
		method : "get",
		toolbar : "#toolbar",
		pagination : true,
		showRefresh : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		search : true,
		undefinedText : "",
		pageNumber : 1,
		pageSize : 20,
		pageList : [20,50,100],
		height : $(window).height(),
		columns : getMobileVersionColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.pdaId;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		}
	});
}

/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getMobileVersionColumns() {
	var columns = [ {
		field : "id",
		title : "序号",
		width : '40px',
		align : 'center',
		valign : "middle",
		formatter : function(value, row, index) {
			row["id"] = index;
			return index + 1;
		}
	}, {
		field : "pdaId",
		title : "序号",
		visible : false
	}, {
		field : "officeId",
		title : "部门",
		align : 'center',
		valign : "middle"
	}, {
		field : "pdaNo",
		title : '设备编号',
		align : 'center',
		valign : "middle"
	}, {
		field : "imei",
		title : 'IMEI',
		align : 'center',
		valign : "middle"
	}, {
		field : "pdaStatus",
		title : '状态',
		align : 'center',
		valign : "middle"
	}, {
		field : "updateUser",
		title : '操作员',
		align : 'center',
		valign : "middle"
	}, {
		field : "createTime",
		title : '入库时间',
		align : 'center',
		valign : "middle"
	} ];
	return columns;
}

function saveSuccess(){
	layer.close(set);
	$("#mobileManageTable").bootstrapTable('refresh');
}
