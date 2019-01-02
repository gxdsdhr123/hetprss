<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<style type="text/css">
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
}
</style>
<title>发送者名称</title>
</head>

<body>
<script type="text/javascript">
$(function(){
	var layer;
	var clickRowId = "";
	var mfromtype = '${mfromtype}';
	if(mfromtype ==2 || mfromtype ==8){
		var setting = {
				data : {
					simpleData : {
						enable : true,
						idKey : "id",
						pIdKey : "pId",
						rootPId : '0'
					}
				},
				view:{
					showLine:false
				},
				
			callback : {
					onClick : function(event, treeId, treeNode) {
						var id = treeNode.id == '0' ? '' : treeNode.id;
						var name=treeNode.name;
						var no=treeNode.no;
						var val1= name;
						var val = id;
						var layero = parent.layer.getChildFrame().context;
	                    var length = $(layero).find("iframe").length;
	                    if(length<=1){
	                    parent.aa(val,val1);}
	                    else if(length>1){
	                    var sc = $(layero).find("iframe")[length-2].contentWindow;
						sc.aa(val,val1);}
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.close(index);

					}
				}
			};
			$.getJSON("${ctx}/message/templ/getTree?mfromtype="+mfromtype, function(data) {
				$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
			});


	} else if(mfromtype ==0 || mfromtype ==1){
		layui.use(['form','layer'],function(){
			layer = layui.layer;
		});
		jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
		jQuery.fn.bootstrapTable.columnDefaults.align = "left";
		jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
		//向导内下拉表
		var mfromtype = $('#mfromtype').val();
		var tableOptions = {
				url: ctx+"/message/templ/getSrListData?mfromtype="+mfromtype, //请求后台的URL（*）
				    method: "get",					  				 //请求方式（*）
				    dataType: "json",
					striped: true,									 //是否显示行间隔色
				    cache: false,
				    search : true,
				    searchOnEnterKey:false,
					height : 345,
				    searchText :'',
				    columns: [
						{field: "no", title: "编号",editable:false,visible:false,searchable : false},
						{field: "name", title: "名称",editable:false}
					
				    ],
				    onDblClickRow:onDblClickRow
				};
		function onDblClickRow(row,tr,field){
			
	var val=row.no;
	var val1=row.name;
	var layero = parent.layer.getChildFrame().context;
	var length = $(layero).find("iframe").length;
	if(length<=1){
	parent.aa(val,val1);}
	else if(length>1){
	var sc = $(layero).find("iframe")[length-2].contentWindow;
	sc.aa(val,val1);}
	
	       var index = parent.layer.getFrameIndex(window.name);  
	        parent.layer.close(index); //再执行关闭
		}	
		$("#srTable").bootstrapTable(tableOptions);
	}
	
	document.onkeydown = keydown;
	function keydown(e) {
		var currKey = 0, e = e || event;
		currKey = e.keyCode || e.which || e.charCode;//支持IE、FF   
		if (currKey == 13) {
			return false;
		}
	}

});
</script>

<input type="hidden" name="mfromtype" id="mfromtype" value="${mfromtype}" />
	<c:if test="${mfromtype=='0'||mfromtype=='1'}">
		<form id="createFormSender" class="layui-form">
			<div id="srTables">
				<table id="srTable"></table>
			</div>
		</form>
	</c:if>

	<c:if test="${mfromtype=='2' || mfromtype=='8'}"> 
		<div id="ztree" class="ztree"></div>
	</c:if>
</body>
</html>