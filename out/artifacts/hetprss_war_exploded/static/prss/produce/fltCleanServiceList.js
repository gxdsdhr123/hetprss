var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var win = null;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	new PerfectScrollbar('#container');
	
	//初始选择日期
	$("#startDate").val(getFormatDate(-3));
	$("#endDate").val(getFormatDate(0));
	
	//下载单据
	$("#down").click(function(){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		$("#exportForm").attr("action",ctx + "/produce/fltCleanService/exportExcel?startDate="+startDate+"&endDate="+endDate);
		$("#exportForm").submit();
	});
	//删除单据
	$("#delete").click(function(){
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			var loading = layer.load(2, {shade : [ 0.3, '#000' ]});
			$.ajax({
		        type: "POST",
		        url: ctx + '/produce/fltCleanService/delete',
		        data: {
		        	"id":clickRowId
		        },
		        success: function(data){
		        	//将航班相关详细信息自动填入、选中
		        	if(data=="succeed"){
		        		layer.msg("删除成功！", {icon: 1,time:3000});
		        		$("#baseTable").bootstrapTable("refresh");
		        		clickRowId = null;
		        	}else{
		        		layer.alert("删除失败！", {icon: 2,time:3000});
		        	}
		        	layer.close(loading);
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
			win = layer.open({
				type : 2,
				title : false,
				area:['610px','460px'],
				closeBtn : false,
				content : ctx + "/produce/fltCleanService/toForm?operateType=modify&id="+clickRowId,
				btn : [ "保存", "取消" ],
				yes : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.doSave();
					clickRowId = null;
					return false;
				}
			});
		}
	});

	//新增功能
	$("#add").click(function() {
		win = layer.open({
			type : 2,
			title : false,
			area:['610px','460px'],
			closeBtn : false,
			content : ctx + "/produce/fltCleanService/toForm?operateType=add&id=",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSave();
				clickRowId = null;
				return false;
			}
		});
	})
	
	//查询按钮
	$("#search").click(function() {
		var options = $('#baseTable').bootstrapTable('refresh', {
			query :  function (params) {
				return {
					startDate : $("#startDate").val(),
		        	endDate : $("#endDate").val(),
		        	pageNumber: params.offset+1,    
	                pageSize: params.limit
				}
			}
        });
	})
	
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/produce/fltCleanService/getData", 
	    method: "get",
	    dataType: "json",
	    align:"center",
		striped: true,
	    cache: true,
	    sortable: false,
	    undefinedText:'',
	    checkboxHeader:false,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 20,
	    pageList: [20,50,100],
	    paginationPreText: "上一页",  
		paginationNextText: "下一页",
	    queryParams: function (params) {
	        return {
	        	startDate : $("#startDate").val(),
	        	endDate : $("#endDate").val(),
	        	pageNumber: params.offset+1,    
                pageSize: params.limit
	        }
	    },
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			clickRowId = row.id;
			win = layer.open({
				type : 2,
				title : false,
				area:['610px','460px'],
				closeBtn : false,
				content : ctx + "/produce/fltCleanService/toForm?operateType=modify&id="+clickRowId,
				btn : [ "保存", "取消" ],
				yes : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.doSave();
					clickRowId = null;
					return false;
				}
			});
		},
	    columns: [
			{field: "SEQ",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "id",title:"id",sortable:false,editable:false,visible:false,
				formatter:function(value,row,index){
					return row.id;
				}
			},
			{field: "flightDate", title: "日期",editable:false},
			{field: "flightNumber", title: "航班号",editable:false},
			{field: "departAptCnname", title: "起场",editable:false},
			{field: "acttypeCode", title: "机型",editable:false},
			{field: "aircraftNumber", title: "机号",editable:false},
			{field: "ataStr", title: "进港实落",editable:false},
			{field: "operatorName", title: "清洁人员",editable:false},
			{	
				field : "PICTURE",
				title : '乘务长签字',
				editable:false,
				align : 'left',
				halign:'center',
				formatter:function(value,row,index){//模仿这个写就行
				var s = '<div style="width:200;height:30px;"></div>';
				if(value!=null && value !=""){
						s = '<img style="width:200;height:30px;" src="data:image/png;base64,'+value+'" />';
					}
		        	return s;
				}
			},
			{field: "remark", title: "备注",editable:false}
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

/*
 * 获取日期
 */
function getFormatDate(days){
	var date = new Date();
	date.setDate(date.getDate() + days);
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (date.getMonth()+1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	return year+""+month+""+day; 
} 

function refreshBaseTable(){
	layer.close(win);
	$("#baseTable").bootstrapTable("refresh");
}
