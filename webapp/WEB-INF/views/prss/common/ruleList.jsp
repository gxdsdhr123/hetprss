<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<title>复制规则</title>
</head>
<body>
<script type="text/javascript">
$(function(){
	var layer;
	var clickRowId = "";
	layui.use(['layer'],function(){
		layer = layui.layer;
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "left";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var tableOptions = {
		url: ctx+"/param/common/getRuleList", //请求后台的URL（*）
	    method: "get",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    search:true,
		height : 355,
	    columns: [
			{field: "ID", title: "编号",editable:false,visible:false},
			{field: "NAME", title: "名称",editable:false,width:120},
			{field: "TEXT",title: "规则内容",editable:false}
	    ],
	    onDblClickRow : onDblClickRow
	};
	$("#varTable").bootstrapTable(tableOptions);
});
function onDblClickRow(row,tr,field){
	var id=row.ID;
	var layero = parent.layer.getChildFrame().context;
	var length = $(layero).find("iframe").length;
	var win = $(layero).find("iframe")[length-2].contentWindow;
	win.setRule(id);
    var index = parent.layer.getFrameIndex(window.name);  
    parent.layer.close(index); 
}	
</script>
	<div id="varTables">
		<table id="varTable"></table>
	</div>
</body>
</html>