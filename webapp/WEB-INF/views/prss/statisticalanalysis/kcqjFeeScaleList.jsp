<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>客舱清洁收费标准</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<body>
	<div id="container" style="padding-top:30px">
		<form  id="listForm" >
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">航空公司</label>
					<div class="layui-input-inline" >
						<select id="alnCode" name="alnCode" class="form-control" >
					         <option value="">请选择</option>
					         <c:forEach items="${airlines}" var="item">
					          <option  value="${item.id}" >${item.text}</option>
					         </c:forEach>
						 </select>
					</div>
					<label class="layui-form-label">机型</label>
					<div class="layui-input-inline" >
						<select id="actType" name="actType" class="mutilselect2 form-control" >
					         <option value="">请选择</option>
					         <c:forEach items="${actType}" var="item">
					          <option  value="${item.id}" >${item.text}</option>
					         </c:forEach>
						 </select>
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="newBtn" type="button" class="layui-btn layui-btn-small layui-btn-primary">新增</button>
					<button id="editBtn" type="button" class="layui-btn layui-btn-small layui-btn-primary">修改</button>
					<button id="delBtn" type="button" class="layui-btn layui-btn-small layui-btn-primary">删除</button>
				</div>
			</div>
		</form>
		

		<div id="baseTables" style="width:100%;">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/kcqjFeeScaleList.js"></script>
</body>
</html>