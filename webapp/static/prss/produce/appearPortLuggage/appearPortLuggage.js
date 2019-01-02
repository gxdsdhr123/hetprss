var baseTable;
var id = "";
$(function(){

	var layer;
	var clickRowId = "";	//当前单选行Id，
	baseTable = $("#baseTable");
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	//基本表格-列的全局默认设置
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {
		toggle : 'dblclick'
	};
	
	//表格的基本选项
	var tableOptions = {
			url : ctx + "/appear/produce/list", // 请求后台的URL（*）
			method : "get", // 请求方式（*）
			dataType : "json", // 返回结果格式
			striped : true, // 是否显示行间隔色
			cache: true,
			undefinedText:'',
		    checkboxHeader:false,
		    toolbar:$("#tool-box"),
		    search :false,
		    pagination: true,
		    sidePagination: 'server',
		    pageNumber: 1,
		    pageSize: 10,
		    sortName:'ID',
		    sortOrder:'DESC',
		    pageList: [10, 15, 20],
		    queryParamsType:'',
		    queryParams:function(params){
		    	return {
		    		pageNumber:params.pageNumber,
		    		pageSize: params.pageSize,
		    		sortName:params.sortName,
		    		sortOrder:params.sortOrder
		    	}
		    },
		    onClickRow:onClickRow,
		    columns:[
		             
				{field: "seq",title:"序号",sortable:false,editable:false,
					formatter:function(value,row,index){
						return index+1;
					}
				},
				{field: "ID", title: "主键",editable:false,visible:false},
				{field: "FLIGHT_DATE", title: "日期",editable:false},
				{field: "FLIGHT_NUMBER", title: "航班号",editable:false},
				{field: "AIRCRAFT_NUMBER", title: "机号",editable:false},
				{field: "CHALI", title: "查理",editable:false},
				{field: "CREATE_DATE", title: "创建时间",editable:false},
		             
				],
	};
	$("#baseTable").bootstrapTable(tableOptions);
	
	//BS单击事件
	function onClickRow(row,tr,field){
		id = row.ID;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
	
	$("#create").click(function(){
		newly();
	});
	
	$("#modify").click(function(){
		modification();
	});
	
	$("#remove").click(function(){
		expurgate();
	});
	
	
	
});

function newly(){
	iframe = layer.open({
		type : 2,
		title : false,
		closeBtn : false,
		content : ctx + "/appear/produce/newly",
		btn : [ "保存","重置", "返回" ],
		yes:function(index, layero){
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.save();
		},
		btn2:function(index, layero){
			layer.getChildFrame("#createForm", index)[0].reset();
			var refBtn = layer.getChildFrame("#refreshDetailTable", index);
			refBtn.click();
			return false;
		},
		btn3:function(index, layero){
			layer.close(index);
			return false;
		}
	});
	layer.full(iframe);// 展开弹出层直接全屏显示
}

function modification(){
	if(id==""){
		layer.msg('请选择一行出港行李交接单',{icon:0});
		return false;
	}
	iframe = layer.open({
		type:2,
		title : false,
		closeBtn : false,
		content : ctx + "/appear/produce/revamp?id="+id,
		btn : [ "保存","重置", "返回" ],
		yes:function(index, layero){
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.revamp();
		},
		btn2:function(index, layero){
			//layer.getChildFrame("#createForm", index)[0].reset();
			var refBtn = layer.getChildFrame("#refreshDetailTable", index);
			refBtn.click();
			return false;
		},
		btn3:function(index, layero){
			layer.close(index);
			return false;
		}
	});
	layer.full(iframe);// 展开弹出层直接全屏显示
}

function expurgate(){
	if(id==""){
		layer.msg('请选择一行出港行李交接单',{icon : 0});
		return false;
	}
	
	layer.confirm('是否删除?',{btn:['确定','取消'],icon: 3},function(){
		$.ajax({
			url: ctx+"/appear/produce/DelRevamp",
			type:'post',
			data:{id:id},
			success:function(result){
				if(result!=0){
					layer.msg("成功",{icon: 1,time: 1000},function(){
						$('#baseTable').bootstrapTable('refresh');
						return false;
					});
				}
			}
			
		});
	});
}












