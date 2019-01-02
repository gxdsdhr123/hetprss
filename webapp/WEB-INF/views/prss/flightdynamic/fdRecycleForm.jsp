<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<link href="${ctxStatic}/prss/plan/css/longTermPlanForm.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<input id="type" type="text" style="display:none" value="${type}">
	<input id="mainId" type="text" style="display:none" value="${mainId}">
	<button id="refreshDetailTable" type="button" style="display:none"></button>
	<button id="valid" type="button" style="display:none"></button>
	<form:form id="createForm" class="layui-form" modelAttribute="plan" action="${ctx}/plan/longTerm/save" method="post" >
		<form:input path="id" type="text" style="display:none" value="${mainId}"/>
		<div class="layui-form-item">
		    <div class="layui-inline">
		    	<label class="layui-form-label">进港航班</label>
		    	<div class="layui-input-inline">
		    		<form:input path="inFltno" htmlEscape="false" placeholder="请选择" class="layui-input" type="text"/>
		      	</div>
		    </div>
		    <div class="layui-inline">
		      	<label class="layui-form-label">出港航班</label>
		      	<div class="layui-input-inline">
		        	<form:input path="outFltno" htmlEscape="false" placeholder="请选择" class="layui-input" type="text"/>
		      	</div>
		    </div>
		    <div class="layui-inline">
				<label class="layui-form-label">外航/国内标识</label>
				<div class="layui-input-inline">
					<form:select path="alnFlag">
						<form:option value="">请选择</form:option>
				    	<c:forEach items="${alnFlags}" var="alnFlag">
							<form:option value="${alnFlag.value}">${alnFlag.text}</form:option>
						</c:forEach>
				  	</form:select>
				</div>
		    </div>
		    <div class="layui-inline">
				<label class="layui-form-label">航站楼</label>
				<div class="layui-input-inline">
					<form:select path="terminal">
						<form:option value="">请选择</form:option>
				    	<c:forEach items="${terminals}" var="terminal">
							<form:option value="${terminal.value}">${terminal.text}</form:option>
						</c:forEach>
				  	</form:select>
				</div>
		    </div>
		</div>
		<div class="layui-form-item">
			
		    <div class="layui-inline">
				<label class="layui-form-label">进港性质</label>
				<div class="layui-input-inline">
					<form:select path="inProperty" lay-search="">
						<form:option value="">请选择</form:option>
				    	<c:forEach items="${fltProperties}" var="fltProperty">
							<form:option value="${fltProperty.id}">${fltProperty.text}</form:option>
						</c:forEach>
				  	</form:select>
				</div>
		    </div>
		    
		    <div class="layui-inline">
				<label class="layui-form-label">出港性质</label>
				<div class="layui-input-inline">
					<form:select path="outProperty" lay-search="">
						<form:option value="">请选择</form:option>
				    	<c:forEach items="${fltProperties}" var="fltProperty">
							<form:option value="${fltProperty.id}">${fltProperty.text}</form:option>
						</c:forEach>
				  	</form:select>
				</div>
		    </div>
		    
		</div>
		
		<textarea name="planDetail" id="planDetail" style="display:none"></textarea>
	</form:form>
	<div id="createDetailTables">
		<table id="createDetailTable"></table>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdRecycleForm.js"></script>
</body>
</html>