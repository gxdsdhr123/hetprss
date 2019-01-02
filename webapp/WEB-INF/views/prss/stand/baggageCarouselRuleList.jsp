<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>行李转盘分配规则</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<body>
	<div id="container">
		<form class="layui-form">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">规则名称</label>
					<div class="layui-input-inline">
						<input id="ruleName" class="layui-input" type="text">
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
                    <button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
                        <i class="fa fa-search">&nbsp;</i>查询
                    </button>
                    <button id="createRule" class="layui-btn layui-btn-small layui-btn-primary" type="button">
                         <i class="fa fa-plus">&nbsp;</i>新增
                    </button>
					<button id="modifyRule" class="layui-btn layui-btn-small layui-btn-primary" type="button">
						 <i class="fa fa-pencil-square-o">&nbsp;</i>修改
					</button>
					<button id="deleteRule" class="layui-btn layui-btn-small layui-btn-primary" type="button">
					 <i class="fa fa-trash">&nbsp;</i>删除
					</button>
				</div>
			</div>
		</form>
		<div id="baseTables">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/stand/baggageCarouselRuleList.js"></script>
</body>
</html>