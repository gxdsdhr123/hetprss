<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>电子围栏配置信息列表</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/prss/rule/gisConfigList.js"></script>
	</head>
	<body>
		<div id="container">
			<form>
				<div class="layui-form-item" id="toolbar">
					<div class="layui-inline" >
						<div class="layui-btn-group">
							<button id="add" class="layui-btn  layui-btn-primary" type="button">
								新增
							</button>
							<button id="delete" class="layui-btn  layui-btn-primary" type="button">
								删除
							</button>
							<button id="useAll" class="layui-btn  layui-btn-primary" type="button">
								一键生效
							</button>
							<button id="notUseAll" class="layui-btn  layui-btn-primary" type="button">
								一键失效
							</button>
						</div>
					</div>
				</div>
			</form>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
	</body>
</html>