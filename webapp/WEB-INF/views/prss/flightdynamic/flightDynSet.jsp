<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>航班动态-设置</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynSet.css" rel="stylesheet" />
</head>
<body>
	<input type="hidden" id="schema" name="schema" value="${schema}">
	<!-- 选择字段 -->
	<div class="row" id="controlDiv">
		<div class="col-xs-9 col-sm-9 col-md-9">
			<div class="box box-solid">
				<div class="box-header">
	              <h3 class="box-title" style="color:#c2c2c2">选择列</h3>
	              <div class="box-tools">
	              	<input class="form-control input-sm" placeholder="输入关键字回车查询" type="text" style="border:0px solid;width:200px" id="searchInput">
	              </div>
	            </div>
	            <div class="box-body" id="allColumns" style="background: #08254C">
					<form class="layui-form" action="">
						<c:forEach items="${columns}" var="columnGroup">
						<div class="box box-solid">
							<div class="box-header with-border" style="padding:5px;">
				              <h3 class="box-title" style="font-size:14px!important">${columnGroup.name}</h3>
				            </div>
				            <div class="box-body">
					            <c:forEach items="${columnGroup.columnList}" var="column">
									<div style="display:inline-block;margin:5px 5px;">
										<span style="display:inline-block;min-width:80px;">${column.title}</span>&nbsp;
						    			<input name="column" 
						    				   value="${column.id}" 
						    				   ${selectedColIds.contains(column.id)?'checked':''} 
						    				   data-cnname="${column.title}" 
						    				   data-width="${column.width}" 
						    				   type="checkbox" 
						    				   lay-skin="switch" 
						    				   lay-filter="switchColumn">
									</div>
								</c:forEach>
				            </div>
			            </div>
						</c:forEach>
					</form>
				</div>
			</div>
		</div>
		<div class="col-xs-3 col-sm-3 col-md-3">
			<div class="box box-solid">
				<div class="box-header">
	              <h3 class="box-title" style="color:#c2c2c2">已选列</h3>
	              <div class="box-tools">
	              	<button type="button" class="btn btn-box-tool" id="settingBtn">设置列</button>
	              </div>
	            </div>
	            <div class="box-body" style="background: #08254C">
					<div class="sortable" id="selectedItem">
						<ul class="list-group choosedField">
							<c:if test="${empty selected}">
								<li data-code="hr" class="list-group-item hr">--------------------分隔线--------------------</li>
							</c:if>
						  	<c:forEach items="${selected}" var="choosed">
								<li class="list-group-item" id="${choosed.id}" data-cnname="${choosed.cnname}" data-custom="${choosed.customCnname}" data-width="${choosed.width}">${choosed.cnname}</li>
								<c:if test="${choosed.className=='separator'}">
									<li data-code="hr" class="list-group-item hr ui-draggable ui-draggable-handle ui-sortable-handle" style="position: relative; text-align:center; height: 42px;">--------------------分隔线--------------------</li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 设置字段 -->
	<div id="settingDiv" style="display:none;overflow: hidden;">
		<div class="box-header">
			<h3 class="box-title" style="color: #c2c2c2">设置列</h3>
			<div class="box-tools">
				<button type="button" class="btn btn-box-tool" id="controlBtn">选择列</button>
			</div>
		</div>
		<table id="fieldTable"></table>
	</div>
	<script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/flightDynSet.js"></script>
</body>
</html>