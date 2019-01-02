var layer;
var clickRowId = "";
var set = null;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	var form = layui.form;
	
	form.on('select(puUser)',function(data){
		var phone = $(data.elem.options[data.elem.selectedIndex]).attr('data-phone');
		$('#puPhone').val(phone);
	});
});
$(document).ready(function() {
	initGrid();
	
	//新增
	$("#addGoodsBtn").click(function() {
		insertRow();
	});
	
});
/**
 * 初始化表格
 */
function initGrid() {
	$("#pickupGoodsTable").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		uniqueId : "id",
		url : ctx + "/produce/pickup/getPickupGoodstData?id="+$("#id").val(),
		method : "get",
		pagination : false,
		showRefresh : false,
		clickToSelect : false,
		undefinedText : "",
		columns : getGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
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
		field : "id",
		title : "序号",
		align : 'center',
		valign : 'middle',
		width : '40px',
		formatter : function(value, row, index) {
			return index + 1;
		}
	},{
		field : "name",
		title : "捡拾物品",
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return value?value:'';
		},
		editable : {
			type : 'text'
		}
	}, {
		field : "pos",
		title : '捡拾地点',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return value?value:'';
		},
		editable : {
			type : 'text'
		}
	}, {
		field : "feature",
		title : '特征',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return value?value:'';
		},
		editable : {
			type : 'text'
		}
	}, {
		field : "btn",
		title : '操作',
		width : '50px',
		align : 'center',
		valign : "middle",
		width : '50px',
		events : operateEvents,
		formatter : function() {
			return '<button type="button" class="delBtn" >删除</button>';
		}
	}];
	return columns;
}

window.operateEvents = {
		'click .delBtn' : function(e, value, row, index) {
			$('#pickupGoodsTable').bootstrapTable('removeByUniqueId', row.id);
		}
};

function insertRow() {
	var index = $('#pickupGoodsTable').bootstrapTable('getData').length;
	var columns = getGridColumns();
	var emptyRow = {};
	emptyRow["id"] = index;
	$('#pickupGoodsTable').bootstrapTable('insertRow', {
		index : index,
		row : emptyRow
	});
}

function savepickup() {

	var dataItme = $('#pickupGoodsTable').bootstrapTable('getData');
	var goods = [];
	for (var i = 0; i < dataItme.length; i++) {
		var vipInfo = dataItme[i];
		var isEmptyRow = true;
		for ( var field in vipInfo) {
			if (field !== "id" && vipInfo[field]) {
				isEmptyRow = false;
				break;
			}
		}
		if (!isEmptyRow) {
			goods.push(vipInfo);
		}
	}
	var json = {};
	var formInput = $("#pickupForm input,#pickupForm select");
	for(var i=0;i<formInput.length;i++){
		var o = formInput.eq(i);
		json[o.attr('name')] = o.val() || '';
	}
	var loading = layer.load(2, {
		shade : [ 0.1, '#000' ]
	});
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/produce/pickup/savePickup",
		data : {
			'pickup' :JSON.stringify(json),
			'goods' : JSON.stringify(goods)
		},
		error : function() {
			layer.close(loading);
			layer.msg('保存失败！', {
				icon : 2
			});
		},
		success : function(data) {
			layer.close(loading);
			if (data == "success") {
				layer.msg('保存成功！', {
					icon : 1,
					time : 600
				},function(){
					parent.saveSuccess();
				});
			} else {
				layer.msg('保存失败！', {
					icon : 2,
					time : 600
				});
			}
		}
	});
}
