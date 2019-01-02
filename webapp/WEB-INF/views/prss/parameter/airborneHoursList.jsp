<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>空中飞行时间</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.layui-layer-page .layui-layer-content{
		overflow: hidden !important;
	}
		
</style>
<body>
	<div id="container">
		<form  id="listForm">
			<div class="layui-form-item">
				<div class="layui-inline">
						<label class="layui-form-label">起飞站</label>
						<div class="layui-input-inline">
							<select name="airport" id="airport" class="select2 form-control telegraphType" data-type="telegraphType" multiple="multiple" >
									<option value="" ></option>
									<c:forEach items="${AIRPORT}" var="airport">
										<option value="${airport.id}">${airport.text}</option>
									</c:forEach>
							</select>
						</div>
							
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">机型</label>
					<div class="layui-input-inline"> 
                       <select name="actType" id="actType" class="select2 form-control telegraphType" data-type="telegraphType" multiple="multiple" >
								<option value="" ></option>
								<c:forEach items="${ACT_TYPE}" var="type">
									<option value="${type.id}">${type.text}</option>
								</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="addBtn" class="layui-btn layui-btn-small layui-btn-primary " type="button">
					 	新增
					</button>
<!-- 					<button id="editBtn" class="layui-btn layui-btn-small layui-btn-primary" type="button"> -->
<!-- 					 	修改 -->
<!-- 					</button> -->
					<button id="deleteBtn" class="layui-btn layui-btn-small layui-btn-primary" type="button">
					 	删除
					</button>
					<button id="computeBtn" class="layui-btn layui-btn-small layui-btn-primary " type="button">
					 	自动计算
					</button>
				</div>
			</div>
		</form>
		<div id="baseTables">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/parameter/airborneHoursList.js"></script>
</body>
</html>