<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员分工维护</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript">
var haveTemp = "${haveTemp}";
</script>
<body>
	<div style="padding-top: 5px;" >
		<div class="layui-form" style="float:left;padding:0px !important">
			<div class="layui-form-item">
			    <div class="layui-input-inline" style="width:150px;">
			      <select name="templateConf" id="templateConf" lay-filter="templateSel">
			      	<option value="new" selected>空模板</option>
		       		<c:forEach items="${templateList}" var="templateVO">
						<option value="${templateVO.ID}">${templateVO.NAME}</option>
					</c:forEach>
		     	  </select>
		     	</div>
		     	<div class="layui-input-inline" style="width:150px;margin-left:10px;">
			      <select name="groups" id="groups" lay-filter="groups">
			      	<option value="%" selected>全部班组</option>
		       		<c:forEach items="${groups}" var="group">
						<option value="${group.groupName}">${group.groupName}</option>
					</c:forEach>
		     	  </select>
		     	</div>
			</div>
		</div>
		<div style="float: left;padding-left:10px">
			 <input name="title" id="searchName" value="" style="width:150px" placeholder="请输入" class="layui-input" type="text">
		</div>
		<div style="float: left;padding-left:15px" id="toolbar">
			<button id="searchBtn" type="button" class="btn btn-link">查询</button>
			<button id="saveBtn" type="button" class="btn btn-link">保存</button>
			<button id="deleteBtn" type="button" class="btn btn-link">删除</button>
			<button id="nameBtn" type="button" class="btn btn-link">修改模板名称</button>
		</div>
	</div>
		<table id="createDetailTable" style="table-layout: fixed;"></table>
	<div id="contentDiv" style="display:none">
		<table id="aptiduteTable"></table>
	</div>
<script type="text/javascript" src="${ctxStatic}/prss/scheduling/workerDivisionMaintain.js"></script>
</body>
</html>