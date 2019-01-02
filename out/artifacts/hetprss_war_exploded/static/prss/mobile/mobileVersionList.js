var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
var clickRowId = "";
var set = null;
$(document).ready(function() {
	
	initMobileVersionGrid();
	
	$("#addVsnBtn").click(function() {
		set = layer.open({
			type : 2,
			title : "上传新版本",
			area : [ '750px', '400px' ],
			content : ctx + "/mobile/mobileVersion/toAddVersion",
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveVersion();
				return false;
			}
		})
	});
	
	$("#editVsnBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要编辑的版本！", {
				icon : 7
			});
			return false;
		}
		set = layer.open({
			type : 2,
			title : "编辑版本信息",
			area : [ '750px', '400px' ],
			content : ctx + "/mobile/mobileVersion/mobileVersionForm?id="+clickRowId,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveVersion();
				return false;
			}
		})
	});

	$("#delVsnBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的版本！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中的版本？', {
			btn : [ '确定', '取消' ]
		}, function() {
			$.ajax({
				async : false,
				type : 'post',
				url : ctx + '/mobile/mobileVersion/delVersion',
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
							$("#mobileVersionTable").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#mobileVersionTable").bootstrapTable('refresh');
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
function initMobileVersionGrid() {
	$("#mobileVersionTable").bootstrapTable({
		uniqueId : "id",
		url : ctx + "/mobile/mobileVersion/getMobileVersionData",
		method : "get",
		toolbar : "#toolbar",
		pagination : false,
		showRefresh : false,
		clickToSelect : false,
		undefinedText : "",
		height : $(window).height(),
		columns : getMobileVersionColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.vsnId;
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
		field : "vsnId",
		title : "序号",
		visible : false
	}, {
		field : "updateversion",
		title : "版本",
		width : '150px',
		align : 'center',
		valign : "middle",
		sortable : true
	}, {
		field : "updatedesc",
		title : '描述',
		valign : "middle"
	}, {
		field : "updatetime",
		title : '上传时间',
		width : '200px',
		align : 'center',
		valign : "middle",
		sortable : true
	}, {
		field : "btn",
		title : '下载',
		width : '50px',
		align : 'center',
		valign : "middle",
		events : operateEvents,
		formatter : function() {
			return '<button type="button" class="dldBtn" >下载</button>';
		}
	} ];
	return columns;
}

window.operateEvents = {
	'click .dldBtn' : function(e, value, row, index) {
		var id = row.vsnId;
		var url = ctx + "/mobile/mobileVersion/downloadApk"
		var $form = $('<form action="' + url + '" method="post" style="display:none"></form>');
		var hidden = '<input type="hidden" name="id" value="' + id + '"/>';
		$form.append(hidden);
		$('body').append($form);
		$form.submit();
	}
};

function saveSuccess(){
	layer.close(set);
	$("#mobileVersionTable").bootstrapTable('refresh');
}
