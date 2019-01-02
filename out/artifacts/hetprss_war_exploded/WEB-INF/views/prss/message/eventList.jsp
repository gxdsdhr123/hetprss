<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<title>关联事件</title>
</head>
<body>
<script type="text/javascript">
var type = '${type}';
$(function(){
	var layer;
	var clickRowId = "";

	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	
	var tableOptionsevent = {
			url: ctx+"/message/templ/searchEventList?type=" + type, //请求后台的URL（*）
			    method: "get",					  				 //请求方式（*）
			    dataType: "json",
				striped: true,									 //是否显示行间隔色
			    cache: true,
			    columns: [
					{field: "no", title: "编号",editable:false},
					{field: "name" ,title: "事件名称",editable:false}
			    ],
	      onDblClickRow:onDblClickRowEvent
	};	
	function onDblClickRowEvent(row, tr, field) {
		
		var name=row.name;
		var no=row.no;
		 parent.getEventValue(name,no);
	     var index = parent.layer.getFrameIndex(window.name);  
	     parent.layer.close(index); //再执行关闭
	}
	tableOptionsevent.height = $("#eventListTables").height();
	$("#eventListTable").bootstrapTable(tableOptionsevent);
});
</script>
	<div id="eventListTables">
		<table id="eventListTable"></table>
	</div>
</body>
</html>