var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
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
	$('#create').click(function() {
		iframe = layer.open({
			type : 2,
			title : false,
			closeBtn : false,
			area:[$("body").width()+"px",$("body").height()+"px"],
			content : ctx + "/produce/sageguard/form?type=create&id=",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var submitData = iframeWin.getSubmitData();
				if(iframeWin.doCheck()){
					$.ajax({
						type : 'post',
						url : ctx + "/produce/sageguard/save",
						data : {
							data : submitData
						},
						async : true,
						dataType : 'json',
						success : function(data) {
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
						},error : function(msg){
							console.info(msg)
						}
					});
				}
			}
		});
		layer.full(iframe);// 展开弹出层直接全屏显示
	});

	// 修改
	$('#modify').click(function() {
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要修改的数据', {
				icon : 2
			});
		}else{
			var id = clickRowId;
				iframe = layer.open({
					type : 2,
					title : false,
					area:[$("body").width()+"px",$("body").height()+"px"],
					closeBtn : false,
					content : ctx + "/produce/sageguard/form?type=update&id=" + id,
					btn : [ "保存", "取消" ],
					yes : function(index, layero) {
						var iframeWin = window[layero.find('iframe')[0]['name']];
						var submitData = iframeWin.getSubmitData();
						if(iframeWin.doCheck()){
							$.ajax({
								type : 'post',
								url : ctx + "/produce/sageguard/save",
								data : {
									data : submitData
								},
								async : true,
								dataType : 'json',
								success : function(data) {
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
		}
	});

	// 删除
	$('#delete').click(function() {
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			var id = clickRowId;
				layer.confirm('确定删除数据？', {
					btn : [ '硬定', '取消' ]
				// 按钮
				}, function() {
					$.ajax({
						type : 'post',
						url : ctx + "/produce/sageguard/delete",
						data : {
							id : id
						},
						async : false,
						dataType : 'json',
						success : function(data) {
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
		url: ctx + "/produce/sageguard/ruleList", 
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
	        	desc : encodeURIComponent($("#desc").val()),
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
			{field: "TYPE_NAME", title: "设备类型",editable:false,align:'left',halign:'center'},
			{field: "MODEL", title: "设备型号",editable:false,align:'left',halign:'center'},
			{field: "DEVICE_ID", title: "设备编号",editable:false,align:'left',halign:'center'},
			{field: "PLATE_NUMBER", title: "车牌号",editable:false,align:'left',halign:'center'},
			{field: "NOTE", title: "备注",editable:false,align:'left',halign:'center'}
//			序号，牵引车类型，设备型号，设备编号，车牌号，描述
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

