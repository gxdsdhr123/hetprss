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
		var flag=$("#flag").val();
		set = layer.open({
			type : 2,
			title : "新增单据",
			content : ctx + "/produce/chargeBill/addChargeBill?flag="+flag,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.addBill();
				return false;
			}
		});
		layer.full(set);// 展开弹出层直接全屏显示
	});
	
	//修改单据
	$("#editBtn").click(function() {
		var flag=$("#flag").val();
		if (clickRowId=="") {
			layer.msg("请选择要查看的单据！", {
				icon : 7
			});
			return false;
		}
		set = layer.open({
			type : 2,
			title : "修改单据",
			content : ctx + "/produce/chargeBill/updateChargeBill?billId="+clickRowId+"&flag="+flag,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveBill();
				return false;
			}
		})
		layer.full(set);// 展开弹出层直接全屏显示
	});
	
	//删除单据
	$("#delBtn").click(function() {
		var flag=$("#flag").val();
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
				url : ctx + '/produce/chargeBill/delChargeBill',
				data : {
					'id':clickRowId,
					'type':clickRowCode,
					'flag':flag
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
							$("#chargeBillGrid").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#chargeBillGrid").bootstrapTable('refresh');
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
	var flag=$("#flag").val();
	$("#chargeBillGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "rowId",
		url : ctx + "/produce/chargeBill/getChargeBillData?flag="+flag,
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
			clickRowCode = row.FLTID;
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
	}, {
		field : "CREATE_DATE",
		title : '日期',
		align : 'center'
	}, {
		field : "FLIGHT_NUMBER",
		title : '航班号',
		align : 'center'
	}, {
		field : "AIRCRAFT_NUMBER",
		title : '机号',
		align : 'center'
	}, {
		field : "OPERATOR",
		title : '客梯车司机',
		align : 'center'
	}];
	return columns;
}



function saveSuccess(){
	layer.close(set);
	$("#chargeBillGrid").bootstrapTable('refresh');
}
