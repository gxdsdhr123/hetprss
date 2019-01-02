<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp" %>
	<style type="text/css">
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}
	</style>
	<script type="text/javascript">
		layui.use(["layer"]);
		var checkedId;
		
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
					var id = treeNode.pId == '0' ? '' : treeNode.pId;
					checkedId = treeNode.id;
					$('#officeContent').attr(
							"src",
							"${ctx}/sys/office/list?id=" + id + "&parentIds="
									+ treeNode.pIds);
				}
			}
		};
		function addBtnClick() {
			redirect(ctx + '/sys/office/form?parent.id=' + checkedId);
		}
		function refreshTree() {
			$.getJSON("${ctx}/sys/office/treeData", function(data) {
				$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
			});
		}
		refreshTree();
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
					<div id="ztree" class="ztree" style="height: 800px;"></div>
				</div>
			</div>
		</div>
		<div class="col-md-9 col-xs-9 col-sm-9" style="padding-left:0px;">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h5 class="box-title">机构列表</h5>
					<div class="box-tools pull-right">
						<div class="layui-inline">
							<shiro:hasPermission name="sys:office:edit">
								<button type="button" class="layui-btn layui-btn-small layui-btn-primary" onclick="addBtnClick()"><i class="fa fa-plus">&nbsp;</i>添加机构</button>
							</shiro:hasPermission>
						</div>
					</div>
				</div>
				<div class="box-body">
					<iframe id="officeContent" name="officeContent" src="${ctx}/sys/office/list?id=&parentIds=" width="100%"
						height="795px" frameborder="0"></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
</html>