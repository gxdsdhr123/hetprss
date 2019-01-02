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
	    	console.info(ruleName);
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
	
	$("#down").click(function(){
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要下载的数据', {
				icon : 2
			});
		}else{
			$("input[name=id]").val(clickRowId);
			$("#printForm").submit();
		}
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
					area:['100%','100%'],
					closeBtn : false,
					content : ctx + "/produce/tractor/form?id=" + id,
					btn : [ "保存", "取消" ],
					yes : function(index, layero) {
						var iframeWin = window[layero.find('iframe')[0]['name']];
						var submitData = iframeWin.getSubmitData();
						$.ajax({
							type : 'post',
							url : ctx + "/produce/tractor/save",
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
				});
				//layer.full(iframe);// 展开弹出层直接全屏显示
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
		url: ctx + "/produce/tractor/data", 
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
			{field: "BOUND_DATE", title: "日期",editable:false,align:'center',halign:'center'},
			{field: "KINDNAME", title: "岗位",editable:false,align:'left',halign:'center'},
			{field: "INNER_NUMBER", title: "车号",editable:false,align:'left',halign:'center'},
			{field: "NAME", title: "驾驶",editable:false,align:'left',halign:'center'}
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

