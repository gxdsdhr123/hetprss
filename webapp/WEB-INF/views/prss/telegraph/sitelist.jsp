<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>发送者名称</title>
</head>

<body>
<script type="text/javascript">

layui.use("form", function() {
	var form = layui.form;
	form.on('select(subFilter)', function(data) {
		$('#torangenames').val("");
		if (data.value == "3") {
			$('#torangenames').val("sys");
		}
		if(data.value=="0"){
			$(".user_ifsms").removeAttr("hidden");
		} else {
			$(".user_ifsms").attr("hidden","hidden");
		}
	});
})

$(function(){
	var layer;
	var clickRowId = "";

	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	//向导内下拉表
	var sitaType = $('#sitaType').val();
	var ids = $("#ids").val();
	var tableOptions = {
			url: ctx+"/telegraph/templ/getListData?sitaType="+sitaType + "&ids=" + ids, //请求后台的URL（*）
			    method: "get",					  				 //请求方式（*）
			    dataType: "json",
				striped: true,									 //是否显示行间隔色
			    cache: true,
			    search:true,
				height : 345,
			    columns: [
					{field: "NO", title: "编号",editable:false,visible:false},
					{field: "NAME", title: "航空公司",editable:false,width:'10'},
					{field: "DES", title: "描述",editable:false},
					{field: "TG_ADDRESS", title: "地址",editable:false}
				
			    ],
			    onDblClickRow:onDblClickRow
			};
	function onDblClickRow(row,tr,field){
        parent.setValue(row);
        var index = parent.layer.getFrameIndex(window.name);  
        parent.layer.close(index); //再执行关闭
	}	
	$("#srTable").bootstrapTable(tableOptions);

});
</script>

<input type="hidden" name="sitaType" id="sitaType" value="${sitaType}" />
<input type="hidden" name="ids" id="ids" value="${ids }" >
<form id="createFormSender" class="layui-form">
	<div id="srTables">
		<table id="srTable"></table>
	</div>
</form>
</body>
</html>