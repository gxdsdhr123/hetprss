var layer;
var id = '';
var clickRowId = "";
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
var setWin = null;
$(document).ready(function() {
	$(".sortable").css("position","relative");
	$(".sortable").each(function(){
		new PerfectScrollbar(this);
	});
	$("html,body").css("cssText","height:100% !important;overflow:hidden");
	initNodeGrid();
	// 部门设置
	$("#setBtn").click(function() {
		if (id=="") {
			layer.msg("请选择要修改的部门！", {
				icon : 7
			});
			return false;
		}
		setWin = layer.open({
			type : 2,
			title : "可选区域类型",
			closeBtn : false,
			area : ['400px','200px'],
			content : ctx + "/aptitude/aptitudeLimits/officeArr?id="+(id?id:""),
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var json = iframeWin.sub();
				var loading = layer.load(2, {
					shade : [ 0.1, '#000' ]
				});
				$.ajax({
					type : 'post',
					async : false,
					url : ctx + "/aptitude/aptitudeLimits/saveOfficeArr",
					data : {
						'officeLimConf' :JSON.stringify(json)
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
								layer.close(setWin);
							});
						} else {
							layer.msg('保存失败！', {
								icon : 2,
								time : 600
							});
						}
					}
				});
				return false;
			}
		});
	});
	// 新增
	$("#addBtn").click(function() {
		if (id=="") {
			layer.msg("请选择要修改的部门！", {
				icon : 7
			});
			return false;
		}
		setWin = layer.open({
			type : 2,
			title : "可选区域类型",
			closeBtn : false,
			area : ['800px','500px'],
			content : ctx + "/aptitude/aptitudeLimits/newAptitude?id="+(id?id:""),
			btn : [ "保存", "取消" ],
			
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				layero.find('iframe').contents().find('iframe')[0].contentWindow.prepareMembers();
				iframeWin.saveForm();
				return false;
			}
		})
	});
	// 修改
	$("#editBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要修改的行！", {
				icon : 7
			});
			return false;
		}
		setWin = layer.open({
			type : 2,
			title : "修改",
			closeBtn : false,
			area : ['800px','500px'],
			content : ctx + "/aptitude/aptitudeLimits/editAptitude?id="+(clickRowId?clickRowId:""),
			btn : [ "保存", "取消" ],
			
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				layero.find('iframe').contents().find('iframe')[0].contentWindow.prepareMembers();
				iframeWin.saveForm();
				return false;
			}
		})
	});
	// 删除
	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的行！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中区域？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				type : 'post',
				url : ctx + "/aptitude/aptitudeLimits/delArea",
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
							$("#aptitudeGrid").bootstrapTable('refresh');
						});
					}else if(data=="used"){
						layer.msg('请先删除分工！', {
							icon : 2
						}, function() {
							$("#aptitudeGrid").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#aptitudeGrid").bootstrapTable('refresh');
						});
					}
					
				}
			});
		});
	});
})
/**
 * 初始化表格
 */
function initNodeGrid() {
	$("#aptitudeGrid").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "id",
		url : ctx + "/aptitude/aptitudeLimits/getAreaConfData",
		method : "post",
		pagination : false,
		showRefresh : false,
		search : true,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		height : $("#rightDiv").height()-$(".bs-bars").height(),
		columns : getGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		}
	})
}
/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getGridColumns() {
	var columns = [ {
		title : "序号",
		width : '40px',
		align : 'center',
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		field : "limitLevel",
		title : '区域类型',
		sortable : true
	},{
		field : "areaName",
		title : '区域名称',
		sortable : true
	},{
		field : "parentAreaName",
		title : '所属资质区域',
		sortable : true
	},{
		field : "limitType",
		title : '区域属性',
		sortable : true
	}, {
		field : "creatorId",
		title : '创建人',
		sortable : true
	}, {
		field : "createTime",
		title : '创建时间',
		sortable : true
	} ];
	return columns;
}

function clickOffice(officeBtn){
	id = $(officeBtn).data("id");
	$(".list-group-item").removeClass("clickRow");
	$(officeBtn).addClass("clickRow");
	$.ajax({
		type : 'POST',
		async : false,
		url : ctx + "/aptitude/aptitudeLimits/changeData",
		data : {
			'officeId':id
		},
		dataType : "json",
		success:function(result){
			$('#aptitudeGrid').bootstrapTable('load', result);
		}
	});
}

function saveSuccess(){
	layer.close(setWin);
	$("#aptitudeGrid").bootstrapTable('refresh');
}