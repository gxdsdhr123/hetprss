<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<title>选择参数变量</title>
</head>
<body>
<script type="text/javascript">
var schema = '${schema}';
$(function(){
	var layer;
	layui.use(['layer'],function(){
		layer = layui.layer;
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var tableOptions = {
		url: ctx+"/param/common/getVariableData?schema="+$("#schema").val(), //请求后台的URL（*）
	    method: "get",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    search:true,
		undefinedText : '', // undefined时显示文本
		height : 355,
	    columns: [
			{field: "no", title: "编号",editable:false,visible:false},
			{field: "name", title: "名称",editable:false},
			{field: "ischg",title: "选择",sortable:false,editable:false,visible : schema=='88'?true:false,
				formatter: function (value, row, index) {
					if(row.ischg =="1"){
			//return "<select><option>请选择</option></select>";
						return "<select><option>请选择</option>"+
						"<option value='0'>原值</option>"+
						"<option value='1'>旧值</option>"+
						"<option value='2'>新值</option></select>";
					}
	             }
			}
	    ],
	    onDblClickRow : onDblClickRow
	};	
	$("#varTable").bootstrapTable(tableOptions);
});
function onDblClickRow(row,tr,field){
	//向导拼接
	var no=row.no;
	var name=row.name;
	var ischg=row.ischg;
	var valselect = $(tr).find("select").val();
	if(schema == 'MM' || schema=='88'){
		if(ischg=="1"){
			if(valselect=="请选择"){
				layer.msg("请选择一条赋值信息",{time:1000});
				return false;
			}
		}
	}
    parent.setParams(name,no,ischg,valselect);
    var index = parent.layer.getFrameIndex(window.name);  
    parent.layer.close(index); //再执行关闭
}
</script>
	<input type="hidden" id="schema" name="schema" value="${schema }"/>
	<div id="varTables">
		<table id="varTable"></table>
	</div>
</body>
</html>