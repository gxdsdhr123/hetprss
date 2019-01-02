var layer;
var clickRowId = "";
var clickRowCode = "";
var set = null;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	initGrid();
	//新增单据
	$("#newBtn").click(function() {
		set = layer.open({
			type : 2,
			title : "新增单据",
			content : ctx + "/produce/bill/newAlnBill",
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveBill();
				return false;
			}
		})
		layer.full(set);
	});
	
	//修改单据
	$("#editBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要查看的单据！", {
				icon : 7
			});
			return false;
		}
		set = layer.open({
			type : 2,
			title : "修改单据",
			area : [ '750px', '400px' ],
			content : ctx + "/produce/bill/editAlnBill?id="+clickRowId,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveBill();
				return false;
			}
		})
		layer.full(set);
	});
	
	//删除单据
	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的单据！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中单据？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				async : false,
				type : 'post',
				url : ctx + '/produce/bill/delAlnBill',
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
							$("#billGrid").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#billGrid").bootstrapTable('refresh');
						})
					}
				}
			});
		});
	});
	
	
	
	$("#downloadBtn").click(function(){
		var columns = '[{"field":"R","title":"序号"},{"field":"FLIGHT_NUMBER","title":"航班号"},{"field":"ACT_TYPE","title":"机型"},{"field":"AIRCRAFT_NUMBER","title":"机号"},{"field":"ITEM_DATE","title":"日期"},{"field":"ITEM_NAME","title":"项目"},{"field":"START_TM","title":"开始时间"},{"field":"END_TM","title":"结束时间"},{"field":"NUM","title":"数量"},{"field":"REMARK","title":"备注"},{"field":"OPERATOR","title":"操作人"},{"field":"CREATE_DATE","title":"创建时间"}]';
		if(clickRowId=="" ||  clickRowId==null || clickRowId.length==0){
			layer.msg('请选择要下载的单据', {	icon:0});
			return false;
		}
		$("#printTitle").text(columns);
		$("#idDou").text(clickRowId);
		$("#printForm").submit();
	});
	
});
/**
 * 初始化表格
 */
function initGrid() {
	$("#billGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "rowId",
		url : ctx + "/produce/bill/getAlnBillData",
		method : "get",
		pagination : false,
		showRefresh : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		pagination : true,
		search : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.ID;
			clickRowCode = row.TYPE_CODE;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
	});
}
/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getGridColumns() {
	var columns = [ {
		field : "rowId",
		title : "序号",
		width : '40px',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return index + 1;
		}
	},{
		field : "ID",
		visible : false
	}, {
		field : "FLIGHT_DATE",
		title : '航班日期',
		align : 'center'
	}, {
		field : "FLIGHT_NUMBER",
		title : '航班号',
		align : 'center'
	}, {
		field : "AIRCRAFT_NUMBER",
		title : '机号',
		align : 'center'
	},  {
		field : "OPERATOR",
		align : 'center',
		title : '操作人'
	}];
	return columns;
}



function saveSuccess(){
	layer.close(set);
	$("#billGrid").bootstrapTable('refresh');
}
