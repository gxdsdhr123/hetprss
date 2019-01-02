$(function(){
	var layer;
	var clickRowId = "";
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "left";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var tableOptions = {
	url: ctx+"/flightDynamic/fdTelegraphList", //请求后台的URL（*）
	    method: "get",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
//	    toolbar:$("#tool-box"),
	    sortName:"CREATETIME",
	    sortOrder:"desc",
	    search:false,
		searchOnEnterKey : true,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
	        	search : $("#fltNo").val(),
	        	beginTime : $("#beginTime").val(),
	        	endTime : $("#endTime").val(),
	        	isHis : $("#isHis").val(),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize,
                sortName:params.sortName,
                sortOrder:params.sortOrder
	        }
	    },
	    columns: [
			{field: "id",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "FLIGHT_DATE", title: "航班日期",editable:false},
			{field: "TEL_TYPE", title: "报文类型",editable:false},
			{field: "FLIGHT_NUMBER", title: "航班号",editable:false},
			{field: "DTTM", title: "报文时间",editable:false},
			{field: "ACCEPT_TIME", title: "接受时间",editable:false}
	    ],
	    onDblClickRow:onDblClickRow,
	    onClickRow : onClickRow
	};
	function onDblClickRow(row,tr,field){
		var id=row.ID;
		info(id);
	}
	//单击事件
	function onClickRow(row, tr, field) {// 记录单选行id，并赋予天蓝色底纹
		clickRowId = row.ID;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
	$("#baseTable").bootstrapTable(tableOptions);
	$("#info").click(function(){
		if(clickRowId == ''){
			layer.msg("请选择一条记录",{icon:0,time:600})
			return;
		}
		info(clickRowId)
	})
});	

function info(id){
	iframe = layer.open({
		type: 2, 
		  title:false,
		  closeBtn:false,
		  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
		  content:  ctx+"/flightDynamic/getTelegraphInfo?id=" + id + "&isHis="+$("#isHis").val(),
		  btn:["返回"]
		});
	layer.full(iframe);
}

//查询功能
$(".search").click(function() {
	var options = $('#baseTable').bootstrapTable('refresh', {
		query : {
        	beginTime : $("#beginTime").val(),
        	endTime : $("#endTime").val(),
			search : $("#fltNo").val()
		}
	});
})
