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

		layui.use(['form','layer'],function(){
			layer = layui.layer;
		});
		jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
		jQuery.fn.bootstrapTable.columnDefaults.align = "center";
		jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
		//向导内下拉表
		var tableOptions = {
				url: ctx+"/message/templ/getmMessageData?mftype="+$("#mftype").val(), //请求后台的URL（*）
				    method: "get",					  				 //请求方式（*）
				    dataType: "json",
					striped: true,									 //是否显示行间隔色
				    cache: false,
				    search:true,
					height : 345,
				    searchText:'',
				    columns: [
						{field: "NO", title: "编号",editable:false,visible:false,searchable:false},
						{field: "NAME", title: "模板名称",editable:false},
						{field: "MTITLE", title: "消息标题",editable:false},
						{field: "MTEXT", title: "消息正文",editable:false,visible:false,searchable:false},
						{field: "MFTYPE", title: "MFTYPE",editable:false,visible:false,searchable:false},
						{field: "VARCOLS", title: "参数",editable:false,visible:false,searchable:false}
				    ],
				    onDblClickRow:onDblClickRow
				};
		function onDblClickRow(row,tr,field){
			var no=row.NO;
			var mtitle=row.MTITLE;
			var mtext = row.MTEXT;
			var varcols = row.VARCOLS;
			var mftype = row.MFTYPE;
			$.ajax({
				type:'post',
				data:{
					'varcols': varcols,
					'mtext' : mtext,
					'schema' : '88'
				},
				async:false,
				url:ctx+"/param/common/transtext",
				dataType:"text",
				success:function(result){
					if(result==null) result='';
					var layero = parent.layer.getChildFrame().context;
	                var length = $(layero).find("iframe").length;
	                if(length<=1){
	                 parent.setParam(mtitle,no,result,varcols,mftype);}
	                else if(length>1){
	                var sc = $(layero).find("iframe")[length-2].contentWindow;
	                sc.setParam(mtitle,no,result,varcols,mftype);}
	                
			        var index = parent.layer.getFrameIndex(window.name);  
			        parent.layer.close(index); //再执行关闭
				},
				error:function(msg){
					var result = "操作失败";
					layer.msg(result, {icon: 1,time: 1000});
				}
			});
		}	

		$("#mTable").bootstrapTable(tableOptions);

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

	<form id="createFormSender" class="layui-form">
		<input name="mftype" id="mftype" value="${mftype }" type="hidden"/>
		<div id="mTables">
			<table id="mTable"></table>
		</div>
	</form>

</body>
</html>