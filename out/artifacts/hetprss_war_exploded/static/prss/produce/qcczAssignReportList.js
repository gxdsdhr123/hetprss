var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	new PerfectScrollbar('#container');
	
	document.onkeydown = function(e){
	    var ev = document.all ? window.event : e;
	    if(ev.keyCode==13) {
	    	var beginTime = encodeURIComponent($("#beginTime").val());
	    	var endTime = encodeURIComponent($("#endTime").val());
	    	console.info(ruleName);
	    	var options = $('#baseTable').bootstrapTable('refresh', {
				query : {
					beginTime : beginTime,
					endTime : endTime,
					pageNumber: 1,    
	                pageSize: 10
					
				}
			});
		    return false;
	     }
	}
	//下载单据
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
	//删除单据
	$("#delete").click(function(){
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			$.ajax({
		        type: "POST",
		        url: ctx + '/produce/qcczAssignReport/delete',
		        data: {
		        	"reportId":clickRowId
		        },
		        dataType: "html",
		        success: function(data){
		            if(data == "1"){
		            	layer.msg('删除成功！', {
		    				icon : 2
		    			});
		            }else{
		            	layer.msg('删除失败！', {
		    				icon : 2
		    			});
		            }
		            $("#baseTable").bootstrapTable("refresh");
		         }
		    });
		}
	});
	// 修改
	$('#modify').click(function() {
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要修改的数据', {
				icon : 2
			});
		}else{
			layer.open({
				type : 2,
				title : false,
				area:['1000px','100%'],
				closeBtn : false,
				content : ctx + "/produce/qcczAssignReport/openModify?id="+clickRowId,
				btn : [ "保存", "取消" ],
				yes : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					var tag = iframeWin.DoSave();
					if(tag == 0){
						$("#baseTable").bootstrapTable("refresh");
					}else if(tag == 0){
						
					}
					if(tag == 0){
						layer.closeAll();
					}
				}
			});
		}
	});

	//新增功能
	$("#add").click(function() {
		layer.open({
			type : 2,
			title : false,
			area:['1000px','100%'],
			closeBtn : false,
			content : ctx + "/produce/qcczAssignReport/openAdd",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				var tag = iframeWin.DoSave();
				if(tag == 0){
					$("#baseTable").bootstrapTable("refresh");
					layer.closeAll();
				}
			}
		});
	})
	
	//查询功能
	$(".search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query : {
				beginTime : encodeURIComponent($("#beginTime").val()),
				endTime : encodeURIComponent($("#endTime").val()),
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
		url: ctx + "/produce/qcczAssignReport/data", 
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
	        	beginTime : encodeURIComponent($("#beginTime").val()),
	        	endTime : encodeURIComponent($("#endTime").val()),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize
	        }
	    },
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.ID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			clickRowId = row.ID;
			layer.open({
				type : 2,
				title : false,
				area:['1000px','100%'],
				closeBtn : false,
				content : ctx + "/produce/qcczAssignReport/openModify?id="+clickRowId,
				btn : [ "保存", "取消" ],
				yes : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					var tag = iframeWin.DoSave();
					if(tag == 0){
						$("#baseTable").bootstrapTable("refresh");
					}else if(tag == 0){
						
					}
					if(tag == 0){
						layer.closeAll();
					}
				}
			});
		},
	    columns: [
			{field: "SEQ",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "ID",title:"ID",sortable:false,editable:false,visible:false,
				formatter:function(value,row,index){
					return row.ID;
				}
			},
			{field: "CREATE_DATE", title: "日期",editable:false,align:'center',halign:'center'},
			{field: "FLIGHT_NUMBER", title: "航班号",editable:false,align:'left',halign:'center'},
			{field: "AIRCRAFT_NUMBER", title: "机号",editable:false,align:'left',halign:'center'},
			{field: "TYPENAME", title: "保障类型",editable:false,align:'left',halign:'center'},
			{field: "NAME", title: "岗位",editable:false,align:'left',halign:'center'},
			{field: "OPERATOR_NAME", title: "操作者",editable:false,align:'left',halign:'center'}
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

