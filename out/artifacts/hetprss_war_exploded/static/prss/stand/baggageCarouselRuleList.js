var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = null;// 当前选中行，以便单选后操作
var clickRow;
var filter;// 用于筛选
var iframe;
var ioFlag;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	new PerfectScrollbar('#container');
	
	document.onkeydown = function(e){
	    var ev = document.all ? window.event : e;
	    if(ev.keyCode==13) {
	    	var ruleName = encodeURIComponent($("#ruleName").val());
	    	var options = $('#baseTable').bootstrapTable('refresh', {
				query : {
					ruleName : ruleName,
					pageNumber: 1,    
	                pageSize: 10
					
				}
			});
		    return false;
	     }
	}
	// 新增
	$('#createRule').click(function() {
		iframe = layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			area:['100%','100%'],
			content : ctx + "/stand/carousel/form?type=create&schemaId=101&id=",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var submitData = iframeWin.getSubmitData();
				if(iframeWin.doCheck()){
					var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
					$.ajax({
						type : 'post',
						url : ctx + "/stand/carousel/save",
						data : {
							data : submitData
						},
						async : true,
						dataType : 'json',
						success : function(data) {
							layer.close(loading);
							if (data.code == 0) {
								layer.msg(data.msg, {
									icon : 1
								});
								$("#baseTable").bootstrapTable("refresh");
								layer.close(index);
							} else {
								layer.msg(data.msg, {
									icon : 2
								});
							}
						}
					});
				}
			}
		});
		layer.full(iframe);// 展开弹出层直接全屏显示
	});

	// 修改
	$('#modifyRule').click(function() {
		if(clickRowId==null){
			layer.msg('请选择要修改的数据', {
				icon : 2
			});
		}else{
			var id = clickRowId;
				iframe = layer.open({
					type : 2,
					title : false,
					area:['100%','100%'],
					closeBtn : false,
					content : ctx + "/stand/carousel/form?type=update&schemaId=101&id=" + id,
					btn : [ "保存", "取消" ],
					yes : function(index, layero) {
						var iframeWin = window[layero.find('iframe')[0]['name']];
						var submitData = iframeWin.getSubmitData();
						if(iframeWin.doCheck()){
							var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
							$.ajax({
								type : 'post',
								url : ctx + "/stand/carousel/save",
								data : {
									data : submitData
								},
								async : true,
								dataType : 'json',
								success : function(data) {
									layer.close(loading);
									if (data.code == 0) {
										layer.msg(data.msg, {
											icon : 1
										});
										$("#baseTable").bootstrapTable("refresh");
										layer.close(index);
									} else {
										layer.msg(data.msg, {
											icon : 2
										});
									}
								}
							});
						}
					}
				});
				//layer.full(iframe);// 展开弹出层直接全屏显示
		}
	});

	// 删除
	$('#deleteRule').click(function() {
		if(clickRowId==null){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			var id = clickRowId;
				layer.confirm('确定删除数据？', {
					btn : [ '硬定', '取消' ]
				// 按钮
				}, function() {
					var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
					$.ajax({
						type : 'post',
						url : ctx + "/stand/carousel/delete",
						data : {
							ruleId : id
						},
						async : false,
						dataType : 'json',
						success : function(data) {
							layer.close(loading);
							if (data.code == 0) {
								layer.msg(data.msg, {
									icon : 1
								});
								clickRowId=null;
								$("#baseTable").bootstrapTable("refresh");
							} else {
								layer.msg(data.msg, {
									icon : 2
								});
							}
						}
					});
				});
		}
	});
	
	//查询功能
	$(".search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query : {
				ruleName : encodeURIComponent($("#ruleName").val()),
				pageNumber: 1,    
                pageSize: 10
				
			}
		});
	})
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/stand/carousel/dataList", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
	        	ruleName : encodeURIComponent($("#ruleName").val()),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize
	        }
	    },
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.ID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
	    columns: [
			{field: "ID",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "PRI_LVL", title: "优先级",editable:false,align:'left',halign:'center'},
			{field: "IF_VALID", title: "是否生效",editable:false,align:'left',halign:'center',formatter:function(value,row,index){
				return value == 1?"否":"是";
			}},
			{field: "RULE_NAME", title: "规则名称",editable:false,align:'left',halign:'center'},
			{field: "TEXT", title: "条件",editable:false,halign:'center'},
			{field: "CRSL_STR", title: "行李转盘",editable:false,halign:'center'}
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});

/**
 * 表单提交回调
 */
function formSubmitCallback() {// 子页面提交完成后，父页面关闭弹出层及刷新表格
	layer.close(iframe);
	$("#baseTable").bootstrapTable("refresh");
}

