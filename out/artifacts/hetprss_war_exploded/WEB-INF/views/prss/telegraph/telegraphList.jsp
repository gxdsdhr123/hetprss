<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>自动发送模板列表</title>
</head>

<body>

<script type="text/javascript">
	var eventId= '${eventId}';
	var airline_code= '${airline_code}';
	
	$(function(){
		var layer;
		var clickRowId = "";

		layui.use(['form','layer'],function(){
			layer = layui.layer;
		});
		jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
		jQuery.fn.bootstrapTable.columnDefaults.align = "left";
		jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
		//向导内下拉表
		var tableOptions = {
				url: ctx+"/telegraph/auto/getTelegraphTempl", //请求后台的URL（*）
				    method: "get",					  				 //请求方式（*）
				    dataType: "json",
					striped: true,									 //是否显示行间隔色
				    cache: false,
				    search:true,
				    height:345,
				    queryParams: function (params) {
				        return {
				        	eventId : eventId,
			                airline_code : airline_code
				        }
				    },
				    columns: [
						{field: "ID", title: "编号",editable:false,visible:false},
						{field: "TG_NAME", title: "模板名称",editable:false,visible:false},
						{field: "NAME", title: "模板名称",editable:false,formatter:function(value,row,index){
							return row.ALN_2CODE + '/' + row.DESCRIPTION_CN + '【' + row.TG_NAME+'】';
							}
						},
						{field: "TG_TEXT", title: "消息正文",editable:false,visible:false},
						{field: "VARCOLS", title: "VARCOLS",editable:false,visible:false}
					
				    ],
				    onDblClickRow:onDblClickRow
				};
		function onDblClickRow(row,tr,field){
			
			var no=row.ID;
			var name=row.TG_NAME;
			var mtext = row.TG_TEXT;
			var varcols = row.VARCOLS;
			if(mtext==null) mtext='';
			
			$.ajax({
				type:'post',
				data:{
					'varcols': varcols,
					'mtext' : mtext,
					'schema' : '99'
				},
				async:false,
				url:ctx+"/param/common/transtext",
				dataType:"text",
				success:function(result){
					if(result==null) result='';
			        parent.setValue(no,name,result,varcols);
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

	});
</script>
	<form id="createFormSender" class="layui-form">
		<div id="mTables">
			<table id="mTable"></table>
		</div>
	</form>
</body>
</html>