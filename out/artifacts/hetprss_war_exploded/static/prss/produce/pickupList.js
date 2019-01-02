var layer;
var clickRowId = "";
var clickRowCode = "";
var set = null;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	_search();
	//查询
	$("#searchBtn").click(function() {
		_search();
	});
	
	//新增单据
	$("#newBtn").click(function() {
		set = layer.open({
			type : 2,
			title : "新增交接单",
			area : [ '1100px', '500px' ],
			content : ctx + "/produce/pickup/newPickup",
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.savepickup();
				return false;
			}
		})
	});
	
	//修改单据
	$("#editBtn").click(function() {
		var clickRows = $("#pickupGrid").bootstrapTable('getSelections');
		if (clickRows.length == 0) {
			layer.msg("请选择要删除的单据！", {
				icon : 7
			});
			return false;
		}
		var clickRowId = clickRows[0].id;
		set = layer.open({
			type : 2,
			title : "修改交接单",
			area : [ '1100px', '500px' ],
			content : ctx + "/produce/pickup/editPickup?id="+clickRowId,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.savepickup();
				return false;
			}
		})
	});
	
	//删除单据
	$("#delBtn").click(function() {
		var clickRows = $("#pickupGrid").bootstrapTable('getSelections');
		if (clickRows.length == 0) {
			layer.msg("请选择要删除的单据！", {
				icon : 7
			});
			return false;
		}
		var rowIds = '';
		$.each(clickRows,function(i,item){
			rowIds += ','+item.id;
		});
		rowIds = rowIds.substring(1);
		var confirm = layer.confirm('您确定要删除选中单据？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				async : false,
				type : 'post',
				url : ctx + '/produce/pickup/delPickup',
				data : {
					'ids':rowIds
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
							_search();
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							_search();
						})
					}
				}
			});
		});
	});
	
	$("#downloadBtn").click(function(){
		var clickRows = $("#pickupGrid").bootstrapTable('getSelections');
		if (clickRows.length == 0) {
			layer.msg("请选择要删除的单据！", {
				icon : 7
			});
			return false;
		}
		var rowIds = '';
		$.each(clickRows,function(i,item){
			rowIds += ','+item.id;
		});
		rowIds = rowIds.substring(1);
		$("input[name=ids]").val(rowIds);
		$("#printForm").submit();
	});
	
});
/**
 * 初始化表格
 */
function _search() {
	$("#pickupGrid").bootstrapTable('destroy');
	$("#pickupGrid").bootstrapTable({
		striped : true,
		idField : "id",
		uniqueId : "rowId",
		url : ctx + "/produce/pickup/getPickupListData",
		method : "get",
		sidePagination: 'server',
		showRefresh : false,
		clickToSelect : true,
		pagination : true,
		undefinedText : "",
		queryParamsType:'',
		pageNumber: 1,
		pageSize: 10,
		pageList: [10, 15, 20],
		queryParams: function (params) {
			params.startDate = $('#startDate').val();
			params.endDate = $('#endDate').val();
			params.searchText = escape($('#searchtext').val());
			
			return params;
		},
		height : $(window).height() - 70,
		checkboxHeader:true,
		columns : getGridColumns(),
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			clickRowId = row.id;
			set = layer.open({
				type : 2,
				title : "修改交接单",
				area : [ '1100px', '500px' ],
				content : ctx + "/produce/pickup/editPickup?id="+clickRowId,
				btn : [ "保存", "取消" ],
				btn1 : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.savepickup();
					return false;
				}
			})
		},
		onClickCell : function(field, value, row, td) {
			// 单击查看图片
			if(field == 'pics'){
				showPics(row.pics);
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
			return index + 1;
		}
	}, {
  	  checkbox:true
    },{
		field : "pickupNumber",
		title : '编号',
		align : 'center'
	}, {
		field : "flightDate",
		title : '航班日期',
		align : 'center'
	}, {
		field : "flightNumber",
		title : '航班号',
		align : 'center'
	}, {
		field : "puUser",
		title : '捡拾者',
		align : 'center'
	}, {
		field : "operator",
		title : '兹收到',
		align : 'center'
	}, {
		field : "goodNames",
		title : '物品',
		align : 'center'
	}, {
		field : "pics",
		title : '图片',
		align : 'center',
		formatter : function(value,row,index){
			if(value && value.length!=0){
//				return '<a href="javascript:void(0)" data-id="'+value+'" data-type="1" onclick="downAtta($(this))"><i class="fa fa-photo">&nbsp;<i></i>查看</i></a>'
				return '<a href="javascript:void(0)"><i class="fa fa-photo">&nbsp;<i></i>查看</i></a>'
			}else{
				return '';
			}
		}
	}];
	return columns;
}

function saveSuccess(){
	layer.close(set);
	_search();
}

function showPics(pics){
	layer.open({
		type:1,
		area: ['800px','500px'],
		skin: 'layui-layer-nobg',
		shadeClose: true,
		title:false,
		content:$('#pic-table-pop'),
		success:function(layor){
			$(layor).find('#pic-table').bootstrapTable('destroy')
			.bootstrapTable({
				striped : true,
				undefinedText : "",
				data:pics,
				height : '480px',
				columns : [
					{
						field : "rowId",
						title : "序号",
						width : '40px',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							return index + 1;
						}
					}, {
						field : "picDate",
						title : '日期',
						align : 'center'
					},{
						field : "picName",
						title : '名称',
						align : 'center'
					},{
						field : "picId",
						title : '操作',
						align : 'center',
						formatter : function(value,row,index){
							if(value && value.length!=0){
								return '<a href="javascript:void(0)" data-id="'+value+'" data-type="1" onclick="viewAtta($(this))"><i class="fa fa-photo">&nbsp;<i></i>查看</i></a>'
								+ ' | <a href="javascript:void(0)" data-id="'+value+'" data-type="1" onclick="downAtta($(this))">下载</i></a>'
							}else{
								return '';
							}
						}
					}
	           ]
			});
		}
	});
}

function viewAtta(btn){
	var id = btn.data("id");
	layer.open({
		type:2,
		area: ['450px','450px'],
		skin: 'layui-layer-nobg',
		shadeClose: true,
		title:false,
		content:ctx + '/produce/pickup/downAtta?id=' + id
	});
}

function downAtta(btn){
    var a = document.querySelector('#saveImg');
    var event = new MouseEvent('click')
    a.download = '下载图片_'+btn.data("id")+'.jpg'
    a.href = ctx + '/produce/pickup/downPic?id=' + btn.data("id");
    a.dispatchEvent(event);
}