var layer;
var clickRowId = "";
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	initGrid();
	// 新增
	$("#addBtn").click(function() {
		clickRowId = "";
		openEditWin(clickRowId,"add");
	});
	// 修改
	$("#editBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要修改的行！", {
				icon : 7
			});
			return false;
		}
		openEditWin(clickRowId,"edit");
	});
	// 删除
	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要修改的行！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中排班？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				type : 'post',
				url : ctx + "/arrange/ambulatoryShifts/del",
				data : {
					'id':clickRowId
				},
				dataType : "text",
				error : function() {
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(data) {
					if(data=="success"){
						layer.msg('删除成功！', {
							icon : 1,
							time : 600
						}, function() {
							$("#ambulatoryShiftsGrid").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#ambulatoryShiftsGrid").bootstrapTable('refresh');
						});
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
	$("#ambulatoryShiftsGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "id",
		url : ctx + "/arrange/ambulatoryShifts/getAmbulatoryShiftsTypeData",
		method : "get",
		pagination : false,
		showRefresh : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $(window).height(),
		columns : getGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.shiftsId;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		responseHandler:function(res) {
			for(var i=0; i<res.length; i++){
				switch(res[i].weekCode)
				{
				case "W1":
					res[i].weekCode = "星期一";
				  break;
				case "W2":
					res[i].weekCode = "星期二";
				  break;
				case "W3":
					res[i].weekCode = "星期三";
				  break;
				case "W4":
					res[i].weekCode = "星期四";
				  break;
				case "W5":
					res[i].weekCode = "星期五";
				  break;
				case "W6":
					res[i].weekCode = "星期六";
				  break;
				case "W7":
					res[i].weekCode = "星期日";
				  break;
				}
			};
			return res;
		},
		onLoadSuccess: function (data) {
			var total = $('#ambulatoryShiftsGrid').bootstrapTable('getData').length;
			for(var i=0;i<total;i++){
				if(i%7==0){
					$('#ambulatoryShiftsGrid').bootstrapTable('mergeCells', {index: i, field: 'rowId', rowspan: 7});
					$('#ambulatoryShiftsGrid').bootstrapTable('mergeCells', {index: i, field: 'shiftsName', rowspan: 7});
				}
			}
        }
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
			return Math.floor(index/7) + 1;
		}
	}, {
		field : "shiftsName",
		title : '名称',
		align : 'center',
		valign : 'middle',
		width : '200px'
	}, {
		field : "weekCode",
		title : '星期',
		width : '150px'
	}, {
		field : "startime",
		title : '开始时间',
		width : '150px'
	}, {
		field : "endtime",
		title : '结束时间',
		width : '150px'
	}, {
		field : "bindFlt",
		title : '航班'
	}];
	return columns;
}

var editWin = null;
function openEditWin(id,type){
	editWin = layer.open({
		closeBtn : false,
		type : 2,
		title : false,
		area : [ '100%', '100%' ],
		btn : [ "保存", "取消" ],
		btn1 : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.saveAFT();
			return false;
		},
		content : [ ctx + "/arrange/ambulatoryShifts/form?id="+(clickRowId?clickRowId:"")]
	});
}

function saveSuccess(){
	layer.close(editWin);
	$("#ambulatoryShiftsGrid").bootstrapTable('refresh');
}
