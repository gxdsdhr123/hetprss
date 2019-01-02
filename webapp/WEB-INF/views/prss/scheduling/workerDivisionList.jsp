<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员分工</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">
	.select2-container .select2-dropdown .select2-results li{
		min-height: 32px;
	}
	.layui-form-checkbox .layui-icon {
		color:#fff !important;
	}
	.layui-disabled:not(.layui-form-checked) {
		opacity:0.3;
	}
</style>
</head>
<body>
	<div style="padding-top: 5px;">
		<input type="hidden" id="operator" value="${operator }"/>
		<div class="layui-form" style="float:left;padding:0px !important">
			<div class="layui-form-item">
			    <div class="layui-input-inline" style="width:150px;">
			      <select name="templateConf" id="templateConf" lay-filter="templateSel">
			      	<option value="all" selected>当前分工</option>
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
			<button id="modifyBtn" type="button" class="btn btn-link">修改</button>
			<button id="saveBtn" type="button" class="btn btn-link">生效</button>
			<c:if test="${operator == null }">
				<button id="templateBtn" type="button" class="btn btn-link">分工版本维护</button>
			</c:if>
		</div>
	</div>
	<div id="createDetailTableDiv" style="height:400px;width:100%;position: relative;">
		<form class="layui-form" action="" style="padding:0px !important">
			<table id="createDetailTable" style="table-layout: fixed;"></table>
		</form>
	</div>
	
	<div id="contentDiv" style="display:none">
		<table id="aptiduteTable"></table>
	</div>
<script type="text/javascript" src="${ctxStatic}/prss/scheduling/workerDivisionList.js"></script>
</body>
</html>