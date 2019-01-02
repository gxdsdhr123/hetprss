<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<style type="text/css">
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
	position: relative;
}
.layui-upload-button{
	height:30px;
	line-height:30px;
}
</style>
<script type="text/javascript">

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
				$('#officeContent').attr(
						"src",
						"${ctx}/sys/user/list?office.id=" + id
								+ "&office.name=" + encodeURIComponent(treeNode.name));
			}
		}
	};
	layui.use([ 'laypage', 'form', 'upload' ], function() {
		refreshTree();
		var laypage = layui.laypage;
		var form = layui.form;
		var layer = layui.layer;
		$("#btnExport").click(function() {
			layer.confirm('确认要导出用户数据吗？', {
				  btn: ['是','取消']
				}, function(index, layero){
					var form = $("#searchForm",window.frames["officeContent"].document);
					form.attr("action", "${ctx}/sys/user/export");
					form.submit();
					layer.close(index);
				});
		});
	});
	
	var treeScroll;
	function refreshTree() {
		$.getJSON("${ctx}/sys/office/treeData", function(data) {
			$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
			if(treeScroll){
				treeScroll.update();
			}else{
				treeScroll = new PerfectScrollbar('#ztree');
			}
		});
	}
	
</script>
</head>
<body>
	<sys:message content="${message}" />
	<div class="row">
		<div class="col-md-3 col-xs-3 col-sm-3">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h5 class="box-title">组织机构</h5>
					<div class="box-tools pull-right">
						<button class="btn btn-box-tool" type="button" onclick="refreshTree();">
							<i class="fa fa-refresh" ></i>
                		</button>
					</div>
				</div>
				<div class="box-body">
					<div id="ztree" class="ztree" style="height: 1015px;"></div>
				</div>
			</div>
		</div>
		<div class="col-md-9 col-xs-9 col-sm-9" style="padding-left:0px;">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h5 class="box-title">用户列表</h5>
					<div class="box-tools pull-right">
						<div class="layui-inline">
							<shiro:hasPermission name="sys:user:edit">
								<button type="button" class="btn btn-box-tool" onclick="redirect('${ctx}/sys/user/form')"><i class="fa fa-plus">&nbsp;</i>添加用户</button>
							</shiro:hasPermission>
							<button id="btnExport" type="button" class="btn btn-box-tool"><i class="layui-icon">&#xe601;</i>&nbsp;导出
							</button>
							<%-- <button type="button" class="btn btn-box-tool"
								onclick="redirect('${ctx}/sys/user/import/template')">下载模板</button>
							<input name="file" class="layui-upload-file layui-btn-small" type="file" lay-type="file"/> --%>
						</div>
					</div>
				</div>
				<div class="box-body">
					<iframe id="officeContent" name="officeContent" src="${ctx}/sys/user/list" width="100%"
						height="1015px" frameborder="0" scrolling="no"></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
</html>