<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>绑定航班</title>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<style type="text/css">
.list-group-item{
	-moz-user-select: -moz-none;
   	-khtml-user-select: none;
   	-webkit-user-select: none;
   	-ms-user-select: none;
   	user-select: none;
</style>
<script type="text/javascript"
	src="${ctxStatic}/prss/aptitude/aptitudeTree.js"></script>
<script type="text/javascript">
 	var treeData = ${treeData};
 	var sTree = ${stree};
</script>	
</head>
<body>
	<div class="content">
		<input type="hidden" name="day" id="day" value="${day}">
		<form:form id="createForm" class="layui-form"
			modelAttribute="cgEntity" enctype="multipart/form-data" method="post">
			<div class="row">
				<div role="tabpanel" class="tab-pane" id="field-choose"
					aria-labelledby="field-choose-tab">
					<div id="leftDiv" class="col-xs-5">
						<div id="leftTitle">
							<label for="keyword" class="control-label">全部机位</label> 
						</div>
						<div  class="sortable" style="height: 155px; overflow: auto">
						<ul id="aztree" class="ztree" style="height: 100px;"></ul>
						</div>
					</div>
					<div id="middleDiv" class="col-xs-2" style="margin-top: 60px;text-align:center">
						<button id="pushright" type="button"
							class="btn btn-default fa fa-angle-double-right"></button>
						<div style="height: 20px;"></div>
						<button id="pushleft" type="button"
							class="btn btn-default fa fa-angle-double-left"></button>
					</div>
					<div id="rightDiv" class="col-xs-5">
						<div id="rightTitle">
							<label for="keyword" class="control-label">已选机位</label>
						</div>
						<div  class="sortable" style="height: 155px; overflow: auto">
						<ul id="sztree" class="ztree" style="height: 100px;"></ul>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>