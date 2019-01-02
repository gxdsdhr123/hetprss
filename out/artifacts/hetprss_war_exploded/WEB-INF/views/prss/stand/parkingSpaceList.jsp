<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>停靠机型规则</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">

.list-group-item{
	-moz-user-select: -moz-none;
   	-khtml-user-select: none;
   	-webkit-user-select: none;
   	-ms-user-select: none;
   	user-select: none;
}
		
</style>
<body>
	<div id="container" style="padding-top:30px">
		<div id="baseTables" style="width:100%;">
			<div style="width:49%;display:inline-block;float:left;margin-right:2px;">
				<table id="baseTable"></table>
			</div>
			
			<div class="content" style="height:100%;width:49%;display:inline-block;float:left;margin-right:2px;">
				<div class="layui-form" id="selectDataForm">
				<form:form id="createForm" class="layui-form"
					modelAttribute="cgEntity" enctype="multipart/form-data" method="post">
					<div class="row">
						<div role="tabpanel" class="tab-pane" id="field-choose"
							aria-labelledby="field-choose-tab">
							<div id="leftDiv" class="col-xs-5">
								<div id="leftTitle">
									<label for="keyword" class="control-label">未选机型</label> <input
										type="text" class="form-control" id="keyword"
										placeholder="请输入关键字" oninput="search(event)">
								</div>
								<div class="sortable" style="height: 330px; overflow: auto">
									<ul class="list-group" id="allul">
									</ul>
								</div>
							</div>
							<div id="middleDiv" class="col-xs-2" style="margin-top: 60px;text-align:center">
								<div class="col-xs-1" style="text-align:center;">
									<button id="pushrightAll" type="button" style="width:60px;"
										class="btn btn-default fa fa-angle-double-right"></button>
									<div style="height: 20px;"></div>
									<button id="pushright" type="button" style="width:60px;"
										class="btn btn-default fa fa-angle-right"></button>
									<div style="height: 20px;"></div>
									<button id="pushleft" type="button" style="width:60px"
										class="btn btn-default fa fa-angle-left"></button>
									<div style="height: 20px;"></div>
									<button id="pushleftAll" type="button" style="width:60px"
										class="btn btn-default fa fa-angle-double-left"></button>
									<div style="height: 20px;"></div>
									<button id="save" type="button" style="width:60px"
										class="btn btn-default">保存</button>
								</div>
							</div>
							<div id="rightDiv" class="col-xs-5">
								<div id="rightTitle">
									<label for="keyword" class="control-label">已选机型</label>
									<input type="text" class="form-control" id="choosed-keyword"
										placeholder="请输入关键字" oninput="search(event)">
								</div>
								<div class="sortable" style="height: 330px; overflow: auto">
									<ul class="list-group choosedField" id="selectul">
									</ul>
								</div>
							</div>
						</div>
					</div>
					<input id="actCode" type="text" style="display:none"></input>
				</form:form>
				
				</div>
			</div>
		</div>
		<div style="clear:both;"></div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/stand/parkingSpaceList.js"></script>
</body>
</html>