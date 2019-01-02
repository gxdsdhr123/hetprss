var layer;// 初始化layer模块
var baseTable;// 基础表格
var scrollbar;
var clickRowId = "";
var setWin = null;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var leftTableOptions = {
		url: ctx + "/stand/parkingMutex/dataList", 
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
	    sortName:'ID',
	    sortOrder:'ASC',
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	    	return {pageNumber: params.pageNumber,
	    		   pageSize: params.pageSize,
	    		   sortName:params.sortName,
	               sortOrder:params.sortOrder}
	    },
	    columns: [
	        {field: "ID", title: "ID",align:'left',halign:'center',editable:false,visible:false},
	        {field: "NUM", title: "序号",align:'left',halign:'center',editable:false},
			{field: "ACTSTAND_CODE1", title: "机位1",align:'left',halign:'center',editable:false},
			{field: "AIRCRAFT_TYPE1", title: "机型1",align:'left',halign:'center',editable:false},
			{field: "ACTSTAND_CODE2", title: "机位2",align:'left',halign:'center',editable:false},
			{field: "AIRCRAFT_TYPE2", title: "机型2",align:'left',halign:'center',editable:false},
			{field: "TIME_MIN", title: "占用时间（分钟）",align:'left',halign:'center',editable:false}
	    ],
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.ID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		}
	};

	leftTableOptions.height = $("#baseTable").height();// 表格适应页面高度
	baseTable.bootstrapTable(leftTableOptions);// 加载基本表格
	
	
	$("#addBtn").click(function() {	
		setWin = layer.open({
			type : 2,
			title : "新增",
			closeBtn : false,
			area : ['800px','500px'],
			content : ctx + "/stand/parkingMutex/addMutex",
			btn : [ "保存", "取消" ],
			
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveForm();
				return false;
			}
		})
	});
	
	$("#editBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要修改的数据", {
				icon : 7
			});
			return false;
		}
		setWin = layer.open({
			type : 2,
			title : "修改",
			closeBtn : false,
			area : ['800px','500px'],
			content : ctx + "/stand/parkingMutex/editMutex?id="+clickRowId,
			btn : [ "保存", "取消" ],
			
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.saveForm();
				return false;
			}
		})
	});
	
	// 删除
	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的行", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中数据？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				type : 'post',
				url : ctx + "/stand/parkingMutex/delMutex",
				data : {
					'id':clickRowId
				},
				dataType : "json",
				error : function() {
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(result) {
					if(result.code=="0000"){
						layer.msg(result.msg,{icon:1},function(){$("#baseTable").bootstrapTable('refresh');});
					}else{
						layer.msg(result.msg,{icon:2});
					}
				}
			});
		});
	});
});

function saveSuccess(){
	layer.close(setWin);
	$("#baseTable").bootstrapTable('refresh');
}





