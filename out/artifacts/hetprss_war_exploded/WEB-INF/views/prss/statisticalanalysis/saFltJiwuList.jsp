<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>机务统计表</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<%@include file="/WEB-INF/views/prss/statisticalanalysis/saFltJiwuFilter.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/saFltJiwuList.js"></script>
	</head>
	<body>
		<div id="container">
				<div class="layui-form-item" id="toolbar">
					<div style="width: 100%">
						<div class="layui-btn-group">
							<button id="filter" class="layui-btn  layui-btn-primary" type="button">
							 	筛选
							</button>
							<button id="down" class="layui-btn  layui-btn-primary" type="button">
								导出
							</button>
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span>搜索</span>
						<div style="width: 100px" class="layui-inline">
							<input style="width: 100px" type='text' name='searchValue' id="searchValue" class='layui-input' onfocus="setNullValue(this)"/>
						</div>
					</div>
				</div>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
		<!-- 导出excel -->
		<form id="exportForm" method="post">
			<input type="hidden" id="param" name="param">
		</form>
	</body>
</html>