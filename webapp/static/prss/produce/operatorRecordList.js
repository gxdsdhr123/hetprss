var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
var clickRow;
var iframe;
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
			$("input[name=bondId]").val(clickRowId);
			$("#printForm").submit();
		}
	});
	// 查看
	$('#query').click(function() {
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要查看的数据', {
				icon : 2
			});
		}else{
			var id = clickRowId;
				iframe = layer.open({
					type : 2,
					title : false,
					area:['100%','100%'],
					closeBtn : false,
					content : ctx + "/produce/operator/from?id=" + id + "&reskind=" + $("#reskind").val(),
					btn : [  "取消" ]
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
		url: ctx + "/produce/operator/data", 
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
	        	operatorName : encodeURIComponent($("input[name=operator_name]").val()),
	        	innerNumber : encodeURIComponent($("input[name=inner_number]").val()),
	        	beginTime : $("input[name=beginTime]").val(),
	        	endTime : $("input[name=endTime]").val(),
	        	reskind : $("#reskind").val(),
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
			{field: "OPER_DATE", title: "日期",editable:false,align:'center',halign:'center'},
			{field: "NAME", title: "人员",editable:false,align:'left',halign:'center'},
			{field: "VEHICLE_NUMBER", title: "车号",editable:false,align:'left',halign:'center'}
//			,{field: "CREATE_DATE", title: "创建时间",editable:false,align:'left',halign:'center'}
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});

