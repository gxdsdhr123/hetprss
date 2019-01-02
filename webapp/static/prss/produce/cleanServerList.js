var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})

	$(".select2").select2({
	    placeholder: "请选择",
	    allowClear: true,
	    width:"100%"
	});
	
	//下载单据
	$("#down").click(function(){
		var row = $('#baseTable').bootstrapTable('getSelections');
		console.info(row);
		var ids="";
		for (var i = 0; i < row.length; i++) {
			if (row[i].ID!=""&&row[i].ID!=null) {
				ids+=row[i].ID+",";
			}
		}
		if(ids==''){
			layer.msg('请勾选要下载的数据', {
				icon : 2
			});
		}else{
			$("input[name=id]").val(ids);
			$("#printForm").submit();
		}
	});
	//删除单据
	$("#delete").click(function(){
		var row = $('#baseTable').bootstrapTable('getSelections');
		var ids="";
		for (var i = 0; i < row.length; i++) {
			if (row[i].ID!=""&&row[i].ID!=null) {
				ids+=row[i].ID+",";
			}
		}
		if(ids==''){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			$.ajax({
		        type: "POST",
		        url: ctx + '/produce/cleanServerBill/delete',
		        data: {
		        	"id":ids.substring(0,ids.length-1)
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
				area:['1000px','530px'],
				closeBtn : false,
				content : ctx + "/produce/cleanServerBill/openModify?id="+clickRowId,
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
			area:['1000px','530px'],
			closeBtn : false,
			content : ctx + "/produce/cleanServerBill/openAdd",
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
				fltDate : $("#fltDate").val(),
	        	fltNum : $("#fltNum").val(),
	        	operator : $("#operator").val(),
				pageNumber: 1,    
                pageSize: 10
			}
		});
	})
	
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/produce/cleanServerBill/data", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:true,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
	        	fltDate : $("#fltDate").val(),
	        	fltNum : $("#fltNum").val(),
	        	operator : $("#operator").val(),
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
				area:['1000px','530px'],
				closeBtn : false,
				content : ctx + "/produce/cleanServerBill/openModify?id="+clickRowId,
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
			{field : "checkbox",
				checkbox: true,
				sortable : false,
				editable : false
			},
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
			{field: "FLIGHT_DATE", title: "日期",editable:false,align:'center',halign:'center'},
			{field: "FLIGHT_NUMBER", title: "航班号",editable:false,align:'center',halign:'center'},
			{field: "JOB_TYPE", title: "作业类型",editable:false,align:'center',halign:'center'},
			{field: "OPERATOR", title: "操作者",editable:false,align:'center',halign:'center'}
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});

