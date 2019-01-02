var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var clickRowInfo = "";// 当前选中行的航班号
var win = null;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	new PerfectScrollbar('#container');
	
	//初始选择日期
	$("#fltDate").val(getFormatDate(0));
	
	//打印
	$("#print").click(function(){
		//获取选中特车计费单
		var ids = $.map($("#baseTable").bootstrapTable('getSelections'), function (row) {
	        return row.id;
	    });
		//所选的航班日期
		var fltDate = $("#fltDate").val();
		if(ids==null||ids==""){
			layer.msg('请选择要打印的数据', {
				icon : 2
			});
			return false;
		}
		if(fltDate==null||fltDate==""){
			layer.msg('请选择航班日期', {
				icon : 2
			});
			return false;
		}
		$("#exportForm").attr("action",ctx + "/produce/billTcFw/printExcel?ids="+ids+"&fltDate="+fltDate);
		$("#exportForm").submit();
	});
	
	//导出
	$("#export").click(function(){
		var fltDate = $("#fltDate").val();
		var fltNo = $("#fltNo").val();
		$("#exportForm").attr("action",ctx + "/produce/billTcFw/exportExcel?fltDate="+fltDate+"&fltNo="+fltNo);
		$("#exportForm").submit();
	});
	
	//删除
	$("#delete").click(function(){
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			var fltDate = $("#fltDate").val();
			layer.confirm("确定删除"+clickRowInfo+"的特车计费单吗？", {btn: ['确定', '取消']}, function () {
				var loading = layer.load(2, {shade : [ 0.3, '#000' ]});
				$.ajax({
			        type: "POST",
			        url: ctx + '/produce/billTcFw/delete',
			        data: {
			        	"id":clickRowId,
			        	"fltDate":fltDate
			        },
			        success: function(data){
			        	layer.close(loading);
			        	if(data=="success"){
			        		layer.msg("删除特车计费单成功！", {icon: 1,time:3000});
			        		$("#baseTable").bootstrapTable("refresh");
			        	}else if(data=="nonDel"){
			        		layer.alert("没有特车计费单被删除！", {icon: 1,time:3000});
			        	}else{
			        		layer.alert("删除特车计费单出现问题，请联系管理员！", {icon: 2,time:3000});
			        	}
			        	clickRowId = null;
			        	clickRowInfo = null;
			        }
			    });
			});
		}
	});

	//新增
	$("#add").click(function() {
		win = layer.open({
			type : 2,
			title : "新增特车计费详情",
			area:['990px','510px'],
			closeBtn : false,
			content : ctx + "/produce/billTcFw/toDetailForm?operateType=add",
			btn : [ "确定", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSave();
				clickRowId = null;
				clickRowInfo = null;
				return false;
			}
		});
	});
	
	//查看
	$("#detail").click(function() {
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要查看的数据', {
				icon : 2
			});
		}else{
			//所选的航班日期
			var fltDate = $("#fltDate").val();
			var detailWin = layer.open({
				type : 2,
				title : "特车计费详情",
				area:['990px','510px'],
				closeBtn : false,
				content : ctx + "/produce/billTcFw/toDetailForm?operateType=modify&id="+clickRowId+"&fltDate="+fltDate,
				btn : [ "打印", "确定", "取消" ],
				btn1 : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.printExcel();
					clickRowId = null;
					clickRowInfo = null;
					return false;
				},
				btn2 : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.doSave();
					clickRowId = null;
					clickRowInfo = null;
					return false;
				}
			});
		}
	});
	
	//查询按钮
	$("#search").click(function() {
		$('#baseTable').bootstrapTable('refresh', {
			query :  function (params) {
				return {
					searchValue : $("#fltNo").val(),
		        	fltDate : $("#fltDate").val(),
		        	pageNumber: params.offset+1, 
	                pageSize: params.limit
				}
			}
        });
		clickRowId = null;
	});
	
	//回车搜索
	var searchInput =$("#fltNo");    
		searchInput.bind('keydown', function(event) {
		if (event.keyCode == "13") {
			$('#baseTable').bootstrapTable('refresh', {
				query :  function (params) {
					return {
						searchValue : $("#fltNo").val(),
			        	fltDate : $("#fltDate").val(),
			        	pageNumber: params.offset+1, 
		                pageSize: params.limit
					}
				}
	        });
		}
	});
	
	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/produce/billTcFw/getData", 
	    method: "get",
	    dataType: "json",
	    align:"center",
		striped: true,
	    cache: true,
	    sortable: false,
	    undefinedText:'',
	    checkboxHeader:true,
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
	        	searchValue : $("#fltNo").val(),
	        	fltDate : $("#fltDate").val(),
	        	pageNumber: params.offset+1,    
                pageSize: params.limit
	        }
	    },
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
			clickRowInfo = "日期："+row.flightDate+",航班号："+row.flightNumber;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			clickRowId = row.id;
			//所选的航班日期
			var fltDate = $("#fltDate").val();
			var detailWin = layer.open({
				type : 2,
				title : "特车计费详情",
				area:['990px','510px'],
				closeBtn : false,
				content : ctx + "/produce/billTcFw/toDetailForm?operateType=modify&id="+clickRowId+"&fltDate="+fltDate,
				btn : [ "打印", "确定", "取消" ],
				btn1 : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.printExcel();
					clickRowId = null;
					clickRowInfo = null;
					return false;
				},
				btn2 : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.doSave();
					clickRowId = null;
					clickRowInfo = null;
					return false;
				}
			});
		},
	    columns: [{
			checkbox : true, // 显示一个勾选框
			align : 'center'
			},
			{field: "SEQ",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "id",title:"id",sortable:false,editable:false,visible:false},
			{field: "serviceNumber",title:"服务单号",sortable:false,editable:false,visible:false},
			{field: "inFltId",title:"inFltId",sortable:false,editable:false,visible:false},
			{field: "outFltId",title:"outFltId",sortable:false,editable:false,visible:false},
			{field: "airlineShortName", title: "航空公司",editable:false},
			{field: "aircraftNumber", title: "机号",editable:false},
			{field: "flightDate", title: "航班日期",editable:false},
			{field: "flightNumber", title: "航班号",editable:false},
			{field: "acttypeCode", title: "机型",editable:false},
			{field: "qianyin", title: "牵引车(次)",editable:false},
			{field: "bdc", title: "摆渡车(次)",editable:false},
			{field: "ktc", title: "客梯车(小时)",editable:false},
			{field: "ptc", title: "平台车(分钟)",editable:false},
			{field: "qingshui", title: "清水车(次)",editable:false},
			{field: "wushui", title: "污水车(次)",editable:false},
			{field: "laji", title: "垃圾车(次)",editable:false},
			{field: "kongtiao", title: "空调车(小时)",editable:false},
			{field: "qiyuan", title: "气源车(小时)",editable:false},
			{field: "dianyuan", title: "电源车(小时)",editable:false},
			{field: "cansheng", title: "残登车(次)",editable:false},
			{field: "chubing", title: "除冰车(小时)",editable:false},
			{field: "chuansong", title: "传送带车(小时)",editable:false}
	    ]
	};
//	tableOptions.height = $(window).height()-100;// 表格适应页面高度
	tableOptions.height = $("#baseTable").height();// 表格适应页面高度
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
