layui.use('layer');
$(function(){
	var layer;
	var clickRowId = "";
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var tableOptions = {
	url: ctx+"/telegraph/analysis/autoanalysislist", //请求后台的URL（*）
	    method: "get",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    toolbar:$("#tool-box"),
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    sortName:'ACCEPT_TIME',
	    sortOrder:'desc',
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
	        	flightdate : $("input[name=flightdate]").val(),
	        	flightnumber : $("input[name=flightnumber]").val(),
	        	aircraft : $("input[name=aircraft]").val(),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize,
                sortName:params.sortName,
                sortOrder:params.sortOrder
	        }
	    },
	    columns: [
			{field: "seq",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "ID", title: "ID",editable:false,visible:false},
			{field: "FLIGHT_NUMBER", title: "航班号",editable:false},
			{field: "FLIGHT_DATE", title: "航班日期",editable:false,align:"center"},
			{field: "AIRCRAFT", title: "机号",editable:false	},
			{field: "TYPE", title: "报文类型",editable:false},
			{field: "ACCEPT_TIME", title: "接收时间",editable:false},
			{field: "SEND_TIME", title: "发送时间",editable:false},
			{field: "mj", title: "操作",editable:false,
				formatter:function(value,row,index){
					return "<i class='fa fa-edit' onclick='modefy("+row.ID+")'></i>";
			}}
	    ],
	    onDblClickRow:onDblClickRow
	};
	function onDblClickRow(row,tr,field){
		var id=row.ID;
		modefy(id);
	}

	tableOptions.height = $("#baseTables").height();
	$("#baseTable").bootstrapTable(tableOptions);
	
	$(".analysis").click(function(e){
		iframe = layer.open({
			type: 2, 
			  title:false,
			  closeBtn:false,
			  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
			  content:  ctx+"/telegraph/analysis/analysis",
			  btn:["解析","重置","返回"],
			  yes:function(index, layero){
				  var obj = $(layero).find("iframe")[0].contentWindow.analysis();
			  },btn2 : function (index,layero){
				  $(layero).find("iframe")[0].contentWindow.reset();
			  },btn3 : function (index,layero){
				  layer.closeAll('iframe');
				  refresh();
			  }
			});
		layer.full(iframe);
	});
})
function modefy(id){
	iframe = layer.open({
		type: 2, 
		  title:false,
		  closeBtn:false,
		  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
		  content:  ctx+"/telegraph/analysis/analysis?id="+id,
		  btn:["解析","返回"],
		  yes:function(index, layero){
				var obj = $(layero).find("iframe")[0].contentWindow.analysis();
		  },btn2 : function (index,layero){
			  layer.closeAll('iframe');
			  refresh();
		  }
		});
	layer.full(iframe);
}

function refresh(){
	var options = $('#baseTable').bootstrapTable('refresh', {
		query : {
        	flightdate : $("input[name=flightdate]").val(),
        	flightnumber : $("input[name=flightnumber]").val(),
        	aircraft : $("input[name=aircraft]").val()
		}
	});
}
